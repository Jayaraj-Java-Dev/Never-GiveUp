package com.jayaraj.CrazyChat7.UnusedActs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.ImageVidDispActs.ImgfullActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.getData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserprofileActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();

    private final String fontName = "";
    private final String typeace = "";
    private final double MAX = 0;
    private double it;
    private String tmp2 = "";
    private double tmpnum;
    private String tempstr = "";
    private final HashMap<String, Object> DataMapContact = new HashMap<>();
    private String tmp = "";
    private boolean animate;

    private final ArrayList<String> NetPhones = new ArrayList<>();
    private final ArrayList<String> phone = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> contacts = new ArrayList<>();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear1;
    private ImageView imageviewBackGround;
    private LinearLayout linear14;
    private LinearLayout linear13;
    private CircleImageView circleimageview1;
    private TextView textview3;
    private TextView textview7;
    private TextView textview10;
    private TextView textview8;

    private TextView tmsg1,tmsg2;
    private LinearLayout lmsg;


    private SharedPreferences ChatList;
    private TimerTask atm;
    private SharedPreferences asp;
    private SharedPreferences pdata,msg;
    private final Intent i = new Intent();


    private Jay jay;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.userprofile);
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
        this.linear1 = this.findViewById(R.id.linear1);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.linear14 = this.findViewById(R.id.linear14);
        this.linear13 = this.findViewById(R.id.linear13);
        this.circleimageview1 = this.findViewById(R.id.circleimageview1);
        this.textview3 = this.findViewById(R.id.textview3);
        this.textview7 = this.findViewById(R.id.textview7);
        this.textview10 = this.findViewById(R.id.textview10);
        this.textview8 = this.findViewById(R.id.textview8);
        this.lmsg = this.findViewById(R.id.totalmsg);
        this.tmsg1 = this.findViewById(R.id.totalmsg1);
        this.tmsg2 = this.findViewById(R.id.totalmsg2);

        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.pdata = this.getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.circleimageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                final Intent intentact = new Intent();
                intentact.setClass(UserprofileActivity.this.getApplicationContext(), ImgfullActivity.class);
                intentact.putExtra("uid", UserprofileActivity.this.getIntent().getStringExtra("uid"));
                UserprofileActivity.this.circleimageview1.setTransitionName("img");
                final android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(UserprofileActivity.this, UserprofileActivity.this.circleimageview1, "img");
                UserprofileActivity.this.startActivity(intentact, optionsCompat.toBundle());
            }
        });
    }

    private void initializeLogic() {
        this.circleimageview1.setTransitionName("img");
        this._Bg();
        this.textview3.setText(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("n"), ""));
        this.tmsg2.setText(this.msg.getString(this.getIntent().getStringExtra("uid"),"0"));
        Glide.with(this.getApplicationContext()).load(Uri.parse(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("i"), ""))).into(this.circleimageview1);
        this.textview7.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,2))));
        this.textview10.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,2))));
        this.textview8.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,2))));
        this.textview3.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,3))));
        this.lmsg.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,2))));
        this.textview3.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview7.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview8.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview10.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview7.setElevation(15);
        this.textview10.setElevation(15);
        this.textview8.setElevation(15);
        this.tmsg1.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.tmsg2.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.lmsg.setElevation(15);

        this.textview7.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textview10.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview8.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview3.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.tmsg1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.tmsg2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);

        this._getAllContacts();
        this.textview8.setText(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("p"), ""));
        if (this.DataMapContact.containsKey(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("p"), ""))) {
            this.textview10.setText(Objects.requireNonNull(this.DataMapContact.get(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("p"), ""))).toString());
        } else {
            this.textview7.setText("User Not in your Contact List");
            this.textview10.setText("Click Here to Add Contact");
            this.textview10.setOnClickListener(v -> {
                final String name = this.textview3.getText().toString() ;
                final String phone = this.textview8.getText().toString() ;
                final Intent contactIntent = new Intent(ContactsContract.Intents.Insert. ACTION ) ;
                contactIntent.setType(ContactsContract.RawContacts. CONTENT_TYPE ) ;
                contactIntent
                        .putExtra(ContactsContract.Intents.Insert. NAME , name)
                        .putExtra(ContactsContract.Intents.Insert. PHONE , phone) ;
                this.startActivityForResult(contactIntent , 1 ) ;
            });
        }

        this.tmp = this.getIntent().getStringExtra("uid");
        this.animate = false;
        new getData(this.tmp, this.getApplicationContext(), this.ChatList);
    }

    @Override
    protected void onActivityResult (final int requestCode , final int resultCode , final Intent intent) {
        super .onActivityResult(requestCode , resultCode , intent) ;
        if (requestCode == 1 ) {
            this._getAllContacts();
            this.textview8.setText(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("p"), ""));
            if (this.DataMapContact.containsKey(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("p"), ""))) {
                this.textview7.setText("Details In Contact");
                this.textview10.setText(Objects.requireNonNull(this.DataMapContact.get(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("p"), ""))).toString());
                this.textview10.setOnClickListener(null);
            } else {
                this.textview7.setText("User Not in your Contact List");
                this.textview10.setText("Click Here to Add Contact");
            }

        }
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
        this._SetGradientView(this.linear1,jay.col(0,3),jay.col(0,4));
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0,3)));

    }

    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(final int a) { setCornerRadius(a);return this; } }.getIns((int)0));
    }


    public void _Animate(@NonNull LinearLayout _layout, double _DURATION) {
        int MAX = _layout.getChildCount();
        for (int ITERATE = 0; ITERATE < MAX; ITERATE++) {
            _layout.getChildAt(ITERATE).setAlpha((float) (0));
            _layout.getChildAt(ITERATE).setTranslationY((float) (1000));
        }
        this.atm = new TimerTask() {
            @Override
            public void run() {
                UserprofileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (UserprofileActivity.this.it == MAX) {
                            UserprofileActivity.this.atm.cancel();
                        } else {
                            _layout.getChildAt((int) UserprofileActivity.this.it).animate().translationY(0).alpha(1.0f).setListener(null);
                        }
                        UserprofileActivity.this.it++;
                    }
                });
            }
        };
        this._timer.scheduleAtFixedRate(this.atm, 500, (int) (_DURATION));
    }


    public void _getAllContacts() {
        final android.database.Cursor c = this.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{BaseColumns._ID, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (c.moveToNext()) {
            this._addContact(c.getString(1), c.getString(2));
        }
    }


    public void _addContact(String _name, String _phone) {
        if (9 < _phone.length()) {
            this.tmp2 = this._Phone(_phone);
            if (this.phone.contains(this.tmp2)) {

            } else {
                if (this.tmp2.equals(this.pdata.getString("PHONE", ""))) {

                } else {
                    this.DataMapContact.put(this.tmp2, _name);
                    {
                        final HashMap<String, Object> _item = new HashMap<>();
                        _item.put("name", _name.trim());
                        this.contacts.add(_item);
                    }

                    this.phone.add(this.tmp2);
                }
            }
        }
    }


    public String _Phone(String _Phone) {
        this.tmpnum = 0;
        this.tempstr = "";
        for (int _repeat11 = 0; _repeat11 < _Phone.length(); _repeat11++) {
            if ("+0123456789".contains(_Phone.substring((int) (this.tmpnum), (int) (this.tmpnum + 1)))) {
                this.tempstr = this.tempstr.concat(_Phone.substring((int) (this.tmpnum), (int) (this.tmpnum + 1)));
            } else {

            }
            this.tmpnum++;
        }
        if (this.tempstr.contains("+")) {
            return (this.tempstr);
        } else {
            return ("+91".concat(this.tempstr));
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
