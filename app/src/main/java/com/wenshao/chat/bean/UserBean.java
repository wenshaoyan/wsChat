package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by wenshao on 2017/3/16.
 * 用户信息对象
 *
 */
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String user_id;
    private String head;
    private String ipv4;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
