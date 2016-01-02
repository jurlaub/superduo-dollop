package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;

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
    private static final int INDEX_SCORES_DATE = 5;
    private static final int INDEX_SCORES_TIME = 6;



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
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
//                final long identityToken = Binder.clearCallingIdentity();
//                Uri scoresWithDateUri = DatabaseContract.ScoresTable.buildScoreWithDate();
//                String[] datesToUse = new String[] {};
//                data = getContentResolver().query(
//                        scoresWithDateUri,
//                        SCORES_COLUMNS,
//                        null,
//                        null,
//                        )
//
//
//                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                return null;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
