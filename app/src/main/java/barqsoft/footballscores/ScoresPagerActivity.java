package barqsoft.footballscores;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.sync.ScoresSyncAdapter;

/**
 * Created by dev on 1/3/16.
 *
 *  portions from chapter 11 of Android Progamming: the Big Nerd Ranch Guide
 */
public class ScoresPagerActivity extends FragmentActivity {
    private static final String LOG_TAG = ScoresPagerActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private ArrayList<String> mDays;  // Strings used to generate the day page view.

    private static final int NUM_PAGES = 5;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScoresSyncAdapter.initializeSyncAdapter(this);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        // generate day - dates to reference for MainScreenFragments
        getListOfDaysToDisplay();

        FragmentManager fm = getSupportFragmentManager();
        MyPageAdapter myPageAdapter = new MyPageAdapter(fm);


        mViewPager.setAdapter(myPageAdapter);



    }











    // This method generates a list of the days used for referencing the data stored in the Cursor.
    // the strings generated match the date string stored in the cursor.
    private void getListOfDaysToDisplay() {


        for (int i = 0; i < NUM_PAGES; i++) {
            Date fragmentDate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String tmpDate = dateFormat.format(fragmentDate);
            Log.v(LOG_TAG, "date: " + tmpDate);

            // add date String entry to mDays list;
            mDays.add(tmpDate);

        }

        Log.v(LOG_TAG, "mDays count is " + mDays.size());


    }


    private class MyPageAdapter extends FragmentStatePagerAdapter {
        @Override
        public Fragment getItem(int i) {
            String targetDay = mDays.get(i);
            return MainScreenFragment.newInstance(targetDay);
        }

        @Override
        public int getCount() {
            return mDays.size();
        }

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }



        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getDayName(getApplicationContext(), System.currentTimeMillis() + ((position - 2) * 86400000));
        }



        public String getDayName(Context context, long dateInMillis) {
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.

            Time t = new Time();
            t.setToNow();

            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);

            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);

            } else if (julianDay == currentJulianDay + 1) {
                return context.getString(R.string.tomorrow);

            } else if (julianDay == currentJulianDay - 1) {
                return context.getString(R.string.yesterday);

            } else {
                Time time = new Time();
                time.setToNow();

                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);

            }
        }




    }



}
