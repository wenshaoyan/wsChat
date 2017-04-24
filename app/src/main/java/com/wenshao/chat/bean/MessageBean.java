package com.wenshao.chat.bean;

import java.util.UUID;

/**
 * Created by wenshao on 2017/4/9.
 * 消息对象
 */

public class MessageBean {
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_AUDIO = "audio";
    public static final int LOCATION_LEFT = 1;
    public static final int LOCATION_RIGHT = 2;


    private String send_id;
    private String type;
    private String content;
    private long create_time;
    private int location;
    private UserBean userBean;
    private String sendCode;
    private int duration;  // 音频或视频的时长



    public MessageBean(){
        UUID uuid = UUID.randomUUID();
        this.sendCode = uuid.toString();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }


    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
}
