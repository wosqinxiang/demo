/**
 * <p>Title: DownCtidService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: DownCtidService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 */
public interface DownCtidService {
	
	CtidResult<ApplyReturnBean> downCtidApply(String authMode,String serverId);
	
	CtidResult<CtidMessage> downCtidRequest(String businessSerialNumber,String authMode,String photoData,String idcardAuthData,String authCode,String authApplyRetainData,String serverId);
	
	CtidResult<CtidMessage> downCtidRequest(String businessSerialNumber,String authMode,String photoData,String idcardAuthData,String authCode,String cardName,String cardNum,String cardStart,String cardEnd,String serverId);

}
