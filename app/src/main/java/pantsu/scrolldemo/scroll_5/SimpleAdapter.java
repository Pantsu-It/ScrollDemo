package pantsu.scrolldemo.scroll_5;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pantsu.scrolldemo.R;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private List<Map<Integer, String>> dataList;

    private Resources res;

    public SimpleAdapter(Context context) {
        res = context.getResources();
        String[] title = res.getStringArray(R.array.title);
        String[] hot_reply = res.getStringArray(R.array.hot_reply);

        TypedArray typedArray = res.obtainTypedArray(R.array.hot_icon);
        int[] hot_icon = new int[title.length];
        int titleLength = title.length;
        for (int index = 0; index < titleLength; index++) {
            hot_icon[index] = typedArray.getResourceId(index, 0);
        }

        dataList = new ArrayList<>();
        for (int i = 0; i < titleLength; i++) {
            Map<Integer, String> map = new HashMap<>();
            map.put(0, String.valueOf(i));
            map.put(1, title[i]);
            map.put(2, String.valueOf(hot_icon[i]));
            map.put(3, hot_reply[i]);
            dataList.add(map);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void remove(int position) {
        dataList.remove(position);
    }

    public int getPosition(View changedView) {
        int position = (int) changedView.getTag(R.id.tag_position);
        int count = 0;
        for (Map<Integer, String> map : dataList) {
            if (Integer.parseInt(map.get(0)) == position)
                return count;
            else
                count++;
        }
        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView hot_icon;
        private TextView hot_reply;
        private int position;

        public ViewHolder(LinearLayout layout) {
            super(layout);

            this.hot_icon = (ImageView) layout.findViewById(R.id.hot_icon);
            this.title = (TextView) layout.findViewById(R.id.title);
            this.hot_reply = (TextView) layout.findViewById(R.id.hot_reply);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cell_2, null);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<Integer, String> map = dataList.get(position);
        holder.position = Integer.parseInt(map.get(0));
        holder.title.setText(map.get(1));
        holder.hot_icon.setImageDrawable(getRoundBitmap(Integer.parseInt(map.get(2))));
        holder.hot_reply.setText(map.get(3));

        holder.itemView.setTag(R.id.tag_type, "item");
        holder.itemView.setTag(R.id.tag_position, holder.position);
    }

    private Drawable getRoundBitmap(int imageId) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, imageId);
        RoundedBitmapDrawable roundBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
        roundBitmapDrawable.setCornerRadius(bitmap.getWidth()/2);
        return roundBitmapDrawable;
    }

}