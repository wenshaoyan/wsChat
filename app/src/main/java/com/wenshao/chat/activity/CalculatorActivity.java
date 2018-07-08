package com.wenshao.chat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wenshao.chat.R;
import com.wenshao.chat.bean.UserBean;

import static com.wenshao.chat.R.id.tv_back;
import static com.wenshao.chat.R.id.tv_user_name;

/**
 * Created by wenshao on 2017/6/4.
 * 计算器页面
 */

public class CalculatorActivity extends ToolBarActivity {
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        mContext = this;
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(false);  //不显示返回按钮
        initUi();

    }

    private void initUi() {

        //EditText ed_input_math = (EditText) findViewById(R.id.ed_input_math);
        //hideKeyboard(ed_input_math);

    }



    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        getLayoutInflater().inflate(R.layout.toobar_common, toolbar);

        TextView tv_common_back = (TextView) toolbar.findViewById(R.id.tv_common_back);
        TextView tv_common_center = (TextView) toolbar.findViewById(R.id.tv_common_center);
        tv_common_center.setText("计算器");
        tv_common_back.setText("");
        tv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 隐藏键盘
     *
     * @param v 点击的控件
     */
    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        v.setFocusable(false);


    }
}
