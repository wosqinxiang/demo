package com.ahdms.auth.common;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author qinxiang
 * @date 2020-07-14 8:44
 */
public class RamdonUtils {

    private RamdonUtils(){}

    public static String randomNumeric(int count){
        return RandomStringUtils.randomNumeric(count);
    }

    public static String randomString(int count){
        return RandomStringUtils.randomAlphabetic(count);
    }

    public static void main(String[] args){
        System.out.println(RandomStringUtils.randomAlphabetic(6));
        System.out.println(RandomStringUtils.randomAscii(6));
        System.out.println(RandomStringUtils.randomGraph(6));
        System.out.println(RandomStringUtils.randomPrint(6));
    }

}
