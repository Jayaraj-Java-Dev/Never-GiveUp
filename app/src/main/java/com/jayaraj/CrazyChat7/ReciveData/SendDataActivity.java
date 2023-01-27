package com.jayaraj.CrazyChat7.ReciveData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.ChatHomeActs.ChathomeActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;
import com.jayaraj.CrazyChat7.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SendDataActivity extends AppCompatActivity {

    private final Timer _timer = new Timer();
    private SharedPreferences ChatList;
    private SharedPreferences msg;
    private SharedPreferences ChatRef;
    private SharedPreferences list, PHONE;
    private SharedPreferences pathssp;

    private LinearLayout linear1;
    private TextView txt;
    private RecyclerView listview;
    private final ArrayList<HashMap<String, Object>> lm = new ArrayList<>();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private DatabaseReference dbSend = this._firebase.getReference("SenderUidRand");
    private Jay jay;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_send_data);
        this.initialize();
        this.initializeLogic();
    }

    private void initialize() {

        this.pathssp = this.getSharedPreferences("paths", Context.MODE_PRIVATE);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.msg = this.getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.ChatRef = this.getSharedPreferences("ref", Context.MODE_PRIVATE);
        this.list = this.getSharedPreferences("list", Context.MODE_PRIVATE);
        this.PHONE = this.getSharedPreferences("PHONE", Context.MODE_PRIVATE);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.txt = this.findViewById(R.id.txt1);
        this.linear1 = this.findViewById(R.id.linear1);
        this.listview = this.findViewById(R.id.listview1);

        this.UI();

        this.listview.setLayoutManager(new LinearLayoutManager(this));
        this.listview.setAdapter(new ArraAdapterList(this.lm));
    }

    private void UI() {
        this._SetGradientView(this.linear1, this.jay.col(0, 3), this.jay.col(0, 4));
        this.txt.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.txt.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
    }

    private String fpath;
    private String ftype;
    private String media = "";

    private void initializeLogic() {
        final Intent ri = this.getIntent();
        final String act = ri.getAction();
        final String typ = ri.getType();

        if (act.equals(Intent.ACTION_SEND_MULTIPLE)) {
            final ArrayList<Uri> allFiles = ri.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            final ArrayList<Uri> finalFiles = new ArrayList<>();
            Uri path;
            HashMap<String, Object> _item;
            this.listResults.clear();
            boolean add;


            for (int i = 0; i < allFiles.size() && i < 15; i++) {
                path = allFiles.get(i);
                this.fpath = path.getPath();
                if (this.fpath.contains(".") && new File(this.fpath).length() < 35000001) {
                    this.ftype = this.fpath.substring(this.fpath.lastIndexOf(".") + 1);
                    switch (this.ftype) {
                        case "mp4":
                        case "mkv":
                        case "3gp":
                            this.FileType = "VIDEO";
                            finalFiles.add(allFiles.get(i));
                            add = true;
                            break;
                        case "png":
                        case "webp":
                        case "jpg":
                        case "jpeg":
                            this.FileType = "IMAGE";
                            finalFiles.add(allFiles.get(i));
                            add = true;
                            break;
                        default:
                            add = false;
                    }
                    if (add) {
                        _item = new HashMap<>();
                        _item.put("path", this.fpath);
                        _item.put("text", "");
                        if (this.FileType.equals("VIDEO")) {
                            _item.put("type", "4");
                        } else {
                            _item.put("type", "2");
                        }
                        this.listResults.add(_item);
                        if (media.equals("")) {
                            media = Objects.requireNonNull(_item.get("type")).toString();
                        } else if (media.equals("media")) {
                        } else if (!media.equals(Objects.requireNonNull(_item.get("type")).toString())) {
                            media = "media";
                        }
                    }
                }
            }

            if (finalFiles.size() > 0) {
                this.txt.setText("Send " + finalFiles.size() + " Media to...");
                this.FileType = "list";
                this._LoadChatList();
            } else {
                this.txt.setText("Selected " + allFiles.size() + " Media files are Not Applicable to send");
            }
        } else if (act.equals(Intent.ACTION_SEND)) {
            if (typ.equals("text/plain")) {
                this.txt.setText("Send Text to...");
                this.text = ri.getStringExtra(Intent.EXTRA_TEXT);
                this.FileType = "txt";
                this._LoadChatList();
            } else if (typ.equals("*/*")) {
                final Uri path = ri.getParcelableExtra(Intent.EXTRA_STREAM);
                this.fpath = path.getPath();
                if (this.fpath.contains(".") && new File(this.fpath).length() < 15000001) {
                    this.ftype = this.fpath.substring(this.fpath.lastIndexOf(".") + 1);
                    switch (this.ftype) {
                        case "mp4":
                        case "mkv":
                        case "3gp":
                            this.FileType = "VIDEO";
                            this.txt.setText("Send Video File to...");
                            media = "4";
                            this._LoadChatList();
                            break;
                        case "png":
                        case "webp":
                        case "jpg":
                        case "jpeg":
                            this.FileType = "IMAGE";
                            media = "2";
                            this.txt.setText("Send Image File to...");
                            this._LoadChatList();
                            break;
                        default:
                            this.txt.setText("Not Aplicable Type '" + this.ftype + "'");
                    }
                } else {
                    this.txt.setText("Not Aplicable File Type/Size");
                }
            } else {
                this.txt.setText("Not Aplicable Type");
            }
        } else {
            this.txt.setText("Not Aplicable Type");
        }
    }


    public void _LoadChatList() {
        this.lm.clear();
        final String Tmp = this.ChatList.getString("list", "");
        ArrayList<HashMap<String, Object>> tlm;

        if ("".equals(Tmp)) {

        } else {
            tlm = new Gson().fromJson(Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            for (int n1 = 0; n1 < tlm.size(); n1++) {
                this.lm.add(tlm.get(n1));
                this.lm.get(this.lm.size() - 1).put("lm", this.ChatList.getString(Objects.requireNonNull(tlm.get(n1).get("uid")).toString().concat("lm"), ""));
            }
            SketchwareUtil.sortListMap(this.lm, "lm", false, false);
        }
        Objects.requireNonNull(this.listview.getAdapter()).notifyDataSetChanged();
        if (this.lm.size() == 0) {
            this.txt.setText("There is no users found");
            this.listview.setVisibility(View.GONE);
        } else {
        }

    }


    public void _SetGradientView(@NonNull View _view, String _color_a,
                                 String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) {
            public GradientDrawable getIns(final int a) {
                setCornerRadius(a);
                return this;
            }
        }.getIns((int) 0));
    }

    public void _SetBackground(View _view, double _radius, double _shadow,
                               String _color, boolean _ripple) {
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

    public class ArraAdapterList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<HashMap<String, Object>> arrayList;

        public ArraAdapterList(final ArrayList<HashMap<String, Object>> _lm) {
            this.arrayList = _lm;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater _inflater = (LayoutInflater) SendDataActivity.this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View _v = _inflater.inflate(R.layout.chatuserlist, null);
            final RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int _position) {
            final View _view = holder.itemView;
            LinearLayout linearhd = _view.findViewById(R.id.heading1);
            LinearLayout linear1 = _view.findViewById(R.id.linear1);
            LinearLayout linear2 = _view.findViewById(R.id.linear2);
            LinearLayout linear3 = _view.findViewById(R.id.linear3);
            TextView textview3 = _view.findViewById(R.id.textview3);
            LinearLayout linear7 = _view.findViewById(R.id.linear7);
            TextView textview2 = _view.findViewById(R.id.textview2);
            TextView textview5 = _view.findViewById(R.id.textview5);
            RelativeLayout linear4 = _view.findViewById(R.id.linear4);
            LinearLayout linear5 = _view.findViewById(R.id.linear5);
            LinearLayout linear6 = _view.findViewById(R.id.linear6);
            ProgressBar progressbar1 = _view.findViewById(R.id.progressbar1);
            de.hdodenhof.circleimageview.CircleImageView circleimageview2 = _view.findViewById(R.id.circleimageview2);


            if ("".equals(Objects.requireNonNull(this.arrayList.get(_position).get("name")).toString())) {
                textview2.setText("Connecting...");
            } else {
                textview2.setText(Objects.requireNonNull(this.arrayList.get(_position).get("name")).toString());
            }
            textview3.setText(SendDataActivity.this.ChatList.getString(Objects.requireNonNull(this.arrayList.get(_position).get("phone")).toString().concat("lastmsg"), ""));
            if ("".equals(Objects.requireNonNull(this.arrayList.get(_position).get("img")).toString())) {
                Glide.with(SendDataActivity.this.getApplicationContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(circleimageview2);
                progressbar1.setVisibility(View.GONE);
            } else {
                Glide.with(SendDataActivity.this.getApplicationContext()).load(Uri.parse(Objects.requireNonNull(this.arrayList.get(_position).get("img")).toString())).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable final GlideException e, final Object model, final Target<Drawable> target, final boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Drawable resource, final Object model, final Target<Drawable> target, final DataSource dataSource, final boolean isFirstResource) {
                        progressbar1.setVisibility(View.GONE);
                        return false;
                    }
                }).into(circleimageview2);
            }

            textview3.setTypeface(Typeface.createFromAsset(SendDataActivity.this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview2.setTypeface(Typeface.createFromAsset(SendDataActivity.this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview5.setTypeface(Typeface.createFromAsset(SendDataActivity.this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            if ("0".equals(SendDataActivity.this.msg.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString(), ""))) {
                textview5.setVisibility(View.GONE);
                textview5.setText("");
            } else {
                if ("".equals(SendDataActivity.this.list.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString().concat("lv"), ""))) {
                    textview5.setVisibility(View.GONE);
                    textview5.setText("");
                } else {
                    if ((Double.parseDouble(SendDataActivity.this.msg.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString(), "")) - Double.parseDouble(SendDataActivity.this.list.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString().concat("lv"), ""))) == 0) {
                        textview5.setVisibility(View.GONE);
                        textview5.setText("");
                    } else {
                        textview5.setVisibility(View.VISIBLE);
                        textview5.setText(String.valueOf((long) (Double.parseDouble(SendDataActivity.this.msg.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString(), "")) - Double.parseDouble(SendDataActivity.this.list.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString().concat("lv"), "")))));
                        SendDataActivity.this._SetBackground(textview5, 40, 8, SendDataActivity.this.jay.col(0, 2), false);
                    }
                }
            }
            if ("".equals(SendDataActivity.this.ChatList.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString().concat("lmsg"), ""))) {
                textview3.setVisibility(View.GONE);
            } else {
                textview3.setVisibility(View.VISIBLE);
                textview3.setText(SendDataActivity.this.ChatList.getString(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString().concat("lmsg"), ""));
            }
            SendDataActivity.this._SetBackground(textview2, 20, 20, SendDataActivity.this.jay.col(0, 1), false);
