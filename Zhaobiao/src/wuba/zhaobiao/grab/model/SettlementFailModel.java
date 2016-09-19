package wuba.zhaobiao.grab.model;

import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.Utils;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.grab.activity.SettlementFailActivity;

/**
 * Created by SongYongmeng on 2016/9/13.
 */
public class SettlementFailModel extends BaseModel implements View.OnClickListener {
    private SettlementFailActivity context;
    private View headView;
    private TextView head;
    private LinearLayout backLayout;
    private TextView hint1;
    private TextView hint2;

    public SettlementFailModel(SettlementFailActivity context) {
        this.context = context;
    }

    public void initView(String failType) {
        backLayout = (LinearLayout) context.findViewById(R.id.back_layout);
        headView = (View) context.findViewById(R.id.layout_head);
        backLayout.setVisibility(View.VISIBLE);
        head = (TextView) context.findViewById(R.id.txt_head);
        head.setText("购买失败");
        hint1 = (TextView) context.findViewById(R.id.txt_hint1);
        hint2 = (TextView) context.findViewById(R.id.txt_hint2);
        if (failType.equals("5")) {
            hint1.setText("系统刚刚开了个小差，一会儿再试试吧~");
            hint2.setVisibility(View.GONE);
        }else if (failType.equals("2")){
            hint1.setText("您选中的商机太火爆，已经被别人买走");
            hint2.setVisibility(View.VISIBLE);
        }
    }

    public void setListener() {
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

    @Override
    public void onClick(View v) {
        context.finish();
    }
}

