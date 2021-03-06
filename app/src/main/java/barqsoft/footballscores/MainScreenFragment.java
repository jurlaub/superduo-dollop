package barqsoft.footballscores;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import barqsoft.footballscores.data.DatabaseContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = MainScreenFragment.class.getSimpleName();
    public static final String EXTRA_SCORES_DATE = "mainscreenfragment.TARGETDATE";

    public ScoresAdapter mAdapter;

    public static final int SCORES_LOADER = 0;
//

    public static final int DEFAULT_DETAIL_VIEW = -1;


    // Removed from being a List, apparently set up this way to facilitate the CursorLoader.
    private String mFragmentDate;
    private String mPageTitle;
    private View mRootView;
    private ListView mScoresList;

    private int last_selected_item = -1;

    // -----  SQLite Query Columns -------------------
    private static final String[] MATCHDETAILS_COLUMNS = {
            DatabaseContract.ScoresTable._ID,
            DatabaseContract.ScoresTable.DATE_COL,
            DatabaseContract.ScoresTable.TIME_COL,
            DatabaseContract.ScoresTable.HOME_COL,
            DatabaseContract.ScoresTable.AWAY_COL,
            DatabaseContract.ScoresTable.LEAGUE_COL,
            DatabaseContract.ScoresTable.HOME_GOALS_COL,
            DatabaseContract.ScoresTable.AWAY_GOALS_COL,
            DatabaseContract.ScoresTable.MATCH_ID,
            DatabaseContract.ScoresTable.MATCH_DAY

    };

    // ---!!--- must change if MATCHDETAILS_COLUMNS changes -----!!---
    public static final int _ID = 0;
    public static final int COL_DATE = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_LEAGUE = 5;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_MATCH_ID = 8;                 //Match ID
    public static final int COL_MATCHDAY = 9;

    private static final String SCORES_BY_DATE =
            DatabaseContract.ScoresTable.DATE_COL + " Like ?";



    // used by ScoresPagerActivity to generate a new fragment.
    public static MainScreenFragment newInstance(String targetDate) {
        Log.v(LOG_TAG, "newInstance with a targetDate: " + targetDate );

        Bundle args = new Bundle();
        args.putString(EXTRA_SCORES_DATE, targetDate);

        MainScreenFragment fragment = new MainScreenFragment();
        fragment.setArguments(args);

        return fragment;
    }





    public MainScreenFragment() {
    }

    // ERROR - (at least its a performance hit)
    // called each time the fragment's onCreateView is called - way more then is necessary
