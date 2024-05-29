package com.photomotion.lightricks.photoeditor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.photomotion.lightricks.photoeditor.R;
import com.photomotion.lightricks.photoeditor.fragment.PhotomotionPhotoFragment;
import com.photomotion.lightricks.photoeditor.utils.Share;


public class PhotomotionGalleryListActivity extends FragmentActivity implements OnClickListener {

    private static PhotomotionGalleryListActivity galleryActivity;
    public Activity mContext;
    public TextView tv_title;
    private ImageView iv_close;
    private long mLastClickTime = 0;

    public static PhotomotionGalleryListActivity getGalleryActivity() {
        return galleryActivity;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gallery_album);
        
        galleryActivity = this;
        mContext = this;
        initView();
        initViewAction();

    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_close = findViewById(R.id.iv_close);

    }

    private void initViewAction() {
        iv_close.setOnClickListener(this);
        updateFragment(PhotomotionPhotoFragment.newInstance());
    }


    public void updateFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.simpleFrameLayout, fragment);
        beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        beginTransaction.commit();
    }

    public void onBackPressed() {
        Share.lst_album_image.clear();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime();
            int id = view.getId();
            if (id == R.id.iv_close) {
                onBackPressed();
            }
        }
    }

}
