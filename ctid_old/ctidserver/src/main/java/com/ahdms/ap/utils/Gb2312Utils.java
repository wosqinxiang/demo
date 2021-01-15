package com.ahdms.ap.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author ht
 * @version 1.0
 * @Title
 * @Description
 * @Copyright &lt;p&gt;Copyright (c) 2019&lt;/p&gt;
 * @Company &lt;p&gt;迪曼森信息科技有限公司 Co., Ltd.&lt;/p&gt;
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class Gb2312Utils {
    public static String gb2312decode(String string) throws UnsupportedEncodingException {
        byte[] bytes = new byte[string.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            byte high = Byte.parseByte(string.substring(i * 2, i * 2 + 1), 16);
            byte low = Byte.parseByte(string.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high << 4 | low);
        }
        return new String(bytes, "gb2312");
    }

    public static String gb2312eecode(String string) throws UnsupportedEncodingException {
        StringBuffer gbkStr = new StringBuffer();
        byte[] gbkDecode = string.getBytes("gb2312");
        for (byte b : gbkDecode) {
            gbkStr.append(Integer.toHexString(b & 0xFF));
        }
        return gbkStr.toString();
    }
}
