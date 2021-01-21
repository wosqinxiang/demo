package com.ahdms.billing.model;

import java.util.List;

public class UserInfoForService extends UserInfo {
    private List<ServiceSumCount> services;

    public List<ServiceSumCount> getServices() {
        return services;
    }

    public void setServices(List<ServiceSumCount> services) {
        this.services = services;
    }
}
