package com.example.serviceexample_musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyService mService;
    boolean isBound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start=findViewById(R.id.start);
        Button pause=findViewById(R.id.pause);
        Button stop=findViewById(R.id.stop);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.start){
            startMusic();
        }else if(v.getId()==R.id.pause){
            pauseMusic();
        }else if(v.getId()==R.id.stop){
            stopMusic();
        }
    }

    void startMusic(){
        Intent intent= new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else{
            startService(intent);
        }
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    void pauseMusic(){
        if(isBound){
            mService.onMusicPause();
        }
    }

    void stopMusic(){
        Intent intent= new Intent(this, MyService.class);
        if(isBound) unbindService(connection);
        stopService(intent);
        isBound=false;
    }

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(getApplicationContext(), "onServiceConnected()",Toast.LENGTH_SHORT).show();
            MyService.LocalBinder binder=(MyService.LocalBinder) service;
            mService=binder.getService();
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplicationContext(), "onServiceDisconnected()",Toast.LENGTH_SHORT).show();
            isBound=false;
        }
    };

}