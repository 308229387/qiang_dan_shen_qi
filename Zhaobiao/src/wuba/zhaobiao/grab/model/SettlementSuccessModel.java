package wuba.zhaobiao.grab.model;

import android.content.Intent;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.Utils;

import de.greenrobot.event.EventBus;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.grab.activity.SettlementSuccessActivity;
import wuba.zhaobiao.respons.BusinessSettlementRespons;

/**
 * Created by SongYongmeng on 2016/9/13.
 */
public class SettlementSuccessModel extends BaseModel implements View.OnClickListener {
    private SettlementSuccessActivity context;
    private LinearLayout backLayout;
    private TextView txt_head;
    private Button toOrderList;
    private Button toBusinessList;
    private View headView;
    private TextView businessNum;
    private TextView businessPrice;
    private TextView businessTag;

    public SettlementSuccessModel(SettlementSuccessActivity context) {
        this.context = context;
    }

    public void initView() {
        backLayout = (LinearLayout) context.findViewById(R.id.back_layout);
        txt_head = (TextView) context.findViewById(R.id.txt_head);
        headView = (View) context.findViewById(R.id.layout_head);
        toOrderList = (Button) context.findViewById(R.id.success_list);
        toBusinessList = (Button) context.findViewById(R.id.success_businesslist);
        businessNum = (TextView) context.findViewById(R.id.success_business_text);
        businessPrice = (TextView) context.findViewById(R.id.success_business_price);
        businessTag = (TextView) context.findViewById(R.id.business_success_tag);
    }

    public void setState() {
        backLayout.setVisibility(View.VISIBLE);
    }

    public void setTitle() {
        txt_head.setText("购买成功");
    }

    public void setListener() {
        toOrderList.setOnClickListener(this);
        toBusinessList.setOnClickListener(this);
        backLayout.setOnClickListener(this);
    }

    public void setTopBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = Utils.getStatusBarHeight(context);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (headView != null)
                headView.setPadding(0, height + more, 0, 0);
        }
    }

    public void setTopBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().setBackgroundDrawable(null);
    }

    private void success(BusinessSettlementRespons temp) {
        String num = String.format(context.getResources().getString(R.string.business_suc_num), temp.getSuccCount());
        String price = String.format(context.getResources().getString(R.string.business_suc_price), temp.getTotalFee());
        businessNum.setText(num);
        businessPrice.setText(price);
    }

    private void moneyNotEnough(BusinessSettlementRespons temp) {
        success(temp);
        String moneyNotEnoughText = String.format(context.getResources().getString(R.string.business_suc_price_tag), temp.getSuccCount());
        businessTag.setText(moneyNotEnoughText);
    }

    private void otherTag(BusinessSettlementRespons temp) {
        success(temp);
        int tempInt = Integer.parseInt(temp.getTotalCount()) - Integer.parseInt(temp.getSuccCount());
        String otherTag = String.format(context.getResources().getString(R.string.business_suc_tag), temp.getTotalCount(), tempInt + "", temp.getSuccCount());
        businessTag.setText(otherTag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.success_list:
                EventBus.getDefault().post(new BusinessResultMessage("order_list"));
                context.finish();
                break;
            case R.id.success_businesslist:
                EventBus.getDefault().post(new BusinessResultMessage("business_opportunity"));
                context.finish();
                break;
            case R.id.back_layout:
                back();
                break;
            default:
                break;
        }

    }

    private void back() {
        context.onBackPressed();
    }

    public void getIntent() {
        Intent intent = context.getIntent();
        BusinessSettlementRespons temp = (BusinessSettlementRespons) intent.getSerializableExtra("value");
        switch (temp.getState()) {
            case "1":
                success(temp);
                break;
            case "3":
                moneyNotEnough(temp);
                break;
            case "4":
                otherTag(temp);
                break;
            default:
                break;
        }
    }


    public class BusinessResultMessage {
        private String msg;

        public String getMsg() {
            return msg;
        }

        public BusinessResultMessage(String msg) {
            this.msg = msg;
        }
    }
}

