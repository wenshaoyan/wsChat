package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by wenshao on 2017/3/16.
 * 登录返回对象
 */
@HttpResponse(parser = JsonResponseParser.class)
public class LoginCallbackData {
    private UserBean userInfo;
    private List<FriendBean> friends;
    private List<ClusterBean> groups;

    public UserBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserBean userInfo) {
        this.userInfo = userInfo;
    }

    public List<FriendBean> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendBean> friends) {
        this.friends = friends;
    }

    public List<ClusterBean> getGroups() {
        return groups;
    }

    public void setGroups(List<ClusterBean> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "LoginCallbackData{" +
                "userInfo=" + userInfo +
                ", friends=" + friends +
                ", groups=" + groups +
                '}';
    }
}
