package com.huangyezhaobiao.callback;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.huangyezhaobiao.R;
import com.lzy.okhttputils.request.BaseRequest;

import java.lang.reflect.ParameterizedType;

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
        if (e != null && e.getMessage().equals(NEED_DOWN_LINE)
                || e != null && e.getMessage().equals(CHILD_FUNCTION_BAN)
                || e != null && e.getMessage().equals(CHILD_HAS_UNBIND)
                || e != null && e.getMessage().equals(PPU_EXPIRED)){
            isToast = true;
        }
    }

    @Override
    public void onAfter(boolean isFromCache, @Nullable T t, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, t, call, response, e);
        if (needProgress)
            progress.stopLoading();

    }

}

