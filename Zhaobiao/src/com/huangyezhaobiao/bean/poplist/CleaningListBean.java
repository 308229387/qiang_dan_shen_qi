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
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.holder.pop.CleaningBidHolder;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.MDUtils;

/**
 * Created by shenzhixin on 2015/12/5.
 * 保洁清洗的标地bean
 * "pushId":"11112222",//推送Id
    "pushTurn":"1",
    "cateId":"1111",    //区分标的的业务类型 装修和工商注册
    "displayType":"6",
    "bidId":"12312321",
    "time":"2015-05-15 18:49",
    "title":"保洁清洗-家庭保洁",
    "cleanSpace":"50平米以下",      //清洁面积
    "location":"北京-朝阳-大山子", //区域
    "serveTime":"2015-11-11",	//服务时间
    "bidState":"0"		//订单状态,如可抢\已经被抢
 */
public class CleaningListBean extends QDBaseBean{
    private long   pushId;
    private int    pushTurn;
    private int    displayType;
    private long   bidId;
    private String time;
    private String title;
    private String cleanSpace;
    private String location;
    private String serveTime;
    private int    bidState;

    /**
     *  edited by chemguangming =====start
     * */
    private String fee;//活动价
    private String originFee;//原价

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

    /**
     * ==============================end
     * */

    private CleaningBidHolder cleaningBidHolder;
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
    public long getPushId() {
        return pushId;
    }

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

    public String getCleanSpace() {
        return cleanSpace;
    }

    public void setCleanSpace(String cleanSpace) {
        this.cleanSpace = cleanSpace;
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

    @Override
    public int getBidState() {
        return bidState;
    }

    public void setBidState(int bidState) {
        this.bidState = bidState;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_cleaning_list;
    }

    @Override
    public void fillDatas() {
        /**
         *  edited by chemguangming =====start
         * */
        cleaningBidHolder.tv_cleaning_activeprice.setText("￥" + fee);
        if(TextUtils.equals(fee, originFee)){
            cleaningBidHolder.tv_cleaning_orignalprice.setVisibility(View.GONE);
        } else {
            cleaningBidHolder.tv_cleaning_orignalprice.setVisibility(View.VISIBLE);
            cleaningBidHolder.tv_cleaning_orignalprice.setText(originFee);
        }

        cleaningBidHolder.tv_cleaning_orignalprice.setText(originFee);
        /**
         * ==============================end
         * */
        cleaningBidHolder.tv_cleaning_size_content.setText("清洁面积:"+cleanSpace);
        cleaningBidHolder.tv_cleaning_service_time_content.setText("服务时间:"+serveTime);
        cleaningBidHolder.tv_cleaning_location_content.setText("服务区域:"+location);
        cleaningBidHolder.grab_cleaning_title.setText(title);
        cleaningBidHolder.grab_cleaning_time.setText(time);
        cleaningBidHolder.cleaning_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bidState==1){//不可抢
                    BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING);
                }else{
                    BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_ENABLE_BIDDING);
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
                cleaningBidHolder.grab_cleaning_knock.setBackgroundResource(R.drawable.t_not_bid_bg);
                cleaningBidHolder.grab_cleaning_knock.setClickable(false);//避免setListener已经设置了,bean被复用了，
                cleaningBidHolder.grab_cleaning_knock.setTextColor(context.getResources().getColor(R.color.transparent));
                cleaningBidHolder.grab_cleaning_knock.setText("已抢完");
                break;
            default:
                cleaningBidHolder.grab_cleaning_knock.setBackgroundResource(R.drawable.t_redbuttonselector);
                cleaningBidHolder.grab_cleaning_knock.setTextColor(context.getResources().getColor(R.color.white));
                cleaningBidHolder.grab_cleaning_knock.setText("抢单");
                cleaningBidHolder.grab_cleaning_knock.setClickable(true);
                cleaningBidHolder.grab_cleaning_knock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.itemClicked(cleaningBidHolder.grab_cleaning_knock.getId(), toPopPassBean());
                        MDUtils.servicePageMD(CleaningListBean.this.context, cateId, String.valueOf(bidId), MDConstans.ACTION_QIANG_DAN);
                    }
                });
                break;
        }

    }

    @Override
    public View initView(View convertView, LayoutInflater inflater, ViewGroup parent, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context                                       = context;
        this.adapter                                       = adapter;
        cleaningBidHolder                                  = new CleaningBidHolder();
        convertView                                        = inflater.inflate(getLayoutId(),parent,false);
        cleaningBidHolder.cleaning_item                    = convertView.findViewById(R.id.cleaning_item);

        cleaningBidHolder.grab_cleaning_knock              = (Button) convertView.findViewById(R.id.grab_main_knock);

        cleaningBidHolder.grab_cleaning_time               = (TextView) convertView.findViewById(R.id.grab_cleaning_time);
        cleaningBidHolder.grab_cleaning_title              = (TextView) convertView.findViewById(R.id.grab_cleaning_title);
        cleaningBidHolder.tv_cleaning_location_content     = (TextView) convertView.findViewById(R.id.tv_cleaning_location);
        cleaningBidHolder.tv_cleaning_service_time_content = (TextView) convertView.findViewById(R.id.tv_cleaning_service_time);
        cleaningBidHolder.tv_cleaning_size_content         = (TextView) convertView.findViewById(R.id.tv_cleaning_size);

        cleaningBidHolder.tv_cleaning_activeprice        = (TextView) convertView.findViewById(R.id.tv_main_fee);
        cleaningBidHolder.tv_cleaning_orignalprice         = (TextView) convertView.findViewById(R.id.tv_main_origin_fee);

        cleaningBidHolder.tv_cleaning_orignalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        convertView.setTag(cleaningBidHolder);
        return convertView;
    }

    @Override
    public void converseView(View convertView, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
        this.context = context;
        this.adapter = adapter;
        cleaningBidHolder = (CleaningBidHolder) convertView.getTag();
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
