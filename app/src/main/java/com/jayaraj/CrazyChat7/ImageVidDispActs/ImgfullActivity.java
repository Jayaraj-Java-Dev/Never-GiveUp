package com.jayaraj.CrazyChat7.ImageVidDispActs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;

import java.util.*;

import android.widget.LinearLayout;
import android.widget.ImageView;
import android.app.Activity;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.jayaraj.CrazyChat7.J.Jay;
import com.jayaraj.CrazyChat7.R;
import com.jayaraj.CrazyChat7.J.SketchwareUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.DialogFragment;

public class ImgfullActivity extends AppCompatActivity {

    private LinearLayout linear1;
    private LinearLayout linearIMG;
    private ImageView imageview1;

    private SharedPreferences ChatList, pdataSP;
    Jay jay;

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        this.setContentView(R.layout.imgfull);
        this.initialize(_savedInstanceState);
        com.google.firebase.FirebaseApp.initializeApp(this);
        this.initializeLogic();
    }

    private void initialize(final Bundle _savedInstanceState) {
        this.linear1 = this.findViewById(R.id.linear1);
        this.linearIMG = this.findViewById(R.id.linearIMG);
        this.imageview1 = this.findViewById(R.id.imageview1);
        this.ChatList = this.getSharedPreferences("ChatUserList", Context.MODE_PRIVATE);
        this.pdataSP = this.getSharedPreferences("pdata", Context.MODE_PRIVATE);
        this.jay = new Jay(this.getSharedPreferences("MODE", Context.MODE_PRIVATE));
    }

    private void initializeLogic() {
        this.zoom1 = new ZoomableImageView(this);
        this.zoom1.setScaleType(ImageView.ScaleType.FIT_XY);
        this.zoom1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        this.linearIMG.addView(this.zoom1);

        this.zoom1.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if ("".equals(this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("i"), ""))) {
            if (this.getIntent().getStringExtra("uid").equals(FirebaseAuth.getInstance().getUid())) {
                String img = this.pdataSP.getString("IMG", "");
                if (img.equals("")) {

                    SketchwareUtil.showMessage(this.getApplicationContext(), "No User Image");
                    this.finish();

                } else {
                    this._Glide(this.zoom1, img);
                    this.zoom1.setTransitionName("img");
                }
            } else {
                if (this.getIntent().hasExtra("url")) {
                    this._Glide(this.zoom1, this.getIntent().getStringExtra("url"));
                    this.zoom1.setTransitionName("img");
                } else {
                    SketchwareUtil.showMessage(this.getApplicationContext(), "User Image Doesn't Found");
                    this.finish();
                }
            }
        } else {
            this._Glide(this.zoom1, this.ChatList.getString(this.getIntent().getStringExtra("uid").concat("i"), ""));
            this.zoom1.setTransitionName("img");
        }
        this.imageview1.setVisibility(View.GONE);
        this._Bg();
    }


    public void _Bg() {
        this._SetGradientView(this.linearIMG, this.jay.col(0, 3), this.jay.col(0, 4));
        final Window w = this.getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(this.jay.col(0, 3)));

    }

    public void _SetGradientView(@NonNull View _view, String _color_a, String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) {
            public GradientDrawable getIns(final int a) {
                setCornerRadius(a);
                return this;
            }
        }.getIns((int) 0));
    }

    @Override
    protected void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {

            default:
                break;
        }
    }

    public void _Glide(ImageView _img, String _url) {
        Glide.with(this.getContext(this)).load(_url).thumbnail(Glide.with(this.getContext(this)).load(this.getContext(this).getResources().getIdentifier("loading", "drawable", this.getContext(this).getPackageName()))).fitCenter().into(_img);
    }

    public Context getContext(final Context c) {
        return c;
    }

    public Context getContext(@NonNull final Fragment f) {
        return f.getActivity();
    }

    public Context getContext(@NonNull final DialogFragment df) {
        return df.getActivity();
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
