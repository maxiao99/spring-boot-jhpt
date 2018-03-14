/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.config;

import com.github.fartherp.framework.core.web.handler.CustomizeExceptionHandler;
import com.github.fartherp.framework.core.web.handler.CustomizeExceptionHandlerResolver;
import com.github.fartherp.framework.core.web.handler.ResponseExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/3/2
 */
@Configuration
public class ExceptionConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionConfiguration.class);

    ResponseExceptionHandler responseExceptionHandler;

    @Bean(value = "responseExceptionHandler")
    @PostConstruct
    public ResponseExceptionHandler newResponseExceptionHandler() {
        ResponseExceptionHandler responseExceptionHandler = new ResponseExceptionHandler();
        this.responseExceptionHandler = responseExceptionHandler;
        return responseExceptionHandler;
    }

    @Bean(value = "customizeExceptionHandlerResolver")
    public CustomizeExceptionHandlerResolver newCustomizeExceptionHandlerResolver() {
        logger.info("Configuration Exception Resolver");
        List<CustomizeExceptionHandler> exceptions = new ArrayList<CustomizeExceptionHandler>();
        exceptions.add(responseExceptionHandler);
        CustomizeExceptionHandlerResolver resolver = new CustomizeExceptionHandlerResolver();
        resolver.setExceptions(exceptions);
        return resolver;
    }
}
