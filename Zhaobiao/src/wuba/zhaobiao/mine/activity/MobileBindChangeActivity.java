package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.MobileBindChangeModel;

/**
 * Created by 58 on 2016/8/9.
 *  手机绑定的修改页面
 *  流程是
 *  一进来获取当前绑定的手机号
 *  获取成功:默认获取手机号验证码
 *  获取成功:弹窗，60秒倒计时
 *  输入新手机号码，点击提交
 *  提交成功---成功，返回
 *  提交失败---重新提交
 *
 *  获取失败:弹窗，可以重新点击获取
 *
 *
 *  获取失败:给出提示
 * 当两个手机号框都达到手机号的输入数字，并且验证码框有数字输入时，可以点击提交
 * 否则提交是不能够点击的
 */
public class MobileBindChangeActivity extends BaseActivity<MobileBindChangeModel>{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_bind_change);
        init();
    }
    private void init(){
        setTopBarColor();
        initData();
        initView();
    }

    private void setTopBarColor(){
        model.setTopBarColor();
    }

    private void initData(){
        model.getMobile();
    }

    private void initView(){
        model.initHeader();
        model.initNowBindMobile();
        model.initNowValidateCode();
        model.initGetCode();
        model.initNewValidateMobile();
        model.initSubmit();
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
    public MobileBindChangeModel createModel() {
        return new MobileBindChangeModel(MobileBindChangeActivity.this);
    }
}
