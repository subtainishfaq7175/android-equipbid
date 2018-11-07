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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meet_darshan on 01-02-2017.
 */
public class SpinnerAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<String> spinList;
        private int test = 0;
        private String Auct_name;
        private String thumb;
        private TextView spinvalue;

    // private ViewHolder viewHolder;

        public SpinnerAdapter(Activity activity, ArrayList<String> spinList) {
        this.activity = activity;
        this.spinList = spinList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

        @Override
        public int getCount() {
        return spinList.size();
    }

        @Override
        public long getItemId(int position) {
        return position;
    }

        @Override
        public Object getItem(int location) {
        return spinList.get(location);
    }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        spinvalue = (TextView) view.findViewById(R.id.spinvalue);
        spinvalue.setTypeface(typeface);
        spinvalue.setText(spinList.get(position));
        return view;
    }
}
