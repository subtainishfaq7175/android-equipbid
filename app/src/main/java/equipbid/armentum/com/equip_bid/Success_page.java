package equipbid.armentum.com.equip_bid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TextView;

public class Success_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button view_auction_history, upload_more;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private String Auction_name, lot_no, upc, lot_status;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private TextView thanks, textView2;
    private String user_role_label;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TextView mTitle;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle              = (TextView) toolbar.findViewById(R.id.toolbar_title);
        sharedpreferences   = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email             = sharedpreferences.getString("email",null);
        p_username          = sharedpreferences.getString("username",null);
        p_token             = sharedpreferences.getString("token",null);
        user_role_label     = sharedpreferences.getString("user_role_label",null);
        p_id                = sharedpreferences.getString("ID",null);

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

        Bundle bundle   = getIntent().getExtras();
        Auction_name    = bundle.getString("name");
        lot_no          = bundle.getString("lotname");
        upc             = bundle.getString("upc");
        lot_status      = bundle.getString("lot_status");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        view_auction_history    =(Button)findViewById(R.id.view_auction_history);
        upload_more             =(Button)findViewById(R.id.upload_more);
        thanks                  =(TextView)findViewById(R.id.thanks);
        textView2               =(TextView)findViewById(R.id.textView2);
        typeface                = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        thanks.setTypeface(typeface);
        textView2.setTypeface(typeface);
        view_auction_history.setTypeface(typeface);
        upload_more.setTypeface(typeface);
        mTitle.setTypeface(typeface);

        upload_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(Success_page.this, Create_New_Auction_Lot.class);
            i.putExtra("name",Auction_name);
            startActivity(i);
            }
        });

        view_auction_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(Success_page.this, Auction_history_For_Auction.class);
            i.putExtra("name",Auction_name);
            startActivity(i);
            }
        });
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
        getMenuInflater().inflate(R.menu.success_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.upload) {
            Intent i = new Intent(Success_page.this, Auction_Screen.class);
            startActivity(i);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_setting) {
            Intent i = new Intent(Success_page.this, User_Settings.class);
            startActivity(i);
        }
        else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Success_page.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.history) {
            Intent i = new Intent(Success_page.this, Auction_History.class);
            startActivity(i);
        }
        else if (id == R.id.home) {
            Intent i = new Intent(Success_page.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Success_page.this, New_lot.class);
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
