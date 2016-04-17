package pantsu.scrolldemo.scroll_6;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
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

    boolean releaseMark;

    public DragBackGroup(final Context context, AttributeSet attrs) {
        super(context, attrs);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (getChildCount() > 0) {
                    View topView = getChildAt(0);
                    if (topView instanceof ListView || topView instanceof ScrollView) {
                        scrollView = topView;
                    }
                }
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
                if (releaseMark && left == maxX) {
                    fListener.finish();
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

                releaseMark = true;
            }

        });
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDelta = yDelta = 0f;
                xDown = event.getX();
                yDown = event.getY();
                mDragHelper.processTouchEvent(event);

                releaseMark = false;

                decideIntercept = false;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!decideIntercept) {
                    float curX = event.getX();
                    float curY = event.getY();
                    xDelta = curX - xDown;
                    yDelta = curY - yDown;

                    if (Math.abs(xDelta) > Math.abs(yDelta)) {
                        intercepted = true;
                    } else if (scrollView == null || !(scrollView.canScrollVertically(-1) || scrollView.canScrollVertically(1))) {
                        intercepted = true;
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

    public void setOnFinishListener(OnFinishListener listener) {
        fListener = listener;
    }


    interface OnFinishListener {
        void finish();
    }

}
