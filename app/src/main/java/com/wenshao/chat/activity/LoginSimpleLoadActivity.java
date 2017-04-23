package com.wenshao.chat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wenshao.chat.R;
import com.wenshao.chat.util.ToastUtil;

/**
 * Created by wenshao on 2017/3/17.
 * 正在登陆  不需要加载网络数据
 */

public class LoginSimpleLoadActivity extends Activity {
    private Context mContext;
    private String TAG = "LoginSimpleLoadActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;


        setContentView(R.layout.activity_login_simple_load);
        //这里Handler的postDelayed方法，等待10000毫秒在执行run方法。
        //在Activity中我们经常需要使用Handler方法更新UI或者执行一些耗时事件，
        //并且Handler中post方法既可以执行耗时事件也可以做一些UI更新的事情，比较好用，推荐使用
        /*new Handler().postDelayed(new Runnable() {
            public void run() {
                //等待10000毫秒后销毁此页面，并提示登陆成功
                LoginSimpleLoadActivity.this.finish();
                ToastUtil.show(mContext, "网络连接超时");
            }
        }, 10000);*/


    }

    // 监听返回
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
