package com.example.broadcastexample_battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int level=intent.getIntExtra("level",-1);
        int scale=intent.getIntExtra("scale",-1);
        float battery=level*100/(float)scale;
        boolean isLow=battery<80;
        
        if(isLow){
            Toast.makeText(
                    context,
                    "앱을 사용 위한 배터리가 충분하지 않습니다 : "+battery+"%",
                    Toast.LENGTH_LONG
            ).show();
        }

    }
}
