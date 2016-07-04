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

/**
 * Created by Pants on 2016/7/4.
 */
public class OverScrollView extends ScrollView {

    View mChild;
    ViewDragHelper mDragHelper;


    public OverScrollView(Context context) {
        this(context, null);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

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

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("tag", "scrolleY:" + getScrollY()
                        + "  height-child.height:" + (getHeight() - mChild.getHeight()));
                return false;
            }
        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mDragHelper.processTouchEvent(event);
//        return true;
////        return super.onTouchEvent(event);
//    }
}
