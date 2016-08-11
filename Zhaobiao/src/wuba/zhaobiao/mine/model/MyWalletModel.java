package wuba.zhaobiao.mine.model;

import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.ConsumptionActivity;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.Utils;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.mine.activity.MyWalletActivity;

/**
 * Created by 58 on 2016/8/10.
 */
public class MyWalletModel extends BaseModel implements View.OnClickListener{

    private MyWalletActivity context;
    private  View layout_back_head;
    private View     back_layout;
    private TextView txt_head;
    private RelativeLayout rl_charge;

    public MyWalletModel(MyWalletActivity context){
        this.context = context;
    }

    public void initHeaderView(){
        createHeaderView();
        initHeaderBack();
        createHeaderTitle();
    }

    private void createHeaderView(){
        layout_back_head = context.findViewById(R.id.layout_head);
    }

    private void initHeaderBack(){
        createHeaderBack();
        setHeaderBackListener();
    }

    private void createHeaderBack(){
        back_layout  = context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setHeaderBackListener(){
        back_layout.setOnClickListener(this);
    }

    private void createHeaderTitle(){
        txt_head  = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText("我的钱包");
    }

    public void initCharge(){
        createCharge();
        setChargeListener();
    }

    private void createCharge(){
        rl_charge = (RelativeLayout) context.findViewById(R.id.rl_charge);
    }

    private void setChargeListener(){
        rl_charge.setOnClickListener(this);
    }

    public void setTopBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = Utils.getStatusBarHeight(context);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }

    public void setTopBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                back();
                break;
            case R.id.rl_charge:
                goToConsumptionPage();
                goToConsumptionPageStatistics();
                break;
        }
    }

    private void back(){
        context.onBackPressed();
    }

    private void goToConsumptionPage(){
        ActivityUtils.goToActivity(context, ConsumptionActivity.class);
    }

    private void goToConsumptionPageStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_CONSUMPTION);
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_WALLET, context.stop_time - context.resume_time);
    }
}
