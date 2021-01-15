/**
 * Created on 2019年8月2日 by liuyipin
 */
package com.ahdms.ap.vo;

import java.io.Serializable;
import java.util.Date;

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
public class OnlineVO implements Serializable{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	
	 private String serialNum;
	 
	 private Integer authResult;
	 
	 private String name;

	 private String idcard;
	 
	 private String verifyDate;
	 
	 private String pic;
	 
	 private String serverName;
	 
	 private String location;
	 
	 private String authDesc;
	 
	 private Integer ctidType;
	 
	 private String authNum;
	 
	 private String token;
	 

	public String getAuthNum() {
		return authNum;
	}

	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getCtidType() {
		return ctidType;
	}

	public void setCtidType(Integer ctidType) {
		this.ctidType = ctidType;
	}

	public String getAuthDesc() {
		return authDesc;
	}

	public void setAuthDesc(String authDesc) {
		this.authDesc = authDesc;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(String verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Integer getAuthResult() {
		return authResult;
	}

	public void setAuthResult(Integer authResult) {
		this.authResult = authResult;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

 

	public OnlineVO(String serialNum, Integer authResult, String name, String idcard, String verifyDate) {
		super();
		this.serialNum = serialNum;
		this.authResult = authResult;
		this.name = name;
		this.idcard = idcard;
		this.verifyDate = verifyDate;
	}

	public OnlineVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
	 

}

