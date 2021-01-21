package com.ahdms.billing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yeqiang on 11/8/16.
 */
public class RegexTool {

    public static final String REGEX_EMAIL = "[0-9A-Za-z]+@[0-9A-Za-z]+\\.[0-9A-Za-z]+";
    public static final String REGEX_MOBILEPHONE = "^\\d{11}$";// 直接匹配11位数字
    private static final Logger logger = LoggerFactory.getLogger(RegexTool.class);
    static Pattern emailPattern = Pattern.compile(REGEX_EMAIL);
    static Pattern mobilePhonePattern = Pattern.compile(REGEX_MOBILEPHONE);

    public static boolean isEmail(String input) {
        try {
            Matcher m = emailPattern.matcher(input);
            if (m.find()) {
                logger.debug("{} is Email", input);
                return true;
            } else {
                logger.debug("{} is not Email", input);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public static boolean isMobilePhone(String input) {
        try {
            Matcher m = mobilePhonePattern.matcher(input);
            if (m.find()) {
                logger.debug("{} is mobilePhone", input);
                return true;
            } else {
                logger.debug("{} is not mobilePhone", input);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
