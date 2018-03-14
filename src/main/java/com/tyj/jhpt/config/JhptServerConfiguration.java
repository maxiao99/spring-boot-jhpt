/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.config;

import com.tyj.jhpt.server.DeviceManagerServer;
import com.tyj.jhpt.server.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/3/14
 */
@Configuration
public class JhptServerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JhptServerConfiguration.class);

    DeviceManagerServer server;

    Thread shutdownHook;

    @PostConstruct
    public void init() {
        SystemConfig.init("system-config.properties");
        server = new DeviceManagerServer(SystemConfig.getInt("socketServerPort"));
        server.start();

        if (shutdownHook == null) {
            shutdownHook = new ShutdownHook();
        }
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    @PreDestroy
    public void destroy(){
        if (server != null) {
            server.close();
        }
    }

    public class ShutdownHook extends Thread {

        public void run() {
            try {
                if (JhptServerConfiguration.this.server != null) {
                    JhptServerConfiguration.this.server.close();
                }
            } catch (Throwable ex) {
                logger.error("{}", ex);
            } finally {

            }
        }
    }
}
