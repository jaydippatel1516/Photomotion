package com.photomotion.lightricks.photoeditor.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.content.FileProvider;


import com.photomotion.lightricks.photoeditor.ApplicationClass;
import com.photomotion.lightricks.photoeditor.R;
import com.photomotion.lightricks.photoeditor.photoAlbum.PhotomotionAlbumHelper;
import com.photomotion.lightricks.photoeditor.photoAlbum.PhotomotionFavouriteHelper;
import com.photomotion.lightricks.photoeditor.photoAlbum.ShareHelper;
import com.photomotion.lightricks.photoeditor.utils.SharedPref;
import com.photomotion.lightricks.photoeditor.utils.VideoWallpaperService;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.io.File;


public class VideoPreviewActivity extends BaseActivity implements OnClickListener {
    public PhotomotionFavouriteHelper fvHelper;
    public int isFav;
    public String outputPath;
    public ProgressBar progressBar;
    public TextView tvTitle;
    LinearLayout lin_set_as_wallpaper;
    public VideoView videoView;
    ImageView imgWhatsApp;
    ImageView imgFacebook;
    ImageView imgInstagram;
    ImageView imgShare;
    private boolean isPlaying = true;
    private ImageView ivBack;
    private ImageView ivDelete;
    private ImageView ivfav;
    private Activity mActivity;
    private long mLastClickTime = 0;

