package com.teamchief.petergok.teamchief.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.ZoomOutPageTransformer;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;
import com.teamchief.petergok.teamchief.fragments.ConversationPageFragment;
import com.teamchief.petergok.teamchief.fragments.EmptyFragment;

/**
 * Created by Peter on 2015-01-10.
 */
public class TeamViewActivity extends ActionBarActivity {

    private ActivityDelegate mDelegate = new ActivityDelegate(this);

    private final int highlightColor = Color.DKGRAY;
    private final int defaultColor = Color.WHITE;

    private String mTeamId;

    private ImageView[] icons = new ImageView[NUM_PAGES];

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

        mTeamId = getIntent().getStringExtra("teamId");

        setContentView(R.layout.activity_team_view);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        icons[0] = (ImageView) findViewById(R.id.conversationIcon);
        icons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        icons[1] = (ImageView) findViewById(R.id.icon2);
        icons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
            }
        });
        icons[2] = (ImageView) findViewById(R.id.icon3);
        icons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(2);
            }
        });
        icons[3] = (ImageView) findViewById(R.id.icon4);
        icons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(3);
            }
        });
        icons[4] = (ImageView) findViewById(R.id.icon5);
        icons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(4);
            }
        });

        selectIcon(0);

        mDelegate.getNewMessages(mTeamId);
    }

    public void selectIcon(int position) {
        for (int icon = 0; icon < NUM_PAGES; icon++) {
            if (icon == position) {
                icons[icon].setBackgroundColor(Color.parseColor("#007AC1"));
                icons[icon].setColorFilter(defaultColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                icons[icon].setBackgroundColor(0x00000000);
                icons[icon].setColorFilter(defaultColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    protected void onDestroy () {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    public ActivityDelegate getDelegate() {
        return mDelegate;
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
