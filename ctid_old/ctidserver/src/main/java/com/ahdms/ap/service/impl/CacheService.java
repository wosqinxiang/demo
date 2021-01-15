package com.ahdms.ap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.dao.ServerAccountDao;
import com.ahdms.ap.model.ServerAccount; 

/**
 * @Title
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author tb
 * @version 2.0
 * @date 2018/11/15
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Component
public class CacheService {
	
	@Autowired
	private ServerAccountDao serverDao;
	
	@Cacheable(value = Contents.CACHE_SERVER_INFO, key = "#deviceName + '_' + #password")
	public ServerAccount selectServer(String deviceName,String password){
		ServerAccount account = serverDao.selectServer(deviceName, password);
		return account;
	}

	@CacheEvict(value = Contents.CACHE_SERVER_INFO, key = "#deviceName + '_' + #password")
	public Boolean clearServer(String deviceName,String password){
		return true;
	}
	
	@Cacheable(value = Contents.CACHE_SERVER_INFO_BYNAME, key = "#deviceName")
	public ServerAccount selectServerByName(String deviceName){
		ServerAccount account = serverDao.selectByName(deviceName);
		return account;
	}

	@CacheEvict(value = Contents.CACHE_SERVER_INFO_BYNAME, key = "#deviceName")
	public Boolean clearServerByName(String deviceName){
		return true;
	}
	
}
