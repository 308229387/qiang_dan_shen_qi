package wuba.zhaobiao.grab.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
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
import wuba.zhaobiao.grab.activity.SettlementFailActivity;
import wuba.zhaobiao.grab.activity.SettlementSuccessActivity;
import wuba.zhaobiao.grab.adapter.BusinessOpportunityAdapter;
import wuba.zhaobiao.grab.fragment.BusinessOpportunityFragment;
import wuba.zhaobiao.respons.BusinessCityRespons;
import wuba.zhaobiao.respons.BusinessOpportunityRespons;
import wuba.zhaobiao.respons.BusinessSettlementRespons;
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
    private int pageSize = 10;
    private String timestamp;
    private String timeTempTag = "";
    private View line;

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
        line = (View) view.findViewById(R.id.line);
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
        refreshDialog = new BusinessRefreshDialogUtils(context.getActivity(), context.getString(R.string.business_refresh));
        clearDialog = new BusinessClearDialogUtils(context.getActivity(), context.getString(R.string.business_clear));
    }

    public void initTimeData() {
        timeList = new ArrayList<String>();
        timeList.add("按时间正序");
        timeList.add("按时间倒序");
        timestate = 1 + "";
    }

    public void getCityData() {
        OkHttpUtils.get(Urls.BUSINESS_GETCITY)
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
        OkHttpUtils.get(Urls.BUSINESS_OPPORTUNITY)
                .params("cityId", cityId)
                .params("areaId", areaId)
                .params("timestate", timestate)
                .params("timestamp", timestamp)
                .execute(new BusinessRequest());
    }

    private void getDataForRefresh() {
        OkHttpUtils.get(Urls.BUSINESS_OPPORTUNITY)
                .params("cityId", cityId)
                .params("areaId", areaId)
                .params("timestamp", timestamp)
                .params("timestate", timestate)
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
            new BusinessFullDialogUtils(context.getActivity(), context.getString(R.string.business_max_prompt)).showSingleButtonDialog();
        else {
            showData.get(position).setIsChoice(true);
            buyData.add(showData.get(position));
        }
    }

    private void judgePriceLayout() {
        showOrDismissPriceLayout();
        getTotal();
        setTextForPrice();
    }

    private void showOrDismissPriceLayout() {
        if (buyData.size() > 0)
            settlementLayout.setVisibility(View.VISIBLE);
        else
            settlementLayout.setVisibility(View.GONE);
    }

    private void getTotal() {
        total = 0;
        for (int i = 0; i < buyData.size(); i++) {
            if(!TextUtils.isEmpty(buyData.get(i).getKey7())){
                double balanceTemp = Double.parseDouble(buyData.get(i).getKey7());
                total = total + balanceTemp;
            }

        }
    }

    private void setTextForPrice() {
        balance.setText("¥  " + PublickMethod.getPriceFromDouble(total));
        settleButton.setText("结算(" + buyData.size() + ")");
    }

    private void showCityPop() {
        mSpinerPopWindow = new SpinerPopWindow<String>(context.getActivity(), cityNameList, cityItemClickListener, dismissListener);
        mSpinerPopWindow.itemHigh();
        mSpinerPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mSpinerPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mSpinerPopWindow.showAsDropDown(line, -70, 0);
        mSpinerPopWindow.setHighForListView();
    }

    private void showTimePop() {
        mSpinerPopWindow = new SpinerPopWindow<String>(context.getActivity(), timeList, timeItemClickListener, dismissListener);
        mSpinerPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mSpinerPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mSpinerPopWindow.showAsDropDown(line, -70, 0);
        mSpinerPopWindow.setHighForTime();
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
        areaId = "";
    }

    private void stateRight(ArrayList<BusinessData> list) {
        clearLocalInfo();
        dealWithListData(list);
    }

    private void clearLocalInfo() {
        if (timestamp == "") {
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
        timestamp = timeTempTag;
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

    public void refresh() {
        timestamp = "";
        canPullUp();
        buyData.clear();
        showOrDismissPriceLayout();
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
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_SETTLE_DIALOG_CHECKED);
        settlement();
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
        String temp = String.format(context.getString(R.string.business_settlement), PublickMethod.getPriceFromDouble(total));
        settlementDialog = new BusinessSettlementDialogUtils(context.getActivity(), temp);
        settlementDialog.setSettlementListener(this);
        settlementDialog.showTwoButtonDialog();
    }

    private void settlement() {
        StringBuffer temp = getBids();
        OkHttpUtils.get(Urls.BUSINESS_SETTLEMENT)
                .params("bids", temp.toString())
                .execute(new BusinessSettlement(context.getActivity(), false));
    }

    @NonNull
    private StringBuffer getBids() {
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < buyData.size(); i++)
            temp.append(buyData.get(i).getBidid() + ",");
        return temp;
    }

    private void goToFail() {
        Intent intent = new Intent();
        intent.putExtra("failType", "2");
        intent.setClass(context.getActivity(), SettlementFailActivity.class);
        context.startActivity(intent);
    }

    private void goToFailOther() {
        Intent intent = new Intent();
        intent.putExtra("failType", "5");
        intent.setClass(context.getActivity(), SettlementFailActivity.class);
        context.startActivity(intent);
    }

    private void goToSettlementResult(BusinessSettlementRespons result) {
        Intent intent = new Intent();
        intent.setClass(context.getActivity(), SettlementSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", result);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @NonNull
    private List<Double> getDoubles() {
        List<Double> temp11 = new ArrayList<>();
        for (int i = 0; i < buyData.size(); i++) {
            double pr = Double.parseDouble(buyData.get(i).getKey7());
            temp11.add(pr);
        }
        Collections.sort(temp11);
        return temp11;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_city:
                if (cityNameList.size() > 0)
                    showCityPop();
                cityClickedStatistics();
                break;
            case R.id.business_time:
                showTimePop();
                timeClickedStatistics();
                break;
            case R.id.empty_view:
                break;
            case R.id.settlement_button:
                getBalance();
                settleClickedStatistics();
                break;
            case R.id.business_clear:
                clearDialog.showTwoButtonDialog();
                clearClickedStatistics();
                break;
            default:
                break;
        }
    }

    private void cityClickedStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_CITY_SELECT);
    }

    private void timeClickedStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_TIME_SELECT);
    }

    private void settleClickedStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_BUSINESS_SETTLEMENT);
    }

    private void clearClickedStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_BUSINESS_CLEAR);
    }

    private View.OnClickListener dismissListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mSpinerPopWindow != null)
                mSpinerPopWindow.dismiss();
        }
    };

    private AdapterView.OnItemClickListener cityItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            if (areaIdList.size() > 0 && position != 0)
                areaId = (areaIdList.get(position - 1));
            else {
                cityId = (cityIdList.get(position));
                areaId = "";
            }
            businessCity.setText(cityNameList.get(position));
            refresh();
        }
    };

    private AdapterView.OnItemClickListener timeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            timestate = position + "";
            if (position == 0)
                businessTime.setText("时间正序");
            else
                businessTime.setText("时间倒序");
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

    private class ClickItem implements AdapterView.OnItemClickListener {
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
            timeTempTag = s.getTimestamp();
            stateRight(data);
            refreshView.refreshComplete();
        }

    }

    private class BusinessRefreshRequest extends DialogCallback<BusinessOpportunityRespons> {

        public BusinessRefreshRequest(Activity context) {
            super(context, true);
        }

        @Override
        public void onResponse(boolean isFromCache, BusinessOpportunityRespons s, Request request, @Nullable Response response) {
            data = s.getRespData();
            timeTempTag = s.getTimestamp();
            stateRight(data);
            refreshView.refreshComplete();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
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
            List<Double> temp11 = getDoubles();
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


    private class BusinessSettlement extends DialogCallback<BusinessSettlementRespons> {
        public BusinessSettlement(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, BusinessSettlementRespons result, Request request, @Nullable Response response) {
            switch (result.getState()) {
                case "1":
                    goToSettlementResult(result);
                    break;
                case "2":
                    goToFail();
                    break;
                case "3":
                    goToSettlementResult(result);
                    break;
                case "4":
                    goToSettlementResult(result);
                    break;
                case "5":
                    goToFailOther();
                    break;
                default:
                    break;
            }
        }
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context.getActivity(), HYEventConstans.PAGE_BUSINESS_OPPORTUNITY, context.stop_time - context.resume_time);
    }

}
