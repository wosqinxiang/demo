package com.ahdms.billing.model;

public class Report {
    private String id;

    private String date;
    
    //1.按服务类型统计所有  2.按服务类型统计用户的使用次数 3.按渠道类型统计所有 4.按渠道类型统计用户的使用次数
    private Integer type;  

    private String content;
    
    private String username;
    
    public Report() {
	}

	public Report(String id, String date, Integer type, String content, String username) {
		super();
		this.id = id;
		this.date = date;
		this.type = type;
		this.content = content;
		this.username = username;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
}