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
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;

/**
 * Created by Darshan on 10/25/2017.
 */

class UserAdapter  extends BaseAdapter {

    private final Activity activity;
    private final LayoutInflater inflater;
    private List<User> userList;
    private ImageLoader imageLoader;
    private User u;
    private String thumb;
    private TextView name, email, role;
    private Button load_more;
    IUserlist mCallback;

    public UserAdapter(Activity activity, List<User> userList, IUserlist mCallback) {
        this.activity = activity;
        this.userList = userList;
        this.mCallback = mCallback;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int location) {
        return userList.get(location);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView a_name;
        TextView des_auction;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.sample, parent, false);
        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);
        name        = (TextView) view.findViewById(R.id.name);
        email       = (TextView) view.findViewById(R.id.email);
        role        = (TextView) view.findViewById(R.id.role) ;
        u           = userList.get(position);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        name.setTypeface(typeface);
        email.setTypeface(typeface);
        role.setTypeface(typeface);
        u.getId();
        name.setText(u.getName());
        email.setText(u.getUser_email());
        role.setText(u.getRole());

        thumb = u.getProfile_image();

        Log.e("position","...."+ position);
        Log.e("categoryList","...."+ userList.size());

        return view;
    }

    public interface IUserlist{
        void loadMoreListView();

    }

    public void xyz(List userList) {
        userList.addAll(userList);
        notifyDataSetChanged();
    }

}
