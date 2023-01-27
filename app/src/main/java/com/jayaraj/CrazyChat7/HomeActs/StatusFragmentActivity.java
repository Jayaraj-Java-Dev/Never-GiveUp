package com.jayaraj.CrazyChat7.HomeActs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.RequestNetwork;
import com.jayaraj.CrazyChat7.OtherActs.SettingsActivity;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;
import com.jayaraj.CrazyChat7.LoginActs.UserActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;
import me.echodev.resizer.Resizer;


public class StatusFragmentActivity extends Fragment {
    public final int REQ_CD_FP = 101;
    private final Timer _timer = new Timer();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

    private FloatingActionButton _fab;
    private String id = "";
    private HashMap<String, Object> nmap = new HashMap<>();
    private final double n = 0;
    private HashMap<String, Object> smap = new HashMap<>();
    private final double n1 = 0;

    private ArrayList<HashMap<String, Object>> datalm = new ArrayList<>();

    private ScrollView vscroll2;
    private LinearLayout linear1;
    private LinearLayout linear6;
    private TextView textview1;
    private RecyclerView recyclerview1;
    private LinearLayout linear3;
    private LinearLayout linear8,posts,friends;
    private ImageView setimg;
    private CircleImageView circleimageview1;
    private TextView textview3,postscount,potstxt,friendscount,friendstxt;
    private TextView textview5;
    private TextView settings;

    private final Intent inte = new Intent();
    private final Intent fp = new Intent(Intent.ACTION_GET_CONTENT);
    private final StorageReference storage = this._firebase_storage.getReference("Profile");

    private final Calendar cal = Calendar.getInstance();
    private DatabaseReference db = this._firebase.getReference("rand");
    private ChildEventListener _db_child_listener;
    private FirebaseAuth auth;
    private SharedPreferences status;
    private SharedPreferences pdataSP;
    private SharedPreferences asp;
    private RequestNetwork rn;

