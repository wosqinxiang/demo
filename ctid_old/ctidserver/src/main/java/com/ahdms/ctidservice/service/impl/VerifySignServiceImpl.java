/**
 * <p>Title: VerifySignServiceImpl.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月14日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahdms.ctidservice.db.dao.DeviceInfoDao;
import com.ahdms.ctidservice.db.model.DeviceModel;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.service.VerifySignService;
import com.ahdms.ctidservice.util.SignAssistUtils;
import com.ahdms.identification.utils.TypeUtils;

/**
 * <p>Title: VerifySignServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月14日  
 */
@Service
public class VerifySignServiceImpl implements VerifySignService {
	Logger logger = LoggerFactory.getLogger(VerifySignServiceImpl.class);
	
	@Autowired
	private TokenCipherService cipherSer;

	@Autowired
	private DeviceInfoDao ddao;

	@Override
	public boolean verifySign(String appId, String signStr, Map<String, String> params) {
		if (StringUtils.isBlank(appId) || StringUtils.isBlank(signStr)) {
			return false;
		}
		String checkContent = SignAssistUtils.getSignCheckContent(params);
		byte[] source = checkContent.getBytes();

		byte[] sign = TypeUtils.hexStringToBinary(signStr);

		// 通过appId获取公钥
		DeviceModel device = ddao.selectByAppid(appId);
		Certificate cert = null;
		boolean bool = false;
		try {
			cert = Certificate.getInstance(ASN1Sequence.fromByteArray(device.getCertValue()));
			// 数字验签
			bool = cipherSer.verify(source, sign, cert);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bool;
	}

}
