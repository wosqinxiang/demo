package com.ahdms.ctidservice.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ahdms.ctidservice.constants.ErrorCodeConstants;

public class AuthResultFinder {

	private static final Map<String, AuthResult> dnSet = new HashMap<String, AuthResult>();
	private static final Map<String, AuthResult> pictureSet = new HashMap<String, AuthResult>();
	private static final Map<String, AuthResult> ctidSet = new HashMap<String, AuthResult>();
	private static final Map<String, AuthResult> securitySet = new HashMap<String, AuthResult>();
	private static final Map<String, AuthExceptionResult> exceptionSet = new HashMap<String, AuthExceptionResult>();

    static {
        //DN第一字符：身份信息匹配比对
    	dnSet.put("0", new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    					"0", "身份信息有效", "", ""));
    	dnSet.put("5",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"5", "身份信息无效", "1.身份信息不存在;2.身份信息不匹配", "检查身份信息"));
    	dnSet.put("6",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"6", "参数错误", "1.身份信息格式不正确;2.业务流水号非法", "1.检测身份信息;2.检查业务流水号"));
    	dnSet.put("7",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"7", "系统错误", "系统错误或服务异常", "联系系统对接人"));
    	dnSet.put("9", new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
				"9", "平台公安接口访问次数用尽", "平台公安接口访问次数用尽", ""));
    	dnSet.put("A",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"A", "穿网调用超时", "穿网调用超时", "重发"));
    	dnSet.put("E",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"E", "公安接口调用异常", "公安接口调用异常", "重发"));
    	dnSet.put("T",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"T", "公安接口调用超时", "公安接口调用超时", "重发"));
    	dnSet.put("X",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"X", "未执行", "", ""));
    	dnSet.put("Z",new AuthResult(AuthResult.AUTH_RET_TYPE_DN,
    			"Z", "该业务站点配置的公安网访问次数耗尽", "该站点配置的每日公安网访问次数耗尽", ""));
    	
    	//pic第二字符：人像比对
    	pictureSet.put("0",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"0", "同一人(人像加密)", "", ""));
    	pictureSet.put("1",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"1", "非同一人(人像加密)", "1、照片为本人，但是照片可能有角度、光线过暗、脸部区域有亮斑;2、照片为本人,但是该照片尺寸太小;", "1、请重新拍照;2、请传入合格照片"));
    	pictureSet.put("2",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"2", "疑似(人像加密)", "1、照片为本人，但是照片可能有角度、光线过暗、脸部区域有亮斑;2、照片为本人,但是该照片尺寸太小;3、两人长相类似", "1、请重新拍照;2、请传入合格照片"));
    	pictureSet.put("A",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"A", "数据库操作错误", "人像比对数据库操作异常", "联系公安部一所"));
    	pictureSet.put("B",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"B", "人像比对异常", "人像比对系统异常", "联系公安部一所"));
    	pictureSet.put("C",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"C", "输入参数错误", "1、业务流水号空或长度不是18位", "检查业务流水号"));
    	pictureSet.put("D",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"D", "无人像信息", "数据库中无该人像信息", "检查该人身份信息是否正确"));
    	pictureSet.put("E",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"E", "图像格式不支持", "非JPEG格式的图像", "检查传入的图像数据是否为 JPEG 格式"));
    	pictureSet.put("F",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"F", "待比对图像建模失败", "1、照片可能有角度、光线过暗、脸部区域有亮斑、模糊;2、照片尺寸太小", "1、请重新拍照;2、请传入合格照片"));
    	pictureSet.put("G",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"G", "现场照片质量不合格", "客户方上传照片质量不合格", "自检上传照片质量"));
    	pictureSet.put("H",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"H", "活体检测控件版本过期", "活体检测控件版本过期", "检查活体检测控件版本，联系一所，获取新的活体检测控件"));
    	pictureSet.put("I",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"I", "活体检测数据校验失败", "加密的人像图像数据校验失败", "数据不完整或加密方法错误"));
    	pictureSet.put("J",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"J", "现场照片小于5K字节", "现场照片小于5K字节", "检查传入的图像数据长度是否小于 5K 字节，重拍照片"));
    	pictureSet.put("K",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"K", "公安制证照片质量不佳", "库内照片质量不佳", "联系公安部一所"));
    	pictureSet.put("T",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"T", "人像引擎超时", "人像服务所有比对引擎同时超时或一个引擎超时，另一个引擎故障", "联系公安部一所"));
    	pictureSet.put("W",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"W", "系统其它错误", "该笔认证超时", "重试"));
    	pictureSet.put("X",new AuthResult(AuthResult.AUTH_RET_TYPE_PICTRUE,
				"X", "未执行", "", ""));
    	
    	//ctid 第三字符：网证及认证码匹配比对
    	ctidSet.put("0", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"0", "网证有效", "", ""));
    	ctidSet.put("1", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"1", "网证已经被注销", "该居民身份证网证已被注销", "重新开通网证"));
    	ctidSet.put("2", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"2", "该身份证没有申请居民身份证网证", "网证不在网证库，或者同款APP重新下载了该网证", "1.确认是否申请过网证;2.确认是否为同款APP中最新网证"));
    	ctidSet.put("3", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"3", "网证不匹配", "ID 验证数据中的网证数据与身份信息不匹配", "确认是否为本人网证"));
    	ctidSet.put("4", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"4", "认证码异常", "认证码和申请时不一致", "检查认证码"));
    	ctidSet.put("5", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"5", "网证已经被冻结", "已做冻结操作，未解冻", "检查网证状态"));
    	ctidSet.put("6", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"6", "身份信息不匹配", "1.简项信息不为空，且简项信息与数据库中数据不一致;2.复合信息不为空，且复合信息与数据库中数据不一致", "1.检查身份信息是否为空;2.检查简项信息;3.检查复合信息"));
    	ctidSet.put("7", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"7", "业务流水号异常", "业务流水号为空", "检查业务流水号参数"));
    	ctidSet.put("8", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"8", "网证已过期", "该网证已过期", "检查身份证有效期或网证下载日期(网证有效期=MIN{身份证有效期，网证下载日期+6 个月})"));
    	ctidSet.put("9", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"9", "输入参数错误", "ID 验证数据为空", "检查 ID 验证数据各项信息是否正确"));
    	ctidSet.put("J", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"J", "验签失败", "网证信息有误", "1.确认网证信息是否正确;2.联系系统对接人员"));
    	ctidSet.put("B", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"B", "下载设备 ID 不匹配", "网证下载设备与认证设备不匹配", "确认网证下载设备与认证设备是否相同"));
    	ctidSet.put("C", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"C", "系统异常", "系统异常", "联系系统对接人员"));
    	ctidSet.put("D", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"D", "网证不在库中", "该身份证已失效", "确认身份证是否有效"));
    	ctidSet.put("G", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"G", "数据库异常", "数据库异常", "联系系统对接人员"));
    	ctidSet.put("E", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"E", "系统异常", "", "联系系统对接人员"));
    	ctidSet.put("X", new AuthResult(AuthResult.AUTH_RET_TYPE_CTID,
				"X", "未执行", "", ""));
    	
    	//第四字符：保留字段
    	securitySet.put("X", new AuthResult(AuthResult.AUTH_RET_TYPE_SECURITY,
				"X", "未执行", "", ""));
    	
    	//exception
    	exceptionSet.put("AAAA", new AuthExceptionResult(
				"AAAA", "没有业务流水号", "", ""));
    	exceptionSet.put("BBBB", new AuthExceptionResult(
				"BBBB", "调研后台引擎超时", "", "联系公安部一所"));
    	exceptionSet.put("CCCC", new AuthExceptionResult(
				"CCCC", "身份证格式错误", "身份证号码、姓名错误", "检查格式"));
    	exceptionSet.put("DDDD", new AuthExceptionResult(
				"DDDD", "身份证格式错误", "身份证有效期格式错误", "检查格式"));
    	exceptionSet.put("EEEE", new AuthExceptionResult(
				"EEEE", "认证服务方法跑出异常默认返回值", "", ""));
    	exceptionSet.put("FFFF", new AuthExceptionResult(
				"FFFF", "解密失败", "", ""));
    	exceptionSet.put("XXXX", new AuthExceptionResult(
				"XXXX", "系统错误", "", ""));
    }

    public static AuthResult findAuthResultForDN(String code) {
    	return dnSet.get(code);
    }
    
    public static AuthResult findAuthResultForDNBy(String codes) {
    	if (null == codes || codes.length() < 4) {
    		return null;
    	}
    	return dnSet.get(codes.substring(0,1));
    }
    
    public static AuthResult findAuthResultForPicture(String code) {
    	return pictureSet.get(code);
    }
    
    public static AuthResult findAuthResultForPictureBy(String codes) {
    	if (null == codes || codes.length() < 4) {
    		return null;
    	}
    	return pictureSet.get(codes.substring(1,2));
    }
    
    public static AuthResult findAuthResultForCtid(String code) {
    	return ctidSet.get(code);
    }
    
    public static AuthResult findAuthResultForCtids(String codes) {
    	if (null == codes || codes.length() < 4) {
    		return null;
    	}
    	return ctidSet.get(codes.substring(2,3));
    }
    
    public static AuthResult findAuthResultForSecurity(String code) {
    	return securitySet.get(code);
    }
    
    public static AuthResult findAuthResultForSecuritys(String codes) {
    	if (null == codes || codes.length() < 4) {
    		return null;
    	}
    	return securitySet.get(codes.substring(3,3));
    }
    
    public static AuthExceptionResult findAuthExceptionResult(String code) {
    	return exceptionSet.get(code);
    }
    
    public static AuthExceptionResult findAuthExceptionResult(byte[] codes) {
    	return exceptionSet.get(new String(codes));
    }
    
    public static boolean isSuccess(String codes) {
    	if(codes == null){
    		return false;
    	}
    	AuthExceptionResult excep = findAuthExceptionResult(codes);
    	if(excep!=null){
    		return false;
    	}
    	return codes.matches("[0,X]{4}");
    }
    
    public static String getPicAuth(AuReturn auRet){
    	String auResult = auRet.getAuResult();
  		if(null == auResult || auResult.length() < 4){
  			return null;
  		}
  		return auResult.substring(1,2);
    }
    
  	public static String getDNstatus(AuReturn auRet){
  		String auResult = auRet.getAuResult();
  		if(null == auResult || auResult.length() < 4){
  			return null;
  		}
  		return auResult.substring(2,3);
  	}
  	
	public static String getAuthResultAllString(String auResult) {
		if (null == auResult || auResult.length() < 4) {
    		return null;
    	}
//    	
    	AuthExceptionResult excep = findAuthExceptionResult(auResult);
    	if (null != excep) {
    		return excep.toString();
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	String dnRes = auResult.substring(0,1);
    	String picRes = auResult.substring(1,2);
    	String ctidRes = auResult.substring(2,3);
    	String securityRes = auResult.substring(3);
    	if (!"0".equals(dnRes) && !"X".equals(dnRes)) {
    		sb.append(dnSet.get(dnRes).toString()).append("; ");
    	} 
    	if (!"0".equals(picRes) && !"X".equals(picRes)) {
    		sb.append(pictureSet.get(picRes).toString()).append("; ");
    	} 
    	if (!"0".equals(ctidRes) && !"X".equals(ctidRes)) {
    		sb.append(ctidSet.get(ctidRes).toString()).append("; ");
    	} 
    	if (!"0".equals(securityRes) && !"X".equals(securityRes)) {
    		sb.append(securitySet.get(securityRes).toString()).append("; ");
    	}
    	if(StringUtils.isBlank(sb.toString())){
    		return auResult;
    	}
		return sb.toString();
	}

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param authResult
	 * @return
	 */
	public static String getAuthResultForApiAuth(String authResult) {
		if("0XXX".equals(authResult) || "00XX".equals(authResult) || "000X".equals(authResult) || "X00X".equals(authResult)){
			return "true";
		}
		return "false";
	}

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param authCode
	 * @return
	 */
	public static int findAuthResultCode(String authCode) {
		int code = 1;
		String ctidCode = authCode.substring(2, 3);
		
		if(ctidCode.matches("[0,X]")){
			code = 0;
		}else if("1".equals(ctidCode)){
			code = ErrorCodeConstants.AUTH_CTID_CANCEL_CODE;
		}else if("5".equals(ctidCode)){
			code = ErrorCodeConstants.AUTH_CTID_BLOCKED_CODE;
		}else if("8".equals(ctidCode)){
			code = ErrorCodeConstants.AUTH_CTID_EXPIRE_CODE;
		}else if(ctidCode.matches("[3,4,6,7,9]")){
			code = ErrorCodeConstants.AUTH_CTID_DATA_CODE;
		}else{
			code = ErrorCodeConstants.AUTH_CTID_DEFAULT_CODE;
		}
		
		String faceCode = authCode.substring(1, 2);
		if(faceCode.matches("[0,X]")){
			
		}else if(faceCode.matches("[1,2]")){
			code = ErrorCodeConstants.AUTH_FACE_COMPARE_CODE;
		}else if(faceCode.matches("[C,D,E,G]")){
			code = ErrorCodeConstants.AUTH_FACE_DATA_CODE;
		}else{
			code = ErrorCodeConstants.AUTH_FACE_DEFAULT_CODE;
		}
		
		String cardCode = authCode.substring(0, 1);
		
		if(cardCode.matches("[0,X]")){
			
		}else if(faceCode.matches("[5]")){
			code = ErrorCodeConstants.AUTH_CARD_NONE_CODE;
		}else if(faceCode.matches("[6]")){
			code = ErrorCodeConstants.AUTH_CARD_DATA_CODE;
		}else if(faceCode.matches("[7,A,E,T]")){
			code = ErrorCodeConstants.AUTH_CARD_API_CODE;
		}else if(faceCode.matches("[9,Z]")){
			code = ErrorCodeConstants.AUTH_CARD_USEUP_CODE;
		}else{
			code = ErrorCodeConstants.AUTH_CARD_DEFAULT_CODE;
		}
		return code;
	}
	
	public static String findAuthResultDesc(String authCode) {
		String desc = authCode;
		String ctidCode = authCode.substring(2, 3);
		
		if(ctidCode.matches("[0,X]")){
		}else if("1".equals(ctidCode)){
			desc = ErrorCodeConstants.AUTH_CTID_CANCEL_MESSAGE;
		}else if("5".equals(ctidCode)){
			desc = ErrorCodeConstants.AUTH_CTID_BLOCKED_MESSAGE;
		}else if("8".equals(ctidCode)){
			desc = ErrorCodeConstants.AUTH_CTID_EXPIRE_MESSAGE;
		}else if(ctidCode.matches("[3,4,6,7,9]")){
			desc = ErrorCodeConstants.AUTH_CTID_DATA_MESSAGE;
		}else{
			desc = ErrorCodeConstants.AUTH_CTID_DEFAULT_MESSAGE;
		}
		
		String faceCode = authCode.substring(1, 2);
		if(faceCode.matches("[0,X]")){
			
		}else if(faceCode.matches("[1,2]")){
			desc = ErrorCodeConstants.AUTH_FACE_COMPARE_MESSAGE;
		}else if(faceCode.matches("[C,D,E,G]")){
			desc = ErrorCodeConstants.AUTH_FACE_DATA_MESSAGE;
		}else{
			desc = ErrorCodeConstants.AUTH_FACE_DEFAULT_MESSAGE;
		}
		
		String cardCode = authCode.substring(0, 1);
		
		if(cardCode.matches("[0,X]")){
			
		}else if(faceCode.matches("[5]")){
			desc = ErrorCodeConstants.AUTH_CARD_NONE_MESSAGE;
		}else if(faceCode.matches("[6]")){
			desc = ErrorCodeConstants.AUTH_CARD_DATA_MESSAGE;
		}else if(faceCode.matches("[7,A,E,T]")){
			desc = ErrorCodeConstants.AUTH_CARD_API_MESSAGE;
		}else if(faceCode.matches("[9,Z]")){
			desc = ErrorCodeConstants.AUTH_CARD_USEUP_MESSAGE;
		}else{
			desc = ErrorCodeConstants.AUTH_CARD_DEFAULT_MESSAGE;
		}
		return desc;
	}
    
}
