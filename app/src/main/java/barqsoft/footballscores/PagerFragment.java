package barqsoft.footballscores;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yehya khaled on 2/27/2015.
 */
public class PagerFragment extends Fragment {

    private static final String LOG_TAG = PagerFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private ArrayList<String> mDays;  // Strings used to generate the day page view.

    private static final int NUM_PAGES = 5;
    private static final int START_DAY = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pager_fragment);

//        ScoresSyncAdapter.initializeSyncAdapter(this);
//
//        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
//        mPagerHandler = (ViewPager) rootView.findViewById(R.id.pager);

//        mViewPager = new ViewPager(this);
//        mViewPager.setId(R.id.viewPager);

//        mViewPager = new ViewPager(this);
//        mViewPager.setId(R.id.pager);
//        setContentView(mViewPager);

        // generate day - dates to reference for MainScreenFragments
//        mDays = getListOfDaysToDisplay();

//        FragmentManager fm = getSupportFragmentManager();
//        MyPageAdapter myPageAdapter = new MyPageAdapter(fm);
//
//
//        mViewPager.setAdapter(myPageAdapter);
//        mViewPager.setCurrentItem(START_DAY);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

        mDays = getListOfDaysToDisplay();

        FragmentManager fm = getChildFragmentManager();
        MyPageAdapter myPageAdapter = new MyPageAdapter(fm);


        mViewPager.setAdapter(myPageAdapter);
        mViewPager.setCurrentItem(START_DAY);

        return rootView;
    }





    // This method generates a list of the days used for referencing the data stored in the Cursor.
    // the strings generated match the date string stored in the cursor.
    private ArrayList<String> getListOfDaysToDisplay() {
        ArrayList<String> tmpList = new ArrayList<>();

        for (int i = 0; i < NUM_PAGES; i++) {
            Date fragmentDate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String tmpDate = dateFormat.format(fragmentDate);
            Log.v(LOG_TAG, "date: " + tmpDate);

            // add date String entry to mDays list;
            tmpList.add(tmpDate);

        }

        Log.v(LOG_TAG, "mDays count is " + tmpList.size());

        return tmpList;
    }


    private class MyPageAdapter extends FragmentStatePagerAdapter {


        public MyPageAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Log.v(LOG_TAG, "MyPageAdapter: position " + i);
            String targetDay = mDays.get(i);
            MainScreenFragment newFragment = MainScreenFragment.newInstance(targetDay);
            Log.v(LOG_TAG, "newFragment pageTitle is set to " + newFragment.getFragmentDate());

            return newFragment;
        }



        @Override
        public int getCount() {
            return mDays.size();
        }



        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getDayName(getActivity(), System.currentTimeMillis() + ((position - 2) * 86400000));
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
