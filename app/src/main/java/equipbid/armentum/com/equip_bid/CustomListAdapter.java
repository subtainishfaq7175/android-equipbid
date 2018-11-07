package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desktop on 11/29/2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private final String user_role_label;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Auction> categoryList;
    ArrayList<Auction> objects_cat;
    private int test = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private NetworkImageView imgBillionaire;
    private String Auct_name;
    private String thumb;
    private ImageView edit;

    // private ViewHolder viewHolder;

    public CustomListAdapter(Activity activity, List<Auction> categoryList, String user_role_label) {
        this.activity = activity;
        this.categoryList = categoryList;
        this.user_role_label = user_role_label;
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

        TextView a_name;
        TextView des_auction;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_row, parent, false);
        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);
        a_name = (TextView) view.findViewById(R.id.a_name);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        a_name.setTypeface(typeface);

        edit = (ImageView)view.findViewById(R.id.edit);
        if(user_role_label.equals("Super Admin"))
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
        }
        Auction c = categoryList.get(position);
        a_name.setText("Auction No: "+ c.getName());

        thumb = c.getThumbnail();

        Log.e("dfgdfgfhggfhgfhgh", c.getThumbnail());
        if(c.getThumbnail().equals("null"))
        {
            thumbNail.setBackgroundResource(R.drawable.no_media);
        }
        else
        {
            thumbNail.setImageUrl(thumb, imageLoader);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = categoryList.get(position).getName();
                //Toast.makeText(activity, categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, Add_New_Auction.class);
                i.putExtra("name",categoryList.get(position).getName());
                i.putExtra("temp","History");
                activity.startActivity(i);
               // activity.finish();
            }
        });
        Log.e("position","...."+ position);
        Log.e("categoryList","...."+ categoryList.size());

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