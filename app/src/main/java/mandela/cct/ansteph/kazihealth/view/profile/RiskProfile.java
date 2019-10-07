package mandela.cct.ansteph.kazihealth.view.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.text.Html;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.ContentTypes;
import mandela.cct.ansteph.kazihealth.api.RiskItemID;
import mandela.cct.ansteph.kazihealth.api.columns.RiskProfileColumns;
import mandela.cct.ansteph.kazihealth.api.columns.UserColumns;
import mandela.cct.ansteph.kazihealth.app.KaziApp;
import mandela.cct.ansteph.kazihealth.data.AppExecutors;
import mandela.cct.ansteph.kazihealth.data.KaziDatabase;
import mandela.cct.ansteph.kazihealth.helper.SessionManager;
import mandela.cct.ansteph.kazihealth.model.RiskProfileItem;
import mandela.cct.ansteph.kazihealth.model.User;
import mandela.cct.ansteph.kazihealth.view.appmanagement.Apps;
import mandela.cct.ansteph.kazihealth.view.firebasereg.Login_Firebase;
import mandela.cct.ansteph.kazihealth.view.tip.About;
import mandela.cct.ansteph.kazihealth.view.tip.Tips;

public class RiskProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    KaziApp mKaziApp;
    TextView txtName, txtEmail;

    String mEmail, mPwd;
    TextView txtbpcolor, txtchlcolor, txtbmicolor, txtbglcolor;
    TextView txtbpmeasurement, txtchlmeasurement, txtbmimeasurement, txtbglmeasurement;
    TextView txtbpcomment, txtchlcomment, txtbmicomment, txtbglcomment;

    RiskProfileItem rBp, rHeartRate, rChol, rBgl,rBMI, rW2H;

    ImageView mImgAvatar;

    RelativeLayout ltyNoriskp;
    SessionManager sessionManager;
    private KaziDatabase kDB;
   // int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);  //getResources().getColor(R.color.riskColorlow);
   // int colorModerateRisk =ContextCompat.getColor(this, R.color.riskColorModerate);//  getResources().getColor(R.color.riskColorModerate);
   // int colorHighRisk =ContextCompat.getColor(this, R.color.riskColorHigh);  //getResources().getColor(R.color.riskColorHigh);
    User cUser;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login_Firebase.class));
        }
        kDB = KaziDatabase.getInstance(getApplicationContext());

        sessionManager =new SessionManager(getApplicationContext());

        mKaziApp = (KaziApp) getApplicationContext();
        mImgAvatar =(ImageView)findViewById(R.id.avatar);

        initFields();

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

        ltyNoriskp  = (RelativeLayout)findViewById(R.id.lytNoRiskP) ;

        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail= (TextView) findViewById(R.id.txtUserEmail);

        String unit = "kg.m<sup>2</sup>";
        ((TextView) findViewById(R.id.txtBMIUnit)).setText(Html.fromHtml(unit));

        AppExecutors.getInstance().getDiskIO().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        // HashMap<String, String >userMap = sessionManager.getUserDetails();
                        User currentUser = kDB.userDao().findUserByUID(sessionManager.getUserDetails().get(SessionManager.KEY_UID));
                        fillRecord(currentUser);
                    }
                }
        );


//        if(cUser!=null)
//        {
//            fillRecord();
//        }else{
//            if(mKaziApp.get_grUser()!=null && mKaziApp.get_grUser().getId()!=0){
//
//
//                cUser = mKaziApp.get_grUser();
//                fillRecord();
//            }else {
//                HashMap<String, String> user = sessionManager.getUserDetails();
//                String fullname =  user.get(SessionManager.KEY_NAME);
//                try{
//                    int id = Integer.parseInt(user.get(SessionManager.KEY_UID));
//
//                    mKaziApp.set_grUser(new User(id, fullname,
//                            user.get(SessionManager.KEY_EMAIL),
//                            user.get(SessionManager.KEY_DOB),
//                            user.get(SessionManager.KEY_PASSWORD),
//                            user.get(SessionManager.KEY_GENDER)
//                    ));
//
//                    cUser = mKaziApp.get_grUser();
//                    fillRecord();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

