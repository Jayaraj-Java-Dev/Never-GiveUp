package com.jayaraj.CrazyChat7.J;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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
import com.jayaraj.CrazyChat7.LoginActs.IntroActivity;
import com.jayaraj.CrazyChat7.LoginActs.OtpLoginActivity;
import com.jayaraj.CrazyChat7.LoginActs.UserActivity;
import com.jayaraj.CrazyChat7.OtherActs.PermissionsActivity;
import com.jayaraj.CrazyChat7.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DeveloperActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear1;
    private ImageView imageviewBackGround;
    private ImageView imageview2;
    private TextView cc1,cc2,cc3,cc4;

    private final Intent i1 = new Intent();
    private TimerTask timer;
    private FirebaseAuth auth;
    private SharedPreferences welcome;
    private final Intent intent = new Intent();
    private Jay jay;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_developer);
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this.initialize(savedInstanceState);
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear1 = this.findViewById(R.id.linear1);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.imageview2 = this.findViewById(R.id.imageview2);
        this.cc1 = this.findViewById(R.id.cc1);
        this.cc2 = this.findViewById(R.id.cc2);
        this.cc3 = this.findViewById(R.id.cc3);
        this.cc4 = this.findViewById(R.id.cc4);
//        ShimmerFrameLayout container = findViewById(R.id.shm1);
//        container.startShimmer();

        try{
            cc4.setText("v".concat(getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0).versionName));
        } catch (Exception e){
            System.out.println(e);
        }

        this.auth = FirebaseAuth.getInstance();
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.welcome = this.getSharedPreferences("welcome", Context.MODE_PRIVATE);
        this._Bg();

        this.cc3.setOnClickListener(v -> {
            final Intent mw = new Intent(Intent.ACTION_VIEW);
            mw.setData(Uri.parse(this.jay.MyWeb));
            this.startActivity(mw);
        });

    }


    private final ArrayList<HashMap<String, Object>> tlm = new ArrayList<>();
    private SharedPreferences ChatList, msg;

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
        this.cc3.setTypeface(Typeface.createFromAsset(this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.cc4.setTypeface(Typeface.createFromAsset(this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);

        this.cc1.setTextColor(Color.parseColor(jay.col(1,1)));
        this.cc2.setTextColor(Color.parseColor(jay.col(1,1)));
        this.cc3.setTextColor(Color.parseColor(jay.col(1,2)));
        this.cc4.setTextColor(Color.parseColor(jay.col(1,2)));

        this._SetBackground(this.cc3, 100, 20, jay.col(0,3), false);

//        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        w.addFlags(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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
    public void _SetBackground(View _view, double _radius, double _shadow, String _color, boolean _ripple) {
        if (_ripple) {
            final android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setColor(Color.parseColor(_color));
            gd.setCornerRadius((int) _radius);
            _view.setElevation((int) _shadow);
            final android.content.res.ColorStateList clrb = new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#9e9e9e")});
            final android.graphics.drawable.RippleDrawable ripdrb = new android.graphics.drawable.RippleDrawable(clrb, gd, null);
            _view.setClickable(true);
            _view.setBackground(ripdrb);
        } else {
            final android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setColor(Color.parseColor(_color));
            gd.setCornerRadius((int) _radius);
            _view.setBackground(gd);
            _view.setElevation((int) _shadow);
        }
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