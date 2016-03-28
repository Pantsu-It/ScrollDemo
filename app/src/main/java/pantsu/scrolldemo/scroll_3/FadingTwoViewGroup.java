package pantsu.scrolldemo.scroll_3;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Pantsu on 2016/3/26.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */


public class FadingTwoViewGroup extends RelativeLayout {
    public FadingTwoViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Set how dividers should be shown between items in this layout
     *
     * @param rate ranges at 0.0f~1.0f,
     *             at 0.0f the first child setAlpha(1)
     *             at 0.5f both child setAlpha(0)
     *             at 1.0f the second child setAlpha(1)
     */
    public void setFadingRate(float rate) {
        int childCount = getChildCount();

        if (childCount >= 1) {
            float alpha0 = Math.max(0, 1 - 2 * rate);
            getChildAt(0).setAlpha(alpha0);
        }
        if (childCount >= 2) {
            float alpha1 = Math.max(0, 2* rate-1);
            getChildAt(1).setAlpha(alpha1);
        }
    }
}
