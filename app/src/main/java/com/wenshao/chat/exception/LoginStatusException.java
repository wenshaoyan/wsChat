package com.wenshao.chat.exception;


/**
 * Created by wenshao on 2017/3/13.
 * 登陆状态异常  token信息错误  需要重新登录
 */

public class LoginStatusException extends Exception {
    public LoginStatusException(String msg){
        super(msg);
    }
    public LoginStatusException(){
        super("登录信息错误！");
    }

}
