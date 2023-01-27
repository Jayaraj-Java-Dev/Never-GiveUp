package com.jayaraj.CrazyChat7.OtherActs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.MainActivity;
import com.jayaraj.CrazyChat7.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class PermissionsActivity extends Activity {
    private final Timer _timer = new Timer();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear3;
    private ImageView imageviewBackGround;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private TextView textview1;
    private Button button1;
    private ImageView imageview1;

    private TimerTask tim;
    private final Intent Int = new Intent();

    private Jay jay;


    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.permissions);
        this.initialize(_savedInstanceState);
        com.google.firebase.FirebaseApp.initializeApp(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            this.initializeLogic();
        } else {
            this.initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (Build.VERSION.SDK_INT > 29) {
                if ((this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) || (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                    Toast.makeText(this.getApplicationContext(), "Permission Not Granted", Toast.LENGTH_LONG).show();
                } else {
                    this._Go();
                }
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    if ((this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) || (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                        Toast.makeText(this.getApplicationContext(), "Permission Not Granted", Toast.LENGTH_LONG).show();
                    } else {
                        this._Go();
                    }
                } else {
                    this._Go();
                }
            }
        }
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear3 = this.findViewById(R.id.linear3);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.linear1 = this.findViewById(R.id.linear1);
        this.linear2 = this.findViewById(R.id.linear2);
        this.textview1 = this.findViewById(R.id.textview1);
        this.button1 = this.findViewById(R.id.button1);
        this.imageview1 = this.findViewById(R.id.imageview1);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.button1.setOnClickListener(_view -> {
            if (Build.VERSION.SDK_INT > 32) {
                if ((this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) ||this.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_DENIED|| (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                    this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.POST_NOTIFICATIONS}, 1000);
                } else {
                    this._Go();
                }
            } else
            if (Build.VERSION.SDK_INT > 29) {
                if ((this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) || (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                    this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                } else {
                    this._Go();
                }
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    if ((this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) || (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                        this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                    } else {
                        this._Go();
                    }
                } else {
                    this._Go();
                }
            }
        });
    }

    private void initializeLogic() {
        this._font();
        this._Ui();
        this._Bg();
        this._OnCrt();
    }


    public void _Bg() {
        //linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        if (FileUtil.isFile(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"))) {
            this.imageviewBackGround.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"), 1024, 1024));
        } else {
//            imageviewBackGround.setImageResource(R.drawable.bgi);
        }
        this.linear1BG.setBackgroundColor(Color.parseColor(this.jay.col(0,2)));
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


    public void _font() {
        this.textview1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.button1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
    }


    public void _Ui() {
        this._SetBackground(this.textview1, 40, 10, this.jay.col(0,1), true);
        this._SetBackground(this.button1, 40, 10, this.jay.col(0,1), true);
        this.textview1.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.button1.setTextColor(Color.parseColor(this.jay.col(1,1)));
    }


    public void _OnCrt() {
        this.textview1.setText("Checking For Allow\nStorage Permission");
        this.button1.setText("Allow");
    }


    public void _Go() {
        this.Int.setClass(this.getApplicationContext(), MainActivity.class);
        this.startActivity(this.Int);
        this.finish();
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
