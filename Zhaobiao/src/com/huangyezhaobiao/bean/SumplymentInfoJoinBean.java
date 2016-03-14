package com.huangyezhaobiao.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;

/**
 * Created by shenzhixin on 2015/8/21.
 * 商机详情页的招商加盟的第三个字段，因为比较特殊，所以自己另搞了个bean
 */
public class SumplymentInfoJoinBean extends QDDetailBaseBean {
    private String name;
    private String special;

    public SumplymentInfoJoinBean() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    @Override
    public View initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_item_sumplyment, null);
        ((TextView) view.findViewById(R.id.detail_item_sumplyment_text1)).setText(special);
        ((TextView) view.findViewById(R.id.tv_ll)).setText("留言内容  :  ");
        return view;

    }
}
