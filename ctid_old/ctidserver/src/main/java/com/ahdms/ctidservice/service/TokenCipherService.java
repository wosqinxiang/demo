/**
 * Created on 2016年9月30日 by lijiefeng
 */
package com.ahdms.ctidservice.service;

import org.bouncycastle.asn1.x509.Certificate;

import com.ahdms.ap.model.AccessToken;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
public interface TokenCipherService {
 	
	public boolean verify(byte[] source, byte[] signData) throws Exception;
	
	public byte[] sign(byte[] source) throws Exception;

	public boolean verifyCert(Certificate cert) throws Exception;
	
	public Certificate getCtidCert() throws Exception;

	public boolean verify(byte[] source, byte[] signature, Certificate signCert) throws Exception;

	byte[] dataSign(byte[] source) throws Exception;
	
	public boolean dataVerify(byte[] source, byte[] signData) throws Exception;
	
	public byte[] encode(String source) throws Exception;
	
	public byte[] decode(byte[] envelopeData) throws Exception;
	
	String encodeIdCardInfo(String cardName,String cardNum);
	
	IdCardInfoBean decodeIdCardInfo(String envelopeData);

	String encodeAccess(String openId, String crateTime, String ip, String string);

	AccessToken decodeAccess(String envelopeData);
	
	public byte[] SVS_GetCTIDEncryptKey(Certificate cert); 
	
	public byte[] SVS_EncryptCTID(byte[] cipherKey, int keyIndex, byte[] inData);
	
	String encryptCTID(byte[] inData);

	String decryptCTID(String envelopeData);
}
