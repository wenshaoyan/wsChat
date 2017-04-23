package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by wenshao on 2017/4/3.
 * 登录接口返回值
 */
@HttpResponse(parser = JsonResponseParser.class)
public class LoginDate {
    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
