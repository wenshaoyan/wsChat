package com.wenshao.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wenshao.chat.constant.UrlConstant;
import com.wenshao.chat.helper.BaseWebSocketHelper;

import java.net.URI;
import java.net.URISyntaxException;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

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
            BaseWebSocketHelper.setConfig(uri,getApplication());
            BaseWebSocketHelper.connect();


        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }
}
