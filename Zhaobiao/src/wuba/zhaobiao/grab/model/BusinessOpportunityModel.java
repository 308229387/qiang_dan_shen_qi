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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.bean.BusinessData;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.grab.adapter.BusinessOpportunityAdapter;
import wuba.zhaobiao.grab.fragment.BusinessOpportunityFragment;
import wuba.zhaobiao.respons.BusinessCityRespons;
import wuba.zhaobiao.respons.BusinessOpportunityRespons;
import wuba.zhaobiao.utils.SpinerPopWindow;

/**
 * Created by SongYongmeng on 2016/9/5.
 */
public class BusinessOpportunityModel extends BaseModel implements View.OnClickListener {
    private BusinessOpportunityFragment context;
    private PullToRefreshLayout refreshView;
    private ListView listView;
    private View view;
    private BusinessOpportunityAdapter adapter;
    private TextView businessCity;
    private TextView businessTime;
    private ArrayList<BusinessData> data;
    private ArrayList<BusinessData> buyData = new ArrayList<>();

    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> cityList = new ArrayList<>();
    private List<String> timeList;

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
        businessCity = (TextView) view.findViewById(R.id.business_city);
        businessTime = (TextView) view.findViewById(R.id.business_time);
    }

    public void setListener() {
        businessCity.setOnClickListener(this);
        businessTime.setOnClickListener(this);
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
                .execute(new BusinessRequest());
    }

    public void getCityData() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getLocal")
                .execute(new BusinessCityRequest());
    }

    public void dealWithData(int position) {
        if (data.get(position).getKey()) {
            data.get(position).setKey(false);
            buyData.remove(data.get(position));
        } else {
            data.get(position).setKey(true);
            buyData.add(data.get(position));
        }
    }

    private void initTimeData() {
        timeList = new ArrayList<String>();
        timeList.add("按时间正序");
        timeList.add("按时间倒序");
    }

    private void showCityPop() {
        mSpinerPopWindow = new SpinerPopWindow<String>(context.getActivity(), cityList, cityItemClickListener);
        mSpinerPopWindow.setWidth(300);
        mSpinerPopWindow.showAsDropDown(businessCity, -70, 42);
    }

    private void showTimePop() {
        initTimeData();
        mSpinerPopWindow = new SpinerPopWindow<String>(context.getActivity(), timeList, timeItemClickListener);
        mSpinerPopWindow.setWidth(335);
        mSpinerPopWindow.showAsDropDown(businessTime, -70, 42);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_city:
                showCityPop();
                break;
            case R.id.business_time:
                showTimePop();
                break;
            default:
                break;
        }
    }

    private AdapterView.OnItemClickListener cityItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            businessCity.setText(cityList.get(position));
            ToastUtils.showToast(cityList.get(position));
        }
    };

    private AdapterView.OnItemClickListener timeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            ToastUtils.showToast(timeList.get(position));
        }
    };

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
            dealWithData(position);
            adapter.setData(data);
        }
    }

    private class BusinessRequest extends JsonCallback<BusinessOpportunityRespons> {

        @Override
        public void onResponse(boolean isFromCache, BusinessOpportunityRespons s, Request request, @Nullable Response response) {
            data = s.getRespData();
            adapter.setData(data);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }

    private class BusinessCityRequest extends JsonCallback<BusinessCityRespons> {
        @Override
        public void onResponse(boolean isFromCache, BusinessCityRespons s, Request request, @Nullable Response response) {
            ToastUtils.showToast(((BusinessCityRespons) s).getData().get(0).getCityId());
            if (s.getData().size() > 1)
                for (int i = 0; i < s.getData().size(); i++)
                    cityList.add(((BusinessCityRespons) s).getData().get(i).getCityName());
            else {
                ArrayList<BusinessCityRespons.Areas> temp = ((BusinessCityRespons) s).getData().get(0).getAreas();
                for (int i = 0; i < temp.size(); i++)
                    cityList.add(temp.get(i).getAreaName());
            }

        }
    }
}
