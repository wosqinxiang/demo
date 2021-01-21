package com.ahdms.billing.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahdms.billing.dao.ChannelInfoMapper;
import com.ahdms.billing.dao.ReportMapper;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.model.ChannelInfo;
import com.ahdms.billing.model.Report;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.model.ServiceLogQuery;
import com.ahdms.billing.utils.DateUtils;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.DataReportVO;
import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

//@Component
public class ReportCountJob implements SimpleJob{
	
	@Autowired
	private ServiceLogMapper serviceLogMapper;
	
	@Autowired
	private ServiceInfoMapper serviceInfoMapper;
	
	@Autowired
	private ChannelInfoMapper channelInfoMapper;
	
	@Autowired
	private ReportMapper reportMapper;

	@Override
	public void execute(ShardingContext shardingContext) {
//		shardingContext.

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		Date date = c.getTime();
		//插入前一天的数据统计
		countDay(date);
		//更新月份的数据统计
		countMonth(date);
	}
	
	private void countMonth(Date date){
		String dateStr = DateUtils.format(date, "yyyy-MM");
		List<DataReportVO> data = new ArrayList<>();
		
		List<ServiceInfo> serviceInfoList = serviceInfoMapper.findAll();
		List<ChannelInfo> findAll = channelInfoMapper.findAll();
		
		for(ServiceInfo si:serviceInfoList){
			DataReportVO vo = new DataReportVO();
			int totalCount = 0;
			List<DataReportVO.ChannelReport> crList = new ArrayList<>();
			for(ChannelInfo channel:findAll){
				ServiceLogQuery slq = new ServiceLogQuery();
				slq.setMonthStr(dateStr);
				slq.setChannelEncode(channel.getChannelCode());
				slq.setServiceEncode(si.getServiceEncode());
				int count = serviceLogMapper.countByServiceLogQuery(slq);
				totalCount += count;
				DataReportVO.ChannelReport cr = new DataReportVO.ChannelReport();
				cr.setChannelCode(channel.getChannelCode());
				cr.setChannelName(channel.getChannelName());
				cr.setCount(count);
				crList.add(cr);
			}
			vo.setCode(si.getServiceEncode());
			vo.setName(si.getServiceName());
			vo.setTotalCount(totalCount);
			vo.setChannelList(crList);
			data.add(vo);
		}
		String content = JSON.toJSONString(data);
		//判断此月是否已有
		Report report = new Report();
		
		report.setDate(dateStr);
		report.setType(1);
		
		Report _report = reportMapper.selectByReport(report);
		if(_report != null){
			_report.setContent(content);
			reportMapper.updateByPrimaryKey(_report);
		}else{
			report.setId(UUIDGenerator.getUUID());
			report.setContent(content);
			reportMapper.insertSelective(report);
		}
		
	}
	
	private void countDay(Date date){
		String dateStr = DateUtils.format(date, "yyyy-MM-dd");
		List<DataReportVO> data = new ArrayList<>();
		
		List<ServiceInfo> serviceInfoList = serviceInfoMapper.findAll();
		List<ChannelInfo> findAll = channelInfoMapper.findAll();
		
		for(ServiceInfo si:serviceInfoList){
			DataReportVO vo = new DataReportVO();
			int totalCount = 0;
			List<DataReportVO.ChannelReport> crList = new ArrayList<>();
			for(ChannelInfo channel:findAll){
				ServiceLogQuery slq = new ServiceLogQuery();
				slq.setDateStr(dateStr);
				slq.setChannelEncode(channel.getChannelCode());
				slq.setServiceEncode(si.getServiceEncode());
				int count = serviceLogMapper.countByServiceLogQuery(slq);
				totalCount += count;
				DataReportVO.ChannelReport cr = new DataReportVO.ChannelReport();
				cr.setChannelCode(channel.getChannelCode());
				cr.setChannelName(channel.getChannelName());
				cr.setCount(count);
				crList.add(cr);
			}
			vo.setCode(si.getServiceEncode());
			vo.setName(si.getServiceName());
			vo.setTotalCount(totalCount);
			vo.setChannelList(crList);
			data.add(vo);
		}
		
		String content = JSON.toJSONString(data);
		//判断此日是否已有
		Report report = new Report();
		
		report.setDate(dateStr);
		report.setType(1);
		
		Report _report = reportMapper.selectByReport(report);
		if(_report != null){
			_report.setContent(content);
			reportMapper.updateByPrimaryKey(_report);
		}else{
			report.setId(UUIDGenerator.getUUID());
			report.setContent(content);
			reportMapper.insertSelective(report);
		}
	}
}
