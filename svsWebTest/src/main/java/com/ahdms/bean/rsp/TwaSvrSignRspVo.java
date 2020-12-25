package com.ahdms.bean.rsp;

import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-18 15:54
 */
@Data
public class TwaSvrSignRspVo {

    private String serverRandom;
    private String base64signValue;
}
