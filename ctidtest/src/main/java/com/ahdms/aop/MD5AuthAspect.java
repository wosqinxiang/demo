package com.ahdms.aop;

import com.ahdms.context.CtidContext;
import com.ahdms.context.holder.CtidContextHolder;
import com.ahdms.model.User;
import com.ahdms.utils.WebUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author qinxiang
 * @date 2020-12-04 15:36
 */
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MD5AuthAspect {

    @Pointcut("execution(* com.ahdms..*Controller.*(..))" +
            "&&@annotation(com.ahdms.aop.MD5Auth)" +
            "&&args(user,..)"
    )
    public void aroundCut(User user) {
    }

    @Before(value="aroundCut(user)")
    public void beforeApi(User user){
        try {
        if("aaa".equals(user.getName())){

            throw new Exception("aassaaass");

        }

        CtidContext ctidContext = CtidContext.builder().user(user).ip(WebUtils.getIP()).build();

        CtidContextHolder.setContext(ctidContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Around(value="aroundCut(user)",argNames = "user")
    public Object aroundApi(ProceedingJoinPoint point,User user) throws Throwable {
        Object responseObject = null;
        try {
            Date startTime = new Date();
            System.out.println(">>>>>>执行前");
            HttpServletRequest request = WebUtils.getRequest();
            Object[] args = point.getArgs();
            if("aaa".equals(user.getName())){
                throw new Exception("aassaaass");
            }
            user = new User();
            user.setName(point.getSignature().getName());
            user.setDesc(request.getRequestURI());
            CtidContext ctidContext = CtidContext.builder().user(user).ip(WebUtils.getIP()).build();

            CtidContextHolder.setContext(ctidContext);

            responseObject = point.proceed();
            return responseObject;
        } catch (Throwable t) {
            throw t;
        } finally {
            CtidContextHolder.removeContext();
        }

    }

}
