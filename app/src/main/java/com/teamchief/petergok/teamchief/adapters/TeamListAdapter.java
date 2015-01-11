package com.teamchief.petergok.teamchief.adapters;

/**
 * Created by KevinMichael on 2015-01-10.
 */

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamchief.petergok.teamchief.R;

public class TeamListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] teamName;
    private final Integer[] imgid;
    //private TextView mTeamNameView, mRecentMessageView, mTimestampView;
    private Typeface latoBlackTypeFace, regularLatoTypeFace;

    public TeamListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.team_row, itemname);
        // TODO Auto-generated constructor stub

        //Get Lato Fonts
        latoBlackTypeFace= Typeface.createFromAsset(context.getAssets(), "fonts/lato-black.ttf");
        regularLatoTypeFace=Typeface.createFromAsset(context.getAssets(),"fonts/lato-regular.ttf");

        /*//Get Text fields requiring font changes
        mTeamNameView =(TextView)mContext.findViewById(R.id.team_name);
        mRecentMessageView =(TextView)mContext.findViewById(R.id.recent_message);
        mTimestampView =(TextView) mContext.findViewById(R.id.timestamp_message);

        mTeamNameView.setTypeface(latoBlackTypeFace);
        mRecentMessageView.setTypeface(regularLatoTypeFace);
        mTimestampView.setTypeface(regularLatoTypeFace);*/

        this.context=context;
        this.teamName=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.team_row, null,true);
        TextView mTeamNameView = (TextView) rowView.findViewById(R.id.team_name);
        mTeamNameView.setTypeface(latoBlackTypeFace);
        mTeamNameView.setText(teamName[position]);
        return rowView;

    };

}