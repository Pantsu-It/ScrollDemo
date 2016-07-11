package pantsu.scrolldemo.scroll_7;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
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
//        int width = MainActivity.displayMetrics.widthPixels;
//        int height = MainActivity.displayMetrics.heightPixels - MainActivity.statusBarHeight;

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 100);
        for (int i = 1; i <= 20; i++) {
            Button btn = new Button(this);
            btn.setText("我是Button-" + i);
            layout.addView(btn, i - 1, params);
        }

        final OverScrollView overScrollView = new OverScrollView(this);
        overScrollView.setBackgroundColor(0xff445566);
        ScrollView.LayoutParams params2 = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
        overScrollView.addView(layout, params2);

        ScrollView.LayoutParams params1 = new ScrollView.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setContentView(overScrollView, params1);

        OverScrollView.OverScrollStrategy scrollStrategy = new OverScrollView.OverScrollStrategy() {
            private float scrollRate = 0.5f;

            @Override
            public int onScrollTop(int y, int dy) {
                int scrollY = Math.min(y, 200);
                layout.scrollTo(0, -scrollY);
                return scrollY;
            }

            @Override
            public int onScrollBottom(int y, int dy) {
//                int scrollY = Math.min(y, 200);
//                layout.scrollTo(0, scrollY);
//                return scrollY;
                return 0;
            }

            @Override
            public void onReleaseTop(int y) {
                ValueAnimator animator = ValueAnimator.ofInt(-y, 0);
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(150);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollY = (int) animation.getAnimatedValue();
                        layout.scrollTo(0, scrollY);
                    }
                });
                animator.start();
            }

            @Override
            public void onReleaseBottom(int y) {
                ValueAnimator animator = ValueAnimator.ofInt(y, 0);
                animator.setInterpolator(new DecelerateInterpolator(0.5f));
                animator.setDuration(150);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollY = (int) animation.getAnimatedValue();
                        layout.scrollTo(0, scrollY);
                    }
                });
                animator.start();
            }
        };
        overScrollView.setScrollStrategy(scrollStrategy);
    }

}
