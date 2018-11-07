package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by desktop on 12/28/2016.
 */
public class CustomImgAdapter extends BaseAdapter {
    Context context;
    private Activity activity;
    List<GridItemImg> GallimgList;
    private LayoutInflater inflater;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String thumb;
    private ImageView thumbNail;
    private ImageView thumbnailget;
    private String url;

    public CustomImgAdapter(Activity activity, List<GridItemImg> GallimgList) {
        this.activity = activity;
        this.GallimgList = GallimgList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return GallimgList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int location) {
        return GallimgList.get(location);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.preview_img, parent, false);
        }

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        thumbnailget = (ImageView) view.findViewById(R.id.thumbnail);

        GridItemImg c = GallimgList.get(position);
        url = c.getImagesUrl();
        Bitmap myBitmap = BitmapFactory.decodeFile(c.getImagesUrl());
        thumbnailget.setImageBitmap(myBitmap);
        return view;
    }
}