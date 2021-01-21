/**
 * Created on 2020年5月11日 by liuyipin
 */
package com.ahdms.billing.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.ahdms.jf.model.JFConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahdms.billing.dao.BoxTypeDao;
import com.ahdms.billing.dao.KeyInfoMapper;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.model.BoxType;
import com.ahdms.billing.model.KeyInfo;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.system.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;

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
@Service(timeout = 3000)
@Component
public class DubboSelectUserImpl implements UserService {
	 
	@Autowired
	private UserInfoMapper userdao;
	
	@Autowired
	private KeyInfoMapper keyInfoMapper;
	
	@Autowired
	private BoxTypeDao boxTypeDao;

	@Override
	public String getUserId(String value, Integer type) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		if(type == 1) {
			map.put("serviceAccount", value);
		}else if (type == 2) {
			map.put("sdkEncode", value);
		}else {
			map.put("boxId", value);
		}
		String userid = userdao.selectUSerId(map);
		return userid;
	}


	@Override
	public String getBoxId(String boxNum) {
//		boxInfoDao.selectByNun(boxNum);
		return null;
	}

	@Override
	public String getSdkEncode(String appId){
		KeyInfo selectByAppId = keyInfoMapper.selectByAppId(appId);
		if(selectByAppId != null){
			return selectByAppId.getUserServiceAccount();
		}
		return null;
	}

	@Override
	public Map<String, String> getUserIdAndServiceAccount(String userServiceAccount, Integer type) {
		Map<String,String> data = new HashMap<>();
		if(type == JFConstants.TYPE_SDK){ //渠道为SDK
			String[] appIdAndPackage = userServiceAccount.split(",");
			KeyInfo keyInfo = keyInfoMapper.selectByAppIdAndAppPackage(appIdAndPackage[0],appIdAndPackage[1]);
			userServiceAccount = keyInfo == null ?null :keyInfo.getUserServiceAccount();
		}else if(type == JFConstants.TYPE_BOX){ //渠道为 宝盒
			BoxType boxType = boxTypeDao.selectByBoxNum(userServiceAccount);
			userServiceAccount = boxType == null ?null :boxType.getUserServiceAccount();
		}else if(type == JFConstants.TYPE_API_PWD){
			//渠道为API服务  用户名以及密码传入
			String[] account = userServiceAccount.split(",");
			UserInfo userInfo = userdao.selectByServiceInfo(account[0],account[1]);
			if(userInfo != null){
				data.put("userId", userInfo.getId());
				data.put("userServiceAccount", account[0]);
				return data;
			}
		}
		if(StringUtils.isNotBlank(userServiceAccount)){
			UserInfo userInfo = userdao.selectByServiceAccount(userServiceAccount);
			if(userInfo != null){
				data.put("userId", userInfo.getId());
			}
		}
		data.put("userServiceAccount", userServiceAccount);
		return data;
	}

}

