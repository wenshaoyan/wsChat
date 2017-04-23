package com.wenshao.chat.bean;

import java.util.UUID;

/**
 * Created by wenshao on 2017/4/23.
 * 文件上传时的对象
 */

public class FileUploadBean {
    private String originalPath;  // 绝对路径
    private String sendCode;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileUploadBean(){
        UUID uuid = UUID.randomUUID();
        this.sendCode = uuid.toString();
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }
}
