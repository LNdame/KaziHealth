package mandela.cct.ansteph.kazihealth.view.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.List;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.RiskItemID;
import mandela.cct.ansteph.kazihealth.app.KaziApp;
import mandela.cct.ansteph.kazihealth.data.AppExecutors;
import mandela.cct.ansteph.kazihealth.data.KaziDatabase;
import mandela.cct.ansteph.kazihealth.helper.SessionManager;
import mandela.cct.ansteph.kazihealth.model.RiskItem;
import mandela.cct.ansteph.kazihealth.model.RiskProfileItem;
import mandela.cct.ansteph.kazihealth.model.User;
import mandela.cct.ansteph.kazihealth.utils.PdfReport;
import mandela.cct.ansteph.kazihealth.view.appmanagement.Apps;
import mandela.cct.ansteph.kazihealth.view.firebasereg.Login_Firebase;
import mandela.cct.ansteph.kazihealth.view.tip.About;
import mandela.cct.ansteph.kazihealth.view.tip.Tips;

public class RiskProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    public static final int PERMISSION_REQUEST_CODE = 0;
    private static String TAG = RiskProfile.class.getSimpleName();
    KaziApp mKaziApp;
    TextView txtName, txtEmail;
    String mEmail, mPwd;
    TextView txtbpcolor, txtchlcolor, txtbmicolor, txtbglcolor;
    TextView txtbpmeasurement, txtchlmeasurement, txtbmimeasurement, txtbglmeasurement;
    TextView txtbpcomment, txtchlcomment, txtbmicomment, txtbglcomment;
    RiskProfileItem rBp, rHeartRate, rChol, rBgl, rBMI, rW2H;
    ImageView mImgAvatar;
    RelativeLayout ltyNoriskp;
    SessionManager sessionManager;
    private KaziDatabase kDB;
    User cUser;
    FirebaseAuth mAuth;
    List<RiskProfileItem> riskProfileItemList;
    List<RiskItem> riskItemList;

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

        sessionManager = new SessionManager(getApplicationContext());

        mKaziApp = (KaziApp) getApplicationContext();
        mImgAvatar = (ImageView) findViewById(R.id.avatar);

        initFields();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ltyNoriskp = (RelativeLayout) findViewById(R.id.lytNoRiskP);

        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtUserEmail);

        String unit = "kg.m<sup>2</sup>";
        ((TextView) findViewById(R.id.txtBMIUnit)).setText(Html.fromHtml(unit));

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                // HashMap<String, String >userMap = sessionManager.getUserDetails();
                User currentUser = kDB.userDao().findUserByUID(sessionManager.getUserDetails().get(SessionManager.KEY_UID));

                fillRecord(currentUser);
            }
        });

        initDrawer();
        subscribeToPushService();
    }

    void initDrawer() {
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

    void setNoRiskPanel() {
        if (cUser != null) {
            int id = cUser.getId();
            AppExecutors.getInstance().getMainThread().execute(() -> {
                List<RiskProfileItem> riskProfileItems = kDB.riskProfileDao().getAllRiskProfileItem(id);
                if (riskProfileItems.size() > 0) {
                    ltyNoriskp.setVisibility(View.VISIBLE);
                } else {
                    ltyNoriskp.setVisibility(View.GONE);
                }
            });
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
        if (id == R.id.action_export) {
            if (!checkPermissions()) {
                requestPermissions();
            } else {
                PdfReport pdfReport = new PdfReport(this, riskProfileItemList, riskItemList, cUser);
                pdfReport.createPdf(new File(getExternalFilesDir("report_data"), "riskreport.pdf").getPath());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), Login_Firebase.class));
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
        } else if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public RiskProfileItem retrieveRecord(String userID, String riskId) {
        RiskProfileItem risk = new RiskProfileItem();
        risk = kDB.riskProfileDao().getRiskProfileItem(Integer.parseInt(userID), Integer.parseInt(riskId));
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
        try {
            setBMIFields(rBMI);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Set the Blood Glucose Levels Fields
        try {
            setBGLFields(rBgl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Set the Cholesterol Levels Fields
        try {
            setCHLFields(rChol);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Set the Blood Pressure Levels Fields
        try {
            setBPFields(rBp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cUser.getProfilePic() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(cUser.getProfilePic(), 0, cUser.getProfilePic().length);
            mImgAvatar.setImageBitmap(bitmap);
        }

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                riskProfileItemList = kDB.riskProfileDao().getAllRiskProfileItem(user.getId());
                riskItemList = kDB.riskItemDao().getAllRiskItem();
            }
        });
    }

    public void setBMIFields(RiskProfileItem riskBMI) {
        String comment = "";

        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);
        int colorModerateRisk = ContextCompat.getColor(this, R.color.riskColorModerate);
        int colorHighRisk = ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;
        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgmoderate, null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgfill, null);

        Drawable used = null;

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
            color = colorHighRisk;
            used = dotHigh;
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
        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);
        int colorModerateRisk = ContextCompat.getColor(this, R.color.riskColorModerate);
        int colorHighRisk = ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;

        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgmoderate, null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgfill, null);

        Drawable used = null;

        double measurement = Double.parseDouble(riskBGL.getMeasurement());

        if (measurement < 7.8) {
            color = colorLowRisk;
            used = dotLow;
            comment = "Low risk";
        } else if (measurement >= 7.9 && measurement <= 11.1) {
            color = colorModerateRisk;
            comment = "Moderate";
            used = dotModerate;
        } else if (measurement >= 11.2) {
            color = colorHighRisk;
            used = dotHigh;
            comment = "High risk";
        }

        txtbglcolor.setTextColor(color);
        txtbglcolor.setBackground(used);
        txtbglmeasurement.setText(riskBGL.getMeasurement());
        txtbglmeasurement.setTextColor(color);
        txtbglcomment.setText(comment);
        txtbglcomment.setTextColor(color);
    }

    public void setCHLFields(RiskProfileItem risk) {
        String comment = "";
        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);
        ;
        int colorModerateRisk = ContextCompat.getColor(this, R.color.riskColorModerate);
        int colorHighRisk = ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;

        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgmoderate, null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgfill, null);

        Drawable used = null;
        double measurement = Double.parseDouble(risk.getMeasurement());

        if (measurement < 5.2) {
            color = colorLowRisk;
            used = dotLow;
            comment = "Low risk";
        } else if (measurement >= 5.2 && measurement <= 6.2) {
            color = colorModerateRisk;
            comment = "Moderate";
            used = dotModerate;
        } else if (measurement >= 6.21) {
            color = colorHighRisk;
            used = dotHigh;
            comment = "High risk";
        }

        txtchlcolor.setTextColor(color);
        txtchlcolor.setBackground(used);
        txtchlmeasurement.setText(risk.getMeasurement());
        txtchlmeasurement.setTextColor(color);
        txtchlcomment.setText(comment);
        txtchlcomment.setTextColor(color);
    }

    public void setBPFields(RiskProfileItem risk) {
        String comment = "";
        int colorLowRisk = ContextCompat.getColor(this, R.color.riskColorlow);
        int colorModerateRisk = ContextCompat.getColor(this, R.color.riskColorModerate);
        int colorHighRisk = ContextCompat.getColor(this, R.color.riskColorHigh);

        int color = colorLowRisk;

        Drawable dotLow = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebglow, null);
        Drawable dotModerate = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgmoderate, null);
        Drawable dotHigh = ResourcesCompat.getDrawable(this.getResources(), R.drawable.circlebgfill, null);

        Drawable used = null;

        String[] bp = risk.getMeasurement().split("/");
        int systolic = Integer.parseInt(bp[0]);
        int diastolic = Integer.parseInt(bp[1]);

        if (systolic < 129 || diastolic < 84) {
            color = colorLowRisk;
            used = dotLow;
            comment = "Low risk";
        } else if ((systolic > 130 && systolic <= 139) || (diastolic > 84 && diastolic <= 89)) {
            color = colorLowRisk;
            comment = "Moderate";
            used = dotModerate;
        } else if (systolic >= 140 || diastolic >= 90) {
            color = colorHighRisk;
            used = dotHigh;
            comment = "High risk";
        }

        txtbpcolor.setTextColor(color);
        txtbpcolor.setBackground(used);
        txtbpmeasurement.setText(systolic + "\n/" + diastolic);
        txtbpmeasurement.setTextColor(color);
        txtbpcomment.setText(comment);
        txtbpcomment.setTextColor(color);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            // finish();
            // startActivity(new Intent(this, RiskProfile.class));
        } else {
            startActivity(new Intent(this, Login_Firebase.class));
        }
    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.default_notification_channel_name))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                    }
                });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
            }
        });
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(this, RiskProfile.class));
            } else {
                this.finish();
            }
        }
    }
}