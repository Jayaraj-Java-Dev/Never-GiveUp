package com.jayaraj.CrazyChat7.LoginActs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jayaraj.CrazyChat7.HomeActs.AddUserChatActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class OtpLoginActivity extends AppCompatActivity {

    private double length;
    private final String Vid = "";
    private boolean OTP;
    private final String phone = "";
    private final Timer _timer = new Timer();
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout mobilenumberbar;
    private LinearLayout linear7;
    private LinearLayout linear10;
    private LinearLayout linear4;
    private ImageView imageview2;
    private LinearLayout linear3;
    private LinearLayout linear5;
    private TextView heading;
    private TextView paragraph;
    private EditText entermobile;
    private LinearLayout ok;
    private ProgressBar progressbar2;
    private TextView textview15;
    private TextView textview13;
    private LinearLayout linear14;
    private LinearLayout linear13;
    private LinearLayout linear12;
    private LinearLayout linear11;
    private LinearLayout bt1;
    private LinearLayout bt2;
    private LinearLayout bt3;
    private TextView textview3;
    private TextView textview4;
    private TextView textview5;
    private LinearLayout bt4;
    private LinearLayout bt5;
    private LinearLayout bt6;
    private TextView textview6;
    private TextView textview7;
    private TextView textview8;
    private LinearLayout bt7;
    private LinearLayout bt8;
    private LinearLayout bt9;
    private TextView textview9;
    private TextView textview10;
    private TextView textview11;
    private LinearLayout tickbt;
    private LinearLayout bt0;
    private LinearLayout btdelete;
    private TextView textview14;
    private TextView textview12;
    private ImageView imageview1;

    private final Intent intent = new Intent();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks pauth;
    private PhoneAuthProvider.ForceResendingToken pauth_resendToken;
    private FirebaseAuth fauth;
    private OnCompleteListener<Void> fauth_updateEmailListener;
    private OnCompleteListener<Void> fauth_updatePasswordListener;
    private OnCompleteListener<Void> fauth_emailVerificationSentListener;
    private OnCompleteListener<Void> fauth_deleteUserListener;
    private OnCompleteListener<Void> fauth_updateProfileListener;
    private OnCompleteListener<AuthResult> fauth_phoneAuthListener;
    private OnCompleteListener<AuthResult> fauth_googleSignInListener;
    private OnCompleteListener<AuthResult> _fauth_create_user_listener;
    private OnCompleteListener<AuthResult> _fauth_sign_in_listener;
    private OnCompleteListener<Void> _fauth_reset_password_listener;
    private SharedPreferences welcome,asp;
    private SharedPreferences pdataSP;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;


    private Jay jay;


    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.otp_login);
        this.initialize(_savedInstanceState);
        com.google.firebase.FirebaseApp.initializeApp(this);
        this.initializeLogic();
    }
    private void initialize(final Bundle _savedInstanceState) {
        this.linear1 = this.findViewById(R.id.linear1);
        this.linear2 = this.findViewById(R.id.linear2);
        this.mobilenumberbar = this.findViewById(R.id.mobilenumberbar);
        this.linear7 = this.findViewById(R.id.linear7);
        this.linear10 = this.findViewById(R.id.linear10);
        this.linear4 = this.findViewById(R.id.linear4);
        this.imageview2 = this.findViewById(R.id.imageview2);
        this.linear3 = this.findViewById(R.id.linear3);
        this.linear5 = this.findViewById(R.id.linear5);
        this.heading = this.findViewById(R.id.heading);
        this.paragraph = this.findViewById(R.id.paragraph);
        this.entermobile = this.findViewById(R.id.entermobile);
        this.ok = this.findViewById(R.id.ok);
        this.progressbar2 = this.findViewById(R.id.progressbar2);
        this.textview15 = this.findViewById(R.id.textview15);
        this.textview13 = this.findViewById(R.id.textview13);
        this.linear14 = this.findViewById(R.id.linear14);
        this.linear13 = this.findViewById(R.id.linear13);
        this.linear12 = this.findViewById(R.id.linear12);
        this.linear11 = this.findViewById(R.id.linear11);
        this.bt1 = this.findViewById(R.id.bt1);
        this.bt2 = this.findViewById(R.id.bt2);
        this.bt3 = this.findViewById(R.id.bt3);
        this.textview3 = this.findViewById(R.id.textview3);
        this.textview4 = this.findViewById(R.id.textview4);
        this.textview5 = this.findViewById(R.id.textview5);
        this.bt4 = this.findViewById(R.id.bt4);
        this.bt5 = this.findViewById(R.id.bt5);
        this.bt6 = this.findViewById(R.id.bt6);
        this.textview6 = this.findViewById(R.id.textview6);
        this.textview7 = this.findViewById(R.id.textview7);
        this.textview8 = this.findViewById(R.id.textview8);
        this.bt7 = this.findViewById(R.id.bt7);
        this.bt8 = this.findViewById(R.id.bt8);
        this.bt9 = this.findViewById(R.id.bt9);
        this.textview9 = this.findViewById(R.id.textview9);
        this.textview10 = this.findViewById(R.id.textview10);
        this.textview11 = this.findViewById(R.id.textview11);
        this.tickbt = this.findViewById(R.id.tickbt);
        this.bt0 = this.findViewById(R.id.bt0);
        this.btdelete = this.findViewById(R.id.btdelete);
        this.textview14 = this.findViewById(R.id.textview14);
        this.textview12 = this.findViewById(R.id.textview12);
        this.imageview1 = this.findViewById(R.id.imageview1);
        this.fauth = FirebaseAuth.getInstance();
        this.welcome = this.getSharedPreferences("welcome", Context.MODE_PRIVATE);
        this.pdataSP = this.getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.welcome.edit().putString("us","").apply();
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));


        this.entermobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {

            }
        });
        this.textview15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                if (OtpLoginActivity.this.OTP) {
                    if (6 == OtpLoginActivity.this.entermobile.getText().toString().length()) {
                        OtpLoginActivity.this._OnVeryfy();
                    } else {
                        SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Enter OTP in 6 Digits");
                    }
                } else {
                    if (13 == OtpLoginActivity.this.entermobile.getText().toString().length()) {
                        OtpLoginActivity.this._OnClickGo();
                    } else {
                        SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Enter Your Mobile Number Correctly");
                    }
                }
            }
        });
        this.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("1"));
            }
        });
        this.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("2"));
            }
        });
        this.bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("3"));
            }
        });
        this.bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("4"));
            }
        });
        this.bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("5"));
            }
        });
        this.bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("6"));
            }
        });
        this.bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("7"));
            }
        });
        this.bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("8"));
            }
        });
        this.bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("9"));
            }
        });
        this.tickbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("+"));
            }
        });
        this.bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().concat("0"));
            }
        });
        this.btdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                OtpLoginActivity.this.length = OtpLoginActivity.this.entermobile.getText().toString().length();
                if (OtpLoginActivity.this.length > 0) {
                    OtpLoginActivity.this.entermobile.setText(OtpLoginActivity.this.entermobile.getText().toString().substring(0, (int) (OtpLoginActivity.this.length - 1)));
                } else {

                }
            }
        });

