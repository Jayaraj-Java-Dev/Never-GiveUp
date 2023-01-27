package com.jayaraj.CrazyChat7.ServicesActs;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.ChatHomeActs.ChathomeActivity;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;
import com.jayaraj.CrazyChat7.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class DbmsgActivity extends Service {
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final double ln = 0;
    private final double n = 0;
    private String positionval = "";
    private HashMap<String, Object> onreMap = new HashMap<>();
    private HashMap<String, Object> onoffMap = new HashMap<>();
    private boolean stoper;
    private HashMap<String, Object> tmap2 = new HashMap<>();
    private String TmpStr = "";
    private HashMap<String, Object> templistmap1 = new HashMap<>();
    private ArrayList<HashMap<String, Object>> tlistmap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> nestlm = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> nlistmap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> nlistmap2 = new ArrayList<>();
    private String Tmp = "";
    private DatabaseReference dbMy = this._firebase.getReference("Receiver");
    private DatabaseReference dbSend = this._firebase.getReference("Sender");
    private DatabaseReference dbSendGame = this._firebase.getReference("SenderUidRandGame");
    private DatabaseReference dbRand = this._firebase.getReference("Rand");
    private DatabaseReference dbOnOff = this._firebase.getReference("Rand");
    private ChildEventListener _dbMy_child_listener;
    private ValueEventListener vel;
    private SharedPreferences msg;
    private SharedPreferences list;
    private SharedPreferences asp;
    private SharedPreferences imgurls;
    private SharedPreferences ChatList;
    private SharedPreferences ChatRef, spbts;
    private SharedPreferences FriendReq;
    private FirebaseAuth auth;
    private RemoteInput remoteInput;

    private Calendar msgC = Calendar.getInstance();
    private Calendar cal = Calendar.getInstance();

    private Bitmap img;
    //private Intent activityIntent;
    //private PendingIntent contentIntent;
    //NotificationManagerCompat notificationManagerCompat;
    private String StartUpTime;

    public DbmsgActivity() {
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // Toast.makeText(this, "Receiving something", Toast.LENGTH_SHORT).show();

//        Toast.makeText(this, "onc", Toast.LENGTH_SHORT).show();
        this.FriendReq = this.getSharedPreferences("request", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.spbts = this.getSharedPreferences("botomsheet", Context.MODE_PRIVATE);
        this.StartUpTime = String.valueOf(System.currentTimeMillis());
        this.asp.edit().putString("SERVICE LIVE", this.StartUpTime).apply();
        this.img = BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon);
        this.stoper = false;
        com.google.firebase.FirebaseApp.initializeApp(this);
        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.list = this.getSharedPreferences("list", Context.MODE_PRIVATE);
        this.imgurls = this.getSharedPreferences("urls", Context.MODE_PRIVATE);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.ChatRef = this.getSharedPreferences("ref", Context.MODE_PRIVATE);
        this.auth = FirebaseAuth.getInstance();
        this.dbMy = this._firebase.getReference("chat/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat("/notify")));
        this.vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if (DbmsgActivity.this.StartUpTime.equals(DbmsgActivity.this.asp.getString("SERVICE LIVE", ""))) {
                    DbmsgActivity.this.tlistmap = new ArrayList<>();
                    try {
                        final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                        };
                        for (final DataSnapshot _data : snapshot.getChildren()) {
                            final HashMap<String, Object> _map = _data.getValue(_ind);
                            DbmsgActivity.this.tlistmap.add(_map);
                        }
                    } catch (final Exception _e) {
                        _e.printStackTrace();
                    }
                    SketchwareUtil.sortListMap(DbmsgActivity.this.tlistmap, "ms", false, true);
                    for (int ln = 0; ln < DbmsgActivity.this.tlistmap.size(); ln++) {
                        if ("".equals(DbmsgActivity.this.asp.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString(), ""))) {

                            DbmsgActivity.this.asp.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString(), "true").apply();
                            if ("".equals(DbmsgActivity.this.msg.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), ""))) {
                                DbmsgActivity.this._addToChatlist(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString());
                            } else {

                            }
                            if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("cus")) {
                                if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("cat")).toString().equals("frdreq")) {
                                    if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("stage")).toString().equals("0")) {
                                        DbmsgActivity.this._Noty(DbmsgActivity.this.ChatList.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("n"), ""), "Has Sent Friend Request", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), 7);
                                        final int n = Integer.parseInt(DbmsgActivity.this.FriendReq.getString("n", "0"));
                                        DbmsgActivity.this.FriendReq.edit().putString(String.valueOf(n), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString()).apply();
                                        DbmsgActivity.this.FriendReq.edit().putString("n", String.valueOf(n + 1)).apply();
                                    } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("stage")).toString().equals("1")) {
                                        DbmsgActivity.this._Noty(DbmsgActivity.this.ChatList.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("n"), ""), "Accepted Your Friend Request", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), 7);
                                    }
                                } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("cat")).toString().equals("ttt")) {
                                    //Game
                                    DbmsgActivity.this.spbts.edit().putString("reqBy", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString()).apply();
                                    if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("stage")).toString().equals("req")) {
                                        DbmsgActivity.this._Noty(DbmsgActivity.this.ChatList.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("n"), ""), "Requesting To Play", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), -1);
                                        DbmsgActivity.this.sendGameMsg("gotreq", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString());
                                        DbmsgActivity.this.spbts.edit().putString("reqGame", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).apply();
                                    } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("stage")).toString().equals("gotreq")) {
                                        DbmsgActivity.this.spbts.edit().putString("reqGot", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).apply();
                                    } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("stage")).toString().equals("allow")) {
                                        DbmsgActivity.this.spbts.edit().putString("reqAccept", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).apply();
                                    } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("stage")).toString().equals("deny")) {
                                        DbmsgActivity.this.spbts.edit().putString("reqDeny", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).apply();
                                    } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("stage")).toString().equals("inOtherChat")) {
                                        DbmsgActivity.this.spbts.edit().putString("inOtherChat", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).apply();
                                    }
                                }
                                DbmsgActivity.this.dbMy.child(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).removeValue();
                            } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("sugtheme")) {

                            } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("like")) {
                                if (DbmsgActivity.this.msg.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), "").equals("")) {
                                } else {
                                    DbmsgActivity.this._updateMsg(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), "sidelike", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typl")).toString());
                                }
                                DbmsgActivity.this.dbMy.child(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).removeValue();
                                DbmsgActivity.this._notifyUpdate("r");
                            } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("got")) {
                                if (DbmsgActivity.this.msg.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), "").equals("")) {
                                } else {
                                    DbmsgActivity.this._updateMsg(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), "stage", "3");
                                }
                                DbmsgActivity.this.dbMy.child(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).removeValue();
                                DbmsgActivity.this._notifyUpdate("r");
                            } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("saw")) {
                                if (DbmsgActivity.this.msg.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), "").equals("")) {
                                } else {
                                    DbmsgActivity.this._updateMsg(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), "stage", "4");
                                }
                                DbmsgActivity.this.dbMy.child(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).removeValue();
                                DbmsgActivity.this._notifyUpdate("r");
                            } else if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("nsaw")) {
                                if (DbmsgActivity.this.msg.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), "").equals("")) {
                                } else {
                                    DbmsgActivity.this._updateNmsg(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString(), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("ngid")).toString(), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("cmap")).toString(), Double.parseDouble(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("pos")).toString()));
                                    DbmsgActivity.this.list.edit().putString("usstage", "4").apply();
                                    DbmsgActivity.this.list.edit().putString("usMid", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString()).apply();
                                    DbmsgActivity.this.list.edit().putString("usid", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("ngid")).toString()).apply();
                                    DbmsgActivity.this.list.edit().putString("us", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();

                                }
                                DbmsgActivity.this.dbMy.child(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).removeValue();
                            } else {
                                if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("updateURL")) {
                                    DbmsgActivity.this.imgurls.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString().concat(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("pos")).toString()), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("url")).toString()).apply();
                                    DbmsgActivity.this.dbMy.child(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).removeValue();
                                    DbmsgActivity.this.list.edit().putString("usurl", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("url")).toString()).apply();
                                    DbmsgActivity.this.list.edit().putString("usMid", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("gid")).toString()).apply();
                                    DbmsgActivity.this.list.edit().putString("usid", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("ngid")).toString()).apply();
                                    DbmsgActivity.this.list.edit().putString("us1", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
                                    DbmsgActivity.this._chated(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString());
                                } else {
                                    DbmsgActivity.this.msg.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), String.valueOf((long) (Double.parseDouble(DbmsgActivity.this.msg.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), "")) + 1))).apply();
                                    DbmsgActivity.this.positionval = DbmsgActivity.this.msg.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), "");
                                    DbmsgActivity.this.msg.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat(DbmsgActivity.this.positionval.concat("cmap")), new Gson().toJson(DbmsgActivity.this.tlistmap.get(ln))).apply();
                                    DbmsgActivity.this.msg.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString(), new Gson().toJson(DbmsgActivity.this.tlistmap.get(ln))).apply();
                                    DbmsgActivity.this.msg.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat(DbmsgActivity.this.positionval.concat("ms")), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("ms")).toString()).apply();
                                    DbmsgActivity.this.msg.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString().concat("pos"), DbmsgActivity.this.positionval).apply();
                                    if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("3")) {
                                        DbmsgActivity.this.ChatList.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("lmsg"), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("msg")).toString().concat(" Audio")).apply();
                                        DbmsgActivity.this._Noty(DbmsgActivity.this.ChatList.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("n"), ""), "Audio", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), 3);
                                    } else {
                                        if (Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("typ")).toString().equals("999")) {
                                            DbmsgActivity.this.ChatList.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("lmsg"), "Media File").apply();
                                            DbmsgActivity.this._Noty(DbmsgActivity.this.ChatList.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("n"), ""), "Media File", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), 2);
                                        } else {
                                            DbmsgActivity.this.ChatList.edit().putString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("lmsg"), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("msg")).toString()).apply();
                                            DbmsgActivity.this._Noty(DbmsgActivity.this.ChatList.getString(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("n"), ""), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("msg")).toString(), Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString(), 1);
                                        }
                                    }
                                    DbmsgActivity.this.dbMy.child(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString()).removeValue();
                                    DbmsgActivity.this.onreMap = new HashMap<>();
                                    DbmsgActivity.this.msgC = Calendar.getInstance();
                                    DbmsgActivity.this._notifyUpdate("rs");
                                    DbmsgActivity.this.onreMap.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(DbmsgActivity.this.msgC.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
                                    DbmsgActivity.this.onreMap.put("gid", Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("id")).toString());
                                    DbmsgActivity.this.onreMap.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    DbmsgActivity.this.onreMap.put("ms", String.valueOf(DbmsgActivity.this.msgC.getTimeInMillis()));
                                    DbmsgActivity.this.onreMap.put("typ", "got");
                                    DbmsgActivity.this.dbSend = DbmsgActivity.this._firebase.getReference("chat/".concat(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString().concat("/notify")));
                                    DbmsgActivity.this.dbSend.child(Objects.requireNonNull(DbmsgActivity.this.onreMap.get("id")).toString()).updateChildren(DbmsgActivity.this.onreMap);
                                    DbmsgActivity.this.onreMap.clear();
                                    DbmsgActivity.this._chated(Objects.requireNonNull(DbmsgActivity.this.tlistmap.get(ln).get("by")).toString());

                                }
                            }
                        }
                    }
                    DbmsgActivity.this.stoper = false;
                } else {
                    DbmsgActivity.this.dbMy.removeEventListener(DbmsgActivity.this.vel);
                    DbmsgActivity.this.stopSelf();
                }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        };
        this.dbMy.addValueEventListener(this.vel);
        this.dbOnOff = this._firebase.getReference("Profile/uid/".concat(FirebaseAuth.getInstance().getCurrentUser().getUid()));
    }

    //Game
    public void sendGameMsg(final String msg, final String to) {
        this.dbSendGame = this._firebase.getReference("chat/".concat(to.concat("/notify")));
        HashMap<String, Object> map = new HashMap<>();
        this.msgC = Calendar.getInstance();
        final int mil = (int) this.msgC.getTimeInMillis();
        map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))).concat(String.valueOf(this.msgC.getTimeInMillis()))));
        map.put("ms", String.valueOf(mil));
        map.put("msg", "requesting to play");
        map.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("stage", msg);
        map.put("typ", "cus");
        map.put("cat", "ttt");
        map.put("gid", "escape");
        this.dbSendGame.child(Objects.requireNonNull(map.get("id")).toString()).updateChildren(map);
        map.clear();
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {

        this.onoffMap = new HashMap<>();
        this.onoffMap.put("ON", "false");
        this.dbOnOff.child("data").updateChildren(this.onoffMap);
        this.onoffMap.clear();

        final Intent restartServiceTask = new Intent(this.getApplicationContext(), getClass());
        restartServiceTask.setPackage(this.getPackageName());

        final PendingIntent restartPendingIntent;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            restartPendingIntent = PendingIntent.getService(this.getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_IMMUTABLE);
        } else {
            restartPendingIntent = PendingIntent.getService(this.getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        }


        final AlarmManager myAlarmService = (AlarmManager) this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 2000, restartPendingIntent);

        super.onTaskRemoved(rootIntent);

    }


    public boolean _NotyVerfy(final String uid, final int typ) {
        if (typ == -1 || typ == 7) {
            return true;
        }
        if (this.asp.getString("ashome", "").equals("r")) {
            return false;
        } else if ((this.asp.getString("ashome", "").equals("p") || this.asp.getString("ashome", "").equals("s")) && this.asp.getString("aschathome", "").equals("r")) {
            return !uid.equals(this.asp.getString("aschathomecurrent", ""));
        }
        return true;
    }

    public void _Noty(String Title, final String Msg, final String uid, final int typ) {
        if (this._NotyVerfy(uid, typ)) {
            final int id;
            if (Title.equals("")) {
                Title = "Unknown Person?";
                id = 999;
            } else {
                final String phone = this.ChatList.getString(uid + "p", "");
                id = Integer.parseInt(phone.substring(5));
            }
            final Intent innoti = new Intent(this, ChathomeActivity.class);
            innoti.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            innoti.putExtra("UID", uid);
            if (typ == -1) {
                innoti.putExtra("Request", "true");
            }
            this.asp.edit().putString("FROMnoty", "true").apply();

            final PendingIntent pendnoti;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendnoti = PendingIntent.getActivity(this.getApplicationContext(), 0, innoti, PendingIntent.FLAG_MUTABLE);
            } else {
                pendnoti = PendingIntent.getActivity(this.getApplicationContext(), 0, innoti, PendingIntent.FLAG_ONE_SHOT);
            }

            ArrayList<String> templist = new ArrayList<String>();
            if (this.asp.getString(uid + "tempData", "").equals("")) {

            } else {
                templist = new Gson().fromJson(this.asp.getString(uid + "tempData", ""), new TypeToken<ArrayList<String>>() {
                }.getType());
            }
            templist.add(Msg);
            final Person pers = new Person.Builder().setName(Title).setImportant(true).build();

            final NotificationCompat.MessagingStyle mstyl = new NotificationCompat.MessagingStyle(pers);
            for (int n = 0; n < templist.size(); n++) {
                final String msgs = templist.get(n);
                mstyl.addMessage(new NotificationCompat.MessagingStyle.Message(msgs, System.currentTimeMillis(), pers));
            }
            final String dat = new Gson().toJson(templist);
            this.asp.edit().putString(uid + "tempData", dat).apply();
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), "CHAT").setSmallIcon(R.drawable.app_icon).setLargeIcon(this.img).setStyle(mstyl).setAutoCancel(true).setOngoing(false).setOnlyAlertOnce(false).setContentIntent(pendnoti).setPriority(NotificationCompat.PRIORITY_HIGH);
            final NotificationManagerCompat noti = NotificationManagerCompat.from(this.getApplicationContext());
            noti.notify(id, builder.build());

        }
    }


    public void _updateMsg(String _id, String _key, String _val) {
        this.tmap2 = new HashMap<>();
        this.tmap2 = new Gson().fromJson(this.msg.getString(_id, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.tmap2.put(_key, _val);
        this.TmpStr = new Gson().toJson(this.tmap2);
        this.msg.edit().putString(_id, this.TmpStr).apply();
        if (_key.equals("sidelike")) {
            this.msg.edit().putString(_id + "sidelike", _val).apply();
        }
        this.msg.edit().putString(Objects.requireNonNull(this.tmap2.get("cmap")).toString(), this.TmpStr).apply();
        this.tmap2.clear();
    }


    public void _updateNmsg(String _mid, String _uppid, String _cmap, double _pos) {
        this.templistmap1 = new HashMap<>();
        this.templistmap1 = new Gson().fromJson(this.msg.getString(_mid, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.nestlm = new Gson().fromJson(Objects.requireNonNull(this.templistmap1.get("list")).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        if (_uppid.equals(Objects.requireNonNull(this.nestlm.get((int) _pos).get("id")).toString())) {
            this.nestlm.get((int) _pos).put("stage", "4");
            this.templistmap1.put("list", new Gson().toJson(this.nestlm));
            this.msg.edit().putString(_mid, new Gson().toJson(this.templistmap1)).apply();
            this.msg.edit().putString(_cmap, new Gson().toJson(this.templistmap1)).apply();
        } else {

        }
        this.templistmap1.clear();
    }


    public void _notifyUpdate(String _Key) {
        if ("rs".equals(_Key)) {
            this.list.edit().putString("a", String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
        } else {
            if ("sr".equals(_Key)) {
                this.list.edit().putString("a", String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
            } else {
                for (int n = 0; n < _Key.length(); n++) {
                    this.list.edit().putString(_Key.substring(n, n + 1), String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
                }
            }
        }
    }

    public void _chated(String _uid) {
        this.cal = Calendar.getInstance();
        this.ChatList.edit().putString(_uid.concat("lm"), String.valueOf(this.cal.getTimeInMillis())).apply();
        this.ChatRef.edit().putString("ref", String.valueOf((long) (SketchwareUtil.getRandom(1, 9999999)))).apply();
    }

    public void _addToChatlist(String _uid) {
        this.msg.edit().putString(_uid, "0").apply();
        this.nlistmap.clear();
        this.Tmp = this.ChatList.getString("list", "");
        if ("".equals(this.Tmp)) {

        } else {
            this.nlistmap = new Gson().fromJson(this.Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
        }
        {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("uid", _uid);
            this.nlistmap.add(_item);
        }

        this.nlistmap.get(this.nlistmap.size() - 1).put("img", "");
        this.nlistmap.get(this.nlistmap.size() - 1).put("name", "");
        this.nlistmap.get(this.nlistmap.size() - 1).put("phone", "");
        int tempStore = this.nlistmap.size() - 1;
        this.ChatList.edit().putString("list", new Gson().toJson(this.nlistmap)).apply();
        this.ChatList.edit().putString(_uid.concat("n"), "").apply();
        this.ChatList.edit().putString(_uid.concat("i"), "").apply();
        this.ChatList.edit().putString(_uid.concat("p"), "").apply();
        this.cal = Calendar.getInstance();
        this.ChatList.edit().putString(_uid.concat("lm"), String.valueOf(this.cal.getTimeInMillis())).apply();
        this.list.edit().putString(_uid.concat("lv"), "0").apply();
        this.dbRand = this._firebase.getReference("Profile/uid/".concat(_uid));
        this.dbRand.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot _dataSnapshot) {
                DbmsgActivity.this.nlistmap2 = new ArrayList<>();
                try {
                    final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (final DataSnapshot _data : _dataSnapshot.getChildren()) {
                        final HashMap<String, Object> _map = _data.getValue(_ind);
                        DbmsgActivity.this.nlistmap2.add(_map);
                    }
                } catch (final Exception _e) {
                    _e.printStackTrace();
                }
                DbmsgActivity.this.nlistmap.clear();
                DbmsgActivity.this.Tmp = DbmsgActivity.this.ChatList.getString("list", "");
                if ("".equals(DbmsgActivity.this.Tmp)) {

                } else {
                    DbmsgActivity.this.nlistmap = new Gson().fromJson(DbmsgActivity.this.Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                    }.getType());
                }

                DbmsgActivity.this.nlistmap.get(tempStore).put("img", Objects.requireNonNull(DbmsgActivity.this.nlistmap2.get(0).get("URL")).toString());
                DbmsgActivity.this.nlistmap.get(tempStore).put("name", Objects.requireNonNull(DbmsgActivity.this.nlistmap2.get(0).get("NAME")).toString());
                DbmsgActivity.this.nlistmap.get(tempStore).put("phone", Objects.requireNonNull(DbmsgActivity.this.nlistmap2.get(0).get("PHONE")).toString());
                DbmsgActivity.this.ChatList.edit().putString("list", new Gson().toJson(DbmsgActivity.this.nlistmap)).apply();
                DbmsgActivity.this.ChatList.edit().putString(Objects.requireNonNull(DbmsgActivity.this.nlistmap2.get(0).get("PHONE")).toString(), "Added").apply();
                DbmsgActivity.this.ChatList.edit().putString(_uid.concat("n"), Objects.requireNonNull(DbmsgActivity.this.nlistmap2.get(0).get("NAME")).toString()).apply();
                DbmsgActivity.this.ChatList.edit().putString(_uid.concat("i"), Objects.requireNonNull(DbmsgActivity.this.nlistmap2.get(0).get("URL")).toString()).apply();
                DbmsgActivity.this.ChatList.edit().putString(_uid.concat("p"), Objects.requireNonNull(DbmsgActivity.this.nlistmap2.get(0).get("PHONE")).toString()).apply();
                DbmsgActivity.this.cal = Calendar.getInstance();
                DbmsgActivity.this.ChatList.edit().putString(_uid.concat("lm"), String.valueOf(DbmsgActivity.this.cal.getTimeInMillis())).apply();
                DbmsgActivity.this.ChatRef.edit().putString("ref", String.valueOf((long) (SketchwareUtil.getRandom(1, 9999999)))).apply();

            }

            @Override
            public void onCancelled(@NonNull final DatabaseError _databaseError) {
            }
        });
    }

}