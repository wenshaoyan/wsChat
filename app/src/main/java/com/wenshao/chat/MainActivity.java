package com.wenshao.chat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wenshao.chat.activity.LoginActivity;
import com.wenshao.chat.activity.ToolBarActivity;
import com.wenshao.chat.bean.TestBean;
import com.wenshao.chat.constant.SpConstant;
import com.wenshao.chat.util.HttpUtil;
import com.wenshao.chat.util.SpUtil;


import java.util.HashMap;
import java.util.Map;

import org.xutils.common.Callback.CommonCallback;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private String url;
    private Application application;
    private String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        application = getApplication();

        Application application = getApplication();

        initUi();
        initData();
    }

    private void initUi() {
        /*Button test = (Button) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTestClick();
            }
        });*/

    }

    private void initData() {
        // url = "http://192.168.1.38:8080/test/a";
        // 判断本地是否登录过
        String name = SpUtil.getString(mContext, SpConstant.LOCAL_USER_NAME, null);
        String password = SpUtil.getString(mContext, SpConstant.LOCAL_USER_PASSWORD, null);
        if (name != null && password != null) {
            // TODO  请求登录接口 自动登陆
            startActivity(new Intent(mContext, LoginActivity.class));
        }else{
            // 跳转至登录页面
            startActivity(new Intent(mContext, LoginActivity.class));

        }

    }


}
