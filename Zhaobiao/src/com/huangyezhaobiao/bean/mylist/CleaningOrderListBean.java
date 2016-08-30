package com.huangyezhaobiao.bean.mylist;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.FetchDetailsActivity;
import com.huangyezhaobiao.holder.order.CleanindOrderHolder;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenzhixin on 2015/12/7.
 * "cateId":"1111",    //区分标的的业务类型 装修和工商注册
 "displayType":"6",
 "orderId":"12312321",
 "time":"2015-05-15 18:49",
 "title":"保洁清洗-家庭保洁",
 "cleanSpace":"50平米以下",      //清洁面积
 "needTools":"用户自备洁具",   //是否自备洁具
 "location":"北京-朝阳-大山子", //区域
 "serveTime":"2015-11-11",	//服务时间
 "phone":"13564782223",		//发标用户手机
 "orderState":"0"			//订单状态，如待服务(支付成功)\服务中(支付成功后到客服确认线下交易成功前)\已结束(已服务/未服务)
 */
public class CleaningOrderListBean extends QDBaseBean{
    private int displayType;
    private String orderId;
    private String time;
    private String title;
    private String cleanSpace;
    private String needTools;
    private String location;
    private String serveTime;
    private String phone;
    //2015.12.8 add
    private String customerName;
    private String refundText;

    //2015.12.8 add end

    private String detailAddress;
    private String orderState;
    private CleanindOrderHolder cleanindOrderHolder;
    private ZhaoBiaoDialog dialog;

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public String getRefundText(){
        return refundText;
    }

    public void setRefundText(String refundText){
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
        return R.layout.item_cleaning_order_list;
    }

    @Override
    public void fillDatas() {
//        initDialog(context);
        FetchDetailsActivity.orderState = orderState;
        FetchDetailsActivity.time =time;

        cleanindOrderHolder.tv_cleaning_order_title.setText(title);
        cleanindOrderHolder.tv_cleaning_qd_time_content.setText(TimeUtils.formatDateTime(time));
        cleanindOrderHolder.tv_cleaning_location.setText(location);
        cleanindOrderHolder.tv_clean_address_content.setText(detailAddress);
        cleanindOrderHolder.tv_customer_name_content.setText(customerName);


//        cleanindOrderHolder.tv_telephone.setText(phone);
//        cleanindOrderHolder.tv_cleaning_time.setText(serveTime);
//        cleanindOrderHolder.tv_cleaning_need_tools.setText(needTools);
//        cleanindOrderHolder.tv_cleaning_size.setText(cleanSpace);
        //判断状态
        if(!TextUtils.isEmpty(orderState)){ //获取订单状态
            if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_FINISH)){ //已完成(成交)
                cleanindOrderHolder.iv_cleaning_order_state_line.setVisibility(View.GONE);
                cleanindOrderHolder.tv_cleaning_order_state.setText(R.string.over_done);
                cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("已结束(成交)").append("(").append(refundText).append(")");
                    cleanindOrderHolder.tv_cleaning_order_state.setText(sb.toString());
                    cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                }
            }else if( TextUtils.equals(orderState, Constans.DONE_FRAGMENT_CANCEL)){ //已完成(未成交)
                cleanindOrderHolder.iv_cleaning_order_state_line.setVisibility(View.GONE);
                cleanindOrderHolder.tv_cleaning_order_state.setText(R.string.over_undone);
                cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                if(TextUtils.isEmpty(refundText) ||TextUtils.equals(refundText,"未退单")){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("已结束(未成交)").append("(").append(refundText).append(")");
                    cleanindOrderHolder.tv_cleaning_order_state.setText(sb.toString());
                    cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#C5C5C5"));
                }
            }else if(TextUtils.equals(orderState, Constans.READY_SERVICE)){
                cleanindOrderHolder.iv_cleaning_order_state_line.setVisibility(View.VISIBLE);
                cleanindOrderHolder.iv_cleaning_order_state_line.setImageResource(R.drawable.onservice_order_state);
                cleanindOrderHolder.tv_cleaning_order_state.setText(R.string.unservice);
                cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("待服务").append("(").append(refundText).append(")");
                    cleanindOrderHolder.tv_cleaning_order_state.setText(sb.toString());
                    cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                }
            }else if(TextUtils.equals(orderState, Constans.ON_SERVICE)){
                cleanindOrderHolder.iv_cleaning_order_state_line.setVisibility(View.VISIBLE);
                cleanindOrderHolder.iv_cleaning_order_state_line.setImageResource(R.drawable.servicing_order_state);
                cleanindOrderHolder.tv_cleaning_order_state.setText(R.string.servicing);
                cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("服务中").append("(").append(refundText).append(")");
                    cleanindOrderHolder.tv_cleaning_order_state.setText(sb.toString());
                    cleanindOrderHolder.tv_cleaning_order_state.setTextColor(Color.parseColor("#4EC5BF"));
                }
            }
        }
        //判断状态end
