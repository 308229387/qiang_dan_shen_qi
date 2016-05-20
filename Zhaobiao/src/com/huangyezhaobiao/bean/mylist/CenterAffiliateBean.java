package com.huangyezhaobiao.bean.mylist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.FetchDetailsActivity;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.holder.CenterAffiliateHolder;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 58 on 2015/8/18.
 * 抢单中心的招商加盟列表页的bean
 */
public class CenterAffiliateBean extends QDBaseBean {
    private ZhaoBiaoDialog dialog;
    private int displayType;
    private String orderId;
    private String time;
    private String consultCategory;
    private String budget;
    private String investKeywords;
    private String phone;
    private String orderState;
    private CenterAffiliateHolder holder;

    @Override
    public String getCateId() {
        return cateId;
    }

    @Override
    public int getDisplayType() {
        return displayType;
    }

    @Override
    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.center_affiliate_bean;
    }

    @Override
    public void fillDatas() {
        holder.tv_telephone.setText(phone);
        holder.time.setText(time);
        holder.investKeywords.setText(investKeywords);
        holder.budget.setText(budget);
        holder.consultCategory.setText(consultCategory);
        if (TextUtils.equals(QiangDanBaseFragment.orderState, Constans.DONE_FRAGMENT)) {
            holder.rl_maybe_not.setVisibility(View.VISIBLE);
            LogUtils.LogE("ashenasas", "lalala visible");
            if (TextUtils.equals(orderState, Constans.DONE_FRAGMENT_FINISH)) {
                holder.tv_message.setText("已结束:您已成功完成此单");
            } else if (TextUtils.equals(orderState, Constans.DONE_FRAGMENT_CANCEL)) {
                holder.tv_message.setText("已结束:请下次努力！");
            }
        } else {
            holder.rl_maybe_not.setVisibility(View.GONE);
            LogUtils.LogE("ashenasas", "lalala gone");
        }
        holder.btn_alreadry_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HYMob.getDataListByCall(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_REFUND, orderId, "0");
                String  data= HYMob.dataBeanToJson(HYMob.dataList, "co","callStyle","orderId","serviceSate", "sa", "cq");
                HYMob.createMap(context, data, "0") ; //0表示正常日志，1表示崩溃日志

                initDialog(CenterAffiliateBean.this.context);
                dialog.show();
            }
        });

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchDetailsActivity.orderState = orderState;
                Map<String, String> map = new HashMap<String, String>();
                LogUtils.LogE("ashenFetch", "orderid:" + orderId);
                map.put(Constans.ORDER_ID, orderId);
                ActivityUtils.goToActivityWithString(CenterAffiliateBean.this.context, FetchDetailsActivity.class, map);
                MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_DETAILS);
            }
        });
        FetchDetailsActivity.orderState = orderState;
    }

    private void initDialog(Context context) {
        if (dialog == null) {
            dialog = new ZhaoBiaoDialog(context, "提示", "确定要拨打电话?");
            dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {

                @Override
                public void onDialogOkClick() {
                    ActivityUtils.goToDialActivity(CenterAffiliateBean.this.context, phone);
                    MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_UP_TEL);
                    dialog.dismiss();
                }

                @Override
                public void onDialogCancelClick() {
                    dialog.dismiss();
                }
            });
        }
    }


    @Override
    public View initView(View convertView, LayoutInflater inflater, ViewGroup parent, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context = context;
        this.adapter = adapter;
        holder = new CenterAffiliateHolder();
        convertView = inflater.inflate(R.layout.center_affiliate_bean, parent, false);
        holder.btn_alreadry_contact = (ImageView) convertView.findViewById(R.id.btn_alreadry_contact);
        holder.budget = (TextView) convertView.findViewById(R.id.budget);
        holder.consultCategory = (TextView) convertView.findViewById(R.id.consultCategory);
        holder.investKeywords = (TextView) convertView.findViewById(R.id.investKeywords);
        holder.ll = convertView.findViewById(R.id.ll);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.tv_telephone = (TextView) convertView.findViewById(R.id.tv_telephone);
        holder.rl_maybe_not = (RelativeLayout) convertView.findViewById(R.id.rl_maybe_not);
        holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void converseView(View convertView, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context = context;
        this.adapter = adapter;
        holder = (CenterAffiliateHolder) convertView.getTag();
    }


    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConsultCategory() {
        return consultCategory;
    }

    public void setConsultCategory(String consultCategory) {
        this.consultCategory = consultCategory;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getInvestKeywords() {
        return investKeywords;
    }

    public void setInvestKeywords(String investKeywords) {
        this.investKeywords = investKeywords;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
}
