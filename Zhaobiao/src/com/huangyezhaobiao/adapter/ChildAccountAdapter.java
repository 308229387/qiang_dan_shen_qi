package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.AddAccountActivity;
import com.huangyezhaobiao.activity.UpdateAccountActivity;
import com.huangyezhaobiao.bean.ChildAccountBean;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 58 on 2016/7/22.
 */
public class ChildAccountAdapter extends BaseAdapter{

    private Context context;
    private List<ChildAccountBean> childAccountList = new ArrayList<>();

    public boolean flag = false; // false表示右上角显示"编辑"，true表示显示"完成"

    private ZhaoBiaoDialog deleteDialog;

    public ChildAccountAdapter(Context context, List<ChildAccountBean> childAccountList){
        this.context = context;
        this.childAccountList = childAccountList;
    }

    public ChildAccountAdapter(Context context,List<ChildAccountBean> childAccountList,boolean flag){
        this.context = context;
        this.childAccountList = childAccountList;
        this.flag = flag;
        initDeleteDialog();
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
                    deleteDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
                        @Override
                        public void onDialogOkClick() {
                            deleteDialog.dismiss();
                            remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onDialogCancelClick() {
                            deleteDialog.dismiss();
                        }
                    });
                    deleteDialog.show();
                }
            });
            holder.account_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put(Constans.CHILD_ACCOUNT_NAME, account.getAccountName());
                    map.put(Constans.CHILD_ACCOUNT_PHONE, account.getAccountPhone());
                    ActivityUtils.goToActivityWithString(context, UpdateAccountActivity.class, map);
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

    protected void initDeleteDialog(){
        deleteDialog= new ZhaoBiaoDialog(context,"确认删除选中权限?");

        deleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                deleteDialog = null;
            }
        });
    }

}
