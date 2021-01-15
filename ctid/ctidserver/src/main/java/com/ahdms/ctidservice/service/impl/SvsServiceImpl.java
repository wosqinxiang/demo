package com.ahdms.ctidservice.service.impl;

import com.ahdms.ctidservice.bean.AccessToken;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
import com.ahdms.ctidservice.bean.model.ConfigInfo;
import com.ahdms.ctidservice.config.svs.SvsProperties;
import com.ahdms.ctidservice.contants.CtidConstants;
import com.ahdms.ctidservice.service.IConfigInfoService;
import com.ahdms.ctidservice.service.ISvsService;
import com.ahdms.ctidservice.util.Base64Utils;
import com.ahdms.ctidservice.util.JsonUtils;
import com.ahdms.exception.SVS_DecryptBytesException;
import com.ahdms.exception.SVS_InvalidParameterException;
import com.ahdms.sv.SVTool;
import org.bouncycastle.asn1.x509.Certificate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-07 16:23
 */
@Service
public class SvsServiceImpl implements ISvsService, InitializingBean {

    @Autowired
    private SvsProperties svsProp;

    @Autowired
    private IConfigInfoService configInfoService;

    private SVTool tool = SVTool.getInstance();

    private Certificate cert;

    private byte[] cipherKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        tool.SVS_InitServerConnect(svsProp.getSvIp(),svsProp.getSvPort());
        try {
            cert = tool.SVS_GetCertInfo(svsProp.getSvSubject());
        } catch (Exception e) {

        }
    }

    @Override
    public boolean PKCS7VerifyData(byte[] source, byte[] signData) throws Exception {
        return tool.SVS_PKCS7VerifyData(true, false, source, signData);
    }

    @Override
    public byte[] PKCS7SignData(byte[] source) throws Exception {
        return tool.SVS_PKCS7SignData(svsProp.getKeyIndex(), svsProp.getKeyValue(), source, true, false);
    }

    @Override
    public boolean verifyCert(Certificate cert) throws Exception {
        return tool.SVS_VerifyCert(cert);
    }

    /**
     * 获取当前服务器对应SVS证书
     * @return
     * @throws Exception
     */
    @Override
    public Certificate getCtidCert() throws Exception {
        if (cert == null) {
            cert = tool.SVS_GetCertInfo(svsProp.getSvSubject());
        }
        return cert;
    }

    /**
     * 数字签名验签
     * @param source
     * @param signData
     * @param signCert
     * @return
     * @throws Exception
     */
    @Override
    public boolean verify(byte[] source, byte[] signData, Certificate signCert) throws Exception {
        return tool.SVS_VerifyData(signCert, source, signData, 0);
    }

    /**
     * 数字签名
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    public byte[] dataSign(byte[] source) throws Exception {
        return tool.SVS_SignData(svsProp.getKeyIndex(), svsProp.getKeyValue(), source);
    }

    /**
     * 用当前服务器证书验证数字签名
     * @param source
     * @param signData
     * @return
     * @throws Exception
     */
    @Override
    public boolean dataVerify(byte[] source, byte[] signData) throws Exception {
        return verify(source,signData,getCtidCert());
    }

    /**
     * 数字信封加密
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    public byte[] encode(String source) throws Exception {
        return tool.SVS_EncryptEnvelope(getCtidCert(), source.getBytes());
    }

    @Override
    public byte[] decode(byte[] envelopeData) throws Exception {
        return tool.SVS_DecryptEnvelope(envelopeData,
                svsProp.getKeyIndex(),
                svsProp.getKeyValue());
    }

    @Override
    public String encodeIdCardInfo(String cardName, String cardNum) {
        try {
            IdCardInfoBean idCardBean = new IdCardInfoBean(cardName, cardNum);
            byte[] encode = encode(JsonUtils.toJson(idCardBean));
            return Base64Utils.encodeToString(encode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IdCardInfoBean decodeIdCardInfo(String envelopeData) {
        try {
            byte[] decode = decode(Base64Utils.decodeFromString(envelopeData));
            IdCardInfoBean bean = JsonUtils.readValue(decode,IdCardInfoBean.class);
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
            byte[] encode = encode(JsonUtils.toJson(token));
            return Base64Utils.encodeToString(encode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AccessToken decodeAccess(String envelopeData) {
        return null;
    }

    @Override
    public byte[] getCTIDEncryptKey(Certificate cert) {
        try {
            return tool.SVS_GetCTIDEncryptKey(cert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] encryptCTID(byte[] cipherKey, byte[] inData) {
        try {
            return tool.SVS_EncryptCTID(cipherKey,svsProp.getKeyIndex(),svsProp.getKeyValue(),inData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String encryptCTID(byte[] inData) {
        return Base64Utils.encodeToString(encryptCTID(getCipherKey(),inData));
    }

    @Override
    public String decryptCTID(String envelopeData) {
        try {
            byte[] bytes = tool.SVS_DecryptCTID(svsProp.getKeyIndex(), svsProp.getKeyValue()
                    , getCipherKey(), Base64Utils.decodeFromString(envelopeData));
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getCipherKey(){
        if (cipherKey == null) {
            ConfigInfo selectByKey = configInfoService.selectByKey(CtidConstants.CIPHER_CONFIG_KEY);
            if (selectByKey == null) {
                return null;
            } else {
                cipherKey = Base64Utils.decodeFromString(selectByKey.getConfigValue());
            }
        }
        return cipherKey;
    }
}
