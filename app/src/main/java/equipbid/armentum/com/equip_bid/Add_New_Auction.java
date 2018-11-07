package equipbid.armentum.com.equip_bid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_New_Auction extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button add_new_auction, add_new_lot;
    private TextView title, input_field_txt;
    private EditText auction_n, lot_n;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = Add_New_Auction.class.getSimpleName();
    private SharedPreferences sharedpreferences;
    private String p_username, p_token, p_email, p_id;
    private ProgressDialog pDialog;
    private String auction;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    int logV = 0;
    private Button update,delete;
    private String Auction_name;
    private ArrayList<String> User_name_list;
    private MultiAutoCompleteTextView MultipleValuesholdt;
   // private UserAdapterName adapter;
    private JSONArray User_Detail_Arr;
    private String Auction_number;
    private String mul;
    private ArrayAdapter<String> TopicName;
    private String name;
    private JSONObject c;
    private String user_role_label;
    private NavigationView navigationView;
    private String id;
    private ArrayList<String> User_id_name_list;
    private List<User> userList = new ArrayList<User>();
    private User auct_list;
    private String userid;
    private ArrayList<String> UId;
    private String user_ids;
    private ArrayList<String> UName;
    private String titleD, titleU, NoteD, NoteU;
    private String temp;
    private ArrayAdapter<User> adapteruser;
    private TextView Username;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__auction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email     = sharedpreferences.getString("email",null);
        p_username  = sharedpreferences.getString("username",null);
        p_token     = sharedpreferences.getString("token",null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id        = sharedpreferences.getString("ID",null);

        titleD = "Are you sure you want to delete?";
        titleU = "Are you sure you want to update?";
        NoteD  = "Note: This will also delete all lots under this auction";
        NoteU  = "Note: This will also update all lots under this auction";

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

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

        Bundle bundle   = getIntent().getExtras();
        Auction_name    = bundle.getString("name");
        temp    = bundle.getString("temp");

        auction_n           = (EditText)findViewById(R.id.auction_number);
        title               = (TextView)findViewById(R.id.name_txt);
        input_field_txt     = (TextView)findViewById(R.id.input_field_txt);
        update              = (Button)findViewById(R.id.update);
        delete              = (Button)findViewById(R.id.delete);
        MultipleValuesholdt = (MultiAutoCompleteTextView)findViewById(R.id.MultiAutoCompleteTextView1);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        auction_n.setTypeface(typeface);
        title.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        input_field_txt.setTypeface(typeface);
        MultipleValuesholdt.setTypeface(typeface);
        //MultipleValuesholdt.setSelection(MultipleValuesholdt.getText().length());
        pDialog     = new ProgressDialog(this);
        pDialog.setCancelable(false);

        auction_n.setText(Auction_name);
        auction_n.setSelection(auction_n.length());
        auction_n.setEnabled(false);
        User_name_list  = new ArrayList<String>();
        User_id_name_list  = new ArrayList<String>();

        isNetworkAvailable();
        if(isNetworkAvailable() == true)
        {
            Auction_number = auction_n.getText().toString();

            String tag_string_req = "req_user_setting";
            pDialog.setMessage("Please Wait ...");
            showDialog();

            Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.User_Details_All);

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.User_Details_All, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "user details Response: " + response.toString());
                    //hideDialog();
                    try {

                        User_Detail_Arr = new JSONArray(response);
                        Log.e(TAG, "try : " + AppConfig.User_Details_All);
                        for (int i = 0; i < User_Detail_Arr.length(); i++) {
                            try {
                                c = User_Detail_Arr.getJSONObject(i);
                                name = c.getString("name");
                                id = c.getString("id");
                                User_name_list.add(name);
                                User_id_name_list.add(id);
                                auct_list = new User();
                                auct_list.setName(name);
                                auct_list.setId(id);
                                userList.add(auct_list);

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.e("User_name_list", User_name_list+"");
                        Log.e("User_name_list", User_name_list.size()+"");
                        Log.e("User_name_list", User_id_name_list+"");
                        Log.e("User_name_list", User_id_name_list.size()+"");
                        Log.e("User_name_list", userList.size()+"");
                        Log.e("User_name_list", new Gson().toJson(userList)+"");

                        getMethod(Auction_number);

                        TopicName = new ArrayAdapter<String>(Add_New_Auction.this, android.R.layout.simple_list_item_1, User_name_list);
                        MultipleValuesholdt.setAdapter(TopicName);
                        MultipleValuesholdt.setTypeface(typeface);
                        MultipleValuesholdt.setThreshold(1);
                        MultipleValuesholdt.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                        mul= MultipleValuesholdt.getText().toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "catch : " + AppConfig.User_Details_All);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),"Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    hideDialog();
                    if(error instanceof ServerError) {
                    }
                }
            })
            {
                @Override
                protected Map<String, String> getParams() {
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
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(Add_New_Auction.this).create();
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleValuesholdt.setError(null);

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(MultipleValuesholdt.getText().toString())) {
                    MultipleValuesholdt.setError(getString(R.string.error_field_required));
                    focusView = MultipleValuesholdt;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {

                    if (isNetworkAvailable() == true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Add_New_Auction.this);
                        builder.setTitle(titleU);
                        builder.setMessage(NoteU)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Auction_number = auction_n.getText().toString();
                                        Log.e("mufgfgfl",  MultipleValuesholdt.getText().toString());
                                        Log.e("userList",  userList+"");
                                        String[] str = MultipleValuesholdt.getText().toString().split(", ");
                                        Log.e("str",  str+"");
                                        UId = new ArrayList<String>();

                                        for(int i=0; i<str.length; i++) {
                                            for(int j = 0; j<userList.size(); j++)
                                            {
                                                if(str[i].equals(userList.get(j).getName()))
                                                {
                                                    userid = userList.get(j).getId() + ", ";
                                                    UId.add(userList.get(j).getId());
                                                }
                                            }
                                        }
                                        Log.e("UId",  UId+"");
                                        user_ids = UId.toString().replace("[", "").replace("]", "");
                                        Log.e("user_idsggg", user_ids);
                                        //Toast.makeText(getBaseContext(), MultipleValuesholdt.getText().toString(), Toast.LENGTH_SHORT).show();
                                        updateMethod(Auction_number, user_ids);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {

                        AlertDialog alertDialog = new AlertDialog.Builder(Add_New_Auction.this).create();
                        alertDialog.setMessage("No Internet Connection");
                        alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                isNetworkAvailable();
                            }


                        });
                        alertDialog.show();
                    }
                }


            }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable() == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Add_New_Auction.this);
                    builder.setTitle(titleD);
                    builder.setMessage(NoteD)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Auction_number = auction_n.getText().toString();
                                    deleteMethod(Auction_number);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(Add_New_Auction.this).create();
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

    private void updateMethod(final String auction_number,final String user_ids) {

        String tag_string_req = "req_Update";
        Log.e("tag_string_req....", tag_string_req);
        pDialog.setMessage("Please Wait..");
        showDialog();

        Log.e("tag_string_reqghghghgj.", tag_string_req);
        StringRequest stringRequest = new StringRequest(Method.POST, AppConfig.Update_Delete_API+auction_number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response.toString());
                        hideDialog();
                        Log.d("vgfgfg","hello");
                        Log.e("stringRequest", AppConfig.Update_Delete_API+auction_number);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String code = jObj.getString("code");
                            Log.e("code", code);
                            if (code.equals("rest_results_found")) {

                                Toast.makeText(getApplicationContext(), "Auction Updated Successfully!!", Toast.LENGTH_SHORT).show();
                                if(temp.equals("History"))
                                {
                                    Intent intent = new Intent(Add_New_Auction.this, Auction_History.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(temp.equals("Existing"))
                                {
                                    Intent intent = new Intent(Add_New_Auction.this, Load_Existing_Auction.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                            else if(code.equals("rest_no_results"))
                            {
                                String message = jObj.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                           // Toast.makeText(getApplicationContext(), "jjjjjJson error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        hideDialog();
                        if(error instanceof AuthFailureError) {
                            String str = null;
                            try {
                                str = new String(error.networkResponse.data, "UTF8");
                                Log.e("str............", str);
                                JSONObject errorJson = new JSONObject(str);
                                String code = errorJson.getString("code");
                                if(code.equals("rest_results_found"))
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
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                if(user_ids != null)params.put("user_ids", user_ids);
                return params;
            }

            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + p_token);
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void deleteMethod(final String auction_number) {

            Log.e("auction_number...", auction_number);

            String tag_string_req = "req_Delete";
            Log.e("tag_string_req....", tag_string_req);
            pDialog.setMessage("Please Wait..");
            showDialog();

            Log.e("tag_string_reqghghghgj.", tag_string_req);
            StringRequest stringRequest = new StringRequest(Method.DELETE, AppConfig.Update_Delete_API+auction_number,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Login Response: " + response.toString());
                            hideDialog();
                            Log.d("vgfgfg","hello");
                            Log.e("stringRequest", AppConfig.Update_Delete_API+auction_number);
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String code = jObj.getString("code");
                                //Toast.makeText(getApplicationContext(), code, Toast.LENGTH_SHORT).show();

                                 if (code.equals("rest_results_found")) {

                                    Toast.makeText(getApplicationContext(), "Auction Deleted Successfully!!", Toast.LENGTH_SHORT).show();
                                     if(temp.equals("History"))
                                     {
                                         Intent intent = new Intent(Add_New_Auction.this, Auction_History.class);
                                         startActivity(intent);
                                         finish();
                                     }
                                     else if(temp.equals("Existing"))
                                     {
                                         Intent intent = new Intent(Add_New_Auction.this, Load_Existing_Auction.class);
                                         startActivity(intent);
                                         finish();
                                     }
                                }
                                else if(code.equals("rest_no_results"))
                                {
                                    String message = jObj.getString("message");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                               // Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            /*if(response.equalsIgnoreCase(LOGIN_SUCCESS)){
                                //Creating a shared preference
                                //Starting profile activity
                                Toast.makeText(getApplicationContext(),"login",Toast.LENGTH_SHORT).show();
                            }else{
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(Login_Activity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Login Error: " + error.getMessage());
                            // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            hideDialog();
                            if(error instanceof AuthFailureError) {
                                String str = null;
                                try {
                                    str = new String(error.networkResponse.data, "UTF8");
                                    Log.e("str............", str);
                                    JSONObject errorJson = new JSONObject(str);
                                    String code = errorJson.getString("code");
                                    if(code.equals("rest_results_found"))
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
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer " + p_token);
                    return headers;
                }
            };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getMethod(final String auction_number) {

        Log.e("auction_number...", auction_number);
        String tag_string_req = "req_Delete";
        Log.e("tag_string_req....", tag_string_req);
        pDialog.setMessage("Please Wait..");
        showDialog();

        Log.e("tag_string_reqghghghgj.", tag_string_req);
        StringRequest stringRequest = new StringRequest(Method.GET, AppConfig.Update_Delete_API+auction_number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response.toString());
                        hideDialog();
                        Log.e("stringRequest", AppConfig.Update_Delete_API+auction_number);
                        UName = new ArrayList<String>();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String code = jObj.getString("code");
                            Log.e("code", code);
                            if (code.equals("rest_results_found")) {
                                JSONObject data  = jObj.getJSONObject("data");
                                Log.e("datasss", data+"");
                                JSONArray auction_arry = data.getJSONArray("auction");
                                Log.e("auction_arry", auction_arry.length()+"");
                                for (int i = 0; i < auction_arry.length(); i++) {

                                    try {
                                        JSONObject c = auction_arry.getJSONObject(i);
                                        Log.e("ccccccc", c+"");
                                        String name_j = c.getString("name");

                                        Log.e("name", name_j);
                                        JSONArray jArray1 = c.getJSONArray("user_ids");
                                        Log.e("jArray1", jArray1.length()+"");

                                        for (int j = 0; j < jArray1.length(); j++) {
                                            Log.e("jArray1vvv", jArray1.get(j)+"");
                                            Log.e("userList", userList.size()+"");
                                            for(int k = 0; k<userList.size(); k++)
                                            {
                                                String ss = jArray1.get(j).toString();
                                                //Log.e("dd", jArray1.get(j).toString())
                                                if(ss.equals(userList.get(k).getId()))
                                                {
                                                    Log.e("fff",jArray1.get(j)+".."+userList.get(k).getId());
                                                  //  Toast.makeText(Add_New_Auction.this, userList.get(k).getName(), Toast.LENGTH_SHORT).show();
                                                    UName.add(userList.get(k).getName());
                                                }
                                            }
                                            Log.e("UName1", UName+"");
                                        }
                                        Log.e("UName11", UName+"");
                                        String user_names = UName.toString().replace("[", "").replace("]", "");
                                        Log.e("user_names", user_names);
                                        user_names = user_names+", ";
                                        Log.e("user_names", user_names);
                                        Log.e("user_names", user_names.length()+"");
                                        MultipleValuesholdt.setText(user_names);

                                        Log.e("user_names", MultipleValuesholdt.getText().length()+"");
                                        MultipleValuesholdt.setSelection(MultipleValuesholdt.getText().length());
                                        MultipleValuesholdt.requestFocus();
                                        MultipleValuesholdt.setTypeface(typeface);
                                    }

                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //auction = sys.getString("country");
                                /*String data = jObj.getString("data");
                                Log.e("datahh", data);
                                JSONObject Jobj1 = jObj.getJSONObject("auction");
                                Log.e("Jobj1", Jobj1+"");
                                */
                            }
                            else if(code.equals("rest_no_results"))
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
                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        hideDialog();
                        if(error instanceof AuthFailureError) {
                            String str = null;
                            try {
                                str = new String(error.networkResponse.data, "UTF8");
                                Log.e("str............", str);
                                JSONObject errorJson = new JSONObject(str);
                                String code = errorJson.getString("code");
                                if(code.equals("rest_results_found"))
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
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + p_token);
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            Intent i = new Intent(Add_New_Auction.this, User_Settings.class);
            startActivity(i);
            finish();

        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Add_New_Auction.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else if (id == R.id.history) {
            Intent i = new Intent(Add_New_Auction.this, Auction_History.class);
            startActivity(i);
            finish();
        }
         else if (id == R.id.home) {
            Intent i = new Intent(Add_New_Auction.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Add_New_Auction.this, New_lot.class);
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

    private void setTextSample(String contactName) {

        final SpannableStringBuilder sb = new SpannableStringBuilder();
        TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.test, null);
        tv.setText(contactName);
        BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(tv);
        bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());

        sb.append(contactName + ",");
        sb.setSpan(new ImageSpan(bd), sb.length()-(contactName.length()+1),
                sb.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        MultipleValuesholdt.setText(sb);
    }

    // wrap text with custom elements
    private static Object convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }
}
