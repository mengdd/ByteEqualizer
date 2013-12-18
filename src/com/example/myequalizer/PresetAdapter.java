package com.example.myequalizer;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class PresetAdapter extends BaseAdapter {

    private Context mContext = null;
    private LayoutInflater mInflater; // ����ʵ���

    private List<PresetItem> mPresetsData;// ���Դ

    private EqualizerFragment mEqualizer = null;

    public void setmEqualizer(EqualizerFragment mEqualizer) {
        this.mEqualizer = mEqualizer;
    }

    public PresetAdapter(Context context, List<PresetItem> data) {

        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mPresetsData = data;

    }

    @Override
    public int getCount() {
        return mPresetsData.size();
    }

    @Override
    public PresetItem getItem(int position) {
        return mPresetsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PresetItem item = getItem(position);

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.preset_item, null);
        }

        final TextView nameView = (TextView) convertView
                .findViewById(R.id.presetName);

        nameView.setText(item.getPresetName());

        final RadioButton btnChosen = (RadioButton) convertView
                .findViewById(R.id.btnPresetChosen);

        btnChosen.setFocusable(false);
        btnChosen.setTag(position);

        btnChosen
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {

                        if (isChecked) {
                            int position = ((Integer) buttonView.getTag())
                                    .intValue();

                            selectOnePreset(mPresetsData, position);
                            PresetAdapter.this.notifyDataSetChanged();
                        }

                    }

                });

        btnChosen.setChecked(item.isChosen);

        return convertView;

    }

    public void selectOnePreset(List<PresetItem> mPresetsData, int presetID) {

        Log.d(AppConstants.LOG_TAG, "selectOnePreset method in presetAdapter: "
                + presetID);
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

        if (null != mEqualizer) {
            if (mPresetsData.get(presetID).getPresetName()
                    .equals(AppConstants.EQ_STYLE_CUSTOM)) {

            } else {
                mEqualizer.usePreset(presetID);

            }

        }
    }

}
