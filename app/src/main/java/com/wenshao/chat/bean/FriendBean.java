package com.wenshao.chat.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wenshao on 2017/3/16.
 * 好友对象
 */
@Entity
public class FriendBean {
    @Id
    private long id;
    @Transient
    private List<UserBean> friend_info_list;

    private String friend_group_name;

    @Generated(hash = 155315197)
    public FriendBean(long id, String friend_group_name) {
        this.id = id;
        this.friend_group_name = friend_group_name;
    }

    @Generated(hash = 152145004)
    public FriendBean() {
    }

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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
