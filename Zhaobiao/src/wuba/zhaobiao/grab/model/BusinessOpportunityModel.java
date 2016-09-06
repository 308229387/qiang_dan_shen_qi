package wuba.zhaobiao.grab.model;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.utils.ToastUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.grab.adapter.BusinessOpportunityAdapter;
import wuba.zhaobiao.grab.fragment.BusinessOpportunityFragment;
import wuba.zhaobiao.respons.BusinessOpportunityRespons;

/**
 * Created by SongYongmeng on 2016/9/5.
 */
public class BusinessOpportunityModel extends BaseModel {
    private BusinessOpportunityFragment context;
    private PullToRefreshLayout refreshView;
    private ListView listView;
    private View view;
    private BusinessOpportunityAdapter adapter;
    private TextView businessCity;
    private TextView businessTime;

    public BusinessOpportunityModel(BusinessOpportunityFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        view =
                inflater.inflate(R.layout.fragment_business_opportunity, container, false);
    }

    public void initView() {
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        listView = (ListView) view.findViewById(R.id.grab_list);
        businessCity = (TextView)view.findViewById(R.id.business_city);
        businessTime = (TextView)view.findViewById(R.id.business_time);
    }

    public void creatAdapter() {
        adapter = new BusinessOpportunityAdapter(context.getActivity());
    }

    public View getView() {
        return view;
    }

    public void setParamsForListView() {
        listView.setAdapter(adapter);
        refreshView.setOnRefreshListener(new Refresh());
        listView.setOnItemClickListener(new ClickItem());
        getData();
    }

    public void getData() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getBids")
                .execute(new Test());
    }

    private class Refresh implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        }
    }

    private class ClickItem implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            adapter.setCheckBoxState(position);
        }
    }

    private class Test extends JsonCallback<BusinessOpportunityRespons> {


        @Override
        public void onResponse(boolean isFromCache, BusinessOpportunityRespons s, Request request, @Nullable Response response) {
            adapter.setData(s.getRespData());
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }
}
