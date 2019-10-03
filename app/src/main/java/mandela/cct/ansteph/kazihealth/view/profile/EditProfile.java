package mandela.cct.ansteph.kazihealth.view.profile;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.yarolegovich.lovelydialog.LovelyDialogCompat;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.ContentTypes;
import mandela.cct.ansteph.kazihealth.api.columns.UserColumns;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.model.User;

public class EditProfile extends AppCompatActivity {

    EditText edtFullName, edtEmail,edtOldPass, edtPass, edtConPass;
    LinearLayout ltyPassword;
    Button btnChangePic, btnChangePassword;

    Boolean isChangePassOpen;
    GlobalRetainer mGlobalRetainer;
    User cUser;

    ImageView mImgAvatar;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private static final int ID_KHN_INPUT_DIALOG = R.id.lytKhNumber;
    private static final int ID_PICTURE_DIALOG = R.id.btnChangePic;

    LinearLayout lytKhNumber ;

    private LovelySaveStateHandler saveStateHandler;
    static final int REQUEST_IMAGE_CAPTURE =0;
    static final int REQUEST_IMAGE_GALLERY =1;


    Spinner spnGender;
    ArrayAdapter<CharSequence> originAdapter;
    private TextView mDateofBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGlobalRetainer = (GlobalRetainer) getApplicationContext();
        saveStateHandler = new LovelySaveStateHandler();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtFullName = (EditText) findViewById(R.id.editFullName);
        edtEmail=(EditText) findViewById(R.id.editEmail);
        edtOldPass=(EditText) findViewById(R.id.editOldPass);
        edtPass=(EditText) findViewById(R.id.editPass);
        edtConPass=(EditText) findViewById(R.id.editConPass);

        mImgAvatar =(ImageView)findViewById(R.id.avatar);

        ltyPassword =(LinearLayout) findViewById(R.id.lytPassword) ;
        lytKhNumber = (LinearLayout) findViewById(R.id.lytKhNumber);

        btnChangePic =(Button) findViewById(R.id.btnChangePic);
        btnChangePassword=(Button) findViewById(R.id.btnChangePass);

        spnGender  =(Spinner)findViewById(R.id.spnGender);

        originAdapter = ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnGender.setAdapter(originAdapter);

        mDateofBirth = (TextView) findViewById(R.id.editDob);
        // unpack the dob

        mDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String bod= simpleDateFormat.format(cal.getTime());
                        mDateofBirth.setText(bod);
                    }
                } , 1985,01,01);

                dialog.show(getSupportFragmentManager(), "Date Picker");




            }
        });



        isChangePassOpen= false;


        if(mGlobalRetainer.get_grUser()!=null){
            cUser =mGlobalRetainer.get_grUser();
            fillRecord();
        }

        requestPermission();

    }


    public void onChangePasswordClicked(View view)
    {
        if(!isChangePassOpen){
            ltyPassword.setVisibility(View.VISIBLE);
            isChangePassOpen= true;
        }else{
            ltyPassword.setVisibility(View.GONE);
            isChangePassOpen= false;
        }
    }


    public void fillRecord() {

        edtFullName.setText(cUser.getName());
        edtEmail.setText(cUser.getEmail());

        mDateofBirth.setText(cUser.getDob());

        if(!cUser.getGender().equals(null))
        {
            int pos = originAdapter.getPosition(cUser.getGender());
            spnGender.setSelection(pos);
        }

        if(cUser.getProfilePic()!=null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(cUser.getProfilePic(), 0, cUser.getProfilePic().length);
            mImgAvatar.setImageBitmap(bitmap);

        }

    }



    public void onChangePictureClicked(View view)
    {
       // takePicture();

        showLovelyDialog(view.getId(), null);
    }


    public void takePicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void pickfromGallery()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(pickPhoto.resolveActivity(getPackageManager())!=null){
            startActivityForResult(pickPhoto,REQUEST_IMAGE_GALLERY);
        }
    }


    public Intent getIntentPicture(){

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List <Intent> allIntents = new ArrayList();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List <ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

        for (ResolveInfo res : listCam) {

            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }


        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List <ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }


        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;


    }


    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extras= data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            mImgAvatar.setImageBitmap(imageBitmap);

            if(SaveImage(imageBitmap)==1){
                Toast.makeText(getApplicationContext(),"Profile Image Changed",Toast.LENGTH_LONG).show();
            }


            // if(getCurrentPicRequester()!=null){
           //     getCurrentPicRequester().setRequestedPicBitmap(imageBitmap);
           // }
        }

        if(requestCode==REQUEST_IMAGE_GALLERY && resultCode==RESULT_OK){
                Uri imageGallery = data.getData();
                 mImgAvatar.setImageURI(imageGallery);

            Bundle extras= data.getExtras();

            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageGallery);

                if(SaveImage(imageBitmap)==1){
                    Toast.makeText(getApplicationContext(),"Profile Image Changed",Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    int SaveImage(Bitmap picBitmap)
    {

        byte [] picByteArray =bitmaptoByte(picBitmap);
        String u_id = String.valueOf(cUser.getId());

        try {
            ContentValues values = new ContentValues();


            values.put(UserColumns.PROFILE_IMAGE,picByteArray) ;

            getContentResolver().update(ContentTypes.USER_CONTENT_URI, values , UserColumns._ID+" =?",new String[]{u_id});

            mGlobalRetainer.get_grUser().setProfilePic(picByteArray);

            return 1;


        }catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }

      /*  ContentValues imagevalues = new ContentValues();

        imagevalues.put(AnswerImageColumns.ANSWER_ID, id);
        imagevalues.put(AnswerImageColumns.IMAGE_BINARY, ansI.getImage_binary());

        getActivity().getContentResolver().insert(ContentTypes.ANWSERIMAGE_CONTENT_URI, imagevalues);*/

    }


    public byte [] bitmaptoByte (Bitmap b)
    {
        int bytes = b.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);

        b.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,0,blob);


        return  blob.toByteArray();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
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

    public void onDoneClicked()
    {
        //flash storing
        String name = edtFullName.getText().toString();
        String email = edtEmail.getText().toString();
       String mDBO = mDateofBirth.getText().toString();
       String password = edtPass.getText().toString();

       String oldPwd = edtOldPass.getText().toString();

        User user = new User (); //(edtName.getText().toString(),edtEmail.getText().toString(),edtPassword.getText().toString());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setDob(mDateofBirth.getText().toString());
        user.setGender((String)spnGender.getSelectedItem());


        updateUser( user);

        if(isChangePassOpen){
            if(checkUser(String.valueOf(cUser.getId()),oldPwd)){
                user.setId(cUser.getId());
                updatePwd(user);
            }

        }

        Toast.makeText(getApplicationContext(),"Changes saved",Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(),Profile.class));

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

            case ID_KHN_INPUT_DIALOG: showKHNumberInputDialog(savedInstanceState);
                break;
            case ID_PICTURE_DIALOG: showPictureDialog(savedInstanceState);
                break;

        }
    }



    private void showKHNumberInputDialog(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.hr_input_title)
                .setMessage(R.string.text_input_hr)
                .setIcon(R.mipmap.ic_pro_heart)
                .setInstanceStateHandler(ID_KHN_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^\\d{1,3}$");
                    }
                })

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        ( (TextView) findViewById(R.id.txtHeartRate)).setText(text +" ");
                       // rHeartRate = new RiskProfileItem(cUser.getId(), RID_HR,text,"");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(  editText -> editText.setMaxLines(1))

                .show();
    }



    private void showPictureDialog(Bundle savedInstanceState) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes( R.color.colorAccent)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.ic_camera_alt)
                .setTitle(R.string.text_picture_title)
                .setInstanceStateHandler(ID_PICTURE_DIALOG, saveStateHandler)
                .setSavedInstanceState(savedInstanceState)
                .setMessage(R.string.text_picture_profile)
                .setPositiveButton(R.string.text_pict_camera, LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               takePicture();

                            }
                        })
                )
                .setNegativeButton(R.string.text_pict_gallery,LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                pickfromGallery();
                    }
                }))
                .show();
    }







    private void requestPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
        }
    }


    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                  //  Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



       /* if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission " +permissions[0]+ " was " +grantResults[0]);
        }*/
    }


    public int updatePwd(User user){

        String u_id = String.valueOf( user.getId());
        try {
            ContentValues values = new ContentValues();


            values.put(UserColumns.PASSWORD,user.getPassword()) ;

            getContentResolver().update(ContentTypes.USER_CONTENT_URI, values , UserColumns._ID+" =?",new String[]{u_id});


            return 1;


        }catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }

    }

    public int updateUser(User user){

        String u_id = String.valueOf( user.getId());
        try {
            ContentValues values = new ContentValues();

            values.put(UserColumns.NAME,user.getName()) ;
            values.put(UserColumns.EMAIL ,user.getEmail()) ;
            values.put(UserColumns.DOB,user.getDob()) ;
            values.put(UserColumns.GENDER,user.getGender()) ;


            getContentResolver().update(ContentTypes.USER_CONTENT_URI, values , UserColumns._ID+" =?",new String[]{u_id});


            return 1;


        }catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }

    }




    boolean checkUser(String id, String password)
    {
        ContentResolver resolver = getContentResolver();
        // cursor = resolver.query(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, RecordedActivtyColumns.PROJECTION,
        // RecordedActivtyColumns.TIME_CREATED +">=? and " + RecordedActivtyColumns.TIME_CREATED+"<=?", new String[]{startDate, endDate},null);

        Cursor cursor = resolver.query(ContentTypes.USER_CONTENT_URI, UserColumns.PROJECTION,UserColumns._ID+ " = ?" + " AND " + UserColumns.PASSWORD + " = ?",
                new String[]{id, password}, null);



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



}
