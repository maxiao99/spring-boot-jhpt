/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.vo;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/3/14
 */
public class MenuVo {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
