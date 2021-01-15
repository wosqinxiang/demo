/**
 * <p>Title: TestController.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月30日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.web.controller;

import com.ahdms.ap.task.elasticjobconfig.StartJobConfig;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.common.FileUtils;
import com.ahdms.ctidservice.common.UUIDGenerator;
import com.ahdms.ctidservice.db.dao.CtidInfosMapper;
import com.ahdms.ctidservice.db.dao.CtidPidInfosMapper;
import com.ahdms.ctidservice.db.model.CtidInfos;
import com.ahdms.ctidservice.db.model.CtidPidInfos;
import com.ahdms.ctidservice.service.*;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.util.SM4Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: TestController</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月30日  
 */
@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
public class TestPCController {
	Logger logger = LoggerFactory.getLogger(TestPCController.class);
	@Autowired
	private DownCtidService downCtidService;
	
	@Autowired
	private AuthCtidService authCtidService;
	
	@Autowired
	private CtidPCManageService ctidPCManage;

	@Autowired
	private CtidInfosMapper ctidInfosMapper;

	@Autowired
	private CtidPidInfosMapper ctidPidInfosMapper;
	
	@Autowired
	private StartJobConfig config;

	@RequestMapping("/test/pidToCtidPidInfo")
	public CtidResult pidToCtidPidInfo(){
		List<CtidInfos> ctidInfosList = ctidInfosMapper.selectAll();
		CtidPidInfos ctidPidInfos;
		for(CtidInfos ctidInfos:ctidInfosList){
			ctidPidInfos = new CtidPidInfos(UUIDGenerator.getUUID(),ctidInfos.getId(),ctidInfos.getPid(),null);
			ctidPidInfosMapper.insertSelective(ctidPidInfos);
		}
		return CtidResult.ok("success"+ctidInfosList.size());
	}
	
	@RequestMapping("/test/update")
	public CtidResult testUpdate(@RequestParam String jobName,@RequestParam String cron,@RequestParam String par){
		config.updateJob(jobName, cron, par);
		return CtidResult.ok("success");
	}

	
	@Autowired
	private CtidLogService ctidLogService;
	
	
	@RequestMapping("/test/decode")
	public String decode(@RequestParam String encodeStr){
		try {
			SM4Utils sm4 = new SM4Utils("AHdms520", "ahdms", false);
			String decryptData_ECB = sm4.decryptData_ECB(encodeStr);
			return decryptData_ECB;
		} catch (Exception e) {
		}
		return "error";
	}

	@RequestMapping("/test/testDownApply")
	public CtidResult testDownApply(@RequestBody CtidRequestBean authMode) throws Exception{
		return CtidResult.ok(authMode);

	}

	@RequestMapping("/test/testAuthApply")
	public CtidResult testAuthApply(@RequestParam String authMode) throws Exception{

		return authCtidService.authCtidApply(authMode,"");

	}
	
	@RequestMapping("/test/testDown")
	public CtidResult<CtidMessage> testDown(@RequestParam String authCode,
			@RequestParam String xM,
			@RequestParam String gMSFZHM,
			@RequestParam String yXQQSRQ,
			@RequestParam String yXQJZRQ,
			@RequestParam String serverId,
			HttpServletRequest request) throws Exception{
		
		return ctidPCManage.downCtidOX10(authCode, xM, gMSFZHM, yXQQSRQ, yXQJZRQ, serverId);
		
	}
	
	@Autowired
	private AuthIdCardService authIdCardService;
	
	@RequestMapping("/test/authIdCard")
	public CtidResult authIdCard(@RequestParam String cardName,@RequestParam String cardNum,@RequestParam String serverId){
		return authIdCardService.auth(cardName, cardNum, null, serverId);
	}
	
	
	@RequestMapping("/test/testAuth")
	public CtidResult<Map<String, String>> testAuth(
			@RequestParam String ctidInfo,@RequestParam String face,
			@RequestParam(required=false) String serverId) throws Exception{
		
		return ctidPCManage.authCtid0X06(Base64Utils.encode(FileUtils.toByteArray(face)), ctidInfo, serverId);
	}
	
	
	@RequestMapping("/test/testCreateCode")
	public CtidResult testCreateCode(@RequestParam Integer type,
			@RequestParam String ctidInfo,
			@RequestParam Integer isPic,@RequestParam String serverId) throws Exception{
		CtidResult createCtidCode = ctidPCManage.createCtidCode(ctidInfo,serverId);
		return createCtidCode;
	}
	
	@RequestMapping("/test/testValidateCode")
	public CtidResult testValidateCode(@RequestParam(required = false) String authCode,
			@RequestParam(required = false) String facePath,
			@RequestParam(required = true) Integer authMode,
			@RequestParam(required = true) String qrCode,@RequestParam String serverId) throws Exception{
		return ctidPCManage.validateCtidCode(authMode, Base64Utils.encode(FileUtils.toByteArray(facePath)), authCode, qrCode,serverId);
	}


	
}
