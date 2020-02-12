package com.hopeyoung.bddemo;

import androidx.appcompat.app.AppCompatActivity;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

public class MainActivity extends AppCompatActivity {

    private MapView baiduMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化context信息
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_main);

        //MapView.removeViewAt(1);

        baiduMapView = (MapView)findViewById(R.id.bmapView);

        View Child = baiduMapView.getChildAt(1);
        if(Child != null && (Child instanceof ImageView || Child instanceof ZoomControls)){
            Child.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        baiduMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baiduMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMapView.onDestroy();
    }

}
