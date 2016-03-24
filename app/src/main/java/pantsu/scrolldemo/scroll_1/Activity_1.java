package pantsu.scrolldemo.scroll_1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import pantsu.scrolldemo.MainActivity;
import pantsu.scrolldemo.R;

/**
 * Created by Pantsu on 2016/3/22.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_1 extends Activity implements View.OnTouchListener {

    private HorizontalScrollView scroll_h;
    private ScrollView scroll_v;
    private HorizontalScrollView scroll_top;
    private ScrollView scroll_left;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_1);
        configureLayout();
    }

    private void configureLayout() {
        int width = MainActivity.displayMetrics.widthPixels;
        int height = MainActivity.displayMetrics.heightPixels;
        int w1 = 70, _w = 870;
        int h1 = 100, h2 = 84, h3 = 100, _h = 1160;
        findViewById(R.id.title).setLayoutParams(getLinearParams(width, h1));
        findViewById(R.id.corner).setLayoutParams(getLinearParams(w1, h2));
        findViewById(R.id.top).setLayoutParams(getLinearParams(_w, h2));
        findViewById(R.id.left).setLayoutParams(getLinearParams(w1, _h));
        findViewById(R.id.content).setLayoutParams(getLinearParams(_w, _h));
        findViewById(R.id.bottom).setLayoutParams(getLinearParams(width, h3));

        scroll_h = (HorizontalScrollView) findViewById(R.id.scroll_h);
        scroll_v = (ScrollView) findViewById(R.id.scroll_v);
        scroll_top = (HorizontalScrollView) findViewById(R.id.scroll_top);
        scroll_left = (ScrollView) findViewById(R.id.scroll_left);
        scroll_h.setOnTouchListener(this);
        scroll_v.setOnTouchListener(this);
        scroll_top.setOnTouchListener(this);
        scroll_left.setOnTouchListener(this);

        findViewById(R.id.content).setOnTouchListener(this);
    }

    private LinearLayout.LayoutParams getLinearParams(int w, int h) {
        return new LinearLayout.LayoutParams(w, h);
    }

    int startX, startY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.scroll_top:
                return true;
            case R.id.scroll_left:
                return true;
            case R.id.content:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scroll_h.requestDisallowInterceptTouchEvent(true);
                        scroll_v.requestDisallowInterceptTouchEvent(true);
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int currentX = (int) event.getX();
                        int currentY = (int) event.getY();

                        int deltaX = startX - currentX;
                        int deltaY = startY - currentY;
                        Log.d("x,y", "x:" + deltaX + " y:" + deltaY);

                        startX = currentX;
                        startY = currentY;

                        if (Math.abs(deltaX) + Math.abs(deltaX) < ViewConfiguration.get(this).getScaledTouchSlop())
                            break;

                        scroll_h.smoothScrollBy(deltaX, 0);
                        scroll_v.smoothScrollBy(0, deltaY);
                        scroll_top.smoothScrollBy(deltaX, 0);
                        scroll_left.smoothScrollBy(0, deltaY);

                        startX = currentX;
                        startY = currentY;
                        break;
                }
                return true;
        }

        return false;
    }

}
