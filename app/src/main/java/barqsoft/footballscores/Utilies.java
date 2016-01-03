package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {

    public static final String LOG_TAG = Utilies.class.getSimpleName();

    public static final int SERIE_A = 401; // 357
    public static final int PREMIER_LEAGUE = 398; // 354
    public static final int CHAMPIONS_LEAGUE = 405; // 362
    public static final int PRIMERA_DIVISION = 399; // 358
    public static final int BUNDESLIGA = 394; // 351
    public static final int BUNDESLIGA2 = 395; // new

    public static String getLeague(int league_num) {
        Log.v("getLeague", "league num:" + league_num);

        switch (league_num) {
            case SERIE_A:
                return "Seria A";
            case PREMIER_LEAGUE:
                return "Premier League";
            case CHAMPIONS_LEAGUE:
                return "UEFA Champions League";
            case PRIMERA_DIVISION:
                return "Primera Division";
            case BUNDESLIGA:
                return "BL1";
            case BUNDESLIGA2:
                return "BL2";
            default:
                return "Not known League Please report";
        }
    }

    public static String getMatchDay(int match_day, int league_num) {

        if (league_num == CHAMPIONS_LEAGUE) {

            if (match_day <= 6) {
                return "Group Stages, Matchday : 6";

            } else if (match_day == 7 || match_day == 8) {
                return "First Knockout round";

            } else if (match_day == 9 || match_day == 10) {
                return "QuarterFinal";

            } else if (match_day == 11 || match_day == 12) {
                return "SemiFinal";

            } else {
                return "Final";
            }

        } else {
            return "Matchday : " + String.valueOf(match_day);
        }
    }



    // check if the layout order is RTL
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean isLayoutRTL(Context context){
        Configuration config =  context.getResources().getConfiguration();

        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return true;
        }

        return false;
    }

    // scores; standard LTR means Home team on the Left; Away on the Right
    public static String getScores(Context context, int home_goals, int away_goals) {
        if (home_goals < 0 || away_goals < 0) {
            return " - ";
        } else {

            // check the SDK version for RTL compatibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                // if true, (this means that RTL if on)
                // then flip the score order.
                if (isLayoutRTL(context)){
                    return String.valueOf(away_goals) + " - " + String.valueOf(home_goals);
                }
            }

            return String.valueOf(home_goals) + " - " + String.valueOf(away_goals);
        }

    }


    public static String getScoresForContentDescription(Context context, String home, int home_goals, String away, int away_goals) {
        String messageFormat = context.getString(R.string.match_scores_contentdescription);
        String message;

        if (home_goals < 0 || away_goals < 0) {
            message = context.getString(R.string.no_match_scores_contentdescription);
            return message;
        }

        message = String.format(messageFormat, home, home_goals, away, away_goals);
        Log.v(LOG_TAG, "getScoresforCD is " + message);

        return message;

    }

//    public static String getMatchInformationForContentDescription(){
//
//    }


    // Added a number of crests that currently exist in the drawable folder
    public static int getTeamCrestByTeamName(String team_name) {
        if (team_name == null) {
            return R.drawable.no_icon;
        }
        Log.v(LOG_TAG, "Team Name: " + team_name);
        switch (team_name) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC":
            case "Arsenal FC":
                return R.drawable.arsenal;

            case "Aston Villa FC":
                return R.drawable.aston_villa;
            case "Burney FC":
                return R.drawable.burney_fc_hd_logo;
            case "Chelsea FC":
                return R.drawable.chelsea;
            case "Crystal Palace FC":
                return R.drawable.crystal_palace_fc;

            case "Hull City FC":
                return R.drawable.hull_city_afc_hd_logo;

            case "Leicester City FC":
            case "Leicester City":
                return R.drawable.leicester_city_fc_hd_logo;

            case "Liverpool FC":
                return R.drawable.liverpool;

            case "Manchester City FC":
                return R.drawable.manchester_city;

            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Newcastle United FC":
                return R.drawable.newcastle_united;
            case "Queens Park Rangers FC":
                return R.drawable.queens_park_rangers_hd_logo;
            case "Southampton FC":
                return R.drawable.southampton_fc;

            case "Swansea City FC":
            case "Swansea City":
                return R.drawable.swansea_city_afc;

            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;

            case "West Bromwich Albion FC":
            case "West Bromwich Albion":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Stoke City FC":
                return R.drawable.stoke_city;
            default:
                return R.drawable.no_icon;
        }
    }



    /*
     * obtains helpful day strings using Calendar classes instead of the other classes.
     *
     */
    public static String getDayName(Context context, String date){

        Log.v(LOG_TAG, "the date is " + date);

        final int TOMORROW = 1;
        final int YESTERDAY = -1;
        final int DAYS_ARE_EQUAL = 0;

        // set up the target calendar date
        Date targetDate = Date.valueOf(date);
        GregorianCalendar targetDay = new GregorianCalendar();
        targetDay.setTime(targetDate);

        // value to compare to target date
        GregorianCalendar currentDay = new GregorianCalendar();

        // set up value for tomorrow
        GregorianCalendar tomorrowDay = new GregorianCalendar();
        tomorrowDay.add(Calendar.DAY_OF_WEEK, TOMORROW);

        // set up value for yesterday
        GregorianCalendar yesterdayDay = new GregorianCalendar();
        yesterdayDay.add(Calendar.DAY_OF_WEEK, YESTERDAY);


        // today
//        if (targetDay.compareTo(currentDay) == DAYS_ARE_EQUAL) {
        if (targetDay.get(Calendar.DAY_OF_WEEK) == currentDay.get(Calendar.DAY_OF_WEEK)) {

            Log.v(LOG_TAG, "today, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            return  context.getString(R.string.today);

        // tomorrow
        } else if (targetDay.get(Calendar.DAY_OF_WEEK) == tomorrowDay.get(Calendar.DAY_OF_WEEK)) {
            Log.v(LOG_TAG, "tomorrow, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            return context.getString(R.string.tomorrow);

        // yesterday
        } else if (targetDay.get(Calendar.DAY_OF_WEEK) == yesterdayDay.get(Calendar.DAY_OF_WEEK)) {
            Log.v(LOG_TAG, "yesterday, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            return context.getString(R.string.yesterday);

        // all other days, return the day
        } else {

            String dayString = targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
          Log.v(LOG_TAG, "otherwise, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
//            Log.v(LOG_TAG, "Day is " + dayString);
            return dayString;

        }



    }

    public static void setMatchDetailViewStatusPreference(Context context, int matchEntry, String fragmentDate) {
        Log.v(LOG_TAG, "matchDetail set to " + matchEntry);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(fragmentDate, matchEntry);
        editor.apply();

    }

    public static int getMatchDetailViewStatusPreference(Context context, String fragmentDate) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int matchEntry = sp.getInt(fragmentDate, MainScreenFragment.DEFAULT_DETAIL_VIEW);

        return matchEntry;
    }



}
