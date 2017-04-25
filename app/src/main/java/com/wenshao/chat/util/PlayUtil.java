package com.wenshao.chat.util;

import android.media.MediaPlayer;

import java.io.IOException;

import static com.wenshao.chat.util.AudioRecordUtil.STATUS_PLAYING;

/**
 * Created by wenshao on 2017/4/25.
 * 全局视音频播放类
 */

public class PlayUtil {
    private static PlayUtil mPlay = new PlayUtil();


    public final static int STATUS_PLAY_FREE = 0;  // 播放空闲
    public final static int STATUS_AUDIO_PLAYING = 1;  // 音频播放中
    public final static int STATUS_VIDEO_PLAYING = 2;  // 视频播放中

    private int status = 0;

    private MediaPlayer mediaPlayer;
    private int mDuration;
    private String mUrl;

    private PlayUtil() {

    }


    public static PlayUtil getInstance() {
        return mPlay;
    }


    public boolean setUrl(String url, int duration) {
        this.mDuration = duration;
        this.mUrl = url;
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            status=0;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    endAudio();

                }
            });
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startAudio() {
        if (status!=STATUS_PLAY_FREE){
            endAudio();
            setUrl(mUrl,mDuration);
        }
        if (mediaPlayer != null) {
            status = STATUS_AUDIO_PLAYING;
            mediaPlayer.start();
        }
    }

    public void endAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        status = STATUS_PLAY_FREE;

    }


}
