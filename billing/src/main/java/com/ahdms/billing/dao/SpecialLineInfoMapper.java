package com.ahdms.billing.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ahdms.billing.model.SpecialLineInfo;
import com.ahdms.billing.service.SpecialLineService;
import com.ahdms.billing.vo.SpecialLineVO;
import com.ahdms.billing.vo.query.SpecialLineQueryVO;

public interface SpecialLineInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(SpecialLineInfo record);

    int insertSelective(SpecialLineInfo record);

    SpecialLineInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SpecialLineInfo record);

    int updateByPrimaryKey(SpecialLineInfo record);

	List<SpecialLineVO> findSpecialLine(SpecialLineQueryVO queryVO);

	SpecialLineInfo selectByNameOrCode(SpecialLineInfo specialLineInfo);
	
	SpecialLineVO selectByCode(@Param("code")String code);

	List<SpecialLineService> findSpecialByProviderIdAndServiceId(@Param("providerId")String providerId, @Param("serviceId")String serviceId);

}