/**
 * Created on 2019年12月10日 by liuyipin
 */
package com.ahdms.ap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ahdms.ap.service.WechatService;
import com.ahdms.ap.utils.DateUtils;
import com.ahdms.ap.vo.TemplateDataVo;
import com.ahdms.ap.vo.WxMssVo;
import com.alibaba.fastjson.JSONObject;

/**
 * @Title
 * @Description
 * @Copyright
 *            <p>
 *            Copyright (c) 2015
 *            </p>
 * @Company
 *          <p>
 *          迪曼森信息科技有限公司 Co., Ltd.
 *          </p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Service
public class WechatServiceImpl implements WechatService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatServiceImpl.class);
 
	@Autowired
	private RestTemplate restTemplate;

	@Value("${wx.mini.program.appId}")
	private String appId;

	@Value("${wx.mini.program.secretKey}")
	private String secretKey;
	
	@Value("${wx.mini.program.wxPushUrl}")
	private String wxPushUrl;
	
	
	@Value("${wx.mini.program.picTokenUrl}")
	private String wxPushToken;

	public String pushOneUser(String access_token, String openid, String templateId, List<TemplateDataVo> keywords) {

		// 如果access_token为空则从新获取
		if (StringUtils.isEmpty(access_token)) {
			access_token = getAccess_token();
		}

		String url = wxPushUrl + access_token;

		// 拼接推送的模版
		WxMssVo wxMssVo = new WxMssVo();
		wxMssVo.setTouser(openid);// 用户openid
		wxMssVo.setTemplate_id(templateId);// 模版id
		Map<String, TemplateDataVo> m = new HashMap<>();

		// 封装数据
		if (keywords.size() > 0) {
			for (TemplateDataVo vo : keywords) {
				System.out.println(vo.getKey() + "  " + vo.getValue());
				m.put(vo.getKey(), vo);
			}
			wxMssVo.setData(m);
		} else {
			LOGGER.error("keywords长度为空");
			return null;
		}

		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMssVo, String.class);
		LOGGER.error("小程序推送结果={}", responseEntity.getBody());
		return responseEntity.getBody();
	}

	/*
	 * 获取access_token appid和appsecret到小程序后台获取，当然也可以让小程序开发人员给你传过来
	 */
	public String getAccess_token() {
		// 获取access_token
		String url = wxPushToken + "&appid=" + appId
				+ "&secret=" + secretKey;
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		String json = restTemplate.getForObject(url, String.class);
		JSONObject myJson = JSONObject.parseObject(json);
		return myJson.get("access_token").toString();
	}

	public static void main(String[] args) {
		System.out.println(new WechatServiceImpl().getAccess_token());

		WechatService weChatUtil = new WechatServiceImpl();
		List<TemplateDataVo> keywords = new ArrayList<TemplateDataVo>();
		keywords.add(new TemplateDataVo("thing14", "11"));
		keywords.add(new TemplateDataVo("date3", DateUtils.getNowTime()));
		keywords.add(new TemplateDataVo("date4", DateUtils.getNowTime()));
		keywords.add(new TemplateDataVo("thing11", "11"));
		keywords.add(new TemplateDataVo("thing15", "11"));
		weChatUtil.pushOneUser(weChatUtil.getAccess_token(), "oXm6-4ps_oDDWvS78O30vIeOHzRU",
				"fDG3KzrN9M8PYoACSkgZVBbGloo6aTBFE_P3YPv5Flo", keywords);
	}
}
