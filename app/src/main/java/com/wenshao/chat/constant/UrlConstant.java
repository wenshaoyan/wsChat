package com.wenshao.chat.constant;

import android.util.Log;

/**
 * Created by wenshao on 2017/3/16.
 * url地址  单例模式
 */

public class UrlConstant {
    // 配置服务器地址
    private final static String SERVICE_ADDRESS ="http://123.207.55.204:8081";

    // 获取请求服务器地址
    public final static String GET_ADDRESS = SERVICE_ADDRESS+"/config/get";
    // 图片上传服务器地址
    public final static String IMAGE_UPLOAD = SERVICE_ADDRESS+"/upload/image";
    // 音频上传服务器地址
    public final static String AUDIO_UPLOAD = SERVICE_ADDRESS+"/upload/audio";
    // 其他上传服务器地址
    public final static String FILE_UPLOAD = SERVICE_ADDRESS+"/upload/file";



    // 登录接口
    private final static String mLogin = "/users/login";
    // 初始化
    private final static String mInit = "/users/init";

    // 获取好友列表
    private final static String mUserInfo = "/users/userInfo";
    // 获取聊天室的消息列表
    private final static String mMessageFriend = "/users/message/friend";


    private static final UrlConstant urlConstant = new UrlConstant();
    private static String serviceUrl;
    private static String webSocketUrl;




    private UrlConstant(){

    }

    public static UrlConstant getInstance(){
        return urlConstant;
    }

    public void setServiceUrl(String url){
        serviceUrl=url;

    }
    public String getServiceUrl(){
        return serviceUrl;
    }

    public static String getWebSocketUrl() {
        return webSocketUrl;
    }

    public static void setWebSocketUrl(String webSocketUrl) {
        UrlConstant.webSocketUrl = webSocketUrl;
    }

    public String login(){
        return getServiceUrl()+mLogin;
    }
    public String userInfo(){
        return getServiceUrl()+mUserInfo;
    }
    public String messageFriend(){
        return getServiceUrl()+mMessageFriend;
    }

    public String init(){
        return getServiceUrl()+mInit;
    }


}
