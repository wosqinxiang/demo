/**
 * Created on 2020年5月9日 by liuyipin
 */
package com.ahdms.billing.common;
/**
 * @Title 渠道信息
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public enum ChannelInfoEnum {

	BOX("0001","身份宝盒"),
	API("0002","API"),
	APP_SDK("0003","APP SDK"),
	WX_FTOFVerify("0004","面对面认证"),
	WX_LDVerify("0005","远程分享认证"),
	WX_REGISTER("0006","小程序注册"),
	WX_LOGIN("0007","小程序登录"),
	WX_SDK("0008","微信SDK");

	private String code;
	private String desc;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	private ChannelInfoEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	 

}

