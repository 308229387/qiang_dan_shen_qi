package com.huangyezhaobiao.bean.mylist;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BiddingDetailsActivity;
import com.huangyezhaobiao.holder.order.NannyOrderHolder;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenzhixin on 2015/12/7.
 * 保姆月嫂订单列表页的listBean
 * "cateId":"1111",    //区分标的的业务类型 装修和工商注册
   "displayType":"5",
   "orderId":"12312321",
   "time":"2015-05-15 18:49",
   "title":"保姆月嫂-保姆(住家)",
   "employTime":"1个月",      //雇佣时间
   "age":"30-35岁",			  //年龄限制
   "experience":"不限经验", 	  //经验年限
   "location":"北京-朝阳-大山子", //区域
   "startTime":"2015-11-11",	//开始时间
   "phone":"13564782223",		//发标用户手机
   "orderState":"0"			//订单状态，如待服务(支付成功)\服务中(支付成功后到客服确认线下交易成功前)\已结束(已服务/未服务)
 */
public class NannyOrderListBean extends QDBaseBean{
    private int displayType;
    private String orderId;
    private String time;
    private String title;
    private String employTime;
    private String age;
    private String experience;
    private String location;
    private String startTime;
    private String phone;
    private String orderState;
    //2015.12.8 add
    private String customerName;
    private String refundText;

    private String detailAddress;
    //2015.12.8 add end
    private NannyOrderHolder nannyOrderHolder;
    private ZhaoBiaoDialog dialog;

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRefundText() {
        return refundText;
    }

    public void setRefundText(String refundText) {
        this.refundText = refundText;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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
        return R.layout.item_nanny_order_list;
    }

    @Override
    public void fillDatas() {
//        initDialog(context);
        BiddingDetailsActivity.orderState = orderState;
        BiddingDetailsActivity.time =time;
        //判断状态
        if(!TextUtils.isEmpty(orderState)){ //获取订单状态
            if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_FINISH)){ //已完成(成交)
                nannyOrderHolder.iv_order_state_line.setVisibility(View.GONE);
                nannyOrderHolder.tv_order_state.setText(R.string.over_done);
                nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("已结束").append("(").append(refundText).append(")");
                    nannyOrderHolder.tv_order_state.setText(sb.toString());
                    nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                }

            }else if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_CANCEL)){ //已完成(未成交)
                nannyOrderHolder.iv_order_state_line.setVisibility(View.GONE);
                nannyOrderHolder.tv_order_state.setText(R.string.over_undone);
                nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单") ){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("已结束").append("(").append(refundText).append(")");
                    nannyOrderHolder.tv_order_state.setText(sb.toString());
                    nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                }
            }else if(TextUtils.equals(orderState, Constans.READY_SERVICE)){
                nannyOrderHolder.iv_order_state_line.setVisibility(View.VISIBLE);
                nannyOrderHolder.iv_order_state_line.setImageResource(R.drawable.onservice_order_state);
                nannyOrderHolder.tv_order_state.setText(R.string.unservice);
                nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单") ){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("待服务").append("(").append(refundText).append(")");
                    nannyOrderHolder.tv_order_state.setText(sb.toString());
                    nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                }

            }else if(TextUtils.equals(orderState, Constans.ON_SERVICE)){
                nannyOrderHolder.iv_order_state_line.setVisibility(View.VISIBLE);
                nannyOrderHolder.iv_order_state_line.setImageResource(R.drawable.servicing_order_state);
                nannyOrderHolder.tv_order_state.setText(R.string.servicing);
                nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("服务中").append("(").append(refundText).append(")");
                    nannyOrderHolder.tv_order_state.setText(sb.toString());
                    nannyOrderHolder.tv_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                }
            }
        }

//        //退单
//        //打电话按钮
//        nannyOrderHolder.btn_alreadry_contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //回调到OrderListActivity中去
//                EventAction action = new EventAction(EventType.EVENT_TELEPHONE_FROM_LIST, new TelephoneBean(orderId, TelephoneBean.SOURCE_LIST));
//                EventbusAgent.getInstance().post(action);
//                //点击了打电话按钮
//                BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_LIST_PHONE);
//
//                HYMob.getDataListByCall(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_REFUND, orderId, "0");
//
//                initDialog(NannyOrderListBean.this.context);
//                dialog.show();
//            }
//        });

        //点击事件
        nannyOrderHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiddingDetailsActivity.orderState = orderState;
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constans.ORDER_ID, orderId);
                ActivityUtils.goToActivityWithString(NannyOrderListBean.this.context, BiddingDetailsActivity.class, map);
