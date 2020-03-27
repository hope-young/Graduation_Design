package com.example.vehicletool;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Record extends MainActivity{

    private Button startrecord;
    private Button stoprecord;
    private TextView recordingTime;
    private String time;
    private int Count;
    private String str;

    //采用Chronometer计时方法
    private Chronometer chronometer;

    //录音对象的声明
    private MediaRecorder mRecorder;

    //开始计时
    private <chronometer> void timeStart(){
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }

    //停止计时
    private void timeStop(){
        chronometer.stop();
    }

    private void startRecording(){
        mRecorder = new MediaRecorder();
        //设置声音来源
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置音频格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        //录音文件
        mRecorder.setOutputFile(getExternalCacheDir() + "/demo.acc");
        //设置编码器
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        //
        try{
            mRecorder.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }

        //开始录音
        mRecorder.start();

    }

    private void stopRecording(){
        //停止录音
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        //开始录音
        recordingTime = (TextView)findViewById(R.id.recordingtime);
        startrecord = (Button)findViewById(R.id.startrecord);
        startrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText("");
                //Log.d("RECORD","start record");
                startRecording();
                Log.d("RECORD","start record");

                //显示计时
                Log.d("TIMING","开始计时");
                timeStart();

            }
        });

        stoprecord = (Button)findViewById(R.id.stoprecord);
        stoprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RECORD","stop record");

                stopRecording();
                Toast.makeText(Record.this,"录音已保存至/data/com.example.vehicle/cache/demo.acc",Toast.LENGTH_SHORT).show();

                //停止计时
                timeStop();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

}
