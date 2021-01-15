/**
 * <p>Title: CtidConstants.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang
 * @date 2019年9月16日
 * @version 1.0
 */
package com.ahdms.ctidservice.contants;

/**
 * <p>Title: CtidConstants</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年9月16日
 */
public class CtidConstants {

	public static final int CTID_RESULT_SUCCESS_CODE = 0;
	//ctid被吊销返回code
	public static final int CTID_RESULT_REVOKED_CODE = 2;


	// 二维码是否生成图片
	public static final Integer CODE_IS_PIC = 1; // 1. 二维码码图流

	public static final Integer CODE_NOT_PIC = 0; // 不生成图片

	public static final String WX_APPID = "0003";

	// 调用PC端控件获取ID验证数据 的type值
	public static final String NO_CARD_AUTH_TYPE = "0"; //无卡认证
	public static final String USE_CARD_AUTH_TYPE = "1"; //读卡认证
	public static final String USE_CARD_DOWN_TYPE = "2";  //读卡下载
	public static final String NO_CARD_DOWN_TYPE = "3";  //无卡下载

	//获取二维码申请数据 类型
	public static final String CREATE_QRCODE_TYPE = "0"; //二维码赋码申请类型
	public static final String AUTH_QRCODE_TYPE = "1";   //二维码验码申请类型

	public static final String AUTH_0X40_MODE = "0x40";
	public static final String AUTH_0X42_MODE = "0x42";
	public static final String AUTH_0X06_MODE = "0x06";

	public static final String DOWN_0X10_MODE = "0x10";
	public static final String DOWN_0X13_MODE = "0x13";
	public static final String DOWN_0X14_MODE = "0x14";

	public static final Integer AUTH_RECORD_TYPE_SDK = 6;

	public static final Integer AUTH_RECORD_WX_SDK = 12;

	public static final String CIPHER_CONFIG_KEY = "cipherConfigKey";

}
