package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.SysListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/15.
 */
public class SystemNotiAdapter extends BaseAdapter{
    private List<SysListBean> listBeans = new ArrayList<>();
    private Context context;
    public SystemNotiAdapter(List<SysListBean> listBeans,Context context){
        this.listBeans = listBeans;
        this.context   = context;
    }


    public List<SysListBean> getDataSources(){
        return listBeans;
    }

    public void refreshDatas(List<SysListBean> listBeans){
        Log.e("shenzhixin","before notifyDataSet"+listBeans.size());
        //this.listBeans.clear();
        this.listBeans = listBeans;
        Log.e("shenzhixin","before notifyDataSet"+listBeans.size());
        Log.e("shenzhixin","notifyDataSet"+this.listBeans.size());
    }

    @Override
    public int getCount() {
        return listBeans==null?0:listBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView           = LayoutInflater.from(context).inflate(getItemLayoutId(),parent,false);
            holder.iv_hint        = (ImageView) convertView.findViewById(R.id.iv_hint);
            holder.tv_sys_content = (TextView) convertView.findViewById(R.id.tv_sys_content);
            holder.tv_sys_time    = (TextView) convertView.findViewById(R.id.tv_sys_time);
            holder.tv_sys_title   = (TextView) convertView.findViewById(R.id.tv_sys_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        SysListBean bean = listBeans.get(position);

        holder.tv_sys_content.setText(bean.getTitle());
        holder.tv_sys_time.setText(bean.getTime());
        switch (bean.getSysType()){
            case "2":
                holder.iv_hint.setImageResource(R.drawable.sys_notification);
                holder.tv_sys_title.setText("系统通知");
                break;
            case "1":
                holder.iv_hint.setImageResource(R.drawable.sys_activity);
                holder.tv_sys_title.setText("活动通知");
                break;
        }
        return convertView;
    }


    private int getItemLayoutId(){
        return R.layout.item_sys_noti;
    }

    class ViewHolder{
        public ImageView iv_hint;
        public TextView  tv_sys_title;
        public TextView  tv_sys_content;
        public TextView  tv_sys_time;
    }
}
