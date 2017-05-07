package com.wenshao.chat.util;

import android.util.Log;

import com.wenshao.chat.exception.LoginStatusException;
import com.wenshao.chat.exception.ParamsException;
import com.wenshao.chat.exception.ResolveException;
import com.wenshao.chat.exception.ServerException;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;

/**
 * Created by wenshao on 2017/3/13.
 * 处理请求回调
 */

public class HttpCallback<T> implements Callback.CommonCallback<T> {
    private String TAG ="HttpCallback";

    @Override
    public void onSuccess(T resultType) {


    }

    @Override
    public void onError(Throwable throwable, boolean b) {
        if (throwable instanceof RuntimeException) {  // 未知异常
            Log.i(TAG, throwable.getMessage());

        } else if (throwable instanceof HttpException) { // 请求异常
            Log.i(TAG, throwable.getMessage());

        } else if (throwable instanceof LoginStatusException) { // 登陆异常
            Log.i(TAG, throwable.getMessage());

        } else if (throwable instanceof ParamsException) { // 参数异常
            Log.i(TAG, throwable.getMessage());

        } else if (throwable instanceof ResolveException) { // 解析异常
            Log.i(TAG, throwable.getMessage());

        } else if (throwable instanceof ServerException) { // 服务器异常
            Log.i(TAG, throwable.getMessage());

        } else {
            Log.i(TAG, throwable.getMessage());
        }
    }

    @Override
    public void onCancelled(CancelledException e) {
        Log.i(TAG, "onCancelled");

    }

    @Override
    public void onFinished() {
        Log.i(TAG, "onFinished");
    }



}
