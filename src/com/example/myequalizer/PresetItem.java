package com.example.myequalizer;

public class PresetItem {
    String presetName;
    boolean isChosen = false;

    public PresetItem(String presetName) {
        super();
        this.presetName = presetName;
        this.isChosen = false;
    }

    public PresetItem(String presetName, boolean isChosen) {
        super();
        this.presetName = presetName;
        this.isChosen = isChosen;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public void setChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }

    public String getPresetName() {
        return presetName;
    }

    public boolean isChosen() {
        return isChosen;
    }

    @Override
    public String toString() {
        return "PresetName: " + presetName + ", isChosen: " + isChosen;

    }

}
