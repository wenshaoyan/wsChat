package com.wenshao.chat.audio;

import android.media.MediaRecorder;

/**
 * 声音来源   MIC 主麦克风  CAMCORDER 话筒
 */
import android.media.MediaRecorder;

public enum AudioSource {
    MIC,
    CAMCORDER;

    public int getSource(){
        switch (this){
            case CAMCORDER:
                return MediaRecorder.AudioSource.CAMCORDER;
            default:
                return MediaRecorder.AudioSource.MIC;
        }
    }
}