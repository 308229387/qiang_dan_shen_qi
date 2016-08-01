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
    private String NEED_DOWN_LINE = "need_down_line";
    private String PPU_EXPIRED = "ppu_expired";

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
    public void onAfter(boolean isFromCache, @Nullable T t, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, t, call, response, e);
        if (needProgress)
            progress.stopLoading();

        if (e != null && e.getMessage().equals(NEED_DOWN_LINE))
            new LogoutDialogUtils(context, context.getString(R.string.force_exit)).initAndShowDialog();
        else if (e != null && e.getMessage().equals(PPU_EXPIRED))
            new LogoutDialogUtils(context, context.getString(R.string.ppu_expired)).initAndShowDialog();
    }

}

