package com.jayaraj.CrazyChat7.ServicesActs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.jayaraj.CrazyChat7.J.SketchwareUtil;

public class MyService extends Service {
    private final Context myContext = this;

    public MyService() {
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        SketchwareUtil.showMessage(this.getApplicationContext(), "OnCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        SketchwareUtil.showMessage(this.getApplicationContext(), intent.getStringExtra("j"));
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        final Intent restartServiceTask = new Intent(this.getApplicationContext(), getClass());
        restartServiceTask.setPackage(this.getPackageName());
        final PendingIntent restartPendingIntent = PendingIntent.getService(this.getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        final AlarmManager myAlarmService = (AlarmManager) this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }


}