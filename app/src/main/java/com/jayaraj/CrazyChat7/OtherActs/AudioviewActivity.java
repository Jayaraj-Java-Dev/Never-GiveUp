package com.jayaraj.CrazyChat7.OtherActs;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class AudioviewActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();
    private final FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

    private String CrazyChatPath = "";
    private String Fpath = "";
    private final double p = 0;
    private final double n = 0;
    private String fontName = "";
    private final String typeace = "";

    private SharedPreferences ChatList;

    private LinearLayout linear1;
    private LinearLayout linear4;
    private LinearLayout linear6;
    private LinearLayout linear3;
    private TextView textview4;
    private LinearLayout linear10;
    private LinearLayout linear7;
    private LinearLayout linear9;
    private SeekBar seekbar1;
    private TextView textview1;
    private TextView textview2;
    private TextView textview3;
    private TextView textview5;
    private TextView textview6;
    private ImageView imageview1;

    private SharedPreferences msg;
    private SharedPreferences PHONE;
    private MediaPlayer playmediaListview;
    private SharedPreferences Aplay;
    private final StorageReference Fstore = this._firebase_storage.getReference("AllSent/ImagesVideos/");
    private OnCompleteListener<Uri> _Fstore_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _Fstore_download_success_listener;
    private OnSuccessListener _Fstore_delete_success_listener;
    private OnProgressListener _Fstore_upload_progress_listener;
    private OnProgressListener _Fstore_download_progress_listener;
    private OnFailureListener _Fstore_failure_listener;
    private FirebaseAuth FAuth;
    private OnCompleteListener<Void> FAuth_updatePasswordListener;
    private OnCompleteListener<Void> FAuth_emailVerificationSentListener;
    private OnCompleteListener<Void> FAuth_deleteUserListener;
    private OnCompleteListener<Void> FAuth_updateProfileListener;
    private OnCompleteListener<AuthResult> FAuth_phoneAuthListener;
    private OnCompleteListener<AuthResult> FAuth_googleSignInListener;
    private OnCompleteListener<AuthResult> _FAuth_create_user_listener;
    private OnCompleteListener<AuthResult> _FAuth_sign_in_listener;
    private OnCompleteListener<Void> _FAuth_reset_password_listener;
    private final ObjectAnimator oba = new ObjectAnimator();
    private final Calendar c1 = Calendar.getInstance();
    private final Calendar c2 = Calendar.getInstance();
    private final Calendar call = Calendar.getInstance();
    private SharedPreferences list;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.audioview);
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
        this.linear4 = this.findViewById(R.id.linear4);
        this.linear6 = this.findViewById(R.id.linear6);
        this.linear3 = this.findViewById(R.id.linear3);
        this.textview4 = this.findViewById(R.id.textview4);
        this.linear10 = this.findViewById(R.id.linear10);
        this.linear7 = this.findViewById(R.id.linear7);
        this.linear9 = this.findViewById(R.id.linear9);
        this.seekbar1 = this.findViewById(R.id.seekbar1);
        this.textview1 = this.findViewById(R.id.textview1);
        this.textview2 = this.findViewById(R.id.textview2);
        this.textview3 = this.findViewById(R.id.textview3);
        this.textview5 = this.findViewById(R.id.textview5);
        this.textview6 = this.findViewById(R.id.textview6);
        this.imageview1 = this.findViewById(R.id.imageview1);
        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.PHONE = this.getSharedPreferences("PHONE", Context.MODE_PRIVATE);
        this.Aplay = this.getSharedPreferences("Aplay", Context.MODE_PRIVATE);
        this.FAuth = FirebaseAuth.getInstance();
        this.list = this.getSharedPreferences("list", Context.MODE_PRIVATE);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);

        this.imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                if (!(AudioviewActivity.this.playmediaListview == null)) {
                    if (AudioviewActivity.this.playmediaListview.isPlaying()) {
                        AudioviewActivity.this.playmediaListview.pause();
                    } else {
                        AudioviewActivity.this.playmediaListview.start();
                    }
                }
            }
        });

        this._Fstore_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull final UploadTask.TaskSnapshot _param1) {
                final double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        this._Fstore_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(final FileDownloadTask.TaskSnapshot _param1) {
                final double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                AudioviewActivity.this.textview6.setText(String.valueOf((long) (_progressValue)).concat("%"));
            }
        };

        this._Fstore_upload_success_listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull final Task<Uri> _param1) {
                String _downloadUrl = _param1.getResult().toString();

            }
        };

        this._Fstore_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final FileDownloadTask.TaskSnapshot _param1) {
                long _totalByteCount = _param1.getTotalByteCount();
                AudioviewActivity.this._Step1Set(AudioviewActivity.this.imageview1, AudioviewActivity.this.seekbar1, AudioviewActivity.this.Fpath);
                AudioviewActivity.this._notifyUpdate("r");
                AudioviewActivity.this.linear9.setVisibility(View.GONE);
            }
        };

        this._Fstore_delete_success_listener = _param1 -> {

        };

        this._Fstore_failure_listener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception _param1) {
                String _message = _param1.getMessage();

            }
        };

        final OnCompleteListener<Void> FAuth_updateEmailListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(final Task<Void> _param1) {
                boolean _success = _param1.isSuccessful();
                String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        this.FAuth_updatePasswordListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull final Task<Void> _param1) {
                boolean _success = _param1.isSuccessful();
                String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        this.FAuth_emailVerificationSentListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull final Task<Void> _param1) {
                boolean _success = _param1.isSuccessful();
                String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        this.FAuth_deleteUserListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull final Task<Void> _param1) {
                boolean _success = _param1.isSuccessful();
                String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        this.FAuth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                boolean _success = task.isSuccessful();
                String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";

            }
        };

        this.FAuth_updateProfileListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull final Task<Void> _param1) {
                boolean _success = _param1.isSuccessful();
                String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        this.FAuth_googleSignInListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                boolean _success = task.isSuccessful();
                String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";

            }
        };

        this._FAuth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> _param1) {
                boolean _success = _param1.isSuccessful();
                String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        this._FAuth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> _param1) {
                boolean _success = _param1.isSuccessful();
                String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        this._FAuth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull final Task<Void> _param1) {
                boolean _success = _param1.isSuccessful();

            }
        };
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
        this.CrazyChatPath = FileUtil.getPackageDataDir(this.getApplicationContext());
        final double pos = Double.parseDouble(this.getIntent().getStringExtra("pos"));
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(this._getMsg(pos, "by"))) {
            this.textview4.setText("You");
            if (this._isFound("is", this._getMsg(pos, "id"))) {
                this.linear9.setVisibility(View.GONE);
                this._Step1Set(this.imageview1, this.seekbar1, this._getIdPath("is", this._getMsg(pos, "id")));
            } else {
                this.linear9.setVisibility(View.VISIBLE);
                this.Fpath = this._getIdPath("is", this._getMsg(pos, "id"));
                this._firebase_storage.getReferenceFromUrl(this._getMsg(pos, "mainl")).getFile(new File(this.Fpath)).addOnSuccessListener(this._Fstore_download_success_listener).addOnFailureListener(this._Fstore_failure_listener).addOnProgressListener(this._Fstore_download_progress_listener);
            }
        } else {
            this.textview4.setText(this.ChatList.getString(this._getMsg(pos, "by")+ "n", ""));
            if (this._isFound("ir", this._getMsg(pos, "id"))) {
                this.linear9.setVisibility(View.GONE);
                this._Step1Set(this.imageview1, this.seekbar1, this._getIdPath("ir", this._getMsg(pos, "id")));
            } else {
                this.linear9.setVisibility(View.VISIBLE);
                this.Fpath = this._getIdPath("ir", this._getMsg(pos, "id"));
                this._firebase_storage.getReferenceFromUrl(this._getMsg(pos, "mainl")).getFile(new File(this.Fpath)).addOnSuccessListener(this._Fstore_download_success_listener).addOnFailureListener(this._Fstore_failure_listener).addOnProgressListener(this._Fstore_download_progress_listener);
            }
        }
        this.textview3.setText(this._getMsg(pos, "msg"));
        final Jay jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.linear7.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20, Color.parseColor(jay.col(0, 1))));
        this.linear9.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20,  Color.parseColor(jay.col(0, 1))));
        this.linear10.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(final int a, final int b) {
                setCornerRadius(a);
                setColor(b);
                return this;
            }
        }.getIns((int) 20,  Color.parseColor(jay.col(0, 1))));
        this._changeActivityFont("product_more_block");
        this.linear1.setBackgroundColor(Color.parseColor(jay.col(0, 3)));
        this.textview1.setTextColor( Color.parseColor(jay.col(1, 1)));
        this.textview2.setTextColor( Color.parseColor(jay.col(1, 1)));
        this.textview3.setTextColor( Color.parseColor(jay.col(1, 1)));
        this.textview5.setTextColor( Color.parseColor(jay.col(1, 1)));
        this.textview6.setTextColor( Color.parseColor(jay.col(1, 1)));
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
        if (!(this.playmediaListview == null)) {
            if (this.playmediaListview.isPlaying()) {
                this.playmediaListview.pause();
            } else {

            }
            this.playmediaListview.release();
        }
    }

    public boolean _isFound(String _path, String _id) {
        if ("is".equals(_path)) {
            return FileUtil.isExistFile(this.CrazyChatPath.concat("/Mic/sent/".concat(_id)));
        }
        if ("ir".equals(_path)) {
            return FileUtil.isExistFile(this.CrazyChatPath.concat("/Mic/received/".concat(_id)));
        }
        return (false);
    }


    public String _getIdPath(String _path, String _id) {
        if (FileUtil.isDirectory(this.CrazyChatPath.concat("/Mic/received"))) {

        } else {
            FileUtil.makeDir(this.CrazyChatPath.concat("/Mic/received"));
        }
        if (FileUtil.isDirectory(this.CrazyChatPath.concat("/Mic/sent"))) {

        } else {
            FileUtil.makeDir(this.CrazyChatPath.concat("/Mic/sent"));
        }
        if ("ir".equals(_path)) {
            return (this.CrazyChatPath.concat("/Mic/received/".concat(_id)));
        }
        if ("is".equals(_path)) {
            return (this.CrazyChatPath.concat("/Mic/sent/".concat(_id)));
        }
        return ("null");
    }


    public void _Step1Set(ImageView _img, SeekBar _seekbar, String _path) {
        _img.setImageResource(R.drawable.ic_pause_white);
        this.playmediaListview = new MediaPlayer();
        try {
            this.playmediaListview.setDataSource(_path);
        } catch (final IllegalArgumentException e) {
            Toast.makeText(this.getApplicationContext(), "Please Wait!", Toast.LENGTH_LONG).show();
        } catch (final SecurityException e2) {
            Toast.makeText(this.getApplicationContext(), "You Are Blocked By DataBase Security!", Toast.LENGTH_LONG).show();
        } catch (final IllegalStateException | IOException e3) {

        }
        try {
            this.playmediaListview.prepare();
        } catch (final IllegalStateException e5) {

        } catch (final java.io.IOException e6) {
            Toast.makeText(this.getApplicationContext(), "Checking Please Wait!", Toast.LENGTH_LONG).show();
        }
        this.playmediaListview.start();
        _seekbar.setMax(this.playmediaListview.getDuration());
        this.Aplay.edit().putString(_path, "true").apply();
        this.Aplay.edit().putString("c", _path).apply();
        _seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar _param1, final int _param2, final boolean _param3) {
                int _progressValue = _param2;

            }

            @Override
            public void onStartTrackingTouch(final SeekBar _param1) {
                if (!(AudioviewActivity.this.playmediaListview == null)) {
                    AudioviewActivity.this.Aplay.edit().putString(_path.concat("seek"), "true").apply();
                }
            }

            @Override
            public void onStopTrackingTouch(final SeekBar _param2) {
                if (!(AudioviewActivity.this.playmediaListview == null)) {
                    AudioviewActivity.this.Aplay.edit().putString(_path.concat("seek"), "false").apply();
                    AudioviewActivity.this.playmediaListview.pause();
                    AudioviewActivity.this.playmediaListview.seekTo(_seekbar.getProgress());
                    AudioviewActivity.this.playmediaListview.start();
                }
            }
        });
        this.c1.setTimeInMillis(this.playmediaListview.getDuration());
        this.c2.setTimeInMillis(1800000);
        this.call.setTimeInMillis(this.c1.getTimeInMillis() - this.c2.getTimeInMillis());
        this.textview3.setText(new SimpleDateFormat("mm:ss").format(this.call.getTime()));
        try {
            final TimerTask aft1 = new TimerTask() {
                @Override
                public void run() {
                    AudioviewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                AudioviewActivity.this.c1.setTimeInMillis(AudioviewActivity.this.playmediaListview.getCurrentPosition());
                                AudioviewActivity.this.c2.setTimeInMillis(1800000);
                                AudioviewActivity.this.call.setTimeInMillis(AudioviewActivity.this.c1.getTimeInMillis() - AudioviewActivity.this.c2.getTimeInMillis());
                                AudioviewActivity.this.textview1.setText(new SimpleDateFormat("mm:ss").format(AudioviewActivity.this.call.getTime()));
                                if (AudioviewActivity.this.playmediaListview.isPlaying()) {
                                    _img.setImageResource(R.drawable.ic_pause_white);
                                    if ("true".equals(AudioviewActivity.this.Aplay.getString(_path.concat("seek"), ""))) {

                                    } else {
                                        _seekbar.setProgress(AudioviewActivity.this.playmediaListview.getCurrentPosition());
                                    }
                                } else {
                                    _img.setImageResource(R.drawable.ic_play_arrow_white);
                                }
                            } catch (final IllegalStateException e5) {
                            }
                        }
                    });
                }
            };
            this._timer.scheduleAtFixedRate(aft1, 0, 100);
        } catch (final Exception e) {

        }
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


    public void _dialogTheme() {
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

    {
    }


    public void _notifyUpdate(String _Key) {
        if ("rs".equals(_Key)) {
            this.list.edit().putString("a", String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
        } else {
            if ("sr".equals(_Key)) {
                this.list.edit().putString("a", String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
            } else {
                for (int n = 0; n < _Key.length(); n++) {
                    this.list.edit().putString(_Key.substring(n, n + 1), String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
                }
            }
        }
    }


    public void _changeActivityFont(String _fontname) {
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
