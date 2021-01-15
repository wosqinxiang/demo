/**
 * <p>Title: CtidManageService.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月14日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ahdms.ctidservice.bean.AuthCtidApplyBean;
import com.ahdms.ctidservice.bean.AuthCtidRequestBean;
import com.ahdms.ctidservice.bean.CreateCodeApplyBean;
import com.ahdms.ctidservice.bean.CreateCodeRequestBean;
import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.bean.DownCtidApplyBean;
import com.ahdms.ctidservice.bean.DownCtidRequestBean;
import com.ahdms.ctidservice.bean.ValidateCodeApplyBean;
import com.ahdms.ctidservice.bean.ValidateCodeRequestBean;
import com.ahdms.ctidservice.bean.dto.WxApplyCtidInputDTO;
import com.ahdms.ctidservice.bean.dto.WxAuthCardInputDTO;
import com.ahdms.ctidservice.vo.CtidResult;

/**
 * <p>Title: CtidManageService</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月14日  
 */
public interface CtidManageService {

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param params
	 * @return
	 */
	String authCtidRequest(Map<String, String> params);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 网证下载申请</p>  
	 * @param biz
	 * @return 
	 */
	CtidResult downCtidApply(CtidRequestBean dcaBean);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 网证下载请求</p>  
	 * @return
	 */
	CtidResult downloadCtidRequest(CtidRequestBean dcaBean,String ip);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 网证认证申请</p>  
	 * @param dcaBean
	 * @return
	 */
	CtidResult authCtidApply(CtidRequestBean dcaBean);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 网证认证请求</p>  
	 * @param biz
	 * @return
	 */
	CtidResult authCtidRequest(CtidRequestBean dcaBean,String ip);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 赋码申请</p>  
	 * @param data
	 * @return
	 */
	CtidResult createCodeApply(CtidRequestBean data);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 二维码赋码请求</p>  
	 * @param bean
	 * @return
	 */
	CtidResult createCodeRequest(CtidRequestBean bean,String ip);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 二维码验码申请</p>  
	 * @param data
	 * @return
	 */
	CtidResult validateCodeApply(CtidRequestBean data);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 二维码验码请求</p>  
	 * @param data
	 * @return
	 */
	CtidResult validateCodeRequest(CtidRequestBean data,String ip);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 保存网证信息</p>  
	 * @param dcaBean
	 * @return
	 */
	CtidResult saveCtidNum(CtidRequestBean dcaBean);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 获取网证信息</p>  
	 * @param dcaBean
	 * @return
	 */
	CtidResult authCtidValidDate(CtidRequestBean dcaBean);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 无网证认证</p>  
	 * @param dcaBean
	 * @return
	 */
	CtidResult v2AuthCard(CtidRequestBean dcaBean,HttpServletRequest request);
	
	/**
	 * <p>Title: </p>  
	 * <p>Description: 通过PC端控件网证下载</p>  
	 * @param dcaBean
	 * @return
	 */
	CtidResult downCtidInfo(CtidRequestBean dcaBean,String ip);
	
	/**
	 * <p>Title: </p>  
	 * <p>Description: 通过PC端控件网证认证</p>  
	 * @param dcaBean
	 * @return
	 */
	CtidResult authCtidInfo(CtidRequestBean dcaBean,String ip);

	/**
	 * <p>Title: </p>  
	 * <p>Description: 通过PC控件生成网证二维码</p>  
	 * @param dcaBean
	 * @return
	 */
	CtidResult createQrCode(CtidRequestBean data,String ip);

	CtidResult authCtidRequestNew(CtidRequestBean dcaBean,String ip);

	CtidResult getPid(String token);

	CtidResult applyCtidInfo(CtidRequestBean dcaBean,String ip);

	CtidResult wxApplyCtid(WxApplyCtidInputDTO params,String ip);

	CtidResult wxAuthCard(WxAuthCardInputDTO data,HttpServletRequest request);

    CtidResult sendSmsCode(CtidRequestBean dcaBean);

	CtidResult authSmsCode(CtidRequestBean dcaBean,String ip);

	/**
	 * 非网证认证 v1.0
	 * @param dcaBean
	 * @param request
	 * @return
	 */
	CtidResult authCard(CtidRequestBean dcaBean, HttpServletRequest request);

	String getDefaultAuthErrorString(String message);
}
