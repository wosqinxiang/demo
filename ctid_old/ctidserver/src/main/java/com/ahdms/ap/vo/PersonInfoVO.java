/**
 * Created on 2019年8月13日 by liuyipin
 */
package com.ahdms.ap.vo;

/**
 * @Title
 * @Description
 * @Copyright
 *            <p>
 *            Copyright (c) 2015
 *            </p>
 * @Company
 *          <p>
 *          迪曼森信息科技有限公司 Co., Ltd.
 *          </p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class PersonInfoVO {

	private String openid;

	private String idcard;

	private String name;
	
	private int isCtid;
	
	
	public int getIsCtid() {
		return isCtid;
	}

	public void setIsCtid(int isCtid) {
		this.isCtid = isCtid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
