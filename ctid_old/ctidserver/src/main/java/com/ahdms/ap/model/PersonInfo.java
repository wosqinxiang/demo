package com.ahdms.ap.model;

import java.util.Date;

public class PersonInfo {
   
	private String id;

    private Integer infoSource;

    private String openid;

    private Date createTime;

    private String idcard;

    private String ctidInfo;

    private String name;

    private String tel;

    private Integer isCtid;

    private String pic;
    
    public PersonInfo(String id, Integer infoSource, String openid, Date createTime, String idcard, String ctidInfo,
			String name, String tel, Integer isCtid, String pic) {
		super();
		this.id = id;
		this.infoSource = infoSource;
		this.openid = openid;
		this.createTime = createTime;
		this.idcard = idcard;
		this.ctidInfo = ctidInfo;
		this.name = name;
		this.tel = tel;
		this.isCtid = isCtid;
		this.pic = pic;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(Integer infoSource) {
        this.infoSource = infoSource;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getCtidInfo() {
        return ctidInfo;
    }

    public void setCtidInfo(String ctidInfo) {
        this.ctidInfo = ctidInfo == null ? null : ctidInfo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Integer getIsCtid() {
        return isCtid;
    }

    public void setIsCtid(Integer isCtid) {
        this.isCtid = isCtid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

	public PersonInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}