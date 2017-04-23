package com.wenshao.chat.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wenshao.chat.R;
import com.wenshao.chat.bean.LoginDate;
import com.wenshao.chat.bean.ServiceAddressData;
import com.wenshao.chat.bean.UserBean;
import com.wenshao.chat.constant.EnvConstant;
import com.wenshao.chat.constant.SelfConstant;
import com.wenshao.chat.constant.SpConstant;
import com.wenshao.chat.constant.UrlConstant;
import com.wenshao.chat.helper.BaseWebSocketHelper;
import com.wenshao.chat.service.WebSocketService;
import com.wenshao.chat.util.HttpCallback;
import com.wenshao.chat.util.HttpUtil;
import com.wenshao.chat.util.PhoneInfo;
import com.wenshao.chat.util.SpUtil;

import org.java_websocket.client.WebSocketClient;
import org.xutils.common.Callback;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 2017/3/20.
 * 欢迎页面
 */

public class SplashActivity extends Activity {

    private Context mContext;
    private TextView tv_version;
    private TextView text_load_des;
    private final String TAG = "SplashActivity";

    private UserBean mUserBean;


    private final int INIT_SERVICE_FINISHED = 0; //初始化完成
    private final int JUMP_LOGIN = 1;           //跳转至登录页面
    private final int JUMP_INDEX = 2;           //跳转至首页





    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INIT_SERVICE_FINISHED:
                    autoLogin();
                    break;
                case JUMP_LOGIN:
                    startActivity(new Intent(mContext,LoginActivity.class));
                    break;
                case JUMP_INDEX:
                    Intent intent1 = new Intent(mContext, WebSocketService.class);
                    //intent1.putExtra("")
                    startService(intent1);
                    Intent intent = new Intent(mContext, IndexActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("userBean",mUserBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                default:
                    break;

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;



        initUi();
        initData();
        intiUniversal();
        //initImageLoader();
    }



    private void initUi() {
        tv_version = (TextView) findViewById(R.id.tv_version);
        text_load_des = (TextView) findViewById(R.id.text_load_des);
    }
    private void initData() {
        // 根据包的版本号获取服务器地址
        initService();
    }

    private void initService() {
        Map<String, String> params = PhoneInfo.getAll(mContext);
        params.put("environment", EnvConstant.ENVIRONMENT);
        HttpUtil.getAddress(getApplication(), UrlConstant.GET_ADDRESS,params,new HttpCallback<ServiceAddressData>(){
            @Override
            public void onSuccess(ServiceAddressData resultType) {
                super.onSuccess(resultType);
                UrlConstant urlConstant = UrlConstant.getInstance();
                urlConstant.setServiceUrl(resultType.getService());
                UrlConstant.setWebSocketUrl(resultType.getWebSocket());
                Log.i(TAG, "onSuccess: suc"+resultType.getService()+urlConstant.getServiceUrl());
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i(TAG, throwable.getMessage());

            }

            @Override
            public void onCancelled(CancelledException e) {
                super.onCancelled(e);

            }

            @Override
            public void onFinished() {
                super.onFinished();
                mHandler.sendEmptyMessage(INIT_SERVICE_FINISHED);

            }
        });
    }


    private void intiUniversal() {
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs() //打印log信息
                .build();

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }
    //ImageLoaderConfiguration
    private void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);


        ImageLoader.getInstance().init(config.build());
    }

    // 检查是否需要自动登录
    private void autoLogin(){
        String name = SpUtil.getString(mContext, SpConstant.LOCAL_USER_NAME,"");
        String password = SpUtil.getString(mContext, SpConstant.LOCAL_USER_PASSWORD,"");

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
            Map<String, String> map = new HashMap<>();
            map.put("name",name);
            map.put("password",password);
            UrlConstant urlConstant = UrlConstant.getInstance();
            HttpUtil.syncPost(getApplication(),urlConstant.login(),map,new HttpCallback<LoginDate>(){
                @Override
                public void onSuccess(LoginDate resultType) {
                    super.onSuccess(resultType);
                    mUserBean =resultType.getUser();
                    SelfConstant.setUserBean(resultType.getUser());

                    mHandler.sendEmptyMessage(JUMP_INDEX);
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    super.onError(throwable, b);
                    mHandler.sendEmptyMessage(JUMP_LOGIN);
                }

                @Override
                public void onCancelled(CancelledException e) {
                    super.onCancelled(e);
                    mHandler.sendEmptyMessage(JUMP_LOGIN);
                }

                @Override
                public void onFinished() {
                    super.onFinished();
                }
            });
        }else{
            mHandler.sendEmptyMessage(JUMP_LOGIN);
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
