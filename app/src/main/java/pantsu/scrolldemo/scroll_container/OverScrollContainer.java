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
import android.widget.FrameLayout;

/**
 * Created by Pants on 2016/7/5.
 */
public class OverScrollContainer extends FrameLayout {

    View mChild;
    ViewDragHelper dragHelper;

    public OverScrollContainer(Context context) {
        this(context, null);
    }

    public OverScrollContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
//                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int childCount = getChildCount();
                if (childCount == 1)
                    mChild = getChildAt(0);
                else if (childCount > 2)
                    throw new RuntimeException("OverScrollContainer don't allow more than one child~");
            }
        });


        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mChild;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return 0;
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("dispatchTouch", "Hi");
        return super.dispatchTouchEvent(ev);
    }
}
