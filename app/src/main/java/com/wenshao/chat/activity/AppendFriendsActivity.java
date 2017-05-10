package com.wenshao.chat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.wenshao.chat.R;

/**
 * Created by wenshao on 2017/5/10.
 * 添加好友页面
 */

public class AppendFriendsActivity extends ToolBarActivity {
    private static final String TAG = "AppendFriendsActivity";
    private Context mContent;
    private TabHost mTabHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_append_friends);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(false);  //不显示返回按钮
        mContent=this;
        initUi();
        initData();


    }

    private void initUi() {
        mTabHost =(TabHost)findViewById(android.R.id.tabhost);//获取tabHost对象
        mTabHost.setup();//初始化TabHost组件


        LayoutInflater inflater=LayoutInflater.from(this);//声明并实例化一个LayoutInflater对象
        inflater.inflate(R.layout.tab_friends, mTabHost.getTabContentView());
        inflater.inflate(R.layout.tab_group, mTabHost.getTabContentView());
        inflater.inflate(R.layout.tab_public, mTabHost.getTabContentView());

        mTabHost.addTab(mTabHost.newTabSpec("tab01")
                .setIndicator("好友")
                .setContent(R.id.ll_tab_friend));
        mTabHost.addTab(mTabHost.newTabSpec("tab02")
                .setIndicator("群")
                .setContent(R.id.ll_tab_group));
        mTabHost.addTab(mTabHost.newTabSpec("tab03")
                .setIndicator("公众号")
                .setContent(R.id.ll_tab_public));
        TabWidget tabWidget = mTabHost.getTabWidget();
        int count = tabWidget.getChildCount();
        for(int i = 0;i<count;i++){
            View view = tabWidget.getChildTabViewAt(i);
            view.getLayoutParams().height = 120;
        }



    }
    private void initData() {

    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        getLayoutInflater().inflate(R.layout.toobar_common, toolbar);
        TextView tv_common_back = (TextView) toolbar.findViewById(R.id.tv_common_back);
        TextView tv_common_center = (TextView) toolbar.findViewById(R.id.tv_common_center);
        TextView tv_common_right = (TextView) toolbar.findViewById(R.id.tv_common_right);

        tv_common_back.setText("返回");
        tv_common_center.setText("添加");
        tv_common_right.setVisibility(View.GONE);

        tv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
