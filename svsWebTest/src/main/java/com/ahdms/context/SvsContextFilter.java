//package com.ahdms.context;
//
//import com.ahdms.config.svs.SvsProperties;
//import com.ahdms.context.holder.SvsContextHolder;
//import com.ahdms.framework.core.context.holder.ExtContextHolders;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collection;
//
///**
// * @author qinxiang
// * @date 2020-12-25 14:34
// */
//@Slf4j
//@Component
//public class SvsContextFilter extends HttpFilter {
//
//    @Autowired
//    private SvsProperties svsProperties;
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            String account = request.getHeader(HeaderConstants.SVS_USER_ID);
//            Collection<String> accounts = svsProperties.getAccounts();
//            boolean b = accounts.stream().anyMatch(account::equals);
//            if(!b){
//                response.getWriter().write("华东师大的耗时");
//            }
//            SvsContext svsContext = SvsContext.builder()
//                    .account(account)
//                    .build();
//
//            SvsContextHolder.setContext(svsContext);
//            chain.doFilter(request, response);
//        } finally {
//            SvsContextHolder.removeContext();
//            ExtContextHolders.clear();
//        }
//    }
//
//
//}
