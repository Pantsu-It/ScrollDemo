package pantsu.scrolldemo.scroll_4;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import pantsu.scrolldemo.R;

/**
 * Created by Pants on 2016/4/18.
 */
public class ScrollControlListView extends ListView {

    private ViewDragHelper mDragHelper;

    private int minX, maxX, minY, maxY;
    private int curX;

    private static final float conditionRate = 0.35f;

    private IScrollView lastCapturedView;
    private Object lastCapturedMark;
    private int lastCapturedState;
    /** 用于短时间连续点击和滑动列表，导致lastCapturedView指向另一个View 以及 前一个View没有完全滑动到正常位置 */
    private IScrollView cachedCapturedView;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_SCROLLED = 1;

    private OnScrollListener mOnScrollListener;

    public ScrollControlListView(Context context) {
        super(context, null);
    }

    public ScrollControlListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScrollControlListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
                Log.d("test2",
                        String.format("state:%d decide:%b interp:%b", scrollState, decideIntercept, intercepted));
                if (scrollState == STATE_SCROLLED && decideIntercept && intercepted) {
                    Log.d("test2", "~~~~~");
                    Integer pos = (Integer) lastCapturedView.getTag(R.id.tag_position);
                    if (pos != null) {
                        int y;
                        if (pos == 0) {
                            y = 0;
                        } else {
                            View sideView = getChildAt(pos - 1);
                            y = (int) (sideView.getY() + sideView.getHeight());
                        }
                        if (y != maxY) {
                            setRange(-lastCapturedView.findViewById(R.id.operation).getWidth(), 0, y, y);
                            requestLayout();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (child.getTag(R.id.tag_type) != null && child.getTag(R.id.tag_type).equals("scroll")) {
                    int y = (int) child.getY();
                    setRange(-child.findViewById(R.id.operation).getWidth(), 0, y, y);
                    cachedCapturedView = lastCapturedView;
                    lastCapturedView = (IScrollView) child;
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
                } else if (curX < maxX + conditionRate * (minX - maxX)) {
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
        if (state == STATE_NORMAL) {
            capturedView.offsetLeftAndRight(maxX - capturedView.getLeft());
            postInvalidate();
        } else if (state == STATE_SCROLLED) {
            capturedView.offsetLeftAndRight(minX - capturedView.getLeft());
            postInvalidate();
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
    private boolean valid;
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

                if (lastCapturedView != null && (lastCapturedState != STATE_NORMAL)) {
                    decideIntercept = true;
                    intercepted = true;

                    if (inRangeOfView(lastCapturedView, event)) {
                        // the last captured view is scrolling or in SCROLLED_STATE ~
                        valid = true;
                    } else {
                        // scroll last captured view to NORMAL_STATE
                        smoothScrollItemToState(STATE_NORMAL);
                        valid = false;
//                        lastCapturedView = null;
                        lastCapturedMark = null;
                    }
                } else {
                    decideIntercept = false;
                    intercepted = false;
                    valid = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!decideIntercept) {
                    float curX = event.getX();
                    float curY = event.getY();
                    xDelta = curX - xDown;
                    yDelta = curY - yDown;

//                    Log.d("foobar", Math.abs(xDelta) + " + " + Math.abs(yDelta) + " = " + (Math.abs(xDelta) + Math.abs
//                            (yDelta)));
                    if (Math.abs(xDelta) > 1.6f * Math.abs(yDelta)) {
                        intercepted = true;
                        decideIntercept = true;
                    }
                    // if not decideIntercept, judge by other method
                    if (!decideIntercept) {
                        if (Math.abs(xDelta) + Math.abs(yDelta) > 30) {
                            intercepted = false;
                            decideIntercept = true;
                        } else {
                            long downTime = event.getDownTime();
                            long curTime = event.getEventTime();
                            if (curTime - downTime > 300) {
                                decideIntercept = true;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("foobar", "ACTION_UP" + "decide:" + decideIntercept + " intercept:" + intercepted + " state:" +
                        lastCapturedState);
                if (decideIntercept && intercepted && lastCapturedState == STATE_SCROLLED) {
                    float curX = event.getX();
                    float curY = event.getY();
                    xDelta = curX - xDown;
                    yDelta = curY - yDown;
                    Log.d("foobar", "ACTION_UP" + " delta:" + (Math.abs(xDelta) + Math.abs(yDelta)));

                    if (Math.abs(xDelta) + Math.abs(yDelta) < 30) {
                        View optionView = lastCapturedView.getOptionView();
                        if (optionView != null && inRangeOfView(optionView, event)) {
                            if (optionView instanceof ViewGroup) {
                                ViewGroup optionViewGroup = (ViewGroup) optionView;
                                for (int i = 0; i < optionViewGroup.getChildCount(); ++i) {
                                    View view = optionViewGroup.getChildAt(i);
                                    if (inRangeOfView(view, event)) {
                                        lastCapturedView.triggerOptionClickListener(view);
                                        smoothScrollItemToState(STATE_NORMAL);
                                        break;
                                    }
                                }
                            } else {
                                optionView.callOnClick();
                            }
                        }
                        smoothScrollItemToState(STATE_NORMAL);
                    }
                } else {
                    if (!intercepted && lastCapturedState == STATE_NORMAL) {
                        if (!isScrolltoRightPosition(lastCapturedView, STATE_NORMAL)) {
                            scrollItemToState(lastCapturedView, STATE_NORMAL);
                        } else if (cachedCapturedView != null
                                && !isScrolltoRightPosition(cachedCapturedView, STATE_NORMAL)) {
                            scrollItemToState(cachedCapturedView, STATE_NORMAL);
                        }
                    }
                }
                break;
        }

        Log.d("foobar", "event:" + event.getAction() + " decide:" + decideIntercept + " interp:" + intercepted);

        if (!valid) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDragHelper.processTouchEvent(event);
            if (decideIntercept && intercepted) {
                Log.d("foobar", "p11");
                return true;
            }
            Log.d("foobar", "p12");
            return super.onTouchEvent(event);
        } else if (decideIntercept) {
            if (intercepted) {
                Log.d("foobar", "p21");
                mDragHelper.processTouchEvent(event);
                return true;
            } else {
                Log.d("foobar", "p22");
                super.onTouchEvent(event);
                return false;
            }
        } else {
            // deliver some motion-events(such as ACTION_UP) to views which help trigger OnClickListener normally!
            Log.d("foobar", "p3");
            return super.onTouchEvent(event);
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

    private boolean isScrolltoRightPosition(View view, int state) {
        if (state == STATE_NORMAL) {
            if (view.getLeft() == maxX) {
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
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

    // TODO: 2017/8/23 公用方法需要检查state状态
    public void updateScrolledItem(View view, Object mark) {
        lastCapturedState = STATE_SCROLLED;
        lastCapturedView = (IScrollView) view;
        lastCapturedMark = mark;
        scrollItemToState(view, STATE_SCROLLED);
    }

    // TODO: 2017/8/23 公用方法需要检查state状态
    public void clearScrollItem() {
        if (lastCapturedView != null && lastCapturedState == STATE_SCROLLED) {
            scrollItemToState(lastCapturedView, STATE_NORMAL);
        }
//        lastCapturedView = null;
        lastCapturedMark = null;
        lastCapturedState = STATE_NORMAL;
    }

    public boolean checkLongClickValid() {
        return !decideIntercept;
    }
}
