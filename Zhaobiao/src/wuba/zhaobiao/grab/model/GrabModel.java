package wuba.zhaobiao.grab.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.CompoundButton;
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
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.adapter.PopAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
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
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.grab.fragment.GrabFragment;
import wuba.zhaobiao.grab.utils.GrabCachUtils;
import wuba.zhaobiao.respons.GrabedResultResponse;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by SongYongmeng on 2016/8/8.
 */
public class GrabModel<T> extends BaseModel implements TitleMessageBarLayout.OnTitleBarClickListener {
    private GrabFragment context;
    private PullToRefreshLayout refreshView;
    private ListView listView;
    private View layout_back_head;
    private LinearLayout choseLayout;
    private TextView textHead;
    private SwitchButton switchButton;
    private PopAdapter adapter;
    private PushToPassBean passBean;
    private View view;
    private List<QDBaseBean> showData = new ArrayList<QDBaseBean>();
    private String pushId = "-1";
    private ViewStub noData;
    private View root;
    protected TitleMessageBarLayout tbl;
    private Boolean isFromCache;
    private BiddingApplication app;

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
    }

    private void initListView() {
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        listView = (ListView) view.findViewById(R.id.grab_list);
        noData = (ViewStub) view.findViewById(R.id.no_data);
    }

    public void creatAdapter() {
        adapter = new PopAdapter(context.getActivity(), adapterListener);
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

    public void registListener() {
        app = BiddingApplication.getBiddingApplication();
        app.setCurrentNotificationListener(context);
        app.registerNetStateListener();
        NetStateManager.getNetStateManagerInstance().setINetStateChangedListener(context);
    }

    public void registerEventBus() {
        EventBus.getDefault().register(context);
    }

    public void setHeaderHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            context.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int height = Utils.getStatusBarHeight(context.getActivity());
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }

    public void grabClickedStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.INDICATOR_BIDDING_LIST_PAGE);
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
                .execute(new GetGrabListRespons(context.getActivity(), true));
    }

    private void grabRequset(PushToPassBean bean, String bidsourceList) {
        String userState = creatGetGrabParams();
        getGrabRequst(bean, bidsourceList, userState);
    }

    @NonNull
    private String creatGetGrabParams() {
        String serviceState = SPUtils.getServiceState(context.getActivity());
        String userState = "";
        if (serviceState.equals("1"))
            userState = "0";
        else if (serviceState.equals("2"))
            userState = "1";
        return userState;
    }

    private void getGrabRequst(PushToPassBean bean, String bidsourceList, String userState) {
        OkHttpUtils.get(Urls.GRAB_REQUEST)
                .params("bidId", "" + bean.getBidId())
                .params("pushId", "" + bean.getPushId())
                .params("pushturn", "" + bean.getPushTurn())
                .params("userState", userState)
                .params("bidSource", bidsourceList)
                .params("token", new Date().getTime() + "")
                .execute(new GrabRespons(context.getActivity(), true));
    }


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

    public void resetTabNumber() {
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
        List<T> ts = getJSONObject(s);
        return (List<QDBaseBean>) ts;
    }

    private List<T> getJSONObject(String s) {
        try {
            JSONObject result = JSON.parseObject(s);
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
                refresh();
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
        context.NetDisConnected();
    }


    public void diaplayMessageBar() {
        if (tbl != null) {
            tbl.showNetError();
            tbl.setVisibility(View.VISIBLE);
        }
    }

    private void refresh() {
        BDMob.getBdMobInstance().onMobEvent(context.getActivity(), BDEventConstans.EVENT_ID_BIDDING_LIAT_PAGE_MANUAL_REFRESH);
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_BIDDING_LIAT_PAGE_MANUAL_REFRESH);
        canPullUp();
        pushId = "-1";
        getData();
    }

    private void NetConnected() {
        context.NetConnected();
    }

    public void closeMessageBar() {
        if (tbl != null && tbl.getType() == TitleBarType.NETWORK_ERROR)
            tbl.setVisibility(View.GONE);
    }

    public void showPush(PushBean pushBean) {
        if (null != pushBean) {
            hasData(pushBean);
        } else {
            nullInfo();
        }
    }

    private void hasData(PushBean pushBean) {
        int type = pushBean.getTag();
        if (type == 100 && StateUtils.getState(context.getActivity()) == 1) {
            dealWhitData();
        } else if (type == 105) {
            grabLogout();
        } else if (type != 100 && type != 105) {
            tbl.setPushBean(pushBean);
            tbl.setVisibility(View.VISIBLE);
            PushUtils.pushList.clear();
        }

    }

    private void dealWhitData() {
        String isSon = UserUtils.getIsSon(context.getActivity());
        if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
            rightInfo();
        } else {
            successGrab();
        }
    }

    private void rightInfo() {
        String rbac = UserUtils.getRbac(context.getActivity());
        if (!TextUtils.isEmpty(rbac)
                && TextUtils.equals("1", rbac) || TextUtils.equals("5", rbac)) {
        } else {
            successGrab();
        }
    }

    private void goingPushin() {
        Intent intent = new Intent(context.getActivity(), PushInActivity.class);
        context.startActivity(intent);
    }

    private void successGrab() {
        refresh();
        goingPushin();
        refreshComeSuccess();
    }

    private void refreshComeSuccess() {
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET_COME_SUCCESS);
        EventbusAgent.getInstance().post(action);
    }

    private void grabLogout() {
        try {
            new LogoutDialogUtils(context.getActivity(), "当前账号被强制退出").showSingleButtonDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nullInfo() {
        Toast.makeText(context.getActivity(), "实体bean为空", Toast.LENGTH_SHORT).show();
    }

    public void jugePush(EventAction action) {

        if (action.getType() == EventType.EVENT_TAB_RESET_SUCCESS) {
            refresh();
        } else if (action.getType() == EventType.EVENT_TAB_RESET_COME_SUCCESS) {
            refresh();
        }
    }


    public void unregistPushAndEventBus() {
        EventBus.getDefault().unregister(context);
        app.removeINotificationListener();
        app.unRegisterNetStateListener();
        NetStateManager.getNetStateManagerInstance().removeINetStateChangedListener();
    }

    private void switchNotChicked() {
        StateUtils.state = 2;
        BDMob.getBdMobInstance().onMobEvent(context.getActivity(), BDEventConstans.EVENT_ID_REST_MODE);

        HYMob.getDataListByModel(context.getActivity(), HYEventConstans.EVENT_ID_CHANGE_MODE);
    }

    public void topSwitchChicked(Boolean tag) {
        if (tag)
            switchChicked();
        else
            switchNotChicked();
        saveState();
        refresh();
    }

    public void topSwitchNotChicked() {
        switchNotChicked();
        saveState();
        refresh();
    }

    private void switchChicked() {
        StateUtils.state = 1;
        BDMob.getBdMobInstance().onMobEvent(context.getActivity(), BDEventConstans.EVENT_ID_SERVICE_MODE);
        HYMob.getDataListByModel(context.getActivity(), HYEventConstans.EVENT_ID_CHANGE_MODE);
    }

    private void saveState() {
        SPUtils.setServiceState(context.getActivity(), StateUtils.state + "");
    }

    public class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                switchChicked();
            } else {
                switchNotChicked();
            }

            saveState();
            refresh();
        }
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

    private class GrabRespons extends DialogCallback<GrabedResultResponse> {

        public GrabRespons(Activity context, boolean b) {
            super(context, b);
        }

        @Override
        public void onResponse(boolean isFromCache, GrabedResultResponse s, Request request, @Nullable Response response) {
            refresh();

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.putExtra("orderId", s.getData().getOrderId());
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
        }
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context.getActivity(), HYEventConstans.PAGE_BINGING_LIST, context.stop_time - context.resume_time);
    }

    private class GetGrabListRespons extends DialogCallback<String> {


        public GetGrabListRespons(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            if (s != null) {
                Log.d("grab_respons", s);
                if (pushId.equals("-1"))
                    saveRespons(s);
                saveCacheState(isFromCache);
                hasData(s);

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
