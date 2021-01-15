package com.ahdms.ctidservice.db.dao;

import org.apache.ibatis.annotations.Param;

import com.ahdms.ctidservice.db.model.CtidInfos;

import java.util.List;

public interface CtidInfosMapper {

    int deleteByPrimaryKey(String id);

    int insert(CtidInfos record);

    int insertSelective(CtidInfos record);

    CtidInfos selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CtidInfos record);

    int updateByPrimaryKey(CtidInfos record);
    
    CtidInfos selectByCardNum(String cardNum);

	CtidInfos selectByUserData(@Param(value = "cardName") String cardName, @Param(value = "cardNum")String cardNum, @Param(value = "cardStartDate")String cardStartDate, @Param(value = "cardEndDate")String cardEndDate);

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param getxM
	 * @param getgMSFZHM
	 * @return
	 */
	CtidInfos selectByCardNameAndCardNum(@Param(value = "cardName") String cardName, @Param(value = "cardNum")String cardNum);

	CtidInfos selectByPid(String pid);

    List<CtidInfos> selectAll();
}