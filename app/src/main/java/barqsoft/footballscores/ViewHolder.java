package barqsoft.footballscores;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder {
    public TextView home_name;
    public TextView away_name;
    public TextView mScore;
    public TextView date;
    public ImageView home_crest;
    public ImageView away_crest;
    public double match_id;

    public TextView mLeague;
    public TextView mMatchday;
    public Button mShareButton;

    public ViewHolder(View view) {
        home_name = (TextView) view.findViewById(R.id.home_name);
        away_name = (TextView) view.findViewById(R.id.away_name);
        mScore = (TextView) view.findViewById(R.id.score_textview);
        date = (TextView) view.findViewById(R.id.data_textview);
        home_crest = (ImageView) view.findViewById(R.id.home_crest);
        away_crest = (ImageView) view.findViewById(R.id.away_crest);

        mLeague = (TextView) view.findViewById(R.id.league_textview);
        mMatchday = (TextView) view.findViewById(R.id.matchday_textview);
        mShareButton = (Button) view.findViewById(R.id.share_button);



    }



}

