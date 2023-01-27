package com.jayaraj.CrazyChat7.ServicesActs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
public class StartWorkDB extends Worker {
    Context con;
    public StartWorkDB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        con = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Toast.makeText(con, "Work Begin", Toast.LENGTH_SHORT).show();
        Intent service =  new Intent(con, DbmsgActivity.class);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            con.startForegroundService(service);
        } else {
            con.startService(new Intent(con, DbmsgActivity.class));
        }
        return null;
    }
}
