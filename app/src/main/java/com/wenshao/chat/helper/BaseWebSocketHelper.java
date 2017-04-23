package com.wenshao.chat.helper;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import com.wenshao.chat.util.SpUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;

/**
 * Created by wenshao on 2017/4/8.
 * WebSocket连接帮助类
 */

public class BaseWebSocketHelper {
    private static BaseWebSocketClient mWs;
    private static final String TAG = "BaseWebSocketHelper";
    private static URI mUri;
    private static Application mApplication;



    private BaseWebSocketHelper() {

    }

    public static void setConfig(URI uri, Application application) {
        mUri = uri;
        mApplication = application;

    }

    public static BaseWebSocketClient getIntent() {
        return mWs;

    }

    public static void connect() {
        if (mWs == null) {
            Log.i(TAG, "connect: "+Thread.currentThread().getId());
            HashMap<String, String> map = new HashMap<>();
            String token = SpUtil.getToken(mApplication, mUri.getHost());
            map.put("token",token);
            mWs = new BaseWebSocketClient(mUri,map,mApplication){
                @Override
                public void onError(Exception e) {
                    super.onError(e);

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    super.onClose(i, s, b);
                    SystemClock.sleep(4000);
                    mWs.getConnection().close();
                    mWs=null;
                    BaseWebSocketHelper.connect();

                }
            };
            mWs.connect();

        }
    }



}
