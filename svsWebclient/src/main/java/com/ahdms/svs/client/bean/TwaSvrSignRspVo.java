package com.ahdms.svs.client.bean;


/**
 * @author qinxiang
 * @date 2020-12-18 15:54
 */
public class TwaSvrSignRspVo {

    private String serverRandom;
    private String base64signValue;

    public String getServerRandom() {
        return serverRandom;
    }

    public void setServerRandom(String serverRandom) {
        this.serverRandom = serverRandom;
    }

    public String getBase64signValue() {
        return base64signValue;
    }

    public void setBase64signValue(String base64signValue) {
        this.base64signValue = base64signValue;
    }
}
