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
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.Utils;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.grab.activity.SettlementSuccessActivity;
import wuba.zhaobiao.respons.BusinessSettlementRespons;

/**
 * Created by SongYongmeng on 2016/9/13.
 */
public class SettlementSuccessModel extends BaseModel implements View.OnClickListener {
    private SettlementSuccessActivity context;
    private LinearLayout back_layout;
    private TextView txt_head;
    private Button toOrderList;
    private Button toBusinessList;
    private View headView;

    public SettlementSuccessModel(SettlementSuccessActivity context) {
        this.context = context;
    }

    public void initView() {
        back_layout = (LinearLayout) context.findViewById(R.id.back_layout);
        txt_head = (TextView) context.findViewById(R.id.txt_head);
        headView = (View) context.findViewById(R.id.layout_head);
        toOrderList = (Button) context.findViewById(R.id.success_list);
        toBusinessList = (Button) context.findViewById(R.id.success_businesslist);
    }

    public void setState() {
        back_layout.setVisibility(View.VISIBLE);
    }

    public void setTitle() {
        txt_head.setText("购买成功");
    }

    public void setListener() {
        toOrderList.setOnClickListener(this);
        toBusinessList.setOnClickListener(this);
    }

    public void setTopBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = Utils.getStatusBarHeight(context);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (headView != null) {
                headView.setPadding(0, height + more, 0, 0);
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
        switch (v.getId()) {
            case R.id.success_list:
                ToastUtils.showToast("chenggongjiemian");
                break;
            case R.id.success_businesslist:
                ToastUtils.showToast("business");
                break;
            default:
                break;
        }

    }

    public void getIntent() {
        Intent intent = context.getIntent();
        BusinessSettlementRespons temp = (BusinessSettlementRespons) intent.getSerializableExtra("value");
        ToastUtils.showToast(temp.getSuccCount());
    }
}