//            }
//        }
        setNoRiskPanel();
        initDrawer();

        subscribeToPushService();
    }


    void initDrawer()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = (TextView) headerView.findViewById(R.id.txtNavName);
        TextView navEmail = (TextView) headerView.findViewById(R.id.txtNavEmail);

        ImageView navAvatar = (ImageView) headerView.findViewById(R.id.avatar);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(cUser.getProfilePic(), 0, cUser.getProfilePic().length);
        } catch (NullPointerException ne) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        }

        navAvatar.setImageBitmap(bitmap);


        navName.setText(mKaziApp.get_grUser().getName());
        navEmail.setText(mKaziApp.get_grUser().getEmail());



    }

    void initFields() {
        txtbpcolor = findViewById(R.id.txtbpcolor);
        txtchlcolor = findViewById(R.id.txtchlcolor);
        txtbmicolor = findViewById(R.id.txtbmicolor);
        txtbglcolor = findViewById(R.id.txtbglcolor);

        txtbpmeasurement = findViewById(R.id.txtbpmeasurement);
        txtchlmeasurement = findViewById(R.id.txtchlmeasurement);
        txtbmimeasurement = findViewById(R.id.txtbmimeasurement);
        txtbglmeasurement = findViewById(R.id.txtbglmeasurement);

        txtbpcomment = findViewById(R.id.txtbpcomment);
        txtchlcomment = findViewById(R.id.txtchlcomment);
        txtbmicomment = findViewById(R.id.txtbmicomment);
        txtbglcomment = findViewById(R.id.txtbglcomment);

    }


    void setNoRiskPanel()
    {
        if(cUser != null){

            String id = String.valueOf(cUser.getId());

            if(!checkRiskProfile(id)){
                ltyNoriskp.setVisibility(View.VISIBLE);
            }else{
                ltyNoriskp.setVisibility(View.GONE);
            }
        }


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
        getMenuInflater().inflate(R.menu.risk_profile, menu);
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

        } else if (id == R.id.nav_tips) {
            startActivity(new Intent(getApplicationContext(), Tips.class));
        } else if (id == R.id.nav_apps) {
            startActivity(new Intent(getApplicationContext(), Apps.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }else if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void retrieveUser(String email, String password) {
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(ContentTypes.USER_CONTENT_URI, UserColumns.PROJECTION, UserColumns.EMAIL + " = ?" + " AND " + UserColumns.PASSWORD + " = ?",
                new String[]{email, password}, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId((cursor.getString(0)) != null ? Integer.parseInt(cursor.getString(0)) : 0);
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserColumns.EMAIL)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserColumns.NAME)));
                user.setDob(cursor.getString(cursor.getColumnIndex(UserColumns.DOB)));
                user.setGender(cursor.getString(cursor.getColumnIndex(UserColumns.GENDER)));

                try{
                    user.setProfilePic(cursor.getBlob(cursor.getColumnIndex(UserColumns.PROFILE_IMAGE)));

                }catch (Exception e){
                    e.printStackTrace();
                }

                mKaziApp.set_grUser(user);
                cUser = user;

            } while (cursor.moveToNext());
        }


        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }


    }
    //TODO replace Database functions
    public RiskProfileItem retrieveRecord(String userID, String riskId) {



          RiskProfileItem risk = new RiskProfileItem();


                        // HashMap<String, String >userMap = sessionManager.getUserDetails();
        risk = kDB.riskProfileDao().getRiskProfileItem(Integer.parseInt(userID), Integer.parseInt(riskId));



//        ContentResolver resolver = getContentResolver();
//        // cursor = resolver.query(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, RecordedActivtyColumns.PROJECTION,
//        // RecordedActivtyColumns.TIME_CREATED +">=? and " + RecordedActivtyColumns.TIME_CREATED+"<=?", new String[]{startDate, endDate},null);
//
//        Cursor cursor = resolver.query(ContentTypes.RISK_PROFILE_CONTENT_URI, RiskProfileColumns.PROJECTION, RiskProfileColumns.USER_ID + " = ?" + " AND " + RiskProfileColumns.RISK_ITEM_ID + " = ?",
//                new String[]{userID, riskId}, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                RiskProfileItem riskProfileItem = new RiskProfileItem();
//                riskProfileItem.setId((cursor.getString(0)) != null ? Integer.parseInt(cursor.getString(0)) : 0);
//                riskProfileItem.setRisk_item_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.RISK_ITEM_ID))));
//                riskProfileItem.setUser_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.USER_ID))));
//                riskProfileItem.setMeasurement(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.MEASUREMENT)));
//                riskProfileItem.setComment(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.COMMENT)));
//
//                risk = riskProfileItem;
//
//
//
//
//
//            } while (cursor.moveToNext());
//        }
        return risk;

    }


    public void fillRecord(User user) {
        cUser = user;
        mKaziApp.set_grUser(user);
       txtName.setText(cUser.getName());
       txtEmail.setText(cUser.getEmail());

        rBp = retrieveRecord(String.valueOf(cUser.getId()), String.valueOf(RiskItemID.RID_BP));
        rBgl = retrieveRecord(String.valueOf(cUser.getId()), String.valueOf(RiskItemID.RID_BGL));
        rChol = retrieveRecord(String.valueOf(cUser.getId()), String.valueOf(RiskItemID.RID_CHL));
        rBMI = retrieveRecord(String.valueOf(cUser.getId()), String.valueOf(RiskItemID.RID_BMI));
        rW2H = retrieveRecord(String.valueOf(cUser.getId()), String.valueOf(RiskItemID.RID_W2H));

        //Set the BMI Fields
        try{
            setBMIFields(rBMI);
        }catch(Exception e){
            e.printStackTrace();
        }
        //Set the Blood Glucose Levels Fields
        try{
            setBGLFields(rBgl);
        }catch(Exception e){
            e.printStackTrace();
        }
        //Set the Cholesterol Levels Fields
        try{
            setCHLFields(rChol);
        }catch(Exception e){
            e.printStackTrace();
        }
        //Set the Blood Pressure Levels Fields
        try {
            setBPFields(rBp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(cUser.getProfilePic()!=null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(cUser.getProfilePic(), 0, cUser.getProfilePic().length);
            mImgAvatar.setImageBitmap(bitmap);

        }

    }


    public void setBMIFields(RiskProfileItem riskBMI) {
        String comment = "";

        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);  //getResources().getColor(R.color.riskColorlow);
        int colorModerateRisk =ContextCompat.getColor(this, R.color.riskColorModerate);//  getResources().getColor(R.color.riskColorModerate);
        int colorHighRisk =ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;
        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgmoderate,null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgfill,null);

        Drawable used= null;

        double measurement = Double.parseDouble(riskBMI.getMeasurement());

        if (measurement < 18.5) {
            color = colorLowRisk;
            used = dotLow;
        } else if (measurement > 18.5 && measurement <= 24.9) {
            color = colorLowRisk;
            used = dotLow;
        } else if (measurement > 24.9 && measurement <= 29.9) {
            color = colorModerateRisk;
            used = dotModerate;
        } else if (measurement > 29.9 && measurement <= 34.9) {
            color = colorModerateRisk;
            used = dotModerate;
        } else if (measurement > 34.9 && measurement <= 39.9) {
            color = colorHighRisk;
            used = dotHigh;
        } else if (measurement >= 40.0) {
            color = colorHighRisk;
            used = dotHigh;
        }


        txtbmicolor.setTextColor(color);
        txtbmicolor.setBackground(used);
        txtbmimeasurement.setText(riskBMI.getMeasurement());
        txtbmimeasurement.setTextColor(color);

        txtbmicomment.setText(riskBMI.getComment());
        txtbmicomment.setTextColor(color);


    }

    public void setBGLFields(RiskProfileItem riskBGL) {
        String comment = "";
        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);  //getResources().getColor(R.color.riskColorlow);
        int colorModerateRisk =ContextCompat.getColor(this, R.color.riskColorModerate);//  getResources().getColor(R.color.riskColorModerate);
        int colorHighRisk =ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;

        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgmoderate,null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgfill,null);

        Drawable used= null;



        double measurement = Double.parseDouble(riskBGL.getMeasurement());

        if (measurement < 7.8) {
            color = colorLowRisk;
            used = dotLow;
            comment = "Low risk";
        } else if (measurement >= 7.9 && measurement <= 11.1) {
            color = colorModerateRisk;
            comment = "Moderate";
            used =dotModerate;
        } else if (measurement >= 11.2) {
            color = colorHighRisk;
            used=dotHigh;
            comment = "High risk";
        }


        txtbglcolor.setTextColor(color);
        txtbglcolor.setBackground(used);
        txtbglmeasurement.setText(riskBGL.getMeasurement());
        txtbglmeasurement.setTextColor(color);

        //txtbglcomment.setText(riskBGL.getComment());
        txtbglcomment.setText(comment);
        txtbglcomment.setTextColor(color);


    }

    public void setCHLFields(RiskProfileItem risk) {
        String comment = "";
        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);  //getResources().getColor(R.color.riskColorlow);
        int colorModerateRisk =ContextCompat.getColor(this, R.color.riskColorModerate);//  getResources().getColor(R.color.riskColorModerate);
        int colorHighRisk =ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;

        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgmoderate,null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgfill,null);

        Drawable used= null;



        double measurement = Double.parseDouble(risk.getMeasurement());

        if (measurement < 5.2) {
            color = colorLowRisk;
            used = dotLow;
            comment = "Low risk";
        } else if (measurement >= 5.2 && measurement <= 6.2) {
            color = colorModerateRisk;
            comment = "Moderate";
            used =dotModerate;
        } else if (measurement >= 6.21) {
            color = colorHighRisk;
            used=dotHigh;
            comment = "High risk";
        }

        txtchlcolor.setTextColor(color);
        txtchlcolor.setBackground(used);
        txtchlmeasurement.setText(risk.getMeasurement());
        txtchlmeasurement.setTextColor(color);

        //txtbglcomment.setText(riskBGL.getComment());
        txtchlcomment.setText(comment);
        txtchlcomment.setTextColor(color);


    }

    public void setBPFields(RiskProfileItem risk) {
        String comment = "";
        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);  //getResources().getColor(R.color.riskColorlow);
        int colorModerateRisk =ContextCompat.getColor(this, R.color.riskColorModerate);//  getResources().getColor(R.color.riskColorModerate);
        int colorHighRisk =ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;

        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgmoderate,null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(),R.drawable.circlebgfill,null);

        Drawable used= null;

        String [] bp = risk.getMeasurement().split("/");
        int systolic =Integer.parseInt( bp[0]);
        int diastolic =Integer.parseInt( bp[1]);


       /* double measurement = 0.3;

        if (measurement < 0.35) {
            color = colorLowRisk;
            used = dotLow;
            comment = "LOW RISK";
        } else if (measurement > 0.35 && measurement <= 1) {
            color = colorLowRisk;
            comment = "LOW RISK";
            used =dotModerate;
        } else if (measurement >= 1) {
            color = colorHighRisk;
            used=dotHigh;
            comment = "HIGH RISK";
        }*/



        if (systolic <129 || diastolic <84) {
            color = colorLowRisk;
            used = dotLow;
            comment = "Low risk";
        } else if ((systolic >130 && systolic <= 139)||(diastolic > 84 && diastolic <= 89) ) {
            color = colorLowRisk;
            comment = "Moderate";
            used =dotModerate;
        } else if (systolic >= 140 || diastolic >= 90) {
            color = colorHighRisk;
            used=dotHigh;
            comment = "High risk";
        }



       // color = colorLowRisk;
        txtbpcolor.setTextColor(color);
        txtbpcolor.setBackground(used);
        txtbpmeasurement.setText(systolic+"\n/"+diastolic);
        txtbpmeasurement.setTextColor(color);

        //txtbglcomment.setText(riskBGL.getComment());
        txtbpcomment.setText(comment);
        txtbpcomment.setTextColor(color);


    }



    boolean checkRiskProfile(String id)
    {
        ContentResolver resolver = getContentResolver();
        // cursor = resolver.query(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, RecordedActivtyColumns.PROJECTION,
        // RecordedActivtyColumns.TIME_CREATED +">=? and " + RecordedActivtyColumns.TIME_CREATED+"<=?", new String[]{startDate, endDate},null);

        Cursor cursor = resolver.query(ContentTypes.RISK_PROFILE_CONTENT_URI, RiskProfileColumns.PROJECTION,RiskProfileColumns.USER_ID+ " = ?" ,
                new String[]{id}, null);



        int cursorCount = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if(cursorCount>0)
        {
            return true;
        }

        return false;

    }



    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
           // finish();
           // startActivity(new Intent(this, RiskProfile.class));
        }else{
            startActivity(new Intent(this, Login_Firebase.class));
        }
    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("kazi");

        Log.d("kazi", "Subscribed");
        //  Toast.makeText(Home.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        //   Log.d("kazi", token);
        // Toast.makeText(Home.this, token, Toast.LENGTH_SHORT).show();
    }


}