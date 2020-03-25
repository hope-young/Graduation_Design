package com.example.vehicletool;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;

public class MyLocationListener extends BDAbstractLocationListener {
    public MapView mMapView = null;
    public BaiduMap mBaiduMap = mMapView.getMap();
    @Override
    public  void onReceiveLocation(BDLocation location) {
        if (location == null || mMapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
    }
}
