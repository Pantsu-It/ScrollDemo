package pantsu.scrolldemo.scroll_4;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListAdapter;
import pantsu.scrolldemo.R;
import pantsu.scrolldemo.scroll_5.MyRecyclerView;
import pantsu.scrolldemo.scroll_5.SimpleAdapter;
import pantsu.scrolldemo.scroll_5.SpaceItemDecoration;

/**
 * Created by Pantsu on 2016/3/22.
 * <p/>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_4 extends Activity {

    ScrollControllListView recyclerView;
    ListAdapter holderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_4);
        configureLayout();
    }

    private void configureLayout() {
        recyclerView = (ScrollControllListView) findViewById(R.id.listView);
        holderAdapter = new SimpleDataAdapter(this);
        recyclerView.setAdapter(holderAdapter);
    }
}
