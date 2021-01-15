package com.ahdms.ctidservice.bean;

import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-09-02 16:06
 */
@Data
public class CtidResponseVo {

    private String ctidInfo;

    private String bsn;

    private String token;

    private String sdkBid;

    private String ctidIndex;

    private String pid;

}
