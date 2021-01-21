package com.ahdms.billing.dao;

import com.ahdms.billing.model.ServiceLog;
import com.ahdms.billing.model.ServiceLogQuery;
import com.ahdms.billing.model.report.ServiceLogForUserServiceData;
import com.ahdms.billing.vo.omp.ServiceLogPageReqVo;
import com.ahdms.billing.vo.omp.ServiceLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServiceLogMapper {

    int deleteByPrimaryKey(String id);

    int insert(ServiceLog record);

    int insertSelective(ServiceLog record);

    ServiceLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ServiceLog record);

    int updateByPrimaryKey(ServiceLog record);

    List<ServiceLog> findAll(ServiceLogQuery serviceLogQuery);

	int countByServiceLogQuery(ServiceLogQuery serviceLogQuery);

    List<ServiceLogForUserServiceData> queryServiceLogByDateAndUser(@Param("dateStr")String dateStr,@Param("monthStr")String monthStr,@Param("username")String username);
    List<ServiceLogForUserServiceData> queryServiceLogByDate(@Param("dateStr")String dateStr,@Param("monthStr")String monthStr);

    List<ServiceLogVo> getLogs(@Param("queryVo")ServiceLogPageReqVo serviceLogPageReqVo);
}