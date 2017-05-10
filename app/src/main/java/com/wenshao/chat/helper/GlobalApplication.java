package com.wenshao.chat.helper;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.danikula.videocache.HttpProxyCacheServer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wenshao.chat.adapter.MessageAdapter;
import com.wenshao.chat.bean.DaoMaster;
import com.wenshao.chat.bean.DaoSession;
import com.wenshao.chat.bean.FriendBean;
import com.wenshao.chat.bean.FriendBeanDao;
import com.wenshao.chat.bean.RecentContactBean;
import com.wenshao.chat.bean.UserBean;
import com.wenshao.chat.bean.UserBeanDao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshao on 2017/4/14.
 * 全局Content对象
 */

public class GlobalApplication extends Application {
    private HttpProxyCacheServer proxy;
    private static Context context;
    private static DaoSession daoSession;
    private static MessageAdapter mMessageAdapter;


    private static Map<String,UserBean> mAllUsers;

    @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
        intiUniversal();


        //配置数据库
        setupDatabase();
    }

    //返回
    public static Context getContextObject() {
        return context;
    }


    public static HttpProxyCacheServer getProxy(Context context) {
        GlobalApplication app = (GlobalApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    public static File getVideoCacheDir(Context context) {
        return new File(context.getExternalCacheDir(), "video-cache");
    }

    private void intiUniversal() {
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs() //打印log信息
                .build();

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }


    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库wsChat.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wsChat.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    public static void setRecentContactsAdapter(MessageAdapter messageAdapter) {
        mMessageAdapter = messageAdapter;
    }

    public static void addRecentContact(RecentContactBean recentContactBean) {
        if (mMessageAdapter != null && recentContactBean != null) {
            mMessageAdapter.add(recentContactBean);
        }
    }
    public static void clearUnreadNumber(RecentContactBean recentContactBean){
        mMessageAdapter.clearUnreadNumber(recentContactBean);
    }

    public static void setFriends(List<FriendBean> friends) {

        updateFriendGroup(friends);
        updateFriendList(friends);

    }
    // 同步好友分组
    private static void updateFriendGroup(List<FriendBean> friends) {
        FriendBeanDao friendBeanDao = daoSession.getFriendBeanDao();
        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
        friendBeanDao.insertOrReplaceInTx(friends);

        List<FriendBean> oldList = friendBeanDao.queryBuilder().list();
        // 删除多余分组和分组下的好友
        oldList.removeAll(friends);
        friendBeanDao.deleteInTx(oldList);
        for (FriendBean o : oldList){
            List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.GroupId.notEq(o.getId())).list();
            userBeanDao.deleteInTx(list);
        }
    }

    // 同步好友列表
    private static void updateFriendList(List<FriendBean> friends){
        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
        ArrayList<UserBean> all = new ArrayList<>();
        for (FriendBean friendBean : friends) {
            all.addAll(friendBean.getFriend_info_list());
            userBeanDao.insertOrReplaceInTx(friendBean.getFriend_info_list());

        }
        List<UserBean> oldList = userBeanDao.queryBuilder().list();
        oldList.removeAll(all);
        userBeanDao.deleteInTx(oldList);
        mAllUsers=new HashMap<>();
        for (UserBean userBean : all){
            mAllUsers.put(userBean.getUser_id(),userBean);
        }

    }


    public static Map<String, UserBean> getAllUsers() {
        if (mAllUsers==null){
            mAllUsers=new HashMap<>();
            List<UserBean> list = getDaoInstant().getUserBeanDao().queryBuilder().list();
            for (UserBean userBean : list){
                mAllUsers.put(userBean.getUser_id(),userBean);
            }
        }
        return mAllUsers;
    }

    public static void setAllUsers(Map<String, UserBean> mAllUsers) {
        GlobalApplication.mAllUsers = mAllUsers;
    }




}
