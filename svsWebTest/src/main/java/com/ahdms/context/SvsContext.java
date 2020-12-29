package com.ahdms.context;

import com.ahdms.config.svs.SvsProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author qinxiang
 * @date 2020-12-25 14:36
 */
@Getter
@Builder
@ToString
public class SvsContext {

    private String account;

    private SvsProperties.SvsServer svsServer;

}
