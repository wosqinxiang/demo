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
public class AddUserVO implements Serializable {

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
     * 用户(微信)识别码
     */
    @ApiModelProperty(value = "用户(微信)识别码", required = true)
    private String openID;
    /**
     * 用户来源(1:微信 2：支付宝 3：生态app 4：其他 )
     */
    @ApiModelProperty(value = "用户类型", required = true)
    private int type;
    

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
 
    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

   
}
