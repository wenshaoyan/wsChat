package com.wenshao.chat.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by UPC on 2017/1/14.
 * 数据流处理类
 */
public class StreamUtil {

    /**
     * 将流转为字符串
     *
     * @param inputStream 流对象
     * @return 对应的字符串   null为转换异常
     */
    public static String streamToString(InputStream inputStream) {
        //在读取过程中将流放在缓存中 一次性转换为字符串
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len=-1;
        try {
            while ((len = inputStream.read(buf)) != -1){
                byteArrayOutputStream.write(buf,0,len);
            }
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                byteArrayOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }
}
