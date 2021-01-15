package com.ahdms.svs.client.util;

import com.ahdms.svs.client.config.HttpSend;
import com.ahdms.svs.client.constants.ApiCode;
import com.ahdms.svs.client.result.ApiResult;

/**
 * @author qinxiang
 * @date 2020-12-24 14:50
 */
public class HttpExecuteUtils {
    static HttpSend hs = new HttpSend();

    public static ApiResult execute(String url,String path,Object object,String account){

        try{
            String response = hs.httpResponse(appendUrl(url, path), object, account);
            if(response != null){
                return JsonUtils.json2Bean(response,ApiResult.class);
            }
//            return HttpRequest.post(appendUrl(url,path))
//                    .addHeader(HeaderConstants.SVS_USER_ID,account)
//                    .bodyJson(object).execute().asValue(ApiResult.class);
        }catch (Exception e){
            System.out.println("密码服务平台连接失败,"+e.getMessage());
        }
        return ApiResult.error(ApiCode.SVS_CONNECT_ERROR);

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
