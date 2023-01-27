package com.jayaraj.CrazyChat7.ChatHomeActs;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ChatstatusFragmentActivity extends Fragment {
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private final double key = 0;
    private final HashMap<String, Object> DataMap = new HashMap<>();
    private final double n = 0;
    private HashMap<String, Object> smap = new HashMap<>();
    private final double n1 = 0;
    private String id = "";

    private ArrayList<HashMap<String, Object>> imgs = new ArrayList<>();

    private LinearLayout linear3;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private TextView textview1,friendtxt;
    private ImageView imageview1;
    private RecyclerView recyclerview1;

    private DatabaseReference dbSendProfile = this._firebase.getReference("rand");
    private DatabaseReference dbSend = this._firebase.getReference("SenderUidRand");

    private ChildEventListener _dbSendProfile_child_listener;
    private FirebaseAuth auth;
    private SharedPreferences PHONE;
    private SharedPreferences ChatList;
    private SharedPreferences spbts;

    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    private Jay jay;


    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater _inflater, @Nullable final ViewGroup _container, @Nullable final Bundle _savedInstanceState) {
        final View _view = _inflater.inflate(R.layout.chatstatus_fragment, _container, false);
        this.initialize(_view);
        com.google.firebase.FirebaseApp.initializeApp(this.requireContext());
        this.initializeLogic();
        return _view;
    }

    private void initialize(final View _view) {
        this.linear3 = _view.findViewById(R.id.linear3);
        this.linear1 = _view.findViewById(R.id.linear1);
        this.linear2 = _view.findViewById(R.id.linear2);
        this.textview1 = _view.findViewById(R.id.textview1);
        this.friendtxt = _view.findViewById(R.id.friendtxt);
        this.imageview1 = _view.findViewById(R.id.imageview1);
        this.recyclerview1 = _view.findViewById(R.id.recyclerview1);
        this.auth = FirebaseAuth.getInstance();
        this.PHONE = this.requireContext().getSharedPreferences("PHONE", Context.MODE_PRIVATE);
        this.ChatList = this.requireContext().getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.spbts = this.requireContext().getSharedPreferences("botomsheet", Context.MODE_PRIVATE);

        this.jay = new Jay(this.requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.friendtxt.setVisibility(View.GONE);
        this.fs.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Friends").whereEqualTo("id", this.PHONE.getString("UID", "")).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().getDocuments().size()==0){
                    this.friendtxt.setVisibility(View.GONE);
                } else {
                    this._Anime("friend", this.linear1);
                    this.friendtxt.setVisibility(View.VISIBLE);
                }
            } else {
                this.friendtxt.setVisibility(View.GONE);
                System.out.println("Network Failure");
            }
        });
        this._dbSendProfile_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (ChatstatusFragmentActivity.this.smap.containsKey(_childKey)) {

                } else {
                    ChatstatusFragmentActivity.this.smap.put(_childKey, String.valueOf((long) (ChatstatusFragmentActivity.this.imgs.size())));
                    ChatstatusFragmentActivity.this.imgs.add(_childValue);
                    Objects.requireNonNull(ChatstatusFragmentActivity.this.recyclerview1.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                assert _childValue != null;
                ChatstatusFragmentActivity.this.id = Objects.requireNonNull(_childValue.get("id")).toString();
                if (ChatstatusFragmentActivity.this.smap.containsKey(ChatstatusFragmentActivity.this.id)) {
                    ChatstatusFragmentActivity.this.imgs.get((int) Double.parseDouble(Objects.requireNonNull(ChatstatusFragmentActivity.this.smap.get(ChatstatusFragmentActivity.this.id)).toString())).put("like", Objects.requireNonNull(_childValue.get("like")).toString());
                } else {
                    ChatstatusFragmentActivity.this.smap.put(ChatstatusFragmentActivity.this.id, String.valueOf((long) (ChatstatusFragmentActivity.this.imgs.size())));
                    ChatstatusFragmentActivity.this.imgs.add(_childValue);
                }

                Objects.requireNonNull(ChatstatusFragmentActivity.this.recyclerview1.getAdapter()).notifyDataSetChanged();
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
//        dbSendProfile.addChildEventListener(_dbSendProfile_child_listener);

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
    public void _Anime(String _Sp, View _Linear) {
        final AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(200);
        TransitionManager.beginDelayedTransition((LinearLayout) _Linear, autoTransition);
    }
    private void initializeLogic() {
        this._SetBackground(this.friendtxt, 40, 0, this.jay.col(0, 1), false);
        this.friendtxt.setTextColor(Color.parseColor(this.jay.col(1, 1)));
        this.friendtxt.setTypeface(Typeface.createFromAsset(this.requireActivity().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        final StaggeredGridLayoutManager slmrecyclerview1 = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        this.recyclerview1.setLayoutManager(slmrecyclerview1);
        this.recyclerview1.setAdapter(new Recyclerview1Adapter(this.imgs));
        this.DataMap.put("P", this.PHONE.getString("PHONE", ""));
        this.DataMap.put("U", this.PHONE.getString("UID", ""));
//        Glide.with(requireContext()).load(Uri.parse("jk")).into(imageview1);
        this.recyclerview1.setVisibility(View.GONE);

        this.textview1.setText(this.ChatList.getString(this.PHONE.getString("UID", "").concat("n"), "").concat("'s Gallery"));
        this.dbSend = this._firebase.getReference("chat/".concat(this.PHONE.getString("UID", "").concat("/notify")));


//        dbSendProfile.removeEventListener(_dbSendProfile_child_listener);
        this.dbSendProfile = this._firebase.getReference("PIMGS/uid/".concat(this.PHONE.getString("UID", "")));
        this.dbSendProfile.addChildEventListener(this._dbSendProfile_child_listener);
        this.friendtxt.setOnClickListener(v -> {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
            dialog.setTitle("Unfriend");
            dialog.setMessage("Click 'Remove' to remove him from your Friends list");
            dialog.setPositiveButton("Remove", (dialogInterface, i) -> {
                this.friendtxt.setText("Removing...");
                this.fs.collection("UserProfile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Friends").document(this.PHONE.getString("UID", "")).delete().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        this.fs.collection("UserProfile").document(this.PHONE.getString("UID", "")).collection("Friends").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).delete();
                        Toast.makeText(this.getContext(), "Removed from friend list", Toast.LENGTH_SHORT).show();
                        this.friendtxt.setText("Removed");
                        this.friendtxt.setOnClickListener(w -> {
                            Toast.makeText(this.getContext(), "Already Removed", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        this.friendtxt.setText("Remove From Friend List");
                        Toast.makeText(this.getContext(), "Somthing Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            dialog.setNeutralButton("Cancel", (dialogInterface, i) -> {});
            dialog.show();
        });
        this.dbSendProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot _dataSnapshot) {
                ChatstatusFragmentActivity.this.imgs = new ArrayList<>();
                try {
                    final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (final DataSnapshot _data : _dataSnapshot.getChildren()) {
                        final HashMap<String, Object> _map = _data.getValue(_ind);
                        ChatstatusFragmentActivity.this.imgs.add(_map);
                    }
                } catch (final Exception _e) {
                    _e.printStackTrace();
                }
                ChatstatusFragmentActivity.this.smap = new HashMap<>();
                for (int n1 = 0; n1 < ChatstatusFragmentActivity.this.imgs.size(); n1++) {
                    ChatstatusFragmentActivity.this.smap.put(Objects.requireNonNull(ChatstatusFragmentActivity.this.imgs.get(n1).get("id")).toString(), String.valueOf((long) (n1)));
                }
                if (0 == ChatstatusFragmentActivity.this.imgs.size()) {
                    ChatstatusFragmentActivity.this.textview1.setText("Empty Images");
                } else {
                    ChatstatusFragmentActivity.this.recyclerview1.setAdapter(new Recyclerview1Adapter(ChatstatusFragmentActivity.this.imgs));
                    ChatstatusFragmentActivity.this._Anime("a1", ChatstatusFragmentActivity.this.linear1);
                    ChatstatusFragmentActivity.this.recyclerview1.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError _databaseError) {
            }
        });
        this.textview1.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview1.setTextColor(Color.parseColor(this.jay.col(1,1)));

    }

    @Override
    public void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    public void _Glide(ImageView _img, String _url) {
        Glide.with(this.getContext()).load(_url).fitCenter().into(_img);
    }
    private HashMap<String, Object> map = new HashMap<>();
    private Calendar msgC = Calendar.getInstance();
    public void sendReqMsg() {
        this.map = new HashMap<>();
        this.msgC = Calendar.getInstance();
        final int mil = (int) this.msgC.getTimeInMillis();
        this.map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))).concat(String.valueOf(this.msgC.getTimeInMillis()))));
        this.map.put("ms", String.valueOf(mil));
        this.map.put("msg", "Friend request");
        this.map.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.map.put("stage", "0");
        this.map.put("typ", "cus");
        this.map.put("cat", "frdreq");
        this.map.put("gid", "escape");
        this.dbSend.child(Objects.requireNonNull(this.map.get("id")).toString()).updateChildren(this.map);
        this.map.clear();
    }

    public Context getContext(final Context c) {
        return c;
    }

    public Context getContext(final Fragment f) {
        return f.getActivity();
    }

    public Context getContext(final DialogFragment df) {
        return df.getActivity();
    }


    public void _notifyUpdate(String _Key) {
        this.spbts.edit().putString(_Key, String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
    }


    public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;

        public Recyclerview1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater _inflater = (LayoutInflater) ChatstatusFragmentActivity.this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View _v = _inflater.inflate(R.layout.item, null);
            final RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder _holder, int _position1) {
            int _position = _position1;
            final View _view = _holder.itemView;

            LinearLayout linear3 = _view.findViewById(R.id.linear3);
            androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
            RelativeLayout linear2 = _view.findViewById(R.id.linear2);
            ImageView imageview1 = _view.findViewById(R.id.imageview1);
            LinearLayout linear4 = _view.findViewById(R.id.linear4);
            ImageView imageview2 = _view.findViewById(R.id.imageview2);
            TextView textview1 = _view.findViewById(R.id.textview1);
            TextView deltext = _view.findViewById(R.id.deltext);

            ChatstatusFragmentActivity.this._Glide(imageview1, Objects.requireNonNull(ChatstatusFragmentActivity.this.imgs.get(_position).get("url")).toString());
            textview1.setText(Objects.requireNonNull(ChatstatusFragmentActivity.this.imgs.get(_position).get("like")).toString());
            cardview1.setOnClickListener(_view1 -> {
                ChatstatusFragmentActivity.this.spbts.edit().putString("pos", String.valueOf((long) (_position))).apply();
                ChatstatusFragmentActivity.this.spbts.edit().putString("list", new Gson().toJson(ChatstatusFragmentActivity.this.imgs)).apply();
                ChatstatusFragmentActivity.this._notifyUpdate("b");
            });
            deltext.setVisibility(View.GONE);
            linear4.setBackground(new GradientDrawable() {
                public GradientDrawable getIns(final int a, final int b) {
                    setCornerRadius(a);
                    setColor(b);
                    return this;
                }
            }.getIns((int) 20, 0x65000000));

            textview1.setTypeface(Typeface.createFromAsset(ChatstatusFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
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
}
