package pantsu.scrolldemo.scroll_2;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Created by Pantsu on 2016/3/23.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class DragViewGroup extends LinearLayout {
    private ViewDragHelper mDragHelper;
    private OnClampListener mListener;

    int maxX, minX, maxY, minY;

    private static final Interpolator sInterpolator = new DecelerateInterpolator(1.5f);
    private ScrollerCompat scrollerCompat;

    private float adjustRate = 0;

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                maxX = 0;
                minX = getWidth() - child.getWidth();
                maxY = 0;
                minY = getHeight() - child.getHeight();
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int newLeft = Math.max(Math.min(left, maxX), minX);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int newTop = Math.max(Math.min(top, maxY), minY);
                return newTop;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (mListener != null) {
                    mListener.onClampHorizontal(left);
                    mListener.onClampVertical(top);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                adjustDuration();
                mDragHelper.flingCapturedView(minX, minY, maxX, maxY);
                postInvalidate();
            }
        });

        setInterpolator(context, mDragHelper, sInterpolator);
    }

    private void adjustDuration() {
        try {
            if (adjustRate == 0) {
                adjustRate = (float) (Math.log(0.78) / Math.log(0.9)) * 6;
            }

            int apiVersion = Build.VERSION.SDK_INT;
            if (apiVersion >= 9) { // ICS >=14 or Gingerbread >=9
                Field rate = Class.forName("android.widget.OverScroller$SplineOverScroller")
                        .getDeclaredField("DECELERATION_RATE");
                rate.setAccessible(true);
                rate.set(null, adjustRate);
            } else { // Base
                Field rate = Class.forName("android.widget.Scroller")
                        .getDeclaredField("DECELERATION_RATE");
                rate.setAccessible(true);
                rate.set(null, adjustRate);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setInterpolator(Context context, ViewDragHelper helper, Interpolator sInterpolator) {
        scrollerCompat = ScrollerCompat.create(context, sInterpolator);
        try {
            Field mScroller = ViewDragHelper.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(helper, scrollerCompat);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    public void setBound(int maxX, int minX, int maxY, int minY) {
        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
    }

    /*
        Calling updateBound() is necessary if this container's LayoutParams has been changed !
        but setLayoutParams() does not impact getHeight() immediately,
        so call setBound(maxX, minX, maxY, minY) directly ~
    */
    public void updateBound() {
        View child = mDragHelper.findTopChildUnder(0, 0);
        if (child != null) {
            maxX = 0;
            minX = getWidth() - child.getWidth();
            maxY = 0;
            minY = getHeight() - child.getHeight();
            int w1 = getHeight();
            int w2 = child.getHeight();
            Log.d("w1", w1 + "");
            Log.d("w2", w2 + "");
        }
    }

    public void dragChildTo(int left, int top) {
        int newLeft = Math.max(Math.min(left, maxX), minX);
        int newTop = Math.max(Math.min(top, maxY), minY);

        LayoutParams params = (LayoutParams) mDragHelper.findTopChildUnder(0, 0).getLayoutParams();
        params.leftMargin = newLeft;
        params.topMargin = newTop;
        mDragHelper.findTopChildUnder(0, 0).setLayoutParams(params);
        mDragHelper.findTopChildUnder(0, 0).invalidate();

        // another method to try dragChildTo
//        View mCapturedView = mDragHelper.findTopChildUnder(0, 0);
//        int clampedX = newLeft;
//        int clampedY = newTop;
//        final int oldLeft = mCapturedView.getLeft();
//        final int oldTop = mCapturedView.getTop();
//
//        ViewCompat.offsetLeftAndRight(mCapturedView, clampedX - oldLeft);
//        ViewCompat.offsetTopAndBottom(mCapturedView, clampedY - oldTop);
//        mCapturedView.invalidate();
    }

    public void setOnClampListener(OnClampListener listener) {
        mListener = listener;
    }

    interface OnClampListener {
        void onClampHorizontal(int left);

        void onClampVertical(int top);
    }

}
