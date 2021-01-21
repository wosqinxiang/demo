/**
 * Created on 2019年12月16日 by liuyipin
 */
package com.ahdms.billing.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.manager.util.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.BoxType;
import com.ahdms.billing.service.DeviceTypeService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.BoxTypeVO;
import com.alibaba.dubbo.common.utils.NetUtils;

import io.swagger.annotations.ApiOperation;

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
@RestController
@RequestMapping("/api/devType")
public class DeviceTypeController {
	
	@Autowired
	private DeviceTypeService deviceTypeService; 
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value = "新增设备分类", notes = "新增设备分类")
	@SysLog(comment="新增离线设备分类")
	public Result<String> addBox(HttpServletRequest request,@RequestParam(value = "province",required = true) String province ,
			@RequestParam(value = "city",required = true) String city ,
			@RequestParam(value = "typeName",required = true) String typeName,
			@RequestParam(value = "userServiceAccount",required = true) String userServiceAccount){
		Result<String> hb = new Result<>();
		BoxType boxType =  null;
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			if(StringUtils.isBlank(province) || StringUtils.isBlank(city) || StringUtils.isBlank(typeName) || StringUtils.isBlank(userServiceAccount)){
				throw new Exception("传入参数有误。");
			}
			boxType = new BoxType(UUIDGenerator.getUUID(), province, city, typeName, new Date());
			boxType.setUserServiceAccount(userServiceAccount);
			deviceTypeService.addBoxType(boxType);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		}
		return hb;

	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ApiOperation(value = "修改设备分类", notes = "修改设备分类")
	@SysLog(comment="新增离线设备分类")
	public Result<String> editType(HttpServletRequest request,
			@RequestParam(value = "typeId",required = true) String typeId,
			@RequestParam(value = "province",required = false) String province,
			@RequestParam(value = "city",required = false) String city ,
			@RequestParam(value = "typeName",required = false) String typeName,
			@RequestParam(value = "userServiceAccount",required = false) String userServiceAccount){
		Result<String> hb = new Result<>();
//		String identity = SessionUtils.getAuthUserIdentity(request); 
		try {
//		 	if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			if(StringUtils.isBlank(typeId) ){
				throw new Exception("传入参数有误。");
			} 
			BoxType boxType  = new BoxType(typeId, province, city, typeName, null);
			boxType.setUserServiceAccount(userServiceAccount);
			deviceTypeService.editBoxType(boxType);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		}
		return hb;

	}
	

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ApiOperation(value = "删除设备分类", notes = "删除设备分类")
	@SysLog(comment="新增离线设备分类")
	public Result<String> delType(HttpServletRequest request,
			@RequestParam(value = "typeId",required = false) String typeId){
		Result<String> hb = new Result<>();
//		String identity = SessionUtils.getAuthUserIdentity(request); 
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			if(StringUtils.isBlank(typeId) ){
				throw new Exception("传入参数有误。");
			}  
			deviceTypeService.delBoxType(typeId);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		}
		return hb;

	}
	
	@RequestMapping(value = "/boxTypeList", method = RequestMethod.GET)
	@ApiOperation(value = "分页查询离线设备列表", notes = "分页查询离线设备列表")
	public Result<GridModel<BoxTypeVO>> boxTypeList(HttpServletRequest request,
			@RequestParam(value = "province",required = false) String province,
			@RequestParam(value = "city",required = false) String city ,
			@RequestParam(value = "typeName",required = false) String typeName,
			@RequestParam(value = "page",defaultValue="0") int page,
			@RequestParam(value = "rows",defaultValue="10") int rows) {

		Result<GridModel<BoxTypeVO>> ret = new Result<GridModel<BoxTypeVO>>();
//		String identity = SessionUtils.getAuthUserIdentity(request);
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			Map<String, Object> param = new HashMap<String, Object>();
			if (!StringUtils.isEmpty(province)) {
				param.put("province", province);
			}
			if (!StringUtils.isEmpty(city)) {
				param.put("city", city);
			}
			if (!StringUtils.isEmpty(typeName)) {
				param.put("typeName", typeName);
			} 
			GridModel<BoxTypeVO> data = deviceTypeService.selectBoxType(param, page, rows);

			ret.setData(data);
			ret.setCode(0);
		} catch (Exception e) {
			ret.setMessage(e.getMessage());
			ret.setCode(1);
			ret.setMessage(e.getMessage());
		}
		return ret;
	}
	
	@RequestMapping(value = "/getType", method = RequestMethod.POST)
	@ApiOperation(value = "获取设备分类", notes = "获取设备分类")
	public Result<List<BoxType>> getType(HttpServletRequest request){
		Result<List<BoxType>> hb = new Result<>();
//		String identity = SessionUtils.getAuthUserIdentity(request);  
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			} 
			List<BoxType> list = deviceTypeService.getType();
			hb.setData(list);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		} 
		return hb;

	}
	
}

