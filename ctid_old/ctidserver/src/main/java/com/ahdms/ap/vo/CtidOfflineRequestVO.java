package com.ahdms.ap.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

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
public class CtidOfflineRequestVO implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

   
    @ApiModelProperty(value = "人像信息", required = true)
    private String facePic;

    @ApiModelProperty(value = "用户(微信)识别码", required = true)
    private String openID;

    @ApiModelProperty(value = "业务流水号", required = true)
    private String serialNum;

    @ApiModelProperty(value = "信息来源(1:微信 2：支付宝 3：生态app 4：其他)", required = true)
    private Integer type;
 
    @ApiModelProperty(value = "网证信息", required = true)
    private String ctid; 
    /**
     * 网证随机数
     */
    @ApiModelProperty(value = "网证随机数", required = true)
    private String businessSerialNumber;

    public String getBusinessSerialNumber() {
		return businessSerialNumber;
	}

	public void setBusinessSerialNumber(String businessSerialNumber) {
		this.businessSerialNumber = businessSerialNumber;
	}
    
    public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    } 

    public String getFacePic() {
        return facePic;
    }

    public void setFacePic(String facePic) {
        this.facePic = facePic;
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
