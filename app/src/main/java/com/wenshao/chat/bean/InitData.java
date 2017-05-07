package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by wenshao on 2017/5/7.
 * 初始化接口数据
 */
@HttpResponse(parser = JsonResponseParser.class)
public class InitData {
    private List<FriendBean> friends;
    //private List<ClusterBean> groups;


    public List<FriendBean> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendBean> friends) {
        this.friends = friends;
    }
}
