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
 * 保姆月嫂的订单详情业务bean
 * "name":"基本信息",
 "serviceType":"保姆月嫂-保姆（住家）",
 "employTime":"1个月",      //雇佣时间
 "age":"30-35岁",			  //年龄限制
 "experience":"不限经验", 	  //经验年限
 "budget":"预算3-5万",
 "location":"北京-朝阳-大山子", //区域
 "startTime":"2015-11-11",	//开始时间
 */
public class NannyOrderDetailBean extends QDDetailBaseBean{
    private String serviceType;
    private String employTime;
    private String age;
    private String experience;
    private String budget;
    private String location;
    private String startTime;
    private String special;
    private String clientPhone;
    private String detailAddress;
    private ZhaoBiaoDialog dialog;


    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getEmployTime() {
        return employTime;
    }

    public void setEmployTime(String employTime) {
        this.employTime = employTime;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public View initView(final Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_nanny_order_detail,null);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_service_type_content)).setText("服务类型:   "+serviceType);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_hire_time_content)).setText("雇佣时间:    "+employTime);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_service_location_content)).setText("服务区域:    "+location);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_age_content)).setText("年龄要求:    "+age);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_experience_content)).setText("经验年限:    "+experience);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_budget)).setText("预    算：    "+budget);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_start_time)).setText("开始时间:    "+startTime);
        ((TextView)rootView.findViewById(R.id.nanny_order_detail_special)).setText("特殊需求:    "+special);
      //  ((TextView)rootView.findViewById(R.id.tv_ch_tel_content)).setText(clientPhone);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_service_detail_address_content)).setText("详细地址:    "+detailAddress);
        /*((ImageView)rootView.findViewById(R.id.iv_tels)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了打电话按钮
                BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE);
                initDialog(context);
                dialog.show();
            }
        });*/
        return rootView;
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
