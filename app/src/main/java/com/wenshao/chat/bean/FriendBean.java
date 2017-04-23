package com.wenshao.chat.bean;

import java.util.List;

/**
 * Created by wenshao on 2017/3/16.
 * 好友对象
 */

public class FriendBean {
    private List<UserBean> friend_info_list;
    private String friend_group_name;

    public List<UserBean> getFriend_info_list() {
        return friend_info_list;
    }

    public void setFriend_info_list(List<UserBean> friend_info_list) {
        this.friend_info_list = friend_info_list;
    }

    public String getFriend_group_name() {
        return friend_group_name;
    }

    public void setFriend_group_name(String friend_group_name) {
        this.friend_group_name = friend_group_name;
    }
}
