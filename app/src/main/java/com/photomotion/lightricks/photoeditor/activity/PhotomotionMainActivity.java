package com.photomotion.lightricks.photoeditor.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;



import com.photomotion.lightricks.photoeditor.ApplicationClass;

import com.photomotion.lightricks.photoeditor.R;
import com.photomotion.lightricks.photoeditor.utils.Share;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;


public class PhotomotionMainActivity extends AppCompatActivity implements OnClickListener {
    public static final String TAG = "MainAct";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM = 103;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2 = 104;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3 = 105;
    public PhotomotionMainActivity moActivity;
    public String mSelectedOutputPath;
    public String mSelectedImagePath;
    public Uri mSelectedImageUri;
    
    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mSelectedImagePath = mSelectedOutputPath;
                        if (stringIsNotEmpty(mSelectedImagePath)) {
                            File fileImageClick = new File(mSelectedImagePath);
                            if (fileImageClick.exists()) {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                    mSelectedImageUri = Uri.fromFile(fileImageClick);
                                } else {
                                    mSelectedImageUri = FileProvider.getUriForFile(PhotomotionMainActivity.this, getPackageName() + ".provider", fileImageClick);
                                }
                                if (mSelectedImageUri == null) {
                                    Toast.makeText(PhotomotionMainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Share.SAVED_BITMAP = mSelectedImageUri;
                                Share.imageUrl = mSelectedImagePath;
                                startActivity(new Intent(PhotomotionMainActivity.this, CropActivity.class));
                            }
                        }
                    }

                }
            });
    private ImageView lin_camera;
    private ImageView lin_gallery;
    private ImageView lin_my_creation;


    public static boolean requestCameraPermissionApp(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM);
                return false;
            }

        } else {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM);
                return false;
            }
        }

        return true;
    }

    public static boolean requestCameraPermissionApp3(Activity activity) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3);
                return false;
            }

        } else {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3);
                return false;
            }
        }


        return true;
    }


    public static boolean checkForCameraPermission(Activity activity) {

        if (ActivityCompat.checkSelfPermission(activity, CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public boolean requestCameraPermissionApp2(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2);
                return false;
            }

        } else {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2);
                return false;
            }
        }


        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        this.moActivity = this;
       
        
        initViews();
        initViewListeners();
        if (ApplicationClass.checkForStoragePermission(this)) {
            ApplicationClass.deleteTemp();
        }
    }


    private void initViewListeners() {
        lin_gallery.setOnClickListener(this);
        lin_camera.setOnClickListener(this);
        lin_my_creation.setOnClickListener(this);


    }


    private void initViews() {
        lin_gallery = findViewById(R.id.lin_gallery);
        lin_camera = findViewById(R.id.lin_camera);
        lin_my_creation = findViewById(R.id.lin_my_creation);

    }

    public void onClick(@NotNull View view) {
        switch (view.getId()) {
            case R.id.lin_camera:

                        if (requestCameraPermissionApp(PhotomotionMainActivity.this)) {
                            toOpenCamera();
                        }


                return;
            case R.id.lin_gallery:

                        if (requestCameraPermissionApp2(PhotomotionMainActivity.this)) {
                            toGallery();
                        }


                return;

            case R.id.lin_my_creation:

                        if (requestCameraPermissionApp3(PhotomotionMainActivity.this)) {
                            requestMyCreation();
                        }

                return;
            default:
                return;
        }
    }

    private void toGallery() {
        Intent intent = new Intent(moActivity, PhotomotionGalleryListActivity.class);
        startActivity(intent);
    }

    private void requestMyCreation() {
        Intent intent = new Intent(moActivity, MyAlbumActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM: {
                if (!checkForCameraPermission(this)) {
                    Toast.makeText(this, R.string.phone_camera_permission_not_granted, Toast.LENGTH_SHORT).show();
                } else if (!ApplicationClass.checkForStoragePermission(this)) {
                    Toast.makeText(this, R.string.storage_permission_not_granted, Toast.LENGTH_SHORT).show();
                } else if (checkForCameraPermission(this) && ApplicationClass.checkForStoragePermission(this)) {
                    toOpenCamera();
                    return;
                }
                requestCameraPermissionApp(PhotomotionMainActivity.this);
                return;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2: {
                if (!ApplicationClass.checkForStoragePermission(this))
                    Toast.makeText(this, R.string.storage_permission_not_granted, Toast.LENGTH_SHORT).show();
                else {
                    toGallery();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3: {
                if (!ApplicationClass.checkForStoragePermission(this))
                    Toast.makeText(this, R.string.storage_permission_not_granted, Toast.LENGTH_SHORT).show();
                else {
                    requestMyCreation();
                }
                return;
            }
        }
    }

    private void toOpenCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", createImageFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            cameraResultLauncher.launch(intent);
        }
    }

    private File createImageFile() {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + getPackageName() + "/CamPic/");
        storageDir.mkdirs();
        File image = null;
        try {
            image = new File(storageDir, getString(R.string.app_folder3));
            if (image.exists())
                image.delete();
            image.createNewFile();

            mSelectedOutputPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
