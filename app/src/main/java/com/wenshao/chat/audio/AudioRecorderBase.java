package com.wenshao.chat.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;
import omrecorder.AudioSource;


/**
 * Created by wenshao on 2017/4/19.
 * 录音执行类
 */

public class AudioRecorderBase {
    private static final String TAG = "AudioRecorderBase";
    private Timer timer;  // 计时器
    private boolean isRecording; // 是否正在录音
    private boolean isPlaying;
    private Recorder mRecorder;
    private String filePath;
    private MediaPlayer mediaPlayer;
    private int recorderSecondsElapsed=0;
    private int playerSecondsElapsed=0;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public AudioRecorderBase() {
        init();
    }

    public AudioRecorderBase(String filePath) {
        this.filePath = filePath;
        init();
    }


    private AudioSource mic() {

        return new AudioSource.Smart(
                MediaRecorder.AudioSource.MIC,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.CHANNEL_IN_MONO, 44100);
    }

    /**
     * 初始化
     */
    private void init() {


    }


    /**
     * 开始录音
     */
    public void startRecording(){
        isRecording=true;
        try {
            if (mRecorder == null) {
                mRecorder = OmRecorder.wav(new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
                        //Log.i(TAG, "onAudioChunkPulled: ");
                        //animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                    }
                }), new File(filePath));
                mRecorder.startRecording();
            }

            mRecorder.startRecording();
        }catch (Exception e){
            e.printStackTrace();
        }

        startTimer();
    }



    /**
     * 重置
     */
    public void restartRecording() {
        Log.i(TAG, "restartRecording: ");
        if (isRecording) {
            stopRecording();
        }
        if (isPlaying) {
            stopPlaying();
        }
        recorderSecondsElapsed = 0;
        playerSecondsElapsed = 0;

    }

    /**
     * 继续录音
     */
    public void resumeRecording() {
        if (mRecorder != null) {
            mRecorder.resumeRecording();
        }

        startTimer();

    }

    /**
     * 暂停录音
     */
    public void pauseRecording() {
        isRecording = false;
        if (mRecorder != null) {
            mRecorder.pauseRecording();
        }
        stopTimer();

    }

    /**
     * 停止录音
     */
    public void stopRecording() {
        recorderSecondsElapsed = 0;
        isRecording = false;
        if (mRecorder != null) {
            mRecorder.stopRecording();
            mRecorder = null;
        }
        stopTimer();

    }

    /**
     * 开始播放
     */
    public void startPlaying() {

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 停止播放
     */
    public void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        isPlaying = false;
        stopTimer();

    }

    /**
     * 开始计时
     */
    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    /**
     * 停止计算
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * 更新计时器的显示
     */
    private void updateTimer() {
        Log.i(TAG, "updateTimer: ");
        if (isPlaying) {
            playerSecondsElapsed++;
        } else if (isRecording) {
            recorderSecondsElapsed++;
        }
    }


}
