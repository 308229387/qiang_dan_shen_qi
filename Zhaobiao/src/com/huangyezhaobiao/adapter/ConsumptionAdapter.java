package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.ConsumeItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/12.
 */
public class ConsumptionAdapter extends BaseAdapter{
    private List<ConsumeItemBean> consumeItemBeans = new ArrayList<ConsumeItemBean>();
    private Context               context;
    public ConsumptionAdapter(Context context,List<ConsumeItemBean> consumeItemBeans){
        this.consumeItemBeans.clear();
        this.consumeItemBeans.addAll(consumeItemBeans);
        this.context = context;
    }

    public void refreshDatas(List<ConsumeItemBean> consumeItemBeans){
        this.consumeItemBeans.clear();
        this.consumeItemBeans.addAll(consumeItemBeans);
    }

    public void notifyDatas(List<ConsumeItemBean> consumeItemBeans){
        this.consumeItemBeans.clear();
        this.consumeItemBeans.addAll(consumeItemBeans);
        Log.e("shenzhixinUUUU","size:"+this.consumeItemBeans.size());
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.consumeItemBeans==null?0:consumeItemBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return consumeItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_consume,parent,false);
            holder.tv_consume_time  = (TextView) convertView.findViewById(R.id.tv_consume_time);
            holder.tv_consume_title = (TextView) convertView.findViewById(R.id.tv_consume_title);
            holder.cost_record      = (TextView) convertView.findViewById(R.id.cost_record);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ConsumeItemBean itemBean = consumeItemBeans.get(position);
        holder.tv_consume_title.setText(itemBean.getTitle());
        holder.tv_consume_time.setText(itemBean.getCostTime());
        holder.cost_record.setText(itemBean.getCost());
        return convertView;
    }

    class ViewHolder{
        public TextView tv_consume_title;
        public TextView tv_consume_time;
        public TextView cost_record;
    }
}
