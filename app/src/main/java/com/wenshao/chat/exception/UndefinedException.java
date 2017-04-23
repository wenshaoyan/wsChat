package com.wenshao.chat.exception;

/**
 * Created by wenshao on 2017/4/3.
 * 未知的异常
 */

public class UndefinedException extends Exception {
    public UndefinedException(String msg){
        super(msg);

    }
    public UndefinedException(){
        super("未知错误！");
    }

}
