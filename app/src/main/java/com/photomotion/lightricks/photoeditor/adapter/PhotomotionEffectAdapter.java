package com.photomotion.lightricks.photoeditor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.photomotion.lightricks.photoeditor.callback.OnClickListner;
import com.photomotion.lightricks.photoeditor.R;
import com.photomotion.lightricks.photoeditor.model.EffectData;
import com.photomotion.lightricks.photoeditor.utils.OnSingleClickListener;
import java.util.List;

public class PhotomotionEffectAdapter extends Adapter<PhotomotionEffectAdapter.ViewHolder> {
    public int checkedPosition = 0;
    public Bitmap moBaseImage;
    public Context moContext;
    public List<EffectData> moEffectList;
    public OnClickListner moOnClickListner;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout loClItemMain;
        public ImageView loIvEffect;
        public ImageView loIvEffectBase;

        public ViewHolder(View view) {
            super(view);
            loClItemMain =  view.findViewById(R.id.CvMain);
            loIvEffect =  view.findViewById(R.id.iv_effect);
            loIvEffectBase =  view.findViewById(R.id.iv_effect_base);
        }
    }

    public PhotomotionEffectAdapter(Context context, List<EffectData> list, Bitmap bitmap, OnClickListner onClickListner) {
        this.moEffectList = list;
        this.moContext = context;
        this.moBaseImage = bitmap;
        this.moOnClickListner = onClickListner;
    }

    public int getItemCount() {
        return this.moEffectList.size();
    }

    public EffectData getSelectedEffect() {
        return (EffectData) this.moEffectList.get(this.checkedPosition);
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.loIvEffectBase.setImageBitmap(this.moBaseImage);
        viewHolder.loIvEffect.setImageDrawable(this.moContext.getResources().getDrawable(((EffectData) this.moEffectList.get(i)).getEffectThumb()));
        int i2 = checkedPosition;
        if (i2 == -1) {
            viewHolder.loClItemMain.setBackground(null);
        } else if (i2 == i) {
            viewHolder.loClItemMain.setBackground(this.moContext.getResources().getDrawable(R.drawable.bg_effect));
        } else {
            viewHolder.loClItemMain.setBackground(null);
        }
        viewHolder.loClItemMain.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                viewHolder.loClItemMain.setBackground(moContext.getResources().getDrawable(R.drawable.bg_effect));
                if (checkedPosition != i) {
                    PhotomotionEffectAdapter effectAdapter = PhotomotionEffectAdapter.this;
                    effectAdapter.notifyItemChanged(effectAdapter.checkedPosition);
                    checkedPosition = i;
                    moOnClickListner.onClick((EffectData) moEffectList.get(i), i);
                }
            }
        });
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_effect, viewGroup, false));
    }
}
