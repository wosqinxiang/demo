package com.ahdms.ctidservice.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.ahdms.ctidservice.common.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ahdms.ap.model.PersonInfo;
import com.ahdms.api.model.AuthCtidValidDateReturnBean;
import com.ahdms.ctidservice.bean.UserCardInfoBean;
import com.ahdms.ctidservice.bean.WxDownCtidRequestBean;
import com.ahdms.ctidservice.service.WxCtidService;
import com.ahdms.ctidservice.vo.CtidResult;

@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
public class OldWxCtidController {
	
	@Autowired
	private WxCtidService wxCtidService;
	
	@RequestMapping("/wxctid/downCtid")
	public CtidResult downCtid(@RequestBody WxDownCtidRequestBean paramsBean,HttpServletRequest request){
		String openID = paramsBean.getOpenID();
		String cardName = paramsBean.getCardName();
		String cardNum = paramsBean.getCardNum();
		String cardStart = paramsBean.getCardStart();
		String cardEnd = paramsBean.getCardEnd();
		String authCode = paramsBean.getAuthCode();
		CtidResult downCtid = wxCtidService.downCtid(openID,cardName,cardNum,cardStart,cardEnd,authCode, IpUtils.getIpAddress(request));
		return downCtid;
	}
	
	@RequestMapping(value="/wxctid/getCardInfo")
	public CtidResult<UserCardInfoBean> getCardInfo(@RequestParam String openID){
		return wxCtidService.getCardInfo(openID);
	}
	
	@RequestMapping(value="/checkUserInfo")
	public CtidResult<PersonInfo> checkUserInfo(@RequestParam String openID){
		return wxCtidService.checkUserInfo(openID);
	}
	
	
	@RequestMapping("/wxctid/getCtidValidDate")
	public CtidResult<AuthCtidValidDateReturnBean> getCtidValidDate(@RequestParam String openID){
		return wxCtidService.getCtidValidDate(openID);
	}
	
	@RequestMapping("/wxctid/getQrCodeData")
	public CtidResult getQrCodeData(@RequestParam String openID,HttpServletRequest request){
		
		return wxCtidService.getQrCodeData(openID);
	}


}
