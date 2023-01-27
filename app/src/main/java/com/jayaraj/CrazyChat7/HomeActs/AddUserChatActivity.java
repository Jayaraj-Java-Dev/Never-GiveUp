package com.jayaraj.CrazyChat7.HomeActs;

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
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class AddUserChatActivity extends AppCompatActivity {
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final Timer _timer = new Timer();
    private final HashMap<String, Object> NetMap = new HashMap<>();
    private String TempPhone = "";
    private String Tmp = "";

    FirebaseFirestore dbf = FirebaseFirestore.getInstance();

    private final ArrayList<HashMap<String, Object>> contacts = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private final ArrayList<String> NetPhones = new ArrayList<>();
    private final ArrayList<String> NetUidsTemp = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> nlistmap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> nlistmap2 = new ArrayList<>();
    private EditText searchedit;
    private LinearLayout linearBackGround, lnet, linear2, searchlin;
    private RelativeLayout lBG;
    private ImageView imageviewBackGround, alertimg, searchimg;
    private TextView textview1, alerttext, textview2;
    private RecyclerView recyclerview1;

    private List<List<String>> subList = new ArrayList<>();
    private SharedPreferences ChatList, asp;
    private final Intent intent = new Intent();

    private DatabaseReference db2 = this._firebase.getReference("FindingDb");
    private final DatabaseReference AllProfileData = this._firebase.getReference("Profile");
    private ChildEventListener _AllProfileData_child_listener;
    private SharedPreferences pdata;
    private SharedPreferences msg;
    private Calendar cal = Calendar.getInstance();
    private SharedPreferences list;
    private Toolbar _toolbar;
    private boolean search = true;
    private String stext = "";

    private TextView netname, netnumber;
    private de.hdodenhof.circleimageview.CircleImageView cimg;

    private Jay jay;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.add_user_chat);
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
        final AppBarLayout _app_bar = this.findViewById(R.id._app_bar);
        final CoordinatorLayout _coordinator = this.findViewById(R.id._coordinator);
        this._toolbar = this.findViewById(R.id._toolbar);
        this.setSupportActionBar(this._toolbar);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this._toolbar.setNavigationOnClickListener(_v -> this.onBackPressed());
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.lBG = this.findViewById(R.id.linear1BG);
        this.linear2 = this.findViewById(R.id.linear2);
        this.lnet = this.findViewById(R.id.lnet);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.alertimg = this.findViewById(R.id.alert);
        this.alerttext = this.findViewById(R.id.alertTitle);
        final LinearLayout linear1 = this.findViewById(R.id.linear1);
        this.textview1 = this.findViewById(R.id.textview1);
        this.recyclerview1 = this.findViewById(R.id.recyclerview1);
        this.searchedit = this.findViewById(R.id.searchedit);
        this.searchlin = this.findViewById(R.id.linearsearch1);
        this.searchimg = this.findViewById(R.id.searchimg1);

        this.netname = this.findViewById(R.id.netname);
        this.netnumber = this.findViewById(R.id.netnumber);
        this.cimg = this.findViewById(R.id.netimg);
        this.textview2 = this.findViewById(R.id.textview2);

        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.pdata = this.getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.list = this.getSharedPreferences("list", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this.recyclerview1.setVisibility(View.GONE);
        this.searchlin.setVisibility(View.GONE);

        this.lnet.setVisibility(View.GONE);
        this.searchedit.setSingleLine(true);
        this.searchedit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        this.searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.toString().trim().equals("")) {
                    AddUserChatActivity.this.search = false;
                } else {
                    AddUserChatActivity.this.search = true;
                    AddUserChatActivity.this.stext = editable.toString().trim().toLowerCase();
                }
                if (!editable.toString().trim().equals("") && editable.toString().trim().length() >= 10) {
                    if (AddUserChatActivity.this.isnumber(editable.toString().trim())) {
                        //find(_phonetoid(_Phone(editable.toString().trim())));
                        AddUserChatActivity.this.dbf.collection("UserProfile").whereEqualTo("PHONE", AddUserChatActivity.this._Phone(editable.toString().trim())).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            final List<DocumentSnapshot> s = queryDocumentSnapshots.getDocuments();
                            if (s.size() == 0) {
                                Toast.makeText(AddUserChatActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                AddUserChatActivity.this.gone();
                            } else {
                                Toast.makeText(AddUserChatActivity.this, "User Located", Toast.LENGTH_SHORT).show();
                                AddUserChatActivity.this.getDataFromNet(s.get(0).get("CID").toString());
                            }
                        });
                    }
                }
                if (AddUserChatActivity.this.contacts.size() >= 1) {
                    Objects.requireNonNull(AddUserChatActivity.this.recyclerview1.getAdapter()).notifyDataSetChanged();
                }
            }
        });

