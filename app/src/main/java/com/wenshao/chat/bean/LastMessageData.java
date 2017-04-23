package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by wenshao on 2017/3/31.
 * message中的消息
 */
@HttpResponse(parser = JsonResponseParser.class)
public class LastMessageData {
    private UserBean userBean;  //用户对象
    private String lastContent; //最后一次回复的内容
    private long lastTime;    // 最后一次回复的时间
    private int unreadNumber; //未读消息总数


    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    public long getLastTime() {
        return lastTime;
    }
    public String getLastTimeString(){

        return "11:11";
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }
}
