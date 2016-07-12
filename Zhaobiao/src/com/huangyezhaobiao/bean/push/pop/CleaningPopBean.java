package com.huangyezhaobiao.bean.push.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;

/**
 * Created by shenzhixin on 2015/12/7.
 * 保洁清洗的推送新订单和推送结果bean
 "voice”:”规则由产品定，服务端拼接”,   //播报内容
 "info":{
 "cateId", "4063"
 "displayType":"6"
 "bidId", "12312321"
 "title", "保洁清洗"
 "serviceType":"家庭保洁",
 "cleanSpace":"50平米以下",      //清洁面积
 "age":"30-35岁",			  //年龄限制
 "needTools":"用户自备洁具",   //是否自备洁具
 "location":"北京-朝阳-大山子", //区域
 "serveTime":"2015-11-11",	//服务时间
 "orderId":"111",//标地Id
 "fee", "300"
 }
 */
public class CleaningPopBean extends PopBaseBean{
    private String orderId;
    private int status;// 抢单结果通知，1代表失败，0代表成功 新订单时不用，结果用
    private int cateId;//":"4065",
    private int displayType;//":"4"
    private long bidId;//":"12312321",
    private String voice;
    private String title;
    private String serviceType;
    private String cleanSpace;
    private String age;
    private String needTools;
    private String location;
    private String serveTime;
    private String fee;
    private String time;
    private String originalFee;

    private String guestName;

    @Override
    public String getOriginalFee() {
        return originalFee;
    }

    public void setOriginalFee(String originalFee) {
        this.originalFee = originalFee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public long getBidId() {
        return bidId;
    }

    public void setBidId(long bidId) {
        this.bidId = bidId;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    @Override
    public View initView(Context context) {
        if(location!=null && location.length()>20){
            location = location.substring(0,19)+"...";
        }
         View view = LayoutInflater.from(context).inflate(R.layout.dialog_pop_cleaning,null);
        ((TextView)view.findViewById(R.id.cleaning_need_content)).setText(serviceType);
        ((TextView)view.findViewById(R.id.cleaning_size)).setText(cleanSpace);
        ((TextView)view.findViewById(R.id.cleaning_age)).setText(age);
        ((TextView)view.findViewById(R.id.cleaning_tools)).setText(needTools);
        ((TextView)view.findViewById(R.id.cleaning_serv_time)).setText(serveTime);
        ((TextView)view.findViewById(R.id.cleaning_location)).setText(location);
        return view;
    }

    @Override
    public int getCateId() {
        return cateId;
    }

    @Override
    public String getVoice() {
        return voice;
    }

    @Override
    public String getFee() {
        return fee;
    }

    @Override
    public PushToStorageBean toPushStorageBean() {
        PushToStorageBean bean = new PushToStorageBean();
        try{
            bean.setOrderid(Long.parseLong(orderId));
        }catch(Exception e){

        }
        bean.setTag(100);//没什么用,外面又赋值了
        bean.setTime(time);
        // 拼接消息字符串
        StringBuilder str = new StringBuilder();
//        if(title.contains("-")){
//            String[] titles = title.split("-");
//            if(titles.length==2){
//                str.append(titles[0]+" ").append(age + " ").append(location+" ").append(serveTime + " ").append(titles[1]+" ");
//            }
//        }else{
//            str.append(title + "").append(age + "").append(location+"").append(serveTime + "");
//        }

        if(location.contains("-")){
            String[] locations = location.split("-");
            if(locations.length == 3){
                str.append(locations[0]+"").append("-").append(locations[1]+"").append("-").append(title + "");
            }else{
                str.append(location+"").append("-").append(title + "");
            }
        }


        //if (status == 1) {
        bean.setFee(fee);
        //}

        bean.setStr(str.toString());
        bean.setStatus(status);
        bean.setGuestName(guestName);
        return bean;
    }

    @Override
    public PushToPassBean toPushPassBean() {
        PushToPassBean bean = new PushToPassBean();
        bean.setBidId(bidId);
        bean.setPushId(pushId);
        bean.setPushTurn(pushTurn);
        bean.setCateId(cateId);
        return bean;
    }
}
