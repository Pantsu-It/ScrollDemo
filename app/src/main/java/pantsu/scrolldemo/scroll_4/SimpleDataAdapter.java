package pantsu.scrolldemo.scroll_4;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import pantsu.scrolldemo.MainActivity;
import pantsu.scrolldemo.R;

public class SimpleDataAdapter extends BaseAdapter {

    private List<DataItem> dataItemList;
    private Context context;

    {
        boolean[] op0 = {false, false, false};
        boolean[] op1 = {true, false, false};
        boolean[] op2 = {true, true, false};
        boolean[] op3 = {true, true, true};
        dataItemList = new ArrayList<>(4);
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op0));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op1));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op2));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op3));
    }

    public SimpleDataAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataItemList.size();
    }

    @Override
    public DataItem getItem(int position) {
        return dataItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_cell_4, null);
        }
        rootView = convertView;
        rootView.setTag(R.id.tag_type, "item");
        rootView.setTag(R.id.tag_mark, position);

        DataItem dataItem = getItem(position);
        bindView(dataItem, rootView);
        return rootView;
    }

    private void bindView(DataItem dataItem, View view) {
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView opTop = (TextView) view.findViewById(R.id.opTop);
        TextView opRead = (TextView) view.findViewById(R.id.opRead);
        TextView opDelete = (TextView) view.findViewById(R.id.opDelete);

        content.setText(dataItem.content);
        opTop.setVisibility(dataItem.operations[0] ? View.VISIBLE : View.GONE);
        opRead.setVisibility(dataItem.operations[1] ? View.VISIBLE : View.GONE);
        opDelete.setVisibility(dataItem.operations[2] ? View.VISIBLE : View.GONE);

        int displayWidth = MainActivity.displayMetrics.widthPixels;
        float density = MainActivity.displayMetrics.density;
        int cellHeight = (int) (72 * density);

//                view.setLayoutParams(getLayoutParams(view, LinearLayout.LayoutParams.WRAP_CONTENT, cellHeight));
        view.setLayoutParams(getLayoutParams(view, (int) (displayWidth * 2), cellHeight));
        content.setLayoutParams(getLayoutParams(content, displayWidth, cellHeight));
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

    private static class DataItem {
        String content;
        boolean[] operations;

        DataItem(String content, boolean[] operations) {
            this.content = content;
            this.operations = operations;
        }

    }

}