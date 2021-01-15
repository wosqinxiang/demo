package com.ahdms.ap.dao;

import java.time.LocalDate;
import java.util.List;

import com.ahdms.ap.model.AuthRecord;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface AuthRecordDao {
    int deleteByPrimaryKey(String id);

    int insert(AuthRecord record);

    int insertSelective(AuthRecord record);

    AuthRecord selectByPrimaryKey(String id);

    AuthRecord selectBySerialNum(String serialNum);

    int updateByPrimaryKeySelective(AuthRecord record);

    int updateByPrimaryKeyWithBLOBs(AuthRecord record);

    int updateByPrimaryKey(AuthRecord record);

    PageList<AuthRecord> select(String IDcard, PageBounds pageBounds);

	List<AuthRecord> selectArchive(LocalDate time);

	void delArchive(LocalDate time);

	AuthRecord getRecordBySerialNum(String openId, String serialNum); 
}