/*
        pauth = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential _credential) {
                FirebaseAuth.getInstance().signInWithCredential(_credential).addOnCompleteListener(fauth_phoneAuthListener);
                SketchwareUtil.showMessage(getApplicationContext(), "Signing In");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                final String _exception = e.getMessage();
                SketchwareUtil.showMessage(getApplicationContext(), _exception);
                if (entermobile.getFreezesText()) {
                    entermobile.setFreezesText(false);
                }
                entermobile.setText("+91");
                entermobile.setHint("Enter Your Mobile Number");
                entermobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                textview13.setText("Please Notice that you have to enter mobile number in international eg: +91");
            }

            @Override
            public void onCodeSent(@NonNull String _verificationId, PhoneAuthProvider.ForceResendingToken _token) {
                pauth_resendToken = _token;
                SketchwareUtil.showMessage(getApplicationContext(), "Code Sent");
                Vid = _verificationId;
                tickbt.setVisibility(View.GONE);
                progressbar2.setVisibility(View.GONE);
                textview15.setVisibility(View.VISIBLE);
                textview13.setText("Enter The 6 Digit Otp ");
                if (entermobile.getFreezesText()) {
                    entermobile.setFreezesText(false);
                }
                entermobile.setText("");
                entermobile.setHint("OTP");
                entermobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                OTP = true;
            }
        };
*/
        this.fauth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                boolean _success = task.isSuccessful();
                String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
                if (_success) {
                    OtpLoginActivity.this.welcome.edit().putString("wel", "true").apply();
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Login Success");
                    OtpLoginActivity.this.intent.setClass(OtpLoginActivity.this.getApplicationContext(), UserActivity.class);
                    OtpLoginActivity.this.startActivity(OtpLoginActivity.this.intent);
                    OtpLoginActivity.this.finish();
                } else {
                    OtpLoginActivity.this.progressbar2.setVisibility(View.GONE);
                    OtpLoginActivity.this.textview15.setVisibility(View.VISIBLE);
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Login Failed");
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), _errorMessage);
                }
            }
        };
    }
    private void initializeLogic() {
        this._OnCrt();
        this._font();
    }
    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
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
        this.heading.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.heading.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.paragraph.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.entermobile.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview3.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview4.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview5.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview6.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview7.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview8.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview9.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview10.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview11.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview12.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview13.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview14.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview15.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this._ui_nextgen_designs();
    }


    public void _Bg() {
        //linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        final Jay jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._SetGradientView(this.linear1,jay.col(0,3),jay.col(0,4));
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0,3)));

    }

    public void _Anime(String _Sp, View _Linear) {
        if ("".equals(this.asp.getString(_Sp, ""))) {
            this.asp.edit().putString(_Sp, "true").apply();
            final AutoTransition autoTransition = new AutoTransition();
            autoTransition.setDuration(200);
            TransitionManager.beginDelayedTransition((LinearLayout) _Linear, autoTransition);
            try {
                this._timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> asp.edit().putString(_Sp, "").apply());
                    }
                }, 350);
            } catch (final Exception e5) {
            }
        } else {

        }
    }
    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(final int a) { setCornerRadius(a);return this; } }.getIns((int)0));
    }

    public void _ui_nextgen_designs() {
        //linear1.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._Bg();
        this.textview13.setTextColor(Color.parseColor(this.jay.col(1,1)));
        this._SetBackground(this.ok, 35, 0, "#81C784", true);
        final LinearLayout[] bts = {this.bt0, this.bt1, this.bt2, this.bt3, this.bt4, this.bt5, this.bt6, this.bt7, this.bt8, this.bt9, this.btdelete, this.tickbt};
        final TextView[] tvs = {this.entermobile, this.textview3, this.textview4, this.textview5, this.textview6, this.textview7, this.textview8, this.textview9, this.textview10, this.textview11, this.textview12, this.textview14};
        for (int i = 0; i < bts.length; i++) {
            this._SetBackground(bts[i], 35, 0, this.jay.col(0,1), true);
            tvs[i].setTextColor(Color.parseColor(this.jay.col(1,1)));
        }
        this._SetBackground(this.mobilenumberbar, 40, 0, this.jay.col(0,1), false);
    }
    public void _OnCrt() {
        this.progressbar2.setVisibility(View.GONE);
        this.entermobile.setEnabled(false);
        this.entermobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        this.OTP = false;
    }

    public void _OnClickGo() {
        //changes checking
        this.entermobile.setFreezesText(true);
        this.progressbar2.setVisibility(View.VISIBLE);
        this.textview15.setVisibility(View.GONE);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(String.valueOf(this.entermobile.getText()))
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull final String verificationId, @NonNull final PhoneAuthProvider.ForceResendingToken token) {
                        SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Code Sent");
                        _Anime("otpreceive",linear1);
                        OtpLoginActivity.this.tickbt.setVisibility(View.GONE);
                        OtpLoginActivity.this.progressbar2.setVisibility(View.GONE);
                        OtpLoginActivity.this.textview15.setVisibility(View.VISIBLE);
                        OtpLoginActivity.this.textview13.setText("Enter The 6 Digit Otp ");
                        if (OtpLoginActivity.this.entermobile.getFreezesText()) {
                            OtpLoginActivity.this.entermobile.setFreezesText(false);
                        }
                        OtpLoginActivity.this.entermobile.setText("");
                        OtpLoginActivity.this.entermobile.setHint("OTP");
                        OtpLoginActivity.this.entermobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                        OtpLoginActivity.this.OTP = true;
                        OtpLoginActivity.this.mVerificationId = verificationId;
                        OtpLoginActivity.this.mResendToken = token;
                        System.out.println("code Sent");
                    }

                    @Override
                    public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        OtpLoginActivity.this.Signin(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(final FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                        }

                        String _exception = e.getMessage();
                        SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), _exception);
                        if (OtpLoginActivity.this.entermobile.getFreezesText()) {
                            OtpLoginActivity.this.entermobile.setFreezesText(false);
                        }
                        OtpLoginActivity.this.entermobile.setText("+91");
                        OtpLoginActivity.this.entermobile.setHint("Enter Your Mobile Number");
                        OtpLoginActivity.this.entermobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                        OtpLoginActivity.this.textview13.setText("Please Notice that you have to enter mobile number in international eg: +91");

                        OtpLoginActivity.this.progressbar2.setVisibility(View.GONE);
                        OtpLoginActivity.this.textview15.setVisibility(View.VISIBLE);
                        OtpLoginActivity.this.OTP = false;
                        OtpLoginActivity.this.pdataSP.edit().putString("PHONE", OtpLoginActivity.this.entermobile.getText().toString()).apply();

                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        this.pdataSP.edit().putString("PHONE", this.entermobile.getText().toString()).apply();
    }

    private void Signin(final PhoneAuthCredential phoneAuthCredential) {
        fauth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    System.out.println("signInWithCredential:success");

                    final FirebaseUser user = task.getResult().getUser();
                    // Update UI
                    OtpLoginActivity.this.welcome.edit().putString("wel", "true").apply();
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Login Success");
                    OtpLoginActivity.this.intent.setClass(OtpLoginActivity.this.getApplicationContext(), UserActivity.class);
                    OtpLoginActivity.this.startActivity(OtpLoginActivity.this.intent);
                    OtpLoginActivity.this.finish();
                } else {
                    // Sign in failed, display a message and update the UI
                    System.out.println("signInWithCredential:failure "+ task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    OtpLoginActivity.this.progressbar2.setVisibility(View.GONE);
                    OtpLoginActivity.this.textview15.setVisibility(View.VISIBLE);
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Login Failed");
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), String.valueOf(task.getException()));
                }
            }
        });

    }


    public void _OnVeryfy() {
        this.entermobile.setFreezesText(true);
        this.progressbar2.setVisibility(View.VISIBLE);
        this.textview15.setVisibility(View.GONE);
        final PhoneAuthCredential credential = PhoneAuthProvider.getCredential(this.mVerificationId, this.entermobile.getText().toString());
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                boolean _success = task.isSuccessful();
                String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
                if (_success) {
                    OtpLoginActivity.this.welcome.edit().putString("wel", "true").apply();
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Login Success");
                    OtpLoginActivity.this.intent.setClass(OtpLoginActivity.this.getApplicationContext(), UserActivity.class);
                    OtpLoginActivity.this.startActivity(OtpLoginActivity.this.intent);
                    OtpLoginActivity.this.finish();
                } else {
                    OtpLoginActivity.this.progressbar2.setVisibility(View.GONE);
                    OtpLoginActivity.this.textview15.setVisibility(View.VISIBLE);
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), "Login Failed");
                    SketchwareUtil.showMessage(OtpLoginActivity.this.getApplicationContext(), _errorMessage);
                }
            }
        });


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
