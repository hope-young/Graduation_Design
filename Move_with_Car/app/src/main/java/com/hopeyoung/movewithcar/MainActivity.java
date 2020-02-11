package com.hopeyoung.movewithcar;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btn_listen;
    private TextView tv_01, tv_02;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_listen = (Button) this.findViewById(R.id.button1);
        tv_01 = (TextView) findViewById(R.id.tv01);
        tv_02 = (TextView) findViewById(R.id.tv02);
        btn_listen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("正在获取位置信息...");
                Log.i("Move_with_Car", "getting location...");
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                tv_01.setText("经度" + location.getLongitude());
                tv_02.setText("纬度" + location.getLatitude());
            }
        });
    }

    }