//    private void update_scores() {
//        Intent service_start = new Intent(getActivity(), MyFetchService.class);
//        getActivity().startService(service_start);
//        Log.v(LOG_TAG, "update_scores called");
//
//    }

    public void setFragmentDate(String date) {
        mFragmentDate = date;
        Log.v(LOG_TAG, "setFragmentDate: " + date);
    }

    public String getFragmentDate() {
        return mFragmentDate;
    }

    public void setPageTitle(String title) {
        mPageTitle = title;
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String targetDate;

        targetDate = getArguments().getString(EXTRA_SCORES_DATE);

        setFragmentDate(targetDate);
        setPageTitle(Utilies.getDayName(getActivity(), targetDate));


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

//      ERROR Called too many times when placed in this location
//        update_scores();

        Log.v(LOG_TAG, "onCreateView date:" + mFragmentDate);

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        mScoresList = (ListView) mRootView.findViewById(R.id.scores_list);



        // set content description for the page and use the page names.
        String pageCDFormat = getString(R.string.page_day_contentdescription);
        String pageContentDescription = String.format(pageCDFormat, mPageTitle);
        mRootView.setContentDescription(pageContentDescription);
        Log.v(LOG_TAG, "pageContentDescription: " + pageContentDescription);




        mAdapter = new ScoresAdapter(getActivity(), null, 0, mFragmentDate, this);

        // set empty view if no data
        TextView emptyView = (TextView) mRootView.findViewById(R.id.scores_list_empty);
        mScoresList.setEmptyView(emptyView);



        mScoresList.setAdapter(mAdapter);

//  ERROR - was only being called once
// getLoaderManager().initLoader(SCORES_LOADER, null, this);
        Log.v(LOG_TAG, "before mAdapter.mDetail_match_id" + mAdapter.mDetail_match_id);
//  ERROR -
// mAdapter.mDetail_match_id = MainActivity.selected_match_id;


//        Possible ERROR in how this was set up
        mScoresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v(LOG_TAG, "position " + position + " selected");

//                ViewHolder selected = (ViewHolder) view.getTag();
//                Log.v(LOG_TAG, "ViewHolder tag: " + selected.match_id);

//                mAdapter.mDetail_match_id = selected.match_id;

                // onClick changes mDetail_match_id. This instructs ScoresAdapter to toggle between
                // the two views.
                if (mAdapter.mSelectView == position) {
                    mAdapter.mSelectView = -1;
                    Log.v(LOG_TAG, "mAdapter.mSelectView should be -1 set to " + mAdapter.mSelectView);

                } else {
                    mAdapter.mSelectView = position;
//                    Log.v(LOG_TAG, "mAdapter.mSelectView should be " + position + " set to " + mAdapter.mSelectView);
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                    Log.v(LOG_TAG, cursor.getString(COL_HOME) + " vs " + cursor.getString(COL_AWAY) );
                }


//
                mAdapter.notifyDataSetChanged();
//                restartLoader();
            }

        });






        return mRootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // changed this from using mFragmentDate to new String[] {mFragmentDate}
        Log.v(LOG_TAG, "onCreateLoader - i:" + i );

        checkFagmentDate("onCreateLoader");

        return new CursorLoader(getActivity(), DatabaseContract.ScoresTable.buildScoreWithDate(),
                MATCHDETAILS_COLUMNS, SCORES_BY_DATE, new String[] {mFragmentDate}, null);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v(LOG_TAG, "onActivityCreated - just before initializing the loaderManager");
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
    }



    private void checkFagmentDate(String g) {
        if (mFragmentDate != null) {
            Log.v(LOG_TAG, "PageTitle: " + mPageTitle + " " + g + " mFragmentDate: " + mFragmentDate);
        } else {
            Log.v(LOG_TAG, "PageTitle: " + mPageTitle + " " +g + " mFragmentDate is null");
        }
    }


    @Override
    public void onResume(){
        super.onResume();

//        checkFagmentDate("onResume");

        // set the SelectView to the saved preference - default is -1
        mAdapter.setmSelectView(Utilies.getElementExpanded(getActivity(), mFragmentDate));

        restartLoader();

    }

    @Override
    public void onPause(){
        super.onPause();


        checkFagmentDate("onPause");
        Log.v(LOG_TAG, "onPause");

        Utilies.setElementExpanded(getActivity(), mAdapter.getmSelectView(), mFragmentDate);


    }



//    ERROR was in this method. The cursor would be direct to skip around unnecessarily.
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //Log.v(FetchScoreTask.LOG_TAG,"loader finished");
        //cursor.moveToFirst();
        /*
        while (!cursor.isAfterLast())
        {
            Log.v(FetchScoreTask.LOG_TAG,cursor.getString(1));
            cursor.moveToNext();
        }
        */
        Log.v(LOG_TAG, "loadFinished. Cursor count: " + cursor.getCount());


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          Log.v("Cursor review", "Position: " + cursor.getPosition() + " " + cursor.getString(COL_HOME) + " vs " + cursor.getString(COL_AWAY));
            cursor.moveToNext();
        }


        cursor.moveToFirst();

        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        mAdapter.swapCursor(cursor);
        //mAdapter.notifyDataSetChanged();



        // added empty view update
        updateEmptyView();

    }


    @Override
    public void onStart(){
        super.onStart();
        Log.v(LOG_TAG, "onStart");

    }





    @Override
    public void onStop(){
        super.onStop();
        Log.v(LOG_TAG, "onStop");

    }


    private void restartLoader() {


        Log.v(LOG_TAG, "restartLoader restarting Loader");
        getLoaderManager().restartLoader(SCORES_LOADER, null, this);

    }





    // Added information for user when day is empty
    // this is the place to dynamically change the empty list text box to display to users.
    private void updateEmptyView(){


        if (mAdapter.getCount() == 0) {
            Log.v(LOG_TAG, "updateEmptyView mAdapter.count = 0");
            TextView emptyView = (TextView) getView().findViewById(R.id.scores_list_empty);

            if (emptyView != null) {

                // this is where logic can be added to decide what information to instruct the user
                // about the app state
                emptyView.setText(getString(R.string.empty_list));
                emptyView.setContentDescription(getString(R.string.empty_list));

                Log.v(LOG_TAG, "emptyView is not null ");
            }

        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


    public void refreshData(){
        mAdapter.notifyDataSetChanged();
    }







}
