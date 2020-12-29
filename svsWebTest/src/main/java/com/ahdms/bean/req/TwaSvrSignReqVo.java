package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-18 8:48
 */
@Data
public class TwaSvrSignReqVo {

    @NotNull
    private String clientRandom;
}
