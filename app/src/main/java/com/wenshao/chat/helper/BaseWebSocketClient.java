package com.wenshao.chat.helper;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 2017/4/8.
 * WebSocket连接
 */

public class BaseWebSocketClient extends WebSocketClient {

    private Application mApplication;


    private static final String TAG = "BaseWebSocketClient";

    public BaseWebSocketClient(URI uri, Draft draft) {
        super(uri, draft);
    }

    public BaseWebSocketClient(URI uri, Application application) {
        super(uri, new Draft_17());
        mApplication = application;
    }

    public BaseWebSocketClient(URI uri, Map<String, String> map, Application mApplication) {
        super(uri, new Draft_17(), map, 5000);
        this.mApplication = mApplication;

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Intent intent = new Intent();

        intent.setAction("com.wenshao.chat.onReconnect");
        mApplication.sendBroadcast(intent);


    }

    @Override
    public void onMessage(String s) {
        // 发送广播
        Intent intent = new Intent();
        try {
            JSONObject jsonObject = new JSONObject(s);
            String eventName = jsonObject.getString("eventName");
            String message = jsonObject.getString("message");
            intent.putExtra("message", message);
            intent.setAction("com.wenshao.chat."+eventName);
            mApplication.sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClose(int i, String s, boolean b) {
        //Log.i(TAG, "onClose: "+s);


    }

    @Override
    public void onError(Exception e) {
        //Log.i(TAG, "onError: ");

    }


    public void postMsg(JSONObject jsonObject) {
        try {
            jsonObject.put("eventName", "postMsg");
            this.send(jsonObject.toString());
        } catch (WebsocketNotConnectedException e) {
            //TODO 加入待代发队列
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
