package pantsu.scrolldemo.scroll_container;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

/**
 * Created by Pants on 2016/7/4.
 */
public class OverScrollView extends ScrollView {

    View child;

    ViewDragHelper dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }
    });


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
                    child = getChildAt(0);
            }
        });
    }
}
