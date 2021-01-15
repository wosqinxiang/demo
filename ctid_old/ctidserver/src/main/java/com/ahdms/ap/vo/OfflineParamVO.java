package com.ahdms.ap.vo;

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
public class OfflineParamVO implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idNum;

    /**
     * 业务流水号
     */
    private String serialNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
