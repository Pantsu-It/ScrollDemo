package pantsu.scrolldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import pantsu.scrolldemo.scroll_1.Activity_1;
import pantsu.scrolldemo.scroll_2.Activity_2;

public class MainActivity extends AppCompatActivity {

    public static DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        displayMetrics = getMetrics(this);
    }

    private DisplayMetrics getMetrics(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.btn1:
                intent = new Intent(MainActivity.this, Activity_1.class);
                break;
            case R.id.btn2:
                intent = new Intent(MainActivity.this, Activity_2.class);
                break;
        }
        startActivity(intent);
    }

}
