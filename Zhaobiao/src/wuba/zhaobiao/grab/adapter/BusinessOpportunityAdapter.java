package wuba.zhaobiao.grab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.huangyezhaobiao.R;

import java.util.ArrayList;

import wuba.zhaobiao.bean.BusinessData;

/**
 * Created by SongYongmeng on 2016/9/5.
 */
public class BusinessOpportunityAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList data = new ArrayList();

    public BusinessOpportunityAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return data.size();
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.business_opportunity_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.grab_cleaning_title);
            holder.time = (TextView) convertView.findViewById(R.id.grab_cleaning_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (data.size() > 0) {
//
//        }
        return convertView;
    }

    public void setData(ArrayList<BusinessData> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    private static class ViewHolder {

        private TextView title;

        private TextView price;

        private TextView time;

        private TextView remarks;

        private CheckBox box;
    }

}
