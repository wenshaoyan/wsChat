package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 2017/3/13.
 *
 */
@HttpResponse(parser = JsonResponseParser.class)
public class TestBean {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
