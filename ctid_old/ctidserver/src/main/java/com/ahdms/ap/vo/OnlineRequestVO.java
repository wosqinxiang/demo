package com.ahdms.ap.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author
 * @version 1.0
 * @Title
 * @Description
 * @Copyright &lt;p&gt;Copyright (c) 2019&lt;/p&gt;
 * @Company &lt;p&gt;迪曼森信息科技有限公司 Co., Ltd.&lt;/p&gt;
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class OnlineRequestVO implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证", required = true)
    private String idCard;
    /**
     * 人像信息
     */
    @ApiModelProperty(value = "人像信息", required = true)
    private String facePic;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    /**
     * 数据来源(1:微信 2：支付宝 3：生态app 4：其他)
     */
    @ApiModelProperty(value = "信息来源(1:微信 2：支付宝 3：生态app 4：其他)", required = true)
    private Integer type;
    /**
     * 用户(微信)识别码
     */
    @ApiModelProperty(value = "用户(微信)识别码", required = true)
    private String openID;
    /**
     * 业务流水号
     */
    @ApiModelProperty(value = "业务流水号", required = true)
    private String serialNum;
    /**
     * 位置信息
     */
    @ApiModelProperty(value = "位置信息", required = false)
    private String location;

    public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getFacePic() {
        return facePic;
    }

    public void setFacePic(String facePic) {
        this.facePic = facePic;
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
