package com.jayaraj.CrazyChat7.HomeActs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.OtherActs.ChatHomeProfileActivity;
import com.jayaraj.CrazyChat7.ChatHomeActs.ChathomeActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class ChatsFragmentActivity extends Fragment {

    private FloatingActionButton _fab;
    private final double n = 0;
    private final double nn = 0;
    private final HashMap<String, Object> Tmap = new HashMap<>();
    private String Tmp = "";
    private final double n1 = 0;

    private final ArrayList<HashMap<String, Object>> ListChatPhones = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> tlm = new ArrayList<>();

    private TextView textview1;
    private RecyclerView recyclerview1;

    private final Intent adduserchat = new Intent();
    private SharedPreferences ChatList;
    private SharedPreferences msg;
    private SharedPreferences ChatRef;
    private SharedPreferences list;
    private AlertDialog.Builder dialog;
    private SharedPreferences PHONE;
    private Jay jay;

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater _inflater, @Nullable final ViewGroup _container, @Nullable final Bundle _savedInstanceState) {
        final View _view = _inflater.inflate(R.layout.chats_fragment, _container, false);
        this.initialize(_savedInstanceState, _view);
        com.google.firebase.FirebaseApp.initializeApp(this.requireContext());
        this.initializeLogic();
        return _view;
    }

    private void initialize(final Bundle _savedInstanceState, final View _view) {
        this._fab = _view.findViewById(R.id._fab);
        this.PHONE = this.requireContext().getSharedPreferences("PHONE", Context.MODE_PRIVATE);
        this.textview1 = _view.findViewById(R.id.textview1);
        this.recyclerview1 = _view.findViewById(R.id.recyclerview1);
        this.ChatList = this.requireContext().getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.msg = this.requireContext().getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.ChatRef = this.requireContext().getSharedPreferences("ref", Context.MODE_PRIVATE);
        this.list = this.requireContext().getSharedPreferences("list", Context.MODE_PRIVATE);
        this.dialog = new AlertDialog.Builder(this.getContext());

        this.jay = new Jay(this.requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this._fab.setOnClickListener(_view1 -> {
            this.adduserchat.setClass(this.getContext(), AddUserChatActivity.class);
            this.startActivity(this.adduserchat);
        });
    }

    private void initializeLogic() {
        this.ChatRef.registerOnSharedPreferenceChangeListener(this.listChangeListener);
        this.textview1.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this._OnCrt();
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
        this.ChatRef.unregisterOnSharedPreferenceChangeListener(this.listChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        this._LoadChatList();
    }

    public void _OnCrt() {
        this.recyclerview1.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.recyclerview1.setAdapter(new Recyclerview1Adapter(this.ListChatPhones));
        this._LoadChatList();
    }


    public void _LoadChatList() {
        this.ListChatPhones.clear();
        this.Tmp = this.ChatList.getString("list", "");

        if ("".equals(this.Tmp)) {

        } else {
            this.tlm = new Gson().fromJson(this.Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            for (int n1 = 0; n1 < this.tlm.size(); n1++) {
                this.ListChatPhones.add(this.tlm.get(n1));
                this.ListChatPhones.get(this.ListChatPhones.size() - 1).put("lm", this.ChatList.getString(Objects.requireNonNull(this.tlm.get(n1).get("uid")).toString().concat("lm"), ""));
            }
            SketchwareUtil.sortListMap(this.ListChatPhones, "lm", false, false);
        }
        this._ref();


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


    SharedPreferences.OnSharedPreferenceChangeListener listChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
            if (key.equals("ref")) {
                ChatsFragmentActivity.this._LoadChatList();
            }
        }
    };

    public void _ref() {
        Objects.requireNonNull(this.recyclerview1.getAdapter()).notifyDataSetChanged();
        if (this.ListChatPhones.size() == 0) {
            this.textview1.setVisibility(View.VISIBLE);
        } else {
            this.textview1.setVisibility(View.GONE);
        }
    }


    public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;

        public Recyclerview1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater _inflater = (LayoutInflater) ChatsFragmentActivity.this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View _v = _inflater.inflate(R.layout.chatuserlist, null);
            final RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder _holder, int _position1) {
            int _position = _position1;
            final View _view = _holder.itemView;
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


            if ("".equals(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("name")).toString())) {
                textview2.setText("Connecting...");
            } else {
                textview2.setText(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("name")).toString());
            }
            textview3.setText(ChatsFragmentActivity.this.ChatList.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("phone")).toString().concat("lastmsg"), ""));
            if ("".equals(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("img")).toString())) {
                Glide.with(ChatsFragmentActivity.this.requireContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(circleimageview2);
                progressbar1.setVisibility(View.GONE);
            } else {
                Glide.with(ChatsFragmentActivity.this.requireContext()).load(Uri.parse(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("img")).toString())).addListener(new RequestListener<Drawable>() {
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
            circleimageview2.setOnClickListener(view -> {
                final Intent intentact = new Intent();
                intentact.setClass(ChatsFragmentActivity.this.getContext(), ChatHomeProfileActivity.class);
                intentact.putExtra("uid", Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString());
                circleimageview2.setTransitionName("img");
                final android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(ChatsFragmentActivity.this.getActivity(), circleimageview2, "img");
                ChatsFragmentActivity.this.startActivity(intentact, optionsCompat.toBundle());

            });
            textview3.setTypeface(Typeface.createFromAsset(ChatsFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview2.setTypeface(Typeface.createFromAsset(ChatsFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview5.setTypeface(Typeface.createFromAsset(ChatsFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            if ("0".equals(ChatsFragmentActivity.this.msg.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString(), ""))) {
                textview5.setVisibility(View.GONE);
                textview5.setText("");
            } else {
                if ("".equals(ChatsFragmentActivity.this.list.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lv"), ""))) {
                    textview5.setVisibility(View.GONE);
                    textview5.setText("");
                } else {
                    if ((Double.parseDouble(ChatsFragmentActivity.this.msg.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString(), "")) - Double.parseDouble(ChatsFragmentActivity.this.list.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lv"), ""))) == 0) {
                        textview5.setVisibility(View.GONE);
                        textview5.setText("");
                    } else {
                        textview5.setVisibility(View.VISIBLE);
                        textview5.setText(String.valueOf((long) (Double.parseDouble(ChatsFragmentActivity.this.msg.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString(), "")) - Double.parseDouble(ChatsFragmentActivity.this.list.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lv"), "")))));
                        ChatsFragmentActivity.this._SetBackground(textview5, 40, 8, ChatsFragmentActivity.this.jay.col(0, 2), false);
                    }
                }
            }
            if ("".equals(ChatsFragmentActivity.this.ChatList.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lmsg"), ""))) {
                textview3.setVisibility(View.GONE);
            } else {
                textview3.setVisibility(View.VISIBLE);
                textview3.setText(ChatsFragmentActivity.this.ChatList.getString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lmsg"), ""));
            }
            ChatsFragmentActivity.this._SetBackground(textview2, 20, 20, ChatsFragmentActivity.this.jay.col(0, 1), true);
//            _SetBackground(linear6, 100, 0, jay.col(0,1), true);
            textview2.setTextColor(Color.parseColor(ChatsFragmentActivity.this.jay.col(1, 1)));
            textview3.setTextColor(Color.parseColor(ChatsFragmentActivity.this.jay.col(1, 1)));
            textview5.setTextColor(Color.parseColor(ChatsFragmentActivity.this.jay.col(1, 1)));
            linear2.setOnClickListener(_view1 -> {
                ChatsFragmentActivity.this.adduserchat.setClass(ChatsFragmentActivity.this.getContext(), ChathomeActivity.class);
                ChatsFragmentActivity.this.adduserchat.putExtra("UID", Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString());
                ChatsFragmentActivity.this.adduserchat.putExtra("PHONE", Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("phone")).toString());
                ChatsFragmentActivity.this.PHONE.edit().putString("PHONE", ChatsFragmentActivity.this.requireActivity().getIntent().getStringExtra("PHONE")).apply();
                ChatsFragmentActivity.this.startActivity(ChatsFragmentActivity.this.adduserchat);
            });
            linear2.setOnLongClickListener(_view12 -> {
                ChatsFragmentActivity.this.dialog.setTitle("Delete or Clear?");
                ChatsFragmentActivity.this.dialog.setIcon(R.drawable.logo_trans);
                ChatsFragmentActivity.this.dialog.setMessage("Choose your option Clear the chat or Delete the Contact");
                ChatsFragmentActivity.this.dialog.setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface _dialog, final int _which) {
                        ChatsFragmentActivity.this.list.edit().putString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lv"), "").apply();
                        ChatsFragmentActivity.this.ChatList.edit().putString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lmsg"), "").apply();
                        ChatsFragmentActivity.this.msg.edit().putString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString(), "0").apply();
                        ChatsFragmentActivity.this._ref();
                    }
                });
                ChatsFragmentActivity.this.dialog.setNegativeButton("DELETE", (_dialog, _which) -> {
                    ChatsFragmentActivity.this.list.edit().putString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lv"), "").apply();
                    ChatsFragmentActivity.this.msg.edit().putString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString(), "").apply();
                    ChatsFragmentActivity.this.ChatList.edit().putString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("phone")).toString(), "").apply();
                    ChatsFragmentActivity.this.ChatList.edit().putString(Objects.requireNonNull(ChatsFragmentActivity.this.ListChatPhones.get(_position).get("uid")).toString().concat("lmsg"), "").apply();
                    ChatsFragmentActivity.this.ListChatPhones.remove(_position);
                    ChatsFragmentActivity.this.ChatList.edit().putString("list", new Gson().toJson(ChatsFragmentActivity.this.ListChatPhones)).apply();
                    ChatsFragmentActivity.this._ref();
                });
                ChatsFragmentActivity.this.dialog.setNeutralButton("Cancel", (_dialog, _which) -> {
                });
                ChatsFragmentActivity.this.dialog.create().show();
                return true;
            });
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
