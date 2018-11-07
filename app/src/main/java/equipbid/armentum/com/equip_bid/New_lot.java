package equipbid.armentum.com.equip_bid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class New_lot extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , UserAdapter.IUserlist{

    private Button add_new_lot, add_new_lot_b;
    private TextView title;
    private EditText lot;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = New_lot.class.getSimpleName();
    private SharedPreferences sharedpreferences;
    private String p_username, p_token, p_email, p_id;
    private ProgressDialog pDialog;
    private String lot_name;
    private String Auction_name;
    private String lot_status;
    private String response_code;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private TextView sample;
    MediaController mediaControls;
    private ListView listView;
    private List<User> userList = new ArrayList<User>();
    private UserAdapter adapter;
    private JSONArray User_Detail_Arr;
    private JSONObject add_obj;
    int Current_page = 1;
    private TextView auction_found;
    private String user_role_label;
    private NavigationView navigationView;
    private Button btnLoadMore;
    private String TotalPage;
    private User tempValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lot);
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

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email     = sharedpreferences.getString("email",null);
        p_username  = sharedpreferences.getString("username",null);
        p_token     = sharedpreferences.getString("token",null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id        = sharedpreferences.getString("ID",null);

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

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

        auction_found   = (TextView)findViewById(R.id.auction_found);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        mTitle.setTypeface(typeface);
        auction_found.setTypeface(typeface);
        pDialog     = new ProgressDialog(this);
        pDialog.setCancelable(false);

        listView        = (ListView) findViewById(R.id.list);

        btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");
        btnLoadMore.setBackgroundResource(R.drawable.btn_green);
        btnLoadMore.setTextColor(Color.WHITE);
        btnLoadMore.setTextSize(19);
        btnLoadMore.setAllCaps(false);
        btnLoadMore.setTypeface(typeface);
        LinearLayout.MarginLayoutParams params = new LinearLayout.MarginLayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 37, 0, 37);
        btnLoadMore.setLayoutParams(params);

        adapter = new UserAdapter(New_lot.this, userList, New_lot.this);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                tempValues = (User) userList.get(position);
                Intent i = new Intent(New_lot.this, User_Single.class);
                i.putExtra("id",tempValues.getId());
                startActivity(i);
                finish();
               // Toast.makeText(New_lot.this, tempValues.getId()+".."+ tempValues.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        if(isNetworkAvailable() == true)
        {
            String tag_string_req = "req_user_setting";

            pDialog.setMessage("Please Wait ...");
            showDialog();

            Log.e(TAG, " tag_string_req : " + tag_string_req );
            Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.User_Details+Current_page);

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.User_Details+Current_page, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "user details Response: " + response.toString());
                    hideDialog();
                    Log.e(TAG, "sdfdsgfgfg : " + AppConfig.User_Details+Current_page);
                    try {

                        User_Detail_Arr = new JSONArray(response);
                        Log.e(TAG, "try : " + AppConfig.User_Details+Current_page);
                        for (int i = 0; i < User_Detail_Arr.length(); i++) {
                            try {
                                JSONObject c = User_Detail_Arr.getJSONObject(i);

                                String id = c.getString("id");
                                String name = c.getString("name");
                                String email = c.getString("user_email");
                                JSONArray jArray1 = c.getJSONArray("user_role");
                                String role = c.getString("user_role_label");
                                Log.e("details name_j.....", name);
                                Log.e("details description..", email);

                                User auct_list = new User();
                                auct_list.setName(name);
                                auct_list.setUser_email(email);
                                auct_list.setRole(role);
                                auct_list.setId(id);
                                userList.add(auct_list);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" User Found");
                        }
                        else if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" User Found");
                        }
                        else
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Users Found");
                        }

                        Log.e("Items", listView.getAdapter().getCount()+"");
                        if(listView.getAdapter().getCount() <= 9)
                        {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                        else
                        {
                            btnLoadMore.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "catch : " + AppConfig.User_Details+Current_page);
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

               /* protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String totalPages=responseHeaders.get("X-WP-TotalPages");
                    return super.parseNetworkResponse(response);
                }*/

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
            AlertDialog alertDialog = new AlertDialog.Builder(New_lot.this).create();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(New_lot.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_lot, menu);
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
            Intent i = new Intent(New_lot.this, User_Settings.class);
            startActivity(i);

        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(New_lot.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }  else if (id == R.id.history) {
            Intent i = new Intent(New_lot.this, Auction_History.class);
            startActivity(i);
        }

        else if (id == R.id.home) {
            Intent i = new Intent(New_lot.this, Auction_Screen_Main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(New_lot.this, New_lot.class);
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

    public void loadMoreListView() {

        if(isNetworkAvailable() == true)
        {
            Current_page +=1;

            String tag_string_req = "req_user_setting";

            pDialog.setMessage("Please Wait ...");
            showDialog();

            Log.e(TAG, " tag_string_req : " + tag_string_req );
            Log.e(TAG, " AppConfig.URL_Auction_search : " + AppConfig.User_Details+Current_page);

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.User_Details+Current_page, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "user details Response: " + response.toString());
                    hideDialog();
                    Log.e(TAG, "sdfdsgfgfg : " + AppConfig.User_Details+Current_page);
                    try {

                        User_Detail_Arr = new JSONArray(response);
                        Log.e(TAG, "try : " + AppConfig.URL_Add_Details);
                        for (int i = 0; i < User_Detail_Arr.length(); i++) {
                            try {
                                JSONObject c = User_Detail_Arr.getJSONObject(i);

                                String name = c.getString("name");
                                String id = c.getString("id");
                                String email = c.getString("user_email");
                                JSONArray jArray1 = c.getJSONArray("user_role");
                                String role = c.getString("user_role_label");
                                Log.e("details name_j.....", name);
                                Log.e("details description..", email);

                                User auct_list = new User();
                                auct_list.setName(name);
                                auct_list.setUser_email(email);
                                auct_list.setRole(role);
                                auct_list.setId(id);
                                userList.add(auct_list);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if(listView.getAdapter().getCount() == 1)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" User Found");
                        }
                        else if(listView.getAdapter().getCount() == 0)
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" User Found");
                        }
                        else
                        {
                            auction_found.setText(listView.getAdapter().getCount()-1 +" Users Found");
                        }
                        int total = Integer.parseInt(TotalPage);
                        if(Current_page == total)
                        {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "catch : " + AppConfig.User_Details+Current_page);
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
            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(New_lot.this).create();
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
}
