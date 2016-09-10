package com.huangyezhaobiao.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BusinessDetailsActivity;
import com.huangyezhaobiao.bean.mydetail.OrderDetail.CallClassifyEntity;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.view.CallClassifyDialog;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by 58 on 2016/6/29.
 */
public class CallClassifyAdapter extends BaseAdapter{

    private Context context;
    private List<CallClassifyEntity> callClassifyList = new ArrayList<>();

    public CallClassifyAdapter(Context context, List<CallClassifyEntity> callClassifyList){
        this.context = context;
        this.callClassifyList.clear();
        this.callClassifyList.addAll(callClassifyList);
    }

//    public OrderStateAdapter(List<OrderStateEntity> stateList){
//        this.orderStateList.clear();
//        this.orderStateList.addAll(stateList);
//
//    }

    @Override
    public int getCount() {
        return this.callClassifyList==null?0:this.callClassifyList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.callClassifyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        final CallClassifyEntity entity = callClassifyList.get(position);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.call_classify_selector,parent,false);
            holder.iv_call_image = (ImageView) convertView.findViewById(R.id.iv_call_image);
            holder.tv_call_title = (TextView) convertView.findViewById(R.id.tv_call_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_call_image.setImageResource(entity.getCallClassifyImage());
        holder.tv_call_title.setText(entity.getCallClassifyName());

        String s = entity.getCallClassifyId();
        if(BusinessDetailsActivity.callCheckedId.contains(s)){
                if(TextUtils.equals("11",s) ||TextUtils.equals("21",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_not_connected_pressed);
                }else if(TextUtils.equals("20",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_can_follow_up_pressed);
                }else if(TextUtils.equals("90",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_ad_and_other_pressed);
                }else if(TextUtils.equals("1",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_already_traded_pressed);
                }else if(TextUtils.equals("2",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_not_traded_pressed);
                }
        }else{
                if(TextUtils.equals("11",s) || TextUtils.equals("21",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_not_connected);
                }else if(TextUtils.equals("20",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_can_follow_up);
                }else if(TextUtils.equals("90",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_ad_and_other);
                }else if(TextUtils.equals("1",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_already_traded);
                }else if(TextUtils.equals("2",s)){
                    holder.iv_call_image.setImageResource(R.drawable.detail_not_traded);
                }
        }
        holder.iv_call_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = entity.getCallClassifyId();

                if (BusinessDetailsActivity.callCheckedId.contains(id)) {//之前选中,变成不选中

                } else {//之前没选中，变成选中
                    BusinessDetailsActivity.callCheckedId.clear();
                    BusinessDetailsActivity.callCheckedId.add(id);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    class ViewHolder{
        public ImageView iv_call_image;
        public TextView tv_call_title;
    }
}
