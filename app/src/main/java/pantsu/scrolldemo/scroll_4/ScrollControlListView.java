package pantsu.scrolldemo.scroll_4;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import pantsu.scrolldemo.R;

/**
 * Created by Pants on 2016/4/18.
 */
public class ScrollControlListView extends ListView {

    private ViewDragHelper mDragHelper;

    private int minX, maxX, minY, maxY;
    private int curX;

    private static final float conditionRate = 0.5f;

    private View lastCapturedView;
    private Object lastCapturedMark;
    private int lastCapturedState;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_SCROLLED = 1;

    public ScrollControlListView(Context context) {
        super(context, null);
    }

    public ScrollControlListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScrollControlListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
//                if (lastCapturedView == child) {
//                int y = (int) child.getY();
//                setRange(-child.findViewById(R.id.operation).getWidth(), 0, y, y);
//                    return true;
//                } else
                if (child.getTag(R.id.tag_type) != null && child.getTag(R.id.tag_type).equals("scroll")) {
                    int y = (int) child.getY();
                    setRange(-child.findViewById(R.id.operation).getWidth(), 0, y, y);
                    lastCapturedView = child;
                    lastCapturedMark = child.getTag(R.id.tag_mark);
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
                    isScrolling = false;
                } else if (Math.abs(maxX - left) < 4) {
                    isScrolling = false;
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                if (xvel > 1200) {
                    smoothScrollItemToState(STATE_NORMAL);
                } else if (xvel < -1200) {
                    smoothScrollItemToState(STATE_SCROLLED);
                } else if (curX < minX + conditionRate * (maxX - minX)) {
                    smoothScrollItemToState(STATE_SCROLLED);
                } else {
                    smoothScrollItemToState(STATE_NORMAL);
                }
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

    private void smoothScrollItemToState(int state) {
        lastCapturedState = state;
        if (state == STATE_NORMAL) {
            mDragHelper.smoothSlideViewTo(lastCapturedView, maxX, maxY);
        } else if (state == STATE_SCROLLED) {
            mDragHelper.smoothSlideViewTo(lastCapturedView, minX, maxY);
        }
        postInvalidate();
    }

    private void scrollItemToState(View capturedView, int state) {
        mDragHelper.captureChildView(capturedView, 0);
        if (state == STATE_NORMAL) {
            mDragHelper.settleCapturedViewAt(maxX, maxY);
        } else if (state == STATE_SCROLLED) {
            mDragHelper.settleCapturedViewAt(minX, maxY);
        }
        postInvalidate();
    }

    public void setRange(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    private float xDelta, yDelta, xDown, yDown;
    private boolean decideIntercept, intercepted;
    private boolean isScrolling = false;

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

                // the last captured view is scrolling or in SCROLLED_STATE ~
//                if (lastCapturedView != null && inRangeOfView(lastCapturedView, event)) {
//                    isScrolling = false;
//
//                    decideIntercept = true;
//                    intercepted = true;
//                } else {
                    if (lastCapturedView != null && lastCapturedState != STATE_NORMAL) {
                        decideIntercept = true;
                        intercepted = false;
                        // scroll last captured view to NORMAL_STATE
                        smoothScrollItemToState(STATE_NORMAL);
                        lastCapturedView = null;
                        lastCapturedMark = null;
                    } else {
                        decideIntercept = false;
                        intercepted = false;
                    }
//                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!decideIntercept) {
                    float curX = event.getX();
                    float curY = event.getY();
                    xDelta = curX - xDown;
                    yDelta = curY - yDown;

                    Log.d("foobar", Math.abs(xDelta) + " + " + Math.abs(yDelta) + " = " + (Math.abs(xDelta) + Math.abs
                            (yDelta)));
                    if (Math.abs(xDelta) > 1.6f * Math.abs(yDelta)) {
                        intercepted = true;
                        decideIntercept = true;
                    }

                    if (Math.abs(xDelta) + Math.abs(yDelta) > 30) {
                        intercepted = false;
                        decideIntercept = true;
                    } else {
                        long downTime = event.getDownTime();
                        long curTime = event.getEventTime();
                        if (curTime - downTime > 200) {
                            decideIntercept = true;
                        }
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
                    // directly accept motion-event while touch last scrolling view
                    mDragHelper.processTouchEvent(event);
                    return true;
                } else {
                    // reject motion-event because of touching different view while last view is still scrolling
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
            // don't deliver any motion-event to views when having not decided which view to deal with motion-events~
            return false;
        }

    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view
                .getHeight())) {
            return false;
        }
        return true;
    }

    public View getLastScrolledViewItem() {
        if (lastCapturedView != null && lastCapturedState == STATE_SCROLLED) {
            return lastCapturedView;
        }
        return null;
    }

    public Object getLastScrolledViewMark() {
        if (lastCapturedView != null && lastCapturedState == STATE_SCROLLED) {
            return lastCapturedMark;
        }
        return null;
    }

    public void updateScrolledItem(View view, Object mark) {
        lastCapturedState = STATE_SCROLLED;
        lastCapturedView = view;
        lastCapturedMark = mark;
        scrollItemToState(view, STATE_SCROLLED);
    }

    public void clearScrollItem() {
        if (lastCapturedView != null && lastCapturedState == STATE_SCROLLED) {
            scrollItemToState(lastCapturedView, STATE_NORMAL);
        }
        lastCapturedView = null;
        lastCapturedMark = null;
        lastCapturedState = STATE_NORMAL;
    }

}