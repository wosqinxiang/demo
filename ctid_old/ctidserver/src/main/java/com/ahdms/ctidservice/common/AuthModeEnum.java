package com.ahdms.ctidservice.common;

public enum AuthModeEnum {
	
	M0X0F("0x0f","DN+人像+网证+安全策略"),M0X4F("0x4f","DN+人像+网证+安全策略+2项信息"),
	M0X1F("0x1f","DN+人像+网证+安全策略+4项信息"),M0X13("0x13","DN+人像+4项信息"),
	M0X1D("0x1d","DN+网证+安全策略+4项信息"),M0X06("0x06","人像+网证"),
	M0X16("0x16","人像+网证+4项信息"),M0X10("0x10","4项信息"),M0X12("0x10","人像+ 4项信息"),
	M0X40("0x40","2项信息"),M0X42("0x42","人像+2项信息");
	
	private AuthModeEnum(String name, String desc) {
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


	public boolean equals(String str) {
		return name.equals(str);
	}

}
