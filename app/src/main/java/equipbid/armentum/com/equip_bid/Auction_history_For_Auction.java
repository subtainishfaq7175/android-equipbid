package equipbid.armentum.com.equip_bid;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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

public class Auction_history_For_Auction extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = Lot_for_Auction.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Lot_Auction> categoryList = new ArrayList<Lot_Auction>();
    private CustomLotListAdapter adapter;
    private CustomLocalAdapter adapterLocal;
    private String thumbnail;
    private String Auction_name;
    private ListView listview;
    private LinearLayout scrollview;
    private LinearLayout linear, linear1;
    private TextView auction_found, auction_found_;
    private Button add_new_lot;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private String LocalVar ="not_set";
    private NavigationView navigationView;
    private String user_role_label;
    private List<LotDetails> LocalList;
    private ListView listviewLocal;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_history__for__auction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

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

        scrollview      =(LinearLayout)findViewById(R.id.scroll);
        linear          =(LinearLayout)findViewById(R.id.l1);
        linear1         =(LinearLayout)findViewById(R.id.l2);
        listview        =(ListView)findViewById(R.id.listview);
        listviewLocal   =(ListView)findViewById(R.id.listviewLocal);
        auction_found   =(TextView)findViewById(R.id.auction_found);
        auction_found_  =(TextView)findViewById(R.id.auction_found_);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        auction_found.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        auction_found_.setTypeface(typeface);

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
         if(isNetworkAvailable() == true)
        {
            adapter = new CustomLotListAdapter(Auction_history_For_Auction.this, categoryList);
            listview.setAdapter(adapter);
            listview.setDivider(null);

            Bundle bundle = getIntent().getExtras();
            Auction_name = bundle.getString("name");
            getSearchAuctionLot(p_id, p_token, Auction_name);
        }
        else
        {
            Bundle bundle = getIntent().getExtras();
            Auction_name = bundle.getString("name");
            LocalList = new ArrayList<LotDetails>();
            getSearchOfflineAuctionLot(p_id, p_token, Auction_name);
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Lot_Auction tempValues = (Lot_Auction)categoryList.get(position);
                Intent i = new Intent(Auction_history_For_Auction.this, Lot_Details_New.class);
                i.putExtra("name",tempValues.getAuctionno());
                i.putExtra("lotname",tempValues.getLotno());
                i.putExtra("lot_status", tempValues.getLotstatus());
                i.putExtra("LocalVar",LocalVar);
                startActivity(i);
            }
        });
    }

    private void getSearchOfflineAuctionLot(String p_id, String p_token, String auction_name) {
        MyDatabaseHelper helper = new MyDatabaseHelper(this);
        LocalList.clear();
        List<HashMap<String, String>> maplst = new ArrayList<HashMap<String,String>>();
        int count = helper.getProfilesCountAuction(auction_name);
        if(count == 1)
        {
            auction_found_.setText(count +" Lot Found");
        }
        else if(count == 0)
        {
            auction_found_.setText(count +" Lot Found");
        }
        else
        {
            auction_found_.setText(count +" Lots Found");
        }
        LocalList = helper.getAuctionRecord(auction_name);
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
                map.put(MyDatabaseHelper.Col_Flag, String.valueOf(l.getFlag()));
                map.put(MyDatabaseHelper.Col_remote_images, l.getRemote_images());
                maplst.add(map);
            }
            Log.e("LocalList", new Gson().toJson(LocalList));
            categoryList.clear();
            linear.setVisibility(View.GONE);
            linear1.setVisibility(View.VISIBLE);
            adapterLocal = new CustomLocalAdapter(Auction_history_For_Auction.this, LocalList);
            listviewLocal.setAdapter(adapterLocal);
            listviewLocal.setDivider(null);
            listviewLocal.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    LotDetails tempValues = (LotDetails)LocalList.get(position);
                    Intent i = new Intent(Auction_history_For_Auction.this, Lot_Details_New.class);
                    i.putExtra("name",tempValues.getAuction_no());
                    i.putExtra("lotname",tempValues.getLot_no());
                    i.putExtra("lot_status", tempValues.getLot_status());
                    i.putExtra("LocalVar",LocalVar);
                    startActivity(i);
                }
            });
        }
    }

    private void getSearchAuctionLot(final String p_id, final String p_token,final String Auction_name) {

        String tag_string_req = "req_user_setting";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.URL_auction_lot_search+Auction_name);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_auction_lot_search+Auction_name, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_auction_lot_search+Auction_name);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_auction_lot_search+Auction_name);
                    Log.e(TAG, "try if: " + AppConfig.URL_auction_lot_search+Auction_name);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if(code.equals("lots_found"))
                    {
                        linear.setVisibility(View.VISIBLE);
                        linear1.setVisibility(View.GONE);
                        Log.e("Tag..found", code);
                        JSONArray auction_arry = jObj.getJSONArray("lots");

                        for (int i = 0; i < auction_arry.length(); i++) {
                            try {
                                JSONObject c = auction_arry.getJSONObject(i);
                                String lot_no = c.getString("lot_no");
                                String auction_no = c.getString("auction_no");
                                String lot_status = c.getString("lot_status");
                                String item_title = c.getString("item_title");
                                thumbnail = c.getString("thumbnail");

                                Log.e("details lot_no.....", lot_no);
                                Log.e("details lot_status.....", lot_status);
                                Log.e("details item_title..", item_title);

                                Lot_Auction auct_list = new Lot_Auction();
                                auct_list.setLotno(lot_no);
                                auct_list.setAuctionno(auction_no);
                                auct_list.setItemtitle(item_title);
                                auct_list.setLotstatus(lot_status);
                                auct_list.setImagesUrl(thumbnail);

                                categoryList.add(auct_list);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if(listview.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listview.getAdapter().getCount()+ " Lot Found");
                        }
                        else if(listview.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listview.getAdapter().getCount()+ " Lot Found");
                        }
                        else
                        {
                            auction_found.setText(listview.getAdapter().getCount()+ " Lots Found");
                        }

                    }
                    else if(code.equals("rest_user_invalid_id"))
                    {
                        linear.setVisibility(View.GONE);
                        linear1.setVisibility(View.GONE);
                        scrollview.setVisibility(View.VISIBLE);
                        Log.e("rest_user_invalid_id", code);
                    }
                    else if(code.equals("no_auctions_found"))
                    {
                        linear.setVisibility(View.GONE);
                        linear1.setVisibility(View.GONE);
                        scrollview.setVisibility(View.VISIBLE);
                        Log.e("Tag..not found", code);
                    }
                    else if(code.equals("lots_not_found"))
                    {
                        linear.setVisibility(View.GONE);
                        linear1.setVisibility(View.GONE);
                        scrollview.setVisibility(View.VISIBLE);
                        Log.e("Tag..not found", code);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_auction_lot_search+Auction_name);
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
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("no_auctions_found", code);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("rest_user_invalid_id", code);
                        }
                        else if(code.equals("lots_not_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("Tag..not found", code);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else if(error instanceof NetworkError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("no_auctions_found", code);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("rest_user_invalid_id", code);
                        }
                        else if(code.equals("lots_not_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("Tag..not found", code);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else if(error instanceof NoConnectionError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("no_auctions_found", code);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("rest_user_invalid_id", code);
                        }
                        else if(code.equals("lots_not_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("Tag..not found", code);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else if(error instanceof AuthFailureError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("no_auctions_found", code);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("rest_user_invalid_id", code);
                        }
                        else if(code.equals("lots_not_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("Tag..not found", code);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else if(error instanceof ParseError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("no_auctions_found", code);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("rest_user_invalid_id", code);
                        }
                        else if(code.equals("lots_not_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("Tag..not found", code);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else if(error instanceof TimeoutError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");
                        if(code.equals("no_auctions_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("no_auctions_found", code);
                        }
                        else if(code.equals("rest_user_invalid_id"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("rest_user_invalid_id", code);
                        }
                        else if(code.equals("lots_not_found"))
                        {
                            linear.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            scrollview.setVisibility(View.VISIBLE);
                            Log.e("Tag..not found", code);
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
        strReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
        getMenuInflater().inflate(R.menu.lot_for__auction, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_setting) {
            Intent i = new Intent(Auction_history_For_Auction.this, User_Settings.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Auction_history_For_Auction.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.history) {
            Intent i = new Intent(Auction_history_For_Auction.this, Auction_History.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.home) {
            Intent i = new Intent(Auction_history_For_Auction.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Auction_history_For_Auction.this, New_lot.class);
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