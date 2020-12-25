package com.ahdms.service.impl;

import com.ahdms.bean.req.*;
import com.ahdms.bean.rsp.TwaSvrSignRspVo;
import com.ahdms.config.svs.SvsProperties;
import com.ahdms.config.svs.SvsToolUtils;
import com.ahdms.context.SvsContextUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.service.ISvsClientService;
import com.ahdms.util.Base64Utils;
import com.ahdms.util.Util;
import org.bouncycastle.asn1.x500.style.X500NameTokenizer;
import org.bouncycastle.asn1.x509.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2020-12-16 18:07
 */
@Service
public class SvsClientServiceImpl implements ISvsClientService {

    @Autowired
    private SvsToolUtils svsToolUtils;

    @Autowired
    private SvsProperties svsProperties;

    @Override
    public String signData(SignDataReqVo signDataReqVo) throws Exception {
        SvsProperties.SvsServer svsServer = svsProperties.getAppCodes().get(SvsContextUtils.getAccount());

        byte[] signData = svsToolUtils.getSVTool(SvsContextUtils.getAccount()).SVS_SignData(svsServer.getKeyIndex(), svsServer.getKeyValue(), signDataReqVo.getInData().getBytes());

        return Base64Utils.encodeToString(signData);
    }

    @Override
    public boolean verifyData(VerifyDataReqVo verifyDataReqVo) throws Exception {
        //获取可信标识
        Certificate cert = Certificate.getInstance(Base64Utils.decodeString(verifyDataReqVo.getBase64EncodeCert()));

        //获取签名值byte[]
        byte[] signatureBytes = Base64Utils.decodeString(verifyDataReqVo.getBase64SignValue());

        boolean result = svsToolUtils.getDefaultSVTool().SVS_VerifyData(cert, verifyDataReqVo.getInData().getBytes(), signatureBytes, verifyDataReqVo.getVerifyLevel());
        return result;

    }

    @Override
    public String pkcs7SignData(Pkcs7SignDataReqVo signDataReqVo) throws Exception {
        SvsProperties.SvsServer svsServer = svsProperties.getAppCodes().get(SvsContextUtils.getAccount());
        byte[] bytes = svsToolUtils.getSVTool(signDataReqVo.getAppCode()).SVS_PKCS7SignData(svsServer.getKeyIndex(), svsServer.getKeyValue(), signDataReqVo.getInData().getBytes(), signDataReqVo.getOriginalText() == 0, signDataReqVo.getIrls() == 0);
        return Base64Utils.encodeToString(bytes);

    }


    @Override
    public boolean pkcs7VerifyData(Pkcs7VerifyDataReqVo verifyDataReqVo) throws Exception {
        return svsToolUtils.getDefaultSVTool().SVS_PKCS7VerifyData(verifyDataReqVo.getOriginalText() == 0, verifyDataReqVo.getIrls() == 0, verifyDataReqVo.getInData().getBytes(), Base64Utils.decodeString(verifyDataReqVo.getBase64Pkcs7SignData()));

    }



    @Override
    public String encryptEnvelope(EncryptEnvelopeReqVo encryptEnvelopeReqVo) throws Exception {

        //获取可信标识
        Certificate cert = Certificate.getInstance(Base64Utils.decodeString(encryptEnvelopeReqVo.getBase64EncodeCert()));
        byte[] bytes = svsToolUtils.getDefaultSVTool().SVS_EncryptEnvelope(cert, encryptEnvelopeReqVo.getInData().getBytes());
        return Base64Utils.encodeToString(bytes);

    }

    @Override
    public String decryptEnvelope(DecryptEnvelopeReqVo decryptEnvelopeReqVo) throws Exception {
        SvsProperties.SvsServer svsServer = svsProperties.getAppCodes().get(SvsContextUtils.getAccount());
        return new String(svsToolUtils.getSVTool(decryptEnvelopeReqVo.getAppCode()).SVS_DecryptEnvelope(
                Base64Utils.decodeString(decryptEnvelopeReqVo.getBase64EnvelopeData())
                , svsServer.getKeyIndex(), svsServer.getKeyValue()));

    }

    @Override
    public String genEncryptKey() throws Exception {
        String base64EncodeCert = certInfo(SvsContextUtils.getAccount());
        Certificate cert = Certificate.getInstance(Base64Utils.decodeString(base64EncodeCert));
        byte[] bytes = svsToolUtils.getSVTool(SvsContextUtils.getAccount()).SVS_GetCTIDEncryptKey(cert);
        return Base64Utils.encodeToString(bytes);
    }

    @Override
    public String encryptData(EncryptDataReqVo reqVo) {
//        svsToolUtils.getDefaultSVTool().SVS_EncryptCTID(null,)
        return null;
    }

