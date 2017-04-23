package com.wenshao.chat.bean;

import com.wenshao.chat.constant.SelfConstant;
import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshao on 2017/4/9.
 * 请求聊天室的消息返回数据
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MessageData {
    private List<MessageBean> messageList;
    private List<UserBean> userInfoList;

    public List<MessageBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageBean> messageList) {
        this.messageList = messageList;
    }

    public List<UserBean> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserBean> userInfoList) {
        this.userInfoList = userInfoList;
    }


    /**
     * 数据规整
     */
    public void integration() {
        Map<String, UserBean> myMap = new HashMap<>();
        for (UserBean userBean : userInfoList) {
            myMap.put(userBean.getUser_id(), userBean);
        }
        for (MessageBean messageBean : messageList){
            String user_id = messageBean.getSend_id();
            if (user_id!=null && myMap.containsKey(user_id)) {
                messageBean.setUserBean(myMap.get(user_id));
                if (SelfConstant.getUserBean()!=null && user_id.equals(SelfConstant.getUserBean().getUser_id())){
                    messageBean.setLocation(MessageBean.LOCATION_RIGHT);
                }else{
                    messageBean.setLocation(MessageBean.LOCATION_LEFT);
                }
            }

        }

    }


}
