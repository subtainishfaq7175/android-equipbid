package equipbid.armentum.com.equip_bid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.volley.Request.Method;
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

public class Sign_up extends AppCompatActivity {

    private Button sign_up;
    private EditText _email, _password, _firstname, _lastname, _username, _phone ;
    private TextView sign_title;
    private String phone, first_name, last_name,email, password, username;
    private static final String TAG = Sign_up.class.getSimpleName();
    private ProgressDialog pDialog;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up      = (Button)findViewById(R.id.sign_up);
        _email       = (EditText)findViewById(R.id.email);
        _password    = (EditText)findViewById(R.id.password);
        _firstname   = (EditText)findViewById(R.id.firstname);
        _lastname    = (EditText)findViewById(R.id.lastname);
        _username    = (EditText)findViewById(R.id.username);
        _phone       = (EditText)findViewById(R.id.mobile);
        sign_title   = (TextView) findViewById(R.id.sign_title);

        _firstname.requestFocus();
        _password.setTypeface(Typeface.DEFAULT);

        Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        sign_title.setTypeface(typeface);
        _firstname.setTypeface(typeface);
        _lastname.setTypeface(typeface);
        _username.setTypeface(typeface);
        _phone.setTypeface(typeface);
        _email.setTypeface(typeface);
        _password.setTypeface(typeface);
        sign_up.setTypeface(typeface);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkAvailable();
                if(isNetworkAvailable() == true)
                {
                    _firstname.setError(null);
                    _lastname.setError(null);
                    _email.setError(null);
                    _username.setError(null);
                    _phone.setError(null);
                    _password.setError(null);

                    // Store values at the time of the login attempt.
                    first_name  = _firstname.getText().toString().trim();
                    last_name 	= _lastname.getText().toString().trim();
                    phone 	    = _phone.getText().toString().trim();
                    email 		= _email.getText().toString().trim();
                    username 	= _username.getText().toString().trim();
                    password 	= _password.getText().toString().trim();

                    boolean cancel = false;
                    View focusView = null;

                    if (TextUtils.isEmpty(password)) {
                        _password.setError(getString(R.string.error_field_required));
                        focusView = _password;
                        cancel = true;
                    }
                    else if (!isPasswordValid(password)) {
                        _password.setError(getString(R.string.error_invalid_password));
                        focusView = _password;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(phone)) {
                        _phone.setError(getString(R.string.error_field_required));
                        focusView = _phone;
                        cancel = true;
                    }
                    else if (!isValidMobile(phone)) {
                        _phone.setError("This Mobile No. is Invalid");
                        focusView = _phone;
                        cancel = true;
                    }

                    if (TextUtils.isEmpty(email)) {
                        _email.setError(getString(R.string.error_field_required));
                        focusView = _email;
                        cancel = true;
                    }
                    else if (!isValidEmail(email)) {
                        _email.setError("This Email Id is Invalid");
                        focusView = _email;
                        cancel = true;
                    }

                    if (TextUtils.isEmpty(username)) {
                        _username.setError(getString(R.string.error_field_required));
                        focusView = _username;
                        cancel = true;
                    }
                    else if (!isUsernameValid(username)) {
                        _username.setError("5 characters with no special characters UserName is required");
                        focusView = _username;
                        cancel = true;
                    }

                    if (TextUtils.isEmpty(last_name)) {
                        _lastname.setError(getString(R.string.error_field_required));
                        focusView = _lastname;
                        cancel = true;
                    }
                    else if (!isLastValid(last_name)) {
                        _lastname.setError("3 characters LastName is required");
                        focusView = _lastname;
                        cancel = true;
                    }

                    if (TextUtils.isEmpty(first_name)) {
                        _firstname.setError(getString(R.string.error_field_required));
                        focusView = _firstname;
                        cancel = true;
                    }
                    else if (!isFirstValid(first_name)) {
                        _firstname.setError("3 characters FirstName is required");
                        focusView = _firstname;
                        cancel = true;
                    }


                    if (cancel) {
                        focusView.requestFocus();
                    } else {
                        Log.e(TAG, " firstName : " + first_name );
                        Log.e(TAG, " lastName : " + last_name );
                        Log.e(TAG, " phone : " + phone );
                        Log.e(TAG, " email : " + email );
                        Log.e(TAG, " username : " + username );
                        Log.e(TAG, " password has : " + password);
                        registerUser1(first_name, last_name, email, username, phone,  password);
                    }
                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Sign_up.this).create();
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

    private boolean isPasswordValid(String password) {
        /*String Password_PATTERN = "^.{8,10}$";
        Pattern pattern = Pattern.compile(Password_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();*/
        return password.length() >= 8;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isUsernameValid(String username) {
        String USER_PATTERN = "^[a-zA-Z0-9]{5,}$";

        Pattern pattern = Pattern.compile(USER_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
       // return username.length() >= 5;
    }

    private boolean isLastValid(String last_name) {

        /*String Last_PATTERN = "[A-Z][a-zA-Z]*";
        Pattern pattern = Pattern.compile(Last_PATTERN);
        Matcher matcher = pattern.matcher(last_name);
        return matcher.matches();*/
        return last_name.length() >= 3;
    }

    private boolean isFirstValid(String first_name) {

        /*String First_PATTERN = "[A-Z][a-zA-Z]*";
        Pattern pattern = Pattern.compile(First_PATTERN);
        Matcher matcher = pattern.matcher(first_name);
        return matcher.matches();*/
        return first_name.length() >= 3;
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void registerUser1(final String first_name, final String last_name,
                               final String email,final String username, final String phone, final String password) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String tag_string_req = "req_login";

        pDialog.setMessage("Signing Up...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Regi Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_REGISTER );
                    String code = jObj.getString("code");
                    //Toast.makeText(getApplicationContext(), "hi...."+  code, Toast.LENGTH_LONG).show();
                    // Check for error node in json
                    if (code.equals("registration_success"))
                    {
                       // Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Signed Up Successfully ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Sign_up.this, Login_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(code.equals("existing_user_login"))
                    {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        _username.requestFocus();
                    }
                    else if(code.equals("existing_user_email"))
                    {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        _email.requestFocus();
                    }
                    else if(code.equals("rest_invalid_param"))
                    {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        _email.requestFocus();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Oops! Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(getApplicationContext(), "Error in Registration !!", Toast.LENGTH_LONG).show();
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
                            _email.requestFocus();
                        }
                        else if(code.equals("existing_user_login"))
                        {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                            _username.requestFocus();
                        }
                        else if(code.equals("existing_user_email"))
                        {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                            _email.requestFocus();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", first_name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("username", username);
                params.put("password", password);
                params.put("last_name", last_name);
                return params;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sign_up.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}


