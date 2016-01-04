package barqsoft.footballscores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yehya khaled on 2/27/2015.
 *
 * ERROR
 *      mostly rewritten - A good portion of the original was a problem
 *      instead of generating an Array or MainScreenFragments and using that data For the FragmentStatePagerAdapter
 *      changed to
 *      generating an array of date strings that are used to create the MainScreenFragments
 *
 *      without this change, a rotation would cause the app to crash - the MainScreenFragment would
 *      not have the necessary String for the date to pull data from the Database.
 *
 */
public class PagerFragment extends Fragment {

    private static final String LOG_TAG = PagerFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private ArrayList<String> mDays;  // Strings used to generate the day page view.

    private static final int NUM_PAGES = 5;     // total number of days to display
    private static final int START_DAY = 2;     // initial position to show the user in the array of days


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

        // Generate list of days that are maintained in order to create the MainScreenFragments
        mDays = getListOfDaysToDisplay();

        FragmentManager fm = getChildFragmentManager();
        MyPageAdapter myPageAdapter = new MyPageAdapter(fm);


        mViewPager.setAdapter(myPageAdapter);
        mViewPager.setCurrentItem(START_DAY);


        return rootView;
    }



//    @Override
//    public void onPause(){
//        super.onPause();
//
//    }



    // This method generates a list of the days used for referencing the data stored in the Cursor.
    // the strings generated match the date string stored in the cursor.
    private ArrayList<String> getListOfDaysToDisplay() {
        ArrayList<String> tmpList = new ArrayList<>();

        for (int i = 0; i < NUM_PAGES; i++) {
            Date fragmentDate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String tmpDate = dateFormat.format(fragmentDate);
//            Log.v(LOG_TAG, "date: " + tmpDate);

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

        // the item position is used to obtain the date String in the mDays arrayList. This is used to
        // generate a new MainScreenFragment
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
            String tmpDate = mDays.get(position);

            return Utilies.getDayName(getActivity(), tmpDate);

//            REFACTOR - only need to calculate time when the PagerFragment mDays is generated.
//            return getDayName(getActivity(), System.currentTimeMillis() + ((position - 2) * 86400000));
        }



//        REFACTORED - using the new one in Utilies. The new one uses Calendar instead of what may
//                      be or soon to be depreciated classes

//        public String getDayName(Context context, long dateInMillis) {
//            // If the date is today, return the localized version of "Today" instead of the actual
//            // day name.
//
//            Time t = new Time();
//            t.setToNow();
//
//            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
//            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
//
//            if (julianDay == currentJulianDay) {
//                return context.getString(R.string.today);
//
//            } else if (julianDay == currentJulianDay + 1) {
//                return context.getString(R.string.tomorrow);
//
//            } else if (julianDay == currentJulianDay - 1) {
//                return context.getString(R.string.yesterday);
//
//            } else {
//                Time time = new Time();
//                time.setToNow();
//
//                // Otherwise, the format is just the day of the week (e.g "Wednesday".
//                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
//                return dayFormat.format(dateInMillis);
//
//            }
//        }
//
//


    }




}
