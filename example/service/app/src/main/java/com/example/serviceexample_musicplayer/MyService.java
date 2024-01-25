package com.example.serviceexample_musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    MediaPlayer mp;
    IBinder binder=new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mp= MediaPlayer.create(this, R.raw.butterfly);
        Toast.makeText(getApplicationContext(), "onCreate()",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp.start();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("default","default", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            Notification.Builder notification =
                    new Notification.Builder(this, notificationChannel.getId())
                            .setContentTitle("음악 재생 중")
                            .setContentText("음악...")
                            .setSmallIcon(R.drawable.ic_launcher_foreground);
            startForeground(1, notification.build());
        }
        Toast.makeText(getApplicationContext(), "onStartCommand()",Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onBind()",Toast.LENGTH_SHORT).show();
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp!=null){
            mp.stop();
            mp.release();
            mp=null;
        }
        Toast.makeText(getApplicationContext(), "onDestroy()",Toast.LENGTH_SHORT).show();
    }

    public void onMusicPause() {
        if(mp!=null&&mp.isPlaying()){
            mp.pause();
            Toast.makeText(getApplicationContext(), "onPause()",Toast.LENGTH_SHORT).show();
        }
    }

    public class LocalBinder extends Binder {
        MyService getService(){
            Toast.makeText(getApplicationContext(), "getService()",Toast.LENGTH_SHORT).show();
            return MyService.this;
        }
    }

}