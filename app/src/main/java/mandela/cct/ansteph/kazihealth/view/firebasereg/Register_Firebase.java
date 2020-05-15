package mandela.cct.ansteph.kazihealth.view.firebasereg;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.data.AppExecutors;
import mandela.cct.ansteph.kazihealth.data.KaziDatabase;
import mandela.cct.ansteph.kazihealth.helper.SessionManager;
import mandela.cct.ansteph.kazihealth.model.User;

public class Register_Firebase extends AppCompatActivity {

    public static String TAG = Register_Firebase.class.getSimpleName();

    EditText edtName, edtfName, edtEmail, edtPassword, edtConfirmPass;
    Spinner spnGender;
    ArrayAdapter<CharSequence> originAdapter;
    private TextView mDateofBirth;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    SessionManager sessionManager;
    private KaziDatabase kDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_firebase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        kDB = KaziDatabase.getInstance(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        edtName = (EditText) findViewById(R.id.editName);
        edtfName = (EditText) findViewById(R.id.editlName);
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtPassword = (EditText) findViewById(R.id.editPass);
        edtConfirmPass = (EditText) findViewById(R.id.editConPass);

        spnGender = (Spinner) findViewById(R.id.spnGender);
        mDateofBirth = (TextView) findViewById(R.id.editDob);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        mDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String bod = simpleDateFormat.format(cal.getTime());
                        mDateofBirth.setText(bod);
                    }
                }, 1985, 01, 01);
                dialog.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        originAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(originAdapter);
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(getApplicationContext(), Login_Firebase.class));
    }

    public boolean isFormCancelled() {
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
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError(getString(R.string.error_field_required));
            focusView = edtPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        } else if (!password.equals(conpassword)) {
            edtConfirmPass.setError(getString(R.string.error_invalid_confirmation));
            focusView = edtConfirmPass;
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
        } else if (TextUtils.isEmpty(name)) {
            edtName.setError(getString(R.string.error_field_required));
            focusView = edtName;
            cancel = true;
        } else if (TextUtils.isEmpty(lname)) {
            edtfName.setError(getString(R.string.error_field_required));
            focusView = edtfName;
            cancel = true;
        }
        //Check
        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    void registerUser() {
        if (!isFormCancelled()) {
            //flash storing
            String name = edtName.getText().toString();
            String lname = edtfName.getText().toString();
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String conpassword = edtConfirmPass.getText().toString();
            String dob = mDateofBirth.getText().toString();
            String fullname = name + " " + lname;

            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                User user = new User(
                                        fullname, email, "", dob
                                );

                                user.setGender((String) spnGender.getSelectedItem());

                                FirebaseDatabase.getInstance().getReference("users").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register_Firebase.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(Register_Firebase.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                user.setPassword(password);
                                registerUserRoom(user);
                            } else {
                                Toast.makeText(Register_Firebase.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
    }

    public void registerUserRoom(User user) {
        AppExecutors.getInstance().getDiskIO().execute(() -> kDB.userDao().insertAll(user));
        sessionManager.createLoginSession(user.getUid(), user.getName(), user.getName(), user.getEmail(), user.getDob(), user.getPassword(), user.getGender());
        startActivity(new Intent(getApplicationContext(), Login_Firebase.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    public void onRegisterClicked(View view) {
        registerUser();
    }
}
