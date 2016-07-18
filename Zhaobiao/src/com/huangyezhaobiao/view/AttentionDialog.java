package com.huangyezhaobiao.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * Created by shenzhixin on 2015/12/14.
 */
public class AttentionDialog extends Dialog{
    private Context context;
    private LayoutInflater mInflater;
    private int resId;
    private String title;
    private View   rootView;
    private TextView   dialog_tv_content;
    private ImageView  iv_hint;
    private RelativeLayout rl_ok;
    private RequestOkListener listener;
    public AttentionDialog(Context context) {
        super(context,-1);
    }


    public void setListener(RequestOkListener listener) {
        this.listener = listener;
    }

    public AttentionDialog(Context context, String title, int resId) {
        super(context, R.style.RequestDialog);
        this.context = context;
        mInflater = LayoutInflater.from(context);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        this.title = title;
        this.resId   = resId;
        initView();
    }

    private void initView() {
        rootView          = mInflater.inflate(getLayoutId(),null);
        setContentView(rootView);
        dialog_tv_content = (TextView) rootView.findViewById(R.id.dialog_tv_content);
        iv_hint           = (ImageView) rootView.findViewById(R.id.iv_hint);
        rl_ok             = (RelativeLayout) rootView.findViewById(R.id.rl_ok);
        dialog_tv_content.setText(title);
        iv_hint.setImageResource(resId);
        rl_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onRequestOkClick();
                }
            }
        });
    }



    private int getLayoutId(){
        return R.layout.dialog_attention;
    }


   public  interface RequestOkListener{
        void onRequestOkClick();
    }
}
