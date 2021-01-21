/**
 * Created on 2020年5月8日 by liuyipin
 */
package com.ahdms.billing.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ProviderInfoMapper;
import com.ahdms.billing.model.ProviderInfo;
import com.ahdms.billing.service.ProviderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Service
public class ProviderServiceImpl implements ProviderService {

	@Autowired
	private ProviderInfoMapper ProviderInfoDao;
	@Override
	public GridModel<ProviderInfo> queryProviderPageList(Map map, int page, int rows, Object object2) {
		GridModel<ProviderInfo> gridModel = new GridModel<ProviderInfo>();
		Page  pages = PageHelper.startPage(page, rows);
		List<ProviderInfo> list = ProviderInfoDao.queryProviderInfoPageList(map); 
		//	        PageInfo<AdminUserInfo> pageInfo = new PageInfo<>(list);
		gridModel.setPage(pages.getPageNum());
		gridModel.setRecords((int) pages.getTotal());
		gridModel.setTotal(pages.getPages());
		gridModel.setRows(list);

		return gridModel;
	}

	@Override
	public Result<String> addProvider(ProviderInfo provider) {
		Result<String> hb = new Result<String>();
		ProviderInfo p1 = ProviderInfoDao.queryByName(provider.getProviderName());
		if(null != p1) {
			hb.setMessage("供应商名称已存在！");
			hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
			return hb;
		}
		ProviderInfoDao.insertSelective(provider);
		hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);

		return hb;
	}

	@Override
	public Result<String> eidtProvider(ProviderInfo provider) {
		Result<String> hb = new Result<String>();  
		ProviderInfo p1 = ProviderInfoDao.queryByName(provider.getProviderName());
		if(null != p1 && !p1.getId().equals(provider.getId())) {
			hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
			hb.setMessage("供应商名称已存在！");
			return hb;
		}
		ProviderInfoDao.updateByPrimaryKey(provider);
		hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		return hb;
	}

	@Override
	public Result<List<ProviderInfo>> getProvider() {
		Result<List<ProviderInfo>> hb = new Result<List<ProviderInfo>>();
		List<ProviderInfo> list = ProviderInfoDao.getProvider();
		hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		hb.setData(list);
		return hb;
	}

	@Override
	public Result<List<ProviderInfo>> getProviderByServiceId(String serviceId) {
		List<ProviderInfo> data = ProviderInfoDao.getProviderByServiceId(serviceId);
		return Result.ok(data);
	}

}

