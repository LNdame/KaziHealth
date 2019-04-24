package mandela.cct.ansteph.kazihealth.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import mandela.cct.ansteph.kazihealth.view.firebasereg.Login_Firebase;
import mandela.cct.ansteph.kazihealth.view.profile.RiskProfile;

/**
 * Created by loicstephan on 2018/06/22.
 */

public class SessionManager {

    SharedPreferences preferences;

    //Editor for shared preferences
    SharedPreferences.Editor editor;

    //Context
    Context _context;

    //shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "KaziPref";


    // All Shared Preferences Keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_NAME = "name";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_DOB = "dob";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";



    public SessionManager(Context context) {
        this._context = context;

        preferences=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession (String id, String name,String lastname, String email, String dob, String pwd, String gender)
    {
        //storing login value as true
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

       editor.putString(KEY_ID, id);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_PASSWORD, pwd);
        editor.putString(KEY_GENDER, gender);


        editor.commit();
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        //Clearing all the data from Shared Preferences
        editor.clear();
        editor.commit();

        //After logout redirect user to login Activity

        Intent i = new Intent(_context, Login_Firebase.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Starting Login Activity
        _context.startActivity(i);

        //
    }


    public void checkLogin(){

        if(!this.isLoggedIn())
        {
            Intent i= new Intent(_context, Login_Firebase.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);
        }else
        {
            Intent i = new Intent(_context, RiskProfile.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<>();

        //user id
        user.put(KEY_ID, preferences.getString(KEY_ID,null));
        //user name
        //user.put(KEY_USERNAME, preferences.getString(KEY_USERNAME,null));
        user.put(KEY_NAME, preferences.getString(KEY_NAME,null));
        user.put(KEY_LASTNAME, preferences.getString(KEY_LASTNAME,null));
        //user email
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL,null));

        user.put(KEY_DOB, preferences.getString(KEY_DOB,null));

        user.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD,null));

        user.put(KEY_GENDER, preferences.getString(KEY_GENDER,null));





        return user;
    }



    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN,false);
    }






}
