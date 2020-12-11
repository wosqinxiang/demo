package com.ahdms.bean;

import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 15:11
 */
@Data
public class CertExtensionRspVo {

    private Integer id;
    private String name;
    private Integer isCritical;
    private Integer criticalValue;
    private Integer isClient;
    private Integer clientValue;
    private Integer isServer;
    private String serverValue;
    private Integer calculation;
    private Integer keyValue;

}
