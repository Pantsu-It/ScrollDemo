package pantsu.scrolldemo.scroll_6;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Method;

import pantsu.scrolldemo.MainActivity;
import pantsu.scrolldemo.R;
import pantsu.scrolldemo.scroll_3.Activity_3;

/**
 * Created by Pantsu on 2016/3/22.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_6 extends Activity implements View.OnTouchListener {

    DragBackGroup dragBackGroup;
    ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_6);

//        convertActivityToTranslucent(this);
        configureLayout();
    }

    private void configureLayout() {
        int width = MainActivity.displayMetrics.widthPixels;
        int height = MainActivity.displayMetrics.heightPixels - MainActivity.statusBarHeight;

        dragBackGroup = (DragBackGroup) findViewById(R.id.dragBackGroup);
        dragBackGroup.setRange(0, width, 0, 0);
        dragBackGroup.setOnFinishListener(new DragBackGroup.OnFinishListener() {
            @Override
            public void finish() {
                finishIt();
            }
        });

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 30; i++) {
            linearLayout.addView(getView(i), i);
        }
        scrollView.addView(linearLayout);
    }

    private View getView(int position) {
        View v = View.inflate(this, R.layout.item_cell, null);
        ((TextView) v.findViewById(R.id.textView)).setText("I'm a textview in Item-" + position);
        return v;
    }

    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class[] t = Activity.class.getDeclaredClasses();
            Class translucentConversionListenerClazz = null;
            Class[] method = t;
            int len$ = t.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                Class clazz = method[i$];
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null, null});
            } else {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null});
            }
        } catch (Throwable e) {
        }
    }

    @Override
    public void onBackPressed() {
        dragBackGroup.scrollToMax();
    }

    private void finishIt() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
