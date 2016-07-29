package wuba.zhaobiao.common.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.ToastUtils;

import wuba.zhaobiao.common.model.BaseModel;

/**
 * Created by SongYongmeng on 2016/7/28.
 * 描    述：基类
 */
public abstract class BaseActivity<T extends BaseModel> extends FragmentActivity {
    protected T model;
    public long resume_time;
    public long stop_time;

    private final static String CLOSE_ACTIVITTY = "CLOSE_ACTIVITTY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume_time = System.currentTimeMillis();
        BDMob.getBdMobInstance().onResumeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BDMob.getBdMobInstance().onPauseActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();
    }

    public void showToast(String str) {
        ToastUtils.showToast(str);
    }

    public void showToast(int i) {
        ToastUtils.showToast(i);
    }

    private void initModel() {
        model = createModel();
    }

    public abstract T createModel();

}
