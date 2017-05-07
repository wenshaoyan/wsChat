package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

import static android.os.Build.VERSION_CODES.N;
import org.greenrobot.greendao.DaoException;

/**
 * Created by wenshao on 2017/3/16.
 * 用户信息对象
 */
@Entity
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    private String name;
    @Id
    private String user_id;
    @NotNull
    private String head;
    private String ipv4;
    private long groupId;
    @ToOne(joinProperty ="groupId")
    private FriendBean friendBean;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 83707551)
    private transient UserBeanDao myDao;
    @Generated(hash = 1532474166)
    private transient Long friendBean__resolvedKey;

    @Generated(hash = 1652161527)
    public UserBean(@NotNull String name, String user_id, @NotNull String head,
            String ipv4, long groupId) {
        this.name = name;
        this.user_id = user_id;
        this.head = head;
        this.ipv4 = ipv4;
        this.groupId = groupId;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1977769575)
    public FriendBean getFriendBean() {
        long __key = this.groupId;
        if (friendBean__resolvedKey == null
                || !friendBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FriendBeanDao targetDao = daoSession.getFriendBeanDao();
            FriendBean friendBeanNew = targetDao.load(__key);
            synchronized (this) {
                friendBean = friendBeanNew;
                friendBean__resolvedKey = __key;
            }
        }
        return friendBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1915125175)
    public void setFriendBean(@NotNull FriendBean friendBean) {
        if (friendBean == null) {
            throw new DaoException(
                    "To-one property 'groupId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.friendBean = friendBean;
            groupId = friendBean.getId();
            friendBean__resolvedKey = groupId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1491512534)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserBeanDao() : null;
    }
}
