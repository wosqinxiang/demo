package com.ahdms.ctidservice.db.dao;

import org.apache.ibatis.annotations.Param;

import com.ahdms.ctidservice.db.model.Ctidinfo;

public interface CtidinfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(Ctidinfo record);

    int insertSelective(Ctidinfo record);

    Ctidinfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Ctidinfo record);

    int updateByPrimaryKey(Ctidinfo record);

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param id
	 * @param appId
	 */
	Ctidinfo selectByUserIdAndAppId(@Param("userId") String userId, @Param("appId")String appId);
	
	Ctidinfo selectByCardNum(String cardNum);
}