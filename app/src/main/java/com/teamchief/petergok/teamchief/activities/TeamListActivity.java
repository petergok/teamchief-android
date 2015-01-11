package com.teamchief.petergok.teamchief.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;


public class TeamListActivity extends ListActivity {
    private ActivityDelegate mDelegate = new ActivityDelegate(this);

    String[] itemname ={
            "SYDE 162 Design Group",
            "JY Waterloo",
            "Music Ministry",
            "NYT",
            "OSTA-AECO",
            "MYAC",
            "Farmsoc"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        this.setListAdapter(new ArrayAdapter<String>(
                this, R.layout.team_row,
                R.id.team_name,itemname));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
