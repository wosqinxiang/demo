package com.ahdms.ctidservice.common;

public enum DownloadModeEnum {
	
	M0X10("0x10","不读卡模式"),M0X01("0x01","读卡模式"),M0X02("0x02","读卡加人像"),M0X03("0x03","读卡加认证码");
	
	private DownloadModeEnum(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	private String name ;
	
	private String desc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
