package com.wenshao.chat.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wenshao on 2017/3/16.
 * 获取手机信息
 */

public class PhoneInfo {
    public static Map<String,String> getAll(Context context){
        Map<String, String> map = new HashMap<>();
        try {
            String deviceId = getDeviceId(context);
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            map.put("uuid",deviceId);
            map.put("package",packageName);
            map.put("versionCode",String.valueOf(packageInfo.versionCode));
            map.put("version",String.valueOf(packageInfo.versionName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            deviceId = getAndroidId(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deviceId != null && !"".equals(deviceId)) {
            return deviceId;
        }
        if (deviceId == null || "".equals(deviceId)) {

            if (deviceId == null || "".equals(deviceId)) {
                UUID uuid = UUID.randomUUID();
                deviceId = uuid.toString().replace("-", "");
            }
        }
        return deviceId;
    }

    // IMEI码
    private static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds") String deviceId = tm.getDeviceId();
        return deviceId;
    }



    // Android Id
    private static String getAndroidId(Context context) {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }





}
