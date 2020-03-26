package com.example.vehicletool;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


public class MapsView extends MainActivity {
    MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirstLocation = true;
    private MapStatusUpdate update = null;
    private Context mContext;
    private float mRotateDegree = 0;


    public double latitude = 0;
    public double longitude = 0;
    public float radius = 0;
    public String province = null;

    //获取陀螺仪传感器的实例
    private SensorManager sensorManager;

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public  void onReceiveLocation(BDLocation location) {
            if(location == null || mMapView == null){
                //Toast.makeText(MainActivity.this,"定位失败，请重试",Toast.LENGTH_SHORT);
                Log.d("ERROR","获取定位数据失败");
                return;
            }

            Log.d("SUCCESS","获取定位数据成功");
            Log.d("Latitude", String.valueOf(latitude));
            Log.d("Longitude", String.valueOf(longitude));
            Log.d("Province", String.valueOf(province));


            //第一次定位时进行地图放大
            if(isFirstLocation){
                isFirstLocation = false;

                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                update = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomTo(16f);
                mBaiduMap.animateMapStatus(update);

            }
            //获取经纬度以及省份信息
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            radius = location.getRadius();
            province = location.getProvince();

            //MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(mRotateDegree).latitude(location.getLatitude()).longitude(location.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDK初始化
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //全屏窗口处理
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maps_layout);
        mMapView = (MapView)findViewById(R.id.bmapView);


        //叠加实时路况图层
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setTrafficEnabled(true);


        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //定位图标的配置[跟随]
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true, BitmapDescriptorFactory.fromBitmap(null)));

        //定位初始化
        mLocationClient = new LocationClient(this);

        //定位参数配置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);

        mLocationClient.setLocOption(option);

        //注册监听器
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        //开启地图定位图层
        mLocationClient.start();


        //查看日志
        Log.d("Locating","开启定位成功");

        //传感器服务
        sensorManager = (SensorManager)getSystemService(Context. SENSOR_SERVICE);
        Sensor accesensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magsensor = sensorManager.getDefaultSensor((Sensor.TYPE_MAGNETIC_FIELD));

        //注册监听器
        sensorManager.registerListener(listener,accesensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,magsensor,SensorManager.SENSOR_DELAY_GAME);

    }

    private SensorEventListener listener = new SensorEventListener() {

        float[] acceValues = new float[3];
        float[] magValues = new float[3];
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                acceValues = event.values.clone();
            }else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                magValues = event.values.clone();
            }
            float[] values = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R,null,acceValues,magValues);
            SensorManager.getOrientation(R,values);
            //  //角度取反
            mRotateDegree = (float)Math.toDegrees(values[0]);
            //打印旋转角度
            Log.d("Sensor", String.valueOf(mRotateDegree));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


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

        if(sensorManager != null){
            sensorManager.unregisterListener(listener);
        }

        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
    }

}



