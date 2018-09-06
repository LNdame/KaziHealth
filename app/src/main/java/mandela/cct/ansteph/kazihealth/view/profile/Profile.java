package mandela.cct.ansteph.kazihealth.view.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelySaveStateHandler;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import de.hdodenhof.circleimageview.CircleImageView;
import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.view.appmanagement.Apps;
import mandela.cct.ansteph.kazihealth.view.register.Login;
import mandela.cct.ansteph.kazihealth.view.tip.About;
import mandela.cct.ansteph.kazihealth.view.tip.Tips;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener {

    GlobalRetainer mGlobalRetainer;
    TextView txtName, txtEmail;

    CircleImageView circleImageView;
    private static final int ID_KHN_INPUT_DIALOG = R.id.lytKhNumber;
    private LovelySaveStateHandler saveStateHandler;

    RadioButton radYes, radNo;

    LinearLayout lytKhNumber,lytKBpart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGlobalRetainer = (GlobalRetainer)getApplicationContext();
        saveStateHandler = new LovelySaveStateHandler();

        circleImageView = (CircleImageView) findViewById(R.id.avatar);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditProfile.class));
            }
        });


        lytKhNumber = (LinearLayout) findViewById(R.id.lytKhNumber);
        lytKBpart= (LinearLayout) findViewById(R.id.lytKBpart);

        lytKhNumber.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail= (TextView) findViewById(R.id.txtUserEmail);;

        if(mGlobalRetainer.get_grUser()!=null)
        {
            //cUser = mGlobalRetainer.get_grUser();

            txtName.setText(mGlobalRetainer.get_grUser().getName());
            txtEmail.setText(mGlobalRetainer.get_grUser().getEmail());


            if(mGlobalRetainer.get_grUser().getProfilePic()!=null)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(mGlobalRetainer.get_grUser().getProfilePic(), 0, mGlobalRetainer.get_grUser().getProfilePic().length);
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
        radNo= (RadioButton) findViewById(R.id.radNo);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rdgParti);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected


             //   Toast
                if(radYes.isChecked())
                {
                    lytKBpart.setVisibility(View.VISIBLE);
                  //  lytKhNumber.setVisibility(View.VISIBLE);
                }
                if(radNo.isChecked())
                {
                    lytKBpart.setVisibility(View.GONE);
                  //  lytKhNumber.setVisibility(View.GONE);
                }
            }
        });


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

    public void onRedoAssessmentClicked(View view)
    {
        startActivity(new Intent(getApplicationContext(), UploadProfile.class));
    }


    public void onImportAssessmentClicked(View view)
    {
        Toast.makeText(getApplicationContext(),"Unable to connect to KaziBantu server try again later", Toast.LENGTH_LONG).show();

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
                        ( (TextView) findViewById(R.id.txtKhNumber)).setText(text +" ");
                        // rHeartRate = new RiskProfileItem(cUser.getId(), RID_HR,text,"");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
         //   startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_risk) {
            startActivity(new Intent(getApplicationContext(), RiskProfile.class));
        } else if (id == R.id.nav_tips) {
            startActivity(new Intent(getApplicationContext(), Tips.class));
        } else if (id == R.id.nav_apps) {
            startActivity(new Intent(getApplicationContext(), Apps.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }else if (id == R.id.nav_logout) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        showLovelyDialog(v.getId(), null);
    }
}
