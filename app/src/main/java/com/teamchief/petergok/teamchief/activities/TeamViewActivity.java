package com.teamchief.petergok.teamchief.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.ZoomOutPageTransformer;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;
import com.teamchief.petergok.teamchief.fragments.ConversationPageFragment;
import com.teamchief.petergok.teamchief.fragments.EmptyFragment;

/**
 * Created by Peter on 2015-01-10.
 */
public class TeamViewActivity extends Activity {

    private ActivityDelegate mDelegate = new ActivityDelegate(this);

    private String mTeamId = "54b1af3088a8f50200000004";

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team_view);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mDelegate.getNewMessages(mTeamId);
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

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("teamId", mTeamId);

                Fragment frag = new ConversationPageFragment();
                frag.setArguments(bundle);

                return frag;
            } else {
                return new EmptyFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
