package wuba.zhaobiao.common.model;

import android.os.Build;
import android.view.WindowManager;

import wuba.zhaobiao.common.activity.SplashActivity;

/**
 * Created by SongYongmeng on 2016/7/28.
 */
public class SplashModel extends BaseModel {
    private SplashActivity context;

    public SplashModel(SplashActivity context) {
        this.context = context;
    }

    public void setHeardColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        context.getWindow().setBackgroundDrawable(null);
    }

}