//        //打电话按钮
//        cleanindOrderHolder.btn_alreadry_contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //回调到OrderListActivity中去
//                EventAction action = new EventAction(EventType.EVENT_TELEPHONE_FROM_LIST,new TelephoneBean(orderId,TelephoneBean.SOURCE_LIST));
//                EventbusAgent.getInstance().post(action);
//                //点击了打电话按钮
//                BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_LIST_PHONE);
//
//                HYMob.getDataListByCall(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_REFUND,orderId,"0");
//
//                initDialog(CleaningOrderListBean.this.context);
//                dialog.show();
//            }
//        });

        //点击事件
        cleanindOrderHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchDetailsActivity.orderState = orderState;
                Map<String, String> map = new HashMap<String, String>();
                LogUtils.LogE("ashenFetch", "orderid:" + orderId);
                map.put(Constans.ORDER_ID, orderId);
                ActivityUtils.goToActivityWithString(CleaningOrderListBean.this.context, FetchDetailsActivity.class, map);
//                MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_DETAILS);

                HYMob.getDataListByServiceState(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE);
            }
        });
    }


//    private void initDialog(Context context) {
//        if(dialog == null){
//            dialog = new ZhaoBiaoDialog(context, context.getString(R.string.hint), context.getString(R.string.make_sure_tel));
//            dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener(){
//
//                @Override
//                public void onDialogOkClick() {
//                    ActivityUtils.goToDialActivity(CleaningOrderListBean.this.context, phone);
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
        this.context = context;
        this.adapter = adapter;
        cleanindOrderHolder = new CleanindOrderHolder();
        convertView         = inflater.inflate(getLayoutId(),parent,false);
        cleanindOrderHolder.tv_cleaning_order_title     = (TextView) convertView.findViewById(R.id.tv_cleaning_order_title);
        cleanindOrderHolder.tv_cleaning_qd_time_content = (TextView) convertView.findViewById(R.id.tv_cleaning_qd_time_content);
        cleanindOrderHolder.tv_cleaning_location        = (TextView) convertView.findViewById(R.id.tv_cleaning_location);
        cleanindOrderHolder.tv_clean_address_content = (TextView) convertView.findViewById(R.id.tv_clean_address_content);
        cleanindOrderHolder.tv_customer_name_content    = (TextView) convertView.findViewById(R.id.tv_customer_name_content);
        cleanindOrderHolder.iv_cleaning_order_state_line = (ImageView) convertView.findViewById(R.id.iv_cleaning_order_state_line);
        cleanindOrderHolder.tv_cleaning_order_state = (TextView) convertView.findViewById(R.id.tv_cleaning_order_state);
        cleanindOrderHolder.ll                          = convertView.findViewById(R.id.ll);

//        cleanindOrderHolder.tv_message                  = (TextView) convertView.findViewById(R.id.tv_message);
//        cleanindOrderHolder.tv_telephone                = (TextView) convertView.findViewById(R.id.tv_telephone);
//        cleanindOrderHolder.btn_alreadry_contact        = (ImageView) convertView.findViewById(R.id.btn_alreadry_contact);
//        cleanindOrderHolder.rl_maybe_not                = convertView.findViewById(R.id.rl_maybe_not);
//        cleanindOrderHolder.tv_cleaning_size            = (TextView) convertView.findViewById(R.id.tv_cleaning_size);
//        cleanindOrderHolder.tv_cleaning_need_tools      = (TextView) convertView.findViewById(R.id.tv_cleaning_need_tools);
//        cleanindOrderHolder.tv_cleaning_time            = (TextView) convertView.findViewById(R.id.tv_cleaning_time);

        convertView.setTag(cleanindOrderHolder);
        return convertView;
    }

    @Override
    public void converseView(View convertView, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context = context;
        this.adapter = adapter;
        cleanindOrderHolder = (CleanindOrderHolder) convertView.getTag();
    }
}
