/**
 * Created on 2019年12月10日 by liuyipin
 */
package com.ahdms.ap.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ahdms.ap.vo.TemplateDataVo;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public interface WechatService {

	  public String pushOneUser(String access_token,String openid,String templateId,List<TemplateDataVo> keywords) ;
	  
	  public String getAccess_token();
}

