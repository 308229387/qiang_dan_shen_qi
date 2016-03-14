package com.huangyezhaobiao.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * Created by shenzhixin on 2015/12/18.
 */
public class UploadPicDialog extends Dialog{
    private TextView dialog_tv_upload_progress;
    public UploadPicDialog(Context context) {
        super(context, R.style.RequestDialog);
        View rootView = LayoutInflater.from(context).inflate(getLayoutId(),null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        initView(rootView);
    }

    private void initView(View view) {
        setContentView(view);
        dialog_tv_upload_progress = (TextView) view.findViewById(R.id.dialog_tv_upload_progress);
    }

    public void setUploadProgress(String msg){
        dialog_tv_upload_progress.setText(msg);
    }


    private int getLayoutId(){
        return R.layout.dialog_upload_pic;
    }
}
