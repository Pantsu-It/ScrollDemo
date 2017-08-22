package pantsu.scrolldemo.scroll_4;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;
import pantsu.scrolldemo.R;

/**
 * Created by Pantsu on 2016/8/21.
 * <p>
 * Blog -> http://pantsu-it.github.io/
 */
public class Activity_4 extends Activity implements IScrollListener {

    ScrollControlListView listView;
    SimpleDataAdapter holderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_4);
        configureLayout();
    }

    private void configureLayout() {
        listView = (ScrollControlListView) findViewById(R.id.listView);
        holderAdapter = new SimpleDataAdapter(this);
        holderAdapter.setIScrollListener(this);
        listView.setAdapter(holderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Activity_4.this, "Content click", Toast.LENGTH_SHORT).show();
                Log.d("foobar", "Content click");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ScrollControlListView listView = (ScrollControlListView) parent;
                if (listView.checkLongClickValid()) {
                    Toast.makeText(Activity_4.this, "Content long-click", Toast.LENGTH_SHORT).show();
                    Log.d("foobar", "Content long-click");
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void resetScrollState() {
        listView.clearScrollItem();
    }
}
