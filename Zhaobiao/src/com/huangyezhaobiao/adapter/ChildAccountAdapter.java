package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.UpdateAccountActivity;
import com.huangyezhaobiao.bean.ChildAccountBean;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 58 on 2016/7/22.
 */
public class ChildAccountAdapter extends BaseAdapter{

    private Context context;
    private List<ChildAccountBean.data.bean> childAccountList = new ArrayList<>();

    public boolean flag = false; // false表示右上角显示"编辑"，true表示显示"完成"

    private ZhaoBiaoDialog deleteDialog;

    public ChildAccountAdapter(Context context, List<ChildAccountBean.data.bean> childAccountList){
        this.context = context;
        this.childAccountList = childAccountList;
    }

    public ChildAccountAdapter(Context context,List<ChildAccountBean.data.bean> childAccountList,boolean flag){
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

        final ChildAccountBean.data.bean account = childAccountList.get(position);
        holder.account_name.setText(account.getUsername());
        holder.account_phone.setText(account.getPhone());

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
                            adeleteChildAccount(account.getId());
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
                    map.put(Constans.CHILD_ACCOUNT_ID,account.getId());
                    map.put(Constans.CHILD_ACCOUNT_NAME, account.getUsername());
                    map.put(Constans.CHILD_ACCOUNT_PHONE, account.getPhone());
                    map.put(Constans.CHILD_ACCOUNT_AUTHORITY,account.getRbac());
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
        deleteDialog= new ZhaoBiaoDialog(context,"确认删除选中子账号?");

        deleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                deleteDialog = null;
            }
        });
    }

    //请求实体
    private void adeleteChildAccount(String id) {
        OkHttpUtils.get("http://zhaobiao.58.com/api/suserdelete")//
                .params("id", id)//
                .execute(new callback());
    }
    //响应类
    private class callback extends JsonCallback<String> {

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount", "add_success");
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {

            ToastUtils.showToast(e.getMessage());
        }

    }

}
