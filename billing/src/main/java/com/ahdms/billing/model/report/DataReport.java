package com.ahdms.billing.model.report;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;


public class DataReport extends BaseRowModel {
    @ExcelProperty(value = {"2020年3月客户统计报表","客户名称","客户名称"},index = 0)
    private String customerName;

    @ExcelProperty(value = {"2020年3月客户统计报表","身份认证服务剩余次数","二项信息"},index = 1)
    private int exxxCount;
    @ExcelProperty(value = {"2020年3月客户统计报表","身份认证服务剩余次数","二项信息+人像"},index = 2)
    private int exxxrxServiceCount;
    @ExcelProperty(value = {"2020年3月客户统计报表","身份认证服务剩余次数","网证+人像"},index = 3)
    private int wzrxServiceCount;
    @ExcelProperty(value = {"2020年3月客户统计报表","身份认证服务剩余次数","网证下载"},index = 4)
    private int wzxzServiceCount;
    @ExcelProperty(value = {"2020年3月客户统计报表","总计","总计"},index = 5)
    private int sfrzServiceCount;

    @ExcelProperty(value = {"2020年3月客户统计报表","电子认证服务剩余次数","可信标识申请"},index = 6)
    private int kxbssqServiceCount;
    @ExcelProperty(value = {"2020年3月客户统计报表","电子认证服务剩余次数","可信标识更新"},index = 7)
    private int kxbsgxServiceCount;
    @ExcelProperty(value = {"2020年3月客户统计报表","电子认证服务剩余次数","电子签名"},index = 8)
    private int dzqmServiceCount;
    @ExcelProperty(value = {"2020年3月客户统计报表","总计","总计"},index = 9)
    private int dzrzServiceCount;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getExxxCount() {
        return exxxCount;
    }

    public void setExxxCount(int exxxCount) {
        this.exxxCount = exxxCount;
    }

    public int getExxxrxServiceCount() {
        return exxxrxServiceCount;
    }

    public void setExxxrxServiceCount(int exxxrxServiceCount) {
        this.exxxrxServiceCount = exxxrxServiceCount;
    }

    public int getWzrxServiceCount() {
        return wzrxServiceCount;
    }

    public void setWzrxServiceCount(int wzrxServiceCount) {
        this.wzrxServiceCount = wzrxServiceCount;
    }

    public int getWzxzServiceCount() {
        return wzxzServiceCount;
    }

    public void setWzxzServiceCount(int wzxzServiceCount) {
        this.wzxzServiceCount = wzxzServiceCount;
    }

    public int getSfrzServiceCount() {
        return sfrzServiceCount;
    }

    public void setSfrzServiceCount(int sfrzServiceCount) {
        this.sfrzServiceCount = sfrzServiceCount;
    }

    public int getKxbssqServiceCount() {
        return kxbssqServiceCount;
    }

    public void setKxbssqServiceCount(int kxbssqServiceCount) {
        this.kxbssqServiceCount = kxbssqServiceCount;
    }

    public int getKxbsgxServiceCount() {
        return kxbsgxServiceCount;
    }

    public void setKxbsgxServiceCount(int kxbsgxServiceCount) {
        this.kxbsgxServiceCount = kxbsgxServiceCount;
    }

    public int getDzqmServiceCount() {
        return dzqmServiceCount;
    }

    public void setDzqmServiceCount(int dzqmServiceCount) {
        this.dzqmServiceCount = dzqmServiceCount;
    }

    public int getDzrzServiceCount() {
        return dzrzServiceCount;
    }

    public void setDzrzServiceCount(int dzrzServiceCount) {
        this.dzrzServiceCount = dzrzServiceCount;
    }
}
