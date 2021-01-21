package com.ahdms.billing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.service.DataReportShowService;

@RestController
@RequestMapping(value="/api/show",method={RequestMethod.GET,RequestMethod.POST})
public class DataReportShowController {
	
	@Autowired
	private DataReportShowService dataReportShowService;
	
	@RequestMapping("showTotalAll")
	public Result showTotalAll(@RequestParam String date,
			@RequestParam(required=false,defaultValue="1") int type,
			@RequestParam(required=false)String username){
		return dataReportShowService.showTotalAll(date, type, username);
	}
	
	@RequestMapping("showByServiceAndChannel")
	public Result showByServiceAndChannel(@RequestParam String date,
			@RequestParam(required=false,defaultValue="1") int type,
			@RequestParam(required=false)String username,
			@RequestParam(required=false)String serviceCode,
			@RequestParam(required=false)String channelCode){
		return dataReportShowService.showByServiceAndChannel(date, type, username, serviceCode, channelCode);
	}

	@RequestMapping("showUserServiceCount")
	public Result showUserServiceCount(@RequestParam(required=false)String username,@RequestParam String date){
		return dataReportShowService.showUserServiceCount(date,username);
	}

}
