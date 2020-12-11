package com.ahdms.framework.feign.ribbon.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Ribbon 配置
 *
 * @author Katrel.zhou
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("dms.ribbon.filter")
public class DmsRibbonProperties {
	/**
	 * 是否开启，默认：true
	 */
	private Boolean enabled = Boolean.TRUE;

	private Metadata metadata = new Metadata();

	@Getter
	@Setter
	public static class Metadata {
		/**
		 * 服务的tag，用于灰度，匹配：nacos.discovery.metadata.tag
		 */
		private String tag;
		/**
		 * 优先的ip列表，支持通配符，例如：10.20.0.8*、10.20.0.*
		 */
		private List<String> priorIpPattern = new ArrayList<>();
	}
}
