package wuba.zhaobiao.utils;

import android.app.Activity;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.view.LoadingProgress;

/**
 * Created by SongYongmeng on 2016/8/1.
 */
public class ProgressUtils {
    private final Activity context;

    public ProgressUtils(Activity context) {
        this.context = context;
    }

    protected LoadingProgress loading; //加载对话框

    /**
     * 加载效果
     */
    public void startLoading() {
        try {
            if (loading == null && context != null) {
                loading = new LoadingProgress(context, R.style.loading);
            }
            loading.show();
        } catch (RuntimeException e) {
            if (!context.isFinishing() && loading != null && loading.isShowing()) {
                loading.dismiss();
                loading = null;
            }
        }
    }

    /**
     * 对话框消失
     */
    public void stopLoading() {
        if (!context.isFinishing() && loading != null && loading.isShowing()) {
            loading.dismiss();
            loading = null;
        }
    }
}
