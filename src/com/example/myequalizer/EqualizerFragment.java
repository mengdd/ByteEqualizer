package com.example.myequalizer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class EqualizerFragment extends Fragment {

    private Equalizer mEqualizer = null;

    private List<SeekBar> mSeekBars = null;

    private boolean isPlaying = false;

    private Activity mActivity = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(AppConstants.LOG_TAG, "EqualizerFragment --> onCreate");
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        Intent intent = mActivity.getIntent();
        int audioSessionId = intent.getIntExtra(AppConstants.KEY_AUDIO_ID, 0);
        isPlaying = intent.getBooleanExtra(AppConstants.KEY_IS_PLAYING, false);

        if (isPlaying) {

            Log.d(AppConstants.LOG_TAG,
                    "EqualizerFragment --> isPlaying, init equalizer object");

            try {

                mEqualizer = new Equalizer(0, audioSessionId);
                Log.d(AppConstants.LOG_TAG, "The AudioSessionId: "
                        + audioSessionId);
                mEqualizer.setEnabled(true);

            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e(AppConstants.LOG_TAG, "Error in MyEqualizer constructor "
                        + mEqualizer);

                Toast.makeText(mActivity, "Equalizer initialize fail",
                        Toast.LENGTH_LONG);

                if (0 == audioSessionId) {
                    Log.e(AppConstants.LOG_TAG,
                            "audioSessionId is 0, if you want to have a global control, ensure you have pemissions");

                } else {
                    Log.e(AppConstants.LOG_TAG,
                            "audioSessionId is NOT 0, but you have proble init EQ");
                }
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(AppConstants.LOG_TAG, "EqualizerFragment --> onCreateView");
        LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(
                R.layout.custom_eq_fragment, container, false);

        if (null != mEqualizer) {
            initUIWithEQ(mLinearLayout);

        }// end if
        return mLinearLayout;
    }

    private void initUIWithEQ(LinearLayout mLinearLayout) {
        // Gets the number of frequency bands supported by the Equalizer
        // engine.
        short bands = mEqualizer.getNumberOfBands();
        Log.d(AppConstants.LOG_TAG, "getNumberOfBands: " + bands);

        mSeekBars = new ArrayList<SeekBar>();

        // the band level range in an array of short integers.
        // The first element is the lower limit of the range, the second
        // element
        // the upper limit.
        final short minEQLevel = mEqualizer.getBandLevelRange()[0];
        final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

        Log.i(AppConstants.NUM_LOG, "minEQLevel: " + minEQLevel);
        Log.i(AppConstants.NUM_LOG, "maxEQLevel: " + maxEQLevel);

        short[] eqLevels = mEqualizer.getBandLevelRange();
        Log.i(AppConstants.NUM_LOG, "eqLevels: count: " + eqLevels.length);

        for (short i = 0; i < bands; i++) {
            Log.v(AppConstants.LOG_TAG, "The " + i + "th band");

            final short band = i;

            TextView freqTextView = new TextView(mActivity);
            freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);

            freqTextView
                    .setText((mEqualizer.getCenterFreq(band) / 1000) + "HZ");
            mLinearLayout.addView(freqTextView);

            LinearLayout row = new LinearLayout(mActivity);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView minDbTextView = new TextView(mActivity);
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            minDbTextView.setText((minEQLevel / 100) + " dB");

            TextView maxDbTextView = new TextView(mActivity);
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxEQLevel / 100) + " dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.weight = 1;

            SeekBar seekbar = new SeekBar(mActivity);
            seekbar.setLayoutParams(layoutParams);
            seekbar.setMax(maxEQLevel - minEQLevel);
            seekbar.setProgress(mEqualizer.getBandLevel(band));

            Log.i(AppConstants.NUM_LOG, "The " + i + "th band Level: "
                    + mEqualizer.getBandLevel(band));

            seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                        boolean fromUser) {
                    // Sets the given equalizer band to the given gain
                    // value.
                    mEqualizer.setBandLevel(band,
                            (short) (progress + minEQLevel));

                    Log.i(AppConstants.NUM_LOG, "progress: " + progress
                            + ", minEQLevel:" + minEQLevel);
                }
            });
            mSeekBars.add(seekbar);
            row.addView(minDbTextView);
            row.addView(seekbar);
            row.addView(maxDbTextView);
            mLinearLayout.addView(row);
        }// end for
    }

    @Override
    public void onPause() {
        Log.d(AppConstants.LOG_TAG, "EqualizerFragment --> Pause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(AppConstants.LOG_TAG, "EqualizerFragment --> onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(AppConstants.LOG_TAG, "EqualizerFragment --> onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(AppConstants.LOG_TAG, "EqualizerFragment --> onDestroy");
        super.onDestroy();
    }

    public void usePreset(int presetID) {
        mEqualizer.usePreset((short) presetID);
        Log.i(AppConstants.NUM_LOG, "usePreset, id: " + presetID);
        Log.i(AppConstants.NUM_LOG,
                "usePreset, name: "
                        + mEqualizer.getPresetName((short) presetID));

        Log.i(AppConstants.NUM_LOG, "preset levels ----");
        for (short i = 0; i < 5; ++i) {
            short level = mEqualizer.getBandLevel(i);
            Log.i(AppConstants.NUM_LOG, "the " + i + "th band: " + level);
        }
        Log.i(AppConstants.NUM_LOG, "end preset levels -----");

        setSeekBarLevels();

    }

    private void setSeekBarLevels() {
        short bands = mEqualizer.getNumberOfBands();

        if (mSeekBars == null || mSeekBars.size() != bands) {
            Log.d(AppConstants.LOG_TAG, "seekBar setting error");
            return;

        }
        for (short i = 0; i < bands; i++) {
            short band = (short) i;
            mSeekBars.get(i).setProgress(mEqualizer.getBandLevel(band));

        }
    }

}
