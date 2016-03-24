package pantsu.scrolldemo.scroll_2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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
public class Activity_2 extends Activity implements View.OnTouchListener {

    private HorizontalScrollView scroll_top;
    private ScrollView scroll_left;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_2);
        configureLayout();
    }

    private static final int WRAP = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int MATCH = LinearLayout.LayoutParams.MATCH_PARENT;

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

        scroll_top = (HorizontalScrollView) findViewById(R.id.scroll_top);
        scroll_left = (ScrollView) findViewById(R.id.scroll_left);
        scroll_top.setOnTouchListener(this);
        scroll_left.setOnTouchListener(this);

        findViewById(R.id.content).setOnTouchListener(this);

        ((DragViewGroup) findViewById(R.id.dragGroup)).setOnClampListener(new DragViewGroup.OnClampListener() {
            @Override
            public void onClampHorizontal(int left) {
                scroll_top.scrollTo(-left, 0);
            }

            @Override
            public void onClampVertical(int top) {
                scroll_left.scrollTo(0, -top);
            }
        });
    }

    private LinearLayout.LayoutParams getLinearParams(int w, int h) {
        return new LinearLayout.LayoutParams(w, h);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.scroll_top:
                return true;
            case R.id.scroll_left:
                return true;
        }
        return false;
    }

}
