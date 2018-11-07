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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    private Button sign_up, login;
    private TextView forgotpass;
    private EditText edtemail, edtpassword;
    private static final String TAG = Login_Activity.class.getSimpleName();
    private TextView login_title;
    private SessionManager session;
    private String email, username, password;
    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    public static final String LOGIN_SUCCESS = "token";
    String p_username, p_token, p_email, p_id;
    private String token, ID, user_role_label ;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email     = sharedpreferences.getString("email",null);
        p_username  = sharedpreferences.getString("username",null);
        p_token     = sharedpreferences.getString("token",null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id  = sharedpreferences.getString("ID",null);
        Log.e("DetailsLogin",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

        session = new SessionManager(getApplicationContext());

        sign_up         = (Button)findViewById(R.id.sign_up);
        login           = (Button)findViewById(R.id.login);
        forgotpass      = (TextView)findViewById(R.id.forgotpass);
        edtemail        = (EditText)findViewById(R.id.email);
        edtpassword     = (EditText)findViewById(R.id.password);
        login_title     = (TextView) findViewById(R.id.login_title);

        edtpassword.setTypeface(Typeface.DEFAULT);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        login_title.setTypeface(typeface);
        edtemail.setTypeface(typeface);
        edtpassword.setTypeface(typeface);
        forgotpass.setTypeface(typeface);
        sign_up.setTypeface(typeface);
        login.setTypeface(typeface);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Activity.this, Forgot_pass.class);
                startActivity(i);
                finish();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Activity.this, Sign_up.class);
                startActivity(i);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isNetworkAvailable();
                if(isNetworkAvailable() == true) {
                     attemptLogin();
                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Login_Activity.this).create();
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

    private void attemptLogin() {
        edtemail.setError(null);
        edtpassword.setError(null);

        email       = edtemail.getText().toString().trim();
        username    = edtemail.getText().toString().trim();
        password    = edtpassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            edtpassword.setError(getString(R.string.error_field_required));
            focusView = edtpassword;
            cancel = true;
        }
        else if (!isPasswordValid(password)) {
            edtpassword.setError(getString(R.string.error_invalid_password));
            focusView = edtpassword;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtemail.setError(getString(R.string.error_field_required));
            focusView = edtemail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            Log.e("username.........", username);
            Log.e("password.........", password);
            Log.e("email...........", email);

            hideKeypad();

            String tag_string_req = "req_login";

            Log.e("tag_string_req....", tag_string_req);

            pDialog.setMessage("Logging in...");
            showDialog();

            Log.e("tag_string_reqghghghgj.", tag_string_req);
            StringRequest stringRequest = new StringRequest(Method.POST, AppConfig.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Login Response: " + response.toString());
                            hideDialog();
                            Log.d("vgfgfg","hello");
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String code = jObj.getString("code");

                                if (code.equals("jwt_auth_success")) {

                                    String token_n = jObj.getString("token");
                                    String id = jObj.getString("ID");
                                    String user_role_label = jObj.getString("user_role_label");

                                    session.createLoginSession(email, password, username, user_role_label, token, ID);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putString("email", email);
                                    editor.putString("username", username);
                                    editor.putString("password", password);
                                    editor.putString("token", token_n);
                                    editor.putString("user_role_label", user_role_label);
                                    editor.putString("ID", id);

                                    Log.d("second","login");

                                    editor.commit();

                                   // Toast.makeText(getApplicationContext(), code, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login_Activity.this, Auction_Screen_Main.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(code.equals("jwt_auth_failed"))
                                {
                                    String message = jObj.getString("message");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Login Error: " + error.getMessage());
                           Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            hideDialog();
                            if(error instanceof AuthFailureError) {
                                String str = null;
                                try {
                                    str = new String(error.networkResponse.data, "UTF8");
                                    Log.e("str............", str);
                                    JSONObject errorJson = new JSONObject(str);
                                    String code = errorJson.getString("code");
                                    if(code.equals("jwt_auth_failed"))
                                    {
                                        String message = errorJson.getString("message");
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put("username", email);
                    params.put("password", password);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        }
    }

    private void hideKeypad() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isPasswordValid(String password) {
        /*String Password_PATTERN = "^.{8,10}$";

        Pattern pattern = Pattern.compile(Password_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();*/
        return password.length() >= 8;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
            builder.setTitle("Exit Application");
            builder.setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            /*pbutton.setBackgroundResource(R.drawable.btn_g);
            pbutton.setTextColor(Color.WHITE);
            nbutton.setBackgroundResource(R.drawable.btn_g);
            nbutton.setTextColor(Color.WHITE);*/
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
