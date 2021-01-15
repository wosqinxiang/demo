/**
 * <p>Title: VerifySignService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月14日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import java.util.Map;

/**
 * <p>Title: VerifySignService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月14日  
 */
public interface VerifySignService {

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param appId
	 * @param sign
	 * @param map
	 * @return
	 */
	boolean verifySign(String appId, String sign, Map<String, String> map);

}
