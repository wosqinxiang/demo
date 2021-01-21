package com.ahdms.billing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.KeyInfoMapper;
import com.ahdms.billing.model.KeyInfo;
import com.ahdms.billing.service.KeyInfoService;

@Service
public class KeyInfoServiceImpl implements KeyInfoService {
	
	@Autowired
	private KeyInfoMapper keyInfoMapper;

	@Override
	public Result add(KeyInfo keyInfo) {
		try {
			keyInfoMapper.insertSelective(keyInfo);
			return Result.ok("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.error("添加失败！");
	}

	@Override
	public Result selectAll() {
		try {
			List<KeyInfo> list = keyInfoMapper.selectAll();
			return Result.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.error("查询失败！");
	}

	@Override
	public Result update(KeyInfo keyInfo) {
		try {
			int i = keyInfoMapper.updateByPrimaryKeySelective(keyInfo);
			return Result.ok("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.error("修改失败！");
	}

	@Override
	public Result delete(int id) {
		try {
			int i = keyInfoMapper.deleteByPrimaryKey(id);
			return Result.ok("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.error("删除失败！");
	}
	
	

}