//            _SetBackground(linear6, 100, 0, jay.col(0,1), true);
            textview2.setTextColor(Color.parseColor(SendDataActivity.this.jay.col(1, 1)));
            textview3.setTextColor(Color.parseColor(SendDataActivity.this.jay.col(1, 1)));
            textview5.setTextColor(Color.parseColor(SendDataActivity.this.jay.col(1, 1)));
            linear2.setOnClickListener(_view1 -> {
                if (SendDataActivity.this.FileType.equals("list")) {
                    SendDataActivity.this.pathssp.edit().putString("media", media).apply();
                    SendDataActivity.this.pathssp.edit().putString("uid", Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString()).apply();
                    SendDataActivity.this.pathssp.edit().putString("uploadlist", new Gson().toJson(SendDataActivity.this.listResults)).apply();
                } else if (SendDataActivity.this.FileType.equals("txt")) {
                    SendDataActivity.this.tmpstrmsg = SendDataActivity.this.text;
                    if ("".equals(SendDataActivity.this.text)) {

                    } else {
                        SendDataActivity.this.dbSend = SendDataActivity.this._firebase.getReference("chat/".concat(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString().concat("/notify")));
                        SendDataActivity.this.type = 1;
                        SendDataActivity.this.msgMark = false;
                        SendDataActivity.this._ChatedNow(Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString());
                        SendDataActivity.this._msgDB(SendDataActivity.this.text, "me", Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString());
                    }
                } else {

                    SendDataActivity.this.ImageVid();
                    SendDataActivity.this.pathssp.edit().putString("media", media).apply();
                    SendDataActivity.this.pathssp.edit().putString("uid", Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString()).apply();
                    SendDataActivity.this.pathssp.edit().putString("uploadlist", new Gson().toJson(SendDataActivity.this.listResults)).apply();
                }
                final Intent adduserchat = new Intent();
                adduserchat.setClass(SendDataActivity.this.getApplicationContext(), ChathomeActivity.class);
                adduserchat.putExtra("UID", Objects.requireNonNull(this.arrayList.get(_position).get("uid")).toString());
                adduserchat.putExtra("PHONE", Objects.requireNonNull(this.arrayList.get(_position).get("phone")).toString());
                SendDataActivity.this.PHONE.edit().putString("PHONE", "").apply();
                SendDataActivity.this.startActivity(adduserchat);
                SendDataActivity.this.finishAffinity();

            });
        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(final View v) {
                super(v);
            }
        }
    }

    String tmpstrmsg, text;
    int type;
    boolean msgMark;

    public void _msgDB(@NonNull String _msg, String _by, String uid) {
        this.msg.edit().putString(uid, String.valueOf((long) (Double.parseDouble(this.msg.getString(uid, "")) + 1))).apply();
        Calendar msgC = Calendar.getInstance();
        this.msg.edit().putString(uid.concat(this.msg.getString(uid, "").concat("ms")), String.valueOf(msgC.getTimeInMillis())).apply();
        final HashMap<String, Object> map = new HashMap<>();
        msgC = Calendar.getInstance();
        map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))).concat(String.valueOf(msgC.getTimeInMillis()))));
        map.put("ms", this.msg.getString(uid.concat(this.msg.getString(uid, "").concat("ms")), ""));
        if (200 < _msg.length()) {
            map.put("msg", _msg.substring(0, 150));
            map.put("mbool", "true");
            map.put("more", _msg);
        } else {
            map.put("msg", _msg);
        }
        map.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("stage", "1");
        map.put("typ", "1");
        map.put("cmap", uid.concat(this.msg.getString(uid, "").concat("cmap")));
        final String TmpStr = new Gson().toJson(map);
        this.msg.edit().putString(Objects.requireNonNull(map.get("id")).toString(), TmpStr).apply();
        this.msg.edit().putString(Objects.requireNonNull(map.get("id")).toString().concat("pos"), this.msg.getString(uid, "")).apply();
        this.msg.edit().putString(uid.concat(this.msg.getString(uid, "").concat("cmap")), TmpStr).apply();
        this._notifyUpdate("rs");
        this._sendToDB(map, Objects.requireNonNull(map.get("id")).toString());
        map.clear();
    }

    private final ArrayList<HashMap<String, Object>> listResults = new ArrayList<>();
    String FileType;

    private void ImageVid() {
        this.listResults.clear();
        final HashMap<String, Object> _item = new HashMap<>();
        _item.put("path", this.fpath);
        _item.put("text", "");
        if (this.FileType.equals("VIDEO")) {
            _item.put("type", "4");
        } else {
            _item.put("type", "2");
        }
        this.listResults.add(_item);
    }


    private boolean upd;
    private TimerTask t1;

    public void _sendToDB(@NonNull HashMap<String, Object> _map, String _ID) {
        if (_map.containsKey("gid")) {
            this.dbSend.child(_ID).updateChildren(_map);
        } else {
            this.dbSend.child(_ID).updateChildren(_map).addOnSuccessListener(void_ -> {
                this._updateMsg(_ID, "stage", "2");
                this.upd = false;
                this.t1 = new TimerTask() {
                    @Override
                    public void run() {
                        SendDataActivity.this.runOnUiThread(() -> {
                            SendDataActivity.this._notifyUpdate("r");
                        });
                    }
                };
                this._timer.schedule(this.t1, 500);
            }).addOnFailureListener(exception -> {
                this._updateMsg(_ID, "stage", "0");
                this._notifyUpdate("r");
            }).addOnCanceledListener(() -> {
                this._updateMsg(_ID, "stage", "0");
                this._notifyUpdate("r");
            });
        }
    }

    public void _updateMsg(String _id, String _key, String _val) {
        HashMap<String, Object> tmap2 = new HashMap<>();
        tmap2 = new Gson().fromJson(this.msg.getString(_id, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        tmap2.put(_key, _val);
        final String TmpStr = new Gson().toJson(tmap2);
        this.msg.edit().putString(_id, TmpStr).apply();
        this.msg.edit().putString(tmap2.get("cmap").toString(), TmpStr).apply();
        tmap2.clear();
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

    public void _ChatedNow(@NonNull final String uid) {
        final Calendar cal = Calendar.getInstance();
        this.ChatList.edit().putString(uid.concat("lm"), String.valueOf(cal.getTimeInMillis())).apply();
        if (this.type == 3) {
            this.ChatList.edit().putString(uid.concat("lmsg"), this.tmpstrmsg.concat(" Audio")).apply();
        } else {
            if (this.type == 999) {
                this.ChatList.edit().putString(uid.concat("lmsg"), "Media File").apply();
            } else {
                this.ChatList.edit().putString(uid.concat("lmsg"), this.tmpstrmsg).apply();
            }
        }
        this.ChatRef.edit().putString("ref", String.valueOf((long) (SketchwareUtil.getRandom(-99999, 99999)))).apply();
    }

}