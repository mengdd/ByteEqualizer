package com.example.myequalizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {

    /** Called when the activity is first created. */

    final int EVENT_PLAY_OVER = 0x100;

    Thread mThread = null;
    byte[] data = null;
    Handler mHandler;

    Button btnPlay, btnStopButton, btnEQButton;
    TextView textView;

    Equalizer mEqualizer;
    boolean isPlaying = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_main);

        textView = (TextView) findViewById(R.id.textview01);

        // 注意：此处的音乐文件需要是wav格式的，因为本程序没有对音频进行解码
        // data = getPCMDataFromFile(AppConstants.filePath);
        data = getPCMDataFromAssets("song.wav");

        if (data == null) {
            Toast.makeText(this, "can't find the file ! ", Toast.LENGTH_LONG)
                    .show();
        }

        mHandler = new Handler() {
            public void handleMessage(Message message) {
                if (message.what == EVENT_PLAY_OVER) {
                    mThread = null;
                }
            }
        };

        init();

    }

    public byte[] getPCMDataFromFile(String filePath) {
        File file = new File(filePath);
        FileInputStream inStream;
        byte[] dataPack = null;
        try {
            inStream = new FileInputStream(file);

            if (inStream != null) {
                long size = file.length();
                dataPack = new byte[(int) size];
                inStream.read(dataPack);
            }
            inStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dataPack;
    }

    public byte[] getPCMDataFromAssets(String fileName) {
        byte[] dataPack = null;

        AssetManager assetManager = getAssets();
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager
                    .openFd(fileName);
            long size = assetFileDescriptor.getLength();
            dataPack = new byte[(int) size];
            assetFileDescriptor.createInputStream().read(dataPack);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return dataPack;
    }

    public void play() {
        if (data == null) {
            Toast.makeText(this, "No File...", Toast.LENGTH_LONG).show();
            return;
        }

        if (mThread == null) {
            isPlaying = true;
            MyThread myThread = new MyThread(data, mHandler);
            myThread.setUp();
            mThread = new Thread(myThread);

            mThread.start();
            textView.setText("Playing...");
        }

    }

    public void stop() {
        if (data == null) {
            return;
        }

        if (mThread != null) {
            isPlaying = false;
            mThread.interrupt();
            mThread = null;
            textView.setText("Stop...");
        }
    }

    private void enterEQ() {
        if (!isPlaying) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, MyEqualizerActivity.class);

        intent.putExtra(AppConstants.KEY_AUDIO_ID, AppConstants.audioSessionId);
        intent.putExtra(AppConstants.KEY_IS_PLAYING, isPlaying);

        startActivity(intent);
    }

    public void init() {
        btnPlay = (Button) findViewById(R.id.buttonPlay);
        btnStopButton = (Button) findViewById(R.id.buttonStop);

        btnEQButton = (Button) findViewById(R.id.buttonEq);

        btnPlay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                play();
            }
        });

        btnStopButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                stop();
            }
        });

        btnEQButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                enterEQ();

            }
        });

    }

    public void onDestroy() {
        stop();

        super.onDestroy();
    }

    public void setUpEqualizerFxUI() {
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.myLayout);

        Log.v(AppConstants.LOG_TAG, "Enter --> setupEqualizerFxAndUI");

        // int audioSessionId = AppConstants.audioSessionId;
        int audioSessionId = 10;
        Log.d(AppConstants.LOG_TAG, "The AudioSessionId: " + audioSessionId);

        // Create the Equalizer object (an AudioEffect subclass) and attach it
        // to our media player,
        // with a default priority (0).

    }
}
