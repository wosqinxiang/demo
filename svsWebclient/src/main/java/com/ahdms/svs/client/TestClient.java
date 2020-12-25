package com.ahdms.svs.client;

import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.svs.client.result.ApiResult;

/**
 * @author qinxiang
 * @date 2020-12-25 15:44
 */
public class TestClient {

    public static void main(String[] args) {
        SvsServiceClient svsServiceClient = SvsServiceClient.getInstance("test1","http://172.16.1.190:9018");
        ApiResult<String> stringApiResult = svsServiceClient.svrGenRnd();
        ApiResult<String> abcdefg = svsServiceClient.signData("abcdefg");
        System.out.println(JsonUtils.toJson(abcdefg));
    }
}
