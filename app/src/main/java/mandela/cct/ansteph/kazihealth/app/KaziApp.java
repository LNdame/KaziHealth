package mandela.cct.ansteph.kazihealth.app;

import android.app.Application;
import android.content.Context;


import com.facebook.stetho.Stetho;
import com.onesignal.OneSignal;

import mandela.cct.ansteph.kazihealth.model.User;

/**
 * Created by loicstephan on 2018/06/22.
 */

public class KaziApp extends Application{

    public static final String TAG = KaziApp.class
            .getSimpleName();

    private static KaziApp mInstance;
    private static Context mAppContext;

    public User _grUser = new User();

    @Override
    public void onCreate() {
        super.onCreate();

        KaziApp.mAppContext = getApplicationContext();
        mInstance = this;

        Stetho.initializeWithDefaults(this);
        //oneSignal Initilization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


    }

    public static synchronized KaziApp getInstance(){
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static void setAppContext(Context mAppContext) {
        KaziApp.mAppContext = mAppContext;
    }

    public User get_grUser() {
        return _grUser;
    }
    public void set_grUser(User _grUser) {
        this._grUser = _grUser;
    }
}
