package wuba.zhaobiao.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangyezhaobiao.R;

import java.util.List;

/**
 * Created by SongYongmeng on 2016/9/9.
 * 描    述：商机地址与时间排序用到的POPWINDOW
 */
public class SpinerPopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    private List<T> list;
    private MyAdapter mAdapter;
    private View viewDismiss;
    private int itemHigh;

    public SpinerPopWindow(Context context, List<T> list, OnItemClickListener clickListener, View.OnClickListener dismissClick) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.list = list;
        init(clickListener, dismissClick);
    }

    private void init(OnItemClickListener clickListener, View.OnClickListener dismissClick) {
        View view = inflater.inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.listview);
        viewDismiss = (View) view.findViewById(R.id.back_dissmiss);
        mListView.setAdapter(mAdapter = new MyAdapter());
        mListView.setOnItemClickListener(clickListener);
        viewDismiss.setOnClickListener(dismissClick);
    }

    public void itemHigh() {
        View listItem = mAdapter.getView(0, null, mListView);
        listItem.measure(0, 0); // 计算子项View 的宽高
        itemHigh = listItem.getMeasuredHeight() + mListView.getDividerHeight();
    }

    public void setHighForListView() {
        LinearLayout.LayoutParams temp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, listHigh());
        mListView.setLayoutParams(temp);
    }

    public int listHigh(){
        if(mAdapter.getCount()>=7)
            return itemHigh*7+2;
        else
            return mAdapter.getCount()*itemHigh+2;
    }

    public void setHighForTime() {
        LinearLayout.LayoutParams temp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mListView.setLayoutParams(temp);
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.spiner_item_layout, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(getItem(position).toString());
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView tvName;
    }
}