    @Override
    public String decryptData(EncryptDataReqVo reqVo) {
//        svsToolUtils.getDefaultSVTool().SVS_DecryptCTID();
        return null;
    }

    @Override
    public String svrGenRnd() throws Exception {
        byte[] randomBytes = svsToolUtils.getSVTool(SvsContextUtils.getAccount()).SVS_GenerateRandom(16);

        return Util.byteToHex(randomBytes);
    }

    @Override
    public boolean owaSvrVerify(OwaSvrVerifyReqVo verifyReqVo) throws Exception {
        //获取可信标识
        Certificate cert = Certificate.getInstance(Base64Utils.decodeString(verifyReqVo.getBase64EncodeCert()));

        //获取签名值byte[]
        byte[] signatureBytes = Base64Utils.decodeString(verifyReqVo.getBase64SignValue());

        //组装签名原文byte[]
        byte[] inData = Util.mergeBytes(Util.hexToByte(verifyReqVo.getServerRandom()), Util.hexToByte(verifyReqVo.getClientRandom()));

        //验证签名值
        return svsToolUtils.getDefaultSVTool().SVS_VerifyData(cert, inData, signatureBytes, verifyReqVo.getVerifyLevel());

    }

    @Override
    public TwaSvrSignRspVo twaSvrSign(TwaSvrSignReqVo svrSignReqVo) throws Exception {
        TwaSvrSignRspVo result = null;

        //生成随机数R1
        String R1 = svrGenRnd();

        //组装签名原文
        byte[] inData = Util.mergeBytes(Util.hexToByte(R1), Util.hexToByte(svrSignReqVo.getClientRandom()));

        //数字签名
        byte[] signatureBytes = svsToolUtils.getDefaultSVTool().SVS_SignData(svrSignReqVo.getKeyIndex(), svrSignReqVo.getKeyValue(), inData);

        if (null != signatureBytes) {
            result = new TwaSvrSignRspVo();
            result.setServerRandom(R1);
            result.setBase64signValue(Base64Utils.encodeToString(signatureBytes));
        }

        return result;
    }

    @Override
    public boolean twaSvrVerify(TwaSvrVerifyReqVo svrVerifyReqVo) throws Exception {
        //获取可信标识
        Certificate cert = Certificate.getInstance(Base64Utils.decodeString(svrVerifyReqVo.getBase64EncodeCert()));

        //获取签名值byte[]
        byte[] signatureBytes = Base64Utils.decodeString(svrVerifyReqVo.getBase64SignValue());

        //组装签名原文byte[]
        byte[] inData = Util.mergeBytes(Util.hexToByte(svrVerifyReqVo.getServerRandom()), Util.hexToByte(svrVerifyReqVo.getClientRandom()));
        String identity = getPartFromDN(cert.getSubject().toString(), "CN");
        inData = Util.mergeBytes(inData, identity.getBytes());

        //验证签名值
        return svsToolUtils.getDefaultSVTool().SVS_VerifyData(cert, inData, signatureBytes, svrVerifyReqVo.getVerifyLevel());
    }

    @Override
    public String certInfo(String appCode) throws Exception {
        SvsProperties.SvsServer svsServer = svsProperties.getAppCodes().get(appCode);
        Certificate certificate = svsToolUtils.getSVTool(appCode).SVS_GetCertInfo(svsServer.getSerialNumber());
        return Base64Utils.encodeToString(certificate.getEncoded());

    }

    @Override
    public Boolean verifyCert(String certBase64Str) throws Exception {
        Certificate cert = Certificate.getInstance(Base64Utils.decodeString(certBase64Str));

        boolean b = svsToolUtils.getDefaultSVTool().SVS_VerifyCert(cert);
        return b;

    }

    @Override
    public String genRandomSignData(GenRandomSignDataReqVo reqVo) throws Exception {
        byte[] inData = Util.mergeBytes(Util.hexToByte(reqVo.getServerRandom()), Util.hexToByte(reqVo.getClientRandom()));
        if(StringUtils.isNotBlank(reqVo.getIdentity())){
            inData = Util.mergeBytes(inData, reqVo.getIdentity().getBytes());
        }
        byte[] signData = svsToolUtils.getDefaultSVTool().SVS_SignData(reqVo.getKeyIndex(), reqVo.getKeyValue(), inData);

        return Base64Utils.encodeToString(signData);
    }

    private String getPartFromDN(String dn, String dnpart) {
        String part = null;
        if ((dn != null) && (dnpart != null)) {
            String o;
            dnpart += "=";
            X500NameTokenizer xt = new X500NameTokenizer(dn);
            while (xt.hasMoreTokens()) {
                o = xt.nextToken();
                if ((o.length() > dnpart.length()) && o.substring(0, dnpart.length()).equalsIgnoreCase(dnpart)) {
                    part = o.substring(dnpart.length());

                    break;
                }
            }
        }
        return part;
    }
}
