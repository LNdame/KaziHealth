package mandela.cct.ansteph.kazihealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import mandela.cct.ansteph.kazihealth.view.intro.WelcomePage;
import mandela.cct.ansteph.kazihealth.view.register.Register;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    /**
                     * Call this function whenever you want to check user login
                     * This will redirect user to Login_Firebase is he is not
                     * logged in
                     * */
                  //  sessionManager.checkReg();
                    Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
                    startActivity(intent);



                }
            }
        };
        timerThread.start();
    }
}
