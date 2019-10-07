package mandela.cct.ansteph.kazihealth.view.profile;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.ContentTypes;
import mandela.cct.ansteph.kazihealth.api.columns.RiskProfileColumns;
import mandela.cct.ansteph.kazihealth.app.KaziApp;
import mandela.cct.ansteph.kazihealth.data.AppExecutors;
import mandela.cct.ansteph.kazihealth.data.KaziDatabase;
import mandela.cct.ansteph.kazihealth.helper.SessionManager;
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

    private LovelySaveStateHandler saveStateHandler;
    LinearLayout lytBloodPressure;
    LinearLayout lytHeartRate;
    LinearLayout lytCholesterol;
    LinearLayout lytBloodGlucose;
    LinearLayout lytHeight;
    LinearLayout lytWeight;
    LinearLayout lytWaist;
    LinearLayout lytHip;

    double mWeight, mHeight, mWaist, mHip;
    KaziApp mKaziApp;
    TextView txtName, txtEmail;
    RiskProfileItem rBp, rHeartRate, rChol, rBgl, rHeight, rWeight, rBMI, rWaist, rHip, rW2H;
    Button btnSave;
    User cUser;
    CircleImageView circleImageView;
    SessionManager sessionManager;
    private KaziDatabase kDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        kDB = KaziDatabase.getInstance(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        mKaziApp = (KaziApp) getApplicationContext();
        circleImageView = (CircleImageView) findViewById(R.id.avatar);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtUserEmail);
        btnSave = (Button) findViewById(R.id.btnSaveAssess);

        if (mKaziApp.get_grUser() != null) {
            cUser = mKaziApp.get_grUser();
            txtName.setText(cUser.getName());
            txtEmail.setText(cUser.getEmail());

            if (mKaziApp.get_grUser().getProfilePic() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(mKaziApp.get_grUser().getProfilePic(), 0, mKaziApp.get_grUser().getProfilePic().length);
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

        mWeight = 75;
        mHeight = 160;
        mWaist = 60;
        mHip = 60;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRiskVars();
        retrieveItems(cUser.getId());
    }

    private void initRiskVars() {
        rBp = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rHeartRate = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rChol = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rBgl = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rHeight = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rWeight = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rBMI = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rWaist = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rHip = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
        rW2H = new RiskProfileItem(0, cUser.getId(), 0, "0", "");
    }

    @Override
    public void onClick(View v) {
        //View's ID correspond to our constants, so we just pass it
        showLovelyDialog(v.getId(), null);
    }

    public void onSaveAssessClicked(View view) {
        onDoneClicked(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload_risk_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {

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
                        ((TextView) findViewById(R.id.txtBloodPres)).setText(text + " mmHg");
                        int tId = rBp.getId();
                        rBp = new RiskProfileItem(tId, cUser.getId(), RID_BP, text, "");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))
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
                        ((TextView) findViewById(R.id.txtHeartRate)).setText(text + " bpm");
                        int tId = rHeartRate.getId();
                        rHeartRate = new RiskProfileItem(tId, cUser.getId(), RID_HR, text, "");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))
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
                        ((TextView) findViewById(R.id.txtCholesterol)).setText(text + " mmol/L");
                        int tId = rChol.getId();
                        rChol = new RiskProfileItem(tId, cUser.getId(), RID_CHL, text, "");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))
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
                        ((TextView) findViewById(R.id.txtBloodGlu)).setText(text + " mmol/L");
                        int tId = rBgl.getId();
                        rBgl = new RiskProfileItem(tId, cUser.getId(), RID_BGL, text, "");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))
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
                        ((TextView) findViewById(R.id.txtHeight)).setText(text + " cm");
                        int tId = rHeight.getId();
                        rHeight = new RiskProfileItem(tId, cUser.getId(), RID_HEIGHT, text, "");

                        calculateBMI(null, Double.parseDouble(text));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))
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
                        ((TextView) findViewById(R.id.txtBloodWeight)).setText(text + " kg");
                        int tId = rWeight.getId();
                        rWeight = new RiskProfileItem(tId, cUser.getId(), RID_WEIGHT, text, "");
                        calculateBMI(Double.parseDouble(text), null);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))

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
                .configureEditText(editText -> editText.setMaxLines(1))

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
                        ((TextView) findViewById(R.id.txtWaist)).setText(text + " cm");
                        int tId = rWaist.getId();
                        rWaist = new RiskProfileItem(tId, cUser.getId(), RID_WAIST, text, "");
                        calculateW2Hratio(Double.parseDouble(text), null);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))

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
                        ((TextView) findViewById(R.id.txtHip)).setText(text + " cm");
                        int tId = rHip.getId();
                        rHip = new RiskProfileItem(tId, cUser.getId(), RID_HIP, text, "");
                        calculateW2Hratio(null, Double.parseDouble(text));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))

                .show();
    }

    @Deprecated
    private void showSavedDialog(Bundle savedInstanceState) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.ic_save_white)
                .setTitle(R.string.text_save_title)
                .setInstanceStateHandler(ID_SAVED_DIALOG, saveStateHandler)
                .setSavedInstanceState(savedInstanceState)
                .setMessage(R.string.text_save_profile)
                .setPositiveButton(R.string.text_save_ok, LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), RiskProfile.class);
                                startActivity(intent);
                            }
                        })
                )
                .setNegativeButton(R.string.text_save_cancel, LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }))
                .show();
    }

    void calculateBMI(@Nullable Double weight, @Nullable Double height) {
        if (weight == null) {
            weight = mWeight;
        } else {
            mWeight = weight;
        }
        if (height == null) {
            height = mHeight;
        } else {
            mHeight = height;
        }

        double bmi = (mWeight) / ((mHeight / 100) * (mHeight / 100));
        String comment = getBMIComment(bmi);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
        DecimalFormat df = (DecimalFormat) nf;
        nf.setMaximumFractionDigits(1);
        String val = nf.format(bmi);

        ((TextView) findViewById(R.id.txtBMI)).setText(val + Html.fromHtml(" kg.m<sup>2</sup> |") + comment);
        int tId = rBMI.getId();
        rBMI = new RiskProfileItem(tId, cUser.getId(), RID_BMI, val, comment);
    }

    void calculateW2Hratio(@Nullable Double waist, @Nullable Double hip) {
        if (waist == null) {
            waist = mWaist;
        } else {
            mWaist = waist;
        }

        if (hip == null) {
            hip = mHip;
        } else {
            mHip = hip;
        }

        double w2hratio = (mWaist) / (mHip);
        String comment = getW2HComment(w2hratio);
        ((TextView) findViewById(R.id.txtWaistHipRatio)).setText(String.format("%.2f", w2hratio) + "|" + comment);
        int tId = rW2H.getId();
        rW2H = new RiskProfileItem(tId, cUser.getId(), RID_W2H, String.format("%.2f", w2hratio), comment);
    }


    public void onDoneClicked(View view) {
        RiskProfileItem[] riskProfileItems = {
                rBp, rHeartRate, rChol, rBgl, rHeight, rWeight, rBMI, rWaist, rHip, rW2H
        };
        for (RiskProfileItem rpfi : riskProfileItems
        ) {
            recordRiskProfileItem(rpfi);
        }
        Toast.makeText(getApplicationContext(), "RisK Profile Saved", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), RiskProfile.class);
        startActivity(intent);
    }

    private void recordRiskProfileItem(RiskProfileItem riskProfileItem) {
        if (riskProfileItem.getId() <= 0) {
            AppExecutors.getInstance().getDiskIO().execute(
                    () -> kDB.riskProfileDao().insert(riskProfileItem)
            );
        } else {
            AppExecutors.getInstance().getDiskIO().execute(
                    () -> kDB.riskProfileDao().updateRiskProfileItem(riskProfileItem)
            );
        }
    }

    @Deprecated
    public RiskProfileItem retrieveRecord(int userID, int riskId) {
        RiskProfileItem risk = new RiskProfileItem();
        risk = kDB.riskProfileDao().getRiskProfileItem(userID, riskId);
        return risk;
    }

    public void retrieveItems(int userID) {
        AppExecutors.getInstance().getDiskIO().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        final List<RiskProfileItem> riskProfileItems = kDB.riskProfileDao().getAllRiskProfileItem(userID);
                        // runOnUiThread(()->fillRecord(riskProfileItems));
                        fillRecord(riskProfileItems);
                    }
                }
        );
    }

    public void fillRecord(List<RiskProfileItem> riskProfileItems) {
        for (RiskProfileItem riskProfileItem : riskProfileItems) {
            switch (riskProfileItem.getRisk_item_id()) {
                case RID_BP:
                    rBp = riskProfileItem;
                    break;
                case RID_HR:
                    rHeartRate = riskProfileItem;
                    break;
                case RID_CHL:
                    rChol = riskProfileItem;
                    break;
                case RID_BGL:
                    rBgl = riskProfileItem;
                    break;
                case RID_HEIGHT:
                    rHeight = riskProfileItem;
                    break;
                case RID_WEIGHT:
                    rWeight = riskProfileItem;
                    break;
                case RID_BMI:
                    rBMI = riskProfileItem;
                    break;
                case RID_WAIST:
                    rWaist = riskProfileItem;
                    break;
                case RID_HIP:
                    rHip = riskProfileItem;
                    break;
                case RID_W2H:
                    rW2H = riskProfileItem;
                    break;
            }
        }

        String rBpValue = "0";
        String rHeartRateValue = "0";
        String rCholValue = "0";
        String rBglValue = "0";
        String rHeightValue = "0";
        String rWeightValue = "0";
        String rWaistValue = "0";
        String rHipValue = "0";

        try {
            rBpValue = (rBp.getMeasurement() != null) ? rBp.getMeasurement() : "0";
            rHeartRateValue = (rHeartRate.getMeasurement() != null) ? rHeartRate.getMeasurement() : "0";
            rCholValue = (rChol.getMeasurement() != null) ? rChol.getMeasurement() : "0";
            rBglValue = (rBgl.getMeasurement() != null) ? rBgl.getMeasurement() : "0";
            rHeightValue = (rHeight.getMeasurement() != null) ? rHeight.getMeasurement() : "0";
            rWeightValue = (rWeight.getMeasurement() != null) ? rWeight.getMeasurement() : "0";
            rWaistValue = (rWaist.getMeasurement() != null) ? rWaist.getMeasurement() : "0";
            rHipValue = (rHip.getMeasurement() != null) ? rHip.getMeasurement() : "0";

        } catch (Exception e) {
        }


        ((TextView) findViewById(R.id.txtBloodPres)).setText(rBpValue + " mmHg");
        ((TextView) findViewById(R.id.txtHeartRate)).setText(rHeartRateValue + " bpm");
        ((TextView) findViewById(R.id.txtCholesterol)).setText(rCholValue + " mmol/L");
        ((TextView) findViewById(R.id.txtBloodGlu)).setText(rBglValue + " mmol/L");

        ((TextView) findViewById(R.id.txtHeight)).setText(rHeightValue + " cm");
        ((TextView) findViewById(R.id.txtBloodWeight)).setText(rWeightValue + " kg");
        ((TextView) findViewById(R.id.txtWaist)).setText(rWaistValue + " cm");
        ((TextView) findViewById(R.id.txtHip)).setText(rHipValue + " cm");

        String rW2HValue = "0";
        String rW2HComment = "";
        try {
            rW2HValue = (rW2H.getMeasurement() != null) ? rW2H.getMeasurement() : "0";
            rW2HComment = (rW2H.getComment() != null) ? rW2H.getComment() : " ";
        } catch (Exception e) {
        }

        ((TextView) findViewById(R.id.txtWaistHipRatio)).setText(rW2HValue + "|" + rW2HComment);

        String rBMIValue = "0";
        String rBMIComment = " ";
        try {
            rBMIValue = (rBMI.getMeasurement() != null) ? rBMI.getMeasurement() : "0";
            rBMIComment = (rBMI.getComment() != null) ? rBMI.getComment() : " ";
        } catch (Exception e) {
        }

        ((TextView) findViewById(R.id.txtBMI)).setText(Html.fromHtml(rBMIValue + " kg.m<sup>2</sup>|" + rBMIComment));
    }

    public String getBMIComment(double measurement) {
        String comment = "";

        if (measurement < 18.5) {
            comment = "Low risk";
        } else if (measurement > 18.5 && measurement <= 24.9) {
            comment = "Low risk";
        } else if (measurement > 24.9 && measurement <= 29.9) {
            comment = "Moderate risk";
        } else if (measurement > 29.9 && measurement <= 34.9) {
            comment = "High risk";
        } else if (measurement > 34.9 && measurement <= 39.9) {
            comment = "High risk";
        } else if (measurement <= 40.0) {
            comment = "High risk";
        }
        return comment;
    }

    public String getW2HComment(double measurement) {
        String comment = "";

        if (cUser.getGender().equals("female")) {
            if (measurement <= 0.85) {
                comment = "Normal";
            } else if (measurement > 0.85) {
                comment = "High risk";
            }
        } else {
            if (measurement <= 0.90) {
                comment = "Normal";
            } else if (measurement > 0.90) {
                comment = "High risk";
            }
        }
        return comment;
    }

}
