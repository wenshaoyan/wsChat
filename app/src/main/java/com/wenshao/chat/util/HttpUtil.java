package com.wenshao.chat.util;

import android.app.Application;
import android.util.Log;

import com.wenshao.chat.bean.FileUploadBean;

import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Created by wenshao on 2017/3/13.
 *
 */

public class HttpUtil{


    public static <T> Callback.Cancelable getAddress(Application app, String url, Map<String,String> map, Callback.CommonCallback<T> callback){
        x.Ext.init(app);
        RequestParams requestParams = new RequestParams(url);
        if (map!=null){
            for(Map.Entry<String, String> entry : map.entrySet()){
                requestParams.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        String token = getToken(url,app);
        if (token!=null){
            requestParams.addHeader("token",token);
        }
        HttpManager http = x.http();


        return http.post(requestParams, callback);
    }
    public static <T> Callback.Cancelable syncPost(Application app, String url, Map<String,String> map, Callback.CommonCallback<T> callback){

        x.Ext.init(app);
        RequestParams requestParams = new RequestParams(url);
        if (map!=null){
            for(Map.Entry<String, String> entry : map.entrySet()){
                requestParams.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        String token = getToken(url,app);
        Log.i("TokenId", "syncPost: "+token+",url:"+url);
        if (token!=null){
            requestParams.addHeader("token",token);
        }

        return x.http().post(requestParams, callback);
    }
    public static <T> Callback.Cancelable syncGet(Application app, String url, Map<String,String> map ,Callback.CommonCallback<T> callback){
        x.Ext.init(app);
        RequestParams requestParams = new RequestParams(url);
        if (map!=null){
            for(Map.Entry<String, String> entry : map.entrySet()){
                requestParams.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        String token = getToken(url,app);
        if (token!=null){
            requestParams.addHeader("token",token);
        }
        return x.http().get(requestParams, callback);
    }
    // 多个图片上传
    public static <T> Callback.Cancelable syncImageUpdate(Application app, String url, List<MediaBean> list, Callback.CommonCallback<T> callback){

        x.Ext.init(app);
        RequestParams requestParams = new RequestParams(url);
        if (list!=null){
            for (MediaBean mediaBean : list){
                requestParams.addBodyParameter("sendCode=", new File(mediaBean.getOriginalPath()));
            }
        }

        requestParams.setMultipart(true);
        return x.http().post(requestParams, callback);
   }

    // 多个图片上传
    public static <T> Callback.Cancelable syncMultiFileUpdate(Application app, String url, List<FileUploadBean> list, Callback.CommonCallback<T> callback){

        x.Ext.init(app);
        RequestParams requestParams = new RequestParams(url);

        if (list!=null){
            for (FileUploadBean fileUploadBean : list){
                requestParams.addBodyParameter("sendCode="+fileUploadBean.getSendCode(), new File(fileUploadBean.getOriginalPath()));
            }
        }
        requestParams.setMultipart(true);
        return x.http().post(requestParams, callback);
    }
    // 单个文件上传
    public static <T> Callback.Cancelable syncSingleFileUpdate(Application app, String url, FileUploadBean fileUploadBean, Callback.CommonCallback<T> callback){

        x.Ext.init(app);
        RequestParams requestParams = new RequestParams(url);
        requestParams.addBodyParameter("sendCode="+fileUploadBean.getSendCode(), new File(fileUploadBean.getOriginalPath()));
        return x.http().post(requestParams, callback);
    }

    private static String getToken(String url,Application app){
        try {
            URL httpUrl = new URL(url);
            String host = httpUrl.getHost();
            String domain="127.0.0.1";
            if (host!=null){ //location
                domain=host;
            }
            return SpUtil.getToken(app,domain);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
