/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.config;

import com.github.fartherp.framework.core.bean.ServiceLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2017/11/8
 */
@Configuration
public class InitSystemContext implements ApplicationContextAware {
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceLocator.setFactory(applicationContext);
    }
}
