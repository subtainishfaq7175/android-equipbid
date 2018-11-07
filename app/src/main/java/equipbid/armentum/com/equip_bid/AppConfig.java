package equipbid.armentum.com.equip_bid;

import java.security.PublicKey;

/**
 * Created by DELL on 7/6/2016.
 */
public class AppConfig {

    public static String DemoLogin = "http://demo.armentum.co/equip-bid/wp-json/";
    public static String Demo = "http://demo.armentum.co/equip-bid/wp-json/inbago/v2/";
    public static String Demo1 = "http://demo.armentum.co/equip-bid//wp-json/inbago/v2/";
    public static String DemoA = "http://demo.armentum.co/equip-bid/wp-json/wp/v2/";
    public static String Live = "http://admin.equipbid.biz/";


    // LIVE
    public static String URL_LOGIN = Live+"wp-json/jwt-auth/v1/token";
    public static String URL_REGISTER = Live+"wp-json/inbago/v2/users";
    public static String URL_USER = Live+"wp-json/inbago/v2/users/";
    public static String URL_FORGOT = Live+"wp-json/inbago/v2/forgot";
    public static String URL_Auction_new = Live+"wp-json/inbago/v2/auctions?auction_no=";
    public static String URL_lot_details = Live+"wp-json/inbago/v2/lots/?auction_no=";
    public static String URL_auction_search = Live+"wp-json/inbago/v2/auctions?s=";
    public static String URL_auction_search_new = Live+"wp-json/inbago/v2/auctions?auction_no=&page=";
    public static String URL_lot_search= Live+"wp-json/inbago/v2/lots/?s=";
    public static String URL_Duplicate_lot= Live+"wp-json/inbago/v2/lots/?duplicate=1&lot_no=";
    public static String URL_New_lot= Live+"wp-json/inbago/v2/lots/?auction_no=";
    public static String URL_New_API_product= "https://api.upcitemdb.com/prod/v1/lookup?upc=";
    public static String URL_auction_lot_search= Live+"wp-json/inbago/v2/auctions?auction_no=";
    public static String URL_Category= Live+"wp-json/inbago/v2/terms?taxonomy=product_cat";
    public static String URL_Add_Details= Live+"wp-json/wp/v2/additional_detail";
    public static String User_Details= Live+"wp-json/inbago/v2/users?offset=0&page=";
    public static String User_Details_All= Live+"wp-json/inbago/v2/users?offset=0&per_page=500";
    public static String Update_Delete_API= Live+"/wp-json/inbago/v2/admin-lots/";
    public static String Super_Admin_Auction_list= Live+"wp-json/inbago/v2/super-admin-auctions?page=";
    public static String Super_Admin_Lots_list= Live+"wp-json/inbago/v2/super-admin-lots?page=";
    public static String Super_Admin_Lots_list_s= Live+"wp-json/inbago/v2/super-admin-lots?s=";
    public static String Super_Admin_Auction_list_s= Live+"wp-json/inbago/v2/super-admin-auctions?s=";


}