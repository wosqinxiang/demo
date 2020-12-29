package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-18 8:41
 */
@Data
public class DecryptEnvelopeReqVo {

    @NotNull
    private String base64EnvelopeData;

}
