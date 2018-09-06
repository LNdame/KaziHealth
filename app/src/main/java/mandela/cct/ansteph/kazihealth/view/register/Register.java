package mandela.cct.ansteph.kazihealth.view.register;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.ContentTypes;
import mandela.cct.ansteph.kazihealth.api.columns.UserColumns;
import mandela.cct.ansteph.kazihealth.model.User;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;

public class Register extends AppCompatActivity {

    public static String TAG = Register.class.getSimpleName();

    EditText edtName,edtfName, edtEmail, edtPassword, edtConfirmPass;
    Spinner spnGender;
    ArrayAdapter<CharSequence> originAdapter;

    private TextView mDateofBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



                edtName = (EditText) findViewById(R.id.editName);
                 edtfName = (EditText) findViewById(R.id.editlName);
                 edtEmail=(EditText) findViewById(R.id.editEmail);
                edtPassword=(EditText) findViewById(R.id.editPass);
                edtConfirmPass=(EditText) findViewById(R.id.editConPass);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Login.class));

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spnGender  =(Spinner)findViewById(R.id.spnGender);

        originAdapter = ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnGender.setAdapter(originAdapter);

        mDateofBirth = (TextView) findViewById(R.id.editDob);
        mDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // DialogFragment nf = new DatePickerFragment();
               // nf.show(getSupportFragmentManager(), "Date Picker");


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

    }


    public void onRegisterClicked (View view)
    {
        if(!isFormCancelled())
        {
            String fullname =  edtName.getText().toString()+" "+edtfName.getText().toString();
            User user = new User (fullname,edtEmail.getText().toString(),edtPassword.getText().toString());
            user.setDob(mDateofBirth.getText().toString());
            user.setGender((String)spnGender.getSelectedItem());

            if(registerUser(user)==1){
                startActivity(new Intent(getApplicationContext(),Login.class));
            }


        }


    }



    public void onLoginClicked (View view)
    {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }


    public boolean isFormCancelled()
    {
        //Reset the errors


        edtName.setError(null);
        edtfName.setError(null);
        edtEmail.setError(null);
        edtPassword.setError(null);
        edtConfirmPass.setError(null);

        //flash storing
        String name = edtName.getText().toString();
        String lname = edtfName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String conpassword = edtConfirmPass.getText().toString();



        boolean cancel = false;
        View focusView = null;

        //Check
        // Check for a valid password, if the user entered one.
        if(TextUtils.isEmpty(password)){
            edtPassword.setError(getString(R.string.error_field_required));
            focusView = edtPassword;
            cancel = true;
        }
        else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;

        } else if(!password.equals(conpassword))
        {
            edtConfirmPass.setError(getString(R.string.error_invalid_confirmation));
            focusView =edtConfirmPass;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            focusView = edtEmail;
            cancel = true;
        }else if(TextUtils.isEmpty(name)) {
            edtName.setError(getString(R.string.error_field_required));
            focusView = edtName;
            cancel = true;

        }else if(TextUtils.isEmpty(lname))
        {
            edtfName.setError(getString(R.string.error_field_required));
            focusView = edtfName;
            cancel = true;
        }
            //Check





        if(cancel)
        {
            focusView.requestFocus();
        }

        return cancel;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic

        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }




    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }



    public int registerUser(User user){


        try {
            ContentValues values = new ContentValues();

            values.put(UserColumns.NAME,user.getName()) ;
            values.put(UserColumns.EMAIL ,user.getEmail()) ;
            values.put(UserColumns.DOB,user.getDob()) ;
            values.put(UserColumns.GENDER,user.getGender()) ;
            values.put(UserColumns.PASSWORD,user.getPassword()) ;

            getContentResolver().insert(ContentTypes.USER_CONTENT_URI, values);

            return 1;


        }catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }

    }



}
