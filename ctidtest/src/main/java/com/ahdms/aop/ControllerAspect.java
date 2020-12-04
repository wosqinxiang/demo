package com.ahdms.aop;

import com.ahdms.aop.customizer.AspectAfterProcess;
import com.ahdms.aop.customizer.AspectBeforeProcess;
import com.ahdms.aop.customizer.AspectThrowableProcess;
import com.ahdms.logger.ControllerAccessLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Date;


/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 15:38
 */
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ControllerAspect {

    private AspectBeforeProcess beforeProcessCustomizer;
    private AspectThrowableProcess throwableProcessCustomizer;
    private AspectAfterProcess afterProcessCustomizer;
    private ControllerAccessLogger controllerAccessLogger;

    public ControllerAspect(AspectBeforeProcess beforeProcessCustomizer,
                            AspectThrowableProcess throwableProcessCustomizer,
                            AspectAfterProcess afterProcessCustomizer,
                            ControllerAccessLogger controllerAccessLogger) {
        this.afterProcessCustomizer = afterProcessCustomizer;
        this.beforeProcessCustomizer = beforeProcessCustomizer;
        this.throwableProcessCustomizer = throwableProcessCustomizer;
        this.controllerAccessLogger = controllerAccessLogger;
    }

    @Pointcut("execution(* com.ahdms..*Controller.*(..))" +
            "&&(@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping))"
    )
    public void aroundCut() {
    }

    @Around("aroundCut()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        Object responseObject = null;
        try {
            beforeProcess(point);
            Date startTime = new Date();
            responseObject = point.proceed();
            afterProcess(point, responseObject, startTime);
        } catch (Throwable t) {
            throwableProcess(point, t);
            throw t;
        }
        return responseObject;
    }

    private void beforeProcess(ProceedingJoinPoint point) {
        controllerAccessLogger.request(point);
        beforeProcessCustomizer.customizer(point);
    }

    private void afterProcess(ProceedingJoinPoint point, Object responseObject, Date startTime) {
        controllerAccessLogger.response(point, responseObject, startTime);
        afterProcessCustomizer.customizer(point, responseObject, startTime);
    }

    private void throwableProcess(ProceedingJoinPoint point, Throwable throwable) {
        throwableProcessCustomizer.customizer(point, throwable);
    }
}
