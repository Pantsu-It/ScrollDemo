package pantsu.scrolldemo.scroll_5;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import pantsu.scrolldemo.MainActivity;
import pantsu.scrolldemo.R;
import pantsu.scrolldemo.scroll_6.DragBackGroup;

/**
 * Created by Pantsu on 2016/3/22.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_5 extends Activity implements View.OnTouchListener {

    DragBackGroup dragBackGroup;
    ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_6);
        configureLayout();
    }

    private void configureLayout() {
        int width = MainActivity.displayMetrics.widthPixels;
        int height = MainActivity.displayMetrics.heightPixels - MainActivity.statusBarHeight;

        dragBackGroup = (DragBackGroup) findViewById(R.id.dragBackGroup);
        dragBackGroup.setRange(0, width, 0, 0);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 10; i++) {
            linearLayout.addView(getView(), i);
        }
        scrollView.addView(linearLayout);
    }

    private View getView() {
        return View.inflate(this, R.layout.item_cell, null);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
