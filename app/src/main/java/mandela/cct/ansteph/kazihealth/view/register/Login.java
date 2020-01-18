package mandela.cct.ansteph.kazihealth.view.register;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.app.KaziApp;
import mandela.cct.ansteph.kazihealth.view.firebasereg.Register_Firebase;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;

public class Login extends AppCompatActivity {

    public static String TAG = Login.class.getSimpleName();
    EditText edtPassword, edtEmail;
    KaziApp mKaziApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mKaziApp = (KaziApp) getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RiskProfile.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtPassword = (EditText) findViewById(R.id.editPass);
    }

    public void onRegisterClicked(View view) {
        startActivity(new Intent(getApplicationContext(), Register_Firebase.class));
    }

    public void onLoginClicked(View view) {
        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();

        if (false) {
            Log.d(TAG, "record found");
            Intent i = new Intent(getApplicationContext(), RiskProfile.class);
            i.putExtra("email", email);
            i.putExtra("pwd", pass);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_LONG).show();
        }
    }



}
