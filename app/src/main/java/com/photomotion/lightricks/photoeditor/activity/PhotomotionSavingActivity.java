package com.photomotion.lightricks.photoeditor.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.photomotion.lightricks.photoeditor.callback.OnProgressReceiver;
import com.photomotion.lightricks.photoeditor.callback.onVideoSaveListener;
import com.photomotion.lightricks.photoeditor.ApplicationClass;
import com.photomotion.lightricks.photoeditor.R;
import com.photomotion.lightricks.photoeditor.customView.beans.Projeto;
import com.photomotion.lightricks.photoeditor.customView.controllersapp.ToolsController;
import com.photomotion.lightricks.photoeditor.customView.controllersapp.Utils;
import com.photomotion.lightricks.photoeditor.customView.controllersapp.VideoSaver;
import com.photomotion.lightricks.photoeditor.utils.DatabaseHandler;

import java.io.File;
import java.text.NumberFormat;

public class PhotomotionSavingActivity extends Activity {

    public static String INTENT_PROJETO = "PROJETO";
    public static int MAX_RESOLUTION_SAVE = 1080;
    public static int MIN_RESOLUTION_SAVE = 600;
    static int total = MIN_RESOLUTION_SAVE + MAX_RESOLUTION_SAVE;
    public static int MAX_RESOLUTION_SAVE_FREE = (total / 2);
    public DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    public NumberFormat f147nf;
    public Projeto projetoToSave;
    public float resolucaoOriginal;
    public SeekBar seekResolucao;
    public TextView txResolucao;
    public TextView txTempoSave;
    public VideoSaver videoSaver;
    public int miHeight;
    public int miWidth;
    VideoSaver videoSaver2;
    ApplicationClass application;
    private Button btSave, btClose;
    private SeekBar seekTempo;
    private LinearLayout rel_process;
    private RelativeLayout rel_save;


