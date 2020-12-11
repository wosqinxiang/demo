package com.ahdms.framework.feign.ribbon.predicate;


import com.ahdms.framework.core.commom.util.BeanLoaderUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.feign.ribbon.support.DmsRibbonProperties;
import org.springframework.cloud.consul.discovery.ConsulServer;


import java.util.Map;

/**
 * 基于 Metadata 的服务筛选
 *
 * @author Katrel.zhou
 */
public class MetadataAwarePredicate extends DiscoveryEnabledPredicate {

    @Override
    protected boolean apply(ConsulServer server) {
        final Map<String, String> metadata = server.getMetadata();

        // 获取配置
        DmsRibbonProperties properties = BeanLoaderUtils.getSpringBean(DmsRibbonProperties.class);
        // 服务里的配置
        String localTag = properties.getMetadata().getTag();

        if (StringUtils.isBlank(localTag)) {
            return true;
        }

        // 本地有 tag，服务没有，返回 false
        String metaDataTag = metadata.get("tag");
        if (StringUtils.isBlank(metaDataTag)) {
            return false;
        }

        return metaDataTag.equals(localTag);
    }

}
