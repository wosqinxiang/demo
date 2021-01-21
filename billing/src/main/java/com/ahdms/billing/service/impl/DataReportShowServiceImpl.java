package com.ahdms.billing.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ahdms.billing.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ChannelInfoMapper;
import com.ahdms.billing.dao.ReportMapper;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.model.ChannelInfo;
import com.ahdms.billing.model.Report;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.service.DataReportShowService;
import com.ahdms.billing.utils.DateUtils;
import com.ahdms.billing.vo.DataReportVO.ChannelReport;
import com.alibaba.fastjson.JSON;

@Service
public class DataReportShowServiceImpl implements DataReportShowService {
	
	@Autowired
	private ServiceLogMapper serviceLogMapper;
	
	@Autowired
	private ServiceInfoMapper serviceInfoMapper;
	
	@Autowired
	private ChannelInfoMapper channelInfoMapper;
	
	@Autowired
	private ReportMapper reportMapper;

	@Override
	public Result showTotalAll(String date, int type, String username) {
		List<DataReportBaseVO> data = new ArrayList<>();
		
		Report r = new Report();
		r.setDate(date);
		r.setType(1);
		r.setUsername(username);
		Report selectByDate = reportMapper.selectByReport(r);
		r.setType(2);
		Report selectByReport = reportMapper.selectByReport(r);
		if(null != selectByDate){
			String content = selectByDate.getContent();
			List<DataReportBaseVO> parseArray = JSON.parseArray(content, DataReportBaseVO.class);
			data.addAll(parseArray);
			if(null != selectByReport){
				String content2 = selectByReport.getContent();
				List<DataReportBaseVO> parseArray2 = JSON.parseArray(content2, DataReportBaseVO.class);
				data.addAll(parseArray2);
			}
		}else{
			List<ServiceInfo> serviceInfos = serviceInfoMapper.findAll();
			for(ServiceInfo si:serviceInfos){
				DataReportBaseVO drb = new DataReportBaseVO();
				drb.setCode(si.getServiceEncode());
				drb.setName(si.getServiceName());
				drb.setTotalCount(0);
				data.add(drb);
			}
			List<ChannelInfo> channelInfos = channelInfoMapper.findAll();
			for(ChannelInfo ci:channelInfos){
				DataReportBaseVO drb = new DataReportBaseVO();
				drb.setCode(ci.getChannelCode());
				drb.setName(ci.getChannelName());
				drb.setTotalCount(0);
				data.add(drb);
			}
		}
		return Result.ok(data);
	}

	@Override
	public Result showByServiceAndChannel(String date, int type, String username, String serviceCode,String channelCode) {
		Date parse = DateUtils.parse(date, "yyyy-MM");
		int countDay = getDaysByYearMonth(parse.getTime());
		//获取年月
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(parse.getTime());
		
		List<DataReportSimple> data = new ArrayList<>();
		
		for(int i=1;i<=countDay;i++){
			calendar.set(Calendar.DATE, i);
			String day = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
			Report report = new Report();
			report.setDate(day);
			report.setType(BasicConstants.COUNT_SERVICE_ALL);
			report.setUsername(username);
			Report selectByReport = reportMapper.selectByReport(report);
			DataReportSimple drs = new DataReportSimple();
			drs.setDay(formatInt(i));
			drs.setDate(day);
			if(selectByReport != null){
				String content = selectByReport.getContent();
				List<DataReportVO> list = JSON.parseArray(content, DataReportVO.class);
				for (DataReportVO dataReportVO : list) {
					String _serviceCode = dataReportVO.getCode();
					
					if(StringUtils.isBlank(serviceCode)){ //服务CODE为空时，选择所有服务，此时查询 渠道的总量
						report.setType(BasicConstants.COUNT_CHANNEL_ALL);
						Report channelReport = reportMapper.selectByReport(report);
						if(channelReport != null){
							String channelContent = channelReport.getContent();
							List<DataReportBaseVO> parseArray = JSON.parseArray(channelContent, DataReportBaseVO.class);
							int count = 0;
							for (DataReportBaseVO cdrVO : parseArray) {
								String cCode = cdrVO.getCode();
								if(StringUtils.isBlank(channelCode)){
									count += cdrVO.getTotalCount();
								}
								if(cCode.equals(channelCode)){
									count += cdrVO.getTotalCount();
								}
							}
							drs.setCount(count);
						}
					}
					
					if(_serviceCode.equals(serviceCode)){
						List<ChannelReport> channelList = dataReportVO.getChannelList();
						int count = 0;
						for (ChannelReport channelReport : channelList) {
							String _channelCode = channelReport.getChannelCode();
							if(StringUtils.isBlank(channelCode)){
								count += channelReport.getCount();
							}
							if(_channelCode.equals(channelCode)){
								count += channelReport.getCount();
							}
						}
						drs.setCount(count);
					}
				}
			}
			data.add(drs);
		}
		return Result.ok(data);
	}
	


	@Override
	public Result showUserServiceCount(String date, String username) {
		Report report = new Report();
		report.setType(1);
		report.setUsername(username);
		report.setDate(date);
		Report report1 = reportMapper.selectByReport(report);
		report.setDate("2021-01-14");

		Report report2 = reportMapper.selectByReport(report);
		String content = report1.getContent();

		List<List<DataReportBaseVO>> tableData = new ArrayList<>();

		List<DataReportBaseVO> parseArray = JSON.parseArray(content,DataReportBaseVO.class);
		List<DataReportBaseVO> parseArray2 = JSON.parseArray(report2.getContent(),DataReportBaseVO.class);

		List<ExcelHeaderVo> excelHeaderVos = new ArrayList<>();
		for(DataReportBaseVO dr : parseArray){
			ExcelHeaderVo excelHeaderVo = new ExcelHeaderVo();
			excelHeaderVo.setName(dr.getName());
			excelHeaderVo.setProp("totalCount");
			excelHeaderVos.add(excelHeaderVo);
		}
		tableData.add(parseArray);
		tableData.add(parseArray2);
		return Result.success(new ExcelUserCountRspVo(tableData,excelHeaderVos));
	}

	//获取指定月份的天数
    public static int getDaysByYearMonth(Long date) {
        Calendar a = Calendar.getInstance();
        a.setTimeInMillis(date);
        a.set(Calendar.MONTH, a.get(Calendar.MONTH)+1);
        a.set(Calendar.DATE, 1);
        a.set(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE) +1;
        return maxDate;
    }
    
    private String formatInt(int i){
		if(i<10){
			return "0"+i;
		}
		return String.valueOf(i);
	}

}
