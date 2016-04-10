package pantsu.scrolldemo.scroll_2;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.lang.reflect.Field;

import pantsu.scrolldemo.R;

/**
 * Created by Pantsu on 2016/3/23.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class DragViewGroup_Slide extends LinearLayout {
    private ViewDragHelper mDragHelper;
    private OnDragListener mListener;

    int maxX, minX, maxY, minY;

    private static final Interpolator sInterpolator = new DecelerateInterpolator(1.5f);
    private ScrollerCompat scrollerCompat;

    public boolean condition = false;

    public DragViewGroup_Slide(Context context, AttributeSet attrs) {
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
                // border_bug - if allowing top = maxY...
                int newTop = Math.max(Math.min(top, maxY - 1), minY);
                return newTop;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (mListener != null) {
                    mListener.onViewPositionChanged(left, top, dx, dy);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                float y = releasedChild.getY();
                if (y == maxY) {
                    condition = true;
                } else if (y == minY) {
                    condition = false;
                } else if (yvel > 1000) {
                    slideOut();
                    condition = true;
                } else if (yvel < -1000) {
                    slideIn();
                    condition = false;
                } else if (y > (maxY + minY) / 2) {
                    slideOut();
                    condition = true;
                } else {
                    slideIn();
                    condition = false;
                }

                if (mListener != null) {
                    mListener.onViewReleased(releasedChild, xvel, yvel);
                }
            }
        });

        setInterpolator(context, mDragHelper, sInterpolator);
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

    public void setBound(int maxX, int minX, int maxY, int minY) {
        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
    }

    /*
        java.lang.IllegalStateException: Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased
        mDragHelper.settleCapturedViewAt(maxX, maxY);
    */
    public void slideIn() {
        mDragHelper.smoothSlideViewTo(mDragHelper.findTopChildUnder(0, 0), maxX, minY);
        postInvalidate();
    }

    public void slideOut() {
        mDragHelper.smoothSlideViewTo(mDragHelper.findTopChildUnder(0, 0), maxX, maxY);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private float xDelta, yDelta, xLast, yLast;
    private boolean decideIntercept;
    private int mode;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDragHelper.processTouchEvent(event);
                xDelta = yDelta = 0f;
                xLast = event.getX();
                yLast = event.getY();

                decideIntercept = false;
                mode = -1;

                if (isSliding()) {
                    intercepted = true;
                    break;
                }

                if (inRangeOfView(findViewById(R.id.container_top), event))
                    mode = 0;
                else if (inRangeOfView(findViewById(R.id.container_center), event))
                    mode = 1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode != -1 && !decideIntercept) {
                    final float curX = event.getX();
                    final float curY = event.getY();

                    xDelta = curX - xLast;
                    yDelta = curY - yLast;

                    if (yDelta < 0 && Math.abs(yDelta) > Math.abs(xDelta)) {
                        if (mode == 0)
                            intercepted = true;
                        else if (mode == 1) {
                            if (isScrollAtBottom((ScrollView) findViewById(R.id.scroll_left)))
                                intercepted = true;
                        }
                    }
                    decideIntercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }


    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    public static boolean isScrollAtTop(View scrollView) {
        int scrollY = scrollView.getScrollY();
        return scrollY == 0;
    }

    public static boolean isScrollAtBottom(ScrollView scrollView) {
        int scrollY = scrollView.getScrollY();
        return scrollY == scrollView.getChildAt(0).getHeight() - scrollView.getHeight();
    }

    public boolean isSliding() {
        return !scrollerCompat.isFinished();
    }

    public void setOnDragListener(OnDragListener listener) {
        mListener = listener;
    }

    interface OnDragListener {
        void onViewPositionChanged(int left, int top, int dx, int dy);

        void onViewReleased(View releasedChild, float xvel, float yvel);
    }

}
