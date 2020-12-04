package com.ahdms.utils;

import com.ahdms.context.CtidContext;
import com.ahdms.context.holder.CtidContextHolder;
import com.ahdms.model.User;

import java.util.Optional;

/**
 * @author qinxiang
 * @date 2020-12-04 15:32
 */
public class CtidContextUtils {

    public static User getUser(){
        return Optional.ofNullable(CtidContextHolder.getContext()).map(CtidContext::getUser).orElse(null);
    }

    public static String getIp(){
        return Optional.ofNullable(CtidContextHolder.getContext()).map(CtidContext::getIp).orElse(null);
    }

}
