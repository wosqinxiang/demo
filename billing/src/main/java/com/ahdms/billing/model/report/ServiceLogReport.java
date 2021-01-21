package com.ahdms.billing.model.report;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

@HeadRowHeight(25) //表头高度
@ColumnWidth(20) //列宽
@ContentRowHeight(25) //行高

public class ServiceLogReport extends BaseRowModel {

    @ColumnWidth(8) //宽度为8,覆盖上面的宽度20
    @ExcelProperty(value = {"业务日志统计报表","序号"},index = 0)
    private String exxxNO;

    @ExcelProperty(value = {"业务日志统计报表","时间"},index = 1, format = "yyyy-MM-dd HH:mm:ss")
    private Date exxxTime;

    @ExcelProperty(value = {"业务日志统计报表","客户名称"},index = 2)
    private String customerName;

    @ExcelProperty(value = {"业务日志统计报表","服务类型"},index = 3)
    private String serviceType;

    @ExcelProperty(value = {"业务日志统计报表","服务名称"},index = 4)
    private String serviceName;

    @ExcelProperty(value = {"业务日志统计报表","渠道名称"},index = 5)
    private String channelName;


    public String getExxxNO() {
        return exxxNO;
    }

    public void setExxxNO(String exxxNO) {
        this.exxxNO = exxxNO;
    }

    public Date getExxxTime() {
        return exxxTime;
    }

    public void setExxxTime(Date exxxTime) {
        this.exxxTime = exxxTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
