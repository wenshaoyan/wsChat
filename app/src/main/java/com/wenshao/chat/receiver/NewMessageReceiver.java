package com.wenshao.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wenshao.chat.listener.WsEventListener;

/**
 * Created by wenshao on 2017/4/9.
 * 新消息广播接收者
 */

public class NewMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "NewMessageReceiver";
    private WsEventListener wsEventListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if ("com.wenshao.chat.onMessage".equals(action)){
            String message = intent.getStringExtra("message");
            if (wsEventListener !=null){
                wsEventListener.newMessage(message);
            }
            Log.i(TAG, "onReceive: "+message);
        }else if ("com.wenshao.chat.onReconnect".equals(action)){
            wsEventListener.reconnect();
        }


    }
    /**
     * 监听广播接收器的接收到的数据
     * @param wsEventListener  事件接口
     */
    public void setOnWsEventListener(WsEventListener wsEventListener) {
        this.wsEventListener = wsEventListener;

    }
}
