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

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Camera2BasicFragment extends Fragment
        implements View.OnClickListener, View.OnLongClickListener, FragmentCompat.OnRequestPermissionsResultCallback, View.OnTouchListener {


    ArrayList<String> imagePaths;
    private Timer timer;
    private SimpleTimer mTimer;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private static final String TAG = "Camera2BasicFragment";
    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    private static final int MAX_SUPPORTED_PREVIEW_SIZE = 1920 * 1088;

    public final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            Log.d("Test","onSurfaceTextureAvailable");
            Log.e("Selectedvalue1", selectWidth+ "..." + selectHeight);
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            Log.d("Test","onSurfaceTextureSizeChanged");
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            Log.d("Test","onSurfaceTextureDestroyed");
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
            Log.d("Test","onSurfaceTextureUpdated");
        }

    };
    private String mCameraId;
    private AutoFitTextureView mTextureView;
    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private Size mPreviewSize;
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.

            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private ImageReader mImageReader;
    private File mFile;
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), new_file));
        }

    };
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private int mState = STATE_PREVIEW;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private boolean mFlashSupported;
    private int mSensorOrientation;
    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };

    private TextView count_number;
    private Handler handler;
    private String timeStamp;
    private String Img_path;
    private String lot_no, Auction_name;
    private File new_file;
    private ImageView cancelpicture;
    private ImageView Selectpicture;
    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";
    private boolean isFlashSupported;
    private Boolean isTorchOn;
    private Activity activity;
    private CameraManager manager;
    private ToggleButton mTorchOnOffButton;
    private String cameraId = CAMERA_BACK;
    private ImageView resolution;
    private ArrayList<String> ff;
    private ArrayList<Integer> HeightRe;
    private ArrayList<Integer> WidthRe;
    private String selectedText;
    private String[] dataSize;
    private Dialog Resolutiondialog;
    private ResolutionAdapter listadapter;
    private ListView listview;
    private static int selectHeight, selectWidth;
    private ImageView flashButtonOnOff;
    public float finger_spacing = 0;
    public int zoom_level = 1;
    private AutoFitTextureView texture;
    private Rect zoom;
    private float maxzoom;
    private boolean torchRequest = false;
    private SessionManager session;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    private TextView selectItem;
    private CheckBox checbx;
    private String selectedItemSess;
    ResolutionModel rm;

    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static Camera2BasicFragment newInstance() {
        return new Camera2BasicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        imagePaths  = new ArrayList<String>();
        ff          = new ArrayList<String>();
        HeightRe    = new ArrayList<Integer>();
        WidthRe     = new ArrayList<Integer>();

        View v = inflater.inflate(R.layout.fragment_camera2_basic, container, false);
        handler = new Handler();
        lot_no = this.getArguments().getString("lotname");
        Auction_name = this.getArguments().getString("name");
        count_number = (TextView) v.findViewById(R.id.count_number);
        cancelpicture =(ImageView) v.findViewById(R.id.cancelpicture);
        Selectpicture =(ImageView)v.findViewById(R.id.Selectpicture);
        resolution =(ImageView)v.findViewById(R.id.resolution);
        selectItem =(TextView)v.findViewById(R.id.selectItem);
        cancelpicture.setOnClickListener(this);
        Selectpicture.setOnClickListener(this);
        count_number.setVisibility(View.GONE);
        Selectpicture.setVisibility(View.INVISIBLE);
        flashButtonOnOff = (ImageView)v.findViewById(R.id.flashButtonOnOff);

        isTorchOn = false;
        Log.e("isTorchOn1", isTorchOn+"");
        flashButtonOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupFlashButtonOnOff();
            }
        });

        rm = new ResolutionModel();
        session = new SessionManager(getActivity());
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectWidth         = sharedpreferences.getInt("selectWidth", 0);
        selectHeight        = sharedpreferences.getInt("selectHeight", 0);
        selectedItemSess        = sharedpreferences.getString("both", "");
        Log.e("value", selectWidth+ "..." + selectHeight);
        if(!selectedItemSess.equals(""))
        {
            selectItem.setText(selectedItemSess);
        }
        return v;
    }

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();

        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();

        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        Log.e("bigEnough", bigEnough+"");
        Log.e("notBigEnough", notBigEnough+"");
        Log.e("sssss", selectWidth+".."+selectHeight);
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            if((selectWidth !=0) && (selectHeight!=0)){
                Log.e("ss", Collections.max(notBigEnough, new CompareSizesByArea())+"");
                notBigEnough.clear();
                String ff = selectWidth+"x"+selectHeight;
                notBigEnough.add(Size.parseSize(ff));
                Log.e("bigEnoughdd", notBigEnough+"");
                return Collections.max(notBigEnough, new CompareSizesByArea());
            }
            else {
                return Collections.max(notBigEnough, new CompareSizesByArea());
            }
        }
        else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
       // view.findViewById(R.id.picture).setOnClickListener(this);
        view.findViewById(R.id.picture).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    Activity activity = getActivity();
                    CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
                    maxzoom = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM))*10;

                    Rect m = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                    float current_finger_spacing;

                    if (event.getPointerCount() > 1) {
                        // Multi touch logic
                        current_finger_spacing = getFingerSpacing(event);
                        if(finger_spacing != 0){
                            if(current_finger_spacing > finger_spacing && maxzoom > zoom_level){
                                zoom_level++;
                            } else if (current_finger_spacing < finger_spacing && zoom_level > 1){
                                zoom_level--;
                            }
                            int minW = (int) (m.width() / maxzoom);
                            int minH = (int) (m.height() / maxzoom);
                            int difW = m.width() - minW;
                            int difH = m.height() - minH;
                            int cropW = difW /100 *(int)zoom_level;
                            int cropH = difH /100 *(int)zoom_level;
                            cropW -= cropW & 3;
                            cropH -= cropH & 3;
                            zoom = new Rect(cropW, cropH, m.width() - cropW, m.height() - cropH);
                            Log.e("Zooooom", zoom+"");
                            mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);
                        }
                        finger_spacing = current_finger_spacing;
                    } else{
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // PRESSED
                                //  Toast.makeText(getActivity(), "PRESSED", Toast.LENGTH_SHORT).show();

                                imagePaths = new ArrayList<String>();
                                timer = new Timer();
                                mTimer = new SimpleTimer(timer);
                                Log.d("mTimerDDD", ""+mTimer);
                                timer.schedule(mTimer,1,1000);
                                Log.e("Hello", "Hello");

                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                                // RELEASED
                                timer.cancel();
                                Log.d("imagePathsssss", ""+imagePaths);
                                Log.d("imagePathsssss", ""+imagePaths.size());
                                count_number.setVisibility(View.GONE);
                                Selectpicture.setVisibility(View.VISIBLE);
                                //  Toast.makeText(getActivity(), "RELEASED "+ imagePaths.size(), Toast.LENGTH_SHORT).show();
                                return true; // if you want to handle the touch event
                        }
                    }

                    try {
                        mCaptureSession
                                .setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                } catch (CameraAccessException e) {
                    throw new RuntimeException("can not access camera.", e);
                }
                return true;
               // return false;
            }
        });

        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);
        mTextureView.setOnTouchListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mFile = new File(getActivity().getExternalFilesDir(null), "pic.jpg");

        mFile = new File(Environment.getExternalStorageDirectory(), "EquipBid/"+Auction_name+"_"+lot_no);
        mFile.mkdirs(); // <----
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Img_path = lot_no + "_" + timeStamp;
        new_file = new File(mFile, Img_path + ".jpg");

        Log.e(TAG, "mFile.." + mFile);
        Log.e(TAG, "filecamera.." + new_file);
        Log.e(TAG, "timeStamp.." + timeStamp);
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        if (mTextureView.isAvailable()) {
            Log.e("SIZe///", mTextureView.getWidth()+".." +mTextureView.getHeight());
            Log.e("Selectedvalue2", selectWidth+ "..." + selectHeight);
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    Log.d("Test11","onSurfaceTextureAvailable");
                    Log.e("SIZeeee", mTextureView.getWidth()+".." +mTextureView.getHeight());

                    //Toast.makeText(activity, "W: "+selectWidth+" H: "+selectHeight, Toast.LENGTH_SHORT).show();
                    if(selectWidth == 0 && selectHeight == 0)
                    {
                        Log.e("SIZee", width+".."+height);
                        openCamera(width, height);
                    }
                    else {
                        Log.e("Selectedvalue3", selectWidth+ "..." + selectHeight);
                        openCamera(selectWidth, selectHeight);
                    }
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                    Log.d("Test11","onSurfaceTextureSizeChanged");
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    Log.d("Test11","onSurfaceTextureDestroyed");
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                    Log.d("Test11","onSurfaceTextureUpdated");
                }
            });
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void requestCameraPermission() {
        if (FragmentCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setUpCameraOutputs(int width, int height) {
        activity = getActivity();
        manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                mCameraId = cameraId;
                Log.e("mCameraId11", mCameraId+"");
                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                Size[] size = map.getOutputSizes(SurfaceTexture.class);
                Log.e("mPreviewSize1", mPreviewSize+"");

                if (size != null) {
                    for (Size mPreviewSize : size)
                        if (mPreviewSize.getWidth() * mPreviewSize.getHeight() <= MAX_SUPPORTED_PREVIEW_SIZE)
                        {
                            Log.e("mPreviewSizes.", mPreviewSize+"");
                            Resolution r = new Resolution();
                            r.setHeight(mPreviewSize.getHeight());
                            r.setWidth(mPreviewSize.getWidth());
                            HeightRe.add(mPreviewSize.getHeight());
                            WidthRe.add(mPreviewSize.getWidth());
                            ff.add(mPreviewSize+"");
                        }
                }

                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                            rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                            maxPreviewHeight, largest);

                Log.e("mPreviewSize2", mPreviewSize+"");
                Log.e("HeightRe", HeightRe+"");
                Log.e("WidthRe", WidthRe+"");
                Log.e("ff", ff+"");
                dataSize = ff.toArray(new String[ff.size()]);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    resolution.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            rm.setResolutionList(ff);
                            Resolutiondialog = new Dialog(activity);
                            Resolutiondialog.setContentView(R.layout.resolution);
                            listview = (ListView) Resolutiondialog.findViewById(R.id.listview);
                            listadapter = new ResolutionAdapter(activity, rm.getResolutionList(), HeightRe, WidthRe);
                            listview.setAdapter(listadapter);
                            listadapter.notifyDataSetChanged();
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ff.get(position);
                                    rm.setSelectRe(rm.getResolutionList().get(position));
                                    selectHeight = HeightRe.get(position);
                                    selectWidth = WidthRe.get(position);
                                    Resolutiondialog.dismiss();
                                    Log.e("hddff", selectWidth+" "+selectHeight);
                                    //mTextureView.setAspectRatio(selectWidth, selectHeight);
                                    selectItem.setText(rm.getSelectRe());

                                    sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                    selectWidth         = sharedpreferences.getInt("selectWidth", 0);
                                    selectHeight        = sharedpreferences.getInt("selectHeight", 0);
                                    selectedItemSess        = sharedpreferences.getString("both", "");
                                    Log.e("value", selectWidth+ "..." + selectHeight);

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    if((selectWidth != 0) && (selectHeight != 0) && (!selectedItemSess.equals("")))
                                    {
                                        session.ClearReUser();
                                    }

                                    session.createCameraResolution(selectWidth, selectHeight,rm.getSelectRe());

                                    editor.putInt("selectWidth", selectWidth);
                                    editor.putInt("selectHeight", selectHeight);
                                    editor.putString("both", rm.getSelectRe());

                                    editor.commit();

                                }
                            });
                            Resolutiondialog.show();
                        }
                    });
                } else {
                    resolution.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            rm.setResolutionList(ff);
                            Resolutiondialog = new Dialog(activity);
                            Resolutiondialog.setContentView(R.layout.resolution);
                            listview = (ListView) Resolutiondialog.findViewById(R.id.listview);
                            listadapter = new ResolutionAdapter(activity, rm.getResolutionList(), HeightRe, WidthRe);
                            listview.setAdapter(listadapter);
                            listadapter.notifyDataSetChanged();
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    rm.setSelectRe(rm.getResolutionList().get(position));
                                    rm.setChecked(true);
                                    selectHeight = HeightRe.get(position);
                                    selectWidth = WidthRe.get(position);
                                    //Toast.makeText(activity, ff.get(position), Toast.LENGTH_SHORT).show();
                                    selectItem.setText(rm.getSelectRe());
                                    Resolutiondialog.dismiss();
                                    Log.e("ww", selectWidth+"");
                                    Log.e("hh", selectHeight+"");
                                    Log.e("hddff", mPreviewSize.getWidth()+" "+mPreviewSize.getHeight());
                                    sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                    selectWidth         = sharedpreferences.getInt("selectWidth", 0);
                                    selectHeight        = sharedpreferences.getInt("selectHeight", 0);
                                    selectedItemSess        = sharedpreferences.getString("both", "");
                                    Log.e("value", selectWidth+ "..." + selectHeight);

                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    if((selectWidth != 0) && (selectHeight != 0) && (!selectedItemSess.equals("")))
                                    {
                                        session.ClearReUser();
                                    }
                                    session.createCameraResolution(selectWidth, selectHeight, rm.getSelectRe());
                                    editor.putInt("selectWidth", selectWidth);
                                    editor.putInt("selectHeight", selectHeight);
                                    editor.putString("both", rm.getSelectRe());
                                    //352x288

                                    editor.commit();
                                }
                            });
                            Resolutiondialog.show();
                        }
                    });

                }
                // Check if the flash is supported.
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                mCameraId = cameraId;
                Log.e("mCameraId", mCameraId);

                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }

    public void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        activity = getActivity();
        manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                               // setAutoFlash(mPreviewRequestBuilder);

                                Log.e("FFF_1", isTorchOn+"");
                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void takePicture() {
        lockFocus();
    }

    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void captureStillPicture() {
        try {
            final Activity activity = getActivity();
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            mPreviewRequestBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            mPreviewRequestBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // setOffFlash(mPreviewRequestBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            mPreviewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            final ArrayList<String> temp = new ArrayList<String>();

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {

                    //imagePaths.clear();
                    imagePaths.add(new_file+"");
                    Log.e("imagePaths", ""+imagePaths.size());
                    Log.e("imagePaths", ""+new_file);
                   // showToast("Saved: " + imagePaths);
                     //imagePaths.add(new_file.toString());
                    //Log.e("imagePaths", ""+new_file);
                    Log.d(TAG, new_file.toString());
                    unlockFocus();
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.capture(mPreviewRequestBuilder.build(), CaptureCallback, null);
            Log.e("Zooooomee", zoom+"");
            mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
          //  setAutoFlash(mPreviewRequestBuilder);
            Log.e("FFF_2", isTorchOn+"");
            if (mFlashSupported && isTorchOn) {
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            }
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Selectpicture: {
                // In fragment A
                Log.e("imagePaths_select",imagePaths+"");
                Log.e("imagePaths_file",mFile+"");
                ((CameraActivity)getActivity()).getPathFolder(mFile);
               // Toast.makeText(getActivity(), ""+imagePaths, Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.cancelpicture: {
               // Toast.makeText(getActivity(), ""+imagePaths, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.picture: {

                imagePaths = new ArrayList<String>();
                timer = new Timer();
                mTimer = new SimpleTimer(timer);
                Log.d("mTimerDDD", ""+mTimer);
                timer.schedule(mTimer,4,4000);
                Log.e("Hello", "Hello");

                break;
            }
        }
        return false;
    }

    private void setOffFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_OFF);
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    private static class ImageSaver implements Runnable {

        private final Image mImage;
        private final File new_file;
        public ImageSaver(Image image, File file) {
            mImage = image;
            new_file = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(new_file);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    public static class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentCompat.requestPermissions(parent,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Activity activity = parent.getActivity();
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
    }

    private class SimpleTimer  extends TimerTask {

        int count = 0;
        Timer timer;
        SimpleTimer( Timer timer){
            this.timer = timer;
        }

        @Override
        public void run() {
            Log.d("count",""+ count);
            count++;

            //mFile = new File(getActivity().getExternalFilesDir(null), "pic"+count+".jpg");
            Log.e(TAG, "ttttt " + Auction_name+" "+lot_no);

            mFile = new File(Environment.getExternalStorageDirectory(), "EquipBid/"+Auction_name+"_"+lot_no);
            mFile.mkdirs(); // <----
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Img_path = lot_no + "_" + timeStamp +"_"+count;
            new_file = new File(mFile, Img_path + ".jpg");
            Log.e(TAG, "mFile.." + mFile);
            Log.e(TAG, "filecamera.." + new_file);
            Log.e(TAG, "timeStamp.." + timeStamp);
           // Log.d("imagePaths", ""+imagePaths);

            takePicture();
            Log.d("count","timer");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    count_number.setVisibility(View.VISIBLE);
                    count_number.setText(count+"");
                }
            });
        }
    }

    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setTorchMode(mCameraId, true);
              //  mTorchOnOffButton.setImageResource(R.drawable.on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setTorchMode(mCameraId, false);
              //  mTorchOnOffButton.setImageResource(R.drawable.off);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupFlashButtonOnOff() {

        /*try {
            manager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }*/
        //  manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try {
            if(isTorchOn) {
                if (mFlashSupported) {
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                            CaptureRequest.CONTROL_AE_MODE_OFF);
                }
                flashButtonOnOff.setImageResource(R.drawable.ic_f_o);
                isTorchOn = false;

            } else {
                if (mFlashSupported) {
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                            CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                }
                flashButtonOnOff.setImageResource(R.drawable.ic_f);
                isTorchOn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ResolutionAdapter extends BaseAdapter {

        private final ArrayList<Integer> heightRe;
        private final ArrayList<Integer> widthRe;
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<String> ff;
        private TextView res_value;

        public ResolutionAdapter(Activity activity, ArrayList<String> ff, ArrayList<Integer> heightRe, ArrayList<Integer> widthRe) {
            this.activity = activity;
            this.ff = ff;
            this.heightRe = heightRe;
            this.widthRe = widthRe;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return ff.size();
        }

        @Override
        public Object getItem(int position) {
            return ff.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/HELVETICANEUELTPRO-LT.ttf");
            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.resolution_list, parent, false);
            }

            res_value = (TextView) view.findViewById(R.id.spinvaluere);
            TextView TextView1 = (TextView) view.findViewById(R.id.s);
            //checbx = (CheckBox) view.findViewById(R.id.checbx);

            res_value.setTypeface(typeface);
            res_value.setText(ff.get(position));
           // TextView1.setText(rm.isChecked()+"");
//            if(rm.isChecked())
//            {
//                if(rm.getSelectRe().equals(ff.get(position))) {
//                    res_value.setBackgroundColor(Color.parseColor("#fbdcbb"));
//                    res_value.setTextColor(Color.parseColor("#040404"));
//                }
//            }

            Log.e("selectedItemSess", selectedItemSess);
            Log.e("rm", "rm.."+new Gson().toJson(rm));
            Log.e("rm", "rm.."+new Gson().toJson(rm.getSelectRe()));

            return view;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        try {
            Activity activity = getActivity();
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            maxzoom = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM))*10;

            Rect m = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            int action = event.getAction();
            float current_finger_spacing;

            if (event.getPointerCount() > 1) {
                // Multi touch logic
                current_finger_spacing = getFingerSpacing(event);
                if(finger_spacing != 0){
                    if(current_finger_spacing > finger_spacing && maxzoom > zoom_level){
                        zoom_level++;
                    } else if (current_finger_spacing < finger_spacing && zoom_level > 1){
                        zoom_level--;
                    }
                    int minW = (int) (m.width() / maxzoom);
                    int minH = (int) (m.height() / maxzoom);
                    int difW = m.width() - minW;
                    int difH = m.height() - minH;
                    int cropW = difW /100 *(int)zoom_level;
                    int cropH = difH /100 *(int)zoom_level;
                    cropW -= cropW & 3;
                    cropH -= cropH & 3;
                    zoom = new Rect(cropW, cropH, m.width() - cropW, m.height() - cropH);
                    Log.e("Zooooom", zoom+"");
                    mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);
                }
                finger_spacing = current_finger_spacing;
            } else{
                if (action == MotionEvent.ACTION_UP) {
                    //single touch logic
                }
            }

            try {
                mCaptureSession
                        .setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        } catch (CameraAccessException e) {
            throw new RuntimeException("can not access camera.", e);
        }
        return true;
    }

    //Determine the space between the first two fingers
    @SuppressWarnings("deprecation")
    private float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}
