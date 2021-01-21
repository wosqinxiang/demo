package com.ahdms.billing.dao;

import com.ahdms.billing.model.Report;

public interface ReportMapper {

    int deleteByPrimaryKey(String id);

    int insert(Report record);

    int insertSelective(Report record);

    Report selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKeyWithBLOBs(Report record);

    int updateByPrimaryKey(Report record);

	Report selectByDate(String date);

	Report selectByReport(Report report);
}