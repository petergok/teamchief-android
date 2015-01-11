package com.teamchief.petergok.teamchief.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ListView;
import android.view.View;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.adapters.TeamListAdapter;


public class TeamListActivity extends ListActivity {

    Integer[] fakeImageIds = {
            1,1,3,4,5,7,8
    };


    String[] teamName ={
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
        setContentView(R.layout.activity_team_list);



        TeamListAdapter adapter=new TeamListAdapter(this, teamName, fakeImageIds);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView lv ,View view,int position,int imgid) {

        String Selecteditem= (String)getListAdapter().getItem(position);
        Toast.makeText(this, Selecteditem, Toast.LENGTH_SHORT).show();
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
