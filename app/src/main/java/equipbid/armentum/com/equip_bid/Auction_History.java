package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
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

import equipbid.armentum.com.equip_bid.basicsyncadapter.SyncAdapter;

public class Auction_History extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SyncAdapter.ListSync {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = Auction_History.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Auction> categoryList = new ArrayList<Auction>();
    private GridViewWithHeaderAndFooter listView;
    private CustomListAdapter adapter;
    private String thumbnail;
    private ScrollView scrollview, scrollview1;
    private LinearLayout linear;
    private TextView auction_found;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    int logV = 0;
    private Button btnLoadMore;
    int Current_page = 1;
    private NavigationView navigationView;
    private String user_role_label;
    private Spinner spinner;
    private LinearLayout scroll;
    private String TotalPage;
    private Button add_new_auction;
    private TextView tx4, tx3;
    private GridViewWithHeaderAndFooter listLocal;
    private LinearLayout li2local;
    private TextView auction_foundlocal;
    private Typeface typeface;
    private List<LotDetails> LocalList;
    private LocalListAdapter contactListAdapter;
    private ArrayList<String> categories;
    private SpinnerAdapterRole spinadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction__history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email         = sharedpreferences.getString("email",null);
        p_username      = sharedpreferences.getString("username",null);
        p_token         = sharedpreferences.getString("token",null);
        user_role_label = sharedpreferences.getString("user_role_label",null);
        p_id            = sharedpreferences.getString("ID",null);

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

        linear = (LinearLayout) findViewById(R.id.li2);
        li2local = (LinearLayout) findViewById(R.id.li2local);
        scroll          = (LinearLayout)findViewById(R.id.scroll);
        listView        = (GridViewWithHeaderAndFooter)findViewById(R.id.list);
        listLocal       = (GridViewWithHeaderAndFooter)findViewById(R.id.listLocal);
        spinner         = (Spinner)findViewById(R.id.spinner);
        add_new_auction = (Button) findViewById(R.id.add_new_auction);
        auction_found      =(TextView)findViewById(R.id.auction_found);
        auction_foundlocal =(TextView)findViewById(R.id.auction_foundlocal);

        tx4 = (TextView)findViewById(R.id.tx4);
        tx3 = (TextView)findViewById(R.id.tx3);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        auction_found.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        auction_foundlocal.setTypeface(typeface);

        categories = new ArrayList<String>();
        categories.add("Online Auctions");
        categories.add("Offline Auctions");

        btnLoadMore = new Button(Auction_History.this);
        btnLoadMore.setText("Load More");
        btnLoadMore.setBackgroundResource(R.drawable.btn_green);
        btnLoadMore.setTextColor(Color.WHITE);
        btnLoadMore.setTextSize(19);
        btnLoadMore.setAllCaps(false);

        btnLoadMore.setTypeface(typeface);
        add_new_auction.setTypeface(typeface);
        tx4.setTypeface(typeface);
        tx3.setTypeface(typeface);

        LinearLayout.MarginLayoutParams params = new LinearLayout.MarginLayoutParams(680, 92);
        params.setMargins(20, 20, 20, 20);
        btnLoadMore.setLayoutParams(params);
        adapter = new CustomListAdapter(Auction_History.this, categoryList,user_role_label);

        listView.addFooterView(btnLoadMore);
        listView.setAdapter(adapter);

        btnLoadMore.setVisibility(View.GONE);
        pDialog     = new ProgressDialog(Auction_History.this);
        pDialog.setCancelable(false);

        LocalList = new ArrayList<LotDetails>();
        contactListAdapter = new LocalListAdapter(Auction_History.this, LocalList, user_role_label);
        listLocal.setAdapter(contactListAdapter);
        Log.e("Counts...",""+listView.getAdapter().getCount());

