package com.ahdms.bean;

import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 16:15
 */
@Data
public class CustomerExtReqVo {

    private String extKey;
    private Integer criticalValue;
    private Integer clientValue;
    private Integer isServer;
    private String serverValue;
    private Integer calculation;

}
