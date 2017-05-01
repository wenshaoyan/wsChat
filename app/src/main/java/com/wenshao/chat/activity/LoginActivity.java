package com.wenshao.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wenshao.chat.R;
import com.wenshao.chat.bean.LoginDate;
import com.wenshao.chat.bean.UserBean;
import com.wenshao.chat.constant.SelfConstant;
import com.wenshao.chat.constant.SpConstant;
import com.wenshao.chat.constant.UrlConstant;
import com.wenshao.chat.exception.LoginInfoException;
import com.wenshao.chat.service.WebSocketService;
import com.wenshao.chat.util.HttpCallback;
import com.wenshao.chat.util.HttpUtil;
import com.wenshao.chat.util.PhoneInfo;
import com.wenshao.chat.util.SpUtil;
import com.wenshao.chat.util.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 2017/3/16.
 * 登录页面
 */

public class LoginActivity extends AppCompatActivity {

    private Context mContext;
    private EditText et_name;
    private EditText et_password;
    private String TAG = "LoginActivity";

    private UserBean mUserBean;

    private final int LOGIN_SUC = 2;           //登录成功
    private final int LOGIN_FAIL = 3;  //登录失败

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==LOGIN_SUC){
                Intent intent1 = new Intent(mContext, WebSocketService.class);
                //intent1.putExtra("")
                startService(intent1);
                Intent intent = new Intent(mContext, IndexActivity.class);
                //用Bundle携带数据
                Bundle bundle=new Bundle();
                bundle.putSerializable("userBean",mUserBean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }else if (msg.what==LOGIN_FAIL){
                ToastUtil.show(mContext,(String) msg.obj);
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);

        initUi();
        initData();

    }

    private void initUi() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);

        Button bt_login = (Button) findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    private void initData() {

    }

    private void login() {
        final String name = et_name.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(mContext, "用户名不能为空");
            return;
        } else if (TextUtils.isEmpty(password)) {
            ToastUtil.show(mContext, "密码不能为空");
            return;
        }
        final Map<String, String> map = new HashMap<>();
        map.put("name",name);
        map.put("password",password);
        Map<String, String> phoneInfoData = PhoneInfo.getAll(mContext);
        map.putAll(phoneInfoData);
        startActivity(new Intent(mContext,LoginSimpleLoadActivity.class));


        UrlConstant urlConstant = UrlConstant.getInstance();
        HttpUtil.syncPost(getApplication(), urlConstant.login(), map, new HttpCallback<LoginDate>() {
            @Override
            public void onSuccess(LoginDate resultType) {
                super.onSuccess(resultType);
                SpUtil.putString(mContext, SpConstant.LOCAL_USER_NAME,name);
                SpUtil.putString(mContext, SpConstant.LOCAL_USER_PASSWORD,password);

                mUserBean=resultType.getUser();
                SelfConstant.setUserBean(mUserBean);
                mHandler.sendEmptyMessage(LOGIN_SUC);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.i(TAG, "onError: ");
                if (throwable instanceof LoginInfoException){
                    Message message = new Message();
                    message.obj=throwable.getMessage();
                    message.what=LOGIN_FAIL;
                    mHandler.sendMessage(message);
                }else{
                    super.onError(throwable, b);
                }


            }

            @Override
            public void onCancelled(CancelledException e) {
                super.onCancelled(e);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
