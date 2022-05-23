package com.example.calleridapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.CallerIdApplication.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /// setContentView(com.example.CallerIdApplication.R.layout.activity_setting);
   //     getSupportFragmentManager().beginTransaction().replace(androidx.appcompat.R.id.content,new SettingsFragment()).commit();


    }
    @Override
    protected void onResume(){
        super.onResume();
    //    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
   //     PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(SettingsHelper.getHelper());
    }




}