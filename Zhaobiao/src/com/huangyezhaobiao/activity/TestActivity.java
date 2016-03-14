package com.huangyezhaobiao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.windowf.AppExitService;

/**
 * Created by 58 on 2016/3/8.
 */
public class TestActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }


    private int getLayoutId(){
        return R.layout.activity_test;
    }


    public void floatButton(View view){
        Intent intent = new Intent(this, AppExitService.class);
        startService(intent);
        onBackPressed();
    }
}
