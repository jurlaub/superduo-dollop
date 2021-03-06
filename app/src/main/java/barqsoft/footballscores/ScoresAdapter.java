package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter {
    private final String LOG_TAG = ScoresAdapter.class.getSimpleName();


    private static final int VIEW_TYPE_MATCH = 0;
    private static final int VIEW_TYPE_DETAIL = 1;

    // ---!! must change if more views are added !!----
    private static final int VIEW_TYPE_COUNT = 2;

    public static final int EXPANDED_ELEMENT_DEFAULT = -1;

    public int mDetail_match_id = 0;


    public int mSelectView;
    private String mStringDate;


//  Replaced with an entry in the string.xml file
//    private String FOOTBALL_SCORES_HASHTAG = "Football_Scores";

    private Context mContext;
//    private ViewHolder mHolder;
    private MainScreenFragment mMainScreenFragment;



/*
   ERROR  - formerly found in its own public class moved here
            While this may not have been a problem - or there may have been a different way to fix
            the problem, By moving it here and making it a static class removed the problem where:

   ERROR - EVERY 5TH ENTRY Problem
            Every 5th match/entry would start changing as the user scrolled through the list. Its as
            if it was on its own cycle. I beleive the problem was that as the views were recycled the
            ViewHolder were not being recycled at the same time.
    */

    public static class ViewHolder {
        public TextView home_name;
        public TextView away_name;
        public TextView mScore;
        public TextView date;
        public ImageView home_crest;
        public ImageView away_crest;
        public int match_id;

        public ViewHolder(View view) {
            home_name = (TextView) view.findViewById(R.id.home_name);
            away_name = (TextView) view.findViewById(R.id.away_name);
            mScore = (TextView) view.findViewById(R.id.score_textview);
            date = (TextView) view.findViewById(R.id.date_textview);
            home_crest = (ImageView) view.findViewById(R.id.home_crest);
            away_crest = (ImageView) view.findViewById(R.id.away_crest);

//            Log.v("ViewHolder", "view id = " + view.getId() + " Date: " + date);


        }



    }





    public ScoresAdapter(Context context, Cursor cursor, int flags, String stringDate, MainScreenFragment mainScreenFragment) {
        super(context, cursor, flags);
        this.mContext = context;

        this.mStringDate = stringDate;
        this.mMainScreenFragment = mainScreenFragment;


        this.mSelectView = EXPANDED_ELEMENT_DEFAULT;

        Log.v(LOG_TAG, "mSelectView: " + mSelectView);

    }



    @Override
    public int getViewTypeCount(){
        return VIEW_TYPE_COUNT;

    }

    @Override
    public int getItemViewType(int position) {

        // if the detail view assigned in MainScreenFragement matches the position
        // then use the detail view. Otherwise use the match view
        if (position == mSelectView) {

            return VIEW_TYPE_DETAIL;
        }

        return VIEW_TYPE_MATCH;

    }



    public int getmSelectView(){
        return mSelectView;
    }

    public void setmSelectView(int val) {
        mSelectView = val;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutID = -1;
        int viewType = getItemViewType(cursor.getPosition());

        switch (viewType) {

            case VIEW_TYPE_DETAIL:
                layoutID = R.layout.detail_fragment;
//                Log.v(LOG_TAG, "newView - detail_fragment and mSelectView: " + mSelectView);

                break;
            case VIEW_TYPE_MATCH:
                layoutID = R.layout.scores_list_item;
//                Log.v(LOG_TAG, "newView - scores_list_item and mSelectView: " + mSelectView);
                break;
            default:
                Log.e(LOG_TAG, "newView no VIEW_TYPE found, layoutID = " + layoutID);
        }


        View view = LayoutInflater.from(context).inflate(layoutID, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");

        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

//        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int viewType = getItemViewType(cursor.getPosition());
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        // ----------   Common to both views  -----------
//        Log.v(LOG_TAG, "Cursor position: " + cursor.getPosition() + " mSelectView: " + mSelectView);

        //        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.home_name.setText(cursor.getString(MainScreenFragment.COL_HOME));
        viewHolder.away_name.setText(cursor.getString(MainScreenFragment.COL_AWAY));


        // Match Time
        String matchTime = cursor.getString(MainScreenFragment.COL_MATCHTIME);
        String matchTimeformat = mContext.getString(R.string.match_time_contentdescription);

        viewHolder.date.setText(matchTime);
        viewHolder.date.setContentDescription(String.format(matchTimeformat, matchTime));


        // Score
        viewHolder.mScore.setText(Utilies.getScores(mContext, cursor.getInt(MainScreenFragment.COL_HOME_GOALS), cursor.getInt(MainScreenFragment.COL_AWAY_GOALS)));

        // set contextual ContentDescription for the score
        String tmpMessage = Utilies.getScoresForContentDescription(mContext,
                cursor.getString(MainScreenFragment.COL_HOME),
                cursor.getInt(MainScreenFragment.COL_HOME_GOALS),
                cursor.getString(MainScreenFragment.COL_AWAY),
                cursor.getInt(MainScreenFragment.COL_AWAY_GOALS));

        viewHolder.mScore.setContentDescription(tmpMessage);

        // Match ID
        viewHolder.match_id = cursor.getInt(MainScreenFragment.COL_MATCH_ID);


        viewHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(cursor.getString(MainScreenFragment.COL_HOME)));
        viewHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(cursor.getString(MainScreenFragment.COL_AWAY)));







        switch (viewType) {

//            This is used for the detailed (i.e. expanded) view
            case VIEW_TYPE_DETAIL:

                TextView league = (TextView) view.findViewById(R.id.league_textview);
                TextView matchday = (TextView) view.findViewById(R.id.matchday_textview);

                Button shareButton = (Button) view.findViewById(R.id.share_button);
                shareButton.setContentDescription(mContext.getString(R.string.share_action_contentdescription));

                league.setText(Utilies.getLeague(mContext, cursor.getInt(MainScreenFragment.COL_LEAGUE)));
                matchday.setText(Utilies.getMatchDay(mContext, cursor.getInt(MainScreenFragment.COL_MATCHDAY), cursor.getInt(MainScreenFragment.COL_LEAGUE)));


                // Share Button listener
                shareButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //add Share Action
                        String shareString = viewHolder.home_name.getText() + " "
                                + viewHolder.mScore.getText() + " " + viewHolder.away_name.getText() + " ";

                        Intent shareIntent = createShareForecastIntent(shareString);

                        mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.share_action_title)));

                    }
                });
//


                // This listener will minimize the expanded layout when the expanded layout is clicked.
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v(LOG_TAG, "inside the detailLayout view");

                        mSelectView = EXPANDED_ELEMENT_DEFAULT;

                        // reset the data
                        mMainScreenFragment.refreshData();
                    }
                });


                break;


//            This is used for non-expanded version
            case VIEW_TYPE_MATCH:

                // set scores_list_item layoutButton Content Description
                String matchButtonFormat = mContext.getString(R.string.match_description_contentdescription);
                String matchButtonMessage = String.format(matchButtonFormat,
                                cursor.getString(MainScreenFragment.COL_HOME),
                                cursor.getString(MainScreenFragment.COL_AWAY));



                LinearLayout layoutButton = (LinearLayout) view.findViewById(R.id.match_content_description);
                layoutButton.setContentDescription(matchButtonMessage);
                Log.v(LOG_TAG, "Content Description message:" + matchButtonMessage);


                break;

            default:
                Log.e(LOG_TAG, "Returned a viewType value that does not exist: " + viewType);


        }



    }

    public Intent createShareForecastIntent(String ShareText) {


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + mContext.getString(R.string.football_scores_hashtag));

        return shareIntent;
    }

}
