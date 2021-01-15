package com.ahdms.ctidservice.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.annotation.Reference;

@Component
public class DubboUtils {

	@Value("${dubbo.registry.address}")
	private String address;

	@Value("${dubbo.version}")
	private String version;
	
	@Value("${default.serverId}")
	private String default_serverId;
	
	@Value("${dubbo.registry.protocol}")
	private String protocol;

	private static ApplicationConfig application = new ApplicationConfig();

	private static Map<String, RegistryConfig> registryConfigCache = new ConcurrentHashMap<>();

	private static Map<String, ReferenceConfig> referenceCache = new ConcurrentHashMap<>();

	static {
		application.setName("test");
	}

	private RegistryConfig getRegistryConfig(String address, String group, String version) {
		String key = address + "-" + group + "-" + version;
		RegistryConfig registryConfig = registryConfigCache.get(key);
		if (null == registryConfig) {
			registryConfig = new RegistryConfig();
			registryConfig.setAddress(address);
			registryConfig.setProtocol(protocol);
			registryConfigCache.put(key, registryConfig);
		}
		return registryConfig;
	}

//	public <T> T getDubboService(String group, String address, String version, Class<T> clazz) {
//		String referenceKey = clazz.getSimpleName() + "," + group;
//		ReferenceConfig<T> referenceConfig = referenceCache.get(referenceKey);
//		if (null == referenceConfig) {
//			referenceConfig = new ReferenceConfig<>();
//			referenceConfig.setApplication(application);
//			referenceConfig.setRegistry(getRegistryConfig(address, group, version));
//			referenceConfig.setInterface(clazz);
//			referenceConfig.setVersion(version);
//			referenceConfig.setGroup(group);
//			referenceCache.put(referenceKey, referenceConfig);
//		}
//		return referenceConfig.get();
//	}
	
	public <T> T getDubboService(String group,  Class<T> clazz) {
		if(StringUtils.isBlank(group)){
			group = default_serverId;
		}
		String referenceKey = clazz.getSimpleName() + "," + group;
		ReferenceConfig<T> referenceConfig = referenceCache.get(referenceKey);
		if (null == referenceConfig) {
			referenceConfig = new ReferenceConfig<>();
			referenceConfig.setApplication(application);
			referenceConfig.setRegistry(getRegistryConfig(address, group, version));
			referenceConfig.setInterface(clazz);
			referenceConfig.setVersion(version);
			referenceConfig.setGroup(group);
			referenceConfig.setRetries(0);
			referenceConfig.setTimeout(3000);
//			referenceConfig.setProtocol(protocol);
			referenceCache.put(referenceKey, referenceConfig);
		}
		T t = referenceConfig.get();
		if(null == t){
			referenceConfig = new ReferenceConfig<>();
			referenceConfig.setApplication(application);
			referenceConfig.setRegistry(getRegistryConfig(address, group, version));
			referenceConfig.setInterface(clazz);
			referenceConfig.setVersion(version);
			referenceConfig.setGroup(group);
			referenceConfig.setRetries(0);
			referenceCache.put(referenceKey, referenceConfig);
		}
		return referenceConfig.get();
	}

}
