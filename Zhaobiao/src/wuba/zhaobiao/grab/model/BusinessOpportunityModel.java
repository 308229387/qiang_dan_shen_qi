package wuba.zhaobiao.grab.model;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.callback.DialogCallback;
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
    private ArrayList<BusinessData> showData = new ArrayList<BusinessData>();


    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> cityNameList = new ArrayList<>();
    private List<String> cityIdList = new ArrayList<>();
    private List<String> areaIdList = new ArrayList<>();
    private List<String> timeList;
    private String cityId = "";
    private String areaId = "";
    private String timestate = "";
    private int pageNum = 1;

    public BusinessOpportunityModel(BusinessOpportunityFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        view =
                inflater.inflate(R.layout.fragment_business_opportunity, container, false);
    }

    public void initView() {
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        refreshView.canNotRefresh();
        listView = (ListView) view.findViewById(R.id.grab_list);
        businessCity = (TextView) view.findViewById(R.id.business_city);
        businessTime = (TextView) view.findViewById(R.id.business_time);
        initTimeData();
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
    }

    public void getData() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getBids")
                .params("cityId", cityId)
                .params("areaId", areaId)
                .params("timestate", timestate)
                .params("pageNum", pageNum + "")
                .params("pageSize", 4 + "")
                .execute(new BusinessRequest());
    }

    private void getDataForRefresh() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getBids")
                .params("cityId", cityId)
                .params("areaId", areaId)
                .params("timestate", timestate)
                .params("pageNum", pageNum + "")
                .params("pageSize", 4 + "")
                .execute(new BusinessRefreshRequest(context.getActivity()));
    }

    public void getCityData() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getLocal")
                .execute(new BusinessCityRequest());
    }

    public void dealWithData(int position) {
        if (showData.get(position).getKey()) {
            showData.get(position).setKey(false);
            buyData.remove(showData.get(position));
        } else {
            showData.get(position).setKey(true);
            buyData.add(showData.get(position));
        }
    }

    private void initTimeData() {
        timeList = new ArrayList<String>();
        timeList.add("按时间正序");
        timeList.add("按时间倒序");
        timestate = 0 + "";
    }

    private void showCityPop() {
        mSpinerPopWindow = new SpinerPopWindow<String>(context.getActivity(), cityNameList, cityItemClickListener);
        mSpinerPopWindow.setWidth(300);
        mSpinerPopWindow.showAsDropDown(businessCity, -70, 42);
    }

    private void showTimePop() {
        mSpinerPopWindow = new SpinerPopWindow<String>(context.getActivity(), timeList, timeItemClickListener);
        mSpinerPopWindow.setWidth(335);
        mSpinerPopWindow.showAsDropDown(businessTime, -70, 42);
    }

    private void ifShowCity(BusinessCityRespons s) {
        for (int i = 0; i < s.getData().size(); i++) {
            cityNameList.add(s.getData().get(i).getCityName());
            cityIdList.add(s.getData().get(i).getCityId());
        }
    }

    private void isShowArea(BusinessCityRespons s) {
        cityNameList.add(s.getData().get(0).getCityName());
        cityIdList.add(s.getData().get(0).getCityId());
        ArrayList<BusinessCityRespons.Areas> temp = s.getData().get(0).getAreas();
        for (int i = 0; i < temp.size(); i++) {
            cityNameList.add(temp.get(i).getAreaName());
            areaIdList.add(temp.get(i).getAreaId());
        }
    }

    private void setCityOrAreaID() {
        if (cityIdList.size() > 0)
            cityId = cityIdList.get(0);
        if (areaIdList.size() > 0)
            areaId = areaIdList.get(0);
        else
            areaId = "";
    }

    private void stateRight(ArrayList<BusinessData> list) {
        clearLocalInfo();
        dealWithListData(list);
    }

    private void clearLocalInfo() {
        if (pageNum == 1) {
            showData.clear();
            listView.setAdapter(adapter);
        }
    }

    private void dealWithListData(ArrayList<BusinessData> list) {
        noData(list);
        hasData(list);
        hasDataButNotFull(list);
    }

    private void noData(ArrayList<BusinessData> list) {
        if (list.size() == 0) {
            if (showData.size() == 0) {
                showEmptyView();
            } else {
                banPullUp();
            }
        }
    }

    private void showEmptyView() {

    }

    private void banPullUp() {
        refreshView.setBanPullUp(true);
    }

    private void canPullUp() {
        refreshView.setBanPullUp(false);
    }

    private void hasData(ArrayList<BusinessData> list) {
        if (list.size() > 0)
            showDataToList(list);
    }

    private void showDataToList(ArrayList<BusinessData> list) {
        showData.addAll(list);
        adapter.setData(showData);
        pageNum++;
    }

    private void hasDataButNotFull(ArrayList<BusinessData> list) {

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
            if (areaIdList.size() > 0)
                areaId = (areaIdList.get(position));
            else
                cityId = (cityIdList.get(position));
            businessCity.setText(cityNameList.get(position));
            ToastUtils.showToast(cityNameList.get(position));
            getData();
        }
    };

    private AdapterView.OnItemClickListener timeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            ToastUtils.showToast(timeList.get(position));
            timestate = position + "";
            getData();
        }
    };

    public void refresh() {
        pageNum = 1;
        canPullUp();
        getDataForRefresh();
    }


    private class Refresh implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            getData();
        }
    }

    private class ClickItem implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dealWithData(position);
            adapter.setData(showData);
        }
    }

    private class BusinessRequest extends JsonCallback<BusinessOpportunityRespons> {

        @Override
        public void onResponse(boolean isFromCache, BusinessOpportunityRespons s, Request request, @Nullable Response response) {
            data = s.getRespData();
            stateRight(data);
            refreshView.refreshComplete();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }

    private class BusinessRefreshRequest extends DialogCallback<BusinessOpportunityRespons> {

        public BusinessRefreshRequest(Activity context) {
            super(context, true);
        }

        @Override
        public void onResponse(boolean isFromCache, BusinessOpportunityRespons s, Request request, @Nullable Response response) {
            data = s.getRespData();
            stateRight(data);
            refreshView.refreshComplete();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }

    private class BusinessCityRequest extends JsonCallback<BusinessCityRespons> {
        @Override
        public void onResponse(boolean isFromCache, BusinessCityRespons s, Request request, @Nullable Response response) {
            if (s.getData().size() > 1)
                ifShowCity(s);
            else {
                isShowArea(s);
            }
            setCityOrAreaID();

            getData();
        }
    }


}
