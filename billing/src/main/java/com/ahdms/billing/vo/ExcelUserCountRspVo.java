package com.ahdms.billing.vo;

import java.util.List;

/**
 * @author qinxiang
 * @date 2021-01-19 15:55
 */
public class ExcelUserCountRspVo {

    private List<List<DataReportBaseVO>> tabelData;
    private List<ExcelHeaderVo> tableHeader;

    public ExcelUserCountRspVo() {
    }

    public ExcelUserCountRspVo(List<List<DataReportBaseVO>> tabelData, List<ExcelHeaderVo> tableHeader) {
        this.tabelData = tabelData;
        this.tableHeader = tableHeader;
    }

    public List<List<DataReportBaseVO>> getTabelData() {
        return tabelData;
    }

    public void setTabelData(List<List<DataReportBaseVO>> tabelData) {
        this.tabelData = tabelData;
    }

    public List<ExcelHeaderVo> getTableHeader() {
        return tableHeader;
    }

    public void setTableHeader(List<ExcelHeaderVo> tableHeader) {
        this.tableHeader = tableHeader;
    }
}
