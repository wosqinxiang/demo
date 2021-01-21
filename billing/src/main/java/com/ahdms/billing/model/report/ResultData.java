package com.ahdms.billing.model.report;

import com.ahdms.billing.model.ServiceType;

import java.util.List;

public class ResultData {
    private String date;
    private String type;
    private List<ServiceType> tableTitle;
    private List<List<DataJSON>> tableContent;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ServiceType> getTableTitle() {
        return tableTitle;
    }

    public void setTableTitle(List<ServiceType> tableTitle) {
        this.tableTitle = tableTitle;
    }

    public List<List<DataJSON>> getTableContent() {
        return tableContent;
    }

    public void setTableContent(List<List<DataJSON>> tableContent) {
        this.tableContent = tableContent;
    }
}
