package com.wenshao.chat.bean;

import com.wenshao.chat.util.JsonResponseParser;
import com.wenshao.chat.util.TimeUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.xutils.http.annotation.HttpResponse;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

import static android.R.attr.id;
import static android.R.attr.thickness;

import org.greenrobot.greendao.DaoException;

/**
 * Created by wenshao on 2017/3/31.
 * 最近联系人对象
 */
@Entity
@HttpResponse(parser = JsonResponseParser.class)
public class RecentContactBean {

    @Id(autoincrement = true)
    private long id;
    @NotNull
    private String lastContent; //最后一次回复的内容
    @NotNull
    private long lastTime;    // 最后一次回复的时间
    @NotNull
    private int unreadNumber; //未读消息总数
    @NotNull
    private String type;
    @NotNull
    private String user_id;
    @ToOne(joinProperty="user_id")
    private UserBean userBean;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1039886482)
    private transient RecentContactBeanDao myDao;
    @Generated(hash = 1618597290)
    private transient String userBean__resolvedKey;



    @Generated(hash = 1445707080)
    public RecentContactBean(long id, @NotNull String lastContent, long lastTime,
            int unreadNumber, @NotNull String type, @NotNull String user_id) {
        this.id = id;
        this.lastContent = lastContent;
        this.lastTime = lastTime;
        this.unreadNumber = unreadNumber;
        this.type = type;
        this.user_id = user_id;
    }

    @Generated(hash = 1361125118)
    public RecentContactBean() {
    }
    public RecentContactBean(MessageBean messageBean){
        this(messageBean,0);
    }
    public RecentContactBean(MessageBean messageBean,int unreadNumber){
        this.user_id=messageBean.getReceive_id();
        this.lastContent=messageBean.getContent();
        this.lastTime=messageBean.getCreate_time();
        this.lastTime=messageBean.getCreate_time();
        this.unreadNumber=unreadNumber;
        this.type=messageBean.getType();
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

        return TimeUtil.getNewChatTime(getLastTime());
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
    // 未读消息自增 i
    public void autoUnreadNumber(int i){
        this.unreadNumber=this.unreadNumber+i;
    }
    // 替换最后相关消息的内容
    public void replace(RecentContactBean temp){
        if (temp!=null){
            if (temp.getUnreadNumber()==0){
                this.setUnreadNumber(0);
            }else{
                this.autoUnreadNumber(temp.getUnreadNumber());
            }
            this.setLastTime(temp.getLastTime());
            this.setLastContent(temp.getLastContent());
        }

    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1550566278)
    public UserBean getUserBean() {
        String __key = this.user_id;
        if (userBean__resolvedKey == null || userBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserBeanDao targetDao = daoSession.getUserBeanDao();
            UserBean userBeanNew = targetDao.load(__key);
            synchronized (this) {
                userBean = userBeanNew;
                userBean__resolvedKey = __key;
            }
        }
        return userBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 251279504)
    public void setUserBean(@NotNull UserBean userBean) {
        if (userBean == null) {
            throw new DaoException(
                    "To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.userBean = userBean;
            user_id = userBean.getUser_id();
            userBean__resolvedKey = user_id;
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
    @Generated(hash = 1238815955)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecentContactBeanDao() : null;
    }
}
