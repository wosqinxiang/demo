/**
 * <p>Title: WxCtidService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年9月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import com.ahdms.ap.model.PersonInfo;
import com.ahdms.api.model.AuthCtidValidDateReturnBean;
import com.ahdms.ctidservice.bean.UserCardInfoBean;
import com.ahdms.ctidservice.bean.WxDownCtidRequestBean;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: WxCtidService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年9月19日  
 */
public interface WxCtidService {

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param openID
	 * @param cardName
	 * @param cardNum
	 * @param cardStart
	 * @param cardEnd
	 * @param authCode
	 * @return
	 */
	CtidResult downCtid(String openID, String cardName, String cardNum, String cardStart, String cardEnd,
			String authCode,String ip);

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param openID
	 * @return
	 */
	CtidResult<UserCardInfoBean> getCardInfo(String openID);

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param openID
	 * @return
	 */
	CtidResult<AuthCtidValidDateReturnBean> getCtidValidDate(String openID);

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param openID
	 * @return
	 */
	CtidResult getQrCodeData(String openID);

	CtidResult<PersonInfo> checkUserInfo(String openID);

}
