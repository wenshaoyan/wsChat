package com.wenshao.chat.listener;

import com.wenshao.chat.bean.MessageBean;

/**
 * Created by wenshao on 2017/4/9.
 * WebSocket监听的事件
 */

public abstract class WsEventListener {
    // 重新连接
    public abstract void reconnect();
    // 聊天室新消息
    public void newMsg(MessageBean messageBean){

    }
    // 聊天室发送消息响应事件
    public void responseMsg(String str){

    }

}
