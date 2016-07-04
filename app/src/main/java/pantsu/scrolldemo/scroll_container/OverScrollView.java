package pantsu.scrolldemo.scroll_container;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by Pants on 2016/7/4.
 */
public class OverScrollView extends ScrollView {

    View mChild;
    Scroller scroller;
    ScrollView scrollView;

    public OverScrollView(Context context) {
        this(context, null);
    }

    int status = STATUS_MIDDLE;
    int markY;
    int curY;

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        scrollView = this;


        ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
//                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (getChildCount() != 0)
                    mChild = getChildAt(0);
            }
        });

//        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
//            @Override
//            public boolean tryCaptureView(View child, int pointerId) {
//                return child == mChild;
//            }
//        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        curY = (int) event.getY();
        Log.d("tag", "scrolleY:" + getScrollY()
                + "  child.height-height:" + (mChild.getHeight() - getHeight()
                + "  curY:" + curY
                + "  markY:" + markY));

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (getScrollY() == 0) {
                // 顶部以上
                if (status != STATUS_TOP) {
                    status = STATUS_TOP;
                    markY = curY;
                } else {
                    mChild.scrollTo(0, markY - curY);
                }
            } else if (getScrollY() == mChild.getHeight() - getHeight()) {
                // 底部以下
                if (status != STATUS_BOTTOM) {
                    status = STATUS_BOTTOM;
                    markY = curY;
                } else {
                    mChild.scrollTo(0, markY - curY);
                }
            } else {
//                if (status != STATUS_MIDDLE) {
//                    status = STATUS_MIDDLE;
//                    mChild.scrollTo(0, markY - curY);
//                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (status == STATUS_TOP)
                scroller.startScroll(0, mChild.getScrollY(), 0, -mChild.getScrollY(), 500);
            else if (status == STATUS_BOTTOM) {
                scroller.startScroll(0, mChild.getScrollY(), 0, -mChild.getScrollY(), 500);
            }
            status = STATUS_MIDDLE;
        }

        if (status == STATUS_MIDDLE)
            return super.onTouchEvent(event);
        else
            return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (scroller.computeScrollOffset()) {
            mChild.scrollTo(0, scroller.getCurrY());
        }
    }

    public static final int STATUS_TOP = 0;
    public static final int STATUS_MIDDLE = 1;
    public static final int STATUS_BOTTOM = 2;

}
