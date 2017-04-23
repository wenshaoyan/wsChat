package com.wenshao.chat.ws_http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.wenshao.chat.exception.ParamsException;
import com.wenshao.chat.exception.ResolveException;
import com.wenshao.chat.util.SpUtil;
import com.wenshao.chat.util.StreamUtil;
import com.wenshao.chat.ws_http.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshao on 2017/3/14.
 * http请求封装
 */

public class HttpConnection {

    public static void post(final Context context, String url, Map<String, String> params, final Callback callback) {
        URL httpUrl = null;
        String domain="127.0.0.1";
        try {
            httpUrl = new URL(url);
            String[] split = httpUrl.getHost().split(".");
            int length = split.length;
            if (length==1){ //location
                domain=split[0];
            }else if (length==2){
                domain=split[0]+split[1];
            }else if (length==3) {
                domain=split[1]+split[2];
            }else {
                domain=httpUrl.getHost();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        final String postParams = postParams(params);
        final String token_local = SpUtil.getToken(context,domain);


        final URL finalHttpUrl = httpUrl;
        final String finalDomain = domain;
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try {
                    connection = setHead(finalHttpUrl, "POST",token_local);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Length", postParams.length()+"");
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(postParams.getBytes());
                    outputStream.flush();

                    //String data = setPostParams(connection, params);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) { //请求成功
                        InputStream inputStream = connection.getInputStream();
                        String result = StreamUtil.streamToString(inputStream);
                        inputStream.close();
                        Log.i("HttpConnection",result);
                        String token = connection.getHeaderField("Token");
                        SpUtil.setToken(context,token, finalDomain);  //写入token
                        callback.onSuccess(result);
                    } else if (responseCode == 404) {

                    } else if (responseCode == 500) {

                    } else {

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    private static HttpURLConnection setHead(URL httpUrl, String method,String token_local) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (token_local!=null){
            connection.setRequestProperty("Token", ""+token_local);
        }
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(5000);

        return connection;
    }

    private static String postParams(final Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    private static String getParams(final Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
