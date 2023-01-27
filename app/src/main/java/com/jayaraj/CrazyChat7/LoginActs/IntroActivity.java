package com.jayaraj.CrazyChat7.LoginActs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


public class IntroActivity extends Activity {

    private final ArrayList<HashMap<String, Object>> ViewPagerListMap = new ArrayList<>();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear2;
    private ImageView imageviewBackGround;
    private ViewPager viewpager1;
    private Button button1;


    private Jay jay;


    private final Intent i1 = new Intent();

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.intro);
        this.initialize(_savedInstanceState);
        com.google.firebase.FirebaseApp.initializeApp(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
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
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear2 = this.findViewById(R.id.linear2);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.viewpager1 = this.findViewById(R.id.viewpager1);
        this.button1 = this.findViewById(R.id.button1);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));

        (this.findViewById(R.id.privacylin)).setVisibility(View.GONE);
        this.viewpager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int _position, final float _positionOffset, final int _positionOffsetPixels) {
                if (2 == _position) {
                    IntroActivity.this.button1.setText("Start");
                    IntroActivity.this._Anime(IntroActivity.this.linear2);
                    (IntroActivity.this.findViewById(R.id.privacylin)).setVisibility(View.VISIBLE);
                } else {
                    IntroActivity.this.button1.setText("Next");
                    (IntroActivity.this.findViewById(R.id.privacylin)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(final int _position) {

            }

            @Override
            public void onPageScrollStateChanged(final int _scrollState) {

            }
        });

        this.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                if (2 == IntroActivity.this.viewpager1.getCurrentItem()) {
                    IntroActivity.this.i1.setClass(IntroActivity.this.getApplicationContext(), OtpLoginActivity.class);
                    IntroActivity.this.startActivity(IntroActivity.this.i1);
                    IntroActivity.this.finish();
                } else {
                    IntroActivity.this.viewpager1.setCurrentItem(IntroActivity.this.viewpager1.getCurrentItem() + 1);
                }
            }
        });
    }

    private void initializeLogic() {
        this._Bg();
        this._font();
        this._OnCrt();
    }

    public void _Anime(View _Linear) {
        final AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(200);
        TransitionManager.beginDelayedTransition((LinearLayout) _Linear, autoTransition);
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    public void _OnCrt() {
        {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("v", "Welcome To Crazy Chat");
            this.ViewPagerListMap.add(_item);
        }

        {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("v", "End to End Encrypted Chat");
            this.ViewPagerListMap.add(_item);
        }

        {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("v", "Terms of Privacy Policy");
            this.ViewPagerListMap.add(_item);
        }

        this.viewpager1.setAdapter(new Viewpager1Adapter(this.ViewPagerListMap));
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
        this.button1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this._SetBackground(this.button1, 40, 8, this.jay.col(0, 1), true);
        this.button1.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        ((TextView) this.findViewById(R.id.pt1)).setTextColor(Color.parseColor(this.jay.col(1, 1)));
        ((TextView) this.findViewById(R.id.pt2)).setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.findViewById(R.id.pt3).setOnClickListener(v -> {
            final Intent mw = new Intent(Intent.ACTION_VIEW);
            mw.setData(Uri.parse(this.jay.PrivacyPolicy));
            this.startActivity(mw);
        });
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

    public class Viewpager1Adapter extends PagerAdapter {
        Context _context;
        ArrayList<HashMap<String, Object>> _data;

        public Viewpager1Adapter(final Context _ctx, final ArrayList<HashMap<String, Object>> _arr) {
            this._context = _ctx;
            this._data = _arr;
        }

        public Viewpager1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._context = IntroActivity.this.getApplicationContext();
            this._data = _arr;
        }

        @Override
        public int getCount() {
            return this._data.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull final View _view, @NonNull final Object _object) {
            return _view == _object;
        }

        @Override
        public void destroyItem(@NonNull final ViewGroup _container, final int _position, @NonNull final Object _object) {
            _container.removeView((View) _object);
        }

        @Override
        public int getItemPosition(@NonNull final Object _object) {
            return super.getItemPosition(_object);
        }

        @Override
        public CharSequence getPageTitle(final int pos) {
            // use the activitiy event (onTabLayoutNewTabAdded) in order to use this method
            return "page " + pos;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup _container, int _position) {
            final View _view = LayoutInflater.from(this._context).inflate(R.layout.introcus, _container, false);

            ImageView imageview1 = _view.findViewById(R.id.imageview1);
            TextView textview1 = _view.findViewById(R.id.textview1);

            textview1.setText(Objects.requireNonNull(IntroActivity.this.ViewPagerListMap.get(_position).get("v")).toString());
            textview1.setTypeface(Typeface.createFromAsset(IntroActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            switch (_position) {
                case ((int) 0): {
                    imageview1.setImageResource(R.drawable.logo_trans);
                    break;
                }
                case ((int) 1): {
                    imageview1.setImageResource(R.drawable.im2);
                    break;
                }
                case ((int) 2): {
                    imageview1.setImageResource(R.drawable.privacy);
                    break;
                }
            }
            IntroActivity.this._SetBackground(imageview1, 35, 0, IntroActivity.this.jay.col(0, 1), true);
            textview1.setTextColor(Color.parseColor(IntroActivity.this.jay.col(1, 1)));
            _container.addView(_view);
            return _view;
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
