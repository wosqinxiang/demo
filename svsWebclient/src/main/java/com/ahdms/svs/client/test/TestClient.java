package com.ahdms.svs.client.test;

import com.ahdms.svs.client.SecurityEngineDeal;
import com.ahdms.svs.client.result.ApiResult;

/**
 * @author qinxiang
 * @date 2021-01-08 14:53
 */
public class TestClient {

    public static void main(String[] args) {
        SecurityEngineDeal sed = SecurityEngineDeal.getInstance("JinxiandaiTest","https://svs.ahdms.top/");
        ApiResult s = sed.encryptDatas("aa","bb","aa1","bb2","aa3","bb4","aa5","bb6","aa7","bb8","aa9","bb0","aa11","bb12","aa13","bb14","aa15","bb16");
        System.out.println(s.getCode());

        ApiResult<String> avv = sed.signData("avv");
        ApiResult<String> certInfo = sed.certInfo();
        ApiResult<Boolean> avv1 = sed.verifyData(certInfo.getData(), avv.getData(), "avv", 0);
        System.out.println(avv1.getCode());

        ApiResult<String> sss = sed.encryptData("sss");
        System.out.println(sss.getData());

    }
}
