package com.wenshao.chat.helper;

import android.app.Application;
import android.content.Context;

/**
 * Created by wenshao on 2017/4/14.
 * 全局Content对象
 */

public class GlobalApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
    }
    //返回
    public static Context getContextObject(){
        return context;
    }
}
