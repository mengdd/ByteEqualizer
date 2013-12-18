package com.example.myequalizer;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class MyAudioTrack {

    int mFrequency;

    int mChannel;

    int mSampBit;

    AudioTrack mAudioTrack;

    public MyAudioTrack(int frequency, int channel, int sampbit) {
        mFrequency = frequency;
        mChannel = channel;
        mSampBit = sampbit;
    }

    public void init() {
        if (mAudioTrack != null) {
            release();
        }

        int minBufSize = AudioTrack.getMinBufferSize(mFrequency, mChannel,
                mSampBit);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mFrequency,
                mChannel, mSampBit, minBufSize, AudioTrack.MODE_STREAM);

        mAudioTrack.play();
    }

    public void release() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
        }
    }

    public void playAudioTrack(byte[] data, int offset, int length) {
        if (data == null || data.length == 0) {
            return;
        }

        try {
            // mAudioTrack.play();
            mAudioTrack.write(data, offset, length);
        }
        catch (Exception e) {
            Log.i("MyAudioTrack", "catch exception...");
        }
    }

    public int getPrimePlaySize() {
        int minBufSize = AudioTrack.getMinBufferSize(mFrequency, mChannel,
                mSampBit);

        return minBufSize * 2;
    }

    public int getAudioSessionId() {
        return mAudioTrack.getAudioSessionId();
    }

}
