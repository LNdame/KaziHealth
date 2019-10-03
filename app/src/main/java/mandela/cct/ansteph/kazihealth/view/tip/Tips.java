package mandela.cct.ansteph.kazihealth.view.tip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.adapter.TipRecyclerViewAdapter;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.model.TipItem;
import mandela.cct.ansteph.kazihealth.view.appmanagement.Apps;
import mandela.cct.ansteph.kazihealth.view.firebasereg.Login_Firebase;
import mandela.cct.ansteph.kazihealth.view.profile.Profile;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;

public class Tips extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    RecyclerView tipRecyclerView;

    ArrayList<TipItem> mTipList;
    TipRecyclerViewAdapter mTipAdapter;

    GlobalRetainer mGlobalRetainer;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

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



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tipRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mTipList = setupList();//setupList();// new ArrayList<>();

        mTipAdapter  = new TipRecyclerViewAdapter(mTipList, this);
        tipRecyclerView.setLayoutManager(mLayoutManager);
        tipRecyclerView.setAdapter(mTipAdapter);

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


    ArrayList<TipItem> setupList()
    {
        ArrayList<TipItem>  arrayL = new ArrayList<>();

        arrayL.add(new TipItem (1,"1","Blood Pressure"));
        arrayL.add(new TipItem (2,"2","Weight Management"));
        arrayL.add(new TipItem (3,"3","Cholesterol"));
        arrayL.add(new TipItem (4,"4","Blood Glucose Levels"));



        // String duration, String task_date, String start, String end, String project, String description, String realduration, String task_break) {
        return  arrayL;
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
        getMenuInflater().inflate(R.menu.tips, menu);
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

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), Login_Firebase.class));
        //updateUI(null);
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
          //  startActivity(new Intent(getApplicationContext(), Tips.class));
        } else if (id == R.id.nav_apps) {
            startActivity(new Intent(getApplicationContext(), Apps.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }else if (id == R.id.nav_logout) {
            signOut();

          //  startActivity(new Intent(getApplicationContext(), Login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
