package com.hopeyoung.bdlocating;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDLocation;


import android.os.Bundle;

public LocationClient mLocationClient = null;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
