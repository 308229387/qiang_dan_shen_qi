package com.huangyezhaobiao.bean.poplist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.holder.pop.NannyBidHolder;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.UserUtils;

/**
 * Created by shenzhixin on 2015/12/4.
 * 保姆月嫂的listBean
 * "pushId":"11112222",//推送Id
 "pushTurn":"1",
 "cateId":"1111",    //区分标的的业务类型 装修和工商注册
 "displayType":"5",
 "bidId":"12312321",
 "time":"2015-05-15 18:49",
 "title":"保姆月嫂-保姆(住家)",
 "employTime":"1个月",      //雇佣时间
 "location":"北京-朝阳-大山子", //区域
 "budget":"面议",				//预算
 "startTime":"2015-11-11",	//开始时间
 "bidState":"0"		//订单状态,如可抢\已经被抢
 */
public class NannyListBean extends QDBaseBean{
    private long pushId;
    private int pushTurn;
    private int displayType;
    private long bidId;
    private String time;
    private String title;
    private String employTime;
    private String location;
    private String budget;
    private String startTime;
    private int bidState;
    private NannyBidHolder nannyBidHolder;

    //RD. shenzhixin 2016.3.29 add for o2o-709
    private String fee;//活动价
    private String originFee;//原价
    //RD. shenzhixin 2016.3.29 add for o2o-709

    public void setPushId(long pushId) {
        this.pushId = pushId;
    }

    public int getPushTurn() {
        return pushTurn;
    }

