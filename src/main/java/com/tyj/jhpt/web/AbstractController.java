/*
 * Copyright (C) 2016 CK, Inc. All Rights Reserved.
 */

package com.tyj.jhpt.web;

import com.github.fartherp.framework.core.kv.service.KvService;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2016/4/4
 */
public abstract class AbstractController {

    @Resource(name = "kvService")
    KvService kvService;

}
