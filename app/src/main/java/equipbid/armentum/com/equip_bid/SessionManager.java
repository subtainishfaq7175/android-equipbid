package equipbid.armentum.com.equip_bid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;


    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidEquipbid";
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_Emailid = "email";
    public static final String KEY_Password = "password";
    public static final String KEY_username = "username";
    public static final String KEY_token = "token";
    public static final String KEY_Roles = "user_role_label";
    public static final String KEY_id = "ID";
    public static final String KEY_selectWidth = "selectWidth";
    public static final String KEY_selectHeight = "selectHeight";
    public static final String KEY_selectBoth = "both";
    private Editor editor1;

    @SuppressLint("CommitPrefEdits") public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor1 = pref.edit();
    }

    public void createLoginSession(String email, String password, String username, String token, String user_role_label, String ID) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_Emailid, email);
        editor.putString(KEY_username, username);
        editor.putString(KEY_token, token);
        editor.putString(KEY_Roles, user_role_label);
        editor.putString(KEY_id, ID);
        // Storing email in pref
        editor.putString(KEY_Password, password);

        // commit changes
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login_Activity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_Emailid, pref.getString(KEY_Emailid, null));

        user.put(KEY_Password, pref.getString(KEY_Password, null));

        user.put(KEY_username, pref.getString(KEY_username, null));

        user.put(KEY_token, pref.getString(KEY_token, null));

        user.put(KEY_Roles, pref.getString(KEY_Roles, null));

        user.put(KEY_id, pref.getString(KEY_id, null));
        // return user
        return user;
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createCameraResolution(int selectWidth, int selectHeight, String Both) {

        editor1.putInt(KEY_selectWidth, selectWidth);
        editor1.putInt(KEY_selectHeight, selectHeight);
        editor1.putString(KEY_selectBoth, Both);
        // commit changes
        editor1.commit();
    }

    public void ClearReUser() {
        // Clearing all data from Shared Preferences

        editor1.clear();
        editor1.commit();

        // After logout redirect user to Loing Activity
    }

}
