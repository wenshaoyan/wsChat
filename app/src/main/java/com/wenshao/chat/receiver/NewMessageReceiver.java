package com.wenshao.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.wenshao.chat.bean.MessageBean;
import com.wenshao.chat.listener.WsEventListener;

import static android.R.attr.data;

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
        if ("com.wenshao.chat.onReconnect".equals(action)){
            wsEventListener.reconnect();
        }else{
            if (wsEventListener==null){
                return;
            }
            String message = intent.getStringExtra("message");
            if ("com.wenshao.chat.responseMsg".equals(action)) {
                wsEventListener.responseMsg(message);
            } else if ("com.wenshao.chat.newMsg".equals(action)) {
                Log.i(TAG, "onReceive: "+message);
                MessageBean messageBean = new Gson().fromJson(message, MessageBean.class);
                wsEventListener.newMsg(messageBean);
            }

        }



    }

    /**
     * 监听广播接收器的接收到的数据
     *
     * @param wsEventListener 事件接口
     */
    public void setOnWsEventListener(WsEventListener wsEventListener) {
        this.wsEventListener = wsEventListener;

    }
}
