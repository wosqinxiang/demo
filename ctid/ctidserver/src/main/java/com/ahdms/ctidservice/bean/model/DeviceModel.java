/**
 * Created on 2018年7月9日 by liuyipin
 */
package com.ahdms.ctidservice.bean.model;

import java.io.Serializable;
import java.sql.Date;

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
public class DeviceModel implements Serializable{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String deviceName;
	private String identity;
	private byte[] certValue;
	private int status;
	private byte[] pkmValue;
	private String createId;
	private Date createTime;
	private String lastOpId;
	private Date lastOpTime;
	private String deviceDesc;
	private String appId;
	
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getDeviceDesc() {
		return deviceDesc;
	}
	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public byte[] getCertValue() {
		return certValue;
	}
	public void setCertValue(byte[] certValue) {
		this.certValue = certValue;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public byte[] getPkmValue() {
		return pkmValue;
	}
	public void setPkmValue(byte[] pkmValue) {
		this.pkmValue = pkmValue;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getLastOpId() {
		return lastOpId;
	}
	public void setLastOpId(String lastOpId) {
		this.lastOpId = lastOpId;
	}
	public Date getLastOpTime() {
		return lastOpTime;
	}
	public void setLastOpTime(Date lastOpTime) {
		this.lastOpTime = lastOpTime;
	}
	
}

