package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desktop on 12/5/2016.
 */

public class GallaryAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Gallary> LotimgList;
    ArrayList<Gallary> objects_cat;
    private int test = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private NetworkImageView thumbnailget;
    private String Auct_name;
    private String url,id,title;

    // private ViewHolder viewHolder;

    public GallaryAdapter(Activity activity, List<Gallary> LotimgList) {
        this.activity = activity;
        this.LotimgList = LotimgList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return LotimgList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int location) {
        return LotimgList.get(location);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_row_gallary, parent, false);
        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        thumbnailget = (NetworkImageView) view.findViewById(R.id.thumbnailget);

        Gallary c = LotimgList.get(position);
        url = c.getImagesUrl();
        id = c.getImgId();
        title = c.getImgTitle();

        Log.e("position...", ""+position);
        thumbnailget.setImageUrl(id, imageLoader);

        return thumbnailget;
    }

}