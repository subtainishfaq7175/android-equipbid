package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desktop on 12/7/2016.
 */

public class CustomLotListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Lot_Auction> categoryList;
    ArrayList<Lot_Auction> objects_cat;
    private int test = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private NetworkImageView imgBillionaire;
    private String Auct_name;
    private String lot_status;
    private String name;
    private TextView item_title;
    private TextView auction_no;
    private String thumb;

    public CustomLotListAdapter(Activity activity, List<Lot_Auction> categoryList) {
        this.activity = activity;
        this.categoryList = categoryList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int location) {
        return categoryList.get(location);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView lot_no;
        TextView lot_details;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.auction_lot_list_row, parent, false);
        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        lot_no = (TextView) view.findViewById(R.id.lot_no);
        item_title  = (TextView) view.findViewById(R.id.item_title);
        auction_no  = (TextView) view.findViewById(R.id.auction_no);
        NetworkImageView thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        item_title.setTypeface(typeface);
        auction_no.setTypeface(typeface);
        lot_no.setTypeface(typeface);

        Lot_Auction c = categoryList.get(position);
        lot_no.setText("Lot No. "+ c.getLotno());
        lot_status = c.getLotstatus();
        lot_status = c.getLotstatus();
        Log.e("lot_status", lot_status);

        if(c.getAuctionno() == "null")
        {
            auction_no.setText("");
        }
        else
        {
            auction_no.setText("Auction No. " + c.getAuctionno());
        }

        if(c.getItemtitle() == "null")
        {
            item_title.setText("");
        }
        else
        {
            item_title.setText(c.getItemtitle());
        }

        thumb = c.getImagesUrl();
        if(thumb.equals("null"))
        {
            thumbNail.setBackgroundResource(R.drawable.no_media);
        }
        else
        {
            thumbNail.setImageUrl(thumb, imageLoader);
        }

        return view;
    }

}