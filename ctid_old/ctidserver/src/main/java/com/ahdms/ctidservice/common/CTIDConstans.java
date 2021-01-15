/**
 * Created on 2018年7月4日 by liuyipin
 */
package com.ahdms.ctidservice.common;

/**
 * @author liuyipin
 * @version 1.0
 * @Title
 * @Description
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class CTIDConstans {

    //通用状态
    public static final Integer STATUS_VALID = 0;
    public static final Integer STATUS_INVALID = 1;

    public static final String ROLE_ADMIN = "ROLE_ADMIN"; //管理员

    public static final Integer INIT_ADMIN = 0; //管理员
    public static final Integer NORMAL_ADMIN = 1; //管理员

    //返回状态码
    //返回码配置开始---------------------------------------------------------------------------------------------
    public static final String RETURN_CODE_SUCCESS = "0"; //成功
    public static final String RETURN_CODE_ERROR = "1"; //失败

    //设备类型:1pkm,2本设备
    public static final Integer CTID_TYPE_PKM = 1;
    public static final Integer CTID_TYPE_DEVICE = 2;

    //OCSP返回状态码证书状态 0：有效；1：无效；2：未知。
    public static final Integer OCSP_STATUS_EFFECT = 0;
    public static final Integer OCSP_STATUS_INVALID = 1;
    public static final Integer OCSP_STATUS_UNKNOWN = 2;

    // 登录
    public static final String LOG_TYPE_LOGIN = "LOG_TYPE_LOGIN";


    //网证状态
    public static final Integer CTID_STATUS_VALID = 0; //正常
    public static final Integer CTID_STATUS_REVOKED = 1; //吊销
    public static final Integer CTID_STATUS_HOLD = 2;  //挂起
    public static final Integer CTID_STATUS_EXPIRED = 3; //过期

    public static final Integer cardValid = 0; //正常
    public static final Integer cardRevoke = 1; //吊销
    public static final Integer cardHold = 2;  //挂起
    public static final Integer cardUnHold = 3; //解挂

    //token在redis中过期时间
    public static final int TOKEN_EXPIRE_SECONDS = 300;

}

