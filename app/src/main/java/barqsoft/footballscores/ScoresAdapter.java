package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter {
    private final String LOG_TAG = ScoresAdapter.class.getSimpleName();

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;

    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";


    private ViewHolder mHolder;


    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);

        mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");

        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {


        //        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.home_name.setText(cursor.getString(COL_HOME));
        mHolder.away_name.setText(cursor.getString(COL_AWAY));

        mHolder.date.setText(cursor.getString(COL_MATCHTIME));
        mHolder.mScore.setText(Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));

        // set contextual ContentDescription for the score
        String tmpMessage = new StringBuilder(cursor.getString(COL_HOME)).append(' ')
                    .append(context.getString(R.string.team_score_contentdescriptions))
                    .append(cursor.getInt(COL_HOME_GOALS))
                    .append(' ').append('-').append(' ')
                    .append(cursor.getString(COL_AWAY)).append(' ')
                    .append(context.getString(R.string.team_score_contentdescriptions))
                    .append(cursor.getInt(COL_AWAY_GOALS))
                    .toString();

        mHolder.mScore.setContentDescription(tmpMessage);

        mHolder.match_id = cursor.getDouble(COL_ID);

        mHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
        mHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));



        // set layoutButton Content Description
        String matchButtonMessage = new StringBuilder(context.getString(R.string.match_description)).append(' ')
                .append(cursor.getString(COL_HOME)).append(' ')
                .append(context.getString(R.string.match_conjunction)).append(' ')
                .append(cursor.getString(COL_AWAY)).toString();


        LinearLayout layoutButton = (LinearLayout) view.findViewById(R.id.match_content_description);
        layoutButton.setContentDescription(matchButtonMessage);
        Log.v(LOG_TAG, "Content Description message:" + matchButtonMessage);


        //Log.v(FetchScoreTask.LOG_TAG,mHolder.home_name.getText() + " Vs. " + mHolder.away_name.getText() +" id " + String.valueOf(mHolder.match_id));
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(detail_match_id));

//        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = vi.inflate(R.layout.detail_fragment, null);
//        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);

        mHolder.mMatchday.setText(Utilies.getMatchDay(cursor.getInt(COL_MATCHDAY), cursor.getInt(COL_LEAGUE)));
        mHolder.mLeague.setText(Utilies.getLeague(cursor.getInt(COL_LEAGUE)));

        mHolder.mShareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //add Share Action
                context.startActivity(createShareForecastIntent(mHolder.home_name.getText() + " "
                        + mHolder.mScore.getText() + " " + mHolder.away_name.getText() + " "));
            }
        });



//        if (mHolder.match_id == detail_match_id) {
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
//                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText() + " "
//                            + mHolder.mScore.getText() + " " + mHolder.away_name.getText() + " "));
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
