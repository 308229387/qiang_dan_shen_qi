package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.ChildAccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2016/7/22.
 */
public class ChildAccountAdapter extends BaseAdapter{

    private Context context;
    private List<ChildAccountBean> childAccountList = new ArrayList<>();

    public boolean flag = false; // false表示右上角显示"编辑"，true表示显示"完成"

    public ChildAccountAdapter(Context context, List<ChildAccountBean> childAccountList){
        this.context = context;
        this.childAccountList = childAccountList;
    }

    public ChildAccountAdapter(Context context,List<ChildAccountBean> childAccountList,boolean flag){
        this.context = context;
        this.childAccountList = childAccountList;
        this.flag = flag;
    }
    @Override
    public int getCount() {
        return childAccountList == null ? 0 : childAccountList.size();
    }

    @Override
    public Object getItem(int position) {
        return childAccountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position){
        childAccountList.remove(childAccountList.get(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.child_account_item,null);
            holder = new ViewHolder();

            holder.account_delete = (ImageView) convertView.findViewById(R.id.account_delete);
            holder.account_name = (TextView) convertView.findViewById(R.id.account_name);
            holder.account_phone = (TextView) convertView.findViewById(R.id.account_phone);
            holder.account_edit = (ImageView) convertView.findViewById(R.id.account_edit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final ChildAccountBean account = childAccountList.get(position);
        holder.account_name.setText(account.getAccountName());
        holder.account_phone.setText(account.getAccountPhone());

        if(flag){
            holder.account_delete.setVisibility(View.VISIBLE);
            holder.account_edit.setVisibility(View.VISIBLE);

            holder.account_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.account_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else{
            holder.account_delete.setVisibility(View.GONE);
            holder.account_edit.setVisibility(View.GONE);

        }
        return convertView;
    }

    class ViewHolder{
        ImageView account_delete;  //删除按钮
        TextView account_name; //名称
        TextView account_phone; //电话
        ImageView account_edit; //修改
    }
}
