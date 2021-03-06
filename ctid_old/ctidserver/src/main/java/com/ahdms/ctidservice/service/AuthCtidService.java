/**
 * <p>Title: AuthCtidService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import java.util.Map;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: AuthCtidService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 */
public interface AuthCtidService {
	
	CtidResult<ApplyReturnBean> authCtidApply(String authMode,String serverId);
	
	CtidResult<CtidAuthReturnBean> authCtidRequest(String businessSerialNumber, String authMode, String photoData, String idcardAuthData, String authCode, String authApplyRetainData, String serverId);


}
