package pantsu.scrolldemo.scroll_3;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import pantsu.scrolldemo.R;

/**
 * Created by Pantsu on 2016/3/23.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class DragViewGroup extends LinearLayout {
    private ViewDragHelper mDragHelper;
    private OnClampListener cListener;

    private static final int STATUS_EXPEND = 0, STATUS_COLLAPSE = 1, STATUS_DRAG = 2, STATUS_SCROLL = 3,
            STATUS_UNKNOWN = -1;
    private int status = STATUS_EXPEND;
    private int expected_status = STATUS_COLLAPSE;

    private int minY, maxY;
    private int curY;
    float edgeRate = 0.12f;
    int minEdge, maxEdge;

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (child.getId() == R.id.list_pager) {
                    status = STATUS_DRAG;
                    return true;
                }
                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int newTop = Math.max(Math.min(top, maxY), minY);
                curY = newTop;
                return newTop;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (atExtactEdge(top) != STATUS_UNKNOWN && status == STATUS_SCROLL) {
                    status = atExtactEdge(top);
                    expected_status = 1 - status;
                }

                if (cListener != null) {
                    cListener.onClampHorizontal(left);
                    cListener.onClampVertical(top);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                int edgeSettle = atExtactEdge(curY);
                if (edgeSettle == status) {
                    status = edgeSettle;
                    return;
                }

                int edgeRange = inWhichEdgeRange(curY);
                if (STATUS_COLLAPSE == edgeRange) {
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, minY);
                } else if (STATUS_EXPEND == edgeRange) {
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, maxY);
                } else {
                    if (STATUS_COLLAPSE == expected_status) {
                        mDragHelper.smoothSlideViewTo(releasedChild, 0, minY);
                    } else {
                        mDragHelper.smoothSlideViewTo(releasedChild, 0, maxY);
                    }
                }
                status = STATUS_SCROLL;

                postInvalidate();
            }

            private int atExtactEdge(int y) {
                if (y == minY)
                    return STATUS_COLLAPSE;
                else if (y == maxY)
                    return STATUS_EXPEND;
                else
                    return STATUS_UNKNOWN;
            }

            private int inWhichEdgeRange(int y) {
                if (y < minEdge)
                    return STATUS_COLLAPSE;
                else if (y > maxEdge)
                    return STATUS_EXPEND;
                else
                    return STATUS_UNKNOWN;
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
        this.minY = minY;
        this.maxY = maxY;

        minEdge = (int) (minY + (maxY - minY) * edgeRate);
        maxEdge = (int) (maxY - (maxY - minY) * edgeRate);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mDragHelper.shouldInterceptTouchEvent(event);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    public void setOnClampListener(OnClampListener listener) {
        cListener = listener;
    }

    interface OnClampListener {
        void onClampHorizontal(int left);

        void onClampVertical(int top);
    }

}
