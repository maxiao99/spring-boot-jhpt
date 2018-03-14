/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.config;

import com.github.fartherp.framework.core.orm.mybatis.annotation.SqlMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 1: 配置事务环境
 * 2: 扫描mybatis注解配置的类
 * 3: 创建SqlSessionFactory对象
 * 4: 扫描mybatis对应的XML文件
 *
 * @author: CK
 * @date: 2017/11/7
 */
@Configuration
@ConditionalOnClass({ EnableTransactionManagement.class}) // 该注解的参数对应的类必须存在，否则不解析该注解修饰的配置类
@AutoConfigureAfter({ DataBaseConfiguration.class })
@MapperScan(basePackages = "com.tyj.jhpt.dao", annotationClass = SqlMapper.class)
public class MybatisConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(MybatisConfiguration.class);

    @Resource
    DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        logger.info("Configuration Mybatis SqlSessionFactory");
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        //全局配置
        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource("classpath:sqlMapConfig.xml"));

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
        return sessionFactory.getObject();
    }
}
