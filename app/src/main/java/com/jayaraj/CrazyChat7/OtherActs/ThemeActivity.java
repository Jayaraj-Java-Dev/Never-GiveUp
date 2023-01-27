package com.jayaraj.CrazyChat7.OtherActs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ThemeActivity extends AppCompatActivity {

    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private final HashMap<String, Object> map = new HashMap<>();
    private final String c1 = "";
    private final String c2 = "";

    private final ArrayList<HashMap<String, Object>> themes = new ArrayList<>();
    private HashMap<String, Object> TempMap = new HashMap<>();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear1;
    private ImageView imageviewBackGround,i1;
    private TextView textview1;
    private GridView gridview1;
    private ImageView demoi;
    private TextView demot1,demot2;
    private LinearLayout demol;

    private DatabaseReference dbSendProfile = this._firebase.getReference("rand");
    private final DatabaseReference db = this._firebase.getReference("Themes");
    private ChildEventListener _db_child_listener;
    Jay jay;
    private SharedPreferences SP,PHONE;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.activity_theme);
        this.initialize(_savedInstanceState);
        com.google.firebase.FirebaseApp.initializeApp(this);
        this.initializeLogic();
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear1 = this.findViewById(R.id.b_hevo_1);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.textview1 = this.findViewById(R.id.textview1);
        this.gridview1 = this.findViewById(R.id.gridview1);
        this.demoi = this.findViewById(R.id.demoi);
        this.demol = this.findViewById(R.id.demol);
        this.demot1 = this.findViewById(R.id.demot);
        this.demot2 = this.findViewById(R.id.demot2);
        this.i1 = this.findViewById(R.id.image_hevo1);
        this.SP = this.getSharedPreferences("MODE", Context.MODE_PRIVATE);
        this.PHONE = this.getSharedPreferences("PHONE", Context.MODE_PRIVATE);
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this.dbSendProfile = this._firebase.getReference("Profile/uid/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
        this._db_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                ThemeActivity.this.themes.add(_childValue);
                ThemeActivity.this.gridview1.setAdapter(new Gridview1Adapter(ThemeActivity.this.themes));
            }

            @Override
            public void onChildChanged(final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(final DataSnapshot _param1, final String _param2) {

            }

            @Override
            public void onChildRemoved(final DataSnapshot _param1) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(final DatabaseError _param1) {
                int _errorCode = _param1.getCode();
                String _errorMessage = _param1.getMessage();

            }
        };
        this.db.addChildEventListener(this._db_child_listener);
    }

    private void initializeLogic() {
        {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("light", "#4D4D4D");
            this.themes.add(_item);
        }

        this.themes.get(0).put("light", "#4D4D4D");
        this.themes.get(0).put("dark", "#212121");
        this.themes.get(0).put("text", "#FFFFFF");
        this.themes.get(0).put("g1", "#212121");
        this.themes.get(0).put("g2", "#212121");
        this.gridview1.setAdapter(new Gridview1Adapter(this.themes));
        this.textview1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.demot1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.demot2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);

        this.i1.setOnClickListener(v -> {
            this.finish();
        });

        this._Bg();
    }

    public void _SetBackground(View _view, double _radius, double _shadow, String _color, boolean _ripple) {
        if (_ripple) {
            final android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setColor(Color.parseColor(_color));
            gd.setCornerRadius((int)_radius);
            _view.setElevation((int)_shadow);
            final android.content.res.ColorStateList clrb = new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#9e9e9e")});
            final android.graphics.drawable.RippleDrawable ripdrb = new android.graphics.drawable.RippleDrawable(clrb , gd, null);
            _view.setClickable(true);
            _view.setBackground(ripdrb);
        }
        else {
            final android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setColor(Color.parseColor(_color));
            gd.setCornerRadius((int)_radius);
            _view.setBackground(gd);
            _view.setElevation((int)_shadow);
        }
    }


    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(final int a) { setCornerRadius(a);return this; } }.getIns((int)50));
    }

    public void _SetGradientView1(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(final int a) { setCornerRadius(a);return this; } }.getIns((int)0));
    }


    public void _g2(View _view, String _color_a, String _color_b) {
        final Window w = this.getWindow();

        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(_color_a));

        if (Build.VERSION.SDK_INT >= 21) {
            w.setNavigationBarColor(Color.parseColor(_color_b));
        }
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}));
    }

    public class Gridview1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Gridview1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @Override
        public int getCount() {
            return this._data.size();
        }

        @Override
        public HashMap<String, Object> getItem(final int _index) {
            return this._data.get(_index);
        }

        @Override
        public long getItemId(final int _index) {
            return _index;
        }

        @Override
        public View getView(int _position, final View _v, final ViewGroup _container) {
            final LayoutInflater _inflater = ThemeActivity.this.getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.themeitem, null);
            }

            LinearLayout linear2 = _view.findViewById(R.id.linear2);
            LinearLayout linear3 = _view.findViewById(R.id.linear3);
            LinearLayout linear4 = _view.findViewById(R.id.linear4);
            LinearLayout linear5 = _view.findViewById(R.id.linear5);
            LinearLayout linear6 = _view.findViewById(R.id.linear6);

            ThemeActivity.this._SetBackground(linear3, 100, 0, ThemeActivity.this.themes.get(_position).get("light").toString(), true);
            ThemeActivity.this._SetBackground(linear4, 100, 0, ThemeActivity.this.themes.get(_position).get("dark").toString(), true);
            ThemeActivity.this._SetBackground(linear6, 100, 0, ThemeActivity.this.themes.get(_position).get("text").toString(), true);
            ThemeActivity.this._SetGradientView(linear2, ThemeActivity.this.themes.get(_position).get("g1").toString(), ThemeActivity.this.themes.get(_position).get("g2").toString());
            linear2.setOnClickListener(_view1 -> {
                ThemeActivity.this.presses(_position);
            });
            linear3.setOnClickListener(v -> {
                ThemeActivity.this.presses(_position);
            });
            linear4.setOnClickListener(v -> {
                ThemeActivity.this.presses(_position);
            });
            linear6.setOnClickListener(v -> {
                ThemeActivity.this.presses(_position);
            });
            return _view;
        }
    }

    private void presses(final double _position) {
        this.SP.edit().putString("light", this.themes.get((int)_position).get("light").toString()).apply();
        this.SP.edit().putString("dark", this.themes.get((int)_position).get("dark").toString()).apply();
        this.SP.edit().putString("text", this.themes.get((int)_position).get("text").toString()).apply();
        this.SP.edit().putString("g1", this.themes.get((int)_position).get("g1").toString()).apply();
        this.SP.edit().putString("g2", this.themes.get((int)_position).get("g2").toString()).apply();
        this._Bg();
        this.TempMap = new HashMap<>();
        this.TempMap.put("UI", this.jay.getCurrentTheme());
        this.dbSendProfile.child("data").updateChildren(this.TempMap);
        this.TempMap.clear();
    }
    public void _Bg() {
        //linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        if (FileUtil.isFile(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"))) {
            this.imageviewBackGround.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"), 1024, 1024));
        } else {
//            imageviewBackGround.setImageResource(R.drawable.bgi);
        }
        this.i1.setColorFilter(Color.parseColor(this.jay.col(1, 1)));
        this.textview1.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._SetBackground(this.linear1, 0, 20, this.jay.col(0,3), false);
        this._SetGradientView1(this.linear1BG, this.jay.col(0,3), this.jay.col(0,4));
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(this.jay.col(0,3)));

        this.demoi.setColorFilter(Color.parseColor(this.jay.col(1, 1)));
        this.demot1.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this._SetBackground(this.demol, 20, 20, this.jay.col(0,1), true);
        this._SetBackground(this.demot2, 15, 20, this.jay.col(0,2), false);
        this.demot2.setTextColor(Color.parseColor(this.jay.col(1, 1)));



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
                _result.add((double)_arr.keyAt(_iIdx));
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
