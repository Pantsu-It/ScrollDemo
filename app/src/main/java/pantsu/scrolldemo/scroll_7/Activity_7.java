package pantsu.scrolldemo.scroll_7;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import pantsu.scrolldemo.MainActivity;
import pantsu.scrolldemo.scroll_6.DragBackGroup;
import pantsu.scrolldemo.scroll_container.OverScrollView;

/**
 * Created by Pantsu on 2016/3/22.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_7 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
    }

    private void configureLayout() {
        int width = MainActivity.displayMetrics.widthPixels;
        int height = MainActivity.displayMetrics.heightPixels - MainActivity.statusBarHeight;


        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50);
        for (int i = 1; i <= 12; i++) {
            Button btn = new Button(this);
            layout.addView(btn, params);
        }

        OverScrollView overScrollView = new OverScrollView(this);
        ScrollView.LayoutParams params1 = new ScrollView.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        overScrollView.setBackgroundColor(0xff445566);
        overScrollView.addView(layout);
        setContentView(overScrollView);
    }

}
