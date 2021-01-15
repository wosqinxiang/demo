/**
 * <p>Title: CtidPCManageService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年9月9日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import java.util.Map;

import com.ahdms.api.model.AuthCtidValidDateReturnBean;
import com.ahdms.api.model.CreateCodeRequestReturnBean;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.ctidservice.vo.SfxxBean;

/**
 * <p>Title: CtidPCManageService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年9月9日  
 */
public interface CtidPCManageService {

	CtidResult<CtidMessage> downCtidOX10(String authCode,String cardName,String cardNum,String cardStart,String cardEnd,String serverId);
	
	CtidResult<Map<String, String>> authCtid0X06(String faceData,String ctidInfo,String serverId);
	
	CtidResult<CreateCodeRequestReturnBean> createCtidCode(String ctidInfo,String serverId);
	
	CtidResult validateCtidCode(Integer authMode,String faceData,String authCode,String qrCode,String serverId);

	CtidResult<CtidMessage> downCtidInfo(String getvCode, String authApplyRetainData,String serverId);

	AuthCtidValidDateReturnBean getCtidNumAndValidDate(String ctidInfo,String serverId);

	CtidResult<CtidMessage> downCtidInfo(String authCode, SfxxBean sfxxBean, String serverId);
	
}
