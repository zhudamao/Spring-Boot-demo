package com.example.demo.Aop;


import com.alibaba.fastjson.JSON;
import com.example.demo.service.GoodsService;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Component
@Aspect
public class WebAspect {

    static  final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GoodsService.class);

    @Pointcut("execution(public * com.example.demo.Controller..*(..))")
    public void executePackage(){

    }


    @Before("executePackage()")
    public void beforeAdvice(){

    }

    @After("executePackage()")
    public void afterAdvice(){

    }


    @Around("executePackage()")
    public Object doAround(ProceedingJoinPoint point) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURI().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        String queryString = request.getQueryString();
        Object[] args = point.getArgs();

        String params = "";
        if (args.length > 0){
            if ("POST".equals(method)){
                Object object = args[0];
                params = JSON.toJSONString(object);
            }else if ("GET".equals(method)){
                params = queryString;
            }
        }

        log.info("host:"+ request.getRemoteHost() +":" +method + " " + url + " " + params);
        Object result = null;

        try {
            result = point.proceed();
            log.info(JSON.toJSON(result));
            return result;
        }catch (Throwable e){
            e.printStackTrace();
        }finally {

        }

        return  result;
    }

}
