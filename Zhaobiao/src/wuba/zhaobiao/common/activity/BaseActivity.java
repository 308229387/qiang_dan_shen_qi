package wuba.zhaobiao.common.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.ToastUtils;

import wuba.zhaobiao.common.model.BaseModel;

/**
 * Created by SongYongmeng on 2016/7/28.
 * 描    述：基类,将Activity加入List管理
 */
public abstract class BaseActivity<T extends BaseModel> extends FragmentActivity {
    protected T model;
    public long resume_time;
    public long stop_time;
    protected final static String CLOSE_ACTIVITTY = "CLOSE_ACTIVITTY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insert(this);
        initModel();
    }

    public void insert(Activity context) {
        BiddingApplication.getInstance().addActivity(context);
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