//        _AllProfileData_child_listener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot _param1, String _param2) {
//                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
//                };
//                final String _childKey = _param1.getKey();
//                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
//                SketchwareUtil.getAllKeysFromMap(_childValue, NetUidsTemp);
//                NetPhones.clear();
//                AllProfileData.removeEventListener(_AllProfileData_child_listener);
//                n = 0;
//                for (int _repeat13 = 0; _repeat13 < NetUidsTemp.size(); _repeat13++) {
//                    TempPhone = _GetVal("PHONE", _GetMap("data", (HashMap<String, Object>) _childValue.get(NetUidsTemp.get((int) (n)))));
//                    if (!NetUidsTemp.get((int) (n)).equals(pdata.getString("UID", ""))) {
//                        if (msg.getString(NetUidsTemp.get((int) (n)), "").equals("")) {
//                            NetPhones.add(_GetVal("PHONE", _GetMap("data", (HashMap<String, Object>) _childValue.get(NetUidsTemp.get((int) (n))))));
//                            NetMap.put(TempPhone.concat("u"), NetUidsTemp.get((int) (n)));
//                            NetMap.put(TempPhone.concat("n"), _GetVal("NAME", _GetMap("data", (HashMap<String, Object>) _childValue.get(NetUidsTemp.get((int) (n))))));
//                            NetMap.put(TempPhone.concat("i"), _GetVal("URL", _GetMap("data", (HashMap<String, Object>) _childValue.get(NetUidsTemp.get((int) (n))))));
//                        }
//                    }
//                    n++;
//                }
//                _getAllContacts();
//                _Anime("animat1", linear2);
//                if (0 == contacts.size()) {
//                    textview1.setText("Nobody Found From Contacts");
//                    searchedit.setHint("Search in Net");
//                    recyclerview1.setVisibility(View.GONE);
//                } else {
//                    textview1.setText("List From the Contacts");
//                    recyclerview1.setVisibility(View.VISIBLE);
//                }
//                searchlin.setVisibility(View.VISIBLE);
//                Objects.requireNonNull(recyclerview1.getAdapter()).notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot _param1, String _param2) {
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot _param1, String _param2) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot _param1) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError _param1) {
//            }
//        };
//        AllProfileData.addChildEventListener(_AllProfileData_child_listener);
        this._getAllContacts2();

        this.contacts.clear();
        this.subList = new ArrayList<>();

        for (this.i = 0; this.i < this.phone.size(); this.i += 10) {
            this.subList.add(this.phone.subList(this.i, Math.min(this.i + 10, this.phone.size())));
        }
        this.phone = new ArrayList<>();
        this.i = 0;
        this.totres =0;

        for (final List<String> ls : this.subList)
            this.dbf.collection("UserProfile").whereIn("PHONE", ls).get().addOnCanceledListener(() -> {
                this.totres++;
                this.check();
            }).addOnFailureListener(e -> {
                this.totres++;
                this.check();
            }).addOnCompleteListener(task -> {
                final QuerySnapshot ds = task.getResult();
                final List<DocumentSnapshot> d = ds.getDocuments();
                this.totres++;
                if (d.size() == 0) {

                } else {
                    for (int n = 0; n < d.size(); n++) {
                        this.TempPhone = d.get(n).get("PHONE").toString();
                        if (!d.get(n).get("CID").toString().equals(this.pdata.getString("UID", ""))) {
                            if (this.msg.getString(d.get(n).get("CID").toString(), "").equals("")) {
                                this.NetPhones.add(this.TempPhone);
                                this.NetMap.put(this.TempPhone.concat("u"), Objects.requireNonNull(d.get(n).get("CID")).toString());
                                this.NetMap.put(this.TempPhone.concat("n"), Objects.requireNonNull(d.get(n).get("NAME")).toString());
                                this.NetMap.put(this.TempPhone.concat("i"), Objects.requireNonNull(d.get(n).get("URL")).toString());
                                {
                                    final HashMap<String, Object> _item = new HashMap<>();
                                    _item.put("name", Objects.requireNonNull(d.get(n).get("NAME")).toString().trim());
                                    this.phone.add(this.TempPhone);
                                    this.contacts.add(_item);
                                }
                            }
                        }

                    }
                }
                this.check();

            });

    }
    void check(){
        if(this.totres == this.subList.size()){
//                    Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show();
            this._Anime("animat1", this.linear2);
            if (0 == this.contacts.size()) {
                this.textview1.setText("Nobody Found From Contacts");
                this.searchedit.setHint("Search in Net");
                this.recyclerview1.setVisibility(View.GONE);
            } else {
                this.textview1.setText("List From the Contacts");
                this.recyclerview1.setVisibility(View.VISIBLE);
            }
            this.searchlin.setVisibility(View.VISIBLE);
            Objects.requireNonNull(this.recyclerview1.getAdapter()).notifyDataSetChanged();
        }
    }
    private int totres;
    private int i;
    private ArrayList<HashMap<String, Object>> tlistmap = new ArrayList<>();

    void getDataFromNet(final String UID) {
        this.db2 = this._firebase.getReference("Profile/uid/" + UID);
        this.db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                AddUserChatActivity.this.tlistmap = new ArrayList<>();
                try {
                    final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (final DataSnapshot _data : snapshot.getChildren()) {
                        final HashMap<String, Object> _map = _data.getValue(_ind);
                        AddUserChatActivity.this.tlistmap.add(_map);
                    }
                } catch (final Exception _e) {
                    _e.printStackTrace();
                }
                if (AddUserChatActivity.this.tlistmap.size() == 0) {
                    Toast.makeText(AddUserChatActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    AddUserChatActivity.this.gone();
                } else {
                    AddUserChatActivity.this.ShowData(AddUserChatActivity.this.tlistmap.get(0));
                }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        });
    }

    void gone() {
        this._Anime("netuserg", this.linear2);
        this.lnet.setVisibility(View.GONE);
    }

    void ShowData(final HashMap<String, Object> map) {
        this._Anime("netuserv", this.linear2);
        this.lnet.setVisibility(View.VISIBLE);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(map.get("CID").toString())) {
            this.netnumber.setText("It's YOU");
            this.lnet.setOnClickListener(v -> {
                Toast.makeText(this, "It's YOU", Toast.LENGTH_SHORT).show();
            });
        } else if (!this.msg.getString(map.get("CID").toString(), "").equals("")) {
            this.netnumber.setText("User Already in Chat");
            this.lnet.setOnClickListener(v -> {
                Toast.makeText(this, "User Already in Chat", Toast.LENGTH_SHORT).show();
            });
        } else {
            this.netnumber.setText("");
            this.setl(map);
        }

        if (map.get("URL").toString().equals("")) {
            this.cimg.setImageResource(R.drawable.default_profile);
        } else {
            Glide.with(this.getApplicationContext()).load(Uri.parse(map.get("URL").toString())).into(this.cimg);
        }
        this.netname.setText(map.get("NAME").toString());
    }

    void setl(final HashMap<String, Object> map) {
        this.lnet.setOnClickListener(v -> {
            this.nlistmap2.clear();
            this.Tmp = this.ChatList.getString("list", "");
            if ("".equals(this.Tmp)) {

            } else {
                this.nlistmap2 = new Gson().fromJson(this.Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
            }
            {
                final HashMap<String, Object> _item = new HashMap<>();
                _item.put("uid", map.get("CID").toString());
                this.nlistmap2.add(_item);
            }

            this.nlistmap2.get(this.nlistmap2.size() - 1).put("img", map.get("URL").toString());
            this.nlistmap2.get(this.nlistmap2.size() - 1).put("name", map.get("NAME").toString());
            this.nlistmap2.get(this.nlistmap2.size() - 1).put("phone", map.get("PHONE").toString());
            this.ChatList.edit().putString(map.get("PHONE").toString(), "Added").apply();
            this.ChatList.edit().putString("list", new Gson().toJson(this.nlistmap2)).apply();
            this.msg.edit().putString(map.get("CID").toString(), "0").apply();
            this.ChatList.edit().putString(map.get("CID").toString().concat("n"), map.get("NAME").toString()).apply();
            this.ChatList.edit().putString(map.get("CID").toString().concat("i"), map.get("URL").toString()).apply();
            this.ChatList.edit().putString(map.get("CID").toString().concat("p"), map.get("PHONE").toString()).apply();
            this.cal = Calendar.getInstance();
            this.ChatList.edit().putString(map.get("CID").toString().concat("lm"), String.valueOf(this.cal.getTimeInMillis())).apply();
            this.list.edit().putString(map.get("CID").toString().concat("lv"), "0").apply();
            this.intent.setClass(this.getApplicationContext(), HomeActivity.class);
            this.startActivity(this.intent);
            this.finishAffinity();
        });
    }

    boolean isnumber(final String num) {
        return this._Phone(num).length() == 13;
    }

    String tmp, t, t2;

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

    private void initializeLogic() {
        this._Bg();
        this._font();
        this.recyclerview1.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerview1.setAdapter(new Recyclerview1Adapter(this.contacts));
    }

    void openc() {
        final String name = "";
        final String phone = "+91";
        final Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        contactIntent
                .putExtra(ContactsContract.Intents.Insert.NAME, name)
                .putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        this.startActivityForResult(contactIntent, 1);
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    public void _getAllContacts() {
        try (final android.database.Cursor c = this.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{BaseColumns._ID, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")) {
            while (c.moveToNext()) {
                this._addContact(c.getString(1), c.getString(2));
            }
        }
    }

    public void _getAllContacts2() {
        try (final android.database.Cursor c = this.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{BaseColumns._ID, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")) {
            while (c.moveToNext()) {
                this._addContact2(c.getString(1), c.getString(2));
            }
        }
    }

    public void _addContact2(String _name, String _phone) {
        if (9 < _phone.length()) {
            final String tmp2 = this._Phone(_phone);

            if (this.phone.contains(tmp2)) {

            } else {
                if ("".equals(this.ChatList.getString(tmp2, ""))) {
                    if (tmp2.equals(this.pdata.getString("PHONE", ""))) {

                    } else {
                        {
                            final HashMap<String, Object> _item = new HashMap<>();
                            _item.put("name", _name.trim());
                            this.contacts.add(_item);
                        }

                        this.phone.add(tmp2);
                    }
                }
            }

        }
    }

    public void _addContact(String _name, String _phone) {
        if (9 < _phone.length()) {
            final String tmp2 = this._Phone(_phone);
            if (this.NetPhones.contains(tmp2)) {
                if (this.phone.contains(tmp2)) {

                } else {
                    if ("".equals(this.ChatList.getString(tmp2, ""))) {
                        if (tmp2.equals(this.pdata.getString("PHONE", ""))) {

                        } else {
                            {
                                final HashMap<String, Object> _item = new HashMap<>();
                                _item.put("name", _name.trim());
                                this.contacts.add(_item);
                            }

                            this.phone.add(tmp2);
                        }
                    }
                }
            }
        }
    }


    public void _setRoundCorner(View _view, String _color, double _radius) {
        final android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();

        gd.setColor(Color.parseColor(_color));

        gd.setCornerRadius((int) _radius);
        _view.setBackground(gd);
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
                        AddUserChatActivity.this.runOnUiThread(() -> AddUserChatActivity.this.asp.edit().putString(_Sp, "").apply());
                    }
                }, 350);
            } catch (final Exception e5) {
            }
        } else {

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
        this._SetGradientView(this.lBG, jay.col(0, 3), jay.col(0, 4));
        this._toolbar.setBackgroundColor(Color.parseColor(jay.col(0, 3)));
        this.textview1.setTextColor(Color.parseColor(jay.col(1, 2)));
        this.alerttext.setTextColor(Color.parseColor(jay.col(1, 2)));
        this.searchedit.setTextColor(Color.parseColor(jay.col(1, 2)));
        this.textview2.setTextColor(Color.parseColor(jay.col(1, 2)));
        this.netname.setTextColor(Color.parseColor(jay.col(1, 2)));
        this.netnumber.setTextColor(Color.parseColor(jay.col(1, 2)));
        this.alertimg.setColorFilter(Color.parseColor(jay.col(1, 2)));
        this.searchimg.setColorFilter(Color.parseColor(jay.col(1, 2)));
        this._SetBackground(this.searchlin, 15, 8, jay.col(0, 4), false);

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


    public void _font() {
        this.textview1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.alerttext.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.searchedit.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textview2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.netnumber.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.netname.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this._SetBackground(this.lnet, 20, 0, this.jay.col(0, 1), true);
    }


    public String _GetVal(String _st, HashMap<String, Object> _Map) {
        if (_Map.containsKey(_st)) {
            return (Objects.requireNonNull(_Map.get(_st)).toString());
        } else {
            return ("");
        }
    }


    public String _Phone(String _Phone) {
        double tmpnum = 0;
        String tempstr = "";
        for (int _repeat11 = 0; _repeat11 < _Phone.length(); _repeat11++) {
            if ("+0123456789".contains(_Phone.substring((int) (tmpnum), (int) (tmpnum + 1)))) {
                tempstr = tempstr.concat(_Phone.substring((int) (tmpnum), (int) (tmpnum + 1)));
            } else {

            }
            tmpnum++;
        }
        if (tempstr.contains("+")) {
            return (tempstr);
        } else {
            return ("+91".concat(tempstr));
        }
    }


    public HashMap<String, Object> _GetMap(String _St, HashMap<String, Object> _Map) {
        return (HashMap<String, Object>) _Map.get(_St);
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


    public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;
        LinearLayout linear1, div;
        LinearLayout linear2;
        LinearLayout linear3;
        TextView textview3;
        LinearLayout linear7;
        TextView textview2;
        TextView textview5;
        RelativeLayout linear4;
        LinearLayout linear5;
        LinearLayout linear6;
        ProgressBar progressbar1;
        de.hdodenhof.circleimageview.CircleImageView circleimageview2;
        boolean show = true;

        public Recyclerview1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater _inflater = (LayoutInflater) AddUserChatActivity.this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View _v = _inflater.inflate(R.layout.chatuserlist, null);
            final RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);

            this.linear1 = _v.findViewById(R.id.linear1);
            this.linear2 = _v.findViewById(R.id.linear2);
            this.linear3 = _v.findViewById(R.id.linear3);
            this.textview3 = _v.findViewById(R.id.textview3);
            this.linear7 = _v.findViewById(R.id.linear7);
            this.textview2 = _v.findViewById(R.id.textview2);
            this.textview5 = _v.findViewById(R.id.textview5);
            this.linear4 = _v.findViewById(R.id.linear4);
            this.linear5 = _v.findViewById(R.id.linear5);
            this.linear6 = _v.findViewById(R.id.linear6);
            this.div = _v.findViewById(R.id.divider);
            this.progressbar1 = _v.findViewById(R.id.progressbar1);
            this.circleimageview2 = _v.findViewById(R.id.circleimageview2);

            this.textview3.setTypeface(Typeface.createFromAsset(AddUserChatActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            this.textview2.setTypeface(Typeface.createFromAsset(AddUserChatActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            this.textview5.setTypeface(Typeface.createFromAsset(AddUserChatActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            AddUserChatActivity.this._SetBackground(this.textview5, 15, 8, AddUserChatActivity.this.jay.col(0, 2), false);
//            _SetBackground(linear2, 20, 20, jay.col(0,1), true);

            this.textview2.setTextColor(Color.parseColor(AddUserChatActivity.this.jay.col(1, 1)));
            this.textview3.setTextColor(Color.parseColor(AddUserChatActivity.this.jay.col(1, 1)));
            this.textview5.setTextColor(Color.parseColor(AddUserChatActivity.this.jay.col(1, 1)));


            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder _holder, final int pos) {
            int _position1;
            _position1 = _holder.getAdapterPosition();
            final View _v = _holder.itemView;

            this.linear1 = _v.findViewById(R.id.linear1);
            this.linear2 = _v.findViewById(R.id.linear2);
            this.linear3 = _v.findViewById(R.id.linear3);
            this.textview3 = _v.findViewById(R.id.textview3);
            this.linear7 = _v.findViewById(R.id.linear7);
            this.textview2 = _v.findViewById(R.id.textview2);
            this.textview5 = _v.findViewById(R.id.textview5);
            this.linear4 = _v.findViewById(R.id.linear4);
            this.linear5 = _v.findViewById(R.id.linear5);
            this.linear6 = _v.findViewById(R.id.linear6);
            this.div = _v.findViewById(R.id.divider);
            this.progressbar1 = _v.findViewById(R.id.progressbar1);
            this.circleimageview2 = _v.findViewById(R.id.circleimageview2);
            this.show = true;
            if (AddUserChatActivity.this.search) {
                if (this._data.get(_position1).get("name").toString().toLowerCase().contains(AddUserChatActivity.this.stext)) {
                } else if (AddUserChatActivity.this.phone.get(_position1).contains(AddUserChatActivity.this.stext)) {
                } else if (AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("n")).toString().toLowerCase().contains(AddUserChatActivity.this.stext)) {
                } else {
                    this.show = false;
                }
            }


            if (this.show) {
                this.linear1.setVisibility(View.VISIBLE);
                this.div.setVisibility(View.VISIBLE);
            } else {
                this.linear1.setVisibility(View.GONE);
                this.div.setVisibility(View.GONE);
            }

            if (this.show) {
                this.textview2.setText(Objects.requireNonNull(this._data.get(_position1).get("name")).toString());
                this.textview3.setText(AddUserChatActivity.this.phone.get(_position1));
                this.textview5.setText("~".concat(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("n")).toString()));

                if ("".equals(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("i")).toString())) {
                } else {
                    Glide.with(AddUserChatActivity.this.getApplicationContext()).load(Uri.parse(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("i")).toString())).into(this.circleimageview2);
                }
                this.linear2.setOnClickListener(_view1 -> {
                    AddUserChatActivity.this.nlistmap.clear();
                    AddUserChatActivity.this.Tmp = AddUserChatActivity.this.ChatList.getString("list", "");
                    if ("".equals(AddUserChatActivity.this.Tmp)) {

                    } else {
                        AddUserChatActivity.this.nlistmap = new Gson().fromJson(AddUserChatActivity.this.Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                        }.getType());
                    }
                    {
                        final HashMap<String, Object> _item = new HashMap<>();
                        _item.put("uid", AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("u")).toString());
                        AddUserChatActivity.this.nlistmap.add(_item);
                    }

                    AddUserChatActivity.this.nlistmap.get(AddUserChatActivity.this.nlistmap.size() - 1).put("img", Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("i"))).toString());
                    AddUserChatActivity.this.nlistmap.get(AddUserChatActivity.this.nlistmap.size() - 1).put("name", Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("n"))).toString());
                    AddUserChatActivity.this.nlistmap.get(AddUserChatActivity.this.nlistmap.size() - 1).put("phone", AddUserChatActivity.this.phone.get(_position1));
                    AddUserChatActivity.this.ChatList.edit().putString(AddUserChatActivity.this.phone.get(_position1), "Added").apply();
                    AddUserChatActivity.this.ChatList.edit().putString("list", new Gson().toJson(AddUserChatActivity.this.nlistmap)).apply();
                    AddUserChatActivity.this.msg.edit().putString(Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("u"))).toString(), "0").apply();
                    AddUserChatActivity.this.ChatList.edit().putString(Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("u"))).toString().concat("n"), Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("n"))).toString()).apply();
                    AddUserChatActivity.this.ChatList.edit().putString(Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("u"))).toString().concat("i"), Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("i"))).toString()).apply();
                    AddUserChatActivity.this.ChatList.edit().putString(Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("u"))).toString().concat("p"), AddUserChatActivity.this.phone.get(_position1)).apply();
                    AddUserChatActivity.this.cal = Calendar.getInstance();
                    AddUserChatActivity.this.ChatList.edit().putString(Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("u"))).toString().concat("lm"), String.valueOf(AddUserChatActivity.this.cal.getTimeInMillis())).apply();
                    AddUserChatActivity.this.list.edit().putString(Objects.requireNonNull(AddUserChatActivity.this.NetMap.get(AddUserChatActivity.this.phone.get(_position1).concat("u"))).toString().concat("lv"), "0").apply();
                    AddUserChatActivity.this.intent.setClass(AddUserChatActivity.this.getApplicationContext(), HomeActivity.class);
                    AddUserChatActivity.this.startActivity(AddUserChatActivity.this.intent);
                    AddUserChatActivity.this.finishAffinity();
                });
            }
        }

        @Override
        public int getItemCount() {
            return this._data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(final View v) {
                super(v);
            }
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
        final ArrayList<Double> _result = new ArrayList<>();
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
