package equipbid.armentum.com.equip_bid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darshan on 11/20/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME              = "EquipBidDB";
    public static final int DB_VERSION              = 13;
    public static final String TABLE_NAME           = "Lot_Details";
    public static final String TABLE_NAME_ADD       = "Additional_Details";
    public static final String TABLE_NAME_CAT       = "Cat_Details";
    public static final String Col_Id               = "id";
    public static final String Col_p_id             = "p_id";
    public static final String Col_auction_number   = "auction_no";
    public static final String Col_lot_number       = "lot_no";
    public static final String Col_lot_status       = "lot_status";
    public static final String Col_upc              = "upc";
    public static final String Col_item_title       = "item_title";
    public static final String Col_seller_id        = "seller_id";
    public static final String Col_description      = "description";
    public static final String Col_item_class       = "item_class";
    public static final String Col_Brand            = "brand";
    public static final String Col_model            = "model";
    public static final String Col_color            = "color";
    public static final String Col_condition        = "condition";
    public static final String Col_location         = "location";
    public static final String Col_add_detalils     = "add_detalils";
    public static final String Col_start_price      = "start_price";
    public static final String Col_reverse_price    = "reverse_price";
    public static final String Col_quantity         = "quantity";
    public static final String Col_textable         = "textable";
    public static final String Col_msrp             = "msrp";
    public static final String Col_dimension        = "dimension";
    public static final String Col_remote_images    = "remote_images";
    public static final String Col_images           = "images";
    public static final String Col_thumbnail        = "thumbnail";
    public static final String Col_category         = "category";
    public static final String Col_Spinselect       = "Spinselect";
    public static final String Col_Adddetails_list  = "additional";
    public static final String Col_Category_list    = "category";
    public static final String Col_Token            = "token";
    public static final String Col_Flag             = "flag";
    public static final String Col_File             = "file";
    public static final String Col_additional_details_checklist   = "additional_details_checklist";

    private static final String CREATE_TABLE_LOT_DETAILS = "CREATE TABLE " + TABLE_NAME + " (" +
            Col_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Col_p_id + " TEXT," +
            Col_Token + " TEXT," +
            Col_auction_number + " TEXT," +
            Col_lot_number + " TEXT," +
            Col_lot_status + " TEXT," +
            Col_upc + " TEXT," +
            Col_item_title + " TEXT," +
            Col_seller_id + " TEXT," +
            Col_description + " TEXT," +
            Col_item_class + " TEXT," +
            Col_Brand + " TEXT," +
            Col_model + " TEXT," +
            Col_color + " TEXT," +
            Col_dimension + " TEXT," +
            Col_condition + " TEXT," +
            Col_location + " TEXT," +
            Col_add_detalils + " TEXT," +
            Col_start_price + " TEXT," +
            Col_reverse_price + " TEXT," +
            Col_msrp + " TEXT," +
            Col_quantity + " TEXT," +
            Col_textable + " TEXT," +
            Col_category + " TEXT," +
            Col_Spinselect + " INTEGER," +
            Col_Flag + " INTEGER," +
            Col_additional_details_checklist + " TEXT," +
            Col_remote_images + " TEXT," +
            Col_File + " TEXT," +
            Col_images + " TEXT," +
            Col_thumbnail + " BLOB" +
            ");";

    private static final String CREATE_TABLE_ADDDETAILS = "CREATE TABLE " + TABLE_NAME_ADD + " (" +
            Col_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Col_Adddetails_list + " TEXT" +
            ");";

    private static final String CREATE_TABLE_CAT = "CREATE TABLE " + TABLE_NAME_CAT + " (" +
            Col_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Col_Category_list + " TEXT" +
            ");";

    private final Context mContext;
    private SQLiteDatabase db;
    private ArrayList<LotDetails> LocalList;
    private ArrayList<String> spinList;
    private ArrayList<Add_Detail> AddDetailList;

    public MyDatabaseHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
        mContext = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_LOT_DETAILS);
            db.execSQL(CREATE_TABLE_CAT);
            db.execSQL(CREATE_TABLE_ADDDETAILS);
            Log.d("Create",CREATE_TABLE_LOT_DETAILS);
            Log.d("Create",CREATE_TABLE_CAT);
            Log.d("Create",CREATE_TABLE_ADDDETAILS);
        } catch (SQLiteException exception) {
            Log.d("Create", exception + "");

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.d("onUpgrade", "xd");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CAT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ADD);
            //db.execSQL(" DROP TABLE " + CREATE_TABLE_LOT_DETAILS + " IF EXISTS;");
            //db.execSQL(" DROP TABLE " + CREATE_TABLE_CAT + " IF EXISTS;");
           // db.execSQL(" DROP TABLE " + CREATE_TABLE_ADDDETAILS + " IF EXISTS;");
            String ff = db.getPath();
            Log.e("fff", ff);
            onCreate(db);
        } catch (SQLiteException exception) {
            Log.d("onUpgrade", exception + "");
        }
    }

    public void insertData(LotDetails l) throws IOException {

        db = this.getWritableDatabase();
        String ff = db.getPath();
        Log.e("fff", ff);
        ContentValues cv = new ContentValues();
         cv.put(Col_auction_number, l.getAuction_no());
         cv.put(Col_lot_number, l.getLot_no());
         cv.put(Col_p_id, l.getId());
         cv.put(Col_Token, l.getToken());
         cv.put(Col_lot_status, l.getLot_status());
         cv.put(Col_upc, l.getUpc());
         cv.put(Col_item_title, l.getItem_title());
         cv.put(Col_seller_id, l.getSeller_id());
         cv.put(Col_description, l.getDescription());
         cv.put(Col_item_class, l.getItem_class());
         cv.put(Col_Brand, l.getBrand());
         cv.put(Col_model, l.getModel());
         cv.put(Col_color, l.getColor());
         cv.put(Col_dimension, l.getDimension());
         cv.put(Col_condition, l.getCondition());
         cv.put(Col_location, l.getLocation());
         cv.put(Col_additional_details_checklist, l.getAdditional_details_checklist());
         cv.put(Col_add_detalils, l.getAdd_detalils());
         cv.put(Col_category, l.getCategory());
         cv.put(Col_Spinselect, l.getSpinselect());
         cv.put(Col_Flag, l.getFlag());
         cv.put(Col_start_price, l.getStart_price());
         cv.put(Col_reverse_price, l.getReverse_price());
         cv.put(Col_msrp, l.getMsrp());
         cv.put(Col_quantity, l.getQuantity());
         cv.put(Col_textable, l.getTextable());
         cv.put(Col_remote_images, l.getRemote_images());
         cv.put(Col_images, l.getImages());
         cv.put(Col_File, String.valueOf(l.getmFile()));
         /* FileInputStream fis = new FileInputStream(l.getThumbnail());
         byte[] image= new byte[fis.available()];
         fis.read(image);*/
         cv.put(Col_thumbnail, l.getThumbnail());

        Log.e("data  ", l.getToken());
        Log.e("data  ", l.getAuction_no()+"\n"+
                l.getLot_no()+"\n"+ l.getId()+"\n"+ l.getLot_status()+"\n"+ l.getUpc()+"\n"+ l.getItem_title()+"\n"+
                l.getSeller_id()+"\n"+ l.getDescription()+"\n"+ l.getItem_class()+"\n"+ l.getModel()+"\n"+ l.getBrand()+"\n"+
                l.getColor()+"\n"+ l.getDimension()+"\n"+ l.getCondition()+"\n"+l.getLocation()+"\n"+l.getAdd_detalils()+"\n"+
                l.getAdditional_details_checklist()+"\n"+ l.getCategory()+"\n"+ l.getStart_price()+"\n"+ l.getReverse_price()+"\n"+
                l.getMsrp()+"\n"+ l.getTextable()+"\n"+ l.getQuantity()+"\n"+ l.getRemote_images()+"\n"+ l.getImages()+"\n"+
                l.getThumbnail()+"\n"+l.getSpinselect()+"\n"+l.getFlag()+"\n"+l.getmFile());

        Log.e("CREATE_TABLE_LOT_DETAILS", CREATE_TABLE_LOT_DETAILS+"");
        long dd = db.insert(TABLE_NAME, null, cv);
        if(dd != -1)
        {
            Toast.makeText(mContext, "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent inext = new Intent(mContext, Success_page.class);
            inext.putExtra("name", l.getAuction_no());
            inext.putExtra("lotname", l.getLot_no());
            inext.putExtra("upc", l.getUpc());
            inext.putExtra("lot_status", l.getLot_status());
            mContext.startActivity(inext);
       }
       else{
       }
        db.close();
    }

    public void updateRecord(LotDetails l, String auction_name, String lot_no) {
        db = this.getWritableDatabase();
        String ff = db.getPath();
        Log.e("fff", ff);
        ContentValues cv = new ContentValues();
        cv.put(Col_auction_number, l.getAuction_no());
        cv.put(Col_lot_number, l.getLot_no());
        cv.put(Col_p_id, l.getId());
        cv.put(Col_Token, l.getToken());
        cv.put(Col_lot_status, l.getLot_status());
        cv.put(Col_upc, l.getUpc());
        cv.put(Col_item_title, l.getItem_title());
        cv.put(Col_seller_id, l.getSeller_id());
        cv.put(Col_description, l.getDescription());
        cv.put(Col_item_class, l.getItem_class());
        cv.put(Col_Brand, l.getBrand());
        cv.put(Col_model, l.getModel());
        cv.put(Col_color, l.getColor());
        cv.put(Col_dimension, l.getDimension());
        cv.put(Col_condition, l.getCondition());
        cv.put(Col_location, l.getLocation());
        cv.put(Col_additional_details_checklist, l.getAdditional_details_checklist());
        cv.put(Col_add_detalils, l.getAdd_detalils());
        cv.put(Col_category, l.getCategory());
        cv.put(Col_Spinselect, l.getSpinselect());
        cv.put(Col_Flag, l.getFlag());
        cv.put(Col_start_price, l.getStart_price());
        cv.put(Col_reverse_price, l.getReverse_price());
        cv.put(Col_msrp, l.getMsrp());
        cv.put(Col_quantity, l.getQuantity());
        cv.put(Col_textable, l.getTextable());
        cv.put(Col_remote_images, l.getRemote_images());
        cv.put(Col_images, l.getImages());
        cv.put(Col_thumbnail, l.getThumbnail());

        Log.e("data  ", l.getToken());
        Log.e("data  ", l.getAuction_no()+"\n"+
                l.getLot_no()+"\n"+ l.getId()+"\n"+ l.getLot_status()+"\n"+ l.getUpc()+"\n"+ l.getItem_title()+"\n"+
                l.getSeller_id()+"\n"+ l.getDescription()+"\n"+ l.getItem_class()+"\n"+ l.getModel()+"\n"+ l.getBrand()+"\n"+
                l.getColor()+"\n"+ l.getDimension()+"\n"+ l.getCondition()+"\n"+l.getLocation()+"\n"+l.getAdd_detalils()+"\n"+
                l.getAdditional_details_checklist()+"\n"+ l.getCategory()+"\n"+ l.getStart_price()+"\n"+ l.getReverse_price()+"\n"+
                l.getMsrp()+"\n"+ l.getTextable()+"\n"+ l.getQuantity()+"\n"+ l.getRemote_images()+"\n"+ l.getImages()+"\n"+
                l.getThumbnail()+"\n"+l.getSpinselect()+"\n"+l.getFlag());

        Log.e("CREATE_TABLE_LOT_DETAILS", CREATE_TABLE_LOT_DETAILS+"");
        long dd = db.update(TABLE_NAME, cv, Col_auction_number+" = "+auction_name+" and "+Col_lot_number+" = "+lot_no, null);
        if(dd != -1)
        {
//            Toast.makeText(mContext, "Lot Details Updated Successfully", Toast.LENGTH_SHORT).show();
//            Intent inext = new Intent(mContext, Success_page.class);
//            inext.putExtra("name", l.getAuction_no());
//            inext.putExtra("lotname", l.getLot_no());
//            inext.putExtra("upc", l.getUpc());
//            inext.putExtra("lot_status", l.getLot_status());
//            mContext.startActivity(inext);
        }
        else{
        }
        db.close();
    }

    public boolean CheckExist(String auction, String lot_number, String lot_status) {
        db = this.getWritableDatabase();
        Log.e("Query", "select "+Col_auction_number+" from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction+" and "+Col_lot_number+" = "+lot_number);
        Cursor c = db.rawQuery("select "+Col_auction_number+" from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction+" and "+Col_lot_number+" = "+lot_number , null);
        if(c.moveToFirst()) {
            String str = c.getString(0);
            Log.e("loading entries ", c.getCount()+"");
            Log.e("loading ", str+"");
            if (auction.equals(str)) {
                Log.e("True", "Exist");
                Toast.makeText(mContext, "Auction and Lot Number already exist!!", Toast.LENGTH_SHORT).show();
                c.close();
                return true;
            }else{
                return false;
            }
        }
        else {
            Log.e("hellooo", "sadsf");
            Intent i = new Intent(mContext, New_Lot_Entry.class);
            i.putExtra("name", auction);
            i.putExtra("lotname", lot_number);
            i.putExtra("lot_status", lot_status);
            mContext.startActivity(i);
            //Toast.makeText(mContext, "Not exist", Toast.LENGTH_SHORT).show();
        }
        c.close();
        db.close();
        return false;
    }

    /*public boolean updateData(String auction, String lot_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }*/

    public List<LotDetails> getAllRecord() {
        db = this.getWritableDatabase();
        LocalList = new ArrayList<LotDetails>();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME, null);
        if(c != null && c.moveToFirst()) {
            Log.e("loading entries ", c.getCount()+"");
            Log.e("data---> ", c.getString(c.getColumnIndex(Col_lot_number))+"\n"+
                c.getString(c.getColumnIndex(Col_auction_number))+"\n"+
                c.getString(c.getColumnIndex(Col_upc))+"\n"+
                c.getString(c.getColumnIndex(Col_item_title))+"\n"+
                c.getString(c.getColumnIndex(Col_item_class))+"\n"+
                c.getString(c.getColumnIndex(Col_seller_id))+"\n"+
                c.getString(c.getColumnIndex(Col_description))+"\n"+
                c.getString(c.getColumnIndex(Col_Brand))+"\n"+
                c.getString(c.getColumnIndex(Col_model))+"\n"+
                c.getString(c.getColumnIndex(Col_color))+"\n"+
                c.getString(c.getColumnIndex(Col_dimension))+"\n"+
                c.getString(c.getColumnIndex(Col_condition))+"\n"+
                c.getString(c.getColumnIndex(Col_location))+"\n"+
                c.getString(c.getColumnIndex(Col_add_detalils))+"\n"+
                c.getString(c.getColumnIndex(Col_lot_status))+"\n"+
                c.getString(c.getColumnIndex(Col_additional_details_checklist))+"\n"+
                c.getString(c.getColumnIndex(Col_category))+"\n"+
                c.getString(c.getColumnIndex(Col_start_price))+"\n"+
                c.getString(c.getColumnIndex(Col_reverse_price))+"\n"+
                c.getString(c.getColumnIndex(Col_msrp))+"\n"+
                c.getString(c.getColumnIndex(Col_quantity))+"\n"+
                c.getString(c.getColumnIndex(Col_textable))+"\n"+
                c.getString(c.getColumnIndex(Col_images))+"\n"+
                c.getString(c.getColumnIndex(Col_Token))+"\n"+
                c.getString(c.getColumnIndex(Col_Spinselect))+"\n"+
                c.getString(c.getColumnIndex(Col_Flag))+"\n"+
                c.getString(c.getColumnIndex(Col_File))+"\n"+
                c.getString(c.getColumnIndex(Col_remote_images)));
            do{
                LotDetails con = new LotDetails();
                con.setLot_no(c.getString(c.getColumnIndex(Col_lot_number)));
                con.setAuction_no(c.getString(c.getColumnIndex(Col_auction_number)));
                con.setThumbnail(c.getString(c.getColumnIndex(Col_thumbnail)));
                con.setUpc(c.getString(c.getColumnIndex(Col_upc)));
                con.setItem_title(c.getString(c.getColumnIndex(Col_item_title)));
                con.setItem_class(c.getString(c.getColumnIndex(Col_item_class)));
                con.setSeller_id(c.getString(c.getColumnIndex(Col_seller_id)));
                con.setDescription(c.getString(c.getColumnIndex(Col_description)));
                con.setBrand(c.getString(c.getColumnIndex(Col_Brand)));
                con.setModel(c.getString(c.getColumnIndex(Col_model)));
                con.setColor(c.getString(c.getColumnIndex(Col_color)));
                con.setDimension(c.getString(c.getColumnIndex(Col_dimension)));
                con.setCondition(c.getString(c.getColumnIndex(Col_condition)));
                con.setLocation(c.getString(c.getColumnIndex(Col_location)));
                con.setAdd_detalils(c.getString(c.getColumnIndex(Col_add_detalils)));
                con.setLot_status(c.getString(c.getColumnIndex(Col_lot_status)));
                con.setAdditional_details_checklist(c.getString(c.getColumnIndex(Col_additional_details_checklist)));
                con.setCategory(c.getString(c.getColumnIndex(Col_category)));
                con.setTextable(c.getString(c.getColumnIndex(Col_textable)));
                con.setStart_price(c.getString(c.getColumnIndex(Col_start_price)));
                con.setReverse_price(c.getString(c.getColumnIndex(Col_reverse_price)));
                con.setMsrp(c.getString(c.getColumnIndex(Col_msrp)));
                con.setQuantity(c.getString(c.getColumnIndex(Col_quantity)));
                con.setImages(c.getString(c.getColumnIndex(Col_images)));
                con.setRemote_images(c.getString(c.getColumnIndex(Col_remote_images)));
                con.setToken(c.getString(c.getColumnIndex(Col_Token)));
                con.setSpinselect(c.getInt(c.getColumnIndex(Col_Spinselect)));
                con.setFlag(c.getInt(c.getColumnIndex(Col_Flag)));
                con.setmFile(c.getString(c.getColumnIndex(Col_File)));
                LocalList.add(con);

            }while(c.moveToNext());
            c.close();
            db.close();
            return LocalList;
        }
        c.close();
        db.close();
        return LocalList;
    }

    public ArrayList<Add_Detail> getAllADetailsList() {
        AddDetailList = new ArrayList<Add_Detail>();
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME_ADD, null);
        if(c != null && c.moveToFirst()) {
            Log.e("loading entries ", c.getCount()+"");
            Log.e("data---> ",
                    c.getString(c.getColumnIndex(Col_Adddetails_list)));
            do{
                Add_Detail con = new Add_Detail();
                con.setName(c.getString(c.getColumnIndex(Col_Adddetails_list)));
                AddDetailList.add(con);
            }
            while(c.moveToNext());
            c.close();
            db.close();
            Log.e("AddDetailList", AddDetailList+"");
            return AddDetailList;
        }
        c.close();
        db.close();
        return AddDetailList;
    }

    public void insertAddD(Add_Detail a) {
        db = this.getWritableDatabase();
        String ff = db.getPath();
        Log.e("fff", ff);
        ContentValues cv = new ContentValues();
        cv.put(Col_Adddetails_list, a.getName());
        Log.e("CREATE_TABLE_ADDDETAILS", CREATE_TABLE_ADDDETAILS+"");
        db.insert(TABLE_NAME_ADD, null, cv);
        db.close();
    }

    public void insertCat(String s) {

        db = this.getWritableDatabase();
        String ff = db.getPath();
        Log.e("fff", ff);
        ContentValues cv = new ContentValues();
        cv.put(Col_Category_list, s+"");
        Log.e("datadd", s+"");
        Log.e("CREATE_TABLE_CAT", CREATE_TABLE_CAT+"");
        db.insert(TABLE_NAME_CAT, null, cv);
        db.close();
    }

    public ArrayList<String> getCatList() {
        spinList = new ArrayList<String>();
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME_CAT, null);
        if(c != null && c.moveToFirst()) {
            Log.e("loading entries ", c.getCount()+"");
            Log.e("data---> ",
                    c.getString(c.getColumnIndex(Col_Category_list)));
            do{
                //Add_Detail con = new Add_Detail();
               // con.setName(c.getString(c.getColumnIndex(Col_Category_list)));
                spinList.add(c.getString(c.getColumnIndex(Col_Category_list)));
            }
            while(c.moveToNext());
            c.close();
            db.close();
            Log.e("spinList", spinList+"");
            return spinList;
        }
        c.close();
        db.close();
        return spinList;
    }

    public List<LotDetails> getAuctionRecord(String auction_name) {
        db = this.getWritableDatabase();
        LocalList = new ArrayList<LotDetails>();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction_name, null);

        if(c != null && c.moveToFirst()) {
            Log.e("loading entries ", c.getCount()+"");
            Log.e("data---> ", c.getString(c.getColumnIndex(Col_lot_number))+"\n"+
                    c.getString(c.getColumnIndex(Col_auction_number))+"\n"+
                    c.getString(c.getColumnIndex(Col_upc))+"\n"+
                    c.getString(c.getColumnIndex(Col_item_title))+"\n"+
                    c.getString(c.getColumnIndex(Col_item_class))+"\n"+
                    c.getString(c.getColumnIndex(Col_seller_id))+"\n"+
                    c.getString(c.getColumnIndex(Col_description))+"\n"+
                    c.getString(c.getColumnIndex(Col_Brand))+"\n"+
                    c.getString(c.getColumnIndex(Col_model))+"\n"+
                    c.getString(c.getColumnIndex(Col_color))+"\n"+
                    c.getString(c.getColumnIndex(Col_dimension))+"\n"+
                    c.getString(c.getColumnIndex(Col_condition))+"\n"+
                    c.getString(c.getColumnIndex(Col_location))+"\n"+
                    c.getString(c.getColumnIndex(Col_add_detalils))+"\n"+
                    c.getString(c.getColumnIndex(Col_lot_status))+"\n"+
                    c.getString(c.getColumnIndex(Col_additional_details_checklist))+"\n"+
                    c.getString(c.getColumnIndex(Col_category))+"\n"+
                    c.getString(c.getColumnIndex(Col_start_price))+"\n"+
                    c.getString(c.getColumnIndex(Col_reverse_price))+"\n"+
                    c.getString(c.getColumnIndex(Col_msrp))+"\n"+
                    c.getString(c.getColumnIndex(Col_quantity))+"\n"+
                    c.getString(c.getColumnIndex(Col_textable))+"\n"+
                    c.getString(c.getColumnIndex(Col_images))+"\n"+
                    c.getString(c.getColumnIndex(Col_thumbnail))+"\n"+
                    c.getInt(c.getColumnIndex(Col_Spinselect))+"\n"+
                    c.getInt(c.getColumnIndex(Col_Flag))+"\n"+
                    c.getString(c.getColumnIndex(Col_remote_images)));
            do{
                LotDetails con = new LotDetails();
                con.setLot_no(c.getString(c.getColumnIndex(Col_lot_number)));
                con.setAuction_no(c.getString(c.getColumnIndex(Col_auction_number)));
                con.setThumbnail(c.getString(c.getColumnIndex(Col_thumbnail)));
                con.setUpc(c.getString(c.getColumnIndex(Col_upc)));
                con.setItem_title(c.getString(c.getColumnIndex(Col_item_title)));
                con.setItem_class(c.getString(c.getColumnIndex(Col_item_class)));
                con.setSeller_id(c.getString(c.getColumnIndex(Col_seller_id)));
                con.setDescription(c.getString(c.getColumnIndex(Col_description)));
                con.setBrand(c.getString(c.getColumnIndex(Col_Brand)));
                con.setModel(c.getString(c.getColumnIndex(Col_model)));
                con.setColor(c.getString(c.getColumnIndex(Col_color)));
                con.setDimension(c.getString(c.getColumnIndex(Col_dimension)));
                con.setCondition(c.getString(c.getColumnIndex(Col_condition)));
                con.setLocation(c.getString(c.getColumnIndex(Col_location)));
                con.setAdd_detalils(c.getString(c.getColumnIndex(Col_add_detalils)));
                con.setLot_status(c.getString(c.getColumnIndex(Col_lot_status)));
                con.setAdditional_details_checklist(c.getString(c.getColumnIndex(Col_additional_details_checklist)));
                con.setCategory(c.getString(c.getColumnIndex(Col_category)));
                con.setTextable(c.getString(c.getColumnIndex(Col_textable)));
                con.setStart_price(c.getString(c.getColumnIndex(Col_start_price)));
                con.setReverse_price(c.getString(c.getColumnIndex(Col_reverse_price)));
                con.setMsrp(c.getString(c.getColumnIndex(Col_msrp)));
                con.setQuantity(c.getString(c.getColumnIndex(Col_quantity)));
                con.setImages(c.getString(c.getColumnIndex(Col_images)));
                con.setSpinselect(c.getInt(c.getColumnIndex(Col_Spinselect)));
                con.setFlag(c.getInt(c.getColumnIndex(Col_Flag)));
                con.setRemote_images(c.getString(c.getColumnIndex(Col_remote_images)));
                LocalList.add(con);

            }while(c.moveToNext());
            c.close();
            db.close();
            return LocalList;
        }
        c.close();
        db.close();
        return LocalList;
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }

    public int getProfilesCountAuction(String auction_name) {
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction_name, null);
        int cnt = c.getCount();
        c.close();
        return cnt;
    }

    public int getProfilesCountAdd() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_NAME_ADD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getProfilesCountSpinn() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_NAME_CAT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /*public void updateRecord(Contact c){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("==>>In Database Update", ""+c.getId());
        ContentValues cv  = new ContentValues();
        cv.put(COL_ID, c.getId());
        cv.put(COL_NAME, c.getName());
        cv.put(COL_NUM, c.getNumber());

        db.update(TABLE_NAME, cv, COL_ID+"=?", new String[]{String.valueOf(c.getId())});//Integer.toString(c.getId());
        db.close();
    }*/

    public boolean deleteTitle(String Auction_name)
    {
        Log.e("Delete", TABLE_NAME+ Col_auction_number + "=" + Auction_name);
        return db.delete(TABLE_NAME, Col_auction_number + "=" + Auction_name, null) > 0;
    }

    public void deleteTitleAdd()
    {
        Log.e("Delete", "delete from "+ TABLE_NAME_ADD);
        db.execSQL("delete from "+ TABLE_NAME_ADD);
    }

    public void deleteTitleCat()
    {
        Log.e("Delete", "delete from "+ TABLE_NAME_CAT);
        db.execSQL("delete from "+ TABLE_NAME_CAT);
    }

    public List<LotDetails> GetDataForAuction(String auction_name, String lot_no) {
        db = this.getWritableDatabase();
        LocalList = new ArrayList<LotDetails>();
        Log.e("Query", "select * from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction_name+" and " +Col_lot_number+" = "+lot_no);
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction_name+" and " +Col_lot_number+" = "+lot_no , null);
        if(c != null && c.moveToFirst()) {
            Log.e("loading entries ", c.getCount()+"");
            Log.e("data---> ", c.getString(c.getColumnIndex(Col_lot_number))+"\n"+
                    c.getString(c.getColumnIndex(Col_auction_number))+"\n"+
                    c.getString(c.getColumnIndex(Col_upc))+"\n"+
                    c.getString(c.getColumnIndex(Col_item_title))+"\n"+
                    c.getString(c.getColumnIndex(Col_item_class))+"\n"+
                    c.getString(c.getColumnIndex(Col_seller_id))+"\n"+
                    c.getString(c.getColumnIndex(Col_description))+"\n"+
                    c.getString(c.getColumnIndex(Col_Brand))+"\n"+
                    c.getString(c.getColumnIndex(Col_model))+"\n"+
                    c.getString(c.getColumnIndex(Col_color))+"\n"+
                    c.getString(c.getColumnIndex(Col_dimension))+"\n"+
                    c.getString(c.getColumnIndex(Col_condition))+"\n"+
                    c.getString(c.getColumnIndex(Col_location))+"\n"+
                    c.getString(c.getColumnIndex(Col_add_detalils))+"\n"+
                    c.getString(c.getColumnIndex(Col_lot_status))+"\n"+
                    c.getString(c.getColumnIndex(Col_additional_details_checklist))+"\n"+
                    c.getString(c.getColumnIndex(Col_category))+"\n"+
                    c.getString(c.getColumnIndex(Col_start_price))+"\n"+
                    c.getString(c.getColumnIndex(Col_reverse_price))+"\n"+
                    c.getString(c.getColumnIndex(Col_msrp))+"\n"+
                    c.getString(c.getColumnIndex(Col_quantity))+"\n"+
                    c.getString(c.getColumnIndex(Col_textable))+"\n"+
                    c.getString(c.getColumnIndex(Col_images))+"\n"+
                    c.getString(c.getColumnIndex(Col_Token))+"\n"+
                    c.getString(c.getColumnIndex(Col_Spinselect))+"\n"+
                    c.getString(c.getColumnIndex(Col_Flag))+"\n"+
                    c.getString(c.getColumnIndex(Col_File))+"\n"+
                    c.getString(c.getColumnIndex(Col_remote_images)));
            do{
                LotDetails con = new LotDetails();
                con.setLot_no(c.getString(c.getColumnIndex(Col_lot_number)));
                con.setAuction_no(c.getString(c.getColumnIndex(Col_auction_number)));
                con.setThumbnail(c.getString(c.getColumnIndex(Col_thumbnail)));
                con.setUpc(c.getString(c.getColumnIndex(Col_upc)));
                con.setItem_title(c.getString(c.getColumnIndex(Col_item_title)));
                con.setItem_class(c.getString(c.getColumnIndex(Col_item_class)));
                con.setSeller_id(c.getString(c.getColumnIndex(Col_seller_id)));
                con.setDescription(c.getString(c.getColumnIndex(Col_description)));
                con.setBrand(c.getString(c.getColumnIndex(Col_Brand)));
                con.setModel(c.getString(c.getColumnIndex(Col_model)));
                con.setColor(c.getString(c.getColumnIndex(Col_color)));
                con.setDimension(c.getString(c.getColumnIndex(Col_dimension)));
                con.setCondition(c.getString(c.getColumnIndex(Col_condition)));
                con.setLocation(c.getString(c.getColumnIndex(Col_location)));
                con.setAdd_detalils(c.getString(c.getColumnIndex(Col_add_detalils)));
                con.setLot_status(c.getString(c.getColumnIndex(Col_lot_status)));
                con.setAdditional_details_checklist(c.getString(c.getColumnIndex(Col_additional_details_checklist)));
                con.setCategory(c.getString(c.getColumnIndex(Col_category)));
                con.setTextable(c.getString(c.getColumnIndex(Col_textable)));
                con.setStart_price(c.getString(c.getColumnIndex(Col_start_price)));
                con.setReverse_price(c.getString(c.getColumnIndex(Col_reverse_price)));
                con.setMsrp(c.getString(c.getColumnIndex(Col_msrp)));
                con.setQuantity(c.getString(c.getColumnIndex(Col_quantity)));
                con.setImages(c.getString(c.getColumnIndex(Col_images)));
                con.setRemote_images(c.getString(c.getColumnIndex(Col_remote_images)));
                con.setToken(c.getString(c.getColumnIndex(Col_Token)));
                con.setSpinselect(c.getInt(c.getColumnIndex(Col_Spinselect)));
                con.setFlag(c.getInt(c.getColumnIndex(Col_Flag)));
                con.setmFile(c.getString(c.getColumnIndex(Col_File)));
                LocalList.add(con);

            }while(c.moveToNext());
            c.close();
            db.close();
            return LocalList;
        }
        c.close();
        db.close();
        return LocalList;
    }

    public int CheckExistAL(String auction_name, String lot_no, String lot_s) {
        db = this.getWritableDatabase();
        Log.e("Query", "select * from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction_name+" and "+Col_lot_number+" = "+lot_no);
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where "+Col_auction_number+" = "+auction_name+" and "+Col_lot_number+" = "+lot_no , null);
        if(c != null && !c.isClosed()){
            c.getCount();
            c.close();
        }
        db.close();
        return c.getCount();
    }

    public void changeFlag(String auction_name, String lot_no) {
        db = this.getWritableDatabase();
        String ff = db.getPath();
        Log.e("fff", ff);
        ContentValues cv = new ContentValues();
        cv.put(Col_Flag, 1);
        Log.e("CREATE_TABLE_LOT_DETAILS", CREATE_TABLE_LOT_DETAILS+"");
        long dd = db.update(TABLE_NAME, cv, Col_auction_number+" = "+auction_name+" and "+Col_lot_number+" = "+lot_no, null);
        if(dd != -1) {
        }
        else{
        }
        db.close();
    }
}

