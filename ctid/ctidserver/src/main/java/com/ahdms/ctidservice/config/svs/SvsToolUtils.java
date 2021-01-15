package com.ahdms.ctidservice.config.svs;

import com.ahdms.sv.SVTool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qinxiang
 * @date 2021-01-07 16:59
 */
@Component
public class SvsToolUtils implements InitializingBean {

    @Autowired
    private SvsProperties svsProp;

    private SVTool tool = SVTool.getInstance();

    @Override
    public void afterPropertiesSet() throws Exception {
        tool.SVS_InitServerConnect(svsProp.getSvIp(),svsProp.getSvPort());
    }
}
