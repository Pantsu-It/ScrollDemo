package pantsu.scrolldemo.scroll_4;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import pantsu.scrolldemo.MainActivity;
import pantsu.scrolldemo.R;

public class SimpleDataAdapter extends BaseAdapter {

    private List<DataItem> dataItemList;
    private Context context;
    private IScrollListener mIScrollListener;

    {
        boolean[] op1 = {true, true, false};
        boolean[] op2 = {true, true, false};
        boolean[] op3 = {true, true, true};
        boolean[] op4 = {true, true, true};
        boolean[] op5 = {true, true, false};
        boolean[] op6 = {true, true, false};
        boolean[] op7 = {true, true, true};
        boolean[] op8 = {true, true, true};
        boolean[] op9 = {true, true, false};
        boolean[] op10 = {true, true, false};
        boolean[] op11 = {true, true, true};
        boolean[] op12 = {true, true, true};
        boolean[] op13 = {true, true, true};
        dataItemList = new ArrayList<>(4);
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op1));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op2));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op3));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op4));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op5));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op6));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op7));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op8));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op9));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op10));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op11));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op12));
        dataItemList.add(new DataItem("我我我我我我我我我我问问我我", op13));
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
        int displayWidth = MainActivity.displayMetrics.widthPixels;
        float density = MainActivity.displayMetrics.density;
        int cellHeight = (int) (72 * density);

        IScrollView iScrollView;
        if (convertView == null) {
            iScrollView = new IScrollView(context, displayWidth, cellHeight);
            iScrollView.setContentView(View.inflate(context, R.layout.item_cell_4, null));
            iScrollView.setOptionView(View.inflate(context, R.layout.item_cell_4_option, null));
        } else {
            iScrollView = (IScrollView) convertView;
        }
        iScrollView.setTag(R.id.tag_mark, position);

        DataItem dataItem = getItem(position);
        bindView(dataItem, iScrollView);
        return iScrollView;
    }

    private void bindView(DataItem dataItem, ViewGroup view) {
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView opTop = (TextView) view.findViewById(R.id.opTop);
        TextView opRead = (TextView) view.findViewById(R.id.opRead);
        TextView opDelete = (TextView) view.findViewById(R.id.opDelete);

        content.setText(dataItem.content);
        opTop.setVisibility(dataItem.operations[0] ? View.VISIBLE : View.GONE);
        opRead.setVisibility(dataItem.operations[1] ? View.VISIBLE : View.GONE);
        opDelete.setVisibility(dataItem.operations[2] ? View.VISIBLE : View.GONE);
        opTop.setOnClickListener(onClickListener);
        opRead.setOnClickListener(onClickListener);
        opDelete.setOnClickListener(onClickListener);
    }

    private static class DataItem {
        String content;
        boolean[] operations;

        DataItem(String content, boolean[] operations) {
            this.content = content;
            this.operations = operations;
        }

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.opTop:
                    Toast.makeText(context, "Top", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.opRead:
                    Toast.makeText(context, "Read", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.opDelete:
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                    break;

            }
            mIScrollListener.resetScrollState();
        }
    };

   public void setIScrollListener(IScrollListener iScrollListener) {
       mIScrollListener = iScrollListener;
   }

}