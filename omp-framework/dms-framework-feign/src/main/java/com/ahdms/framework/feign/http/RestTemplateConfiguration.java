package com.ahdms.framework.feign.http;


import com.ahdms.framework.core.web.DmsServletConfiguration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Http RestTemplateHeaderInterceptor 配置
 *
 * @author Katrel.zhou
 */
@Configuration
@ConditionalOnClass(OkHttpClient.class)
@EnableConfigurationProperties(OkHttpProperties.class)
@AutoConfigureAfter(DmsServletConfiguration.class)
@RequiredArgsConstructor
public class RestTemplateConfiguration {
    private final OkHttpProperties properties;


    /**
     * 普通的 RestTemplate，不透传请求头，一般只做外部 http 调用
     *
     * @param builderProvider RestTemplateBuilder
     * @return RestTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(ObjectProvider<RestTemplateBuilder> builderProvider) {
        RestTemplateBuilder restTemplateBuilder = builderProvider.getIfAvailable(RestTemplateBuilder::new);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpSlf4jLogger());
        interceptor.setLevel(properties.getLevel());

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();


        restTemplateBuilder.requestFactory(() -> new OkHttp3ClientHttpRequestFactory(httpClient));
        RestTemplate restTemplate = restTemplateBuilder.build();
        /**
         *  DopServletConfiguration#messageConverters(ObjectProvider<List<HttpMessageConverter<?>>>)
         *  指定了全局的HttpMessageConverters,调整了规则顺序,不再二次调整。
         */
// 		configMessageConverters(restTemplate.getMessageConverters());
        return restTemplate;
    }

}
