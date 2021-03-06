package barqsoft.footballscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import barqsoft.footballscores.sync.ScoresSyncAdapter;





public class MainActivity extends ActionBarActivity  {

//    ERROR - (or preference) relocated to ScoresAdapter and PagerFragment
//    public static int selected_match_id;
//    public static int current_fragment = 2;
    public static String LOG_TAG = "MainActivity";

//    Unnecessary
//    private final String SAVE_TAG = "Save Test";
//    private PagerFragment my_main;

//    ERROR - (or preference) ViewPager handles most position preferences and created others to replace these
//    private static final String PAGER_CURRENT = "Pager_Current";
//    private static final String SELECTED_MATCH = "Selected_match";
//    private static final String PAGERFRAGMENT_MAIN = "my_main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Reached MainActivity onCreate");


        if (savedInstanceState == null) {
             PagerFragment pagerFragment = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, pagerFragment)
                    .commit();
        }


        // initialize the syncadapter which will obtain the data from the Football API
        ScoresSyncAdapter.initializeSyncAdapter(this);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;


        // refesh data from web
        } else if (id == R.id.action_refresh) {

            ScoresSyncAdapter.syncImmediately(this);
        }

        return super.onOptionsItemSelected(item);
    }


//    ERROR -  attempting to save data or something. Replaced with ViewPager and other SharedPreference
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        Log.v(SAVE_TAG, "will save");
//        Log.v(SAVE_TAG, "fragment: " + String.valueOf(my_main.mPagerHandler.getCurrentItem()));
//        Log.v(SAVE_TAG, "selected id: " + selected_match_id);
//
//        outState.putInt(PAGER_CURRENT, my_main.mPagerHandler.getCurrentItem());
//        outState.putInt(SELECTED_MATCH, selected_match_id);
//        getSupportFragmentManager().putFragment(outState, PAGERFRAGMENT_MAIN, my_main);
//
//        super.onSaveInstanceState(outState);
//    }
//
    //    ERROR -  attempting to save data or something. Replaced with ViewPager and other SharedPreference
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.v(SAVE_TAG, "will retrive");
//        Log.v(SAVE_TAG, "fragment: " + String.valueOf(savedInstanceState.getInt(PAGER_CURRENT)));
//        Log.v(SAVE_TAG, "selected id: " + savedInstanceState.getInt(SELECTED_MATCH));
//
//        current_fragment = savedInstanceState.getInt(PAGER_CURRENT);
//        selected_match_id = savedInstanceState.getInt(SELECTED_MATCH);
//        my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, PAGERFRAGMENT_MAIN);
//
//
//        super.onRestoreInstanceState(savedInstanceState);
//    }
//





}
