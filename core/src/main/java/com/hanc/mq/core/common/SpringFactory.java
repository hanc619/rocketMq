package com.hanc.mq.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Component
public class SpringFactory {
    
    private static ApplicationContext applicationContext;

    
    public <T> T getOrCreateBean(Class<T> beanClass) { 
    	String beanName = StringUtils.uncapitalize(beanClass.getSimpleName());
    	return getOrCreateBean(beanClass, beanName);
    }
    
    public <T> T getOrCreateBean(Class<T> beanClass, String beanName) { 
    	registerSingletonBean(beanName, beanClass);
    	return applicationContext.getBean(beanName, beanClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T initializeBean(T bean) {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(bean);
        return (T) beanFactory.initializeBean(bean, bean.getClass().getName());
    }


    /**
     * register bean definition with singleton scope, when the definition has existed, do nothing, or register it
     * @param beanName
     * @param beanClass
     */
    public void registerSingletonBean(String beanName, Class<?> beanClass) {
    	BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)applicationContext.getAutowireCapableBeanFactory();
    	try {
    		beanRegistry.getBeanDefinition(beanName);
    		return;
    	} catch (Exception e) {
    		GenericBeanDefinition definition =  new GenericBeanDefinition();
    		definition.setBeanClass(beanClass);
            definition.setScope(BeanDefinition.SCOPE_SINGLETON);
            beanRegistry.registerBeanDefinition(beanName, definition);
    	}
    }
    
    @Autowired
    public void setApplicationContext(ApplicationContext appCtx) {
        applicationContext = appCtx;
        Assert.isTrue(applicationContext.getAutowireCapableBeanFactory() instanceof BeanDefinitionRegistry, "autowireCapableBeanFactory should be BeanDefinitionRegistry");
    }
    
}