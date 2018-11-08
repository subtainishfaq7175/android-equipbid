package equipbid.armentum.com.equip_bid;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import equipbid.armentum.com.equip_bid.model.LotDetailObj;
import equipbid.armentum.com.equip_bid.model.LotDetailServer;
import equipbid.armentum.com.equip_bid.model.LotDetailsData;
import equipbid.armentum.com.equip_bid.model.LotDetailsModel;
import equipbid.armentum.com.equip_bid.model.LotDetailsOffer;

public class Lot_Details_New extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,  View.OnClickListener  {

    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email, p_id;
    private static final String TAG = "Lot_Details_New";
    private EditText lotno, _item_title, descr, auction_no, _quantity, _textable, _item_class, upcno;
    private EditText _seller_id, cat, _condition, _add_detalils, _location, _start_price, _end_price, _msrp, _color, _model ,_dimension, _brand;
    private String item_title, quantity, textable, item_class, upc;
    private String seller_id, description, category, condition, add_detalils, location, start_price, end_price, msrp;
    private ExpandableHeightGridView gridGallery, gridGallery1;
    Handler handler;
    CustomGalleryAdapter adapter;
    CustomCameraAdapter adapterCamera;
    private ProgressDialog pDialog;
    String action;
    ImageLoader imageLoader;
    private String lot_no, Auction_name, Lot_S, Lot_S_o;
    private ArrayList<Gallary> LotimgList = new ArrayList<Gallary>();
    private GallaryAdapter glryadapter;
    private String ScanResult;
    private String images;
    private JSONArray img_arry_get;
    private JSONObject Jobj1;
    private ConnectivityManager ConnectionManager;
    private NetworkInfo networkInfo;
    private String path1;
    String images_arr;
    private String LocalVar;
    private String Auction_name_b, lot_no_b, upc_b, Lot_S_b, item_title_b, item_class_b, seller_id_b, description_b, LocalVar_b, color_b,model_b, dimension_b, brand_b;
    private String category_b, location_b, condition_b, add_detalils_b, start_price_b, msrp_b, reverse_price_b, quantity_b, textable_b, images_b;
    private String i1;
    private String i2;
    ArrayList<String> arrayList;
    private byte[] byteArray;
    private Bitmap bitmap;
    private CustomAdapterNew glryadapterback;
    ArrayList<String> arrayList1 = new ArrayList<>();
    ArrayList<String> imagePaths;
    ArrayList<String> imagePaths_external;
    ArrayList<String> spin_array;
    private String imagePaths_str, imagePaths_ext_str, timeStamp, Img_path;
    private File imagesFolder;
    private ArrayList<CustomGallery> dataT;
    private ArrayList<CustomGallery> dataCam;
    private ArrayList<CustomGallery> dataGallery;
    private ArrayList<CustomGallery> dataTT;
   // private ArrayList<CustomGallery> dataG;
   // private ArrayList<CustomGallery> dataC;
    private CustomGallery item;
    private CustomGallery item_r;
    private Gallary tempValues;
    private Spinner spn;
    private ArrayList<String> spinList = new ArrayList<String>();
    ArrayList<String> modified_response_array;
    private SpinnerAdapter spinadapter;
    private Gallary gallary_list_list;
    private String Spin_select_value;
    private JSONArray jArr;
    private JSONObject spin_obj;
    private String Catselect;
    private ArrayList<String> spin_array_back;
    private String Display = "CatF";
    private ExpandableHeightListView list;
    private AddDetailsAdapter detailadapter;
    private ArrayList<Add_Detail> AddDetailList = new ArrayList<Add_Detail>();
    private JSONArray Add_Detail_Arr;
    private JSONObject add_obj;
    private Add_Detail select_val;
    private ArrayList<String> detailsSelectlist;
    private StringBuffer responseText;
    private JSONObject jObj;
    private Typeface typeface;
    private String multiple_select;
    private ArrayList<String> detailsSelectlist_back;
    private ArrayList<String> AddDetail_List;
    private ArrayList<String> AddDetail_List_get = null;
    private ArrayList<String> Cat_Select_get;
    private String additional_details_checklist ="i";
    private CheckBox checkBox;
    private GoogleApiClient client;
    private JSONArray price_array;
    private JSONObject JobjOffer;
    private JSONArray additonl_array;
    private String color, model , brand, dimension;
    private JSONObject ChildObj;
    private String name_c;
    private String name_d;
    public String spin_get_value;
    private Button submit;
    private EditText upc_search_edt;
    private ImageView search;
    private String upc_search;
    private String Auction_n_D, Lot_n_D;
    private RadioGroup radioTax;
    private RadioButton rdYes, rdNo;
    private String remote_images_;
    private String remote_images;
    private String remote_images_second_;
    private String remote_images_second;
    private String listString ="";
    private PowerManager.WakeLock mWakeLock;
    private String thumb_lot;
    private ArrayList<String> imagePaths_All;
    FloatingActionButton fab, fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen=false;
    private String end_price_b;
    private FullScreenImageAdapterLocal ViewPageradapterLocal;
    private FullScreenImageAdapterRemote ViewPageradapterRemote;
    private Dialog Imagedialog;
    private CustomRectcleAdapter horizontalAdapter;
    private CustomListImgAdapter ListAdapter;
    private int count = 0;
    private TextView extImg, glryImg;
    final Context context = this;
    private TextView txtcamera, txtcancel, txtgallery;
    private ScrollView scrollview;
    private ArrayList<String> Images_Camera;
    private ArrayList<String> imagePaths_camera;
    private Set<String> Custom_images_new;
    private LotDetailsModel poDtls;
    private ArrayList<String> Spin_list;
    private File Mfile;
    private LotDetailObj lotdetailsobj;
    private LotDetailsData lotDetailsdataL;
    ArrayList<String> imagesObj;
    private NavigationView navigationView;
    private String user_role_label;
    private ExpandableHeightGridView gridGalleryCamera;
    private FullScreenImageAdapterLocalGallery ViewPageradapterLocalG;
    private int selectWidth, selectHeight;
    private String Local_Images;
    private List<LotDetails> LocalList;
    private int Flag = 0;
    private int image_count_before = 0;
    private final int CAPTURE_IMAGES_FROM_CAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lot_details_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fabLayout1  = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2  = (LinearLayout) findViewById(R.id.fabLayout2);
        fabLayout3  = (LinearLayout) findViewById(R.id.fabLayout3);
        fab         = (FloatingActionButton) findViewById(R.id.fab);
        fab1        = (FloatingActionButton) findViewById(R.id.fab1);
        fab2        = (FloatingActionButton) findViewById(R.id.fab2);
        fab3        = (FloatingActionButton) findViewById(R.id.fab3);
        txtcamera   = (TextView) findViewById(R.id.txtcamera);
        txtcancel   = (TextView) findViewById(R.id.txtcancel);
        txtgallery  = (TextView) findViewById(R.id.txtgallery);
        fabBGLayout =  findViewById(R.id.fabBGLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedpreferences   = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_email             = sharedpreferences.getString("email", null);
        p_username          = sharedpreferences.getString("username", null);
        p_token             = sharedpreferences.getString("token", null);
        user_role_label     = sharedpreferences.getString("user_role_label",null);
        p_id                = sharedpreferences.getString("ID", null);
        selectWidth         = sharedpreferences.getInt("selectWidth", 0);
        selectHeight        = sharedpreferences.getInt("selectHeight", 0);

        Log.e("Details", p_email + " ..username.. " + p_username + " ..tokentoken.. " + p_token + " ..id.." + p_id);
        Log.e("value", selectWidth+ "..." + selectHeight);

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

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire();

        isNetworkAvailable();

        upc_search_edt  = (EditText) findViewById(R.id.upc_search_edt);
        search          = (ImageView) findViewById(R.id.search);
        upcno           = (EditText) findViewById(R.id.upc);
        lotno           = (EditText) findViewById(R.id.lot_no);
        auction_no      = (EditText) findViewById(R.id.auction_no);
        _item_title     = (EditText) findViewById(R.id.item_title);
        _seller_id      = (EditText) findViewById(R.id.seller_id);
        descr           = (EditText) findViewById(R.id.descr);
        _item_class     = (EditText) findViewById(R.id.item_class);
        _color          = (EditText) findViewById(R.id.color);
        _model          = (EditText) findViewById(R.id.model);
        _dimension      = (EditText) findViewById(R.id.dimension);
        _brand          = (EditText) findViewById(R.id.brand);
        _condition      = (EditText) findViewById(R.id.condition);
        _add_detalils   = (EditText) findViewById(R.id.add_detalils);
        _location       = (EditText) findViewById(R.id.location);
        _start_price    = (EditText) findViewById(R.id.start_price);
        _msrp           = (EditText) findViewById(R.id.msrp);
        _end_price      = (EditText) findViewById(R.id.end_price);
        _quantity       = (EditText) findViewById(R.id.quantity);
        spn             = (Spinner) findViewById(R.id.cat);
        submit          = (Button) findViewById(R.id.submit);
        radioTax        = (RadioGroup) findViewById(R.id.radioTax);
        rdYes           = (RadioButton) findViewById(R.id.rdYes);
        rdNo            = (RadioButton) findViewById(R.id.rdNo);
        extImg          = (TextView) findViewById(R.id.extImg);
        glryImg         = (TextView) findViewById(R.id.glryImg);
        scrollview      = (ScrollView)findViewById(R.id.scrollview);

        Bundle bundle   = getIntent().getExtras();
        Auction_name    = bundle.getString("name");
        lot_no          = bundle.getString("lotname");
        ScanResult      = bundle.getString("upc");
        Lot_S           = bundle.getString("lot_status");
        Lot_S_o         = bundle.getString("lot_status_o");
        LocalVar        = bundle.getString("LocalVar");
        Auction_n_D     = bundle.getString("AA");
        Lot_n_D         = bundle.getString("LL");
        Images_Camera   = getIntent().getStringArrayListExtra("Images_Camera");
        Log.e("Images_Camera", Images_Camera+"");
        upc_search      = upc_search_edt.getText().toString().trim();
        gridGallery     = (ExpandableHeightGridView) findViewById(R.id.gridGallery);
        gridGallery1    = (ExpandableHeightGridView) findViewById(R.id.gridGallery1);
        gridGalleryCamera    = (ExpandableHeightGridView) findViewById(R.id.gridGalleryCamera);
        list            = (ExpandableHeightListView) findViewById(R.id.list);

        imagePaths_All = getIntent().getStringArrayListExtra("imagePaths_All");
        Log.e("imagePaths_All", " "+ imagePaths_All);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");

        descr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.des_view, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                userInput.setText(descr.getText());
                userInput.setSelection(userInput.length());
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        descr.setText(userInput.getText());
                                        descr.setSelection(descr.length());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        upc_search_edt.setTypeface(typeface);
        upcno.setTypeface(typeface);
        auction_no.setTypeface(typeface);
        lotno.setTypeface(typeface);
        _item_title.setTypeface(typeface);
        _seller_id.setTypeface(typeface);
        descr.setTypeface(typeface);
        _color.setTypeface(typeface);
        _model.setTypeface(typeface);
        _dimension.setTypeface(typeface);
        _brand.setTypeface(typeface);
        _condition.setTypeface(typeface);
        _item_class.setTypeface(typeface);
        _add_detalils.setTypeface(typeface);
        _location.setTypeface(typeface);
        _start_price.setTypeface(typeface);
        _end_price.setTypeface(typeface);
        _quantity.setTypeface(typeface);
        _msrp.setTypeface(typeface);
        txtcamera.setTypeface(typeface);
        txtcancel.setTypeface(typeface);
        txtgallery.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        auction_no.setEnabled(false);
        mTitle.setEnabled(false);

        lotno.setEnabled(false);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Adaptervalue for remote images
        glryadapter = new GallaryAdapter(Lot_Details_New.this, LotimgList);
        gridGallery1.setAdapter(glryadapter);
        gridGallery1.setExpanded(true);
        gridGallery1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                tempValues = (Gallary)LotimgList.get(i);
                thumb_lot = tempValues.getImagesUrl();
                glryadapter.notifyDataSetChanged();
                Log.e("imagePath_external__1ex",""+imagePaths_external);
                Log.e("imagesObj",""+imagesObj);
                Log.e("LotimgList",""+LotimgList);
                Log.e("LotimgList",""+LotimgList.size());
                Imagedialog = new Dialog(Lot_Details_New.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                Imagedialog.setContentView(R.layout.activity_lot_details__img);

                ViewPager viewPager = (ViewPager) Imagedialog.findViewById(R.id.pager);
                ViewPageradapterRemote = new FullScreenImageAdapterRemote(Lot_Details_New.this, LotimgList, imagePaths_external, imagesObj);
                viewPager.setAdapter(ViewPageradapterRemote);
                viewPager.setCurrentItem(i);
                ViewPageradapterRemote.notifyDataSetChanged();
                Imagedialog.show();
            }
        });

        arrayList               = new ArrayList<String>();
        imagePaths              = new ArrayList<String>();
        imagePaths_external     = new ArrayList<String>();
        spin_array              = new ArrayList<String>();
        AddDetail_List          = new ArrayList<String>();
        modified_response_array = new ArrayList<String>();
        AddDetail_List_get      = new ArrayList<String>();
        Cat_Select_get          = new ArrayList<String>();
        detailsSelectlist_back  = new ArrayList<String>();
        detailsSelectlist       = new ArrayList<String>();
        imagePaths_All          = new ArrayList<String>();
        imagePaths_camera       = new ArrayList<String>();
        Custom_images_new       = new HashSet<String>();
        dataT                   = new ArrayList<CustomGallery>();
        dataCam                 = new ArrayList<CustomGallery>();
        dataGallery             = new ArrayList<CustomGallery>();
        dataTT                  = new ArrayList<CustomGallery>();
        Spin_list               = new ArrayList<String>();

        imagePaths_str      = imagePaths.toString();
        imagePaths_ext_str  = imagePaths_external.toString();
        remote_images_      = imagePaths_ext_str;
        remote_images       = remote_images_.replaceAll(" ", "");

        spinadapter         = new SpinnerAdapter(Lot_Details_New.this, spinList);
        spn.setAdapter(spinadapter);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spin_value = spinList.get(position);
                spin_value = spin_value.replace(" - ","");
                Spin_select_value = spin_value;
                Log.e("name", "jxcdfgfgfh.." + spin_value);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        detailadapter       = new AddDetailsAdapter(Lot_Details_New.this, AddDetailList);
        list.setAdapter(detailadapter);
        list.setExpanded(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                select_val = (Add_Detail) AddDetailList.get(i);
                Log.e("select_val", select_val.getName());
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                isNetworkAvailable();
                if(isNetworkAvailable() == true)
                {
                    upc_search      = upc_search_edt.getText().toString().trim();
                    Log.e(TAG, " upc_number : " + upc_search);
                    getProductDetails(upc_search);
                }
                else
                {

                }
            }
        });

        initImageLoader();
        init();

        if (isNetworkAvailable() == true) {

            poDtls = bundle.getParcelable("LotDetailsModel");
            lotdetailsobj = bundle.getParcelable("lotdetailsobj");
            if(poDtls != null)
            {
                lot_no          = bundle.getString("lotname");
                Auction_name    = bundle.getString("name");
                Log.e("Images_Camera", Images_Camera+"");
                Log.e("ModelBack",  new Gson().toJson(poDtls));
                Log.e("lotdetailsobj",  new Gson().toJson(lotdetailsobj));
                SetProductValue(poDtls, false);
            }
            else {
                if (Lot_S.equals("publish")) {
                    if (LocalVar.equals("not_set")) {
                        auction_no.setText(Auction_name);
                        lotno.setText(lot_no);
                        getcategorySpin(p_token);
                    } else if (LocalVar.equals("Set_local")) {
                        Bundle bundle_back = getIntent().getExtras();
                        LocalVar_b = bundle_back.getString("LocalVar");
                        imagePaths_All = getIntent().getStringArrayListExtra("imagePaths_external");
                        Log.e("imagePaths_All", " " + imagePaths_All);
                    }
                } else if (Lot_S.equals("draft")) {
                    if (LocalVar.equals("not_set")) {
                        auction_no.setText(Auction_name);
                        lotno.setText(lot_no);
                        Log.e(TAG, "ScanResult.22.." + ScanResult);

                        if (ScanResult.equals("")) {
                            Log.e(TAG, "ScanResult if" + ScanResult);
                            getcategorySpinFirst(p_token);
                            getAddDetailsFirst(p_token);
                        } else if (ScanResult.equals("1234")) {
                            Log.e(TAG, "ScanResult.else D..." + ScanResult);
                            auction_no.setText(Auction_n_D);
                            lotno.setText(Lot_n_D);
                            getcategorySpinDuplicate(p_token);
                        } else if (!ScanResult.equals("")) {
                            Log.e(TAG, "ScanResult.else" + ScanResult);
                            auction_no.setText(Auction_name);
                            lotno.setText(lot_no);
                            getcategorySpinFirst(p_token);
                            getAddDetailsFirst(p_token);
                            getProductDetails(ScanResult);
                        }
                    }
                }
            }
        } else {

            Bundle bundleL   = getIntent().getExtras();
            Flag = bundleL.getInt("Flag");
            if(Flag == 1)
            {
                //Toast.makeText(context, Mfile+"", Toast.LENGTH_SHORT).show();
                auction_no.setEnabled(true);
                auction_no.setBackgroundResource(R.drawable.edittext_createnew);
                lotno.setEnabled(true);
                lotno.setBackgroundResource(R.drawable.edittext_createnew);
            }
            else {
               // Toast.makeText(context, Flag+"", Toast.LENGTH_SHORT).show();
            }

            Log.e("Details", Auction_name+".."+lot_no+".."+Lot_S);
            auction_no.setText(Auction_name);
            lotno.setText(lot_no);
            fillDetailsList();
            fillCatList();
            poDtls = bundle.getParcelable("LotDetailsModel");
            lotdetailsobj = bundle.getParcelable("lotdetailsobj");
            if(poDtls != null)
            {
                lot_no          = bundle.getString("lotname");
                Auction_name    = bundle.getString("name");
                Log.e("Images_Camera", Images_Camera+"");
                Log.e("ModelBack",  new Gson().toJson(poDtls));
                Log.e("lotdetailsobj",  new Gson().toJson(lotdetailsobj));
                SetProductValue(poDtls, false);
            }

            LocalList = new ArrayList<LotDetails>();
            MyDatabaseHelper helper = new MyDatabaseHelper(Lot_Details_New.this);
            LocalList = helper.GetDataForAuction(Auction_name, lot_no);
            List<HashMap<String, String>> maplst = new ArrayList<HashMap<String,String>>();
            LotDetails l = new LotDetails();
            if(LocalList.size()>0) {
                for (int i = 0; i < LocalList.size(); i++) {
                    l = LocalList.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();
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
                    map.put(MyDatabaseHelper.Col_File, l.getmFile());
                    map.put(MyDatabaseHelper.Col_Spinselect, String.valueOf(l.getSpinselect()));
                    maplst.add(map);
                }
                Log.e("LocalList", new Gson().toJson(LocalList)+"");
            }

            for (int i = 0; i < LocalList.size(); i++) {
                upcno.setText(LocalList.get(i).getUpc());
                _item_title.setText(LocalList.get(i).getItem_title());
                _seller_id.setText(LocalList.get(i).getSeller_id());
                descr.setText(LocalList.get(i).getDescription());
                _item_class.setText(LocalList.get(i).getItem_class());
                _color.setText(LocalList.get(i).getColor());
                _brand.setText(LocalList.get(i).getBrand());
                _dimension.setText(LocalList.get(i).getDimension());
                _model.setText(LocalList.get(i).getModel());
                _condition.setText(LocalList.get(i).getCondition());
                _location.setText(LocalList.get(i).getLocation());
                _add_detalils.setText(LocalList.get(i).getAdd_detalils());
                _start_price.setText(LocalList.get(i).getStart_price());
                _end_price.setText(LocalList.get(i).getReverse_price());
                _quantity.setText(LocalList.get(i).getQuantity());
                _msrp.setText(LocalList.get(i).getMsrp());
                if(rdYes.getText().toString().equals(LocalList.get(i).getTextable()))
                {
                    rdYes.setChecked(true);
                }
                else if(rdNo.getText().toString().equals(LocalList.get(i).getTextable()))
                {
                    rdNo.setChecked(true);
                }

                Cat_Select_get.add(LocalList.get(i).getCategory());

                spinadapter = new SpinnerAdapter(Lot_Details_New.this, spinList);
                spn.setAdapter(spinadapter);
                spn.setSelection(LocalList.get(i).getSpinselect());

                ArrayList<String> Add_All = new ArrayList<String>();

                Log.e("Add3", LocalList.get(i).getAdditional_details_checklist());
                Log.e("Add3 ", ""+LocalList.get(i).getAdditional_details_checklist().split(", ").toString());
                Log.e("Add3 ", ""+LocalList.get(i).getAdditional_details_checklist().split(", ").length);

                for(int a=0; a< LocalList.get(i).getAdditional_details_checklist().split(", ").length; a++)
                {
                    Log.e("Add4 ", LocalList.get(i).getAdditional_details_checklist().split(", ")[a]+"");
                    Add_All.add(AddDetailList.get(a).getName());
                    AddDetail_List_get.add(LocalList.get(i).getAdditional_details_checklist().split(", ")[a]);
                    detailsSelectlist.add(LocalList.get(i).getAdditional_details_checklist().split(", ")[a]);
                }

                Log.e("Local_img ", LocalList.get(i).getImages());
                Log.e("Local_img ", ""+LocalList.get(i).getImages().length());
                Log.e("Remote_img ", LocalList.get(i).getRemote_images());
                Log.e("Remote_img ", ""+LocalList.get(i).getRemote_images().length());
                Log.e("File.. ", ""+LocalList.get(i).getmFile());
                String file = LocalList.get(i).getmFile();
                File MFile = new File(file);
                Log.e(TAG, "File.."+ MFile);
                if(!MFile.equals(""))
                {
                    deleteRecursive(MFile);
                }
                else
                {

                }
                String listStringRemote = LocalList.get(i).getRemote_images().replaceAll("\\[", "");
                listStringRemote = listStringRemote.replaceAll("\\]", "");

                Log.e(TAG, "listString " + listStringRemote.length());
                if(listStringRemote.length() == 0) {}
                else {
                    for(int j = 0; j < listStringRemote.split(",").length; j++)
                    {
                        String Image = listStringRemote.split(",")[j].trim();
                        Log.e(TAG, "remote_imagesdfdgfg "+Image);
                        imagePaths_external.add(Image);
                        arrayList.add(Image);
                        Log.e(TAG, "imagePaths_external "+imagePaths_external);
                        Log.e(TAG, "arrayList "+arrayList);
                        gallary_list_list = new Gallary();
                        gallary_list_list.setImagesUrl(Image);
                        gallary_list_list.setImgId(Image);
                        gallary_list_list.setImgTitle(Image);
                        LotimgList.add(gallary_list_list);
                    }
                }
                if(imagePaths_external.size() != 0){
                    extImg.setVisibility(View.VISIBLE);
                }

                glryadapter.notifyDataSetChanged();

                String listStringLocal = LocalList.get(i).getImages().replaceAll("\\[", "");
                listStringLocal = listStringLocal.replaceAll("\\]", "");

                Log.e(TAG, "listStringLocal " + listStringLocal);
                Log.e(TAG, "listStringLocal " + listStringLocal.split(", ").length);
                if(listStringLocal.length() == 0) {}
                else {
                    for (int c = 0; c < listStringLocal.split(", ").length; c++) {
                        Log.e("listStringL", listStringLocal.split(", ")[c]);
                        item = new CustomGallery();
                        item.sdcardPath = listStringLocal.split(", ")[c];
                        imagePaths_camera.add(listStringLocal.split(", ")[c]);
                        dataT.add(item);
                    }
                }
                if(dataT.size() != 0){
                    glryImg.setVisibility(View.VISIBLE);
                }

                Log.e("dataCamera", dataT+"");
                adapter.addAll(dataT);
                adapter.notifyDataSetChanged();

            }

            Log.e("helpercxvcv", helper+"");
        }
        submit.setOnClickListener(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void fillDetailsList() {
        MyDatabaseHelper helper = new MyDatabaseHelper(this);
        AddDetailList.clear();
        List<HashMap<String, String>> maplst = new ArrayList<HashMap<String,String>>();
        AddDetailList = helper.getAllADetailsList();
        Add_Detail l = new Add_Detail();
        if(AddDetailList.size()>0){
            Log.e("AddDetailList", AddDetailList.size()+"");
            for(int i=0;i<AddDetailList.size();i++){
                l = AddDetailList.get(i);
                Log.e("aaaaaa", l.getName()+"");
                HashMap< String, String> map = new HashMap<String, String>();
                map.put(MyDatabaseHelper.Col_Adddetails_list, String.valueOf(l.getName()));
                maplst.add(map);
            }

            detailadapter  = new AddDetailsAdapter(Lot_Details_New.this, AddDetailList);
            list.setAdapter(detailadapter);
            list.setExpanded(true);
        }
    }

    private void fillCatList() {
        MyDatabaseHelper helper = new MyDatabaseHelper(this);
        spinList.clear();
        List<HashMap<String, String>> maplst = new ArrayList<HashMap<String,String>>();
        spinList = helper.getCatList();
        Add_Detail l = new Add_Detail();
        if(AddDetailList.size()>0){
            Log.e("AddDetailList", AddDetailList.size()+"");
            for(int i=0;i<AddDetailList.size();i++){
                l = AddDetailList.get(i);
                Log.e("aaaaaa", l.getName()+"");
                HashMap< String, String> map = new HashMap<String, String>();
                map.put(MyDatabaseHelper.Col_Adddetails_list, String.valueOf(l.getName()));
                maplst.add(map);
            }

            spinadapter         = new SpinnerAdapter(Lot_Details_New.this, spinList);
            spn.setAdapter(spinadapter);
        }
    }

    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void getProductDetails(final String scanResult) {

        String tag_string_req = "req_get_product_details";
        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_get_product : " + AppConfig.URL_New_API_product + scanResult);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_New_API_product + scanResult, new Response.Listener<String>() {

            public ArrayList<String> itemsToAdd;

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_New_API_product + scanResult);

                try {
                    poDtls = new LotDetailsModel();

                    poDtls.setAuction_name(Auction_name);
                    poDtls.setLot_no(lot_no);
                    poDtls.setScanResult(ScanResult);
                    poDtls.setLot_S(Lot_S);
                    poDtls.setLot_S_o(Lot_S_o);
                    poDtls.setLocalVar(LocalVar);
                    poDtls.setAuction_n_D(Auction_n_D);
                    poDtls.setLot_n_D(Lot_n_D);

                    JSONObject jObj = new JSONObject(response);

                    String code = jObj.getString("code");
                    poDtls.setCode(code);

                    Log.e("Tag..", code);
                    if (code.equals("OK")) {

                        String total = jObj.getString("total");
                        poDtls.setTotal(total);
                       // _quantity.setText(poDtls.getTotal());

                        JSONArray item_arry = jObj.getJSONArray("items");
                        ArrayList<LotDetailObj>  itemsP = new ArrayList<LotDetailObj>();
                        for (int i = 0; i < item_arry.length(); i++) {

                            LotDetailObj obj = new LotDetailObj();
                            try {
                                Jobj1 = item_arry.getJSONObject(i);
                                String ean = Jobj1.getString("ean");
                                obj.setEan(ean);
                                String title = Jobj1.getString("title");
                                obj.setTitle(title);
                               // _item_title.setText(obj.getTitle()); //

                                final String description = Jobj1.getString("description");
                                obj.setDescription(description);
                                //descr.setText(obj.getDescription());//
                                descr.setSelection(descr.length());
                                descr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LayoutInflater li = LayoutInflater.from(context);
                                        View promptsView = li.inflate(R.layout.des_view, null);

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                        alertDialogBuilder.setView(promptsView);

                                        final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                                        userInput.setText(descr.getText());
                                        userInput.setSelection(userInput.length());
                                        // set dialog message
                                        alertDialogBuilder
                                                .setCancelable(false)
                                                .setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog,int id) {

                                                                descr.setText(userInput.getText());
                                                                descr.setSelection(descr.length());
                                                            }
                                                        })
                                                .setNegativeButton("Cancel",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog,int id) {
                                                                dialog.cancel();
                                                            }
                                                        });

                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });

                                String upc = Jobj1.getString("upc");
                                obj.setUpc(upc);
                                upcno.setText(obj.getUpc());
                                String brand = Jobj1.getString("brand");
                                obj.setBrand(brand);
                                String model = Jobj1.getString("model");
                                obj.setModel(model);
                                String color = Jobj1.getString("color");
                                obj.setColor(color);
                                String dimension = Jobj1.getString("dimension");
                                obj.setDimension(dimension);

                                price_array = Jobj1.getJSONArray("offers");

                                itemsToAdd = new ArrayList<String>();
                                ArrayList<LotDetailsOffer>  OffersP = new ArrayList<LotDetailsOffer>();

                                for (int k = 0; k < price_array.length(); k++) {
                                    LotDetailsOffer OfferObj = new LotDetailsOffer();
                                    JobjOffer = price_array.getJSONObject(k);
                                    String price = JobjOffer.getString("price");
                                    OfferObj.setPrice(price);
                                    Log.e("MSRP.."+(k), price);
                                    itemsToAdd.add(OfferObj.getPrice());
                                    OffersP.add(OfferObj);
                                }

                                obj.setOffers(OffersP);
                                Log.e("MSRP", ""+itemsToAdd.toString());
                                Log.e("MSRP_size", ""+itemsToAdd.size());
                                if(itemsToAdd.size() == 0)
                                {
                                   // _msrp.setText("$"+00.00);
                                }
                                else {
                                   // String Msrp = Collections.max(itemsToAdd);
                                   // _msrp.setText("$"+Msrp);
                                }

                                img_arry_get = Jobj1.getJSONArray("images");
                                Log.e(TAG, "length " + img_arry_get.length());
                                if(img_arry_get.length() != 0){
                                    extImg.setVisibility(View.VISIBLE);
                                }

                                imagesObj = new ArrayList<String>();
                                for (int j = 0; j < img_arry_get.length(); j++) {
                                    try {
                                        imagesObj.add(img_arry_get.getString(j));
                                        /*images = img_arry_get.getString(j);
                                        imagePaths_external.add(img_arry_get.getString(j));
                                        arrayList.add(img_arry_get.getString(j));
                                        gallary_list_list = new Gallary();
                                        gallary_list_list.setImagesUrl(images);
                                        gallary_list_list.setImgId(images);
                                        gallary_list_list.setImgTitle(images);*/
                                        //LotimgList.add(gallary_list_list);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                obj.setImages(imagesObj);
                                obj.setLotimgList(LotimgList);
                                Log.e(TAG, " ean has : " + ean);
                                Log.e(TAG, " title has : " + title);
                                Log.e(TAG, " description has : " + description);
                                Log.e(TAG, " upc has : " + upc);
                                Log.e(TAG, " brand has : " + brand);
                                Log.e(TAG, " model has : " + model);
                                Log.e(TAG, " images_product : " + images);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            glryadapter.notifyDataSetChanged();
                            itemsP.add(obj);
                        }

                        poDtls.setItems(itemsP);
                        /*Log.e("model", poDtls.toString());
                        Log.e("model1", poDtls.getTotal());
                        Log.e("model1", poDtls.getItems().get(0).getDescription());
                        Log.e("model1", poDtls.getItems().get(0).getOffers().get(0).getPrice());*/
                       // lotdetailsobj = new LotDetailObj();
                        SetProductValue(poDtls, true);

                    } else if (code.equals("INVALID_UPC")) {
                        String message = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                     else if (code.equals("INVALID_QUERY")) {
                        String message = "Please Enter UPC Code";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                     }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_New_API_product + scanResult);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
               // onBackPressed();
                hideDialog();

                if (error instanceof AuthFailureError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str.AuthFailureError", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");

                        if (code.equals("INVALID_UPC")) {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                        else if (code.equals("INVALID_QUERY")) {
                            String message = "Please Enter UPC Code";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if( error instanceof NetworkError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str..NetworkError....", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");

                        if (code.equals("INVALID_UPC")) {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }

                        else if (code.equals("INVALID_QUERY")) {
                            String message = "Please Enter UPC Code";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if( error instanceof ServerError) {
                String str = null;
                try {
                    str = new String(error.networkResponse.data, "UTF8");
                    Log.e("str..ServerError...", str);
                    JSONObject errorJson = new JSONObject(str);
                    String code = errorJson.getString("code");

                    if (code.equals("INVALID_UPC")) {
                        String message = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else if (code.equals("INVALID_QUERY")) {
                        String message = "Please Enter UPC Code";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                } else if( error instanceof AuthFailureError) {
                String str = null;
                try {
                    str = new String(error.networkResponse.data, "UTF8");
                    Log.e("str.AuthFailureErr..", str);
                    JSONObject errorJson = new JSONObject(str);
                    String code = errorJson.getString("code");

                    if (code.equals("INVALID_UPC")) {
                        String message = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else if (code.equals("INVALID_QUERY")) {
                        String message = "Please Enter UPC Code";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                } else if( error instanceof ParseError) {
                String str = null;
                try {
                    str = new String(error.networkResponse.data, "UTF8");
                    Log.e("str...ParseError....", str);
                    JSONObject errorJson = new JSONObject(str);
                    String code = errorJson.getString("code");

                    if (code.equals("INVALID_UPC")) {
                        String message = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else if (code.equals("INVALID_QUERY")) {
                        String message = "Please Enter UPC Code";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                    } catch (Exception e) {
                        e.printStackTrace();
                }
                } else if( error instanceof NoConnectionError) {
                String str = null;
                try {
                    str = new String(error.networkResponse.data, "UTF8");
                    Log.e("str..NoConnectionErr.", str);
                    JSONObject errorJson = new JSONObject(str);
                    String code = errorJson.getString("code");

                        if (code.equals("INVALID_UPC")) {
                            String message = errorJson.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                        else if (code.equals("INVALID_QUERY")) {
                            String message = "Please Enter UPC Code";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if( error instanceof TimeoutError) {
                String str = null;
                try {
                    str = new String(error.networkResponse.data, "UTF8");
                    Log.e("str...TimeoutError...", str);
                    JSONObject errorJson = new JSONObject(str);
                    String code = errorJson.getString("code");

                    if (code.equals("INVALID_UPC")) {
                        String message = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else if (code.equals("INVALID_QUERY")) {
                        String message = "Please Enter UPC Code";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("user_key", "be813dc94d5addc379d97ee4d6b6eada");
                headers.put("key_type", "3scale");
                return headers;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void initImageLoader() {
        // for universal image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void init() {
        //Adaptervalue for gallery images
        handler = new Handler();
        gridGallery.setFastScrollEnabled(true);
        adapter = new CustomGalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);
        gridGallery.setExpanded(true);
        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                Log.e(TAG, "dataT" + dataT);
                Log.e("imagePaths",  imagePaths+"");
                imagePaths.clear();

                for(int j=0; j<dataT.size(); j++){
                    String dd = dataT.get(j).sdcardPath;
                    Log.e("sfdsfddf", dd);
                    imagePaths.add(dataT.get(j).sdcardPath);
                }

                Log.e("imagePaths_gallery",  imagePaths+"");
                Imagedialog = new Dialog(Lot_Details_New.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                Imagedialog.setContentView(R.layout.activity_lot_details__img);
                ViewPager viewPager = (ViewPager) Imagedialog.findViewById(R.id.pager);
                ViewPageradapterLocalG = new FullScreenImageAdapterLocalGallery(Lot_Details_New.this, imagePaths);
                viewPager.setAdapter(ViewPageradapterLocalG);
                viewPager.setCurrentItem(i);
                ViewPageradapterLocalG.notifyDataSetChanged();
                Imagedialog.show();
            }
        });

        // Adaptervalue for Camera Images
        gridGalleryCamera.setFastScrollEnabled(true);
        adapterCamera = new CustomCameraAdapter(getApplicationContext(), imageLoader);
        adapterCamera.setMultiplePick(false);
        gridGalleryCamera.setAdapter(adapterCamera);
        gridGalleryCamera.setExpanded(true);
        gridGalleryCamera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                Toast.makeText(context,gridGalleryCamera.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "dataTCam" + dataCam);
                imagePaths_camera.clear();

                for(int j=0; j<dataCam.size(); j++){
                    String dd = dataCam.get(j).sdcardPath;
                    Log.e("sfdsfddf", dd);
                    imagePaths_camera.add(dataCam.get(j).sdcardPath);
                }

                Log.e("imagePaths_camera",  imagePaths_camera+"");
                Imagedialog = new Dialog(Lot_Details_New.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                Imagedialog.setContentView(R.layout.activity_lot_details__img);
                ViewPager viewPager = (ViewPager) Imagedialog.findViewById(R.id.pager);
                ViewPageradapterLocal = new FullScreenImageAdapterLocal(Lot_Details_New.this, imagePaths_camera, Mfile);
                viewPager.setAdapter(ViewPageradapterLocal);
                viewPager.setCurrentItem(i);
                ViewPageradapterLocal.notifyDataSetChanged();
                Imagedialog.show();
            }
        });


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        txtcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        txtcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetUpdatedProductValue(poDtls);
                startCameraActivity();
                /*Intent inext = new Intent(Lot_Details_New.this, CameraActivity.class);
                Log.e("second2", lot_no+" "+Auction_name);
                inext.putExtra("lotname",lot_no);
                inext.putExtra("name",Auction_name);
                Bundle b = new Bundle();
                b.putParcelable("LotDetailsModel", poDtls);
                b.putParcelable("lotdetailsobj", lotdetailsobj);
                inext.putExtras(b);
                startActivity(inext);*/
                closeFABMenu();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetUpdatedProductValue(poDtls);
                startCameraActivity();
                /*Log.e("ModelCam", new Gson().toJson(poDtls));
                Intent inext = new Intent(Lot_Details_New.this, CameraActivity.class);
                Log.e("second3", lot_no+" "+Auction_name);
                inext.putExtra("lotname",lot_no);
                inext.putExtra("name",Auction_name);
                Bundle b = new Bundle();
                b.putParcelable("LotDetailsModel", poDtls);
                b.putParcelable("lotdetailsobj", lotdetailsobj);
                inext.putExtras(b);
                startActivity(inext);*/
                closeFABMenu();
            }
        });

        txtgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
                closeFABMenu();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
                closeFABMenu();
            }
        });
    }
    // ----------------------------------------------------------------------
    ///// camera

    public void startCameraActivity() {

        Cursor cursor = loadCursor();

        Log.e(TAG, "cursor "+ cursor);
        //current images in mediaStore
        image_count_before = cursor.getCount();
        Log.e(TAG, "image_count_before "+ image_count_before);
        cursor.close();
        Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(cameraIntent, 0);

        if (activities.size() > 0)
            startActivityForResult(cameraIntent, CAPTURE_IMAGES_FROM_CAMERA);
        else
            Toast.makeText(this, "No camera", Toast.LENGTH_SHORT).show();
    }

    public Cursor loadCursor() {

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        Log.e(TAG, "columns "+ columns);
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        Log.e(TAG, "orderBy "+ orderBy);
        return getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
    }

    //------------------------------------------------------------------------------------

    public com.google.android.gms.appindexing.Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Lot_Details Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new com.google.android.gms.appindexing.Action.Builder(com.google.android.gms.appindexing.Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(com.google.android.gms.appindexing.Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class CustomAdapterNew extends BaseAdapter {

        Context context;
        private LayoutInflater inflater = null;
        private ImageView thumbnailget;

        public CustomAdapterNew(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList1.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int location) {
            return location;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.grid_img_list, parent, false);
            }
            thumbnailget = (ImageView) view.findViewById(R.id.thumbnailget);

            Glide.with(getApplicationContext())
                    .load(arrayList1.get(position))
                    .placeholder(R.drawable.loaderd)
                    .into(thumbnailget);

            return view;
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

    private void getcategorySpin(final String p_token) {

        pDialog.setMessage("Please Wait ...");
        showDialog();

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_cat : " + AppConfig.URL_Category);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Category, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if (code.equals("terms_found")) {

                        JSONArray item_arry = jObj.getJSONArray("terms");
                        ArrayList<String> modified_response_array = new ArrayList<String>();
                        for (int i = 0; i < item_arry.length(); i++) {
                            try {

                                Jobj1 = item_arry.getJSONObject(i);
                                String name = Jobj1.getString("name");
                                Log.e("name", name);

                                modified_response_array.add(name);
                                JSONArray Child_array = Jobj1.getJSONArray("childs");
                                for (int k = 0; k < Child_array.length(); k++) {
                                    ChildObj = Child_array.getJSONObject(k);
                                    name_c = ChildObj.getString("name");
                                    name_d = " - "+ name_c;
                                    modified_response_array.add(name_d);
                                }
                                Log.e("childs", name_c);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for(int j=0; j<modified_response_array.size(); j++)
                        {
                            spinList.add(modified_response_array.get(j));
                        }

                        Log.e("mdfied_array", modified_response_array.toString());

                        spinadapter.notifyDataSetChanged();

                        // Toast.makeText(Lot_Details.this, "Second", Toast.LENGTH_SHORT).show();
                        getLotDetails(p_id, p_token, Auction_name, lot_no);
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

    private void getcategorySpinDuplicate(final String p_token) {

        pDialog.setMessage("Please Wait ...");
        showDialog();

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_cat : " + AppConfig.URL_Category);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Category, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String code = jObj.getString("code");
                    Log.e("Tag..", code);
                    if (code.equals("terms_found")) {

                        JSONArray item_arry = jObj.getJSONArray("terms");
                        ArrayList<String> modified_response_array = new ArrayList<String>();
                        for (int i = 0; i < item_arry.length(); i++) {
                            try {

                                Jobj1 = item_arry.getJSONObject(i);
                                String name = Jobj1.getString("name");
                                Log.e("name", name);

                                modified_response_array.add(name);
                                JSONArray Child_array = Jobj1.getJSONArray("childs");
                                for (int k = 0; k < Child_array.length(); k++) {
                                    ChildObj = Child_array.getJSONObject(k);
                                    name_c = ChildObj.getString("name");
                                    name_d = " - "+ name_c;
                                    modified_response_array.add(name_d);
                                }
                                Log.e("childs", name_c);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for(int j=0; j<modified_response_array.size(); j++)
                        {
                            spinList.add(modified_response_array.get(j));
                        }

                        Log.e("mdfied_array", modified_response_array.toString());

                        spinadapter.notifyDataSetChanged();

                        // Toast.makeText(Lot_Details.this, "Second", Toast.LENGTH_SHORT).show();
                        getLotDetailsD(p_id, p_token, Auction_name, lot_no);
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
                        ArrayList<String> modified_response_array = new ArrayList<String>();
                        for (int i = 0; i < item_arry.length(); i++) {
                            try {

                                Jobj1 = item_arry.getJSONObject(i);
                                String name = Jobj1.getString("name");
                                Log.e("name", name);

                                modified_response_array.add(name);
                                JSONArray Child_array = Jobj1.getJSONArray("childs");
                                for (int k = 0; k < Child_array.length(); k++) {
                                    ChildObj = Child_array.getJSONObject(k);
                                    name_c = ChildObj.getString("name");
                                    name_d = " - "+ name_c;
                                    modified_response_array.add(name_d);
                                }
                                Log.e("childs", name_c);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for(int j=0; j<modified_response_array.size(); j++)
                        {
                            spinList.add(modified_response_array.get(j));
                        }

                        Log.e("spinListBack", spinList+"");
                        Log.e("modified_array", modified_response_array.toString());

                        spinadapter.notifyDataSetChanged();
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

    private void getAddDetails(final String p_token) {

        pDialog.setMessage("Please Wait ...");
        showDialog();

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_cat : " + AppConfig.URL_Add_Details);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Add_Details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    lotDetailsdataL = new LotDetailsData();
                    Add_Detail_Arr = new JSONArray(response);
                    Log.e(TAG, "try : " + AppConfig.URL_Add_Details);
                    for (int i = 0; i < Add_Detail_Arr.length(); i++) {
                        try {
                            add_obj = Add_Detail_Arr.getJSONObject(i);
                            String slug = add_obj.getString("slug");
                            String name_j = add_obj.getString("name");
                            String id = add_obj.getString("id");

                            Log.e("details namg....", name_j);

                            AddDetail_List.add(add_obj.getString("name"));
                            Add_Detail Add_Detail_list = new Add_Detail();
                            Add_Detail_list.setName(name_j);
                            Add_Detail_list.setId(id);
                            AddDetailList.add(Add_Detail_list);
                            lotDetailsdataL.setAddDetailList(AddDetailList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.e("AddDetailListAA", AddDetailList+"");
                    lotDetailsdataL.setAddDetailList(AddDetailList);
                    detailadapter.notifyDataSetChanged();

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

    private void getAddDetailsFirst(final String p_token) {

        pDialog.setMessage("Please Wait ...");
        showDialog();

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_cat : " + AppConfig.URL_Add_Details);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Add_Details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    Add_Detail_Arr = new JSONArray(response);
                    Log.e(TAG, "try : " + AppConfig.URL_Add_Details);
                    for (int i = 0; i < Add_Detail_Arr.length(); i++) {
                        try {
                            add_obj = Add_Detail_Arr.getJSONObject(i);
                            String slug = add_obj.getString("slug");
                            String name_j = add_obj.getString("name");
                            String id = add_obj.getString("id");

                            Log.e("details namg....", name_j);

                            AddDetail_List.add(add_obj.getString("name"));
                            Add_Detail Add_Detail_list = new Add_Detail();
                            Add_Detail_list.setName(name_j);
                            Add_Detail_list.setId(id);
                            AddDetailList.add(Add_Detail_list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    detailadapter.notifyDataSetChanged();

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

    private void getLotDetails(final String p_id, final String p_token, final String Auction_name, final String lot_no) {

        pDialog.setMessage("Please Wait ...");
        showDialog();

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_Auction_new : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                // hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);

                try {

                    poDtls = new LotDetailsModel();

                    poDtls.setAuction_name(Auction_name);
                    poDtls.setLot_no(lot_no);
                    poDtls.setScanResult(ScanResult);
                    poDtls.setLot_S(Lot_S);
                    poDtls.setLot_S_o(Lot_S_o);
                    poDtls.setLocalVar(LocalVar);
                    poDtls.setAuction_n_D(Auction_n_D);
                    poDtls.setLot_n_D(Lot_n_D);

                    jObj = new JSONObject(response);
                    String code = jObj.getString("code");
                    poDtls.setCode(code);
                    Log.e("Tag..", code);
                    //lot exists
                    if (code.equals("lot exists")) {
                        Log.e(TAG, "try : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);
                        Log.e(TAG, "try if: " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);

                        Jobj1 = jObj.getJSONObject("lot_data");

                       // auction_no.setText(Auction_name);
                       // lotno.setText(lot_no);
                        LotDetailObj obj = new LotDetailObj();
                        lotDetailsdataL = new LotDetailsData();
                        ArrayList<LotDetailObj>  itemsP = new ArrayList<LotDetailObj>();
                        if (Jobj1.getString("item_title") == "null") {
                            obj.setTitle(Jobj1.getString("item_title"));
                           // _item_title.setText("");

                        } else {
                            obj.setTitle(Jobj1.getString("item_title"));
                          //  _item_title.setText(Jobj1.getString("item_title"));
                        }

                        if (Jobj1.getString("seller_id") == "null") {
                            lotDetailsdataL.setSeller_id(Jobj1.getString("seller_id"));
                           // _seller_id.setText("");
                        } else {
                            lotDetailsdataL.setSeller_id(Jobj1.getString("seller_id"));
                           // _seller_id.setText(Jobj1.getString("seller_id"));
                        }

                        if (Jobj1.getString("item_class") == "null") {
                            lotDetailsdataL.setItem_class(Jobj1.getString("item_class"));
                           // _item_class.setText("");
                        } else {
                            lotDetailsdataL.setItem_class(Jobj1.getString("item_class"));
                           // _item_class.setText(Jobj1.getString("item_class"));
                        }

                        if (Jobj1.getString("color") == "null") {
                            obj.setColor(Jobj1.getString("color"));
                           // _color.setText("");
                        } else {
                            obj.setColor(Jobj1.getString("color"));
                           // _color.setText(Jobj1.getString("color"));
                        }

                        if (Jobj1.getString("model") == "null") {
                            obj.setModel(Jobj1.getString("model"));
                           // _model.setText("");
                        } else {
                            obj.setModel(Jobj1.getString("model"));
                            //_model.setText(Jobj1.getString("model"));
                        }

                        if (Jobj1.getString("brand") == "null") {
                            obj.setBrand(Jobj1.getString("brand"));
                           // _brand.setText("");
                        } else {
                            obj.setBrand(Jobj1.getString("brand"));
                           // _brand.setText(Jobj1.getString("brand"));
                        }

                        if (Jobj1.getString("dimension") == "null") {
                            obj.setDimension(Jobj1.getString("dimension"));
                           // _dimension.setText("");
                        } else {
                            obj.setDimension(Jobj1.getString("dimension"));
                           // _dimension.setText(Jobj1.getString("dimension"));
                        }

                        Catselect = Jobj1.getString("category");
                        Cat_Select_get.add(Catselect);
                        Log.e("Catselect", Catselect);
                        Log.e(TAG, "spin_array3." + modified_response_array);
                        Log.e(TAG, "spin_array3." + Cat_Select_get);
                        Log.e(TAG, "spin_arr3." + spinList);
                        Log.e(TAG, "spin_arr3." + spinList.size());

                        Log.e(TAG, "fgfhgfhjkjk." + Cat_Select_get);
                        if(Cat_Select_get.size() > 0)
                        {
                            for(int s=0; s<Cat_Select_get.size(); s++) {
                                Log.e(TAG, "fjkjk." + Cat_Select_get.get(s));
                                spin_get_value = Cat_Select_get.get(s);
                            }
                            Log.e(TAG, "fgfhk." + spin_get_value);

                            for(int k=0; k<spinList.size(); k++) {
                                Log.e(TAG, "fffff." + spinList.get(k).replace(" - ",""));
                                if (spin_get_value.equals(spinList.get(k).replace(" - ",""))) {
                                    Log.e("back_cat", "f " + spinList.size());
                                    Log.e("back_cat1", spinList.get(k).replace(" - ",""));
                                    Log.e("back_cat2", "hh.." + k);
                                    spn.setSelection(k);
                                }
                                else if (Catselect.equals("Default")) {
                                    String category_Default = "Default";
                                    if (category_Default.equals(spinList.get(k).replace(" - ",""))) {
                                        spn.setSelection(spinList.get(k).replace(" - ","").indexOf("Default"));
                                    }
                                }
                            }
                        }

                        additonl_array = Jobj1.getJSONArray("additional_details_checklist");

                        //lotDetailsdataL.setadditionalList(Jobj1.getJSONArray("additional_details_checklist"));
                        Log.e(TAG, "AddDetailList " + AddDetailList);
                        Log.e(TAG, "additonl_array " + additonl_array);
                        for (int i = 0; i < additonl_array.length(); i++)
                        {
                            additional_details_checklist = additonl_array.getString(i);
                            lotDetailsdataL.setAdditional_details_checklist(additonl_array.getString(i));
                            AddDetail_List_get.add(additional_details_checklist);
                        }
                        //getAddDetailsTest(p_token);

                        lotDetailsdataL.setAdditionalList(AddDetail_List_get);
                        lotDetailsdataL.setAddDetailList(AddDetailList);
                        Log.e("AddDetail_List_get", new Gson().toJson(lotDetailsdataL));
                        if (Jobj1.getString("description") == "null") {
                            descr.setText("");
                            descr.setSelection(descr.length());
                            descr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.des_view, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                    alertDialogBuilder.setView(promptsView);

                                    final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                                    userInput.setText(descr.getText());
                                    userInput.setSelection(userInput.length());
                                    // set dialog message
                                    alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        descr.setText(userInput.getText());
                                                        descr.setSelection(descr.length());
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            });
                        } else {
                            obj.setDescription(Jobj1.getString("description"));
                            //descr.setText(Jobj1.getString("description"));
                            descr.setSelection(descr.length());
                            descr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.des_view, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                    alertDialogBuilder.setView(promptsView);

                                    final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                                    userInput.setText(descr.getText());
                                    userInput.setSelection(userInput.length());
                                    // set dialog message
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            descr.setText(userInput.getText());
                                                            descr.setSelection(descr.length());
                                                        }
                                                    })
                                            .setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            });
                        }
                        if (Jobj1.getString("condition") == "null") {
                            lotDetailsdataL.setCondition(Jobj1.getString("condition"));
                           // _condition.setText("");
                        } else {
                            lotDetailsdataL.setCondition(Jobj1.getString("condition"));
                          //  _condition.setText(Jobj1.getString("condition"));
                        }
                        if (Jobj1.getString("add_detalils") == "null") {
                            lotDetailsdataL.setAdd_detalils(Jobj1.getString("add_detalils"));
                            //_add_detalils.setText("");
                        } else {
                            lotDetailsdataL.setAdd_detalils(Jobj1.getString("add_detalils"));
                            //_add_detalils.setText(Jobj1.getString("add_detalils"));
                        }
                        if (Jobj1.getString("location") == "null") {
                            lotDetailsdataL.setLocation(Jobj1.getString("location"));
                           // _location.setText("");
                        } else {
                            lotDetailsdataL.setLocation(Jobj1.getString("location"));
                           // _location.setText(Jobj1.getString("location"));
                        }
                        if (Jobj1.getString("start_price") == "null") {
                            lotDetailsdataL.setStart_price(Jobj1.getString("start_price"));
                           // _start_price.setText("");
                        } else {
                            lotDetailsdataL.setStart_price(Jobj1.getString("start_price"));
                           // _start_price.setText(Jobj1.getString("start_price"));
                        }

                        ArrayList<LotDetailsOffer>  OffersP = new ArrayList<LotDetailsOffer>();
                        LotDetailsOffer OfferObj = new LotDetailsOffer();

                        if (Jobj1.getString("msrp") == "null") {
                            OfferObj.setPrice("");
                            OffersP.add(OfferObj);
                            _msrp.setText("");

                        } else {

                            OfferObj.setPrice(Jobj1.getString("msrp"));
                            OffersP.add(OfferObj);
                            _msrp.setText(Jobj1.getString("msrp"));
                        }

                        obj.setOffers(OffersP);

                        if (Jobj1.getString("reverse_price") == "null") {
                            lotDetailsdataL.setReverse_price(Jobj1.getString("reverse_price"));
                            //_end_price.setText("");
                        } else {
                            lotDetailsdataL.setReverse_price(Jobj1.getString("reverse_price"));
                           // _end_price.setText(Jobj1.getString("reverse_price"));
                        }
                        if (Jobj1.getString("quantity") == "null") {
                             poDtls.setTotal(Jobj1.getString("quantity"));
                           // _quantity.setText("");
                        } else {
                            poDtls.setTotal(Jobj1.getString("quantity"));
                           // _quantity.setText(Jobj1.getString("quantity"));
                        }

                        if (Jobj1.getString("upc") == "null") {
                            obj.setUpc(Jobj1.getString("upc"));
                           // upcno.setText("");
                        } else {
                            obj.setUpc(Jobj1.getString("upc"));
                           // upcno.setText(Jobj1.getString("upc"));
                        }

                        String RadioValue = Jobj1.getString("textable");
                        Log.e("RRR......."+RadioValue , RadioValue);
                        String YesValue = rdYes.getText().toString();
                        String NoValue  = rdNo.getText().toString();

                        Log.e("hhh", YesValue);
                        Log.e("hhh", NoValue);

                        if (RadioValue.equals(YesValue)) {
                            Log.e("RRR...22...."+RadioValue , RadioValue);
                            rdYes.setChecked(true);
                        }

                        else if(RadioValue.equals(NoValue)) {
                            Log.e("RRR....33..."+RadioValue , RadioValue);
                            rdNo.setChecked(true);
                        }

                        //lotdetailsobj = new LotDetailObj();

                        img_arry_get = Jobj1.getJSONArray("images");
                        Log.e("img_arry_get", ""+img_arry_get.length());

                        if(img_arry_get.length() !=0){
                            extImg.setVisibility(View.VISIBLE);
                        }

                        imagesObj = new ArrayList<String>();

                        for (int i = 0; i < img_arry_get.length(); i++) {
                            try {
                                imagesObj.add(img_arry_get.getString(i));
                                /*images = img_arry_get.getString(i);
                                imagePaths_external.add(img_arry_get.getString(i));
                                arrayList.add(img_arry_get.getString(i));
                                gallary_list_list = new Gallary();
                                gallary_list_list.setImagesUrl(images);
                                gallary_list_list.setImgId(images);
                                gallary_list_list.setImgTitle(images);*/
                                //LotimgList.add(gallary_list_list);
                                Log.e(TAG, "images_get_lot " + images);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        obj.setImages(imagesObj);
                        obj.setLotimgList(LotimgList);
                        Log.e("obj", new Gson().toJson(obj));
                        glryadapter.notifyDataSetChanged();
                        itemsP.add(obj);
                        Log.e("itemsP", new Gson().toJson(itemsP));
                      //  Toast.makeText(Lot_Details_New.this, "Third", Toast.LENGTH_SHORT).show();
                        getAddDetails(p_token);
                        Log.e("AddDetailListtttt", AddDetailList+"");
                        LotDetailServer lotdetailserver = new LotDetailServer();

                        lotdetailserver.setLotdetailsdata(lotDetailsdataL);
                        poDtls.setLotdetailsserver(lotdetailserver);
                        poDtls.setItems(itemsP);
                        Log.e("poDtlsssssss", new Gson().toJson(poDtls));

                        SetProductValue(poDtls, true);

                    } else if (code.equals("lot does not exist")) {

                        getcategorySpin(p_token);

                        auction_no.setText(Auction_name);
                        lotno.setText(lot_no);
                        _item_title.setText("");
                        _seller_id.setText("");
                        _item_class.setText("");
                        descr.setText("");
                        descr.setSelection(descr.length());
                        descr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.des_view, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setView(promptsView);

                                final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                                userInput.setText(descr.getText());
                                userInput.setSelection(userInput.length());
                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        descr.setText(userInput.getText());
                                                        descr.setSelection(descr.length());
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();
                            }
                        });
                        _condition.setText("");
                        _color.setText("");
                        _dimension.setText("");
                        _brand.setText("");
                        _model.setText("");
                        _add_detalils.setText("");
                        _location.setText("");
                        _start_price.setText("");
                        _end_price.setText("");
                        _quantity.setText("");
                      //_textable.setText("");
                        upcno.setText("");
                        _msrp.setText("");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);
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

    private void getLotDetailsD(final String p_id, final String p_token, final String Auction_name, final String lot_no) {

        pDialog.setMessage("Please Wait ...");
        showDialog();

        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_Auction_new : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user details Response: " + response.toString());
                // hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);

                try {

                    poDtls = new LotDetailsModel();

                    poDtls.setAuction_name(Auction_n_D);
                    poDtls.setLot_no(Lot_n_D);
                    poDtls.setScanResult(ScanResult);
                    poDtls.setLot_S(Lot_S);
                    poDtls.setLot_S_o(Lot_S_o);
                    poDtls.setLocalVar(LocalVar);
                    poDtls.setAuction_n_D(Auction_n_D);
                    poDtls.setLot_n_D(Lot_n_D);

                    jObj = new JSONObject(response);
                    String code = jObj.getString("code");
                    poDtls.setCode(code);
                    Log.e("Tag..", code);
                    //lot exists
                    if (code.equals("lot exists")) {
                        Log.e(TAG, "try : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);
                        Log.e(TAG, "try if: " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);

                        Jobj1 = jObj.getJSONObject("lot_data");

                        // auction_no.setText(Auction_name);
                        // lotno.setText(lot_no);
                        LotDetailObj obj = new LotDetailObj();
                        lotDetailsdataL = new LotDetailsData();
                        ArrayList<LotDetailObj>  itemsP = new ArrayList<LotDetailObj>();
                        if (Jobj1.getString("item_title") == "null") {
                            obj.setTitle(Jobj1.getString("item_title"));
                            // _item_title.setText("");

                        } else {
                            obj.setTitle(Jobj1.getString("item_title"));
                            //  _item_title.setText(Jobj1.getString("item_title"));
                        }

                        if (Jobj1.getString("seller_id") == "null") {
                            lotDetailsdataL.setSeller_id(Jobj1.getString("seller_id"));
                            // _seller_id.setText("");
                        } else {
                            lotDetailsdataL.setSeller_id(Jobj1.getString("seller_id"));
                            // _seller_id.setText(Jobj1.getString("seller_id"));
                        }

                        if (Jobj1.getString("item_class") == "null") {
                            lotDetailsdataL.setItem_class(Jobj1.getString("item_class"));
                            // _item_class.setText("");
                        } else {
                            lotDetailsdataL.setItem_class(Jobj1.getString("item_class"));
                            // _item_class.setText(Jobj1.getString("item_class"));
                        }

                        if (Jobj1.getString("color") == "null") {
                            obj.setColor(Jobj1.getString("color"));
                            // _color.setText("");
                        } else {
                            obj.setColor(Jobj1.getString("color"));
                            // _color.setText(Jobj1.getString("color"));
                        }

                        if (Jobj1.getString("model") == "null") {
                            obj.setModel(Jobj1.getString("model"));
                            // _model.setText("");

                        } else {
                            obj.setModel(Jobj1.getString("model"));
                            //_model.setText(Jobj1.getString("model"));
                        }

                        if (Jobj1.getString("brand") == "null") {
                            obj.setBrand(Jobj1.getString("brand"));
                            // _brand.setText("");
                        } else {
                            obj.setBrand(Jobj1.getString("brand"));
                            // _brand.setText(Jobj1.getString("brand"));
                        }

                        if (Jobj1.getString("dimension") == "null") {
                            obj.setDimension(Jobj1.getString("dimension"));
                            // _dimension.setText("");
                        } else {
                            obj.setDimension(Jobj1.getString("dimension"));
                            // _dimension.setText(Jobj1.getString("dimension"));
                        }

                        Catselect = Jobj1.getString("category");
                        Cat_Select_get.add(Catselect);
                        Log.e("Catselect", Catselect);
                        Log.e(TAG, "spin_array3." + modified_response_array);
                        Log.e(TAG, "spin_array3." + Cat_Select_get);
                        Log.e(TAG, "spin_arr3." + spinList);
                        Log.e(TAG, "spin_arr3." + spinList.size());

                        Log.e(TAG, "fgfhgfhjkjk." + Cat_Select_get);
                        if(Cat_Select_get.size() > 0)
                        {
                            for(int s=0; s<Cat_Select_get.size(); s++) {
                                Log.e(TAG, "fjkjk." + Cat_Select_get.get(s));
                                spin_get_value = Cat_Select_get.get(s);
                            }
                            Log.e(TAG, "fgfhk." + spin_get_value);

                            for(int k=0; k<spinList.size(); k++) {
                                Log.e(TAG, "fffff." + spinList.get(k).replace(" - ",""));
                                if (spin_get_value.equals(spinList.get(k).replace(" - ",""))) {
                                    Log.e("back_cat", "f " + spinList.size());
                                    Log.e("back_cat1", spinList.get(k).replace(" - ",""));
                                    Log.e("back_cat2", "hh.." + k);
                                    spn.setSelection(k);
                                }
                                else if (Catselect.equals("Default")) {
                                    String category_Default = "Default";
                                    if (category_Default.equals(spinList.get(k).replace(" - ",""))) {
                                        spn.setSelection(spinList.get(k).replace(" - ","").indexOf("Default"));
                                    }
                                }
                            }
                        }

                        additonl_array = Jobj1.getJSONArray("additional_details_checklist");
                        for (int i = 0; i < additonl_array.length(); i++)
                        {
                            additional_details_checklist = additonl_array.getString(i);
                            Log.e(TAG, "additional_details_checklist " + additional_details_checklist);
                            AddDetail_List_get.add(additional_details_checklist);
                        }

                        Log.e(TAG, "additional_details_checklist " + AddDetail_List_get.toString());
                        //getAddDetailsTest(p_token);

                        if (Jobj1.getString("description") == "null") {
                            descr.setText("");
                            descr.setSelection(descr.length());
                            descr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.des_view, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                    alertDialogBuilder.setView(promptsView);

                                    final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                                    userInput.setText(descr.getText());
                                    userInput.setSelection(userInput.length());
                                    // set dialog message
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            descr.setText(userInput.getText());
                                                            descr.setSelection(descr.length());
                                                        }
                                                    })
                                            .setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            });
                        } else {
                            obj.setDescription(Jobj1.getString("description"));
                            //descr.setText(Jobj1.getString("description"));
                            descr.setSelection(descr.length());
                            descr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.des_view, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                    alertDialogBuilder.setView(promptsView);

                                    final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                                    userInput.setText(descr.getText());
                                    userInput.setSelection(userInput.length());
                                    // set dialog message
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            descr.setText(userInput.getText());
                                                            descr.setSelection(descr.length());
                                                        }
                                                    })
                                            .setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            });
                        }
                        if (Jobj1.getString("condition") == "null") {
                            lotDetailsdataL.setCondition(Jobj1.getString("condition"));
                            // _condition.setText("");
                        } else {
                            lotDetailsdataL.setCondition(Jobj1.getString("condition"));
                            //  _condition.setText(Jobj1.getString("condition"));
                        }
                        if (Jobj1.getString("add_detalils") == "null") {
                            lotDetailsdataL.setAdd_detalils(Jobj1.getString("add_detalils"));
                            //_add_detalils.setText("");
                        } else {
                            lotDetailsdataL.setAdd_detalils(Jobj1.getString("add_detalils"));
                            //_add_detalils.setText(Jobj1.getString("add_detalils"));
                        }
                        if (Jobj1.getString("location") == "null") {
                            lotDetailsdataL.setLocation(Jobj1.getString("location"));
                            // _location.setText("");
                        } else {
                            lotDetailsdataL.setLocation(Jobj1.getString("location"));
                            // _location.setText(Jobj1.getString("location"));
                        }
                        if (Jobj1.getString("start_price") == "null") {
                            lotDetailsdataL.setStart_price(Jobj1.getString("start_price"));
                            // _start_price.setText("");
                        } else {
                            lotDetailsdataL.setStart_price(Jobj1.getString("start_price"));
                            // _start_price.setText(Jobj1.getString("start_price"));
                        }

                        ArrayList<LotDetailsOffer>  OffersP = new ArrayList<LotDetailsOffer>();
                        LotDetailsOffer OfferObj = new LotDetailsOffer();

                        if (Jobj1.getString("msrp") == "null") {
                            OfferObj.setPrice("");
                            OffersP.add(OfferObj);
                            _msrp.setText("");

                        } else {

                            OfferObj.setPrice(Jobj1.getString("msrp"));
                            OffersP.add(OfferObj);
                            _msrp.setText(Jobj1.getString("msrp"));
                        }

                        obj.setOffers(OffersP);

                        if (Jobj1.getString("reverse_price") == "null") {
                            lotDetailsdataL.setReverse_price(Jobj1.getString("reverse_price"));
                            //_end_price.setText("");
                        } else {
                            lotDetailsdataL.setReverse_price(Jobj1.getString("reverse_price"));
                            // _end_price.setText(Jobj1.getString("reverse_price"));
                        }
                        if (Jobj1.getString("quantity") == "null") {
                            poDtls.setTotal(Jobj1.getString("quantity"));
                            // _quantity.setText("");
                        } else {
                            poDtls.setTotal(Jobj1.getString("quantity"));
                            // _quantity.setText(Jobj1.getString("quantity"));
                        }

                        if (Jobj1.getString("upc") == "null") {
                            obj.setUpc(Jobj1.getString("upc"));
                            // upcno.setText("");
                        } else {
                            obj.setUpc(Jobj1.getString("upc"));
                            // upcno.setText(Jobj1.getString("upc"));
                        }

                        String RadioValue = Jobj1.getString("textable");
                        Log.e("RRR......."+RadioValue , RadioValue);
                        String YesValue = rdYes.getText().toString();
                        String NoValue  = rdNo.getText().toString();

                        Log.e("hhh", YesValue);
                        Log.e("hhh", NoValue);

                        if (RadioValue.equals(YesValue)) {
                            Log.e("RRR...22...."+RadioValue , RadioValue);
                            rdYes.setChecked(true);
                        }

                        else if(RadioValue.equals(NoValue)) {
                            Log.e("RRR....33..."+RadioValue , RadioValue);
                            rdNo.setChecked(true);
                        }

                        //lotdetailsobj = new LotDetailObj();

                        img_arry_get = Jobj1.getJSONArray("images");
                        Log.e("img_arry_get", ""+img_arry_get.length());

                        if(img_arry_get.length() !=0){
                            extImg.setVisibility(View.VISIBLE);
                        }

                        imagesObj = new ArrayList<String>();

                        for (int i = 0; i < img_arry_get.length(); i++) {
                            try {
                                imagesObj.add(img_arry_get.getString(i));
                               /* images = img_arry_get.getString(i);
                                imagePaths_external.add(img_arry_get.getString(i));
                                arrayList.add(img_arry_get.getString(i));
                                gallary_list_list = new Gallary();
                                gallary_list_list.setImagesUrl(images);
                                gallary_list_list.setImgId(images);
                                gallary_list_list.setImgTitle(images);*/
                                //LotimgList.add(gallary_list_list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        obj.setImages(imagesObj);
                        obj.setLotimgList(LotimgList);
                        Log.e("obj", new Gson().toJson(obj));
                        glryadapter.notifyDataSetChanged();
                        itemsP.add(obj);
                        Log.e("itemsP", new Gson().toJson(itemsP));
                        //Toast.makeText(Lot_Details.this, "Third", Toast.LENGTH_SHORT).show();
                        getAddDetails(p_token);

                        LotDetailServer lotdetailserver = new LotDetailServer();


                        lotdetailserver.setLotdetailsdata(lotDetailsdataL);
                        poDtls.setLotdetailsserver(lotdetailserver);
                        poDtls.setItems(itemsP);
                        Log.e("poDtlsssssss", new Gson().toJson(poDtls));

                        SetProductValue(poDtls, true);

                    } else if (code.equals("lot does not exist")) {

                        getcategorySpin(p_token);
                        auction_no.setText(Auction_name);
                        lotno.setText(lot_no);
                        _item_title.setText("");
                        _seller_id.setText("");
                        _item_class.setText("");
                        descr.setText("");
                        descr.setSelection(descr.length());
                        descr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.des_view, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setView(promptsView);

                                final EditText userInput = (EditText)promptsView.findViewById(R.id.descralert);
                                userInput.setText(descr.getText());
                                userInput.setSelection(userInput.length());
                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        descr.setText(userInput.getText());
                                                        descr.setSelection(descr.length());
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();
                            }
                        });
                        _condition.setText("");
                        _color.setText("");
                        _dimension.setText("");
                        _brand.setText("");
                        _model.setText("");
                        _add_detalils.setText("");
                        _location.setText("");
                        _start_price.setText("");
                        _end_price.setText("");
                        _quantity.setText("");
                        //_textable.setText("");
                        upcno.setText("");
                        _msrp.setText("");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_lot_details + Auction_name + "&lot_no=" + lot_no);
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

    public class AddDetailsAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<Add_Detail> AddDetailList;
        private int test = 0;
        private String Auct_name;
        private String thumb;
        private TextView spinvalue;
        private TextView name;
        private ArrayAdapter<String> Values;

        public AddDetailsAdapter(Activity activity, ArrayList<Add_Detail> AddDetailList) {
            this.activity = activity;
            this.AddDetailList = AddDetailList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return AddDetailList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int location) {
            return AddDetailList.get(location);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.add_detail_list, parent, false);
            }

            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            checkBox.setTypeface(typeface);

            Add_Detail add_d = AddDetailList.get(position);
            checkBox.setText(add_d.getName());
            checkBox.setChecked(add_d.isSelected());
            checkBox.setTag(add_d);

            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    ArrayList<Add_Detail> countryList = detailadapter.AddDetailList;
                    Log.e("aaaaaaaaa", "" + detailsSelectlist_back.toString());
                    Log.e("bbbbbbbb", "" + AddDetail_List_get.toString());

                    if (detailsSelectlist_back.size() == 0) {
                        detailsSelectlist = new ArrayList<String>();
                        Add_Detail add_d = (Add_Detail)cb.getTag();
                        Log.e("add_d", "" + add_d.selected);
                        add_d.setSelected(cb.isChecked());

                        for (int j = 0; j < countryList.size(); j++) {
                            Log.e("add_d", "" + add_d.selected);
                            add_d.setSelected(cb.isChecked());
                            Add_Detail add_ss = countryList.get(j);
                            if (add_ss.isSelected()) {
                                detailsSelectlist.add(add_ss.getName());
                                Log.e("add_d", "" + add_ss.getName());
                            }
                            else if(!add_ss.isSelected())
                            {
                                Log.e("8888888",AddDetail_List_get.toString());
                                for (int k = 0; k < AddDetail_List_get.size(); k++) {
                                    Log.e("666666", AddDetail_List_get.get(k));
                                    Add_Detail add_dd = AddDetailList.get(position);
                                    //checkBox.setText(add_dd.getName());
                                    if (checkBox.getText().toString().equals(AddDetail_List_get.get(k))) {
                                        Log.e("ggddddd", "" + add_d.selected);
                                        Log.e("collCollectffff", "...." + detailsSelectlist);
                                        detailsSelectlist.remove(checkBox.getText().toString());
                                        AddDetail_List_get.remove(checkBox.getText().toString());
                                        Log.e("collCollect", "...." + detailsSelectlist);
                                    }
                                }
                            }
                        }

                        if (AddDetail_List_get.size() > 0) {

                            Log.e("second....back", "...." + AddDetail_List_get.toString());
                            detailsSelectlist = new ArrayList<String>();
                            detailsSelectlist.addAll(AddDetail_List_get);
                            Log.e("new", "N.."+detailsSelectlist.toString());
                            Log.e("size", "s,.." + countryList.size());
                            for (int j = 0; j < countryList.size(); j++) {
                                Log.e("add_d", "" + add_d.selected);
                                add_d.setSelected(cb.isChecked());
                                Add_Detail add_ss = countryList.get(j);
                                if (add_ss.isSelected()) {
                                    Log.e("ggmaing", ".."+add_d.isSelected());
                                    detailsSelectlist.add(add_ss.getName());
                                }
                                else if(!add_ss.isSelected())
                                {
                                    for (int k = 0; k < AddDetail_List_get.size(); k++) {
                                        Log.e("AddDetailmmmmm", AddDetail_List_get.get(k));
                                        Add_Detail add_dd = AddDetailList.get(position);
                                        Log.e("..........",add_dd.getName());
                                        Log.e("..........",checkBox.getText().toString());
                                        if (add_dd.getName().equals(AddDetail_List_get.get(k))) {
                                            Log.e("AddDetailllllllllllllll", "" + add_d.selected);
                                            Log.e("detailsSelecttttttttt", "...." + detailsSelectlist);
                                            detailsSelectlist.remove(add_dd.getName());
                                            AddDetail_List_get.remove(add_dd.getName());
                                            Log.e("collCollect", "...." + detailsSelectlist);
                                        }
                                    }
                                }
                            }
                            Log.e("collCollect", "...." + detailsSelectlist.toString());
                        }
                        else {
                            detailsSelectlist = new ArrayList<String>();
                            Log.e("size", "s,.." + countryList.size());
                            StringBuffer responseText = new StringBuffer();
                            for (int j = 0; j < countryList.size(); j++) {
                                Log.e("add_d", "" + add_d.selected);
                                add_d.setSelected(cb.isChecked());
                                Add_Detail add_ss = countryList.get(j);
                                if (add_ss.isSelected()) {
                                    responseText.append(add_ss.getName());
                                    detailsSelectlist.add(add_ss.getName());
                                }
                            }

                            Log.e("collCollect", "...." + detailsSelectlist.toString());
                        }
                    }

                    else if (detailsSelectlist_back.size() > 0) {

                        Log.e("second,...back", "...." + detailsSelectlist_back.toString());
                        detailsSelectlist = new ArrayList<String>();
                        detailsSelectlist.addAll(detailsSelectlist_back);

                        Log.e("new", "N.."+detailsSelectlist.toString());
                        Log.e("size", "s,.." + countryList.size());
                        for (int j = 0; j < countryList.size(); j++) {
                            Add_Detail add_d = (Add_Detail) cb.getTag();
                            Log.e("add_d", "" + add_d.selected);
                            add_d.setSelected(cb.isChecked());
                            Add_Detail add_ss = countryList.get(j);
                            if (add_ss.isSelected()) {
                                Log.e("ggmaing", ".."+add_d.isSelected());
                                detailsSelectlist.add(add_ss.getName());
                            }
                            else if(!add_ss.isSelected())
                            {
                                for (int k = 0; k < detailsSelectlist_back.size(); k++) {
                                    Log.e("dd.get(k)", detailsSelectlist_back.get(k));
                                    Add_Detail add_dd = AddDetailList.get(position);
                                    // checkBox.setText(add_dd.getName());
                                    if (add_dd.getName().equals(detailsSelectlist_back.get(k))) {
                                        Log.e("ggg", "" + add_d.selected);
                                        Log.e("collCollect", "...." + detailsSelectlist);
                                        detailsSelectlist.remove(add_dd.getName());
                                        detailsSelectlist_back.remove(add_dd.getName());
                                        Log.e("collCollect", "...." + detailsSelectlist);
                                    }
                                }
                            }
                        }

                        Log.e("collCollect", "...." + detailsSelectlist.size());
                        Log.e("collCollect", "...." + detailsSelectlist.toString());
                    }
                }
            });

            //Log.e(TAG, "checked_value " + AddDetail_List_get.toString());
            if (AddDetail_List_get.size() > 0) {
                for (int k = 0; k < AddDetail_List_get.size(); k++) {
                    Log.e("detailsk.get(k)", AddDetail_List_get.get(k));
                    if (checkBox.getText().toString().equals(AddDetail_List_get.get(k))) {
                        ArrayList<Add_Detail> countryList = detailadapter.AddDetailList;
                        Log.e("size", "s,.." + countryList.size());
                        checkBox.setTag(add_d);
                        checkBox.setChecked(!add_d.isSelected());
                        Log.e("ggg", "" + !add_d.isSelected());
                    }
                }
            }

            Log.e("dfgfdgf", "...." + detailsSelectlist_back);
            Log.e("checkgggvalie", "...." + add_d.isSelected());
            Log.e("checkvalie", "...." + checkBox.getText().toString());
            if (detailsSelectlist_back.size() > 0) {
                for (int k = 0; k < detailsSelectlist_back.size(); k++) {
                    Log.e("detailsk.get(k)", detailsSelectlist_back.get(k));
                    if (checkBox.getText().toString().equals(detailsSelectlist_back.get(k))) {
                        ArrayList<Add_Detail> countryList = detailadapter.AddDetailList;
                        Log.e("size", "s,.." + countryList.size());
                        checkBox.setTag(add_d);
                        checkBox.setChecked(!add_d.isSelected());
                        Log.e("ggg", "" + !add_d.isSelected());
                    }
                }
            }
            return view;
        }

        public ArrayList<String> getList() {
            return detailsSelectlist;
        }

        public ArrayList<Add_Detail> getAllData() {
            return AddDetailList;
        }

        ArrayList<Add_Detail> getBox() {
            ArrayList<Add_Detail> AddList = new ArrayList<Add_Detail>();
            for (Add_Detail c : AddDetailList) {
                if (c.AddList)
                    AddList.add(c);
            }
            return AddList;
        }
    }

    private void SetProductValue(LotDetailsModel poDtls, Boolean isServer) {

        poDtls.getTotal();
        poDtls.getAuction_name();
        poDtls.getLocalVar();
        poDtls.getLot_no();
        poDtls.getLot_n_D();
        poDtls.getLot_S_o();
        poDtls.getAuction_n_D();
        poDtls.getScanResult();
        poDtls.getLot_S();

        Log.e("Second",  poDtls.getAuction_name()+" "+poDtls.getLot_no());
        auction_no.setText(poDtls.getAuction_name());
        lotno.setText(poDtls.getLot_no());
        _quantity.setText(poDtls.getTotal());

        if(lotdetailsobj == null)
        {
             //Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();
            _item_title.setText(poDtls.getItems().get(0).getTitle());
            descr.setText(poDtls.getItems().get(0).getDescription());
            upcno.setText(poDtls.getItems().get(0).getUpc());
            _color.setText(poDtls.getItems().get(0).getColor());
            _brand.setText(poDtls.getItems().get(0).getBrand());
            _dimension.setText(poDtls.getItems().get(0).getDimension());
            _model.setText(poDtls.getItems().get(0).getModel());
        }
        else {
            //Toast.makeText(context, "Not Null", Toast.LENGTH_SHORT).show();
            _item_title.setText(lotdetailsobj.getTitle());
            descr.setText(lotdetailsobj.getDescription());
            upcno.setText(lotdetailsobj.getUpc());
            _color.setText(lotdetailsobj.getColor());
            _brand.setText(lotdetailsobj.getBrand());
            _dimension.setText(lotdetailsobj.getDimension());
            _model.setText(lotdetailsobj.getModel());
        }

        Log.e("ModelBack", new Gson().toJson(poDtls));
        Log.e("ModelBack", new Gson().toJson(lotdetailsobj));
        Log.e("lotDetailsdataL", new Gson().toJson(lotDetailsdataL));
        Log.e("ModelBack", isServer+"");
        Log.e("lotDetailsdataLfdgfgh", new Gson().toJson(lotDetailsdataL));

        if(lotDetailsdataL != null)
        {
            _seller_id.setText(lotDetailsdataL.getSeller_id() != null ? lotDetailsdataL.getSeller_id() : " ");
            _condition.setText(lotDetailsdataL.getCondition() != null ? lotDetailsdataL.getCondition() : " ");
            _item_class.setText(lotDetailsdataL.getItem_class() != null ? lotDetailsdataL.getItem_class() : " ");
            _add_detalils.setText(lotDetailsdataL.getAdd_detalils() != null ? lotDetailsdataL.getAdd_detalils() : " ");
            _location.setText(lotDetailsdataL.getLocation() != null ? lotDetailsdataL.getLocation() : " ");
            _start_price.setText(lotDetailsdataL.getStart_price() != null ? lotDetailsdataL.getStart_price() : " ");
            _end_price.setText(lotDetailsdataL.getReverse_price() != null ? lotDetailsdataL.getReverse_price() : " ");

            if(lotDetailsdataL.getAdditionalList() != null)
            {
                for (int i = 0; i < lotDetailsdataL.getAdditionalList().size(); i++)
                {
                    additional_details_checklist = lotDetailsdataL.getAdditionalList().get(i);
                }
                Log.e("add_dets_checkffff", additional_details_checklist);
            }
        }

        if(!isServer) {
           // Toast.makeText(context, "Hellow", Toast.LENGTH_SHORT).show();
            LotDetailsData current = poDtls.getLotdetailsserver() != null ? poDtls.getLotdetailsserver().getLotdetailsdata() : null;
            _seller_id.setText(current.getSeller_id() != null ? current.getSeller_id() : " ");
            _condition.setText(current.getCondition() != null ? current.getCondition() : " ");
            _item_class.setText(current.getItem_class() != null ? current.getItem_class() : " ");
            _add_detalils.setText(current.getAdd_detalils() != null ? current.getAdd_detalils() : " ");
            _location.setText(current.getLocation() != null ? current.getLocation() : " ");
            _start_price.setText(current.getStart_price() != null ? current.getStart_price() : " ");
            _end_price.setText(current.getReverse_price() != null ? current.getReverse_price() : " ");
            _msrp.setText(current.getMsrp() != null ? current.getMsrp() : "00.00");

            if(rdYes.getText().toString().equals(current.getTextable()))
            {
                rdYes.setChecked(true);
            }
            else if(rdNo.getText().toString().equals(current.getTextable()))
            {
                rdNo.setChecked(true);
            }

            Cat_Select_get.add(current.getCategory());
            Log.e("Spin_Select", current.getSpinselect()+"");
            spinList = current.getSpinList();
            spinadapter         = new SpinnerAdapter(Lot_Details_New.this, spinList);
            spn.setAdapter(spinadapter);
            spn.setSelection(current.getSpinselect());

            ArrayList<String> Add_All = new ArrayList<String>();
            Log.e("lotdeta", new Gson().toJson(current));
            Log.e("Add3", current.getAdditional_details_checklist());
            for (int a = 0; a < current.getAddDetailList().size(); a++) {
                Log.e("Add4", current.getAddDetailList()+"");
                Log.e("Add5", current.getAddDetailList().get(a).getName()+"");
                Add_All.add(current.getAddDetailList().get(a).getName());
            }

            Log.e("Add6",Add_All+"");
            AddDetailList = current.getAddDetailList();
            Log.e("AddDetailList",new Gson().toJson(AddDetailList));
            detailadapter       = new AddDetailsAdapter(Lot_Details_New.this, AddDetailList);
            list.setAdapter(detailadapter);

            additional_details_checklist = current.getAdditional_details_checklist();
            Log.e("add_dets_check", additional_details_checklist);

            dataT = current.getDataT();
           // LotimgList = current.get

            Log.e("dataTGallery..", dataT+"");
            for(int i=0; i<dataT.size(); i++){
                String dd = dataT.get(i).sdcardPath;
                Log.e("sfdsfddf", dd);
                imagePaths.add(dataT.get(i).sdcardPath);
            }

            Log.e("imagePaths",imagePaths+"");
            Log.e("Images_Camera..", Images_Camera+"");
            Log.e("Images_Camera..", poDtls.getCam_Images()+"");
            for (int c = 0; c < poDtls.getCam_Images().size(); c++) {
                item = new CustomGallery();
                item.sdcardPath = poDtls.getCam_Images().get(c).toString();
                imagePaths_camera.add(poDtls.getCam_Images().get(c).toString());
                dataCam.add(item);
            }

            Log.e("imagePaths",imagePaths+"");

            ArrayList<String> dataCamString = new ArrayList<String>();
            Set<String> unique = new HashSet<String>();
            for(int i =0; i<dataCam.size(); i++) {
                Log.e("datahhhhh", dataCam.get(i)+"");
                Log.e("datahhhhh", dataCam.get(i).sdcardPath+"");
                unique.add(dataCam.get(i).sdcardPath+":"+(dataCam.get(i).isSeleted?"0":"1"));
            }

            Log.e("data11", unique+"");
            dataCamString.addAll(unique);
            Log.e("data1", dataCamString+"");

            for(int j =0; j<dataCamString.size();j++) {
                String[] data = dataCamString.get(j).split(":");
                Log.e("data", dataCamString.get(j)+"");
                CustomGallery item1 = new CustomGallery();
                item1.sdcardPath = data[0];
                item1.isSeleted = data[1].equals("0");
                item1.setIdx(CustomGallery.index);
                CustomGallery.index++;
                Log.e("Index", ""+CustomGallery.index);
                Log.e("dataaaa", data[0]);
                imagePaths_camera.add(data[0]);
                Log.e("New ",item.sdcardPath +" ..... "+CustomGallery.index);
                Log.e("New ",item.sdcardPath +" ..... "+item.getIdx());
            }

            if(dataCam.size() != 0){
                glryImg.setVisibility(View.VISIBLE);
            }

            Log.e("dataCamera", dataCam+"");
            Log.e("dataGallery", dataT+"");

            //here
            adapterCamera.addAll(dataCam);
            adapterCamera.notifyDataSetChanged();
            adapter.addAll(dataT);
            adapter.notifyDataSetChanged();

            gridGallery.requestFocus();
            gridGalleryCamera.requestFocus();
            String Mfile_string = poDtls.getMFile();
            Mfile = new File(Mfile_string);
            Log.e("MfileCam.", Mfile+"");
            Log.e("lotdetailsobj ", new Gson().toJson(lotdetailsobj));
            Log.e("lotdetailsobj ", new Gson().toJson(lotdetailsobj.getLotimgList().size()));
        }
        Log.e("lotdetailsobjdvfgvfg", new Gson().toJson(lotdetailsobj));

        if(lotdetailsobj == null)
        {
            ArrayList<String> itemsToAdd = new ArrayList<String>();
            Log.e("poDtls.getItems()",""+poDtls.getItems().get(0).getOffers());
            if(poDtls.getItems().get(0).getOffers()== null) {}
            else{
                for (int k = 0; k < poDtls.getItems().get(0).getOffers().size(); k++) {
                    LotDetailsOffer OfferObj = poDtls.getItems().get(0).getOffers().get(k);
                    String price = OfferObj.getPrice();
                    Log.e("MSRP.." + (k), price);
                    itemsToAdd.add(OfferObj.getPrice());
                }
                if(itemsToAdd.size() == 0)
                {
                    _msrp.setText(""+00.00);
                }
                else {
                    String Msrp = Collections.max(itemsToAdd);
                    _msrp.setText(Msrp);
                }
            }
        }
        else if(lotdetailsobj.getOffers() != null)
        {
            ArrayList<String> itemsToAdd = new ArrayList<String>();
            for (int k = 0; k < lotdetailsobj.getOffers().size(); k++) {
                LotDetailsOffer OfferObj = lotdetailsobj.getOffers().get(k);
                String price = OfferObj.getPrice();
                Log.e("MSRP.." + (k), price);
                itemsToAdd.add(OfferObj.getPrice());
            }

            Log.e("MSRP", ""+itemsToAdd.toString());
            Log.e("MSRP_size", ""+itemsToAdd.size());
            if(itemsToAdd.size() == 0)
            {
                _msrp.setText(""+00.00);
            }
            else {
                String Msrp = Collections.max(itemsToAdd);
                _msrp.setText(Msrp);
            }
        }
        if(lotdetailsobj == null)
        {
            //Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ImageSetProductValueHI " + poDtls.getItems().get(0).getImages());

            if(poDtls.getItems().get(0).getImages() == null) {}
            else
            {
                for (int l = 0; l < poDtls.getItems().get(0).getImages().size(); l++) {
                    String Image = poDtls.getItems().get(0).getImages().get(l);
                    imagePaths_external.add(Image);
                    arrayList.add(Image);
                    gallary_list_list = new Gallary();
                    gallary_list_list.setImagesUrl(Image);
                    gallary_list_list.setImgId(Image);
                    gallary_list_list.setImgTitle(Image);
                    LotimgList.add(gallary_list_list);
                }
            }

            glryadapter.notifyDataSetChanged();

            if(imagePaths_external.size() != 0){
                extImg.setVisibility(View.VISIBLE);
            }
            Log.e(TAG, " LotimgList has : " + LotimgList);
            Log.e(TAG, " imagePaths_external has : " + imagePaths_external);
            Log.e(TAG, " arrayList has : " + arrayList);
        }
        else if(lotdetailsobj != null)
        {
            Log.e(TAG, "ImageSetProductValueHI " + lotdetailsobj.getLotimgList());
            Log.e(TAG, "ImageSetProductValueHI " + lotdetailsobj.getLotimgList().size());
            for (int l = 0; l < lotdetailsobj.getLotimgList().size(); l++) {
                String Image = lotdetailsobj.getLotimgList().get(l).getImgId();
                Log.e(TAG, "Image " + Image);
                imagePaths_external.add(Image);
                arrayList.add(Image);
                gallary_list_list = new Gallary();
                gallary_list_list.setImagesUrl(Image);
                gallary_list_list.setImgId(Image);
                gallary_list_list.setImgTitle(Image);
                LotimgList.add(gallary_list_list);
            }
            glryadapter.notifyDataSetChanged();

            if(imagePaths_external.size() != 0){
                extImg.setVisibility(View.VISIBLE);
            }

            Log.e(TAG, " LotimgList has : " + LotimgList);
            Log.e(TAG, " imagePaths_external has : " + imagePaths_external);
            Log.e(TAG, " arrayList has : " + arrayList);
        }
        else if(lotdetailsobj.getImages() != null)
        {
           // Toast.makeText(context, "Hiiiiiiiiiiiiiiii", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ImageSetProductValue " + lotdetailsobj.getLotimgList());
            imagesObj = lotdetailsobj.getImages();
            Log.e(TAG, "ImageSetProductValue " + imagesObj);
            Log.e(TAG, "ImageSetProductValue " + lotdetailsobj.getImages());

            for (int l = 0; l < lotdetailsobj.getImages().size(); l++) {
                String Image = lotdetailsobj.getImages().get(l);
                imagePaths_external.add(Image);
                arrayList.add(Image);
                gallary_list_list = new Gallary();
                gallary_list_list.setImagesUrl(Image);
                gallary_list_list.setImgId(Image);
                gallary_list_list.setImgTitle(Image);
                LotimgList.add(gallary_list_list);
            }
            glryadapter.notifyDataSetChanged();

            Log.e(TAG, " LotimgList has : " + LotimgList);
            Log.e(TAG, " imagePaths_external has : " + imagePaths_external);
            Log.e(TAG, " arrayList has : " + arrayList);

            if(imagePaths_external.size() != 0){
                extImg.setVisibility(View.VISIBLE);
            }
        }
    }

    private void GetUpdatedProductValue(LotDetailsModel poDtls) {

        if(poDtls != null) {

           // Toast.makeText(context, "Get Not null", Toast.LENGTH_SHORT).show();
            Log.e("Modele",new Gson().toJson(poDtls));
            Log.e("Second1", auction_no.getText().toString()+" "+lotno.getText().toString());
            poDtls.setAuction_name(auction_no.getText().toString());
            poDtls.setLot_no(lotno.getText().toString());
            poDtls.setTotal(_quantity.getText().toString());
            Log.e("fgfdgfgfh", new Gson().toJson(poDtls));
            lotdetailsobj = new LotDetailObj();
            Log.e("zxcxc", new Gson().toJson(lotdetailsobj));
            lotdetailsobj = poDtls.getItems().get(0);
            Log.e("lotdetailsobj", new Gson().toJson(lotdetailsobj));
            Log.e("lotdetailsobj", _item_title.getText().toString());
            Log.e("lotdetailsobj", poDtls.getItems().get(0).toString());
            Log.e("Imgesssss", poDtls.getItems().get(0).getImages()+"");
            Log.e("Imgesssss", lotdetailsobj.getLotimgList()+"");

            lotdetailsobj.setTitle(_item_title.getText().toString());
            lotdetailsobj.setDescription(descr.getText().toString());
            lotdetailsobj.setUpc(upcno.getText().toString());
            lotdetailsobj.setColor(_color.getText().toString());
            lotdetailsobj.setBrand(_brand.getText().toString());
            lotdetailsobj.setDimension(_dimension.getText().toString());
            lotdetailsobj.setModel(_model.getText().toString());
            lotdetailsobj.setLotimgList(LotimgList);
            lotdetailsobj.setImages(imagesObj);

            ArrayList<LotDetailObj> LotDetailObjMore = new ArrayList<LotDetailObj>();
            Log.e("LotDetailObjMore", new Gson().toJson(LotDetailObjMore));
            LotDetailObjMore.add(lotdetailsobj);
            Log.e("LotDetailObjMore", new Gson().toJson(LotDetailObjMore));
            poDtls.setItems(LotDetailObjMore);

            LotDetailsData lotdetaildata = new LotDetailsData();
            lotdetaildata.setSeller_id(_seller_id.getText().toString());
            lotdetaildata.setItem_class(_item_class.getText().toString());
            lotdetaildata.setLocation(_location.getText().toString());
            lotdetaildata.setStart_price(_start_price.getText().toString());
            lotdetaildata.setCondition(_condition.getText().toString());
            lotdetaildata.setAdd_detalils(_add_detalils.getText().toString());
            lotdetaildata.setMsrp(_msrp.getText().toString());
            lotdetaildata.setReverse_price(_end_price.getText().toString());

            int selectedId = radioTax.getCheckedRadioButtonId();
            final RadioButton radioButton;
            radioButton = (RadioButton) findViewById(selectedId);
            textable = radioButton.getText().toString();
            lotdetaildata.setTextable(textable);
            Log.e("spinListBack", spinList+"");
            Log.e("modified_array", modified_response_array.toString());

            lotdetaildata.setSpinList(spinList);
            category        = Spin_select_value;
            int cat_position = spn.getSelectedItemPosition();
            lotdetaildata.setSpinselect(cat_position);
            lotdetaildata.setCategory(category);
            if(detailsSelectlist.size() == 0){
                Log.e(TAG, "ms0 " + AddDetail_List_get.toString());
                detailsSelectlist = AddDetail_List_get;
                Log.e("AddDetailList", AddDetailList+"");
                Log.e("AddDetailList", AddDetailList.size()+"");
                for(int i = 0; i< AddDetailList.size(); i++)
                {
                    Log.e("AddDetailList",  AddDetailList.get(i).getName());
                    Log.e("AddDetailList",  AddDetail_List_get+"");

                    for(int j = 0; j< AddDetail_List_get.size(); j++) {
                        if(AddDetailList.get(i).getName().equals(AddDetail_List_get.get(j)))
                        {
                            AddDetailList.get(i).selected = true;
                        }
                    }
                }
            }
            else
            {
                Log.e(TAG, "ms1 " + detailsSelectlist.toString());
            }

            Log.e("multiple_select", new Gson().toJson(AddDetailList));
            Log.e(TAG, "multiple_select " + detailsSelectlist.toString());

            lotdetaildata.setAddDetailList(AddDetailList);
            additional_details_checklist = detailsSelectlist.toString();
            additional_details_checklist = additional_details_checklist.replaceAll("\\[", "");
            additional_details_checklist = additional_details_checklist.replaceAll("\\]", "");

            Log.e("add_dets_check", additional_details_checklist);
            lotdetaildata.setAdditional_details_checklist(additional_details_checklist);

            Log.e("dataTA", dataT+"");
            lotdetaildata.setDataT(dataT);

            LotDetailServer lotdetailserver = new LotDetailServer();
            lotdetailserver.setLotdetailsdata(lotdetaildata);

            Log.e("Modeleee", new Gson().toJson(lotdetailserver));
            poDtls.setLotdetailsserver(lotdetailserver);
            Log.e("Modeleee", new Gson().toJson(poDtls));

            lot_no          = lotno.getText().toString();
            Auction_name    = auction_no.getText().toString();
            Log.e("Second11", lot_no+" "+Auction_name);
        }
        else {
          //  Toast.makeText(context, "Get Null", Toast.LENGTH_SHORT).show();
            poDtls = new LotDetailsModel();
            poDtls.setAuction_name(auction_no.getText().toString());
            poDtls.setLot_no(lotno.getText().toString());
            poDtls.setTotal(_quantity.getText().toString());

            lotdetailsobj = new LotDetailObj();
            lotdetailsobj.setTitle(_item_title.getText().toString());
            lotdetailsobj.setDescription(descr.getText().toString());
            lotdetailsobj.setUpc(upcno.getText().toString());
            lotdetailsobj.setColor(_color.getText().toString());
            lotdetailsobj.setBrand(_brand.getText().toString());
            lotdetailsobj.setDimension(_dimension.getText().toString());
            lotdetailsobj.setModel(_model.getText().toString());
            lotdetailsobj.setLotimgList(LotimgList);
            lotdetailsobj.setImages(imagesObj);

            ArrayList<LotDetailObj> LotDetailObjMore = new ArrayList<LotDetailObj>();
            LotDetailObjMore.add(lotdetailsobj);
            poDtls.setItems(LotDetailObjMore);
            Log.e("LotDetailObjMore", new Gson().toJson(LotDetailObjMore));
            LotDetailsData lotdetaildata = new LotDetailsData();

            lotdetaildata.setSeller_id(_seller_id.getText().toString());
            lotdetaildata.setItem_class(_item_class.getText().toString());
            lotdetaildata.setLocation(_location.getText().toString());
            lotdetaildata.setStart_price(_start_price.getText().toString());
            lotdetaildata.setCondition(_condition.getText().toString());
            lotdetaildata.setAdd_detalils(_add_detalils.getText().toString());
            lotdetaildata.setMsrp(_msrp.getText().toString());
            lotdetaildata.setReverse_price(_end_price.getText().toString());

            int selectedId = radioTax.getCheckedRadioButtonId();
            final RadioButton radioButton;
            radioButton = (RadioButton) findViewById(selectedId);

            textable = radioButton.getText().toString();

            lotdetaildata.setTextable(textable);
            Log.e("spinListBack", spinList+"");
            Log.e("modified_array", modified_response_array.toString());

            lotdetaildata.setSpinList(spinList);
            category        = Spin_select_value;
            int cat_position = spn.getSelectedItemPosition();
            lotdetaildata.setSpinselect(cat_position);
            lotdetaildata.setCategory(category);

            if(detailsSelectlist.size() == 0){
                Log.e(TAG, "ms0 " + AddDetail_List_get.toString());
                detailsSelectlist = AddDetail_List_get;
            }
            else
            {
                Log.e(TAG, "ms1 " + detailsSelectlist.toString());
                Log.e("AddDetailList", AddDetailList+"");
                Log.e("AddDetailList", AddDetailList.size()+"");
                for(int i = 0; i< AddDetailList.size(); i++)
                {
                    Log.e("AddDetailList",  AddDetailList.get(i).getName());
                    Log.e("AddDetailList",  AddDetail_List_get+"");

                    for(int j = 0; j< AddDetail_List_get.size(); j++) {
                        if(AddDetailList.get(i).getName().equals(AddDetail_List_get.get(j)))
                        {
                           // Toast.makeText(this, AddDetailList.get(i).getName() + " = " + AddDetail_List_get.get(j), Toast.LENGTH_SHORT).show();
                            AddDetailList.get(i).selected = true;
                        }
                    }
                }
            }

            Log.e(TAG, "multiple_select " + AddDetailList.toString());
            Log.e("multiple_select", new Gson().toJson(AddDetailList));
            Log.e(TAG, "multiple_select " + detailsSelectlist.toString());

            lotdetaildata.setAddDetailList(AddDetailList);
            additional_details_checklist = detailsSelectlist.toString();
            additional_details_checklist = additional_details_checklist.replaceAll("\\[", "");
            additional_details_checklist = additional_details_checklist.replaceAll("\\]", "");

            Log.e("add_dets_check", additional_details_checklist);
            lotdetaildata.setAdditional_details_checklist(additional_details_checklist);

            Log.e("dataTA", dataT+"");

            lotdetaildata.setDataT(dataT);
            LotDetailServer lotdetailserver = new LotDetailServer();
            lotdetailserver.setLotdetailsdata(lotdetaildata);
            poDtls.setLotdetailsserver(lotdetailserver);
            Log.e("ModeleeeNull", new Gson().toJson(poDtls));

            lot_no          = lotno.getText().toString();
            Auction_name    = auction_no.getText().toString();
            Log.e("Second11", lot_no+" "+Auction_name);
            this.poDtls = poDtls;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            item = new CustomGallery();
            if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
                String[] all_path = data.getStringArrayExtra("all_path");
                Log.e(TAG, "image_gay..." + all_path);

                for (String string : all_path) {
                    CustomGallery item = new CustomGallery();
                    item.sdcardPath = string;
                    imagePaths.add(string);
                    if(imagePaths.size() != 0){
                        glryImg.setVisibility(View.VISIBLE);
                    }
                    Log.e(TAG, "tttI " + string);
                    Log.e(TAG, "tttvfI " + item.sdcardPath);

                    item.setIdx(CustomGallery.index);
                    CustomGallery.index++;
                    Log.e("Index", ""+CustomGallery.index);
                    dataT.add(item);
                    Log.e("New ",item.sdcardPath +" ..... "+CustomGallery.index);
                    Log.e("New1 ",item.sdcardPath +" ..... "+item.getIdx());
                }
                Log.e("dataTA.", dataT+"");
                adapter.addAll(dataT);
                gridGallery.requestFocus();
                images = imagePaths.toString();
            }
            Log.e("dataTB.", dataT+"");
            Log.e(TAG, "image_gallery" + imagePaths);
            Log.e(TAG, "image_gallery" + imagePaths.size());
        }
        if(requestCode == CAPTURE_IMAGES_FROM_CAMERA)
        {
            exitingCamera();
        }
        return;
    }

    private void exitingCamera() {

        Cursor cursor = loadCursor();
        Log.e(TAG, "cursor "+ cursor.toString());
        String[] paths = getImagePaths(cursor, image_count_before);
        cursor.close();
        try{
            int count = 0;
            for (String stringC : paths) {
                count++;
                CustomGallery item = new CustomGallery();
                File file = new File(stringC);
                Log.e(TAG,"file "+file.getAbsolutePath());
                File newFile = new File(Environment.getExternalStorageDirectory(), "EquipBid/"+Auction_name+"_"+lot_no);
                newFile.mkdirs();  // <----
                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Img_path = lot_no + "_" + timeStamp+"_"+count;
                File new_file = new File(newFile, Img_path + ".jpg");
                Log.e(TAG, "new_file "+new_file);

                if(file.renameTo(new_file)){
                    Log.e(TAG, "new_fileSuccess");
                }else{
                    Log.e(TAG, "new_fileFail ");
                }

                item.sdcardPath = new_file.toString();
                imagePaths_camera.add(new_file.toString());

                Log.e(TAG, "item.sdcardPath "+item.sdcardPath);
                Log.e(TAG, "item.item " +item.sdcardPath);

                if(imagePaths_camera.size() != 0){
                    glryImg.setVisibility(View.VISIBLE);
                }
                dataCam.add(item);
            }
            if(dataCam.size() != 0){
                glryImg.setVisibility(View.VISIBLE);
            }

            Log.e("dataCamera", dataCam+"");
            adapterCamera.addAll(dataCam);
            adapterCamera.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            Log.e("Exc", e.getMessage());
        }

    }

    public String[] getImagePaths(Cursor cursor, int startPosition) {

        int size = cursor.getCount() - startPosition;
        Log.e(TAG, "size "+ size);
        if (size <= 0) return null;

        String[] paths = new String[size];
        Log.e(TAG, "paths1 "+ new Gson().toJson(paths));
        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        Log.e(TAG, "dataColumnIndex "+ dataColumnIndex);
        for (int i = startPosition; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);

            paths[i - startPosition] = cursor.getString(dataColumnIndex);
        }
        Log.e(TAG, "pathsRe "+ new Gson().toJson(paths));
        return paths;
    }

    private class FullScreenImageAdapterLocalGallery extends PagerAdapter {

        private LayoutInflater inflater = null;
        private ImageView imgDisplay, btnClose, btnDelete;
        private Activity _activity;
        private ArrayList<String> imagePaths;
        Bitmap bitmap;

        public FullScreenImageAdapterLocalGallery(Activity activity, ArrayList<String> imagePaths) {
            // TODO Auto-generated constructor stub
            this._activity = activity;
            this.imagePaths = imagePaths;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.preview_images, container, false);

            imgDisplay  = (ImageView) viewLayout.findViewById(R.id.thumbnail);
            btnClose    = (ImageView) viewLayout.findViewById(R.id.btnClose);
            btnDelete   = (ImageView) viewLayout.findViewById(R.id.btnDelete);

            Log.e(TAG, "dataT" + dataT);
            Log.e("imagePaths_All_gallery", ""+ imagePaths);
            Log.e("imagePaths_All_gallery", ""+ imagePaths.size());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(imagePaths.get(position), options);
            Log.e("bitmap", ""+ bitmap);
            imgDisplay.setImageBitmap(bitmap);

            (container).addView(viewLayout);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Imagedialog.cancel();
                    ViewPageradapterLocalG.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    gridGallery.requestFocus();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("imagePathsBefore", imagePaths+"");
                    Log.e("imagePathsBefore", imagePaths.size()+"");
                    imagePaths.remove(position);
                    ViewPageradapterLocalG.notifyDataSetChanged();
                    Log.e("imagePathsAfter", imagePaths+"");
                    Log.e("imagePathsAfter", imagePaths.size()+"");
                    Log.e(TAG, "dataTB" + dataT);
                    Log.e(TAG, "dataTB" + dataT.size());
                    try {
                        dataT.remove(position);
                    }
                    catch (Exception e) {
                    }
                    adapter.addAll(dataT);
                    adapter.notifyDataSetChanged();
                    gridGallery.requestFocus();
                    Log.e(TAG, "dataTA " + dataT);
                    Log.e(TAG, "dataTA " + dataT.size());
                    if(imagePaths.size() == 0)
                    {
                        glryImg.setVisibility(View.GONE);
                        Imagedialog.cancel();
                    }
                }
            });
            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((RelativeLayout) object);
        }
    }

    public class FullScreenImageAdapterLocal extends PagerAdapter {

        private final File mfile;
        private LayoutInflater inflater = null;
        private ImageView imgDisplay, btnClose, btnDelete;
        private Activity _activity;
        private ArrayList<String> imagePaths_camera;
        Bitmap bitmap;

        public FullScreenImageAdapterLocal(Activity activity, ArrayList<String> imagePaths_camera, File mfile) {
            // TODO Auto-generated constructor stub
            this._activity = activity;
            this.imagePaths_camera = imagePaths_camera;
            this.mfile = mfile;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePaths_camera.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.preview_images, container, false);

            imgDisplay  = (ImageView) viewLayout.findViewById(R.id.thumbnail);
            btnClose    = (ImageView) viewLayout.findViewById(R.id.btnClose);
            btnDelete   = (ImageView) viewLayout.findViewById(R.id.btnDelete);

            Log.e(TAG, "dataCam " + dataCam);
            Log.e("imagePaths_cam", ""+ imagePaths_camera);
            Log.e("imagePaths_cam", ""+ imagePaths_camera.size());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(imagePaths_camera.get(position), options);
            Log.e("bitmap", ""+ bitmap);
            imgDisplay.setImageBitmap(bitmap);

            (container).addView(viewLayout);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Imagedialog.cancel();
                    ViewPageradapterLocal.notifyDataSetChanged();
                    adapterCamera.notifyDataSetChanged();
                    gridGalleryCamera.requestFocus();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("imagePathsBefore", imagePaths_camera+"");
                    Log.e("imagePathsBefore", imagePaths_camera.size()+"");
                   // File[] listOfFiles = Mfile.listFiles();
//                    for (int i = 0; i < listOfFiles.length; i++) {
//                        if (listOfFiles[i].isFile()) {
//                            Log.e("File ",  listOfFiles[i].getPath());
//                            if(imagePaths_camera.get(position).equals(listOfFiles[i].getPath()))
//                            {
//                                File ff = listOfFiles[i];
//                                ff.delete();
//                            }
//                        } else if (listOfFiles[i].isDirectory()) {
//                            Log.e("Directory " , listOfFiles[i].getName());
//                        }
//                    }
                    imagePaths_camera.remove(position);
                    ViewPageradapterLocal.notifyDataSetChanged();

                    try {
                        dataCam.remove(position);
                    }
                    catch (Exception e) {
                    }
                    adapterCamera.addAll(dataCam);
                    adapterCamera.notifyDataSetChanged();
                    gridGalleryCamera.requestFocus();
                    Log.e(TAG, "dataCam " + dataCam);
                    Log.e(TAG, "dataCam " + dataCam.size());
                    if(imagePaths_camera.size() == 0)
                    {
                        glryImg.setVisibility(View.GONE);
                        Imagedialog.cancel();
                    }
                }
            });
            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((RelativeLayout) object);
        }
    }

    private class FullScreenImageAdapterRemote extends PagerAdapter {

        private final ArrayList<String> imagePaths_external;
        private final ArrayList<String> imagesObj;
        private LayoutInflater inflater = null;
        private ImageView imgDisplayR, btnCloseR, btnDeleteR;
        private Activity activity;
        private List<Gallary> LotimgList = new ArrayList<Gallary>();
        private String id;

        public FullScreenImageAdapterRemote(Activity activity, List<Gallary> LotimgList, ArrayList<String> imagePaths_external, ArrayList<String> imagesObj) {
            this.activity = activity;
            this.LotimgList = LotimgList;
            this.imagePaths_external = imagePaths_external;
            this.imagesObj = imagesObj;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return LotimgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.preview_remoteimg, container, false);

            imgDisplayR  = (ImageView) viewLayout.findViewById(R.id.thumbnailR);
            btnCloseR    = (ImageView) viewLayout.findViewById(R.id.btnCloseR);
            btnDeleteR   = (ImageView) viewLayout.findViewById(R.id.btnDeleteR);

            Log.e("LotimgList...",""+LotimgList.size());
            Log.e("LotList",""+LotimgList);
            Log.e("imagePaths_external",""+imagePaths_external);
            Log.e("imagesObj",""+imagesObj);
            Gallary c = LotimgList.get(position);
            id = c.getImgId();
            Log.e("id",""+ id);
            Log.e("LotListn",""+LotimgList.get(position));
            //Toast.makeText(activity, "External", Toast.LENGTH_SHORT).show();
            Picasso.with(activity).load(id).into(imgDisplayR);
            (container).addView(viewLayout);

            btnCloseR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Imagedialog.cancel();
                    ViewPageradapterRemote.notifyDataSetChanged();
                    glryadapter.notifyDataSetChanged();
                    gridGallery1.requestFocus();
                }
            });

            btnDeleteR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(activity,"External", Toast.LENGTH_SHORT).show();
                    Log.e("LotimgList", "before,,,," + LotimgList);
                    tempValues = (Gallary) LotimgList.get(position);
                    Log.e("tempValues", "gfh,,,," + tempValues);

                    imagePaths_external.remove(position);
                    imagesObj.remove(position);
                    ViewPageradapterRemote.notifyDataSetChanged();
                    glryadapter.notifyDataSetChanged();
                    // imagePaths_All.remove(imagePaths_All.get(position));
                    LotimgList.remove(tempValues.getImgId());
                    LotimgList.remove(tempValues);

                    Log.e("LotimgList", "dgdg,,,," + LotimgList);
                    Log.e("LotimgList", "dgdg,,,," + tempValues);
                    Log.e("arrayList", "arrayList,,,," + arrayList);
                    ViewPageradapterRemote.notifyDataSetChanged();
                    glryadapter.notifyDataSetChanged();
                    gridGallery1.requestFocus();
                    if(imagesObj.size() ==0)
                    {
                        extImg.setVisibility(View.GONE);
                        Imagedialog.cancel();
                    }
                }
            });
            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((RelativeLayout) object);
        }
    }

    public void deleteRecursive(File MFile) {

        Log.e("Mfile..s", Mfile+"");
        if (MFile.isDirectory()) {
            for (File child : MFile.listFiles()) {
                deleteRecursive(child);
            }
        }
        MFile.delete();
    }

    @Override
    public void onClick(View v) {
            Log.e(TAG, "Local..." + "LOCAL");
            Log.e(TAG, "dataT: " + dataT);
            Log.e(TAG, "dataCam: " + dataCam);
            dataTT.addAll(dataCam);
            dataTT.addAll(dataT);

            Log.e(TAG, "dataTT:" + dataTT);
            Log.e(TAG, " imagePaths_camra: " + imagePaths_camera);
            Log.e(TAG, " imagePaths_camra: " + imagePaths_camera.size());
            Log.e(TAG, "image_gallery: " + imagePaths);
            Log.e(TAG, "image_gallery: " + imagePaths.size());
            Log.e(TAG, " imagePaths_external: " + imagePaths_external);
            Log.e(TAG, " imagePaths_external: " + imagePaths_external.size());
            Log.e("Mfifgflse", Mfile + "");

            _item_title.setError(null);
            descr.setError(null);

            item_title      = _item_title.getText().toString().trim();
            upc             = upcno.getText().toString();
            seller_id       = _seller_id.getText().toString();
            description     = descr.getText().toString().trim();
            condition       = _condition.getText().toString();
            item_class      = _item_class.getText().toString();
            add_detalils    = _add_detalils.getText().toString();
            location        = _location.getText().toString();
            start_price     = _start_price.getText().toString();
            msrp            = _msrp.getText().toString();
            end_price       = _end_price.getText().toString();
            quantity        = _quantity.getText().toString();
            color           = _color.getText().toString();
            brand           = _brand.getText().toString();
            model           = _model.getText().toString();
            dimension       = _dimension.getText().toString();
            category        = Spin_select_value;
            int selectedId  = radioTax.getCheckedRadioButtonId();

            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            Log.e(TAG, "radioButton " + radioButton.getText());
            textable = radioButton.getText().toString();

            if(additional_details_checklist.equals("i"))
            {
                if (detailsSelectlist.size() == 0) {
                    Log.e(TAG, "multiple_select0 " + AddDetail_List_get.toString());
                    detailsSelectlist = AddDetail_List_get;
                } else {
                    Log.e(TAG, "multiple_select1 " + detailsSelectlist.toString());
                }
                Log.e(TAG, "multiple_select " + AddDetail_List_get.toString());
                Log.e(TAG, "multiple_select " + detailsSelectlist.toString());
                additional_details_checklist = detailsSelectlist.toString();
                additional_details_checklist = additional_details_checklist.replaceAll("\\[", "");
                additional_details_checklist = additional_details_checklist.replaceAll("\\]", "");
            }
            else {
                Log.e("add_dets_check", additional_details_checklist);
            }

            lotno           = (EditText) findViewById(R.id.lot_no);
            auction_no      = (EditText) findViewById(R.id.auction_no);
            Auction_name    = auction_no.getText().toString();
            lot_no          = lotno.getText().toString();

            Log.e(TAG, "image_gallary_size_n " + imagePaths.size());
            Log.e(TAG, "image_gallary_again " + arrayList1);
            Log.e(TAG, "Spin_select_value " + Spin_select_value);
            Log.e(TAG, "spin_array1q" + spin_array);
            Log.e(TAG, "arrayList" + arrayList);
            Log.e(TAG, " imagePaths_str: " + imagePaths_str);
            Log.e(TAG, " remote_images: " + remote_images_);
            Log.e(TAG, " remote_images: " + remote_images);
            Log.e(TAG, " imagePaths_camera: " + imagePaths_camera);
            Log.e(TAG, " remote_images_array imagePaths_external: " + imagePaths_external);
            Log.e(TAG, " Auction_name has : " + Auction_name);
            Log.e(TAG, " lot_no has : " + lot_no);
            Log.e(TAG, " upc has : " + upc);
            Log.e(TAG, " lot_status has : " + Lot_S);
            Log.e(TAG, " _item_title has : " + item_title);
            Log.e(TAG, " seller_id has : " + seller_id);
            Log.e(TAG, " _item_class has : " + item_class);
            Log.e(TAG, " description has : " + description);
            Log.e(TAG, " category has : " + category);
            Log.e(TAG, " condition has : " + condition);
            Log.e(TAG, " add_detalils has : " + add_detalils);
            Log.e(TAG, " location has : " + location);
            Log.e(TAG, " start_price has : " + start_price);
            Log.e(TAG, " msrp has : " + msrp);
            Log.e(TAG, " reverse_price has : " + end_price);
            Log.e(TAG, " quantity has : " + quantity);
            Log.e(TAG, " textable has : " + textable);
            Log.e(TAG, " model has : " + model);
            Log.e(TAG, " brand has : " + brand);
            Log.e(TAG, " color has : " + color);
            Log.e(TAG, " dimension has : " + dimension);
            Log.e(TAG, " LocalVar has : " + LocalVar);
            Log.e(TAG, " arrayList has : " + arrayList);
            Log.e(TAG, " additional_details_checklist has : " + additional_details_checklist);
            imagePaths_ext_str = imagePaths_external.toString();
            Log.e(TAG, " imagePaths_ext_str has : " + imagePaths_ext_str);

            remote_images_   = arrayList.toString();
            remote_images    = remote_images_.replaceAll(" ", "");

            Log.e(TAG, " remote_images_second_ has : " + remote_images_second_);
            Log.e(TAG, " remote_images_second has : " + remote_images_second);
            imagePaths_camera.clear();
            for(int i=0; i<dataTT.size(); i++){
                String dd = dataTT.get(i).sdcardPath;
                Log.e("sfdsfddf", dd);
                imagePaths_camera.add(dataTT.get(i).sdcardPath);
            }
            Log.e(TAG, " imagePaths_camera_New : " + imagePaths_camera);
            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(description)) {
                descr.setError(getString(R.string.error_field_required));
                focusView = descr;
                cancel = true;
            }
            if (TextUtils.isEmpty(item_title)) {
                _item_title.setError(getString(R.string.error_field_required));
                focusView = _item_title;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            } else {
                Gson gson = new Gson();
                Local_Images= gson.toJson(imagePaths_camera);
                Log.e("Local_Images ", Local_Images);

                final int cat_position = spn.getSelectedItemPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder(Lot_Details_New.this);
                builder.setTitle("Submit Lot Details");
                builder.setMessage("Are you sure you want to submit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LotDetails l = new LotDetails();
                                l.setId(p_id);
                                l.setToken(p_token);
                                l.setLot_status("draft");
                                l.setAuction_no(Auction_name);
                                l.setLot_no(lot_no);
                                l.setUpc(upc);
                                l.setItem_title(item_title);
                                l.setSeller_id(seller_id);
                                l.setDescription(description);
                                l.setItem_class(item_class);
                                l.setBrand(brand);
                                l.setModel(model);
                                l.setColor(color);
                                l.setDimension(dimension);
                                l.setCondition(condition);
                                l.setLocation(location);
                                l.setAdditional_details_checklist(additional_details_checklist);
                                l.setAdd_detalils(add_detalils);
                                l.setCategory(category);
                                l.setStart_price(start_price);
                                l.setReverse_price(end_price);
                                l.setMsrp(msrp);
                                l.setQuantity(quantity);
                                l.setTextable(textable);
                                l.setRemote_images(remote_images);
                                l.setImages(imagePaths_camera+"");
                                l.setFlag(0);
                                if(Mfile != null)
                                {
                                   l.setmFile(Mfile+"");
                                }
                                else
                                {
                                    l.setmFile("");
                                }

                                l.setSpinselect(cat_position);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 8;
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                if((imagePaths_camera.size() != 0) && (arrayList.size() != 0))
                                {
                                    l.setThumbnail(arrayList.get(0));
                                }
                                else if((imagePaths_camera.size() != 0) && (arrayList.size() == 0))
                                {
                                    l.setThumbnail(imagePaths_camera.get(0));
                                }
                                else if((arrayList.size() != 0) && (imagePaths_camera.size() == 0))
                                {
                                    l.setThumbnail(arrayList.get(0));
                                }
                                else
                                {
                                    l.setThumbnail("");
                                }

                               if(isNetworkAvailable()) {
                                   if(imagePaths_camera.size()!=0)
                                       uploadMultipartBoth(imagePaths_camera);
                                   else
                                       setLotDetails(p_token, Auction_name, lot_no, item_title, seller_id, description, item_class, model, color,
                                               brand, dimension, category, condition, add_detalils, location, start_price, msrp, end_price,
                                               quantity, textable, upc, remote_images, additional_details_checklist);
                               }
                                MyDatabaseHelper helper = new MyDatabaseHelper(Lot_Details_New.this);
                                helper.setIsNetworkState(isNetworkAvailable());
                                int count_AL = helper.CheckExistAL(Auction_name, lot_no, Lot_S);
                                if(count_AL != 0)
                                {
                                    helper.updateRecord(l, Auction_name, lot_no);
                                }
                                else {
                                    try {
                                        helper.insertData(l);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Log.e("helpercxvcv", helper+"");
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

    private void setLotDetails(final String p_id, final String Auction_name, final String lot_no, final String item_title,
                               final String seller_id, final String description, final String item_class,
                               final String model, final String color, final String brand , final String dimension,
                               final String category, final String condition, final String add_detalils, final String location,
                               final String start_price,final String msrp, final String reverse_price, final String quantity,
                               final String textable, final String upc, final String remote_images,
                               final String additional_details_checklist) {

        String tag_string_req = "req_user_setting";
        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, "URL_lot_details_update :" + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "images "+ images);
                // Log.d(TAG, "user details update Response: " + response);
                System.out.println("Responce---- "+ response);
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    Log.e(TAG, "try if: " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    String code = jObj.getString("code");
                    JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                    Log.d(TAG, "user_details_update_Response: " + response.toString());
                    if(code.equals("updated_lot"))
                    {
                        Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                        inext.putExtra("name",Auction_name);
                        inext.putExtra("lotname",lot_no);
                        inext.putExtra("upc",upc);
                        inext.putExtra("lot_status",Lot_S);
                        startActivity(inext);
                       // Log.e("Mfilse..s", Mfile+"");
                        if(Mfile != null)
                        {
                            deleteRecursive(Mfile);
                        }
                        else
                        {

                        }
                    }
                    else if (code.equals("lot_not_used_yet"))
                    {
                        Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                        inext.putExtra("name",Auction_name);
                        inext.putExtra("lotname",lot_no);
                        inext.putExtra("upc",upc);
                        inext.putExtra("lot_status",Lot_S);
                        startActivity(inext);
                      //  Log.e("Mfilse..s", Mfile+"");
                      //  deleteRecursive(Mfile);
                        if(Mfile != null)
                        {
                            deleteRecursive(Mfile);
                        }
                        else
                        {

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit details update Error: " + error.getMessage());
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

                        if(code.equals("lot does not exist"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot does ot Exist", Toast.LENGTH_LONG).show();
                        }
                        else if(code.equals("lot exists"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot Exist", Toast.LENGTH_LONG).show();
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

                Log.e("remote_imagesBothFirstp", remote_images);
                Map<String, String> params = new HashMap<String, String>();
                if(p_id != null)params.put("p_id", p_id);
                if(Auction_name != null)params.put("Auction_name", Auction_name);
                if(lot_no != null)params.put("lot_no", lot_no);
                if(item_title != null)params.put("item_title", item_title);
                if(seller_id != null)params.put("seller_id", seller_id);
                if(description != null)params.put("description", description);
                if(item_class != null)params.put("item_class", item_class);
                if(model != null)params.put("model", model);
                if(color != null)params.put("color", color);
                if(brand != null)params.put("brand", brand);
                if(dimension != null)params.put("dimension", dimension);
                if(category != null)params.put("category", category);
                if(condition != null)params.put("condition", condition);
                if(add_detalils != null)params.put("add_detalils", add_detalils);
                if(location != null)params.put("location", location);
                if(start_price != null)params.put("start_price", start_price);
                if(msrp != null)params.put("msrp", msrp);
                if(reverse_price != null)params.put("reverse_price", reverse_price);
                if(quantity != null)params.put("quantity", quantity);
                if(textable != null)params.put("textable", textable);
                if(upc != null)params.put("upc", upc);
                if(remote_images != null)params.put("remote_images", remote_images);
                if(additional_details_checklist != null)params.put("additional_details_checklist", additional_details_checklist);
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

    private void setLotDetailsFirstBoth(final String p_id, final String Auction_name, final String lot_no, final String item_title,
                                        final String seller_id, final String description, final String item_class,
                                        final String model, final String color, final String brand , final String dimension,
                                        final String category, final String condition, final String add_detalils, final String location,
                                        final String start_price,final String msrp, final String reverse_price, final String quantity,
                                        final String textable, final String upc, final String remote_images,
                                        final String additional_details_checklist) {

        String tag_string_req = "req_user_setting";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "images "+ images);
                // Log.d(TAG, "user details update Response: " + response);
                System.out.println("Responce---- "+ response);
                // hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    Log.e(TAG, "try if: " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    String code = jObj.getString("code");
                    JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                    Log.d(TAG, "user_details_update_Response: " + response.toString());
                    if(code.equals("updated_lot"))
                    {
                    }
                    else if (code.equals("lot_not_used_yet"))
                    {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit details update Error: " + error.getMessage());
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

                        if(code.equals("lot does not exist"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot does not Exist", Toast.LENGTH_LONG).show();
                        }
                        else if(code.equals("lot exists"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot Exist", Toast.LENGTH_LONG).show();
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

                Log.e("remote_imagesBothFirstp", remote_images);
                Map<String, String> params = new HashMap<String, String>();
                if(p_id != null)params.put("p_id", p_id);
                if(Auction_name != null)params.put("Auction_name", Auction_name);
                if(lot_no != null)params.put("lot_no", lot_no);
                if(item_title != null)params.put("item_title", item_title);
                if(seller_id != null)params.put("seller_id", seller_id);
                if(description != null)params.put("description", description);
                if(item_class != null)params.put("item_class", item_class);
                if(model != null)params.put("model", model);
                if(color != null)params.put("color", color);
                if(brand != null)params.put("brand", brand);
                if(dimension != null)params.put("dimension", dimension);
                if(category != null)params.put("category", category);
                if(condition != null)params.put("condition", condition);
                if(add_detalils != null)params.put("add_detalils", add_detalils);
                if(location != null)params.put("location", location);
                if(start_price != null)params.put("start_price", start_price);
                if(msrp != null)params.put("msrp", msrp);
                if(reverse_price != null)params.put("reverse_price", reverse_price);
                if(quantity != null)params.put("quantity", quantity);
                if(textable != null)params.put("textable", textable);
                if(upc != null)params.put("upc", upc);
                if(remote_images != null)params.put("remote_images", remote_images);
                if(additional_details_checklist != null)params.put("additional_details_checklist", additional_details_checklist);

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

    private void setLotDetails1(final String p_id, final String Auction_name, final String lot_no, final String item_title,
                                final String seller_id, final String description, final String item_class,
                                final String model, final String color, final String brand , final String dimension,
                                final String category, final String condition, final String add_detalils, final String location,
                                final String start_price,final String msrp, final String reverse_price, final String quantity,
                                final String textable, final String upc, final String remote_images_second,
                                final String additional_details_checklist) {

        String tag_string_req = "req_user_setting";
        pDialog.setMessage("Please Wait ...");
        showDialog();
        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, "URL_lot_details_update :" + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "images "+ images);
                // Log.d(TAG, "user details update Response: " + response);
                System.out.println("Responce-fffff--- "+ response);
                hideDialog();
                Log.e(TAG, "fffffff : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    String code = jObj.getString("code");
                    JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                    Log.d(TAG, "user_details_update_Response: " + response.toString());
                    if(code.equals("updated_lot"))
                    {
                        Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                        inext.putExtra("name", Auction_name);
                        inext.putExtra("lotname", lot_no);
                        inext.putExtra("upc", upc);
                        inext.putExtra("lot_status", Lot_S);
                        startActivity(inext);
                        // Log.e("Mfilse..s", Mfile+"");
                        //deleteRecursive(Mfile);
                        if(Mfile != null)
                        {
                            deleteRecursive(Mfile);
                        }
                        else
                        {
                           // Toast.makeText(Lot_Details_New.this, "Null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (code.equals("lot_not_used_yet"))
                    {
                        Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                        inext.putExtra("name",Auction_name);
                        inext.putExtra("lotname",lot_no);
                        inext.putExtra("upc",upc);
                        inext.putExtra("lot_status",Lot_S);
                        startActivity(inext);
                      //  Log.e("Mfilse..s", Mfile+"");
                     //   deleteRecursive(Mfile);
                        if(Mfile != null)
                        {
                            deleteRecursive(Mfile);
                        }
                        else
                        {
                            //Toast.makeText(Lot_Details_New.this, "Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit details update Error: " + error.getMessage());
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

                        if(code.equals("lot does not exist"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot does ot Exist", Toast.LENGTH_LONG).show();
                        }
                        else if(code.equals("lot exists"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot Exist", Toast.LENGTH_LONG).show();
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

                if(p_id != null)params.put("p_id", p_id);
                if(Auction_name != null)params.put("Auction_name", Auction_name);
                if(lot_no != null)params.put("lot_no", lot_no);
                if(item_title != null)params.put("item_title", item_title);
                if(seller_id != null)params.put("seller_id", seller_id);
                if(description != null)params.put("description", description);
                if(item_class != null)params.put("item_class", item_class);
                if(model != null)params.put("model", model);
                if(color != null)params.put("color", color);
                if(brand != null)params.put("brand", brand);
                if(dimension != null)params.put("dimension", dimension);
                if(category != null)params.put("category", category);
                if(condition != null)params.put("condition", condition);
                if(add_detalils != null)params.put("add_detalils", add_detalils);
                if(location != null)params.put("location", location);
                if(start_price != null)params.put("start_price", start_price);
                if(msrp != null)params.put("msrp", msrp);
                if(reverse_price != null)params.put("reverse_price", reverse_price);
                if(quantity != null)params.put("quantity", quantity);
                if(textable != null)params.put("textable", textable);
                if(upc != null)params.put("upc", upc);
                if(remote_images_second != null)params.put("remote_images", remote_images_second);
                if(additional_details_checklist != null)params.put("additional_details_checklist", additional_details_checklist);
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

    private void setLotDetailsRemotes(final String p_id, final String Auction_name, final String lot_no, final String item_title,
                                      final String seller_id, final String description, final String item_class,
                                      final String model, final String color, final String brand , final String dimension,
                                      final String category, final String condition, final String add_detalils, final String location,
                                      final String start_price,final String msrp, final String reverse_price, final String quantity,
                                      final String textable, final String upc, final String remote_images_s_remote,
                                      final String additional_details_checklist) {

        String tag_string_req = "req_user_setting";
        pDialog.setMessage("Please Wait ...");
        showDialog();

        Log.e(TAG, " tag_string_req : " + tag_string_req );
        Log.e(TAG, "URL_lot_details_update :" + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "images "+ images);
                // Log.d(TAG, "user details update Response: " + response);
                System.out.println("Responce---- "+ response);
                hideDialog();
                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    Log.e(TAG, "try if: " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    String code = jObj.getString("code");
                    JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                    Log.e("i-----",Jobj1.toString());
                    Log.d(TAG, "user_details_update_Response: " + response.toString());
                    if(code.equals("updated_lot"))
                    {

                        Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                        inext.putExtra("name",Auction_name);
                        inext.putExtra("lotname",lot_no);
                        inext.putExtra("upc",upc);
                        inext.putExtra("lot_status",Lot_S);
                        startActivity(inext);
                       // Log.e("Mfilse..s", Mfile+"");
                      //  deleteRecursive(Mfile);
                        if(Mfile != null)
                        {
                            deleteRecursive(Mfile);
                        }
                        else
                        {
                            //Toast.makeText(Lot_Details_New.this, "Null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (code.equals("lot_not_used_yet"))
                    {
                        Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                        inext.putExtra("name",Auction_name);
                        inext.putExtra("lotname",lot_no);
                        inext.putExtra("upc",upc);
                        inext.putExtra("lot_status",Lot_S);
                        startActivity(inext);
                      //  Log.e("Mfilse..s", Mfile+"");
                      //  deleteRecursive(Mfile);
                        if(Mfile != null)
                        {
                            deleteRecursive(Mfile);
                        }
                        else
                        {
                            //Toast.makeText(Lot_Details_New.this, "Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "catch : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit details update Error: " + error.getMessage());
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

                        if(code.equals("lot does not exist"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot does ot Exist", Toast.LENGTH_LONG).show();
                        }
                        else if(code.equals("lot exists"))
                        {
                            Toast.makeText(getApplicationContext(),"Lot Exist", Toast.LENGTH_LONG).show();
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
                Log.e("rmt_img_udt_lcl", remote_images_s_remote);

                if(p_id != null)params.put("p_id", p_id);
                if(Auction_name != null)params.put("Auction_name", Auction_name);
                if(lot_no != null)params.put("lot_no", lot_no);
                if(item_title != null)params.put("item_title", item_title);
                if(seller_id != null)params.put("seller_id", seller_id);
                if(description != null)params.put("description", description);
                if(item_class != null)params.put("item_class", item_class);
                if(model != null)params.put("model", model);
                if(color != null)params.put("color", color);
                if(brand != null)params.put("brand", brand);
                if(dimension != null)params.put("dimension", dimension);
                if(category != null)params.put("category", category);
                if(condition != null)params.put("condition", condition);
                if(add_detalils != null)params.put("add_detalils", add_detalils);
                if(location != null)params.put("location", location);
                if(start_price != null)params.put("start_price", start_price);
                if(msrp != null)params.put("msrp", msrp);
                if(reverse_price != null)params.put("reverse_price", reverse_price);
                if(quantity != null)params.put("quantity", quantity);
                if(textable != null)params.put("textable", textable);
                if(upc != null)params.put("upc", upc);
                if(remote_images_s_remote != null)params.put("remote_images", remote_images_s_remote);
                if(additional_details_checklist != null)params.put("additional_details_checklist", additional_details_checklist);
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

    public void uploadMultipart(ArrayList<String> imagePaths_camera) {

        try {

            Log.e(TAG, "dataT: " + dataT);
            Log.e(TAG, "dataCam: " + dataCam);
            Log.e(TAG, "imagePaths_camera: " + imagePaths_camera);
            Log.e(TAG, "imagePaths_camera: " + imagePaths_camera.size());
            Log.e(TAG, "dataTTF:" + dataTT);

            Log.e(TAG, "try : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
            String uploadId = UUID.randomUUID().toString();
            Log.e("uploadId..ss..", uploadId);
            for (String s : imagePaths_camera)
            {
                listString += s + ",";
            }

            Log.e("arrt_upad_length  " , String.valueOf(listString.split(",").length));
            Log.e("arrayList_upload_str " ,listString);
            MultipartUploadRequest m =
                    new MultipartUploadRequest(Lot_Details_New.this, uploadId, AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);

            for(int i = 0; i< listString.split(",").length; i++)
            {
                Log.e("remote_imagesdfdgfg", listString.split(",")[i].trim()+"");
                m.addFileToUpload(listString.split(",")[i].trim(), "images[]");
            }
            m.addHeader("Authorization", "Bearer "+p_token);
            m.addParameter("Auction_name", Auction_name);
            m.addParameter("lot_no", lot_no);
            m.addParameter("item_title", item_title);
            m.addParameter("seller_id", seller_id);
            m.addParameter("upc", upc);
            m.addParameter("description", description);
            m.addParameter("item_class", item_class);
            m.addParameter("model", model);
            m.addParameter("color", color);
            m.addParameter("brand", brand);
            m.addParameter("dimension", dimension);
            m.addParameter("condition", condition);
            m.addParameter("add_detalils", add_detalils);
            m.addParameter("location", location);
            m.addParameter("start_price", start_price);
            m.addParameter("msrp", msrp);
            m.addParameter("reverse_price", end_price);
            m.addParameter("quantity", quantity);
            m.addParameter("textable", textable);
            m.addParameter("category", category);
            m.addParameter("remote_images", remote_images);
            m.addParameter("additional_details_checklist", additional_details_checklist);
            m.setMaxRetries(2);
            m.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(UploadInfo uploadInfo) {
                    pDialog.setMessage("Please Wait ...");
                    showDialog();
                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onProgress..", listString.split(",")[i]);
                    }
                }
                @Override
                public void onError(UploadInfo uploadInfo, Exception exception) {
                    // your code here

                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onError..", listString.split(",")[i]);
                    }
                }

                @Override
                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {

                    showDialog();
                    //Toast.makeText(Preview_Lot.this,"Upload Complete", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onCompletedUpload..", listString.split(",")[i]);
                    }
                    if(mWakeLock.isHeld())
                        mWakeLock.release();
                    hideDialog();

                    Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                    inext.putExtra("name",Auction_name);
                    inext.putExtra("lotname",lot_no);
                    inext.putExtra("upc",upc);
                    inext.putExtra("lot_status",Lot_S);
                    startActivity(inext);
                   // Log.e("Mfilse..s", Mfile+"");
                   // deleteRecursive(Mfile);
                    if(Mfile != null)
                    {
                        deleteRecursive(Mfile);
                    }
                    else
                    {
                        //Toast.makeText(Lot_Details_New.this, "Null", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(UploadInfo uploadInfo) {
                    // your code here
                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onCancelled..", listString.split(",")[i]);
                    }
                }
            });
            m.startUpload();
        } catch (Exception exc) {
            Log.e("exc ", exc+"");
            //Toast.makeText(getApplicationContext(),exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadMultipartBoth(ArrayList<String> imagePaths_camera) {

        try {

            Log.e(TAG, "dataT: " + dataT);
            Log.e(TAG, "dataCam: " + dataCam);
            Log.e(TAG, "imagePaths_camera: " + imagePaths_camera);
            Log.e(TAG, "imagePaths_camera: " + imagePaths_camera.size());
            Log.e(TAG, "dataTTF:" + dataTT);

            Log.e(TAG, "try : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
            String uploadId = UUID.randomUUID().toString();
            Log.e("uploadId..ss..", uploadId);
            for (String s : imagePaths_camera)
            {
                listString += s + ",";
            }

            Log.e("arrt_upad_length  " , String.valueOf(listString.split(",").length));
            Log.e("arrayList_upload_str " ,listString);

            remote_images_ = imagePaths_external.toString();
            remote_images  = remote_images_.replaceAll(" ", "");

            Log.e("remote_imagesdfdgfg", remote_images_);
            Log.e("remote_imagesdfdgfg", remote_images);

            MultipartUploadRequest m =
                new MultipartUploadRequest(Lot_Details_New.this, uploadId, AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);

            for(int i = 0; i< listString.split(",").length; i++)
            {
                Log.e("remote_imagesdfdgfg", listString.split(",")[i].trim()+"");
                m.addFileToUpload(listString.split(",")[i].trim(), "images[]");
            }
            m.addHeader("Authorization", "Bearer "+p_token);
            m.addParameter("Auction_name", Auction_name);
            m.addParameter("lot_no", lot_no);
            m.addParameter("item_title", item_title);
            m.addParameter("seller_id", seller_id);
            m.addParameter("upc", upc);
            m.addParameter("description", description);
            m.addParameter("item_class", item_class);
            m.addParameter("model", model);
            m.addParameter("color", color);
            m.addParameter("brand", brand);
            m.addParameter("dimension", dimension);
            m.addParameter("condition", condition);
            m.addParameter("add_detalils", add_detalils);
            m.addParameter("location", location);
            m.addParameter("start_price", start_price);
            m.addParameter("msrp", msrp);
            m.addParameter("reverse_price", end_price);
            m.addParameter("quantity", quantity);
            m.addParameter("textable", textable);
            m.addParameter("category", category);
            m.addParameter("remote_images", remote_images);
            m.addParameter("additional_details_checklist", additional_details_checklist);
            // m.setNotificationConfig(new UploadNotificationConfig());
            m.setMaxRetries(2);
            m.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(UploadInfo uploadInfo) {
                    pDialog.setMessage("Please Wait ...");
                    showDialog();
                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onProgress..", listString.split(",")[i]);
                    }
                }
                @Override
                public void onError(UploadInfo uploadInfo, Exception exception) {

                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onError..", listString.split(",")[i]);
                    }
                }

                @Override
                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {

                    //Toast.makeText(Preview_Lot.this,"Upload Complete", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onCompletedUpload..", listString.split(",")[i]);
                    }
                    if(mWakeLock.isHeld())
                        mWakeLock.release();
                    hideDialog();
                    Toast.makeText(getApplicationContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent inext = new Intent(Lot_Details_New.this, Success_page.class);
                    inext.putExtra("name",Auction_name);
                    inext.putExtra("lotname",lot_no);
                    inext.putExtra("upc",upc);
                    inext.putExtra("lot_status",Lot_S);
                    startActivity(inext);
                    Log.e("Mfilse..s", Mfile+"");
                    if(Mfile != null)
                    {
                        deleteRecursive(Mfile);
                    }
                    else
                    {
                        //Toast.makeText(Lot_Details_New.this, "Null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(UploadInfo uploadInfo) {
                    // your code here
                    for (int i = 0; i < listString.split(",").length; i++) {
                        Log.e("onCancelled..", listString.split(",")[i]);
                    }
                }
            });
            m.startUpload();

            Log.e("remote_imagesBothFirst", remote_images);
            setLotDetailsFirstBoth(p_id, Auction_name, lot_no, item_title, seller_id, description,
                    item_class, model, color , brand, dimension, category, condition, add_detalils, location,
                    start_price, msrp, end_price, quantity, textable, upc, remote_images, additional_details_checklist);

        } catch (Exception exc) {
            Log.e("exc ", exc+"");
            //Toast.makeText(getApplicationContext(),exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lot__details, menu);
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
            Intent i = new Intent(Lot_Details_New.this, User_Settings.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Lot_Details_New.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else if (id == R.id.history) {
            Intent i = new Intent(Lot_Details_New.this, Auction_History.class);
            startActivity(i);
        } else if (id == R.id.home) {
            Intent i = new Intent(Lot_Details_New.this, Auction_Screen_Main.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.user) {
            Intent i = new Intent(Lot_Details_New.this, New_lot.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class CustomListImgAdapter extends BaseAdapter {
        private final Activity activity;
        private final ArrayList<String> imagePaths_All;
        private final List<Gallary> LotimgList;
        private final LayoutInflater inflater;
        private ImageView thumbnail;

        public CustomListImgAdapter(Activity activity, ArrayList<String> imagePaths_All, List<Gallary> LotimgList) {
            this.activity = activity;
            this.imagePaths_All = imagePaths_All;
            this.LotimgList = LotimgList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePaths_All.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int location) {
            return imagePaths_All.get(location);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.preview_img, parent, false);
            }

            Log.e("imagePathfdgfgfgfgs_All", ""+imagePaths_All);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            Picasso.with(activity)
                    .load(new File(imagePaths_All.get(position)))
                    .into(thumbnail);

            return view;
        }
    }

    class CustomRectcleAdapter extends RecyclerView.Adapter<CustomRectcleAdapter.MyViewHolder> {

        private final Activity activity;
        private final List<Gallary> LotimgList;
        private final LayoutInflater inflater;
        private final ArrayList<String> imagePaths_All;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageViewIcon;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.imageViewIcon = (ImageView) itemView.findViewById(R.id.thumbnail);
            }
        }

        public CustomRectcleAdapter(Activity activity, ArrayList<String> imagePaths_All,  List<Gallary> LotimgList) {

            this.activity = activity;
            this.imagePaths_All = imagePaths_All;
            this.LotimgList = LotimgList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public CustomRectcleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.preview_img, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);
            CustomRectcleAdapter.MyViewHolder myViewHolder = new CustomRectcleAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(CustomRectcleAdapter.MyViewHolder holder, int position) {

            Log.e("imagePaths_AAAAll", ""+imagePaths_All);
            Log.e("LotimgList", ""+LotimgList);
            Log.e("imagePaths_all_size", ""+imagePaths_All.size());
            Log.e("imagePaths_posi", ""+imagePaths_All.get(position));
            ImageView imageView = holder.imageViewIcon;
            Picasso.with(activity).load(imagePaths_All.get(position)).into(imageView);
            // imageView.setImageResource(imagePaths_all.get(position));
        }

        @Override
        public int getItemCount() {
            return imagePaths_All.size();
        }
    }

}
