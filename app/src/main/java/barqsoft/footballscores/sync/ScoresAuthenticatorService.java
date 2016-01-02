package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by dev on 1/2/16.
 */
public class ScoresAuthenticatorService extends Service {

    private ScoresAuthenticator mAuthenticator;


    @Override
    public void onCreate(){
        mAuthenticator = new ScoresAuthenticator(this);
    }

    /*
    * When the system binds to this Service to make the RPC call
    * return the authenticator's IBinder.
    */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}


