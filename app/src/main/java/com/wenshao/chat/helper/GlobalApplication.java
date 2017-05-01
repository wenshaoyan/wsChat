package com.wenshao.chat.helper;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by wenshao on 2017/4/14.
 * 全局Content对象
 */

public class GlobalApplication extends Application {
    private HttpProxyCacheServer proxy;
    private static Context context;

    @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
        intiUniversal();
    }
    //返回
    public static Context getContextObject(){
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
}
