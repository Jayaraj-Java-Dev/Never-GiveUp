package com.jayaraj.CrazyChat7.OtherActs;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;


public class TextFullViewActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();
    private final String Fpath = "";
    private String fontName = "";
    private SharedPreferences msg;
    private SharedPreferences PHONE;
    private SharedPreferences list;

    TextView textview2;
    TextView textview1;
    LinearLayout linear1;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.textfullview);
        this.initialize(_savedInstanceState);
        com.google.firebase.FirebaseApp.initializeApp(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {
            this.initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            this.initializeLogic();
        }
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.linear1 = this.findViewById(R.id.linear1);
        this.textview1 = this.findViewById(R.id.textview1);
        this.textview2 = this.findViewById(R.id.textview2);

        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.PHONE = this.getSharedPreferences("PHONE", Context.MODE_PRIVATE);
        this.list = this.getSharedPreferences("list", Context.MODE_PRIVATE);

        this.textview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
            }
        });

    }

    private void initializeLogic() {
        this.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        {
            final android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
            final int d = (int) this.getApplicationContext().getResources().getDisplayMetrics().density;
            SketchUi.setColor(0xFFFFFFFF);
            SketchUi.setCornerRadius(d * 15);
            ((ViewGroup) this.getWindow().getDecorView()).getChildAt(0).setBackground(SketchUi);
        }
        final double pos = Double.parseDouble(this.getIntent().getStringExtra("pos"));
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(this._getMsg(pos, "by"))) {
            this.textview2.setText("You");
        } else {
            this.textview2.setText("He");
        }
        this.textview1.setText(this._getMsg(pos, "more"));
        this._changeActivityFont("product_more_block");
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String _getMsg(double _position, String _key) {
        if (_key.equals("ms")) {
            return (this.msg.getString(this.PHONE.getString("UID", "").concat(String.valueOf((long) (_position + 1)).concat(_key)), ""));
        } else {
            HashMap<String, Object> tmap = new HashMap<>();
            tmap = new Gson().fromJson(this.msg.getString(this.PHONE.getString("UID", "").concat(String.valueOf((long) (_position + 1)).concat("cmap")), ""), new TypeToken<HashMap<String, Object>>() {
            }.getType());
            if (tmap.containsKey(_key)) {
                return (Objects.requireNonNull(tmap.get(_key)).toString());
            } else {
                return ("");
            }
        }
    }

    // setTheme() should be set before setContentView() so a small hack to do this in sketchware
    @Override
    public void setContentView(final int layoutResID) {
        if (this.getIntent().getBooleanExtra("dialogTheme", true)) {
            this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setTheme(R.style.Theme_AppCompat_Light_Dialog);
            this.setFinishOnTouchOutside(true);

            //change true to false if you want to make dialog non cancellable when clicked outside
            //if you want to use this without app compat  change supportRequestWindowFeature() and setTheme() to below codes.
			/*
requestWindowFeature(Window.FEATURE_NO_TITLE);
setTheme(android.R.style.Theme_Dialog);
*/
            // Calling this allows the Activity behind this one to be seen again. Once all such Activities have been redrawn
            try {
                final java.lang.reflect.Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
                getActivityOptions.setAccessible(true);
                final Object options = getActivityOptions.invoke(this);
                final Class<?>[] classes = Activity.class.getDeclaredClasses();
                Class<?> translucentConversionListenerClazz = null;
                for (final Class clazz : classes) {
                    if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                        translucentConversionListenerClazz = clazz;
                    }
                }
                final java.lang.reflect.Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
                convertToTranslucent.setAccessible(true);
                convertToTranslucent.invoke(this, null, options);
            } catch (final Throwable ignored) {
            }
        }
        super.setContentView(layoutResID);
    }


    public void _changeActivityFont(@NonNull String _fontname) {
        this.fontName = "fonts/".concat(_fontname.concat(".ttf"));
        this.overrideFonts(this, this.getWindow().getDecorView());
    }

    private void overrideFonts(android.content.Context context, View v) {

        try {
            final Typeface
                    typeace = Typeface.createFromAsset(this.getAssets(), this.fontName);
            if ((v instanceof ViewGroup)) {
                final ViewGroup vg = (ViewGroup) v;
                for (int i = 0;
                     i < vg.getChildCount();
                     i++) {
                    final View child = vg.getChildAt(i);
                    this.overrideFonts(context, child);
                }
            } else {
                if ((v instanceof TextView)) {
                    ((TextView) v).setTypeface(typeace);
                } else {
                    if ((v instanceof EditText)) {
                        ((EditText) v).setTypeface(typeace);
                    } else {
                        if ((v instanceof Button)) {
                            ((Button) v).setTypeface(typeace);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            SketchwareUtil.showMessage(this.getApplicationContext(), "Error Loading Font");
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
