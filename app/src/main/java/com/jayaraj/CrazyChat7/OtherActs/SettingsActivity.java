package com.jayaraj.CrazyChat7.OtherActs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.jayaraj.CrazyChat7.J.DeveloperActivity;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.ImageVidDispActs.ImgfullActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.LoginActs.UserActivity;
import com.jayaraj.CrazyChat7.MainActivity;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.ServicesActs.DbmsgActivity;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends Activity {


    private LinearLayout b_hevo_1;
    private ImageView image_hevo1;

    private LinearLayout linear1;
    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear2;
    private ImageView imageviewBackGround;
    private LinearLayout b_hevo_4;
    private TextView textName;
    private TextView textNumber;
    private CircleImageView circleimageview1;
    private LinearLayout llogout;
    private ImageView image_hevo10;
    private TextView text_hevo8;
    private ImageView image_hevo_bt6;
    private LinearLayout lhelp;
    private ImageView image_hevo7;
    private TextView text_hevo5;
    private ImageView image_hevo_bt3;
    private LinearLayout ltheme,editprof;
    private ImageView image_hevo8,image_hevo82;
    private TextView text_hevo6,text_hevo62;
    private ImageView image_hevo_bt4,image_hevo_bt42;
    private LinearLayout lfriend;
    private ImageView image_hevo9;
    private TextView text_hevo7;
    private ImageView image_hevo_bt5;
    private TextView textview1;

    private SharedPreferences pdataSP;
    private final Intent intent = new Intent();
    private Jay jay;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.settings_activity);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this.b_hevo_1 = this.findViewById(R.id.b_hevo_1);
        this.image_hevo1 = this.findViewById(R.id.image_hevo1);
        this.linear1 = this.findViewById(R.id.linear1);
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear2 = this.findViewById(R.id.linear2);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.b_hevo_4 = this.findViewById(R.id.b_hevo_4);
        this.editprof = this.findViewById(R.id.editprof);
        this.textName = this.findViewById(R.id.textName);
        this.textNumber = this.findViewById(R.id.textNumber);
        this.circleimageview1 = this.findViewById(R.id.circleimageview1);
        this.llogout = this.findViewById(R.id.llogout);
        this.image_hevo10 = this.findViewById(R.id.image_hevo10);
        this.text_hevo8 = this.findViewById(R.id.text_hevo8);
        this.image_hevo_bt6 = this.findViewById(R.id.image_hevo_bt6);
        this.lhelp = this.findViewById(R.id.lhelp);
        this.image_hevo7 = this.findViewById(R.id.image_hevo7);
        this.text_hevo5 = this.findViewById(R.id.text_hevo5);
        this.image_hevo_bt3 = this.findViewById(R.id.image_hevo_bt3);
        this.ltheme = this.findViewById(R.id.ltheme);
        this.image_hevo8 = this.findViewById(R.id.image_hevo8);
        this.text_hevo6 = this.findViewById(R.id.text_hevo6);
        this.image_hevo_bt4 = this.findViewById(R.id.image_hevo_bt4);
        this.image_hevo82 = this.findViewById(R.id.image_hevo82);
        this.text_hevo62 = this.findViewById(R.id.text_hevo62);
        this.image_hevo_bt42 = this.findViewById(R.id.image_hevo_bt42);
        this.lfriend = this.findViewById(R.id.lfriend);
        this.image_hevo9 = this.findViewById(R.id.image_hevo9);
        this.text_hevo7 = this.findViewById(R.id.text_hevo7);
        this.image_hevo_bt5 = this.findViewById(R.id.image_hevo_bt5);
        this.textview1 = this.findViewById(R.id.textview1);

        this.pdataSP = this.getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.ONCRT();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this._Bg();
    }

    private void ONCRT() {
        this._Bg();
        this.textNumber.setText(this.pdataSP.getString("PHONE", "Something Went Wrong"));
        this.textName.setText(this.pdataSP.getString("NAME", "No Name"));

        if(!this.pdataSP.getString("IMG", "").equals("")) {
            Glide.with(this.getApplicationContext()).load(Uri.parse(this.pdataSP.getString("IMG", ""))).into(this.circleimageview1);
        }

        this.image_hevo1.setOnClickListener(v -> {
            this.finish();
        });

        this.lhelp.setOnClickListener(v -> {
            final Intent mw = new Intent();
            mw.setClass(this.getApplicationContext(),DeveloperActivity.class);
            this.startActivity(mw);
        });

        this.findViewById(R.id.privacypol).setOnClickListener(v -> {
            final AlertDialog.Builder log = new AlertDialog.Builder(this);
            log.setIcon(R.drawable.app_icon);
            log.setTitle("Terms Of Privacy Policy");
            log.setMessage("You are already agreed our terms of privacy policy\nYou can deny it by clicking DENY & LOGOUT.\n Without agreeing the terms you can't use CRAZY CHAT app");
            log.setPositiveButton("Close", (dialogInterface, i) -> {
            });
            log.setNegativeButton("View", (dialogInterface, i) -> {
                final Intent mw = new Intent(Intent.ACTION_VIEW);
                mw.setData(Uri.parse(this.jay.PrivacyPolicy));
                this.startActivity(mw);
            });
            log.setNeutralButton("Deny & Log Out", (dialogInterface, i) -> {
                this.logout();
                final Intent li = new Intent();
                li.setClass(this.getApplicationContext(), MainActivity.class);
                this.startActivity(li);
                FirebaseAuth.getInstance().signOut();
                this.finishAffinity();
            });
            log.show();
        });
        this.llogout.setOnClickListener(v -> {
            final AlertDialog.Builder log = new AlertDialog.Builder(this);
            log.setIcon(R.drawable.app_icon);
            log.setTitle("Logout?");
            log.setMessage("Do You Want to Log Out?");
            log.setNeutralButton("No", (dialogInterface, i) -> {
            });
            log.setPositiveButton("Log Out", (dialogInterface, i) -> {
                this.logout();
                final Intent li = new Intent();
                li.setClass(this.getApplicationContext(),MainActivity.class);
                this.startActivity(li);
                FirebaseAuth.getInstance().signOut();
                this.finishAffinity();
            });
            log.show();
        });

        this.circleimageview1.setOnClickListener(v -> {
            final Intent intentact = new Intent();
            intentact.setClass(this.getApplicationContext(), ImgfullActivity.class);
            intentact.putExtra("uid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            this.circleimageview1.setTransitionName("img");
            final android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(this, this.circleimageview1, "img");
            this.startActivity(intentact, optionsCompat.toBundle());
        });

        this.lfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                String apk = "";
                final String uri = "com.jayaraj.CrazyChat7";
                try {
                    final android.content.pm.PackageInfo pi = SettingsActivity.this.getPackageManager().getPackageInfo(uri, android.content.pm.PackageManager.GET_ACTIVITIES);
                    apk = pi.applicationInfo.publicSourceDir;
                } catch (final Exception e) {
                    Toast.makeText(SettingsActivity.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                final Intent iten = new Intent(Intent.ACTION_SEND);
                iten.setType("apk/*");
                iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(apk)));

                SettingsActivity.this.startActivity(Intent.createChooser(iten, "Share APK with"));
            }
        });

    }
    void logout(){
        this.stopService(new Intent(this.getApplicationContext(), DbmsgActivity.class));
    }
    public void UI() {

        this._SetBackground(this.b_hevo_1, 0, 20, this.jay.col(0,3), false);
//        _SetBackground(b_hevo_4, 35, 20, jay.col(0,1), false);
        this._SetBackground(this.lhelp, 20, 20, this.jay.col(0,1), true);
        this._SetBackground(this.llogout, 20, 20, this.jay.col(0,1), true);
        this._SetBackground(this.ltheme, 20, 20, this.jay.col(0,1), true);
        this._SetBackground(this.editprof, 20, 20, this.jay.col(0,1), true);
        this._SetBackground(this.lfriend, 20, 20, this.jay.col(0,1), true);
        this._SetBackground(this.findViewById(R.id.privacypol), 20, 20, this.jay.col(0,1), true);
        final TextView[] tvs = {this.textview1, this.textName, this.textNumber, this.text_hevo5, this.text_hevo6, this.text_hevo7, this.text_hevo8, this.text_hevo62, this.findViewById(R.id.privacypoltext)};
        for (final TextView tv : tvs) {
            tv.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            tv.setTextColor(Color.parseColor(this.jay.col(1,1)));
        }
        final ImageView[] imgs = {this.image_hevo1, this.image_hevo7, this.image_hevo8, this.image_hevo9, this.image_hevo10, this.image_hevo_bt3, this.image_hevo_bt4, this.image_hevo_bt5, this.image_hevo_bt6, this.image_hevo82, this.image_hevo_bt42, this.findViewById(R.id.privacypolimg), this.findViewById(R.id.privacypolimg2)};

        for (final ImageView img : imgs) {
            img.setColorFilter(Color.parseColor(this.jay.col(1, 1)));
        }
        this.ltheme.setOnClickListener(v -> {
            this.intent.setClass(this.getApplicationContext(), ThemeActivity.class);
            this.startActivity(this.intent);
        });
        this.editprof.setOnClickListener(v -> {
            final Intent inte = new Intent();
            inte.setClass(this.getApplicationContext(), UserActivity.class);
            this.startActivity(inte);
            this.finish();
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
        this._SetBackground(this.b_hevo_1, 0, 20, jay.col(0,3), false);
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._SetGradientView(this.linear1BG,jay.col(0,3),jay.col(0,4));
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0,3)));
        this.UI();
    }

    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(final int a) { setCornerRadius(a);return this; } }.getIns((int)0));
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
}