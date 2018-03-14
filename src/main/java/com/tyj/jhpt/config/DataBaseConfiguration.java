/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 1: 配置数据源
 * 2: 配置事务管理器
 *
 * @author: CK
 * @date: 2017/11/7
 */
@Configuration
@EnableTransactionManagement // 开启注解事务管理，等同于xml配置文件中的 <tx:annotation-driven />
public class DataBaseConfiguration implements TransactionManagementConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseConfiguration.class);

    DataSource dataSource;

    @Value("${jdbc.driverClassName}")
    String driverClassName;

    @Value("${jdbc.url}")
    String url;

    @Value("${jdbc.username}")
    String username;

    @Value("${jdbc.password}")
    String password;

    @Value("${jdbc.initialSize}")
    Integer initialSize;

    @Value("${jdbc.maxActive}")
    Integer maxActive;

    @Value("${jdbc.maxIdle}")
    Integer maxIdle;

    @Value("${jdbc.minIdle}")
    Integer minIdle;

    @Value("${jdbc.maxWait}")
    Long maxWait;

    @Value("${jdbc.removeAbandoned}")
    Boolean removeAbandoned;

    @Value("${jdbc.removeAbandonedTimeout}")
    Integer removeAbandonedTimeout;

    @Value("${jdbc.timeBetweenEvictionRunsMillis}")
    Long timeBetweenEvictionRunsMillis;

    @Value("${jdbc.minEvictableIdleTimeMillis}")
    Long minEvictableIdleTimeMillis;

    @Value("${jdbc.validationQuery}")
    String validationQuery;

    @Value("${jdbc.testWhileIdle}")
    Boolean testWhileIdle;

    @Value("${jdbc.testOnBorrow}")
    Boolean testOnBorrow;

    @Value("${jdbc.testOnReturn}")
    Boolean testOnReturn;

    @Value("${jdbc.poolPreparedStatements}")
    Boolean poolPreparedStatements;

    @Value("${jdbc.maxPoolPreparedStatementPerConnectionSize}")
    Integer maxPoolPreparedStatementPerConnectionSize;

    @Value("${jdbc.filters}")
    String filters;

    @Bean("dataSource")
    @PostConstruct
    public DataSource newDataSource() throws SQLException {
        logger.info("Configuration Write DruidDataSource");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxWait(maxWait);
        dataSource.setMinIdle(minIdle);
        dataSource.setRemoveAbandoned(removeAbandoned);
        dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        dataSource.setFilters(filters);
        dataSource.init();
        this.dataSource = dataSource;
        return dataSource;
    }

    @Bean("transactionManager")
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource);
    }
}
