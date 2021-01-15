/**
 * Created on 2019年8月2日 by liuyipin
 */
package com.ahdms.ctidservice.contants;

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
public class Contents {

    //返回状态码
    public static final int RETURN_CODE_SUCCESS = 0; //成功
    public static final int RETURN_CODE_ERROR = 1; //失败
    

    public static final int RETURN_CODE_TOKEN_ERROR = 100; //token获取失败

    public static final int AUTH_TYPE_ONLINE = 1; //在线
    public static final int AUTH_TYPE_OFFLINE = 2; //离线
    public static final int AUTH_TYPE_SIMPLE = 3; //便捷
    public static final int AUTH_TYPE_API = 4; //API
    public static final int AUTH_TYPE_ADDUSER = 5; //添加用户  
    public static final int AUTH_TYPE_APPVERIFY = 6; //APP认证 
    public static final int AUTH_TYPE_USERLOGIN = 7; //用户登录
    public static final int AUTH_TYPE_FTF = 8; //面对面认证 
    public static final int AUTH_TYPE_TRANSPORT = 9; //远程分享认证
    public static final int AUTH_TYPE_PLAGUE = 10; //疫情注册居民用户
    public static final int AUTH_TYPE_WPOS = 11; //wpos认证

    public static final int INFO_SOURCE_WECHAT = 1; //微信
    public static final int INFO_SOURCE_ALIPAY = 2; //支付宝
    public static final int INFO_SOURCE_APP = 3; //生态APP
    public static final int INFO_SOURCE_OTHER = 4; //其他 
    

    public static final int AUTH_BIZ_TYPE_ONLINE = 1; //在线认证
    public static final int AUTH_BIZ_TYPE_FTF = 2; //面对面认证
    public static final int AUTH_BIZ_TYPE_TRANSPORT = 3; //远程分享认证
    public static final int AUTH_BIZ_TYPE_WPOS = 4; //远程分享认证


    public static final int AUTH_RESULT_TRUE = 0; //成功
    public static final int AUTH_RESULT_FALSE = 1; //失败

    public static final int AUTH_OBJECT_SELF = 1; //认证本人
    public static final int AUTH_OBJECT_OTHER = 2; //认证对方
    
    public static final int SERVER_LOCATION_TRUE = 0; //账号需要位置信息
    public static final int SERVER_LOCATION_FALSE = 1; //账号不需要位置信息

    /**
     * 是否回调 0：是
     */
    public static final int CALL_BACK_TRUE  = 0;
    /**
     * 是否回调 1：否
     */
    public static final int CALL_BACK_FALSE = 1;

    /**
     * 是否开通网证 0：是
     */
    public static final int IS_CTID_TRUE  = 0;
    /**
     * 是否开通网证 1：否
     */
    public static final int IS_CTID_FALSE = 1;

    /**
     * 使用网证模式 1:0x06(网证)
     */
    public static final int CTID_TYPE_ONE = 1;
    /**
     * 使用网证模式 2：0x40（二项信息）
     */
    public static final int CTID_TYPE_TWO  = 2;
    /**
     * 使用网证模式 3：0x42（二项信息+人脸）
     */
    public static final int CTID_TYPE_THREE = 3;

    /**
     * 设备类型编号 02：认证盒
     */
    public static final String CERT_BOX = "02";
    /**
     * 设备类型编号 03：PDA86手持枪
     */
    public static final String HAND_SET = "03";
    /**
     * 设备类型编号 04：PDA手机认证(安卓手持机)
     */
    public static final String PDA_MOBILE_AUTH = "04";
    


	public static final String  CACHE_SERVER_INFO = "CACHE_SERVER_INFO";
	
	public static final String  CACHE_SERVER_INFO_BYNAME = "CACHE_SERVER_INFO_BYNAME";


}

