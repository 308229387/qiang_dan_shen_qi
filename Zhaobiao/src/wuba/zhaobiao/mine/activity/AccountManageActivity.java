package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.AccountManageModel;

/**
 * Created by 58 on 2016/8/18.
 */
public class AccountManageActivity extends BaseActivity<AccountManageModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manange);
        init();
    }
    private void init(){
        setTopBarColor();
        initView();
    }

    private void setTopBarColor(){
        model.setTopBarColor();
    }

    private void initView(){
        model.initHeader();
        model.initEdit();
        model.initAccountList();
        model.initAdd();
        model.initDivider();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.setTopBarHeight();
        model.getChildAccountList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.statisticsDeadTime();
    }

    @Override
    public AccountManageModel createModel() {
        return new AccountManageModel(AccountManageActivity.this);
    }
}
