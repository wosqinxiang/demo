package com.ahdms.billing.task;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ChannelInfoMapper;
import com.ahdms.billing.dao.ReportMapper;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.model.ChannelInfo;
import com.ahdms.billing.model.Report;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.model.ServiceType;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.report.DataJSON;
import com.ahdms.billing.model.report.ResultData;
import com.ahdms.billing.model.report.ServiceLogForUserServiceData;
import com.ahdms.billing.model.report.UserData;
import com.ahdms.billing.service.ServiceTypeService;
import com.ahdms.billing.service.UserInfoService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Component
public class DataReportUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(DataReportUtils.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ServiceInfoMapper serviceInfoMapper;

    @Autowired
    private ChannelInfoMapper channelInfoMapper;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ServiceLogMapper serviceLogMapper;
    
    public void createDataForUser(String date,String monthStr,UserInfo userInfo) {
        //生成表头
        try {
			Result<Object> objectResult = serviceTypeService.findAll();
			List<ServiceType> serviceType = (List<ServiceType>) objectResult.getData();
			List<List<ServiceInfo>> serviceInfos = new ArrayList<>();
			for (ServiceType type : serviceType) {
			    List<ServiceInfo> serviceInfosByType = serviceInfoMapper.selectServiceByType(type.getId());
			    serviceInfos.add(serviceInfosByType);
			    type.setServiceInfos(serviceInfosByType);
			}

			List<UserData> userDatas = new ArrayList<>();
			
			List<ServiceLogForUserServiceData> slfusd=serviceLogMapper.queryServiceLogByDateAndUser(date,monthStr,userInfo.getUsername());
		    UserData userData = new UserData();
		    userData.setId(userInfo.getId());
		    userData.setUsername(userInfo.getUsername());
		    userData.setServiceLogForUserServiceData(slfusd);
		    userDatas.add(userData);
		    
			List<List<DataJSON>> dataJSONS = new ArrayList<>();//内容数据
			ArrayList<DataJSON> dataContent = new ArrayList<>();
			DataJSON dataJSON = new DataJSON();
			dataJSON.setName("客户名称");
			dataContent.add(dataJSON);
			for (ServiceType type : serviceType) {
			    for (int i = 0; i < serviceInfos.size(); i++) {
			        for (int j = 0; j < serviceInfos.get(i).size(); j++) {
			            if (serviceInfos.get(i).get(j).getServiceType().equals(type.getId())) {
			                DataJSON dataJSON_service = new DataJSON();
			                dataJSON_service.setName(serviceInfos.get(i).get(j).getServiceName());
			                dataJSON_service.setId(serviceInfos.get(i).get(j).getServiceEncode());
			                dataJSON_service.setContent("0");
			                dataContent.add(dataJSON_service);
			            }
			        }
			    }
			    DataJSON dataJSON_sum = new DataJSON();
			    dataJSON_sum.setName("合计");
			    dataJSON_sum.setContent("0");
			    dataContent.add(dataJSON_sum);
			}
			//表内容
			for (UserData user : userDatas) {
			    ArrayList<DataJSON> dataContentForUser = new ArrayList<>();
			    for (DataJSON d : dataContent) {
			        DataJSON da = new DataJSON();
			        da.setContent(d.getContent());
			        da.setId(d.getId());
			        da.setName(d.getName());
			        dataContentForUser.add(da);
			    }
			    int sum = 0;
			    for (int i = 0; i < dataContentForUser.size(); i++) {
			        if (i == 0) {//第一列写入用户名称（固定列）
			            dataContentForUser.get(0).setContent(user.getUsername());
			            dataContentForUser.get(0).setId(user.getId());
			        } else {
			            List<ServiceLogForUserServiceData> serviceLogForUserServiceData=user.getServiceLogForUserServiceData();
			            if (serviceLogForUserServiceData != null && serviceLogForUserServiceData.size() > 0) {//含有服务数据的进行统计，否则使用默认值0
			                if (dataContentForUser.get(i).getId() != null) {
			                    for (int j = 0; j < serviceLogForUserServiceData.size(); j++) {
			                        if (dataContentForUser.get(i).getId() != null) {
			                            if (dataContentForUser.get(i).getId().equals(serviceLogForUserServiceData.get(j).getServiceId())) {
//			                                dataContentForUser.get(i).setContent("" + serviceLogForUserServiceData.get(j).getCount());
											dataContentForUser.get(i).setContent("" + (Integer.parseInt(dataContentForUser.get(i).getContent())+serviceLogForUserServiceData.get(j).getCount()));
			                                sum = sum + serviceLogForUserServiceData.get(j).getCount();
			                            }
			                        }
			                    }
			                } else {//只有合计列 id为空
			                    dataContentForUser.get(i).setContent("" + sum);
			                    sum = 0;//合计一组结束 sum清零
			                }
			            }
			        }
			    }
			    dataJSONS.add(dataContentForUser);
			}
			ResultData resultData = new ResultData();
			resultData.setTableTitle(serviceType);
			resultData.setTableContent(dataJSONS);
			Report report = new Report();
			report.setId(UUIDGenerator.getUUID());
			report.setType(3);
			report.setDate(date);
			report.setUsername(userInfo.getUsername());
			report.setContent(JSON.toJSONString(resultData, SerializerFeature.DisableCircularReferenceDetect));
			insertOrUpdate(report);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    }
	
	 /**
     * 生成统计报表内容-用户服务统计报表
     *
     * @param reportType 0 日报表 1月报表
     */
    public void createDataForUser(String date,int reportType) {
        //生成表头
        try {
        	String dayStr = null;
        	String monthStr = null;
        	if(0 == reportType){
        		dayStr = date;
        	}else{
        		monthStr = date;
        	}
        	
			Result<Object> objectResult = serviceTypeService.findAll();
			List<ServiceType> serviceType = (List<ServiceType>) objectResult.getData();
			List<List<ServiceInfo>> serviceInfos = new ArrayList<>();
			for (ServiceType type : serviceType) {
			    List<ServiceInfo> serviceInfosByType = serviceInfoMapper.selectServiceByType(type.getId());
			    serviceInfos.add(serviceInfosByType);
			    type.setServiceInfos(serviceInfosByType);
			}
			List<UserInfo> userInfos = userInfoService.findAll().getData();

			List<UserData> userDatas = new ArrayList<>();
			for (UserInfo user : userInfos) {
			    List<ServiceLogForUserServiceData> serviceLogForUserServiceData=serviceLogMapper.queryServiceLogByDateAndUser(dayStr,monthStr,user.getUsername());
			    UserData userData = new UserData();
			    userData.setId(user.getId());
			    userData.setUsername(user.getUsername());
			    userData.setServiceLogForUserServiceData(serviceLogForUserServiceData);
			    userDatas.add(userData);
			    
			    createDataForUser(date,monthStr,user);
			}
			List<List<DataJSON>> dataJSONS = new ArrayList<>();//内容数据
			ArrayList<DataJSON> dataContent = new ArrayList<>();
			DataJSON dataJSON = new DataJSON();
			dataJSON.setName("客户名称");
			dataContent.add(dataJSON);
			for (ServiceType type : serviceType) {
			    for (int i = 0; i < serviceInfos.size(); i++) {
			        for (int j = 0; j < serviceInfos.get(i).size(); j++) {
			            if (serviceInfos.get(i).get(j).getServiceType().equals(type.getId())) {
			                DataJSON dataJSON_service = new DataJSON();
			                dataJSON_service.setName(serviceInfos.get(i).get(j).getServiceName());
			                dataJSON_service.setId(serviceInfos.get(i).get(j).getServiceEncode());
			                dataJSON_service.setContent("0");
			                dataContent.add(dataJSON_service);
			            }
			        }
			    }
			    DataJSON dataJSON_sum = new DataJSON();
			    dataJSON_sum.setName("合计");
			    dataJSON_sum.setContent("0");
			    dataContent.add(dataJSON_sum);
			}
			//表内容
			for (UserData user : userDatas) {
			    ArrayList<DataJSON> dataContentForUser = new ArrayList<>();
			    for (DataJSON d : dataContent) {
			        DataJSON da = new DataJSON();
			        da.setContent(d.getContent());
			        da.setId(d.getId());
			        da.setName(d.getName());
			        dataContentForUser.add(da);
			    }
			    int sum = 0;
			    for (int i = 0; i < dataContentForUser.size(); i++) {
			        if (i == 0) {//第一列写入用户名称（固定列）
			            dataContentForUser.get(0).setContent(user.getUsername());
			            dataContentForUser.get(0).setId(user.getId());
			        } else {
			            List<ServiceLogForUserServiceData> serviceLogForUserServiceData=user.getServiceLogForUserServiceData();
			            if (serviceLogForUserServiceData != null && serviceLogForUserServiceData.size() > 0) {//含有服务数据的进行统计，否则使用默认值0
			                if (dataContentForUser.get(i).getId() != null) {
			                    for (int j = 0; j < serviceLogForUserServiceData.size(); j++) {
			                        if (dataContentForUser.get(i).getId() != null) {
			                            if (dataContentForUser.get(i).getId().equals(serviceLogForUserServiceData.get(j).getServiceId())) {
//			                                dataContentForUser.get(i).setContent("" + serviceLogForUserServiceData.get(j).getCount());
											dataContentForUser.get(i).setContent("" + (Integer.parseInt(dataContentForUser.get(i).getContent())+serviceLogForUserServiceData.get(j).getCount()));
			                                sum = sum + serviceLogForUserServiceData.get(j).getCount();
			                            }
			                        }
			                    }
			                } else {//只有合计列 id为空
			                    dataContentForUser.get(i).setContent("" + sum);
			                    sum = 0;//合计一组结束 sum清零
			                }
			            }
			        }
			    }
			    dataJSONS.add(dataContentForUser);
			}
			ResultData resultData = new ResultData();
			resultData.setTableTitle(serviceType);
			resultData.setTableContent(dataJSONS);
			Report report = new Report();
			report.setId(UUIDGenerator.getUUID());
			report.setType(3);
			report.setDate(date);
			report.setContent(JSON.toJSONString(resultData, SerializerFeature.DisableCircularReferenceDetect));
			insertOrUpdate(report);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    }
    
    public void createDataForServiceByUsers(String date,int reportType){
    	
    	List<UserInfo> userInfos = userInfoService.findAll().getData();
    	for (UserInfo userInfo : userInfos) {
    		createDataForServiceByUser(date,userInfo.getUsername(),reportType);
		}
    }
    
    public void createDataForServiceByUser(String date,String username,int reportType) {
        try {
            String dayStr = null;
            String monthStr = null;
            if(0 == reportType){
                dayStr = date;
            }else{
                monthStr = date;
            }
            //生成表头
            Result<Object> objectResult = serviceTypeService.findAll();
            List<ServiceType> serviceType = (List<ServiceType>) objectResult.getData();
            List<List<ServiceInfo>> serviceInfos = new ArrayList<>();
            for (ServiceType type : serviceType) {
                List<ServiceInfo> serviceInfosByType = serviceInfoMapper.selectServiceByType(type.getId());
                serviceInfos.add(serviceInfosByType);
                type.setServiceInfos(serviceInfosByType);
            }
            List<ChannelInfo> channelInfos = channelInfoMapper.findAll();
            List<ServiceLogForUserServiceData> serviceLogForUserServiceData=serviceLogMapper.queryServiceLogByDateAndUser(dayStr,monthStr,username);
            List<List<DataJSON>> dataJSONS = new ArrayList<>();//内容数据
            ArrayList<DataJSON> dataContent = new ArrayList<>();
            DataJSON dataJSON = new DataJSON();
            dataJSON.setName("渠道名称");
            dataContent.add(dataJSON);
            for (ServiceType type : serviceType) {
                for (int i = 0; i < serviceInfos.size(); i++) {
                    for (int j = 0; j < serviceInfos.get(i).size(); j++) {
                        if (serviceInfos.get(i).get(j).getServiceType().equals(type.getId())) {
                            DataJSON dataJSON_service = new DataJSON();
                            dataJSON_service.setName(serviceInfos.get(i).get(j).getServiceName());
                            dataJSON_service.setId(serviceInfos.get(i).get(j).getServiceEncode());
                            dataJSON_service.setContent("0");
                            dataContent.add(dataJSON_service);
                        }
                    }
                }
                DataJSON dataJSON_sum = new DataJSON();
                dataJSON_sum.setName("合计");
                dataJSON_sum.setContent("0");
                dataContent.add(dataJSON_sum);
            }
            ChannelInfo channel = new ChannelInfo();
            channel.setChannelName("合计");
            channelInfos.add(channel);
            //表内容
            List<Integer> serviceSums = new ArrayList<>();//服务合计数据
            for (int i = 0; i < dataContent.size() - 1; i++) {
                serviceSums.add(0);
            }
            for (ChannelInfo channelInfo : channelInfos) {
                ArrayList<DataJSON> dataContentForUser = new ArrayList<>();
                for (DataJSON d : dataContent) {
                    DataJSON da = new DataJSON();
                    da.setContent(d.getContent());
                    da.setId(d.getId());
                    da.setName(d.getName());
                    dataContentForUser.add(da);
                }
                int sum = 0;
                for (int i = 0; i < dataContentForUser.size(); i++) {
                    if (i == 0) {//第一列写入用户名称（固定列）
                        dataContentForUser.get(i).setContent(channelInfo.getChannelName());
                        dataContentForUser.get(i).setId(channelInfo.getChannelCode());
                    } else {
                        if (channelInfo.getId() == null) {//若id为null，则此行数据为合计数据
                            dataContentForUser.get(i).setContent("" + serviceSums.get(i - 1));
                        } else {
                            if (dataContentForUser.get(i).getId() != null) {
                                for (ServiceLogForUserServiceData slfusd : serviceLogForUserServiceData) {
                                    if (dataContentForUser.get(i).getId() != null) {
                                        if (dataContentForUser.get(i).getId().equals(slfusd.getServiceId())) {
                                            if (slfusd.getChannelEncode().equals(channelInfo.getChannelCode())) {
                                                dataContentForUser.get(i).setContent("" + slfusd.getCount());
                                                serviceSums.set(i - 1, serviceSums.get(i - 1) + slfusd.getCount());
                                                sum = sum + slfusd.getCount();
                                            }
                                        }
                                    }
                                }
                            } else {//只有合计列 id为空
                                dataContentForUser.get(i).setContent("" + sum);
                                serviceSums.set(i - 1, serviceSums.get(i - 1) + sum);
                                sum = 0;//合计一组结束 sum清零
                            }
                        }

                    }
                }
                dataJSONS.add(dataContentForUser);
            }
            List<List<Object>> list = new ArrayList<>();
            for (List<DataJSON> datas : dataJSONS) {
                List<Object> row = new ArrayList<>();
                for (int i = 0; i < datas.size(); i++) {
                    row.add(datas.get(i).getContent());
                }
                list.add(row);
            }

            ResultData resultData = new ResultData();
            resultData.setTableTitle(serviceType);
            resultData.setTableContent(dataJSONS);
            Report report = new Report();
            report.setId(UUIDGenerator.getUUID());
            report.setType(4);
            report.setDate(date);
            report.setUsername(username);
            report.setContent(JSON.toJSONString(resultData, SerializerFeature.DisableCircularReferenceDetect));
            insertOrUpdate(report);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    /**
     * 生成统计报表内容-渠道服务统计报表
     *
     * @param reportType 0 日报表 1月报表
     */
    public void createDataForService(String date,int reportType) {
        try {
        	String dayStr = null;
        	String monthStr = null;
        	if(0 == reportType){
        		dayStr = date;
        	}else{
        		monthStr = date;
        	}
        	
			//生成表头
			Result<Object> objectResult = serviceTypeService.findAll();
			List<ServiceType> serviceType = (List<ServiceType>) objectResult.getData();
			List<List<ServiceInfo>> serviceInfos = new ArrayList<>();
			for (ServiceType type : serviceType) {
			    List<ServiceInfo> serviceInfosByType = serviceInfoMapper.selectServiceByType(type.getId());
			    serviceInfos.add(serviceInfosByType);
			    type.setServiceInfos(serviceInfosByType);
			}
			List<ChannelInfo> channelInfos = channelInfoMapper.findAll();
			List<ServiceLogForUserServiceData> serviceLogForUserServiceData=serviceLogMapper.queryServiceLogByDate(dayStr,monthStr);
			List<List<DataJSON>> dataJSONS = new ArrayList<>();//内容数据
			ArrayList<DataJSON> dataContent = new ArrayList<>();
			DataJSON dataJSON = new DataJSON();
			dataJSON.setName("渠道名称");
			dataContent.add(dataJSON);
			for (ServiceType type : serviceType) {
			    for (int i = 0; i < serviceInfos.size(); i++) {
			        for (int j = 0; j < serviceInfos.get(i).size(); j++) {
			            if (serviceInfos.get(i).get(j).getServiceType().equals(type.getId())) {
			                DataJSON dataJSON_service = new DataJSON();
			                dataJSON_service.setName(serviceInfos.get(i).get(j).getServiceName());
			                dataJSON_service.setId(serviceInfos.get(i).get(j).getServiceEncode());
			                dataJSON_service.setContent("0");
			                dataContent.add(dataJSON_service);
			            }
			        }
			    }
			    DataJSON dataJSON_sum = new DataJSON();
			    dataJSON_sum.setName("合计");
			    dataJSON_sum.setContent("0");
			    dataContent.add(dataJSON_sum);
			}
			ChannelInfo channel = new ChannelInfo();
			channel.setChannelName("合计");
			channelInfos.add(channel);
			//表内容
			List<Integer> serviceSums = new ArrayList<>();//服务合计数据
			for (int i = 0; i < dataContent.size() - 1; i++) {
			    serviceSums.add(0);
			}
			for (ChannelInfo channelInfo : channelInfos) {
			    ArrayList<DataJSON> dataContentForUser = new ArrayList<>();
			    for (DataJSON d : dataContent) {
			        DataJSON da = new DataJSON();
			        da.setContent(d.getContent());
			        da.setId(d.getId());
			        da.setName(d.getName());
			        dataContentForUser.add(da);
			    }
			    int sum = 0;
			    for (int i = 0; i < dataContentForUser.size(); i++) {
			        if (i == 0) {//第一列写入用户名称（固定列）
			            dataContentForUser.get(i).setContent(channelInfo.getChannelName());
			            dataContentForUser.get(i).setId(channelInfo.getChannelCode());
			        } else {
			            if (channelInfo.getId() == null) {//若id为null，则此行数据为合计数据
			                dataContentForUser.get(i).setContent("" + serviceSums.get(i - 1));
			            } else {
			                if (dataContentForUser.get(i).getId() != null) {
			                    for (ServiceLogForUserServiceData slfusd : serviceLogForUserServiceData) {
			                        if (dataContentForUser.get(i).getId() != null) {
			                            if (dataContentForUser.get(i).getId().equals(slfusd.getServiceId())) {
			                                if (slfusd.getChannelEncode().equals(channelInfo.getChannelCode())) {
			                                    dataContentForUser.get(i).setContent("" + slfusd.getCount());
			                                    serviceSums.set(i - 1, serviceSums.get(i - 1) + slfusd.getCount());
			                                    sum = sum + slfusd.getCount();
			                                }
			                            }
			                        }
			                    }
			                } else {//只有合计列 id为空
			                    dataContentForUser.get(i).setContent("" + sum);
			                    serviceSums.set(i - 1, serviceSums.get(i - 1) + sum);
			                    sum = 0;//合计一组结束 sum清零
			                }
			            }

			        }
			    }
			    dataJSONS.add(dataContentForUser);
			}
			List<List<Object>> list = new ArrayList<>();
			for (List<DataJSON> datas : dataJSONS) {
			    List<Object> row = new ArrayList<>();
			    for (int i = 0; i < datas.size(); i++) {
			        row.add(datas.get(i).getContent());
			    }
			    list.add(row);
			}

			ResultData resultData = new ResultData();
			resultData.setTableTitle(serviceType);
			resultData.setTableContent(dataJSONS);
			Report report = new Report();
			report.setId(UUIDGenerator.getUUID());
			report.setType(4);
			report.setDate(date);
			report.setContent(JSON.toJSONString(resultData, SerializerFeature.DisableCircularReferenceDetect));
			insertOrUpdate(report);
		} catch (Exception e) {
			logger.error(e.getMessage());
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
