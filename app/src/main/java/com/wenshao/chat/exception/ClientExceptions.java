package com.wenshao.chat.exception;

/**
 * Created by wenshao on 2017/4/3.
 * 用户端导致的异常
 */

public class ClientExceptions{

    private static final ClientExceptions clientExceptions = new ClientExceptions();

    private ClientExceptions(){

    }
    public static ClientExceptions getInstance(){
        return clientExceptions;
    }


    // 错误错误异常
    public class ParamsException extends Exception{
        public ParamsException(String msg){
            super(msg);
        }
    }







}
