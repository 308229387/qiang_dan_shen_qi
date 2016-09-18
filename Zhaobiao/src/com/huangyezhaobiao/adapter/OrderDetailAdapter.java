package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangyezhaobiao.R;

import java.util.ArrayList;
import java.util.List;

import wuba.zhaobiao.respons.OrderDetailRespons;

/**
 * Created by 58 on 2016/6/29.
 */
public class OrderDetailAdapter extends BaseAdapter{

    private Context context;
    private List<OrderDetailRespons.bean> list = new ArrayList<>();

    public OrderDetailAdapter(Context context, List<OrderDetailRespons.bean> list){
        this.context = context;
        this.list.clear();
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return this.list==null?0:this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        final OrderDetailRespons.bean bean = list.get(position);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.order_detail_selector,parent,false);
            holder.tv_order_detail_title = (TextView) convertView.findViewById(R.id.tv_order_detail_title);
            holder.tv_order_detail_content = (TextView) convertView.findViewById(R.id.tv_order_detail_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_order_detail_title.setText(bean.getTitle());
        holder.tv_order_detail_content.setText(bean.getContent());
        return convertView;
    }

    class ViewHolder{
        public TextView tv_order_detail_title, tv_order_detail_content;
    }


}
