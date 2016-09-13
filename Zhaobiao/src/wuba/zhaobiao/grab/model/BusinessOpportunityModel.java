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
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.bean.BusinessData;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.grab.adapter.BusinessOpportunityAdapter;
import wuba.zhaobiao.grab.fragment.BusinessOpportunityFragment;
import wuba.zhaobiao.respons.BusinessCityRespons;
import wuba.zhaobiao.respons.BusinessOpportunityRespons;
import wuba.zhaobiao.respons.UserInfoRespons;
import wuba.zhaobiao.utils.BusinessClearDialogUtils;
import wuba.zhaobiao.utils.BusinessFullDialogUtils;
import wuba.zhaobiao.utils.BusinessRefreshDialogUtils;
import wuba.zhaobiao.utils.BusinessSettlementDialogUtils;
import wuba.zhaobiao.utils.PublickMethod;
import wuba.zhaobiao.utils.SpinerPopWindow;

/**
 * Created by SongYongmeng on 2016/9/5.
 */
public class BusinessOpportunityModel extends BaseModel implements View.OnClickListener, BusinessRefreshDialogUtils.RefreshListener, BusinessClearDialogUtils.ClearListener, BusinessSettlementDialogUtils.SettlementListener {
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
    private boolean isread;
    private RelativeLayout emptyView;
    private RelativeLayout topLayout;
    private RelativeLayout settlementLayout;
    private TextView settleButton;
    private TextView balance;
    private TextView clearButton;
    private BusinessClearDialogUtils clearDialog;
    private double total;
    private BusinessSettlementDialogUtils settlementDialog;

    public BusinessOpportunityModel(BusinessOpportunityFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        view =
                inflater.inflate(R.layout.fragment_business_opportunity, container, false);
    }

    public void initView() {
        settlementLayout = (RelativeLayout) view.findViewById(R.id.settlement_layout);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        topLayout = (RelativeLayout) view.findViewById(R.id.top_layout);
        listView = (ListView) view.findViewById(R.id.grab_list);
        businessCity = (TextView) view.findViewById(R.id.business_city);
        businessTime = (TextView) view.findViewById(R.id.business_time);
        emptyView = (RelativeLayout) view.findViewById(R.id.empty_view);
        settleButton = (TextView) view.findViewById(R.id.settlement_button);
        clearButton = (TextView) view.findViewById(R.id.business_clear);
        balance = (TextView) view.findViewById(R.id.business_balance);
    }

    public void setCanotPullDown() {
        refreshView.canNotRefresh();
    }

    public void setListener() {
        businessCity.setOnClickListener(this);
        businessTime.setOnClickListener(this);
        refreshDialog.setRefreshListener(this);
        clearDialog.setClearListener(this);
        settleButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        emptyView.setOnClickListener(this);
    }

    public void creatDialog() {
        refreshDialog = new BusinessRefreshDialogUtils(context.getActivity(), context.getActivity().getString(R.string.business_refresh));
        clearDialog = new BusinessClearDialogUtils(context.getActivity(), context.getActivity().getString(R.string.business_clear));
    }

    public void initTimeData() {
        timeList = new ArrayList<String>();
        timeList.add("按时间正序");
        timeList.add("按时间倒序");
        timestate = 0 + "";
    }

    public void getCityData() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getLocal")
                .execute(new BusinessCityRequest());
    }

    public void creatAdapter() {
        adapter = new BusinessOpportunityAdapter(context.getActivity());
    }

    public void setParamsForListView() {
        listView.setAdapter(adapter);
        refreshView.setOnRefreshListener(new Refresh());
        listView.setOnItemClickListener(new ClickItem());
    }

    public void getMaskState() {
        isread = UserUtils.getBusinessMask(context.getActivity());
    }

    public View getView() {
        return view;
    }

    public void getData() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getBids")
                .params("cityId", cityId)
//                .params("areaId", areaId)
                .params("timestate", timestate)
                .params("pageNum", pageNum + "")
                .params("pageSize", pageSize + "")
                .execute(new BusinessRequest());
    }

    private void getDataForRefresh() {
        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/getBids")
                .params("cityId", cityId)
//                .params("areaId", areaId)
                .params("timestate", timestate)
                .params("pageNum", pageNum + "")
                .params("pageSize", pageSize + "")
                .execute(new BusinessRefreshRequest(context.getActivity()));
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
        if (buyData.size() > 0)
            settlementLayout.setVisibility(View.VISIBLE);
        else
            settlementLayout.setVisibility(View.GONE);
        total = 0;
        for (int i = 0; i < buyData.size(); i++) {
            double balanceTemp = Double.parseDouble(buyData.get(i).getKey7());
            total = total + balanceTemp;
        }

        balance.setText(PublickMethod.getPriceFromDouble(total));
        settleButton.setText("结算(" + buyData.size() + ")");
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
        refreshView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
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
        settlementLayout.setVisibility(View.GONE);
    }

    @Override
    public void clear() {
        buyData.clear();
        settlementLayout.setVisibility(View.GONE);
        setAllCancelState();
        adapter.setData(showData);
    }

    @Override
    public void settlementCheck() {
        if (settlementDialog.getCheckState())
            UserUtils.setBusinessCheckBox(context.getActivity(), false);
        ToastUtils.showToast("zhifu");
    }

    public void setAllCancelState() {
        for (int i = 0; i < showData.size(); i++)
            showData.get(i).setIsChoice(false);
    }

    private void getBalance() {
        OkHttpUtils.get(Urls.USER_INFO)
                .execute(new GetBalance(context.getActivity(), false));
    }


    private void showSettlementDialog() {
        String temp = "当前选中商机预计" + PublickMethod.getPriceFromDouble(total) + "元，当选中商机已售完时或余额不足时，实际支付金额低于此报价";
        settlementDialog = new BusinessSettlementDialogUtils(context.getActivity(), temp);
        settlementDialog.setSettlementListener(this);
        settlementDialog.showTwoButtonDialog();
    }

    private void settlement() {
        ToastUtils.showToast("getget");
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < buyData.size(); i++)
            temp.append(buyData.get(i).getBidid() + ",");

        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/order/purchase")
                .params("bids", temp.toString())
                .execute(new BusinessSettlement(context.getActivity(), false));
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
            case R.id.settlement_button:
                getBalance();
                break;
            case R.id.business_clear:
                clearDialog.showTwoButtonDialog();
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
            if (!isread) {
                isread = true;
                EventBus.getDefault().post(new BusinessMessage("business_mask"));
            } else
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
            showEmptyView();
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

    public class BusinessMessage {
        private String msg;

        public BusinessMessage(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

    private class GetBalance extends DialogCallback<UserInfoRespons> {

        public GetBalance(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, UserInfoRespons userInfoRespons, Request request, @Nullable Response response) {
            String temp = userInfoRespons.getData().getBalance();
            double balance = Double.parseDouble(temp);
            List<Double> temp11 = new ArrayList<>();
            for (int i = 0; i < buyData.size(); i++) {
                double pr = Double.parseDouble(buyData.get(i).getKey7());
                temp11.add(pr);
            }
            Collections.sort(temp11);
            if (balance < temp11.get(0))
                ToastUtils.showToast("余额不足");
            else {
                boolean needShow = UserUtils.getBusinessCheckBox(context.getActivity());
                if (needShow)
                    showSettlementDialog();
                else
                    settlement();
            }
        }

    }

    private class BusinessSettlement extends DialogCallback<String> {
        public BusinessSettlement(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            ToastUtils.showToast(s);
            LogUtils.LogV("settlement",s);
        }
    }
}
