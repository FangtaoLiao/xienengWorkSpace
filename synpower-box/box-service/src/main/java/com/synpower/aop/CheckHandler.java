package com.synpower.aop;

import com.synpower.lang.ServiceException;
import com.synpower.util.annotation.NotNull;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 各种参数检查
 *
 * @author SP0013
 * @date 2017/10/31
 */

public class CheckHandler {

    private static final String NULL = "参数不能为空";
    @Before("execution(* com.synpower.serviceImpl..*(..))")
    public void checkNotNull(JoinPoint point) throws ServiceException{
       /**
       *
       *@author SP0013
       *@method checkNotNull 检查参数是否为空
       *@param [point]
       *@return void
       *@Date 2017/10/31
       */
        Object [] paras = point.getArgs();//获取参数
        if(paras==null || paras.length==0) {
            return;
        }
        String methodName = point.getSignature().getName();
        Method method = getMethodByClassAndName(point.getTarget().getClass(),methodName);
        Annotation[][] parameterAnnotations =  method.getParameterAnnotations();
        /**
         * 定位有NOTNULL注解的参数位置
         */
        int paraAt = 0;
        for (Annotation[] annotations : parameterAnnotations) {
               if (annotations!=null && annotations.length > 0) {

                   for (Annotation annotation : annotations) {
                       if (annotation.annotationType()==NotNull.class) {
                            Object para = paras[paraAt];
                           if (para==null) {
                               throw new ServiceException(NULL);
                           }
                           if (para instanceof Map) {
                               if(((Map) para).isEmpty()) {
                                   throw  new ServiceException(NULL);
                               }
                           } else if(para instanceof List) {
                               if(((List) para).isEmpty()) {
                                   throw  new ServiceException(NULL);
                               }
                           } else if(para instanceof String) {
                               String tmp = (String) para;
                               if(StringUtils.isBlank(tmp)) {
                                   throw  new ServiceException(NULL);
                               }
                           } else if(para instanceof Set) {
                               if(((Set) para).isEmpty()) {
                                   throw  new ServiceException(NULL);
                               }
                           }
                       }
                   }
               }
            paraAt++;
        }
    }

    private Method getMethodByClassAndName(Class c , String methodName){
        /**
        *
        *@author SP0013
        *@method getMethodByClassAndName 通过反射获取当前方法,根据类和方法名得到方法
        *@param [c, methodName]
        *@return java.lang.reflect.Method
        *@Date 2017/10/31
        */
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if(method.getName().equals(methodName)){
                return method ;
            }
        }
        return null;
    }
}
