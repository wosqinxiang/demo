//package com.ahdms.ap.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
///**
// * @author ht
// * @version 1.0
// * @Title
// * @Description
// * @Copyright &lt;p&gt;Copyright (c) 2019&lt;/p&gt;
// * @Company &lt;p&gt;迪曼森信息科技有限公司 Co., Ltd.&lt;/p&gt;
// * @修改记录
// * @修改序号，修改日期，修改人，修改内容
// */
////@Configuration
//public class RestTemplateConfig {
//
//    /**
//     * 必须new 一个RestTemplate并放入spring容器当中,否则启动时报错
//     */
////    @Bean
//    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectionRequestTimeout(30 * 1000);
//        httpRequestFactory.setConnectTimeout(30 * 3000);
//        httpRequestFactory.setReadTimeout(30 * 3000);
//        return new RestTemplate(httpRequestFactory);
//    }
//}
