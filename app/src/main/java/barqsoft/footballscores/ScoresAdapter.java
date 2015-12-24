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

    public int mDetail_match_id = 0;
    public int mSelectView = -1;



    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    private Context mContext;
//    private ViewHolder mHolder;

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

            Log.v("ViewHolder", "view id = " + view.getId());


        }



    }





    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.mContext = context;


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





    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutID = -1;
        int viewType = getItemViewType(cursor.getPosition());

        switch (viewType) {

            case VIEW_TYPE_DETAIL:
                layoutID = R.layout.detail_fragment;
                Log.v(LOG_TAG, "newView - detail_fragment and mSelectView: " + mSelectView);

                break;
            case VIEW_TYPE_MATCH:
                layoutID = R.layout.scores_list_item;
                Log.v(LOG_TAG, "newView - scores_list_item and mSelectView: " + mSelectView);
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
    public void bindView(View view, final Context context, Cursor cursor) {

//        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int viewType = getItemViewType(cursor.getPosition());
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        // ----------   Common to both views  -----------
        Log.v(LOG_TAG, "Cursor position: " + cursor.getPosition() + " mSelectView: " + mSelectView );

        //        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.home_name.setText(cursor.getString(MainScreenFragment.COL_HOME));
        viewHolder.away_name.setText(cursor.getString(MainScreenFragment.COL_AWAY));

        viewHolder.date.setText(cursor.getString(MainScreenFragment.COL_MATCHTIME));
        viewHolder.mScore.setText(Utilies.getScores(cursor.getInt(MainScreenFragment.COL_HOME_GOALS), cursor.getInt(MainScreenFragment.COL_AWAY_GOALS)));

        // set contextual ContentDescription for the score
        String tmpMessage = new StringBuilder(cursor.getString(MainScreenFragment.COL_HOME)).append(' ')
                .append(context.getString(R.string.team_score_contentdescriptions))
                .append(cursor.getInt(MainScreenFragment.COL_HOME_GOALS))
                .append(' ').append('-').append(' ')
                .append(cursor.getString(MainScreenFragment.COL_AWAY)).append(' ')
                .append(context.getString(R.string.team_score_contentdescriptions))
                .append(cursor.getInt(MainScreenFragment.COL_AWAY_GOALS))
                .toString();

        viewHolder.mScore.setContentDescription(tmpMessage);

        viewHolder.match_id = cursor.getInt(MainScreenFragment.COL_MATCH_ID);

        viewHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(cursor.getString(MainScreenFragment.COL_HOME)));
        viewHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(cursor.getString(MainScreenFragment.COL_AWAY)));







        switch (viewType) {

            case VIEW_TYPE_DETAIL:

                TextView league = (TextView) view.findViewById(R.id.league_textview);
                TextView matchday = (TextView) view.findViewById(R.id.matchday_textview);
                Button shareButton = (Button) view.findViewById(R.id.share_button);

                league.setText(Utilies.getLeague(cursor.getInt(MainScreenFragment.COL_LEAGUE)));
                matchday.setText(Utilies.getMatchDay(cursor.getInt(MainScreenFragment.COL_MATCHDAY), cursor.getInt(MainScreenFragment.COL_LEAGUE)));
                shareButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //add Share Action
                        context.startActivity(createShareForecastIntent(viewHolder.home_name.getText() + " "
                                + viewHolder.mScore.getText() + " " + viewHolder.away_name.getText() + " "));
                    }
                });



                break;

            case VIEW_TYPE_MATCH:



                // set scores_list_item layoutButton Content Description
                String matchButtonMessage = new StringBuilder(context.getString(R.string.match_description)).append(' ')
                        .append(cursor.getString(MainScreenFragment.COL_HOME)).append(' ')
                        .append(context.getString(R.string.match_conjunction)).append(' ')
                        .append(cursor.getString(MainScreenFragment.COL_AWAY)).toString();


                LinearLayout layoutButton = (LinearLayout) view.findViewById(R.id.match_content_description);
                layoutButton.setContentDescription(matchButtonMessage);
                Log.v(LOG_TAG, "Content Description message:" + matchButtonMessage);


                break;

            default:
                Log.e(LOG_TAG, "Returned a viewType value that does not exist: " + viewType);


        }



        //Log.v(FetchScoreTask.LOG_TAG,viewHolder.home_name.getText() + " Vs. " + viewHolder.away_name.getText() +" id " + String.valueOf(viewHolder.match_id));
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(mDetail_match_id));

//        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = vi.inflate(R.layout.detail_fragment, null);
//        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);










//        if (viewHolder.match_id == mDetail_match_id) {
//            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");
//
//            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//
//            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
//            match_day.setText(Utilies.getMatchDay(cursor.getInt(COL_MATCHDAY), cursor.getInt(COL_LEAGUE)));
//
//            TextView league = (TextView) v.findViewById(R.id.league_textview);
//            league.setText(Utilies.getLeague(cursor.getInt(COL_LEAGUE)));
//
//            Button share_button = (Button) v.findViewById(R.id.share_button);
//
//            share_button.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    //add Share Action
//                    context.startActivity(createShareForecastIntent(viewHolder.home_name.getText() + " "
//                            + viewHolder.mScore.getText() + " " + viewHolder.away_name.getText() + " "));
//                }
//            });
//
//        } else {
//
//            container.removeAllViews();
//
//
//
//        }



    }

    public Intent createShareForecastIntent(String ShareText) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);

        return shareIntent;
    }

}
