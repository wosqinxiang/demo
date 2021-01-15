/**
 * <p>Title: ApiAuthServiceImpl.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.CtidAuthReturnBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahdms.ctidservice.service.ApiAuthService;
import com.ahdms.ctidservice.service.AuthIdCardService;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: ApiAuthServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 */
@Service
public class ApiAuthServiceImpl implements ApiAuthService {
	Logger logger = LoggerFactory.getLogger(ApiAuthServiceImpl.class);
	
	@Autowired
	private AuthIdCardService authIdCardService;
	
	@Override
	public CtidResult<CtidAuthReturnBean> auth(String cardName, String cardNum, String photoData, String serverId) {
		
		return authIdCardService.auth(cardName, cardNum, photoData, serverId);
		
	}

}
