package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darshan on 11/28/2017.
 */

public class CustomLocalAdapter extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;
        private List<LotDetails> LocalList;
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

        public CustomLocalAdapter(Activity activity, List<LotDetails> LocalList) {
            this.activity = activity;
            this.LocalList = LocalList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return LocalList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int location) {
            return LocalList.get(location);
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
            item_title          = (TextView) view.findViewById(R.id.item_title);
            auction_no          = (TextView) view.findViewById(R.id.auction_no);
            ImageView thumbNailL = (ImageView) view.findViewById(R.id.thumbnailL);
            ImageView error = (ImageView) view.findViewById(R.id.error);
            LinearLayout line = (LinearLayout)view.findViewById(R.id.line);

            NetworkImageView thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);

            Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
            item_title.setTypeface(typeface);
            auction_no.setTypeface(typeface);
            lot_no.setTypeface(typeface);

            LotDetails c = LocalList.get(position);
            lot_no.setText("Lot No. "+ c.getLot_no());
            if(c.getLot_status().equals(null))
            {
                lot_status = "draft";
            }
            else {
                lot_status = c.getLot_status();
                Log.e("lot_status", lot_status);
            }

            if(c.getAuction_no() == "null")
            {
                auction_no.setText("");
            }
            else
            {
                auction_no.setText("Auction No. " + c.getAuction_no());
            }

            if(c.getItem_title() == "null")
            {
                item_title.setText("");
            }
            else
            {
                item_title.setText(c.getItem_title());
            }
            if(c.getFlag() == 1)
            {
                error.setVisibility(view.VISIBLE);
            }
            Log.e("thumbNail",c.getThumbnail());
            thumbNailL.setVisibility(View.VISIBLE);
            thumbNail.setVisibility(View.GONE);
            //thumbNail.setBackgroundResource(R.drawable.no_media);
            if(c.getThumbnail().equals(null))
            {
                thumbNailL.setImageResource(R.drawable.no_media);
            }
            else
            {
                if(c.getThumbnail().contains("https://")) {
                    //Toast.makeText(activity, c.getThumbnail(), Toast.LENGTH_SHORT).show();
                    thumbNail.setVisibility(View.VISIBLE);
                    thumbNailL.setVisibility(View.GONE);
                    thumbNail.setImageUrl(c.getThumbnail(), imageLoader);
                } else if(c.getThumbnail().contains("http://")) {
                    // Toast.makeText(activity, c.getThumbnail(), Toast.LENGTH_SHORT).show();
                    thumbNail.setVisibility(View.VISIBLE);
                    thumbNailL.setVisibility(View.GONE);
                    thumbNail.setImageUrl(c.getThumbnail(), imageLoader);
                } else {
                    File imgFile = new File(c.getThumbnail());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        thumbNailL.setImageBitmap(myBitmap);
                    }
                }
            }
            return view;
        }

    }