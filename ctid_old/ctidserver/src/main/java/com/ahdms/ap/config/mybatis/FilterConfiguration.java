///**
// * Created on 2017年12月22日 by luotao
// */
//package com.ahdms.ap.config.mybatis;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.alibaba.druid.support.http.WebStatFilter;
//
///**
// * @Title 
// * @Description 
// * @Copyright <p>Copyright (c) 2017</p>
// * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
// * @author luotao
// * @version 1.0
// * @修改记录
// * @修改序号，修改日期，修改人，修改内容
// */
//@Configuration
//public class FilterConfiguration {
//    @Bean
//    public FilterRegistrationBean druidStatFilterBean() {
//        FilterRegistrationBean druidStatFilterBean=new FilterRegistrationBean(new WebStatFilter());
//        List<String> urlPattern=new ArrayList<>();
//        urlPattern.add("/*");
//        druidStatFilterBean.setUrlPatterns(urlPattern);
//        Map<String,String> initParams=new HashMap<>();
//        initParams.put("exclusions","*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
//        druidStatFilterBean.setInitParameters(initParams);
//        return druidStatFilterBean;
//    }
//}
//
