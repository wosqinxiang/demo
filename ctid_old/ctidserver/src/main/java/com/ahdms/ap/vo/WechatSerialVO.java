/**
 * Created on 2019年12月18日 by liuyipin
 */
package com.ahdms.ap.vo;

/**
 * @Title 
 * @Description 微信小程序端获取随机数的入参vo
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class WechatSerialVO {
	private String socketId;
	private String openid;
	private int authType; 
	public String getSocketId() {
		return socketId;
	}
	public void setSocketId(String socketId) {
		this.socketId = socketId;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public int getAuthType() {
		return authType;
	}
	public void setAuthType(int authType) {
		this.authType = authType;
	}
	 
	
	
}

