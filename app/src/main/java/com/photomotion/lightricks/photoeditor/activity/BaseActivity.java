package com.photomotion.lightricks.photoeditor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public abstract class BaseActivity extends Activity {
    public Context mContext;
    public abstract Context getContext();

    public abstract void initActions();

    public abstract void initData();

    public abstract void initViews();

    public void log(int i) {
    }

    public void log(Exception exc) {
    }

    public void log(String str) {
    }

    public void log(String str, int i) {
    }

    public void log(String str, Exception exc) {
    }

    public void log(String str, String str2) {
    }




    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getContext();
    }

    public void setContentView(int i) {
        super.setContentView(i);
        initViews();
        initData();
        initActions();
    }

}
