/**
 * <p>Title: CtidPCManageServiceImpl.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年9月9日  
 * @version 1.0  
*/
package com.ahdms.auth.service.impl;

import java.util.Map;

import com.ahdms.api.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ahdms.api.service.CtidAuthService;
import com.ahdms.api.service.CtidCreateCodeService;
import com.ahdms.api.service.CtidDownService;
import com.ahdms.api.service.CtidPCManageService;
import com.ahdms.api.service.CtidValidateCodeService;
import com.ahdms.auth.common.PCControlApiUtils;
import com.ahdms.auth.constants.CtidConstants;
import com.ahdms.auth.core.PKCS7Tool;
import com.ahdms.auth.model.ReservedDataEntity;
import com.alibaba.dubbo.config.annotation.Service;

import net.sf.json.JSONObject;

/**
 * <p>Title: CtidPCManageServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年9月9日  
 */
@Service(group="${bjca.group}",version="${dubbo.version}")
@Component
public class CtidPCManageServiceImpl implements CtidPCManageService {
	private static final Logger logger = LoggerFactory.getLogger(CtidPCManageServiceImpl.class);
	
	@Autowired
	private CtidDownService downCtidService;
	
	@Autowired
	private CtidAuthService authCtidService;
	
	@Autowired
	private CtidCreateCodeService createCodeService;
	
	@Autowired
	private CtidValidateCodeService validateCodeService;
	
//	@Autowired
//	private HttpSend hs;
	
	@Autowired
	private PKCS7Tool p7Tool;
	
	@Value("${ctid.pc.address}")
	private String address;
	
	@Autowired
	private PCControlApiUtils pcUtils;

