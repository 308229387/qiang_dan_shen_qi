package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.UpdateAccountModel;

/**
 * Created by 58 on 2016/8/18.
 */
public class UpdateAccountActivity extends BaseActivity<UpdateAccountModel>{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        init();
    }

    private void init(){
        setTopBarColor();
        initView();
        initData();
    }

    private void setTopBarColor(){
        model.setTopBarColor();
    }

    private void initView(){
        model.initHeader();
        model.initUpdateUser();
        model.initUpdatePhone();
        model.initBaseHelp();
        model.initUpdateSave();
        model.initUpdateGrabAndOrder();
    }

    private void initData(){
        model.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.setTopBarHeight();
    }

    @Override
    public void onBackPressed() {
       model.initSaveDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.statisticsDeadTime();
    }

    @Override
    public UpdateAccountModel createModel() {
        return new UpdateAccountModel(UpdateAccountActivity.this);
    }
}
