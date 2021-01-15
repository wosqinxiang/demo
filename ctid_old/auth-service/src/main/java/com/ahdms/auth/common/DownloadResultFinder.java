package com.ahdms.auth.common;

import java.util.HashMap;
import java.util.Map;

import com.ahdms.auth.constants.ErrorCodeConstants;
import org.apache.commons.lang3.StringUtils;


public class DownloadResultFinder {

    private static final Map<String, String> retMap = new HashMap<String, String>();

    static {
        retMap.put("0", "下载成功");
        retMap.put("1", "网证已被冻结");
        retMap.put("2", "网证已被注销");
        retMap.put("3", "认证码错误");
        retMap.put("4", "输入参数异常（为空或长度不合法）");
        retMap.put("5", "网证开通输入参数异常（为空或长度不合法）");
        retMap.put("6", "身份信息无法核实");
        retMap.put("7", "网证开通系统异常");
        retMap.put("8", "短信验证码已失效");
        retMap.put("9", "短信验证码填写错误");
        retMap.put("10", "业务流水号已失效");
        retMap.put("11", "发送短信验证码失败");
        retMap.put("12", "当日短信条数已达限制");
        retMap.put("13", "不许重复提交请求(一分钟后重新获取短信验证码)");
        retMap.put("14", "手机号格式不正确");
        retMap.put("15", "手机验证码格式不正确");
        retMap.put("16", "业务流水号与模式不匹配");
        retMap.put("21", "身份信息不匹配或者身份信息和网证不匹配");
        retMap.put("22", "业务流水号异常");
        retMap.put("23", "网证未开通");
        retMap.put("25", "网证不在库中");
        retMap.put("C", "新网证生成异常");
        retMap.put("J", "调用签名服务器异常");
        retMap.put("G", "数据库异常");
        retMap.put("77", "系统异常");
    }

    public static String findDownloadResult(String code) {
        String result = retMap.get(code);
        if(StringUtils.isBlank(result)){
            return "人像比对失败或身份信息有误";
        }
        return result;
    }

    public static int findDownloadCode(String result) {
        int code = 1;
        switch (result) {
            case "1":
                code = ErrorCodeConstants.DOWN_CTID_BLOCK_CODE;
                break;
            case "2":
                code = ErrorCodeConstants.DOWN_CTID_CANCEL_CODE;
                break;
            case "3":
                code = ErrorCodeConstants.DOWN_CTID_AUTHCODE_CODE;
                break;
            case "7":
                code = ErrorCodeConstants.DOWN_SMSCODE_ERROR_CODE;
                break;
            case "8":
                code = ErrorCodeConstants.DOWN_SMSCODE_EXPIRE_CODE;
                break;
            case "9":
                code = ErrorCodeConstants.DOWN_SMSCODE_CANCEL_CODE;
                break;
            case "10":
                code = ErrorCodeConstants.DOWN_SMSCODE_BSNEXPIRE_CODE;
                break;
            case "11":
                code = ErrorCodeConstants.DOWN_SMSCODE_SEND_CODE;
                break;
            case "12":
                code = ErrorCodeConstants.DOWN_SMSCODE_COUNT_CODE;
                break;
            case "13":
                code = ErrorCodeConstants.DOWN_SMSCODE_REPEAT_CODE;
                break;
            case "14":
                code = ErrorCodeConstants.DOWN_SMSCODE_MOBILE_CODE;
                break;
            case "16":
                code = ErrorCodeConstants.DOWN_SMSCODE_BSN_CODE;
                break;
            case "21":
            case "23":
                code = ErrorCodeConstants.DOWN_CTID_IDCARD_CODE;
                break;
            case "25":
                code = ErrorCodeConstants.DOWN_CTID_NONE_CODE;
                break;
            default:
                code = ErrorCodeConstants.DOWN_CTID_DEFAULT_CODE;
                break;
        }
        return code;
    }
}
