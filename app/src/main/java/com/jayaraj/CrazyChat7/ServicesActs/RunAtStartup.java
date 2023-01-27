package com.jayaraj.CrazyChat7.ServicesActs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RunAtStartup extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
//        if(Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)){
//            Toast.makeText(context, "screen offed", Toast.LENGTH_SHORT).show();
//        } else if(Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF)){
//            Toast.makeText(context, "screen offed", Toast.LENGTH_SHORT).show();
//        } else if(Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)){
//            Toast.makeText(context, "cc7 switch on", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Unspecified Intent filter", Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();
        final Intent service = new Intent(context, DbmsgActivity.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(service);
        } else {
            context.startService(service);
        }
//        PeriodicWorkRequest saveRequest =
//                new PeriodicWorkRequest.Builder(StartWorkDB.class, 15, TimeUnit.MINUTES)
//                         Constraints
//                        .build();
    }

}