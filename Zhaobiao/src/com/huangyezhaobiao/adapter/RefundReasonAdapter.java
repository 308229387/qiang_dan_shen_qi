package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.tt.RefundFirstReasonEntity;
import com.huangyezhaobiao.mediator.RefundMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/9.
 * 退单原因的适配器
 * 有一个checkedId
 */
public class RefundReasonAdapter extends BaseAdapter {
    private Context context;
    private List<RefundFirstReasonEntity> reasonEntities = new ArrayList<RefundFirstReasonEntity>();
    public RefundReasonAdapter(Context context,List<RefundFirstReasonEntity> reasonEntities){
        this.context = context;
        this.reasonEntities.clear();
        this.reasonEntities.addAll(reasonEntities);
    }

    public void refreshDataSources(List<RefundFirstReasonEntity> reasonEntities){
        this.reasonEntities.clear();
        this.reasonEntities.addAll(reasonEntities);

    }
    @Override
    public int getCount() {
        return this.reasonEntities==null?0:this.reasonEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return this.reasonEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final RefundFirstReasonEntity entity = reasonEntities.get(position);
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(getLayoutId(),parent,false);
            holder.item_reason_widget = (Button) convertView.findViewById(R.id.item_reason_widget);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_reason_widget.setText(entity.getReasonName());
        if(RefundMediator.checkedId.contains(entity.getReasonId())){
            holder.item_reason_widget.setBackgroundResource(R.drawable.drawable_reason_entity_check);
            holder.item_reason_widget.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.item_reason_widget.setBackgroundResource(R.drawable.drawable_reason_entity_uncheck);
            holder.item_reason_widget.setTextColor(context.getResources().getColor(R.color.blacks));
        }
        holder.item_reason_widget.setText(entity.getReasonName());
        holder.item_reason_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = entity.getReasonId();
                if(RefundMediator.checkedId.contains(id)){//之前选中,变成不选中
                    RefundMediator.checkedId.remove(id);
                }else{//之前没选中，变成选中
                    RefundMediator.checkedId.clear();
                    RefundMediator.checkedId.add(id);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder{
        public Button item_reason_widget;
    }

    private int getLayoutId(){
        return R.layout.item_reason_refund;
    }
}
