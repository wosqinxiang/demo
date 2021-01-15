package com.ahdms.ctidservice.util;

import java.util.regex.Pattern;

/**
 * @author qinxiang
 * @date 2020-07-15 8:20
 */
public class ValidateUtils {

    //用户名正则
    public static final String USERNAME_REGEX = "[a-zA-Z0-9_]{8,16}";

    public static final String PASSWORD_REGEX = "\\w{8,24}";

    //手机号正则
    public static final String MOBILE_REGEX = "^[1][3456789]\\d{9}$";

    //手机验证码正则
    public static final String SMSCODE_REGEX = "\\d{6}";

    public static boolean validate(String value,String regex){
        if(value == null){
            return false;
        }
        return value.matches(regex);
    }

    public static boolean validateUsername(String username){

        return validate(username,USERNAME_REGEX);
    }

    public static boolean validatePWD(String password){

        return validate(password,PASSWORD_REGEX);
    }

    public static boolean validateMobile(String mobile){

        return validate(mobile,MOBILE_REGEX);
    }

    public static boolean validateSmsCode(String smsCode){

        return validate(smsCode,SMSCODE_REGEX);
    }

}
