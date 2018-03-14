/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/3/14
 */
@Controller
public class IndexController {

    @RequestMapping(value = "index", produces = {"text/html"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = "op_manage", produces = {"text/html"})
    public String pageOpManage() {
        return "op_manage";
    }
}
