package com.synpower.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory factory) throws BeansException {
        this.beanFactory = factory;
    }
    /**
     * 实例方法
     * 使用的时候先通过getInstance方法获取实例
     *
     * @param beanId
     * @return
     */
    public static Object getBeanById(String beanId) {
        if (null != beanFactory) {
            return beanFactory.getBean(beanId);
        }
        return null;
    }

}
