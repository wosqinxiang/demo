package com.ahdms.billing.aop;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ManageLogMapper;
import com.ahdms.billing.model.ManageLog;
import com.ahdms.billing.utils.SessionUtil;
import com.ahdms.billing.utils.UUIDGenerator;

@Aspect
@Component
public class SysLogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
	
	@Autowired
	private ManageLogMapper manageLogMapper;
	
	//通过该注解来判断该接口请求是否进行cut
    @Pointcut("@annotation(com.ahdms.billing.aop.SysLog)")
    public void cutController() {
    	
    }
    
    @Around("cutController()")
    public Object recordSysLog(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();

        ServletRequestAttributes sra = (ServletRequestAttributes) ra;

        HttpServletRequest request = sra.getRequest();

        HttpServletResponse response = sra.getResponse();

        ManageLog manageLog = new ManageLog();
        manageLog.setId(UUIDGenerator.getUUID());
        manageLog.setOperationtime(new Date());
        
        String userName = SessionUtil.getUserName(request.getSession());
        manageLog.setOperator(userName);
        
        Signature signature =  joinPoint.getSignature();//方法签名
		Method method1 = ( (MethodSignature)signature ).getMethod(); 
		Method method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method1.getParameterTypes());

		SysLog rateLimit = method.getAnnotation(SysLog.class);
		String comment = rateLimit.comment();
		manageLog.setComment(comment);
        
		Result result = null;
		
        try {
            result = (Result) joinPoint.proceed();
            manageLog.setResult(result.getCode());
            manageLog.setMessage(result.getMessage());

        } catch (Exception e) {
        	manageLog.setResult(1);
            manageLog.setMessage(e.getMessage());
        }
        manageLogMapper.insertSelective(manageLog);
        return result;
    }

}
