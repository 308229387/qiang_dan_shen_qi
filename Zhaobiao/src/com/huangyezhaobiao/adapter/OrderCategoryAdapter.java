package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.mylist.Order.OrderStateEntity;

import java.util.ArrayList;
import java.util.List;

import wuba.zhaobiao.order.model.OrderModel;

/**
 * Created by 58 on 2016/6/29.
 */
public class OrderCategoryAdapter extends BaseAdapter{

    private Context context;
    private List<OrderStateEntity> orderStateList = new ArrayList<>();

    public OrderCategoryAdapter(Context context, List<OrderStateEntity> stateList){
        this.context = context;
        this.orderStateList.clear();
        this.orderStateList.addAll(stateList);
    }

//    public OrderStateAdapter(List<OrderStateEntity> stateList){
//        this.orderStateList.clear();
//        this.orderStateList.addAll(stateList);
//
//    }

    @Override
    public int getCount() {
        return this.orderStateList==null?0:this.orderStateList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.orderStateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        final OrderStateEntity entity = orderStateList.get(position);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.order_state_selector,parent,false);
            holder.tv_orderState_popup = (TextView) convertView.findViewById(R.id.tv_orderState_popup);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_orderState_popup.setText(entity.getOrderState());

        if(OrderModel.CategoryCheckedId.contains(entity.getOrderStateId())){
            holder.tv_orderState_popup.setBackgroundResource(R.drawable.order_state_checked
            );
            holder.tv_orderState_popup.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.tv_orderState_popup.setBackgroundResource(R.drawable.order_state_check);
            holder.tv_orderState_popup.setTextColor(context.getResources().getColor(R.color.refund_reason));
        }
//        holder.tv_orderState_popup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String id = entity.getOrderStateId();
//                if (OrderModel.CategoryCheckedId.contains(id)) {//之前选中,变成不选中
//                    OrderModel.CategoryCheckedId.remove(id);
//                } else {//之前没选中，变成选中
//
////                    if(TextUtils.equals(id,"1")){
////                        isBidding = true;
////                    }else if(TextUtils.equals(id,"2")){
////                        isBidding = false;
////                    }
//                    OrderModel.CategoryCheckedId.clear();
//                    OrderModel.CategoryCheckedId.add(id);
//                }
//                notifyDataSetChanged();
//            }
//        });
        return convertView;
    }

    class ViewHolder{
        public TextView tv_orderState_popup;
    }
}
