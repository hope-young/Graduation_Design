package com.example.vehicletool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private Button button_record;
    private Button button_map;
    private Button button_time;
    private Button button_camera;
    public String region = "ap-chengdu";
    private Context context;

    //数据上传至COS对象存储桶（永久密钥）
    public void send_message(String Message,String cosPath){

        context = getApplicationContext();

        // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .builder();

        String secretId = "AKIDCE4UcUVccMqwlInCRP8aw1Ugc5jZj9aH"; //永久密钥 secretId
        String secretKey = "cUYQP9AHz5u3T9lA3cyV2KabXEfpsqWw"; //永久密钥 secretKey

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

