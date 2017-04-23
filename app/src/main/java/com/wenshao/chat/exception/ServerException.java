package com.wenshao.chat.exception;

/**
 * Created by wenshao on 2017/3/13.
 * 服务器异常
 */

public class ServerException extends Exception {
    public ServerException(String msg){
        super(msg);
    }
    public ServerException(){
        super("服务器错误!");
    }
}
