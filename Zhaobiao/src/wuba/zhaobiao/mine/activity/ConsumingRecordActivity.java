package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.ConsumingRecordModel;

/**
 * Created by 58 on 2016/8/19.
 */
public class ConsumingRecordActivity  extends BaseActivity<ConsumingRecordModel>{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumingrecord);
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
        model.initListView();
        model.createAdapter();
        model.setParamsForListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.setTopBarHeight();
        model.getCostRecord();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.pageDeadStatistics();
    }

    @Override
    public ConsumingRecordModel createModel() {
        return new ConsumingRecordModel(ConsumingRecordActivity.this);
    }
}
