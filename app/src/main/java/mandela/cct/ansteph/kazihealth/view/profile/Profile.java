package mandela.cct.ansteph.kazihealth.view.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.RestAPI;
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

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    KaziApp mKaziApp;
    TextView txtName, txtEmail;

    CircleImageView circleImageView;
    private static final int ID_KHN_INPUT_DIALOG = R.id.lytKhNumber;
    private LovelySaveStateHandler saveStateHandler;

    RadioButton radYes, radNo;
    LinearLayout lytKhNumber, lytKBpart;
    EditText edtKhNumber;
    Button btnImport;

    private static final int RID_BP = 1;
    private static final int RID_HR = 2;
    private static final int RID_CHL = 3;
    private static final int RID_BGL = 4;
    private static final int RID_HEIGHT = 5;
    private static final int RID_WEIGHT = 6;
    private static final int RID_BMI = 7;
    private static final int RID_WAIST = 8;
    private static final int RID_HIP = 9;
    private static final int RID_W2H = 10;

    RiskProfileItem rBp, rHeartRate, rChol, rBgl, rHeight, rWeight, rBMI, rWaist, rHip, rW2H;
    User cUser;
    FirebaseAuth mAuth;
    SessionManager sessionManager;
    private KaziDatabase kDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        kDB = KaziDatabase.getInstance(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        mKaziApp = (KaziApp) getApplicationContext();
        saveStateHandler = new LovelySaveStateHandler();

        circleImageView = (CircleImageView) findViewById(R.id.avatar);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });

        lytKhNumber = (LinearLayout) findViewById(R.id.lytKhNumber);
        lytKBpart = (LinearLayout) findViewById(R.id.lytKBpart);
        lytKhNumber.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtUserEmail);

        if (mKaziApp.get_grUser() != null) {
            cUser = mKaziApp.get_grUser();
            txtName.setText(mKaziApp.get_grUser().getName());
            txtEmail.setText(mKaziApp.get_grUser().getEmail());

            if (mKaziApp.get_grUser().getProfilePic() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(mKaziApp.get_grUser().getProfilePic(), 0, mKaziApp.get_grUser().getProfilePic().length);
                circleImageView.setImageBitmap(bitmap);
            }
        }

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

        radYes = (RadioButton) findViewById(R.id.radYes);
        radNo = (RadioButton) findViewById(R.id.radNo);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rdgParti);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (radYes.isChecked()) {
                    lytKBpart.setVisibility(View.VISIBLE);
                    //  lytKhNumber.setVisibility(View.VISIBLE);
                }
                if (radNo.isChecked()) {
                    lytKBpart.setVisibility(View.GONE);
                    //  lytKhNumber.setVisibility(View.GONE);
                }
            }
        });

        edtKhNumber = (EditText) findViewById(R.id.editKHNumber);
    }

    void initDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = (TextView) headerView.findViewById(R.id.txtNavName);
        TextView navEmail = (TextView) headerView.findViewById(R.id.txtNavEmail);
        ImageView navAvatar = (ImageView) headerView.findViewById(R.id.avatar);

        if (mKaziApp.get_grUser().getProfilePic() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(mKaziApp.get_grUser().getProfilePic(), 0, mKaziApp.get_grUser().getProfilePic().length);
            navAvatar.setImageBitmap(bitmap);
        }
        navName.setText(mKaziApp.get_grUser().getName());
        navEmail.setText(mKaziApp.get_grUser().getEmail());
    }

    public void onRedoAssessmentClicked(View view) {
        startActivity(new Intent(getApplicationContext(), UploadProfile.class));
    }


    public void onImportAssessmentClicked(View view) {
        RestAPI rAPI = new RestAPI();
        String userID = edtKhNumber.getText().toString();
        JSONObject userData;
        new DownloadUserData().execute(userID);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveStateHandler.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (LovelySaveStateHandler.wasDialogOnScreen(savedState)) {
            //Dialog won't be restarted automatically, so we need to call this method.
            //Each dialog knows how to restore its state
            showLovelyDialog(LovelySaveStateHandler.getSavedDialogId(savedState), savedState);
        }
    }

    private void showLovelyDialog(int dialogId, Bundle savedInstanceState) {
        switch (dialogId) {
            case ID_KHN_INPUT_DIALOG:
                showKHNumberInputDialog(savedInstanceState);
                break;
        }
    }


    private void showKHNumberInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.text_kh_title)
                .setMessage(R.string.text_input_khnumber)
                .setIcon(R.mipmap.ic_id)
                .setInstanceStateHandler(ID_KHN_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^/^([a-zA-Z0-9_-]){3,5}$/"); //"^\\d{1,3}\\/\\d{1,3}$"  /^([a-zA-Z0-9_-]){3,5}$/
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ((TextView) findViewById(R.id.txtKhNumber)).setText(text + " ");
                        // rHeartRate = new RiskProfileItem(cUser.getId(), RID_HR,text,"");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))

                .show();
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
        getMenuInflater().inflate(R.menu.profile, menu);
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
            // do nothing
        } else if (id == R.id.nav_risk) {
            startActivity(new Intent(getApplicationContext(), RiskProfile.class));
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

    @Override
    public void onClick(View v) {
        showLovelyDialog(v.getId(), null);
    }

    private void recordProfileItem(RiskProfileItem... riskProfileItems) {
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            kDB.riskProfileDao().deleteAll();
            kDB.riskProfileDao().insertAll(riskProfileItems);
        });
    }


    private class DownloadUserData extends AsyncTask<String, String, JSONObject> {
       /* private String userID;
        public DownloadUserData(String userID) {
            this.userID = userID;
        }*/

        @Override
        protected JSONObject doInBackground(String... strings) {
            RestAPI rAPI = new RestAPI();
            JSONObject userData;
            try {
                userData = rAPI.KaziHealthParametersByTeacherUniqueID(strings[0], "kaziWSuserTOconsume", "43007e66a22569f6b7e0682d83ce824b91bed696");
                return userData;
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Unable to connect to KaziBantu server try again later", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            // super.onPostExecute(jsonObject);
            recordUserData(jsonObject);
        }
    }


    public void recordUserData(JSONObject jsonObject) {
        JSONObject jo = jsonObject;
        String value = null;
        JSONArray fromStringValue = null;
        try {
            value = jo.getString("Value");
            fromStringValue = new JSONArray(value);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (value != null) {
            try {
                JSONObject userdata = fromStringValue.getJSONObject(0);
                String bp = userdata.getString("ceBPOutcomeValue1") + "/" + userdata.getString("ceBPOutcomeValue2");
                String hr = userdata.getString("ceHROutcomeValue");
                String chol = userdata.getString("ceTotalCholesterol");
                String bgl = userdata.getString("ceAlereHbA1cMol");
                String height = userdata.getString("aabcHeight");
                String weight = userdata.getString("aabcWeight");
                String bmi = userdata.getString("aabcBMI");
                String waist = userdata.getString("aabcWaist");
                String hip = userdata.getString("aabcHip");
                String w2h = userdata.getString("aabcWaistToHipValue");

                rBp = new RiskProfileItem(cUser.getId(), RID_BP, bp, "");
                rHeartRate = new RiskProfileItem(cUser.getId(), RID_HR, hr, "");
                rChol = new RiskProfileItem(cUser.getId(), RID_CHL, chol, "");
                rBgl = new RiskProfileItem(cUser.getId(), RID_BGL, bgl, "");
                rHeight = new RiskProfileItem(cUser.getId(), RID_HEIGHT, height, "");
                rWeight = new RiskProfileItem(cUser.getId(), RID_WEIGHT, weight, "");
                rBMI = new RiskProfileItem(cUser.getId(), RID_BMI, bmi, userdata.getString("BMIClassification"));
                rWaist = new RiskProfileItem(cUser.getId(), RID_WAIST, waist, "");
                rHip = new RiskProfileItem(cUser.getId(), RID_HIP, hip, "");
                rW2H = new RiskProfileItem(cUser.getId(), RID_W2H, w2h, userdata.getString("WaistToHipRatioClassification"));

                recorded();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void recorded() {
        RiskProfileItem[] riskProfileItems = {
                rBp, rHeartRate, rChol, rBgl, rHeight, rWeight, rBMI, rWaist, rHip, rW2H
        };
        recordProfileItem(riskProfileItems);
        Toast.makeText(getApplicationContext(), "RisK Profile Saved", Toast.LENGTH_LONG).show();
    }


}
