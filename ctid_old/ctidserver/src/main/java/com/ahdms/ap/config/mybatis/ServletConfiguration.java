///**
// * Created on 2017年12月22日 by luotao
// */
//package com.ahdms.ap.config.mybatis;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.alibaba.druid.support.http.StatViewServlet;
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
//public class ServletConfiguration {
//    @Bean
//    public ServletRegistrationBean druidStatViewServletBean() {
//        //后台的路径
//        ServletRegistrationBean statViewServletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        Map<String,String> params = new HashMap<>();
//        //账号密码，是否允许重置数据
//        params.put("loginUsername","admin");
//        params.put("loginPassword","admin");
//        params.put("resetEnable","true");
//        statViewServletRegistrationBean.setInitParameters(params);
//        return statViewServletRegistrationBean;
//    }
//}
//
