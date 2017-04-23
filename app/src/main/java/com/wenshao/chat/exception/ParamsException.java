package com.wenshao.chat.exception;

/**
 * Created by wenshao on 2017/3/13.
 * 参数错误异常
 */

public class ParamsException extends Exception {
    public ParamsException(String msg){
        super(msg);
    }
    public ParamsException(){
        super("参数错误!");
    }
}
