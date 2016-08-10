package wuba.zhaobiao.grab.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 抢单列表Adapter
 *
 * @author linyueyang
 */
public class GrabAdapter extends GrabBaseAdapter<GrabBaseBean> {

    public GrabAdapter(Context context, AdapterListener listener) {
        super(context, listener);
    }

    @Override
    public void transferBeanTypeToAdapterType() {
        typeMap.put("1", 0);
        typeMap.put("2", 1);
        typeMap.put("3", 2);
        //2015.8.21 add
        typeMap.put("4", 3);
        //2015.12.4 add
        typeMap.put("5", 4);
        //2015.12.5 add
        typeMap.put("6", 5);
    }

    @Override
    public int getTotalTypeCount() {
        return typeMap.size();
    }

    @Override
    public View initView(GrabBaseBean t, View view, ViewGroup parent, LayoutInflater inflater, Context context) {

        return t.initView(view, inflater, parent, context, this);
    }

    @Override
    public void converseView(GrabBaseBean t, View view, Context context) {
        t.converseView(view, context, this);

    }

    @Override
    public void fillDatas(GrabBaseBean t) {
        t.fillDatas();
    }

    @Override
    public GrabBaseBean initJavaBean(int position) {

        return beans.get(position);
    }

    @Override
    protected int getAdapterItemType(int position) {

        return typeMap.get("" + beans.get(position).getDisplayType());
    }

}
