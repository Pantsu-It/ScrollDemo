package pantsu.scrolldemo.scroll_4;

import android.app.Activity;
import android.os.Bundle;
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
public class Activity_4 extends Activity {

    ScrollControlListView listView;
    ListAdapter holderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_4);
        configureLayout();
    }

    private void configureLayout() {
        listView = (ScrollControlListView) findViewById(R.id.listView);
        holderAdapter = new SimpleDataAdapter(this);
        listView.setAdapter(holderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Activity_4.this, "Content click", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Activity_4.this, "Content long-click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
