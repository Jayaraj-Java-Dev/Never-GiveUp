package com.jayaraj.CrazyChat7.ImageVidDispActs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;

import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.text.*;
import android.util.*;

import java.io.File;
import java.util.*;

import java.util.HashMap;
import java.util.ArrayList;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;

import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Timer;
import java.util.TimerTask;

import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.gson.Gson;
import com.bumptech.glide.Glide;

import android.graphics.Typeface;

import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;

public class MultiPickerActivity extends AppCompatActivity {
    private static final int REQ_CD_FP = 101;
    private final Timer _timer = new Timer();

    private double n1;
    private double pos;
    private final boolean userchange = false;
    private final boolean timer = false;
    private boolean checkfor0;
    private final boolean vidpause = false;
    private final HashMap<String, Object> edittextmap = new HashMap<>();
    private boolean playing;
    private final HashMap<String, Object> tm = new HashMap<>();
    private final double tn = 0;
    private boolean multi;
    private String fontName = "";
    private String media = "";
    private final String typeace = "";

    private final ArrayList<HashMap<String, Object>> listResults = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> cache = new ArrayList<>();

    private LinearLayout linear1;
    private RelativeLayout linear4;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear5;
    private TextView textview1;
    private ViewPager viewpager1;
    private LinearLayout linear7;
    private EditText edittext1;
    private Button button1;
    private Button button2;
    private ImageView imageview1;
    private Button button3;

    private final Intent back = new Intent();
    private final Intent picker = new Intent();
    private SharedPreferences result;
    private TimerTask aft;
    private SharedPreferences pathssp;


