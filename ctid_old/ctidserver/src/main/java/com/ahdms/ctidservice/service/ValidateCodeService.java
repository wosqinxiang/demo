/**
 * <p>Title: ValidateCodeService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.ValidateCodeRequestReturnBean;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: ValidateCodeService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 */
public interface ValidateCodeService {
	
	CtidResult<ApplyReturnBean> validateCodeApply(String applyData,Integer authMode,String serverId);
	
	CtidResult<ValidateCodeRequestReturnBean> validateCodeRequest(String bizSerialNum,Integer authMode,String authCode,String photoData,String idcardAuthData,String serverId);

}
