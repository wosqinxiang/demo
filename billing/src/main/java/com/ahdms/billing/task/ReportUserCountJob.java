package com.ahdms.billing.task;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ahdms.billing.dao.*;
import com.ahdms.billing.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.utils.DateUtils;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.ChannelDataReportVO;
import com.ahdms.billing.vo.DataReportVO;
import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

@Component
public class ReportUserCountJob implements SimpleJob{
	private static final Logger logger = LoggerFactory.getLogger(ReportUserCountJob.class);
	
	@Autowired
	private ServiceLogMapper serviceLogMapper;
	
	@Autowired
	private ServiceInfoMapper serviceInfoMapper;

	@Autowired
	private UserServiceMapper userServiceMapper;
	
	@Autowired
	private ChannelInfoMapper channelInfoMapper;
	
	@Autowired
	private ReportMapper reportMapper;
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private DataReportUtils dataReportUtils;
	
	@Override
	public void execute(ShardingContext shardingContext) {
		logger.info("统计报表定时任务执行..."+LocalTime.now());
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		Date date = c.getTime();
		String yearStr = DateUtils.format(date, "yyyy");
		String monthStr = DateUtils.format(date, "yyyy-MM");
		String dayStr = DateUtils.format(date, "yyyy-MM-dd");
		List<ServiceInfo> serviceInfoList = serviceInfoMapper.findAll();
		List<ChannelInfo> findAll = channelInfoMapper.findAll();
		List<UserInfo> userInfos = userInfoMapper.findAll();
		
		//统计当天及当月按服务类型的总量
		countDayAndMonthForAll(monthStr, dayStr, serviceInfoList, findAll);
		//按用户统计当天及当月按服务类型的总量
		countDayAndMonthForUsername(monthStr, dayStr, yearStr, findAll, userInfos);
		//统计当天及当月按渠道类型的总量
		countDayAndMonthByChannelForAll(monthStr, dayStr, serviceInfoList, findAll);
		//按用户统计当天及当月按渠道类型的总量
		countDayAndMonthByChannelForUser(monthStr, dayStr, yearStr, findAll, userInfos);
		
		dataReportUtils.createDataForService(dayStr,0);
		dataReportUtils.createDataForService(monthStr,1);
		dataReportUtils.createDataForUser(dayStr,0);
		dataReportUtils.createDataForUser(monthStr,1);
		
		dataReportUtils.createDataForServiceByUsers(dayStr,0);
		dataReportUtils.createDataForServiceByUsers(monthStr,1);
		
	}
	
	
	public void execute(String date){
		Date parse = DateUtils.parse(date, "yyyy-MM-dd");
		//插入前一天的数据统计
		//更新月份的数据统计
		String monthStr = DateUtils.format(parse, "yyyy-MM");
		String dayStr = DateUtils.format(parse, "yyyy-MM-dd");
		String yearStr = DateUtils.format(parse, "yyyy");
		List<ServiceInfo> serviceInfoList = serviceInfoMapper.findAll();
		List<ChannelInfo> channelInfoList = channelInfoMapper.findAll();
		List<UserInfo> userInfos = userInfoMapper.findAll();
		
		//统计当天及当月按服务类型的总量
		countDayAndMonthForAll(monthStr, dayStr, serviceInfoList, channelInfoList);
		//按用户统计当天及当月按服务类型的总量
		countDayAndMonthForUsername(monthStr, dayStr, yearStr, channelInfoList, userInfos);
		//统计当天及当月按渠道类型的总量
		countDayAndMonthByChannelForAll(monthStr, dayStr, serviceInfoList, channelInfoList);
		//按用户统计当天及当月按渠道类型的总量
		countDayAndMonthByChannelForUser(monthStr, dayStr, yearStr, channelInfoList, userInfos);
//		
//		dataReportUtils.createDataForService(dayStr,0);
//		dataReportUtils.createDataForService(monthStr,1);
//		dataReportUtils.createDataForUser(dayStr,0);
//		dataReportUtils.createDataForUser(monthStr,1);
//
//		dataReportUtils.createDataForServiceByUsers(dayStr,0);
//		dataReportUtils.createDataForServiceByUsers(monthStr,1);
		
//		ServiceLogQuery slq_month = new ServiceLogQuery();
//		slq_month.setMonthStr(monthStr);
//		slq_month.setChannelEncode("0001");
//		slq_month.setServiceEncode("0A03");
//		int countMonth = serviceLogMapper.countByServiceLogQuery(slq_month);
//		System.out.println(countMonth);
	}
	
	
	
	
	private void countDayAndMonthForUsername(String monthStr,String dayStr,String yearStr,List<ChannelInfo> findAll,List<UserInfo> userInfos){
		
		
		try {
			for(UserInfo userInfo:userInfos){
				String username = userInfo.getUsername();
				List<DataReportVO> dataMonth = new ArrayList<>();
				List<DataReportVO> dataDay = new ArrayList<>();

				//获取用户已购买的服务
				List<ServiceInfo> userServiceInfos = getUserServiceInfo(userInfo.getId());

				countByService(userServiceInfos, findAll, monthStr, dataMonth, dayStr, dataDay, username);
				
				String contentMonth = JSON.toJSONString(dataMonth);
				String contentDay = JSON.toJSONString(dataDay);
				//判断此月是否已有
				Report monthReport = new Report(null,monthStr,BasicConstants.COUNT_SERVICE_ALL,contentMonth,username);
				Report dayReport = new Report(null,dayStr,BasicConstants.COUNT_SERVICE_ALL,contentDay,username);
				insertOrUpdate(monthReport);
				insertOrUpdate(dayReport);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	private List<ServiceInfo> getUserServiceInfo(String id) {

		List<UserService> userServices = userServiceMapper.selectByUserId(id);
		List<ServiceInfo> serviceInfos = userServices.stream()
				.map(userService -> serviceInfoMapper.queryByServiceEncode(userService.getServiceId()))
				.collect(Collectors.toList());
		return serviceInfos;
	}

	private void countDayAndMonthByChannelForAll(String monthStr,String dayStr,List<ServiceInfo> serviceInfoList,List<ChannelInfo> findAll){
		
		List<ChannelDataReportVO> dataMonth = new ArrayList<>();
		List<ChannelDataReportVO> dataDay = new ArrayList<>();
		
		try {
			countByChannel(serviceInfoList, findAll, monthStr, dataMonth, dayStr, dataDay,null);
			String contentMonth = JSON.toJSONString(dataMonth);
			String contentDay = JSON.toJSONString(dataDay);
			//判断此月是否已有
			Report monthReport = new Report(null,monthStr,BasicConstants.COUNT_CHANNEL_ALL,contentMonth,null);
			Report dayReport = new Report(null,dayStr,BasicConstants.COUNT_CHANNEL_ALL,contentDay,null);
			insertOrUpdate(monthReport);
			insertOrUpdate(dayReport);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void countDayAndMonthByChannelForUser(String monthStr,String dayStr,String yearStr,List<ChannelInfo> channelInfos,List<UserInfo> userInfos){
		try {
			for(UserInfo userInfo:userInfos){
				List<ChannelDataReportVO> dataYear = new ArrayList<>();
				List<ChannelDataReportVO> dataMonth = new ArrayList<>();
				List<ChannelDataReportVO> dataDay = new ArrayList<>();
				String username = userInfo.getUsername();
				//获取用户已购买的服务
				List<ServiceInfo> userServiceInfos = getUserServiceInfo(userInfo.getId());
				countByChannel(userServiceInfos, channelInfos, monthStr, dataMonth, dayStr, dataDay,username);
				String contentMonth = JSON.toJSONString(dataMonth);
				String contentDay = JSON.toJSONString(dataDay);
				//判断此月是否已有
				Report monthReport = new Report(null,monthStr,BasicConstants.COUNT_CHANNEL_ALL,contentMonth,username);
				Report dayReport = new Report(null,dayStr,BasicConstants.COUNT_CHANNEL_ALL,contentDay,username);
				insertOrUpdate(monthReport);
				insertOrUpdate(dayReport);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void countDayAndMonthForAll(String monthStr,String dayStr,List<ServiceInfo> serviceInfoList,List<ChannelInfo> findAll){
		
		try {
			List<DataReportVO> dataMonth = new ArrayList<>();
			List<DataReportVO> dataDay = new ArrayList<>();
			
			countByService(serviceInfoList, findAll, monthStr, dataMonth, dayStr, dataDay,null);
			String contentMonth = JSON.toJSONString(dataMonth);
			String contentDay = JSON.toJSONString(dataDay);
			//判断此月是否已有
			Report monthReport = new Report(null,monthStr,BasicConstants.COUNT_SERVICE_ALL,contentMonth,null);
			Report dayReport = new Report(null,dayStr,BasicConstants.COUNT_SERVICE_ALL,contentDay,null);
			insertOrUpdate(monthReport);
			insertOrUpdate(dayReport);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void countByChannel(List<ServiceInfo> serviceInfoList, List<ChannelInfo> findAll, String monthStr,
			List<ChannelDataReportVO> dataMonth, String dayStr, List<ChannelDataReportVO> dataDay,String username) {
		for(ChannelInfo channel:findAll){
			ChannelDataReportVO voMonth = new ChannelDataReportVO();
			ChannelDataReportVO voDay = new ChannelDataReportVO();
			int totalCountMonth = 0;
			int totalCountDay = 0;
			List<ChannelDataReportVO.ServiceReport> crListMonth = new ArrayList<>();
			List<ChannelDataReportVO.ServiceReport> crListDay = new ArrayList<>();
			for(ServiceInfo serviceInfo:serviceInfoList){
				ServiceLogQuery slq_month = new ServiceLogQuery();
				slq_month.setMonthStr(monthStr);
				slq_month.setChannelEncode(channel.getChannelCode());
				slq_month.setServiceEncode(serviceInfo.getServiceEncode());
				slq_month.setUsername(username);
				int countMonth = serviceLogMapper.countByServiceLogQuery(slq_month);
				totalCountMonth += countMonth;
				ChannelDataReportVO.ServiceReport crMonth = new ChannelDataReportVO.ServiceReport();
				crMonth.setServiceCode(serviceInfo.getServiceEncode());
				crMonth.setServiceName(serviceInfo.getServiceName());
				crMonth.setCount(countMonth);
				crListMonth.add(crMonth);
				
				ServiceLogQuery slq_day = new ServiceLogQuery();
				slq_day.setDateStr(dayStr);
				slq_day.setChannelEncode(channel.getChannelCode());
				slq_day.setServiceEncode(serviceInfo.getServiceEncode());
				slq_day.setUsername(username);
				int countDay = serviceLogMapper.countByServiceLogQuery(slq_day);
				totalCountDay += countDay;
				ChannelDataReportVO.ServiceReport crDay = new ChannelDataReportVO.ServiceReport();
				crDay.setServiceCode(serviceInfo.getServiceEncode());
				crDay.setServiceName(serviceInfo.getServiceName());
				crDay.setCount(countDay);
				crListDay.add(crDay);
			}
			voMonth.setCode(channel.getChannelCode());
			voMonth.setName(channel.getChannelName());
			voMonth.setTotalCount(totalCountMonth);
			voMonth.setChannelList(crListMonth);
			dataMonth.add(voMonth);
			
			voDay.setCode(channel.getChannelCode());
			voDay.setName(channel.getChannelName());
			voDay.setTotalCount(totalCountDay);
			voDay.setChannelList(crListDay);
			dataDay.add(voDay);
		}
	}
	

	private void countByService(List<ServiceInfo> serviceInfoList, List<ChannelInfo> findAll, String monthStr,
			List<DataReportVO> dataMonth, String dayStr, List<DataReportVO> dataDay,String username) {
		for(ServiceInfo si:serviceInfoList){
			DataReportVO voMonth = new DataReportVO();
			DataReportVO voDay = new DataReportVO();
			int totalCountMonth = 0;
			int totalCountDay = 0;
			List<DataReportVO.ChannelReport> crListMonth = new ArrayList<>();
			List<DataReportVO.ChannelReport> crListDay = new ArrayList<>();
			for(ChannelInfo channel:findAll){
				ServiceLogQuery slq_month = new ServiceLogQuery();
				slq_month.setMonthStr(monthStr);
				slq_month.setChannelEncode(channel.getChannelCode());
				slq_month.setServiceEncode(si.getServiceEncode());
				slq_month.setUsername(username);
				int countMonth = serviceLogMapper.countByServiceLogQuery(slq_month);
				totalCountMonth += countMonth;
				DataReportVO.ChannelReport crMonth = new DataReportVO.ChannelReport();
				crMonth.setChannelCode(channel.getChannelCode());
				crMonth.setChannelName(channel.getChannelName());
				crMonth.setCount(countMonth);
				crListMonth.add(crMonth);
				
				ServiceLogQuery slq_day = new ServiceLogQuery();
				slq_day.setDateStr(dayStr);
				slq_day.setChannelEncode(channel.getChannelCode());
				slq_day.setServiceEncode(si.getServiceEncode());
				slq_day.setUsername(username);
				int countDay = serviceLogMapper.countByServiceLogQuery(slq_day);
				totalCountDay += countDay;
				DataReportVO.ChannelReport crDay = new DataReportVO.ChannelReport();
				crDay.setChannelCode(channel.getChannelCode());
				crDay.setChannelName(channel.getChannelName());
				crDay.setCount(countDay);
				crListDay.add(crDay);
			}
			voMonth.setCode(si.getServiceEncode());
			voMonth.setName(si.getServiceName());
			voMonth.setTotalCount(totalCountMonth);
			voMonth.setChannelList(crListMonth);
			dataMonth.add(voMonth);
			
			voDay.setCode(si.getServiceEncode());
			voDay.setName(si.getServiceName());
			voDay.setTotalCount(totalCountDay);
			voDay.setChannelList(crListDay);
			dataDay.add(voDay);
		}
	}
	
	
	private void insertOrUpdate(Report report){
		Report _report = reportMapper.selectByReport(report);
		if(_report != null){
			report.setId(_report.getId());
			reportMapper.updateByPrimaryKeySelective(report);
		}else{
			report.setId(UUIDGenerator.getUUID());
			reportMapper.insertSelective(report);
		}
	}
}
