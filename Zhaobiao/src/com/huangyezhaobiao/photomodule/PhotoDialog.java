package com.huangyezhaobiao.photomodule;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.huangyezhaobiao.R;

/**
 * Created by shenzhixin on 2015/12/4.
 * 选择是照相还是相册的dialog
 */
public class PhotoDialog extends Dialog{
    private Button btn_take_photo;
    private Button btn_select_from_gallery;
    private View   rl_select_take;
    private OnPPViewClickListener mListener;
    public PhotoDialog(Context context) {
        this(context,-1);
    }

    public PhotoDialog(Context context, int theme) {
        super(context, R.style.DialogWithOutAnim);
       // initWindowAttrs();
        initView();

    }

    public void setPPViewListener(OnPPViewClickListener listener){
        mListener = listener;
    }
    private void initView() {
        setContentView(getLayoutId());
        btn_select_from_gallery = (Button) findViewById(R.id.btn_select_from_gallery);
        btn_take_photo          = (Button) findViewById(R.id.btn_take_photo);
        rl_select_take          = findViewById(R.id.rl_select_take);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mListener!=null){
                    mListener.onTakePhotoClick();
                }
            }
        });
        btn_select_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mListener!=null){
                    Log.e("shenzhixinUUU","takeGallery");
                    mListener.onTakeGalleryClick();
                }
            }
        });
    }

    private int getLayoutId(){
        return R.layout.dialog_media_menu;
    }


    private void initWindowAttrs() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = ViewGroup.LayoutParams.FILL_PARENT;
        lp.width = ViewGroup.LayoutParams.FILL_PARENT;
        dialogWindow.setAttributes(lp);
    }



   public interface OnPPViewClickListener{
       void onTakePhotoClick();
       void onTakeGalleryClick();
   }

}
