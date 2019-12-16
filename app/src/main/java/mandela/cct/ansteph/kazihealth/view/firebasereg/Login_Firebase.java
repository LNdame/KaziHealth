package mandela.cct.ansteph.kazihealth.view.firebasereg;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.app.KaziApp;
import mandela.cct.ansteph.kazihealth.data.AppExecutors;
import mandela.cct.ansteph.kazihealth.data.KaziDatabase;
import mandela.cct.ansteph.kazihealth.helper.SessionManager;
import mandela.cct.ansteph.kazihealth.model.User;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;

public class Login_Firebase extends AppCompatActivity {


    public static String TAG = Login_Firebase.class.getSimpleName();
    EditText edtPassword, edtEmail;

    KaziApp mKaziApp;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    SessionManager sessionManager;
    KaziDatabase kDb;
    List<User> users = new ArrayList<>();
    boolean doesUserExist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mKaziApp = (KaziApp)getApplicationContext();
        sessionManager = new SessionManager(getApplicationContext());
        kDb = KaziDatabase.getInstance(getApplicationContext());
        edtEmail=(EditText) findViewById(R.id.editEmail);
        edtPassword=(EditText) findViewById(R.id.editPass);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }


    public void onRegisterClicked (View view)
    {
        startActivity(new Intent(getApplicationContext(),Register_Firebase.class));
    }

    public void onForgotClicked (View view)
    {
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
            edtPassword.setError("Minimum length of password should be 6");
            edtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    addUserIfMissing(uid);
                    sessionManager.createLoginSession(uid,email,password);
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

    private void userExists(String uid){
        AppExecutors.getInstance().getMainThread().execute(new Runnable() {
            @Override
            public void run() {
                users = kDb.userDao().checkUser(uid);
                doesUserExist= users.size()>0;
            }
        });

    }

    private void addUserIfMissing(String uid){
         DatabaseReference kDBRef= FirebaseDatabase.getInstance().getReference("Users").child(uid);
         kDBRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);

//                 AppExecutors.getInstance().getSerialExecIO().execute(new Runnable() {
//                     @Override
//                     public void run() {
//                         Log.d(TAG, "Checking user exist");
//                         users = kDb.userDao().checkUser(uid);
//                         doesUserExist= users.size()>0;
//                     }
//                 });
//
//                 AppExecutors.getInstance().getSerialExecIO().execute(new Runnable() {
//                     @Override
//                     public void run() {
//                         Log.d(TAG, "try to insert user");
//                         if(!doesUserExist){
//                             Log.d(TAG, "Inserting User");
//                             kDb.userDao().insertAll(user);
//                         }
//
//                     }
//                 }); Log.d(TAG, user.getName()+" "+user.getGender());


             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

}
