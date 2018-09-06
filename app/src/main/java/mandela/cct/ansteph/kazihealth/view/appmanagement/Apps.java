package mandela.cct.ansteph.kazihealth.view.appmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.view.profile.Profile;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;
import mandela.cct.ansteph.kazihealth.view.register.Login;
import mandela.cct.ansteph.kazihealth.view.tip.About;
import mandela.cct.ansteph.kazihealth.view.tip.Tips;

public class Apps extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    GlobalRetainer mGlobalRetainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGlobalRetainer = (GlobalRetainer) getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initDrawer();
    }

    void initDrawer()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = (TextView) headerView.findViewById(R.id.txtNavName);
        TextView navEmail= (TextView) headerView.findViewById(R.id.txtNavEmail);
        ImageView navAvatar = (ImageView)headerView.findViewById(R.id.avatar);

        if(mGlobalRetainer.get_grUser().getProfilePic()!=null)
        {            Bitmap bitmap = BitmapFactory.decodeByteArray(mGlobalRetainer.get_grUser().getProfilePic(), 0, mGlobalRetainer.get_grUser().getProfilePic().length);
            navAvatar.setImageBitmap(bitmap);
        }
        navName.setText(mGlobalRetainer.get_grUser().getName());
        navEmail.setText(mGlobalRetainer.get_grUser().getEmail());
    }

    public void gotoHiFit(View view)
    {

        Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",1);
        startActivity(i);

       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.ouj.hifit"));
        startActivity(intent);*/
    }

    public void gotoWorkout(View view)
    {
        Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",2);
        startActivity(i);

        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.skimble.workouts&rdid=com.skimble.workouts"));
        startActivity(intent);*/
    }


    public void gotoStepCounter(View view)
    {
        Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",3);
        startActivity(i);

        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=pedometer.steptracker.calorieburner.stepcounter"));
        startActivity(intent);*/
    }


    public void gotoMyFitnessPal(View view)
    {
        Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",4);
        startActivity(i);/*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.myfitnesspal.android"));
        startActivity(intent);*/
    }



    public void gotoLifesum(View view)
    {
        Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",5);
        startActivity(i);
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.sillens.shapeupclub"));
        startActivity(intent);*/
    }

    public void gotoSeeHowEat(View view)
    {
        Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",6);
        startActivity(i);
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=fi.seehowyoueat.shye"));
        startActivity(intent);*/

    }


    public void gotoCatchIt(View view)
    {
        Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",7);
        startActivity(i);

       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=uk.ac.liv.catchit"));
        startActivity(intent);*/
    }


    public void gotoStopBreathe(View view)
    {Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",8);
        startActivity(i);
       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=org.stopbreathethink.app"));
        startActivity(intent);*/
    }

    public void gotoSmiling(View view)
    {Intent i = new  Intent(getApplicationContext(), AppDownload.class);
        i.putExtra("FLAG",9);
        startActivity(i);
      /*  Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.smilingmind.app"));
        startActivity(intent);*/
    }











    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.apps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_risk) {
            startActivity(new Intent(getApplicationContext(), RiskProfile.class));
        } else if (id == R.id.nav_tips) {
            startActivity(new Intent(getApplicationContext(), Tips.class));
        } else if (id == R.id.nav_apps) {
        //    startActivity(new Intent(getApplicationContext(), Apps.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }else if (id == R.id.nav_logout) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
