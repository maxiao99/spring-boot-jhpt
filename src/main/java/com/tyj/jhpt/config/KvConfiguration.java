/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.config;

import com.github.fartherp.framework.core.kv.dao.KvDao;
import com.github.fartherp.framework.core.kv.dao.impl.KvDaoImpl;
import com.github.fartherp.framework.core.kv.service.KvService;
import com.github.fartherp.framework.core.kv.service.impl.KvServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2017/11/8
 */
@Configuration
@AutoConfigureAfter({ DataBaseConfiguration.class, InitSystemContext.class })
public class KvConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(KvConfiguration.class);

    @Resource
    DataSource dataSource;

    KvDao kvDao;

    @Bean(value = "kvDao")
    @PostConstruct
    public KvDao newKvDao() {
        logger.info("Configuration newKvDao");
        KvDaoImpl kvDao = new KvDaoImpl();
        kvDao.setDataSource(dataSource);
        this.kvDao = kvDao;
        return kvDao;
    }

    @Bean(value = "kvService")
    public KvService newKvService() {
        logger.info("Configuration newKvService");
        KvServiceImpl kvService = new KvServiceImpl();
        kvService.setDao(kvDao);
        return kvService;
    }
}
