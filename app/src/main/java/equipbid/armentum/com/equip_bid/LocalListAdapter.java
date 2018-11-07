package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darshan on 11/21/2017.
 */

public class LocalListAdapter extends BaseAdapter {
    private final String user_role_label;
    private List<LotDetails> LocalList;
    private Activity activity;
    private LayoutInflater inflater;
    ArrayList<Auction> objects_cat;
    private int test = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private NetworkImageView imgBillionaire;
    private String Auct_name;
    private String thumb;
    private ImageView edit;
    private Bitmap bitmap;
    private URL url;

    // private ViewHolder viewHolder;

    public LocalListAdapter(Activity activity, List<LotDetails> LocalList, String user_role_label) {
        this.activity = activity;
        this.LocalList = LocalList;
        this.user_role_label = user_role_label;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(List<LotDetails> LocalList)
    {
        this.LocalList = LocalList;
        this.notifyDataSetChanged();
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

        TextView a_name;
        TextView des_auction;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_row, parent, false);
        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);
        ImageView thumbNailL = (ImageView) view.findViewById(R.id.thumbnailL);
        LinearLayout line = (LinearLayout)view.findViewById(R.id.line);
        a_name = (TextView) view.findViewById(R.id.a_name);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        a_name.setTypeface(typeface);

        edit = (ImageView)view.findViewById(R.id.edit);
        edit.setVisibility(View.INVISIBLE);
       /* if(user_role_label.equals("Super Admin"))
        {
            edit.setVisibility(View.VISIBLE);
        }
        else if(user_role_label.equals("Admin"))
        {
            edit.setVisibility(View.VISIBLE);
        }
        else
        {
            edit.setVisibility(View.GONE);
        }*/
        LotDetails c = LocalList.get(position);
        a_name.setText("Auction No: "+ c.getAuction_no());

        Log.e("getThumbnail", c.getThumbnail() +" fdzzxzxf");

        if(c.getFlag() == 1)
        {
            line.setBackgroundResource(R.drawable.text_view_red);
        }
        thumbNailL.setVisibility(View.VISIBLE);
        thumbNail.setVisibility(View.GONE);
        if(c.getThumbnail().equals(""))
        {
            thumbNailL.setImageResource(R.drawable.no_media);
        }
        else
            if(c.getThumbnail().contains("https://")) {
                //Toast.makeText(activity, c.getThumbnail(), Toast.LENGTH_SHORT).show();
                thumbNail.setVisibility(View.VISIBLE);
                thumbNailL.setVisibility(View.GONE);
                thumbNail.setImageUrl(c.getThumbnail(), imageLoader);
            }
            else if(c.getThumbnail().contains("http://")) {
               // Toast.makeText(activity, c.getThumbnail(), Toast.LENGTH_SHORT).show();
                thumbNail.setVisibility(View.VISIBLE);
                thumbNailL.setVisibility(View.GONE);
                thumbNail.setImageUrl(c.getThumbnail(), imageLoader);
            }else {
                File imgFile = new  File(c.getThumbnail());
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    thumbNailL.setImageBitmap(myBitmap);
                }
            }
        {

        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = LocalList.get(position).getAuction_no();
                //Toast.makeText(activity, categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, Add_New_Auction.class);
                i.putExtra("name",LocalList.get(position).getAuction_no());
                i.putExtra("temp","History");
                activity.startActivity(i);
                // activity.finish();
            }
        });
        Log.e("position","...."+ position);
        Log.e("categoryList","...."+ LocalList.size());

        return view;
    }

    public interface Icustomelist{
        void loadMoreListView();
    }

    public void xyz(List categoryList) {
        categoryList.addAll(categoryList);
        notifyDataSetChanged();
    }
}