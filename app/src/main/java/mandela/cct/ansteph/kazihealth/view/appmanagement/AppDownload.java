package mandela.cct.ansteph.kazihealth.view.appmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import mandela.cct.ansteph.kazihealth.R;

public class AppDownload extends AppCompatActivity {

    private VideoView myVideoView;

    private int position = 0;

    private ProgressDialog progressDialog;

    private MediaController mediaControls;


    public static int APP_HIFIT = 1;
    public static int APP_WORKOUT = 2;
    public static int APP_STEPCOUNTER = 3;
    public static int APP_FITNESSPAL = 4;
    public static int APP_LIFESUM = 5;
    public static int APP_HOWYOUEAT = 6;
    public static int APP_CATCHIT = 7;
    public static int APP_STOPBREATHE = 8;
    public static int APP_SMILINGMIND = 9;

    int mFlag=0;

    ImageView imgLargeIcon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();

        imgLargeIcon = (ImageView) findViewById(R.id.imglargeIcon);
        myVideoView =(VideoView) findViewById(R.id.videoView);

        if(b!=null)
        {
            int flag = b.getInt("FLAG");
            mFlag = flag;
            setViews(flag);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(mediaControls ==null)
        {
            mediaControls = new MediaController(this);

        }




        try{
            myVideoView.setMediaController(mediaControls);
          //  myVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.myfitnesspal));
            myVideoView.setVideoURI(Uri.parse(getVideoUri(mFlag)));

        }catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();

        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                myVideoView.seekTo(position);
                if(position == 0){
                    myVideoView.start();
                }else{
                    myVideoView.pause();
                }
            }
        });




    }



    void setViews(int flag)
    {

        switch (flag)
        {
            case 1: setTitle("HiFit"); imgLargeIcon.setImageResource(R.mipmap.ic_hifit); break;
            case 2: setTitle("Workout Trainer"); imgLargeIcon.setImageResource(R.mipmap.ic_workout);break;
            case 3: setTitle("Step Counter"); imgLargeIcon.setImageResource(R.mipmap.ic_stepcounter);break;
            case 4:setTitle("MyFitnessPal"); imgLargeIcon.setImageResource(R.mipmap.ic_fitnesspal); break;
            case 5:setTitle("Lifesum"); imgLargeIcon.setImageResource(R.mipmap.ic_lifesum); break;
            case 6:setTitle("See How You Eat"); imgLargeIcon.setImageResource(R.mipmap.ic_seehoweat); break;
            case 7:setTitle("Catch It"); imgLargeIcon.setImageResource(R.mipmap.ic_catchit); break;
            case 8:setTitle("Stop, Breathe &amp; Think"); imgLargeIcon.setImageResource(R.mipmap.ic_stopbreathe); break;
            case 9: setTitle("Smiling Mind"); imgLargeIcon.setImageResource(R.mipmap.ic_smilingmind);break;
        }

    }


    String getVideoUri(int flag){
        String uri = "android.resource://"+getPackageName()+"/"+R.raw.myfitnesspalvid;


        switch (mFlag)
        {
            case 1:uri="android.resource://"+getPackageName()+"/"+R.raw.hifit_youtube  ; break;
            case 2:uri = "android.resource://"+getPackageName()+"/"+R.raw.workout_trainer; break;
            case 3:uri="android.resource://"+getPackageName()+"/"+R.raw.nopromo  ;break;
            case 4:uri="android.resource://"+getPackageName()+"/"+R.raw.myfitnesspalvid  ; break;
            case 5:uri="android.resource://"+getPackageName()+"/"+R.raw.nopromo ; break;
            case 6:uri="android.resource://"+getPackageName()+"/"+R.raw.nopromo  ; break;
            case 7:uri="android.resource://"+getPackageName()+"/"+R.raw.nopromo  ; break;
            case 8:uri="android.resource://"+getPackageName()+"/"+R.raw.breathes  ; break;
            case 9:uri="android.resource://"+getPackageName()+"/"+R.raw.smiling_mind  ;break;
        }

        return uri;
    }


    public void onGoogleClicked(View view)
    {
        String url = "market://details?id=com.skimble.workouts&rdid=com.skimble.workouts";


        switch (mFlag)
        {
            case 1:url="market://details?id=com.ouj.hifit"  ; break;
            case 2:url="market://details?id=com.skimble.workouts&rdid=com.skimble.workouts"; ;break;
            case 3:url="market://details?id=pedometer.steptracker.calorieburner.stepcounter"  ;break;
            case 4:url="market://details?id=com.myfitnesspal.android"  ; break;
            case 5:url="market://details?id=com.sillens.shapeupclub"  ; break;
            case 6:url="market://details?id=fi.seehowyoueat.shye"  ; break;
            case 7:url="market://details?id=uk.ac.liv.catchit"  ; break;
            case 8:url="market://details?id=org.stopbreathethink.app"  ; break;
            case 9:url="market://details?id=com.smilingmind.app"  ;break;
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }


    public void onLinkClicked(View view)
    {
        String uri = "https://www.youtube.com/watch?v=dhtZFj_oG-k";


        switch (mFlag)
        {
            case 1:uri="https://www.youtube.com/watch?v=dhtZFj_oG-k"  ; break;
            case 2:uri = "https://www.youtube.com/watch?v=WwZMfqWVPRc"; break;
            case 3:uri="https://www.youtube.com/watch?v=nr7nGJEqedQ"  ;break;
            case 4:uri="https://www.youtube.com/watch?v=HEnIQ962BWE"  ; break;
            case 5:uri="https://www.youtube.com/watch?v=NBsXo2qkVRc"  ; break;
            case 6:uri="https://www.youtube.com/watch?v=dhtZFj_oG-k"  ; break;
            case 7:uri="https://www.youtube.com/watch?v=dhtZFj_oG-k"  ; break;
            case 8:uri="https://www.youtube.com/watch?v=d2oC9pledcQ"  ; break;
            case 9:uri="https://www.youtube.com/watch?v=AarjKX5OSro"  ;break;
        }


        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(uri)));
        Log.i("Video", "Video Playing....");

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }
}
