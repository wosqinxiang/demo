package com.ahdms.framework.feign.ribbon.rule;


import com.ahdms.framework.core.commom.util.BeanLoaderUtils;
import com.ahdms.framework.core.commom.util.NetUtils;
import com.ahdms.framework.core.commom.util.ObjectUtils;
import com.ahdms.framework.feign.ribbon.predicate.DiscoveryEnabledPredicate;
import com.ahdms.framework.feign.ribbon.predicate.MetadataAwarePredicate;
import com.ahdms.framework.feign.ribbon.support.DmsRibbonProperties;
import com.netflix.loadbalancer.Server;
import org.springframework.util.PatternMatchUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ribbon 路由规则器
 *
 * @author Katrel.zhou
 */
public class MetadataAwareRule extends DiscoveryEnabledRule {

	public MetadataAwareRule() {
		this(new MetadataAwarePredicate());
	}

	public MetadataAwareRule(DiscoveryEnabledPredicate predicate) {
		super(predicate);
	}

	@Override
	public List<Server> filterServers(List<Server> serverList) {
		DmsRibbonProperties ribbonProperties = BeanLoaderUtils.getSpringBean(DmsRibbonProperties.class);
		List<String> priorIpPattern = ribbonProperties.getMetadata().getPriorIpPattern();

		// 1. 查找是否有本机 ip
		String hostIp = NetUtils.getHostIp();

		// 优先的 ip 规则
		boolean hasPriorIpPattern = !priorIpPattern.isEmpty();
		String[] priorIpPatterns = priorIpPattern.toArray(new String[0]);

		List<Server> priorServerList = new ArrayList<>();
		for (Server server : serverList) {
			String host = server.getHost();
			// 2. 优先本地 ip 的服务
			if (ObjectUtils.nullSafeEquals(hostIp, host)) {
				return Collections.singletonList(server);
			}
			// 3. 优先的 ip 服务
			if (hasPriorIpPattern && PatternMatchUtils.simpleMatch(priorIpPatterns, host)) {
				priorServerList.add(server);
			}
		}

		// 4. 如果优先的有数据直接返回
		if (!priorServerList.isEmpty()) {
			return priorServerList;
		}

		return Collections.unmodifiableList(serverList);
	}

}
