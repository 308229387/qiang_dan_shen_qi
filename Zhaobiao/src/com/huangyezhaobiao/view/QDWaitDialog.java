package com.huangyezhaobiao.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.huangyezhaobiao.R;

/**
 * Created by shenzhixin on 2015/11/23.
 */
public class QDWaitDialog extends ProgressDialog{
    public QDWaitDialog(Context context) {
        super(context,R.style.BackgroundDimEnabledDialog);
    }

    public QDWaitDialog(Context context, int theme) {
        super(context, theme);
        View view = LayoutInflater.from(context).inflate(getLayoutId(),null);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



    private int getLayoutId(){
        return R.layout.dialog_qd_wait;
    }
}
