package wuba.zhaobiao.grab.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BidFailureActivity;
import com.huangyezhaobiao.activity.BidGoneActivity;
import com.huangyezhaobiao.activity.BidSuccessActivity;
import com.huangyezhaobiao.adapter.PopAdapter;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.db.UserRequestDao;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.iview.SwitchButton;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.grab.fragment.GrabFragment;
import wuba.zhaobiao.grab.utils.GrabCachUtils;
import wuba.zhaobiao.respons.GrabedResultResponse;

/**
 * Created by SongYongmeng on 2016/8/8.
 */
public class GrabModel<T> extends BaseModel implements TitleMessageBarLayout.OnTitleBarClickListener {
    private GrabFragment context;
    private PullToRefreshLayout refreshView;
    private ListView listView;
    private LinearLayout choseLayout;
    private TextView textHead;
    private SwitchButton switchButton;
    private PopAdapter adapter;
    private PushToPassBean passBean;
    private KnockViewModel knockViewModel;
    private View view;
    private List<QDBaseBean> showData = new ArrayList<QDBaseBean>();
    private String pushId = "-1";
    private ViewStub noData;
    private View root;
    protected TitleMessageBarLayout tbl;
    private Boolean isFromCache;


    public GrabModel(GrabFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_grab, container, false);
    }

    public void initView() {
        initTopBar();
        initListView();
    }

    private void initTopBar() {
        tbl = (TitleMessageBarLayout) view.findViewById(R.id.tbl);
        choseLayout = (LinearLayout) view.findViewById(R.id.ll_grab);
        textHead = (TextView) view.findViewById(R.id.txt_head);
        switchButton = (SwitchButton) view.findViewById(R.id.switch_button);
    }

    private void initListView() {
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        listView = (ListView) view.findViewById(R.id.grab_list);
        noData = (ViewStub) view.findViewById(R.id.no_data);
    }

    public void creatAdapter() {
        adapter = new PopAdapter(context.getActivity(), adapterListener);
    }

    public void setInfoForTop() {
        textHead.setText("抢单");
        choseLayout.setVisibility(View.VISIBLE);
    }

    public void setParamsForListView() {
        listView.setDividerHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        listView.setAdapter(adapter);
        refreshView.setOnRefreshListener(new Refresh());
    }

    public void registMessageBar() {
        if (tbl != null) {
            tbl.setVisibility(View.GONE);
            tbl.setTitleBarListener(this);
        }
    }

    public void banPullUp() {
        refreshView.setBanPullUp(true);
    }

    public void canPullUp() {
        refreshView.setBanPullUp(false);
    }

    public void getData() {
        OkHttpUtils.get(Urls.GRAB_GET_LIST)
                .params("pushId", pushId)
                .params("bidId", "-1")
                .params("bidState", "-1")
                .execute(new GetGrabListRespons(context.getActivity()));
    }

    private void grabRequset(PushToPassBean bean, String bidsourceList) {
        String serviceState = SPUtils.getServiceState(context.getActivity());
        String userState = "";
        switch (serviceState) {
            case "1"://服务模式
                userState = "0";
                break;
            case "2"://休息模式
                userState = "1";
                break;
        }

        long bidId = bean.getBidId();
        long pushId = bean.getPushId();
        int pushTurn = bean.getPushTurn();

        OkHttpUtils.get(Urls.GRAB_REQUEST)
                .params("bidId", "" + bidId)
                .params("pushId", "" + pushId)
                .params("pushturn", "" + pushTurn)
                .params("userState", userState)
                .params("bidSource", bidsourceList)
                .params("token", new Date().getTime() + "")
                .execute(new GrabRespons(context.getActivity(), true));
    }

    /**
     * adapter的回调监听
     */
    ZBBaseAdapter.AdapterListener adapterListener = new ZBBaseAdapter.AdapterListener() {
        @Override
        public void onAdapterRefreshSuccess() {
            listView.setAdapter(adapter);
        }

        @Override
        public void onAdapterLoadMoreSuccess() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onAdapterViewClick(int id, PushToPassBean bean) {
            //点击了抢单
            BDMob.getBdMobInstance().onMobEvent(context.getActivity(), BDEventConstans.EVENT_ID_BIDDING_LIST_PAGE_BIDDING);

            HYMob.getDataListForQiangdan(context.getActivity(), HYEventConstans.EVENT_ID_BIDDING_LIST_PAGE_BIDDING, String.valueOf(bean.getBidId()), "1");

            passBean = bean;
            grabRequset(bean, AppConstants.BIDSOURCE_LIST);

        }
    };

    public Boolean getBanPullUpState() {
        return refreshView.getBanPullUpState();
    }

    public View getView() {
        return view;
    }

    public void selectChange() {
        try {
            if (UnreadUtils.isHasNewOrder(context.getActivity())) {
                UnreadUtils.clearNewOder(context.getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET);
        EventbusAgent.getInstance().post(action);
    }

    private void hasData(String s) {
        clearLocalInfo();
        dealWithData(s);
    }

    private void dealWithData(String s) {
        List<QDBaseBean> list = getData(s);
        if (list != null)
            dealWithDateNow(list);
    }

    private void dealWithDateNow(List<QDBaseBean> list) {
        noData(list);
        hasData(list);
        hasDataButNotFull(list);
    }

    private List<QDBaseBean> getData(String s) {
        JSONObject result = JSON.parseObject(s);
        List<T> ts = getJSONObject(result);
        return (List<QDBaseBean>) ts;
    }

    private List<T> getJSONObject(JSONObject result) {
        try {
            return new GrabCachUtils().transferToListBean(result.getString("data"));
        } catch (Exception e) {
            return null;
        }
    }


    private void hasDataButNotFull(List<QDBaseBean> list) {
        if (list.size() < 20)
            banPullUp();
    }

    private void hasData(List<QDBaseBean> list) {
        if (list.size() > 0)
            showDataToList(list);
    }

    private void showDataToList(List<QDBaseBean> list) {
        setPushId(list);
        showData.addAll(list);
        adapter.loadMoreSuccess(showData);
        adapter.notifyDataSetChanged();
    }

    private void setPushId(List<QDBaseBean> list) {
        if (isFromCache != null && !isFromCache)
            pushId = String.valueOf(list.get(list.size() - 1).getPushId());
    }

    private void noData(List<QDBaseBean> list) {
        if (list.size() == 0 && showData.size() == 0)
            showEmptyView();
        else
            dismissEmptyView();
    }

    public void showEmptyView() {
        if (root == null)
            creatRoot();
        root.setVisibility(View.VISIBLE);
    }

    private void creatRoot() {
        root = noData.inflate();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void dismissEmptyView() {
        if (root != null)
            root.setVisibility(View.GONE);
    }

    private void clearLocalInfo() {
        if (pushId.equals("-1"))
            showData.clear();
    }

    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onTitleBarClosedClicked() {
        if (tbl != null)
            tbl.setVisibility(View.GONE);
    }


    private void saveRespons(String s) {
        UserRequestDao.addData(UserRequestDao.INTERFACE_GETBINDS, s);
    }

    public void setCachRespons() {
        String respons = UserRequestDao.getData(UserRequestDao.INTERFACE_GETBINDS);
        if (respons != null) {
            Log.d("grab_respons_set", respons);
            hasData(respons);
        }
    }

    public void checkNet() {
        if (NetUtils.isNetworkConnected(context.getActivity())) {
            NetConnected();
        } else {
            NetDisConnected();
        }
    }

    private void NetDisConnected() {
        if (tbl != null) {
            tbl.showNetError();
            tbl.setVisibility(View.VISIBLE);
        }
    }

    private void refresh() {
        canPullUp();
        pushId = "-1";
        getData();
    }

    private void NetConnected() {
        if (tbl != null && tbl.getType() == TitleBarType.NETWORK_ERROR)
            tbl.setVisibility(View.GONE);
    }

    private class Refresh implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refresh();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (getBanPullUpState())
                refreshView.refreshComplete();
            else
                getData();
        }
    }

    private class GrabRespons extends DialogCallback<GrabedResultResponse> {

        public GrabRespons(Activity context, boolean b) {
            super(context, b);
        }

        @Override
        public void onResponse(boolean isFromCache, GrabedResultResponse s, Request request, @Nullable Response response) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.putExtra("orderId",s.getData().getOrderId());
            bundle.putSerializable("passBean", passBean);
            intent.putExtras(bundle);

            switch (s.getData().getStatus()) {
                case "1":
                    intent.setClass(context.getActivity(), BidGoneActivity.class);
                    context.getActivity().startActivity(intent);
                    break;
                case "2":
                    ToastUtils.showToast(context.getActivity().getString(R.string.not_enough_balance));
                    if (passBean != null) {
                        MDUtils.YuENotEnough(passBean.getCateId() + "", passBean.getBidId() + "");
                    }
                    break;
                case "3":
                    intent.setClass(context.getActivity(), BidSuccessActivity.class);
                    context.startActivity(intent);
                    Toast.makeText(context.getActivity(), "抢单成功", Toast.LENGTH_SHORT).show();
                    break;
                case "4":
                    ToastUtils.showToast(context.getActivity().getString(R.string.bidding_already_bid));
                    break;
                case "5":
                    intent.setClass(context.getActivity(), BidFailureActivity.class);
                    context.getActivity().startActivity(intent);
                    Toast.makeText(context.getActivity(), "抢单并没有成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            refresh();
        }
    }

    private class GetGrabListRespons extends DialogCallback<String> {
        public GetGrabListRespons(Activity context) {
            super(context);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            if (s != null) {
                Log.d("grab_respons", s);
                if (pushId.equals("-1"))
                    saveRespons(s);
                hasData(s);
                saveCacheState(isFromCache);
            }
            refreshView.refreshComplete();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            refreshView.refreshComplete();
        }
    }

    private void saveCacheState(Boolean isFromCache) {
        this.isFromCache = isFromCache;
    }

}
