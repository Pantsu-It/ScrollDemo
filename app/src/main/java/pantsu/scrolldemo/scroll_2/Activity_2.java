package pantsu.scrolldemo.scroll_2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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

    private ImageView title;
    private LinearLayout container_center;
    private DragViewGroup dragViewGroup;
    private DragViewGroup_Slide dragViewGroupSlide;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_2);
        configureLayout();
    }

    private static final int WRAP = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int MATCH = LinearLayout.LayoutParams.MATCH_PARENT;

    private void configureLayout() {
        final int width = MainActivity.displayMetrics.widthPixels;
        int height = MainActivity.displayMetrics.heightPixels - MainActivity.statusBarHeight;
        final int w1 = 70, _w = 870;
        final int h1 = 100, h2 = 84, h3 = 100, _h = 1160;
        int w1_banner = 118, _w_banner = 2406;
        final int h_banner = 188, h_container = height - h1 - h2 - h3, h_container_shrink = h_container - h_banner;

        findViewById(R.id.corner).setLayoutParams(getLinearParams(w1, h2));
        findViewById(R.id.top).setLayoutParams(getLinearParams(_w, h2));
        findViewById(R.id.left).setLayoutParams(getLinearParams(w1, _h));
        findViewById(R.id.content).setLayoutParams(getLinearParams(_w, _h));
        findViewById(R.id.bottom).setLayoutParams(getLinearParams(width, h3));

        dragViewGroup = ((DragViewGroup) findViewById(R.id.dragGroup));
        dragViewGroupSlide = (DragViewGroup_Slide) findViewById(R.id.dragGroupSlide);


        title = (ImageView) findViewById(R.id.title);
        title.setLayoutParams(getLinearParams(width, h1));
        title.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dragViewGroupSlide.isSliding())
                            return;
                        if (dragViewGroupSlide.condition) {
                            dragViewGroupSlide.slideIn();
                            title.setImageResource(R.drawable.a_title);
                            container_center.setLayoutParams(getLinearParams(width, h_container));
                            dragViewGroup.setBound(0, width - _w, 0, h_container - _h);

                            dragViewGroupSlide.condition = false;
                        } else {
                            dragViewGroupSlide.slideOut();
                            title.setImageResource(R.drawable.a_title_2);
                            container_center.setLayoutParams(getLinearParams(width, h_container_shrink));
                            dragViewGroup.setBound(0, width - _w, 0, h_container_shrink - _h);

                            dragViewGroupSlide.condition = true;
                        }
                        dragViewGroup.dragChildTo(-scroll_top.getScrollX(), -scroll_left.getScrollY());
                    }
                }
        );


        LinearLayout slide = (LinearLayout) findViewById(R.id.slide);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) slide.getLayoutParams();
        params.topMargin = -h_banner;

        findViewById(R.id.banner_head).setLayoutParams(getLinearParams(w1_banner, h_banner));
        findViewById(R.id.banner_content).setLayoutParams(getLinearParams(_w_banner, h_banner));

        container_center = (LinearLayout) findViewById(R.id.container_center);
        container_center.setLayoutParams(getLinearParams(width, h_container));

        scroll_top = (HorizontalScrollView) findViewById(R.id.scroll_top);

        scroll_left = (ScrollView) findViewById(R.id.scroll_left);

        scroll_top.setOnTouchListener(this);
        scroll_left.setOnTouchListener(this);

        dragViewGroup.setOnClampListener(
                new DragViewGroup.OnClampListener() {
                    @Override
                    public void onClampHorizontal(int left) {
                        scroll_top.scrollTo(-left, 0);
                    }

                    @Override
                    public void onClampVertical(int top) {
                        scroll_left.scrollTo(0, -top);
                    }
                }

        );
        dragViewGroupSlide.setOnDragListener(new DragViewGroup_Slide.OnDragListener() {
            @Override
            public void onViewPositionChanged(int left, int top, int dx, int dy) {

            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (dragViewGroupSlide.condition) {
                    title.setImageResource(R.drawable.a_title_2);
                    container_center.setLayoutParams(getLinearParams(width, h_container_shrink));
                    dragViewGroup.setBound(0, width - _w, 0, h_container_shrink - _h);
                } else {
                    title.setImageResource(R.drawable.a_title);
                    container_center.setLayoutParams(getLinearParams(width, h_container));
                    dragViewGroup.setBound(0, width - _w, 0, h_container - _h);
                }
                dragViewGroup.dragChildTo(-scroll_top.getScrollX(), -scroll_left.getScrollY());
            }
        });
    }

    private LinearLayout.LayoutParams getLinearParams(int w, int h) {
        return new LinearLayout.LayoutParams(w, h);
    }

    private FrameLayout.LayoutParams getFrameParams(int w, int h) {
        return new FrameLayout.LayoutParams(w, h);
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
