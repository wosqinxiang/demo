package com.ahdms.bean.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2021-01-03 10:02
 */
@Data
public class SvsUserReqVo {

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("服务账号")
    private String account;
    @ApiModelProperty("应用描述")
    private String info;
    @ApiModelProperty("签名验签服务器IP")
    private String svsIp;
    @ApiModelProperty("签名验签服务器端口")
    private Integer svsPort;
    @ApiModelProperty("私钥索引值")
    private Integer svsKeyIndex;
    @ApiModelProperty("私钥权限标识码")
    private String svsKeyValue;
    @ApiModelProperty("可信标识序列号")
    private String svsSerialNumber;
    @ApiModelProperty("对称密钥密文")
    private String svsEncryptKey;


    private String whiteIp;

}
