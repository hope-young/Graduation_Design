package com.example.vehicletool;

import android.app.Activity;
import android.content.pm.ActivityInfo;
//import android.graphics.Camera;
//import android.hardware.camera2.*;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;

public class mMediaRecorder extends MainActivity {

    private SurfaceView mSurfaceview;
    private Button button_startstop;
    private boolean startFLAG = false;
    private boolean backFLAG = false;
    private MediaRecorder mediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    private Camera camera;
    private TextView textView;
    private File videoFile;
    private Chronometer chronometer;

    /*
    private android.os.Handler handler = new android.os.Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            text++;
            textView.setText(text+"");
            handler.postDelayed(this,1000);
        }
    }
    ;

     */


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


    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.media_recorder);

        mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        button_startstop = (Button) findViewById(R.id.start_stop);

        //横屏显示
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        //设置显示配置(全屏)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //设置分辨率
        mSurfaceview.getHolder().setFixedSize(1280,720);

        //不会自动熄屏
        mSurfaceview.getHolder().setKeepScreenOn(true);

        //RESET MEDIA对象
        mediaRecorder = new MediaRecorder();

        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(90);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        camera.unlock();

        mediaRecorder.setCamera(camera);

        mediaRecorder.reset();
        //设置声源
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置图像源
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //视频文件的输出格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //声音编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //图像编码
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        //mediaRecorder.setVideoSize(1280,720);
        //每秒四帧
        //mediaRecorder.setVideoFrameRate(20);

        //mediaRecorder.setOrientationHint(90);

        //预览
        //mediaRecorder.prepare();
        mediaRecorder.setPreviewDisplay(mSurfaceview.getHolder().getSurface());
        //camera.startPreview();

        //开始录制的监听
        button_startstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断拍摄状态
                if(!startFLAG){
                    if(!backFLAG) {
                        try {
                            //创建视频文件
                            videoFile = new File(getExternalCacheDir() + "/video.mp4");

                            mediaRecorder.setOutputFile(videoFile.getAbsoluteFile());
                            mediaRecorder.setPreviewDisplay(mSurfaceview.getHolder().getSurface());

                            mediaRecorder.prepare();
                            //开始录制
                            mediaRecorder.start();
                            timeStart();
                            Toast.makeText(mMediaRecorder.this, "开始录制", Toast.LENGTH_SHORT).show();
                            Log.d("VideoRecord", "开始录制");
                            startFLAG = true;
                            button_startstop.setText("stop");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        finish();

                    }
                }else {
                    //停止录制
                    mediaRecorder.stop();
                    timeStop();
                    mediaRecorder.release();

                    //mediaRecorder = null;
                    Toast.makeText(mMediaRecorder.this,"停止录制,视频信息已保存！",Toast.LENGTH_SHORT).show();
                    Log.d("VideoRecord","停止录制");
                    button_startstop.setText("back");
                    backFLAG = true;
                }
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
        camera.release();
        camera = null;
        super.onDestroy();
    }



}
