package mandela.cct.ansteph.kazihealth.view.firebasereg;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;

public class Login_Firebase extends AppCompatActivity {


    public static String TAG = Login_Firebase.class.getSimpleName();
    EditText edtPassword, edtEmail;

    GlobalRetainer mGlobalRetainer;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mGlobalRetainer = (GlobalRetainer)getApplicationContext();


        edtEmail=(EditText) findViewById(R.id.editEmail);
        edtPassword=(EditText) findViewById(R.id.editPass);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }


    public void onRegisterClicked (View view)
    {
        //startActivity(new Intent(getApplicationContext(),Register.class));
        startActivity(new Intent(getApplicationContext(),Register_Firebase.class));
    }

    public void onForgotClicked (View view)
    {
        //startActivity(new Intent(getApplicationContext(),Register.class));
        startActivity(new Intent(getApplicationContext(),ResetPassword.class));
    }

    public void onLoginClicked (View view) {
        userLogin();
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    private void userLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please enter a valid email");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Minimum lenght of password should be 6");
            edtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), RiskProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic

        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }


    @Override
    protected void onStart() {
        super.onStart();

      /*  if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, RiskProfile.class));
        }*/
    }

}
