package com.wenshao.chat.exception;

/**
 * Created by wenshao on 2017/3/13.
 * 数据解析异常
 */

public class ResolveException extends Exception {
    public ResolveException(String msg){
        super(msg);
    }
    public ResolveException(){
        super("解析错误!");
    }
}
