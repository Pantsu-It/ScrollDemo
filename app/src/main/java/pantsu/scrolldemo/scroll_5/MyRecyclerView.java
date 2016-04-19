package pantsu.scrolldemo.scroll_5;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import pantsu.scrolldemo.MainActivity;

/**
 * Created by Pants on 2016/4/18.
 */
public class MyRecyclerView extends RecyclerView {
    private ViewDragHelper mDragHelper;
    private OnSlideOutListener sListener;

    float conditionRate = 0.5f;
    private int minX, maxX, minY, maxY;
    private int curX;

    private View lastCapturedView;

    public MyRecyclerView(Context context) {
        super(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (lastCapturedView == child){
                    int y = (int) child.getY();
                    setRange(-MainActivity.displayMetrics.widthPixels, 0, y, y);
                    return true;
                }
                else if (child.getTag() != null && ((String) child.getTag()).equals("item")) {
                    int y = (int) child.getY();
                    setRange(-MainActivity.displayMetrics.widthPixels, 0, y, y);
                    lastCapturedView = child;
                    return true;
                }

                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                curX = left;
                return Math.max(minX, Math.min(maxX, left));
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return Math.max(minY, Math.min(maxY, top));
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (Math.abs(minX - left) < 4) {
                    sListener.OnSlideOut(changedView);

                    lastCapturedView = null;
                } else if (startScroll && Math.abs(maxX - left) < 4) {
                    startScroll = false;
                    isScrolling = false;

                    lastCapturedView = null;
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                if (xvel > 1200) {
                    mDragHelper.smoothSlideViewTo(releasedChild, maxX, maxY);
                    postInvalidate();
                } else if (xvel < -1200) {
                    mDragHelper.smoothSlideViewTo(releasedChild, minX, maxY);
                    postInvalidate();
                } else if (curX < minX + conditionRate * (maxX - minX)) {
                    mDragHelper.smoothSlideViewTo(releasedChild, minX, maxY);
                    postInvalidate();
                } else {
                    mDragHelper.smoothSlideViewTo(releasedChild, maxX, maxY);
                    postInvalidate();
                }
                Log.d("maxY", String.valueOf(maxY));

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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDelta = yDelta = 0f;
                xDown = event.getX();
                yDown = event.getY();

                pointCount = 0;

                if (isScrolling) {
                    startScroll = false;
                    isScrolling = false;

                    View nowView = mDragHelper.findTopChildUnder((int) event.getX(), (int) event.getY());
                    if (nowView == lastCapturedView) {
                        decideIntercept = true;
                        intercepted = true;
                    } else {
                        decideIntercept = true;
                        intercepted = false;
                    }
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

                    if (pointCount++ >= 1) {
                        if (xDelta > 0) {
                            intercepted = false;
                        } else if (Math.abs(xDelta) > 1.6f * Math.abs(yDelta)) {
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

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (decideIntercept) {
                if (intercepted) {
                    mDragHelper.processTouchEvent(event);
                    return true;
                } else {
                    return false;
                }
            } else {
                mDragHelper.processTouchEvent(event);
                return super.onTouchEvent(event);
            }
        } else if (decideIntercept) {
            if (intercepted) {
                mDragHelper.processTouchEvent(event);
                return true;
            } else {
                super.onTouchEvent(event);
                return true;
            }
        } else {
            return false;
        }

    }

    public void setOnSlideOutListener(OnSlideOutListener listener) {
        sListener = listener;
    }

    interface OnSlideOutListener {
        void OnSlideOut(View view);
    }

}
