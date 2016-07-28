package wuba.zhaobiao.common.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.huangyezhaobiao.utils.ToastUtils;

import wuba.zhaobiao.common.model.BaseModel;

/**
 * Created by SongYongmeng on 2016/7/28.
 */
public abstract class BaseActivity<T extends BaseModel> extends FragmentActivity {
    protected T model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initModel();
    }

    private void initModel() {
        model = createModel();
    }


    public abstract T createModel();

    public void showToast(String str){
        ToastUtils.showToast(str);
    }

    public void showToast(int i){
        ToastUtils.showToast(i);
    }
}
