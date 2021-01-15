package com.ahdms.auth.common;

import java.util.UUID;

import org.bouncycastle.crypto.digests.SM3Digest;

/***
 * 随机数 ID值
 * @标题：
 * @描述：
 * @创建人：xiaopu
 * @日期：2016年10月26日-下午5:01:06
 * @版本信息：V1.0 
 * @Copyright (c) 2016 长沙迪曼森信息科技有限公司-版权所有
 */
public class UUIDGenerator { 
    private UUIDGenerator() {
    } 
    /***
     * 获取32位随机数
     * @标题：
     * @描述: 
     * @创建人：xiaopu
     * @创建日期：2016年10月26日-下午5:01:43
     * @return
     */
    public static String getUUID(){ 
        String s = UUID.randomUUID().toString(); 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    } 

    /***
     * 自定义位数
     * @标题：
     * @描述: 
     * @创建人：xiaopu
     * @创建日期：2016年10月26日-下午5:02:01
     * @param number
     * @return
     */
    public static String[] getUUID(int number){ 
        if(number < 1){ 
            return null; 
        } 
        String[] ss = new String[number]; 
        for(int i=0;i<number;i++){ 
            ss[i] = getUUID(); 
        } 
        return ss; 
    } 

    /***
     * 16位随机数
     * @标题：
     * @描述: 
     * @创建人：xiaopu
     * @创建日期：2016年10月26日-下午5:02:15
     * @return
     */
    public static String getSerialId(){
    	 String s = UUID.randomUUID().toString(); 
    	 return s.substring(0,16);
    }

    public static byte[] getSM3Digest(byte[] data) {
        byte[] md = new byte[32];
        SM3Digest sm3 = new SM3Digest();
        sm3.update(data, 0, data.length);
        sm3.doFinal(md, 0);
        return md;
    }
    
}   