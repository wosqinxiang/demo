/**
 * Created on 2016年11月24日 by lijiefeng
 */
package com.ahdms.ctidservice.service.impl;

import com.ahdms.ap.model.AccessToken;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.db.core.CTIDProperties;
import com.ahdms.ctidservice.db.dao.ConfigInfoMapper;
import com.ahdms.ctidservice.db.model.ConfigInfo;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.util.JsonUtils;
import com.ahdms.exception.*;
import com.ahdms.sv.SVTool;
import net.sf.json.JSONObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.EnvelopedData;
import org.bouncycastle.asn1.x509.Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TokenCipherServiceImpl implements TokenCipherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCipherServiceImpl.class);

    @Autowired
    private CTIDProperties pro;

    @Autowired
    private ConfigInfoMapper configMapper;

    @Value("${cipher.config.key:cipherConfigKey}")
    private String cipherConfigKey;

    private SVTool tool = null;

    private Certificate cert;

    private byte[] cipherKey;

    @PostConstruct
    public void init() throws Exception {
        tool = SVTool.getInstance();
        tool.SVS_InitServerConnect(pro.getSvIp(), pro.getSvPort());
        try {
            cert = tool.SVS_GetCertInfo(pro.getSvSubject());
        } catch (Exception e) {
        }
    }

    @Override
    public boolean verify(byte[] source, byte[] signData) throws Exception {
        //		SVTool tool = SVTool.getInstance();
        boolean b = false;
        //		if(tool.SVS_InitServerConnect(pro.getSvIp())){
        try {

            b = tool.SVS_PKCS7VerifyData(true, false, source, signData);
        } catch (Exception e) {
            throw new Exception("证书验签失败");
        }
        //		}
        return b;
    }

    @Override
    public byte[] sign(byte[] source) throws Exception {
        return tool.SVS_PKCS7SignData(Integer.parseInt(pro.getKeyIndex()), pro.getKeyValue(), source, true, false);
    }

    @Override
    public boolean dataVerify(byte[] source, byte[] signData) throws Exception {
        return tool.SVS_VerifyData(getCtidCert(), source, signData, 0);
    }

    @Override
    public byte[] dataSign(byte[] source) throws Exception {
        //数字签名验证签名
        return tool.SVS_SignData(Integer.parseInt(pro.getKeyIndex()), pro.getKeyValue(), source);
    }

    @Override
    public Certificate getCtidCert() throws Exception {
        if (cert == null) {
            cert = tool.SVS_GetCertInfo(pro.getSvSubject());
        }
        return cert;
    }

    @Override
    public boolean verifyCert(Certificate cert) throws Exception {
        return tool.SVS_VerifyCert(cert);
    }

    @Override
    public boolean verify(byte[] source, byte[] signData, Certificate signCert) throws SVS_InvalidParameterException, SVS_SignatureEncodeException, SVS_VerifyDataException, SVS_SignatureException, SVS_CertNotTrustException, SVS_CertExpiredException, SVS_CertCancelException, SVS_CheckIRLException, SVS_CertIneffectiveException, SVS_CertException, SVS_CertTypeException, SVS_NotFoundPKMException, SVS_ServerConnectException, SVS_SignedDataEncodeException, SVS_VerifyPKCS7SignDataException, SVS_NotFoundOriginalTextException, SVS_GetIRLException {
        boolean b = false;
        b = tool.SVS_VerifyData(signCert, source, signData, 0);
        return b;
    }

    @Override
    public byte[] encode(String source) throws Exception {
        Certificate cert = getCtidCert();

        return tool.SVS_EncryptEnvelope(cert, source.getBytes());
    }

    @Override
    public byte[] decode(byte[] envelopeData) throws Exception {
        ASN1Sequence seq = ASN1Sequence.getInstance(envelopeData);

        EnvelopedData envelope = null;
        try {
            envelope = EnvelopedData.getInstance(seq);
        } catch (Exception e) {
            ContentInfo contentInfo = ContentInfo.getInstance(seq);
            envelope = EnvelopedData.getInstance(contentInfo.getContent());
        }

        return tool.SVS_DecryptEnvelope(envelope.getEncoded(),
                Integer.parseInt(pro.getKeyIndex()),
                pro.getKeyValue());
    }

    @Override
    public String encodeIdCardInfo(String cardName, String cardNum) {
        try {
            IdCardInfoBean idCardBean = new IdCardInfoBean(cardName, cardNum);
            byte[] encode = encode(JsonUtils.bean2Json(idCardBean));
            return Base64Utils.encode(encode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IdCardInfoBean decodeIdCardInfo(String envelopeData) {
        try {
            byte[] decode = decode(Base64Utils.decode(envelopeData));
            IdCardInfoBean bean = (IdCardInfoBean) JSONObject.toBean(JSONObject.fromObject(new String(decode)), IdCardInfoBean.class);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String encodeAccess(String openId, String crateTime, String ip, String serialNum) {
        try {
            AccessToken token = new AccessToken(openId, crateTime, ip, serialNum);
            byte[] encode = encode(JSONObject.fromObject(token).toString());
            return Base64Utils.encode(encode);
        } catch (Exception e) {
            LOGGER.error("token加密失败");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AccessToken decodeAccess(String envelopeData) {
        try {
            byte[] decode = decode(Base64Utils.decode(envelopeData));
            AccessToken bean = (AccessToken) JSONObject.toBean(JSONObject.fromObject(new String(decode)), AccessToken.class);
            return bean;
        } catch (Exception e) {
            LOGGER.error("token解密失败");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] SVS_GetCTIDEncryptKey(Certificate cert) {
        try {
            return tool.SVS_GetCTIDEncryptKey(cert);
        } catch (SVS_InvalidParameterException e) {
            e.printStackTrace();
        } catch (SVS_GetEncryptKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] SVS_EncryptCTID(byte[] cipherKey, int keyIndex, byte[] inData) {
        try {
//            tool.SVS_DecryptCTID()
            return tool.SVS_EncryptCTID(cipherKey, keyIndex,pro.getKeyValue(), inData);
        } catch (SVS_InvalidParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SVS_EncryptBytesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String encryptCTID(byte[] inData) {
        try {
            if (cipherKey == null) {
                ConfigInfo selectByKey = configMapper.selectByKey(cipherConfigKey);
                if (selectByKey == null) {
                    return null;
                } else {
                    cipherKey = Base64Utils.decode(selectByKey.getConfigValue());
                }
            }
            byte[] svs_EncryptCTID = SVS_EncryptCTID(cipherKey, Integer.parseInt(pro.getKeyIndex()), inData);
            return Base64Utils.encode(svs_EncryptCTID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String decryptCTID(String envelopeData) {
        try {
            if (cipherKey == null) {
                ConfigInfo selectByKey = configMapper.selectByKey(cipherConfigKey);
                if (selectByKey == null) {
                    return null;
                } else {
                    cipherKey = Base64Utils.decode(selectByKey.getConfigValue());
                }
            }
//            return new String(tool.SVS_DecryptCTID(Integer.parseInt(pro.getKeyIndex()), pro.getKeyValue(),cipherKey, Base64Utils.decode(envelopeData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
