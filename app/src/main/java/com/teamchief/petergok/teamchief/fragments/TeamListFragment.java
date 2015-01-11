package com.teamchief.petergok.teamchief.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ListView;
import android.view.View;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.LoginActivity;
import com.teamchief.petergok.teamchief.activities.TeamListActivity;
import com.teamchief.petergok.teamchief.activities.TeamViewActivity;
import com.teamchief.petergok.teamchief.adapters.TeamListAdapter;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;


public class TeamListFragment extends ListFragment {
    private ActivityDelegate mDelegate;
    private TeamListActivity mActivity;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (TeamListActivity)getActivity();
        mDelegate = mActivity.getDelegate();
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_team_list, container, false);

        TeamListAdapter adapter = new TeamListAdapter(mActivity, teamName, fakeImageIds);
        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String selectedItem = (String)getListAdapter().getItem(position);

        Toast.makeText(mActivity, selectedItem, Toast.LENGTH_SHORT).show();
    }
}
