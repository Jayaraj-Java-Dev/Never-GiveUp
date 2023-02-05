package com.jayaraj.CrazyChat7.HomeActs;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.BaseColumns;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.ServicesActs.DbmsgActivity;
import com.jayaraj.CrazyChat7.ServicesActs.KeepOnlineService;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import me.echodev.resizer.Resizer;

public class HomeActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
    private double tmpnum;
    private String tempstr = "";
    private String tempstr2 = "";
    private HashMap<String, Object> onoffMap = new HashMap<>();
    private final String id = "";
    private final HashMap<String, Object> nmap = new HashMap<>();
    private double key;
    private String tmp = "";

    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> emptylm = new ArrayList<>();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear2;
    private ImageView imageviewBackGround;
    private LinearLayout linear1;
    private Button button1;
    private TabLayout tablayout1;
    private ViewPager2 viewpager1;

    private SharedPreferences Sp;
    private final DatabaseReference dbMyProfile = this._firebase.getReference("rand");
    private DatabaseReference dbOnOff = this._firebase.getReference("rand");
    private FirebaseAuth auth;
    private TimerTask dbOnline;
    private Calendar onoffc = Calendar.getInstance();
    private SharedPreferences status;
    private final Calendar cal = Calendar.getInstance();
    private final StorageReference storage = this._firebase_storage.getReference("Profile rand");
    private DatabaseReference db = this._firebase.getReference("rand");
    private SharedPreferences ChatRef;
    private SharedPreferences asp;

    private Jay jay;


    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(com.jayaraj.CrazyChat7.R.layout.home);
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
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            this.initializeLogic();
        }
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.linear1BG = this.findViewById(com.jayaraj.CrazyChat7.R.id.linear1BG);
        this.linearBackGround = this.findViewById(com.jayaraj.CrazyChat7.R.id.linearBackGround);
        this.linear2 = this.findViewById(com.jayaraj.CrazyChat7.R.id.linear2);
        this.imageviewBackGround = this.findViewById(com.jayaraj.CrazyChat7.R.id.imageviewBackGround);
        this.linear1 = this.findViewById(com.jayaraj.CrazyChat7.R.id.linear1);
        this.button1 = this.findViewById(com.jayaraj.CrazyChat7.R.id.button1);
        this.tablayout1 = this.findViewById(com.jayaraj.CrazyChat7.R.id.tablayout1);
        this.viewpager1 = this.findViewById(com.jayaraj.CrazyChat7.R.id.viewpager1);
        this.Sp = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.auth = FirebaseAuth.getInstance();
        this.status = this.getSharedPreferences("status", Context.MODE_PRIVATE);
        this.ChatRef = this.getSharedPreferences("ref", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.button1.setVisibility(View.GONE);
        this.asp.edit().putString("ashome", "r").apply();
        this.asp.edit().putString("aschathomecurrent", "home").apply();

    }

    private void initializeLogic() {
        final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                final java.lang.reflect.Method m =
                        StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (final Exception ignored) {
            }
        }
        this._Bg();
        this.db = this._firebase.getReference("PIMGS/uid/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
        this._OnCrt();
        this.onoffMap = new HashMap<>();
        this.onoffMap.put("ON", "true");
        this.dbOnOff.child("data").updateChildren(this.onoffMap);
        this.onoffMap.clear();
        this.ChatRef.edit().putString("sts", "").apply();
//        this.dbOnline = new TimerTask() {
//            @Override
//            public void run() {
//                HomeActivity.this.runOnUiThread(() -> {
//                    HomeActivity.this.onoffc = Calendar.getInstance();
//                    HomeActivity.this.tmp = String.valueOf(HomeActivity.this.onoffc.getTimeInMillis());
//                    HomeActivity.this.tmp = HomeActivity.this.tmp.substring(0, 10).concat("000");
//                    HomeActivity.this.onoffMap = new HashMap<>();
//                    if (HomeActivity.this.ChatRef.getString("sts", "").equals("")) {
//                        HomeActivity.this.onoffMap.put("STS", "");
//                        HomeActivity.this.onoffMap.put("ChatText","");
//                    } else {
//                        HomeActivity.this.onoffMap.put("STS", HomeActivity.this.ChatRef.getString("sts", ""));
//                        HomeActivity.this.onoffMap.put("ChatText", HomeActivity.this.ChatRef.getString("ChatText", ""));
//                    }
//                    HomeActivity.this.onoffMap.put("SEEN", HomeActivity.this.tmp);
//                    HomeActivity.this.dbOnOff.child("data").updateChildren(HomeActivity.this.onoffMap);
//                    HomeActivity.this.onoffMap.clear();
//                });
//            }
//        };
//        this._timer.scheduleAtFixedRate(this.dbOnline, 1, 1000);

        this.status.registerOnSharedPreferenceChangeListener(this.listChangeListener);
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
            return 3;
        }

        @NonNull
        @Override
        public Fragment createFragment(final int _position) {
            if (0 == _position) {
                return new ChatsFragmentActivity();
            } else if (1 == _position) {
                return new StatusFragmentActivity();
            } else if (2 == _position) {
                return new FriendsFragment();
            } else {
                return new ChatsFragmentActivity();
            }
        }

    }

    @Override
    public void onBackPressed() {
        this.onoffMap = new HashMap<>();
        this.onoffMap.put("ON", "false");
        this.dbOnOff.child("data").updateChildren(this.onoffMap);
        this.onoffMap.clear();
        this.finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        //asp.edit().putString("ashome","s").apply();
        this.onoffMap = new HashMap<>();
        this.onoffMap.put("ON", "false");
        this.dbOnOff.child("data").updateChildren(this.onoffMap);
        this.onoffMap.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.asp.edit().putString("ashome", "p").apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        this._Bg();
        if (this.status.getString("theme", "").equals("")) {
            this.status.edit().putString("theme", "1").apply();
        } else {
            this.status.edit().putString("theme", "").apply();
        }
        this.asp.edit().putString("aschathomecurrent", "home").apply();
        this.asp.edit().putString("ashome", "r").apply();
        this.onoffMap = new HashMap<>();
        this.onoffMap.put("ON", "true");
        this.dbOnOff.child("data").updateChildren(this.onoffMap);
        this.onoffMap.clear();
        this.ChatRef.edit().putString("sts", "").apply();
        this.ChatRef.edit().putString("ref", String.valueOf((long) (SketchwareUtil.getRandom(-99999, 99999)))).apply();
        if (this.status.getString("CHS", "").equals("true")) {
            this.status.edit().putString("CHS", "false").apply();
            this.viewpager1.setAdapter(new MyFragmentAdapter(this));
            this.viewpager1.setCurrentItem(this.tablayout1.getSelectedTabPosition());
        }
    }

    @Override
    public void onDestroy() {
        //aspsp
        super.onDestroy();
        this.asp.edit().putString("ashome", "s").apply();
        this.onoffMap = new HashMap<>();
        this.onoffMap.put("ON", "false");
        this.dbOnOff.child("data").updateChildren(this.onoffMap);
        this.onoffMap.clear();
        this.status.unregisterOnSharedPreferenceChangeListener(this.listChangeListener);
//        this.dbOnline.cancel();
    }


    public void _OnCrt() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel("CHAT", "Chat", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Chat Message");
            final NotificationManager nm = this.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }

        this.viewpager1.setAdapter(new MyFragmentAdapter(this));
