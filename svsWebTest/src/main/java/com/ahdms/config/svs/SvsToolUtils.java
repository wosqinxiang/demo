package com.ahdms.config.svs;

import com.ahdms.bean.model.SvsConfig;
import com.ahdms.context.SvsContextUtils;
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
public class SvsToolUtils {

    @Autowired
    private SvsConfigCache svsConfigCache;

    private static Map<String, SVTool> svToolMap = new ConcurrentHashMap<>();

    public SVTool getSVTool(String account) throws Exception{
        SVTool svTool = svToolMap.get(account);
        if(svTool == null){
            SvsConfig svsConfig = svsConfigCache.get(account);
            svTool = SVTool.getInstance();
            svTool.SVS_InitServerConnect(svsConfig.getIp(), svsConfig.getPort());
            svToolMap.put(account,svTool);
        }
        return svTool;
    }

    public SVTool getDefaultSVTool() throws Exception{

        return getSVTool(SvsContextUtils.getAccount());
    }

    public void removeSVTool(String account) {
        svToolMap.remove(account);
    }

}
