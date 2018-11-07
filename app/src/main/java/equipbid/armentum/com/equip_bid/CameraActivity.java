/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import equipbid.armentum.com.equip_bid.model.LotDetailObj;
import equipbid.armentum.com.equip_bid.model.LotDetailsModel;

public class CameraActivity extends Activity {

    private String lot_no, Auction_name;
    LotDetailsModel poDtls;
    LotDetailObj lotdetailsobj;
    private ArrayList<String> imagesObj;
    private ArrayList<String> imagePaths_external;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Bundle bundle       = getIntent().getExtras();
        lot_no              = bundle.getString("lotname");
        Auction_name        = bundle.getString("name");
        poDtls              = bundle.getParcelable("LotDetailsModel");
        lotdetailsobj       = bundle.getParcelable("lotdetailsobj");
        Log.e("Model",  new Gson().toJson(poDtls));
        Log.e("CamtoF",  lot_no+" "+Auction_name);

        Camera2BasicFragment camera2Frag = new Camera2BasicFragment();
        camera2Frag.setArguments(bundle);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, camera2Frag)
                    .commit();
        }
    }

    public void getPathFolder(File mFile) {
        Log.e("Files", ""+ mFile);
        File directory = new File(mFile.toString());
        File[] files = directory.listFiles();
        ArrayList<String> Images = new ArrayList<String>();
        Log.e("Files", "Path: "+ files);

        for (int i = 0; i < files.length; i++)
        {
            Log.e("Files", "FileName:" + files[i].getName());
            Images.add(files[i].getAbsolutePath());
        }
        Log.e("FilesImages", ""+Images);
        Log.e("FilesImages", ""+Images.size());
        Intent inext = new Intent(CameraActivity.this, Lot_Details_New.class);
        Log.e("Model11",  new Gson().toJson(poDtls));
        inext.putStringArrayListExtra("Images_Camera", Images);
        Log.e("Images_CameraINBack", Images+"");
        poDtls.setCam_Images(Images);
        poDtls.setMFile(mFile.toString());
        Log.e("Model11",  new Gson().toJson(poDtls));

        Bundle b = new Bundle();
        lot_no          = b.getString("lotname");
        Auction_name    = b.getString("name");

        b.putParcelable("LotDetailsModel", poDtls);
        b.putParcelable("lotdetailsobj", lotdetailsobj);

        inext.putExtras(b);
        startActivity(inext);
        finish();
    }
}
