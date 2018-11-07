package equipbid.armentum.com.equip_bid;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import equipbid.armentum.com.equip_bid.basicsyncadapter.SyncUtils;

public class Auction_Screen_Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button create_new_auction, load_existing ,existing_lot;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = Add_New_Auction.class.getSimpleName();
    private Button menu_img;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ProgressDialog pDialog;
    private String auction_name;
    private AppBarLayout mAppBarLayout;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private NavigationView navigationView;
    private String user_role_label;
    private JSONArray Add_Detail_Arr;
    private JSONObject add_obj;
    private ArrayList<String> AddDetail_List;
    private ArrayList<Add_Detail> AddDetailList = new ArrayList<Add_Detail>();
    private JSONObject Jobj1;
    private JSONObject ChildObj;
    private String name_c, name_d;
    private ArrayList<String> spinList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_screen_main);

        SyncUtils.CreateSyncAccount(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.mAppBarLayout);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email     = sharedpreferences.getString("email",null);
        p_username  = sharedpreferences.getString("username",null);
        p_token     = sharedpreferences.getString("token",null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id        = sharedpreferences.getString("ID",null);

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+user_role_label);

        ConnectionManager   = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo         =  ConnectionManager.getActiveNetworkInfo();

        pDialog     = new ProgressDialog(this);
        pDialog.setCancelable(false);

        drawer      = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle      = new ActionBarDrawerToggle(
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
        
        create_new_auction  =(Button)findViewById(R.id.create_new_auction);
        load_existing       =(Button)findViewById(R.id.load_existing);
        existing_lot        =(Button)findViewById(R.id.existing_lot);

        AddDetail_List          = new ArrayList<String>();
        spinList                = new ArrayList<String>();

        getAddDetails(p_token);
        getcategorySpinFirst(p_token);

        create_new_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isNetworkAvailable();
                if(isNetworkAvailable() == true) {
                    Intent i = new Intent(Auction_Screen_Main.this, Create_New_Auction_Lot.class);
                    i.putExtra("name","");
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(Auction_Screen_Main.this, Create_New_Auction_Lot.class);
                    i.putExtra("name","");
                    startActivity(i);
                    finish();
                }
            }
        });

        load_existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkAvailable();
                if(isNetworkAvailable() == true) {
                    Intent i = new Intent(Auction_Screen_Main.this, Load_Existing_Auction.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(Auction_Screen_Main.this, Load_Existing_Auction.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        existing_lot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkAvailable();
                if(isNetworkAvailable() == true) {
                    Intent i = new Intent(Auction_Screen_Main.this, Loat_listing.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(Auction_Screen_Main.this, Loat_listing.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            String[] permissionsList = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this,permissionsList,1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            String[] permissionsList = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissionsList, 2);
        }

    }

    private void getAddDetails(final String p_token) {

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_cat : " + AppConfig.URL_Add_Details);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Add_Details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Add_Detail_Arr = new JSONArray(response);
                    Log.e(TAG, "try : " + AppConfig.URL_Add_Details);
                    for (int i = 0; i < Add_Detail_Arr.length(); i++) {
                        try {
                            add_obj = Add_Detail_Arr.getJSONObject(i);
                            String slug = add_obj.getString("slug");
                            String name_j = add_obj.getString("name");
                            String id = add_obj.getString("id");

                            AddDetail_List.add(add_obj.getString("name"));
                            Add_Detail Add_Detail_list = new Add_Detail();
                            Add_Detail_list.setName(name_j);
                            Add_Detail_list.setId(id);
                            AddDetailList.add(Add_Detail_list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("detailsList ", AddDetail_List+"");
                    Log.e("detailsList", AddDetailList.size()+"");
                    MyDatabaseHelper helper = new MyDatabaseHelper(Auction_Screen_Main.this);
                    Add_Detail a = new Add_Detail();
                    String ff = null;
                    int count = helper.getProfilesCountAdd();
                    Log.e("counttttt", count+"");
                    if(count == 0)
                    {
                        for(int i = 0; i<AddDetail_List.size(); i++)
                        {
                           // Toast.makeText(Auction_Screen_Main.this, "First", Toast.LENGTH_SHORT).show();
                            ff = AddDetail_List.get(i);
                            Log.e("entry", AddDetail_List.get(i));
                            Log.e("ss", a+"");
                            a.setName(AddDetail_List.get(i));
                            helper.insertAddD(a);
                        }
                        Log.e("countt", count+"");
                    }
                    else if(count != 0)
                    {
                        if(AddDetail_List.size() != count )
                        {
                            helper.deleteTitleAdd();
                            //Toast.makeText(Auction_Screen_Main.this, "delete", Toast.LENGTH_SHORT).show();
                            for(int i = 0; i<AddDetail_List.size(); i++)
                            {
                                //Toast.makeText(Auction_Screen_Main.this, "First", Toast.LENGTH_SHORT).show();
                                ff = AddDetail_List.get(i);
                                Log.e("entry", AddDetail_List.get(i));
                                Log.e("ss", a+"");
                                a.setName(AddDetail_List.get(i));
                                helper.insertAddD(a);
                            }
                            Log.e("countt", count+"");
                        }
                        else
                        {
                           // Toast.makeText(Auction_Screen_Main.this, count+"..."+AddDetail_List.size(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_Add_Details);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
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
        strReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getcategorySpinFirst(final String p_token) {

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_cat : " + AppConfig.URL_Category);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Category, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if (code.equals("terms_found")) {

                        JSONArray item_arry = jObj.getJSONArray("terms");
                        for (int i = 0; i < item_arry.length(); i++) {
                            try {

                                Jobj1 = item_arry.getJSONObject(i);
                                String name = Jobj1.getString("name");
                                Log.e("name", name);

                                spinList.add(name);
                                JSONArray Child_array = Jobj1.getJSONArray("childs");
                                for (int k = 0; k < Child_array.length(); k++) {
                                    ChildObj = Child_array.getJSONObject(k);
                                    name_c = ChildObj.getString("name");
                                    name_d = " - "+ name_c;
                                    spinList.add(name_d);
                                }
                                Log.e("childs", name_c);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.e("modified_array", spinList+"");
                        String ss;
                        MyDatabaseHelper helper = new MyDatabaseHelper(Auction_Screen_Main.this);

                        int count = helper.getProfilesCountSpinn();
                        Log.e("counttttt", count+"");
                        if(count == 0)
                        {
                            for(int i = 0; i<spinList.size(); i++)
                            {
                                ss = spinList.get(i);
                                Log.e("spinList..", spinList.get(i));
                                helper.insertCat(spinList.get(i));
                            }
                            Log.e("countt", count+"");
                        }
                        else if(count != 0)
                        {
                            if(spinList.size() != count )
                            {
                                helper.deleteTitleCat();
                                //Toast.makeText(Auction_Screen_Main.this, "delete", Toast.LENGTH_SHORT).show();
                                for(int i = 0; i<spinList.size(); i++)
                                {
                                    ss = spinList.get(i);
                                    Log.e("spinList..", spinList.get(i));
                                    helper.insertCat(spinList.get(i));
                                }
                                Log.e("countt", count+"");
                            }
                            else
                            {
                                //Toast.makeText(Auction_Screen_Main.this, count+"..."+spinList.size(), Toast.LENGTH_SHORT).show();
                            }
                        }




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_Category);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
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
        strReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
//        if (requestCode == 2) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Now user should be able to use camera
//            }
//            else {
//                // Your app will not have this permission. Turn off all functions
//                // that require this permission or it will force close like your
//                // original question
//            }
//        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(Auction_Screen_Main.this);
            builder.setTitle("Exit Application");
            builder.setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            Auction_Screen_Main.this.finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auction__screen, menu);
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
            Intent i = new Intent(Auction_Screen_Main.this, User_Settings.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Auction_Screen_Main.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.history) {
            Intent i = new Intent(Auction_Screen_Main.this, Auction_History.class);
            startActivity(i);
        }
        else if (id == R.id.home) {
            Intent i = new Intent(Auction_Screen_Main.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Auction_Screen_Main.this, New_lot.class);
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