        //LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                loadMoreListView();
            }
        });

        add_new_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Auction_History.this, Create_New_Auction_Lot.class);
                i.putExtra("name","");
                startActivity(i);
            }
        });

        spinadapter = new SpinnerAdapterRole(Auction_History.this, categories);
        spinner.setAdapter(spinadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = categories.get(position);
                if(position == 0) {
                    isNetworkAvailable();
                    if(isNetworkAvailable() == true)
                    {
                        Log.e("Current_page", Current_page+"");
                        Current_page = 1;
                        categoryList.clear();
                        onLineData();
                        scroll.setVisibility(View.GONE);
                        li2local.setVisibility(View.GONE);
                        linear.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Auction_History.this);
                        builder.setTitle("No Internet Connection");
                        builder.setMessage("Click OK to show Offline Auctions")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        spinner.setSelection(1);
                                        scroll.setVisibility(View.GONE);
                                        li2local.setVisibility(View.VISIBLE);
                                        linear.setVisibility(View.GONE);
                                        //  Toast.makeText(Auction_History.this, "OffLine", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                else if(position == 1) {

                    fillListView();
                    spinner.setSelection(1);
                    scroll.setVisibility(View.GONE);
                    li2local.setVisibility(View.VISIBLE);
                    linear.setVisibility(View.GONE);
                    // Toast.makeText(Auction_History.this, "OffLine", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        isNetworkAvailable();
        if(isNetworkAvailable() == true)
        {}
        else
        {
            spinner.setSelection(1);
            li2local.setVisibility(View.VISIBLE);
            scroll.setVisibility(View.GONE);
            linear.setVisibility(View.GONE);
           // Toast.makeText(Auction_History.this, "OffLine", Toast.LENGTH_SHORT).show();
        }

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
                Intent i = new Intent(Auction_History.this, Lot_for_Auction.class);
                i.putExtra("name",tempValues.getName());
                startActivity(i);
            }
        });
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Receiver", "Localdata");
            if  ("Localdata".equals(intent.getAction()))
            {
                Log.e("Receiver", "Localdata");
            }
        }
    };

    public void broadcastIntent(){
        Intent intent = new Intent();
        intent.setAction("Localdata");
        sendBroadcast(intent);
        Log.e("broadcastIntent", "sdfs");
    }

    private void fillListView() {
        MyDatabaseHelper helper = new MyDatabaseHelper(this);
        LocalList.clear();
        long count = helper.getProfilesCount();
        Log.e("Count", count+"");
        if(count == 1)
        {
            auction_foundlocal.setText(count +" Auction Found");
        }
        else if(count == 0)
        {
            auction_foundlocal.setText(count +" Auction Found");
        }
        else
        {
            auction_foundlocal.setText(count +" Auctions Found");
        }

        List<HashMap<String, String>> maplst = new ArrayList<HashMap<String,String>>();
        LocalList = helper.getAllRecord();
        LotDetails l = new LotDetails();
        if(LocalList.size()>0){
            for(int i=0;i<LocalList.size();i++){
                l = LocalList.get(i);
                HashMap< String, String> map = new HashMap<String, String>();
                map.put(MyDatabaseHelper.Col_lot_number, String.valueOf(l.getLot_no()));
                map.put(MyDatabaseHelper.Col_auction_number, l.getAuction_no());
                map.put(MyDatabaseHelper.Col_thumbnail, String.valueOf(l.getThumbnail()));
                map.put(MyDatabaseHelper.Col_upc, l.getUpc());
                map.put(MyDatabaseHelper.Col_item_title, l.getItem_title());
                map.put(MyDatabaseHelper.Col_item_class, l.getItem_class());
                map.put(MyDatabaseHelper.Col_seller_id, l.getSeller_id());
                map.put(MyDatabaseHelper.Col_description, l.getDescription());
                map.put(MyDatabaseHelper.Col_Brand, l.getBrand());
                map.put(MyDatabaseHelper.Col_model, l.getModel());
                map.put(MyDatabaseHelper.Col_color, l.getColor());
                map.put(MyDatabaseHelper.Col_dimension, l.getDimension());
                map.put(MyDatabaseHelper.Col_condition, l.getCondition());
                map.put(MyDatabaseHelper.Col_location, l.getLocation());
                map.put(MyDatabaseHelper.Col_add_detalils, l.getAdd_detalils());
                map.put(MyDatabaseHelper.Col_lot_status, l.getLot_status());
                map.put(MyDatabaseHelper.Col_additional_details_checklist, l.getAdditional_details_checklist());
                map.put(MyDatabaseHelper.Col_category, l.getCategory());
                map.put(MyDatabaseHelper.Col_textable, l.getTextable());
                map.put(MyDatabaseHelper.Col_start_price, l.getStart_price());
                map.put(MyDatabaseHelper.Col_reverse_price, l.getReverse_price());
                map.put(MyDatabaseHelper.Col_msrp, l.getMsrp());
                map.put(MyDatabaseHelper.Col_quantity, l.getQuantity());
                map.put(MyDatabaseHelper.Col_images, l.getImages());
                map.put(MyDatabaseHelper.Col_remote_images, l.getRemote_images());
                map.put(MyDatabaseHelper.Col_Token, l.getToken());
                map.put(MyDatabaseHelper.Col_Flag, String.valueOf(l.getFlag()));
                maplst.add(map);
            }

            Log.e("LocalList", new Gson().toJson(LocalList)+"");
             contactListAdapter.updateData(LocalList);
            listLocal.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    LotDetails tempValues = (LotDetails)LocalList.get(position);
                    Intent i = new Intent(Auction_History.this, Lot_for_Auction.class);
                    i.putExtra("name",tempValues.getAuction_no());
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void refreshLocal() {
        Log.e("refreshLocal", "dxfdff");
        //contactListAdapter.notifyDataSetChanged();
        //LocalList = new ArrayList<LotDetails>();
        //contactListAdapter = new LocalListAdapter(Auction_History.this, LocalList, user_role_label);
       // listLocal.setAdapter(contactListAdapter);
       // contactListAdapter.updateData(LocalList);
        /*} catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, "catch : " + e);
        }*/
    }

    private void onLineData() {
        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        if(logV == 0)
        {
            Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.URL_auction_search_new+Current_page);
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_auction_search_new+Current_page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String code = jObj.getString("code");
                    String number = jObj.getString("number");
                    if(logV == 1)
                    {
                        Log.e(TAG, "try if: " + AppConfig.URL_auction_search_new+Current_page);
                        Log.e("Tag..", code);
                    }

                    if(code.equals("auctions_found"))
                    {
                        if(logV == 1)
                        {
                            Log.e("Tag..found", code);
                        }
                        JSONArray auction_arry = jObj.getJSONArray("auctions");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String slug = c.getString("slug");
                                String name_j = c.getString("name");
                                String description = c.getString("description");
                                thumbnail = c.getString("thumbnail");

                                if(logV == 1)
                                {
                                    Log.e("details slug.....", slug);
                                    Log.e("details name_j.....", name_j);
                                    Log.e("details description..", description);
                                    Log.e("details thumbnail.....", thumbnail);
                                }
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

                        if(Integer.parseInt(number) == 1)
                        {
                            auction_found.setVisibility(View.VISIBLE);
                            auction_found.setText(number +" Auction Found");
                        }
                        else if(Integer.parseInt(number) == 0)
                        {
                            auction_found.setVisibility(View.VISIBLE);
                            auction_found.setText(number +" Auction Found");
                        }
                        else
                        {
                            auction_found.setVisibility(View.VISIBLE);
                            auction_found.setText(number +" Auctions Found");
                        }

                        Log.e("Items", listView.getAdapter().getCount()+"");

                        if(listView.getAdapter().getCount()-1 <=9)
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
                        if(logV == 1)
                        {
                            Log.e("rest_user_invalid_id", code);
                        }
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        scroll.setVisibility(View.VISIBLE);
                        li2local.setVisibility(View.GONE);
                        linear.setVisibility(View.GONE);
                        if(logV == 1)
                        {
                            Log.e("Tag..not found", code);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_auction_search_new+Current_page);
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

    private void loadMoreListView() {

        if(isNetworkAvailable() == true)
        {
            Current_page +=1;
            Log.e("Current_page", Current_page+"");
            String tag_string_req = "req_user_setting";

            pDialog.setMessage("Please Wait ...");
            showDialog();

            if(logV == 0)
            {
                Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.URL_auction_search_new+Current_page);
            }

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.URL_auction_search_new+Current_page, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "user details Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String code = jObj.getString("code");
                        if(logV == 0)
                        {
                            Log.e(TAG, "try if: " + AppConfig.URL_auction_search_new+Current_page);
                            Log.e("Tag..", code);
                        }

                        if(code.equals("auctions_found"))
                        {
                            if(logV == 1)
                            {
                                Log.e("Tag..found", code);
                            }
                            JSONArray auction_arry = jObj.getJSONArray("auctions");

                            for (int i = 0; i < auction_arry.length(); i++) {
                                try {
                                    JSONObject c = auction_arry.getJSONObject(i);
                                    String slug = c.getString("slug");
                                    String name_j = c.getString("name");
                                    String description = c.getString("description");
                                    thumbnail = c.getString("thumbnail");

                                    if(logV == 1)
                                    {
                                        Log.e("details slug.....", slug);
                                        Log.e("details name_j.....", name_j);
                                        Log.e("details description..", description);
                                        Log.e("details thumbnail.....", thumbnail);
                                    }
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
                            if(listView.getAdapter().getCount() == 1)
                            {
                                auction_found.setVisibility(View.VISIBLE);
                                auction_found.setText(listView.getAdapter().getCount()-3 +" Auction Found");
                            }
                            else if(listView.getAdapter().getCount() == 0)
                            {
                                auction_found.setVisibility(View.VISIBLE);
                                auction_found.setText(listView.getAdapter().getCount()-3 +" Auction Found");
                            }
                            else
                            {
                                auction_found.setVisibility(View.VISIBLE);
                                auction_found.setText(listView.getAdapter().getCount()-3 +" Auctions Found");
                            }

                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            if(logV == 1)
                            {
                                Log.e("rest_user_invalid_id", code);
                            }
                        }
                        else if(code.equals("no_auctions_found"))
                        {
                            scroll.setVisibility(View.VISIBLE);
                            li2local.setVisibility(View.GONE);
                            linear.setVisibility(View.GONE);

                            if(logV == 1)
                            {
                                Log.e("Tag..not found", code);
                            } 
                        }
                        else if(code.equals("auctions_not_found"))
                        {
                            Log.e("Tag..not found", code);
                            Toast.makeText(Auction_History.this, "No More Auction Found", Toast.LENGTH_SHORT).show();
                            btnLoadMore.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "catch : " + AppConfig.URL_auction_search_new+Current_page);
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
                                scroll.setVisibility(View.VISIBLE);
                                li2local.setVisibility(View.GONE);
                                linear.setVisibility(View.GONE);
                            }
                            else if(code.equals("rest_user_invalid_id"))
                            {
                                Log.e("rest_user_invalid_id", code);
                               /* linear.setVisibility(View.INVISIBLE);
                                scrollview.setVisibility(View.INVISIBLE);
                                scrollview1.setVisibility(View.VISIBLE);*/
                            }
                            else if(code.equals("auctions_not_found"))
                            {
                                Log.e("Tag..not found", code);
                                Toast.makeText(Auction_History.this, "No More Auction Found", Toast.LENGTH_SHORT).show();
                            /*linear.setVisibility(View.INVISIBLE);
                            scrollview1.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);*/
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
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(Auction_History.this).create();
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
            super.onBackPressed();
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
            Intent i = new Intent(Auction_History.this, User_Settings.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Auction_History.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.history) {
            Intent i = new Intent(Auction_History.this, Auction_History.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.home) {
            Intent i = new Intent(Auction_History.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Auction_History.this, New_lot.class);
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

    private class SpinnerAdapterRole extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<String> categories;
        private int test = 0;
        private TextView spinvalue;

        public SpinnerAdapterRole(Activity activity, ArrayList<String> categories) {
            this.activity = activity;
            this.categories = categories;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.spin_role, parent, false);
            }

            spinvalue = (TextView) view.findViewById(R.id.spinvalue);
            spinvalue.setTypeface(typeface);
            spinvalue.setText(categories.get(position));
            return view;
        }
    }
}
