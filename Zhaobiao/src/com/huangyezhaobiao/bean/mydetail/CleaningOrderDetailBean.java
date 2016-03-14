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
 * Created by shenzhixin on 2015/12/7.
 * 抢单详情页的家庭清洗的业务bean
 *"name":"基本信息",
 "serviceType":"保洁清洗-家庭保洁",
 "cleanSpace":"50平米以下",      //清洁面积
 "needTools":"用户自备洁具",   //是否自备洁具
 "location":"北京-朝阳-大山子", //区域
 "serveTime":"2015-11-11",	//服务时间
 "budget":"预算3-5万",
 */
public class CleaningOrderDetailBean extends QDDetailBaseBean{
    private String name;
    private String serviceType;
    private String cleanSpace;
    private String needTools;
    private String location;
    private String serveTime;
    private String budget;
    private String special;
    private String clientPhone;
    private String detailAddress;
    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private ZhaoBiaoDialog dialog;

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCleanSpace() {
        return cleanSpace;
    }

    public void setCleanSpace(String cleanSpace) {
        this.cleanSpace = cleanSpace;
    }

    public String getNeedTools() {
        return needTools;
    }

    public void setNeedTools(String needTools) {
        this.needTools = needTools;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServeTime() {
        return serveTime;
    }

    public void setServeTime(String serveTime) {
        this.serveTime = serveTime;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
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

    @Override
    public View initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.cleaning_order_detail_layout,null);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_type_content)).setText(serviceType);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_size_content)).setText(cleanSpace);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_location_content)).setText(location);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_time)).setText(serveTime);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_cleaning_tools)).setText(needTools);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_budget)).setText(budget);
        ((TextView)view.findViewById(R.id.cleaning_order_detail_special)).setText(special);
        ((TextView)view.findViewById(R.id.tv_ch_tel_content)).setText(clientPhone);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_detail_age)).setText(age);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_detail_address_content)).setText(detailAddress);
        ((ImageView)view.findViewById(R.id.iv_tels)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了打电话按钮
                BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE);
                initDialog(context);
                dialog.show();
            }
        });
        return view;
    }

    private void initDialog(final Context context) {
        if(dialog == null){
            dialog = new ZhaoBiaoDialog(context, "提示", "确定要拨打电话?");
            dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {

                @Override
                public void onDialogOkClick() {
                    LogUtils.LogE("assssshenaaa", "bidid:" + DetailsLogBeanUtils.bean.getBidID() + ",cateid:" + DetailsLogBeanUtils.bean.getCateID());
                    ActivityUtils.goToDialActivity(context, clientPhone);
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
}
