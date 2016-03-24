package pantsu.scrolldemo.scroll_2;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Pantsu on 2016/3/23.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class DragViewGroup extends LinearLayout {
    private ViewDragHelper mDragger;
    private OnClampListener mListener;

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = 0;
                final int rightBound = getWidth() - child.getWidth();
                final int newLeft = Math.max(Math.min(left, leftBound), rightBound);
                if (mListener != null)
                    mListener.onClampHorizontal(newLeft);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = 0;
                final int bottomBound = getHeight() - child.getHeight();
                final int newTop = Math.max(Math.min(top, topBound), bottomBound);
                if (mListener != null)
                    mListener.onClampVertical(newTop);
                return newTop;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    public void setOnClampListener(OnClampListener listener) {
        mListener = listener;
    }

    interface OnClampListener {
        void onClampHorizontal(int left);

        void onClampVertical(int top);
    }
}
