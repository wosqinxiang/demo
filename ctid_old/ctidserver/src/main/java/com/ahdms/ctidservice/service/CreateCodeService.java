/**
 * <p>Title: CreateCodeService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CreateCodeRequestReturnBean;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: CreateCodeService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 */
public interface CreateCodeService {
	
	CtidResult<ApplyReturnBean> createCodeApply(String applyData,String serverId);
	
	CtidResult<CreateCodeRequestReturnBean> createCodeRequest(String bizSerialNum,String checkData,Integer isPic,String serverId);

}
