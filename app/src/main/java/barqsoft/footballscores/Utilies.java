package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by yehya khaled on 3/3/2015.
 *
 */
public class Utilies {

    public static final String LOG_TAG = Utilies.class.getSimpleName();

    public static final int SERIE_A = 401; // 357
    public static final int PREMIER_LEAGUE = 398; // 354
    public static final int CHAMPIONS_LEAGUE = 405; // 362
    public static final int PRIMERA_DIVISION = 399; // 358
    public static final int BUNDESLIGA = 394; // 351
    public static final int BUNDESLIGA2 = 395; // new

    public static String getLeague(Context context, int league_num) {

        switch (league_num) {
            case SERIE_A:
                return context.getString(R.string.seriaa);
            case PREMIER_LEAGUE:
                return context.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE:
                return context.getString(R.string.champions_league);
            case PRIMERA_DIVISION:
                return context.getString(R.string.primeradivison);
            case BUNDESLIGA:
                return context.getString(R.string.bl1);
            case BUNDESLIGA2:
                return context.getString(R.string.bl2);
            default:
                return context.getString(R.string.league_unknown);
        }
    }

    public static String getMatchDay(Context context, int match_day, int league_num) {

        if (league_num == CHAMPIONS_LEAGUE) {

            if (match_day <= 6) {
                String messageFormat = context.getString(R.string.group_stage_text);
                return String.format(messageFormat, match_day);

            } else if (match_day == 7 || match_day == 8) {
                return context.getString(R.string.first_knockout_round);

            } else if (match_day == 9 || match_day == 10) {
                return context.getString(R.string.quarter_final);

            } else if (match_day == 11 || match_day == 12) {
                return context.getString(R.string.semi_final);

            } else {
                return context.getString(R.string.final_text);
            }

        } else {
            return  context.getString(R.string.matchday_text) + String.valueOf(match_day);
        }
    }



    // check if the layout order is RTL.
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


//    Generate a Content Description message
    public static String getScoresForContentDescription(Context context, String home, int home_goals, String away, int away_goals) {
        String messageFormat = context.getString(R.string.match_scores_contentdescription);
        String message;

        if (home_goals < 0 || away_goals < 0) {
            message = context.getString(R.string.no_match_scores_contentdescription);
            return message;
        }

        message = String.format(messageFormat, home, home_goals, away, away_goals);
//        Log.v(LOG_TAG, "getScoresforCD is " + message);

        return message;

    }



    // Added a number of crests that currently exist in the drawable folder
    public static int getTeamCrestByTeamName(String team_name) {
        if (team_name == null) {
            return R.drawable.no_icon;
        }

        //
        //
        // NOTE: strings here are names of teams and not customer visible. Not placing in strings.xml
        //
        //
        switch (team_name) {
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

//        Log.v(LOG_TAG, "the date is " + date);

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

//            Log.v(LOG_TAG, "today, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            return  context.getString(R.string.today);

        // tomorrow
        } else if (targetDay.get(Calendar.DAY_OF_WEEK) == tomorrowDay.get(Calendar.DAY_OF_WEEK)) {
//            Log.v(LOG_TAG, "tomorrow, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            return context.getString(R.string.tomorrow);

        // yesterday
        } else if (targetDay.get(Calendar.DAY_OF_WEEK) == yesterdayDay.get(Calendar.DAY_OF_WEEK)) {
//            Log.v(LOG_TAG, "yesterday, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            return context.getString(R.string.yesterday);

        // all other days, return the day
        } else {

            String dayString = targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
//          Log.v(LOG_TAG, "otherwise, the day is " + targetDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
//            Log.v(LOG_TAG, "Day is " + dayString);
            return dayString;

        }



    }



    //create a specific key used for the ElementKey  for a specificDate
    private static String getElementKey(Context context, String specificDate){
        return context.getString(R.string.element_is_expanded_key) + specificDate;
    }

    // sets whether one element found on a ScoresApapter is expanded or not, and saves that element.
    // should send the String / SQL representation of the date "XXXX-XX-XX"
    public static void setElementExpanded(Context context, int selectView, String specificDate){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(getElementKey(context, specificDate), selectView);
        editor.apply();
    }


    // obtains the expanded key if saved.
    // should send the String / SQL representation of the date "XXXX-XX-XX"
    public static int getElementExpanded(Context context, String specificDate) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int selectView = sp.getInt(getElementKey(context, specificDate), ScoresAdapter.EXPANDED_ELEMENT_DEFAULT);

        return  selectView;
    }


}
