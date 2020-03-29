package com.example.vehicletool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button_record;
    private Button button_map;
    private Button button_time;
    private Button button_camera;

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

