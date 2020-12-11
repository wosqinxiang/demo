package com.ahdms.bean;

import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 16:48
 */
@Data
public class CustomerExtRspVo {

    private Integer id;
    private String extKey;
    private String name;
    private Integer criticalValue;
    private Integer clientValue;
    private Integer isServer;
    private String serverValue;
    private Integer calculation;
}
