package com.ahdms.ctidservice.context;

import com.ahdms.ctidservice.bean.model.KeyInfo;
import com.ahdms.ctidservice.context.holder.CtidContextHolder;

import java.util.Optional;

/**
 * @author qinxiang
 * @date 2021-01-04 17:11
 */
public class CtidContextUtils {

    public static KeyInfo getKeyInfo() {
        return Optional.ofNullable(CtidContextHolder.getContext())
                .map(CtidContext::getKeyInfo)
                .orElse(null);
    }

    public static String getRequestIp(){
        return Optional.ofNullable(CtidContextHolder.getContext())
                .map(CtidContext::getRequestIp)
                .orElse(null);
    }

    public static String getSpecialCode(){
        return Optional.ofNullable(CtidContextHolder.getContext())
                .map(CtidContext::getSpecialCode)
                .orElse(null);
    }

    public static String getBsn(){
        return Optional.ofNullable(CtidContextHolder.getContext())
                .map(CtidContext::getBsn)
                .orElse(null);
    }

    public static String getServerAccount(){
        return Optional.ofNullable(CtidContextHolder.getContext())
                .map(CtidContext::getServerAccount)
                .orElse(null);
    }

}
