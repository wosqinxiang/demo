package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.bean.AccessToken;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
import org.bouncycastle.asn1.x509.Certificate;

/**
 * @author qinxiang
 * @date 2021-01-07 16:23
 */
public interface ISvsService {

    boolean PKCS7VerifyData(byte[] source, byte[] signData) throws Exception;

    byte[] PKCS7SignData(byte[] source) throws Exception;

    boolean verifyCert(Certificate cert) throws Exception;

    Certificate getCtidCert() throws Exception;

    boolean verify(byte[] source, byte[] signature, Certificate signCert) throws Exception;

    byte[] dataSign(byte[] source) throws Exception;

    boolean dataVerify(byte[] source, byte[] signData) throws Exception;

    byte[] encode(String source) throws Exception;

    byte[] decode(byte[] envelopeData) throws Exception;

    String encodeIdCardInfo(String cardName, String cardNum);

    IdCardInfoBean decodeIdCardInfo(String envelopeData);

    String encodeAccess(String openId, String crateTime, String ip, String string);

    AccessToken decodeAccess(String envelopeData);

    byte[] getCTIDEncryptKey(Certificate cert);

    byte[] encryptCTID(byte[] cipherKey, byte[] inData);

    String encryptCTID(byte[] inData);

    String decryptCTID(String envelopeData);
}
