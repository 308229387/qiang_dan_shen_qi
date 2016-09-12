package wuba.zhaobiao.grab.model;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import wuba.zhaobiao.utils.BusinessFullDialogUtils;
import wuba.zhaobiao.utils.BusinessRefreshDialogUtils;
import wuba.zhaobiao.utils.SpinerPopWindow;

/**
 * Created by SongYongmeng on 2016/9/5.
 */
public class BusinessOpportunityModel extends BaseModel implements View.OnClickListener, BusinessRefreshDialogUtils.RefreshListener {
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

    private BusinessRefreshDialogUtils refreshDialog;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> cityNameList = new ArrayList<>();
    private List<String> cityIdList = new ArrayList<>();
    private List<String> areaIdList = new ArrayList<>();
    private List<String> timeList;
    private String cityId = "";
    private String areaId = "";
    private String timestate = "";
    private int pageNum = 1;
    private int pageSize = 20;
    private RelativeLayout emptyView;
    private RelativeLayout topLayout;

    public BusinessOpportunityModel(BusinessOpportunityFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        view =
                inflater.inflate(R.layout.fragment_business_opportunity, container, false);
    }

    public void initView() {
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        topLayout = (RelativeLayout) view.findViewById(R.id.top_layout);
        listView = (ListView) view.findViewById(R.id.grab_list);
        businessCity = (TextView) view.findViewById(R.id.business_city);
        businessTime = (TextView) view.findViewById(R.id.business_time);
        emptyView = (RelativeLayout) view.findViewById(R.id.empty_view);
        refreshDialog = new BusinessRefreshDialogUtils(context.getActivity(), "刷新列表将会清空您的购物车，是否继续？");
        refreshView.canNotRefresh();
        initTimeData();
    }

    public void setCanotPullDown() {
        refreshView.canNotRefresh();
    }

    public void setListener() {
        businessCity.setOnClickListener(this);
        businessTime.setOnClickListener(this);
        refreshDialog.setRefreshListener(this);
        emptyView.setOnClickListener(this);
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
                .params("pageSize", pageSize + "")
                .execute(new BusinessRequest());
    }

    private void getDataForRefresh() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getBids")
                .params("cityId", cityId)
                .params("areaId", areaId)
                .params("timestate", timestate)
                .params("pageNum", pageNum + "")
                .params("pageSize", pageSize + "")
                .execute(new BusinessRefreshRequest(context.getActivity()));
    }

    public void getCityData() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getLocal")
                .execute(new BusinessCityRequest());
    }

    public void dealWithData(int position) {
        setStateToData(position);
        judgePriceLayout();
        adapter.setData(showData);
    }

    private void setStateToData(int position) {
        if (showData.get(position).getIsChoice())
            setNotChoiceState(position);
        else
            setChoiceState(position);
    }

    private void setNotChoiceState(int position) {
        showData.get(position).setIsChoice(false);
        buyData.remove(showData.get(position));
    }

    private void setChoiceState(int position) {
        if (buyData.size() == 20)
            new BusinessFullDialogUtils(context.getActivity(), "您最多可一次购买20条哦~").showSingleButtonDialog();
        else {
            showData.get(position).setIsChoice(true);
            buyData.add(showData.get(position));
        }
    }

    private void judgePriceLayout() {

    }

    public void initTimeData() {
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
            getEmptyData();
        } else {
            showDataView();
        }
    }

    private void getEmptyData() {
        if (showData.size() == 0)
            showEmptyView();
        else {
            showDataView();
            banPullUp();
        }
    }

    private void showDataView() {
        topLayout.setVisibility(View.VISIBLE);
        refreshView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        topLayout.setVisibility(View.GONE);
        refreshView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
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
        if (list.size() < pageSize)
            banPullUp();
    }

    public void clickRefresh() {
        if (buyData.size() != 0)
            refreshDialog.showTwoButtonDialog();
        else
            refresh();
    }

    private void refresh() {
        pageNum = 1;
        canPullUp();
        buyData.clear();
        getDataForRefresh();
    }

    @Override
    public void refreshList() {
        refresh();
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
            case R.id.empty_view:
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
            refresh();
        }
    };

    private AdapterView.OnItemClickListener timeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            timestate = position + "";
            refresh();
        }
    };

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
