package com.huangyezhaobiao.bean.mydetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.DetailsLogBeanUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * Created by 58 on 2015/8/25.
 * 工程装修的订单详情bean
 */
public class OrderDetailDecorateProjectBean extends QDDetailBaseBean {
    private Context context;
    private TextView tv_type_content;
    private TextView tv_size_content;
    private TextView tv_budget_content;
    private TextView tv_zx_type_content;
    private TextView tv_lf_time_content;
    private TextView tv_zx_time_content;
    private TextView tv_location_content;
    private TextView tv_needs_content;
    private TextView tv_ch_tel_content;
    private TextView tv_house_type_content;
    private TextView tv_details_address_content;
    private ImageView iv_tels;
    private String name;
    private String decoraType;//装修类型，都有的
    private String space;
    private String budget;
    private String type;
    private String measureTime;
    private String decorateTime;
    private String location;
    private String special;
    private String clientPhone;
    private String houseType;
    private String detailAddress;
    private ZhaoBiaoDialog dialog;

    @Override
    public View initView(Context context) {
        this.context = context;
        initDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_qiangdan_middle_decorate_project, null);
        tv_budget_content = (TextView) view.findViewById(R.id.tv_budget_content);
        tv_ch_tel_content = (TextView) view.findViewById(R.id.tv_ch_tel_content);
        tv_lf_time_content = (TextView) view.findViewById(R.id.tv_lf_time_content);
        tv_location_content = (TextView) view.findViewById(R.id.tv_location_content);
        tv_needs_content = (TextView) view.findViewById(R.id.tv_needs_content);
        tv_size_content = (TextView) view.findViewById(R.id.tv_size_content);
        tv_type_content = (TextView) view.findViewById(R.id.tv_type_content);
        tv_zx_time_content = (TextView) view.findViewById(R.id.tv_zx_time_content);
        tv_zx_type_content = (TextView) view.findViewById(R.id.tv_zx_type_content);
        tv_house_type_content = (TextView) view.findViewById(R.id.tv_house_type_content);
        tv_details_address_content = (TextView) view.findViewById(R.id.tv_details_address_content);
        iv_tels = (ImageView) view.findViewById(R.id.iv_tels);
        iv_tels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了打电话按钮
                BDMob.getBdMobInstance().onMobEvent(OrderDetailDecorateProjectBean.this.context, BDEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE);
                initDialog(OrderDetailDecorateProjectBean.this.context);
                dialog.show();

            }
        });
        fillDatas();
        return view;
    }


    /**
     * 填充数据
     */
    private void fillDatas() {
        tv_budget_content.setText(budget);
        tv_ch_tel_content.setText(clientPhone);
        tv_lf_time_content.setText(measureTime);
        tv_location_content.setText(location);
        tv_needs_content.setText(special);
        tv_size_content.setText(space);
        tv_type_content.setText(decoraType);
        tv_zx_time_content.setText(decorateTime);
        tv_zx_type_content.setText(type);
        tv_house_type_content.setText(houseType);
        tv_details_address_content.setText(detailAddress);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private void initDialog(Context context) {
        if (dialog == null) {
            dialog = new ZhaoBiaoDialog(context, context.getString(R.string.hint),context.getString(R.string.make_sure_tel));
            dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {

                @Override
                public void onDialogOkClick() {
                    LogUtils.LogE("assssshenaaa", "bidid:" + DetailsLogBeanUtils.bean.getBidID() + ",cateid:" + DetailsLogBeanUtils.bean.getCateID());
                    ActivityUtils.goToDialActivity(OrderDetailDecorateProjectBean.this.context, clientPhone);
                    MDUtils.OrderDetailsPageMD(QiangDanBaseFragment.orderState, DetailsLogBeanUtils.bean.getCateID() + "", orderId + "", MDConstans.ACTION_DOWN_TEL, clientPhone);
                    dialog.dismiss();
                    dialog = null;
                }

                @Override
                public void onDialogCancelClick() {
                    dialog.dismiss();
                    dialog = null;
                }
            });
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecoraType() {
        return decoraType;
    }

    public void setDecoraType(String decoraType) {
        this.decoraType = decoraType;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }

    public String getDecorateTime() {
        return decorateTime;
    }

    public void setDecorateTime(String decorateTime) {
        this.decorateTime = decorateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
