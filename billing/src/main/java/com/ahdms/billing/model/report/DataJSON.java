package com.ahdms.billing.model.report;

public class DataJSON {
    private String id;//表头对应的服务id
    private String content;//内容、服务次数
    private String name;//表头名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
