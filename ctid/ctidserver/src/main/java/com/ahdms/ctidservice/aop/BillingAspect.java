package com.ahdms.ctidservice.aop;

import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.bean.model.KeyInfo;
import com.ahdms.ctidservice.context.CtidContextUtils;
import com.ahdms.ctidservice.exception.ApiException;
import com.ahdms.ctidservice.util.StringUtils;
import com.ahdms.jf.client.JFClient;
import com.ahdms.jf.model.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author qinxiang
 * @date 2021-01-13 10:38
 */
@Aspect
@Component
@Slf4j
public class BillingAspect {

    @Autowired
    private JFClient jfClient;

    @Pointcut("@annotation(com.ahdms.ctidservice.aop.Billing)")
    public void cutService() {

    }

    @Around("@annotation(billing)")
    public CtidResult before(ProceedingJoinPoint point,Billing billing){
        try {
            JFChannelEnum channel = billing.channel();
            JFServiceEnum service = billing.service();
            CtidResult result = new CtidResult();
            JFInfoModel jfData = new JFInfoModel();
            String key = "";
            String comment = "";
            if(channel == JFChannelEnum.APP_SDK){
                KeyInfo keyInfo = CtidContextUtils.getKeyInfo();
                JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), service, channel,CtidContextUtils.getRequestIp());
                if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                    log.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                    return CtidResult.error(jfResult.getMessage());
                }
                jfData = jfResult.getData();
                result = (CtidResult) point.proceed();
                key = jfData.getUserServiceAccount();
                comment = keyInfo.getAppId();
            }
            String bsn = CtidContextUtils.getBsn();
            jfClient.send(key, service, channel, result.getCode(), result.getMessage(), bsn, jfData.getSpecialCode(), comment);

            //记录日志
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        throw new ApiException("服务器出错");

    }

}
