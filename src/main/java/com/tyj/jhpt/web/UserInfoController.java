/*
 * Copyright (C) 2016 CK, Inc. All Rights Reserved.
 */

package com.tyj.jhpt.web;

import com.github.fartherp.framework.common.util.DateUtil;
import com.github.fartherp.framework.common.util.HttpClientUtils;
import com.github.fartherp.framework.common.util.JsonUtil;
import com.github.fartherp.framework.common.util.MapUtils;
import com.github.fartherp.framework.core.util.JsonResp;
import com.github.fartherp.framework.core.validate.ValidUtils;
import com.github.fartherp.framework.core.web.filter.auth.AuthWrapper;
import com.github.fartherp.framework.core.web.filter.auth.GeneralAuthWrapper;
import com.github.fartherp.framework.core.web.http.session.SessionProvider;
import com.tyj.jhpt.bo.User;
import com.tyj.jhpt.server.SystemConfig;
import com.tyj.jhpt.service.UserService;
import com.tyj.jhpt.validate.AddGroup;
import com.tyj.jhpt.validate.EditGroup;
import com.tyj.jhpt.vo.MenuVo;
import com.tyj.jhpt.vo.UserPageVo;
import com.tyj.jhpt.vo.UserVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2016/5/1
 */
@Controller
@RequestMapping(value = "user")
public class UserInfoController extends AbstractController {
    @Resource(name = "userService")
    UserService userService;

//    @Resource(name = "sessionProvider")
//    SessionProvider sessionProvider;

    @RequestMapping(value = "/login")
    public ModelAndView login(@RequestParam Map<String, Object> param) {
        ModelAndView mv;
        User user = userService.findUserByMap(param);
        if (null == user) {
            mv = new ModelAndView("redirect:/");
            mv.addObject("message", "用户名或密码错误");
            return mv;
        }
        String url = SystemConfig.getStr("auth.url");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getAuthId());
        params.put("systemId", 4);
        String json = HttpClientUtils.execute(params, url);
        GeneralAuthWrapper generalAuthWrapper = JsonUtil.fromJson(json, GeneralAuthWrapper.class);
        if (generalAuthWrapper.getStatus() != 0) {
            mv = new ModelAndView("redirect:/");
            mv.addObject("message", generalAuthWrapper.getStatusInfo());
            return mv;
        }
//        sessionProvider.setAttribute(AuthWrapper.SESSION_USER_MESSAGE, user);
//        sessionProvider.setAttribute(AuthWrapper.SESSION_AUTH_WRAPPER_MESSAGE, generalAuthWrapper.getData());
        List l = generalAuthWrapper.getData().getMenuList();
        if (l != null && !l.isEmpty()) {
            mv = new ModelAndView("common/frame");
            mv.addObject("message", JsonResp.asList().addAll(l).toJson());
            mv.addObject("userNameShow", user.getUserName());
            return mv;
        }
        mv = new ModelAndView("redirect:/");
        mv.addObject("message", "没有菜单,请找管理员");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/get_menu", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "获取用户菜单", notes = "入参格式：{\"userName\":\"admin\",\"userPassword\":\"admin\"}")
    @ApiImplicitParam(name = "vo", value = "菜单vo", required = true, dataType = "MenuVo")
    public String getMenu(@Valid @RequestBody MenuVo vo, BindingResult error) {
        ValidUtils.valid(vo, error);

        User user = userService.findUserByMap(MapUtils.toMap(vo));
        if (user == null) {
            return JsonResp.asEmpty().error("用户不存在").toJson();
        }
        String url = SystemConfig.getStr("auth.url");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getAuthId());
        params.put("systemId", 4);
        String json = HttpClientUtils.execute(params, url);
        GeneralAuthWrapper generalAuthWrapper = JsonUtil.fromJson(json, GeneralAuthWrapper.class);
        if (generalAuthWrapper.getStatus() != 0) {
            return JsonResp.asEmpty().error(generalAuthWrapper.getStatusInfo()).toJson();
        }

        List menuList = generalAuthWrapper.getData().getMenuList();
        if (menuList != null && !menuList.isEmpty()) {
            return JsonResp.asList().addAll(menuList).toJson();
        }
        return JsonResp.asEmpty().error("没有菜单,请找管理员").toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/page/list", method = {RequestMethod.GET}, produces = {"application/json"})
    @ApiOperation(value = "账户列表分页", notes = "入参格式：keyword=1&currentPage=1&limit=10")
    @ApiImplicitParam(name = "vo", value = "用户分页vo", required = true, dataType = "UserPageVo")
    public String list(UserPageVo vo) {
        List<User> l = userService.findPageUser(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/add_user", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "添加用户", notes = "入参格式：{\"userName\":\"admin\",\"realName\":\"admin\",\"password\":\"admin\",\"confirmPassword\":\"admin\",\"authId\":1}")
    @ApiImplicitParam(name = "vo", value = "用户vo", required = true, dataType = "UserVo")
    public String addUser(@Valid @RequestBody UserVo vo, BindingResult error) {
        ValidUtils.valid(vo, error, AddGroup.class);

        User u = userService.findByUserName(vo.getUserName());
        if (u != null) {
            return JsonResp.asData().error("用户名已存在,请重新添加").toJson();
        }
        User user = new User();
        BeanUtils.copyProperties(vo, user);
//        User userInfos = (User) sessionProvider.getAttribute(AuthWrapper.SESSION_USER_MESSAGE);
        user.setCreatorId(0L);
        user.setCreateTime(new Date());
        userService.saveEntitySelective(user);
        return JsonResp.asData().toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/edit_user", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "编辑用户", notes = "入参格式：{\"id\":1,\"userName\":\"admin\",\"realName\":\"admin\",\"password\":\"admin\",\"confirmPassword\":\"admin\",\"authId\":1}")
    @ApiImplicitParam(name = "vo", value = "用户vo", required = true, dataType = "UserVo")
    public String editUser(@Valid @RequestBody UserVo vo, BindingResult error) {
        ValidUtils.valid(vo, error, EditGroup.class);

        User user = userService.findByUserName(vo.getUserName());
        if (user != null && !user.getId().equals(vo.getId())) {
            return JsonResp.asData().error("用户名已存在,请重新编辑").toJson();
        }
        BeanUtils.copyProperties(vo, user);
        userService.updateEntitySelective(user);
        return JsonResp.asData().toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/del_user", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "删除用户", notes = "入参格式：id=1")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long")
    public String delUser(@RequestParam Long id) {
        User user = new User();
        user.setId(id);
        userService.delete(user);
        return JsonResp.asEmpty().toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/get_user", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "查看用户", notes = "入参格式：id:1")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long")
    public String getUser(@RequestParam Long id) {
        User user = userService.findById(id);
        return JsonResp.asData(user).toJson();
    }

    @RequestMapping(value = "/loginOut")
    public ModelAndView loginOut() {
//        sessionProvider.logout();
        return new ModelAndView("redirect:/");
    }
}
