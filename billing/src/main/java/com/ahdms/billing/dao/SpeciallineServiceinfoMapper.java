package com.ahdms.billing.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ahdms.billing.model.SpeciallineServiceinfo;
import com.ahdms.billing.service.SpecialLineService;
import com.ahdms.billing.vo.SpecialLineServiceVO;

public interface SpeciallineServiceinfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(SpeciallineServiceinfo record);

    int insertSelective(SpeciallineServiceinfo record);

    SpeciallineServiceinfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SpeciallineServiceinfo record);

    int updateByPrimaryKey(SpeciallineServiceinfo record);

	List<SpecialLineServiceVO> findSpecialLineService(String specialLineId);

	SpeciallineServiceinfo selectBySpecialIdAndServiceId(@Param("specialId")String specialId,@Param("serviceId") String serviceId);

}