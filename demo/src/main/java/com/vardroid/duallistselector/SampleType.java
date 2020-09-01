package com.vardroid.duallistselector;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public enum SampleType {

    DEFAULT(
            R.string.sample_type_default_title) {
        @Override
        public Fragment createFragment() {
            return new SampleDefaultFragment();
        }
    },

    COOL(
            R.string.sample_type_cool_title) {
        @Override
        public Fragment createFragment() {
            return new SampleCoolFragment();
        }
    },

    DARK(
            R.string.sample_type_dark_title) {
        @Override
        public Fragment createFragment() {
            return new SampleDarkFragment();
        }
    },

    ;

    @StringRes
    private final int titleResId;

    SampleType(
            @StringRes int titleResId) {
        this.titleResId = titleResId;
    }

    @StringRes
    public int getTitleResId() {
        return titleResId;
    }

    public abstract Fragment createFragment();
}
