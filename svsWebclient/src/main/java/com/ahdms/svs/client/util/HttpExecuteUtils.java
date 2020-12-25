package com.ahdms.svs.client.util;

import com.ahdms.framework.http.request.HttpRequest;
import com.ahdms.svs.client.constants.HeaderConstants;
import com.ahdms.svs.client.result.ApiResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinxiang
 * @date 2020-12-24 14:50
 */
@Slf4j
public class HttpExecuteUtils {


    public static ApiResult execute(String url,String path,Object object,String account){
        try{
            return HttpRequest.post(appendUrl(url,path))
                    .addHeader(HeaderConstants.SVS_USER_ID,account)
                    .bodyJson(object).execute().asValue(ApiResult.class);
        }catch (Exception e){
            log.error("密码服务平台连接失败,{}",e.getMessage());
        }
        return ApiResult.error("密码服务平台连接失败");

    }

    private static String appendUrl(String url,String path){
        StringBuilder stb = new StringBuilder(url);
        if(!url.endsWith("/")){
            stb.append("/");
        }
        stb.append(path);
        return stb.toString();
    }

}
