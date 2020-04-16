package com.example.vehicletool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.heweather.plugin.view.HeContent;
import com.heweather.plugin.view.HeWeatherConfig;
import com.heweather.plugin.view.HorizonView;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.vehicletool.R.integer.Icon_Size;

public class MainActivity extends AppCompatActivity {

    private Button button_record;
    private Button button_map;
    private Button button_time;
    private Button button_camera;
    public String region = "ap-chengdu";
    private Context context;
    private HorizonView horizonView;


    //数据上传至COS对象存储桶（永久密钥）
    public void send_message(String Message,String cosPath){

        context = getApplicationContext();

        // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .builder();

        String secretId = "不给你"; //永久密钥 secretId
        String secretKey = "不给你"; //永久密钥 secretKey

        QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(secretId, secretKey, 300);

        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        // 初始化 TransferConfig
        TransferConfig transferConfig = new TransferConfig.Builder().build();

        // 初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);

        String bucket = "car-message-1301782340"; //存储桶，格式：BucketName-APPID
        //String srcPath = new File(context.getExternalCacheDir(), "exampleobject").toString(); //本地文件的绝对路径

        //String uploadId = null; //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
        // 上传对象
        //COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
        byte[] bytes = Message.getBytes(Charset.forName("UTF-8"));
        COSXMLUploadTask cosxmlUploadTask= transferManager.upload(bucket, cosPath, bytes);

        Log.d("SEND______:","Success!");
    }


    //和风天气插件配置
    private void Weather_View(){
        //调用和风天气的sdk显示天气插件
        horizonView = findViewById(R.id.weather_view);
        //horizonView.setDefaultBack(false);
        //horizonView.setStroke(5, Color.BLUE,1,Color.BLUE);
        //温度
        horizonView.addTemp(50,Color.WHITE);
        //天气图标
        horizonView.addWeatherIcon(50);
        horizonView.addLocation(25,Color.WHITE);
        //预警图标
        //horizonView.addAlarmIcon(R.integer.Icon_Size);
        //预警描述
        //horizonView.addAlarmTxt(R.integer.Icon_Size);

        //天气描述
        horizonView.addCond(25,Color.WHITE);

        //风力图标
        horizonView.addWindIcon(40);
        //风力
        horizonView.addWind(20,Color.WHITE);
        //空气质量描述
        //horizonView.addAqiText(Icon_Size,Color.WHITE);
        //空气质量
        //horizonView.addAqiQlty(Icon_Size);
        //空气指数
        //horizonView.addAqiNum(Icon_Size);
        //降雨图标
        //horizonView.addRainIcon(Icon_Size);
        //降雨详情
        //horizonView.addRainDetail(Icon_Size, Color.WHITE);
        //控件居中方式
        horizonView.setViewGravity(HeContent.GRAVITY_CENTER);

        //horizonView.setViewPadding(5,5,5,0);

        horizonView.show();
    }


    //动态申请权限
    private void request_permissions(){
        List<String> permissionList = new ArrayList<>();

        //判断是否授予权限
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.INTERNET);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        //列表为空，权限已经全部获取了
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1002);
        }else{
            //Toast.makeText(this,"已经获取全部权限！",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //请求权限
        request_permissions();

        //天气插件初始化
        HeWeatherConfig.init("4fa560955f4247a7a479d09583569be9");

        Weather_View();

        //导入百度地图（调用Maps_Layout）
        button_map = (Button)findViewById(R.id.button_map);

        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapsView.class);
                startActivity(intent);
            }
        });

        //速度显示


        //录音录像功能实现
        button_record = (Button)findViewById(R.id.button_record);
        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Record.class);
                startActivity(intent);
            }
        });


        //紧急拨号
        final int[] time = {0};

        button_time = (Button)findViewById(R.id.button_time);
        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time[0] == 0){
                    Toast.makeText(MainActivity.this, "Please Set Your Emergency Call Number!", Toast.LENGTH_SHORT).show();
                    time[0]++;
                }
                else {
                    Toast.makeText(MainActivity.this, "CALLING!!!", Toast.LENGTH_SHORT).show();
                    Intent intentcall = new Intent(Intent.ACTION_DIAL);
                    intentcall.setData(Uri.parse("tel:110"));
                    startActivity(intentcall);
                }
            }
        });

        //录像功能实现
        button_camera = (Button)findViewById(R.id.button_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"正在启动录像",Toast.LENGTH_SHORT).show();
                Intent intentmediarecorder = new Intent(MainActivity.this,mMediaRecorder.class);
                startActivity(intentmediarecorder);
            }
        });

    }

    //请求权限的回调方法
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        switch (requestCode) {
            case 1002:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(MainActivity.this, permission[i] + "权限被拒绝了 :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }


}

