package com.wenshao.chat.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wenshao on 2017/3/16.
 * 吐司
 */

public class ToastUtil {

    public static void show(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }



}
