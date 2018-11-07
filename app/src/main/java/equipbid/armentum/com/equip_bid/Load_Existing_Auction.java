package equipbid.armentum.com.equip_bid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
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
import java.util.List;
import java.util.Map;

public class Load_Existing_Auction extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CustomListAdapterExisting.Icustomelist {

    private  Button add_new_auction, add_new_auction1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = Load_Existing_Auction.class.getSimpleName();
    private EditText auction_name;
    private ProgressDialog pDialog;
    private String auction_n;
    private ImageView search;
    private List<Auction> categoryList = new ArrayList<Auction>();
    private ListView listView;
    private CustomListAdapterExisting adapter;
    private String thumbnail;
    private ScrollView scrollview, scrollview1, scrollno;
    private LinearLayout linear;
    private TextView auction_found;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private TextView tx1, tx2, tx3, tx4, tx5, tx6;
    int Current_page = 1;
    private NavigationView navigationView;
    private String user_role_label;
    private Button btnLoadMore;
    private String TotalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load__existing__auction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedpreferences   = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email             = sharedpreferences.getString("email",null);
        p_username          = sharedpreferences.getString("username",null);
        p_token             = sharedpreferences.getString("token",null);
        user_role_label     = sharedpreferences.getString("user_role_label",null);
        p_id                = sharedpreferences.getString("ID",null);

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

        auction_name    = (EditText)findViewById(R.id.auction_name);
        auction_found   = (TextView)findViewById(R.id.auction_found);
        search          = (ImageView)findViewById(R.id.search);
        scrollview      = (ScrollView)findViewById(R.id.scroll);
        scrollno        = (ScrollView)findViewById(R.id.scrollno);
        scrollview1     = (ScrollView)findViewById(R.id.scroll1);
        linear          = (LinearLayout)findViewById(R.id.li2);
        listView        = (ListView) findViewById(R.id.list);

        pDialog     = new ProgressDialog(this);
        pDialog.setCancelable(false);

        tx1            =(TextView)findViewById(R.id.tx1);
        tx2            =(TextView)findViewById(R.id.tx2);
        tx3            =(TextView)findViewById(R.id.tx3);
        tx4            =(TextView)findViewById(R.id.tx4);
        tx5            =(TextView)findViewById(R.id.tx5);
        tx6            =(TextView)findViewById(R.id.tx6);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        auction_name.setTypeface(typeface);
        auction_found.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        tx1.setTypeface(typeface);
        tx2.setTypeface(typeface);
        tx3.setTypeface(typeface);
        tx4.setTypeface(typeface);
        tx5.setTypeface(typeface);
        tx6.setTypeface(typeface);

        btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");
        btnLoadMore.setBackgroundResource(R.drawable.btn_green);
        btnLoadMore.setTextColor(Color.WHITE);
        btnLoadMore.setTextSize(19);
        btnLoadMore.setAllCaps(false);
        btnLoadMore.setTypeface(typeface);