    private Jay jay;


    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.multi_picker);
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
        this.linear1 = this.findViewById(R.id.linear1);
        this.linear4 = this.findViewById(R.id.linear4);
        this.linear2 = this.findViewById(R.id.linear2);
        this.linear3 = this.findViewById(R.id.linear3);
        this.linear5 = this.findViewById(R.id.linear5);
        this.textview1 = this.findViewById(R.id.textview1);
        this.viewpager1 = this.findViewById(R.id.viewpager1);
        this.linear7 = this.findViewById(R.id.linear7);
        this.edittext1 = this.findViewById(R.id.edittext1);
        this.button1 = this.findViewById(R.id.button1);
        this.button2 = this.findViewById(R.id.button2);
        this.imageview1 = this.findViewById(R.id.imageview1);
        this.button3 = this.findViewById(R.id.button3);
        this.result = this.getSharedPreferences("sharedpreferences", Context.MODE_PRIVATE);
        this.pathssp = this.getSharedPreferences("paths", Context.MODE_PRIVATE);

        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        if (this.getIntent().getStringExtra("files").equals("img")) {
            this.textview1.setText("PICK IMAGES");
        } else {
            this.textview1.setText("PICK VIDEOS");
        }

        this.viewpager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int _position, final float _positionOffset, final int _positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int _position) {
                MultiPickerActivity.this.pos = _position;
                MultiPickerActivity.this.edittext1.setText(MultiPickerActivity.this.listResults.get(_position).get("text").toString());
                MultiPickerActivity.this.edittext1.requestFocus();
            }

            @Override
            public void onPageScrollStateChanged(final int _scrollState) {
                if (2 == _scrollState) {
                    if (Uri.parse(MultiPickerActivity.this.listResults.get((int) MultiPickerActivity.this.pos).get("path").toString()).getLastPathSegment().substring(Uri.parse(MultiPickerActivity.this.listResults.get((int) MultiPickerActivity.this.pos).get("path").toString()).getLastPathSegment().length() - ".".concat("mp4").length()).equals(".mp4")) {
                        if (MultiPickerActivity.this.playing) {
                            MultiPickerActivity.this.playing = false;
                            MultiPickerActivity.this.checkfor0 = true;
                        } else {

                        }
                    } else {

                    }
                }
                if (MultiPickerActivity.this.checkfor0) {
                    if (0 == _scrollState) {
                        MultiPickerActivity.this.viewpager1.setAdapter(new Viewpager1Adapter(MultiPickerActivity.this.listResults));
                        MultiPickerActivity.this.viewpager1.setCurrentItem((int) MultiPickerActivity.this.pos);
                        MultiPickerActivity.this.checkfor0 = false;
                    }
                } else {

                }
            }
        });

        this.edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence _param1, final int _param2, final int _param3, final int _param4) {
                String _charSeq = _param1.toString();
                if (0 == MultiPickerActivity.this.listResults.size()) {

                } else {
                    MultiPickerActivity.this.listResults.get(MultiPickerActivity.this.viewpager1.getCurrentItem()).put("text", _charSeq);
                }
            }

            @Override
            public void beforeTextChanged(final CharSequence _param1, final int _param2, final int _param3, final int _param4) {

            }

            @Override
            public void afterTextChanged(final Editable _param1) {

            }
        });

        this.button1.setOnClickListener(_view -> this.finish());

        this.button2.setOnClickListener(_view -> {
            final Intent i;
            if (this.getIntent().getStringExtra("files").equals("img")) {
                i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            } else {
                i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            }

            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            this.startActivityForResult(i, MultiPickerActivity.REQ_CD_FP);
//                picker.setClass(getApplicationContext(), ImagespickerActivity.class);
//                picker.putExtra("files", getIntent().getStringExtra("files"));
//                try {
//                    picker.putExtra("multiple_images", getIntent().getStringExtra("multiple_images"));
//                } catch (Exception e) {
//                    picker.putExtra("multiple_images", "true");
//                }
//                startActivity(picker);
        });

        this.button3.setOnClickListener(_view -> {
            this.pathssp.edit().putString("media", media).apply();
            this.pathssp.edit().putString("uid", this.getIntent().getStringExtra("UID")).apply();
            this.pathssp.edit().putString("uploadlist", new Gson().toJson(this.listResults)).apply();
            this.finish();
        });
    }

    private void initializeLogic() {
        this._changeActivityFont("font1");
        this.edittext1.setSingleLine(true);
        Glide.with(this.getApplicationContext()).load(Uri.parse("y")).into(this.imageview1);
        this.setTitle("Multiple Images Picker");
        this.result.edit().putString("result", "-").apply();
        this.checkfor0 = false;
        this.playing = false;
        this.viewpager1.setAdapter(new Viewpager1Adapter(this.listResults));
        //_SetGradientView(linear1, "#2B5962", "#111828");
        this._Bg();
        //linear1.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._refreshviews();
        try {
            this.multi = this.getIntent().getStringExtra("multiple_images").equals("true");
        } catch (final Exception e) {
            this.multi = true;
        }
        this._SetBackground(this.button1, 20, 5, "#f44336", true);
        this._SetBackground(this.button2, 20, 5, "#00bcd4", true);
        this._SetBackground(this.button3, 20, 5, "#03a9f4", true);
        this._SetBackground(this.edittext1, 20, 5, "#00bcd4", true);
        this.edittext1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.edittext1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
    }


    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {
            case MultiPickerActivity.REQ_CD_FP:
                if (_resultCode == Activity.RESULT_OK) {
                    final ArrayList<String> _filePath = new ArrayList<>();
                    if (_data != null) {
                        if (_data.getClipData() != null) {
                            for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
                                final ClipData.Item _item = _data.getClipData().getItemAt(_index);
                                _filePath.add(FileUtil.convertUriToFilePath(this.getApplicationContext(), _item.getUri()));
                            }
                        } else {
                            _filePath.add(FileUtil.convertUriToFilePath(this.getApplicationContext(), _data.getData()));
                        }
                    }
                    for (int n1 = 0; n1 < _filePath.size() && this.listResults.size() < 15; n1++) {
                        if (new File(_filePath.get(n1)).length() < 35000001) {
                            {
                                final HashMap<String, Object> _item = new HashMap<>();
                                _item.put("path", Objects.requireNonNull(_filePath.get(n1)));
                                this.listResults.add(_item);
                            }

                            this.listResults.get(this.listResults.size() - 1).put("text", "");
                            String typ;
                            if (Uri.parse(_filePath.get(n1)).getLastPathSegment().endsWith(".mp4")) {
                                this.listResults.get(this.listResults.size() - 1).put("type", "4");
                                typ="4";
                            } else {
                                if (Uri.parse(_filePath.get(n1)).getLastPathSegment().startsWith(".3gp", Uri.parse(_filePath.get(n1)).getLastPathSegment().length() - ".3gp".length())) {
                                    this.listResults.get(this.listResults.size() - 1).put("type", "4");
                                    typ="4";
                                } else {
                                    this.listResults.get(this.listResults.size() - 1).put("type", "2");
                                    typ="2";
                                }
                            }
                            if (media.equals("")) {
                                media = typ;
                            }

                        }
                    }
                    Objects.requireNonNull(this.viewpager1.getAdapter()).notifyDataSetChanged();
                    this._refreshviews();

                } else {

                }
                break;
            default:
                break;
        }
    }

    void add() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!this.result.getString("result", "").equals("-")) {
            this._loadResults();
            this.result.edit().putString("result", "-").apply();
        }
    }


    public void _loadResults() {
        if (this.multi) {
            this.cache.clear();
            this.cache = new Gson().fromJson(this.result.getString("result", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            this.n1 = 0;
            for (int _repeat13 = 0; _repeat13 < this.cache.size(); _repeat13++) {
                {
                    final HashMap<String, Object> _item = new HashMap<>();
                    _item.put("path", Objects.requireNonNull(this.cache.get((int) this.n1).get("path")).toString());
                    this.listResults.add(_item);
                }

                this.listResults.get(this.listResults.size() - 1).put("text", "");
                if (Uri.parse(Objects.requireNonNull(this.cache.get((int) this.n1).get("path")).toString()).getLastPathSegment().substring(Uri.parse(Objects.requireNonNull(this.cache.get((int) this.n1).get("path")).toString()).getLastPathSegment().length() - ".".concat("mp4").length()).equals(".mp4")) {
                    this.listResults.get(this.listResults.size() - 1).put("type", "4");
                } else {
                    if (Uri.parse(Objects.requireNonNull(this.cache.get((int) this.n1).get("path")).toString()).getLastPathSegment().startsWith(".".concat(".3gp"), Uri.parse(Objects.requireNonNull(this.cache.get((int) this.n1).get("path")).toString()).getLastPathSegment().length() - ".".concat(".3gp").length())) {
                        this.listResults.get(this.listResults.size() - 1).put("type", "4");
                    } else {
                        this.listResults.get(this.listResults.size() - 1).put("type", "2");
                    }
                }
                this.n1++;
            }
            this.cache.clear();
            Objects.requireNonNull(this.viewpager1.getAdapter()).notifyDataSetChanged();
            this._refreshviews();
        } else {
            SketchwareUtil.showMessage(this.getApplicationContext(), this.result.getString("result", ""));
            this.listResults.clear();
            {
                final HashMap<String, Object> _item = new HashMap<>();
                _item.put("path", this.result.getString("result", ""));
                this.listResults.add(_item);
            }

            this.listResults.get(this.listResults.size() - 1).put("text", "");
        }
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


    public void _refreshForDel(double _pos) {
        if (1 == this.listResults.size()) {
            this.listResults.remove(0);
            this.viewpager1.setAdapter(new Viewpager1Adapter(this.listResults));
            this.edittext1.setText("");
        } else {
            this.listResults.remove((int) (_pos));
            this.viewpager1.setAdapter(new Viewpager1Adapter(this.listResults));
            if ((this.listResults.size() - 1) == _pos) {
                this.viewpager1.setCurrentItem(this.listResults.size() - 1);
            } else {
                this.viewpager1.setCurrentItem((int) _pos);
            }
        }
        this._refreshviews();
    }


    public void _refreshviews() {
        if (0 == this.listResults.size()) {
            this.viewpager1.setVisibility(View.GONE);
            this.textview1.setVisibility(View.VISIBLE);
            this.linear5.setVisibility(View.GONE);
            this.button3.setVisibility(View.GONE);
        } else {
            this.viewpager1.setVisibility(View.VISIBLE);
            this.textview1.setVisibility(View.GONE);
            this.linear5.setVisibility(View.VISIBLE);
            this.button3.setVisibility(View.VISIBLE);
            this.edittext1.setText(Objects.requireNonNull(this.listResults.get(this.viewpager1.getCurrentItem()).get("text")).toString());
        }
    }


    public void _Bg() {
        final Jay jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._SetGradientView(this.linear1, jay.col(0, 3), jay.col(0, 4));
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


    public void _lib() {
    }

    public static class ZoomableImageView extends androidx.appcompat.widget.AppCompatImageView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

        private Matrix mCurrentMatrix;
        private GestureDetector mGestureDetector;
        private static final String TAG = "ZoomView";
        private ScaleGestureDetector scaleGestureDetector;
        private final PointF mRect = new PointF();
        private final PointF mCurrentZoomPoint = new PointF();
        private MatrixValueManager matrixValueManager, mImageMatrixManager;
        private final android.os.Handler mHandler = new android.os.Handler();
        private float mLastPositionY;
        private float mLastPositionX;
        private boolean isZooming;

        public ZoomableImageView(final Context context) {
            super(context);
            this.initaial();
        }

        @Override
        public void setImageBitmap(final Bitmap bm) {
            super.setImageBitmap(bm);
        }

        @Override
        public void setImageDrawable(final Drawable drawable) {
            super.setImageDrawable(drawable);
        }

        public ZoomableImageView(final Context context, final android.util.AttributeSet attrs) {
            super(context, attrs);
            this.initaial();
        }

        public ZoomableImageView(final Context context, final android.util.AttributeSet attrs, final int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.initaial();
        }

        private void initaial() {
            this.matrixValueManager = new MatrixValueManager();
            this.mImageMatrixManager = new MatrixValueManager();
            this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            this.mCurrentMatrix = this.getImageMatrix();
            this.mGestureDetector = new GestureDetector(this.getContext(), this);
            this.scaleGestureDetector = new ScaleGestureDetector(this.getContext(), this);
            this.mGestureDetector.setOnDoubleTapListener(this);

            setOnTouchListener((view, motionEvent) -> {
                this.scaleGestureDetector.onTouchEvent(motionEvent);
                this.mGestureDetector.onTouchEvent(motionEvent);
                return true;
            });
        }

        @Override
        public void invalidate() {
            super.invalidate();
            this.matrixValueManager.setMatrix(this.mCurrentMatrix);
        }

        protected void onActionUp() {
            if (this.matrixValueManager.getScaleX() <= 1) {
                this.zoomAnimation(1.0f);
            } else {
                this.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ZoomableImageView.this.adjustPosition();
                    }
                });
            }
        }

        private void adjustPosition() {

            final float imgH = (this.getHeight() - (this.mImageMatrixManager.getTransitionY() * 2)) * this.matrixValueManager.getScaleY();
            final float mY = (this.matrixValueManager.getTransitionY() + (this.mImageMatrixManager.getTransitionY()) * this.matrixValueManager.getScaleY());
            final float scrollAbleY = (this.getHeight() - imgH);

            final float vH = ((this.getHeight() * this.matrixValueManager.getScaleY()) - this.getHeight()) / 2;
            final float vW = ((this.getWidth() * this.matrixValueManager.getScaleX()) - this.getWidth()) / 2;

            float x = 0, y = 0;

            if (imgH < this.getHeight()) {
                y = (-vH - this.matrixValueManager.getTransitionY());
            } else if (imgH >= this.getHeight()) {
                if (mY > 0) {
                    y = -mY;
                } else if (mY < scrollAbleY) {
                    y = scrollAbleY - mY;
                }
            }

            final float mX = (this.matrixValueManager.getTransitionX() + (this.mImageMatrixManager.getTransitionX()) * this.matrixValueManager.getScaleX());
            final float imgW = (this.getWidth() - (this.mImageMatrixManager.getTransitionX() * 2)) * this.matrixValueManager.getScaleX();
            final float scrollAbleX = (this.getWidth() - imgW);

            if (imgW < this.getWidth()) {
                x = (-vW - this.matrixValueManager.getTransitionX());
            } else if (imgW >= this.getWidth()) {
                if (mX > 0) {
                    x = -mX;
                } else if (mX < scrollAbleX) {
                    x = scrollAbleX - mX;
                }
            }

            if (x != 0 || y != 0) {
                this.moveAnimation(x, y);
            } else {
                this.findCurrentZoomPoint();
            }
        }

        private void moveAnimation(float x, float y) {

            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ZoomableImageView.this.mLastPositionY = 0;
                    ZoomableImageView.this.mLastPositionX = 0;
                    final android.animation.PropertyValuesHolder valueY = android.animation.PropertyValuesHolder.ofFloat("y", 0, y);
                    final android.animation.PropertyValuesHolder valueX = android.animation.PropertyValuesHolder.ofFloat("x", 0, x);
                    final android.animation.ValueAnimator anim = new android.animation.ValueAnimator();
                    anim.setValues(valueX, valueY);
                    anim.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(final android.animation.ValueAnimator animation) {
                            final float valueX = (float) animation.getAnimatedValue("x");
                            final float valueY = (float) animation.getAnimatedValue("y");
                            ZoomableImageView.this.mCurrentMatrix.postTranslate(valueX - ZoomableImageView.this.mLastPositionX, valueY - ZoomableImageView.this.mLastPositionY);
                            ZoomableImageView.this.matrixValueManager.setMatrix(ZoomableImageView.this.mCurrentMatrix);
                            ZoomableImageView.this.postInvalidate();
                            ZoomableImageView.this.mLastPositionY = valueY;
                            ZoomableImageView.this.mLastPositionX = valueX;
                            if (valueX >= x && valueY >= y) {
                                ZoomableImageView.this.findCurrentZoomPoint();
                            }
                        }
                    });
                    anim.setDuration(250);
                    anim.start();
                }
            });

        }

        private void move(final float x, final float y) {
            this.mCurrentMatrix.postTranslate(x, y);
            this.postInvalidate();
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ZoomableImageView.this.findCurrentZoomPoint();
                }
            });
        }

        @Override
        protected void onDraw(final Canvas canvas) {
            canvas.save();
            canvas.concat(this.mCurrentMatrix);
            this.matrixValueManager.setMatrix(this.mCurrentMatrix);
            this.mImageMatrixManager.setMatrix(this.getImageMatrix());
            super.onDraw(canvas);
            canvas.restore();
        }

        @Override
        public boolean onDown(final MotionEvent motionEvent) {
            this.mRect.set(motionEvent.getX(motionEvent.getPointerCount() - 1), motionEvent.getY(motionEvent.getPointerCount() - 1));
            return true;
        }

        @Override
        public void onShowPress(final MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(final MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, final float v, final float v1) {
            if (!this.isZooming) {
                if (!this.mRect.equals(motionEvent1.getX(), motionEvent1.getY())) {
                    this.calculatePosition(motionEvent1.getX(), motionEvent1.getY());
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canScrollHorizontally(final int direction) {
            return this.matrixValueManager.getScaleX() > 1;
        }

        @Override
        public boolean canScrollVertically(final int direction) {
            return this.matrixValueManager.getScaleY() > 1;
        }

        private void calculatePosition(final float rawX, final float rawY) {
            float x = (rawX - this.mRect.x);
            float y = (rawY - this.mRect.y);

            final float mY = (this.matrixValueManager.getTransitionY() + (this.mImageMatrixManager.getTransitionY() * this.matrixValueManager.getScaleY()));
            final float imgH = (this.getHeight() - (this.mImageMatrixManager.getTransitionY() * 2)) * this.matrixValueManager.getScaleY();
            final float scrollAbleY = (this.getHeight() - imgH);
            if (imgH > this.getHeight()) {
                final float r = (mY + y);
                final float s = (r - scrollAbleY);

                if (s < 0) {
                    y = 0;
                }

                if (r > 0) {
                    y = 0;
                }

            } else {
                y = 0;
            }

            final float mX = (this.matrixValueManager.getTransitionX() + (this.mImageMatrixManager.getTransitionX() * this.matrixValueManager.getScaleX()));
            final float imgW = (this.getWidth() - (this.mImageMatrixManager.getTransitionX() * 2)) * this.matrixValueManager.getScaleX();
            final float scrollAbleX = (this.getWidth() - imgW);
            if ((imgW) > this.getWidth()) {
                final float l = (mX + x);
                final float s = (l - scrollAbleX);

                if (s < 0) {
                    x = 0;
                }

                if (l > 0) {
                    x = 0;
                }

            } else {
                x = 0;
            }

            this.mRect.set(rawX, rawY);
            this.move(x, y);
        }

        private void findCurrentZoomPoint() {
            final float _x;
            final float _y;
            //X
            final float imgW = (this.getWidth() - (this.mImageMatrixManager.getTransitionX() * 2)) * this.matrixValueManager.getScaleX();
            final float scrollAbleX = (this.getWidth() - imgW);
            if (scrollAbleX < 0) {
                final float mX = ((this.matrixValueManager.getTransitionX() / this.matrixValueManager.getScaleX()));
                final float visibleScreenX = (this.getWidth() / this.matrixValueManager.getScaleX());
                final float percentX = ((Math.abs(mX)) * 100) / (this.getWidth() - visibleScreenX);
                _x = Math.abs(mX) + ((percentX * visibleScreenX) / 100);
            } else {
                _x = this.getWidth() / 2;
            }
            //Y
            final float imgH = (this.getHeight() - (this.mImageMatrixManager.getTransitionY() * 2)) * this.matrixValueManager.getScaleY();
            final float scrollAbleY = (this.getHeight() - imgH);
            if (scrollAbleY < 0) {
                final float mY = ((this.matrixValueManager.getTransitionY() / this.matrixValueManager.getScaleY()));
                final float visibleScreenY = (this.getHeight() / this.matrixValueManager.getScaleY());
                final float percentY = ((Math.abs(mY)) * 100) / (this.getHeight() - visibleScreenY);
                _y = Math.abs(mY) + ((percentY * visibleScreenY) / 100);
            } else {
                _y = this.getHeight() / 2;
            }
            this.mCurrentZoomPoint.set(_x, _y);
        }

        @Override
        public void onLongPress(final MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, final float v, final float v1) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
            return false;
        }

        float mLastScale;

        public void releaseZoom() {
            if (this.matrixValueManager.getScaleX() > 1 || this.matrixValueManager.getScaleY() > 1) {
                this.isZooming = true;
                this.mLastScale = 0;
                float scale = this.matrixValueManager.getScaleX();
                final android.animation.ValueAnimator valueAnimator = android.animation.ValueAnimator.ofFloat(scale, 1.0f);
                valueAnimator.setInterpolator(new android.view.animation.LinearInterpolator());
                valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(final android.animation.ValueAnimator animation) {
                        final float value = (float) animation.getAnimatedValue();
                        if (value != scale) {
                            ZoomableImageView.this.mCurrentMatrix.setScale(value, value, ZoomableImageView.this.mCurrentZoomPoint.x, ZoomableImageView.this.mCurrentZoomPoint.y);
                            ZoomableImageView.this.postInvalidate();
                            if (value == 1) {
                                ZoomableImageView.this.isZooming = false;
                            }
                        }
                    }
                });
                valueAnimator.setDuration(350);
                valueAnimator.start();
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            return true;
        }

        private void zoomAnimation(float scale) {
            this.isZooming = true;
            final android.animation.ValueAnimator valueAnimator = android.animation.ValueAnimator.ofFloat(this.matrixValueManager.getScaleX(), scale);
            valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final android.animation.ValueAnimator animation) {
                    final float value = (float) animation.getAnimatedValue();
                    ZoomableImageView.this.mCurrentMatrix.setScale(value, value, ZoomableImageView.this.mCurrentZoomPoint.x, ZoomableImageView.this.mCurrentZoomPoint.y);
                    ZoomableImageView.this.postInvalidate();
                    if (value == scale) {
                        ZoomableImageView.this.mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ZoomableImageView.this.adjustPosition();
                            }
                        }, 100);
                        ZoomableImageView.this.isZooming = false;
                    }
                }
            });
            valueAnimator.setDuration(250);
            valueAnimator.start();
        }

        @Override
        public boolean onDoubleTapEvent(final MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (this.matrixValueManager.getScaleX() > 1 || this.matrixValueManager.getScaleX() > 1) {
                    this.releaseZoom();
                } else {
                    this.mCurrentZoomPoint.set(motionEvent.getX(), motionEvent.getY());
                    this.zoomAnimation(2.0F);
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
            this.isZooming = true;
            final float scale = scaleGestureDetector.getCurrentSpan() / scaleGestureDetector.getPreviousSpan();
            if (this.matrixValueManager.getScaleX() >= 1) {
                final float focusX = scaleGestureDetector.getFocusX();
                final float focusY = scaleGestureDetector.getFocusY();
                this.mCurrentMatrix.postScale(scale, scale, focusX, focusY);
                this.postInvalidate();
                this.mCurrentZoomPoint.set(focusX, focusY);
                return true;
            }
            return false;
        }

        @Override
        public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
            this.mRect.set(scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            return true;
        }

        @Override
        public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
            this.mRect.set(scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            this.isZooming = false;
            this.onActionUp();
        }

        public static final class MatrixValueManager {

            float[] floats;

            public void setMatrix(final Matrix matrix) {
                this.floats = new float[9];
                matrix.getValues(this.floats);
            }

            public float getTransitionX() {
                return this.floats[Matrix.MTRANS_X];
            }

            public float getTransitionY() {
                return this.floats[Matrix.MTRANS_Y];
            }

            public float getScaleX() {
                return this.floats[Matrix.MSCALE_X];
            }

            public float getScaleY() {
                return this.floats[Matrix.MSCALE_Y];
            }

        }
    }

    private ZoomableImageView zoom1;

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
                    if ((v instanceof EditText)) {
                        ((EditText) v).setTypeface(typeace);
                    } else {
                        if ((v instanceof Button)) {
                            ((Button) v).setTypeface(typeace);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            SketchwareUtil.showMessage(this.getApplicationContext(), "Error Loading Font");
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


    public class Viewpager1Adapter extends PagerAdapter {
        Context _context;
        ArrayList<HashMap<String, Object>> _data;

        public Viewpager1Adapter(final Context _ctx, final ArrayList<HashMap<String, Object>> _arr) {
            this._context = _ctx;
            this._data = _arr;
        }

        public Viewpager1Adapter(final ArrayList<HashMap<String, Object>> _arr) {
            this._context = MultiPickerActivity.this.getApplicationContext();
            this._data = _arr;
        }

        @Override
        public int getCount() {
            return this._data.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull final View _view, @NonNull final Object _object) {
            return _view == _object;
        }

        @Override
        public void destroyItem(final ViewGroup _container, final int _position, @NonNull final Object _object) {
            _container.removeView((View) _object);
        }

        @Override
        public int getItemPosition(@NonNull final Object _object) {
            return super.getItemPosition(_object);
        }

        @Override
        public CharSequence getPageTitle(final int pos) {
            // use the activitiy event (onTabLayoutNewTabAdded) in order to use this method
            return "page " + pos;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup _container, int _position) {
            final View _view = LayoutInflater.from(this._context).inflate(R.layout.viewfilecus, _container, false);

            LinearLayout linear1 = _view.findViewById(R.id.linear1);
            LinearLayout linear8 = _view.findViewById(R.id.linear8);
            LinearLayout linear3 = _view.findViewById(R.id.linear3);
            RelativeLayout linear7 = _view.findViewById(R.id.linear7);
            LinearLayout linear11 = _view.findViewById(R.id.linear11);
            LinearLayout linear9 = _view.findViewById(R.id.linear9);
            LinearLayout linearIMG = _view.findViewById(R.id.linearIMG);
            LinearLayout linear5 = _view.findViewById(R.id.linear5);
            VideoView videoview1 = _view.findViewById(R.id.videoview1);
            ImageView imageview2 = _view.findViewById(R.id.imageview2);
            TextView textview2 = _view.findViewById(R.id.textview2);
            Button button1 = _view.findViewById(R.id.button1);

            MultiPickerActivity.this.zoom1 = new ZoomableImageView(MultiPickerActivity.this);
            MultiPickerActivity.this.zoom1.setScaleType(ImageView.ScaleType.FIT_XY);
            MultiPickerActivity.this.zoom1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearIMG.addView(MultiPickerActivity.this.zoom1);
            MultiPickerActivity.this.zoom1.setScaleType(ImageView.ScaleType.FIT_CENTER);
            MultiPickerActivity.this._Glide(MultiPickerActivity.this.zoom1, Objects.requireNonNull(MultiPickerActivity.this.listResults.get(_position).get("path")).toString());
            //_SetGradientView(zoom1, "#2B5962", "#111828");
            //zoom1.setBackgroundColor(Color.parseColor(jay.col(0,2)));
            MultiPickerActivity.this._SetGradientView(MultiPickerActivity.this.zoom1, MultiPickerActivity.this.jay.col(0, 3), MultiPickerActivity.this.jay.col(0, 4));
            MultiPickerActivity.this.textview1.setTextColor(Color.parseColor(MultiPickerActivity.this.jay.col(1, 1)));
            MultiPickerActivity.this._SetBackground(button1, 20, 5, "#03a9f4", true);
            textview2.setText(Uri.parse(Objects.requireNonNull(MultiPickerActivity.this.listResults.get(_position).get("path")).toString()).getLastPathSegment());
            videoview1.setVisibility(View.GONE);
            linearIMG.setVisibility(View.VISIBLE);
            if (Uri.parse(Objects.requireNonNull(MultiPickerActivity.this.listResults.get(_position).get("path")).toString()).getLastPathSegment().substring(Uri.parse(Objects.requireNonNull(MultiPickerActivity.this.listResults.get(_position).get("path")).toString()).getLastPathSegment().length() - ".".concat("mp4").length()).equals(".mp4")) {
                imageview2.setVisibility(View.VISIBLE);
                linear5.setVisibility(View.VISIBLE);
                videoview1.setVideoURI(Uri.parse(Objects.requireNonNull(MultiPickerActivity.this.listResults.get(_position).get("path")).toString()));
                linear5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View _view) {
                        videoview1.setVisibility(View.VISIBLE);
                        linearIMG.setVisibility(View.GONE);
                        if (videoview1.isPlaying()) {
                            imageview2.setVisibility(View.VISIBLE);
                            videoview1.pause();
                            MultiPickerActivity.this.playing = false;
                        } else {
                            imageview2.setVisibility(View.GONE);
                            videoview1.start();
                            MultiPickerActivity.this.playing = true;
                        }
                    }
                });
            } else {
                linear5.setVisibility(View.GONE);
            }
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View _view) {
                    if (videoview1.isPlaying()) {
                        imageview2.setVisibility(View.VISIBLE);
                        videoview1.pause();
                        MultiPickerActivity.this.playing = false;
                    } else {

                    }
                    videoview1.setVisibility(View.GONE);
                    linearIMG.setVisibility(View.VISIBLE);
                    MultiPickerActivity.this._refreshForDel(_position);
                }
            });
            textview2.setTypeface(Typeface.createFromAsset(MultiPickerActivity.this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            button1.setTypeface(Typeface.createFromAsset(MultiPickerActivity.this.getAssets(), "fonts/font1.ttf"), Typeface.NORMAL);

            _container.addView(_view);
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
