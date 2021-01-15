package com.ahdms.ap.service.impl;

import com.ahdms.ap.dao.PersonInfoDao;
import com.ahdms.ap.model.PersonInfo;
import com.ahdms.ap.service.WxAuthLoginService;
import com.ahdms.ap.utils.HttpRequestUtils;
import com.ahdms.ap.utils.JsonUtils;
import com.ahdms.ap.utils.UUIDGenerator;
import com.ahdms.ap.vo.WxMiniProgramVo;
import com.ahdms.ctidservice.service.TokenCipherService;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisCluster;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * WxAuthLoginServiceImpl class
 *
 * @author hetao
 * @date 2019/08/01
 */
@Service
public class WxAuthLoginServiceImpl implements WxAuthLoginService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WxAuthLoginServiceImpl.class);
	private static final String ERRCODE = "errcode";
	private static final String SESSION_KEY = "session_key";

	@Autowired
	private PersonInfoDao personDao;

	@Value("${wx.mini.program.url}")
	private String weixinUrl;

	@Autowired
	private TokenCipherService cipherService;

	@Resource(name="ctidJedis")
	private JedisCluster jedisCluster;

	@Override
	public WxMiniProgramVo wxAuthLogin(String code, String appId, String secretKey,String ip) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("code: {}", code);
			LOGGER.debug("appId: {}", appId);
			LOGGER.debug("secretKey: {}", secretKey);
		}
		WxMiniProgramVo ret = new WxMiniProgramVo();
		if (code == null || "".equals(code)) {
			return null;
		} else {
			// 发送http请求
			String url = weixinUrl+"?appid=" + appId + "&secret=" + secretKey
					+ "&js_code=" + code + "&grant_type=authorization_code";   //接口地址
			String results = HttpRequestUtils.sendGetReq(url);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("url: {}", url);
				LOGGER.debug("results: {}", results);
			}
			if (results == null || "".equals(results)) {
				LOGGER.error("网络超时");
				ret.setCode(1);
				ret.setMessage("网络超时");
			} else {
				JSONObject res = JSONObject.fromObject(results);
				//把信息封装到map
				Map<String, Object> map = JsonUtils.parseJSON2Map(res);
				LOGGER.info("map: {}", map);
				if (map.containsKey(ERRCODE)) {
					String errcode = map.get("errcode").toString();
					LOGGER.error("微信返回的错误码 {}", errcode);
					ret.setCode(1);
					ret.setMessage("微信返回的错误码: "+errcode);
				} else if (map.containsKey(SESSION_KEY)) {
					LOGGER.info("调用微信成功");
					//                    ret.setOpenid(map.get("openid").toString());
					//                    ret.setSessionKey(map.get("session_key").toString());
					String uuid = UUIDGenerator.getUUID();
					String token = cipherService.encodeAccess(map.get("openid").toString(), LocalDateTime.now().toString(), ip,uuid.substring(16));
					if(StringUtils.isBlank(token)){ 
						ret.setCode(1);
						ret.setMessage("获取token失败，请重新登录");
					}else{
						jedisCluster.set(uuid.substring(0,16), token);
						jedisCluster.expire(uuid.substring(0,16), 1800);
						ret.setToken(uuid);
						ret.setOpenid(map.get("openid").toString());
						ret.setCode(0);
						ret.setMessage("调用微信成功");
					}
				}
			}
		}
		return ret;
	}

	@Override
	public WxMiniProgramVo oldwxAuthLogin(String code, String appId, String secretKey) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("code: {}", code);
			LOGGER.debug("appId: {}", appId);
			LOGGER.debug("secretKey: {}", secretKey);
		}
		WxMiniProgramVo ret = new WxMiniProgramVo();
		if (code == null || "".equals(code)) {
			return null;
		} else {
			// 发送http请求
			String url = weixinUrl+"?appid=" + appId + "&secret=" + secretKey
					+ "&js_code=" + code + "&grant_type=authorization_code";   //接口地址
			String results = HttpRequestUtils.sendGetReq(url);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("url: {}", url);
				LOGGER.debug("results: {}", results);
			}
			if (results == null || "".equals(results)) {
				LOGGER.error("网络超时");
				ret.setCode(1);
				ret.setMessage("网络超时");
			} else {
				JSONObject res = JSONObject.fromObject(results);
				//把信息封装到map
				Map<String, Object> map = JsonUtils.parseJSON2Map(res);
				LOGGER.info("map: {}", map);
				if (map.containsKey(ERRCODE)) {
					String errcode = map.get("errcode").toString();
					LOGGER.error("微信返回的错误码 {}", errcode);
					ret.setCode(1);
					ret.setMessage("微信返回的错误码: "+errcode);
				} else if (map.containsKey(SESSION_KEY)) {
					LOGGER.info("调用微信成功");
					ret.setOpenid(map.get("openid").toString());
					ret.setSessionKey(map.get("session_key").toString());
					ret.setCode(0);
					ret.setMessage("调用微信成功");
				}
			}
		}
		return ret;
	}


	@Override
	public List<PersonInfo> selectByOpenId(String openid) {
		return personDao.selectByOpenId(openid);
	}
	@Override
	public PersonInfo selectByOpenID(String openid) {
		return personDao.selectByOpenID(openid);
	}  

 
}
