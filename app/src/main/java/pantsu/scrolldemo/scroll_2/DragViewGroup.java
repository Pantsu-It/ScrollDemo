package pantsu.scrolldemo.scroll_2;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
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

    int leftBound, rightBound, topBound, bottomBound;

    private static final Interpolator sInterpolator = new DecelerateInterpolator(1.5f);
    private ScrollerCompat scrollerCompat;

    private float adjustRate = 0;

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                leftBound = 0;
                rightBound = getWidth() - child.getWidth();
                topBound = 0;
                bottomBound = getHeight() - child.getHeight();
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int newLeft = Math.max(Math.min(left, leftBound), rightBound);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int newTop = Math.max(Math.min(top, topBound), bottomBound);
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
                mDragHelper.flingCapturedView(rightBound, bottomBound, leftBound, topBound);
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

    public void setOnClampListener(OnClampListener listener) {
        mListener = listener;
    }

    public void scrollChildTo(int scrollX, int scrollY) {
        LayoutParams params = (LayoutParams) mDragHelper.findTopChildUnder(0, 0).getLayoutParams();
        params.topMargin = -scrollY;
        params.leftMargin = -scrollX;
        mDragHelper.findTopChildUnder(0, 0).setLayoutParams(params);
    }

    interface OnClampListener {
        void onClampHorizontal(int left);

        void onClampVertical(int top);
    }

}
