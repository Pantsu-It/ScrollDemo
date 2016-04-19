package pantsu.scrolldemo.scroll_5;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import pantsu.scrolldemo.R;

/**
 * Created by Pantsu on 2016/3/22.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_5 extends Activity {

    MyRecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter<SimpleAdapter.ViewHolder> holderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_5);
        configureLayout();
    }

    private void configureLayout() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (MyRecyclerView) findViewById(R.id.recycleView);
        holderAdapter = new SimpleAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(holderAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setHasFixedSize(true);

        recyclerView.setOnSlideOutListener(new MyRecyclerView.OnSlideOutListener() {
            @Override
            public void OnSlideOut(View changedView) {
                SimpleAdapter adapter = (SimpleAdapter) recyclerView.getAdapter();
                int position = adapter.getPosition(changedView);
                if (position != -1) {
                    adapter.notifyItemRemoved(position);
                    adapter.remove(position);
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                }
            }
        });


    }
}
