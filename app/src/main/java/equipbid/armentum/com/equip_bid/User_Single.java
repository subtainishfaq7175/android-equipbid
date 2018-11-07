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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_Single extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private TextView name,username, email, phone, password, role;
    private ProgressDialog pDialog;
    private static final String TAG = User_Single.class.getSimpleName();
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private String user_role_label;
    private NavigationView navigationView;
    String User_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email     = sharedpreferences.getString("email",null);
        p_username  = sharedpreferences.getString("username",null);
        p_token     = sharedpreferences.getString("token",null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id        = sharedpreferences.getString("ID",null);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

        name        = (TextView)findViewById(R.id.name);
        username    = (TextView)findViewById(R.id.username);
        email       = (TextView)findViewById(R.id.email);
        phone       = (TextView)findViewById(R.id.phone);
        password    = (TextView)findViewById(R.id.password);
        role        = (TextView)findViewById(R.id.role);

        Bundle bundle   = getIntent().getExtras();
        User_id    = bundle.getString("id");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");

        name.setTypeface(typeface);
        username.setTypeface(typeface);
        email.setTypeface(typeface);
        phone.setTypeface(typeface);
        password.setTypeface(typeface);
        role.setTypeface(typeface);
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

        isNetworkAvailable();
        if(isNetworkAvailable() == true )
        {
           getProfileU(User_id, p_token);
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(User_Single.this).create();
            alertDialog.setMessage("No Internet Connection");
            alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }
    }

    private void getProfileU(final String user_id, final String p_token) {

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_REGISTER : " + AppConfig.URL_USER+user_id+"?context=edit");

        StringRequest strReq = new StringRequest(Method.GET,
                AppConfig.URL_USER+user_id+"?context=edit", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_USER+user_id+"?context=edit");

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try if: " + AppConfig.URL_USER+user_id+"?context=edit");

                    String code = jObj.getString("code");
                    if(code.equals("rest_user_cannot_view"))
                    {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                    }
                    else if(code.equals("user exist"))
                    {
                        String f_n = jObj.getString("first_name");
                        String l_n = jObj.getString("last_name");
                        name.setText(f_n +" "+ l_n);
                        email.setText(jObj.getString("email"));
                        username.setText(jObj.getString("username"));
                        String user_role_label = jObj.getString("user_role_label");
                        JSONArray jArray1 = jObj.getJSONArray("phone");
                        String phone_s = (String)jArray1.get(0);
                        phone.setText(phone_s);
                        role.setText(user_role_label);
                        // password.setText(jObj.getString("password"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_USER+user_id+"?context=edit");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                onBackPressed();
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+p_token);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(User_Single.this, New_lot.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user__settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {

            Intent i = new Intent(User_Single.this, User_Edit_Single.class);
            i.putExtra("id",User_id);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_setting) {
            Intent i = new Intent(User_Single.this, User_Settings.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(User_Single.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.history) {
            Intent i = new Intent(User_Single.this, Auction_History.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.home) {
            Intent i = new Intent(User_Single.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(User_Single.this, New_lot.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
