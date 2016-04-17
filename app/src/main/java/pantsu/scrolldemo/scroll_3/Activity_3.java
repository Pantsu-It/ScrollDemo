package pantsu.scrolldemo.scroll_3;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import pantsu.scrolldemo.MainActivity;
import pantsu.scrolldemo.R;

/**
 * Created by Pantsu on 2016/3/22.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_3 extends Activity {

    private ImageView clock, clock_collapse, setting;
    private FadingTwoViewGroup fade_group;
    private DragViewGroup drag_group;
    private ViewPager list_pager;

    int width = MainActivity.displayMetrics.widthPixels;
    int height = MainActivity.displayMetrics.heightPixels - MainActivity.statusBarHeight;
    public int h_clock = 796, h_clock_collapse = 310, h_setting = 120,
            h_list = height - h_clock_collapse - h_setting;

    int[] bgColors = {0xff00816F, 0xff1E7DA9, 0xff369245, 0xff838F97};
    int[] clockImage = {R.drawable.b_clock1, R.drawable.b_clock2, R.drawable.b_clock3, R.drawable.b_clock4};
    int[] clockCollapseImage = {R.drawable.b_collapse_clock1, R.drawable.b_collapse_clock2,
            R.drawable.b_collapse_clock3, R.drawable.b_collapse_clock4};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_3);
        configureLayout();
        initialView();
    }

    private void configureLayout() {
        clock = (ImageView) findViewById(R.id.clock);
        clock_collapse = (ImageView) findViewById(R.id.clock_collapse);
        fade_group = (FadingTwoViewGroup) findViewById(R.id.fade_group);
        list_pager = (ViewPager) findViewById(R.id.list_pager);
        setting = (ImageView) findViewById(R.id.setting);

        setRelativeParams(clock, width, h_clock);
        setRelativeParams(clock_collapse, width, h_clock_collapse);
        setLinearParams(fade_group, width, h_clock);
        setLinearParams(list_pager, width, h_list);
        setFrameParams(setting, width, h_setting);

        drag_group = (DragViewGroup) findViewById(R.id.dragGroup);
    }

    private RelativeLayout.LayoutParams setRelativeParams(View view, int w, int h) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (params == null)
            params = new RelativeLayout.LayoutParams(w, h);
        params.width = w;
        params.height = h;
        return params;
    }

    private LinearLayout.LayoutParams setLinearParams(View view, int w, int h) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (params == null)
            params = new LinearLayout.LayoutParams(w, h);
        params.width = w;
        params.height = h;
        return params;
    }

    private FrameLayout.LayoutParams setFrameParams(View view, int w, int h) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (params == null)
            params = new FrameLayout.LayoutParams(w, h);
        params.width = w;
        params.height = h;
        return params;
    }

    private void initialView() {
        fade_group.setFadingRate(0);

        drag_group.setRange(0, 0, h_clock_collapse, h_clock);
        drag_group.setBackgroundColor(bgColors[0]);
        drag_group.setOnClampListener(new DragViewGroup.OnClampListener() {
            @Override
            public void onClampHorizontal(int left) {
            }

            @Override
            public void onClampVertical(int top) {
                fade_group.setFadingRate((float) (h_clock - top) / (h_clock - h_clock_collapse));
            }
        });

        list_pager.setOffscreenPageLimit(3);
        list_pager.setAdapter(new MyPagerAdapter());
        list_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

                drag_group.setBackgroundColor(bgColors[position]);
                drag_group.scrollToAdjust();
                clock.setImageResource(clockImage[position]);
                clock_collapse.setImageResource(clockCollapseImage[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


/*
        list_pager.setOnTouchListener(new View.OnTouchListener() {
            float downX, downY;
            float moveX, moveY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("event", "list - down");

                        downX = event.getX();
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("event", "list - move");

                        moveX = event.getX();
                        moveY = event.getY();
                        if (Math.abs(downY - moveY) > Math.abs(downX - moveX)) {
                            Log.d("event", "list - rechange ------------------------------------------------");
                            list_pager.dispatchTouchEvent(getUpEvent(moveX, moveY));
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            drag_group.dispatchTouchEvent(getDownEvent(moveX, moveY));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        Log.d("event", "list - up");
                        break;
                }
                return false;
            }
        });
        */
    }

    public static MotionEvent getDownEvent(float x, float y) {
        return MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
    }

    public static MotionEvent getUpEvent(float x, float y) {
        return MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
    }

    private int currentPage = 0;
    public ScrollView[] scrollViews = new ScrollView[4];

    public boolean isOnTop() {
        int scrollY = scrollViews[currentPage].getScrollY();
        return scrollY < 2;
    }

    public int currentScrollY() {
        return scrollViews[currentPage].getScrollY();
    }

    private class MyPagerAdapter extends PagerAdapter {
        int[] listIds = {
                R.drawable.b_list1_img, R.drawable.b_list2_img,
                R.drawable.b_list3_img, R.drawable.b_list4_img
        };

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ScrollView item = (ScrollView) View.inflate(Activity_3.this, R.layout.pager_item, null);
            ImageView list = (ImageView) item.findViewById(R.id.list);
            list.setImageResource(listIds[position]);
            container.addView(item);

            scrollViews[position] = item;
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
