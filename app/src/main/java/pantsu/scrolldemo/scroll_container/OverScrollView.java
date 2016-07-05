package pantsu.scrolldemo.scroll_container;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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

    private View mChild;
    private Scroller scroller;

    public OverScrollView(Context context) {
        this(context, null);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);

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
    }

    private static final int STATUS_TOP = 0;
    private static final int STATUS_MIDDLE = 1;
    private static final int STATUS_BOTTOM = 2;

    private int status = STATUS_MIDDLE;
    private int markY;
    private int curY;
    private int lastY;
    private float scrollRate = 0.5f;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        lastY = curY;
        curY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("喵喵喵", "aaaaaaaaaaaaaa");
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() <= 0) {
                    // 顶部以上
                    if (status != STATUS_TOP) {
                        status = STATUS_TOP;
                        markY = curY;
                    } else if (curY < markY) {
                        status = STATUS_MIDDLE;
                    } else {
                        mChild.scrollBy(0, (int) ((lastY - curY) * scrollRate));
                    }
                } else if (getScrollY() == mChild.getHeight() - getHeight()) {
                    // 底部以下
                    if (status != STATUS_BOTTOM) {
                        status = STATUS_BOTTOM;
                        markY = curY;
                    } else if (curY > markY) {
                        status = STATUS_MIDDLE;
                    } else {
                        mChild.scrollBy(0, (int) ((lastY - curY) * scrollRate));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (status == STATUS_TOP) {
                    scroller.startScroll(0, mChild.getScrollY(), 0, -mChild.getScrollY(), 500);
                    scrollTo(0, 1);
                } else if (status == STATUS_BOTTOM) {
                    scroller.startScroll(0, mChild.getScrollY(), 0, -mChild.getScrollY(), 500);
                    scrollTo(0, mChild.getHeight() - getHeight() - 1);
                }
                status = STATUS_MIDDLE;
                break;
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
}
