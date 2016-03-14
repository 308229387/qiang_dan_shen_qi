package com.huangyezhaobiao.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;

import com.huangyezhaobiao.constans.CommonValue;
import com.huangyezhaobiao.iview.MobileChangeIView;

/**
 * Created by shenzhixin on 2015/11/10.
 */
public class MobileBindChangePresenter {
    private Context context;
    private boolean newPhoneSatisfy;
    private boolean validateCodeSatisfy;
    private MobileChangeIView mobileChangeIView;
    int time = 60;

    public MobileBindChangePresenter(Context context,MobileChangeIView mobileChangeIView){
        this.context = context;
        this.mobileChangeIView = mobileChangeIView;
    }
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            time--;
            if(time>0){
                mobileChangeIView.validateNumberUnEnabled(time);
                handler.sendEmptyMessageDelayed(0,1000*1);
            }else{
                mobileChangeIView.validateNumberEnabled();
            }

        }
    };

    /**
     *手机验证码的edittext输入回调框
     * @return
     */
    public TextWatcher getMobileCodeListener(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if(length>0){
                    validateCodeSatisfy = true;
                }else{
                    validateCodeSatisfy = false;
                }
                checkSubmitEnabled(validateCodeSatisfy,newPhoneSatisfy);
            }
        };

        return textWatcher;
    }

    private void checkSubmitEnabled(boolean validateCodeSatisfy, boolean newPhoneSatisfy) {
        if(validateCodeSatisfy && newPhoneSatisfy){
            mobileChangeIView.onSubmitEnabled();
        }else{
            mobileChangeIView.onSubmitUnEnabled();
        }
    }

    /**
     * 获取新手机号的edittext的输入监听事件
     * @return
     */
    public TextWatcher getNewMobileTextWatcher(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if(length == CommonValue.PHONE_LENGTH){
                    newPhoneSatisfy = true;
                }else{
                    newPhoneSatisfy = false;
                }
                checkSubmitEnabled(validateCodeSatisfy,newPhoneSatisfy);
            }
        };

        return textWatcher;
    }


    public void startCountDown() {
        time = 60;
        handler.sendEmptyMessageDelayed(0, 1000);
    }
}
