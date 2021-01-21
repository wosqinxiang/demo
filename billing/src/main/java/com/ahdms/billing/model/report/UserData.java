package com.ahdms.billing.model.report;

import com.ahdms.billing.model.ServiceSumCount;
import com.ahdms.billing.model.UserInfo;

import java.util.List;

public class UserData extends UserInfo {
    private List<ServiceLogForUserServiceData> serviceLogForUserServiceData;

    public List<ServiceLogForUserServiceData> getServiceLogForUserServiceData() {
        return serviceLogForUserServiceData;
    }

    public void setServiceLogForUserServiceData(List<ServiceLogForUserServiceData> serviceLogForUserServiceData) {
        this.serviceLogForUserServiceData = serviceLogForUserServiceData;
    }
    //    private List<ServiceSumCount> serviceSumCounts;
//
//    public List<ServiceSumCount> getServiceSumCounts() {
//        return serviceSumCounts;
//    }
//
//    public void setServiceSumCounts(List<ServiceSumCount> serviceSumCounts) {
//        this.serviceSumCounts = serviceSumCounts;
//    }
}
