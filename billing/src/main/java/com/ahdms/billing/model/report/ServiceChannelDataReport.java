package com.ahdms.billing.model.report;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;

@HeadRowHeight(25) //表头高度
@ColumnWidth(20) //列宽
@ContentRowHeight(25) //行高
public class ServiceChannelDataReport extends BaseRowModel {
	
	@ColumnWidth(8) //宽度为8,覆盖上面的宽度20
    @ExcelProperty(value = {"管理日志统计报表","序号"},index = 0)
    private String exxxNO;

    @ExcelProperty(value = {"管理日志统计报表","时间"},index = 1,format = "yyyy-MM-dd HH:mm:ss")
    private Date exxxTime;

    @ExcelProperty(value = {"管理日志统计报表","操作员"},index = 2)
    private String operator;

    @ExcelProperty(value = {"管理日志统计报表","操作内容"},index = 3)
    private String content;

    
}
