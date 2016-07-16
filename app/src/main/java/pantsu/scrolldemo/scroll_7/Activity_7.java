package pantsu.scrolldemo.scroll_7;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pantsu.scrolldemo.R;
import pantsu.scrolldemo.scroll_7_parts.ImageLoader;
import pantsu.scrolldemo.scroll_7_parts.ImageUtil;
import pantsu.scrolldemo.scroll_7_parts.SystemBarTintManager;
import pantsu.scrolldemo.scroll_container.OverScrollView;

/**
 * Created by Pants on 2016/7/5.
 */
public class Activity_7 extends Activity {
    private OverScrollView overScrollView;
    private ImageView titleBackground;
    private LinearLayout imageScrollView;
    private RelativeLayout.LayoutParams scrollLayoutParams;

    OverScrollView.OverScrollStrategy scrollStrategy1, scrollStrategy2, scrollStrategy3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_detail);
        setStatusBarAlpha();
        configureTitleLayout();
        configureImageScroll();


        ((TextView) findViewById(R.id.tvCampaignName)).setText("活动名称");
        ((TextView) findViewById(R.id.tvCampaignTime))
                .setText(getInterval("2016-07-13 10:10:10", "2016-07-29 10:10:10"));
        ((TextView) findViewById(R.id.tvCampaignDetail)).setText("活动详情"
                + "\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>"
                + "\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>\n<br/>");
        ImageLoader.getInstance(this).bindBitmap("",
                R.drawable.default_user_icon, (ImageView) findViewById(R.id.ivCampaignImage), new ImageLoader.BindStrategy() {
                    @Override
                    public void bindBitmapToTarget(ImageView imageView, Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);

                        int ivWidth = getResources().getDisplayMetrics().widthPixels;
                        int ivHeight = imageHeight;
                        int bWidth = bitmap.getWidth();
                        int bHeight = bitmap.getHeight();
                        float ratioWidth = 1f * ivWidth / bWidth;
                        float ratioHeight = 1f * ivHeight / bHeight;
                        float ratioClipHeight = 1f * titleLayoutHeight / ivHeight;

                        Bitmap titleBitmap;
                        int x, y, width, height;
                        if (ratioWidth < ratioHeight) {
                            width = (int) (ivWidth / ratioHeight);
                            x = (bWidth - width) / 2;
                            height = (int) (bHeight * ratioClipHeight);
                            y = bHeight - height;
                        } else {
                            width = bWidth;
                            x = 0;
                            height = (int) (ivHeight / ratioWidth * ratioClipHeight);
                            y = (int) ((bHeight + ivHeight / ratioWidth) / 2) - height;
                        }

                        titleBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
                        titleBitmap = ImageUtil.createDarkBitmap(titleBitmap);
                        titleBackground.setImageBitmap(titleBitmap);
                    }
                });
        overScrollView = (OverScrollView) findViewById(R.id.overScrollView);

        scrollStrategy1 = new OverScrollView.OverScrollStrategy() {
            @Override
            public int onOverScrollTop(int y, int dy) {
                int scrollY = Math.min(y, imageHeight - imageScrollHeight);
                imageScrollView.scrollTo(0, (imageHeight - imageScrollHeight - scrollY) / 2);
                scrollLayoutParams.height = imageScrollHeight + scrollY;
                imageScrollView.requestLayout();
                return scrollY;
            }

            @Override
            public void onReleaseTop(int y) {
                ValueAnimator animator = ValueAnimator.ofInt(y, 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollY = (int) animation.getAnimatedValue();
                        imageScrollView.scrollTo(0, (imageHeight - imageScrollHeight - scrollY) / 2);
                        scrollLayoutParams.height = imageScrollHeight + scrollY;
                        imageScrollView.requestLayout();
                    }
                });
                animator.setDuration(150);
                animator.start();
            }
        };
        scrollStrategy2 = new OverScrollView.OverScrollStrategy() {
            @Override
            public int onOverScrollTop(int y, int dy) {
                int scrollY = Math.min(y, 200);
                overScrollView.getChildAt(0).scrollTo(0, -scrollY);
                return scrollY;
            }

            @Override
            public void onReleaseTop(int y) {
                ValueAnimator animator = ValueAnimator.ofInt(y, 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollY = (int) animation.getAnimatedValue();
                        overScrollView.getChildAt(0).scrollTo(0, -scrollY);
                    }
                });
                animator.setDuration(150);
                animator.start();
            }
        };
        scrollStrategy3 = new OverScrollView.OverScrollStrategy() {
            @Override
            public int onOverScrollTop(int y, int dy) {
                int scrollY = Math.min(y, 500);
                float scaleY = 0.15f * scrollY / 500  + 1;
                View view = overScrollView.getChildAt(0);
                view.setPivotY(0);
                view.setScaleY(scaleY);
                view.invalidate();
                return scrollY;
            }

            @Override
            public void onReleaseTop(int y) {
                ValueAnimator animator = ValueAnimator.ofInt(y, 0);
                animator.setInterpolator(new OvershootInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollY = (int) animation.getAnimatedValue();
                        float scaleY = 0.15f * scrollY / 500  + 1;
                        View view = overScrollView.getChildAt(0);
                        view.setPivotY(0);
                        view.setScaleY(scaleY);
                        view.invalidate();
                    }
                });
                animator.setDuration(150);
                animator.start();
            }
        };
        overScrollView.setScrollStrategy(scrollStrategy1);
        overScrollView.setOnScrollChangeListener(new OverScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(int scrollX, int scrollY, int oldX, int oldY) {
                float alpha = 1.0f * scrollY / (imageScrollHeight - titleLayoutHeight);
                alpha = Math.min(alpha, 1);
                titleBackground.setAlpha(alpha);
            }
        });
    }

    int imageHeight;
    int imageScrollHeight;
    int titleLayoutHeight;

    private void configureImageScroll() {
        imageScrollView = (LinearLayout) findViewById(R.id.imageScrollView);

        imageHeight = getResources().getDisplayMetrics().widthPixels;
        imageScrollHeight = (int) (imageHeight * 0.8f);

        findViewById(R.id.ivCampaignImage).setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, imageHeight));
        scrollLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, imageScrollHeight);
        imageScrollView.setLayoutParams(scrollLayoutParams);

        final ViewTreeObserver observer = imageScrollView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                imageScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                imageScrollView.scrollTo(0, (imageHeight - imageScrollHeight) / 2);
            }
        });
    }

    private void configureTitleLayout() {
        int statusBarHeight = getStatusBarHeight();
        int titleBarHeight = (int) (50 * getResources().getDisplayMetrics().density);
        titleLayoutHeight = statusBarHeight + titleBarHeight;

        findViewById(R.id.statusBar).setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight));
        findViewById(R.id.titleBar).setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, titleBarHeight));

        titleBackground = (ImageView) findViewById(R.id.titleBackground);
        titleBackground.setLayoutParams(
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, titleLayoutHeight));
        titleBackground.setBackgroundColor(0xff404040);
        titleBackground.setAlpha(0f);
    }

    private String getInterval(String campaignStartTime, String campaignEndTime) {
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatOut = new SimpleDateFormat("yyyy.MM.dd");

        try {
            Date startDatetime = formatIn.parse(campaignStartTime);
            Date endDatetime = formatIn.parse(campaignEndTime);
            String startDate = formatOut.format(startDatetime);
            String endDate = formatOut.format(endDatetime);
            if (startDate.substring(0, 3).contentEquals(endDate.substring(0, 3)))
                return startDate.concat("-").concat(endDate.substring(5));
            else
                return startDate.concat("-").concat(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btnPrevious) {
            finish();
        } else if (viewId == R.id.btnSetting) {
            List<String> list = new ArrayList<>();
            list.add("自定义模式");
            list.add("Over-Scroll");
            list.add("Over-Stretch");

            final ListPopupWindow popupWindow = new ListPopupWindow(this);
            popupWindow.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));
            popupWindow.setWidth(400);
            popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnchorView(view);
            popupWindow.setModal(true);
            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            overScrollView.setScrollStrategy(scrollStrategy1);
                            break;
                        case 1:
                            overScrollView.setScrollStrategy(scrollStrategy2);
                            break;
                        case 2:
                            overScrollView.setScrollStrategy(scrollStrategy3);
                            break;
                    }
                    popupWindow.dismiss();
                }
            });
            popupWindow.show();
        }
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }

    public void setStatusBarAlpha() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        //使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
        tintManager.setStatusBarTintResource(R.color.colorAlpha);

    }

    //    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
