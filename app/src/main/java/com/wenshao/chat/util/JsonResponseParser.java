package com.wenshao.chat.util;

import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.wenshao.chat.exception.LoginInfoException;
import com.wenshao.chat.exception.LoginStatusException;
import com.wenshao.chat.exception.ParamsException;
import com.wenshao.chat.exception.ResolveException;
import com.wenshao.chat.exception.ServerException;
import com.wenshao.chat.exception.UndefinedException;

import org.json.JSONObject;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

/**
 * Created by wenshao on 2017/3/13.
 * 格式化数据
 */

public class JsonResponseParser implements ResponseParser {
    private String TAG = "JsonResponseParser";

    private static final SparseArray<Exception> mException = new SparseArray<Exception>();

    static {
        mException.put(1,new ParamsException());
        mException.put(2,new LoginStatusException());
        mException.put(4,new LoginInfoException());



        mException.put(201,new ServerException());      // 后端错误
        mException.put(202,new ResolveException());     // 解析异常
        mException.put(203,new UndefinedException());     // 未定义的异常

    }







    @Override
    public void checkResponse(UriRequest uriRequest) throws Throwable {
        if (uriRequest.getResponseCode() == 200) {
            try {
                String token = uriRequest.getResponseHeader("token");
                Log.i(TAG, "checkResponse: "+token);
                String requestUri = uriRequest.getRequestUri();
                String domain="127.0.0.1";

                URL httpUrl = new URL(requestUri);
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
                SpUtil.setToken(x.app(),token,domain);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param type   返回值类型(可能带有泛型信息)
     * @param aClass 返回值类型
     * @param result 字符串数据
     * @return Object 返回对象
     * @throws Throwable
     */
    @Override
    public Object parse(Type type, Class<?> aClass, String result) throws Throwable {
        Log.i("parse", "parse: "+result);
        JSONObject jsonObject = new JSONObject(result);
        int code = jsonObject.getInt("code");
        if (code==0){  // 请求成功
            try {
                return resolve(result,type,aClass);
            }catch (Exception e){
                throw mException.get(202);
            }

        } else if (code>=1 && code<=200){  //前端导致的错误
            Exception e;
            if ((e =mException.get(code))==null ){
                e=mException.get(203);
            }
            throw e;

        } else {  //后端或数据库等错误
            throw mException.get(201);
        }


    }
    private Object  resolve(String data,Type type, Class<?> aClass){
        Object msg;
        msg = new Gson().fromJson(data, type);
        if (aClass == List.class) {
            return msg;
        } else {
            return msg;
        }
    }

}
