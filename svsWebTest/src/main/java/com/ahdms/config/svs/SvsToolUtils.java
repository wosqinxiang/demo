package com.ahdms.config.svs;

import com.ahdms.sv.SVTool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinxiang
 * @date 2020-12-21 11:32
 */
@Component
public class SvsToolUtils implements InitializingBean {

    @Autowired
    private SvsProperties svsProperties;

    private static Map<String, SVTool> svToolMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
//        getSVTool(svsProperties.getServer());
    }

    public static SVTool getSVTool(SvsProperties.SvsServer server) throws Exception{
        String key = server.getIp()+":"+server.getPort();
        SVTool svTool = svToolMap.get(key);
        if(svTool == null){
            svTool = SVTool.getInstance();
            svTool.SVS_InitServerConnect(server.getIp(), server.getPort());
            svToolMap.put(key,svTool);
        }
        return svTool;
    }

    public SVTool getSVTool(String appCode) throws Exception{
        SvsProperties.SvsServer svsServer = svsProperties.getAppCodes().get(appCode);
//        String key = svsServer.getServer().getIp()+":"+svsServer.getServer().getPort();
        SVTool svTool = svToolMap.get(appCode);
        if(svTool == null){
            svTool = SVTool.getInstance();
            svTool.SVS_InitServerConnect(svsServer.getIp(), svsServer.getPort());
            svToolMap.put(appCode,svTool);
        }
        return svTool;
    }

    public SVTool getDefaultSVTool() throws Exception{

        return getSVTool("test1");
    }

}
