package com.wenshao.chat.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by wenshao on 2017/3/16.
 * 群组对象
 */

public class ClusterBean {
    private String cluName;         //群名称
    private String cluHead;         //群头像
    private String description;     //描述
    private long createTime;        //创建时间
    private List<UserBean> member;  //群成员
    private List<String> owner;     //群主的用户id
    private List<String> admin;     //群管理员列表

    public String getCluName() {
        return cluName;
    }

    public void setCluName(String cluName) {
        this.cluName = cluName;
    }

    public String getCluHead() {
        return cluHead;
    }

    public void setCluHead(String cluHead) {
        this.cluHead = cluHead;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<UserBean> getMember() {
        return member;
    }

    public void setMember(List<UserBean> member) {
        this.member = member;
    }

    public List<String> getOwner() {
        return owner;
    }

    public void setOwner(List<String> owner) {
        this.owner = owner;
    }

    public List<String> getAdmin() {
        return admin;
    }

    public void setAdmin(List<String> admin) {
        this.admin = admin;
    }
}
