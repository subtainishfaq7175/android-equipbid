package equipbid.armentum.com.equip_bid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Change_Pass_Single extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = Edit_User_Setting.class.getSimpleName();
    private EditText _password, _re_password;
    private String  password ,repassword;
    private Button Change_pass;
    private LinearLayout l1_pass;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private NavigationView navigationView;
    private String user_role_label;
    private String User_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__pass__single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email     = sharedpreferences.getString("email",null);
        p_username  = sharedpreferences.getString("username",null);
        p_token     = sharedpreferences.getString("token",null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id        = sharedpreferences.getString("ID",null);

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

        Bundle bundle   = getIntent().getExtras();
        User_id    = bundle.getString("id");

        _password       = (EditText)findViewById(R.id.password);
        _re_password    = (EditText)findViewById(R.id.repassword);

        _password.setTypeface(Typeface.DEFAULT);
        _re_password.setTypeface(Typeface.DEFAULT);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        _password.setTypeface(typeface);
        _re_password.setTypeface(typeface);
        mTitle.setTypeface(typeface);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        if(user_role_label.equals("Super Admin")) {

            nav_Menu.findItem(R.id.user).setVisible(true);
        }
        else
        {
            nav_Menu.findItem(R.id.user).setVisible(false);
        }


        pDialog     = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(Change_Pass_Single.this, User_Single.class);
            i.putExtra("id",User_id);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change__pass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {

            isNetworkAvailable();
            if(isNetworkAvailable() == true) {

                _password.setError(null);
                _re_password.setError(null);

                // Store values at the time of the login attempt.
                password = _password.getText().toString().trim();
                repassword = _re_password.getText().toString().trim();

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(repassword)) {
                    _re_password.setError(getString(R.string.error_field_required));
                    focusView = _re_password;
                    cancel = true;
                }
                else if (!repassword.equals(password)) {
                    _re_password.setError("Confirm Password does not match New Password");
                    focusView = _re_password;
                    cancel = true;
                }

                if (TextUtils.isEmpty(password)) {
                    _password.setError(getString(R.string.error_field_required));
                    focusView = _password;
                    cancel = true;

                } else if (!isPasswordValid(password)) {
                    _password.setError(getString(R.string.error_invalid_password));
                    focusView = _password;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    Log.e(TAG, " password has : " + password);
                    setProfilePass(password, repassword,User_id, p_token);
                }
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(Change_Pass_Single.this).create();
                alertDialog.setMessage("No Internet Connection");

                alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        isNetworkAvailable();
                    }
                });
                alertDialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_setting) {
            Intent i = new Intent(Change_Pass_Single.this, User_Settings.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Change_Pass_Single.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.history) {
            Intent i = new Intent(Change_Pass_Single.this, Auction_History.class);
            startActivity(i);
        }
        else if (id == R.id.home) {
            Intent i = new Intent(Change_Pass_Single.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Change_Pass_Single.this, New_lot.class);
            startActivity(i);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isPasswordValid(String password) {
        String Password_PATTERN = "^.{8,10}$";

        Pattern pattern = Pattern.compile(Password_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void setProfilePass(final String password, String repassword, final String User_id, final String p_token) {

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_USER : " + AppConfig.URL_USER+User_id);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_USER+User_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_USER+User_id);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try if: " + AppConfig.URL_USER+User_id);
                    //String code = jObj.getString("code");
                    String uid = jObj.getString("id");
                    if(uid != null)
                    {
                        Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Change_Pass_Single.this, User_Single.class);
                        intent.putExtra("id",User_id);
                        startActivity(intent);
                        finish();
                    }
                    /*else if(code.equals("rest_invalid_param"))
                    {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_USER+User_id);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit details Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Change_Pass_Single.this, User_Single.class);
                intent.putExtra("id",User_id);
                startActivity(intent);
                finish();
                hideDialog();

                if(error instanceof ServerError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");

                        if(code.equals("rest_invalid_param"))
                        {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", password);

                return params;
            }

            public Map < String, String > getHeaders()  {
                HashMap < String, String > headers = new HashMap < String, String > ();
                headers.put("Authorization", "Bearer "+p_token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        try {
            if ((this.pDialog != null) && this.pDialog.isShowing()) {
                this.pDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            // this.pDialog = null;
            this.pDialog.dismiss();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
