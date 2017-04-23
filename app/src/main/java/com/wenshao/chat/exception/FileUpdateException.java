package com.wenshao.chat.exception;

/**
 * Created by wenshao on 2017/4/3.
 * 上传文件失败
 */

public class FileUpdateException extends Exception {
    public FileUpdateException(String msg){
        super(msg);
    }
    public FileUpdateException(){
        super("上传文件失败!");
    }

}
