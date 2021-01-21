package com.ahdms.billing.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.SpecialLineInfoMapper;
import com.ahdms.billing.dao.SpeciallineServiceinfoMapper;
import com.ahdms.billing.model.ManageLog;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.model.SpecialLineInfo;
import com.ahdms.billing.model.SpeciallineServiceinfo;
import com.ahdms.billing.service.SpecialLineService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.SpecialLineInfoVO;
import com.ahdms.billing.vo.SpecialLineServiceVO;
import com.ahdms.billing.vo.SpecialLineVO;
import com.ahdms.billing.vo.query.SpecialLineQueryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class SpecialLineServiceImpl implements SpecialLineService {
	Logger logger = LoggerFactory.getLogger(SpecialLineServiceImpl.class);
	
	@Autowired
	private SpecialLineInfoMapper specialLineMapper;
	
	@Autowired
	private SpeciallineServiceinfoMapper specialServiceMapper;

	@Override
	public Result addSpecialLine(SpecialLineInfo specialLineInfo) {
		try {
			SpecialLineInfo spInfo = specialLineMapper.selectByNameOrCode(specialLineInfo);
			if(spInfo != null){
				if(spInfo.getCode().equals(specialLineInfo.getCode())){
					return Result.error("新增专线失败,编码已存在");
				}
				return Result.error("新增专线失败,供应商下已存在相同的专线名称");
			}
			specialLineInfo.setId(UUIDGenerator.getUUID());
			specialLineMapper.insertSelective(specialLineInfo);
			return Result.ok(specialLineInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Result.error("添加专线失败");
	}

	@Override
	public Result addSpecialLineService(SpecialLineInfoVO specialLineInfoVO) {
		try {
			SpeciallineServiceinfo _specialServiceInfo = specialServiceMapper.selectBySpecialIdAndServiceId(specialLineInfoVO.getSpecialId(),specialLineInfoVO.getServiceId());
			if(_specialServiceInfo != null){
				return Result.error("专线服务已添加");
			}
			SpeciallineServiceinfo specialServiceInfo = new SpeciallineServiceinfo();
			specialServiceInfo.setId(UUIDGenerator.getUUID());
			specialServiceInfo.setServiceInfoId(specialLineInfoVO.getServiceId());
			specialServiceInfo.setSpecialLineId(specialLineInfoVO.getSpecialId());
			specialServiceMapper.insertSelective(specialServiceInfo);
			return Result.ok(specialServiceInfo);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return Result.error("添加专线服务失败");
	}

	@Override
	public Result<List<SpecialLineVO>> findSpecialLine(String providerId,String name, String code) {
		SpecialLineQueryVO queryVO = new SpecialLineQueryVO(providerId,name,code);
        List<SpecialLineVO> list = specialLineMapper.findSpecialLine(queryVO);
		return Result.ok(list);
	}

	@Override
	public Result deleteSpecialLine(String id) {
		try {
			int i = specialLineMapper.deleteByPrimaryKey(id);
			return Result.ok(i);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Result.error("删除专线失败");
	}

	@Override
	public Result findSpecialLineService(String id) {
		List<SpecialLineServiceVO> list = new ArrayList<SpecialLineServiceVO>();
		list = specialServiceMapper.findSpecialLineService(id);
		return Result.ok(list);
	}

	@Override
	public Result deleteSpecialLineService(String id) {
		try {
			int i = specialServiceMapper.deleteByPrimaryKey(id);
			return Result.ok(i);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Result.error("删除专线服务失败");
	}

	@Override
	public Result findSpecial(String providerId, String serviceId) {
		List<SpecialLineService> data = specialLineMapper.findSpecialByProviderIdAndServiceId(providerId,serviceId);
		return Result.ok(data);
	}

	@Override
	public Result updateSpecialLine(SpecialLineInfo specialLineInfo) {
		try {
			SpecialLineInfo spInfo = specialLineMapper.selectByNameOrCode(specialLineInfo);
			if(spInfo != null){
				if(spInfo.getCode().equals(specialLineInfo.getCode())){
					return Result.error("更新专线失败,编码已存在");
				}
				return Result.error("更新专线失败,供应商下已存在相同的专线名称");
			}
			int i = specialLineMapper.updateByPrimaryKey(specialLineInfo);
			if(i>0){
				return Result.success("更新成功!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Result.error("更新失败!");
	}

}
