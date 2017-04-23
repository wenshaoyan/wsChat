package com.wenshao.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wenshao.chat.constant.UrlConstant;
import com.wenshao.chat.helper.BaseWebSocketHelper;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by wenshao on 2017/4/9.
 * WebSocketService
 */

public class WebSocketService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        URI uri = null;
        try {
            uri = new URI(UrlConstant.getWebSocketUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        BaseWebSocketHelper.setConfig(uri,getApplication());
        BaseWebSocketHelper.connect();
        super.onCreate();
    }
}
