/**
 * Created on 2019年11月27日 by liuyipin
 */
package com.ahdms.billing.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.ReadExcel;
import com.ahdms.billing.dao.BoxInfoDao;
import com.ahdms.billing.model.BoxInfo;
import com.ahdms.billing.model.BoxInfos;
import com.ahdms.billing.model.ServiceLog;
import com.ahdms.billing.service.ImportDeviceService;
import com.ahdms.billing.vo.BoxInfoVO;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
public class ImportDeviceServiceImpl implements ImportDeviceService {

	@Autowired
	private BoxInfoDao boxDao;

	@Override
	@Transactional
	public void importFile(MultipartFile file, String typeId) throws Exception { 
		//创建处理EXCEL的类  
		ReadExcel readExcel = new ReadExcel();  
		//解析excel，获取上传的事件单  
		List<BoxInfo> boxsList = readExcel.getExcelInfo(file);  
		//至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
		if(boxsList == null || boxsList.isEmpty()){   
			throw new Exception("待导入数据为空");
		}
		for(BoxInfo box : boxsList){ 
			int ret = 0 ;
			if(!StringUtils.isBlank(box.getBoxId())){
				if(StringUtils.isNotBlank(box.getBoxNum()) ){ 
					int i = boxDao.selectByNUM(box.getBoxId(),box.getBoxNum());
					if(i > 0){
						throw new Exception("芯片编号已存在，请检查参数。");
					}
				} 
				
				if(null != boxDao.selectByPrimaryKey(box.getBoxId())){ 
					box.setUpdateTime(new Date());
					box.setTypeId(typeId);
					ret = boxDao.updateByPrimaryKeySelective(box);
				}else{ 
					box.setCreateTime(new Date());
					box.setTypeId(typeId);
					ret = boxDao.insertSelective(box);
				} 
				if(ret == 0){ 
					throw new Exception("数据入库失败");
				}
			}

		}  
	}

	@Override
	public void addBox(BoxInfo boxInfo) throws Exception {
		// 单个新增设备
		if(StringUtils.isNotBlank(boxInfo.getBoxNum()) ){ 
			int i = boxDao.selectByNUM(boxInfo.getBoxId(),boxInfo.getBoxNum());
			if(i > 0){
				throw new Exception("芯片编号已存在，请检查参数。");
			}
		}
		BoxInfo box = boxDao.selectByPrimaryKey(boxInfo.getBoxId());
		if(null != box){
			throw new Exception("设备信息已存在，请检查录入信息。");
		}else{ 
			boxInfo.setCreateTime(new Date());
			int ret = boxDao.insertSelective(boxInfo);
			if(ret == 0){
				throw new Exception("设备录入失败。");
			}
		}
	}

	@Override
	public void editBox(BoxInfo boxInfo) throws Exception {
		// 单个修改设备
		int ret = 0;
		boxInfo.setUpdateTime(new Date());
		if(StringUtils.isNotBlank(boxInfo.getBoxNum()) ){ 
			int i = boxDao.selectByNUM(boxInfo.getBoxId(),boxInfo.getBoxNum());
			if(i > 0){
				throw new Exception("芯片编号已存在，请检查参数。");
			}
		}
		try{
			ret = boxDao.updateByPrimaryKeySelective(boxInfo); 
		}catch(Exception e){
			throw new Exception("数据过长。");
		}
		if(ret == 0){
			throw new Exception("设备更新失败。");
		}
	}

	@Override
	public void deleteBox(String boxId) throws Exception {
		// 删除设备
		int ret = boxDao.deleteByPrimaryKey(boxId);
		if(ret == 0){
			throw new Exception("设备删除失败。");
		}

	}

	@Override
	public GridModel<BoxInfoVO> selectBox(Map<String, Object> param, int page, int rows) {
		PageHelper.startPage(page, rows);
		List<BoxInfos> oplogPage = boxDao.pageQuery(param);
		PageInfo<BoxInfos> pageInfo = new PageInfo<>(oplogPage);
		List<BoxInfoVO> list = new ArrayList<BoxInfoVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (BoxInfos boxInfo : oplogPage) {
			BoxInfoVO vo = new BoxInfoVO();
			vo.setBoxId(boxInfo.getBoxId());
			vo.setBoxLocation(boxInfo.getBoxLocation());
			vo.setBoxNum(boxInfo.getBoxNum());
			vo.setBoxType(boxInfo.getBoxType());
			vo.setCreateTime(sdf.format(boxInfo.getCreateTime()));
			vo.setServerDepartment(boxInfo.getServerDepartment());
			if(null != boxInfo.getUpdateTime()){
				vo.setUpdateTime(sdf.format(boxInfo.getUpdateTime()));
			}
			vo.setTypeId(boxInfo.getTypeId());
			vo.setTypeName(boxInfo.getProvince()+boxInfo.getCity()+boxInfo.getTypeName());
			vo.setCounts(boxInfo.getCounts());
			list.add(vo);
		}

		GridModel<BoxInfoVO> girdModel = new GridModel<BoxInfoVO>();
		girdModel.setPage(pageInfo.getPageNum());
		girdModel.setRecords((int) pageInfo.getTotal());
		girdModel.setTotal(pageInfo.getPages());
		girdModel.setRows(list);

		return girdModel;
	}


} 

