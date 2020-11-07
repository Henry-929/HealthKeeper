package comp5216.sydney.edu.au.assignment2.addMeal;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.aip.imageclassify.*;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.SquareFrameLayout;

public class CameraActivity extends Activity{
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageButton captureBtn;
    private CameraDevice mCameraDevice;
    private Handler childHandler, mainHandler;
    private ImageReader mImageReader;
    private String mCameraID;
    private CameraManager mCameraManager;
    private CameraCaptureSession mCameraCaptureSession;
    private TextView processLabel;
    private ProgressBar progressBar;
    public String foodName;
    public SquareFrameLayout sl_surfaceView;

    private static final SparseIntArray ORIENTATION = new SparseIntArray();

    //Baidu API token
    private AipImageClassify aipImageClassify;
    public static final String APP_ID = "22861707";
    public static final String API_KEY = "UblMw518IZZcpG6BUVOeEd5A";
    public static final String SECRET_KEY = "4CwDnD0G9w6PoYEpU8bgDRbwlUEtX2Rj";

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_camera);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        captureBtn = (ImageButton) findViewById(R.id.capture);

        try{
            sl_surfaceView.setVisibility(View.VISIBLE);
            mSurfaceView.setVisibility(View.VISIBLE);
            captureBtn.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
        initView();

        processLabel = (TextView)findViewById(R.id.progress_label);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });
    }

    private void initView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);

        // mSurfaceView add call back
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //create SurfaceView
                // Initialize Camera
                initCamera2();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { //destroy SurfaceView
                // release camera resource
                if (null != mCameraDevice) {
                    mCameraDevice.close();
                    CameraActivity.this.mCameraDevice = null;
                }
            }
        });
    }

    private void initCamera2() {
        Log.d("initCamera2","start");
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG,1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //可以在这里处理拍照得到的临时照片 例如，写入本地
            @Override
            public void onImageAvailable(ImageReader reader) {
                Log.d("initCamera2","start");
                mCameraDevice.close();
                mSurfaceView.setVisibility(View.GONE);
                captureBtn.setVisibility(View.GONE);
                // get the image
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                if (bitmap != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    processLabel.setVisibility(View.VISIBLE);
                    imageRecognition_baiduAPI(bitmap);
                }
            }
        }, mainHandler);
        //access camera manager
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //open camera
            mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {//open camera
            mCameraDevice = camera;
            //开启预览
            takePreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {//close camera
            if (null != mCameraDevice) {
                mCameraDevice.close();
                CameraActivity.this.mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {//error occur
            Toast.makeText(CameraActivity.this, "Fail to open Camera", Toast.LENGTH_SHORT).show();
        }
    };

    private void takePreview() {
        try {
            //  create CaptureRequest.Builder for camera preview
            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // add (SurfaceView)surface as the target of CaptureRequest.Builder的
            previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
            // create CameraCaptureSession，This object is responsible for managing the processing of preview requests and photo requests
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(),
                    mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
            {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice) return;
                    // When the camera is ready, the preview displays
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        // autofocus
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // Turn on the flash
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // display the preview
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "Configuration failed", Toast.LENGTH_SHORT).show();
                }
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void capture() {
        if (mCameraDevice == null) return;
        // create CaptureRequest.Builder for capture
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // add imageReader's surface as the target of CaptureRequest.Builder
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // autofocus
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // automatic exposure
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // Get the direction of the phone
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // Set the direction of the photo according to the device direction calculation
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation));
            // capture
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
            Log.d("Take photo","Success");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    /*
    implement ml kit from Google for image recognition
     */

    public void imageLabelling_mlkit(Bitmap bitmap){
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        labeler.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        // Task completed successfully
                        Toast.makeText(CameraActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        for (ImageLabel label : labels) {
                            String text = label.getText();
                            float confidence = label.getConfidence();
                            int index = label.getIndex();
                            System.out.println(text);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
    }

    /*
    implement ImageRecognition API from Baidu for image recognition
     */

    public void imageRecognition_baiduAPI(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {

                byte[] content = getBitmapByte(bitmap);

                aipImageClassify = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
                aipImageClassify.setConnectionTimeoutInMillis(2000);
                aipImageClassify.setSocketTimeoutInMillis(6000);
                HashMap<String, String> options = new HashMap<String, String>();
                options.put("baike_num", "1");
                JSONObject res = aipImageClassify.plantDetect(content, options);
                try {
                    JSONArray data = res.getJSONArray("result");
                    Bundle bundle = new Bundle();
                    foodName = data.getJSONObject(0).getString("name");
                    //pass the food name and photo to confirm activity
                    Intent intent = new Intent(CameraActivity.this,ImageConfirmActivity.class);
                    if (intent != null) {
                        bundle.putBinder("bitmap", new BitmapBinder(bitmap));
                        intent.putExtras(bundle);
                        intent.putExtra("foodName", foodName);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //Convert a Bitmap to a binary array
    public byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}


