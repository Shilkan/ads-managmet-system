package com.example.adsmanagmetsystem;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.BaseClass;

public class ActivityNativeBanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_banner);

        BaseClass.getInstance(this).showNativeAd(findViewById(com.example.mylibrary.R.id.native_layout), null);

        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_layout));


    }
}