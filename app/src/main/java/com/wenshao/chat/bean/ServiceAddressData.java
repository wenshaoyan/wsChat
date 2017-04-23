package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by wenshao on 2017/3/21.
 * 服务器地址对象
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ServiceAddressData {
    private String service;
    private String webSocket;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(String webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public String toString() {
        return "ServiceAddressData{" +
                "service='" + service + '\'' +
                '}';
    }
}
