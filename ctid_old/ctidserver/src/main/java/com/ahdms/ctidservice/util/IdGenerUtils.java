package com.ahdms.ctidservice.util;

import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.service.TokenCipherService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;

/**
 * @author qinxiang
 * @date 2020-09-02 13:40
 */
@Component
public class IdGenerUtils {

    @Autowired
    private TokenCipherService cipherSer;

    public String encryptBIdByIdCard(String idCard,String key){

        return encryptBId(CalculateHashUtils.calculateHash1(idCard.getBytes()),key);
    }

    public String encryptBId(String hash,String key){

        return encryptBId(Base64Utils.decode(hash),key);
    }

    public String encryptBId(byte[] hash,String key){

        return cipherSer.encryptCTID(concat(hash,key));
    }

    public String encrypt2BId(byte[] hash,String key){
        return cipherSer.encryptCTID(Base64Utils.decode(encryptBId(hash,key)));
    }

    public String encrypt2BId(String bid){
        return cipherSer.encryptCTID(Base64Utils.decode(bid));
    }

    /**
     * Concatenates 2 arrays
     *
     * @param one   数组1
     * @param other 数组2
     * @return 新数组
     */
    public static byte[] concat(byte[] one, byte[] other) {
        byte[] target = new byte[one.length+16];
        System.arraycopy(one, 0, target, 0, one.length);
        if(null != other){
            System.arraycopy(other, 0, target, one.length, other.length);
        }
        return target;
    }

    public static byte[] concat(byte[] one, String other) {
        byte[] target = new byte[one.length+16];
        System.arraycopy(one, 0, target, 0, one.length);
        if(StringUtils.isNotBlank(other)){
            byte[] b = other.getBytes();
            if(b.length > 16){
                System.arraycopy(b, 0, target, one.length, 16);
            }else{
                System.arraycopy(b, 0, target, one.length, b.length);
            }
        }
        return target;
    }

    /**
     * Concatenates 2 arrays
     *
     * @param one   数组1
     * @param other 数组2
     * @param clazz 数组类
     * @return 新数组
     */
    public static <T> T[] concat(T[] one, T[] other, Class<T> clazz) {
        T[] target = (T[]) Array.newInstance(clazz, one.length + other.length);
        System.arraycopy(one, 0, target, 0, one.length);
        System.arraycopy(other, 0, target, one.length, other.length);
        return target;
    }

}
