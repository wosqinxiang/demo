package com.ahdms.svs.client;

import com.ahdms.svs.client.result.ApiResult;

/**
 * @author qinxiang
 * @date 2020-12-25 15:44
 */
public class TestClient {

    public static void main(String[] args) {
        SecurityEngineDeal sed= SecurityEngineDeal.getInstance("test1","http://172.16.1.6:9280");
        ApiResult<String> signData = sed.signData("abcdefg");
        ApiResult<String> encryptData = sed.encryptData("中国人");
        ApiResult<String> stringApiResult = sed.decryptData(encryptData.getData());
        ApiResult<String> certInfo = sed.certInfo();
        ApiResult<String> sdasdsds = sed.encryptEnvelope(certInfo.getData(), "sdasdsds");
        System.out.println(sdasdsds.getData());
        ApiResult<String> stringApiResult1 = sed.decryptEnvelope(sdasdsds.getData());

        ApiResult<Boolean> booleanApiResult = sed.verifyCert(certInfo.getData());
        ApiResult<Boolean> abcdefg = sed.verifyData(certInfo.getData(), signData.getData(), "abcdefg", 0);

        System.out.println(booleanApiResult.getData());
        System.out.println(abcdefg.getData());
        System.out.println(stringApiResult1.getData());
        System.out.println(stringApiResult.getData());
        System.out.println(signData.getData());
    }
}
