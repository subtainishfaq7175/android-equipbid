package equipbid.armentum.com.equip_bid;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import net.sourceforge.zbar.ImageScanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class New_Lot_Entry extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ZXingScannerView.ResultHandler {

    private android.support.v7.widget.SwitchCompat mySwitch;
    private ImageView scan_main, scanner, camera, manual_img, duplicate_img;
    private TextView entry_title, manual_txt, duplicate_txt, note;
    private LinearLayout linear_manual,linear_duplicate ;
    private TextView result1;
    private Camera mCamera;
    private Handler autoFocusHandler;
    private Button scanButton;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private EditText scantextView;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = New_Lot_Entry.class.getSimpleName();
    private String ScanResult;
    private ZXingScannerView mScannerView;
    private String ScanResultFormat;
    private ProgressDialog pDialog;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private String LocalVar ="not_set";
    private String Auction_name, Lot_name, lot_status;
    private ViewGroup contentFrame;
    private static New_Lot_Entry mInst = null;
    private NavigationView navigationView;
    private String user_role_label;
    private TextView proceed;
    private LinearLayout scanview;
    private TextView test;
    private LinearLayout ssd;
    private boolean mFlash;
    private boolean mAutoFocus;
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";

    public static New_Lot_Entry instance() {
        Log.e("mInst",mInst+"");
        return mInst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lot_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false);
            mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, true);

        } else {
            mFlash = false;
            mAutoFocus = true;
        }

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mInst = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email          = sharedpreferences.getString("email",null);
        p_username       = sharedpreferences.getString("username",null);
        p_token          = sharedpreferences.getString("token",null);
        user_role_label  = sharedpreferences.getString("user_role_label",null);
        p_id             = sharedpreferences.getString("ID",null);

        Log.e("Details",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+p_id);

        Bundle bundle = getIntent().getExtras();
        Auction_name  = bundle.getString("name");
        Lot_name      = bundle.getString("lotname");
        lot_status    = bundle.getString("lot_status");

        Log.e("Details",Auction_name +"..."+Lot_name+"..."+lot_status);

        scan_main         = (ImageView) findViewById(R.id.scan_main);
        scanner           = (ImageView) findViewById(R.id.scanner);
        camera            = (ImageView) findViewById(R.id.camera);
        manual_img        = (ImageView) findViewById(R.id.manual_img);
        duplicate_img     = (ImageView) findViewById(R.id.duplicate_img);
        entry_title       = (TextView) findViewById(R.id.entry_title);
        manual_txt        = (TextView)findViewById(R.id.manual_txt);
        note              = (TextView)findViewById(R.id.note);
        test              = (TextView)findViewById(R.id.test);
        duplicate_txt     = (TextView)findViewById(R.id.duplicate_txt);
        linear_manual     = (LinearLayout)findViewById(R.id.linear_manual);
        linear_duplicate  = (LinearLayout)findViewById(R.id.linear_duplicate);
        scanview          = (LinearLayout)findViewById(R.id.scanview);
        scantextView      = (EditText) findViewById(R.id.upcno);
        proceed           = (Button) findViewById(R.id.proceed);
        contentFrame      = (ViewGroup) findViewById(R.id.cameraPreview);
        mScannerView      = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);

        pDialog     = new ProgressDialog(this);
        pDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");

        manual_txt.setTypeface(typeface);
        duplicate_txt.setTypeface(typeface);
        entry_title.setTypeface(typeface);
        note.setTypeface(typeface);
        scantextView.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        proceed.setTypeface(typeface);
        test.setTypeface(typeface);
        scantextView.getText().toString();
        Log.e("dfdsfd", scantextView.getText().toString());

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(New_Lot_Entry.this, "UPC Number is " +scantextView.getText().toString(), Toast.LENGTH_SHORT).show();
                String txt = scantextView.getText().toString();

                setViewText(txt);
            }
        });

        mySwitch = (android.support.v7.widget.SwitchCompat)findViewById(R.id.switch1);
        mySwitch.setChecked(true);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    entry_title.setText("Barcode Scanner");
                    camera.setImageResource(R.mipmap.camera);
                    scanner.setImageResource(R.mipmap.scanner_green);
                    scanview.setVisibility(View.VISIBLE);
                    scan_main.setVisibility(View.VISIBLE);
                    contentFrame.setVisibility(View.GONE);

                } else {
                    entry_title.setText("Camera");
                    camera.setImageResource(R.mipmap.camera_green);
                    scanner.setImageResource(R.mipmap.scanner);
                    contentFrame.setVisibility(View.VISIBLE);
                    scanview.setVisibility(View.INVISIBLE);
                    scan_main.setVisibility(View.GONE);
                }
            }
        });

        linear_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(New_Lot_Entry.this, Lot_Details_New.class);
                    i.putExtra("upc","");
                    i.putExtra("name",Auction_name);
                    i.putExtra("lotname",Lot_name);
                    i.putExtra("lot_status",lot_status);
                    i.putExtra("LocalVar",LocalVar);
                    startActivity(i);
            }
        });

        manual_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(New_Lot_Entry.this, Lot_Details_New.class);
                i.putExtra("upc","");
                i.putExtra("name",Auction_name);
                i.putExtra("lotname",Lot_name);
                i.putExtra("lot_status",lot_status);
                i.putExtra("LocalVar",LocalVar);
                startActivity(i);
            }
        });

        manual_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(New_Lot_Entry.this, Lot_Details_New.class);
                i.putExtra("upc","");
                i.putExtra("name",Auction_name);
                i.putExtra("lotname",Lot_name);
                i.putExtra("lot_status",lot_status);
                i.putExtra("LocalVar",LocalVar);
                startActivity(i);
            }
        });

        linear_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(New_Lot_Entry.this, Duplicate_existing_Lot.class);
                    i.putExtra("name",Auction_name);
                    i.putExtra("lotname",Lot_name);
                    i.putExtra("lot_status",lot_status);
                    startActivity(i);
            }
        });

        duplicate_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(New_Lot_Entry.this, Duplicate_existing_Lot.class);
                i.putExtra("name",Auction_name);
                i.putExtra("lotname",Lot_name);
                i.putExtra("lot_status",lot_status);
                startActivity(i);
            }
        });

        duplicate_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(New_Lot_Entry.this, Duplicate_existing_Lot.class);
                i.putExtra("name",Auction_name);
                i.putExtra("lotname",Lot_name);
                i.putExtra("lot_status",lot_status);
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
    protected void onDestroy() {
        mInst = null;
        super.onDestroy();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_lot, menu);
        return true;
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
            Intent i = new Intent(New_Lot_Entry.this, User_Settings.class);
            startActivity(i);

        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(New_Lot_Entry.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }  else if (id == R.id.history) {
            Intent i = new Intent(New_Lot_Entry.this, Auction_History.class);
            startActivity(i);
        }

        else if (id == R.id.home) {
            Intent i = new Intent(New_Lot_Entry.this, Auction_Screen_Main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(New_Lot_Entry.this, New_lot.class);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //  Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void handleResult(Result rawResult) {

        ScanResult = rawResult.getText();
        ScanResultFormat = rawResult.getBarcodeFormat().toString();
        Log.e("ScanResult", ScanResult);
        Log.e("ScanResultFormat", ScanResultFormat);
        isNetworkAvailable();
        if(isNetworkAvailable() == true) {
            String tag_string_req = "req_get_product_details";

            pDialog.setMessage("Please Wait ...");

            Log.e(TAG, " tag_string_req : " + tag_string_req );
            Log.e(TAG, " AppConfig.URL_get_product : " + AppConfig.URL_New_API_product+ScanResult);

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.URL_New_API_product+ScanResult, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "user details Response: " + response.toString());
                    Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_New_API_product+ScanResult);

                    try {
                        JSONObject jObj = new JSONObject(response);

                        String code = jObj.getString("code");
                        Log.e("Tag..", code);
                        if (code.equals("OK"))
                        {
                            String total = jObj.getString("total");
                            Log.e("total", total);
                            int total_int = Integer.valueOf(total);
                            System.out.print("total.......int"+ total_int);
                            if(total_int == 1)
                            {

                                Toast toast = Toast.makeText(New_Lot_Entry.this, "UPC Number is " + ScanResult, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL,0,310);
                                toast.show();
                                onPause();
                                Intent i = new Intent(New_Lot_Entry.this, Lot_Details_New.class);
                                i.putExtra("upc",ScanResult);
                                i.putExtra("name",Auction_name);
                                i.putExtra("lotname",Lot_name);
                                i.putExtra("lot_status",lot_status);
                                i.putExtra("LocalVar",LocalVar);
                                startActivity(i);
                            }

                            else if(total_int == 0) {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(code.equals("INVALID_UPC"))
                        {
                            Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "catch : " + AppConfig.URL_New_API_product+ScanResult);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    //Toast.makeText(getApplicationContext(),"Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    //onBackPressed();
                    if (error instanceof ServerError) {
                        String str = null;
                        try {
                            str = new String(error.networkResponse.data, "UTF8");
                            Log.e("str............", str);
                            JSONObject errorJson = new JSONObject(str);
                            String code = errorJson.getString("code");

                            if(code.equals("INVALID_UPC"))
                            {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (error instanceof NetworkError) {
                        String str = null;
                        try {
                            str = new String(error.networkResponse.data, "UTF8");
                            Log.e("str............", str);
                            JSONObject errorJson = new JSONObject(str);
                            String code = errorJson.getString("code");

                            if(code.equals("INVALID_UPC"))
                            {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if(error instanceof AuthFailureError) {
                        String str = null;
                        try {
                            str = new String(error.networkResponse.data, "UTF8");
                            Log.e("str............", str);
                            JSONObject errorJson = new JSONObject(str);
                            String code = errorJson.getString("code");

                            if(code.equals("INVALID_UPC"))
                            {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
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
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

                public Map < String, String > getHeaders()  {
                    HashMap < String, String > headers = new HashMap < String, String > ();
                    headers.put("user_key", "be813dc94d5addc379d97ee4d6b6eada");
                    headers.put("key_type", "3scale");
                    return headers;
                }
            };

            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(New_Lot_Entry.this);
                }
            }, 1000);
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(New_Lot_Entry.this).create();
            alertDialog.setMessage("No Internet Connection");
            alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    isNetworkAvailable();
                    finish();
                    startActivity(getIntent());
                }
            });
            alertDialog.show();
        }

    }

    void setViewText(String txt) {
        Log.e("fghfhgfhgfhfgh",txt);
        //scantextView.setText(txt);
        Log.e("ScanResultBarcode",txt);
       // Toast.makeText(New_Lot_Entry.this, "UPC NO.: "+ txt, Toast.LENGTH_SHORT).show();
        ScanResult = scantextView.getText().toString();
        Log.e("ScanResult,,,",ScanResult);
        isNetworkAvailable();
        if(isNetworkAvailable() == true) {
            String tag_string_req = "req_get_product_details";

            pDialog.setMessage("Please Wait ...");

            Log.e(TAG, " tag_string_req : " + tag_string_req );
            Log.e(TAG, " AppConfig.URL_get_product : " + AppConfig.URL_New_API_product+ScanResult);

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.URL_New_API_product+ScanResult, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "user details Response: " + response.toString());
                    Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_New_API_product+ScanResult);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String code = jObj.getString("code");
                        Log.e("Tag..", code);
                        if (code.equals("OK"))
                        {
                            String total = jObj.getString("total");
                            Log.e("total", total);
                            int total_int = Integer.valueOf(total);
                            System.out.print("total.......int"+ total_int);
                            if(total_int == 1)
                            {
                                Toast toast = Toast.makeText(New_Lot_Entry.this, "UPC Number is " + ScanResult, Toast.LENGTH_SHORT);
                                toast.show();
                                onPause();
                                Intent i = new Intent(New_Lot_Entry.this, Lot_Details_New.class);
                                i.putExtra("upc",ScanResult);
                                i.putExtra("name",Auction_name);
                                i.putExtra("lotname",Lot_name);
                                i.putExtra("lot_status",lot_status);
                                i.putExtra("LocalVar",LocalVar);
                                startActivity(i);
                            }
                            else if(total_int == 0) {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(code.equals("INVALID_UPC"))
                        {
                            Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "catch : " + AppConfig.URL_New_API_product+ScanResult);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    //Toast.makeText(getApplicationContext(),"Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    //onBackPressed();
                    if (error instanceof ServerError) {
                        String str = null;
                        try {
                            str = new String(error.networkResponse.data, "UTF8");
                            Log.e("str............", str);
                            JSONObject errorJson = new JSONObject(str);
                            String code = errorJson.getString("code");

                            if(code.equals("INVALID_UPC"))
                            {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (error instanceof NetworkError) {
                        String str = null;
                        try {
                            str = new String(error.networkResponse.data, "UTF8");
                            Log.e("str............", str);
                            JSONObject errorJson = new JSONObject(str);
                            String code = errorJson.getString("code");

                            if(code.equals("INVALID_UPC"))
                            {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if(error instanceof AuthFailureError) {
                        String str = null;
                        try {
                            str = new String(error.networkResponse.data, "UTF8");
                            Log.e("str............", str);
                            JSONObject errorJson = new JSONObject(str);
                            String code = errorJson.getString("code");

                            if(code.equals("INVALID_UPC"))
                            {
                                Toast.makeText(getApplicationContext(),"Invalid UPC",Toast.LENGTH_SHORT).show();
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
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

                public Map < String, String > getHeaders()  {
                    HashMap < String, String > headers = new HashMap < String, String > ();
                    headers.put("user_key", "be813dc94d5addc379d97ee4d6b6eada");
                    headers.put("key_type", "3scale");
                    return headers;
                }
            };

            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(New_Lot_Entry.this);
                }
            }, 1000);
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(New_Lot_Entry.this).create();
            alertDialog.setMessage("No Internet Connection");
            alertDialog.setButton("RETRY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    isNetworkAvailable();
                    finish();
                    startActivity(getIntent());
                }
            });
            alertDialog.show();
        }
    }
}
