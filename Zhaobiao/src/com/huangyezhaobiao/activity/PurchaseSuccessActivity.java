package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.view.TitleMessageBarLayout;

/**
 * Created by 58 on 2016/9/2.
 */
public class PurchaseSuccessActivity extends QBBaseActivity{

    private LinearLayout back_layout;
    private TextView txt_head;
    private Button toOrderList;
    private Button toBusinessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_success);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        layout_back_head = getView(R.id.layout_head);
        back_layout = (LinearLayout) this.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        txt_head = (TextView) findViewById(R.id.txt_head);
        txt_head.setText("购买成功");
        tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);

        toOrderList = (Button) findViewById(R.id.success_list);
        toBusinessList = (Button) findViewById(R.id.success_businesslist);
    }

    @Override
    public void initListener() {
        toOrderList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //查看订单

            }
        });
        toBusinessList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //继续购买


            }
        });
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
