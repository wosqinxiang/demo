//package com.ahdms.ctidservice.service.core;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateExpiredException;
//import java.security.cert.CertificateFactory;
//import java.security.cert.CertificateNotYetValidException;
//import java.security.cert.X509Certificate;
//import java.util.Enumeration;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.commons.lang.StringUtils;
//import org.bouncycastle.asn1.ASN1Encodable;
//import org.bouncycastle.asn1.ASN1EncodableVector;
//import org.bouncycastle.asn1.ASN1InputStream;
//import org.bouncycastle.asn1.ASN1Integer;
//import org.bouncycastle.asn1.ASN1ObjectIdentifier;
//import org.bouncycastle.asn1.ASN1OctetString;
//import org.bouncycastle.asn1.ASN1Primitive;
//import org.bouncycastle.asn1.ASN1Sequence;
//import org.bouncycastle.asn1.ASN1Set;
//import org.bouncycastle.asn1.DEROctetString;
//import org.bouncycastle.asn1.DERSequence;
//import org.bouncycastle.asn1.DERTaggedObject;
//import org.bouncycastle.asn1.cms.EncryptedContentInfo;
//import org.bouncycastle.asn1.cms.EnvelopedData;
//import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
//import org.bouncycastle.asn1.cms.KeyTransRecipientInfo;
//import org.bouncycastle.asn1.cms.RecipientIdentifier;
//import org.bouncycastle.asn1.cms.RecipientInfo;
//import org.bouncycastle.asn1.pkcs.ContentInfo;
//import org.bouncycastle.asn1.pkcs.SignedData;
//import org.bouncycastle.asn1.pkcs.SignerInfo;
//import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
//import org.bouncycastle.asn1.x509.Certificate;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.ahdms.ctidservice.common.Base64Utils;
//import com.ahdms.ctidservice.common.UUIDGenerator;
//import com.ahdms.identification.ans1.SM2Signature;
//import com.ahdms.identification.gm.SM2Utils;
//import com.ahdms.identification.gm.SM4Utils;
//import com.ahdms.identification.iki.IKIObjectIdentifiers;
//import com.ahdms.identification.utils.CertTools;
//import com.ahdms.util.AsnUtil;
//
//@Component
//public class PKCS7Tool {
//	/** 根证书 */
//	private Certificate rootCertificate = null;
//	
//	@Value("${ctid.envelop.cert}")
//    private String rootCertificateStr;
//	
//	private boolean loadRootCert;
//	
//	public boolean isLoadRootCert() {
//		return loadRootCert;
//	}
//
//	public void setLoadRootCert(boolean loadRootCert) {
//		this.loadRootCert = loadRootCert;
//	}
//
//	public Certificate getRootCertificate() {
//		return rootCertificate;
//	}
//
//	public void setRootCertificate(Certificate rootCertificate) {
//		this.rootCertificate = rootCertificate;
//	}
//	
//	@PostConstruct
//	public void init(){
//		try {
//			byte[] sign = Base64Utils.decode(rootCertificateStr);
//			ASN1Sequence as = byteToASN1Sequence(sign);
//			Certificate _rootCertificate = Certificate.getInstance(as);
//			this.setRootCertificate(_rootCertificate);
//			this.setLoadRootCert(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	Logger logger = LoggerFactory.getLogger(PKCS7Tool.class);
//	/**
//	 * 私有构造方法
//	 */
//	private static ASN1Sequence byteToASN1Sequence(byte[] bytes) {
//		ASN1InputStream input = null;
//		ASN1Primitive object = null;
//		if(null!=bytes){
//			try {
//				input = new ASN1InputStream(bytes);
//				object = input.readObject();
//			} catch (IOException ioe) {
//				ioe.printStackTrace();
//				return null;
//			} finally {
//				try {
//					if (null != input) {
//						input.close();
//					}
//				} catch (IOException e) {
//					return null;
//				}
//			}
//		}
//		return (ASN1Sequence)object;
//	}
//	
//	/**
//	 * 取得验签名工具 加载信任证书
//	 * 
//	 */
//	private void getVerifier() {
//		// 加载信任证书
//		try {
//			//test
////			String _rootCertificateStr="MIIBejCCAR6gAwIBAgIIf3rBKuJQKc8wDAYIKoEcz1UBg3UFADAxMQswCQYDVQQGEwJDTjERMA8GA1UEChMIR0xDVElEMDExDzANBgNVBAMTBkdMQ0EwMTAeFw0xNTEwMTIwNzA3MjJaFw0yMDEwMTAwNzA3MjJaMEAxCzAJBgNVBAYTAkNOMREwDwYDVQQKEwhHTENUSUQwMTENMAsGA1UECxMEU0lHTjEPMA0GA1UEAxMGUlpGVzAxMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEKw83VckU2cYctP5rX7HRpoOz03Cg4kzR6St/gKcyN0GNHZsrQrCMwWdI9NS2KDjs6RGTVBpDK5QN5R1F8BOMG6MPMA0wCwYDVR0PBAQDAgQwMAwGCCqBHM9VAYN1BQADSAAwRQIhAPJ5hwmhhP7mhifGjDatcXt4D2oUM0yilBlnRIa6tcX7AiADp3c4841BP+yaeTNoKBj+AEH6nYZ4ITtHkCKkw9ARBw==";
//			byte[] sign = Base64Utils.decode(rootCertificateStr);
//			ASN1Sequence as = byteToASN1Sequence(sign);
//			Certificate _rootCertificate = Certificate.getInstance(as);
//			this.setRootCertificate(_rootCertificate);
//		} catch (Exception exception) {
//			logger.info(exception.getMessage());
//		}
//	}
//
//	
//
//	/**
//	 * 
//	 * @param signedData
//	 * @return
//	 */
//	private static Certificate getCertBySignedData(SignedData signedData) {
//		Certificate cert = null;// 证书
//		// 获取SignedData中的Certificate数据
//		ASN1Set certs = signedData.getCertificates();
//		if (null != certs) {
//			Enumeration e = certs.getObjects();
//			cert = Certificate.getInstance(e.nextElement());
//		}
//		return cert;
//	}
//
//	
//	/**
//	 * 验证签名(无CRL)
//	 * 
//	 * @param signature 签名签名结果
//	 * @param data      被签名数据
//	 * @throws Exception 
//	 */
//	private boolean verify(String signature, byte[] data) {
//		byte[] sign = null;
//		try {
//			if(!StringUtils.isBlank(signature)) {
//				sign = Base64Utils.decode(signature);
//			}else {
//				logger.info("签名签名结果不能为空");
//				return false;
//			}
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			logger.info(e1.getMessage());
//			return false;
//		}
//		
//		org.bouncycastle.asn1.pkcs.ContentInfo contentInfo = org.bouncycastle.asn1.pkcs.ContentInfo.getInstance(sign);
////		ASN1ObjectIdentifier type = contentInfo.getContentType();
//		
//		SignedData signedData = SignedData.getInstance(contentInfo.getContent());
////		String version = signedData.getVersion().toString();
//		Certificate cert1 = getCertBySignedData(signedData);
//		X509Certificate x509Cert;
//		try {
//			x509Cert = (X509Certificate) CertificateFactory.getInstance("X509",new BouncyCastleProvider()).generateCertificate(new ByteArrayInputStream(cert1.getEncoded()));
//
//			// 证书是否过期验证，如果不用系统日期可用cert.checkValidity(date);
//			x509Cert.checkValidity();
//		} catch (CertificateExpiredException e1) {
//			// TODO Auto-generated catch block
//			logger.info(e1.getMessage());
//			return false;
//		} catch (CertificateNotYetValidException e1) {
//			// TODO Auto-generated catch block
//			logger.info(e1.getMessage());
//			return false;
//		} catch (CertificateException e1) {
//			// TODO Auto-generated catch block
//			logger.info(e1.getMessage());
//			return false;
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			logger.info(e1.getMessage());
//			return false;
//		}
//
//		byte[] pubkey = CertTools.getPublicKeyBytes(cert1, true);
//		if(pubkey.length == 0) {
//			logger.info("矩阵不能为空");
//			return false;
//		}
////		byte[] inData = ASN1OctetString.getInstance(signedData.getContentInfo().getContent()).getOctets();
//		//获取SignedData中的SignerInfo数据
//		ASN1Set signerInfos = signedData.getSignerInfos();
//		Enumeration e = signerInfos.getObjects();
//		SignerInfo signerInfo = SignerInfo.getInstance(e.nextElement());
//
//		//获取SignerInfo中的签名值
//		ASN1OctetString encryptedDigest = signerInfo.getEncryptedDigest();
//		ASN1Sequence asn1Sequence2 = ASN1Sequence.getInstance(encryptedDigest.getOctets());
//		SM2Signature sm2Signature = new SM2Signature(asn1Sequence2);
//	    
//	    try {
//			boolean verifyResult=SM2Utils.verifySign(pubkey,data,sm2Signature.getEncoded());
//			
//			return verifyResult;
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			logger.info(e1.getMessage());
//			return false;
//		}
//	}
//
//	/***
//	 * 
//	 * @param sign 签名结果
//	 * @param data 被签名数据
//	 */
//	public boolean signVerifier(String sign,byte[] data ) {
//		getVerifier();
//		if(getRootCertificate() != null) {
//			boolean flag = verify(sign,data);
//			return flag;
//		}
//		logger.info("加载信任证书失败");
//		return false;
//	}
//	
//    public String encodeLocal(String source) throws Exception {
//    	EnvelopedData encodeLocal = encodeLocalToEnvelopedData(getRootCertificate(),source);
//    	byte[] en = encodeToContentInfo(encodeLocal);
//    	String encodeStr = Base64Utils.encode(en);
//    	return encodeStr;
//    }
//    
//    public static byte[] encodeToContentInfo(EnvelopedData by){
//    	ASN1ObjectIdentifier asn1ObjectIdentifier = IKIObjectIdentifiers.enveloped_data;
//    	
//    	ContentInfo contentInfo = new ContentInfo(asn1ObjectIdentifier,by);
//    	
//    	return AsnUtil.asn1ToByte(contentInfo);
//    }
//    
//    
//    public EnvelopedData encodeLocalToEnvelopedData(Certificate x509Cert, String source) throws Exception {
//
//		byte[] cipherKey = null; //数据加密密文SM2cipher，定义见GM/T 0009
//		byte[] cipherData = null; //内容加密结果
//
//		//解析证书加密公钥
//		byte[] publicKey = CertTools.getPublicKeyBytes(x509Cert, false);//加密公钥
//		//调用国密接口，获取加密数据
//		byte[] tmpKey = UUIDGenerator.getSerialId().getBytes();
//		
//		cipherData = SM4Utils.encryptDataECBNoGroup(source.getBytes(), tmpKey);
//		cipherKey = SM2Utils.encrypt(publicKey, tmpKey);
//		
//		//组装加密内容信息
//		ASN1ObjectIdentifier contentType = IKIObjectIdentifiers.data; //加密数据内容类型
//		AlgorithmIdentifier contentEncryptionAlgorithm = new AlgorithmIdentifier(IKIObjectIdentifiers.sm_4,null); //内容加密算法
//		ASN1OctetString encryptedContent = new DEROctetString(cipherData); //内容加密结果
//		EncryptedContentInfo encryptedContentInfo = new EncryptedContentInfo(contentType,contentEncryptionAlgorithm,encryptedContent);
//		
//		//组装接受者信息集合
//		IssuerAndSerialNumber issuerAndSerialNumber = new IssuerAndSerialNumber(x509Cert.getIssuer(), x509Cert.getSerialNumber().getPositiveValue()); //颁发者可辨别名和颁发序列号
//		AlgorithmIdentifier keyEncryptionAlgorithm = new AlgorithmIdentifier(IKIObjectIdentifiers.sm_2_encrypt,null); //用接受者公钥加密数据的加密密钥算法，为SM2-3椭圆曲线加密算法
//		ASN1OctetString encryptedKey = new DEROctetString(cipherKey); //数据加密密文SM2cipher，定义见GM/T 0009
//		
//		RecipientIdentifier rid = new RecipientIdentifier(issuerAndSerialNumber);
//		KeyTransRecipientInfo keyTransRecipientInfo = new KeyTransRecipientInfo(rid, keyEncryptionAlgorithm, encryptedKey);
//		RecipientInfo recipientInfo = new RecipientInfo(keyTransRecipientInfo);
//		ASN1Set recipientInfos = getASN1Set(recipientInfo);
//		
//		//封装数字信封
//		ASN1EncodableVector  v = new ASN1EncodableVector();
//        v.add(new ASN1Integer(0));
//        v.add(recipientInfos);
//        v.add(encryptedContentInfo);
//		EnvelopedData envelopedData = EnvelopedData.getInstance(new DERSequence(v));
//		
//		return envelopedData;
//	}
//    
//    private ASN1Set getASN1Set(ASN1Encodable obj){
//		ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
//		asn1EncodableVector.add(obj);
//		DERTaggedObject localDERTaggedObject = new DERTaggedObject(false, 1, new DERSequence(asn1EncodableVector));
//		ASN1Set result = ASN1Set.getInstance(localDERTaggedObject, false);
//		return result;
//	}
//
//}
