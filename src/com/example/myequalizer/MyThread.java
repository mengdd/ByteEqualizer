package com.example.myequalizer;

import android.media.AudioFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyThread implements Runnable {

    final int EVENT_PLAY_OVER = 0x100;

    byte[] data;
    Handler mHandler;
    MyAudioTrack myAudioTrack;

    public MyThread(byte[] data, Handler handler) {
        this.data = data;
        mHandler = handler;
    }

    public void setUp() {
        Log.d(AppConstants.LOG_TAG, "setUp()");

        if (data == null || data.length == 0) {
            return;
        }

        myAudioTrack = new MyAudioTrack(44100,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        myAudioTrack.init();

        AppConstants.audioSessionId = myAudioTrack.getAudioSessionId();

        Log.d(AppConstants.LOG_TAG, "AudioTrack get AudioSessionId: "
                + myAudioTrack.getAudioSessionId());
        Log.d(AppConstants.LOG_TAG, "AppConstants: "
                + AppConstants.audioSessionId);

    }

    public void run() {
        Log.d(AppConstants.LOG_TAG, "run()");

        int playSize = myAudioTrack.getPrimePlaySize();

        Log.i("MyThread", "total data size = " + data.length + ", playSize = "
                + playSize);

        int index = 0;
        int offset = 0;
        while (true) {
            try {
                Thread.sleep(0);

                offset = index * playSize;

                if (offset >= data.length) {
                    break;
                }

                myAudioTrack.playAudioTrack(data, offset, playSize);

            }
            catch (Exception e) {
                break;
            }

            index++;
        }

        myAudioTrack.release();

        Message msg = Message.obtain(mHandler, EVENT_PLAY_OVER);
    }

}
