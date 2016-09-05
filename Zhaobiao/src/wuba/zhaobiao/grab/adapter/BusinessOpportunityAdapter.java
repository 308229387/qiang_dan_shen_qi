package wuba.zhaobiao.grab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * Created by SongYongmeng on 2016/9/5.
 */
public class BusinessOpportunityAdapter extends BaseAdapter {
    private Context mContext;

    public BusinessOpportunityAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 5;
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (data.size() > 0) {
//
//        }
        return convertView;
    }


    private static class ViewHolder {

        private TextView mTitle;

        private TextView price;

        private TextView time;

        private TextView title;

        private CheckBox box;
    }
}
