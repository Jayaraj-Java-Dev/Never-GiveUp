package com.jayaraj.CrazyChat7.OtherActs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jayaraj.CrazyChat7.ImageVidDispActs.ImgfullActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.MainActivity;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;
import com.jayaraj.CrazyChat7.J.getData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ChatHomeProfileActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();

    private final String fontName = "";
    private final String typeace = "";
    private final double MAX = 0;
    private final double it = 0;
    private String tmp2 = "";
    private double tmpnum;
    private String tempstr = "";
    private final HashMap<String, Object> DataMapContact = new HashMap<>();
    private String tmp = "";
    private boolean animate;

    private final ArrayList<String> NetPhones = new ArrayList<>();
    private final ArrayList<String> phone = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> contacts = new ArrayList<>();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final DatabaseReference dbSend = this._firebase.getReference("SenderUidRand");

    private double height;
    private double width;
    private double sY;
    private double isY;
    private double eY;
    private double ieY;
    private double n;
    private double val;
    private final double h = 0;
    private final double radius = 0;
    private double MaxWidth;
    private double MaxHeight;
    private final ObjectAnimator animation_2 = new ObjectAnimator();

    private double last;

    private TextView textview3;
    private TextView textview7;
    private TextView textview10;
    private TextView textview8,textapply;

    private TextView tmsg1,tmsg2,thmtext;
    private LinearLayout lmsg,theme,thm1,thm2,thm3,thmback;


    private SharedPreferences ChatList;
    private TimerTask atm;
    private SharedPreferences asp;
    private SharedPreferences pdata,msg;
    private final Intent i = new Intent();


    private Jay jay;


    private RelativeLayout BG;
    private LinearLayout linear1;
    private LinearLayout img;
    private LinearLayout linear3;
    private ImageView circleimageview1;
    private LinearLayout linear5;
    private LinearLayout linear6;
    private LinearLayout trans;

    private DatabaseReference dbthemeup = this._firebase.getReference("rand");
    private TimerTask at;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.activity_chat_home_profile);
        this.initialize(_savedInstanceState);
        this.initializeLogic();
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.textview7 = this.findViewById(R.id.textview7);
        this.textview10 = this.findViewById(R.id.textview10);
        this.textview8 = this.findViewById(R.id.textview8);
        this.lmsg = this.findViewById(R.id.totalmsg);
        this.tmsg1 = this.findViewById(R.id.totalmsg1);
        this.tmsg2 = this.findViewById(R.id.totalmsg2);
        this.BG = this.findViewById(R.id.BG);
        this.linear1 = this.findViewById(R.id.backl);
        this.img = this.findViewById(R.id.img);
        this.linear3 = this.findViewById(R.id.linear3);
        this.textview3 = this.findViewById(R.id.textview3);
        this.circleimageview1 = this.findViewById(R.id.circleimageview1);
        this.linear5 = this.findViewById(R.id.linear5);
        this.linear6 = this.findViewById(R.id.linear6);
        this.trans = this.findViewById(R.id.trans);
        this.theme = this.findViewById(R.id.theme);
        this.thm1 = this.findViewById(R.id.thm1);
        this.thm2 = this.findViewById(R.id.thm2);
        this.thm3 = this.findViewById(R.id.thm3);
        this.thmback = this.findViewById(R.id.thmback);
        this.thmtext = this.findViewById(R.id.texttheme);
        this.textapply = this.findViewById(R.id.textapply);

        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.pdata = this.getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this.dbthemeup = this._firebase.getReference("Profile/uid/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));

        this.circleimageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                final Intent intentact = new Intent();
                intentact.setClass(ChatHomeProfileActivity.this.getApplicationContext(), ImgfullActivity.class);
                intentact.putExtra("uid", ChatHomeProfileActivity.this.getIntent().getStringExtra("uid"));
                ChatHomeProfileActivity.this.circleimageview1.setTransitionName("img");
                final android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(ChatHomeProfileActivity.this, ChatHomeProfileActivity.this.circleimageview1, "img");
                ChatHomeProfileActivity.this.startActivity(intentact, optionsCompat.toBundle());
            }
        });
    }

    private void initializeLogic() {

        this.height = 50;
        this.width = 50;
        this.MaxWidth = SketchwareUtil.getDisplayWidthPixels(this.getApplicationContext());
        this.MaxHeight = SketchwareUtil.getDisplayHeightPixels(this.getApplicationContext()) / 3;
        this._timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ChatHomeProfileActivity.this.runOnUiThread(() -> {
                    ChatHomeProfileActivity.this._pad();
                    ChatHomeProfileActivity.this._AnimateTranslationY(ChatHomeProfileActivity.this.img, 1000, ChatHomeProfileActivity.this.MaxHeight / 2);
                    ChatHomeProfileActivity.this._AnimateXY(ChatHomeProfileActivity.this.linear3, 1000, ChatHomeProfileActivity.this.height + ((ChatHomeProfileActivity.this.MaxHeight / 2) / 3), ChatHomeProfileActivity.this.width + (((ChatHomeProfileActivity.this.MaxWidth - 180) / ChatHomeProfileActivity.this.MaxHeight) * ((ChatHomeProfileActivity.this.MaxHeight / 2) / 3)));
                    ChatHomeProfileActivity.this._AnimateXY(ChatHomeProfileActivity.this.trans, 1000, ChatHomeProfileActivity.this.height + ((ChatHomeProfileActivity.this.MaxHeight / 2) / 3), ChatHomeProfileActivity.this.width + (((ChatHomeProfileActivity.this.MaxWidth - 180) / ChatHomeProfileActivity.this.MaxHeight) * ((ChatHomeProfileActivity.this.MaxHeight / 2) / 3)));
                    ChatHomeProfileActivity.this.last = ChatHomeProfileActivity.this.width + (((ChatHomeProfileActivity.this.MaxWidth - 180) / ChatHomeProfileActivity.this.MaxHeight) * ((ChatHomeProfileActivity.this.MaxHeight / 2) / 3));
                });
            }
        }, 500);
        this._addCardView(this.circleimageview1, 5, 65, 5, 5, true, "#FFFFFF");

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
        }.getIns((int) 40, Color.parseColor(this.jay.col(0,3))));
        this.theme.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,2))));
        this.lmsg.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,2))));
        this.textapply.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(this.jay.col(0,3))));
        this.textview3.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview7.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview8.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview10.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.thmtext.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textapply.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.textview7.setElevation(15);
        this.textview10.setElevation(15);
        this.textview8.setElevation(15);
        this.tmsg1.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.tmsg2.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this.lmsg.setElevation(15);
        this.theme.setElevation(15);

        this.textview7.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textview10.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview8.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview3.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.thmtext.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.tmsg1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.tmsg2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textapply.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);

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
        this.Theme();
    }

    public void _Bg() {

        final Jay jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this._SetGradientView(this.linear1,jay.col(0,4),jay.col(0,3),0);
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0,4)));
    }

    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b, int rad) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(final int a) { setCornerRadius(a);return this; } }.getIns((int)rad));
    }

    private void Theme() {
        final String[] cols = this.jay.parseCol(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("ui"), this.jay.getdefaultTheme()));
        this._SetBackground(this.thm2, 100, 0, cols[0], false);
        this._SetBackground(this.thm1, 100, 0, cols[1], false);
        this._SetBackground(this.thm3, 100, 0, cols[2], false);
        this._SetGradientView(this.thmback, cols[3], cols[4] ,40);
        this.textapply.setOnClickListener(v -> {
            this.jay.setTheme(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("ui"), this.jay.getdefaultTheme()));
            final Intent i = new Intent();
            i.setClass(this, MainActivity.class);
            this.startActivity(i);
            final HashMap<String,Object> TempMap = new HashMap<>();
            TempMap.put("UI", this.jay.getCurrentTheme());
            this.dbthemeup.child("data").updateChildren(TempMap);
            TempMap.clear();
            this.finishAffinity();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void _pad() {
        this.img.setOnTouchListener((_view, _motionEvent) -> {
            if (_motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                this.sY = _motionEvent.getY();
                this.isY = this.img.getTranslationY();
            }
            if (_motionEvent.getAction()==MotionEvent.ACTION_UP) {
                this.eY = _motionEvent.getY();
                this.ieY = this.img.getTranslationY();
                if (this.isY > this.ieY) {
                    this.n = 1;
                    this._AnimateTranslationY(this.img, 500,0);
                    this._AnimateXY(this.linear3, 500, this.height, this.width);
                    this._AnimateXY(this.trans, 500, this.height, this.width);
                }
                else {
                    this.n = 0;
                    this._AnimateTranslationY(this.img, 500, this.MaxHeight);
                    this._AnimateXY(this.linear3, 500, this.height + (this.MaxHeight / 3), this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.MaxHeight / 3)));
                    this._AnimateXY(this.trans, 500, this.height + (this.MaxHeight / 3), 0);
                    this.last = (this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.MaxHeight / 3))) / 2;
                    this.last = this.last + (this.width / 2);
                }
            }
            if (_motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                this.eY = _motionEvent.getY();
                if (this.sY > this.eY) {
                    this.val = this.img.getTranslationY() + (0 - (this.sY - this.eY));
                    this.img.setTranslationY((float)(this.val));
                }
                else {
                    this.val = this.img.getTranslationY() + (this.eY - this.sY);
                    this.img.setTranslationY((float)(this.val));
                }
                if ((this.img.getTranslationY() < this.MaxHeight) && (this.img.getTranslationY() > -1)) {
                    if (this.img.getTranslationY() < (this.MaxHeight / 2)) {
                        this._sethw(this.trans, this.height + (this.img.getTranslationY() / 3), this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.img.getTranslationY() / 3)));
                        this.last = this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.img.getTranslationY() / 3));
                    }
                    else {
                        this._sethw(this.trans, this.height + (this.img.getTranslationY() / 3), (this.last - ((this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.img.getTranslationY() / 3))) - this.last)) - (((this.img.getTranslationY() - (this.MaxHeight / 2)) / (this.MaxHeight / 2)) * 50));
                    }
                    this._sethw(this.linear3, this.height + (this.img.getTranslationY() / 3), this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.img.getTranslationY() / 3)));
                }
                else {
                    if (this.img.getTranslationY() < 100) {
                        this.img.setTranslationY((float)(0));
                        this._sethw(this.linear3, this.height, this.width);
                    }
                    else {
                        this.img.setTranslationY((float)(this.MaxHeight));
                        this._sethw(this.linear3, this.height + (this.MaxHeight / 3), this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.MaxHeight / 3)));
                        this._sethw(this.trans, this.height + (this.MaxHeight / 3), (this.last - ((this.width + (((this.MaxWidth - 180) / this.MaxHeight) * (this.img.getTranslationY() / 3))) - this.last)) - this.width);
                    }
                }
            }
            return true;
        });
    }
    public void _AnimateTranslationY(View _view, int Duration, double _Y) {
        this.animation_2.setTarget(_view);
        this.animation_2.setPropertyName("translationY");
        this.animation_2.setFloatValues((float)(_Y));
        this.animation_2.setDuration(Duration);
        this.animation_2.start();
    }


    public void _AnimateWidth(View _view, double _duration, double _from, double _to) {
        final ValueAnimator anim = ValueAnimator.ofInt((int)_from,(int) _to);
        anim.addUpdateListener(valueAnimator -> {
            final int val = (Integer) valueAnimator.getAnimatedValue();
            final ViewGroup.LayoutParams layoutParams = _view.getLayoutParams();
            layoutParams.width = val; _view.setLayoutParams(layoutParams);
        });
        anim.setDuration((long)_duration);
        anim.start();
    }


    public void _AnimateHeight(View _view, double _duration, double _from, double _to) {
        final ValueAnimator anim2 = ValueAnimator.ofInt((int)_from,(int) _to);
        anim2.addUpdateListener(valueAnimator -> {
            final int val = (Integer) valueAnimator.getAnimatedValue();
            final ViewGroup.LayoutParams layoutParams = _view.getLayoutParams();
            layoutParams.height = val; _view.setLayoutParams(layoutParams);
        });
        anim2.setDuration((long)_duration);
        anim2.start();
    }


    public void _AnimateXY(View _view, double _duration, double _height, double _width) {
        this._AnimateWidth(_view, _duration, this._getw(_view), SketchwareUtil.getDip(this.getApplicationContext(), (int)(_width)));
        this._AnimateHeight(_view, _duration, this._geth(_view), SketchwareUtil.getDip(this.getApplicationContext(), (int)(_height)));
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



    public void _sethw(View _view, double _h, double _w) {
        final ViewGroup.LayoutParams params = _view.getLayoutParams();
        params.height = (int)  SketchwareUtil.getDip(this.getApplicationContext(), (int)_h);
        params.width = (int)  SketchwareUtil.getDip(this.getApplicationContext(),(int) _w);
        _view.setLayoutParams(params);
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

    public void _addCardView(View _layoutView, double _margins, double _cornerRadius, double _cardElevation, double _cardMaxElevation, boolean _preventCornerOverlap, String _backgroundColor) {
        final androidx.cardview.widget.CardView cv = new androidx.cardview.widget.CardView(this);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final int m = (int)_margins;
        lp.setMargins(m,m,m,m);
        cv.setLayoutParams(lp);
        final int c = Color.parseColor(_backgroundColor);
        cv.setCardBackgroundColor(c);
        cv.setRadius((float)_cornerRadius);
        cv.setCardElevation((float)_cardElevation);
        cv.setMaxCardElevation((float)_cardMaxElevation);
        cv.setPreventCornerOverlap(_preventCornerOverlap);
        if(_layoutView.getParent() instanceof LinearLayout){
            final ViewGroup vg = ((ViewGroup)_layoutView.getParent());
            vg.removeView(_layoutView);
            vg.removeAllViews();
            vg.addView(cv);
            cv.addView(_layoutView);
        }else{

        }
    }


    public double _getw(View _view) {
        return (_view.getLayoutParams()).width;
    }


    public double _geth(View _view) {
        return (_view.getLayoutParams()).height;
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