	@Override
	public ApiResult<CtidMessage> downCtidOX10(String authCode,String cardName,String cardNum,String cardStart,String cardEnd) {
		try {
			ApiResult<ApplyReturnBean> downCtidApply = downCtidService.downCtidApply("0x10");
			if(downCtidApply.getCode() != 0){
				return ApiResult.error(downCtidApply.getMessage());
			}
			ApplyReturnBean data = downCtidApply.getData();
			String businessSerialNumber = data.getBusinessSerialNumber();
			String randomNumber = data.getRandomNumber();
			//将认证码和随机数发给PC端服务器
			String authCodeData = pcUtils.getAuthCodeData(randomNumber, authCode);
			String idAuthData = pcUtils.getIDAuthData(randomNumber,"",CtidConstants.NO_CARD_DOWN_TYPE);
			if(StringUtils.isNotBlank(authCodeData) && StringUtils.isNotBlank(idAuthData)){
				ReservedDataEntity.SFXXBean sfxx = getSFXXBeanEntity(cardName, cardNum, cardStart, cardEnd);
				String authApplyRetainData = p7Tool.encodeLocal(JSONObject.fromObject(sfxx).toString());
				
				ApiResult<CtidMessage> downCtidRequest = downCtidService.downCtidRequest(businessSerialNumber, "0x10", "", idAuthData, authCodeData, authApplyRetainData);
				return downCtidRequest;
			}else{
				logger.error("调用PC控件出错");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ApiResult.error("服务器内部出错！请重试！");
	}
	
	private ReservedDataEntity.SFXXBean getSFXXBeanEntity(String xM,String gMSFZHM,String yXQQSRQ,String yXQJZRQ){
		ReservedDataEntity.SFXXBean sfxx = new ReservedDataEntity.SFXXBean();
		sfxx.setxM(xM);
		sfxx.setgMSFZHM(gMSFZHM);
		sfxx.setyXQQSRQ(yXQQSRQ);
		sfxx.setyXQJZRQ(yXQJZRQ);
		return sfxx;
	}

	@Override
	public ApiResult<CtidAuthReturnBean> authCtid0X06(String faceData, String ctidInfo) {
		try {
			String authMode = "0x06";
			ApiResult<ApplyReturnBean> authCtidApply = authCtidService.authCtidApply(authMode);
			if(authCtidApply.getCode() != 0){
				return ApiResult.error(authCtidApply.getMessage());
			}
			ApplyReturnBean data = authCtidApply.getData();
			String businessSerialNumber = data.getBusinessSerialNumber();
			String randomNumber = data.getRandomNumber();
			//从PC端得到  idCheck数据
			String idAuthData = pcUtils.getIDAuthData(randomNumber, ctidInfo, CtidConstants.NO_CARD_AUTH_TYPE);
			if(StringUtils.isNotBlank(idAuthData)){
				ApiResult<CtidAuthReturnBean> authCtidRequest = authCtidService.authCtidRequest(businessSerialNumber, authMode, faceData, idAuthData, "", "");
				return authCtidRequest;
			}else{
				logger.error("调用PC控件出错");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ApiResult.error("服务器内部出错！请重试！");
	}

	@Override
	public ApiResult<CreateCodeRequestReturnBean> createCtidCode(String ctidInfo) {
		try {
			String applyData = pcUtils.getQRCodeApplyData(CtidConstants.CREATE_QRCODE_TYPE);
			logger.info("applyData>>"+applyData);
			if (StringUtils.isNotBlank(applyData)) {
				ApiResult<ApplyReturnBean> applyResult = createCodeService.createCodeApply(applyData);
				if (applyResult.getCode() == 0) {
					ApplyReturnBean data = applyResult.getData();
					String checkData = pcUtils.getReqQRCodeData(data.getRandomNumber(), ctidInfo);
					logger.info("checkData>>"+checkData);
					if(StringUtils.isNotBlank(checkData)){
						ApiResult<CreateCodeRequestReturnBean> createCodeRequest = createCodeService
								.createCodeRequest(data.getBusinessSerialNumber(), checkData, CtidConstants.CODE_NOT_PIC);
						return createCodeRequest;
					}
				}else{
					return ApiResult.error(applyResult.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApiResult.error("服务器内部出错！请重试！");
	}

	@Override
	public ApiResult validateCtidCode(Integer authMode, String faceData, String authCode, String qrCode) {
		
		String applyData = pcUtils.getQRCodeApplyData(CtidConstants.AUTH_QRCODE_TYPE);
		if (StringUtils.isNotBlank(applyData)) {
			ApiResult<ApplyReturnBean> applyResult = validateCodeService.validateCodeApply(applyData, authMode);
			if(applyResult.getCode() == 0){
				ApplyReturnBean data = applyResult.getData();
				String checkData = pcUtils.getAuthQRCodeData(data.getRandomNumber(), qrCode);
				
				if(StringUtils.isNotBlank(authCode)){
					authCode = pcUtils.getAuthCodeData(data.getRandomNumber(), authCode);
				}
				
				if(StringUtils.isNotBlank(checkData)){
					ApiResult<ValidateCodeRequestReturnBean> validateCodeRequest = validateCodeService.validateCodeRequest(data.getBusinessSerialNumber(), authMode, authCode, faceData, checkData);
					return validateCodeRequest;
				}
			}
		}
		return ApiResult.error("服务器内部出错！请重试！");
	}

	@Override
	public ApiResult<CtidMessage> downCtidInfo(String authCode, String authApplyRetainData) {
		try {
			ApiResult<ApplyReturnBean> downCtidApply = downCtidService.downCtidApply("0x10");
			ApplyReturnBean data = downCtidApply.getData();
			String businessSerialNumber = data.getBusinessSerialNumber();
			String randomNumber = data.getRandomNumber();
			//将认证码和随机数发给PC端服务器
			String authCodeData = pcUtils.getAuthCodeData(randomNumber, authCode);
			String idAuthData = pcUtils.getIDAuthData(randomNumber,"",CtidConstants.NO_CARD_DOWN_TYPE);
			if(StringUtils.isNotBlank(authCodeData) && StringUtils.isNotBlank(idAuthData)){
				
				ApiResult<CtidMessage> downCtidRequest = downCtidService.downCtidRequest(businessSerialNumber, "0x10", "", idAuthData, authCodeData, authApplyRetainData);
				return downCtidRequest;
			}else{
				logger.error("调用PC控件出错");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ApiResult.error("服务器内部出错！请重试！");
	}
	
	/**
	 * 获得网证编号以及网证有效期
	 */
	@Override
	public AuthCtidValidDateReturnBean getCtidNumAndValidDate(String ctidInfo) {
		AuthCtidValidDateReturnBean bean = pcUtils.getCtidNumAndValidDate(ctidInfo);
		return bean;
	}
	
}
