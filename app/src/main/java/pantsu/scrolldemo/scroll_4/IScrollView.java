package pantsu.scrolldemo.scroll_4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import pantsu.scrolldemo.R;

/**
 * Created by pangc on 2017/8/22.
 */
public class IScrollView extends LinearLayout {

    private View mContentView;
    private View mOptionView;

    private int mContentWidth, mHeight;

    public IScrollView(Context context, int contentWidth, int height) {
        this(context, null);
        mContentWidth = contentWidth;
        mHeight = height;
        setTag(R.id.tag_type, "scroll");

        // TODO: 2017/8/22 此处width为什么不能用wrap_content
        setLayoutParams(getLayoutParams(this, contentWidth * 2, height));
    }

    public IScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setContentView(View contentView) {
        if (mContentView != null) {
            removeView(mContentView);
        }
        mContentView = contentView;

        contentView.setLayoutParams(getLayoutParams(contentView, mContentWidth, mHeight));
        addView(contentView, 0);
    }

    public void setOptionView(View optionView) {
        if (mOptionView != null) {
            removeView(mOptionView);
        }
        mOptionView = optionView;

        optionView.setLayoutParams(getLayoutParams(optionView, LayoutParams.WRAP_CONTENT, mHeight));
        addView(optionView, 1);
    }

    public View getContentView() {
        return mContentView;
    }

    public View getOptionView() {
        return mOptionView;
    }

    private LinearLayout.LayoutParams getLayoutParams(View view, int width, int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (params == null) {
            params = new LinearLayout.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        return params;
    }

}
