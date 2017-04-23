package com.wenshao.chat.ws_http;

/**
 * Created by wenshao on 2017/3/14.
 * 回调
 */

public interface Callback {
    public void onSuccess(String result);
    public void onError(Throwable throwable);
    public void onFinished();


}
