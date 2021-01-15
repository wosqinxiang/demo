/**
 * Created on 2019年11月21日 by liuyipin
 */
package com.ahdms.ap.model;

import java.io.Serializable;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class AccessToken implements Serializable{
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	private String openId;
	
	private String createTime;
	
	private String ip ;
	
	private String serialNum;

	 

	public AccessToken() {
		super();
		// TODO Auto-generated constructor stub
	}
 
	public AccessToken(String openId, String createTime, String ip, String serialNum) {
		super();
		this.openId = openId;
		this.createTime = createTime;
		this.ip = ip;
		this.serialNum = serialNum;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	} 

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	

}

