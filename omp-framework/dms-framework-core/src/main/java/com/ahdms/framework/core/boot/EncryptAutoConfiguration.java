package com.ahdms.framework.core.boot;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库等密码加密配置
 *
 * @author katrel.zhou
 */
@Configuration
@EnableEncryptableProperties
public class EncryptAutoConfiguration {
}