    public void setPushTurn(int pushTurn) {
        this.pushTurn = pushTurn;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getOriginFee() {
        return originFee;
    }

    public void setOriginFee(String originFee) {
        this.originFee = originFee;
    }

    @Override
    public long getPushId() {
        return pushId;
    }

    @Override
    public long getBidId() {
        return bidId;
    }

    public void setBidId(long bidId) {
        this.bidId = bidId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public int getBidState() {
        return bidState;
    }

    public void setBidState(int bidState) {
        this.bidState = bidState;
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
        return R.layout.item_nanny_list;
    }

    @Override
    public void fillDatas() {
        //RD shenzhixin 2016.3.29 add for o2o-709
        nannyBidHolder.tv_main_fee.setText("￥"+fee);
        nannyBidHolder.tv_main_origin_fee.setText(originFee);
        nannyBidHolder.tv_main_origin_fee.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if(TextUtils.equals(fee,originFee)){//此时没有活动价
            nannyBidHolder.tv_main_origin_fee.setVisibility(View.INVISIBLE);
        }else{
            nannyBidHolder.tv_main_origin_fee.setVisibility(View.VISIBLE);
        }
        //RD shenzhixin 2016.3.29 add for o2o-709 end


        nannyBidHolder.grab_nanny_time.setText(TimeUtils.formatDateTime(time));
        nannyBidHolder.grab_nanny_title.setText(title);
        nannyBidHolder.tv_nanny_budget_content.setText("预算   "+budget);
        nannyBidHolder.tv_nanny_location_content.setText("区域   "+location);
        nannyBidHolder.tv_nanny_startTime_content.setText("开始时间   "+startTime);
        nannyBidHolder.tv_nanny_time_content.setText("雇佣时间   "+employTime);
        nannyBidHolder.nanny_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bidState==1){//不可抢
                    BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING);

                    HYMob.getDataListByState(context, HYEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING, String.valueOf(bidId), "0");

                }else{
                    BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_ENABLE_BIDDING);

                    HYMob.getDataListByState(context, HYEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING, String.valueOf(bidId), "1");

                }
                Intent intent = new Intent();
                intent.setClass(context, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("passBean", toPopPassBean());
                intent.putExtras(bundle);
                context.startActivity(intent);
                MDUtils.servicePageMD(context, cateId, String.valueOf(bidId), MDConstans.ACTION_DETAILS);
            }
        });
        switch (bidState){
            case 1://不可抢
                nannyBidHolder.grab_style.setImageResource(R.drawable.type_back_grabbed);
                nannyBidHolder.iv_nanny_type.setImageResource(R.drawable.iv_nanny_label);
                nannyBidHolder.view_nanny_bottom.setVisibility(View.GONE);
//                nannyBidHolder.grab_nanny_knock.setBackgroundResource(R.drawable.t_not_bid_bg);
//                nannyBidHolder.grab_nanny_knock.setText("已抢完");
//                nannyBidHolder.grab_nanny_knock.setTextColor(context.getResources().getColor(R.color.transparent));
//                nannyBidHolder.grab_nanny_knock.setClickable(false);
                break;
            default://可抢
                nannyBidHolder.grab_style.setImageResource(R.drawable.type_back_grab);
                nannyBidHolder.iv_nanny_type.setImageResource(R.drawable.iv_nanny_label_knock);
                nannyBidHolder.view_nanny_bottom.setVisibility(View.VISIBLE);

                String isSon = UserUtils.getIsSon(context);
                if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
                    String rbac = UserUtils.getRbac(context);
                    if (!TextUtils.isEmpty(rbac)
                            && TextUtils.equals("1", rbac) || TextUtils.equals("5", rbac)) {
                        nannyBidHolder.grab_nanny_knock.setBackgroundResource(R.color.t_gray);
                        nannyBidHolder.grab_nanny_knock.setTextColor(context.getResources().getColor(R.color.white));
                        nannyBidHolder.grab_nanny_knock.setText("抢单");
                        nannyBidHolder.grab_nanny_knock.setClickable(false);
                    }else{
                        nannyBidHolder.grab_nanny_knock.setBackgroundResource(R.drawable.bt_knock_button_selector);
                        nannyBidHolder.grab_nanny_knock.setTextColor(context.getResources().getColor(R.color.white));
                        nannyBidHolder.grab_nanny_knock.setText("抢单");
                        nannyBidHolder.grab_nanny_knock.setClickable(true);
                        nannyBidHolder.grab_nanny_knock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter.itemClicked(nannyBidHolder.grab_nanny_knock.getId(), toPopPassBean());
                                MDUtils.servicePageMD(NannyListBean.this.context, cateId, String.valueOf(bidId),
                                        MDConstans.ACTION_QIANG_DAN);
                            }
                        });
                    }

                } else {
                    nannyBidHolder.grab_nanny_knock.setBackgroundResource(R.drawable.bt_knock_button_selector);
                    nannyBidHolder.grab_nanny_knock.setTextColor(context.getResources().getColor(R.color.white));
                    nannyBidHolder.grab_nanny_knock.setText("抢单");
                    nannyBidHolder.grab_nanny_knock.setClickable(true);
                    nannyBidHolder.grab_nanny_knock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapter.itemClicked(nannyBidHolder.grab_nanny_knock.getId(), toPopPassBean());
                            MDUtils.servicePageMD(NannyListBean.this.context, cateId, String.valueOf(bidId),
                                    MDConstans.ACTION_QIANG_DAN);
                        }
                    });
                }

                break;
        }
    }

    @Override
    public View initView(View convertView, LayoutInflater inflater, ViewGroup parent, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context                              = context;
        this.adapter                              = adapter;
        convertView                               = inflater.inflate(getLayoutId(),parent,false);
        nannyBidHolder                            = new NannyBidHolder();
        nannyBidHolder.grab_style                 = (ImageView) convertView.findViewById(R.id.grab_style);
        nannyBidHolder.iv_nanny_type              = (ImageView) convertView.findViewById(R.id.iv_nanny_type);
        nannyBidHolder.grab_nanny_time            = (TextView) convertView.findViewById(R.id.grab_nanny_time);
        nannyBidHolder.grab_nanny_title           = (TextView) convertView.findViewById(R.id.grab_nanny_title);
        nannyBidHolder.tv_nanny_budget_content    = (TextView) convertView.findViewById(R.id.tv_nanny_budget);
        nannyBidHolder.tv_nanny_location_content  = (TextView) convertView.findViewById(R.id.tv_nanny_location);
        nannyBidHolder.tv_nanny_startTime_content = (TextView) convertView.findViewById(R.id.tv_nanny_startTime);
        nannyBidHolder.tv_nanny_time_content      = (TextView) convertView.findViewById(R.id.tv_nanny_time);
        nannyBidHolder.nanny_item                 = convertView.findViewById(R.id.nanny_item);

        nannyBidHolder.view_nanny_bottom          = convertView.findViewById(R.id.view_nanny_bottom);
        nannyBidHolder.grab_nanny_knock           = (Button) convertView.findViewById(R.id.grab_main_knock);
        nannyBidHolder.tv_main_fee                = (TextView) convertView.findViewById(R.id.tv_main_fee);
        nannyBidHolder.tv_main_origin_fee         = (TextView) convertView.findViewById(R.id.tv_main_origin_fee);

        convertView.setTag(nannyBidHolder);
        return convertView;
    }

    @Override
    public void converseView(View convertView, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context = context;
        this.adapter = adapter;
        nannyBidHolder = (NannyBidHolder) convertView.getTag();
    }


    public PushToPassBean toPopPassBean() {
        PushToPassBean bean = new PushToPassBean();
        bean.setBidId(bidId);
        bean.setPushId(pushId);
        bean.setPushTurn(pushTurn);
        bean.setCateId(Integer.parseInt(cateId));
        return bean;
    }
}
