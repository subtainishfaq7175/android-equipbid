/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package equipbid.armentum.com.equip_bid.basicsyncadapter;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import equipbid.armentum.com.equip_bid.AppConfig;
import equipbid.armentum.com.equip_bid.AppController;
import equipbid.armentum.com.equip_bid.Auction_History;
import equipbid.armentum.com.equip_bid.LotDetails;
import equipbid.armentum.com.equip_bid.MyDatabaseHelper;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_ENTRY_ID = 1;
    public static final int COLUMN_TITLE = 2;
    public static final int COLUMN_LINK = 3;
    public static final int COLUMN_PUBLISHED = 4;
    private final ListSync mCallback;
    private PowerManager.WakeLock mWakeLock;
    private List<LotDetails> LocalList;
    private String p_token;
    private String listString ="";
    private String[] listImage_Local;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, ListSync mCallback) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        this.mCallback = mCallback;
        PowerManager pm = (PowerManager)context.getSystemService(context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs, ListSync mCallback) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        this.mCallback = mCallback;
    }


    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link android.content.AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        Log.i(TAG, "Hello");

        Intent msgrcv = new Intent("Msg");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(msgrcv);
        ComponentName comp = new ComponentName(getContext().getPackageName(),Auction_History.class.getName());

        LocalList = new ArrayList<LotDetails>();
        fillListView();

        Log.i(TAG, "Network synchronization complete");
    }

    private void fillListView() {
        MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
        LocalList.clear();
        long count = helper.getProfilesCount();
        Log.e(TAG, "Count_ " + count);
        LocalList = helper.getAllRecord();
        List<HashMap<String, String>> maplst = new ArrayList<HashMap<String, String>>();
        LotDetails l = new LotDetails();
        if (LocalList.size() > 0) {
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
                map.put(MyDatabaseHelper.Col_Flag, String.valueOf(l.getFlag()));
                maplst.add(map);
            }
            Log.e(TAG, "LocalListSync_ " + new Gson().toJson(LocalList));
            Log.e(TAG, "LocalListSync_ " + LocalList.size());
        }

        Log.e(TAG, "LocalListSync_w " + LocalList.size());
        for (int i = 0; i < LocalList.size(); i++) {
            Log.e(TAG, "LocalList_Au " + LocalList.get(i).getAuction_no());
            Log.e(TAG, "LocalList_Lot " + LocalList.get(i).getLot_no());
            Log.e(TAG, "LocalList_Det " + LocalList.get(i).getItem_title());
            Log.e(TAG, "LocalList_Token " + LocalList.get(i).getToken());
            Log.e(TAG, "LocalList_LocalImg " + LocalList.get(i).getImages());
            Log.e(TAG, "LocalList_Img " + LocalList.get(i).getRemote_images());
            Log.e(TAG, "LocalList_LocalImg " + LocalList.get(i).getImages().length());
            Log.e(TAG, "LocalList_Flag " + LocalList.get(i).getFlag());
            Log.e(TAG, "LocalList_File " + LocalList.get(i).getmFile());

            listString = LocalList.get(i).getImages().replaceAll("\\[", "");
            listString = listString.replaceAll("\\]", "");

            Log.e(TAG, "listString" + listString);
            Log.e(TAG, "listString" + listString.length());

            checkexistAL(listString, LocalList.get(i).getToken(), LocalList.get(i).getAuction_no(), LocalList.get(i).getLot_no(),
                    LocalList.get(i).getItem_title(), LocalList.get(i).getSeller_id(), LocalList.get(i).getDescription(),
                    LocalList.get(i).getItem_class(), LocalList.get(i).getModel(), LocalList.get(i).getColor(),
                    LocalList.get(i).getBrand(), LocalList.get(i).getDimension(), LocalList.get(i).getCategory(),
                    LocalList.get(i).getCondition(), LocalList.get(i).getAdd_detalils(), LocalList.get(i).getLocation(),
                    LocalList.get(i).getStart_price(), LocalList.get(i).getMsrp(),LocalList.get(i).getReverse_price(),
                    LocalList.get(i).getQuantity(), LocalList.get(i).getTextable(), LocalList.get(i).getUpc(),
                    LocalList.get(i).getRemote_images(), LocalList.get(i).getAdditional_details_checklist(),
                    LocalList.get(i).getImages(),LocalList.get(i).getLot_status(),LocalList.get(i).getmFile());
        }
    }

    public void checkexistAL(final String listString, final String p_token, final String Auction_name ,final String lot_no,
                             final String item_title, final String seller_id, final String description, final String item_class,
                             final String model,final String color, final String brand, final String dimension, final String category,
                             final String condition, final String add_detalils, final String location, final String start_price,
                             final String msrp, final String reverse_price,final String quantity, final String textable,
                             final String upc ,final String remote_images, final String additional_details_checklist, final String images,
                             final String lot_status, final String file)
    {
        String tag_string_req = "req_user_setting";
        Log.e(TAG, " tag_string_req : " + tag_string_req);
        Log.e(TAG, " AppConfig.URL_lot_new : " + AppConfig.URL_New_lot + Auction_name + "&lot_no=" + lot_no + "&generate_lot=1");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_New_lot + Auction_name + "&lot_no=" + lot_no + "&generate_lot=1",
                new Response.Listener<String>() {

                    public String code;
                    public String lot_s;
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "user details Response: " + response.toString());
                        Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_New_lot + Auction_name + "&lot_no=" + lot_no + "&generate_lot=1");

                        try {
                            JSONObject jObj = new JSONObject(response);
                            code = (jObj.getString("code"));
                            Log.e("Tag..", code);

                            if (code.equals("created lot")) {

                                JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                Log.e("Lotstatus", lot_status);
                                Log.e("Lot", lot_no + "....." + lot_status);
                                if(listString.length() == 0) {

                                Log.e(TAG, "LocalList.get(i).getImages()" + "No Local");
                                Log.e(TAG, "LocalList.get(i).getImages()" + listString.length());

                                setLotDetails(p_token, Auction_name, lot_no, item_title, seller_id, description, item_class, model, color,
                                    brand, dimension, category, condition, add_detalils, location, start_price, msrp, reverse_price,
                                    quantity, textable, upc, remote_images, additional_details_checklist, images,file);
                                }
                                else {
                                Log.e(TAG, "LocalList.get(i).getImages() " + "Local");
                                Log.e(TAG, "LocalList.get(i).getImages() " + listString.length());

                                uploadMultipartBoth(listString, p_token, Auction_name, lot_no, item_title, seller_id, description,
                                    item_class, model, color, brand, dimension, category, condition, add_detalils, location,
                                    start_price, msrp, reverse_price, quantity, textable, upc, remote_images, additional_details_checklist
                                        ,file);
                                }

                            } else if (code.equals("generated_lot")) {
                                JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                if(listString.length() == 0) {

                                Log.e(TAG, "LocalList.get(i).getImages()" + "No Local");
                                Log.e(TAG, "LocalList.get(i).getImages()" + listString.length());

                                setLotDetails(p_token, Auction_name, lot_no, item_title, seller_id, description, item_class, model, color,
                                    brand, dimension, category, condition, add_detalils, location, start_price, msrp, reverse_price,
                                    quantity, textable, upc, remote_images, additional_details_checklist, images,file);
                                }
                                else
                                {
                                Log.e(TAG, "LocalList.get(i).getImages() " + "Local");
                                Log.e(TAG, "LocalList.get(i).getImages() " + listString.length());

                                uploadMultipartBoth(listString, p_token, Auction_name, lot_no, item_title, seller_id, description,
                                    item_class, model, color, brand, dimension, category, condition, add_detalils, location,
                                    start_price, msrp, reverse_price, quantity, textable, upc, remote_images, additional_details_checklist
                                        ,file);
                                }
                            } else if (code.equals("lot_not_used_yet")) {
                                JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                if(listString.length() == 0) {

                                Log.e(TAG, "LocalList.get(i).getImages()" + "No Local");
                                Log.e(TAG, "LocalList.get(i).getImages()" + listString.length());

                                setLotDetails(p_token, Auction_name, lot_no, item_title, seller_id, description, item_class, model, color,
                                    brand, dimension, category, condition, add_detalils, location, start_price, msrp, reverse_price,
                                    quantity, textable, upc, remote_images, additional_details_checklist, images,file);
                                }
                                else
                                {
                                Log.e(TAG, "LocalList.get(i).getImages() " + "Local");
                                Log.e(TAG, "LocalList.get(i).getImages() " + listString.length());

                                uploadMultipartBoth(listString, p_token, Auction_name, lot_no, item_title, seller_id, description,
                                    item_class, model, color, brand, dimension, category, condition, add_detalils, location,
                                    start_price, msrp, reverse_price, quantity, textable, upc, remote_images, additional_details_checklist
                                        ,file);
                                }

                            } else if (code.equals("updated_lot")) {
                                JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                                if(listString.length() == 0) {

                                Log.e(TAG, "LocalList.get(i).getImages()" + "No Local");
                                Log.e(TAG, "LocalList.get(i).getImages()" + listString.length());

                                setLotDetails(p_token, Auction_name, lot_no, item_title, seller_id, description, item_class, model, color,
                                    brand, dimension, category, condition, add_detalils, location, start_price, msrp, reverse_price,
                                    quantity, textable, upc, remote_images, additional_details_checklist, images, file);
                                }
                                else
                                {
                                Log.e(TAG, "LocalList.get(i).getImages() " + "Local");
                                Log.e(TAG, "LocalList.get(i).getImages() " + listString.length());

                                uploadMultipartBoth(listString, p_token, Auction_name, lot_no, item_title, seller_id, description,
                                    item_class, model, color, brand, dimension, category, condition, add_detalils, location,
                                    start_price, msrp, reverse_price, quantity, textable, upc, remote_images, additional_details_checklist
                                        ,file);
                                }

                            } else if (code.equals("lot_already_exist")) {
                                String message = (jObj.getString("message"));
                                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                //Error
                                MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                                helper.changeFlag(Auction_name, lot_no);
                                Log.e("LocalListtt", LocalList+"");
                                Log.e("LocalListtt", LocalList.size()+"");
                                mCallback.refreshLocal();

                            } else if (code.equals("lot_no_is_not_valid")) {
                                String message = (jObj.getString("message"));
                                Log.e("messagebfbgfh", message);
                                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                //Error
                                MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                                helper.changeFlag(Auction_name, lot_no);
                                Log.e("LocalListtt", LocalList+"");
                                Log.e("LocalListtt", LocalList.size()+"");
                                mCallback.refreshLocal();

                            } else if (code.equals("rest_invalid_lot_no")) {
                                String message = (jObj.getString("message"));
                               // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                                helper.changeFlag(Auction_name, lot_no);
                                Log.e("LocalListtt", LocalList+"");
                                Log.e("LocalListtt", LocalList.size()+"");
                                mCallback.refreshLocal();

                            } else if (code.equals("unable to create lot")) {
                                String message = (jObj.getString("message"));
                               // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                                helper.changeFlag(Auction_name, lot_no);
                                Log.e("LocalListtt", LocalList+"");
                                Log.e("LocalListtt", LocalList.size()+"");
                                mCallback.refreshLocal();

                            } else if (code.equals("lot does not exist")) {
                                String message = (jObj.getString("message"));
                                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                                MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                                helper.changeFlag(Auction_name, lot_no);
                                Log.e("LocalListtt", LocalList+"");
                                Log.e("LocalListtt", LocalList.size()+"");
                                mCallback.refreshLocal();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Catch:" + e);
                            Log.e(TAG, "catch :" + AppConfig.URL_New_lot + Auction_name + "&lot_no=" + lot_no + "&generate_lot=1");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
               // Toast.makeText(getContext(), "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("lot_no", lot_no);
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

    private void setLotDetails(final String p_token, final String Auction_name, final String lot_no, final String item_title,
                               final String seller_id, final String description, final String item_class,
                               final String model, final String color, final String brand , final String dimension,
                               final String category, final String condition, final String add_detalils, final String location,
                               final String start_price,final String msrp, final String reverse_price, final String quantity,
                               final String textable, final String upc, final String remote_images,
                               final String additional_details_checklist,final String images, final String file) {

        String tag_string_req = "req_user_setting";

        Log.e(TAG, "tag_string_req : " + tag_string_req );
        Log.e(TAG, "URL_lot_details_update :" + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("Responce---- "+ response);

                Log.e(TAG, "sdfdsgfgfg : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                try {
                   // Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Auction Uploading to server", Toast.LENGTH_SHORT).show();
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, "try : " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    Log.e(TAG, "try if: " + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
                    String code = jObj.getString("code");
                    JSONObject Jobj1 = jObj.getJSONObject("lot_data");
                    Log.d(TAG, "user_details_update_Response: " + response.toString());
                    if(code.equals("updated_lot"))
                    {
                       //Toast.makeText(getContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                        helper.deleteTitle(Auction_name);
                        File MFile = new File(file);
                        Log.e(TAG, "File.."+ MFile);
                        if(!MFile.equals(""))
                        {
                            deleteRecursive(MFile);
                        }
                        else
                        {

                        }
                        Log.e("LocalListtt", LocalList+"");
                        Log.e("LocalListtt", LocalList.size()+"");
                        mCallback.refreshLocal();
                    }
                    else if (code.equals("lot_not_used_yet"))
                    {
                        //Toast.makeText(getContext(), "Lot Details Updated Successfully........", Toast.LENGTH_SHORT).show();
                        MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                        helper.deleteTitle(Auction_name);
                        File MFile = new File(file);
                        Log.e(TAG, "File.."+ MFile);
                        if(!MFile.equals(""))
                        {
                            deleteRecursive(MFile);
                        }
                        else
                        {

                        }
                        Log.e("LocalListtt", LocalList+"");
                        Log.e("LocalListtt", LocalList.size()+"");
                        mCallback.refreshLocal();
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
               // Toast.makeText(getContext(),"Oops! Something went wrong", Toast.LENGTH_SHORT).show();

                if(error instanceof ServerError) {
                    String str = null;
                    try {
                        str = new String(error.networkResponse.data, "UTF8");
                        Log.e("str............", str);
                        JSONObject errorJson = new JSONObject(str);
                        String code = errorJson.getString("code");

                        if(code.equals("lot does not exist"))
                        {
                            //Toast.makeText(getContext(),"Lot does ot Exist", Toast.LENGTH_LONG).show();
                        }
                        else if(code.equals("lot exists"))
                        {
                            //Toast.makeText(getContext(),"Lot Exist", Toast.LENGTH_LONG).show();
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

                listString = images.replaceAll("\\[", "");
                listString = listString.replaceAll("\\]", "");
                listImage_Local = listString.split(",");
                Log.e("listString " , listImage_Local+"");
                Log.e("listString " , listImage_Local.length+"");

                // Log.e("remote_imagesBothFirstp", remote_images);
                Map<String, String> params = new HashMap<String, String>();

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


    public void uploadMultipartBoth(final String listString, final String p_token, final String Auction_name, final String lot_no, final String item_title,
                                    final String seller_id, final String description, final String item_class,
                                    final String model, final String color, final String brand , final String dimension,
                                    final String category, final String condition, final String add_detalils, final String location,
                                    final String start_price,final String msrp, final String reverse_price, final String quantity,
                                    final String textable, final String upc, final String remote_images,
                                    final String additional_details_checklist, final String file) {
        try {

            Toast.makeText(getContext(), "Auction Uploading to server", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "try :" + AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);
            String uploadId = UUID.randomUUID().toString();
            Log.e(TAG, "listString..."+listString.split(",").length);
            Log.e(TAG, "listString..."+listString.split(","));
            Log.e(TAG, "uploadId..."+uploadId);
            MultipartUploadRequest m =
                    new MultipartUploadRequest(getContext(), uploadId, AppConfig.URL_lot_details+Auction_name+"&lot_no="+lot_no);

            m.addHeader("Authorization", "Bearer "+p_token);
            Log.e(TAG, "Token "+ "Bearer "+p_token);

            m.addParameter("Auction_name", Auction_name);
            Log.e(TAG, "Auction_name "+Auction_name);
            m.addParameter("lot_no", lot_no);
            Log.e(TAG, "lot_no "+lot_no);
            m.addParameter("item_title", item_title);
            Log.e(TAG, "item_title "+item_title);
            m.addParameter("seller_id", seller_id);
            Log.e(TAG, "seller_id "+seller_id);
            m.addParameter("upc", upc);
            Log.e(TAG, "upc "+upc);
            m.addParameter("description", description);
            Log.e(TAG, "description "+description);
            m.addParameter("item_class", item_class);
            Log.e(TAG, "item_class "+item_class);
            m.addParameter("model", model);
            Log.e(TAG, "model "+model);
            m.addParameter("color", color);
            Log.e(TAG, "color "+color);
            m.addParameter("brand", brand);
            Log.e(TAG, "brand "+brand);
            m.addParameter("dimension", dimension);
            Log.e(TAG, "dimension "+dimension);
            m.addParameter("condition", condition);
            Log.e(TAG, "condition "+condition);
            m.addParameter("add_detalils", add_detalils);
            Log.e(TAG, "add_detalils "+add_detalils);
            m.addParameter("location", location);
            Log.e(TAG, "location "+location);
            m.addParameter("start_price", start_price);
            Log.e(TAG, "start_price "+start_price);
            m.addParameter("msrp", msrp);
            Log.e(TAG, "msrp "+msrp);
            m.addParameter("reverse_price", reverse_price);
            Log.e(TAG, "reverse_price "+reverse_price);
            m.addParameter("quantity", quantity);
            Log.e(TAG, "quantity "+quantity);
            m.addParameter("textable", textable);
            Log.e(TAG, "textable "+textable);
            m.addParameter("category", category);
            Log.e(TAG, "category "+category);
            m.addParameter("remote_images", remote_images);
            Log.e(TAG, "remote_images "+remote_images);
            m.addParameter("additional_details_checklist", additional_details_checklist);
            Log.e(TAG, "additional_details_checklist "+additional_details_checklist);
            for(int i = 0; i< listString.split(",").length; i++)
            {
                Log.e(TAG, "remote_imagesdfdgfg "+listString.split(",")[i].trim()+"");
                m.addFileToUpload(listString.split(",")[i].trim(), "images[]");
            }

            m.setMaxRetries(6);
            m.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(UploadInfo uploadInfo) {

                    for(int i = 0; i< listString.split(",").length; i++)
                    {
                        Log.e(TAG, "onProgress "+listString.split(",")[i].trim()+"");
                    }
                }
                @Override
                public void onError(UploadInfo uploadInfo, Exception exception) {
                    for(int i = 0; i< listString.split(",").length; i++)
                    {
                        Log.e(TAG, "OnError "+listString.split(",")[i].trim()+"");
                    }
                }

                @Override
                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {

                    for(int i = 0; i< listString.split(",").length; i++)
                    {
                        Log.e(TAG, "OnComplete "+listString.split(",")[i].trim()+"");
                    }

                    if(mWakeLock.isHeld())
                        mWakeLock.release();
                    //Toast.makeText(getContext(), "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
                    MyDatabaseHelper helper = new MyDatabaseHelper(getContext());
                    helper.deleteTitle(Auction_name);
                    File MFile = new File(file);
                    Log.e(TAG, "File.."+ MFile);
                    if(!MFile.equals(""))
                    {
                        deleteRecursive(MFile);
                    }
                    else
                    {

                    }
                    Log.e("LocalListtt", LocalList+"");
                    Log.e("LocalListtt", LocalList.size()+"");
                    mCallback.refreshLocal();
                }

                @Override
                public void onCancelled(UploadInfo uploadInfo) {
                    // your code here
                    for(int i = 0; i< listString.split(",").length; i++)
                    {
                        Log.e(TAG, "onCancelled "+listString.split(",")[i].trim()+"");
                    }
                }
            });

            m.startUpload();
        }
        catch (Exception exc) {
            Log.e(TAG, "exc "+exc);
            //Toast.makeText(getContext(),exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteRecursive(File MFile) {

        Log.e("Mfile..s", MFile+"");
        if (MFile.isDirectory()) {
            for (File child : MFile.listFiles()) {
                deleteRecursive(child);
            }
        }
        MFile.delete();
    }

    public interface ListSync{
        void refreshLocal();
    }


}
