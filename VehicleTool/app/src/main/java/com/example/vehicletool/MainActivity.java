package com.example.vehicletool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //导入百度地图（调用Maps_Layout）
        Button button_map = (Button)findViewById(R.id.button_map);
        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapsView.class);
                startActivity(intent);
            }
        });

        //速度显示

        //录音录像功能实现

        //紧急拨号
        final int[] time = {0};
        Button button_time = (Button)findViewById(R.id.button_time);
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
        }
        );

    }
}
