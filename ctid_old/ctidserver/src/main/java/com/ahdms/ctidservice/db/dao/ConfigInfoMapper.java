package com.ahdms.ctidservice.db.dao;

import com.ahdms.ctidservice.db.model.ConfigInfo;

public interface ConfigInfoMapper {
	
	 int insertSelective(ConfigInfo record);
	 
	 ConfigInfo selectByKey(String configKey);

}
