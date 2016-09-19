package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import wuba.zhaobiao.respons.OrderListRespons;

/**
 * Created by 58 on 2016/7/22.
 */
public class OrderListAdapter extends BaseAdapter{

    private Context context;
    private List<OrderListRespons.bean> orderList = new ArrayList<>();


    public OrderListAdapter(Context context, List<OrderListRespons.bean> orderList){
        this.context = context;
        this.orderList = orderList;
    }


    @Override
    public int getCount() {
        return orderList == null ? 0 : orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position){
        orderList.remove(orderList.get(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_list,null);
            holder = new ViewHolder();

            holder.ll_order_item = (LinearLayout) convertView.findViewById(R.id.ll_order_item);
            holder.tv_order_title = (TextView) convertView.findViewById(R.id.tv_order_title);
            holder.tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);
            holder.tv_order_customer_name = (TextView) convertView.findViewById(R.id.tv_order_customer_name);
            holder.tv_order_customer_name_content = (TextView) convertView.findViewById(R.id.tv_order_customer_name_content);
            holder.tv_order_data_title = (TextView) convertView.findViewById(R.id.tv_order_data_title);
            holder.tv_order_data_content = (TextView) convertView.findViewById(R.id.tv_order_data_content);
            holder.ll_order_state = (LinearLayout) convertView.findViewById(R.id.ll_order_state);
            holder.iv_order_state_line = (ImageView) convertView.findViewById(R.id.iv_order_state_line);
            holder.tv_order_state_content = (TextView) convertView.findViewById(R.id.tv_order_state_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderListRespons.bean order = orderList.get(position);
        if (order != null) {
            String title = order.getKey1();
            if(!TextUtils.isEmpty(title)){
                holder.tv_order_title.setText(title);
            }
            String time = order.getKey2();
            if(!TextUtils.isEmpty(time)){
                holder.tv_order_time.setText(TimeUtils.formatDateTime(time));
            }
            String name_title = order.getKey3();
            if(!TextUtils.isEmpty(name_title)){
                holder.tv_order_customer_name.setText(name_title);
            }
            String name_content = order.getKey4();
            if(!TextUtils.isEmpty(name_content)){
                holder.tv_order_customer_name_content.setText(name_content);
            }
            String data_title = order.getKey5();
            if(!TextUtils.isEmpty(data_title)){
                holder.tv_order_data_title.setText(data_title);
            }
           String data_content = order.getKey6();
            if(!TextUtils.isEmpty(data_content)){
                holder.tv_order_data_content.setText(data_content);
            }

            String state =order.getKey7();
            String refundText = order.getKey8();
            //判断订单状态
            if(!TextUtils.isEmpty(state)){ //获取订单状态
                if(TextUtils.equals(state, Constans.DONE_FRAGMENT_FINISH)){ //已完成(成交)
                    holder.iv_order_state_line.setVisibility(View.GONE);
                    holder.tv_order_state_content.setText(R.string.over_done);
                    holder.tv_order_state_content.setTextColor(Color.parseColor("#C5C5C5"));
                    if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                    }else{
                        StringBuilder sb = new StringBuilder();
                        sb.append("已结束(成交)").append("(").append(refundText).append(")");
                        holder.tv_order_state_content.setText(sb.toString());
                        holder.tv_order_state_content.setTextColor(Color.parseColor("#C5C5C5"));
                    }
                }else if( TextUtils.equals(state, Constans.DONE_FRAGMENT_CANCEL)){ //已完成(未成交)
                    holder.iv_order_state_line.setVisibility(View.GONE);
                    holder.tv_order_state_content.setText(R.string.over_undone);
                    holder.tv_order_state_content.setTextColor(Color.parseColor("#C5C5C5"));
                    if(TextUtils.isEmpty(refundText) ||TextUtils.equals(refundText,"未退单")){

                    }else{
                        StringBuilder sb = new StringBuilder();
                        sb.append("已结束(未成交)").append("(").append(refundText).append(")");
                        holder.tv_order_state_content.setText(sb.toString());
                        holder.tv_order_state_content.setTextColor(Color.parseColor("#C5C5C5"));
                    }
                }else if(TextUtils.equals(state, Constans.READY_SERVICE)){
                    holder.iv_order_state_line.setVisibility(View.VISIBLE);
                    holder.iv_order_state_line.setImageResource(R.drawable.onservice_order_state);
                    holder.tv_order_state_content.setText(R.string.unservice);
                    holder.tv_order_state_content.setTextColor(Color.parseColor("#4EC5BF"));
                    if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                    }else{
                        StringBuilder sb = new StringBuilder();
                        sb.append("待服务").append("(").append(refundText).append(")");
                        holder.tv_order_state_content.setText(sb.toString());
                        holder.tv_order_state_content.setTextColor(Color.parseColor("#4EC5BF"));
                    }
                }else if(TextUtils.equals(state, Constans.ON_SERVICE)){
                    holder.iv_order_state_line.setVisibility(View.VISIBLE);
                    holder.iv_order_state_line.setImageResource(R.drawable.servicing_order_state);
                    holder.tv_order_state_content.setText(R.string.servicing);
                    holder.tv_order_state_content.setTextColor(Color.parseColor("#4EC5BF"));
                    if(TextUtils.isEmpty(refundText) || TextUtils.equals(refundText,"未退单")){

                    }else{
                        StringBuilder sb = new StringBuilder();
                        sb.append("服务中").append("(").append(refundText).append(")");
                        holder.tv_order_state_content.setText(sb.toString());
                        holder.tv_order_state_content.setTextColor(Color.parseColor("#4EC5BF"));
                    }
                }
            }

            //判断商机状态
            if(!TextUtils.isEmpty(state)){
                if(TextUtils.equals(state, Constans.BUSINESS_NOT_CALSSFY)){ //未分类
                    holder.iv_order_state_line.setVisibility(View.VISIBLE);
                    holder.iv_order_state_line.setImageResource(R.drawable.onservice_order_state);
                    holder.tv_order_state_content.setText("未分类");
                    holder.tv_order_state_content.setTextColor(Color.parseColor("#4EC5BF"));

                }else if( TextUtils.equals(state,Constans.BUSINESS_WAIT_FOLLOW)){ //待跟进
                    holder.iv_order_state_line.setVisibility(View.VISIBLE);
                    holder.iv_order_state_line.setImageResource(R.drawable.servicing_order_state);
                    holder.tv_order_state_content.setText("待跟进");
                    holder.tv_order_state_content.setTextColor(Color.parseColor("#4EC5BF"));
                }else if(TextUtils.equals(state, Constans.BUSINESS_ALREADY_FINISH)){
                    holder.ll_order_state.setBackgroundColor(Color.parseColor("#4EC5BF"));
                    holder.iv_order_state_line.setVisibility(View.GONE);
                    holder.tv_order_state_content.setText("已完结");
                    holder.tv_order_state_content.setTextColor(Color.parseColor("#C5C5C5"));
                }
            }


        }

        return convertView;
    }

    class ViewHolder{
        LinearLayout ll_order_item;
        TextView tv_order_title; //类型
        TextView tv_order_time; //时间
        TextView tv_order_customer_name;
        TextView tv_order_customer_name_content ;
        TextView tv_order_data_title;
        TextView tv_order_data_content;
        LinearLayout ll_order_state;
        ImageView iv_order_state_line; //状态条
        TextView tv_order_state_content; //状态
    }


}
