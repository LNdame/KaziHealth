package mandela.cct.ansteph.kazihealth.view.tip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.view.appmanagement.Apps;
import mandela.cct.ansteph.kazihealth.view.profile.Profile;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;
import mandela.cct.ansteph.kazihealth.view.register.Login;

public class About extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GlobalRetainer mGlobalRetainer;

    SpannableTextView stvAboutKH, stvfunderKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGlobalRetainer = (GlobalRetainer) getApplicationContext();

        stvAboutKH = findViewById(R.id.stvAboutKH);
        stvfunderKH = findViewById(R.id.stvfunderKH);

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

        buildAboutKH();
        buildFunderKH();
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
        getMenuInflater().inflate(R.menu.about, menu);
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
            startActivity(new Intent(getApplicationContext(),Legal.class));
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
            startActivity(new Intent(getApplicationContext(), Apps.class));
        } else if (id == R.id.nav_about) {
           // startActivity(new Intent(getApplicationContext(), About.class));
        }else if (id == R.id.nav_logout) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onLogoClicked(View view){

        int id = view.getId();

        String uri = "http://www.mandela.ac.za/";

        switch (id)
        {
            case R.id.imguninmu: uri ="http://www.mandela.ac.za/";break;
            case R.id.imgunibasel:uri ="https://www.unibas.ch/en.html";break;
            case R.id.imgdsbg:uri ="https://dsbg.unibas.ch/de/home/";break;
            case R.id.imgtph:uri ="https://www.swisstph.ch/en/";break;
        }


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);


    }


    void buildAboutKH()
    {
        String aboutb1 =" is a workplace health promotion programme that educates and improves health behaviours in individuals. " +
                "The programme starts with an individualised health risk assessment, followed by face-to-face lifestyle coaching " +
                "sessions and self-monitoring and motivation through the " ;


        String aboutb2=" app. These tools are aimed at reducing the risks for cardiovascular diseases and improve physical activity and physical fitness, " +
                "nutrition and diet, and psychosocial health.\n\n\n The ";
        String aboutb3=" mobile app integrates three lifestyle interventions namely, physical activity, nutrition and stress " +
                "management to guide individuals in achieving their personal health goals. Education, motivation and self-monitoring is " +
                "provided within the ";

        String aboutb4=" app to keep individuals motivated and informed, and to ultimately make healthier lifestyle choices and decrease health risks \n";


        stvAboutKH.addSlice(new Slice.Builder("KaziHealth").style(Typeface.ITALIC).build());
        stvAboutKH.addSlice(new Slice.Builder(aboutb1).style(Typeface.NORMAL).build());

        stvAboutKH.addSlice(new Slice.Builder("KaziHealth").style(Typeface.ITALIC).build());
        stvAboutKH.addSlice(new Slice.Builder(aboutb2).style(Typeface.NORMAL).build());


        stvAboutKH.addSlice(new Slice.Builder("KaziHealth").style(Typeface.ITALIC).build());
        stvAboutKH.addSlice(new Slice.Builder(aboutb3).style(Typeface.NORMAL).build());

        stvAboutKH.addSlice(new Slice.Builder("KaziHealth").style(Typeface.ITALIC).build());
        stvAboutKH.addSlice(new Slice.Builder(aboutb4).style(Typeface.NORMAL).build());
        stvAboutKH.display();

    }

    void buildFunderKH()
    {



        String funderb1= "The Novartis Foundation funds the joint collaborative project between Nelson Mandela University in South Africa," +
                " University of Basel and the Swiss Tropical and Public Health Institute in Switzerland.\n\n";

        String funderb2= " Project is translated into \"Active People\" and aims at creating - Healthy Schools for Healthy Communities.\n\n";

        String funderb3= "The Novartis Foundation is a philanthropic organization which strives to have a sustainable impact on the " +
                "health of low-income communities through a combination of programmatic work, health outcomes research, and its translation into" +
                " policy to tackle global health challenges. ";

        stvfunderKH.addSlice(new Slice.Builder(funderb1).style(Typeface.NORMAL).build());

        stvfunderKH.addSlice(new Slice.Builder("The ").style(Typeface.NORMAL).build());
        stvfunderKH.addSlice(new Slice.Builder("KaziBantu").style(Typeface.ITALIC).build());
        stvfunderKH.addSlice(new Slice.Builder(funderb2).style(Typeface.NORMAL).build());

        stvfunderKH.addSlice(new Slice.Builder(funderb3).style(Typeface.NORMAL).build());

        stvfunderKH.display();
    }


}
