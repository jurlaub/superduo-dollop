package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by dev on 1/1/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();

    // data columns to pull from db
    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.ScoresTable._ID,
            DatabaseContract.ScoresTable.HOME_COL,
            DatabaseContract.ScoresTable.AWAY_COL,
            DatabaseContract.ScoresTable.HOME_GOALS_COL,
            DatabaseContract.ScoresTable.AWAY_GOALS_COL,
            DatabaseContract.ScoresTable.DATE_COL,
            DatabaseContract.ScoresTable.TIME_COL
    };

    // --- !! --- must stay in sync with SCORES_COLUMNS, change above order then the number must also change.
    private static final int INDEX_SCORE_ID = 0;
    private static final int INDEX_SCORE_HOME = 1;
    private static final int INDEX_SCORE_AWAY = 2;
    private static final int INDEX_SCORES_HOME_GOALS = 3;
    private static final int INDEX_SCORES_AWAY_GOALS = 4;
    private static final int INDEX_SCORES_MATCHDATE = 5;
    private static final int INDEX_SCORES_TIME = 6;



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v(LOG_TAG, "onGetViewFactory");

        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

//
                final long identityToken = Binder.clearCallingIdentity();
                Uri scoresWithDateUri = DatabaseContract.ScoresTable.buildScoreWithDate();

//                String[] datesToUse = new String[] {};
                data = getContentResolver().query(
                        scoresWithDateUri,
                        SCORES_COLUMNS,
                        null,
                        null,
                        DatabaseContract.ScoresTable.DATE_COL + " ASC");

//                if (data != null) {
//                    Log.v(LOG_TAG, "data count: " + data.getCount());
//                } else {
//                    Log.v(LOG_TAG, "data is null");
//                }


                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_details_item_list);

                // obtain values from cursor
                String homeName = data.getString(INDEX_SCORE_HOME);
                int homeScore = data.getInt(INDEX_SCORES_HOME_GOALS);
                int homeIcon = Utilies.getTeamCrestByTeamName(homeName);

                String awayName = data.getString(INDEX_SCORE_AWAY);
                int awayScore = data.getInt(INDEX_SCORES_AWAY_GOALS);
                int awayIcon = Utilies.getTeamCrestByTeamName(awayName);

                String matchScores = Utilies.getScores(getApplicationContext(), homeScore, awayScore);
                String matchDate = Utilies.getDayName(getApplicationContext(), data.getString(INDEX_SCORES_MATCHDATE));
//                String matchDate = data.getString(INDEX_SCORES_MATCHDATE);
//                Log.v(LOG_TAG, homeName + " vs " + awayName + " occurred, " + matchDate + " the date: " + data.getString(INDEX_SCORES_MATCHDATE) );
//                Log.v(LOG_TAG, "MatchTime: " + data.getInt(INDEX_SCORES_TIME));
                // set the values in the widget listview
                views.setTextViewText(R.id.home_name, homeName);
                views.setImageViewResource(R.id.home_crest, homeIcon);

                views.setTextViewText(R.id.away_name, awayName);
                views.setImageViewResource(R.id.away_crest, awayIcon);

                views.setTextViewText(R.id.score_textview, matchScores);
                views.setTextViewText(R.id.match_date, matchDate);


//                Log.v(LOG_TAG, "view position: " + position);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_details_item_list);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position)) {
                    return data.getLong(INDEX_SCORE_ID);
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
