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
            holder.title = (TextView) convertView.findViewById(R.id.business_key1);
            holder.timeKey = (TextView) convertView.findViewById(R.id.business_key2);
            holder.timeValue = (TextView) convertView.findViewById(R.id.business_key3);
            holder.remarksKey = (TextView) convertView.findViewById(R.id.business_key4);
            holder.remarksValue = (TextView) convertView.findViewById(R.id.business_key5);
            holder.priceKey = (TextView) convertView.findViewById(R.id.business_key6);
            holder.priceValue = (TextView) convertView.findViewById(R.id.business_key7);
            holder.box = (CheckBox) convertView.findViewById(R.id.check_business);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.size() > 0) {
            holder.title.setText(((BusinessData) data.get(position)).getKey1());
            holder.timeKey.setText(((BusinessData) data.get(position)).getKey2());
            holder.timeValue.setText(((BusinessData) data.get(position)).getKey3());
            holder.remarksKey.setText(((BusinessData) data.get(position)).getKey4());
            holder.remarksValue.setText(((BusinessData) data.get(position)).getKey5());
            holder.priceKey.setText(((BusinessData) data.get(position)).getKey6());
            holder.priceValue.setText(((BusinessData) data.get(position)).getKey7());
            if (((BusinessData) data.get(position)).getKey())
                holder.box.setChecked(true);
            else
                holder.box.setChecked(false);
        }
        return convertView;
    }

    public void setData(ArrayList<BusinessData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    private static class ViewHolder {

        private TextView title;
        private TextView timeKey;
        private TextView timeValue;
        private TextView remarksValue;
        private TextView priceKey;
        private TextView priceValue;        private TextView remarksKey;
        private CheckBox box;
    }

}
