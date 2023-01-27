package com.jayaraj.CrazyChat7;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.HomeActs.HomeActivity;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.LoginActs.IntroActivity;
import com.jayaraj.CrazyChat7.LoginActs.OtpLoginActivity;
import com.jayaraj.CrazyChat7.LoginActs.UserActivity;
import com.jayaraj.CrazyChat7.OtherActs.PermissionsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear1;
    private ImageView imageviewBackGround;
    private ImageView imageview2;
    private TextView cc1,cc2;

    private final Intent i1 = new Intent();
    private TimerTask timer;
    private FirebaseAuth auth;
    private SharedPreferences welcome;
    private final Intent intent = new Intent();
    private Jay jay;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.main);
        this.initialize(_savedInstanceState);
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        com.google.firebase.FirebaseApp.initializeApp(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            this.initializeLogic();
        } else {
            this.initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            this.initializeLogic();
        }
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear1 = this.findViewById(R.id.linear1);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.imageview2 = this.findViewById(R.id.imageview2);
        this.cc1 = this.findViewById(R.id.cc1);
        this.cc2 = this.findViewById(R.id.cc2);
//
//        ShimmerFrameLayout container = findViewById(R.id.shm1);
//        container.startShimmer();
        this.auth = FirebaseAuth.getInstance();
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.welcome = this.getSharedPreferences("welcome", Context.MODE_PRIVATE);
    }

    private void initializeLogic() {
        this._Bg();
        this._permission_check();
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    public void _permission_check() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            Intent intent = new Intent();
//            String packageName = MainActivity.this.getPackageName();
//            PowerManager pm = (PowerManager) MainActivity.this.getSystemService(Context.POWER_SERVICE);
//
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + packageName));
//                startActivity(intent);
//            } else {
//
//            }
        } else {

        }
        if (Build.VERSION.SDK_INT >= 23) {
            if ((this.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_DENIED
                    || this.checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == android.content.pm.PackageManager.PERMISSION_DENIED)
                    || (this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED
                    || this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED)) {
                this.i1.setClass(this.getApplicationContext(), PermissionsActivity.class);
                this.startActivity(this.i1);
                this.finish();
            } else {
                this._StartAct();
            }
        } else {
            this._StartAct();
        }
    }

    private ArrayList<HashMap<String, Object>> tlm = new ArrayList<>();
    private SharedPreferences ChatList, msg;

    public void _StartAct() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            final String Tmp = this.ChatList.getString("list", "");
            if ("".equals(Tmp)) {

            } else {
                new Thread(() -> {
                    this.tlm = new Gson().fromJson(Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                    }.getType());
                    for (int n1 = 0; n1 < this.tlm.size(); n1++) {
                        this.ChatList.edit().putString(Objects.requireNonNull(this.tlm.get(n1).get("phone")).toString(), "").apply();
                        this.msg.edit().putString(Objects.requireNonNull(this.tlm.get(n1).get("uid")).toString(), "").apply();
                    }
                    this.ChatList.edit().putString("list", "").apply();
                }).start();

            }
        }
        this.timer = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(() -> {

                    if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
                        if ("".equals(MainActivity.this.welcome.getString("us", ""))) {
                            MainActivity.this.i1.setClass(MainActivity.this.getApplicationContext(), UserActivity.class);
                        } else {
                            MainActivity.this.i1.setClass(MainActivity.this.getApplicationContext(), HomeActivity.class);
                        }
                    } else {
                        if ("".equals(MainActivity.this.welcome.getString("weln", ""))) {
                            MainActivity.this.i1.setClass(MainActivity.this.getApplicationContext(), IntroActivity.class);
                        } else {
                            MainActivity.this.i1.setClass(MainActivity.this.getApplicationContext(), OtpLoginActivity.class);
                        }
                    }

                    MainActivity.this.startActivity(MainActivity.this.i1);
                    MainActivity.this.finish();
                });
            }
        };
        this._timer.schedule(this.timer, 3000);
    }


    public void _Bg() {
        //linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        if (FileUtil.isFile(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"))) {
            this.imageviewBackGround.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"), 1024, 1024));
        } else {
//            imageviewBackGround.setImageResource(R.drawable.bgi);
        }
        final Jay jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._SetGradientView(this.linear1BG, jay.col(0, 3), jay.col(0, 4));
        final Window w = this.getWindow();
        this.cc1.setTypeface(Typeface.createFromAsset(this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.cc2.setTypeface(Typeface.createFromAsset(this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.cc1.setTextColor(Color.parseColor(jay.col(1,1)));
        this.cc2.setTextColor(Color.parseColor(jay.col(1,1)));

        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0, 3)));

    }

    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) {
            public GradientDrawable getIns(final int a) {
                setCornerRadius(a);
                return this;
            }
        }.getIns((int) 0));
    }


    @Deprecated
    public void showMessage(final String _s) {
        Toast.makeText(this.getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(final View _v) {
        final int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(final View _v) {
        final int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(final int _min, final int _max) {
        final Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(final ListView _list) {
        final ArrayList<Double> _result = new ArrayList<Double>();
        final SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(final int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, this.getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return this.getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }
}