    public Context getContext() {
        return this;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_preview);
        getWindow().setFlags(1024, 1024);
        mActivity = this;
        
    }

    public void initViews() {
        lin_set_as_wallpaper = (LinearLayout) findViewById(R.id.lin_set_as_wallpaper);
        ivBack =  findViewById(R.id.iv_back);
        tvTitle =  findViewById(R.id.tv_title);
        ivDelete =  findViewById(R.id.iv_delete);
        videoView = (VideoView) findViewById(R.id.videoView);
        ivfav =  findViewById(R.id.iv_fav);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgWhatsApp = findViewById(R.id.imgWhatsApp);
        imgFacebook = findViewById(R.id.imgFacebook);
        imgInstagram = findViewById(R.id.imgInstagram);
        imgShare = findViewById(R.id.imgShare);
        lin_set_as_wallpaper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVideo = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                SharedPref.getInstance().setString("live_wall_path", outputPath);
                intentVideo.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(VideoPreviewActivity.this, VideoWallpaperService.class));
                startActivity(intentVideo);
            }
        });

        displayFocusView();

        if (ApplicationClass.checkForStoragePermission(this)) {
            ApplicationClass.deleteTemp();
        }
    }

    private void displayFocusView() {
        if (SharedPref.getInstance(VideoPreviewActivity.this).getBoolean("isDisplayTargetView", false)) {
            TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.ivLiveWall)
                    , getString(R.string.set_live_wall_target_title)
                    , getString(R.string.set_live_wall_target_msg)) // All options below are optional
                    .outerCircleColor(R.color.colorPrimary)// Specify a color for the outer circle
                    .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                    .targetCircleColor(R.color.white)   // Specify a color for the target circle
                    .titleTextSize(20)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.white)      // Specify the color of the title text
                    .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                    .descriptionTextColor(R.color.colorPrimary) // Specify the color of the description text
                    .textColor(R.color.white)        // Specify a color for both the title and description text
                    .textTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf"))  // Specify a typeface for the text
                    .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                    .transparentTarget(true)
                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                    .tintTarget(true)                  // Whether to tint the target view's color
            );
            SharedPref.getInstance(VideoPreviewActivity.this).setBoolean("isDisplayTargetView", true);
        }
    }

    public void initData() {
        outputPath = getIntent().getStringExtra("video_path");
        isFav = getIntent().getIntExtra("isfav", 0);
        fvHelper = new PhotomotionFavouriteHelper(mContext);
        ivfav.setSelected(fvHelper.isPathExists(outputPath));
        playVideo();
        if (isFav == 1) {
            ivDelete.setVisibility(View.GONE);
        }
    }

    public void initActions() {
        ivBack.setOnClickListener(this);
        ivfav.setOnClickListener(this);
        imgWhatsApp.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);
        imgInstagram.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
    }

    public void onPause() {
        super.onPause();
        isPlaying = videoView.isPlaying();
    }

    public void onResume() {
        super.onResume();
        if (isPlaying) {
            videoView.start();
        }
        isPlaying = false;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void playVideo() {
        StringBuilder sb = new StringBuilder();
        sb.append("playVideo: 22 ");
        sb.append(outputPath);
        Log.i("AAAAA", sb.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri videoUri = FileProvider.getUriForFile(VideoPreviewActivity.this, getPackageName() + ".provider", new File(outputPath));
            videoView.setVideoURI(videoUri);
        } else {
            videoView.setVideoURI(Uri.parse(outputPath));
        }

        videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                return false;
            }
        });
    }

    public void ConfirmationDialog() {

        Builder builder = new Builder(mContext);
        builder.setMessage((CharSequence) "Are you sure want to delete this video?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (isFav == 1) {
                    fvHelper.deleteFav(outputPath);
                } else {
                    if (fvHelper.isPathExists(outputPath)) {
                        fvHelper.deleteFav(outputPath);
                    }
                    PhotomotionAlbumHelper.delete(outputPath);
                }
                setResult(-1, new Intent());
                finish();
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }

    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime();
            switch (view.getId()) {
                case R.id.iv_back:
                    onBackPressed();
                    break;
                case R.id.iv_delete:
                    ConfirmationDialog();
                    break;
                case R.id.imgFacebook:
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.TEXT", getPackageName());
                    intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(VideoPreviewActivity.this, getPackageName() + ".provider", new File(outputPath)));
                    try {
                        intent.setPackage("com.facebook.katana");
                        startActivity(intent);
                        return;
                    } catch (Exception unused2) {
                        Toast.makeText(VideoPreviewActivity.this, "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                        return;
                    }
                case R.id.iv_fav:
                    if (!fvHelper.isPathExists(outputPath)) {
                        fvHelper.insertPath(outputPath);
                        ivfav.setSelected(true);
                        break;
                    } else {
                        fvHelper.deleteFav(outputPath);
                        ivfav.setSelected(false);
                        break;
                    }
                case R.id.imgInstagram:
                    Intent i = new Intent("android.intent.action.SEND");
                    i.setType("image/*");
                    i.putExtra("android.intent.extra.TEXT",  getPackageName());
                    i.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(VideoPreviewActivity.this, getPackageName() + ".provider", new File(outputPath)));
                    try {
                        i.setPackage("com.instagram.android");
                        startActivity(i);
                        return;
                    } catch (Exception unused3) {
                        Toast.makeText(VideoPreviewActivity.this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
                        return;
                    }
                case R.id.imgShare:
                    ShareHelper.share(mActivity, outputPath, false);

                    Intent i1 = new Intent("android.intent.action.SEND");
                    i1.setType("image/*");
                    i1.putExtra("android.intent.extra.TEXT",  getPackageName());
                    i1.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(VideoPreviewActivity.this, getPackageName() + ".provider", new File(outputPath)));
                    startActivity(Intent.createChooser(i1, "Share Image using"));
                    break;
                case R.id.imgWhatsApp:

                    Intent intent1 = new Intent("android.intent.action.SEND");
                    intent1.setType("image/*");
                    intent1.putExtra("android.intent.extra.TEXT",  getPackageName());
                    intent1.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(VideoPreviewActivity.this, getPackageName() + ".provider", new File(outputPath)));
                    try {
                        intent1.setPackage("com.whatsapp");
                        startActivity(intent1);
                    } catch (Exception unused4) {
                        Toast.makeText(VideoPreviewActivity.this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }
}
