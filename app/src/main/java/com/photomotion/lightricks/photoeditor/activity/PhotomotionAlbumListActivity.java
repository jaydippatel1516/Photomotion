package com.photomotion.lightricks.photoeditor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.photomotion.lightricks.photoeditor.R;
import com.photomotion.lightricks.photoeditor.adapter.PhoneAlbumImagesAdapter;
import com.photomotion.lightricks.photoeditor.utils.GridSpacingItemDecoration;
import com.photomotion.lightricks.photoeditor.utils.Share;
import com.photomotion.lightricks.photoeditor.utils.Share.KEYNAME;

public class PhotomotionAlbumListActivity extends Activity implements OnClickListener {
    public static PhotomotionAlbumListActivity activity;
    public Activity mContext;
    PhoneAlbumImagesAdapter albumAdapter;
    GridLayoutManager gridLayoutManager;
    ImageView iv_back_album_image;
    RecyclerView rcv_album_images;
    TextView tv_title_album_image;
    private long mLastClickTime = 0;

    public PhotomotionAlbumListActivity() {
        String str = "";
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_album);
        

        this.mContext = this;
        activity = this;
        initView();
        initViewAction();
    }


    private void initView() {
        rcv_album_images = findViewById(R.id.rcv_album_images);
        tv_title_album_image = findViewById(R.id.tv_title_album_image);
        iv_back_album_image = findViewById(R.id.iv_back_album_image);
        gridLayoutManager = new GridLayoutManager(this, 3);
        rcv_album_images.setLayoutManager(this.gridLayoutManager);
        rcv_album_images.addItemDecoration(new GridSpacingItemDecoration(3, 10, true));
        try {
            albumAdapter = new PhoneAlbumImagesAdapter(this, Share.lst_album_image);
            rcv_album_images.setAdapter(this.albumAdapter);
            tv_title_album_image.setText(getIntent().getExtras().getString(KEYNAME.ALBUM_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViewAction() {
        this.iv_back_album_image.setOnClickListener(this);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }



    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
            this.mLastClickTime = SystemClock.elapsedRealtime();
            int id = view.getId();
            if (id == R.id.iv_back_album_image) {
                onBackPressed();
            }
        }
    }

}
