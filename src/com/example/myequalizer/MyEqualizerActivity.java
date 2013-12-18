package com.example.myequalizer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyEqualizerActivity extends FragmentActivity {

    private List<PresetItem> mPresetsData;
    private final String[] presetNameStrings = { AppConstants.EQ_STYLE_NORMAL,
            AppConstants.EQ_STYLE_CLASSICAL, AppConstants.EQ_STYLE_DANCE,
            AppConstants.EQ_STYLE_FLAT, AppConstants.EQ_STYLE_FOLK,
            AppConstants.EQ_STYLE_HEAVY_METAL, AppConstants.EQ_STYLE_HIP_HOP,
            AppConstants.EQ_STYLE_JAZZ, AppConstants.EQ_STYLE_POP,
            AppConstants.EQ_STYLE_ROCK, AppConstants.EQ_STYLE_CUSTOM };

    private ListView mPresetListView = null;
    private PresetAdapter mAdapter = null;

    private Button backButton = null;

    private boolean isPlaying = false;

    private FragmentManager fragmentManager = null;
    private EqualizerFragment eqFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(AppConstants.LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_main);

        mPresetsData = getData();

        mAdapter = new PresetAdapter(this, mPresetsData);
        mPresetListView = (ListView) findViewById(R.id.presetListView);
        mPresetListView.setAdapter(mAdapter);
        mPresetListView.setOnItemClickListener(presetItemClickListener);

        selectOnePreset(mPresetsData, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        eqFragment = new EqualizerFragment();

        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.eqMainLinearLayout, eqFragment);
        fragmentTransaction.commit();

        mAdapter.setmEqualizer(eqFragment);

        backButton = (Button) findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPresetListView.setVisibility(View.VISIBLE);

            }
        });
        backButton.setVisibility(View.INVISIBLE);

    }

    private List<PresetItem> getData() {
        Log.d(AppConstants.LOG_TAG, "getData");
        List<PresetItem> list = new ArrayList<PresetItem>();

        for (int i = 0; i < presetNameStrings.length; ++i) {
            list.add(new PresetItem(presetNameStrings[i]));
        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_equalizer, menu);
        return true;
    }

    private OnItemClickListener presetItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            Log.d(AppConstants.LOG_TAG, "onItemClick: position: " + position
                    + ", " + id);
            selectOnePreset(mPresetsData, position);
            mAdapter.notifyDataSetChanged();

        }
    };

    public void selectOnePreset(List<PresetItem> mPresetsData, int presetID) {

        Log.d(AppConstants.LOG_TAG, "selectOnePreset: " + presetID);
        if (presetID < 0 || presetID > mPresetsData.size() - 1) {
            return;
        }

        for (int i = 0; i < mPresetsData.size(); ++i) {
            PresetItem presetItem = mPresetsData.get(i);

            if (i == presetID) {
                presetItem.setChosen(true);

            } else {
                presetItem.setChosen(false);
            }
        }

        if (null != eqFragment) {
            if (mPresetsData.get(presetID).getPresetName()
                    .equals(AppConstants.EQ_STYLE_CUSTOM)) {

                mPresetListView.setVisibility(View.GONE);
                backButton.setVisibility(View.VISIBLE);

            } else {
                eqFragment.usePreset(presetID);

            }

        }
    }

}