//                MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_DETAILS);
                HYMob.getDataListByServiceState(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE);

            }
        });

        nannyOrderHolder.tv_nanny_order_title.setText(title);
        nannyOrderHolder.tv_time_nanny_qd_content.setText(TimeUtils.formatDateTime(time));
        nannyOrderHolder.tv_nanny_location.setText(location);
        nannyOrderHolder.tv_nanny_address_content.setText(detailAddress);
        nannyOrderHolder.tv_customer_name_content.setText(customerName);

//        nannyOrderHolder.tv_telephone.setText(phone);
//        nannyOrderHolder.tv_nanny_time_limit.setText(employTime);
//        nannyOrderHolder.tv_cleaning_age.setText(age);
//        nannyOrderHolder.tv_nanny_experience.setText(experience);
//        nannyOrderHolder.tv_nanny_time.setText(startTime);
    }

//    private void initDialog(Context context) {
//        if(dialog == null){
//            dialog = new ZhaoBiaoDialog(context, context.getString(R.string.hint), context.getString(R.string.make_sure_tel));
//            dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener(){
//
//                @Override
//                public void onDialogOkClick() {
//                    ActivityUtils.goToDialActivity(NannyOrderListBean.this.context, phone);
//                    MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_UP_TEL);
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onDialogCancelClick() {
//                    dialog.dismiss();
//                }
//            });
//        }
//    }

    @Override
    public View initView(View convertView, LayoutInflater inflater, ViewGroup parent, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context                              = context;
        this.adapter                              = adapter;
        nannyOrderHolder                          = new NannyOrderHolder();
        convertView                               = inflater.inflate(getLayoutId(),parent,false);
        nannyOrderHolder.tv_nanny_order_title     = (TextView) convertView.findViewById(R.id.tv_nanny_order_title);
        nannyOrderHolder.tv_time_nanny_qd_content = (TextView) convertView.findViewById(R.id.tv_time_nanny_qd_content);
        nannyOrderHolder.tv_nanny_location        = (TextView) convertView.findViewById(R.id.tv_nanny_location);
        nannyOrderHolder.tv_nanny_address_content = (TextView) convertView.findViewById(R.id.tv_nanny_address_content);
        nannyOrderHolder.tv_customer_name_content = (TextView) convertView.findViewById(R.id.tv_customer_name_content);
        nannyOrderHolder.iv_order_state_line = (ImageView) convertView.findViewById(R.id.iv_order_state_line);
        nannyOrderHolder.tv_order_state = (TextView) convertView.findViewById(R.id.tv_order_state);
        nannyOrderHolder.ll                       = convertView.findViewById(R.id.ll);

//        nannyOrderHolder.btn_alreadry_contact     = (ImageView) convertView.findViewById(R.id.btn_alreadry_contact);
//        nannyOrderHolder.rl_maybe_not             = convertView.findViewById(R.id.rl_maybe_not);
//        nannyOrderHolder.tv_cleaning_age          = (TextView) convertView.findViewById(R.id.tv_cleaning_age);
//        nannyOrderHolder.tv_message               = (TextView) convertView.findViewById(R.id.tv_message);
//        nannyOrderHolder.tv_nanny_experience      = (TextView) convertView.findViewById(R.id.tv_nanny_experience);
//        nannyOrderHolder.tv_nanny_time            = (TextView) convertView.findViewById(R.id.tv_nanny_time);
//        nannyOrderHolder.tv_nanny_time_limit      = (TextView) convertView.findViewById(R.id.tv_nanny_time_limit);
//        nannyOrderHolder.tv_telephone             = (TextView) convertView.findViewById(R.id.tv_telephone);
        convertView.setTag(nannyOrderHolder);
        return convertView;
    }

    @Override
    public void converseView(View convertView, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context     = context;
        this.adapter     = adapter;
        nannyOrderHolder = (NannyOrderHolder) convertView.getTag();
    }
}
