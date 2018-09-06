package mandela.cct.ansteph.kazihealth.view.register;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.api.ContentTypes;
import mandela.cct.ansteph.kazihealth.api.columns.UserColumns;
import mandela.cct.ansteph.kazihealth.app.GlobalRetainer;
import mandela.cct.ansteph.kazihealth.model.User;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;

public class Login extends AppCompatActivity {

     public static String TAG = Login.class.getSimpleName();
    EditText edtPassword, edtEmail;

    GlobalRetainer mGlobalRetainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGlobalRetainer = (GlobalRetainer)getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), RiskProfile.class));

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        edtEmail=(EditText) findViewById(R.id.editEmail);
        edtPassword=(EditText) findViewById(R.id.editPass);
    }


    public void onRegisterClicked (View view)
    {
        startActivity(new Intent(getApplicationContext(),Register.class));
    }

    public void onLoginClicked (View view)
    {
        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();

        if(checkUser(email, pass)) {
            Log.d(TAG, "record found");
            Intent i = new Intent(getApplicationContext(), RiskProfile.class);
            i.putExtra("email", email);
            i.putExtra("pwd",pass);

            startActivity(i);


        }else{
            Toast.makeText(getApplicationContext(),"Incorrect email or password", Toast.LENGTH_LONG).show();

        }
    }


    boolean checkUser(String email, String password)
    {
        ContentResolver resolver = getContentResolver();
        // cursor = resolver.query(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, RecordedActivtyColumns.PROJECTION,
       // RecordedActivtyColumns.TIME_CREATED +">=? and " + RecordedActivtyColumns.TIME_CREATED+"<=?", new String[]{startDate, endDate},null);

        Cursor cursor = resolver.query(ContentTypes.USER_CONTENT_URI, UserColumns.PROJECTION,UserColumns.EMAIL+ " = ?" + " AND " + UserColumns.PASSWORD + " = ?",
                new String[]{email, password}, null);



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
