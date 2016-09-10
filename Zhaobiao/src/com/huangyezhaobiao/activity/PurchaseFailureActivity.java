package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.view.TitleMessageBarLayout;

/**
 * 商机购买失败界面
 */
public class PurchaseFailureActivity extends QBBaseActivity{

    private LinearLayout back_layout;
    private TextView txt_head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_failure);

        initView();
        initListener();
    }

    @Override
    public void initView() {
        layout_back_head = getView(R.id.layout_head);
        back_layout = (LinearLayout) this.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        txt_head = (TextView) findViewById(R.id.txt_head);
        txt_head.setText("购买失败");
        tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
