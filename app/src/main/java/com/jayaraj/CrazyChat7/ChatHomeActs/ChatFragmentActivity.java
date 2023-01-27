package com.jayaraj.CrazyChat7.ChatHomeActs;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.OtherActs.AudioviewActivity;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.RequestNetwork;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;
import com.jayaraj.CrazyChat7.OtherActs.TextFullViewActivity;
import com.jayaraj.CrazyChat7.OtherActs.ViewfileActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ChatFragmentActivity extends Fragment {
    private final Timer _timer = new Timer();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double CurrentSize;
    private String CrazyChatPath = "";
    private final HashMap<String, Object> DataMap = new HashMap<>();
    private boolean bind;
    private double unreadpos;
    private boolean animBool;
    private HashMap<String, Object> tempmap = new HashMap<>();

    private final ArrayList<HashMap<String, Object>> listMap = new ArrayList<>();

    private ImageView imageview1;
    private LinearLayout linear3;
    private LinearLayout linear1;
    private ListView recyclerview1;
    private LinearLayout linear2;
    private GridView gridview1;

    private SharedPreferences CHAT;
    private SharedPreferences PHONE;
    private SharedPreferences list;
    private SharedPreferences msg;
    private final Calendar tmpCal = Calendar.getInstance();
    private Calendar tmpCal2 = Calendar.getInstance();
    private Calendar tmpCal3 = Calendar.getInstance();
    private SharedPreferences Aplay;
    private TimerTask aft1;
    private final Intent audioIntent = new Intent();
    private FirebaseAuth FAuth;
    private Calendar msgc = Calendar.getInstance();
    private DatabaseReference dbSend = this._firebase.getReference("SenderUid");
    private TimerTask at, ot1;
    private SharedPreferences asp;
    private TimerTask loadTm;
    private SharedPreferences ChatList;
    private DatabaseReference dbSendProfile = this._firebase.getReference("profile");
    private SharedPreferences spbts;
    private Fragment f;

    private Jay jay;
    private final int msgcount = 0;


    private final ObjectAnimator openAnim = new ObjectAnimator();
    private final ObjectAnimator openAnim2 = new ObjectAnimator();
    private final ObjectAnimator closeAnim = new ObjectAnimator();
    private final ObjectAnimator closeAnim2 = new ObjectAnimator();
    private LinearLayout bottom, btr1, bottom2;
    private TextView btr1t1, btr1t2, btr1t3, btr1t4, b2t1;
    private double bottomCURRENT, bottomCURRENTpos2;
    private boolean isopen;
    private boolean isopen2;

    private int balance;
    private int firstElement, lastElement;
    private boolean pad;
    RecyclerView.LayoutManager lman;
    private boolean lbool = true;
    private final int MIN = 50;
    private int LastItem = -1;


    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater _inflater, @Nullable final ViewGroup _container, @Nullable final Bundle _savedInstanceState) {
        final View _view = _inflater.inflate(R.layout.chat_fragment, _container, false);
        this.initialize(_savedInstanceState, _view);
        com.google.firebase.FirebaseApp.initializeApp(this.requireContext());
        this.initializeLogic();
        return _view;
    }

    private void initialize(final Bundle _savedInstanceState, final View _view) {
        this.linear3 = _view.findViewById(R.id.linear3);
        this.linear1 = _view.findViewById(R.id.linear1);
        this.recyclerview1 = _view.findViewById(R.id.recyclerview1);
        this.linear2 = _view.findViewById(R.id.linear2);
        //BOTTOM LAYOUTS
        this.bottom = _view.findViewById(R.id.bottom);
        this.bottom2 = _view.findViewById(R.id.bottom2);
        this.b2t1 = _view.findViewById(R.id.b2t1);
        this.btr1 = _view.findViewById(R.id.brow1);
        this.btr1t1 = _view.findViewById(R.id.br1t1);
        this.btr1t2 = _view.findViewById(R.id.br1t2);
        this.btr1t3 = _view.findViewById(R.id.br1t3);
        this.btr1t4 = _view.findViewById(R.id.br1t4);
        this.gridview1 = _view.findViewById(R.id.gridview1);
        this.CHAT = this.requireContext().getSharedPreferences("CHAT", Context.MODE_PRIVATE);
        this.PHONE = this.requireContext().getSharedPreferences("PHONE", Context.MODE_PRIVATE);
        this.jay = new Jay(this.requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE));
        this.list = this.requireContext().getSharedPreferences("list", Context.MODE_PRIVATE);
        this.msg = this.requireContext().getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.Aplay = this.requireContext().getSharedPreferences("Aplay", Context.MODE_PRIVATE);
        this.FAuth = FirebaseAuth.getInstance();
        this.asp = this.requireContext().getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.ChatList = this.requireContext().getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        final RequestNetwork rn = new RequestNetwork((Activity) this.getContext());
        this.spbts = this.requireContext().getSharedPreferences("botomsheet", Context.MODE_PRIVATE);
        this.balance = Integer.parseInt(this.msg.getString(this.PHONE.getString("UID", ""), ""));

        this.linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {

            }
        });
    }

    private void oncrt() {
        this._SetBackground(this.bottom2, 30, 20, this.jay.col(0, 3), true);

        this._SetBackground(this.bottom, 30, 20, this.jay.col(0, 3), false);
        this._SetBackground(this.btr1t1, 30, 20, this.jay.col(0, 1), true);
        this._SetBackground(this.btr1t2, 30, 20, this.jay.col(0, 1), true);
        this._SetBackground(this.btr1t3, 30, 20, this.jay.col(0, 1), true);
        this._SetBackground(this.btr1t4, 30, 20, this.jay.col(0, 1), true);
        this.b2t1.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.btr1t1.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.btr1t2.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.btr1t3.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.b2t1.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.btr1t1.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.btr1t2.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.btr1t3.setTextColor(Color.parseColor(this.jay.col(1, 1)));

        this.btr1t1.setOnClickListener(v -> {
            this.swiped((int) this.bottomCURRENT, (int) this.bottomCURRENTpos2);
            this._closeBottom();
        });
        this.btr1t2.setOnClickListener(v -> {
            final String msgcp;
            if ("".equals(this._getMsg(this.bottomCURRENT, "mbool"))) {
                msgcp = this._getMsg(this.bottomCURRENT, "msg");
            } else {
                msgcp = this._getMsg(this.bottomCURRENT, "more");
            }

            ((ClipboardManager) this.requireContext().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", msgcp));
            Toast.makeText(this.getContext(), "Copied", Toast.LENGTH_LONG).show();
            this._closeBottom();
        });
        this.btr1t3.setOnClickListener(v -> {
            this._closeBottom();
        });
        this.btr1t4.setOnClickListener(v -> {
            this._extendBottom();
        });
    }

    private void initializeLogic() {
        final boolean onetimeDef = true;
        this.DataMap.put("U", this.PHONE.getString("UID", ""));
        this.DataMap.put("P", this.PHONE.getString("PHONE", ""));
        this.recyclerview1.setVisibility(View.GONE);
        //Glide.with(getContext()).load(Uri.parse("d")).placeholder(R.drawable.default_profile).into(imageview1);
        this.CrazyChatPath = FileUtil.getPackageDataDir(this.requireContext());
        this.CurrentSize = 0;
        this.lman = new LinearLayoutManager(this.getContext());
        this.recyclerview1.setAdapter(new Listview1Adapter(this.listMap));
        this.dbSend = this._firebase.getReference("chat/".concat(this.PHONE.getString("UID", "").concat("/notify")));
        this.dbSendProfile = this._firebase.getReference("Profile/uid/".concat(this.PHONE.getString("UID", "")));
        this.list.registerOnSharedPreferenceChangeListener(this.listChangeListener);
        this.oncrt();
        this.bind = false;
        this.gridview1.setAlpha(0);
        if (this.jay.isDefaultEmoji()) {
            this.EmojiLmap = this.jay.getDefaultEmoji();
        } else {
            this.EmojiLmap = this.jay.Emoji();
        }
        this.gridview1.setAdapter(new Gridview1Adapter(this.EmojiLmap));
        this.loadTm = new TimerTask() {
            @Override
            public void run() {
                ChatFragmentActivity.this.requireActivity().runOnUiThread(() -> {
                    ChatFragmentActivity.this._loadmsg();
                });
            }
        };
        this._timer.schedule(this.loadTm, 10);

    }

    private void swiped(final int position, final int pos2) {
        if (pos2 == this.listMap.size() - 1) {
            SketchwareUtil.showMessage(this.getContext(), "It Can't be Marked");
        } else {
            this.spbts.edit().putString("pos", this.PHONE.getString("UID", "").concat(String.valueOf((long) (position + 1)).concat("cmap"))).apply();
            this.spbts.edit().putString("msgmark", String.valueOf((long) (SketchwareUtil.getRandom(10000, 9999999)))).apply();
        }
    }

    @Override
    public void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.list.unregisterOnSharedPreferenceChangeListener(this.listChangeListener);
    }

    //Shared Preference Listener
    SharedPreferences.OnSharedPreferenceChangeListener listChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
            if (key.equals("r")) {
                ChatFragmentActivity.this.unreadpos = -2;
                ChatFragmentActivity.this._loadmsgAscrol(false);
            }
            if (key.equals("s")) {
                ChatFragmentActivity.this.recyclerview1.smoothScrollToPosition(ChatFragmentActivity.this.listMap.size());
            }
            if (key.equals("a")) {
                ChatFragmentActivity.this.unreadpos = -2;
                ChatFragmentActivity.this._loadmsgAscrol(true);
            }
        }
    };

    private boolean isextended;

    public void _extendBottom() {
        this.isextended = true;
        this.openAnim.setTarget(this.bottom);
        this.openAnim.setPropertyName("translationY");
        this.openAnim.setFloatValues(SketchwareUtil.getDip(this.getContext(), -315));
        this.openAnim.setDuration(500);
        this.openAnim.start();
        this.gridview1.animate().alpha(1).setDuration(500).start();
    }

    public void _openBottom() {
        this.isopen = true;
        this.gridview1.animate().alpha(0).setDuration(500).start();
        this.openAnim.setTarget(this.bottom);
        this.openAnim.setPropertyName("translationY");
        this.openAnim.setFloatValues(SketchwareUtil.getDip(this.getContext(), -150));
        this.openAnim.setDuration(500);
        this.openAnim.start();
    }

    public void _openBottom2() {
        this.tmpCal3 = Calendar.getInstance();
        this.nowDate2 = new SimpleDateFormat("dd/MM/yyyy").format(this.tmpCal2.getTime());
        if (this.msgDate2.equals(this.nowDate2)) {

        } else {
            this.isopen2 = true;
            this.openAnim2.setTarget(this.bottom2);
            this.openAnim2.setPropertyName("translationY");
            if (this.isopen) {
                this.openAnim2.setFloatValues(SketchwareUtil.getDip(this.getContext(), -155));
            } else {
                this.openAnim2.setFloatValues(SketchwareUtil.getDip(this.getContext(), -105));
            }
            this.openAnim2.setDuration(300);
            this.openAnim2.start();
        }


    }

    private String msgDate2, nowDate2;

    public void _closeBottom() {
        this.isopen = false;
        this.isextended = false;
        this.gridview1.animate().alpha(0).setDuration(500).start();
        this.closeAnim.setTarget(this.bottom);
        this.closeAnim.setPropertyName("translationY");
        this.closeAnim.setFloatValues((float) (0));
        this.closeAnim.setDuration(500);
        this.closeAnim.start();
    }

    public void _closeBottom2() {
        this.isopen2 = false;
        this.closeAnim2.setTarget(this.bottom2);
        this.closeAnim2.setPropertyName("translationY");
        this.closeAnim2.setFloatValues((float) (0));
        this.closeAnim2.setDuration(200);
        this.closeAnim2.start();
    }

    public void _loadmsg() {
        this.balance = Integer.parseInt(this.msg.getString(this.PHONE.getString("UID", ""), "")) - (int) this.CurrentSize;
        if (this.balance != 0) {
            int max = this.balance;
            int endmax = 0;
            if (!this.pad) {
                final HashMap<String, Object> _item = new HashMap<>();
                _item.put("timeT", "PAD");
//            pad=true;
                this.firstElement = max;
                this.listMap.add(_item);
                endmax++;
            }
            if (this.balance < this.MIN) {
                for (int i = max - 1; i >= 0; i--) {
                    final HashMap<String, Object> _item = new HashMap<>();
                    _item.put("timeT", String.valueOf(i));
                    this.listMap.add(0, _item);
                    this.CurrentSize++;
                    this.balance--;
                    this.lastElement = i;
                    endmax++;
                }

            } else {
                for (int i = this.MIN - 1; i >= 0; i--) {
                    final HashMap<String, Object> _item = new HashMap<>();
                    _item.put("timeT", String.valueOf(this.balance - 1));
                    this.listMap.add(0, _item);
                    this.CurrentSize++;
                    this.balance--;
                    this.lastElement = i;
                    endmax++;
                }

            }
            this.animBool = false;
            ((BaseAdapter) this.recyclerview1.getAdapter()).notifyDataSetChanged();

            if (!this.pad) {
                this.firstpad();
            } else {
                this.recyclerview1.setSelection(endmax + 4);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void firstpad() {
        this._Anime("FirstPad", this.linear1);
        this.recyclerview1.setVisibility(View.VISIBLE);
        if ("".equals(this.list.getString(Objects.requireNonNull(this.DataMap.get("U")).toString().concat("lv"), ""))) {
            this.recyclerview1.setSelection(this.listMap.size() - 1);
        } else {
            int v = Integer.parseInt(this.list.getString(Objects.requireNonNull(this.DataMap.get("U")).toString().concat("lv"), ""));
            final int le = Integer.parseInt(Objects.requireNonNull(this.listMap.get(this.listMap.size() - 2).get("timeT")).toString());
            final int nv = ((le - v) + 1) / this.MIN;
            this.pad = true;
            for (int j = 0; j < nv; j++) {
                this._loadmsgforscrolling();

            }
            this.unreadpos = v;
            for (int i = 0; i < this.listMap.size() - 1; i++) {
                try {
                    if (this._getMsg(Integer.parseInt(this.listMap.get(i).get("timeT").toString()), "id").equals(this._getMsg(v, "id"))) {
                        v = i;
                        break;
                    }
                } catch (final Exception e) {

                }
            }

            ((BaseAdapter) this.recyclerview1.getAdapter()).notifyDataSetChanged();
            this.recyclerview1.setSelection(v);
        }
        this.bind = true;
        this.recyclerview1.setOnScrollListener(new AbsListView.OnScrollListener() {
            int pos;

            @Override
            public void onScrollStateChanged(final AbsListView absListView, final int i) {
                ChatFragmentActivity.this.animBool = i == 1;
                if (i == 0) {
                    if (ChatFragmentActivity.this.isopen2) {
                        ChatFragmentActivity.this._closeBottom2();
                    }
                } else {
                    this.pos = ChatFragmentActivity.this.getrposbypos(ChatFragmentActivity.this.LastItem);
                    if (this.pos != -1) {
                        if (!ChatFragmentActivity.this.isopen2) {
                            ChatFragmentActivity.this._openBottom2();
                        }
                    } else {
                        if (ChatFragmentActivity.this.isopen2) {
                            ChatFragmentActivity.this._closeBottom2();
                        }
                    }
                }
            }

            @Override
            public void onScroll(final AbsListView absListView, final int i, final int i1, final int i2) {
                if ((i == 3 && ChatFragmentActivity.this.pad) && ChatFragmentActivity.this.lbool) {
                    ChatFragmentActivity.this.lbool = false;
                    ChatFragmentActivity.this.loadTm = new TimerTask() {
                        @Override
                        public void run() {
                            ChatFragmentActivity.this.requireActivity().runOnUiThread(() -> {
                                ChatFragmentActivity.this._loadmsg();
                                ChatFragmentActivity.this.lbool = true;
                            });
                        }
                    };
                    ChatFragmentActivity.this._timer.schedule(ChatFragmentActivity.this.loadTm, 10);
                }
                ChatFragmentActivity.this.LastItem = i + i1 - 3;
                this.pos = ChatFragmentActivity.this.getrposbypos(ChatFragmentActivity.this.LastItem);
                if (this.pos != -1) {
                    ChatFragmentActivity.this.msgDate2 = ChatFragmentActivity.this._GetTime("dd/MM/yyyy", ChatFragmentActivity.this._getMsg(this.pos, "ms"));
                    ChatFragmentActivity.this.b2t1.setText(ChatFragmentActivity.this.msgDate2);
                }
            }
        });
        this.pad = true;

    }

    public int getrposbypos(final int pos) {
        if (pos >= 0 && pos < this.listMap.size() - 2) {
            return Integer.parseInt(this.listMap.get(pos).get("timeT").toString());
        } else {
            return -1;
        }
    }

    public void _loadmsgforscrolling() {
        this.balance = Integer.parseInt(this.msg.getString(this.PHONE.getString("UID", ""), "")) - (int) this.CurrentSize;
        if (this.balance != 0) {
            int max = this.balance;
            if (!this.pad) {
                final HashMap<String, Object> _item = new HashMap<>();
                _item.put("timeT", "PAD");
                this.firstElement = max;
                this.listMap.add(_item);
            }
            if (this.balance < this.MIN) {
                for (int i = max - 1; i >= 0; i--) {
                    final HashMap<String, Object> _item = new HashMap<>();
                    _item.put("timeT", String.valueOf(i));
                    this.listMap.add(0, _item);
                    this.CurrentSize++;
                    this.balance--;
                    this.lastElement = i;
                }
            } else {
                for (int i = this.MIN - 1; i >= 0; i--) {
                    final HashMap<String, Object> _item = new HashMap<>();
                    _item.put("timeT", String.valueOf(this.balance - 1));
                    this.listMap.add(0, _item);
                    this.CurrentSize++;
                    this.balance--;
                    this.lastElement = i;
                }
            }
        }
    }

    public void _loadmsgAscrol(boolean _scroll) {
        final int balance1 = Integer.parseInt(this.msg.getString(this.PHONE.getString("UID", ""), ""));
        if (!this.pad) {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("timeT", "PAD");
//            pad=true;
            this.listMap.add(_item);
        }
        for (; balance1 > this.firstElement; this.firstElement++) {
            final HashMap<String, Object> _item = new HashMap<>();
            _item.put("timeT", String.valueOf(this.firstElement));
            this.listMap.add(this.listMap.size() - 1, _item);
            this.CurrentSize++;
        }
        ((BaseAdapter) this.recyclerview1.getAdapter()).notifyDataSetChanged();
        if (!this.pad) {
            this.firstpad();
        }
        if (_scroll) {
            try {
                if (!(this.listMap.size() == 0)) {
                    this.loadTm = new TimerTask() {
                        @Override
                        public void run() {
                            ChatFragmentActivity.this.requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ChatFragmentActivity.this.recyclerview1.smoothScrollToPosition(ChatFragmentActivity.this.listMap.size() - 1);
                                }
                            });
                        }
                    };
                    this._timer.schedule(this.loadTm, 200);
                }
            } catch (final Exception e5) {
            }
        }
    }

    private HashMap<String, Object> tmap, tmap2;
    private String TmpStr;

    public String _getMsg(double _position, String _key) {
        if (_key.equals("ms")) {
            return (this.msg.getString(this.PHONE.getString("UID", "").concat(String.valueOf((long) (_position + 1)).concat(_key)), ""));
        } else {
            this.tmap = new Gson().fromJson(this.msg.getString(this.PHONE.getString("UID", "").concat(String.valueOf((long) (_position + 1)).concat("cmap")), ""), new TypeToken<HashMap<String, Object>>() {
            }.getType());
            if (this.tmap.containsKey(_key)) {
                return (Objects.requireNonNull(this.tmap.get(_key)).toString());
            } else {
                return ("");
            }
        }
    }

    public void _updateMsg(String _id, String _key, String _val) {
        this.tmap2 = new Gson().fromJson(this.msg.getString(_id, ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.tmap2.put(_key, _val);
        this.TmpStr = new Gson().toJson(this.tmap2);
        this.msg.edit().putString(_id, this.TmpStr).apply();
        if (_key.equals("sidelike")) {
            this.msg.edit().putString(_id + "sidelike", _val).apply();
        }
        this.msg.edit().putString(Objects.requireNonNull(this.tmap2.get("cmap")).toString(), this.TmpStr).apply();
        this.tmap2.clear();
    }

    private String getlike(final String _id) {
        return this.msg.getString(_id + "sidelike", "");
    }

    private void setlike(final String _id, final String _val) {
        this.msg.edit().putString(_id + "sidelike", _val).apply();
    }

    public String _GetTime(String _format, String _ms) {
        this.tmpCal.setTimeInMillis((long) (Double.parseDouble(_ms)));
        return (new SimpleDateFormat(_format).format(this.tmpCal.getTime()));
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


    public boolean _isFound(String _path, String _id) {
        if ("is".equals(_path)) {
            return FileUtil.isExistFile(this.CrazyChatPath.concat("/Image/sent/".concat(_id.concat(".jpg"))));
        }
        if ("ir".equals(_path)) {
            return FileUtil.isExistFile(this.CrazyChatPath.concat("/Image/received/".concat(_id.concat(".jpg"))));
        }
        if ("mis".equals(_path)) {
            return FileUtil.isExistFile(this.CrazyChatPath.concat("/Mic/sent/".concat(_id)));
        }
        if ("mir".equals(_path)) {
            return FileUtil.isExistFile(this.CrazyChatPath.concat("/Mic/received/".concat(_id)));
        }
        return (false);
    }


    public String _getIdPath(String _path, String _id) {
        if ("is".equals(_path)) {
            return (this.CrazyChatPath.concat("/Image/sent/".concat(_id.concat(".jpg"))));
        }
        if ("ir".equals(_path)) {
            return (this.CrazyChatPath.concat("/Image/received/".concat(_id.concat(".jpg"))));
        }
        if ("mis".equals(_path)) {
            return (this.CrazyChatPath.concat("/Mic/sent/".concat(_id)));
        }
        if ("mir".equals(_path)) {
            return (this.CrazyChatPath.concat("/Mic/received/".concat(_id)));
        }
        return ("null");
    }


    public void _Glide(ImageView _img, String _url) {
        Glide.with(this.requireContext()).load(_url).placeholder(R.drawable.default_profile).fitCenter().into(_img);
    }

    public Context getContext(final Context c) {
        return c;
    }

    public Context getContext(@NonNull final Fragment f) {
        this.f = f;
        return f.getActivity();
    }

    public Context getContext(@NonNull final DialogFragment df) {
        return df.getActivity();
    }


    public void _sendToDB(HashMap<String, Object> _map, String _ID) {
        this.dbSend.child(_ID).updateChildren(_map);
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
                        ChatFragmentActivity.this.requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ChatFragmentActivity.this.asp.edit().putString(_Sp, "").apply();
                            }
                        });
                    }
                };
                this._timer.schedule(this.at, 350);
            } catch (final Exception ignored) {
            }
        }
    }

    private HashMap<String, Object> onreMap;

    public void _sendLike(String _msgid, final String like) {
        this.onreMap = new HashMap<>();
        this.msgc = Calendar.getInstance();
        this.onreMap.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(this.msgc.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
        this.onreMap.put("gid", _msgid);
        this.onreMap.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.onreMap.put("ms", String.valueOf(this.msgc.getTimeInMillis()));
        this.onreMap.put("typl", like);
        this.onreMap.put("typ", "like");
        this._sendToDB(this.onreMap, Objects.requireNonNull(this.onreMap.get("id")).toString());
        this.onreMap.clear();
    }

    public void _send(String _msgid) {
        this.onreMap = new HashMap<>();
        this.msgc = Calendar.getInstance();
        this.onreMap.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(this.msgc.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
        this.onreMap.put("gid", _msgid);
        this.onreMap.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.onreMap.put("ms", String.valueOf(this.msgc.getTimeInMillis()));
        this.onreMap.put("typ", "saw");
        this._sendToDB(this.onreMap, Objects.requireNonNull(this.onreMap.get("id")).toString());
        this.onreMap.clear();
    }


    public void _BinderTyp999(double _position, @NonNull View _l4, @NonNull View _l9, @NonNull ImageView _i3, @NonNull TextView _t3) {
        _l4.setVisibility(View.VISIBLE);
        _l9.setVisibility(View.GONE);
        _t3.setVisibility(View.VISIBLE);
        _i3.setImageResource(R.drawable.photoedit);
        final String jsonTemp = this._getMsg(_position, "list");
        if ("".equals(jsonTemp)) {
            _t3.setText("Failed to Get Count");
        } else {

            String s,count = this._getMsg(_position, "count");

            //for v1.5 and below
            if(count.equals("")){
                final ArrayList<HashMap<String, Object>> tempRorLM = new Gson().fromJson(jsonTemp, new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType());
                count= String.valueOf((long) (tempRorLM.size()));
            }

            s = (Integer.parseInt(count)>1)?"s":"";

            if(this._getMsg(_position, "media").equals("2")){
                _t3.setText(count.concat(" Image").concat(s));
            } else if(this._getMsg(_position, "media").equals("4")){
                _t3.setText(count.concat(" Video").concat(s));
            } else {
                _t3.setText(count.concat(" Media"));
            }


        }
        _l4.setOnClickListener(_view -> {
            this.audioIntent.setClass(this.getContext(), ViewfileActivity.class);
            this.audioIntent.putExtra("mid", this._getMsg(_position, "id"));
            this.audioIntent.putExtra("uid", this.PHONE.getString("UID", ""));
            this.audioIntent.putExtra("list", this._getMsg(_position, "list"));
            this.audioIntent.putExtra("by", this._getMsg(_position, "by"));
            this.audioIntent.putExtra("cmap", this._getMsg(_position, "cmap"));
            this.startActivity(this.audioIntent);
        });
    }


    public void _BinderTye3(double _position, @NonNull View _l4, @NonNull View _l9, @NonNull ImageView _i3, @NonNull TextView _t3) {
        _l4.setVisibility(View.VISIBLE);
        _l9.setVisibility(View.GONE);
        _t3.setVisibility(View.VISIBLE);
        _i3.setImageResource(R.drawable.play_button);
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(this._getMsg(_position, "by"))) {
            if (this._isFound("mis", this._getMsg(_position, "id"))) {
                _t3.setText("Voice Record");
            } else {
                if ("".equals(this._getMsg(_position, "mainl"))) {
                    _t3.setText("Uploading...");
                } else {
                    _t3.setText("Download Audio");
                }
            }
        } else {
            if (this._isFound("mir", this._getMsg(_position, "id"))) {
                _t3.setText("Voice Record");
            } else {
                if ("".equals(this._getMsg(_position, "mainl"))) {
                    _t3.setText("Uploading...");
                } else {
                    _t3.setText("Download Audio");
                }
            }
        }
        _l4.setOnClickListener(_view -> {
            this.audioIntent.setClass(this.getContext(), AudioviewActivity.class);
            this.audioIntent.putExtra("pos", String.valueOf((long) (_position)));
            this.startActivity(this.audioIntent);
        });
    }


    public void _BinderTyp2(double _position, @NonNull View _l4, @NonNull View _l9, View _l2, ImageView _i2) {
        _l9.setVisibility(View.VISIBLE);
        _l4.setVisibility(View.GONE);
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(this._getMsg(_position, "by"))) {
            if (this._isFound("is", this._getMsg(_position, "id"))) {
                this._Glide(_i2, this._getIdPath("is", this._getMsg(_position, "id")));
            } else {
                this._Glide(_i2, this._getMsg(_position, "mainl"));
            }
        } else {
            if (this._isFound("ir", this._getMsg(_position, "id"))) {
                this._Glide(_i2, this._getIdPath("ir", this._getMsg(_position, "id")));
            } else {
                this._Glide(_i2, this._getMsg(_position, "prv"));
            }
        }
    }

    private String lstr;
    private boolean likebool;
    private String likemsgidtmp;
    private String templike;

    private void _BinderSetLike(final LinearLayout back, final TextView liket1, final TextView like, final int pos, final int lpos) {
        liket1.setOnLongClickListener(view1 -> {
            this.templike = "❤";
            this.likemsgidtmp = this._getMsg(pos, "id");
            if (this.getlike(this._getMsg(pos, "id")).equals(this.templike)) {
                this.templike = "";
            }
            this._sendLike(this.likemsgidtmp, this.templike);
            this.tmap = new Gson().fromJson(this.msg.getString(this.PHONE.getString("UID", "").concat(String.valueOf((long) (pos + 1)).concat("cmap")), ""), new TypeToken<HashMap<String, Object>>() {
            }.getType());
            this._updateMsg(this.likemsgidtmp, "sidelike", this.templike);
            this.setlike(this.likemsgidtmp, this.templike);
            ((BaseAdapter) this.recyclerview1.getAdapter()).notifyDataSetChanged();
            this._closeBottom();
            return true;
        });
        this.lstr = this.getlike(this._getMsg(pos, "id"));

        if (!this.lstr.equals("")) {
            if (this.asp.getString(String.valueOf(pos), "").equals("")) {
                this.asp.edit().putString(String.valueOf(pos), "true").apply();
                this._Anime(pos + "like", back);
                like.setVisibility(View.VISIBLE);
            } else {
                like.setVisibility(View.VISIBLE);
            }
            like.setText(this.lstr);
        } else {
            if (this.asp.getString(String.valueOf(pos), "").equals("true")) {
                this.asp.edit().putString(String.valueOf(pos), "").apply();
                this._Anime(pos + "like2", back);
                like.setVisibility(View.GONE);
            } else {
                like.setVisibility(View.GONE);
            }
        }
    }

    private String temp;
    private boolean temp2;

    public void _BinderSetStage(double _position, LinearLayout anil, LinearLayout BAC, ImageView _i1, LinearLayout _li, View _lextra) {
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(this._getMsg(_position, "by"))) {
            this.temp = this._getMsg(_position, "stage");
            this.temp2 = true;
            if ("1".equals(this.temp)) {
                _i1.setImageResource(R.drawable.upload_time);
                this.temp2 = false;
            } else if ("2".equals(this.temp)) {
                _i1.setImageResource(R.drawable.singletick);
            } else if ("3".equals(this.temp)) {
                _i1.setImageResource(R.drawable.doubletick);
            } else {
                _i1.setImageResource(R.drawable.bluetick);
            }

            _lextra.setVisibility(View.VISIBLE);
            if (_position == 0) {
                _li.setVisibility(View.VISIBLE);
            } else {
                if (this._GetTime("dd/MM/yyyy hh:mm a", this._getMsg(_position, "ms")).equals(this._GetTime("dd/MM/yyyy hh:mm a", this._getMsg(_position - 1, "ms")))) {
                    if (this._getMsg(_position, "by").equals(this._getMsg(_position - 1, "by"))) {
                        if (this.temp2) {
                            this.temp = this._getMsg(_position - 1, "stage");
                            if ("1".equals(this.temp)) {
                                _li.setVisibility(View.VISIBLE);
                            } else if ("2".equals(this.temp)) {
                                _li.setVisibility(View.VISIBLE);
                                _lextra.setVisibility(View.GONE);
                            } else if ("3".equals(this.temp)) {
                                _li.setVisibility(View.VISIBLE);
                                _lextra.setVisibility(View.GONE);

                            } else {

                                if (this.asp.getString(_position + "linearinfo", "").equals("")) {
                                    this.asp.edit().putString(_position + "linearinfo", "true").apply();
                                    {
                                        final AutoTransition autoTransition = new AutoTransition();
                                        autoTransition.setDuration(200);
                                        TransitionManager.beginDelayedTransition(BAC, autoTransition);
                                        _li.setVisibility(View.GONE);
                                    }
                                    _lextra.setVisibility(View.GONE);

                                } else {
                                    _li.setVisibility(View.GONE);
                                    _lextra.setVisibility(View.GONE);
                                }


                            }
                        } else {
                            _li.setVisibility(View.VISIBLE);
                        }
                    } else {
                        _li.setVisibility(View.VISIBLE);
                    }
                } else {
                    _li.setVisibility(View.VISIBLE);
                }
            }
        }

    }


    public void _BinderSetTextGV(double _position, TextView _t1) {
        if ("".equals(this._getMsg(_position, "msg"))) {
            _t1.setVisibility(View.GONE);
            _t1.setTextSize(15);
        } else {
            _t1.setVisibility(View.VISIBLE);
            if (2 == this._getMsg(_position, "msg").length()) {
                _t1.setTextSize(25);
            } else {
                _t1.setTextSize(15);
            }
        }
    }


    public void _BinderLeftRightOrientation(double _position, View _lextra, View _l1, View _lL, View _lR, TextView _tT, View _li, ImageView _i1, TextView _t2, View _lData) {
        _lextra.setVisibility(View.VISIBLE);
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(this._getMsg(_position, "by"))) {
            _lL.setVisibility(View.VISIBLE);
            _lR.setVisibility(View.GONE);
            _tT.setVisibility(View.GONE);
            _i1.setVisibility(View.VISIBLE);
            _li.setVisibility(View.VISIBLE);
            _li.setOnClickListener(_view -> {
                this._Anime(String.valueOf((long) (_position)), _lData);
                _tT.setText(this._getTime(Double.parseDouble(this._getMsg(_position, "ms"))));
                _tT.setVisibility(View.VISIBLE);
            });
            _tT.setOnClickListener(v -> {
                this._Anime(String.valueOf((long) (_position)), _lData);
                _tT.setText(this._GetTime("hh:mm a", this._getMsg(_position, "ms")));
            });
        } else {
            _tT.setText(this._getTime(Double.parseDouble(this._getMsg(_position, "ms"))));
            _tT.setOnClickListener(v -> {
                this._Anime(String.valueOf((long) (_position)), _lData);
                _tT.setText(this._GetTime("hh:mm a", this._getMsg(_position, "ms")));
            });
            _tT.setVisibility(View.VISIBLE);
            _lL.setVisibility(View.GONE);
            _lR.setVisibility(View.VISIBLE);
            _i1.setVisibility(View.GONE);
            _li.setOnClickListener(null);
            if ("".equals(this.list.getString(this._getMsg(_position, "id").concat("saw"), ""))) {
                this.list.edit().putString(this._getMsg(_position, "id").concat("saw"), "true").apply();
                this._send(this._getMsg(_position, "id"));
            } else {

            }

            if (_position == 0) {
                _li.setVisibility(View.VISIBLE);
            } else {
                if (this._GetTime("dd/MM/yyyy hh:mm a", this._getMsg(_position, "ms")).equals(this._GetTime("dd/MM/yyyy hh:mm a", this._getMsg(_position - 1, "ms")))) {
                    if (this._getMsg(_position, "by").equals(this._getMsg(_position - 1, "by"))) {
                        _li.setVisibility(View.GONE);
                        _lextra.setVisibility(View.GONE);
                    } else {
                        _li.setVisibility(View.VISIBLE);
                    }
                } else {
                    _li.setVisibility(View.VISIBLE);
                }
            }
        }
        if ("".equals(this._getMsg(_position, "mbool"))) {
            _t2.setVisibility(View.GONE);
            _t2.setOnClickListener(null);
        } else {
            _t2.setTextColor(Color.parseColor(this.jay.col(1, 2)));
            _t2.setVisibility(View.VISIBLE);
            _t2.setOnClickListener(view -> {
                final Intent adduserchat = new Intent();
                adduserchat.setClass(this.getContext(), TextFullViewActivity.class);
                adduserchat.putExtra("pos", Double.toString(_position));
                this.startActivity(adduserchat);
            });
        }
    }


    public void _Binder234LeftRight(double _position, View _lL2, View _lR2, View _l8) {
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(this._getMsg(_position, "by"))) {
            _lL2.setVisibility(View.VISIBLE);
            _lR2.setVisibility(View.GONE);
        } else {
            _lL2.setVisibility(View.GONE);
            _lR2.setVisibility(View.VISIBLE);
        }
    }

    public int _findPositionOfMsg(final int Pos, final String MsgID) {
        int ret = -1;
        for (int i = Pos - 1; i >= 0; i--) {
            if (this._getMsg(i, "id").equals(MsgID)) {
                ret = i;
                final int nv = (Pos - ret) / this.MIN;
                for (int j = 0; j <= nv; j++) {
                    this._loadmsgforscrolling();
                }
                break;
            }
        }


        if (ret == -1) {
            Toast.makeText(this.getContext(), "Unable to Find Message", Toast.LENGTH_SHORT).show();
            return -1;
        }
        ((BaseAdapter) this.recyclerview1.getAdapter()).notifyDataSetChanged();

        for (int i = 0; i < this.listMap.size(); i++) {
            if (this._getMsg(i, "id").equals(MsgID)) {
                ret = i;
                break;
            }
        }

        this._scrollto(ret);

        return ret;
    }

    public void _scrollto(int _pos) {
        this.recyclerview1.smoothScrollToPosition(_pos);
    }

    private final ObjectAnimator swop = new ObjectAnimator();
    private final ObjectAnimator swcl = new ObjectAnimator();

    private double currentT2;
    public String _getTime(final double _ms) {
        currentT2 = Calendar.getInstance().getTimeInMillis() - _ms;
        if (currentT2 < 5001) {
            return "Just now";
        } else if (currentT2 < 60000) {
            return String.valueOf((long) (currentT2 / 1000)).concat(" Seconds ago");
        } else if (currentT2 < 3600000) {
            return String.valueOf((long) (currentT2 / 60000)).concat(" Minutes ago");
        } else if (currentT2 < 86400000) {
            return String.valueOf((long) (currentT2 / 3600000)).concat(" Hours ago");
        } else if (currentT2 < 2628002880d) {
            return String.valueOf((long) (currentT2 / 86400000)).concat(" Days ago");
        } else if (currentT2 < 31536034560d) {
            return String.valueOf((long) (currentT2 / 2628002880d)).concat(" Months ago");
        } else {
            return String.valueOf((long) (currentT2 / 31536034560d)).concat(" Years ago");
        }
    }

    public void anim(final View BACK) {
        this.swop.setTarget(BACK);
        this.swop.setPropertyName("translationX");
        this.swop.setFloatValues(SketchwareUtil.getDip(Objects.requireNonNull(this.getContext()), 100));
        this.swop.setDuration(200);
        this.swop.start();
        this._timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ChatFragmentActivity.this.requireActivity().runOnUiThread(() -> {
                    ChatFragmentActivity.this.swcl.setTarget(BACK);
                    ChatFragmentActivity.this.swcl.setPropertyName("translationX");
                    ChatFragmentActivity.this.swcl.setFloatValues(SketchwareUtil.getDip(ChatFragmentActivity.this.getContext(), 0));
                    ChatFragmentActivity.this.swcl.setDuration(200);
                    ChatFragmentActivity.this.swcl.start();
                });
            }
        }, 101);
    }

    private float sx, isx;
    private float ex, iex;
    private float val;

    @SuppressLint("ClickableViewAccessibility")
    public void TOUCH(final View BACK, final int _position, final int pos2) {
        BACK.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.sx = motionEvent.getX();
                    this.isx = BACK.getTranslationX();
                    break;
                case MotionEvent.ACTION_UP:
                    this.ex = motionEvent.getX();
                    this.iex = BACK.getTranslationX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.ex = motionEvent.getX();
                    if (this.sx + SketchwareUtil.getDip(this.getContext(), 10) < this.ex) {
                        this.anim(BACK);
                        this.swiped(_position, pos2);
                    }
                    break;
            }
            return true;
        });
    }

    public class Listview1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;
        private LinearLayout BACK, animatel;
        private LinearLayout linearSpace;
        private LinearLayout linearData2;
        private LinearLayout linearExtra;
        private LinearLayout linearData;
        private LinearLayout linearinfo;
        private LinearLayout linearR2;
        private LinearLayout linearL2;
        private LinearLayout linear10;
        private LinearLayout linear8;
        private LinearLayout linear9;
        private LinearLayout linear4;
        private LinearLayout linear2;
        private LinearLayout linearL;
        private LinearLayout linear1;
        private LinearLayout linearR;
        private ImageView imageview1;
        private ImageView imageview2;
        private ImageView imageview3;
        private TextView textviewTime;
        private TextView textviewUR;
        private TextView textview3;
        private TextView textview4;
        private TextView textview1;
        private TextView textview2;
        private TextView imageviewlike;

        public Listview1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @Override
        public int getCount() {
            return this._data.size();
        }

        @Override
        public HashMap<String, Object> getItem(final int _index) {
            return this._data.get(_index);
        }

        @Override
        public long getItemId(final int _index) {
            return _index;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public View getView(int _position1, final View _v, final ViewGroup _container) {
            final LayoutInflater _inflater = ChatFragmentActivity.this.getLayoutInflater();
            View _view = _v;

            if (_view == null) {
                _view = _inflater.inflate(R.layout.messagecus, null);
                this.BACK = _view.findViewById(R.id.BACK);
                this.linearSpace = _view.findViewById(R.id.linearSpace);
                this.linearData2 = _view.findViewById(R.id.linearData2);
                this.linear10 = _view.findViewById(R.id.linear10);
                this.linearData = _view.findViewById(R.id.linearData);
                this.linearL2 = _view.findViewById(R.id.linearL2);
                this.linear8 = _view.findViewById(R.id.linear8);
                this.linearR2 = _view.findViewById(R.id.linearR2);
                this.linear9 = _view.findViewById(R.id.linear9);
                this.linear4 = _view.findViewById(R.id.linear4);
                this.linear2 = _view.findViewById(R.id.linear2);
                this.imageview2 = _view.findViewById(R.id.imageview2);
                this.imageview3 = _view.findViewById(R.id.imageview3);
                this.textview3 = _view.findViewById(R.id.textview3);
                this.textview4 = _view.findViewById(R.id.textview4);
                this.textviewUR = _view.findViewById(R.id.textviewUR);
                this.textview1 = _view.findViewById(R.id.textview1);
                this.textviewTime = _view.findViewById(R.id.textviewTime);
                this.textview2 = _view.findViewById(R.id.textview2);
                this.linearL = _view.findViewById(R.id.linearL);
                this.linear1 = _view.findViewById(R.id.linear1);
                this.linearR = _view.findViewById(R.id.linearR);
                this.linearinfo = _view.findViewById(R.id.linearinfo);
                this.imageview1 = _view.findViewById(R.id.imageview1);
                this.textviewUR = _view.findViewById(R.id.textviewUR);
                this.linearExtra = _view.findViewById(R.id.linearExtra);
                this.imageviewlike = _view.findViewById(R.id.imageviewlike);
                this.animatel = _view.findViewById(R.id.animatel);
                ChatFragmentActivity.this._SetBackground(this.textviewUR, 50, 20, ChatFragmentActivity.this.jay.col(0, 2), true);
                this.textviewUR.setTextColor(Color.parseColor(ChatFragmentActivity.this.jay.col(1, 1)));
                ChatFragmentActivity.this._SetBackground(this.textview1, 10, 0, ChatFragmentActivity.this.jay.col(0, 1), false);
                ChatFragmentActivity.this._SetBackground(this.linearinfo, 50, 20, ChatFragmentActivity.this.jay.col(0, 1), false);
                ChatFragmentActivity.this._SetBackground(this.linear2, 10, 0, ChatFragmentActivity.this.jay.col(0, 1), false);
                ChatFragmentActivity.this._SetBackground(this.linear4, 10, 0, ChatFragmentActivity.this.jay.col(0, 1), false);
                ChatFragmentActivity.this._SetBackground(this.linear4, 10, 0, ChatFragmentActivity.this.jay.col(0, 1), false);

                ChatFragmentActivity.this._SetBackground(this.textview4, 15, 0, ChatFragmentActivity.this.jay.col(0, 1), false);

                ChatFragmentActivity.this._SetBackground(this.textview2, 10, 20, ChatFragmentActivity.this.jay.col(0, 1), false);
//                imageviewlike.setColorFilter(Color.parseColor(jay.col(0,1)));
                this.textviewUR.setTypeface(Typeface.createFromAsset(ChatFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
                this.textview3.setTypeface(Typeface.createFromAsset(ChatFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
                this.textview4.setTypeface(Typeface.createFromAsset(ChatFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
                this.textview1.setTypeface(Typeface.createFromAsset(ChatFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
                this.textview2.setTypeface(Typeface.createFromAsset(ChatFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
                this.textviewTime.setTypeface(Typeface.createFromAsset(ChatFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
                for (final TextView tv : new TextView[]{this.textview1, this.textview3, this.textview4, this.textviewTime})
                    tv.setTextColor(Color.parseColor(ChatFragmentActivity.this.jay.col(1, 1)));
                this.textview4.setLinkTextColor(Color.parseColor(ChatFragmentActivity.this.jay.col(1, 1)));
                this.textview1.setLinkTextColor(Color.parseColor(ChatFragmentActivity.this.jay.col(1, 1)));

            } else {
                this.BACK = _view.findViewById(R.id.BACK);
                this.linearSpace = _view.findViewById(R.id.linearSpace);
                this.linearData2 = _view.findViewById(R.id.linearData2);
                this.linear10 = _view.findViewById(R.id.linear10);
                this.linearData = _view.findViewById(R.id.linearData);
                this.linearL2 = _view.findViewById(R.id.linearL2);
                this.linear8 = _view.findViewById(R.id.linear8);
                this.linearR2 = _view.findViewById(R.id.linearR2);
                this.linear9 = _view.findViewById(R.id.linear9);
                this.linear4 = _view.findViewById(R.id.linear4);
                this.linear2 = _view.findViewById(R.id.linear2);
                this.imageview2 = _view.findViewById(R.id.imageview2);
                this.imageview3 = _view.findViewById(R.id.imageview3);
                this.textview3 = _view.findViewById(R.id.textview3);
                this.textview4 = _view.findViewById(R.id.textview4);
                this.textviewUR = _view.findViewById(R.id.textviewUR);
                this.textview1 = _view.findViewById(R.id.textview1);
                this.textviewTime = _view.findViewById(R.id.textviewTime);
                this.textview2 = _view.findViewById(R.id.textview2);
                this.linearL = _view.findViewById(R.id.linearL);
                this.linear1 = _view.findViewById(R.id.linear1);
                this.linearR = _view.findViewById(R.id.linearR);
                this.linearinfo = _view.findViewById(R.id.linearinfo);
                this.imageview1 = _view.findViewById(R.id.imageview1);
                this.textviewUR = _view.findViewById(R.id.textviewUR);
                this.linearExtra = _view.findViewById(R.id.linearExtra);
                this.imageviewlike = _view.findViewById(R.id.imageviewlike);
                this.animatel = _view.findViewById(R.id.animatel);
            }
            final String CMD = ChatFragmentActivity.this.listMap.get(_position1).get("timeT").toString();
            if (CMD.equals("PAD")) {
                this.linearSpace.setVisibility(View.VISIBLE);
                this.linearData.setVisibility(View.GONE);
                this.linearData2.setVisibility(View.GONE);
                this.textviewUR.setVisibility(View.GONE);
                this.linear10.setVisibility(View.GONE);
                this.BACK.setOnTouchListener(null);
            } else {

                int _position = Integer.parseInt(CMD);
                this.linearData.setVisibility(View.VISIBLE);

                if (ChatFragmentActivity.this.unreadpos == _position) {
                    this.textviewUR.setText("Unread Messages");
                    this.linearSpace.setVisibility(View.VISIBLE);
                    this.textviewUR.setVisibility(View.VISIBLE);
                } else {
                    this.linearSpace.setVisibility(View.GONE);
                }

//                this.textviewTime.setTextSize(8);
                this.textview1.setText(ChatFragmentActivity.this._getMsg(_position, "msg"));

                ChatFragmentActivity.this.TOUCH(this.BACK, _position, _position1);

                this.textview1.setOnClickListener(view -> {
                    ChatFragmentActivity.this.bottomCURRENT = _position;
                    ChatFragmentActivity.this.bottomCURRENTpos2 = _position1;
                    ChatFragmentActivity.this._openBottom();
                });

                if (ChatFragmentActivity.this.bind) {
                    if ("".equals(ChatFragmentActivity.this.list.getString(Objects.requireNonNull(ChatFragmentActivity.this.DataMap.get("U")).toString().concat("lv"), ""))) {
                        ChatFragmentActivity.this.list.edit().putString(Objects.requireNonNull(ChatFragmentActivity.this.DataMap.get("U")).toString().concat("lv"), String.valueOf((long) (_position + 1))).apply();
                    } else {
                        if (Double.parseDouble(ChatFragmentActivity.this.list.getString(Objects.requireNonNull(ChatFragmentActivity.this.DataMap.get("U")).toString().concat("lv"), "")) < (_position + 1)) {
                            ChatFragmentActivity.this.list.edit().putString(Objects.requireNonNull(ChatFragmentActivity.this.DataMap.get("U")).toString().concat("lv"), String.valueOf((long) (_position + 1))).apply();
                        }
                    }
                }
                ChatFragmentActivity.this._BinderSetTextGV(_position, this.textview1);
                ChatFragmentActivity.this._BinderLeftRightOrientation(_position, this.linearExtra, this.linear1, this.linearL, this.linearR, this.textviewTime, this.linearinfo, this.imageview1, this.textview2, this.linearData);
                ChatFragmentActivity.this._BinderSetStage(_position, this.animatel, this.linearData, this.imageview1, this.linearinfo, this.linearExtra);
                ChatFragmentActivity.this._BinderSetLike(this.linear1, this.textview1, this.imageviewlike, _position, _position1);
                if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(ChatFragmentActivity.this._getMsg(_position, "by"))) {
                    this.linear1.setGravity(Gravity.TOP | Gravity.RIGHT);
                    this.linear10.setGravity(Gravity.TOP | Gravity.RIGHT);
                } else {
                    this.linear1.setGravity(Gravity.TOP | Gravity.LEFT);
                    this.linear10.setGravity(Gravity.TOP | Gravity.LEFT);
                }
                if (17 < ChatFragmentActivity.this._getMsg(_position, "msg").length()) {
                    this.linear1.setOrientation(LinearLayout.VERTICAL);
                } else {
                    this.linear1.setOrientation(LinearLayout.HORIZONTAL);
                }
                if ("1".equals(ChatFragmentActivity.this._getMsg(_position, "typ"))) {
                    this.linearData2.setVisibility(View.GONE);
                    this.linear4.setVisibility(View.GONE);
                    if (ChatFragmentActivity.this.animBool) {
                        this.linearData.setAlpha((float) (0));
                        this.linearData.animate().translationY(0).alpha(1.0f).setListener(null);
                    }
                } else {
                    if (ChatFragmentActivity.this.animBool) {
                        this.linearData.setAlpha((float) (0));
                        this.linearData2.setAlpha((float) (0));
                        this.linearData.animate().translationY(0).alpha(1.0f).setListener(null);
                        this.linearData2.animate().translationY(0).alpha(1.0f).setListener(null);
                    }
                    this.linearData2.setVisibility(View.VISIBLE);
                    ChatFragmentActivity.this._Binder234LeftRight(_position, this.linearL2, this.linearR2, this.linear8);
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ChatFragmentActivity.this._getMsg(_position, "by"))) {
                        this.linear8.setGravity(Gravity.TOP | Gravity.RIGHT);
                    } else {
                        this.linear8.setGravity(Gravity.TOP | Gravity.LEFT);
                    }
                    if ("2".equals(ChatFragmentActivity.this._getMsg(_position, "typ"))) {
                        ChatFragmentActivity.this._BinderTyp2(_position, this.linear4, this.linear9, this.linear2, this.imageview2);
                    } else {
                        if ("3".equals(ChatFragmentActivity.this._getMsg(_position, "typ"))) {
                            ChatFragmentActivity.this._BinderTye3(_position, this.linear4, this.linear9, this.imageview3, this.textview3);
                        } else {
                            if ("999".equals(ChatFragmentActivity.this._getMsg(_position, "typ"))) {
                                ChatFragmentActivity.this._BinderTyp999(_position, this.linear4, this.linear9, this.imageview3, this.textview3);
                            } else {

                            }
                        }
                    }
                }
                if ("".equals(ChatFragmentActivity.this._getMsg(_position, "mark"))) {
                    this.linear10.setVisibility(View.GONE);
                } else {
                    this.linear10.setVisibility(View.VISIBLE);
                    try {
                        ChatFragmentActivity.this.tempmap = new Gson().fromJson(ChatFragmentActivity.this.msg.getString(ChatFragmentActivity.this._getMsg(_position, "mark"), ""), new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                        if (ChatFragmentActivity.this.tempmap.containsKey("msg")) {
                            this.textview4.setText(Objects.requireNonNull(ChatFragmentActivity.this.tempmap.get("msg")).toString());
                            //MARK1 - marked message on click
                            this.linear10.setOnClickListener(v -> {
                                ChatFragmentActivity.this._findPositionOfMsg(_position, ChatFragmentActivity.this._getMsg(_position, "mark"));
                            });
                        } else {

                            this.textview4.setText("Message Not Found");
                        }

                    } catch (final Exception e) {
                        this.textview4.setText("Message Not Found");
                    }
                    if (ChatFragmentActivity.this.animBool) {
                        this.linear10.setAlpha((float) (0));
                        this.linear10.animate().translationY(0).alpha(0.7f).setListener(null);
                    }
                }

                if (ChatFragmentActivity.this.unreadpos != _position) {
                    if (_position == 0) {
                        ChatFragmentActivity.this.msgDate = ChatFragmentActivity.this._GetTime("dd/MM/yyyy", ChatFragmentActivity.this._getMsg(0, "ms"));
                        ChatFragmentActivity.this.tmpCal2 = Calendar.getInstance();
                        ChatFragmentActivity.this.nowDate = new SimpleDateFormat("dd/MM/yyyy").format(ChatFragmentActivity.this.tmpCal2.getTime());
                        if (ChatFragmentActivity.this.msgDate.equals(ChatFragmentActivity.this.nowDate)) {
                            this.textviewUR.setText("Today");
                        } else {
                            this.textviewUR.setText(ChatFragmentActivity.this.msgDate);
                        }
                        this.linearSpace.setVisibility(View.VISIBLE);
                        this.textviewUR.setVisibility(View.VISIBLE);
                    } else {
                        if (!ChatFragmentActivity.this._GetTime("dd/MM/yyyy", ChatFragmentActivity.this._getMsg(_position, "ms")).equals(ChatFragmentActivity.this._GetTime("dd/MM/yyyy", ChatFragmentActivity.this._getMsg(_position - 1, "ms")))) {
                            this.linearSpace.setVisibility(View.VISIBLE);
                            this.textviewUR.setVisibility(View.VISIBLE);
                            ChatFragmentActivity.this.msgDate = ChatFragmentActivity.this._GetTime("dd/MM/yyyy", ChatFragmentActivity.this._getMsg(_position, "ms"));
                            ChatFragmentActivity.this.tmpCal2 = Calendar.getInstance();
                            ChatFragmentActivity.this.nowDate = new SimpleDateFormat("dd/MM/yyyy").format(ChatFragmentActivity.this.tmpCal2.getTime());
                            if (ChatFragmentActivity.this.msgDate.equals(ChatFragmentActivity.this.nowDate)) {
                                this.textviewUR.setText("Today");
                            } else {
                                this.textviewUR.setText(ChatFragmentActivity.this.msgDate);
                            }

                        }
                    }
                }
            }
            return _view;
        }
    }

    private String msgDate, nowDate;
    private ArrayList<HashMap<String, Object>> EmojiLmap;

    public class Gridview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Gridview1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @Override
        public int getCount() {
            return this._data.size();
        }

        @Override
        public HashMap<String, Object> getItem(final int _index) {
            return this._data.get(_index);
        }

        @Override
        public long getItemId(final int _index) {
            return _index;
        }

        @Override
        public View getView(int _position, final View _v, final ViewGroup _container) {
            final LayoutInflater _inflater = ChatFragmentActivity.this.getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.emj, null);
            }

            LinearLayout linear1 = _view.findViewById(R.id.linear1);
            TextView textview1 = _view.findViewById(R.id.textview1);
            textview1.setText(this._data.get(_position).get("v").toString());
            linear1.setOnClickListener(v -> {
                ChatFragmentActivity.this.templike = this._data.get(_position).get("v").toString();
                ChatFragmentActivity.this.likemsgidtmp = ChatFragmentActivity.this._getMsg(ChatFragmentActivity.this.bottomCURRENT, "id");
                if (ChatFragmentActivity.this.getlike(ChatFragmentActivity.this._getMsg(ChatFragmentActivity.this.bottomCURRENT, "id")).equals(ChatFragmentActivity.this.templike)) {
                    ChatFragmentActivity.this.templike = "";
                }
                ChatFragmentActivity.this._sendLike(ChatFragmentActivity.this.likemsgidtmp, ChatFragmentActivity.this.templike);
                ChatFragmentActivity.this.tmap = new Gson().fromJson(ChatFragmentActivity.this.msg.getString(ChatFragmentActivity.this.PHONE.getString("UID", "").concat(String.valueOf((long) (ChatFragmentActivity.this.bottomCURRENT + 1)).concat("cmap")), ""), new TypeToken<HashMap<String, Object>>() {
                }.getType());
                ChatFragmentActivity.this._updateMsg(ChatFragmentActivity.this.likemsgidtmp, "sidelike", ChatFragmentActivity.this.templike);
                ChatFragmentActivity.this.setlike(ChatFragmentActivity.this.likemsgidtmp, ChatFragmentActivity.this.templike);
                ((BaseAdapter) ChatFragmentActivity.this.recyclerview1.getAdapter()).notifyDataSetChanged();
                ChatFragmentActivity.this._closeBottom();
            });
            return _view;
        }
    }

}