    public void onCreate(Bundle bundle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(bundle);
        setContentView(R.layout.activity_save);
        
        rel_process = findViewById(R.id.rel_process);
        rel_save = findViewById(R.id.rel_save);
        rel_process.setVisibility(View.GONE);
        rel_save.setVisibility(View.VISIBLE);

        this.setFinishOnTouchOutside(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        projetoToSave = (Projeto) getIntent().getParcelableExtra(INTENT_PROJETO);
        projetoToSave = databaseHandler.getProjeto(projetoToSave.getId());
        projetoToSave.reloadBitmapUri(this, databaseHandler);
        btClose = (Button) findViewById(R.id.btClose);
        btClose.setOnClickListener(new SaveCloseClick());
        seekTempo = (SeekBar) findViewById(R.id.seekTempoSave);
        seekTempo.setMax(8000);
        txTempoSave = findViewById(R.id.txTempoSave);
        seekTempo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                projetoToSave.setTempoSave(10000 - Math.round((((float) i) / ((float) seekBar.getMax())) * 8000.0f));
                TextView access$200 = txTempoSave;
                StringBuilder sb = new StringBuilder();
                sb.append(f147nf.format((double) (((float) projetoToSave.getTempoSave()) / 1000.0f)));
                access$200.setText(sb.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                projetoToSave.updateProjeto(databaseHandler);
            }
        });
        f147nf = NumberFormat.getInstance();
        f147nf.setMaximumFractionDigits(1);
        int tempoSave = projetoToSave.getTempoSave();
        if (tempoSave < 6000) {
            tempoSave = 6000;
        }
        seekTempo.setProgress(1);
        seekTempo.setProgress(2);
        seekTempo.setProgress(10000 - tempoSave);
        seekResolucao = (SeekBar) findViewById(R.id.seekResolucaoSave);
        seekResolucao.setMax(MAX_RESOLUTION_SAVE - MIN_RESOLUTION_SAVE);
        txResolucao = findViewById(R.id.txResolucaoSave);
        seekResolucao.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String str;
                int round = Math.round((((float) i) / ((float) seekBar.getMax())) * ((float) (PhotomotionSavingActivity.MAX_RESOLUTION_SAVE - PhotomotionSavingActivity.MIN_RESOLUTION_SAVE))) + PhotomotionSavingActivity.MIN_RESOLUTION_SAVE;
                if (round % 2 != 0) {
                    round++;
                }
                if (projetoToSave.getWidth() > projetoToSave.getHeight()) {
                    miWidth = round;
                    float f = (float) round;
                    miHeight = Math.round((((float) projetoToSave.getHeight()) / ((float) projetoToSave.getWidth())) * f);
                    StringBuilder sb = new StringBuilder();
                    sb.append(round);
                    sb.append("x");
                    sb.append(Math.round((((float) projetoToSave.getHeight()) / ((float) projetoToSave.getWidth())) * f));
                    str = sb.toString();
                } else {
                    PhotomotionSavingActivity saveActivity2 = PhotomotionSavingActivity.this;
                    float f2 = (float) round;
                    saveActivity2.miWidth = Math.round((((float) saveActivity2.projetoToSave.getWidth()) / ((float) projetoToSave.getHeight())) * f2);
                    miHeight = round;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(Math.round((((float) projetoToSave.getWidth()) / ((float) projetoToSave.getHeight())) * f2));
                    sb2.append(" x ");
                    sb2.append(round);
                    str = sb2.toString();
                }
                txResolucao.setText(str);
                pintarTextoIdeal(round);
                projetoToSave.setResolucaoSave(round);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                projetoToSave.updateProjeto(databaseHandler);
            }
        });
        resolucaoOriginal = (float) Math.min(Math.max(projetoToSave.getHeight(), projetoToSave.getWidth()), MAX_RESOLUTION_SAVE);
        float f = resolucaoOriginal;
        if (f % 2.0f != 0.0f) {
            f += 1.0f;
        }
        resolucaoOriginal = f;
        int resolucaoSave = projetoToSave.getResolucaoSave();
        if (resolucaoSave > MAX_RESOLUTION_SAVE_FREE) {
            seekResolucao.setProgress(1);
            seekResolucao.setProgress(2);
            seekResolucao.setProgress(resolucaoSave - MIN_RESOLUTION_SAVE);
            (findViewById(R.id.txTamanhoIdeal)).setOnClickListener(new tamanhoIdealOnClickListener());
            btSave = findViewById(R.id.btSalvar);
            btSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    rel_process.setVisibility(View.VISIBLE);
                    rel_save.setVisibility(View.GONE);
                    salvarVideo(false);
                }
            });
            setFinishOnTouchOutside(false);
            videoSaver2 = (VideoSaver) getLastNonConfigurationInstance();
        } else {
            seekResolucao.setProgress(1);
            seekResolucao.setProgress(2);
            seekResolucao.setProgress(resolucaoSave - MIN_RESOLUTION_SAVE);
            (findViewById(R.id.txTamanhoIdeal)).setOnClickListener(new tamanhoIdealOnClickListener());
            btSave = findViewById(R.id.btSalvar);

            btSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    rel_process.setVisibility(View.VISIBLE);
                    rel_save.setVisibility(View.GONE);
                    salvarVideo(false);
                }
            });
            setFinishOnTouchOutside(false);
            videoSaver2 = (VideoSaver) getLastNonConfigurationInstance();
        }
        if (videoSaver2 != null) {
            videoSaver = videoSaver2;
            videoSaver.setContext(this);
            if (videoSaver.isSalvando()) {
                setFinishOnTouchOutside(false);
            }
        }


        application = new ApplicationClass();
        application.setOnProgressReceiver(new OnProgressReceiver() {
            @Override
            public void onImageProgressFrameUpdate(final float progress) {
                int p = (int) ((25.0f * progress) / 100.0f);
                Log.e("TAG", "Image Progress = " + ((int) progress) + " || P = " + p);
                int ddd = (int) progress;
            }

        });


    }


    public void pintarTextoIdeal(int i) {
        TextView textView = findViewById(R.id.txTamanhoIdeal);
        if (i == ((int) resolucaoOriginal)) {
            textView.setTextColor(Utils.getColor(this, R.color.colorPrimary));
        } else {
            textView.setTextColor(Utils.getColor(this, R.color.color_dialog_hint_text));
        }
    }


    public void salvarVideo(boolean z) {
        setFinishOnTouchOutside(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        getWindow().setFlags(16, 16);
        videoSaver = new VideoSaver(this, this.projetoToSave, this.miWidth, this.miHeight);
        videoSaver.setTempoAnimacao(projetoToSave.getTempoSave());
        videoSaver.setResolucao(projetoToSave.getResolucaoSave());
        videoSaver.setComLogo(z);
        getResources().getString(R.string.project_folder);
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.getPublicAlbumStorageDir(getResources().getString(R.string.videos_folder)).getPath());
        sb.append("/");
        sb.append(projetoToSave.getTitulo());
        sb.append(".mp4");
        File file = new File(sb.toString());
        videoSaver.setSaveListener(new onVideoSaveListener() {
            @Override
            public void onError(String str) {
                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(PhotomotionSavingActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                });
                setFinishOnTouchOutside(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                finish();
            }

            @Override
            public void onSaved(String filePath) {
                videoSaver = null;
                getWindow().clearFlags(16);
                setFinishOnTouchOutside(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                ToolsController object = ToolsController.getObject();
                object.apagarSelecao();
                object.setTipoFerramenta(0);
                ToolsController.init(PhotoMotionEditActivity.object);


                Intent intent2 = new Intent(PhotomotionSavingActivity.this, VideoPreviewActivity.class);
                intent2.putExtra("video_path", filePath);
                startActivity(intent2);
                finish();
                if (PhotoMotionEditActivity.object != null) {
                    PhotoMotionEditActivity.object.finish();
                }
                if (PhotomotionAlbumListActivity.activity != null) {
                    PhotomotionAlbumListActivity.activity.finish();
                    return;
                }

            }

            @Override
            public void onSaving(int i) {

            }

            @Override
            public void onStartSave(int i) {

            }
        });
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.nome_logo, null);


        videoSaver.execute(new Object[]{file.getPath(), decodeResource});
    }

    class tamanhoIdealOnClickListener implements OnClickListener {
        public void onClick(View view) {
            seekResolucao.setProgress(((int) resolucaoOriginal) - PhotomotionSavingActivity.MIN_RESOLUTION_SAVE);
        }
    }


    class SaveCloseClick implements OnClickListener {
        public void onClick(View view) {
            finish();
        }
    }
}
