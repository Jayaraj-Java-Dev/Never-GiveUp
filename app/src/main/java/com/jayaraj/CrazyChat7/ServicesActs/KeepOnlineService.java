package com.jayaraj.CrazyChat7.ServicesActs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jayaraj.CrazyChat7.HomeActs.HomeActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class KeepOnlineService extends Service {
    private SharedPreferences ChatRef,asp;
    private SharedPreferences live;
    private final Timer _timer = new Timer();
    private DatabaseReference dbOnOff;
    private Calendar onoffc;
    private String tmp;
    private HashMap<String,Object> onoffMap = new HashMap<>();
    private String Start_Time;
    public KeepOnlineService() { }

    @Override
    public void onCreate() {
        com.google.firebase.FirebaseApp.initializeApp(this);
        ChatRef = this.getSharedPreferences("ref", Context.MODE_PRIVATE);
        live = this.getSharedPreferences("ONLINE_SERVICE", Context.MODE_PRIVATE);
        asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);
        FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
        dbOnOff = _firebase.getReference("Profile/uid/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
        _OnCrt();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return Service.START_STICKY;
    }

    private void _OnCrt(){
        onoffc = Calendar.getInstance();
        Start_Time = String.valueOf(onoffc.getTimeInMillis());
        live.edit().putString("last_service_time",Start_Time).apply();
        Loop();
    }
    private void Loop(){
        onoffMap = new HashMap<>();
        onoffMap.put("ON", "true");
        dbOnOff.child("data").updateChildren(onoffMap);
        onoffMap.clear();
        if(!Start_Time.equals(live.getString("last_service_time",""))){
            Toast.makeText(this, "Stoped", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
        _timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (asp.getString("ashome", "s").equals("s") && asp.getString("aschathome", "d").equals("d")) {
                    stopSelf();
                } else if (!Start_Time.equals(live.getString("last_service_time", ""))) {
                    stopSelf();
                }
                if (asp.getString("ashome", "").equals("r") || asp.getString("aschathome", "").equals("r")) {
                    onoffc = Calendar.getInstance();
                    tmp = String.valueOf(onoffc.getTimeInMillis());
                    tmp = tmp.substring(0, 10).concat("000");
                    onoffMap = new HashMap<>();
                    if (ChatRef.getString("sts", "").equals("")) {
                        onoffMap.put("STS", "");
                        onoffMap.put("ChatText", "");
                    } else {
                        onoffMap.put("STS", ChatRef.getString("sts", ""));
                        onoffMap.put("ChatText", ChatRef.getString("ChatText", ""));
                    }
                    onoffMap.put("SEEN", tmp);
                    dbOnOff.child("data").updateChildren(onoffMap);
                    onoffMap.clear();
                }
            }
        }, 1, 1000);
    }

    @Override
    public void onDestroy() {
        onoffMap = new HashMap<>();
        onoffMap.put("ON", "false");
        dbOnOff.child("data").updateChildren(onoffMap);
        onoffMap.clear();
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
    }
}