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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Create_New_Auction_Lot extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button done, done_d;
    private TextView title, title1;
    private EditText auction_n, lot_n;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = Create_New_Auction_Lot.class.getSimpleName();
    private SharedPreferences sharedpreferences;
    private String p_username, p_token, p_email, p_id;
    private ProgressDialog pDialog;
    private String auction;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    int logV = 0;
    private String lot_number;
    private String lot_status;
    private int position;
    private String Auctionname;
    private NavigationView navigationView;
    private String user_role_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_auction_lot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email     = sharedpreferences.getString("email", null);
        p_username  = sharedpreferences.getString("username", null);
        p_token     = sharedpreferences.getString("token", null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id        = sharedpreferences.getString("ID", null);

        Log.e("Details", p_email + " ..username.. " + p_username + " ..tokentoken.. " + p_token + " ..id.." + p_id);

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

        Bundle bundleb = getIntent().getExtras();
        Auctionname = bundleb.getString("name");
        Log.e("Auctionname", Auctionname);

        auction_n   = (EditText) findViewById(R.id.auction);
        lot_n       = (EditText) findViewById(R.id.lot);
        title       = (TextView) findViewById(R.id.title);
        title1      = (TextView) findViewById(R.id.title1);
        done        = (Button) findViewById(R.id.done);
        done_d      = (Button) findViewById(R.id.done_d);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        auction_n.setTypeface(typeface);
        lot_n.setTypeface(typeface);
        title.setTypeface(typeface);
        title1.setTypeface(typeface);
        done.setTypeface(typeface);
        done_d.setTypeface(typeface);
        mTitle.setTypeface(typeface);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        lot_number = lot_n.getText().toString().trim();
        if (lot_n.getText().toString().length() == 0 ) {

            done.setEnabled(false);
            done_d.setVisibility(View.VISIBLE);
            done_d.setEnabled(false);
        }

        lot_n.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Toast.makeText(New_lot.this, lot_name.length(), Toast.LENGTH_SHORT).show();
                Log.e("lot_name.M ..", "" + lot_n.getText().toString().length());

                if (lot_n.getText().toString().length() == 0) {
                    Log.e("lot_namen...", "" + lot_n.getText().toString().length());
                    done.setEnabled(false);
                    done_d.setVisibility(View.VISIBLE);
                    done_d.setEnabled(false);
                } else {

                    Log.e("lot_name...", "" + lot_n.getText().toString().length());
                    // Toast.makeText(New_lot.this, lot_name.length(), Toast.LENGTH_SHORT).show();
                    done.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        isNetworkAvailable();
        if (isNetworkAvailable() == true) {
            if (!Auctionname.equals("")) {
                //Toast.makeText(this, "Existing", Toast.LENGTH_SHORT).show();
                auction_n.setText(Auctionname);
                auction_n.setEnabled(false);
                auction_n.setBackgroundResource(R.drawable.desable_edit);
                lot_n.requestFocus();
            }
            else {
                auction = auction_n.getText().toString().trim();
                String tag_string_req = "req_user_setting";
                pDialog.setMessage("Please Wait ...");
                showDialog();

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.URL_Auction_new + auction, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        hideDialog();
                        if (logV == 1) {
                            Log.d(TAG, "user details Response: " + response.toString());
                            Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_Auction_new + auction);
                        }

                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (logV == 1) {
                                Log.e(TAG, "try if: " + AppConfig.URL_Auction_new + auction);
                            }
                            String code = (jObj.getString("code"));
                            if (code.equals("auction_exists")) {
                                auction_n.setText(jObj.getString("name"));
                                Log.e("dd", "." + auction_n.length());
                                auction_n.setSelection(auction_n.length());
                            } else if (code.equals("auction_already_exists_but_not_used")) {
                                auction_n.setText(jObj.getString("name"));
                                Log.e("dd", "." + auction_n.length());
                                auction_n.setSelection(auction_n.length());
                            } else if (code.equals("auction_no_is_not_valid")) {
                                String message = (jObj.getString("message"));
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Log.e("dd", "." + auction_n.length());
                                auction_n.setSelection(auction_n.length());
                            } else if (code.equals("auction_already_exists")) {
                                Toast.makeText(getApplicationContext(), "Auction already exists", Toast.LENGTH_SHORT).show();
                                done.setEnabled(false);
                                Log.e("dd", "." + auction_n.length());
                                auction_n.setSelection(auction_n.length());
                            } else if (code.equals("auction_generated")) {
                                auction_n.setText(jObj.getString("name"));
                                Log.e("dd", "." + auction_n.length());
                                auction_n.setSelection(auction_n.length());
                            } else if (code.equals("rest_user_invalid_id")) {
                                String message = (jObj.getString("message"));
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                done.setEnabled(false);
                                Log.e("dd", "." + auction_n.length());
                                auction_n.setSelection(auction_n.length());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "catch : " + AppConfig.URL_Auction_new + auction);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "Error: " + error.getMessage());
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
                strReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        }
        else
        {
            Log.e("Auctionnameeee", Auctionname);

            if (!Auctionname.equals("")) {
            auction_n.setText(Auctionname);
            auction_n.setSelection(auction_n.length());
            }
            /*AlertDialog alertDialog = new AlertDialog.Builder(Create_New_Auction_Lot.this).create();
            alertDialog.setMessage("No Internet Connection");
            alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            alertDialog.show();*/
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable() == true) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    auction = auction_n.getText().toString().trim();
                    Log.e(TAG, " auction..... : " + auction);

                    if (auction_n.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Enter Auction Number", Toast.LENGTH_SHORT).show();
                    } else {

                        int Auctionname_int = Integer.parseInt(auction);
                        Log.e(TAG, " lot_number_int : " + Auctionname_int );
                        if(Auctionname_int == 00)
                        {
                            Toast.makeText(Create_New_Auction_Lot.this, "Auction Number is not valid", Toast.LENGTH_SHORT).show();
                        } else if (!Auctionname.equals("")) {
                            // Toast.makeText(Create_New_Auction_Lot.this, "Existing", Toast.LENGTH_SHORT).show();
                            auction = auction_n.getText().toString().trim();
                            addNewLot();
                        } else {

                            auction = auction_n.getText().toString().trim();
                            String tag_string_req = "req_user_setting";

                            pDialog.setMessage("Please Wait ...");
                            showDialog();

                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    AppConfig.URL_Auction_new + auction, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    if (logV == 1) {
                                        Log.d(TAG, "user details Response: " + response.toString());
                                        Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_Auction_new + auction);
                                    }
                                    try {
                                        JSONObject jObj = new JSONObject(response);

                                        Log.e(TAG, "try if: " + AppConfig.URL_Auction_new + auction);
                                        String code = (jObj.getString("code"));
                                        Log.e("code,,,,,", code);

                                        if (code.equals("auction_exists")) {

                                            auction_n.setText(jObj.getString("name"));
                                            addNewLot();
                                       /* Intent i = new Intent(Create_New_Auction_Lot.this, New_lot.class);
                                        i.putExtra("name", auction);
                                        startActivity(i);*/

                                        } else if (code.equals("auction_already_exists_but_not_used")) {

                                            auction_n.setText(jObj.getString("name"));
                                            addNewLot();
                                       /* Intent i = new Intent(Create_New_Auction_Lot.this, New_lot.class);
                                        i.putExtra("name", auction);
                                        startActivity(i);*/

                                        } else if (code.equals("auction_no_is_not_valid")) {

                                            hideDialog();
                                            String message = (jObj.getString("message"));
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        } else if (code.equals("auction_already_exists")) {

                                            hideDialog();
                                            Toast.makeText(getApplicationContext(), "Auction already exists", Toast.LENGTH_SHORT).show();

                                        } else if (code.equals("auction_generated")) {

                                            addNewLot();

                                        } else if (code.equals("rest_user_invalid_id")) {
                                            hideDialog();
                                            String message = (jObj.getString("message"));
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "catch : " + AppConfig.URL_Auction_new + auction);
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

                                    params.put("name", auction);
                                    params.put("Authorization", "Bearer " + p_token);
                                    return params;
                                }

                                public Map<String, String> getHeaders() {

                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("Authorization", "Bearer " + p_token);
                                    return headers;

                                }
                            };
                            strReq.setRetryPolicy(new DefaultRetryPolicy(15000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                        }
                    }
                } else {


                    auction = auction_n.getText().toString().trim();
                    lot_number = lot_n.getText().toString().trim();
                    lot_status = "draft";
                    Log.e("lot_status", lot_status);

                    if (auction.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Enter Auction Number", Toast.LENGTH_SHORT).show();
                    } else {
                        int Auctionname_int = Integer.parseInt(auction);
                        int lot_number_int = Integer.parseInt(lot_number);
                        Log.e(TAG, " auction_number_int : " + Auctionname_int );
                        if(Auctionname_int == 00)
                        {
                            Toast.makeText(Create_New_Auction_Lot.this, "Auction Number is not valid", Toast.LENGTH_SHORT).show();
                        }
                        else if(lot_number_int == 00){
                            Toast.makeText(Create_New_Auction_Lot.this, "Lot Number is not valid", Toast.LENGTH_SHORT).show();
                        }
                       /* else if (!Auctionname.equals("")) {
                            auction = auction_n.getText().toString().trim();
                        }*/
                        else {
                            auction_n.setText(auction);
                            //auction_n.setEnabled(false);
                            MyDatabaseHelper helper = new MyDatabaseHelper(Create_New_Auction_Lot.this);
                            helper.CheckExist(auction, lot_number, lot_status);
                            Log.e("helper", helper+"");
                            LotDetails l = new LotDetails();
                            l.setAuction_no(auction);
                            l.setLot_no(lot_number);

                            /*Intent i = new Intent(Create_New_Auction_Lot.this, New_Lot_Entry.class);
                            i.putExtra("name", auction);
                            i.putExtra("lotname", lot_number);
                            i.putExtra("lot_status", lot_status);
                            startActivity(i);*/
                        }
                    }
                }
            }
        });
    }

    private void addNewLot() {
        if(isNetworkAvailable() == true)
        {
            lot_n.setError(null);

            lot_number = lot_n.getText().toString().trim();
            // Store values at the time of the login attempt.

            int lot_number_int = Integer.parseInt(lot_number);

            Log.e(TAG, " lot_number_int : " + lot_number_int );
            if(lot_number_int == 00)
            {
                hideDialog();
                Toast.makeText(this, "Lot Number is not valid", Toast.LENGTH_SHORT).show();
            }
            else {
                String tag_string_req = "req_user_setting";

                pDialog.setMessage("Please Wait ...");
                showDialog();

                Log.e(TAG, " tag_string_req : " + tag_string_req);
                Log.e(TAG, " AppConfig.URL_lot_new : " + AppConfig.URL_New_lot + auction + "&lot_no=" + lot_number + "&generate_lot=1");

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.URL_New_lot + auction + "&lot_no=" + lot_number + "&generate_lot=1",
                        new Response.Listener<String>() {

                            public String code;

                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "user details Response: " + response.toString());
                                hideDialog();
                                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_New_lot + auction + "&lot_no=" + lot_number + "&generate_lot=1");

                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    code = (jObj.getString("code"));
                                    Log.e("Tag..", code);
                                    //lot exists

                                    if (code.equals("created lot")) {

                                        JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                        // Jobj1.getString("lot_no");
                                        lot_status = Jobj1.getString("lot_status");
                                        Log.e("Lotstatus", lot_status);
                                        //lot.setText(Jobj1.getString("lot_no"));
                                        Log.e("Lot", lot_number + "....." + lot_status);
                                        if (lot_status.equals("draft")) {
                                            Intent i = new Intent(Create_New_Auction_Lot.this, New_Lot_Entry.class);
                                            i.putExtra("name", auction);
                                            i.putExtra("lotname", lot_number);
                                            i.putExtra("lot_status", lot_status);
                                            startActivity(i);
                                        }
                                    } else if (code.equals("generated_lot")) {
                                        JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                        // Jobj1.getString("lot_no");
                                        lot_status = Jobj1.getString("lot_status");
                                        Log.e("Lotstatus", lot_status);
                                        Log.e("Loghggjgjt", lot_number + "....." + lot_status);
                                        Intent i = new Intent(Create_New_Auction_Lot.this, New_Lot_Entry.class);
                                        i.putExtra("name", auction);
                                        i.putExtra("lotname", lot_number);
                                        i.putExtra("lot_status", lot_status);
                                        startActivity(i);
                                    } else if (code.equals("lot_not_used_yet")) {
                                        JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                        // Jobj1.getString("lot_no");
                                        lot_status = Jobj1.getString("lot_status");
                                        Log.e("Lotstatus", lot_status);
                                        Intent i = new Intent(Create_New_Auction_Lot.this, New_Lot_Entry.class);
                                        i.putExtra("name", auction);
                                        i.putExtra("lotname", lot_number);
                                        i.putExtra("lot_status", lot_status);
                                        startActivity(i);
                                    } else if (code.equals("updated_lot")) {
                                        JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                        // Jobj1.getString("lot_no");
                                        lot_status = Jobj1.getString("lot_status");
                                        Log.e("Lotstatus", lot_status);
                                        Intent i = new Intent(Create_New_Auction_Lot.this, New_Lot_Entry.class);
                                        i.putExtra("name", auction);
                                        i.putExtra("lotname", lot_number);
                                        i.putExtra("lot_status", lot_status);
                                        startActivity(i);
                                    }
                                    else if (code.equals("lot_already_exist")) {
                                        String message = (jObj.getString("message"));
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    } else if (code.equals("lot_no_is_not_valid")) {
                                        String message = (jObj.getString("message"));
                                        Log.e("messagebfbgfh", message);
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    } else if (code.equals("rest_invalid_lot_no")) {
                                        String message = (jObj.getString("message"));
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    } else if (code.equals("unable to create lot")) {
                                        String message = (jObj.getString("message"));
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    } else if (code.equals("lot does not exist")) {
                                        String message = (jObj.getString("message"));
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "Catch:" + e);
                                    Log.e(TAG, "catch :" + AppConfig.URL_New_lot + auction + "&lot_no=" + lot_number + "&generate_lot=1");
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
                        params.put("lot_no", lot_number);
                        params.put("Authorization", "Bearer " + p_token);
                        return params;
                    }

                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer " + p_token);
                        return headers;
                    }
                };
                strReq.setRetryPolicy(new DefaultRetryPolicy(15000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(Create_New_Auction_Lot.this).create();
            alertDialog.setMessage("No Internet Connection");
            alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    isNetworkAvailable();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(Create_New_Auction_Lot.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add__new__auction, menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_setting) {
            Intent i = new Intent(Create_New_Auction_Lot.this, User_Settings.class);
            startActivity(i);
            finish();

        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Create_New_Auction_Lot.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else if (id == R.id.history) {
            Intent i = new Intent(Create_New_Auction_Lot.this, Auction_History.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.home) {
            Intent i = new Intent(Create_New_Auction_Lot.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Create_New_Auction_Lot.this, New_lot.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

