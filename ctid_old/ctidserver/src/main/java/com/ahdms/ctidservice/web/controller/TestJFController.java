package com.ahdms.ctidservice.web.controller;

import java.io.IOException;
import java.util.Map;

import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import com.ahdms.ctidservice.util.IdGenerUtils;
import com.ahdms.jf.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.api.model.CtidMessage;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.common.FileUtils;
import com.ahdms.ctidservice.common.UUIDGenerator;
import com.ahdms.ctidservice.service.AuthIdCardService;
import com.ahdms.ctidservice.service.CtidPCManageService;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.ctidservice.vo.SfxxBean;
import com.ahdms.jf.client.JFClient;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/testjf",method={RequestMethod.GET,RequestMethod.POST})
public class TestJFController {
	
	@Autowired
	private CtidPCManageService ctidPCManageService;
	
	@Autowired
	private AuthIdCardService authIdCardService;
	
	@Autowired
	private JFClient jfClient;
	
	@RequestMapping("testJFClient")
	public CtidResult testJFClient(@RequestParam String serverAccount,
			@RequestParam String channelCode,
			@RequestParam String serviceCode,
								   HttpServletRequest request){
		
		//调用计费接口
		JFResult<JFInfoModel> jf = jfClient.jf(serverAccount, JFServiceEnum.getJFServiceEnum(serviceCode),JFChannelEnum.getJFChannelEnum(channelCode),IpUtils.getIpAddress(request));
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			return CtidResult.error(jf.getMessage());
		}

		JFInfoModel jfData = jf.getData();
		serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		jfClient.send(serverAccount, JFServiceEnum.getJFServiceEnum(serviceCode), JFChannelEnum.getJFChannelEnum(channelCode), 0, "测试计费接口成功", UUIDGenerator.getSerialId(),specialCode,serverAccount);
		
		return CtidResult.ok("成功");
	}

	@RequestMapping("testJFClient22")
	public CtidResult testJFClient(@RequestParam String serverAccount,
								   @RequestParam String servicePwd,
								   @RequestParam String channelCode,
								   @RequestParam String serviceCode,
								   HttpServletRequest request){

		//调用计费接口
		JFResult<JFInfoModel> jf = jfClient.jf(serverAccount,servicePwd, JFServiceEnum.getJFServiceEnum(serviceCode),JFChannelEnum.getJFChannelEnum(channelCode),IpUtils.getIpAddress(request));
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			return CtidResult.error(jf.getMessage());
		}

		JFInfoModel jfData = jf.getData();
		serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		jfClient.send(serverAccount, JFServiceEnum.getJFServiceEnum(serviceCode), JFChannelEnum.getJFChannelEnum(channelCode), 0, "测试计费接口成功", UUIDGenerator.getSerialId(),specialCode,serverAccount);

		return CtidResult.ok("成功");
	}
	
	@RequestMapping("/downCtid")
	public CtidResult downCtid(@RequestParam String type, @RequestParam String serverAccount, @RequestParam String cardName, @RequestParam String cardNum,
							   @RequestParam String cardStart, @RequestParam String cardEnd
			, @RequestParam String authCode, HttpServletRequest request){
		JFChannelEnum channel = JFChannelEnum.API;
		if("2".equals(type)){
			channel = JFChannelEnum.APP_SDK;
		}else if("3".equals(type)){
			channel = JFChannelEnum.BOX;
		}else if("4".equals(type)){
			channel = JFChannelEnum.WX_DOWNCTID;
		}
		
		//调用计费接口
		JFResult<JFInfoModel> jf = jfClient.jf(serverAccount, JFServiceEnum.CTID_DOWN,channel, IpUtils.getIpAddress(request));
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			return CtidResult.error(jf.getMessage());
		}
		
		SfxxBean sfxxBean = new SfxxBean();
		sfxxBean.setName(cardName);
		sfxxBean.setIdCard(cardNum);
		sfxxBean.setStartDate(cardStart);
		sfxxBean.setEndDate(cardEnd);

		JFInfoModel jfData = jf.getData();
		String userServerAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		CtidResult<CtidMessage> downCtidInfo = ctidPCManageService.downCtidInfo(authCode, sfxxBean, specialCode);
		
		jfClient.send(userServerAccount, JFServiceEnum.CTID_DOWN, channel, downCtidInfo.getCode(), downCtidInfo.getMessage(), UUIDGenerator.getSerialId(),specialCode,serverAccount);
		
		return downCtidInfo;
		
	}
	
	@RequestMapping("/authCard")
	public CtidResult authCard(@RequestParam String type,@RequestParam String serverAccount,
			@RequestParam String cardName,@RequestParam String cardNum,
			@RequestParam(required=false) String facePath,
							   HttpServletRequest request) throws IOException{
		JFChannelEnum channel = JFChannelEnum.API;
		if("2".equals(type)){
			channel = JFChannelEnum.APP_SDK;
		}else if("3".equals(type)){
			channel = JFChannelEnum.BOX;
		}else if("4".equals(type)){
			channel = JFChannelEnum.WX_FTOFVerify;
		}else if("5".equals(type)){
			channel = JFChannelEnum.WX_LDVerify;
		}else if("6".equals(type)){
			channel = JFChannelEnum.WX_LOGIN;
		}else if("7".equals(type)){
			channel = JFChannelEnum.WX_REGISTER;
		}
		
		String faceData = null;
		JFServiceEnum jfService = JFServiceEnum.CARD_AUTH;
		if(StringUtils.isNotBlank(facePath)){
			jfService = JFServiceEnum.CARD_FACE_AUTH;
			faceData = Base64Utils.encode(FileUtils.toByteArray(facePath));
		}
		
		//调用计费接口
		JFResult<JFInfoModel> jf = jfClient.jf(serverAccount, jfService,channel,IpUtils.getIpAddress(request));
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			return CtidResult.error(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String userServerAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		CtidResult auth = authIdCardService.auth(cardName, cardNum, faceData, specialCode);
		
		jfClient.send(userServerAccount, jfService, channel, auth.getCode(), auth.getMessage(), UUIDGenerator.getSerialId(),specialCode,serverAccount);
		
		return auth;
		
	}

	@Autowired
	private IdGenerUtils idGenerUtils;

	@Autowired
	private TokenCipherService tokenCipherService;

	@RequestMapping("bid")
	public String getBid(@RequestParam String idCard){
		String s = idGenerUtils.encryptBId(CalculateHashUtils.calculateHash1(idCard.getBytes()),"dms123");
		String s1 = idGenerUtils.encrypt2BId(s);
		System.out.println(s1);
		System.out.println(Base64Utils.decode(s1).length);
		System.out.println("dms123".getBytes());
		byte[] inData = CalculateHashUtils.calculateHash1(idCard.getBytes());
		String s3 = tokenCipherService.encryptCTID(inData);


		String s2 = tokenCipherService.decryptCTID(s3);
		System.out.println(">>>s2..."+s2);

		return s3;
	}

	public static void main(String[] args) {
		System.out.println("IGhA8mCK1MzXj5z/YVwfHeL2npYAHiaQGpvWxG/HDAAJIItSsHc+qF9ETjFB0gzA".length());
	}

}
