/**
 * <p>Title: ApiAuthService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: ApiAuthService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月7日  
 */
public interface ApiAuthService {

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param authMode
	 * @param cardName
	 * @param cardNum
	 * @param photoData
	 * @return
	 */
	CtidResult<CtidAuthReturnBean> auth(String cardName, String cardNum, String photoData, String serverId);

}