//        tablayout1.setupWithViewPager(viewpager1);
        this.tablayout1.addTab(this.tablayout1.newTab().setText("Chats"));
        this.tablayout1.addTab(this.tablayout1.newTab().setText("Profile"));
        this.tablayout1.addTab(this.tablayout1.newTab().setText("Friends"));
        this.tablayout1.setBackgroundColor(Color.parseColor(this.jay.col(0, 3)));
        this.tablayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                HomeActivity.this.viewpager1.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {

            }
        });

        this.viewpager1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                HomeActivity.this.tablayout1.selectTab(HomeActivity.this.tablayout1.getTabAt(position));
            }
        });
        if ("".equals(this.Sp.getString("HomeAct(Welcome)(snakbar)", ""))) {
            com.google.android.material.snackbar.Snackbar.make(this.tablayout1, "Welcome to Crazy Chat!", BaseTransientBottomBar.LENGTH_SHORT).setAction("Ok", _view -> {

            }).show();
            this.Sp.edit().putString("HomeAct(Welcome)(snakbar)", "t").apply();
        } else {

        }
        this.dbOnOff = this._firebase.getReference("Profile/uid/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
//        PeriodicWorkRequest saveRequest =
//                new PeriodicWorkRequest.Builder(StartWorkDB.class, 15, TimeUnit.MINUTES)
//                         Constraints
//                        .build();


        if (this.isServiceRunning(this, DbmsgActivity.class)) {
//            Toast.makeText(this, "running", Toast.LENGTH_SHORT).show();
        } else {
            final Intent service = new Intent(this, DbmsgActivity.class);
            getApplicationContext().startService(service);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    getApplicationContext().startForegroundService(service);
//                } else {
//                    getApplicationContext().startService(service);
//                }

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
    }

    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) {
            public GradientDrawable getIns(final int a) {
                setCornerRadius(a);
                return this;
            }
        }.getIns((int) 0));
    }

    public void _Bg() {
        //linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        if (FileUtil.isFile(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"))) {
            this.imageviewBackGround.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"), 1024, 1024));
        } else {
//            imageviewBackGround.setImageResource(R.drawable.bgi);
        }
        final Jay jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this.tablayout1.setBackgroundColor(Color.parseColor(jay.col(0, 3)));
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._SetGradientView(this.linear1BG, jay.col(0, 3), jay.col(0, 4));
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0, 3)));
    }


    public void _getAllContacts() {
        final android.database.Cursor c = this.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{BaseColumns._ID, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (c.moveToNext()) {
            this._addContact(c.getString(1), c.getString(2));
        }
    }

    public void _addContact(String _name, @NonNull String _phone) {
        if (9 < _phone.length()) {
            this.tempstr2 = this._Phone(_phone);
            if ("".equals(this.Sp.getString(this.tempstr2, ""))) {

            } else {
                this.Sp.edit().putString(this.tempstr2.concat("cname"), _name).apply();
            }
        }
    }


    public String _Phone(@NonNull String _Phone) {
        this.tmpnum = 0;
        this.tempstr = "";
        for (int _repeat11 = 0; _repeat11 < _Phone.length(); _repeat11++) {
            if ("+0123456789".contains(_Phone.substring((int) (this.tmpnum), (int) (this.tmpnum + 1)))) {
                this.tempstr = this.tempstr.concat(_Phone.substring((int) (this.tmpnum), (int) (this.tmpnum + 1)));
            } else {

            }
            this.tmpnum++;
        }
        return (this.tempstr);
    }


    public void _Share() {
    }

    private void shareApplication() {
        final android.content.pm.ApplicationInfo app =
                this.getApplicationContext().getApplicationInfo();
        final String filePath = app.sourceDir;
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("apk/*");
        final java.io.File originalApk = new java.io.File(filePath);
        try {
            java.io.File tempFile = new java.io.File(this.getExternalCacheDir() + "/ExtractedApk");
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            tempFile = new java.io.File(tempFile.getPath() + "/" +
                    "export.apk");
            if (!tempFile.exists()) {
                try {
                    if (!tempFile.createNewFile()) {
                        return;
                    }
                } catch (final java.io.IOException ignored) {
                }
            }
            final java.io.InputStream in;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                in = Files.newInputStream(originalApk.toPath());
            } else {
                in = new java.io.FileInputStream(originalApk);
            }
            final java.io.OutputStream out;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                out = Files.newOutputStream(tempFile.toPath());
            } else {
                out = new java.io.FileOutputStream(tempFile);
            }
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            this.startActivity(Intent.createChooser(intent, "Share app via"));
        } catch (final java.io.IOException e) {
            this.showMessage(e.toString());
        }

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

    //listeners sharedpreference
    SharedPreferences.OnSharedPreferenceChangeListener listChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, @NonNull final String key1) {
            if (key1.equals("f")) {

            }
            if (key1.equals("b")) {
                HomeActivity.this.key = Double.parseDouble(HomeActivity.this.status.getString("pos", ""));
                com.google.android.material.bottomsheet.BottomSheetDialog bs_base = new com.google.android.material.bottomsheet.BottomSheetDialog(HomeActivity.this);
                final View layBase = HomeActivity.this.getLayoutInflater().inflate(com.jayaraj.CrazyChat7.R.layout.menu, null);
                bs_base.setContentView(layBase);
                bs_base.show();
                LinearLayout line = layBase.findViewById(com.jayaraj.CrazyChat7.R.id.linear1);
                HomeActivity.this.maplist = new Gson().fromJson(HomeActivity.this.status.getString("list", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
                line.setBackground(new GradientDrawable() {
                    public GradientDrawable getIns(final int a, final int b) {
                        setCornerRadius(a);
                        setColor(b);
                        return this;
                    }
                }.getIns((int) 0, 0x65000000));
                HomeActivity.this._viewpager(line, HomeActivity.this.maplist, SketchwareUtil.getDip(HomeActivity.this.getApplicationContext(), 20));
            }
        }
    };

    public void _viewpager(View _linear, ArrayList<HashMap<String, Object>> _list, double _padding) {
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
            this.layoutInflater = LayoutInflater.from(container.getContext());
            final View view = this.layoutInflater.inflate(com.jayaraj.CrazyChat7.R.layout.view_pager, container, false);
            ImageView image;
            final TextView title;
            TextView description;
            final TextView tv1;

            tv1 = view.findViewById(com.jayaraj.CrazyChat7.R.id.tv1);
            //image = view.findViewById(R.id.image);
            title = view.findViewById(com.jayaraj.CrazyChat7.R.id.title);
            final TextView tdel = view.findViewById(com.jayaraj.CrazyChat7.R.id.deltext);
            description = view.findViewById(com.jayaraj.CrazyChat7.R.id.description);
            final ImageView imageview1 = view.findViewById(com.jayaraj.CrazyChat7.R.id.img);
            final ImageView imageview2 = view.findViewById(com.jayaraj.CrazyChat7.R.id.blike);


            final LinearLayout cardview = view.findViewById(com.jayaraj.CrazyChat7.R.id.cardview);
            final LinearLayout linear = view.findViewById(R.id.linear1);
            Glide.with(HomeActivity.this.getApplicationContext()).load(Uri.parse(Objects.requireNonNull(HomeActivity.this.maplist.get(position).get("url")).toString())).into(imageview1);
            imageview2.setVisibility(View.GONE);
            HomeActivity.this.cal.setTimeInMillis((long) (Double.parseDouble(Objects.requireNonNull(HomeActivity.this.maplist.get(position).get("id")).toString())));
            description.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(HomeActivity.this.cal.getTime()));
            tv1.setText(HomeActivity.this.maplist.get(position).get("like").toString());

            tv1.setTypeface(Typeface.createFromAsset(HomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            description.setTypeface(Typeface.createFromAsset(HomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            title.setTypeface(Typeface.createFromAsset(HomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            tdel.setTypeface(Typeface.createFromAsset(HomeActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);

            HomeActivity.this._SetBackground(container, 0, 20, HomeActivity.this.jay.col(0, 3), false);
            HomeActivity.this._SetBackground(tdel, 30, 20, HomeActivity.this.jay.col(0, 3), false);
            HomeActivity.this._SetBackground(cardview, 20, 15, HomeActivity.this.jay.col(0, 4), false);

            tv1.setTextColor(Color.parseColor(HomeActivity.this.jay.col(1, 1)));
            description.setTextColor(Color.parseColor(HomeActivity.this.jay.col(1, 1)));
            title.setTextColor(Color.parseColor(HomeActivity.this.jay.col(1, 1)));
            tdel.setTextColor(Color.parseColor(HomeActivity.this.jay.col(1, 1)));

            tdel.setOnClickListener(v -> {
//                status.edit().putString("del",String.valueOf(position));
            });

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

    public void _resize(String _id, String _path) {
        try {
            new Resizer(this).setTargetLength(1700).setQuality(15).setOutputFormat("WEBP").setOutputFilename(_id).setOutputDirPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent")).setSourceImage(new java.io.File(_path)).getResizedFile();
        } catch (final IOException ignored) {
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
