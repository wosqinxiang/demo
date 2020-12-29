package com.ahdms.bean.req;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-18 8:38
 */
@Data
public class Pkcs7SignDataReqVo {

    private String inData;
    private int originalText = 1;
    private int irls = 1;

}
