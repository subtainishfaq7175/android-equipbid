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
 * Created by desktop on 12/1/2016.
 */
public class CustomListLotAdapter extends BaseAdapter {

    private final Icustomelist mCallback;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Lot> categoryList;
    ArrayList<Lot> objects_cat;
    private int test = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private NetworkImageView imgBillionaire;
    private String lot_status;
    private String thumb;

    public CustomListLotAdapter(Activity activity, List<Lot> categoryList, Icustomelist mCallback) {
        this.activity = activity;
        this.categoryList = categoryList;
        this.mCallback = mCallback;
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
        TextView item_title;
        TextView auction_no;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_row_lot, parent, false);
        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);
        lot_no      = (TextView) view.findViewById(R.id.lot_no);
        //description = (TextView) view.findViewById(R.id.description);
        item_title  = (TextView) view.findViewById(R.id.item_title);
        auction_no  = (TextView) view.findViewById(R.id.auction_no);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        item_title.setTypeface(typeface);
        auction_no.setTypeface(typeface);
        lot_no.setTypeface(typeface);

        Lot c = categoryList.get(position);
        lot_no.setText("Lot No. "+c.getLotno());

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

        lot_status = c.getLotstatus();
        Log.e("lot_status", lot_status);

        //thumbNail.setBackgroundResource(R.drawable.no_media);

        Log.e("dfgdfgfhggfhgfhgh", c.getImagesUrl());
        thumb = c.getImagesUrl();
        if(thumb.equals("null"))
        {
            thumbNail.setBackgroundResource(R.drawable.no_media);
        }
        else
        {
            thumbNail.setImageUrl(thumb, imageLoader);
        }
        /*if(c.getThumbnail() != "null")
        {
            thumbNail.setImageUrl(c.getThumbnail(), imageLoader);
        }
        else
        {
            thumbNail.setImageResource(R.drawable.img);
        }
        Log.e("gh", c.getThumbnail());
        // thumbNail.setImageResource(R.mipmap.ic_launcher);*/

        return view;
    }

    public interface Icustomelist {
        void loadMoreListView();
    }
}