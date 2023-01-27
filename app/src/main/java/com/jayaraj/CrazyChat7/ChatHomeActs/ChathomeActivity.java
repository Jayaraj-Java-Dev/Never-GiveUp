package com.jayaraj.CrazyChat7.ChatHomeActs;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.HomeActs.HomeActivity;
import com.jayaraj.CrazyChat7.ImageVidDispActs.MultiPickerActivity;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;
import com.jayaraj.CrazyChat7.J.getData;
import com.jayaraj.CrazyChat7.OtherActs.ChatHomeProfileActivity;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.ServicesActs.DbmsgActivity;
import com.jayaraj.CrazyChat7.ServicesActs.KeepOnlineService;
import com.jayaraj.CrazyChat7.videocompressor.VideoCompress;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.echodev.resizer.Resizer;

public class ChathomeActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
    private double n;
    private boolean filesVisiblebool;
    private double ran;
    private double n2_min;
    private double n3_hrs;
    private boolean recordbool;
    private String Audio = "";
    private boolean uploading;
    private HashMap<String, Object> map = new HashMap<>();
    private final HashMap<String, Object> map2 = new HashMap<>();
    private float nmc;

    FirebaseFirestore ffs = FirebaseFirestore.getInstance();
    private boolean pause;
    private HashMap<String, Object> tmap2 = new HashMap<>();
    private String TmpStr = "";
    private boolean upLoad;
    private HashMap<String, Object> onreMap = new HashMap<>();
    private String text = "";
    private boolean micg;
    private boolean merge;
    private HashMap<String, Object> Nmap = new HashMap<>();
    private HashMap<String, Object> TestMap = new HashMap<>();
    private boolean ivCrNew;
    private HashMap<String, Object> templistmap1 = new HashMap<>();
    private final String positionval = "";
    private String tmpVar = "";
    private double total;
    private String text2 = "";
    private boolean isInJob;
    private String image = "";
    private String current = "";
    private String lseen = "";
    private boolean upd;
    private double key;
    private String tmp = "";
    private String tmp2 = "";
    private double tn1;
    private double tn2;
    private boolean msgMark;
    private HashMap<String, Object> markmsgmap = new HashMap<>();
    private String ID = "";
    private String fontName = "";
    private double type;
    private String currentSTS = "";
    private String tmpstrmsg = "";
    private ArrayList<HashMap<String, Object>> imgvidpaths = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> tlm = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> tlm2 = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> bundleLM = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> tlistmap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> nestlm = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> uploadlm = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear2;
    private ImageView imageviewBackGround;
    private RelativeLayout linear1;
    private ViewPager2 viewpager1;
    private LinearLayout linear3;
    private LinearLayout linear4;
    private CircleImageView circleimageview1;
    private LinearLayout linear55;
    private LinearLayout linear59;
    private TextView textviewName;
    private TextView textviewOnOff;
    private ImageView imageview1;
    private LinearLayout linear22;
    private LinearLayout linear56;
    private LinearLayout linear40;
    private LinearLayout linear23;
    private LinearLayout linear11;
    private TextView textview6;
    private TextView textview5;
    private LinearLayout linear49;
    private LinearLayout linear44;
    private LinearLayout linear47;
    private TextView textview4;
    private LinearLayout linear48;
    private LinearLayout linear41;
    private LinearLayout linear42;
    private TextView textview2;
    private TextView textview3;
    private LinearLayout linear43;
    private HorizontalScrollView hscroll1;
    private LinearLayout linear17;
    private LinearLayout linear24;
    private LinearLayout linear9;
    private LinearLayout linear20;
    private LinearLayout linear14;
    private LinearLayout linear18;
    private LinearLayout linear58;
    private EditText edittext1;
    private LinearLayout linear21;
    private LinearLayout linear16;

    //Game
    private LinearLayout linearpad, linearpad2, gamepad, requestlinear;
    private LinearLayout linearanipad;
    private TextView textpad, textpad2, reqtext1, reqtext2, reqtext3, labeltext;


    private SharedPreferences PHONE;
    private TimerTask keyb;
    private SharedPreferences list;
    private SharedPreferences msg;
    private TimerTask msgT;
    private Calendar msgC = Calendar.getInstance();
    private TimerTask loti;
    private TimerTask after,after3,after2;
    private TimerTask at;
    private SharedPreferences asp;
    private Calendar cal1 = Calendar.getInstance();
    private MediaRecorder rec;
    private SpeechRecognizer spt;
    private TimerTask t1;
    private FirebaseAuth auth;
    private final Intent fpic = new Intent();
    private SharedPreferences pathssp;
    private AlertDialog.Builder failureDialog;
    private final StorageReference storage = this._firebase_storage.getReference("AllSent/ImagesVideos/");
    private DatabaseReference dbSend = this._firebase.getReference("SenderUidRand");
    private DatabaseReference dbMy = this._firebase.getReference("MyUidRand");
    private SharedPreferences imgurls;
    private DatabaseReference dbSendProfile = this._firebase.getReference("ProfileRand");
    private ChildEventListener _dbSendProfile_child_listener;
    private TimerTask onofftim;
    private SharedPreferences ChatList;
    private Calendar tcal = Calendar.getInstance();
    private SharedPreferences spbts;
    private Calendar cal = Calendar.getInstance();
    private DatabaseReference dbGal = this._firebase.getReference("Rand");
    private TimerTask tmptimer;
    private SharedPreferences ChatRef;
    private final ObjectAnimator objanim = new ObjectAnimator();
    private LottieAnimationView l1, l2, l3, l4, l5, l6, l7, l8;
    private long StartUpTime;
    private LinearLayout bottom, br1;
    private TextView br1t1, br1t2, br1t3;
    private Jay jay;
    private String txtmsg;
    private boolean tebool = true;
    private int timei = 3;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.chathome);
        this.initialize(_savedInstanceState);
        com.google.firebase.FirebaseApp.initializeApp(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        } else {
            this.initializeLogic();
        }

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            this.initializeLogic();
        }
    }

    private void initialize(final Bundle _savedInstanceState) {
        //OTHER LAYOUTS
        this.l1 = this.findViewById(R.id.lottie1);
        this.l1.setAnimation("sendMsg.json");
        this.l2 = this.findViewById(R.id.lottie2);
        this.l2.setAnimation("voice.json");
        this.l3 = this.findViewById(R.id.lottie3);
        this.l3.setAnimation("file.json");
        this.l4 = this.findViewById(R.id.lottie4);
        this.l4.setAnimation("sendMsg.json");
        this.l5 = this.findViewById(R.id.lottie5);
        this.l5.setAnimation("reddot.json");
        this.l6 = this.findViewById(R.id.lottieaddfriend);
        this.l6.setAnimation("addFriend.json");
        this.l7 = this.findViewById(R.id.lottieimage1);
        this.l7.setAnimation("imagepic.json");
        this.l8 = this.findViewById(R.id.lottieimage2);
        this.l8.setAnimation("videopic.json");


        this.linearpad = this.findViewById(R.id.pad);
        this.linearanipad = this.findViewById(R.id.padanim);
        this.textpad = this.findViewById(R.id.padtext);

        //Game
        this.linearpad2 = this.findViewById(R.id.pad2);

        this.gamepad = this.findViewById(R.id.gamepad);
        this.textpad2 = this.findViewById(R.id.padtext2);
        this.linearpad2.setTranslationX(-this.getResources().getDisplayMetrics().widthPixels);
        this.reqtext1 = this.findViewById(R.id.reqtext1);
        this.reqtext2 = this.findViewById(R.id.reqtext2);
        this.reqtext3 = this.findViewById(R.id.reqtext3);
        this.requestlinear = this.findViewById(R.id.requestLinear);


        this.bottom = this.findViewById(R.id.bottom);
//        this.br1t1 = this.findViewById(R.id.br1t1);
//        this.br1t2 = this.findViewById(R.id.br1t2);
        this.br1t3 = this.findViewById(R.id.br1t3);

        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear2 = this.findViewById(R.id.linear2);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.linear1 = this.findViewById(R.id.linear1);
        this.viewpager1 = this.findViewById(R.id.viewpager1);
        this.linear3 = this.findViewById(R.id.linear3);
        this.linear4 = this.findViewById(R.id.linear4);
        this.circleimageview1 = this.findViewById(R.id.circleimageview1);
        this.linear55 = this.findViewById(R.id.linear55);
        this.linear59 = this.findViewById(R.id.linear59);
        this.textviewName = this.findViewById(R.id.textviewName);
        this.textviewOnOff = this.findViewById(R.id.textviewOnOff);
        this.imageview1 = this.findViewById(R.id.imageview1);
        this.linear22 = this.findViewById(R.id.linear22);
        this.linear56 = this.findViewById(R.id.linear56);
        this.linear40 = this.findViewById(R.id.linear40);
        this.linear23 = this.findViewById(R.id.linear23);
        this.linear11 = this.findViewById(R.id.linear11);
        this.textview6 = this.findViewById(R.id.textview6);
        this.textview5 = this.findViewById(R.id.textview5);
        //lottie11 = (LottieAnimationView) findViewById(R.id.lottie11);
        this.linear49 = this.findViewById(R.id.linear49);
        this.linear44 = this.findViewById(R.id.linear44);
        this.linear47 = this.findViewById(R.id.linear47);
        this.textview4 = this.findViewById(R.id.textview4);
        this.linear48 = this.findViewById(R.id.linear48);
        //lottie14 = (LottieAnimationView) findViewById(R.id.lottie14);
        this.linear41 = this.findViewById(R.id.linear41);
        this.linear42 = this.findViewById(R.id.linear42);
        this.textview2 = this.findViewById(R.id.textview2);
        this.textview3 = this.findViewById(R.id.textview3);
        this.linear43 = this.findViewById(R.id.linear43);
        //lottie12 = (LottieAnimationView) findViewById(R.id.lottie12);

        this.linear17 = this.findViewById(R.id.linear17);
        this.linear9 = this.findViewById(R.id.linear9);
        this.linear20 = this.findViewById(R.id.linear20);
        this.linear14 = this.findViewById(R.id.linear14);
        this.linear18 = this.findViewById(R.id.linear18);
        //lottie2 = (LottieAnimationView) findViewById(R.id.lottie2);
        this.linear58 = this.findViewById(R.id.linear58);
        this.edittext1 = this.findViewById(R.id.edittext1);
        //lottie16 = (LottieAnimationView) findViewById(R.id.lottie16);
        this.linear21 = this.findViewById(R.id.linear21);
        //lottie3 = (LottieAnimationView) findViewById(R.id.lottie3);
        this.linear16 = this.findViewById(R.id.linear16);
        //lottie1 = (LottieAnimationView) findViewById(R.id.lottie1);
        this.PHONE = this.getSharedPreferences("PHONE", Context.MODE_PRIVATE);
        this.list = this.getSharedPreferences("list", Context.MODE_PRIVATE);
        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.spt = SpeechRecognizer.createSpeechRecognizer(this);
        this.auth = FirebaseAuth.getInstance();
        this.pathssp = this.getSharedPreferences("paths", Context.MODE_PRIVATE);
        this.failureDialog = new AlertDialog.Builder(this);
        this.imgurls = this.getSharedPreferences("urls", Context.MODE_PRIVATE);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.spbts = this.getSharedPreferences("botomsheet", Context.MODE_PRIVATE);
        this.ChatRef = this.getSharedPreferences("ref", Context.MODE_PRIVATE);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        //Game
        this.initGame();
        this.linearpad.setTranslationX(SketchwareUtil.getDip(this.getApplicationContext(), -150));
        this.requestlinear.setVisibility(View.GONE);
        this.viewpager1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int _position) {
                super.onPageSelected(_position);
                ChathomeActivity.this._Anime(String.valueOf((long) (SketchwareUtil.getRandom(0, 9999))), ChathomeActivity.this.linear4);
                if (_position == 0) {
                    ChathomeActivity.this.linear22.setVisibility(View.VISIBLE);
                } else {
                    ChathomeActivity.this.linear22.setVisibility(View.GONE);
                    ChathomeActivity.this._hidekeyb();
                }
                ChathomeActivity.this.objanim.setTarget(ChathomeActivity.this.imageview1);
                ChathomeActivity.this.objanim.setPropertyName("rotation");
                if (_position == 1) {
                    ChathomeActivity.this.objanim.setFloatValues((float) (0));
                } else {
                    ChathomeActivity.this.objanim.setFloatValues((float) (180));
                }
                ChathomeActivity.this.objanim.setDuration(400);
                ChathomeActivity.this.objanim.setRepeatCount(0);
                ChathomeActivity.this.objanim.start();
            }

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(final int _scrollState) {
                super.onPageScrollStateChanged(_scrollState);
            }
        });

        this.linear3.setOnClickListener(_view -> {
            final Intent intentact = new Intent();
            intentact.setClass(this.getApplicationContext(), ChatHomeProfileActivity.class);
            intentact.putExtra("uid", this.getIntent().getStringExtra("UID"));
            this.circleimageview1.setTransitionName("img");
            final android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(this, this.circleimageview1, "img");
            this.startActivity(intentact, optionsCompat.toBundle());
        });

        this.imageview1.setOnClickListener(_view -> {
            if (this.viewpager1.getCurrentItem() == 1) {
                this.viewpager1.setCurrentItem(0);
            } else {
                this.viewpager1.setCurrentItem(1);
            }
        });

        this.textview5.setOnClickListener(_view -> {
            ChathomeActivity.this._Anime("li22", ChathomeActivity.this.linear22);
            ChathomeActivity.this.linear56.setVisibility(View.GONE);
            ChathomeActivity.this.msgMark = false;
        });

        this.textview2.setOnClickListener(_view -> {
            if (ChathomeActivity.this.recordbool) {
                ChathomeActivity.this.recordbool = false;
                ChathomeActivity.this.textview2.setText("RERECORD");
                try {
                    ChathomeActivity.this.rec.stop();
                    ChathomeActivity.this.rec.release();
                    ChathomeActivity.this.rec = null;
                } catch (final Exception ignored) {
                }
                ChathomeActivity.this.t1.cancel();
                ChathomeActivity.this._Anime("l47g", ChathomeActivity.this.linear40);
                ChathomeActivity.this.linear47.setVisibility(View.GONE);
            } else {
                ChathomeActivity.this.recordbool = true;
                ChathomeActivity.this._Anime("l47v", ChathomeActivity.this.linear40);
                ChathomeActivity.this.textview2.setText("STOP");
                ChathomeActivity.this.linear47.setVisibility(View.VISIBLE);
                ChathomeActivity.this.linear42.setVisibility(View.VISIBLE);
                ChathomeActivity.this.cal1 = Calendar.getInstance();
                //lottie11.playAnimation();
                ChathomeActivity.this.ran = SketchwareUtil.getRandom(0, 9999);
                if (FileUtil.isDirectory(FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Mic"))) {

                } else {
                    FileUtil.makeDir(FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Mic"));
                }
                if (ChathomeActivity.this.textview2.getText().toString().equals("RERECORD")) {
                    ChathomeActivity.this._IsExistDelete(ChathomeActivity.this.Audio);
                } else {

                }
                ChathomeActivity.this.Audio = FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Mic/").concat(new SimpleDateFormat("hh_mm_EEE_d_MMM").format(ChathomeActivity.this.cal1.getTime()).concat("_".concat(new DecimalFormat("0000").format(ChathomeActivity.this.ran).concat(".mp3"))));
                ChathomeActivity.this.rec = new MediaRecorder();
                ChathomeActivity.this.rec.setAudioSource(MediaRecorder.AudioSource.MIC);
                ChathomeActivity.this.rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                ChathomeActivity.this.rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                ChathomeActivity.this.rec.setOutputFile(ChathomeActivity.this.Audio);
                try {
                    ChathomeActivity.this.rec.prepare();
                    ChathomeActivity.this.rec.start();
                } catch (final Exception ignored) {
                }
                ChathomeActivity.this.n = 0;
                ChathomeActivity.this.n2_min = 0;
                ChathomeActivity.this.n3_hrs = 0;
                ChathomeActivity.this.textview4.setText("00:00");

                ChathomeActivity.this.t1 = new TimerTask() {
                    @Override
                    public void run() {
                        ChathomeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ChathomeActivity.this.n++;
                                if (ChathomeActivity.this.n > 59) {
                                    ChathomeActivity.this.n = 0;
                                    ChathomeActivity.this.n2_min++;
                                }
                                if (ChathomeActivity.this.n2_min > 59) {
                                    ChathomeActivity.this.n2_min = 0;
                                    ChathomeActivity.this.n3_hrs++;
                                }
                                ChathomeActivity.this.textview4.setText(new DecimalFormat("00").format(ChathomeActivity.this.n2_min).concat(":".concat(new DecimalFormat("00").format(ChathomeActivity.this.n))));
                            }
                        });
                    }
                };
                ChathomeActivity.this._timer.scheduleAtFixedRate(ChathomeActivity.this.t1, 1000, 900);

            }
        });

        this.textview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                if (ChathomeActivity.this.recordbool) {
                    ChathomeActivity.this.recordbool = false;
                    try {
                        ChathomeActivity.this.rec.stop();
                        ChathomeActivity.this.rec.release();
                        ChathomeActivity.this.rec = null;
                    } catch (final Exception ignored) {
                    }
                    ChathomeActivity.this.t1.cancel();
                } else {

                }
                ChathomeActivity.this.textview2.setText("RECORD");
                ChathomeActivity.this.textview4.setText("00:00");
                ChathomeActivity.this._IsExistDelete(ChathomeActivity.this.Audio);
                ChathomeActivity.this._Anime("voicevisible", ChathomeActivity.this.linear22);
                ChathomeActivity.this.linear40.setVisibility(View.GONE);
                ChathomeActivity.this.linear11.setVisibility(View.VISIBLE);
                ChathomeActivity.this.linear23.setVisibility(View.VISIBLE);
                ChathomeActivity.this.edittext1.requestFocus();
            }
        });

        this.linear43.setOnClickListener(_view -> {
            if (this.recordbool) {
                this.textview2.setText("RECORD");
                this.recordbool = false;
                try {
                    this.rec.stop();
                    this.rec.release();
                    this.rec = null;
                } catch (final Exception e) {
                }
                this.t1.cancel();
                this.l4.playAnimation();
                this._Anime("voicevisible", this.linear22);
                this.linear41.setVisibility(View.GONE);
                this.after = new TimerTask() {
                    @Override
                    public void run() {
                        ChathomeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ChathomeActivity.this._Anime("voicevisible", ChathomeActivity.this.linear22);
                                ChathomeActivity.this.linear40.setVisibility(View.GONE);
                                ChathomeActivity.this.linear11.setVisibility(View.VISIBLE);
                                ChathomeActivity.this.linear23.setVisibility(View.VISIBLE);
                                ChathomeActivity.this._msgDBAudio(ChathomeActivity.this.textview4.getText().toString(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), "3", ChathomeActivity.this.Audio, "");
                                ChathomeActivity.this.tmpstrmsg = ChathomeActivity.this.textview4.getText().toString();
                                ChathomeActivity.this.textview4.setText("00:00");

                                ChathomeActivity.this.type = 3;
                                ChathomeActivity.this._ChatedNow();
                            }
                        });
                    }
                };
                this._timer.schedule(this.after, 1400);
            }
        });

        this.l7.setOnClickListener(v -> {

            this.fpic.setClass(this.getApplicationContext(), MultiPickerActivity.class);
            this.fpic.putExtra("UID", this.getIntent().getStringExtra("UID"));
            this.fpic.putExtra("multiple_images", "true");
            this.fpic.putExtra("files", "img");
            this.startActivity(this.fpic);
            if (false) {
                if (this.filesVisiblebool) {
                    this.filesVisiblebool = false;
                    this._Anime("filesvi", this.linear22);
                    this.linear24.setVisibility(View.VISIBLE);
//						lottie4.playAnimation();
//						lottie6.playAnimation();
//						lottie9.playAnimation();
//						lottie10.playAnimation();
//						lottie15.playAnimation();
                } else {
                    this.filesVisiblebool = true;
                    this._Anime("filesgo", this.linear22);
                    this.linear24.setVisibility(View.GONE);
                }
            }
            this._closeBottom();
        });

        this.l8.setOnClickListener(v -> {

            this.fpic.setClass(this.getApplicationContext(), MultiPickerActivity.class);
            this.fpic.putExtra("UID", this.getIntent().getStringExtra("UID"));
            this.fpic.putExtra("multiple_images", "true");
            this.fpic.putExtra("files", "vid");
            this.startActivity(this.fpic);
            if (false) {
                if (this.filesVisiblebool) {
                    this.filesVisiblebool = false;
                    this._Anime("filesvi", this.linear22);
                    this.linear24.setVisibility(View.VISIBLE);
//						lottie4.playAnimation();
//						lottie6.playAnimation();
//						lottie9.playAnimation();
//						lottie10.playAnimation();
//						lottie15.playAnimation();
                } else {
                    this.filesVisiblebool = true;
                    this._Anime("filesgo", this.linear22);
                    this.linear24.setVisibility(View.GONE);
                }
            }
            this._closeBottom();
        });

        this.linear18.setOnClickListener(_view -> {
            this._openBottom();
            this.l7.playAnimation();
            this.l8.playAnimation();
        });
        this.br1t3.setOnClickListener(v -> {
            this._closeBottom();
        });


        this.edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence _param1, final int _param2, final int _param3, final int _param4) {
                String _charSeq = _param1.toString();
                ChathomeActivity.this.list.edit().putString("bottom", _charSeq).apply();
                if ("".equals(_charSeq)) {
                    if (ChathomeActivity.this.micg) {
                        ChathomeActivity.this._Anime("text11", ChathomeActivity.this.linear22);
                        ChathomeActivity.this.linear14.setVisibility(View.GONE);
                        ChathomeActivity.this.linear20.setVisibility(View.VISIBLE);
                        ChathomeActivity.this.micg = false;
                    }
                    ChathomeActivity.this.ChatRef.edit().putString("sts", "").apply();
                } else {
                    if (!ChathomeActivity.this.micg) {
                        ChathomeActivity.this._Anime("text21", ChathomeActivity.this.linear22);
                        ChathomeActivity.this.linear14.setVisibility(View.VISIBLE);
                        ChathomeActivity.this.linear20.setVisibility(View.GONE);
                        ChathomeActivity.this.micg = true;
                    }//edittext1

                    ChathomeActivity.this.txtmsg = _charSeq.trim();
                    if (ChathomeActivity.this.txtmsg.length() > 50) {
                        ChathomeActivity.this.txtmsg = "..." + ChathomeActivity.this.txtmsg.substring(ChathomeActivity.this.txtmsg.length() - 45);
                    }


                    ChathomeActivity.this.ChatRef.edit().putString("ChatText", ChathomeActivity.this.txtmsg).apply();
                    ChathomeActivity.this.ChatRef.edit().putString("sts", ChathomeActivity.this.getIntent().getStringExtra("UID")).apply();
                }
            }

            @Override
            public void beforeTextChanged(final CharSequence _param1, final int _param2, final int _param3, final int _param4) {

            }

            @Override
            public void afterTextChanged(final Editable _param1) {

            }
        });

        this.linear21.setOnClickListener(_view -> {
            this.linear41.setVisibility(View.VISIBLE);
            this._Anime("voicegone", this.linear22);
            this.linear23.setVisibility(View.GONE);
            this.linear40.setVisibility(View.VISIBLE);
            this.linear11.setVisibility(View.GONE);
            this.linear47.setVisibility(View.GONE);
            this.linear42.setVisibility(View.GONE);
        });


        this.l1.setOnClickListener(_view -> {
            this.text = this.edittext1.getText().toString().trim();
            this.tmpstrmsg = this.text;
            this.edittext1.setText("");
            if ("".equals(this.text)) {

            } else {
                this.type = 1;
                this._ChatedNow();
                this._msgDB(this.text, "me");

                if (this.msgMark) {
                    this.msgMark = false;
                    this._Anime("filesgo", this.linear22);
                    this.linear56.setVisibility(View.GONE);
                }


            }

        });

        this._dbSendProfile_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                assert _childValue != null;

                boolean pb = false;

                if (!ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.getIntent().getStringExtra("UID") + "n", "").equals(Objects.requireNonNull(_childValue.get("NAME")).toString())) {
                    ChathomeActivity.this.textviewName.setText(Objects.requireNonNull(_childValue.get("NAME")).toString());
                    pb = true;
                }

                if (!ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.getIntent().getStringExtra("UID") + "i", "").equals(Objects.requireNonNull(_childValue.get("URL")).toString())) {
                    final String s = Objects.requireNonNull(_childValue.get("URL")).toString();
                    Glide.with(ChathomeActivity.this.getApplicationContext()).load(Uri.parse(s)).placeholder(R.drawable.default_profile).into(ChathomeActivity.this.circleimageview1);
                    pb = true;
                }
                if (Objects.requireNonNull(_childValue).containsKey("UI")) {
                    final String s = Objects.requireNonNull(_childValue.get("UI")).toString();
                    ChathomeActivity.this.ChatList.edit().putString(ChathomeActivity.this.getIntent().getStringExtra("UID") + "ui", s).apply();

                } else {
                    ChathomeActivity.this.ChatList.edit().putString(ChathomeActivity.this.getIntent().getStringExtra("UID") + "ui", ChathomeActivity.this.jay.getdefaultTheme()).apply();
                }
                if (pb) {
                    new getData(ChathomeActivity.this.getIntent().getStringExtra("UID"), ChathomeActivity.this.getApplicationContext(), ChathomeActivity.this.ChatList);
                }

                if (Objects.requireNonNull(_childValue).containsKey("ChatWith")) {
                    final String s = Objects.requireNonNull(_childValue.get("ChatWith")).toString();
                    final String uidt = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    if (s.equals(uidt)) {
                        if (Objects.requireNonNull(_childValue).containsKey("ChatText")) {
                            final String txt = Objects.requireNonNull(_childValue.get("ChatText")).toString();
                            if (txt.equals("")) {
                                ChathomeActivity.this.hidepad();
                                ChathomeActivity.this.textpad.setText("");
                            } else {
                                ChathomeActivity.this.showpad();
                                ChathomeActivity.this.textpad.setText(txt);
                            }
                        } else {
                            ChathomeActivity.this.hidepad();
                            ChathomeActivity.this.textpad.setText("");
                        }
                    } else {
                        ChathomeActivity.this.hidepad();
                        ChathomeActivity.this.textpad.setText("");
                    }
                }


                if (Objects.requireNonNull(_childValue).containsKey("SEEN")) {
                    ChathomeActivity.this.current = Objects.requireNonNull(_childValue.get("SEEN")).toString();
                    if (_childValue.containsKey("STS")) {
                        ChathomeActivity.this.currentSTS = Objects.requireNonNull(_childValue.get("STS")).toString();
                    } else {
                        ChathomeActivity.this.currentSTS = "";
                    }
                    ChathomeActivity.this.textviewOnOff.setVisibility(View.VISIBLE);
                } else {
                    ChathomeActivity.this.textviewOnOff.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                assert _childValue != null;


                boolean pb = false;

                if (!ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.getIntent().getStringExtra("UID") + "n", "").equals(Objects.requireNonNull(_childValue.get("NAME")).toString())) {
                    ChathomeActivity.this.textviewName.setText(Objects.requireNonNull(_childValue.get("NAME")).toString());
                    pb = true;
                }

                if (!ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.getIntent().getStringExtra("UID") + "i", "").equals(Objects.requireNonNull(_childValue.get("URL")).toString())) {
                    final String s = Objects.requireNonNull(_childValue.get("URL")).toString();
                    Glide.with(ChathomeActivity.this.getApplicationContext()).load(Uri.parse(s)).placeholder(R.drawable.default_profile).into(ChathomeActivity.this.circleimageview1);
                    pb = true;
                }
                if (pb) {
                    new getData(ChathomeActivity.this.getIntent().getStringExtra("UID"), ChathomeActivity.this.getApplicationContext(), ChathomeActivity.this.ChatList);
                    System.out.println("triggered2");
                }

                if (Objects.requireNonNull(_childValue).containsKey("SEEN")) {
                    ChathomeActivity.this.current = Objects.requireNonNull(_childValue.get("SEEN")).toString();
                    if (_childValue.containsKey("STS")) {
                        ChathomeActivity.this.currentSTS = Objects.requireNonNull(_childValue.get("STS")).toString();
                    } else {
                        ChathomeActivity.this.currentSTS = "";
                    }
                    ChathomeActivity.this.textviewOnOff.setVisibility(View.VISIBLE);
                    if (ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.lseen, "").equals("")) {
                        ChathomeActivity.this.ChatList.edit().putString(ChathomeActivity.this.lseen, Objects.requireNonNull(_childValue.get("SEEN")).toString()).apply();
                    } else {

                    }

                    if (Objects.requireNonNull(_childValue).containsKey("STS")) {
                        final String s = Objects.requireNonNull(_childValue.get("STS")).toString();
                        final String uidt = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        if (s.equals(uidt)) {
                            if (Objects.requireNonNull(_childValue).containsKey("ChatText")) {
                                final String txt = Objects.requireNonNull(_childValue.get("ChatText")).toString();
                                if (txt.equals("")) {
                                    ChathomeActivity.this.hidepad();
                                    ChathomeActivity.this._Anime("txt", ChathomeActivity.this.linearanipad);
                                    ChathomeActivity.this.textpad.setText("");
                                } else {
                                    ChathomeActivity.this.showpad();
                                    ChathomeActivity.this._Anime("txt", ChathomeActivity.this.linearanipad);
                                    ChathomeActivity.this.textpad.setText(txt);
                                }
                            } else {
                                ChathomeActivity.this.hidepad();
                                ChathomeActivity.this._Anime("txt", ChathomeActivity.this.linearanipad);
                                ChathomeActivity.this.textpad.setText("");
                            }
                        } else {
                            ChathomeActivity.this.hidepad();
                            ChathomeActivity.this._Anime("txt", ChathomeActivity.this.linearanipad);
                            ChathomeActivity.this.textpad.setText("");
                        }
                    }

                } else {
                    ChathomeActivity.this.textviewOnOff.setVisibility(View.GONE);
                    ChathomeActivity.this.hidepad();
                    ChathomeActivity.this._Anime("txt", ChathomeActivity.this.linearanipad);
                    ChathomeActivity.this.textpad.setText("");
                }

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
        //dbSendProfile.addChildEventListener(_dbSendProfile_child_listener);
    }

    //Game
    public void sendGameMsg(final String msg) {
        this.map = new HashMap<>();
        this.msgC = Calendar.getInstance();
        final int mil = (int) this.msgC.getTimeInMillis();
        this.map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))).concat(String.valueOf(this.msgC.getTimeInMillis()))));
        this.map.put("ms", String.valueOf(mil));
        this.map.put("msg", "requesting to play");
        this.map.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.map.put("stage", msg);
        this.map.put("typ", "cus");
        this.map.put("cat", "ttt");
        this.map.put("gid", "escape");
        this._sendToDB(this.map, Objects.requireNonNull(this.map.get("id")).toString());
        this.map.clear();
    }

    public void initGame() {

        this.linear58.setOnClickListener(v -> {
            if (this.isopen3) {
                this._closeGame();
            } else if (this.textviewOnOff.getText().toString().equals("Online")) {
                this._openGame();
            } else {
                Toast.makeText(this, "The User was Not in Online", Toast.LENGTH_SHORT).show();
            }

        });

        this.reqtext2.setOnClickListener(v -> {
            this.sendGameMsg("deny");
            this._closeGame();
            this.textpad2.setVisibility(View.VISIBLE);
            this.requestlinear.setVisibility(View.GONE);
        });
        this.reqtext3.setOnClickListener(v -> {
            this.sendGameMsg("allow");
            this._closeGame();
            this.requestlinear.setVisibility(View.GONE);
            this._timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ChathomeActivity.this.runOnUiThread(() -> {

                        ChathomeActivity.this.sendGameMsg("beginGame");
                        ChathomeActivity.this._openGame();
                    });
                }
            },1000);

        });


        this.textpad2.setOnClickListener(v -> {
            if(this.tebool) {
                this.tebool = false;
                this.sendGameMsg("req");
                this.textpad2.setText("Request was Sent 3");
                this.timei = 3;
                this.textpad2.setAlpha(0.6f);
                this.after2 = new TimerTask() {
                    @Override
                    public void run() {
                        ChathomeActivity.this.runOnUiThread(() -> {
                            ChathomeActivity.this.textpad2.setText("Request was Sent " + ChathomeActivity.this.timei);
                            ChathomeActivity.this.timei--;
                        });
                    }
                };
                this._timer.scheduleAtFixedRate(this.after2, 0, 1000);
                this.after3 = new TimerTask() {
                    @Override
                    public void run() {
                        ChathomeActivity.this.runOnUiThread(() -> {
                            ChathomeActivity.this.after2.cancel();
                            ChathomeActivity.this.textpad2.setText("Send Request Again");
                            ChathomeActivity.this.textpad2.setAlpha(1.0f);
                            ChathomeActivity.this.tebool = true;
                        });
                    }
                };
                this._timer.schedule(this.after3, 3500);
            }
        });
    }


    private final ObjectAnimator openAnim3 = new ObjectAnimator();
    private final ObjectAnimator closeAnim3 = new ObjectAnimator();

    private boolean isopen3;

    public void _openGame() {
        this.isopen3 = true;
        this.openAnim3.setTarget(this.linearpad2);
        this.openAnim3.setPropertyName("translationX");
        this.openAnim3.setFloatValues((float) (0));
        this.openAnim3.setDuration(500);
        this.openAnim3.start();
        this.closeAnim3.setTarget(this.linearpad2);
        this.closeAnim3.setPropertyName("alpha");
        this.closeAnim3.setFloatValues(1);
        this.closeAnim3.setDuration(500);
        this.closeAnim3.start();

    }

    public void _closeGame() {
        this.isopen3 = false;
        this.closeAnim3.setTarget(this.linearpad2);
        this.closeAnim3.setPropertyName("translationX");
        this.closeAnim3.setFloatValues(SketchwareUtil.getDip(this.getApplicationContext(), -450));
        this.closeAnim3.setDuration(500);
        this.closeAnim3.start();
        this.openAnim3.setTarget(this.linearpad2);
        this.openAnim3.setPropertyName("alpha");
        this.openAnim3.setFloatValues((float) (0));
        this.openAnim3.setDuration(500);
        this.openAnim3.start();
    }

    private final ObjectAnimator openAnim2 = new ObjectAnimator();
    private final ObjectAnimator closeAnim2 = new ObjectAnimator();

    private boolean isopen2;

    public void _openBottom() {
        this.isopen2 = true;
        this.openAnim2.setTarget(this.bottom);
        this.openAnim2.setPropertyName("translationY");
        this.openAnim2.setFloatValues(SketchwareUtil.getDip(this.getApplicationContext(), -120));
        this.openAnim2.setDuration(500);
        this.openAnim2.start();
    }

    public void _closeBottom() {
        this.isopen2 = false;
        this.closeAnim2.setTarget(this.bottom);
        this.closeAnim2.setPropertyName("translationY");
        this.closeAnim2.setFloatValues((float) (0));
        this.closeAnim2.setDuration(500);
        this.closeAnim2.start();
    }

    private void initializeLogic() {
        this.pause = false;
        this.upLoad = false;
        this.micg = false;
        this.uploading = false;
        this.ivCrNew = false;
        this.upd = false;
        this.msgMark = false;
        this.spbts.registerOnSharedPreferenceChangeListener(this.listChangeListener);
        this._OnCrt();
        this._Bg();
        this._ui();
        this._changeActivityFont("product_more_block");
        this.linear56.setVisibility(View.GONE);
        this.edittext1.requestFocus();
        this.linear14.setVisibility(View.GONE);
//        linear58.setVisibility(View.GONE);
        this.asp.edit().putString("aschathome", "r").apply();
    }
    //pads

    private final ObjectAnimator openAnim = new ObjectAnimator();
    private final ObjectAnimator closeAnim = new ObjectAnimator();
    private boolean ispadopen;

    private void showpad() {
        this.ispadopen = true;
        this.openAnim.setTarget(this.linearpad);
        this.openAnim.setPropertyName("translationX");
        this.openAnim.setFloatValues(SketchwareUtil.getDip(this.getApplicationContext(), 0));
        this.openAnim.setDuration(500);
        this.openAnim.start();
    }

    private void hidepad() {
        this.textpad.setText("");
        this.ispadopen = false;
        this.closeAnim.setTarget(this.linearpad);
        this.closeAnim.setPropertyName("translationX");
        this.closeAnim.setFloatValues(SketchwareUtil.getDip(this.getApplicationContext(), -150));
        this.closeAnim.setDuration(500);
        this.closeAnim.start();
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    public static class MyFragmentAdapter extends FragmentStateAdapter {

        public MyFragmentAdapter(final FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment createFragment(final int _position) {
            if (0 == _position) {
                return new ChatFragmentActivity();
            }
            if (1 == _position) {
                return new ChatstatusFragmentActivity();
            }
            return null;
        }
    }


    private HashMap<String,Object> j;
    @Override
    public void onStart() {
        super.onStart();
        if (((this.getIntent().getStringExtra("UID")).equals(this.pathssp.getString("uid", ""))) && (!"".equals(this.pathssp.getString("uploadlist", "")))) {
            this.imgvidpaths = new Gson().fromJson(this.pathssp.getString("uploadlist", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            this.pathssp.edit().putString("uploadlist", "").apply();
            this.isInJob = 0 != this.uploadlm.size();
            this.tmpVar = String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)));
            this.total = this.imgvidpaths.size();
            for (int n = 0; n < this.imgvidpaths.size(); n++) {
                this.tmap2 = this.imgvidpaths.get(n);
                this._msgDBimgvid(Objects.requireNonNull(this.tmap2.get("text")).toString(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), Objects.requireNonNull(this.tmap2.get("type")).toString(), Objects.requireNonNull(this.tmap2.get("path")).toString(), this.tmpVar,this.pathssp.getString("media", ""),this.imgvidpaths.size());
            }

            this._sendToDB(this._getMsgByID(this.asp.getString(this.tmpVar, "")), this.asp.getString(this.tmpVar, ""));
            this.type = 999;
            this._ChatedNow();
            this.imgvidpaths.clear();
            for (int i = 0; i < this.uploadlm.size(); i++) {
                this._UploadTask(Objects.requireNonNull(this.uploadlm.get(i).get("path")).toString(), Objects.requireNonNull(this.uploadlm.get(i).get("id")).toString(), Objects.requireNonNull(this.uploadlm.get(i).get("nid")).toString(), Objects.requireNonNull(this.uploadlm.get(i).get("type")).toString(), this.uploadlm, i);
            }
            /*
            if (isInJob) {

            } else {
                _UploadTask(Objects.requireNonNull(uploadlm.get(0).get("path")).toString(), Objects.requireNonNull(uploadlm.get(0).get("id")).toString(), Objects.requireNonNull(uploadlm.get(0).get("nid")).toString(), Objects.requireNonNull(uploadlm.get(0).get("type")).toString(), uploadlm,);
            }
             */
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.asp.edit().putString("aschathome", "d").apply();
        this.spbts.unregisterOnSharedPreferenceChangeListener(this.listChangeListener);
        this.onofftim.cancel();
        this.ChatRef.edit().putString("sts", "").apply();
        this.dbSendProfile.removeEventListener(this._dbSendProfile_child_listener);
    }

    @Override
    public void onBackPressed() {
        if (this.asp.getString("ashome", "").equals("") || this.asp.getString("ashome", "").equals("s")) {
            final Intent backint = new Intent();
            backint.setClass(this.getApplicationContext(), HomeActivity.class);
            this.startActivity(backint);
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.asp.edit().putString("aschathome", "r").apply();
        this.objanim.setTarget(this.imageview1);
        this.objanim.setPropertyName("rotation");
        if (this.viewpager1.getCurrentItem() == 1) {
            this.objanim.setFloatValues((float) (0));
        } else {
            this.objanim.setFloatValues((float) (180));
        }
        this.objanim.setDuration(400);
        this.objanim.setRepeatCount(0);
        this.objanim.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.asp.edit().putString("aschathome", "p").apply();
        if (this.objanim.isRunning()) {
            this.objanim.cancel();
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


    public boolean isServiceRunning(@NonNull final Context c, final Class<?> serviceClass) {
        final ActivityManager activityManager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);

        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (final ActivityManager.RunningServiceInfo runningServiceInfo : services) {

            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }

        }
        return false;
    }

    public void _OnCrt() {

        if (this.isServiceRunning(this, DbmsgActivity.class)) {
//            Toast.makeText(this, "running", Toast.LENGTH_SHORT).show();
        } else {
            final Intent service = new Intent(this, DbmsgActivity.class);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                getApplicationContext().startForegroundService(service);
//            } else {
                getApplicationContext().startService(service);
//            }
        }
        if (this.isServiceRunning(this, KeepOnlineService.class)) {
//            Toast.makeText(this, "running", Toast.LENGTH_SHORT).show();
        } else {
            final Intent service = new Intent(this, KeepOnlineService.class);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                getApplicationContext().startForegroundService(service);
//            } else {
                getApplicationContext().startService(service);
//            }
        }
        final String pn = this.ChatList.getString(this.getIntent().getStringExtra("UID") + "p", "");
        if (this.getIntent().hasExtra("Request")){
            if(this.getIntent().getStringExtra("Request").equals("true")){
                this.textpad2.setVisibility(View.GONE);
                this.requestlinear.setVisibility(View.VISIBLE);
                this._openGame();
            }
        }

        //new getData(getIntent().getStringExtra("UID"), this, ChatList);
        final int id = Integer.parseInt(pn.substring(5));

        this.startService(new Intent(this.getApplicationContext(), DbmsgActivity.class));
        final NotificationManagerCompat nclear = NotificationManagerCompat.from(this);
        nclear.cancel(id);
        this.asp.edit().putString(this.getIntent().getStringExtra("UID") + "tempData", "").apply();
        this.StartUpTime = System.currentTimeMillis();
        //PHONE.edit().putString("PHONE", getIntent().getStringExtra("PHONE")).apply();
        this.PHONE.edit().putString("UID", this.getIntent().getStringExtra("UID")).apply();
        if (this.asp.getString("aschathomecurrent", "").equals(this.getIntent().getStringExtra("UID")) && (this.asp.getString("aschathome", "").equals("p") || this.asp.getString("aschathome", "").equals("r"))) {
            this.spbts.edit().putLong("stopOLD", this.StartUpTime).apply();
        }
        this.asp.edit().putString("aschathomecurrent", this.getIntent().getStringExtra("UID")).apply();
        this.dbSend = this._firebase.getReference("chat/".concat(this.getIntent().getStringExtra("UID").concat("/notify")));
        this.dbMy = this._firebase.getReference("chat/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat("/notify")));
        this.dbSendProfile.removeEventListener(this._dbSendProfile_child_listener);
        this.dbSendProfile = this._firebase.getReference("Profile/uid/".concat(this.getIntent().getStringExtra("UID")));
        this.dbSendProfile.addChildEventListener(this._dbSendProfile_child_listener);
        this.dbGal = this._firebase.getReference("PIMGS/uid/".concat(this.getIntent().getStringExtra("UID")));

        this.viewpager1.setAdapter(new MyFragmentAdapter(this));

        this.filesVisiblebool = true;
//		linear24.setVisibility(View.GONE);
        this.linear40.setVisibility(View.GONE);
        this.loti = new TimerTask() {
            @Override
            public void run() {
                ChathomeActivity.this.runOnUiThread(() -> ChathomeActivity.this.l1.pauseAnimation());
            }
        };
        this._timer.schedule(this.loti, 100);
        this.recordbool = false;
        this.lseen = this.getIntent().getStringExtra("UID").concat("SEEN");
        this.textviewName.setText(this.ChatList.getString(this.getIntent().getStringExtra("UID").concat("n"), ""));
        final String s = this.ChatList.getString(this.getIntent().getStringExtra("UID").concat("i"), "");
        if (s.equals("")) {
            Glide.with(this.getApplicationContext()).load(R.drawable.default_profile).into(this.circleimageview1);
        } else {
            Glide.with(this.getApplicationContext()).load(Uri.parse(s)).placeholder(R.drawable.default_profile).into(this.circleimageview1);
        }
        this.onofftim = new TimerTask() {
            @Override
            public void run() {
                ChathomeActivity.this.runOnUiThread(() -> {
                    if (SketchwareUtil.isConnected(ChathomeActivity.this.getApplicationContext())) {
                        if (ChathomeActivity.this.current.equals("")) {
                            ChathomeActivity.this.textviewOnOff.setVisibility(View.GONE);
                        } else {
                            if (ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.lseen, "").equals("")) {
                                ChathomeActivity.this.textviewOnOff.setVisibility(View.GONE);
                            } else {
                                if (ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.lseen, "").equals(ChathomeActivity.this.current)) {
                                    ChathomeActivity.this.tmptimer = new TimerTask() {
                                        @Override
                                        public void run() {
                                            ChathomeActivity.this.runOnUiThread(() -> {
                                                if (ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.lseen, "").equals(ChathomeActivity.this.current)) {
                                                    ChathomeActivity.this._showlastseen();
                                                    ChathomeActivity.this.textviewOnOff.setVisibility(View.VISIBLE);
                                                } else {
                                                    if (ChathomeActivity.this.currentSTS.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                        ChathomeActivity.this.textviewOnOff.setText("Typing...");
                                                        ChathomeActivity.this.textviewOnOff.setVisibility(View.VISIBLE);
                                                        ChathomeActivity.this.ChatList.edit().putString(ChathomeActivity.this.lseen, ChathomeActivity.this.current).apply();
                                                    } else {
                                                        ChathomeActivity.this.textviewOnOff.setText("Online");
                                                        ChathomeActivity.this.textviewOnOff.setVisibility(View.VISIBLE);
                                                        ChathomeActivity.this.ChatList.edit().putString(ChathomeActivity.this.lseen, ChathomeActivity.this.current).apply();
                                                    }
                                                }
                                            });
                                        }
                                    };
                                    ChathomeActivity.this._timer.schedule(ChathomeActivity.this.tmptimer, 500);
                                } else {
                                    if (ChathomeActivity.this.currentSTS.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        ChathomeActivity.this.textviewOnOff.setText("Typing...");
                                        ChathomeActivity.this.textviewOnOff.setVisibility(View.VISIBLE);
                                        ChathomeActivity.this.ChatList.edit().putString(ChathomeActivity.this.lseen, ChathomeActivity.this.current).apply();
                                    } else {
                                        ChathomeActivity.this.textviewOnOff.setText("Online");
                                        ChathomeActivity.this.textviewOnOff.setVisibility(View.VISIBLE);
                                        ChathomeActivity.this.ChatList.edit().putString(ChathomeActivity.this.lseen, ChathomeActivity.this.current).apply();
                                    }
                                }
                            }
                        }
                    } else {
                        ChathomeActivity.this.textviewOnOff.setVisibility(View.GONE);
                    }
                    if (ChathomeActivity.this.upd) {
                        ChathomeActivity.this._notifyUpdate("r");
                        ChathomeActivity.this.upd = false;
                    } else {

                    }
                });
            }
        };


        this._timer.scheduleAtFixedRate(this.onofftim, 1, 1100);
        this.l6.setVisibility(View.GONE);
        final DocumentReference ds = this.ffs.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ds.collection("Friends").whereEqualTo("id", this.getIntent().getStringExtra("UID")).count().get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final AggregateQuerySnapshot snapshot = task.getResult();
                if(snapshot.getCount()==0){
                    this.l6.setVisibility(View.VISIBLE);
                    this.l6.setOnClickListener(v -> {

                        this.msgC = Calendar.getInstance();
                        final int mil = (int) this.msgC.getTimeInMillis();
                        final int nextreq = Integer.parseInt(this.ChatRef.getString("lastreq" + this.getIntent().getStringExtra("UID"), ""+mil));

                        if (nextreq<=mil) {
                            this.ChatRef.edit().putString("lastreq"+ this.getIntent().getStringExtra("UID"),String.valueOf(mil+100000)).apply();
                            this.map = new HashMap<>();
                            this.map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))).concat(String.valueOf(this.msgC.getTimeInMillis()))));
                            this.map.put("ms", String.valueOf(mil));
                            this.map.put("msg", "friend request");
                            this.map.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            this.map.put("stage", "0");
                            this.map.put("typ", "cus");
                            this.map.put("cat", "frdreq");
                            this.map.put("gid", "escape");
                            this._sendToDB(this.map, Objects.requireNonNull(this.map.get("id")).toString());
                            this.l6.reverseAnimationSpeed();
                            Toast.makeText(this, "Friend Request Sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Friend Request Already Sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    this.l6.setVisibility(View.GONE);
                }
            } else {
                System.out.println("Count failed: "+ task.getException());
            }
        });


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

    //Ui
    public void _ui() {

        this._SetBackground(this.gamepad, 30, 20, this.jay.col(0, 3), false);
        this._SetBackground(this.textpad2, 30, 20, this.jay.col(0, 1), false);
        this._SetBackground(this.reqtext3, 30, 20, this.jay.col(0, 1), false);
        this._SetBackground(this.reqtext2, 30, 20, this.jay.col(0, 1), false);

        this._SetBackground(this.linearpad, 30, 20, this.jay.col(0, 3), false);
        this._SetBackground(this.bottom, 30, 20, this.jay.col(0, 3), false);
//        this._SetBackground(this.br1t1, 30, 20, this.jay.col(0, 1), true);
//        this._SetBackground(this.br1t2, 30, 20, this.jay.col(0, 1), true);
        this._SetBackground(this.br1t3, 30, 20, this.jay.col(0, 1), true);

        this._SetBackground(this.linear9, 40, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.linear3, 0, 15, this.jay.col(0, 3), true);
        this._SetBackground(this.linear14, 50, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.linear17, 50, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.linear20, 50, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.linear42, 50, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.textview2, 50, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.textview3, 50, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.linear49, 50, 0, this.jay.col(0, 1), true);
        this._SetBackground(this.linear56, 50, 0, "#65000000", true);

        this.textviewName.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.textviewOnOff.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.edittext1.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.textview2.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.textview3.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.textview4.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.textpad.setTextColor(Color.parseColor(this.jay.col(1, 1)));
//        this.br1t1.setTextColor(Color.parseColor(this.jay.col(1, 1)));
//        this.br1t2.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.br1t3.setTextColor(Color.parseColor(this.jay.col(1, 1)));

        this.imageview1.setColorFilter(Color.parseColor(this.jay.col(0, 1)));
        this.l6.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                frameInfo -> new PorterDuffColorFilter(Color.parseColor(this.jay.col(0, 1)), PorterDuff.Mode.SRC_ATOP)
        );

    }


    public void _msgDB(@NonNull String _msg, String _by) {

        this.msg.edit().putString(this.PHONE.getString("UID", ""), String.valueOf((long) (Double.parseDouble(this.msg.getString(this.PHONE.getString("UID", ""), "")) + 1))).apply();
        this.msgC = Calendar.getInstance();
        this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("ms")), String.valueOf(this.msgC.getTimeInMillis())).apply();
        this.map = new HashMap<>();
        this.msgC = Calendar.getInstance();
        this.map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))).concat(String.valueOf(this.msgC.getTimeInMillis()))));
        this.map.put("ms", this.msg.getString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("ms")), ""));
        if (200 < _msg.length()) {
            this.map.put("msg", _msg.substring(0, 150));
            this.map.put("mbool", "true");
            this.map.put("more", _msg);
        } else {
            this.map.put("msg", _msg);
        }
        this.map.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (this.msgMark) {
            this.map.put("mark", this.ID);
        } else {

        }
        this.map.put("stage", "1");
        this.map.put("typ", "1");
        this.map.put("cmap", this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")));
        this.TmpStr = new Gson().toJson(this.map);
        this.msg.edit().putString(Objects.requireNonNull(this.map.get("id")).toString(), this.TmpStr).apply();
        this.msg.edit().putString(Objects.requireNonNull(this.map.get("id")).toString().concat("pos"), this.msg.getString(this.PHONE.getString("UID", ""), "")).apply();
        this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")), this.TmpStr).apply();
        this._notifyUpdate("rs");
        this._sendToDB(this.map, Objects.requireNonNull(this.map.get("id")).toString());
        this.map.clear();

    }


    public void _notifyUpdate(String _Key) {
        if ("rs".equals(_Key)) {
            this.list.edit().putString("a", String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
        } else if ("sr".equals(_Key)) {
            this.list.edit().putString("a", String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
        } else {
            for (int n = 0; n < _Key.length(); n++) {
                this.list.edit().putString(_Key.substring(n, n + 1), String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
            }
        }

    }


    public void _Anime(String _Sp, View _Linear) {
        if ("".equals(this.asp.getString(_Sp, ""))) {
            this.asp.edit().putString(_Sp, "true").apply();
            final AutoTransition autoTransition = new AutoTransition();
            autoTransition.setDuration(200);

            TransitionManager.beginDelayedTransition((LinearLayout) _Linear, autoTransition);
            try {
                this.at = new TimerTask() {
                    @Override
                    public void run() {
                        ChathomeActivity.this.runOnUiThread(() -> ChathomeActivity.this.asp.edit().putString(_Sp, "").apply());
                    }
                };
                this._timer.schedule(this.at, 350);
            } catch (final Exception e5) {
            }
        } else {

        }
    }


    public void _randomLetterGen(double _lenght, TextView _textview) {
        this.cal1 = Calendar.getInstance();
        this.ran = SketchwareUtil.getRandom(0, 9999);
        this.textview2.setText(new SimpleDateFormat("hh_mm_EEE_d_MMM").format(this.cal1.getTime()).concat("_".concat(new DecimalFormat("0000").format(this.ran).concat(".mp3"))));
    }





    public void _IsExistDelete(String _path) {
        if (!_path.equals("")) {
            if (FileUtil.isExistFile(_path)) {
                FileUtil.deleteFile(_path);
            }
        }
    }


    public void _msgDBimgvid(String _msg, String _by, String _type, String _tmpl, String _mainl, String media, int size) {
        if (_mainl.equals(this.asp.getString("prvIV", ""))) {
            this.merge = true;
        } else {
            this.merge = false;
            this.msg.edit().putString(this.PHONE.getString("UID", ""), String.valueOf((long) (Double.parseDouble(this.msg.getString(this.PHONE.getString("UID", ""), "")) + 1))).apply();
        }
        this.asp.edit().putString("prvIV", _mainl).apply();
        this.map = new HashMap<>();
        this.map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(this.msgC.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
        this.cal1 = Calendar.getInstance();
        this.map.put("ms", String.valueOf(this.cal1.getTimeInMillis()));
        if (200 < _msg.length()) {
            this.map.put("msg", _msg.substring(0, 190));
            this.map.put("mbool", "true");
            this.map.put("more", _msg);
        } else {
            this.map.put("msg", _msg);
        }
        this.map.put("by", _by);
        this.map.put("stage", "1");
        this.map.put("typ", _type);
        this.map.put("tmpl", _tmpl);
        this.map.put("mainl", "");
        this.map.put("cmap", this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")));
        if (this.merge) {
            this.bundleLM.clear();
            this.bundleLM = new Gson().fromJson(Objects.requireNonNull(this.Nmap.get("list")).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            this.msg.edit().putString(Objects.requireNonNull(this.map.get("id")).toString().concat("999"), String.valueOf((long) (this.bundleLM.size()))).apply();
            this.bundleLM.add(this.map);
            this.Nmap.put("list", new Gson().toJson(this.bundleLM));
            this.TmpStr = new Gson().toJson(this.Nmap);
        } else {
            this.bundleLM.clear();
            this.bundleLM.add(this.map);
            this.msgC = Calendar.getInstance();
            this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("ms")), String.valueOf(this.msgC.getTimeInMillis())).apply();
            this.Nmap = new HashMap<>();
            this.msg.edit().putString("TmpIDForImgVid", FirebaseAuth.getInstance().getCurrentUser().getUid().concat(String.valueOf(this.msgC.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))))).apply();
            this.asp.edit().putString(_mainl, this.msg.getString("TmpIDForImgVid", "")).apply();
            this.Nmap.put("id", this.msg.getString("TmpIDForImgVid", ""));
            this.Nmap.put("ms", this.msg.getString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("ms")), ""));
            this.Nmap.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
            this.Nmap.put("typ", "999");
            this.Nmap.put("media", media);
            this.Nmap.put("count", String.valueOf(size));
            this.Nmap.put("list", new Gson().toJson(this.bundleLM));
            this.Nmap.put("cmap", this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")));
            this.Nmap.put("stage", "1");
            this.TmpStr = new Gson().toJson(this.Nmap);
            this.msg.edit().putString(Objects.requireNonNull(this.map.get("id")).toString().concat("999"), "0").apply();
            this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("type")), "999").apply();
        }
        this.msg.edit().putString(this.msg.getString("TmpIDForImgVid", ""), this.TmpStr).apply();
        this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")), this.TmpStr).apply();
        this.msg.edit().putString(Objects.requireNonNull(this.map.get("id")).toString().concat("pos"), this.msg.getString(this.PHONE.getString("UID", ""), "")).apply();
        {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("id", Objects.requireNonNull(this.map.get("id")).toString());
            this.uploadlm.add(_item);
        }

        this.uploadlm.get(this.uploadlm.size() - 1).put("nid", Objects.requireNonNull(this.Nmap.get("id")).toString());
        this.uploadlm.get(this.uploadlm.size() - 1).put("path", _tmpl);
        this.uploadlm.get(this.uploadlm.size() - 1).put("type", _type);
        this.uploadlm.get(this.uploadlm.size() - 1).put("pos", this.msg.getString(Objects.requireNonNull(this.map.get("id")).toString().concat("999"), ""));
        this.uploadlm.get(this.uploadlm.size() - 1).put("cmap", this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")));
        this.map.clear();
        this._notifyUpdate("rs");
    }


    public void _sendToDB(HashMap<String, Object> _map, String _ID) {
        if (_map.containsKey("gid")) {
            this.dbSend.child(_ID).updateChildren(_map);
        } else {
            this.dbSend.child(_ID).updateChildren(_map).addOnSuccessListener(void_ -> {
                ChathomeActivity.this._updateMsg(_ID, "stage", "2");
                ChathomeActivity.this.upd = false;
                ChathomeActivity.this.t1 = new TimerTask() {
                    @Override
                    public void run() {
                        ChathomeActivity.this.runOnUiThread(() -> {
                            if ("".equals(ChathomeActivity.this.edittext1.getText().toString())) {
                                ChathomeActivity.this._notifyUpdate("r");
                            } else {
                                ChathomeActivity.this.upd = true;
                            }
                        });
                    }
                };
                ChathomeActivity.this._timer.schedule(ChathomeActivity.this.t1, 500);
            }).addOnFailureListener(exception -> {
                ChathomeActivity.this._updateMsg(_ID, "stage", "0");
                ChathomeActivity.this._notifyUpdate("r");
            }).addOnCanceledListener(() -> {
                ChathomeActivity.this._updateMsg(_ID, "stage", "0");
                ChathomeActivity.this._notifyUpdate("r");
            });
        }
    }


    public void _updateMsg(String _id, String _key, String _val) {
        this.tmap2 = new HashMap<>();
        this.tmap2 = new Gson().fromJson(this.msg.getString(_id, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.tmap2.put(_key, _val);
        this.TmpStr = new Gson().toJson(this.tmap2);
        this.msg.edit().putString(_id, this.TmpStr).apply();
        this.msg.edit().putString(this.tmap2.get("cmap").toString(), this.TmpStr).apply();
        this.tmap2.clear();
    }


    public HashMap<String, Object> _getMsgByID(String _id) {
        this.tmap2 = new HashMap<>();
        this.tmap2 = new Gson().fromJson(this.msg.getString(_id, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return this.tmap2;
    }


    public String getStringImage(final Bitmap bmp) {
        final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] imageBytes = baos.toByteArray();
        final String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);

        return encodedImage;

    }


    public void _AddToUpLM(String _msg, String _type, String _path, String _up) {
        if ("3".equals(_type)) {
            {
                final HashMap<String, Object> _item = new HashMap<>();
                _item.put("text", _msg);
                this.tlm2.add(_item);
            }

            this.tlm2.get(this.tlm2.size() - 1).put("type", _type);
            this.tlm2.get(this.tlm2.size() - 1).put("path", _path);
            this.tlm2.get(this.tlm2.size() - 1).put("up", _up);
        } else {
            {
                final HashMap<String, Object> _item = new HashMap<>();
                _item.put("text", _msg);
                this.tlm.add(_item);
            }

            this.tlm.get(this.tlm.size() - 1).put("type", _type);
            this.tlm.get(this.tlm.size() - 1).put("path", _path);
            this.tlm.get(this.tlm.size() - 1).put("up", _up);
        }
    }


    public void _msgDBAudio(String _msg, String _by, String _type, String _tmpl, String _mainl) {
        this.msg.edit().putString(this.PHONE.getString("UID", ""), String.valueOf((long) (Double.parseDouble(this.msg.getString(this.PHONE.getString("UID", ""), "")) + 1))).apply();
        this.msgC = Calendar.getInstance();
        this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("ms")), String.valueOf(this.msgC.getTimeInMillis())).apply();
        this.map = new HashMap<>();
        this.map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(this.msgC.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
        this.map.put("ms", this.msg.getString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("ms")), ""));
        if (200 < _msg.length()) {
            this.map.put("msg", _msg.substring(0, 150));
            this.map.put("mbool", "true");
            this.map.put("more", _msg);
        } else {
            this.map.put("msg", _msg);
        }
        this.map.put("by", _by);
        this.map.put("stage", "1");
        this.map.put("typ", _type);
        this.map.put("tmpl", _tmpl);
        this.map.put("mainl", _mainl);
        this.map.put("cmap", this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")));
        this.TmpStr = new Gson().toJson(this.map);
        this.msg.edit().putString(Objects.requireNonNull(this.map.get("id")).toString(), this.TmpStr).apply();
        this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")), this.TmpStr).apply();
        this.msg.edit().putString(Objects.requireNonNull(this.map.get("id")).toString().concat("pos"), this.msg.getString(this.PHONE.getString("UID", ""), "")).apply();
        FileUtil.copyFile(_tmpl, FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Mic/sent/".concat(Objects.requireNonNull(this.map.get("id")).toString())));
        FileUtil.deleteFile(_tmpl);
        String msgAid = Objects.requireNonNull(this.map.get("id")).toString();
        this.text = FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/".concat(Objects.requireNonNull(this.map.get("id")).toString().concat(".mp3")));
        final OnCompleteListener<Uri> success_listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull final Task<Uri> _param1) {
                String _DUrl = _param1.getResult().toString();
                ChathomeActivity.this._updateMsg(msgAid, "stage", "2");
                ChathomeActivity.this._updateMsg(msgAid, "mainl", _DUrl);
                ChathomeActivity.this._sendToDB(ChathomeActivity.this._getMsgByID(msgAid), msgAid);
            }
        };

        final OnProgressListener<UploadTask.TaskSnapshot> progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(final UploadTask.TaskSnapshot _param1) {
                final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
            }
        };
        final OnFailureListener failure_listener = new OnFailureListener() {
            @Override
            public void onFailure(final Exception _param1) {
                String _msg = _param1.getMessage();
                SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), _msg);
            }
        };

        this.storage.child(Uri.parse(this.text).toString()).putFile(Uri.fromFile(new File(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Mic/sent/".concat(Objects.requireNonNull(this.map.get("id")).toString()))))).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull final Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ChathomeActivity.this.storage.child(Uri.parse(ChathomeActivity.this.text).toString()).getDownloadUrl();
            }
        }).addOnCompleteListener(success_listener);
        this.map.clear();
        this._notifyUpdate("rs");
    }


    public void _updateNestedMsg(String _mid, @NonNull String _upid, double _pos) {
        this.templistmap1 = new HashMap<>();
        this.templistmap1 = new Gson().fromJson(this.msg.getString(_mid, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.nestlm = new Gson().fromJson(Objects.requireNonNull(this.templistmap1.get("list")).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        if (_upid.equals(Objects.requireNonNull(this.nestlm.get((int) _pos).get("id")).toString())) {
            this.nestlm.get((int) _pos).put("stage", "4");
            this.templistmap1.put("list", new Gson().toJson(this.nestlm));
            this.msg.edit().putString(_mid, new Gson().toJson(this.templistmap1)).apply();
            SketchwareUtil.showMessage(this.getApplicationContext(), "done");
        } else {
            SketchwareUtil.showMessage(this.getApplicationContext(), "failed");
        }
        this.templistmap1.clear();
    }


    public void _updateNmsg(String _mid, @NonNull String _uppid, String _cmap, double _pos) {
        this.templistmap1 = new HashMap<>();
        this.templistmap1 = new Gson().fromJson(this.msg.getString(_mid, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.nestlm = new Gson().fromJson(Objects.requireNonNull(this.templistmap1.get("list")).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        if (_uppid.equals(Objects.requireNonNull(this.nestlm.get((int) _pos).get("id")).toString())) {
            this.nestlm.get((int) _pos).put("stage", "4");
            this.templistmap1.put("list", new Gson().toJson(this.nestlm));
            this.msg.edit().putString(_mid, new Gson().toJson(this.templistmap1)).apply();
            this.msg.edit().putString(_cmap, new Gson().toJson(this.templistmap1)).apply();
        } else {

        }
        this.templistmap1.clear();
    }


    public void _MakeDir() {
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/received"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/received"));
        }
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent"));
        }
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/received"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/received"));
        }
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/sent"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/sent"));
        }
    }


    public void _OnUploadComplete(String _url, String _id, String _nid, String _type) {
        this.TestMap = new HashMap<>();
        this.TestMap = new Gson().fromJson(this.msg.getString(_nid, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.bundleLM.clear();
        this.bundleLM = new Gson().fromJson(Objects.requireNonNull(this.TestMap.get("list")).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        this.bundleLM.get((int) Double.parseDouble(this.msg.getString(_id.concat("999"), ""))).put("mainl", _url);
        this.bundleLM.get((int) Double.parseDouble(this.msg.getString(_id.concat("999"), ""))).put("stage", "2");
        this.list.edit().putString("usstage", "2").apply();
        this.list.edit().putString("usMid", _nid).apply();
        this.list.edit().putString("usid", Objects.requireNonNull(this.bundleLM.get((int) Double.parseDouble(this.msg.getString(_id.concat("999"), ""))).get("id")).toString()).apply();
        this.list.edit().putString("us", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
        this.TestMap.put("list", new Gson().toJson(this.bundleLM));
        this.msg.edit().putString(_nid, new Gson().toJson(this.TestMap)).apply();
        this.msg.edit().putString(this.PHONE.getString("UID", "").concat(this.msg.getString(this.PHONE.getString("UID", ""), "").concat("cmap")), new Gson().toJson(this.TestMap)).apply();
        this._notifyUpdate("r");
        this.onreMap = new HashMap<>();
        this.msgC = Calendar.getInstance();
        this.onreMap.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(this.msgC.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
        this.onreMap.put("gid", _nid);
        this.onreMap.put("ngid", _id);
        this.onreMap.put("url", _url);
        this.onreMap.put("cmap", Objects.requireNonNull(this.uploadlm.get(0).get("cmap")).toString());
        this.onreMap.put("pos", Objects.requireNonNull(this.uploadlm.get(0).get("pos")).toString());
        this.onreMap.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.onreMap.put("ms", String.valueOf(this.msgC.getTimeInMillis()));
        this.onreMap.put("typ", "updateURL");
        this._sendToDB(this.onreMap, Objects.requireNonNull(this.onreMap.get("id")).toString());
        this.onreMap.clear();
        this.list.edit().putString("usR", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
        this.uploadlm.remove(0);
        if (!(0 == this.uploadlm.size())) {
            this._AddUploadTask(Objects.requireNonNull(this.uploadlm.get(0).get("path")).toString(), Objects.requireNonNull(this.uploadlm.get(0).get("id")).toString(), Objects.requireNonNull(this.uploadlm.get(0).get("nid")).toString(), Objects.requireNonNull(this.uploadlm.get(0).get("type")).toString());
        }
    }


    public void _AddUploadTask(String _path, String _id, String _nid, @NonNull String _type) {
        if (_type.equals("2")) {
            try {
                new Resizer(this).setTargetLength(250).setQuality(30).setOutputFormat("JPEG").setOutputFilename(_id).setOutputDirPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent/")).setSourceImage(new File(_path)).getResizedFile();
            } catch (final Exception e) {
                SketchwareUtil.showMessage(this.getApplicationContext(), "ERROR :\n".concat(e.toString()));
            }
            this.text = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat("/".concat(_id.concat(".jpg")));
            this.text2 = FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent/".concat(_id.concat(".jpg")));
            SketchwareUtil.showMessage(this.getApplicationContext(), _id);
            final OnCompleteListener<Uri> success_listener = new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(final Task<Uri> _param1) {
                    String _DUrl = _param1.getResult().toString();
                    SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), _id);
                    ChathomeActivity.this._OnUploadComplete(_DUrl, _id, _nid, "2");
                }
            };

            final OnProgressListener<UploadTask.TaskSnapshot> progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(final UploadTask.TaskSnapshot _param1) {
                    final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                }
            };
            final OnFailureListener failure_listener = new OnFailureListener() {
                @Override
                public void onFailure(final Exception _param1) {
                    String _msg = _param1.getMessage();
                    SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), _msg);
                }
            };

            this.storage.child(Uri.parse(this.text).toString()).putFile(Uri.fromFile(new File(Uri.parse(this.text2).toString()))).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull final Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return ChathomeActivity.this.storage.child(Uri.parse(ChathomeActivity.this.text).toString()).getDownloadUrl();
                }
            }).addOnCompleteListener(success_listener);
            this.map.clear();
        } else {
            this.imgurls.edit().putString(_nid.concat(_id), "true").apply();
            this.list.edit().putString("usMid", _nid).apply();
            this.list.edit().putString("usR", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
            VideoCompress.compressVideoHigh(_path, FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4"))), new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess() {
                    ChathomeActivity.this.imgurls.edit().putString(_nid.concat(_id), "").apply();
                    ChathomeActivity.this.list.edit().putString("usMid", _nid).apply();
                    ChathomeActivity.this.list.edit().putString("usR", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
                    if (FileUtil.getFileLength(FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4")))) < 1000) {
                        FileUtil.copyFile(_path, FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4"))));
                    } else {

                    }
                    ChathomeActivity.this.text = FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/".concat(_id.concat(".mp4")));
                    ChathomeActivity.this.text2 = FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4")));

                    ChathomeActivity.this.imgurls.edit().putString(_nid.concat(_id).concat("u"), "true").apply();
                    final OnCompleteListener success_listener = new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(final Task<Uri> _param1) {
                            String _DUrl = _param1.getResult().toString();
                            ChathomeActivity.this.list.edit().putString("usMid", _nid).apply();
                            ChathomeActivity.this.imgurls.edit().putString(_nid.concat(_id).concat("u"), "").apply();
                            ChathomeActivity.this._OnUploadComplete(_DUrl, _id, _nid, "4");
                        }
                    };

                    final OnProgressListener progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(final UploadTask.TaskSnapshot _param1) {
                            final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                        }
                    };
                    final OnFailureListener failure_listener = new OnFailureListener() {
                        @Override
                        public void onFailure(final Exception _param1) {
                            String _msg = _param1.getMessage();
                            SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), _msg);
                        }
                    };

                    ChathomeActivity.this.storage.child(Uri.parse(ChathomeActivity.this.text).toString()).putFile(Uri.fromFile(new File(Uri.parse(ChathomeActivity.this.text2).toString()))).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(final Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return ChathomeActivity.this.storage.child(Uri.parse(ChathomeActivity.this.text).toString()).getDownloadUrl();
                        }
                    }).addOnCompleteListener(success_listener);
                    ChathomeActivity.this.map.clear();
                    ChathomeActivity.this._notifyUpdate("rs");
                }

                @Override
                public void onFail() {
                    SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), "failure");
                }

                @Override
                public void onProgress(final float progress) {
                    ChathomeActivity.this.nmc = progress;

                }
            });
        }
    }


    public void _updateNmsg2(String _mid, String _uppid, String _cmap, String _pos, String _url) {
        this.templistmap1 = new HashMap<>();
        this.templistmap1 = new Gson().fromJson(this.msg.getString(_mid, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.nestlm = new Gson().fromJson(Objects.requireNonNull(this.templistmap1.get("list")).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        if (_uppid.equals(Objects.requireNonNull(this.nestlm.get((int) Double.parseDouble(_pos)).get("id")).toString())) {
            this.nestlm.get((int) Double.parseDouble(_pos)).put("mainl", _url);
            this.templistmap1.put("list", new Gson().toJson(this.nestlm));
            this.msg.edit().putString(_mid, new Gson().toJson(this.templistmap1)).apply();
            this.msg.edit().putString(_cmap, new Gson().toJson(this.templistmap1)).apply();
        } else {

        }
        this.templistmap1.clear();
    }


    public void _UploadTask(String _path, String _id, String _nid, @NonNull String _type, ArrayList<HashMap<String, Object>> _lm, int _index) {
        if (_type.equals("2")) {
            this._ResizeTo(_id, _path);
            this.text = "";
            this.text2 = "";
            new java.io.File(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent/".concat(_id.concat(".webp")))).renameTo(new java.io.File(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent/".concat(_id.concat(".jpg")))));
            final OnCompleteListener<Uri> success_listener = new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull final Task<Uri> _param1) {
                    String _DUrl = _param1.getResult().toString();
                    ChathomeActivity.this._UploadComplete(_DUrl, _id, _nid, _type, _lm, _index);
                }
            };

            final OnProgressListener<UploadTask.TaskSnapshot> progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull final UploadTask.TaskSnapshot _param1) {
                    final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                }
            };
            final OnFailureListener failure_listener = new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception _param1) {
                    String _msg = _param1.getMessage();
                    ChathomeActivity.this._UploadComplete("*Fail*", _id, _nid, _type, _lm, _index);
                }
            };

            this.storage.child(Uri.parse(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat("/".concat(_id.concat(".jpg")))).toString()).putFile(Uri.fromFile(new File(Uri.parse(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent/".concat(_id.concat(".jpg")))).toString()))).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull final Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return ChathomeActivity.this.storage.child(Uri.parse(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/".concat(_id.concat(".jpg")))).toString()).getDownloadUrl();
                }
            }).addOnCompleteListener(success_listener);
        } else {
            this.imgurls.edit().putString(_nid.concat(_id), "true").apply();
            this.list.edit().putString("usMid", _nid).apply();
            this.list.edit().putString("usR", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
            VideoCompress.compressVideoLow(_path, FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4"))), new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess() {
                    ChathomeActivity.this.imgurls.edit().putString(_nid.concat(_id), "").apply();
                    ChathomeActivity.this.list.edit().putString("usMid", _nid).apply();
                    ChathomeActivity.this.list.edit().putString("usR", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
                    if (FileUtil.getFileLength(FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4")))) < 1000) {
                        FileUtil.copyFile(_path, FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4"))));
                    } else {

                    }
                    ChathomeActivity.this.text = FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/".concat(_id.concat(".mp4")));
                    ChathomeActivity.this.text2 = FileUtil.getPackageDataDir(ChathomeActivity.this.getApplicationContext()).concat("/Video/sent/".concat(_id.concat(".mp4")));
                    ChathomeActivity.this.list.edit().putString("usMid", _nid).apply();
                    ChathomeActivity.this.imgurls.edit().putString(_nid.concat(_id).concat("u"), "true").apply();
                    final OnCompleteListener success_listener = new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(final Task<Uri> _param1) {
                            String _DUrl = _param1.getResult().toString();
                            ChathomeActivity.this.list.edit().putString("usMid", _nid).apply();
                            ChathomeActivity.this.imgurls.edit().putString(_nid.concat(_id).concat("u"), "").apply();
                            ChathomeActivity.this._UploadComplete(_DUrl, _id, _nid, "4", _lm, _index);
                        }
                    };

                    final OnProgressListener progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(final UploadTask.TaskSnapshot _param1) {
                            final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                        }
                    };
                    final OnFailureListener failure_listener = new OnFailureListener() {
                        @Override
                        public void onFailure(final Exception _param1) {
                            String _msg = _param1.getMessage();
                            SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), _msg);
                            ChathomeActivity.this._UploadComplete("*Failed*", _id, _nid, "4", _lm, _index);
                        }
                    };

                    ChathomeActivity.this.storage.child(Uri.parse(ChathomeActivity.this.text).toString()).putFile(Uri.fromFile(new File(Uri.parse(ChathomeActivity.this.text2).toString()))).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(final Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return ChathomeActivity.this.storage.child(Uri.parse(ChathomeActivity.this.text).toString()).getDownloadUrl();
                        }
                    }).addOnCompleteListener(success_listener);
                    ChathomeActivity.this.map.clear();
                    ChathomeActivity.this._notifyUpdate("rs");
                }

                @Override
                public void onFail() {
                    SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), "failure");
                }

                @Override
                public void onProgress(final float progress) {
                    ChathomeActivity.this.nmc = progress;

                }
            });
        }
    }


    public void _UploadComplete(String _url, String _id, String _nid, String _type, ArrayList<HashMap<String, Object>> _lm, int index) {
        this.TestMap = new HashMap<>();
        this.TestMap = new Gson().fromJson(this.msg.getString(_nid, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.bundleLM.clear();
        this.bundleLM = new Gson().fromJson(Objects.requireNonNull(this.TestMap.get("list")).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        this.bundleLM.get((int) Double.parseDouble(this.msg.getString(_id.concat("999"), ""))).put("mainl", _url);
        this.bundleLM.get((int) Double.parseDouble(this.msg.getString(_id.concat("999"), ""))).put("stage", "2");
        this.list.edit().putString("usstage", "2").apply();
        this.list.edit().putString("usMid", _nid).apply();
        this.list.edit().putString("usid", Objects.requireNonNull(this.bundleLM.get((int) Double.parseDouble(this.msg.getString(_id.concat("999"), ""))).get("id")).toString()).apply();
        this.list.edit().putString("us", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
        this.TestMap.put("list", new Gson().toJson(this.bundleLM));
        this.msg.edit().putString(_nid, new Gson().toJson(this.TestMap)).apply();
        this.msg.edit().putString(Objects.requireNonNull(_lm.get(index).get("cmap")).toString(), new Gson().toJson(this.TestMap)).apply();
        this._notifyUpdate("r");
        this.onreMap = new HashMap<>();
        this.msgC = Calendar.getInstance();
        this.onreMap.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(this.msgC.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
        this.onreMap.put("gid", _nid);
        this.onreMap.put("ngid", _id);
        this.onreMap.put("url", _url);
        this.onreMap.put("cmap", Objects.requireNonNull(_lm.get(index).get("cmap")).toString());
        this.onreMap.put("pos", Objects.requireNonNull(_lm.get(index).get("pos")).toString());
        this.onreMap.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.onreMap.put("ms", String.valueOf(this.msgC.getTimeInMillis()));
        this.onreMap.put("typ", "updateURL");
        this._sendToDB(this.onreMap, Objects.requireNonNull(this.onreMap.get("id")).toString());
        this.onreMap.clear();
        this.list.edit().putString("usR", String.valueOf((long) (SketchwareUtil.getRandom(0, 9999999)))).apply();
        /*
        _lm.remove(0);
        if (0 == _lm.size()) {

        } else {
            _UploadTask(Objects.requireNonNull(_lm.get(0).get("path")).toString(), Objects.requireNonNull(_lm.get(0).get("id")).toString(), Objects.requireNonNull(_lm.get(0).get("nid")).toString(), Objects.requireNonNull(_lm.get(0).get("type")).toString(), _lm);
        }
        */
    }


    public String _convertToText(String _file) {
        final Bitmap bitmap = BitmapFactory.decodeFile(_file);
        this.image = this.getStringImage(bitmap);
        return (this.image);
    }


    public void _ResizeTo(String _id, String _path) {
        try {
            new Resizer(this).setTargetLength(1000).setQuality(15).setOutputFormat("WEBP").setOutputFilename(_id).setOutputDirPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent/")).setSourceImage(new java.io.File(_path)).getResizedFile();
        } catch (final Exception e) {
            SketchwareUtil.showMessage(this.getApplicationContext(), "ERROR :\n".concat(e.toString()));
        }
    }

    public String purify(final String val) {
        return val.replaceAll("\n", " ");
    }

    SharedPreferences.OnSharedPreferenceChangeListener listChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key1) {
            if (key1.equals("stopOLD")) {
                if (ChathomeActivity.this.StartUpTime != ChathomeActivity.this.spbts.getLong("stopOLD", 1)) {
                    System.out.println("Old ChatHome Removed");
                    ChathomeActivity.this.finish();
                }
            }
            //Game
            if (key1.equals("reqGame")) {
                if (ChathomeActivity.this.asp.getString("aschathomecurrent", "").equals(ChathomeActivity.this.spbts.getString("reqBy", ""))) {
                    Toast.makeText(ChathomeActivity.this, "Requesting to play", Toast.LENGTH_SHORT).show();
                    ChathomeActivity.this.textpad2.setVisibility(View.GONE);
                    ChathomeActivity.this.requestlinear.setVisibility(View.VISIBLE);
                    ChathomeActivity.this._openGame();
                } else {
                    final String Name = ChathomeActivity.this.ChatList.getString(ChathomeActivity.this.spbts.getString("reqBy", "")+"n","");
                    ChathomeActivity.this.sendGameMsg("inOtherChat");
                    Toast.makeText(ChathomeActivity.this, Name + " is Requesting to play Now", Toast.LENGTH_SHORT).show();
                }
            }
            if (key1.equals("inOtherChat")){
                Toast.makeText(ChathomeActivity.this, "Viewed Your Request", Toast.LENGTH_SHORT).show();
                if (ChathomeActivity.this.asp.getString("aschathomecurrent", "").equals(ChathomeActivity.this.spbts.getString("reqBy", ""))) {

                    ChathomeActivity.this.textpad2.setText("Waiting to Confirm...\nThe User In the Others Chat");
                    ChathomeActivity.this._openGame();
                    if (!ChathomeActivity.this.tebool) {
                        ChathomeActivity.this.after2.cancel();
                        ChathomeActivity.this.after3.cancel();
                        ChathomeActivity.this.textpad2.setAlpha(1.0f);
                        ChathomeActivity.this.tebool = true;
                    }
                }
            }
            if (key1.equals("reqGot")) {
                Toast.makeText(ChathomeActivity.this, "Viewed Your Request", Toast.LENGTH_SHORT).show();
                if (ChathomeActivity.this.asp.getString("aschathomecurrent", "").equals(ChathomeActivity.this.spbts.getString("reqBy", ""))) {

                    ChathomeActivity.this.textpad2.setText("Waiting to Confirm...");
                    ChathomeActivity.this._openGame();
                    if (!ChathomeActivity.this.tebool) {
                        ChathomeActivity.this.after2.cancel();
                        ChathomeActivity.this.after3.cancel();
                        ChathomeActivity.this.textpad2.setAlpha(1.0f);
                        ChathomeActivity.this.tebool = true;
                    }
                }
            }
            if (key1.equals("reqAccept")) {
                Toast.makeText(ChathomeActivity.this, "Accepted Your Request", Toast.LENGTH_SHORT).show();
                if (ChathomeActivity.this.asp.getString("aschathomecurrent", "").equals(ChathomeActivity.this.spbts.getString("reqBy", ""))) {
                    ChathomeActivity.this.textpad2.setText("Accepted");
                    ChathomeActivity.this._closeGame();
                }
            }
            if (key1.equals("reqDeny")) {
                Toast.makeText(ChathomeActivity.this, "Denied Your Request", Toast.LENGTH_SHORT).show();
                if (ChathomeActivity.this.asp.getString("aschathomecurrent", "").equals(ChathomeActivity.this.spbts.getString("reqBy", ""))) {
                    ChathomeActivity.this.textpad2.setText("Request To Play");
                    ChathomeActivity.this._closeGame();
                }
            }
            if (key1.equals("msgmark")) {
                ChathomeActivity.this.markmsgmap = new Gson().fromJson(ChathomeActivity.this.msg.getString(ChathomeActivity.this.spbts.getString("pos", ""), ""), new TypeToken<HashMap<String, Object>>() {
                }.getType());
                if (ChathomeActivity.this.markmsgmap.containsKey("msg")) {
                    ChathomeActivity.this.ID = Objects.requireNonNull(ChathomeActivity.this.markmsgmap.get("id")).toString();
                    ChathomeActivity.this.textview6.setText(ChathomeActivity.this.purify(Objects.requireNonNull(ChathomeActivity.this.markmsgmap.get("msg")).toString()));
                    ChathomeActivity.this.msgMark = true;
                    ChathomeActivity.this._Anime("filesgo", ChathomeActivity.this.linear22);
                    ChathomeActivity.this.linear56.setVisibility(View.VISIBLE);
                } else {
                    SketchwareUtil.showMessage(ChathomeActivity.this.getApplicationContext(), "Can't Mark It");
                    ChathomeActivity.this.textview6.setText("err");
                    ChathomeActivity.this.msgMark = false;
                    ChathomeActivity.this._Anime("filesgo", ChathomeActivity.this.linear22);
                    ChathomeActivity.this.linear56.setVisibility(View.GONE);
                }
            }
            if (key1.equals("b")) {
                ChathomeActivity.this.key = Double.parseDouble(ChathomeActivity.this.spbts.getString("pos", ""));
                com.google.android.material.bottomsheet.BottomSheetDialog bs_base = new com.google.android.material.bottomsheet.BottomSheetDialog(ChathomeActivity.this);
                final View layBase = ChathomeActivity.this.getLayoutInflater().inflate(R.layout.menu, null);
                bs_base.setContentView(layBase);
                bs_base.show();
                LinearLayout line = layBase.findViewById(R.id.linear1);
                ChathomeActivity.this.maplist = new Gson().fromJson(ChathomeActivity.this.spbts.getString("list", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
                line.setBackground(new GradientDrawable() {
                    public GradientDrawable getIns(final int a, final int b) {
                        setCornerRadius(a);
                        setColor(b);
                        return this;
                    }
                }.getIns((int) 0, 0x65000000));
                ChathomeActivity.this._viewpager(line, ChathomeActivity.this.maplist, SketchwareUtil.getDip(ChathomeActivity.this.getApplicationContext(), 20));
            }
        }
    };

    {
    }


    public void _viewpager(@NonNull View _linear, ArrayList<HashMap<String, Object>> _list, double _padding) {
        final androidx.viewpager.widget.ViewPager viewPager = new androidx.viewpager.widget.ViewPager(_linear.getContext());
        final android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewPager.setLayoutParams(params);
        viewPager.setClipToPadding(false);
        viewPager.setPadding((int) _padding, 8, (int) _padding, (int) _padding);

        viewPager.setAdapter(new AdapterVP(_list));
        viewPager.setCurrentItem((int) this.key);
        final LinearLayout layout = (LinearLayout) _linear;
        if (layout.getChildCount() != 0) {
            layout.removeAllViews();
        }
        layout.addView(viewPager);

    }

    class AdapterVP extends androidx.viewpager.widget.PagerAdapter {

        ArrayList<HashMap<String, Object>> _list;

        public AdapterVP(final ArrayList<HashMap<String, Object>> _list) {
            this._list = _list;
        }

        private LayoutInflater layoutInflater;

        @Override
        public int getCount() {
            return this._list.size();
        }

        @Override
        public boolean isViewFromObject(final View view, @NonNull final Object object) {
            return view.equals(object);
        }


        @NonNull
        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            this.layoutInflater = ChathomeActivity.this.getLayoutInflater();
            final View view = this.layoutInflater.inflate(R.layout.view_pager, null);

            TextView title, description, tv1;

            tv1 = view.findViewById(R.id.tv1);

            title = view.findViewById(R.id.title);

            description = view.findViewById(R.id.description);

            final ImageView imageview1 = view.findViewById(R.id.img);
            final TextView tdel = view.findViewById(R.id.deltext);

            ImageView img2 = view.findViewById(R.id.blike);

            final LinearLayout cardview = view.findViewById(R.id.cardview);

            LinearLayout linear = view.findViewById(R.id.linear1);

            Glide.with(ChathomeActivity.this.getApplicationContext()).load(Uri.parse(Objects.requireNonNull(ChathomeActivity.this.maplist.get(position).get("url")).toString())).placeholder(R.drawable.default_profile).into(imageview1);
            ChathomeActivity.this.cal.setTimeInMillis((long) (Double.parseDouble(ChathomeActivity.this.maplist.get(position).get("id").toString())));
            description.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(ChathomeActivity.this.cal.getTime()));
            tv1.setText(ChathomeActivity.this.maplist.get(position).get("like").toString());
            img2.setVisibility(View.GONE);
            imageview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View _view) {
                    ChathomeActivity.this._Anime("tmp", linear);
                    img2.setVisibility(View.VISIBLE);
                    if ("".equals(ChathomeActivity.this.spbts.getString(ChathomeActivity.this.getIntent().getStringExtra("UID").concat(Objects.requireNonNull(ChathomeActivity.this.maplist.get(position).get("id")).toString()), ""))) {
                        ChathomeActivity.this.maplist.get(position).put("like", String.valueOf((long) (Double.parseDouble(Objects.requireNonNull(ChathomeActivity.this.maplist.get(position).get("like")).toString()) + 1)));
                        tv1.setText(Objects.requireNonNull(ChathomeActivity.this.maplist.get(position).get("like")).toString());
                        ChathomeActivity.this.spbts.edit().putString(ChathomeActivity.this.getIntent().getStringExtra("UID").concat(Objects.requireNonNull(ChathomeActivity.this.maplist.get(position).get("id")).toString()), "true").apply();
                        ChathomeActivity.this.map = new HashMap<>();
                        ChathomeActivity.this.map.put("like", String.valueOf((long) (Double.parseDouble(Objects.requireNonNull(ChathomeActivity.this.maplist.get(position).get("like")).toString()))));
                        ChathomeActivity.this.dbGal.child(Objects.requireNonNull(ChathomeActivity.this.maplist.get(position).get("id")).toString()).updateChildren(ChathomeActivity.this.map);
                        ChathomeActivity.this.map.clear();
                    }
                    ChathomeActivity.this.tmptimer = new TimerTask() {
                        @Override
                        public void run() {
                            ChathomeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ChathomeActivity.this._Anime("tmp2", linear);
                                    img2.setVisibility(View.GONE);
                                }
                            });
                        }
                    };
                    ChathomeActivity.this._timer.schedule(ChathomeActivity.this.tmptimer, 500);
                }
            });
            tv1.setTypeface(Typeface.createFromAsset(ChathomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            description.setTypeface(Typeface.createFromAsset(ChathomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            title.setTypeface(Typeface.createFromAsset(ChathomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            tdel.setTypeface(Typeface.createFromAsset(ChathomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);

            ChathomeActivity.this._SetBackground(container, 0, 20, ChathomeActivity.this.jay.col(0, 3), false);
            ChathomeActivity.this._SetBackground(tdel, 30, 20, ChathomeActivity.this.jay.col(0, 3), false);
            ChathomeActivity.this._SetBackground(cardview, 20, 15, ChathomeActivity.this.jay.col(0, 4), false);

            tv1.setTextColor(Color.parseColor(ChathomeActivity.this.jay.col(1, 1)));
            description.setTextColor(Color.parseColor(ChathomeActivity.this.jay.col(1, 1)));
            title.setTextColor(Color.parseColor(ChathomeActivity.this.jay.col(1, 1)));
            tdel.setTextColor(Color.parseColor(ChathomeActivity.this.jay.col(1, 1)));
            tdel.setVisibility(View.GONE);

            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, @NonNull final Object object) {
            container.removeView((View) object);
        }
    }

    androidx.viewpager.widget.ViewPager viewPager;
    com.google.android.material.tabs.TabLayout tabLayout;

    private void foo() {

    }


    public void _ChatedNow() {
        this.cal = Calendar.getInstance();
        this.ChatList.edit().putString(this.getIntent().getStringExtra("UID").concat("lm"), String.valueOf(this.cal.getTimeInMillis())).apply();
        if (this.type == 3) {
            this.ChatList.edit().putString(this.getIntent().getStringExtra("UID").concat("lmsg"), this.tmpstrmsg.concat(" Audio")).apply();
        } else {
            if (this.type == 999) {
                this.ChatList.edit().putString(this.getIntent().getStringExtra("UID").concat("lmsg"), "Media File").apply();
            } else {
                this.ChatList.edit().putString(this.getIntent().getStringExtra("UID").concat("lmsg"), this.tmpstrmsg).apply();
            }
        }
        this.ChatRef.edit().putString("ref", String.valueOf((long) (SketchwareUtil.getRandom(-99999, 99999)))).apply();
    }


    public void _hidekeyb() {

    }


    public void _showlastseen() {
        this.tcal = Calendar.getInstance();
        this.tmp = new SimpleDateFormat("dd MM").format(this.tcal.getTime());
        this.tmp2 = new SimpleDateFormat("yyyy").format(this.tcal.getTime());
        this.tcal.setTimeInMillis((long) (Double.parseDouble(this.current)));
        if (this.tmp2.equals(new SimpleDateFormat("yyyy").format(this.tcal.getTime()))) {
            if (this.tmp.equals(new SimpleDateFormat("dd MM").format(this.tcal.getTime()))) {
                this.textviewOnOff.setText("Last Seen on ".concat(new SimpleDateFormat("hh:mm a").format(this.tcal.getTime())));
            } else {
                this.textviewOnOff.setText("Last Seen on ".concat(new SimpleDateFormat("MMMM d - hh:mm a").format(this.tcal.getTime())));
            }
        } else {
            this.textviewOnOff.setText("Last Seen on ".concat(new SimpleDateFormat("yyyy MMMM d - hh:mm a").format(this.tcal.getTime())));
        }
    }


    public boolean _isOffline() {
        this.tmp = this.ChatList.getString(this.lseen, "");
        if (this.tmp.equals("")) {
            if (this.current.equals("")) {
                return (false);
            } else {
                return (false);
            }
        } else {
            if (this.current.equals("")) {
                return (false);
            } else {
                try {
                    this.tn1 = Double.parseDouble(this.tmp);
                    this.tn2 = Double.parseDouble(this.current);
                    return this.tn1 == this.tn2;
                } catch (final Exception e) {
                    return (false);
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
                    if (v instanceof EditText) {
                        ((EditText) v).setTypeface(typeace);

                    } else {
                        if (v instanceof Button) {
                            ((Button) v).setTypeface(typeace);

                        }
                    }
                }
            }
        } catch (final Exception e) {
            SketchwareUtil.showMessage(this.getApplicationContext(), "Error Loading Font");
        }
    }


    public void _findUID(String _uid) {

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
