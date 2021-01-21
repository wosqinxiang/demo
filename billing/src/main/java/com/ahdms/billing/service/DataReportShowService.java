package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;

public interface DataReportShowService {
	
	Result showTotalAll(String date,int type,String username);
	
	Result showByServiceAndChannel(String date,int type,String username,String serviceCode,String channelCode);

    Result showUserServiceCount(String date, String username);
}
