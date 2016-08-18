package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.AddAccountModel;

/**
 * Created by 58 on 2016/8/18.
 */
public class AddAccountActivity extends BaseActivity<AddAccountModel>{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        init();
    }

    private void init (){
        setTopBarColor();
        initView();
    }

    private void setTopBarColor(){
        model.setTopBarColor();
    }

    private void initView(){
        model.initHeader();
        model.initUser();
        model.initPhone();
        model.initBaseHelp();
        model.initSave();
        model.initGrabAndOrder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.setTopBarHeight();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.statisticsDeadTime();
    }

    @Override
    public void onBackPressed() {
       model.closePage();
    }

    @Override
    public AddAccountModel createModel() {
        return new AddAccountModel(AddAccountActivity.this);
    }
}
