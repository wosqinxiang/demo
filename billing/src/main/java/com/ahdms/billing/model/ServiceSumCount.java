package com.ahdms.billing.model;


import java.util.List;

/**
 * 用户服务、渠道剩余次数统计bean
 */
public class ServiceSumCount {
    private String id;
    private int count;
    private List<ServiceSumCount> chennels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ServiceSumCount> getChennels() {
        return chennels;
    }

    public void setChennels(List<ServiceSumCount> chennels) {
        this.chennels = chennels;
    }
}
