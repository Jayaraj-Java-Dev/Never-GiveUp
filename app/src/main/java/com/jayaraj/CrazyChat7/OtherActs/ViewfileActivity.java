package com.jayaraj.CrazyChat7.OtherActs;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayaraj.CrazyChat7.J.FileUtil;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ViewfileActivity extends AppCompatActivity {
    private final Timer _timer = new Timer();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

    private double pos;
    private boolean playing;
    private boolean checkfor0;
    private final HashMap<String, Object> idmap = new HashMap<>();
    private HashMap<String, Object> onreMap = new HashMap<>();
    private boolean me;
    private String tpath = "";
    private String file = "";
    private double tnum;
    private boolean ont;
    private double scrollstateval;
    private ArrayList<HashMap<String, Object>> lmap = new ArrayList<>();

	private ImageView imageview1;
    private LinearLayout background;
    private LottieAnimationView lottie1;
    private ViewPager viewpager1;

    private SharedPreferences LOF1;
    private SharedPreferences LOF2;
    private SharedPreferences spview;
    private SharedPreferences lsp;
    private TimerTask at;
    private SharedPreferences asp;
    private FirebaseAuth fauth;
    private SharedPreferences list;
    private DatabaseReference dbSend = this._firebase.getReference("SenderUid");
    private Calendar msgc = Calendar.getInstance();
    private final Calendar calg = Calendar.getInstance();
    private SharedPreferences imgurls;
    private Jay jay;


    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.viewfile);
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
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            this.initializeLogic();
        }
    }

    private void initialize(final Bundle _savedInstanceState) {

        this.imageview1 = this.findViewById(R.id.imageview1);
        this.background = this.findViewById(R.id.background);
        this.lottie1 = this.findViewById(R.id.lottie1);
        this.viewpager1 = this.findViewById(R.id.viewpager1);
        this.LOF1 = this.getSharedPreferences("LOF", Context.MODE_PRIVATE);
        this.LOF2 = this.getSharedPreferences("LOF2", Context.MODE_PRIVATE);
        this.spview = this.getSharedPreferences("sp", Context.MODE_PRIVATE);
        this.lsp = this.getSharedPreferences("lsp", Context.MODE_PRIVATE);
        this.asp = this.getSharedPreferences("asp", Context.MODE_PRIVATE);
        this.fauth = FirebaseAuth.getInstance();
        this.list = this.getSharedPreferences("list", Context.MODE_PRIVATE);
        this.imgurls = this.getSharedPreferences("urls", Context.MODE_PRIVATE);
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));

        this.viewpager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int _position, final float _positionOffset, final int _positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int _position) {
                ViewfileActivity.this.pos = _position;
                ViewfileActivity.this._setFabText(ViewfileActivity.this._getMsg(_position, "msg"));
                ViewfileActivity.this._setFabstage(Double.parseDouble(Objects.requireNonNull(ViewfileActivity.this.idmap.get(ViewfileActivity.this._getMsg(_position, "id"))).toString()));
                ViewfileActivity.this.calg.setTimeInMillis((long) (Double.parseDouble(ViewfileActivity.this._getMsg(_position, "ms"))));
                ViewfileActivity.this.fab.textview2.setText(new SimpleDateFormat("hh:mm").format(ViewfileActivity.this.calg.getTime()));
                ViewfileActivity.this.fab.textview3.setText(new SimpleDateFormat("a").format(ViewfileActivity.this.calg.getTime()));
                if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(ViewfileActivity.this._getMsg(_position, "by"))) {

                } else {
                    if (ViewfileActivity.this._getMsg(_position, "typ").equals("2")) {
                        if (!ViewfileActivity.this._getMsg(_position, "mainl").equals("")) {
                            ViewfileActivity.this._UpdateAsViewed(_position);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(final int _scrollState) {
                ViewfileActivity.this.scrollstateval = _scrollState;
                if (2 == _scrollState) {
                    if ("4".equals(ViewfileActivity.this._getMsg(ViewfileActivity.this.pos, "typ"))) {
                        if (ViewfileActivity.this.playing) {
                            ViewfileActivity.this.playing = false;
                            ViewfileActivity.this.checkfor0 = true;
                        } else {

                        }
                    } else {

                    }
                }
                if (ViewfileActivity.this.checkfor0) {
                    if (0 == _scrollState) {
                        ViewfileActivity.this.viewpager1.setAdapter(new Viewpager1Adapter(ViewfileActivity.this.lmap));
                        if (ViewfileActivity.this.pos < ViewfileActivity.this.lmap.size()) {
                            ViewfileActivity.this.viewpager1.setCurrentItem((int) ViewfileActivity.this.pos);
                        }
                        ViewfileActivity.this.checkfor0 = false;
                    }
                } else {

                }
            }
        });

    }

    private void initializeLogic() {
        this._addFab(this.background);
        this.dbSend = this._firebase.getReference("chat/".concat(this.getIntent().getStringExtra("uid").concat("/notify")));
        this.list.registerOnSharedPreferenceChangeListener(this.listChangeListener);
        this.lmap = new Gson().fromJson(this.getIntent().getStringExtra("list"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        for (int nm = 0; nm < this.lmap.size(); nm++) {
            this.idmap.put(Objects.requireNonNull(this.lmap.get(nm).get("id")).toString().concat("pos"), String.valueOf((long) (nm)));
            if (this.lmap.get(nm).containsKey("stage")) {
                this.idmap.put(Objects.requireNonNull(this.lmap.get(nm).get("id")).toString(), Objects.requireNonNull(this.lmap.get(nm).get("stage")).toString());
            } else {
                this.idmap.put(Objects.requireNonNull(this.lmap.get(nm).get("id")).toString(), "1");
            }
        }
        this.me = this.getIntent().getStringExtra("by").equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        this.viewpager1.setAdapter(new Viewpager1Adapter(this.lmap));
        this.pos = 0;
        this.playing = false;
        this.checkfor0 = false;
        this.viewpager1.setCurrentItem(0);
        Glide.with(this.getApplicationContext()).load(Uri.parse("f")).into(this.imageview1);
        //_SetGradientView(viewpager1, "#2B5962", "#111828");
        this._Bg();
        if (this.lmap.get(0).containsKey("msg")) {
            this._setFabText(this._getMsg(0, "msg"));
        } else {
            this._setFabText("");
        }
        this._setFabstage(Double.parseDouble(Objects.requireNonNull(this.idmap.get(this._getMsg(0, "id"))).toString()));
        this.fab.lottie1.performClick();
        this.calg.setTimeInMillis((long) (Double.parseDouble(this._getMsg(0, "ms"))));
        this.fab.textview2.setText(new SimpleDateFormat("hh:mm").format(this.calg.getTime()));
        this.fab.textview3.setText(new SimpleDateFormat("a").format(this.calg.getTime()));
        this.fab.textview1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
        this.fab.textview2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/font1.ttf"), Typeface.NORMAL);
        this.fab.textview3.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/font1.ttf"), Typeface.NORMAL);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(this._getMsg(0, "by"))) {

        } else {
            if ("2".equals(this._getMsg(0, "typ"))) {
                if ("".equals(this._getMsg(0, "mainl"))) {

                } else {
                    this._UpdateAsViewed(0);
                }
            } else {

            }
        }
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
    public void onResume() {
        super.onResume();
        this.viewpager1.setCurrentItem(this.viewpager1.getCurrentItem());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.list.unregisterOnSharedPreferenceChangeListener(this.listChangeListener);
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
            setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View view, MotionEvent motionEvent) {
                    ZoomableImageView.this.scaleGestureDetector.onTouchEvent(motionEvent);
                    ZoomableImageView.this.mGestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
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


    public void _Bg() {
        //linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        final Jay jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        this._SetGradientView(this.viewpager1,jay.col(0,3),jay.col(0,4));
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0,3)));

    }

    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(final int a) { setCornerRadius(a);return this; } }.getIns((int)0));
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

    private SketchwareExtendedFab fab;

    public class SketchwareExtendedFab {

        private final ATX view;
        private final ImageView image_1;
        private final ImageView image_2;
        private final TextView textview1;
        private final TextView textview2;
        private final TextView textview3;
        private final LottieAnimationView lottie1;
        private final LinearLayout ll1;
        private int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

        private final int p;

        private android.animation.ValueAnimator anim;

        private boolean extended = true;

        public class ATX extends LinearLayout {
            private boolean r;
            private float w;

            public ATX(final Context a) {
                super(a);
            }

            public void req() {
                this.r = true;
                this.invalidate();
            }

            protected void onDraw(final Canvas a) {
                if (this.r) {
                    this.r = false;
                    this.w = (float) a.getWidth();
                }
            }
        }

        public SketchwareExtendedFab(final Context a) {
            this.p = (int) ViewfileActivity.this.getDip(6);
            final View gk = ViewfileActivity.this.getLayoutInflater().inflate(R.layout.fabsviewfilecus, null);
            final LinearLayout gt = gk.findViewById(R.id.base);
            this.view = new ATX(a);
            this.view.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            ((ViewGroup) gt.getParent()).removeView(gt);
            this.view.addView(gt);
            gt.setBackgroundColor(Color.TRANSPARENT);
            this.view.setPadding(0, 0, 0, 0);
            ((LinearLayout.LayoutParams) this.view.getLayoutParams()).setMargins(this.p, this.p, this.p, this.p);
            this.view.req();
            this.image_1 = this.view.findViewById(R.id.forward);
            this.textview1 = this.view.findViewById(R.id.textview1);
            this.textview2 = this.view.findViewById(R.id.textview2);
            this.textview3 = this.view.findViewById(R.id.textview3);

            this.image_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {
                    ViewfileActivity.this.showMessage("Forward");
                }
            });
            this.image_1.setVisibility(View.GONE);
            this.image_1.setEnabled(false);

            this.image_2 = this.view.findViewById(R.id.stage);
            this.ll1 = this.view.findViewById(R.id.linear22);
            this.ll1.setVisibility(View.GONE);
            this.image_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {

                    if (ViewfileActivity.this.me) {
                        ViewfileActivity.this._Anime("showtime", SketchwareExtendedFab.this.view);
                        ViewfileActivity.this.fab.image_2.setVisibility(View.GONE);
                        ViewfileActivity.this.fab.ll1.setVisibility(View.VISIBLE);
                        SketchwareExtendedFab.this.ll1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View _v) {


                                ViewfileActivity.this._Anime("showstage", SketchwareExtendedFab.this.view);
                                ViewfileActivity.this.fab.image_2.setVisibility(View.VISIBLE);
                                ViewfileActivity.this.fab.ll1.setVisibility(View.GONE);
                            }
                        });
                    } else {

                    }
                }
            });
            this.image_2.setVisibility(View.GONE);
            this.textview1.setVisibility(View.GONE);
            this.lottie1 = this.view.findViewById(R.id.lottie1);


            this.lottie1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {

                    if (SketchwareExtendedFab.this.image_1.isEnabled()) {
                        SketchwareExtendedFab.this.image_1.setEnabled(false);
                        SketchwareExtendedFab.this.lottie1.setAnimation("occlose.json");
                        ViewfileActivity.this._Anime("close", SketchwareExtendedFab.this.view);
                        SketchwareExtendedFab.this.textview1.setVisibility(View.GONE);
                        SketchwareExtendedFab.this.image_1.setVisibility(View.GONE);
                        SketchwareExtendedFab.this.image_2.setVisibility(View.GONE);
                        SketchwareExtendedFab.this.ll1.setVisibility(View.GONE);
                    } else {
                        SketchwareExtendedFab.this.image_1.setEnabled(true);
                        SketchwareExtendedFab.this.lottie1.setAnimation("ocopen.json");
                        ViewfileActivity.this._Anime("open", SketchwareExtendedFab.this.view);
                        SketchwareExtendedFab.this.image_1.setVisibility(View.VISIBLE);
                        SketchwareExtendedFab.this.textview1.setVisibility(View.VISIBLE);
                        if (ViewfileActivity.this.me) {
                            SketchwareExtendedFab.this.image_2.setVisibility(View.VISIBLE);
                            SketchwareExtendedFab.this.ll1.setVisibility(View.GONE);
                        } else {
                            SketchwareExtendedFab.this.image_2.setVisibility(View.GONE);
                            SketchwareExtendedFab.this.ll1.setVisibility(View.VISIBLE);
                        }
                    }
                    SketchwareExtendedFab.this.lottie1.playAnimation();
                }
            });

            this.view.setElevation(10f);
            this.view.setClipToOutline(true);
            this.applyRound(Color.WHITE, ViewfileActivity.this.getDip(28));
            this.applyRippleEffect(Color.parseColor("#EEEEEE"));
            this.view.req();
        }

        private void initAnim() {
            if (this.anim != null) {
                this.anim.cancel();
                this.anim = null;
            }
        }

        private void applyRound(final int a, final float b) {
            final android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setColor(Color.parseColor("#65000000"));
            gd.setCornerRadius(20.0f);
            this.view.setBackground(gd);
        }

        private void applyRippleEffect(final int a) {
            final android.graphics.drawable.GradientDrawable gd = (android.graphics.drawable.GradientDrawable) this.view.getBackground();
            final android.content.res.ColorStateList clrb = new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{a});
            final android.graphics.drawable.RippleDrawable ripdrb = new android.graphics.drawable.RippleDrawable(clrb, gd, null);
            this.view.setClickable(true);
            this.view.setBackground(ripdrb);
        }


        private void setFtext(final String s) {
            ViewfileActivity.this._Anime("oc", this.view);
            this.textview1.setText(s);
        }

        private void setFimage(final int s) {
            if (s == 1) {
                this.image_2.setImageResource(R.drawable.upload_time);
            }
            if (s == 2) {
                this.image_2.setImageResource(R.drawable.singletick);
            }
            if (s == 3) {
                this.image_2.setImageResource(R.drawable.doubletick);
            }
            if (s == 4) {
                this.image_2.setImageResource(R.drawable.bluetick);
            }
        }

        private void setFabColor(final int a) {
            this.applyRound(a, ViewfileActivity.this.getDip(6));
        }

        private void setFabRippleColor(final int a) {
            this.applyRippleEffect(a);
        }

        /*
private void setFabIcon(int a) {
icon.setImageResource(a);
}
*/
        private void setFabGravity(final int a) {
            this.gravity = a;
            if (this.view.getParent() != null && this.view.getParent() instanceof LinearLayout) {
                ((LinearLayout) this.view.getParent()).setGravity(a);
            }
        }

        public void extend() {
            if (!this.extended) {
                this.extended = true;
                this.anim(this.extended);
            }
        }

        public void shrink() {
            if (this.extended) {
                this.extended = false;
                this.anim(this.extended);
            }
        }

        public boolean isExtended() {
            return this.extended;
        }

        private void anim(final boolean a) {
            this.initAnim();
            if (a) {
                this.anim = android.animation.ValueAnimator.ofFloat(ViewfileActivity.this.getDip(56), this.view.w);
            } else {
                this.anim = android.animation.ValueAnimator.ofFloat(this.view.w, ViewfileActivity.this.getDip(56));
            }
            this.anim.setDuration(100);
            this.anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator v) {
                    SketchwareExtendedFab.this.view.setLayoutParams(new LinearLayout.LayoutParams(Math.round((float) v.getAnimatedValue()), SketchwareExtendedFab.this.view.getHeight()));
                    ((LinearLayout.LayoutParams) SketchwareExtendedFab.this.view.getLayoutParams()).setMargins(SketchwareExtendedFab.this.p, SketchwareExtendedFab.this.p, SketchwareExtendedFab.this.p, SketchwareExtendedFab.this.p);
                }
            });
            this.anim.start();
        }


    }


    public void _addFab(View _view) {
        this.fab = new SketchwareExtendedFab(this);
        ViewGroup a = (ViewGroup) _view.getParent();
        if (a != null) {
            a.removeView(_view);
            android.widget.RelativeLayout b = new android.widget.RelativeLayout(this);

            b.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            LinearLayout c = new LinearLayout(this);

            c.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));

            c.setGravity(this.fab.gravity);
            a.addView(b);
            b.addView(_view);
            c.addView(this.fab.view);
            b.addView(c);
        }
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
                        ViewfileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ViewfileActivity.this.asp.edit().putString(_Sp, "").apply();
                            }
                        });
                    }
                };
                this._timer.schedule(this.at, 350);
            } catch (final Exception ignored) {
            }
        } else {

        }
    }


    public void _setFabText(String _st) {
        this.fab.setFtext(_st);
    }


    public void _setFabstage(double _stage) {
        this.fab.setFimage((int) _stage);
    }


    public String _getMsg(double _pow, String _key) {
        if (_key.equals("mainl")) {
            if ("".equals(this.imgurls.getString(this.getIntent().getStringExtra("mid").concat(String.valueOf((long) (_pow))), ""))) {
                try {
                    return (Objects.requireNonNull(this.lmap.get((int) _pow).get(_key)).toString());
                } catch (final Exception e) {
                    return ("");
                }
            } else {
                return (this.imgurls.getString(this.getIntent().getStringExtra("mid").concat(String.valueOf((long) (_pow))), ""));
            }
        } else {
            if (_key.equals("compress")) {
                return (this.imgurls.getString(this.getIntent().getStringExtra("mid").concat(Objects.requireNonNull(this.lmap.get((int) _pow).get("id")).toString()), ""));
            } else {
                if (_key.equals("upload")) {
                    return (this.imgurls.getString(this.getIntent().getStringExtra("mid").concat(Objects.requireNonNull(this.lmap.get((int) _pow).get("id")).toString().concat("u")), ""));
                } else {
                    try {
                        return (Objects.requireNonNull(this.lmap.get((int) _pow).get(_key)).toString());
                    } catch (final Exception e) {
                        return ("");
                    }
                }
            }
        }
    }


    public void _lis() {
    }

    SharedPreferences.OnSharedPreferenceChangeListener listChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
            if (key.equals("us")) {
                if (ViewfileActivity.this.getIntent().getStringExtra("mid").equals(ViewfileActivity.this.list.getString("usMid", ""))) {
                    ViewfileActivity.this.idmap.put(ViewfileActivity.this.list.getString("usid", ""), ViewfileActivity.this.list.getString("usstage", ""));
                    if (ViewfileActivity.this.viewpager1.getCurrentItem() == Double.parseDouble(Objects.requireNonNull(ViewfileActivity.this.idmap.get(ViewfileActivity.this.list.getString("usid", "").concat("pos"))).toString())) {
                        ViewfileActivity.this._setFabstage(Double.parseDouble(ViewfileActivity.this.list.getString("usstage", "")));
                    }
                } else {

                }
            }
            if (key.equals("us1")) {
                if (ViewfileActivity.this.getIntent().getStringExtra("mid").equals(ViewfileActivity.this.list.getString("usMid", ""))) {
                    ViewfileActivity.this.lmap.get((int) Double.parseDouble(Objects.requireNonNull(ViewfileActivity.this.idmap.get(ViewfileActivity.this.list.getString("usid", "").concat("pos"))).toString())).put("mainl", ViewfileActivity.this.list.getString("usurl", ""));
                    if (ViewfileActivity.this.scrollstateval == 0) {
                        ViewfileActivity.this.tnum = ViewfileActivity.this.viewpager1.getCurrentItem();
                        ViewfileActivity.this.viewpager1.setAdapter(new Viewpager1Adapter(ViewfileActivity.this.lmap));
                        ViewfileActivity.this.viewpager1.setCurrentItem((int) ViewfileActivity.this.tnum);
                    } else {
                        ViewfileActivity.this.checkfor0 = true;
                    }
                } else {

                }
            }
            if (key.equals("usR")) {
                if (ViewfileActivity.this.getIntent().getStringExtra("mid").equals(ViewfileActivity.this.list.getString("usMid", ""))) {
                    if (ViewfileActivity.this.scrollstateval == 0) {
                        ViewfileActivity.this.tnum = ViewfileActivity.this.viewpager1.getCurrentItem();
                        ViewfileActivity.this.viewpager1.setAdapter(new Viewpager1Adapter(ViewfileActivity.this.lmap));
                        ViewfileActivity.this.viewpager1.setCurrentItem((int) ViewfileActivity.this.tnum);
                    } else {
                        ViewfileActivity.this.checkfor0 = true;
                    }
                } else {

                }
            }
        }
    };

    {
    }


    public void _sendToDB(HashMap<String, Object> _map, String _ID) {
        this.dbSend.child(_ID).updateChildren(_map);
    }


    public void _sendnsaw(String _msgid, double _pos) {
        this.onreMap = new HashMap<>();
        this.msgc = Calendar.getInstance();
        this.onreMap.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().concat(String.valueOf(this.msgc.getTimeInMillis()).concat(String.valueOf((long) (SketchwareUtil.getRandom(1000000, 9999999))))));
        this.onreMap.put("cmap", this.getIntent().getStringExtra("cmap"));
        this.onreMap.put("gid", this.getIntent().getStringExtra("mid"));
        this.onreMap.put("ngid", _msgid);
        this.onreMap.put("pos", String.valueOf((long) (_pos)));
        this.onreMap.put("by", FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.onreMap.put("ms", String.valueOf(this.msgc.getTimeInMillis()));
        this.onreMap.put("typ", "nsaw");
        this._sendToDB(this.onreMap, Objects.requireNonNull(this.onreMap.get("id")).toString());
        this.onreMap.clear();
    }


    public void _UpdateAsViewed(double _pos) {
        if (this._getMsg(_pos, "typ").equals("2")) {
            if (this.viewpager1.getCurrentItem() == _pos) {
                if ("".equals(this.list.getString(this._getMsg(_pos, "id").concat("saw"), ""))) {
                    this.list.edit().putString(this._getMsg(_pos, "id").concat("saw"), "true").apply();
                    this._sendnsaw(this._getMsg(_pos, "id"), _pos);
                } else {

                }
            } else {

            }
        } else {
            if ("".equals(this.list.getString(this._getMsg(_pos, "id").concat("saw"), ""))) {
                this.list.edit().putString(this._getMsg(_pos, "id").concat("saw"), "true").apply();
                this._sendnsaw(this._getMsg(_pos, "id"), _pos);
            } else {

            }
        }
    }


    public void _MakeDir() {
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/received"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/received"));
        }
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Image/sent"));
        }
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/received"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/received"));
        }
        if (FileUtil.isDirectory(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/sent"))) {

        } else {
            FileUtil.makeDir(FileUtil.getPackageDataDir(this.getApplicationContext()).concat("/Video/sent"));
        }
    }


    public void _decodeImgFromText(ImageView _img, String _code) {
        try {
            final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();

            //decode base64 string to image
            imageBytes = android.util.Base64.decode(_code, android.util.Base64.DEFAULT);
            final Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            _img.setImageBitmap(decodedImage);
        } catch (final Exception e) {
            SketchwareUtil.showMessage(this.getApplicationContext(), "can't load image");
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
            this._context = ViewfileActivity.this.getApplicationContext();
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
            final View _view = LayoutInflater.from(this._context).inflate(R.layout.tabcus, _container, false);

            LinearLayout linear1 = _view.findViewById(R.id.linear1);
            LinearLayout linear8 = _view.findViewById(R.id.linear8);
            RelativeLayout linear7 = _view.findViewById(R.id.linear7);
            LinearLayout linear9 = _view.findViewById(R.id.linear9);
            LinearLayout linearIMG = _view.findViewById(R.id.linearIMG);
            LinearLayout linear5 = _view.findViewById(R.id.linear5);
            VideoView videoview1 = _view.findViewById(R.id.videoview1);
            TextView textview1 = _view.findViewById(R.id.textview1);
            ProgressBar progressbar1 = _view.findViewById(R.id.progressbar1);
            ImageView imageview2 = _view.findViewById(R.id.imageview2);
            textview1.setTypeface(Typeface.createFromAsset(ViewfileActivity.this.getApplicationContext().getAssets(), "fonts/product_more_block.ttf"), Typeface.NORMAL);
            ViewfileActivity.this._SetBackground(textview1, 40, 5, ViewfileActivity.this.jay.col(0,2), false);
            ViewfileActivity.this.zoom1 = new ZoomableImageView(ViewfileActivity.this);
            ViewfileActivity.this.zoom1.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewfileActivity.this.zoom1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearIMG.addView(ViewfileActivity.this.zoom1);
            ViewfileActivity.this.zoom1.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if ("4".equals(ViewfileActivity.this._getMsg(_position, "typ"))) {
                ViewfileActivity.this.tpath = ".mp4";
                ViewfileActivity.this.file = "Video";
            } else {
                ViewfileActivity.this.tpath = ".jpg";
                ViewfileActivity.this.file = "Image";
            }
            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(ViewfileActivity.this._getMsg(_position, "by"))) {
                if ("2".equals(ViewfileActivity.this._getMsg(_position, "typ"))) {
                    if ("".equals(ViewfileActivity.this._getMsg(_position, "mainl"))) {
                        ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, ViewfileActivity.this._getMsg(_position, "tmpl"));
                        textview1.setVisibility(View.VISIBLE);
                        textview1.setText("Uploading...");
                    } else {
                        textview1.setVisibility(View.GONE);
                        textview1.setText("");
                        if (FileUtil.isExistFile(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat(ViewfileActivity.this.file).concat("/sent/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(ViewfileActivity.this.tpath))))) {
                            ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat(ViewfileActivity.this.file).concat("/sent/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(ViewfileActivity.this.tpath))));
                        } else {
                            ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, ViewfileActivity.this._getMsg(_position, "mainl"));
                        }
                    }
                } else {
                    if ("true".equals(ViewfileActivity.this._getMsg(_position, "compress"))) {
                        ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, ViewfileActivity.this._getMsg(_position, "tmpl"));
                        textview1.setVisibility(View.VISIBLE);
                        textview1.setText("Compressing...");
                    } else {
                        if ("".equals(ViewfileActivity.this._getMsg(_position, "mainl"))) {
                            ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat(ViewfileActivity.this.file).concat("/sent/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(ViewfileActivity.this.tpath))));
                            textview1.setVisibility(View.GONE);
                            textview1.setText("");
                        } else {
                            textview1.setVisibility(View.GONE);
                            textview1.setText("");
                            if (FileUtil.isExistFile(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat(ViewfileActivity.this.file).concat("/sent/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(ViewfileActivity.this.tpath))))) {
                                ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat(ViewfileActivity.this.file).concat("/sent/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(ViewfileActivity.this.tpath))));
                            } else {
                                ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, ViewfileActivity.this._getMsg(_position, "mainl"));
                            }
                        }
                    }
                }
            } else {
                if ("".equals(ViewfileActivity.this._getMsg(_position, "mainl"))) {
                    ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat(ViewfileActivity.this.file).concat("/received/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(ViewfileActivity.this.tpath))));
                    textview1.setVisibility(View.VISIBLE);
                    textview1.setText("Uploading...");
                } else {
                    textview1.setVisibility(View.GONE);
                    textview1.setText("");
                    if (FileUtil.isExistFile(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat("Image").concat("/received/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(".jpg"))))) {
                        ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/".concat("Image").concat("/received/").concat(ViewfileActivity.this._getMsg(_position, "id").concat(".jpg"))));
                    } else {
                        ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, ViewfileActivity.this._getMsg(_position, "mainl"));
                        if ("2".equals(ViewfileActivity.this._getMsg(_position, "typ"))) {
                            ViewfileActivity.this._MakeDir();
                            final OnSuccessListener<FileDownloadTask.TaskSnapshot> success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(final FileDownloadTask.TaskSnapshot _param1) {
                                    long _totalbytes = _param1.getTotalByteCount();

                                }
                            };
                            final OnProgressListener<FileDownloadTask.TaskSnapshot> progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(final FileDownloadTask.TaskSnapshot _param1) {
                                    final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                                }
                            };
                            final OnFailureListener failure_listener = new OnFailureListener() {
                                @Override
                                public void onFailure(final Exception _param1) {
                                    String _msg = _param1.getMessage();

                                }
                            };
                            ViewfileActivity.this._firebase_storage.getReferenceFromUrl(Uri.parse(ViewfileActivity.this._getMsg(_position, "mainl")).toString()).getFile(new File(Uri.parse(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Image/received/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".jpg")))).toString())).addOnSuccessListener(success_listener).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener);
                        } else {

                        }
                    }
                }
            }
            ViewfileActivity.this._SetGradientView(ViewfileActivity.this.zoom1, ViewfileActivity.this.jay.col(0,3), ViewfileActivity.this.jay.col(0,4));
            progressbar1.setVisibility(View.GONE);
            if ("4".equals(ViewfileActivity.this._getMsg(_position, "typ"))) {
                ViewfileActivity.this.ont = true;
                if (!"true".equals(ViewfileActivity.this._getMsg(_position, "compress"))) {
                    imageview2.setVisibility(View.VISIBLE);
                    linear5.setVisibility(View.VISIBLE);
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ViewfileActivity.this._getMsg(_position, "by"))) {
                        if ("true".equals(ViewfileActivity.this._getMsg(_position, "upload"))) {
                            textview1.setText("");
                            textview1.setVisibility(View.GONE);
                        } else {
                            textview1.setText("");
                            textview1.setVisibility(View.GONE);
                        }
                        if (FileUtil.isExistFile(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/sent/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4"))))) {
                            videoview1.setVideoURI(Uri.parse(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/sent/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4")))));
                            imageview2.setImageResource(R.drawable.ic_play_arrow_white);
                            ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/sent/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4"))));
                        } else {
                            if ("".equals(ViewfileActivity.this._getMsg(_position, "mainl"))) {
                                ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, ViewfileActivity.this._getMsg(_position, "tmpl"));
                                textview1.setText("Waiting...");
                                textview1.setVisibility(View.VISIBLE);
                                imageview2.setVisibility(View.VISIBLE);
                                ViewfileActivity.this.ont = false;
                                if (FileUtil.isExistFile(ViewfileActivity.this._getMsg(_position, "tmpl"))) {
                                    videoview1.setVideoURI(Uri.parse(ViewfileActivity.this._getMsg(_position, "tmpl")));
                                    imageview2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View _view) {
                                            if (FileUtil.isExistFile(ViewfileActivity.this._getMsg(_position, "tmpl"))) {
                                                videoview1.setVisibility(View.VISIBLE);
                                                linearIMG.setVisibility(View.GONE);
                                                if (videoview1.isPlaying()) {
                                                    videoview1.pause();
                                                    ViewfileActivity.this.playing = false;
                                                    imageview2.setImageResource(R.drawable.ic_play_arrow_white);
                                                } else {
                                                    videoview1.start();
                                                    ViewfileActivity.this.playing = true;
                                                    imageview2.setImageResource(R.drawable.ic_pause_white);
                                                    imageview2.setEnabled(false);
                                                    imageview2.setVisibility(View.GONE);
                                                }
                                            } else {

                                            }
                                        }
                                    });
                                    linear5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View _view) {
                                            if (imageview2.isEnabled()) {
                                                imageview2.setEnabled(false);
                                                imageview2.setVisibility(View.GONE);
                                            } else {
                                                imageview2.setEnabled(true);
                                                imageview2.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                } else {

                                }
                            } else {
                                imageview2.setImageResource(R.drawable.ic_download_white);
                                textview1.setText("File Not Found");
                            }
                        }
                    } else {
                        if (FileUtil.isExistFile(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/received/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4"))))) {
                            videoview1.setVideoURI(Uri.parse(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/received/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4")))));
                            imageview2.setImageResource(R.drawable.ic_play_arrow_white);
                        } else {
                            if ("".equals(ViewfileActivity.this._getMsg(_position, "mainl"))) {
                                imageview2.setVisibility(View.GONE);
                                textview1.setText("Waiting...");
                                ViewfileActivity.this.ont = false;
                                linear5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View _view) {

                                    }
                                });
                            } else {
                                imageview2.setImageResource(R.drawable.ic_download_white);
                            }
                        }
                    }
                    if (ViewfileActivity.this.ont) {
                        videoview1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(final MediaPlayer _mediaPlayer, final int _what, final int _extra) {
                                SketchwareUtil.showMessage(ViewfileActivity.this.getApplicationContext(), "Failed to Play");
                                return true;
                            }
                        });

                        videoview1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(final MediaPlayer _mediaPlayer) {
                                videoview1.start();
                            }
                        });
                        imageview2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View _view) {
                                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ViewfileActivity.this._getMsg(_position, "by"))) {
                                    if (FileUtil.isExistFile(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/sent/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4"))))) {
                                        videoview1.setVisibility(View.VISIBLE);
                                        linearIMG.setVisibility(View.GONE);
                                        if (videoview1.isPlaying()) {
                                            videoview1.pause();
                                            ViewfileActivity.this.playing = false;
                                            imageview2.setImageResource(R.drawable.ic_play_arrow_white);
                                        } else {
                                            videoview1.start();
                                            ViewfileActivity.this.playing = true;
                                            imageview2.setImageResource(R.drawable.ic_pause_white);
                                            imageview2.setEnabled(false);
                                            imageview2.setVisibility(View.GONE);
                                        }
                                    } else {
                                        if ("".equals(ViewfileActivity.this._getMsg(_position, "mainl"))) {
                                            textview1.setVisibility(View.VISIBLE);
                                            imageview2.setVisibility(View.GONE);
                                        } else {
                                            ViewfileActivity.this._MakeDir();
                                            final OnSuccessListener<FileDownloadTask.TaskSnapshot> success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(final FileDownloadTask.TaskSnapshot _param1) {
                                                    long _totalbytes = _param1.getTotalByteCount();
                                                    videoview1.setVideoURI(Uri.parse(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/sent/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4")))));
                                                    videoview1.start();
                                                    linear5.setEnabled(true);
                                                    linearIMG.setVisibility(View.GONE);
                                                    videoview1.setVisibility(View.VISIBLE);
                                                    progressbar1.setVisibility(View.GONE);
                                                    textview1.setVisibility(View.GONE);
                                                    ViewfileActivity.this.playing = true;
                                                }
                                            };
                                            final OnProgressListener<FileDownloadTask.TaskSnapshot> progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(final FileDownloadTask.TaskSnapshot _param1) {
                                                    final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                                                }
                                            };
                                            final OnFailureListener failure_listener = new OnFailureListener() {
                                                @Override
                                                public void onFailure(final Exception _param1) {
                                                    String _msg = _param1.getMessage();
                                                    SketchwareUtil.showMessage(ViewfileActivity.this.getApplicationContext(), _msg);
                                                }
                                            };
                                            ViewfileActivity.this._firebase_storage.getReferenceFromUrl(Uri.parse(ViewfileActivity.this._getMsg(_position, "mainl")).toString()).getFile(new File(Uri.parse(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/sent/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4")))).toString())).addOnSuccessListener(success_listener).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener);
                                            imageview2.setImageResource(R.drawable.ic_pause_white);
                                            imageview2.setVisibility(View.GONE);
                                            imageview2.setEnabled(false);
                                            linear5.setEnabled(false);
                                            progressbar1.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    if (FileUtil.isExistFile(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/received/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4"))))) {
                                        videoview1.setVisibility(View.VISIBLE);
                                        linearIMG.setVisibility(View.GONE);
                                        if (videoview1.isPlaying()) {
                                            videoview1.pause();
                                            ViewfileActivity.this.playing = false;
                                            imageview2.setImageResource(R.drawable.ic_play_arrow_white);
                                        } else {
                                            videoview1.start();
                                            ViewfileActivity.this.playing = true;
                                            imageview2.setImageResource(R.drawable.ic_pause_white);
                                            imageview2.setEnabled(false);
                                            imageview2.setVisibility(View.GONE);
                                        }
                                    } else {
                                        imageview2.setImageResource(R.drawable.ic_pause_white);
                                        imageview2.setVisibility(View.GONE);
                                        imageview2.setEnabled(false);
                                        linear5.setEnabled(false);
                                        progressbar1.setVisibility(View.VISIBLE);
                                        ViewfileActivity.this._MakeDir();
                                        final OnSuccessListener<FileDownloadTask.TaskSnapshot> success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(final FileDownloadTask.TaskSnapshot _param1) {
                                                long _totalbytes = _param1.getTotalByteCount();
                                                ViewfileActivity.this._UpdateAsViewed(_position);
                                                videoview1.setVideoURI(Uri.parse(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/received/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4")))));
                                                videoview1.start();
                                                linear5.setEnabled(true);
                                                linearIMG.setVisibility(View.GONE);
                                                videoview1.setVisibility(View.VISIBLE);
                                                progressbar1.setVisibility(View.GONE);
                                                textview1.setVisibility(View.GONE);
                                                ViewfileActivity.this.playing = true;
                                            }
                                        };
                                        final OnProgressListener<FileDownloadTask.TaskSnapshot> progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(final FileDownloadTask.TaskSnapshot _param1) {
                                                final double _prog = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
                                            }
                                        };
                                        final OnFailureListener failure_listener = new OnFailureListener() {
                                            @Override
                                            public void onFailure(final Exception _param1) {
                                                String _msg = _param1.getMessage();
                                                SketchwareUtil.showMessage(ViewfileActivity.this.getApplicationContext(), _msg);
                                            }
                                        };
                                        ViewfileActivity.this._firebase_storage.getReferenceFromUrl(Uri.parse(ViewfileActivity.this._getMsg(_position, "mainl")).toString()).getFile(new File(Uri.parse(FileUtil.getPackageDataDir(ViewfileActivity.this.getApplicationContext()).concat("/Video/received/".concat(ViewfileActivity.this._getMsg(_position, "id").concat(".mp4")))).toString())).addOnSuccessListener(success_listener).addOnFailureListener(failure_listener).addOnProgressListener(progress_listener);
                                    }
                                }
                            }
                        });
                        linear5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View _view) {
                                if (imageview2.isEnabled()) {
                                    imageview2.setEnabled(false);
                                    imageview2.setVisibility(View.GONE);
                                } else {
                                    imageview2.setEnabled(true);
                                    imageview2.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                } else {
                    ViewfileActivity.this._Glide(ViewfileActivity.this.zoom1, ViewfileActivity.this._getMsg(_position, "tmpl"));
                    if (FileUtil.isExistFile(ViewfileActivity.this._getMsg(_position, "tmpl"))) {
                        videoview1.setVideoURI(Uri.parse(ViewfileActivity.this._getMsg(_position, "tmpl")));
                        imageview2.setVisibility(View.VISIBLE);
                        imageview2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View _view) {
                                if (FileUtil.isExistFile(ViewfileActivity.this._getMsg(_position, "tmpl"))) {
                                    videoview1.setVisibility(View.VISIBLE);
                                    linearIMG.setVisibility(View.GONE);
                                    if (videoview1.isPlaying()) {
                                        videoview1.pause();
                                        ViewfileActivity.this.playing = false;
                                        imageview2.setImageResource(R.drawable.ic_play_arrow_white);
                                    } else {
                                        videoview1.start();
                                        ViewfileActivity.this.playing = true;
                                        imageview2.setImageResource(R.drawable.ic_pause_white);
                                        imageview2.setEnabled(false);
                                        imageview2.setVisibility(View.GONE);
                                    }
                                } else {

                                }
                            }
                        });
                        linear5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View _view) {
                                if (imageview2.isEnabled()) {
                                    imageview2.setEnabled(false);
                                    imageview2.setVisibility(View.GONE);
                                } else {
                                    imageview2.setEnabled(true);
                                    imageview2.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        imageview2.setVisibility(View.GONE);
                        linear5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View _view) {

                            }
                        });
                    }
                }
            } else {
                linear5.setVisibility(View.GONE);
                imageview2.setVisibility(View.GONE);
                linearIMG.setVisibility(View.VISIBLE);
            }

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
