package com.ahdms.ctidservice.context;

import com.ahdms.ctidservice.bean.model.KeyInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author qinxiang
 * @date 2021-01-04 11:29
 */
@Getter
@Builder
@ToString
public class CtidContext {

    private KeyInfo keyInfo;

    private String requestIp;

    private String specialCode;

    private String serverAccount;

    private String bsn;

}
