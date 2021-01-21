/**
 * Created on 2019年11月25日 by liuyipin
 */
package com.ahdms.billing.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.BoxInfo;
import com.ahdms.billing.service.ImportDeviceService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.BoxInfoVO;
import com.alibaba.dubbo.common.utils.NetUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @Title 
 * @Description 身份宝盒数据导入（管理界面操作）
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@RestController
@RequestMapping("/api/box")
public class ImportDeviceController { 
	
	@Autowired
	private ImportDeviceService ImportDeviceService; 
	
//	@Autowired
//	private OpinfoService opinfoService;
//
//	@Autowired
//	private LogService logService;
	
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ApiOperation(value = "导入文件", notes = "导入文件")
	@SysLog(comment="导入宝盒文件")
	public Result<String> importExcel(HttpServletRequest request,
			@RequestParam(value="file",required = false) MultipartFile file,@RequestParam(required=false) String typeId){
		Result<String> hb = new Result<>();
//		String identity = SessionUtils.getAuthUserIdentity(request);
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
//			if(StringUtils.isEmpty(typeId)){
//				throw new Exception("参数有误。");
//			}
			ImportDeviceService.importFile(file, typeId);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		}
		return hb;

	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value = "新增设备", notes = "新增设备")
	@SysLog(comment="新增宝盒设备")
	public Result<String> addBox(HttpServletRequest request,@RequestBody BoxInfo boxInfo){
		Result<String> hb = new Result<>();
//		String identity = SessionUtils.getAuthUserIdentity(request);
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			ImportDeviceService.addBox(boxInfo);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		}
		return hb;

	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ApiOperation(value = "修改设备", notes = "修改设备")
	@SysLog(comment="修改宝盒设备")
	public Result<String> editBox(HttpServletRequest request,@RequestBody BoxInfo boxInfo){
		Result<String> hb = new Result<>();
//		String identity =  SessionUtils.getAuthUserIdentity(request);
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			ImportDeviceService.editBox(boxInfo);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		}
		return hb;

	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ApiOperation(value = "删除设备", notes = "删除设备")
	@SysLog(comment="删除宝盒设备")
	public Result<String> deleteBox(HttpServletRequest request,@RequestParam(required=true) String boxId){
		Result<String> hb = new Result<>();
//		String identity = SessionUtils.getAuthUserIdentity(request);
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			ImportDeviceService.deleteBox(boxId);
			hb.setCode(0);
		} catch (Exception e) { 
			e.printStackTrace(); 
			hb.setMessage(e.getMessage());
			hb.setCode(1);
		}
//		OpLog opLog = new OpLog();
//		opLog.setId(UUIDGenerator.getUUID());
//		opLog.setIdentity(identity);
//		opLog.setIp(NetUtils.getIpAddress(request));
//		opLog.setActionType("删除离线设备");
//		opLog.setActionDesc("删除离线设备"+boxId);
//		opLog.setActionResult(hb.getCode());
//		logService.insertOpLog(opLog);
		return hb;

	}
	
	@RequestMapping(value = "/boxList", method = RequestMethod.GET)
	@ApiOperation(value = "分页查询操作设备列表", notes = "分页查询操作设备列表")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "boxNum", value = "设备编号", required = false, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "boxId", value = "设备表面id", required = false, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间", required = false, dataType = "date"),
		@ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", required = false, dataType = "date"),
		@ApiImplicitParam(paramType = "query", name = "boxTypeId", value = "设备分类id", required = false, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "page", value = "返回结果的页号", required = false, dataType = "int", defaultValue = "0"),
		@ApiImplicitParam(paramType = "query", name = "rows", value = "返回结果的行数", required = false, dataType = "int", defaultValue = "10") })
	public Result<GridModel<BoxInfoVO>> opLogList(HttpServletRequest request,
			@RequestParam(value = "boxNum",required = false) String boxNum ,
			@RequestParam(value = "boxId",required = false) String boxId ,
			@RequestParam(value = "startTime",required = false) String startTime,
			@RequestParam(value = "endTime",required = false) String endTime,
			@RequestParam(value = "boxTypeId",required = false) String boxTypeId,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {

		Result<GridModel<BoxInfoVO>> ret = new Result<GridModel<BoxInfoVO>>();
//		String identity = SessionUtils.getAuthUserIdentity(request);
		try {
//			if (null == opinfoService.queryByIdentity(identity)) {
//				throw new Exception("当前登录用户已被删除。");
//			}
			Map<String, Object> param = new HashMap<String, Object>();
			if (!StringUtils.isEmpty(boxNum)) {
				param.put("boxNum", boxNum);
			}
			if (!StringUtils.isEmpty(boxId)) {
				param.put("boxId", boxId);
			}
			if (!StringUtils.isEmpty(boxTypeId)) {
				param.put("boxTypeId", boxTypeId);
			}
			if (!StringUtils.isEmpty(startTime)) {
				param.put("startTime",new Date(Long.parseLong(startTime)));
			}
			if (!StringUtils.isEmpty(endTime)) {
				param.put("endTime", new Date(Long.parseLong(endTime)));
			}
			GridModel<BoxInfoVO> data = ImportDeviceService.selectBox(param, page, rows);

			ret.setData(data);
			ret.setCode(0);
		} catch (Exception e) {
			ret.setCode(1);
			ret.setMessage(e.getMessage());
		}
		return ret;
	}


}

