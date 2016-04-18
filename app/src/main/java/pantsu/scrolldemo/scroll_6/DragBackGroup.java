package pantsu.scrolldemo.scroll_6;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Pantsu on 2016/3/23.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class DragBackGroup extends LinearLayout {
    private ViewDragHelper mDragHelper;
    private OnFinishListener fListener;

    private View scrollView = null;

    private int minX, maxX, minY, maxY;
    private int curX;
    float conditionRate = 0.33f;

    public DragBackGroup(final Context context, AttributeSet attrs) {
        super(context, attrs);


        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                findScrollView();
                return child == scrollView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                curX = left;
                return Math.max(minX, Math.min(maxX, left));
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return 0;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.d("d", "" + left);
                if (Math.abs(maxX - left) < 8) {
                    fListener.finish();
                } else if (startScroll && Math.abs(minX - left) < 4) {
                    startScroll = false;
                    isScrolling = false;
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                if (xvel > 1200) {
                    mDragHelper.smoothSlideViewTo(scrollView, maxX, 0);
                    postInvalidate();
                } else if (xvel < -1200) {
                    mDragHelper.smoothSlideViewTo(scrollView, minX, 0);
                    postInvalidate();
                } else if (curX < minX + conditionRate * (maxX - minX)) {
                    mDragHelper.smoothSlideViewTo(scrollView, minX, 0);
                    postInvalidate();
                } else {
                    mDragHelper.smoothSlideViewTo(scrollView, maxX, 0);
                    postInvalidate();
                }
                startScroll = true;
                isScrolling = true;
            }

        });
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void findScrollView() {
        if (scrollView == null && getChildCount() > 0) {
            View topView = getChildAt(0);
            if (topView instanceof ListView || topView instanceof ScrollView) {
                scrollView = topView;
            }
        }
    }


    public void setRange(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    private float xDelta, yDelta, xDown, yDown;
    private boolean decideIntercept, intercepted;
    private int pointCount = 0;
    private boolean startScroll = false, isScrolling = false;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDelta = yDelta = 0f;
                xDown = event.getX();
                yDown = event.getY();
                mDragHelper.processTouchEvent(event);

                if (isScrolling) {
                    startScroll = false;
                    isScrolling = false;

                    decideIntercept = true;
                    intercepted = true;
                } else {
                    decideIntercept = false;
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!decideIntercept) {
                    float curX = event.getX();
                    float curY = event.getY();
                    xDelta = curX - xDown;
                    yDelta = curY - yDown;

                    if (pointCount++ >= 4) {
                        if (xDelta < 0) {
                            intercepted = false;
                        } else if (xDelta > 1.2f * Math.abs(yDelta)) {
                            intercepted = true;
                        } else if (scrollView == null || !(scrollView.canScrollVertically(-1) || scrollView.canScrollVertically(1))) {
                            intercepted = true;
                        }

                        decideIntercept = true;
                    }
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

    public void setOnFinishListener(OnFinishListener listener) {
        fListener = listener;
    }

    public void scrollToMax() {
        findScrollView();
        mDragHelper.smoothSlideViewTo(scrollView, maxX, 0);
        postInvalidate();
    }

    interface OnFinishListener {
        void finish();
    }

}
