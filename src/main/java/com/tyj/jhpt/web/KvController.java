/*
 * Copyright (C) 2016 CK, Inc. All Rights Reserved.
 */

package com.tyj.jhpt.web;

import com.github.fartherp.framework.core.kv.handler.KvHandlerSupport;
import com.github.fartherp.framework.core.kv.service.KvService;
import com.github.fartherp.framework.core.kv.vo.KvVo;
import com.github.fartherp.framework.core.util.JsonResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2016/1/23
 */
@Controller
@RequestMapping(value = "/kv")
public class KvController {
    public static final int HINT_MAX = 10;

    @Resource
    KvService kvService;

    @RequestMapping(value = "/hint", method = {RequestMethod.POST}, produces = {"application/json"})
    @ResponseBody
    public String hint(@RequestBody KvVo vo) {
        KvHandlerSupport support = kvService.lookup(vo.getModule());
        if (!support.isExist()) {
            return JsonResp.asData().error("无此模块").toJson();
        }
        support.setDefaultValue(vo.getDefaultValue());
        List<Map<Object, Object>> list = kvService.all(support, vo.convertMap());
        return JsonResp.asList().addAll(list).toJson();
    }

    @RequestMapping(value = "/fuzzy", method = {RequestMethod.POST}, produces = {"application/json"})
    @ResponseBody
    public String fuzzy(@RequestBody KvVo vo) {
        KvHandlerSupport support = kvService.lookup(vo.getModule());
        if (!support.isExist()) {
            return JsonResp.asData().error("无此模块").toJson();
        }
        String key = vo.getKey() != null ? vo.getKey() : "";
        List<Map<Object, Object>> list = kvService.fuzzy(support, key, vo.convertMap(), HINT_MAX);
        return JsonResp.asList().addAll(list).toJson();
    }
}
