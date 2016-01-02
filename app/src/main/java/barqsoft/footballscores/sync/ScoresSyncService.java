package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by dev on 1/2/16.
 */
public class ScoresSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static ScoresSyncAdapter sScoresSyncAdapter = null;


    @Override
    public void onCreate(){
        synchronized (sSyncAdapterLock) {
            if (sScoresSyncAdapter == null) {
                sScoresSyncAdapter = new ScoresSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sScoresSyncAdapter.getSyncAdapterBinder();
    }


}
