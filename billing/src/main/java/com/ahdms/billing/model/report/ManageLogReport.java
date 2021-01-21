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

public class ManageLogReport extends BaseRowModel {

    @ColumnWidth(8) //宽度为8,覆盖上面的宽度20
    @ExcelProperty(value = {"管理日志统计报表","序号"},index = 0)
    private String exxxNO;

    @ExcelProperty(value = {"管理日志统计报表","时间"},index = 1,format = "yyyy-MM-dd HH:mm:ss")
    private Date exxxTime;

    @ExcelProperty(value = {"管理日志统计报表","操作员"},index = 2)
    private String operator;

    @ExcelProperty(value = {"管理日志统计报表","操作内容"},index = 3)
    private String content;


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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
