package com.vardroid.duallistselector;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vardroid.duallistselector.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding.inflate(getLayoutInflater());
        navigate();
    }

    private void navigate() {
        boolean showSampleTab = getResources().getBoolean(R.bool.show_sample_tab);
        Intent intent = new Intent(
                this,
                showSampleTab
                        ? SampleTabActivity.class
                        : SampleSingleActivity.class);
        startActivity(intent);
        finish();
    }
}