package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by wenshao on 2017/4/16.
 * 文件上传返回对象
 */
@HttpResponse(parser = JsonResponseParser.class)
public class FileUploadData {
    private List<FileUploadBean> msg;

    public List<FileUploadBean> getMsg() {
        return msg;
    }

    public void setMsg(List<FileUploadBean> msg) {
        this.msg = msg;
    }
}
