package mandela.cct.ansteph.kazihealth.view.profile;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyDialogCompat;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.ContentTypes;
import mandela.cct.ansteph.kazihealth.api.columns.RiskProfileColumns;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.model.RiskProfileItem;
import mandela.cct.ansteph.kazihealth.model.User;

public class UploadProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int ID_BP_INPUT_DIALOG = R.id.lytBloodPressure;
    private static final int ID_TEXT_INPUT_DIALOG = R.id.lytBloodPressure;

    private static final int ID_HR_INPUT_DIALOG = R.id.lytHeartRate;
    private static final int ID_CHL_INPUT_DIALOG = R.id.lytCholesterol;
    private static final int ID_BGL_INPUT_DIALOG = R.id.lytBloodGlucose;
    private static final int ID_HG_INPUT_DIALOG = R.id.lytHeight;
    private static final int ID_WG_INPUT_DIALOG = R.id.lytWeight;
    private static final int ID_WAIST_INPUT_DIALOG = R.id.lytWaist;
    private static final int ID_HIP_INPUT_DIALOG = R.id.lytHip;

    private static final int ID_SAVED_DIALOG = R.id.btnSaveAssess;



    private static final int RID_BP= 1;
    private static final int RID_HR= 2;
    private static final int RID_CHL= 3;
    private static final int RID_BGL= 4;
    private static final int RID_HEIGHT= 5;
    private static final int RID_WEIGHT= 6;
    private static final int RID_BMI= 7;
    private static final int RID_WAIST= 8;
    private static final int RID_HIP= 9;
    private static final int RID_W2H= 10;



    private LovelySaveStateHandler saveStateHandler;
    LinearLayout lytBloodPressure ;
    LinearLayout lytHeartRate ;
    LinearLayout lytCholesterol ;
    LinearLayout lytBloodGlucose ;
    LinearLayout lytHeight ;
    LinearLayout lytWeight ;
    LinearLayout lytWaist ;
    LinearLayout lytHip ;


    double mWeight, mHeight, mWaist , mHip;

    GlobalRetainer mGlobalRetainer;

    TextView txtName, txtEmail;

    RiskProfileItem rBp, rHeartRate,rChol, rBgl,rHeight,rWeight,rBMI, rWaist,rHip, rW2H;
    Button btnSave;
    User cUser;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGlobalRetainer = (GlobalRetainer)getApplicationContext();

        circleImageView = (CircleImageView) findViewById(R.id.avatar);

        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail= (TextView) findViewById(R.id.txtUserEmail);;

        btnSave = (Button) findViewById(R.id.btnSaveAssess);

        if(mGlobalRetainer.get_grUser()!=null)
        {
            cUser = mGlobalRetainer.get_grUser();

            txtName.setText(cUser.getName());
            txtEmail.setText(cUser.getEmail());

            if(mGlobalRetainer.get_grUser().getProfilePic()!=null)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(mGlobalRetainer.get_grUser().getProfilePic(), 0, mGlobalRetainer.get_grUser().getProfilePic().length);
                circleImageView.setImageBitmap(bitmap);
            }
        }





        saveStateHandler = new LovelySaveStateHandler();

        lytBloodPressure = (LinearLayout) findViewById(R.id.lytBloodPressure);
        lytHeartRate = (LinearLayout) findViewById(R.id.lytHeartRate);
        lytCholesterol = (LinearLayout) findViewById(R.id.lytCholesterol);
        lytBloodGlucose = (LinearLayout) findViewById(R.id.lytBloodGlucose);
        lytHeight = (LinearLayout) findViewById(R.id.lytHeight);
        lytWeight = (LinearLayout) findViewById(R.id.lytWeight);
        lytWaist = (LinearLayout) findViewById(R.id.lytWaist);
        lytHip = (LinearLayout) findViewById(R.id.lytHip);


        lytBloodPressure.setOnClickListener(this);
        lytHeartRate.setOnClickListener(this);
        lytCholesterol.setOnClickListener(this);
        lytBloodGlucose.setOnClickListener(this);
        lytHeight.setOnClickListener(this);
        lytWeight.setOnClickListener(this);
        lytWaist.setOnClickListener(this);
        lytHip.setOnClickListener(this);

       // btnSave.setOnClickListener(this);

        mWeight=75; mHeight= 160;mWaist= 60 ; mHip= 60;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillRecord();


    }




    @Override
    public void onClick(View v) {
        //View's ID correspond to our constants, so we just pass it
        showLovelyDialog(v.getId(), null);
    }


    public void onSaveAssessClicked(View view){
        onDoneClicked();

        showLovelyDialog(view.getId(), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload_risk_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            onDoneClicked();
        }

        return super.onOptionsItemSelected(item);
    }



    private void showLovelyDialog(int dialogId, Bundle savedInstanceState) {
        switch (dialogId) {

            case ID_BP_INPUT_DIALOG:
                showBPInputDialog(savedInstanceState);
                break;
            case ID_HR_INPUT_DIALOG:
                showHeartRateInputDialog(savedInstanceState);
                break;
            case ID_CHL_INPUT_DIALOG:
                showCholesterolInputDialog(savedInstanceState);
                break;
            case ID_BGL_INPUT_DIALOG:
                showBGlucoseInputDialog(savedInstanceState);
                break;
            case ID_HG_INPUT_DIALOG:
                showHeightInputDialog(savedInstanceState);
                break;
            case ID_WG_INPUT_DIALOG:
                showWeightInputDialog(savedInstanceState);
                break;
            case ID_WAIST_INPUT_DIALOG:
                showWaistInputDialog(savedInstanceState);
                break;
            case ID_HIP_INPUT_DIALOG:
                showHipInputDialog(savedInstanceState);
                break;
            case ID_SAVED_DIALOG:
                showSavedDialog(savedInstanceState);
                break;


        }
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





    private void showBPInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.bp_input_title)
                .setMessage(R.string.text_input_bp)
                .setIcon(R.mipmap.ic_pro_bp)
                .setInstanceStateHandler(ID_BP_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}\\/\\d{1,3}$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtBloodPres)).setText(text +" mmHg");

                        rBp = new RiskProfileItem(cUser.getId(), RID_BP,text,"");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }




    private void showHeartRateInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.hr_input_title)
                .setMessage(R.string.text_input_hr)
                .setIcon(R.mipmap.ic_pro_heart)
                .setInstanceStateHandler(ID_HR_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtHeartRate)).setText(text +" bpm");
                        rHeartRate = new RiskProfileItem(cUser.getId(), RID_HR,text,"");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }



    private void showCholesterolInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.chl_input_title)
                .setMessage(R.string.text_input_ch)
                .setIcon(R.mipmap.ic_pro_cholesterol)
                .setInstanceStateHandler(ID_CHL_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d+(\\.\\d{1,2})?$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtCholesterol)).setText(text +" mmol/L");
                        rChol = new RiskProfileItem(cUser.getId(), RID_CHL,text,"");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }


    private void showBGlucoseInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.bgl_input_title)
                .setMessage(R.string.text_input_bg)
                .setIcon(R.mipmap.ic_pro_bloodglucose)
                .setInstanceStateHandler(ID_BGL_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d+(\\.\\d{1,2})?$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtBloodGlu)).setText(text +" mmol/L");
                        rBgl = new RiskProfileItem(cUser.getId(), RID_BGL,text,"");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }




    private void showHeightInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.hg_input_title)
                .setMessage(R.string.text_input_hg)
                .setIcon(R.mipmap.ic_pro_height)
                .setInstanceStateHandler(ID_HG_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}(\\.\\d{1,2})?$");
                    } // ^\d{1,3}(\.\d{1,2})?$  //^\d{1,3}$
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtHeight)).setText(text +" cm");

                        rHeight = new RiskProfileItem(cUser.getId(), RID_HEIGHT,text,"");

                        calculateBMI(null,Double.parseDouble(text));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }



    private void showWeightInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.wg_input_title)
                .setMessage(R.string.text_input_wg)
                .setIcon(R.mipmap.ic_pro_weight)
                .setInstanceStateHandler(ID_WG_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}(\\.\\d{1,2})?$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtBloodWeight)).setText(text +" kg");
                        rWeight = new RiskProfileItem(cUser.getId(), RID_WEIGHT,text,"");
                        calculateBMI(Double.parseDouble(text),null);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }




    private void showBMIInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.text_input_title)
                .setMessage(R.string.text_input_message)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setInstanceStateHandler(ID_TEXT_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }




    private void showWaistInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.waist_input_title)
                .setMessage(R.string.text_input_wt)
                .setIcon(R.mipmap.ic_pro_waist)
                .setInstanceStateHandler(ID_WAIST_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}(\\.\\d{1,2})?$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtWaist)).setText(text +" cm");
                        rWaist = new RiskProfileItem(cUser.getId(), RID_WAIST,text,"");
                        calculateW2Hratio( Double.parseDouble(text),null);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }





    private void showHipInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.hip_input_title)
                .setMessage(R.string.text_input_hip)
                .setIcon(R.mipmap.ic_pro_hip)
                .setInstanceStateHandler(ID_HIP_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}(\\.\\d{1,2})?$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtHip)).setText(text +" cm");
                        rHip = new RiskProfileItem(cUser.getId(), RID_HIP,text,"");
                        calculateW2Hratio(null, Double.parseDouble(text));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }



    private void showSavedDialog(Bundle savedInstanceState) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes( R.color.colorAccent)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.ic_save_white)
                .setTitle(R.string.text_save_title)
                .setInstanceStateHandler(ID_SAVED_DIALOG, saveStateHandler)
                .setSavedInstanceState(savedInstanceState)
                .setMessage(R.string.text_save_profile)
                .setPositiveButton(R.string.text_save_ok, LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),RiskProfile.class);
                               // intent.setData(Uri.parse("market://details?id=ee.mtakso.client&hl=en"));
                                startActivity(intent);

                            }
                        })

                )
                .setNegativeButton(R.string.text_save_cancel,LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }))
                .show();
    }


    void calculateBMI(@Nullable Double weight, @Nullable Double height)
    {

        if(weight== null)
        {
            weight = mWeight;
        }else{mWeight= weight;}

        if(height== null){height = mHeight;}else{ mHeight= height;}

        double bmi = (mWeight)/((mHeight/100)*(mHeight/100)) ;

        String comment = getBMIComment(bmi);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
        DecimalFormat df = (DecimalFormat)nf;

        nf.setMaximumFractionDigits(1);
        String val = nf.format(bmi);


      //  ( (TextView) findViewById(R.id.txtBMI)).setText(String.format("%.1f", bmi) + Html.fromHtml(" kg.m<sup>2</sup> |")+comment);
        ( (TextView) findViewById(R.id.txtBMI)).setText(val + Html.fromHtml(" kg.m<sup>2</sup> |")+comment);

       // rBMI = new RiskProfileItem(cUser.getId(), RID_BMI,String.format("%.1f", bmi),comment);
        rBMI = new RiskProfileItem(cUser.getId(), RID_BMI,val,comment);


    }

    void calculateW2Hratio(@Nullable Double waist, @Nullable Double hip)
    {

        if(waist== null)
        {
            waist = mWaist;
        }else{mWaist= waist;}

        if(hip== null){hip = mHip;}else{ mHip= hip;}

        double w2hratio = (mWaist)/(mHip) ;

        String comment = getW2HComment(w2hratio);

        ( (TextView) findViewById(R.id.txtWaistHipRatio)).setText(String.format("%.2f", w2hratio) +"|"+comment);

        rW2H = new RiskProfileItem(cUser.getId(), RID_W2H,String.format("%.2f", w2hratio),comment);


    }


    public void onDoneClicked()
    {
        //save all the record  rBp, rHeartRate,rChol, rBgl,rHeight,rWeight,rBMI, rWaist,rHip, rW2H;
        recordProfileItem(rBp);

        recordProfileItem(rHeartRate);
        recordProfileItem(rChol);
        recordProfileItem(rBgl);

        recordProfileItem(rHeight);
        recordProfileItem(rWeight);
        recordProfileItem(rBMI);
        recordProfileItem(rWaist);

        recordProfileItem(rHip);
        recordProfileItem(rW2H);


        Toast.makeText(getApplicationContext(),"RisK Profile Saved", Toast.LENGTH_LONG).show();


    }




    public int recordProfileItem(RiskProfileItem riskProfileItem){


        try {
            ContentValues values = new ContentValues();

          values.put(RiskProfileColumns.USER_ID, riskProfileItem.getUser_id()) ;
            values.put(RiskProfileColumns.RISK_ITEM_ID , riskProfileItem.getRisk_item_id()) ;
            values.put(RiskProfileColumns.MEASUREMENT , riskProfileItem.getMeasurement()) ;
            values.put(RiskProfileColumns.COMMENT , riskProfileItem.getComment()) ;


            getContentResolver().insert(ContentTypes.RISK_PROFILE_CONTENT_URI, values);

            return 1;


        }catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }

    }



    public RiskProfileItem retrieveRecord(String userID, String riskId){


        RiskProfileItem risk = new RiskProfileItem();
        ContentResolver resolver = getContentResolver();
        // cursor = resolver.query(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, RecordedActivtyColumns.PROJECTION,
        // RecordedActivtyColumns.TIME_CREATED +">=? and " + RecordedActivtyColumns.TIME_CREATED+"<=?", new String[]{startDate, endDate},null);

        Cursor cursor = resolver.query(ContentTypes.RISK_PROFILE_CONTENT_URI, RiskProfileColumns.PROJECTION,RiskProfileColumns.USER_ID+ " = ?" + " AND " + RiskProfileColumns.RISK_ITEM_ID + " = ?",
                new String[]{userID, riskId}, null);

        if(cursor.moveToFirst()){
            do{
                RiskProfileItem riskProfileItem = new RiskProfileItem();
                riskProfileItem.setId((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0);
                riskProfileItem.setRisk_item_id (Integer.parseInt(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.RISK_ITEM_ID))));
                riskProfileItem.setUser_id (Integer.parseInt(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.USER_ID))));
                riskProfileItem.setMeasurement(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.MEASUREMENT)));
                riskProfileItem.setComment(cursor.getString(cursor.getColumnIndex(RiskProfileColumns.COMMENT)));

               risk= riskProfileItem;


            }while(cursor.moveToNext());
        }
        return risk;

    }



    public void fillRecord(){

        rBp =retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_BP) );
        rHeartRate=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_HR) );
        rChol=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_CHL) );
        rBgl=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_BGL) );
        rHeight=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_HEIGHT) );
        rWeight=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_WEIGHT) );
        rBMI=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_BMI) );
        rWaist=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_WAIST) );
        rHip=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_HIP) );
        rW2H=retrieveRecord(String.valueOf(cUser.getId()) ,String.valueOf(RID_W2H) );


        String rBpValue = (rBp.getMeasurement()!=null)?rBp.getMeasurement():"0";
        String rHeartRateValue =(rHeartRate.getMeasurement()!=null)?rHeartRate.getMeasurement():"0";
        String rCholValue = (rChol.getMeasurement()!=null)?rChol.getMeasurement():"0" ;
        String rBglValue =(rBgl.getMeasurement()!=null)?rBgl.getMeasurement():"0" ;
        String rHeightValue =(rHeight.getMeasurement()!=null)?rHeight.getMeasurement():"0" ;
        String rWeightValue = (rWeight.getMeasurement()!=null)?rWeight.getMeasurement():"0";
        String rWaistValue = (rWaist.getMeasurement()!=null)?rWaist.getMeasurement():"0";
        String rHipValue = (rHip.getMeasurement()!=null)?rHip.getMeasurement():"0" ;





        ( (TextView) findViewById(R.id.txtBloodPres)).setText( rBpValue+" mmHg");
        ( (TextView) findViewById(R.id.txtHeartRate)).setText(rHeartRateValue +" bpm");
        ( (TextView) findViewById(R.id.txtCholesterol)).setText( rCholValue+" mmol/L");
        ( (TextView) findViewById(R.id.txtBloodGlu)).setText( rBglValue+" mmol/L");

        ( (TextView) findViewById(R.id.txtHeight)).setText( rHeightValue+" cm");
        ( (TextView) findViewById(R.id.txtBloodWeight)).setText(rWeightValue +" kg");
        ( (TextView) findViewById(R.id.txtWaist)).setText( rWaistValue+" cm");
        ( (TextView) findViewById(R.id.txtHip)).setText(rHipValue+" cm");

      //  ( (TextView) findViewById(R.id.txtWaistHipRatio)).setText(String.format("%.2f", rW2H.getMeasurement()) +"|"+rW2H.getComment());

        String rW2HValue =(rW2H.getMeasurement()!=null)?rW2H.getMeasurement():"0";
        String rW2HComment =(rW2H.getComment()!=null)?rW2H.getComment():" ";

        ( (TextView) findViewById(R.id.txtWaistHipRatio)).setText( rW2HValue +"|"+ rW2HComment);

        String rBMIValue =(rBMI.getMeasurement()!=null)?rBMI.getMeasurement():"0";
        String rBMIComment =(rBMI.getComment()!=null)?rBMI.getComment():" ";

          ( (TextView) findViewById(R.id.txtBMI)).setText(Html.fromHtml( rBMIValue +" kg.m<sup>2</sup>|"+rBMIComment));

    }


    public String getBMIComment(double measurement)
    {
        String comment="";

        if(measurement<18.5)
        {
            comment="Low risk";
        }else if (measurement>18.5 && measurement<=24.9){
            comment="Low risk";
        }else if (measurement>24.9 && measurement<=29.9){
            comment="Moderate risk";
        }else if (measurement>29.9 && measurement<=34.9){
            comment="High risk";
        }else if (measurement>34.9 && measurement<=39.9){
            comment="High risk";
        }else if ( measurement<=40.0){
            comment="High risk";
        }


        return comment;

    }


    public String getW2HComment(double measurement)
    {
        String comment="";

        if(cUser.getGender().equals("female"))
        {
            if(measurement<=	0.85)
            {
                comment="Normal";
            }else if (measurement>0.85 ){
                comment="High risk";
            }
        }else{
            if(measurement<=	0.90)
            {
                comment="Normal";
            }else if (measurement>0.90 ){
                comment="High risk";
            }
        }


        return comment;

    }






}
