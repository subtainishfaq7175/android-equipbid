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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_pass extends AppCompatActivity {

    private Button sign_up, send;
    private EditText _email;
    private TextView dont, for_title;
    private String email;
    private static final String TAG = Forgot_pass.class.getSimpleName();
    private ProgressDialog pDialog;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        sign_up     = (Button)findViewById(R.id.sign_up);
        send        = (Button)findViewById(R.id.send);
        _email       = (EditText)findViewById(R.id.email);
        dont        = (TextView)findViewById(R.id.dont);
        for_title   = (TextView)findViewById(R.id.for_title);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        for_title.setTypeface(typeface);
        _email.setTypeface(typeface);
        dont.setTypeface(typeface);
        sign_up.setTypeface(typeface);
        send.setTypeface(typeface);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        isNetworkAvailable();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()==true)
                {
                    Intent i = new Intent(Forgot_pass.this, Sign_up.class);
                    startActivity(i);
                    finish();
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(Forgot_pass.this).create();
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()==true)
                {
                    _email.setError(null);

                    email 		= _email.getText().toString().trim();

                    boolean cancel = false;
                    View focusView = null;

                    if (TextUtils.isEmpty(email)) {
                        _email.setError(getString(R.string.error_field_required));
                        focusView = _email;
                        cancel = true;
                    }

                    if (cancel) {
                        focusView.requestFocus();
                    } else {

                        Log.e(TAG, " email : " + email );
                        forgotPass(email);
                    }

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Forgot_pass.this).create();
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void forgotPass(final String email) {
        String tag_string_req = "req_Forgot";

        pDialog.setMessage("Please Wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_FORGOT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Forgot_Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_FORGOT );
                    String code = jObj.getString("code");

                    Log.e("cofghfhgfhgfhde",code);

                    if (code.equals("forgot_password_sent_success"))
                    {
                        Toast.makeText(getApplicationContext(), "Reset Password Link sent successfully to your Email Id !!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Forgot_pass.this, Login_Activity.class);
                        startActivity(i);
                        finish();
                    }
                    else if(code.equals("rest_user_find"))
                    {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                    else if(code.equals("rest_invalid_param"))
                    {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                    else if(code.equals("rest_no_route"))
                    {
                        Toast.makeText(getApplicationContext(), "Reset Password Link sent successfully to your Email Id !!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Forgot_pass.this, Login_Activity.class);
                        startActivity(i);
                        finish();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               hideDialog();
                Log.e(TAG, "trygfhghgjh : " + AppConfig.URL_FORGOT );
                Log.e(TAG, "trygfhghgjh : " + error );

                finish();
                if(error instanceof TimeoutError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if (code.equals("forgot_password_sent_success"))
                        {
                            Toast.makeText(getApplicationContext(), "Reset Password Link sent successfully to your Email Id !!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Forgot_pass.this, Login_Activity.class);
                            startActivity(i);
                        }
                        else if(code.equals("rest_user_find"))
                        {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        }
                        else if(code.equals("rest_invalid_param"))
                        {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        }
                        else if(code.equals("rest_no_route"))
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(15000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

        if(isNetworkAvailable()==true)
        {
            Intent intent = new Intent(Forgot_pass.this, Login_Activity.class);
            startActivity(intent);
            finish();
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(Forgot_pass.this).create();
            alertDialog.setMessage("No Internet Connection");
            alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    isNetworkAvailable();
                }
            });
            alertDialog.show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
