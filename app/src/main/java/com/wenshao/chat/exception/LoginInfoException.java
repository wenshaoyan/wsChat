package com.wenshao.chat.exception;

/**
 * Created by wenshao on 2017/4/3.
 * 用户名或密码错误
 */

public class LoginInfoException extends Exception {
    public LoginInfoException(String msg){
        super(msg);
    }
    public LoginInfoException(){
        super("用户名或密码错误！");
    }

}
