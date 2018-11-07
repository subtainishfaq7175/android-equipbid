package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import equipbid.armentum.com.equip_bid.helper.Utils;
import equipbid.armentum.com.equip_bid.photoview.PhotoViewAttacher;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Lot_details_Img extends AppCompatActivity {

    private FullScreenImageAdapter1 adapter;
    private ViewPager viewPager;
    private ArrayList<String> imagePaths_external;
    private ArrayList<String> imagePaths;
    private ArrayList<String> imagePaths_All;
    private String Lot_S;
    private String lot_status,_item_title, _seller_id, _item_class, description, category, _location, _condition, _add_detalils;
    private String _start_price,_msrp, _end_price, _quantity, _textable, _images_arr, lot_no, Auction_name, upc, images;
    private String imagePaths_str, LocalVar, remote_images, remote_images_, _color,_model, _brand, _dimension;
    private String multiple_select;
    private ArrayList<String> arrayList;
    private ArrayList<String> spin_array;
    private ArrayList<String> detailsSelectlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_details__img);

        Bundle bundle       = getIntent().getExtras();
        LocalVar            = bundle.getString("LocalVar");
        Lot_S               = bundle.getString("Lot_S");
        Auction_name        = bundle.getString("name");
        lot_no              = bundle.getString("lotname");
        upc                 = bundle.getString("upc");
        lot_status          = bundle.getString("lot_status");
        _item_title         = bundle.getString("item_title");
        _seller_id          = bundle.getString("seller_id");
        _item_class         = bundle.getString("item_class");
        description         = bundle.getString("description");
        category            = bundle.getString("category");
        _location           = bundle.getString("location");
        _condition          = bundle.getString("condition");
        _add_detalils       = bundle.getString("add_detalils");
        _start_price        = bundle.getString("start_price");
        _color              = bundle.getString("color");
        _model              = bundle.getString("model");
        _brand              = bundle.getString("brand");
        _dimension          = bundle.getString("dimension");
        _msrp               = bundle.getString("msrp");
        _end_price          = bundle.getString("end_price");
        _quantity           = bundle.getString("quantity");
        _textable           = bundle.getString("textable");
        _images_arr         = bundle.getString("images");
        LocalVar            = bundle.getString("LocalVar");
        imagePaths_str      = bundle.getString("imagePaths_str");
        remote_images_      = bundle.getString("imagePaths_ext_str");
        multiple_select     = bundle.getString("multiple_select");
        arrayList           = getIntent().getStringArrayListExtra("stringArray");
        imagePaths          = getIntent().getStringArrayListExtra("imagePaths");
        spin_array          = getIntent().getStringArrayListExtra("spin_array");
        imagePaths_external = getIntent().getStringArrayListExtra("imagePaths_external");
        detailsSelectlist   = getIntent().getStringArrayListExtra("detailsSelectlist");

        Log.e("Auction_Details", Auction_name +" "+lot_no +" "+ _seller_id+" "+upc+" "+lot_status+" "+_item_title+" "+_item_class);
        Log.e("LocalVar", LocalVar);
        Log.e("Lot_S", Lot_S);
        imagePaths_external = getIntent().getStringArrayListExtra("imagePaths_external");
        imagePaths = getIntent().getStringArrayListExtra("imagePaths");

        imagePaths_All     = new ArrayList<String>();
        Log.e("imagePaths_external", ""+imagePaths_external);
        Log.e("imagePaths", ""+imagePaths);
        Log.e("imagePaths_All..1", ""+imagePaths_All);

        imagePaths_external.addAll(imagePaths);

        Log.e("imagePaths_external..2", ""+imagePaths_external);

        imagePaths_All.addAll(imagePaths_external);

        Log.e("imagePaths_All..3", ""+imagePaths_All);

    }


    public class FullScreenImageAdapter1 extends PagerAdapter {

        private LayoutInflater inflater = null;
        private ImageView imgDisplay, btnClose, btnDelete;
        private Activity _activity;
        private ArrayList<String> imagePaths_All;

        public FullScreenImageAdapter1(Activity activity, ArrayList<String> imagePaths_All) {
            // TODO Auto-generated constructor stub
            this._activity = activity;
            this.imagePaths_All = imagePaths_All;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return imagePaths_All.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.preview_images, container, false);

            imgDisplay  = (ImageView) viewLayout.findViewById(R.id.thumbnail);
            btnClose    = (ImageView) viewLayout.findViewById(R.id.btnClose);
            btnDelete   = (ImageView) viewLayout.findViewById(R.id.btnDelete);

            Log.e("positionB",""+(position));
            Log.e("imagePaths_All", ""+imagePaths_All);

            for (int i = 0; i < imagePaths_All.size(); i++) {
                Log.e("arrayListSecond ", imagePaths_All.get(i));
                Log.e("positionA",""+(position));
                Log.e("cvcvcv22",""+ imagePaths_All.get(position));
            }

            Picasso.with(_activity).load(imagePaths_All.get(position)).into(imgDisplay);
            (container).addView(viewLayout);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.finish();
                    adapter.notifyDataSetChanged();
                    Log.e("new", " " +imagePaths_All);
                    Log.e("new", " " +imagePaths_All.size());
                   // Toast.makeText(_activity, imagePaths_All.toString(), Toast.LENGTH_SHORT).show();

                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.notifyDataSetChanged();

                    Toast.makeText(_activity, imagePaths_All.get(position), Toast.LENGTH_SHORT).show();
                    imagePaths_All.remove(imagePaths_All.get(position));
                    adapter.notifyDataSetChanged();
                    Log.e("imagePaths_nal_remove",""+imagePaths_All);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(_activity, imagePaths_All.toString(), Toast.LENGTH_SHORT).show();
                }
            });


            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((RelativeLayout) object);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lot__details__new, menu);
        return true;
    }

}