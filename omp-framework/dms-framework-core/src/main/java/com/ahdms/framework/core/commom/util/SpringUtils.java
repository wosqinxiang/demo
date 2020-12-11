package com.ahdms.framework.core.commom.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Slf4j
public abstract class SpringUtils {


    /**
     * 支持classpath、url的locations
     * @param locations
     * @return
     */
    public static Properties loadProperties(String... locations) {
        Properties ret = new Properties();
        if (CollectionUtils.isEmpty(locations)) return ret;

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for (String location : locations) {
            try {
                for (Resource res : resolver.getResources(location)) {
                    try {
                        PropertiesLoaderUtils.fillProperties(ret, res);
                    } catch (IOException e) {
                        log.info("location={}, res={}, ex={}", location, res, e);
                    }
                }
            } catch (IOException e) {
                log.info("location={}, ex={}", location, e);
            }
        }
        return ret;
    }

}
