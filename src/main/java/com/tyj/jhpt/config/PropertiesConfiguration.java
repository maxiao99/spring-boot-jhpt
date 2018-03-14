/*
 * Copyright (c) 2017. CK. All rights reserved.
 */

package com.tyj.jhpt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * 扫描自定义属性配置
 *
 * @author: CK
 * @date: 2017/11/7
 */
@Configuration
@PropertySources({
        @PropertySource(value = {"classpath:system-config.properties"}, encoding = "utf-8"),
        @PropertySource(value = {"classpath:jdbc.properties"}, encoding = "utf-8")
})
public class PropertiesConfiguration {

}
