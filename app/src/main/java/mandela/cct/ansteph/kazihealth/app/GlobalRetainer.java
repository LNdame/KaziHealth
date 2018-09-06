package mandela.cct.ansteph.kazihealth.app;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;

import mandela.cct.ansteph.kazihealth.model.User;

/**
 * Created by loicstephan on 2018/06/22.
 */

public class GlobalRetainer extends Application{

    public static final String TAG = GlobalRetainer.class
            .getSimpleName();

    private static GlobalRetainer mInstance;
    private static Context mAppContext;

    public User _grUser = new User();

    @Override
    public void onCreate() {
        super.onCreate();

        GlobalRetainer.mAppContext = getApplicationContext();
        mInstance = this;


        //oneSignal Initilization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


    }

    public static synchronized GlobalRetainer getInstance(){
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static void setAppContext(Context mAppContext) {
        GlobalRetainer.mAppContext = mAppContext;
    }


    public User get_grUser() {
        return _grUser;
    }

    public void set_grUser(User _grUser) {
        this._grUser = _grUser;
    }
}