        LinearLayout.MarginLayoutParams params = new LinearLayout.MarginLayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 40, 50, 40);
        btnLoadMore.setLayoutParams(params);

        adapter = new CustomListAdapterExisting(Load_Existing_Auction.this, categoryList, Load_Existing_Auction.this, user_role_label);
        listView.setAdapter(adapter);
        listView.addFooterView(btnLoadMore);
        listView.setDivider(null);
        btnLoadMore.setVisibility(View.GONE);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                loadMoreListView();
            }
        });

        isNetworkAvailable();
        if(isNetworkAvailable() == true)
        {
            Log.e("user_role_label", user_role_label);
            if(user_role_label.equals("Super Admin"))
             {
                 dataForSuperAdmin();
             }
             else {
                dataForAdmin();
             }
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(Load_Existing_Auction.this).create();
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if(isNetworkAvailable() == true)
                {
                    // Store values at the time of the login attempt.
                    auction_n = auction_name.getText().toString().trim();
                    //Toast.makeText(Load_Existing_Auction.this, "Yes", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, " auction_n....... : " + auction_n);
                        if (auction_n.equals("")) {
                            scrollno.setVisibility(View.VISIBLE);
                            scrollview.setVisibility(View.INVISIBLE);
                            linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Log.e(TAG, " auction_n : " + auction_n);
                            Log.e("user_role_label", user_role_label);
                            if(user_role_label.equals("Super Admin"))
                            {
                               // dataForSuperAdmin();
                                getSearchSuperAuction(p_id, p_token,auction_n);
                               // Toast.makeText(Load_Existing_Auction.this, "Hello", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                getSearchAuction(p_id, p_token,auction_n);
                            }
                        }
                }
                else
                {
                        AlertDialog alertDialog = new AlertDialog.Builder(Load_Existing_Auction.this).create();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Auction tempValues = (Auction)categoryList.get(position);
                Intent i = new Intent(Load_Existing_Auction.this, Create_New_Auction_Lot.class);
                i.putExtra("name",tempValues.getName());
                startActivity(i);
            }
        });

        add_new_auction=(Button)findViewById(R.id.add_new_auction);
        add_new_auction.setTypeface(typeface);
        add_new_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Load_Existing_Auction.this, Create_New_Auction_Lot.class);
                i.putExtra("name","");
                startActivity(i);
            }
        });

        add_new_auction1=(Button)findViewById(R.id.add_new_auction1);
        add_new_auction1.setTypeface(typeface);
        add_new_auction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Load_Existing_Auction.this, Create_New_Auction_Lot.class);
                i.putExtra("name","");
                startActivity(i);
            }
        });
    }


    private void dataForSuperAdmin() {
        auction_n = auction_name.getText().toString().trim();
        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.Super_Admin_Auction_list+Current_page);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.Super_Admin_Auction_list+Current_page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.Super_Admin_Auction_list+Current_page);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.Super_Admin_Auction_list+Current_page);
                    Log.e(TAG, "try if: " + AppConfig.Super_Admin_Auction_list+Current_page);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if(code.equals("auctions_found")) {
                        Log.e("Tag..found", code);
                        scrollview.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        JSONArray auction_arry = jObj.getJSONArray("auctions");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String slug = c.getString("slug");
                                String name_j = c.getString("name");
                                String description = c.getString("description");
                                thumbnail = c.getString("thumbnail");

                                Log.e("details slug.....", slug);
                                Log.e("details name_j.....", name_j);
                                Log.e("details description..", description);
                                Log.e("details thumbnail.....", thumbnail);

                                Auction auct_list = new Auction();
                                auct_list.setName(name_j);
                                auct_list.setDescription(description);
                                auct_list.setThumbnail(thumbnail);
                                categoryList.add(auct_list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auction Found");
                        }
                        else if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auction Found");
                        }
                        else
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auctions Found");
                        }
                        int total = Integer.parseInt(TotalPage);
                        if(Current_page == total)
                        {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                        else {
                            btnLoadMore.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else if(code.equals("rest_user_invalid_id"))
                    {
                        Log.e("rest_user_invalid_id", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.VISIBLE);*/
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        Log.e("Tag..not found", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.Super_Admin_Auction_list+Current_page);
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
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                                /*linear.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.VISIBLE);*/
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            Log.e("rest_user_invalid_id", code);
                               /* linear.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.VISIBLE);*/
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
                params.put("Authorization","Bearer "+p_token);
                return params;
            }

            public Map < String, String > getHeaders()  {
                HashMap < String, String > headers = new HashMap < String, String > ();
                headers.put("Authorization", "Bearer "+p_token);
                return headers;
            }

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                Log.e("header", responseHeaders.get("X-WP-TotalPages")+"");
                TotalPage = responseHeaders.get("X-WP-TotalPages");
                return super.parseNetworkResponse(response);
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void dataForAdmin() {
        auction_n = auction_name.getText().toString().trim();
        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_auction_search+auction_n+"&page="+Current_page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
                    Log.e(TAG, "try if: " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
                    String code = jObj.getString("code");
                    String number = jObj.getString("number");
                    Log.e("Tag..", code);
                    if(code.equals("auctions_found")) {
                        Log.e("Tag..found", code);
                        scrollview.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        JSONArray auction_arry = jObj.getJSONArray("auctions");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String slug = c.getString("slug");
                                String name_j = c.getString("name");
                                String description = c.getString("description");
                                thumbnail = c.getString("thumbnail");

                                Log.e("details slug.....", slug);
                                Log.e("details name_j.....", name_j);
                                Log.e("details description..", description);
                                Log.e("details thumbnail.....", thumbnail);

                                Auction auct_list = new Auction();
                                auct_list.setName(name_j);
                                auct_list.setDescription(description);
                                auct_list.setThumbnail(thumbnail);
                                categoryList.add(auct_list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 + " Auction Found");
                        }
                        else if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 + " Auction Found");
                        }
                        else {
                            auction_found.setText(listView.getAdapter().getCount()-1 + " Auctions Found");
                        }

                        int total = Integer.parseInt(number);
                        if(total <= 9)
                        {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                        else
                        {
                            btnLoadMore.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(code.equals("rest_user_invalid_id"))
                    {
                        Log.e("rest_user_invalid_id", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.VISIBLE);*/
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        Log.e("Tag..not found", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
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
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                                /*linear.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.VISIBLE);*/
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            Log.e("rest_user_invalid_id", code);
                               /* linear.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.VISIBLE);*/
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
                params.put("Authorization","Bearer "+p_token);
                return params;
            }

            public Map < String, String > getHeaders()  {
                HashMap < String, String > headers = new HashMap < String, String > ();
                headers.put("Authorization", "Bearer "+p_token);
                return headers;
            }

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                Log.e("header", responseHeaders.get("X-WP-TotalPages")+"");
                TotalPage = responseHeaders.get("X-WP-TotalPages");
                return super.parseNetworkResponse(response);
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getSearchAuction(final String p_id,final String p_token, final String auction_n) {

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);

        StringRequest strReq = new StringRequest(Method.GET,
                AppConfig.URL_auction_search+auction_n+"&page="+Current_page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
                    Log.e(TAG, "try if: " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if(code.equals("auctions_found"))
                    {
                        categoryList.clear();
                        Log.e("Tag..found", code);
                        scrollview.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        JSONArray auction_arry = jObj.getJSONArray("auctions");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String slug = c.getString("slug");
                                String name_j = c.getString("name");
                                String description = c.getString("description");
                                thumbnail = c.getString("thumbnail");

                                Log.e("details slug.....", slug);
                                Log.e("details name_j.....", name_j);
                                Log.e("details description..", description);
                                Log.e("details thumbnail.....", thumbnail);

                                Auction auct_list = new Auction();
                                auct_list.setName(name_j);
                                auct_list.setDescription(description);
                                auct_list.setThumbnail(thumbnail);
                                categoryList.add(auct_list);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount() +" Auction Found");
                        }
                        else if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount() +" Auction Found");
                        }
                        else
                        {
                            auction_found.setText(listView.getAdapter().getCount() +" Auctions Found");
                        }

                        btnLoadMore.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                    else if(code.equals("rest_user_invalid_id"))
                    {
                        Log.e("rest_user_invalid_id", code);
                        linear.setVisibility(View.INVISIBLE);
                        scrollview.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        scrollview1.setVisibility(View.VISIBLE);
                    }
                    else if(code.equals("auctions_not_found"))
                    {
                        Log.e("Tag..not found", code);
                        linear.setVisibility(View.INVISIBLE);
                        scrollview1.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        scrollview.setVisibility(View.VISIBLE);
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        Log.e("Tag..not found", code);
                        linear.setVisibility(View.INVISIBLE);
                        scrollview1.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        scrollview.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                linear.setVisibility(View.INVISIBLE);
                scrollview.setVisibility(View.INVISIBLE);
                scrollno.setVisibility(View.INVISIBLE);
                scrollview1.setVisibility(View.VISIBLE);
                hideDialog();
                if(error instanceof ServerError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                            linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollno.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            Log.e("rest_user_invalid_id", code);
                            linear.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.INVISIBLE);
                            scrollno.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.VISIBLE);
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

    private void getSearchSuperAuction(final String p_id,final String p_token, final String auction_n) {

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);

        StringRequest strReq = new StringRequest(Method.GET,
                AppConfig.Super_Admin_Auction_list_s+auction_n+"&page="+Current_page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.Super_Admin_Auction_list_s+auction_n+"&page="+Current_page);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.Super_Admin_Auction_list_s+auction_n+"&page="+Current_page);
                    Log.e(TAG, "try if: " + AppConfig.Super_Admin_Auction_list_s+auction_n+"&page="+Current_page);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if(code.equals("auctions_found"))
                    {
                        categoryList.clear();
                        Log.e("Tag..found", code);
                        scrollview.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        JSONArray auction_arry = jObj.getJSONArray("auctions");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String slug = c.getString("slug");
                                String name_j = c.getString("name");
                                String description = c.getString("description");
                                thumbnail = c.getString("thumbnail");

                                Log.e("details slug.....", slug);
                                Log.e("details name_j.....", name_j);
                                Log.e("details description..", description);
                                Log.e("details thumbnail.....", thumbnail);

                                Auction auct_list = new Auction();
                                auct_list.setName(name_j);
                                auct_list.setDescription(description);
                                auct_list.setThumbnail(thumbnail);
                                categoryList.add(auct_list);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auction Found");
                        }
                        else if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auction Found");
                        }
                        else
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auctions Found");
                        }
                         btnLoadMore.setVisibility(View.GONE);

                        adapter.notifyDataSetChanged();
                    }
                    else if(code.equals("rest_user_invalid_id"))
                    {
                        Log.e("rest_user_invalid_id", code);
                        linear.setVisibility(View.INVISIBLE);
                        scrollview.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        scrollview1.setVisibility(View.VISIBLE);
                    }
                    else if(code.equals("auctions_not_found"))
                    {
                        Log.e("Tag..not found", code);
                        linear.setVisibility(View.INVISIBLE);
                        scrollview1.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        scrollview.setVisibility(View.VISIBLE);
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        Log.e("Tag..not found", code);
                        linear.setVisibility(View.INVISIBLE);
                        scrollview1.setVisibility(View.INVISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        scrollview.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.Super_Admin_Auction_list_s+auction_n+"&page="+Current_page);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                linear.setVisibility(View.INVISIBLE);
                scrollview.setVisibility(View.INVISIBLE);
                scrollno.setVisibility(View.INVISIBLE);
                scrollview1.setVisibility(View.VISIBLE);
                hideDialog();
                if(error instanceof ServerError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                            linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollno.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            Log.e("rest_user_invalid_id", code);
                            linear.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.INVISIBLE);
                            scrollno.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.VISIBLE);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(Load_Existing_Auction.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.load__existing__auction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            Intent i = new Intent(Load_Existing_Auction.this, User_Settings.class);
            startActivity(i);
        }
            else if (id == R.id.logout) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(Load_Existing_Auction.this, Login_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }  else if (id == R.id.history) {
            Intent i = new Intent(Load_Existing_Auction.this, Auction_History.class);
            startActivity(i);
        }else if (id == R.id.home) {
            Intent i = new Intent(Load_Existing_Auction.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }else if (id == R.id.user) {
            Intent i = new Intent(Load_Existing_Auction.this, New_lot.class);
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

    @Override
    public void loadMoreListView() {

        if(isNetworkAvailable() == true)
        {
            Log.e("user_role_label", user_role_label);
            if(user_role_label.equals("Super Admin"))
            {
                ///Toast.makeText(Load_Existing_Auction.this, user_role_label, Toast.LENGTH_SHORT).show();
                LoadMoreForSAdmin();
            }
            else {
               // Toast.makeText(Load_Existing_Auction.this, "no", Toast.LENGTH_SHORT).show();
                LoadMoreForAdmin();
            }
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(Load_Existing_Auction.this).create();
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

    private void LoadMoreForAdmin() {
        auction_n = auction_name.getText().toString().trim();
        Current_page +=1;

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_auction_search+auction_n+"&page="+Current_page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
                    Log.e(TAG, "try if: " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
                    String code = jObj.getString("code");
                    String number = jObj.getString("number");
                    Log.e("Tag..", code);
                    if(code.equals("auctions_found"))
                    {
                        Log.e("Tag..found", code);
                        scrollview.setVisibility(View.INVISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        JSONArray auction_arry = jObj.getJSONArray("auctions");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String slug = c.getString("slug");
                                String name_j = c.getString("name");
                                String description = c.getString("description");
                                thumbnail = c.getString("thumbnail");

                                Log.e("details slug.....", slug);
                                Log.e("details name_j.....", name_j);
                                Log.e("details description..", description);
                                Log.e("details thumbnail.....", thumbnail);

                                Auction auct_list = new Auction();
                                auct_list.setName(name_j);
                                auct_list.setDescription(description);
                                auct_list.setThumbnail(thumbnail);
                                categoryList.add(auct_list);

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 + " Auction Found");
                        }
                        else if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 + " Auction Found");
                        }
                        else {
                            auction_found.setText(listView.getAdapter().getCount()-1 + " Auctions Found");
                        }
                        int total = Integer.parseInt(number);
                        if(total <= 9)
                        {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                        else
                        {
                            btnLoadMore.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(code.equals("rest_user_invalid_id"))
                    {
                        Log.e("rest_user_invalid_id", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.VISIBLE);*/
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        Log.e("Tag..not found", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);*/
                    }
                    else if(code.equals("auctions_not_found"))
                    {
                        Log.e("Tag..not found", code);
                        Toast.makeText(Load_Existing_Auction.this, "No More Auction Found", Toast.LENGTH_SHORT).show();
                        btnLoadMore.setVisibility(View.GONE);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_auction_search+auction_n+"&page="+Current_page);
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
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                                /*linear.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.VISIBLE);*/
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            Log.e("rest_user_invalid_id", code);
                               /* linear.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.VISIBLE);*/
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
                params.put("Authorization","Bearer "+p_token);
                return params;
            }

            public Map < String, String > getHeaders()  {
                HashMap < String, String > headers = new HashMap < String, String > ();
                headers.put("Authorization", "Bearer "+p_token);
                return headers;
            }

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                Log.e("header", responseHeaders.get("X-WP-TotalPages")+"");
                TotalPage = responseHeaders.get("X-WP-TotalPages");
                return super.parseNetworkResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void LoadMoreForSAdmin() {
        auction_n = auction_name.getText().toString().trim();
        Current_page +=1;

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.Super_Admin_Auction_list+Current_page);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.Super_Admin_Auction_list+Current_page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.Super_Admin_Auction_list+Current_page);

                try {
                    JSONObject jObj = new JSONObject(response);

                    Log.e(TAG, "try if: " + AppConfig.Super_Admin_Auction_list+Current_page);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if(code.equals("auctions_found"))
                    {
                        Log.e("Tag..found", code);
                        scrollview.setVisibility(View.INVISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        scrollno.setVisibility(View.INVISIBLE);
                        JSONArray auction_arry = jObj.getJSONArray("auctions");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String slug = c.getString("slug");
                                String name_j = c.getString("name");
                                String description = c.getString("description");
                                thumbnail = c.getString("thumbnail");

                                Log.e("details slug.....", slug);
                                Log.e("details name_j.....", name_j);
                                Log.e("details description..", description);
                                Log.e("details thumbnail.....", thumbnail);

                                Auction auct_list = new Auction();
                                auct_list.setName(name_j);
                                auct_list.setDescription(description);
                                auct_list.setThumbnail(thumbnail);
                                categoryList.add(auct_list);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auction Found");
                        }
                        else if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auction Found");
                        }
                        else
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Auctions Found");
                        }
                        int total = Integer.parseInt(TotalPage);
                        if(Current_page == total)
                        {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                        else {
                            btnLoadMore.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();

                    }
                    else if(code.equals("rest_user_invalid_id"))
                    {
                        Log.e("rest_user_invalid_id", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.VISIBLE);*/
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        Log.e("Tag..not found", code);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);*/
                    }
                    else if(code.equals("auctions_not_found"))
                    {
                        Log.e("Tag..not found", code);
                        Toast.makeText(Load_Existing_Auction.this, "No More Auction Found", Toast.LENGTH_SHORT).show();
                        btnLoadMore.setVisibility(View.GONE);
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.Super_Admin_Auction_list+Current_page);
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
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                                /*linear.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.VISIBLE);*/
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            Log.e("rest_user_invalid_id", code);
                               /* linear.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.VISIBLE);*/
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
                params.put("Authorization","Bearer "+p_token);
                return params;
            }

            public Map < String, String > getHeaders()  {
                HashMap < String, String > headers = new HashMap < String, String > ();
                headers.put("Authorization", "Bearer "+p_token);
                return headers;
            }

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                Log.e("header", responseHeaders.get("X-WP-TotalPages")+"");
                TotalPage = responseHeaders.get("X-WP-TotalPages");
                return super.parseNetworkResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
