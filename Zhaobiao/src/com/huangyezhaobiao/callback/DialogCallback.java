package com.huangyezhaobiao.callback;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.ToastUtils;
import com.lzy.okhttputils.request.BaseRequest;

import java.lang.reflect.ParameterizedType;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;
import wuba.zhaobiao.utils.LogoutDialogUtils;
import wuba.zhaobiao.utils.ProgressUtils;

/**
 * Created by SongYongmeng on 2016/7/29.
 * 描    述：对于网络请求是否需要弹出退出对话框
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private ProgressUtils progress;
    private Boolean needProgress = false;
    private Class<T> clazz;
    private Activity context;
    protected String NEED_DOWN_LINE = "need_down_line";
    protected String PPU_EXPIRED = "ppu_expired";
    protected String CHILD_FUNCTION_BAN = "child_function_ban";
    protected String CHILD_HAS_UNBIND = "child_has_unbind";

    protected boolean isToast = false;

    private static Boolean isShow = false;


    private static Boolean toastOnce = false;

    public DialogCallback(Activity context) {
        this.context = context;
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public DialogCallback(Activity context, Boolean needProgress) {
        this.context = context;
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.needProgress = needProgress;
        progress = new ProgressUtils(context);

    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        if (needProgress)
            progress.startLoading();
    }

    @Override
    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onError(isFromCache, call, response, e);

        if(e!= null){
            if (e.getMessage().equals(NEED_DOWN_LINE)
                    ||e.getMessage().equals(CHILD_FUNCTION_BAN)
                    || e.getMessage().equals(CHILD_HAS_UNBIND)
                    || e.getMessage().equals(PPU_EXPIRED)
                    || e.getMessage().equals("网络错误")){
                isToast = true;
            }

            if (isToast &&  e.getMessage().equals("网络错误") && !toastOnce) {
                toastOnce = true;
                ToastUtils.showToast(e.getMessage());
                showTimeRunTask();
            }

            if (!isToast ) {
                ToastUtils.showToast(e.getMessage());
            }

        }

    }

    @Override
    public void onAfter(boolean isFromCache, @Nullable T t, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, t, call, response, e);
        if (needProgress)
            progress.stopLoading();

        if(context != null &&  !context .isFinishing() && e!= null){
            if (e.getMessage().equals(NEED_DOWN_LINE) && !isShow) {
                isShow = true;
                new LogoutDialogUtils(context, context.getString(R.string.force_exit)).showSingleButtonDialog();
                exitTimeRunTask();
            } else if ( e.getMessage().equals(CHILD_FUNCTION_BAN)  && !isShow) {
                isShow = true;
                new LogoutDialogUtils(context, context.getString(R.string.child_function_ban)).showSingleButtonDialog();
                exitTimeRunTask();
            } else if ( e.getMessage().equals(CHILD_HAS_UNBIND)  && !isShow) {
                isShow = true;
                new LogoutDialogUtils(context, context.getString(R.string.child_has_unbind)).showSingleButtonDialog();
                exitTimeRunTask();
            } else if ( e.getMessage().equals(PPU_EXPIRED)  && !isShow) {
                isShow = true;
                new LogoutDialogUtils(context, context.getString(R.string.ppu_expired)).showSingleButtonDialog();
                exitTimeRunTask();
            }

        }

    }

    private void exitTimeRunTask() {
        Timer isDialogShow  = new Timer();
        isDialogShow.schedule(new TimerTask() {
            @Override
            public void run() {
                isShow = false;
            }
        }, 3000);
    }


    private void showTimeRunTask() {
        Timer isToastShow  = new Timer();
        isToastShow.schedule(new TimerTask() {
            @Override
            public void run() {
                toastOnce = false;
            }
        }, 3000);
    }
}

