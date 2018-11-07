package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desktop on 12/7/2016.
 */
public class GAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<GallaryN> mylist;
    ArrayList<GallaryN> objects_cat;
    private int test = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private NetworkImageView thumbnailget;
    private String Auct_name;
    private String url,id,title;

    public GAdapter(Activity activity, List<GallaryN> mylist) {
        this.activity = activity;
        this.mylist = mylist;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int location) {
        return mylist.get(location);
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

        GallaryN c = mylist.get(position);
        url = c.getImgu();

        thumbnailget.setImageUrl(url, imageLoader);
        return view;
    }

    public void setGridData(List<GallaryN> mylist) {
        this.mylist = mylist;
        notifyDataSetChanged();
    }
}
