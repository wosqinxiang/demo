package com.ahdms.ap.task.elasticjobconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@Configuration
public class ZookeeperConfig {
	@Value("${zk.serverLists}")
	private String serverLists;
	
	@Value("${zk.namespace}")
	private String namespace;
	
	
	@Bean
	public CoordinatorRegistryCenter registryCenter() {
		CoordinatorRegistryCenter registryCenter = 
					new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverLists, namespace));
		registryCenter.init();
		return registryCenter;
	}

}
