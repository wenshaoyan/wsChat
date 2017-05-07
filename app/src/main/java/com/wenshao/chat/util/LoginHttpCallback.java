package com.wenshao.chat.util;

import com.wenshao.chat.bean.LoginDate;

import org.xutils.common.Callback;

/**
 * Created by wenshao on 2017/5/7.
 * 登录成功返回数据处理
 */

public class LoginHttpCallback<T> implements Callback.CommonCallback<T> {

    @Override
    public void onSuccess(T t) {
        LoginDate loginDate = (LoginDate) t;
        if (loginDate!=null){

        }


    }

    @Override
    public void onError(Throwable throwable, boolean b) {

    }

    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }
}
