package com.ahdms.context;

import com.ahdms.context.holder.SvsContextHolder;

import java.util.Optional;

/**
 * @author qinxiang
 * @date 2020-12-25 14:47
 */
public class SvsContextUtils {

    public static String getAccount(){
        return Optional.ofNullable(SvsContextHolder.getContext())
                .map(SvsContext::getAccount)
                .orElse(null);
    }

}
