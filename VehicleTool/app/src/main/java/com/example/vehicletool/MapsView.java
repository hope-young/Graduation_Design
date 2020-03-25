package com.example.vehicletool;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;

public class MapsView extends MainActivity {
    public MapView mMapView = null;
    //public LocationClient mLocationClient = new LocationClient(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDK初始化
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //全屏窗口处理
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maps_layout);
        mMapView = (MapView)findViewById(R.id.bmapView);

        //叠加实时路况图层
        //BaiduMap mBaiduMap = mMapView.getMap();
        //mBaiduMap.setTrafficEnabled(true);
/*
        //定位图层
        mBaiduMap.setMyLocationEnabled(true);


        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);

        mLocationClient.setLocOption(option);

        //设置监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        //开启地图定位图层
        mLocationClient.start();

 */
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        //mLocationClient.stop();
        //mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        mMapView.onDestroy();
        //mMapView = null;
    }

}

