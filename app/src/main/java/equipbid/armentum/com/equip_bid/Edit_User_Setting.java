package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edit_User_Setting extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = Edit_User_Setting.class.getSimpleName();
    private EditText _first_name, _last_name, _username, _email, _phone;
    private String phone, first_name, last_name, email, username;
    private Button Change_pass;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private NavigationView navigationView;
    private Spinner spinner;
    private ArrayList<String> Spin_list;
    private Edit_User_Setting.SpinnerAdapterRole spinadapter;
    private String Spin_select_value;
    private String user_role_label, roles;
    private EditText _userrole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email = sharedpreferences.getString("email", null);
        p_username = sharedpreferences.getString("username", null);
        p_token = sharedpreferences.getString("token", null);
        user_role_label = sharedpreferences.getString("user_role_label", null);
        p_id = sharedpreferences.getString("ID", null);

        Log.e("Details", p_email + " ..username.. " + p_username + " ..tokentoken.. " + p_token + " ..id.." + p_id);

        _first_name = (EditText) findViewById(R.id.first_name);
        _last_name = (EditText) findViewById(R.id.last_name);
        _username = (EditText) findViewById(R.id.username);
        _userrole = (EditText) findViewById(R.id.userrole);
        _email = (EditText) findViewById(R.id.email);
        _phone = (EditText) findViewById(R.id.phone);
        spinner = (Spinner) findViewById(R.id.spinner);
        Change_pass = (Button) findViewById(R.id.Change_pass);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");

        _first_name.setTypeface(typeface);
        _last_name.setTypeface(typeface);
        _username.setTypeface(typeface);
        _userrole.setTypeface(typeface);
        _phone.setTypeface(typeface);
        _email.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        Change_pass.setTypeface(typeface);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        if (user_role_label.equals("Super Admin")) {
            nav_Menu.findItem(R.id.user).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.user).setVisible(false);
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        isNetworkAvailable();
        if (isNetworkAvailable() == true) {
            _username.setEnabled(false);
            _userrole.setEnabled(false);
            _first_name.requestFocus();

            getProfile(p_id, p_token);

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(Edit_User_Setting.this).create();
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

        Change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable() == true) {
                    Intent i = new Intent(Edit_User_Setting.this, Change_Pass.class);
                    startActivity(i);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(Edit_User_Setting.this).create();
                    alertDialog.setMessage("No Internet Connection");
                    alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            isNetworkAvailable();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.done) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            isNetworkAvailable();
            if (isNetworkAvailable() == true) {
                _first_name.setError(null);
                _last_name.setError(null);
                _email.setError(null);
                _phone.setError(null);

                first_name = _first_name.getText().toString().trim();
                last_name = _last_name.getText().toString().trim();
                phone = _phone.getText().toString();
                email = _email.getText().toString().trim();
                username = _username.getText().toString().trim();
                 String user_role_label = Spin_select_value;
                roles = Spin_select_value;

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(email)) {
                    _email.setError(getString(R.string.error_field_required));
                    focusView = _email;
                    cancel = true;
                } else if (!isValidEmail(email)) {
                    _email.setError("This Email Id is Invalid");
                    focusView = _email;
                    cancel = true;
                    focusView = _last_name;
                    cancel = true;
                }

                if (TextUtils.isEmpty(last_name)) {
                    _last_name.setError(getString(R.string.error_field_required));
                    focusView = _last_name;
                    cancel = true;
                } else if (!isLastValid(last_name)) {
                    _last_name.setError("3 characters Lastname is required");
                    focusView = _last_name;
                    cancel = true;
                }

                if (TextUtils.isEmpty(first_name)) {
                    _first_name.setError(getString(R.string.error_field_required));
                    focusView = _first_name;
                    cancel = true;
                } else if (!isFirstValid(first_name)) {
                    _first_name.setError("3 characters Firstname is required");
                    focusView = _first_name;
                    cancel = true;
                }

                if (TextUtils.isEmpty(phone)) {
                    _phone.setError(getString(R.string.error_field_required));
                    focusView = _phone;
                    cancel = true;
                } else if (!isValidMobile(phone)) {
                    _phone.setError("This Mobile No. is Invalid");
                    focusView = _phone;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    Log.e(TAG, " firstname : " + first_name);
                    Log.e(TAG, " phone : " + phone);
                    Log.e(TAG, " email : " + email);
                    Log.e(TAG, " username : " + username);
                    setProfile(first_name, last_name, phone, email, username, p_id, p_token);
                }
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(Edit_User_Setting.this).create();
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isLastValid(String last_name) {
        /*String Last_PATTERN = "[A-Z][a-zA-Z]*";

        Pattern pattern = Pattern.compile(Last_PATTERN);
        Matcher matcher = pattern.matcher(last_name);
        return matcher.matches();*/
        return last_name.length() >= 3;
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isFirstValid(String first_name) {
        /*String First_PATTERN = "[A-Z][a-zA-Z]*";

        Pattern pattern = Pattern.compile(First_PATTERN);
        Matcher matcher = pattern.matcher(first_name);
        return matcher.matches();*/
        return first_name.length() >= 3;
    }

    private void getProfile(final String p_id, final String p_token) {
        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_USER : " + AppConfig.URL_USER + p_id + "?context=edit");

        StringRequest strReq = new StringRequest(Method.GET,
                AppConfig.URL_USER + p_id + "?context=edit", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "fgfgfhdddddddd : " + AppConfig.URL_USER + p_id + "?context=edit");

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_USER + p_id + "?context=edit");
                    _first_name.setText(jObj.getString("first_name"));
                    _first_name.setSelection(_first_name.length());

                    _last_name.setText(jObj.getString("last_name"));
                    _last_name.setSelection(_last_name.length());

                    _email.setText(jObj.getString("email"));
                    _email.setSelection(_email.length());

                    _username.setText(jObj.getString("username"));
                     String user_role_label= jObj.getString("user_role_label");

                    _userrole.setText(user_role_label);
                    JSONArray jArray1 = jObj.getJSONArray("phone");
                    String phone_s = (String) jArray1.get(0);
                    _phone.setText(phone_s);
                    _phone.setSelection(_phone.length());


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_USER + p_id + "?context=edit");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                onBackPressed();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + p_token);
                return params;
            }

            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + p_token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setProfile(final String first_name, final String last_name, final String phone, final String email,
                            final String username, final String p_id, final String p_token) {

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_USER : " + AppConfig.URL_USER + p_id);

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_USER + p_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "" + AppConfig.URL_USER + p_id);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try " + AppConfig.URL_USER + p_id);
                    String uid = jObj.getString("id");
                    Log.e(TAG, "uid " + uid+"");
                    if (uid != null) {
                        Toast.makeText(getApplicationContext(), "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Edit_User_Setting.this, User_Settings.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_USER + p_id);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Log.e("uidelse..", jObj+"");
                        Log.e("uidelse..", jObj.get("id")+"");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    Toast.makeText(Edit_User_Setting.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit details Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Edit_User_Setting.this, User_Settings.class);
                startActivity(intent);
                finish();
                hideDialog();

                if (error instanceof ServerError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");

                        if (code.equals("rest_invalid_param")) {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + p_token);
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("phone", phone);
                params.put("email", email);
                params.put("username", username);
                //params.put("user_role_label", user_role_label);
                //params.put("roles", roles);

                return params;
            }

            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                Log.e(TAG, "token-- " + "Bearer " + p_token);
                headers.put("Authorization", "Bearer " + p_token);
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
            Intent i = new Intent(Edit_User_Setting.this, User_Settings.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit__user__setting, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_setting) {
            Intent i = new Intent(Edit_User_Setting.this, User_Settings.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Edit_User_Setting.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.history) {
            Intent i = new Intent(Edit_User_Setting.this, Auction_History.class);
            startActivity(i);
        } else if (id == R.id.home) {
            Intent i = new Intent(Edit_User_Setting.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        } else if (id == R.id.user) {
            Intent i = new Intent(Edit_User_Setting.this, New_lot.class);
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

    private class SpinnerAdapterRole extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<String> spin_list;
        private int test = 0;
        private TextView spinvalue;

        public SpinnerAdapterRole(Activity activity, ArrayList<String> spin_list) {
            this.activity = activity;
            this.spin_list = spin_list;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return spin_list.size();
        }

        @Override
        public Object getItem(int position) {
            return Spin_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.spin_role, parent, false);
            }

            spinvalue = (TextView) view.findViewById(R.id.spinvalue);
            spinvalue.setTypeface(typeface);
            spinvalue.setText(spin_list.get(position));
            return view;
        }
    }
}
