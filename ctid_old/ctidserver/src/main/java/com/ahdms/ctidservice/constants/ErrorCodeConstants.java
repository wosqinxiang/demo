/**
 * <p>Title: ErrorCodeConstants.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月21日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.constants;

/**
 * <p>Title: ErrorCodeConstants</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月21日  
 */
public class ErrorCodeConstants {
	
	//网证认证错误码
	public static final int AUTH_CTID_EXPIRE_CODE = 0x00A19001; //网证数据过期 (下载超过6个月)
	public static final String AUTH_CTID_EXPIRE_MESSAGE = "网证数据过期";
	
	public static final int AUTH_USERCARD_EXPIRE_CODE = 0x00A19002; //身份证有效期过期
	public static final String AUTH_USERCARD_EXPIRE_MESSAGE = "身份证过期";
	
	public static final int AUTH_CTID_CANCEL_CODE = 0x00A19003; //网证已被注销
	public static final String AUTH_CTID_CANCEL_MESSAGE = "网证已被注销";
	
	public static final int AUTH_CTID_BLOCKED_CODE = 0x00A19004; //网证已被冻结
	public static final String AUTH_CTID_BLOCKED_MESSAGE = "网证已被冻结";
	
	public static final int AUTH_CTID_DATA_CODE = 0x00A19005; //网证数据有误
	public static final String AUTH_CTID_DATA_MESSAGE = "网证数据有误";

	public static final int AUTH_CTID_REDOWN_CODE = 0x00A19006;
	public static final String AUTH_CTID_REDOWN_MESSAGE = "网证不在网证库，或者同款APP重新下载了该网证";
	
	public static final int AUTH_CTID_DEFAULT_CODE = 0x00A19000;
	public static final String AUTH_CTID_DEFAULT_MESSAGE = "网证验证失败";
	
	
	//人像比对错误码
	public static final int AUTH_FACE_DEFAULT_CODE = 0x00A19100;
	public static final String AUTH_FACE_DEFAULT_MESSAGE = "人像比对失败";
	
	public static final int AUTH_FACE_DATA_CODE = 0x00A19101; //人像数据有误
	public static final String AUTH_FACE_DATA_MESSAGE = "人像数据有误";
	
	public static final int AUTH_FACE_COMPARE_CODE = 0x00A19102; //人像比对失败，非同一人
	public static final String AUTH_FACE_COMPARE_MESSAGE = "人像比对失败，非同一人";
	
	public static final int AUTH_FACE_NOPIC_CODE = 0x00A19103; //数据库中无该人像信息
	public static final String AUTH_FACE_NOPIC_MESSAGE = "数据库中无该人像信息";
	
	public static final int AUTH_FACE_TYPE_CODE = 0x00A19104; //非JPEG格式的图像
	public static final String AUTH_FACE_TYPE_MESSAGE = "非JPEG格式的图像";
	
	public static final int AUTH_FACE_QUALITY_CODE = 0x00A19105; //上传照片质量不合格
	public static final String AUTH_FACE_QUALITY_MESSAGE = "上传照片质量不合格";
	
	//身份信息匹配比对错误码
	public static final int AUTH_CARD_DEFAULT_CODE = 0x00A19400;
	public static final String AUTH_CARD_DEFAULT_MESSAGE = "身份信息比对失败";
	
	public static final int AUTH_CARD_NONE_CODE = 0x00A19401;
	public static final String AUTH_CARD_NONE_MESSAGE = "身份信息无效";
	
	public static final int AUTH_CARD_DATA_CODE = 0x00A19402;
	public static final String AUTH_CARD_DATA_MESSAGE = "参数错误";
	
	public static final int AUTH_CARD_API_CODE = 0x00A19403;
	public static final String AUTH_CARD_API_MESSAGE = "认证接口调用异常";
	
	public static final int AUTH_CARD_USEUP_CODE = 0x00A19404;
	public static final String AUTH_CARD_USEUP_MESSAGE = "访问次数耗尽";
	
	
	//网证下载错误码
	public static final int DOWN_CTID_DEFAULT_CODE = 0x00A19200;
	public static final String DOWN_CTID_DEFAULT_MESSAGE = "网证下载失败";
	
	public static final int DOWN_CTID_BLOCK_CODE = 0x00A19004; //网证已冻结
	public static final String DOWN_CTID_BLOCK_MESSAGE = "网证已冻结";
	
	public static final int DOWN_CTID_CANCEL_CODE = 0x00A19003; //网证已注销
	public static final String DOWN_CTID_CANCEL_MESSAGE = "网证已注销";
	
	public static final int DOWN_CTID_AUTHCODE_CODE = 0x00A19201; //认证码错误
	public static final String DOWN_CTID_AUTHCODE_MESSAGE = "认证码错误";
	
	public static final int DOWN_CTID_IDCARD_CODE = 0x00A19202; //身份信息错误或未开通网证
	public static final String DOWN_CTID_IDCARD_MESSAGE = "身份信息错误或未开通网证";
	
	public static final int DOWN_CTID_NONE_CODE = 0x00A19203; //网证不在库中
	public static final String DOWN_CTID_NONE_MESSAGE = "网证不在库中";
	
	
	//二维码赋码错误码
	public static final int CREATE_QRCODE_DEFAULT_CODE = 0x00A19300;
	public static final String CREATE_QRCODE_DEFAULT_MESSAGE = "二维码赋码失败";
	
	public static final int CREATE_QRCODE_NONE_CODE = 0x00A19301;
	public static final String CREATE_QRCODE_NONE_MESSAGE = "网证不存在";
	
	public static final int CREATE_QRCODE_CANCEL_CODE = 0x00A19003;
	public static final String CREATE_QRCODE_CANCEL_MESSAGE = "网证已注销";
	
	public static final int CREATE_QRCODE_BLOCK_CODE = 0x00A19004;
	public static final String CREATE_QRCODE_BLOCK_MESSAGE = "网证已冻结";
	
	public static final int CREATE_QRCODE_EXPIRY_CODE = 0x00A19302;
	public static final String CREATE_QRCODE_EXPIRY_MESSAGE = "网证已失效";
	
	
}