    private final FirebaseFirestore dbfs = FirebaseFirestore.getInstance();
    private Jay jay;

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater _inflater, @Nullable final ViewGroup _container, @Nullable final Bundle _savedInstanceState) {
        final View _view = _inflater.inflate(R.layout.status_fragment, _container, false);
        this.initialize(_savedInstanceState, _view);
        com.google.firebase.FirebaseApp.initializeApp(this.requireContext());
        this.initializeLogic();
        return _view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.status.unregisterOnSharedPreferenceChangeListener(this.listChangeListener);
    }

    private void initialize(final Bundle _savedInstanceState, final View _view) {
        this._fab = _view.findViewById(R.id._fab);
        this.settings = _view.findViewById(R.id.settings);
        this.vscroll2 = _view.findViewById(R.id.vscroll2);
        this.linear1 = _view.findViewById(R.id.linear1);
        this.linear6 = _view.findViewById(R.id.linear6);
        this.textview1 = _view.findViewById(R.id.textview1);
        this.recyclerview1 = _view.findViewById(R.id.recyclerview1);
        this.linear3 = _view.findViewById(R.id.linear3);
        this.linear8 = _view.findViewById(R.id.linear8);
        this.circleimageview1 = _view.findViewById(R.id.circleimageview1);
        this.textview3 = _view.findViewById(R.id.textview3);
        this.textview5 = _view.findViewById(R.id.textview5);
        this.setimg = _view.findViewById(R.id.image_hevo8);

        this.posts = _view.findViewById(R.id.posts);
        this.postscount = _view.findViewById(R.id.postscount);
        this.potstxt = _view.findViewById(R.id.potstxt);

        this.friends = _view.findViewById(R.id.friends);
        this.friendscount = _view.findViewById(R.id.friendscount);
        this.friendstxt = _view.findViewById(R.id.friendstxt);

        this.fp.setType("image/*");
        this.fp.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        this.auth = FirebaseAuth.getInstance();
        this.status = this.requireContext().getSharedPreferences("status", Context.MODE_PRIVATE);
        this.pdataSP = this.requireContext().getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.asp = this.requireContext().getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.rn = new RequestNetwork((Activity) this.getContext());
        this.status.registerOnSharedPreferenceChangeListener(this.listChangeListener);
        this.jay = new Jay(this.requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.textview1.setTextColor(Color.parseColor(this.jay.col(1,1)));


        this.friends.setVisibility(View.GONE);
        final AggregateQuery countQuery = this.dbfs.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Friends").count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final AggregateQuerySnapshot snapshot = task.getResult();
                if(snapshot.getCount()>0){
                    this._Anime("oncrt2", this.linear1);
                    this.friends.setVisibility(View.VISIBLE);
                    this.friendscount.setText(String.valueOf(snapshot.getCount()));
                }
            } else {
                System.out.println("Count failed: "+ task.getException());
            }
        });


        this._fab.setOnClickListener(_view1 -> this.startActivityForResult(this.fp, this.REQ_CD_FP));

        this.textview3.setOnClickListener(view -> {
            this.inte.setClass(this.getContext(), UserActivity.class);
            this.startActivity(this.inte);
        });

        this.textview5.setOnClickListener(view -> {
            this.inte.setClass(this.getContext(), UserActivity.class);
            this.startActivity(this.inte);
        });

        this.circleimageview1.setOnClickListener(view -> {
            this.inte.setClass(this.getContext(), UserActivity.class);
            this.startActivity(this.inte);
        });


        this._db_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (StatusFragmentActivity.this.smap.containsKey(_childKey)) {

                } else {
                    StatusFragmentActivity.this.smap.put(_childKey, String.valueOf((long) (StatusFragmentActivity.this.datalm.size())));
                    StatusFragmentActivity.this.datalm.add(_childValue);
                    Objects.requireNonNull(StatusFragmentActivity.this.recyclerview1.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);
                StatusFragmentActivity.this.id = Objects.requireNonNull(_childValue.get("id")).toString();
                if (StatusFragmentActivity.this.smap.containsKey(StatusFragmentActivity.this.id)) {
                    StatusFragmentActivity.this.datalm.get((int) Double.parseDouble(Objects.requireNonNull(StatusFragmentActivity.this.smap.get(StatusFragmentActivity.this.id)).toString())).put("like", Objects.requireNonNull(_childValue.get("like")).toString());
                } else {
                    StatusFragmentActivity.this.smap.put(StatusFragmentActivity.this.id, String.valueOf((long) (StatusFragmentActivity.this.datalm.size())));
                    StatusFragmentActivity.this.datalm.add(_childValue);
                }
                Objects.requireNonNull(StatusFragmentActivity.this.recyclerview1.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull final DataSnapshot _param1, final String _param2) {

            }

            @Override
            public void onChildRemoved(@NonNull final DataSnapshot _param1) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey;
				_childKey = _param1.getKey();
				HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(@NonNull final DatabaseError _param1) {
                int _errorCode = _param1.getCode();
                String _errorMessage = _param1.getMessage();
            }
        };


    }
    SharedPreferences.OnSharedPreferenceChangeListener listChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, @NonNull final String key1) {
            if (key1.equals("theme")) {
                StatusFragmentActivity.this.UI();
                if(StatusFragmentActivity.this.recyclerview1 !=null){
                    Objects.requireNonNull(StatusFragmentActivity.this.recyclerview1.getAdapter()).notifyDataSetChanged();
                }
            }
        }
    };
    public void _Anime(String _Sp, View _Linear) {
        final AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(200);
        TransitionManager.beginDelayedTransition((LinearLayout) _Linear, autoTransition);

    }
    private void initializeLogic() {
        final androidx.recyclerview.widget.StaggeredGridLayoutManager slmrecyclerview1 = new androidx.recyclerview.widget.StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        this.recyclerview1.setLayoutManager(slmrecyclerview1);
        this.recyclerview1.setAdapter(new Recyclerview1Adapter(this.datalm));
        if (!this.pdataSP.getString("IMG", "").equals("")) {
            Glide.with(this.requireContext()).load(Uri.parse(this.pdataSP.getString("IMG", ""))).into(this.circleimageview1);
        }

        this.db = this._firebase.getReference("PIMGS/uid/".concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
        this.db.addChildEventListener(this._db_child_listener);
        this.recyclerview1.setVisibility(View.GONE);

        if (SketchwareUtil.isConnected(this.requireContext())) {
            this.textview1.setText("Your Profile Gallery");
        } else {
            this.textview1.setText("Connecting...");
        }

        this.db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot _dataSnapshot) {
                StatusFragmentActivity.this.datalm = new ArrayList<>();
                try {
                    final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (final DataSnapshot _data : _dataSnapshot.getChildren()) {
                        final HashMap<String, Object> _map = _data.getValue(_ind);
                        StatusFragmentActivity.this.datalm.add(_map);
                    }
                } catch (final Exception _e) {
                    _e.printStackTrace();
                }
                StatusFragmentActivity.this.smap = new HashMap<>();
                for (int n1 = 0; n1 < StatusFragmentActivity.this.datalm.size(); n1++) {
                    StatusFragmentActivity.this.smap.put(Objects.requireNonNull(StatusFragmentActivity.this.datalm.get(n1).get("id")).toString(), String.valueOf((long) (n1)));
                }
                if (0 == StatusFragmentActivity.this.datalm.size()) {
                    StatusFragmentActivity.this.textview1.setText("Add Your Images Now");
                } else {
                    StatusFragmentActivity.this.recyclerview1.setAdapter(new Recyclerview1Adapter(StatusFragmentActivity.this.datalm));
                    StatusFragmentActivity.this.recyclerview1.setHasFixedSize(true);
                    StatusFragmentActivity.this._Anime("a1", StatusFragmentActivity.this.linear1);
                    StatusFragmentActivity.this.recyclerview1.setVisibility(View.VISIBLE);
                }
                StatusFragmentActivity.this.postscount.setText(String.valueOf(StatusFragmentActivity.this.datalm.size()));
                StatusFragmentActivity.this.textview1.setText("Your Profile Gallery");
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError _databaseError) {
            }
        });


        this.settings.setOnClickListener(v -> {
            this.inte.setClass(this.getContext(), SettingsActivity.class);
            this.startActivity(this.inte);
        });
        this.UI();
    }

    private void UI() {
        this._SetBackground(this.textview3, 100, 5, "#65000000", true);
        this._SetBackground(this.textview5, 100, 5, "#65000000", true);
        this._SetBackground(this.settings, 100, 5, "#65000000", true);
        this._SetBackground(this.friends, 40, 5, "#65000000", true);
        this._SetBackground(this.posts, 40, 5, "#65000000", true);
        this.textview3.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
        this.textview5.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.textview1.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.settings.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.friendstxt.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.potstxt.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.friendscount.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.postscount.setTypeface(Typeface.createFromAsset(this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);


        this.setimg.setColorFilter(Color.parseColor(this.jay.col(1, 1)));
        this.textview5.setText(this.pdataSP.getString("PHONE", "Something Went Wrong"));
        this.textview3.setText(this.pdataSP.getString("NAME", "No Name"));
        this.textview1.setTextColor(Color.parseColor(this.jay.col(1,1)));

    }

    public void _resize(String _id, String _path) {
        try {
            new Resizer(this.getContext()).setTargetLength(1700).setQuality(15).setOutputFormat("WEBP").setOutputFilename(_id).setOutputDirPath(FileUtil.getPackageDataDir(this.getContext()).concat("/Image/sent")).setSourceImage(new java.io.File(_path)).getResizedFile();
        } catch (final IOException ignored) {}
    }

    private void upload() {
        this._fab.setVisibility(View.GONE);
        final ProgressDialog pb1 = new ProgressDialog(this.getContext());
        pb1.setProgress(0);
        pb1.setTitle("Uploading Photo");
        pb1.setMessage("Please Wait...");
        pb1.setCancelable(false);
        pb1.setCanceledOnTouchOutside(false);
        pb1.show();
        final Calendar cal = Calendar.getInstance();
        final String id2 = String.valueOf(cal.getTimeInMillis());
        this._resize(id2, this.status.getString("file", ""));
        new java.io.File(FileUtil.getPackageDataDir(this.getContext()).concat("/Image/sent/".concat(id2.concat(".webp")))).renameTo(new java.io.File(FileUtil.getPackageDataDir(this.getContext()).concat("/Image/sent/".concat(id2.concat(".jpg")))));
        final OnCompleteListener<Uri> success_listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull final Task<Uri> _param1) {
                String _DUrl = _param1.getResult().toString();
                pb1.hide();
                StatusFragmentActivity.this.nmap = new HashMap<>();
                StatusFragmentActivity.this.nmap.put("id", id2);
                StatusFragmentActivity.this.nmap.put("url", _DUrl);
                StatusFragmentActivity.this.nmap.put("like", "0");

                StatusFragmentActivity.this.db.child(id2).updateChildren(StatusFragmentActivity.this.nmap);
                StatusFragmentActivity.this.nmap.clear();
                StatusFragmentActivity.this._fab.setVisibility(View.VISIBLE);
                SketchwareUtil.showMessage(StatusFragmentActivity.this.getContext(), "Profile Updated");
            }
        };

        final OnProgressListener<UploadTask.TaskSnapshot> progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull final UploadTask.TaskSnapshot _param1) {
                final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                pb1.setProgress((int)_prog);
            }
        };
        final OnFailureListener failure_listener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception _param1) {
                pb1.hide();
                String _msg = _param1.getMessage();
                StatusFragmentActivity.this._fab.setVisibility(View.VISIBLE);
                SketchwareUtil.showMessage(StatusFragmentActivity.this.getContext(), "Failed to Upload");
                SketchwareUtil.showMessage(StatusFragmentActivity.this.getContext(), _msg);
            }
        };

        this.storage.child(Uri.parse(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/imgs/".concat(id2.concat(".jpg")))).toString()).putFile(Uri.fromFile(new File(Uri.parse(FileUtil.getPackageDataDir(this.getContext()).concat("/Image/sent/".concat(id2.concat(".jpg")))).toString()))).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(final Task<UploadTask.TaskSnapshot> task) {
                return StatusFragmentActivity.this.storage.child(Uri.parse(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/imgs/".concat(id2.concat(".jpg")))).toString()).getDownloadUrl();
            }
        }).addOnCompleteListener(success_listener);
    }
    @Override
    public void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        if (_requestCode==this.REQ_CD_FP) {
                if (_resultCode == Activity.RESULT_OK) {
                    final ArrayList<String> _filePath = new ArrayList<>();
                    if (_data != null) {
                        if (_data.getClipData() != null) {
                            for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
                                final ClipData.Item _item = _data.getClipData().getItemAt(_index);
                                _filePath.add(FileUtil.convertUriToFilePath(this.getContext(), _item.getUri()));
                            }
                        } else {
                            _filePath.add(FileUtil.convertUriToFilePath(this.getContext(), _data.getData()));
                        }
                    }
                    this.status.edit().putString("file", _filePath.get(0)).apply();
                    this.upload();
                } else {

                }
        }
    }

    public void _Glide(ImageView _img, String _url) {
        Glide.with(this.requireContext()).load(_url).placeholder(R.drawable.app_icon).fitCenter().into(_img);
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


    public Bitmap getResizedBitmap(final Bitmap image, final int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        final float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }


    public String getStringImage(final Bitmap bmp) {
        final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] imageBytes = baos.toByteArray();
        final String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        return encodedImage;
    }


    public void _notifyUpdate(String _Key) {
        this.status.edit().putString(_Key, String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999)))).apply();
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

        public Recyclerview1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @NonNull
		@Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater _inflater = (LayoutInflater) StatusFragmentActivity.this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            StatusFragmentActivity.this._Glide(imageview1, Objects.requireNonNull(this._data.get(_position).get("url")).toString());

            textview1.setText(Objects.requireNonNull(this._data.get(_position).get("like")).toString());
            cardview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View _view) {
                    StatusFragmentActivity.this.status.edit().putString("pos", String.valueOf((long) (_position))).apply();
                    StatusFragmentActivity.this.status.edit().putString("list", new Gson().toJson(StatusFragmentActivity.this.datalm)).apply();
                    StatusFragmentActivity.this._notifyUpdate("b");
                }
            });
            StatusFragmentActivity.this._SetBackground(deltext,20,15, StatusFragmentActivity.this.jay.col(0,2),true);
            deltext.setTextColor(Color.parseColor(StatusFragmentActivity.this.jay.col(1,2)));
            linear4.setBackground(new GradientDrawable() {
                public GradientDrawable getIns(final int a, final int b) {
                    setCornerRadius(a);
                    setColor(b);
                    return this;
                }
            }.getIns((int) 20, 0x65000000));
            deltext.setTypeface(Typeface.createFromAsset(StatusFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            textview1.setTypeface(Typeface.createFromAsset(StatusFragmentActivity.this.requireContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);

            deltext.setOnClickListener(v -> {
                Toast.makeText(StatusFragmentActivity.this.getContext(), "Deleting Post", Toast.LENGTH_SHORT).show();
                StatusFragmentActivity.this.db.child(Objects.requireNonNull(this._data.get(_position).get("id")).toString()).removeValue((error, ref) -> {
                    Toast.makeText(StatusFragmentActivity.this.getContext(), "Post Deleted", Toast.LENGTH_SHORT).show();
                    StatusFragmentActivity.this.datalm.remove(_position);
                    StatusFragmentActivity.this.recyclerview1.getAdapter().notifyDataSetChanged();
                });
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
