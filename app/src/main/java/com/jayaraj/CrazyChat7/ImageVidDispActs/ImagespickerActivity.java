package com.jayaraj.CrazyChat7.ImageVidDispActs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class ImagespickerActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();

    private String folderPaths = "";
    private String a = "";
    private boolean MultiPicker;
    private double n1;
    private String imageFormats = "";
    private boolean inFolder;
    private double selected;
    private double n2;
    private HashMap<String, Object> cacheMap = new HashMap<>();
    private boolean stopLoop;
    private final double n3 = 0;
    private boolean freeze;
    private double oldLen;
    private HashMap<String, Object> datamap = new HashMap<>();
    private boolean image;
    private String A = "";
    private String B = "";
    private String icon = "";
    private double bindN;

    private ArrayList<HashMap<String, Object>> itemsList = new ArrayList<>();
    private final ArrayList<String> cache = new ArrayList<>();
    private final ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
    private final ArrayList<String> tmpfiles = new ArrayList<>();
    private final ArrayList<String> allfolderslist = new ArrayList<>();

    private RelativeLayout linear1BG;
    private LinearLayout linearBackGround;
    private LinearLayout linear6;
    private ImageView imageviewBackGround;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private ImageView imageview1;
    private RelativeLayout linear5;
    private GridView gridview1;
    private LinearLayout linear4;
    private TextView textview1;
    private ProgressBar progressbar1;
    private TextView textview2;

    private SharedPreferences result;
    private TimerTask ti;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.imagespicker);
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
        this.linear1BG = this.findViewById(R.id.linear1BG);
        this.linearBackGround = this.findViewById(R.id.linearBackGround);
        this.linear6 = this.findViewById(R.id.linear6);
        this.imageviewBackGround = this.findViewById(R.id.imageviewBackGround);
        this.linear1 = this.findViewById(R.id.linear1);
        this.linear2 = this.findViewById(R.id.linear2);
        this.linear3 = this.findViewById(R.id.linear3);
        this.imageview1 = this.findViewById(R.id.imageview1);
        this.linear5 = this.findViewById(R.id.linear5);
        this.gridview1 = this.findViewById(R.id.gridview1);
        this.linear4 = this.findViewById(R.id.linear4);
        this.textview1 = this.findViewById(R.id.textview1);
        this.progressbar1 = this.findViewById(R.id.progressbar1);
        this.textview2 = this.findViewById(R.id.textview2);
        this.result = this.getSharedPreferences("sharedpreferences", Context.MODE_PRIVATE);

        this.linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                if (ImagespickerActivity.this.selected > 0) {
                    ImagespickerActivity.this.n2 = 0;
                    for (int _repeat17 = 0; _repeat17 < ImagespickerActivity.this.itemsList.size(); _repeat17++) {
                        if (Objects.requireNonNull(ImagespickerActivity.this.itemsList.get((int) ImagespickerActivity.this.n2).get("select")).toString().equals("1")) {
                            ImagespickerActivity.this.cacheMap = ImagespickerActivity.this.itemsList.get((int) ImagespickerActivity.this.n2);
                            ImagespickerActivity.this.resultList.add(ImagespickerActivity.this.cacheMap);
                        }
                        ImagespickerActivity.this.n2++;
                    }
                    ImagespickerActivity.this.itemsList.clear();
                    ImagespickerActivity.this.gridview1.setVisibility(View.GONE);
                    ImagespickerActivity.this.result.edit().putString("result", new Gson().toJson(ImagespickerActivity.this.resultList)).apply();
                    ImagespickerActivity.this.finish();
                }
            }
        });

        this.gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> _param1, final View _param2, final int _param3, final long _param4) {
                int _position = _param3;
                ImagespickerActivity.this._onItemClick(_position);
            }
        });
    }

    private void initializeLogic() {
        this.freeze = false;
        this.imageFormats = this.getIntent().getStringExtra("files");
        if (this.imageFormats.equals("|.jpeg|.bmp|.png|.jpg|")) {
            this.image = true;
            this.A = "tlm";
            this.B = "tlmfol";
        } else {
            this.image = false;
            this.A = "tlmv";
            this.B = "tlmfolv";
        }
        this.inFolder = false;
        this.selected = 0;
        this.MultiPicker = "true".equals(this.getIntent().getStringExtra("multiple_images"));
        this.setTitle("Images Picker");
        this.gridview1.setNumColumns(2);
        this.gridview1.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        this.gridview1.setGravity(Gravity.CENTER);
        if ("".equals(this.result.getString(this.A, ""))) {
            this.oldLen = 0;
            this.gridview1.setAdapter(new Gridview1Adapter(this.itemsList));
        } else {
            this.itemsList = new Gson().fromJson(this.result.getString(this.A, ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            this.datamap = new Gson().fromJson(this.result.getString(this.B, ""), new TypeToken<HashMap<String, Object>>() {
            }.getType());
            this.oldLen = this.itemsList.size();
            this.gridview1.setAdapter(new Gridview1Adapter(this.itemsList));
        }
        this.getAllShownImagesPath(this);
        if (this.MultiPicker) {
            this._refreshSelected();
        } else {
            this.linear3.setVisibility(View.GONE);
        }
        this.textview1.setVisibility(View.GONE);
        Glide.with(this.getApplicationContext()).load(Uri.parse("5")).into(this.imageview1);
        this.imageview1.setVisibility(View.GONE);
        this._Bg();
        this.textview1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/font1.ttf"), Typeface.NORMAL);
        this.textview2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/font1.ttf"), Typeface.NORMAL);
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (this.inFolder) {
            if (this.selected > 0) {
                this.selected = 0;
                this.n1 = 0;
                for (int _repeat28 = 0; _repeat28 < this.itemsList.size(); _repeat28++) {
                    this.itemsList.get((int) this.n1).put("select", "0");
                    this.n1++;
                }
                this.gridview1.invalidateViews();
                this._refreshSelected();
            } else {
                this.selected = 0;
                this.folderPaths = "";
                this.inFolder = false;
                this._refreshSelected();
                this.itemsList.clear();
                this.datamap.clear();
                this.freeze = false;
                this.itemsList = new Gson().fromJson(this.result.getString(this.A, ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
                this.datamap = new Gson().fromJson(this.result.getString(this.B, ""), new TypeToken<HashMap<String, Object>>() {
                }.getType());
                this.gridview1.setAdapter(new Gridview1Adapter(this.itemsList));
                this.getAllShownImagesPath(this);
            }
        } else {
            this.finish();
        }
    }

    public void _init() {
    }

    public class CustomGridViewAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public CustomGridViewAdapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._data = _arr;
        }

        @Override
        public int getCount() {
            return this._data.size();
        }


        @Override
        public Object getItem(final int position) {
            return this._data.get(position);
        }

        @Override
        public long getItemId(final int _index) {
            return _index;
        }

        @Override
        public View getView(int _position, final View _view, final ViewGroup _viewGroup) {
            final LayoutInflater _inflater = (LayoutInflater) ImagespickerActivity.this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.grid_item, null);
            }

            LinearLayout linear1 = _v.findViewById(R.id.linear1);
            LinearLayout linear2 = _v.findViewById(R.id.linear2);
            LinearLayout linear3 = _v.findViewById(R.id.linear3);
            ImageView imageview1 = _v.findViewById(R.id.imageview1);
            TextView textview1 = _v.findViewById(R.id.textview1);
            _v.findViewById(R.id.imageview1);
            TextView textview2 = _v.findViewById(R.id.textview2);
            ImagespickerActivity.this._applySetItem(_position, linear1, linear2, imageview1, textview1, textview2, linear3);


            return _v;
        }
    }

    public void getAllShownImagesPath(final Activity activity) {

        android.net.Uri uri;
        android.database.Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {android.provider.MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DATA);

        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);


        this.allfolderslist.clear();
        while (cursor.moveToNext()) {
            this.allfolderslist.add(cursor.getString(column_index_data));
        }
        if (this.oldLen == this.allfolderslist.size()) {

        } else {
            this._startProcess(this.allfolderslist);
        }
    }

    {
    }


    public void _filterToFolder(String _path) {
        this.a = _path.substring(0, _path.lastIndexOf("/"));
        if (this.folderPaths.contains(":".concat(this.a.concat(":")))) {

        } else {
            this.folderPaths = this.folderPaths.concat(":".concat(this.a.concat(":")));
            this._checkAdd(this.a, _path);
        }
    }


    public void _onItemClick(double _pos) {
        if (Objects.requireNonNull(this.itemsList.get((int) _pos).get("type")).toString().equals("folder")) {
            this.progressbar1.setVisibility(View.GONE);
            this.freeze = true;
            this._loadFolder(Objects.requireNonNull(this.itemsList.get((int) _pos).get("path")).toString());
            this.inFolder = true;
        } else {
            if (this.MultiPicker) {
                if (Objects.requireNonNull(this.itemsList.get((int) _pos).get("select")).toString().equals("1")) {
                    this.itemsList.get((int) _pos).put("select", "0");
                    this.selected--;
                } else {
                    this.itemsList.get((int) _pos).put("select", "1");
                    this.selected++;
                }
                this._refreshSelected();
                this.gridview1.invalidateViews();
            } else {
                this.result.edit().putString("result", Objects.requireNonNull(this.itemsList.get((int) _pos).get("path")).toString()).apply();
                this.finish();
            }
        }
    }


    public void _removeView(View _v) {
        if (_v.getParent() != null) {

            ((ViewGroup) _v.getParent()).removeView(_v);

        }
    }


    public void _loadFolder(String _path) {
        this.n1 = 0;
        this.selected = 0;
        this._refreshSelected();
        this.cache.clear();
        this.itemsList.clear();
        FileUtil.listDir(_path, this.cache);
        if (this.cache.size() > 0) {
            this.gridview1.setVisibility(View.VISIBLE);
            for (int _repeat22 = 0; _repeat22 < this.cache.size(); _repeat22++) {
                if (FileUtil.isFile(this.cache.get((int) (this.n1))) && (this.cache.get((int) (this.n1)).contains(".") && this.imageFormats.contains("|".concat(this.cache.get((int) (this.n1)).substring(this.cache.get((int) (this.n1)).lastIndexOf(".")).concat("|"))))) {
                    {
                        final HashMap<String, Object> _item = new HashMap<>();
                        _item.put("type", "file");
                        this.itemsList.add(_item);
                    }

                    this.itemsList.get(this.itemsList.size() - 1).put("path", this.cache.get((int) (this.n1)));
                    this.itemsList.get(this.itemsList.size() - 1).put("name", Uri.parse(this.cache.get((int) (this.n1))).getLastPathSegment());
                    this.itemsList.get(this.itemsList.size() - 1).put("select", "0");
                }
                this.n1++;
            }
            this.cache.clear();
        } else {
            this.gridview1.setVisibility(View.GONE);
        }
        ((BaseAdapter) this.gridview1.getAdapter()).notifyDataSetChanged();
    }


    public void _refreshSelected() {
        if (this.selected > 0) {
            this.linear3.setEnabled(true);
            this.textview2.setText(String.valueOf((long) (this.selected)).concat(" IMAGES SELECTED, TAP HERE TO CONTINUE"));
        } else {
            this.linear3.setEnabled(false);
            this.textview2.setText("NO IMAGE SELECTED");
        }
    }


    public void _setImageFromFile(ImageView _img, String _path) {
        this._Glide(_img, _path);
    }


    public void _Glide(ImageView _img, String _url) {
        Glide.with(this.getContext(this)).load(_url).thumbnail(Glide.with(this.getContext(this)).load(this.getContext(this).getResources().getIdentifier("loading", "drawable", this.getContext(this).getPackageName()))).fitCenter().into(_img);
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

    {
    }


    public void _applySetItem(double _pos, View _a, View _b, ImageView _c, TextView _d, TextView _f, View _e) {
        _a.setElevation(9f);
        _e.setVisibility(View.GONE);
        if (Objects.requireNonNull(this.itemsList.get((int) _pos).get("type")).toString().equals("folder")) {
            _f.setVisibility(View.GONE);
            if (FileUtil.getFileLength(Objects.requireNonNull(this.itemsList.get((int) _pos).get("icon")).toString()) > 0) {
                this._setImageFromFile(_c, Objects.requireNonNull(this.itemsList.get((int) _pos).get("icon")).toString());
            } else {
                _c.setImageResource(R.drawable.ic_folder);
            }
        } else {
            _f.setVisibility(View.VISIBLE);
            this._setImageFromFile(_c, Objects.requireNonNull(this.itemsList.get((int) _pos).get("path")).toString());
            if (Objects.requireNonNull(this.itemsList.get((int) _pos).get("select")).toString().equals("1")) {
                _e.setVisibility(View.VISIBLE);
            }
            if (Uri.parse(Objects.requireNonNull(this.itemsList.get((int) _pos).get("path")).toString()).getLastPathSegment().substring(Uri.parse(Objects.requireNonNull(this.itemsList.get((int) _pos).get("path")).toString()).getLastPathSegment().length() - ".".concat("mp4").length()).equals(".mp4")) {
                _f.setText("Video");
            } else {
                _f.setText("Image");
            }
        }
        _e.setBackgroundColor(Color.parseColor("#65000000"));
        _d.setText(Objects.requireNonNull(this.itemsList.get((int) _pos).get("name")).toString());
    }


    public void _checkAdd(String _folder, String _path) {
        if (this.datamap.containsKey(_folder)) {

        } else {
            this.stopLoop = false;
            FileUtil.listDir(_folder, this.tmpfiles);
            if (this.tmpfiles.size() > 0) {
                for (int n3 = 0; n3 < this.tmpfiles.size(); n3++) {
                    if (FileUtil.isFile(this.tmpfiles.get(n3)) && (this.tmpfiles.get(n3).contains(".") && this.imageFormats.contains("|".concat(this.tmpfiles.get(n3).substring(this.tmpfiles.get(n3).lastIndexOf(".")).concat("|"))))) {
                        this.icon = this.tmpfiles.get(n3);
                        this.stopLoop = true;
                        break;
                    }
                }
                this.tmpfiles.clear();
            } else {

            }
            if (this.stopLoop) {
                if (this.freeze) {

                } else {
                    this.datamap.put(_folder, "true");
                    {
                        final HashMap<String, Object> _item = new HashMap<>();
                        _item.put("type", "folder");
                        this.itemsList.add(_item);
                    }

                    this.itemsList.get(this.itemsList.size() - 1).put("path", _folder);
                    this.itemsList.get(this.itemsList.size() - 1).put("name", Uri.parse(_folder).getLastPathSegment());
                    this.itemsList.get(this.itemsList.size() - 1).put("icon", this.icon);
                    this.result.edit().putString(this.A, new Gson().toJson(this.itemsList)).apply();
                    this.result.edit().putString(this.B, new Gson().toJson(this.datamap)).apply();
                }
            }
        }
    }


    public void _startProcess(ArrayList<String> _fl) {
        this.progressbar1.setVisibility(View.VISIBLE);
        //background executor create here
        final java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {

            @Override

            public void run() {
                ImagespickerActivity.this._filterToFolder(_fl.get(0));
                _fl.remove(0);
                handler.post(new Runnable() {

                    @Override

                    public void run() {
                        if (ImagespickerActivity.this.freeze) {
                            ImagespickerActivity.this.progressbar1.setVisibility(View.GONE);
                        } else {
                            if (ImagespickerActivity.this.stopLoop) {
                                ImagespickerActivity.this.gridview1.invalidateViews();
                            }
                            if (_fl.size() == 0) {
                                ImagespickerActivity.this.progressbar1.setVisibility(View.GONE);
                            } else {
                                ImagespickerActivity.this._startProcess(_fl);
                            }
                        }

                        //UI Thread work here

                    }
                });
                //background task start here


            }
        });

    }


    public void _Bg() {
        this.linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        if (FileUtil.isFile(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"))) {
            this.imageviewBackGround.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/bg.png"), 1024, 1024));
        } else {
//            imageviewBackGround.setImageResource(R.drawable.bgi);
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
            final LayoutInflater _inflater = (LayoutInflater) ImagespickerActivity.this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.grid_item, null);
            }

            RelativeLayout linear5 = _view.findViewById(R.id.linear5);
            LinearLayout linear_base = _view.findViewById(R.id.linear_base);
            LinearLayout linear2 = _view.findViewById(R.id.linear2);
            LinearLayout linear3 = _view.findViewById(R.id.linear3);
            LinearLayout linear1 = _view.findViewById(R.id.linear1);
            ImageView imageview1 = _view.findViewById(R.id.imageview1);
            LinearLayout linear4 = _view.findViewById(R.id.linear4);
            TextView textview2 = _view.findViewById(R.id.textview2);
            TextView textview1 = _view.findViewById(R.id.textview1);
            ImageView imageview2 = _view.findViewById(R.id.imageview2);

            ImagespickerActivity.this._SetBackground(linear4, 20, 0, "#65000000", false);
            try {
                ImagespickerActivity.this._applySetItem(_position, linear1, linear2, imageview1, textview1, textview2, linear3);

            } catch (final Exception e) {
            }
            ImagespickerActivity.this.bindN = _position;
            textview2.setTypeface(Typeface.createFromAsset(ImagespickerActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.BOLD);
            textview1.setTypeface(Typeface.createFromAsset(ImagespickerActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);

            return _view;
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
