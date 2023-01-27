package com.jayaraj.CrazyChat7.HomeActs;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.ChatHomeActs.ChathomeActivity;
import com.jayaraj.CrazyChat7.ImageVidDispActs.ImgfullActivity;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.OtherActs.ChatHomeProfileActivity;
import com.jayaraj.CrazyChat7.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsFragment extends Fragment {
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    HashMap<String, Object> localmap;
    private Jay jay;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View _view = inflater.inflate(R.layout.fragment_friends, container, false);
        this.initialize(_view);
        this.initializelogic();
        return _view;
    }

    RecyclerView listviewrequest, listviewfriends;
    ProgressBar prgreq, prgfri;
    LinearLayout root, reqlay, frilay;
    TextView reqtxt, fritxt, notice;

    SharedPreferences FriendReq, ChatList, ChatRef, pdataSP, msg, list, PHONE;

    ArrayList<HashMap<String, Object>> reqlist = new ArrayList<>();
    ArrayList<HashMap<String, Object>> frilist = new ArrayList<>();

    private Calendar cal = Calendar.getInstance();

    private void initialize(@NonNull final View _view) {

        this.jay = new Jay(this.requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE));
        //linear layouts
        this.root = _view.findViewById(R.id.root);
        this.reqlay = _view.findViewById(R.id.requets);
        this.frilay = _view.findViewById(R.id.friends);
        //text views
        this.reqtxt = _view.findViewById(R.id.tabreq);
        this.fritxt = _view.findViewById(R.id.tabfri);
        //notice textview
        this.notice = _view.findViewById(R.id.notice);
        //listview
        this.listviewrequest = _view.findViewById(R.id.list1);
        this.listviewfriends = _view.findViewById(R.id.list2);
        //progressbar
        this.prgreq = _view.findViewById(R.id.prg1);
        this.prgfri = _view.findViewById(R.id.prg2);

        this.listviewrequest.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.listviewfriends.setLayoutManager(new LinearLayoutManager(this.getContext()));

        this.localmap = new HashMap<>();
        this.FriendReq = this.requireActivity().getSharedPreferences("request", Context.MODE_PRIVATE);
        this.ChatList = this.requireActivity().getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.ChatRef = this.requireActivity().getSharedPreferences("ref", Context.MODE_PRIVATE);
        this.pdataSP = this.requireActivity().getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.msg = this.requireActivity().getSharedPreferences("msg", Context.MODE_PRIVATE);
        this.list = this.requireActivity().getSharedPreferences("list", Context.MODE_PRIVATE);
        this.PHONE = this.requireContext().getSharedPreferences("PHONE", Context.MODE_PRIVATE);

        this.listviewrequest.setAdapter(new ArrayAdapterList1(this.reqlist));
        this.listviewfriends.setAdapter(new ArrayAdapterList2(this.frilist));

        this.UI();

        this.reqtxt.setOnClickListener(v -> {
            this.reqlay.setVisibility(View.VISIBLE);
            this.frilay.setVisibility(View.GONE);
            this.loadrequests();
        });
        this.fritxt.setOnClickListener(v -> {
            this.reqlay.setVisibility(View.GONE);
            this.frilay.setVisibility(View.VISIBLE);
            this.loadfriends();
        });
    }

    private void initializelogic() {
        this.reqlay.setVisibility(View.GONE);
        this.frilay.setVisibility(View.VISIBLE);
        this.loadfriends();
    }

    HashMap<String, Object> Verify = new HashMap<>();

    private void loadrequests() {
        this.notice.setVisibility(View.GONE);
        this.prgreq.setVisibility(View.VISIBLE);
        this.listviewrequest.setVisibility(View.GONE);
        this.reqlist.clear();
        this.prgreq.setAlpha(0);
        this.localmap.clear();
        this.prgreq.animate().setDuration(1500).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull final Animator animator) {
            }

            @Override
            public void onAnimationEnd(@NonNull final Animator animator) {
                FriendsFragment.this.notice.setText("No Friend Requests Available");
                FriendsFragment.this.notice.setVisibility(View.VISIBLE);
                final int n = Integer.parseInt(FriendsFragment.this.FriendReq.getString("n", "0"));
                FriendsFragment.this.listviewrequest.setVisibility(View.VISIBLE);
                FriendsFragment.this.prgreq.setVisibility(View.GONE);

                for (int i = n - 1; n - 11 < i && i >= 0; i--) {
                    final int finalI = i;
                    FriendsFragment.this.fs.collection("UserProfile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Friends").whereEqualTo("id", FriendsFragment.this.FriendReq.getString(String.valueOf(i), "null")).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().size() == 0) {
                                boolean flag = true;
                                for (int j = 0; j < FriendsFragment.this.reqlist.size(); j++) {
                                    if (Objects.requireNonNull(FriendsFragment.this.reqlist.get(j).get("id")).toString().equals(FriendsFragment.this.FriendReq.getString(String.valueOf(finalI), "null"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    final HashMap<String, Object> hm = new HashMap<>();
                                    hm.put("sp", finalI);
                                    hm.put("id", FriendsFragment.this.FriendReq.getString(String.valueOf(finalI), "null"));
                                    FriendsFragment.this.reqlist.add(FriendsFragment.this.reqlist.size(), hm);

                                    FriendsFragment.this.notice.setText(FriendsFragment.this.reqlist.size() + " Requests");
                                    Objects.requireNonNull(FriendsFragment.this.listviewrequest.getAdapter()).notifyDataSetChanged();
                                }
                            }
                        } else {
                            System.out.println("Network Error");
                        }
                    });

                }

            }

            @Override
            public void onAnimationCancel(@NonNull final Animator animator) {
            }

            @Override
            public void onAnimationRepeat(@NonNull final Animator animator) {
            }
        }).start();

    }

    int lenthfriends;

    private void loadfriends() {
        this.notice.setVisibility(View.GONE);
        this.prgfri.setVisibility(View.VISIBLE);
        this.listviewfriends.setVisibility(View.GONE);
        this.frilist.clear();
        this.prgfri.setAlpha(0);
        this.prgfri.animate().setDuration(1500).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull final Animator animator) {
            }

            @Override
            public void onAnimationEnd(@NonNull final Animator animator) {
                final int n = Integer.parseInt(FriendsFragment.this.FriendReq.getString("n", "0"));
                FriendsFragment.this.listviewfriends.setVisibility(View.VISIBLE);
//                prgfri.setVisibility(View.GONE);
                FriendsFragment.this.frilist.clear();
                FriendsFragment.this.fs.collection("UserProfile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Friends").orderBy("id").limit(100).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final List<DocumentSnapshot> l = task.getResult().getDocuments();
                        final CollectionReference user;
                        user = FriendsFragment.this.fs.collection("UserProfile");
                        if (l.size() == 0) {
                            FriendsFragment.this.notice.setVisibility(View.VISIBLE);
                            FriendsFragment.this.prgfri.setVisibility(View.GONE);
                            FriendsFragment.this.prgfri.setVisibility(View.GONE);
                            FriendsFragment.this.notice.setText("No Friends Found");
                        }
                        FriendsFragment.this.lenthfriends = 0;
                        for (int i = 0; i < l.size(); i++) {
                            HashMap<String, Object> hm = new HashMap<>();
                            hm.put("id", l.get(i).get("id"));
                            hm.put("phone", l.get(i).get("phone"));
                            hm.put("time", l.get(i).get("time"));

                            user.document(Objects.requireNonNull(l.get(i).get("id")).toString()).get().addOnCompleteListener(task1 -> {
                                FriendsFragment.this.lenthfriends++;
                                if (task1.isSuccessful()) {
                                    final DocumentSnapshot ds = task1.getResult();
                                    hm.put("name", ds.get("NAME"));
                                    hm.put("image", ds.get("URL"));
                                    FriendsFragment.this.frilist.add(hm);
                                    Objects.requireNonNull(FriendsFragment.this.listviewfriends.getAdapter()).notifyDataSetChanged();
                                }
                                if (FriendsFragment.this.lenthfriends == l.size()) {
                                    FriendsFragment.this.prgfri.setVisibility(View.GONE);
                                }
                            });

                        }

                    } else {
                        System.out.println("Network Error");
                    }
                });


            }

            @Override
            public void onAnimationCancel(@NonNull final Animator animator) {
            }

            @Override
            public void onAnimationRepeat(@NonNull final Animator animator) {
            }
        }).start();
    }

    public void UI() {
        this._SetBackground(this.reqtxt, 100, 5, "#65000000", true);
        this._SetBackground(this.fritxt, 100, 5, "#65000000", true);
        this.reqtxt.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.fritxt.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.notice.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.notice.setTextColor(Color.parseColor(this.jay.col(1, 1)));

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

    public boolean search, show;
    public String stext = "";

    class ArrayAdapterList1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

        Jay jay;

        public ArrayAdapterList1(final ArrayList<HashMap<String, Object>> _list) {
            this.arrayList = _list;
            this.jay = new Jay(FriendsFragment.this.requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE));
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater _inflater = (LayoutInflater) FriendsFragment.this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View _v = _inflater.inflate(R.layout.chatuserlist, null);
            final RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);

            TextView textview2 = _v.findViewById(R.id.textview2);
            TextView textview3 = _v.findViewById(R.id.textview3);
            TextView textview5 = _v.findViewById(R.id.textview5);

            textview3.setTypeface(Typeface.createFromAsset(FriendsFragment.this.requireActivity().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview2.setTypeface(Typeface.createFromAsset(FriendsFragment.this.requireActivity().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview5.setTypeface(Typeface.createFromAsset(FriendsFragment.this.requireActivity().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            FriendsFragment.this._SetBackground(textview5, 15, 8, this.jay.col(0, 2), false);
//            _SetBackground(linear2, 20, 20, jay.col(0,1), true);

            textview2.setTextColor(Color.parseColor(this.jay.col(1, 1)));
            textview3.setTextColor(Color.parseColor(this.jay.col(1, 1)));
            textview5.setTextColor(Color.parseColor(this.jay.col(1, 1)));


            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder _holder, final int position) {
            int _position1;
            _position1 = _holder.getAdapterPosition();
            final View _v = _holder.itemView;

            LinearLayout div = _v.findViewById(R.id.divider);
            LinearLayout linear1 = _v.findViewById(R.id.linear1);
            TextView textview2 = _v.findViewById(R.id.textview2);
            TextView textview3 = _v.findViewById(R.id.textview3);
            TextView textview5 = _v.findViewById(R.id.textview5);
            CircleImageView circleimageview2 = _v.findViewById(R.id.circleimageview2);
            FriendsFragment.this.show = true;
            if (FriendsFragment.this.search) {
                if (FriendsFragment.this.ChatList.getString(this.arrayList.get(_position1).get("id") + "n", "").toLowerCase().contains(FriendsFragment.this.stext)) {
                } else if (FriendsFragment.this.ChatList.getString(this.arrayList.get(_position1).get("id") + "p", "Processing...").contains(FriendsFragment.this.stext)) {
                } else {
                    FriendsFragment.this.show = false;
                }
            }

//            linear1.setOnClickListener(v-> {
//                Intent intentact = new Intent();
//                intentact.setClass(getContext(), ChatHomeProfileActivity.class);
//                intentact.putExtra("uid", Objects.requireNonNull(arrayList.get(_position1).get("id")).toString());
//                circleimageview2.setTransitionName("img");
//                android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(getActivity(), circleimageview2, "img");
//                startActivity(intentact, optionsCompat.toBundle());
//            });
            linear1.setOnClickListener(v -> {
                final Intent intentact = new Intent();
                intentact.setClass(FriendsFragment.this.getContext(), ImgfullActivity.class);
                intentact.putExtra("uid", Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString());
                circleimageview2.setTransitionName("img");
                final android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(FriendsFragment.this.getActivity(), circleimageview2, "img");
                FriendsFragment.this.startActivity(intentact, optionsCompat.toBundle());
            });


            if (FriendsFragment.this.show) {
                linear1.setVisibility(View.VISIBLE);
                div.setVisibility(View.VISIBLE);
            } else {
                linear1.setVisibility(View.GONE);
                div.setVisibility(View.GONE);
            }

            if (FriendsFragment.this.show) {
                textview2.setText(FriendsFragment.this.ChatList.getString(this.arrayList.get(_position1).get("id") + "n", "Processing..."));
                textview3.setText(FriendsFragment.this.ChatList.getString(this.arrayList.get(_position1).get("id") + "p", "Processing..."));

                if ("".equals(FriendsFragment.this.ChatList.getString(this.arrayList.get(_position1).get("id") + "i", ""))) {
                } else {
                    Glide.with(FriendsFragment.this.requireContext()).load(Uri.parse(FriendsFragment.this.ChatList.getString(this.arrayList.get(_position1).get("id") + "i", ""))).into(circleimageview2);
                }

                if (!FriendsFragment.this.localmap.containsKey(this.arrayList.get(_position1).get("id").toString())) {
                    textview5.setText("Accept");
                    textview5.setAlpha(1);

                    if (FriendsFragment.this.ChatRef.getString("IsUsed" + this.arrayList.get(_position1).get("sp"), "").equals("used")) {
                        textview5.setOnClickListener(null);
                        textview5.setText("Old Request");
                        textview5.setAlpha(0.5f);
                    } else {
                        textview5.setAlpha(1);
                        textview5.setOnClickListener(_view1 -> {
                            textview5.setOnClickListener(null);
                            textview5.setText("Loading...");
                            final Calendar cal = Calendar.getInstance();
                            final HashMap<String, Object> hm = new HashMap<>();
                            hm.put("id", Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString());
                            hm.put("phone", FriendsFragment.this.ChatList.getString(this.arrayList.get(_position1).get("id") + "p", "null"));
                            hm.put("time", cal.getTimeInMillis());
                            FriendsFragment.this.fs.collection("UserProfile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Friends").document(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString()).set(hm)
                                    .addOnFailureListener(v -> {
                                        textview5.setText("Failed");
                                        Toast.makeText(FriendsFragment.this.getContext(), "Network Failed", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnSuccessListener(v -> {
                                        FriendsFragment.this.localmap.put(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString(), "true");
                                        FriendsFragment.this.ChatRef.edit().putString("IsFriend" + this.arrayList.get(_position1).get("id"), "true").apply();
                                        FriendsFragment.this.ChatRef.edit().putString("IsUsed" + this.arrayList.get(_position1).get("sp"), "used").apply();
                                        textview5.setText("Added");
                                        Toast.makeText(FriendsFragment.this.getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                                        Objects.requireNonNull(FriendsFragment.this.listviewrequest.getAdapter()).notifyDataSetChanged();
                                        hm.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                                        hm.put("phone", FriendsFragment.this.pdataSP.getString("PHONE", "null"));
                                        hm.put("time", cal.getTimeInMillis());
                                        FriendsFragment.this.fs.collection("UserProfile").document(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString()).collection("Friends").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).set(hm);
                                    });
                        });
                    }
                } else {
                    textview5.setText("Added");
                    textview5.setAlpha(0.5f);
                    textview5.setOnClickListener(null);
                }
            } else {
                textview5.setOnClickListener(null);
            }
        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull final View itemView) {
                super(itemView);
            }
        }


    }

    class ArrayAdapterList2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<HashMap<String, Object>> arrayList;
        Jay jay;


        public ArrayAdapterList2(final ArrayList<HashMap<String, Object>> _list) {
            this.arrayList = _list;
            this.jay = new Jay(FriendsFragment.this.requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE));


        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater _inflater = (LayoutInflater) FriendsFragment.this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View _v = _inflater.inflate(R.layout.chatuserlist, null);
            final RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);

            TextView textview2 = _v.findViewById(R.id.textview2);
            TextView textview3 = _v.findViewById(R.id.textview3);
            TextView textview5 = _v.findViewById(R.id.textview5);

            textview3.setTypeface(Typeface.createFromAsset(FriendsFragment.this.requireActivity().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview2.setTypeface(Typeface.createFromAsset(FriendsFragment.this.requireActivity().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
//            textview5.setTypeface(Typeface.createFromAsset(requireActivity().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
//            _SetBackground(textview5, 15, 8, jay.col(0, 2), false);
//            _SetBackground(linear2, 20, 20, jay.col(0,1), true);

            textview2.setTextColor(Color.parseColor(this.jay.col(1, 1)));
            textview3.setTextColor(Color.parseColor(this.jay.col(1, 1)));
//            textview5.setTextColor(Color.parseColor(jay.col(1, 1)));
            textview5.setVisibility(View.GONE);

            return new ViewHolder(_v);
        }

        private ArrayList<HashMap<String, Object>> nlistmap = new ArrayList<>();

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder _holder, final int position) {
            int _position1;
            _position1 = _holder.getAdapterPosition();
            final View _v = _holder.itemView;

            LinearLayout div = _v.findViewById(R.id.divider);
            LinearLayout linear1 = _v.findViewById(R.id.linear1);
            TextView textview2 = _v.findViewById(R.id.textview2);
            TextView textview3 = _v.findViewById(R.id.textview3);
//            final TextView textview5 = _v.findViewById(R.id.textview5);
            CircleImageView circleimageview2 = _v.findViewById(R.id.circleimageview2);
            FriendsFragment.this.show = true;
            if (FriendsFragment.this.search) {
                if (Objects.requireNonNull(this.arrayList.get(_position1).get("name")).toString().toLowerCase().contains(FriendsFragment.this.stext)) {
                } else if (Objects.requireNonNull(this.arrayList.get(_position1).get("phone")).toString().contains(FriendsFragment.this.stext)) {
                } else {
                    FriendsFragment.this.show = false;
                }
            }


            if (FriendsFragment.this.show) {
                linear1.setVisibility(View.VISIBLE);
                div.setVisibility(View.VISIBLE);
            } else {
                linear1.setVisibility(View.GONE);
                div.setVisibility(View.GONE);
            }

            if (FriendsFragment.this.show) {
                textview2.setText(Objects.requireNonNull(this.arrayList.get(_position1).get("name")).toString());
                textview3.setText(Objects.requireNonNull(this.arrayList.get(_position1).get("phone")).toString());
                if ("".equals(Objects.requireNonNull(this.arrayList.get(_position1).get("image")).toString())) {
                } else {
                    Glide.with(FriendsFragment.this.requireContext()).load(Uri.parse(Objects.requireNonNull(this.arrayList.get(_position1).get("image")).toString())).into(circleimageview2);
                }

            } else {
//                textview5.setOnClickListener(null);
            }

            linear1.setOnClickListener(v -> {
                if (FriendsFragment.this.msg.getString(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString(),"").equals("")) {
                    this.nlistmap.clear();
                    final String Tmp = FriendsFragment.this.ChatList.getString("list", "");
                    if ("".equals(Tmp)) {

                    } else {
                        this.nlistmap = new Gson().fromJson(Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                        }.getType());
                    }
                    {
                        final HashMap<String, Object> _item = new HashMap<>();
                        _item.put("uid", Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString());
                        this.nlistmap.add(_item);
                    }

                    this.nlistmap.get(this.nlistmap.size() - 1).put("img", Objects.requireNonNull(this.arrayList.get(_position1).get("image")).toString());
                    this.nlistmap.get(this.nlistmap.size() - 1).put("name", Objects.requireNonNull(this.arrayList.get(_position1).get("name")).toString());
                    this.nlistmap.get(this.nlistmap.size() - 1).put("phone", Objects.requireNonNull(this.arrayList.get(_position1).get("phone")).toString());
                    FriendsFragment.this.ChatList.edit().putString(Objects.requireNonNull(this.arrayList.get(_position1).get("phone")).toString(), "Added").apply();
                    FriendsFragment.this.ChatList.edit().putString("list", new Gson().toJson(this.nlistmap)).apply();
                    FriendsFragment.this.msg.edit().putString(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString(), "0").apply();
                    FriendsFragment.this.ChatList.edit().putString(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString().concat("n"), Objects.requireNonNull(this.arrayList.get(_position1).get("name")).toString()).apply();
                    FriendsFragment.this.ChatList.edit().putString(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString().concat("i"), Objects.requireNonNull(this.arrayList.get(_position1).get("image")).toString()).apply();
                    FriendsFragment.this.ChatList.edit().putString(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString().concat("p"), Objects.requireNonNull(this.arrayList.get(_position1).get("phone")).toString()).apply();
                    FriendsFragment.this.cal = Calendar.getInstance();
                    FriendsFragment.this.ChatList.edit().putString(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString().concat("lm"), String.valueOf(FriendsFragment.this.cal.getTimeInMillis())).apply();
                    FriendsFragment.this.list.edit().putString(Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString().concat("lv"), "0").apply();
                    final Intent intent = new Intent();
                    intent.setClass(FriendsFragment.this.getContext(), HomeActivity.class);
                    FriendsFragment.this.startActivity(intent);
                    FriendsFragment.this.requireActivity().finishAffinity();
                }
                final Intent intent = new Intent();
                intent.setClass(FriendsFragment.this.getContext(), ChathomeActivity.class);
                intent.putExtra("UID", Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString());
                intent.putExtra("PHONE", Objects.requireNonNull(this.arrayList.get(_position1).get("phone")).toString());
                FriendsFragment.this.PHONE.edit().putString("PHONE", FriendsFragment.this.requireActivity().getIntent().getStringExtra("PHONE")).apply();
                FriendsFragment.this.startActivity(intent);

            });

            circleimageview2.setOnClickListener(v -> {
                final Intent intentact = new Intent();
                intentact.setClass(FriendsFragment.this.getContext(), ImgfullActivity.class);
                intentact.putExtra("url", Objects.requireNonNull(this.arrayList.get(_position1).get("image")).toString());
                intentact.putExtra("uid", Objects.requireNonNull(this.arrayList.get(_position1).get("id")).toString());
                circleimageview2.setTransitionName("img");
                final android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(FriendsFragment.this.getActivity(), circleimageview2, "img");
                FriendsFragment.this.startActivity(intentact, optionsCompat.toBundle());
            });
        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull final View itemView) {
                super(itemView);
            }
        }


    }
}