package com.vardroid.duallistselector;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class SampleTabActivity extends AppCompatActivity {

    private final SavedState savedState = new SavedState();

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_tab);
        savedState.update(savedInstanceState);

        initViews();
    }

    @Override
    protected void onSaveInstanceState(
            @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savedState.save(outState);
    }

    private void initViews() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        for (SampleType sampleType : SampleType.values()) {
            tabLayout.addTab(tabLayout.newTab()
                    .setTag(sampleType)
                    .setText(sampleType.getTitleResId()));

        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Object tag = tab.getTag();
                if (tag instanceof SampleType) {
                    SampleType sampleType = (SampleType) tag;
                    showContent(sampleType);
                    savedState.sampleType = sampleType;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        TabLayout.Tab tab = tabLayout.getTabAt(savedState.sampleType.ordinal());
        if (tab != null) {
            tab.select();
        }
    }

    private void showContent(
            SampleType sampleType) {
        String tag = sampleType.name();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = sampleType.createFragment();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private static class SavedState implements Parcelable {

        private static final String STATE_KEY = "STATE";

        private SampleType sampleType;

        public SavedState() {
            this.sampleType = SampleType.COOL;
        }

        public SavedState(
                Parcel parcel) {
            this.sampleType = SampleType.valueOf(parcel.readString());
        }

        public void update(
                Bundle bundle) {
            SavedState savedState = bundle == null
                    ? null
                    : bundle.getParcelable(STATE_KEY);
            if (savedState != null) {
                this.sampleType = savedState.sampleType;
            }
        }

        public void save(
                Bundle bundle) {
            if (bundle != null) {
                bundle.putParcelable(STATE_KEY, this);
            }
        }

        @Override
        public void writeToParcel(
                Parcel out,
                int flags) {
            out.writeString(this.sampleType.name());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}