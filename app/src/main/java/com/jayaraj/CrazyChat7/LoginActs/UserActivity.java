package com.jayaraj.CrazyChat7.LoginActs;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.HomeActs.HomeActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class UserActivity extends AppCompatActivity {
    public final int REQ_CD_FP = 101;
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

    private final Timer _timer = new Timer();
    private SharedPreferences asp;
    private String st = "";
    private HashMap<String, Object> TempMap = new HashMap<>();
    private final HashMap<String, Object> map = new HashMap<>();
    private String IMG = "";
    private String tmp = "";
    private final double n = 0;
    private String t = "";
    private String t2 = "";

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear10;
    private ImageView imageviewBackGround;
    private LinearLayout linear1;
    private RelativeLayout linear3;
    private TextView textviewPhone, textviewFriends;
    private LinearLayout mobilenumberbar;
    private LinearLayout ok;
    private LinearLayout linear6;
    private LinearLayout linear7;
    private LinearLayout linear9;
    private ProgressBar progressbar3;
    private ImageView imageview1;
    private ImageView imageview2;
    private TextView textview16;
    private EditText entermobile;
    private TextView textview17;
    private ProgressBar progressbar2;
    private TextView textview15;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference ProfileDB = this._firebase.getReference("rand");
    private ChildEventListener _ProfileDB_child_listener;
    private FirebaseAuth auth;
    private final StorageReference fstore = this._firebase_storage.getReference("Profile/");
    private OnCompleteListener<Uri> _fstore_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _fstore_download_success_listener;
    private OnSuccessListener _fstore_delete_success_listener;
    private OnProgressListener _fstore_upload_progress_listener;
    private OnProgressListener _fstore_download_progress_listener;
    private OnFailureListener _fstore_failure_listener;
    private SharedPreferences pdataSP;
    private SharedPreferences status;
    private final Intent inte = new Intent();
    private final Intent fp = new Intent(Intent.ACTION_GET_CONTENT);
    private SharedPreferences wekcome;
    private SharedPreferences ChatList;
    private DatabaseReference FindUIDdb = this._firebase.getReference("UID");

    private Bitmap img;
    private Intent innoti;
    private PendingIntent pendnoti;


    private Jay jay;


    NotificationManagerCompat noti;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.user);
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
    public void onDestroy() {
        super.onDestroy();
        this.ProfileDB.removeEventListener(this._ProfileDB_child_listener);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            this.initializeLogic();
        }
    }

    private void initialize(final Bundle _savedInstanceState) {

        this.img = BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon);
        this.innoti = new Intent(this.getApplicationContext(), UserActivity.class);
        this.innoti.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            this.pendnoti = PendingIntent.getActivity(this.getApplicationContext(), 0, this.innoti, PendingIntent.FLAG_MUTABLE);
        } else {
            this.pendnoti = PendingIntent.getActivity(this.getApplicationContext(), 0, this.innoti, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel("PROFILE", "Profile", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Profile Updates");
            final NotificationManager nm = this.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear10 = this.findViewById(R.id.linear10);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.linear1 = this.findViewById(R.id.linear1);
        this.linear3 = this.findViewById(R.id.linear3);
        this.textviewPhone = this.findViewById(R.id.textviewPhone);
        this.textviewFriends = this.findViewById(R.id.textviewFriends);
        this.mobilenumberbar = this.findViewById(R.id.mobilenumberbar);
        this.ok = this.findViewById(R.id.ok);
        this.linear6 = this.findViewById(R.id.linear6);
        this.linear7 = this.findViewById(R.id.linear7);
        this.linear9 = this.findViewById(R.id.linear9);
        this.progressbar3 = this.findViewById(R.id.progressbar3);
        this.imageview1 = this.findViewById(R.id.imageview1);
        this.imageview2 = this.findViewById(R.id.imageview2);
        this.textview16 = this.findViewById(R.id.textview16);
        this.entermobile = this.findViewById(R.id.entermobile);
        this.textview17 = this.findViewById(R.id.textview17);
        this.progressbar2 = this.findViewById(R.id.progressbar2);
        this.textview15 = this.findViewById(R.id.textview15);
        this.auth = FirebaseAuth.getInstance();
        this.pdataSP = this.getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.status = this.getSharedPreferences("status", Context.MODE_PRIVATE);
        this.fp.setType("image/*");
        this.fp.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        this.wekcome = this.getSharedPreferences("welcome", Context.MODE_PRIVATE);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));


        this.imageview2.setOnClickListener(_view -> {
            final Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            this.startActivityForResult(i, this.REQ_CD_FP);
        });


        this.entermobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence _param1, final int _param2, final int _param3, final int _param4) {
                String _charSeq = _param1.toString();
                if (25 == _charSeq.length()) {
                    UserActivity.this.textview17.setTextColor(0xFFF44336);
                } else {
                    if (3 > _charSeq.length()) {
                        UserActivity.this.textview17.setTextColor(0xFFF44336);
                    } else {
                        UserActivity.this.textview17.setTextColor(0xFF000000);
                    }
                }
                UserActivity.this.textview17.setText(String.valueOf((long) (_charSeq.length())).concat("/25"));
            }

            @Override
            public void beforeTextChanged(final CharSequence _param1, final int _param2, final int _param3, final int _param4) {

            }

            @Override
            public void afterTextChanged(final Editable _param1) {

            }
        });

        this.textview15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                UserActivity.this.entermobile.setText(UserActivity.this.entermobile.getText().toString().trim());
                if (3 > UserActivity.this.entermobile.getText().toString().length()) {
                    SketchwareUtil.showMessage(UserActivity.this.getApplicationContext(), "Enter Your Name Greater than 3 Letters");
                } else {
                    UserActivity.this._OnClickSave();
                }
            }
        });

        this._ProfileDB_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                UserActivity.this._Anime("onchildadded", UserActivity.this.linear10);
                UserActivity.this.linear3.setVisibility(View.VISIBLE);
                UserActivity.this.mobilenumberbar.setVisibility(View.VISIBLE);
                UserActivity.this.progressbar2.setVisibility(View.GONE);
                UserActivity.this.textview15.setVisibility(View.VISIBLE);
                assert _childValue != null;
                if (_childValue.containsKey("NAME")) {
                    UserActivity.this.entermobile.setText(Objects.requireNonNull(_childValue.get("NAME")).toString());
                } else {
                    SketchwareUtil.showMessage(UserActivity.this.getApplicationContext(), "Welcome To Crazy Chat");
                }
                if (_childValue.containsKey("URL")) {
                    if ("".equals(Objects.requireNonNull(_childValue.get("URL")).toString())) {
                        UserActivity.this.imageview1.setImageResource(R.drawable.default_profile);
                    } else {
                        Glide.with(UserActivity.this.getApplicationContext())
                                .load(Uri.parse(Objects.requireNonNull(_childValue.get("URL")).toString()))
                                .placeholder(R.drawable.default_profile)
                                .into(UserActivity.this.imageview1);
                    }
                    UserActivity.this.IMG = Objects.requireNonNull(_childValue.get("URL")).toString();
                } else {
                    UserActivity.this.TempMap = new HashMap<>();
                    UserActivity.this.TempMap.put("URL", "");
                    UserActivity.this.ProfileDB.child("data").updateChildren(UserActivity.this.TempMap);
                    UserActivity.this.TempMap.clear();
                    UserActivity.this.progressbar3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot _param1, final String _param2) {
            }

            @Override
            public void onChildMoved(@NonNull final DataSnapshot _param1, final String _param2) {
            }

            @Override
            public void onChildRemoved(@NonNull final DataSnapshot _param1) {
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError _param1) {
            }
        };

        this._fstore_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull final UploadTask.TaskSnapshot _param1) {
                final double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        this._fstore_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull final FileDownloadTask.TaskSnapshot _param1) {
                final double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        this._fstore_upload_success_listener = _param1 -> {
            String _downloadUrl = _param1.getResult().toString();
            this._Anime("oncompleteup", this.linear10);
            this.textview15.setVisibility(View.VISIBLE);
            this.progressbar2.setVisibility(View.GONE);
            this.imageview2.setEnabled(true);
            this.IMG = _downloadUrl;
            this.TempMap = new HashMap<>();
            this.TempMap.put("URL", _downloadUrl);
            this.ProfileDB.child("data").updateChildren(this.TempMap);
            this.TempMap.clear();
            this.pdataSP.edit().putString("IMG", _downloadUrl).apply();
            this.status.edit().putString("CHS", "true").apply();
            SketchwareUtil.showMessage(this.getApplicationContext(), "Profile Picture Updated");

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), "PROFILE")
                    .setSmallIcon(R.drawable.app_icon)
                    .setLargeIcon(this.img)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setOnlyAlertOnce(false)
                    .setContentIntent(this.pendnoti)
                    .setContentTitle("Profile Status")
                    .setContentText("Profile Picture is Uploaded Successfully")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            this.noti = NotificationManagerCompat.from(this.getApplicationContext());

            this.noti.notify(1, builder.build());
            this.saveFirestore();
        };

        this._fstore_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final FileDownloadTask.TaskSnapshot _param1) {
                long _totalByteCount = _param1.getTotalByteCount();

            }
        };

        this._fstore_delete_success_listener = new OnSuccessListener() {
            @Override
            public void onSuccess(final Object _param1) {

            }
        };

        this._fstore_failure_listener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception _param1) {
                String _message = _param1.getMessage();
                UserActivity.this.textview15.setVisibility(View.VISIBLE);
                UserActivity.this.progressbar2.setVisibility(View.GONE);
                UserActivity.this.imageview2.setEnabled(true);
                SketchwareUtil.showMessage(UserActivity.this.getApplicationContext(), "Upload Failed");
                SketchwareUtil.showMessage(UserActivity.this.getApplicationContext(), _message);
            }
        };
    }

    private void initializeLogic() {
        this._font();
        this._Ui();
        this._Bg();
        this._OnActCrt();
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        if (_requestCode==this.REQ_CD_FP) {
                if (_resultCode == Activity.RESULT_OK) {
                    final ArrayList<String> _filePath = new ArrayList<>();
                    if (_data != null) {
                        if (_data.getClipData() != null) {
                            for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
                                final ClipData.Item _item = _data.getClipData().getItemAt(_index);
                                _filePath.add(FileUtil.convertUriToFilePath(this.getApplicationContext(), _item.getUri()));
                            }
                        } else {
                            _filePath.add(FileUtil.convertUriToFilePath(this.getApplicationContext(), _data.getData()));
                        }
                    }
                    if (_filePath.get(0).endsWith(".png")) {
                        this._ComImage(_filePath.get(0));
                    } else {
                        if (_filePath.get(0).endsWith(".jpg")) {
                            this._ComImage(_filePath.get(0));
                        } else {
                            SketchwareUtil.showMessage(this.getApplicationContext(), "Please Select a Image\nIncorrect Image Format");
                        }
                    }
                } else {

                }

        }
    }

    public void _font() {
        this.textviewPhone.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.entermobile.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textview15.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textview16.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textview17.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textviewFriends.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
    }


    public void _OnActCrt() {
        this._ConnectDB();
        this.textviewPhone.setText(this.pdataSP.getString("PHONE", ""));
        this.entermobile.setSingleLine(true);
        this.entermobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        this.textviewFriends.setVisibility(View.GONE);
        this._Anime("oncrt", this.linear10);
        this.mobilenumberbar.setVisibility(View.GONE);
        this.linear3.setVisibility(View.GONE);
        this.textview15.setVisibility(View.GONE);

    }


    public void _Ui() {
        this._SetBackground(this.mobilenumberbar, 40, 0, this.jay.col(0, 1), false);
        this._SetBackground(this.textviewPhone, 35, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.textviewFriends, 35, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.ok, 40, 0, "#81c784", true);
        this._SetBackground(this.linear3, 40, 0, this.jay.col(0, 1), false);
        this.textview16.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.entermobile.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.textviewPhone.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.textviewFriends.setTextColor(Color.parseColor(this.jay.col(1, 1)));
    }

    public void _Anime(String _Sp, View _Linear) {

        if ("".equals(this.asp.getString(_Sp, ""))) {
            this.asp.edit().putString(_Sp, "true").apply();
            final AutoTransition autoTransition = new AutoTransition();
            autoTransition.setDuration(200);


            TransitionManager.beginDelayedTransition((LinearLayout) _Linear, autoTransition);
            try {
                final TimerTask at = new TimerTask() {
                    @Override
                    public void run() {
                        UserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserActivity.this.asp.edit().putString(_Sp, "").apply();
                            }
                        });
                    }
                };
                this._timer.schedule(at, 350);
            } catch (final Exception ignored) {
            }
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

    void save(final String Key, final String val) {
        this.TempMap = new HashMap<>();
        this.TempMap.put("Reserve", "");
        this.db.collection("App").document("UserProfile").collection(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).document("About").update(this.TempMap);
        this.TempMap.clear();
    }

    public void _ConnectDB() {
        this.st = "Profile/uid/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        this.ProfileDB = this._firebase.getReference(this.st);
        this.ProfileDB.addChildEventListener(this._ProfileDB_child_listener);
        this.FindUIDdb = this._firebase.getReference("UserUids/".concat(this._phonetoid(this.pdataSP.getString("PHONE", ""))));
        this.TempMap = new HashMap<>();
        this.TempMap.put("CID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.TempMap.put("PHONE", this.pdataSP.getString("PHONE", ""));
        this.ProfileDB.child("data").updateChildren(this.TempMap);
        this.TempMap.clear();
        this.TempMap = new HashMap<>();
        this.TempMap.put("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.FindUIDdb.child("data").updateChildren(this.TempMap);
        this.TempMap.clear();

        final AggregateQuery countQuery = this.db.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Friends").count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final AggregateQuerySnapshot snapshot = task.getResult();
                if(snapshot.getCount()>0){
                    this._Anime("oncrt2", this.linear10);
                    this.textviewFriends.setVisibility(View.VISIBLE);
                    this.textviewFriends.setText(String.valueOf(snapshot.getCount()).concat(" Friends"));
                }
            } else {
                System.out.println("Count failed: "+ task.getException());
            }
        });
    }


    public void _ComImage(String _Path) {
        this.imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_Path, 1024, 1024));
        try {
            final BitmapDrawable imageview1BD = (BitmapDrawable) this.imageview1.getDrawable();
            final Bitmap imageview1B = imageview1BD.getBitmap();
            FileOutputStream imageview1FOS = null;
            final File imageview1F = Environment.getExternalStorageDirectory();
            final File imageview1F2 = new File(FileUtil.getPackageDataDir(this.getApplicationContext()));
            imageview1F2.mkdirs();
            final String imageview1FN = "TmpImg.png";
            final File imageview1F3 = new File(imageview1F2, imageview1FN);
            imageview1FOS = new FileOutputStream(imageview1F3);
            imageview1B.compress(Bitmap.CompressFormat.JPEG, 30, imageview1FOS);
            imageview1FOS.flush();
            imageview1FOS.close();
            final Intent imageview1I = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            imageview1I.setData(Uri.fromFile(imageview1F));
            this.sendBroadcast(imageview1I);
        } catch (final Exception e) {
        }
        if (0 == FileUtil.getFileLength(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/TmpImg.png"))) {
            SketchwareUtil.showMessage(this.getApplicationContext(), "Image Is Crashed\nPlease Select Another Image");
        } else {
            this.fstore.child(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/MyProfilePicture.png")).putFile(Uri.fromFile(new File(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/TmpImg.png")))).addOnFailureListener(this._fstore_failure_listener).addOnProgressListener(this._fstore_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(final Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return UserActivity.this.fstore.child(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/MyProfilePicture.png")).getDownloadUrl();
                }
            }).addOnCompleteListener(this._fstore_upload_success_listener);
            this._Anime("changephoto", this.linear10);
            this.textview15.setVisibility(View.GONE);
            this.progressbar2.setVisibility(View.VISIBLE);
            this.imageview2.setEnabled(false);
            SketchwareUtil.showMessage(this.getApplicationContext(), "Uploading Please Wait");
        }
    }

    void saveFirestore() {
        //Firestore save
        this.TempMap = new HashMap<>();
        this.TempMap.put("NAME", this.pdataSP.getString("NAME",""));
        this.TempMap.put("URL", this.pdataSP.getString("IMG",""));
        this.TempMap.put("PHONE", this.pdataSP.getString("PHONE",""));
        this.TempMap.put("CID",Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        this.db.collection("UserProfile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).set(this.TempMap);
        this.TempMap.clear();
    }

    //onsave
    public void _OnClickSave() {
        this.pdataSP.edit().putString("NAME", this.entermobile.getText().toString()).apply();
        this.pdataSP.edit().putString("IMG", this.IMG).apply();
        this.status.edit().putString("CHS", "true").apply();
        this._Anime("onsave", this.linear10);
        this.progressbar2.setVisibility(View.VISIBLE);
        this.textview15.setVisibility(View.GONE);
        this.TempMap = new HashMap<>();
        this.TempMap.put("NAME", this.entermobile.getText().toString());
        this.ProfileDB.child("data").updateChildren(this.TempMap);
        this.db.collection("App").document("UserProfile").collection(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).document("About").update(this.TempMap);
        this.TempMap.clear();
        this._SetSP();
        if (this.wekcome.getString("us", "").equals("")) {
            this.wekcome.edit().putString("us", "true").apply();
            this.inte.setClass(this.getApplicationContext(), HomeActivity.class);
            this.startActivity(this.inte);
        }

        this.saveFirestore();

        final NotificationManagerCompat nclear = NotificationManagerCompat.from(this);
        nclear.cancel(1);

        this.finish();

    }


    public void _SetSP() {
        this.ChatList.edit().putString("n", "0").apply();
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


    public String _phonetoid(String _phone) {
        this.tmp = "";
        for (int n = 0; n < _phone.length(); n++) {
            this.t = _phone.substring(n, n + 1);
            if (this.t.equals("+")) {
                this.t2 = "A";
            }
            if (this.t.equals("0")) {
                this.t2 = "B";
            }
            if (this.t.equals("1")) {
                this.t2 = "C";
            }
            if (this.t.equals("2")) {
                this.t2 = "D";
            }
            if (this.t.equals("3")) {
                this.t2 = "E";
            }
            if (this.t.equals("4")) {
                this.t2 = "F";
            }
            if (this.t.equals("5")) {
                this.t2 = "G";
            }
            if (this.t.equals("6")) {
                this.t2 = "H";
            }
            if (this.t.equals("7")) {
                this.t2 = "I";
            }
            if (this.t.equals("8")) {
                this.t2 = "J";
            }
            if (this.t.equals("9")) {
                this.t2 = "K";
            }
            this.tmp = this.tmp.concat(this.t2);
        }
        return (this.tmp);
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
