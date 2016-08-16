package wuba.zhaobiao.order.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.FetchDetailsActivity;
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.adapter.OrderLVAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.db.UserRequestDao;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.iview.OrderCatePopupWindow;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.order.fragment.OrderFragment;
import wuba.zhaobiao.order.utils.OrderCachUtils;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/8/15.
 */
public class OrderModel<T> extends BaseModel implements TitleMessageBarLayout.OnTitleBarClickListener {

    private OrderFragment context;

    private View view;

    private View layout_back_head;
    private TextView txt_head;
    private ImageView btn_clean;
    private TitleMessageBarLayout tbl;
    private OrderCatePopupWindow mOrderCateWindow; //筛选框

    private PullToRefreshLayout refreshView;
    private ListView listView;
    private View layout_no_data; //您还没有已抢订单呢！
    private TextView tv_no_orders;

    private OrderLVAdapter adapter;

    private String pageNum = "1";
    private int pageNumber = 1;
    private String totalPage = "";

    private List<QDBaseBean> showData = new ArrayList<>();

    private BiddingApplication app;

    public static String orderState ="0"; //根据筛选器得到订单状态 待服务1,服务中2,已服务3

    public final static  List<String> checkedId = new ArrayList<>();

    private Boolean isFromCache;

    public OrderModel(OrderFragment context) {
        this.context = context;
    }

    public void createView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
    }

    public void initView() {
        initTopBar();
        initListView();
    }

    private void initTopBar() {
        layout_back_head = view.findViewById(R.id.layout_back);
        tbl = (TitleMessageBarLayout) view.findViewById(R.id.tbl);
        txt_head = (TextView) view.findViewById(R.id.txt_head);
        txt_head.setText(R.string.my_bidding);
        btn_clean = (ImageView) view.findViewById(R.id.btn_clean);
        btn_clean.setVisibility(View.VISIBLE);
        btn_clean.setOnClickListener(listener);
    }

    private void initListView() {
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        listView = (ListView) view.findViewById(R.id.order_list);
        layout_no_data = view.findViewById(R.id.layout_no_data);
        layout_no_data.setVisibility(View.GONE);
        tv_no_orders = (TextView) view.findViewById(R.id.tv_no_orders);
    }


    public void createAdapter() {
        adapter = new OrderLVAdapter(context.getActivity(), adapterListener);
    }

    public void setParamsForListVew() {
        listView.setDividerHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
        refreshView.setOnRefreshListener(new Refresh());

    }

    public void setCacheRespons(){
        String result = UserRequestDao.getData(UserRequestDao.INTERFACE_ORDERLIST);
        if (result != null) {
            Log.d("order_respons_set", result);
            hasData(result);
        }
    }

    public void registerMessageBar() {
        if (tbl != null) {
            tbl.setVisibility(View.GONE);
            tbl.setTitleBarListener(this);
        }
    }


    public void registerListenrer() {
        app = BiddingApplication.getBiddingApplication();
        app.setCurrentNotificationListener(context);
        app.registerNetStateListener();
    }


    public void checkNet() {
        if (NetUtils.isNetworkConnected(context.getActivity())) {
            NetConnected();
        } else {
            NetDisConnected();
        }
    }

    private void NetConnected() {
        if (tbl != null && tbl.getType() == TitleBarType.NETWORK_ERROR)
            tbl.setVisibility(View.GONE);
    }

    private void NetDisConnected() {
        if (tbl != null) {
            tbl.showNetError();
            tbl.setVisibility(View.VISIBLE);
        }
    }
    public void OrderTabClickedStatistics() {
        //我的订单中心
        BDMob.getBdMobInstance().onMobEvent(context.getActivity(), BDEventConstans.EVENT_ID_MY_BIDDING);
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_MY_BIDDING);

        HYMob.getDataList(context.getActivity(), HYEventConstans.INDICATOR_ORDER_PAGE);
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

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOrderCateWindow != null && mOrderCateWindow.isShowing()) {
                LogUtils.LogV("tag", "正在关闭");
                mOrderCateWindow.dismiss();
                pageNum ="1";
                getData();
                filterClickedStatistics();
            } else {
                LogUtils.LogV("tag", "正在打开");
                mOrderCateWindow = new OrderCatePopupWindow(context.getActivity(), R.layout.order_cate_popup, popListener, orderState);
                mOrderCateWindow.showAsDropDown(btn_clean, 0, 15);
                filterClickedStatistics();
            }
        }
    };

    private void filterClickedStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_FILTER);
    }

    private OrderCatePopupWindow.OnOrderCatePopupWindowItemClick popListener =  new OrderCatePopupWindow.OnOrderCatePopupWindowItemClick() {
        @Override
        public void onOrderWindowItemClick(View v) {
            switch (v.getId()) {

                case R.id.tv_orderState_confirm:
                    LogUtils.LogV("tag", "确定被点击");
                    if(checkedId != null && checkedId.size() >0){
                        for(String s: checkedId){
                            orderState =s;
                        }
                    }else{
                        orderState = "0";
                    }
                    if(mOrderCateWindow!=null && mOrderCateWindow.isShowing()){
                        mOrderCateWindow.dismiss();
                        pageNum ="1";
                        getData();
                    }
                    HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_FILTER_CONFIRM);

                    break;

            }


        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivityUtils.goToActivity(context.getActivity(), FetchDetailsActivity.class);

        }
    };



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

    public Boolean getBanPullUpState() {
        return refreshView.getBanPullUpState();
    }

    private void refresh() {
        canPullUp();
        pageNum = "1";
        getData();
    }


    public void banPullUp() {
        refreshView.setBanPullUp(true);
    }

    public void canPullUp() {
        refreshView.setBanPullUp(false);
    }

    public void getData() {
        OkHttpUtils.get(Urls.ORDER_GET_LIST)
                .params("orderState", orderState)
                .params("pageNum", pageNum)
                .execute(new getOrderListRespons(context.getActivity()));
    }

    private void saveRespons(String s) {
        UserRequestDao.addData(UserRequestDao.INTERFACE_ORDERLIST, s);
    }

    private void hasData(String s) {
        clearLocalInfo();
        dealWithData(s);
        getPage(s);
    }

    private void clearLocalInfo() {
        if (TextUtils.equals("1", pageNum)) {
            showData.clear();
        }
    }

    private void dealWithData(String s) {
        List<QDBaseBean> list = getData(s);
        if (list != null)
            dealWithDateNow(list);
    }

    private String  getPage(String s ){
        JSONObject result = JSON.parseObject(s);
        totalPage = getJSONObjectOfPage(result);
        return totalPage;
    }

    private List<QDBaseBean> getData(String s) {
        JSONObject result = JSON.parseObject(s);
        List<T> ts = getJSONObject(result);
        return (List<QDBaseBean>) ts;
    }

    private List<T> getJSONObject(JSONObject result) {
        try {
            return new OrderCachUtils().transferToListBean(result.getString("data"));
        } catch (Exception e) {
            return null;
        }
    }

    private String  getJSONObjectOfPage(JSONObject result) {
        try {
            return new OrderCachUtils().transferToBean(result.getString("other"));
        } catch (Exception e) {
            return null;
        }
    }

    private void dealWithDateNow(List<QDBaseBean> list) {
        String isSon = UserUtils.getIsSon(context.getActivity());
        if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
            String rbac = UserUtils.getRbac(context.getActivity());
            if (!TextUtils.isEmpty(rbac)
                    && TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {
                listView.setVisibility(View.GONE);
                layout_no_data.setVisibility(View.VISIBLE);
                tv_no_orders.setText("对不起，您的账号暂无\n查看已抢订单权限");
            } else {
                noData(list);
                hasData(list);
            }

        } else {
            noData(list);
            hasData(list);
        }
        hasDataButNotFull(list);
    }

    private void noData(List<QDBaseBean> list) {
        if (list.size() == 0 && showData.size() == 0){
            listView.setVisibility(View.GONE);
            layout_no_data.setVisibility(View.VISIBLE);
            layout_no_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pageNum = "1";
                    getData();
                }
            });
        }else{
            listView.setVisibility(View.VISIBLE);
            layout_no_data.setVisibility(View.GONE);
        }


    }


    private void showDataToList(List<QDBaseBean> list) {
        setPageNum();
        showData.addAll(list);
        adapter.loadMoreSuccess(showData);
        adapter.notifyDataSetChanged();
    }

    private void setPageNum(){
        if (isFromCache != null && !isFromCache ){
            try {
                if(pageNumber <= Integer.parseInt(totalPage)){
                    LogUtils.LogV("totalPage","totalpage" + totalPage);
                    pageNumber++;
                    pageNum = String.valueOf(pageNumber);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


    }

    private void hasData(List<QDBaseBean> list) {
        if (list.size() > 0){
            showDataToList(list);
        }
    }

    private void hasDataButNotFull(List<QDBaseBean> list) {
        if (list.size() < 5)
            banPullUp();
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
            orderLogout();
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
            goingPushin();
        }
    }

    private void rightInfo() {
        String rbac = UserUtils.getRbac(context.getActivity());
        if (!TextUtils.isEmpty(rbac)
                && TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {
        } else {
            successGrab();
        }
    }

    private void successGrab() {
        refresh();
        goingPushin();
    }

    private void goingPushin() {
        Intent intent = new Intent(context.getActivity(), PushInActivity.class);
        context.startActivity(intent);
    }

    private void orderLogout() {
        try {
            new LogoutDialogUtils(context.getActivity(), "当前账号被强制退出").showSingleButtonDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nullInfo() {
        Toast.makeText(context.getActivity(), "实体bean为空", Toast.LENGTH_SHORT).show();
    }

    public void unregistPushAndEventBus() {
        app.removeINotificationListener();
        app.unRegisterNetStateListener();
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context.getActivity(), HYEventConstans.PAGE_MY_ORDER_LIST, context.stop_time - context.resume_time);
    }

    ZBBaseAdapter.AdapterListener adapterListener = new ZBBaseAdapter.AdapterListener() {
        @Override
        public void onAdapterRefreshSuccess() {
            if (listView != null && adapter != null) {
                listView.setAdapter(adapter);
            }
        }

        @Override
        public void onAdapterLoadMoreSuccess() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onAdapterViewClick(int id, PushToPassBean bean) {

        }
    };

    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onTitleBarClosedClicked() {
        if (tbl != null)
            tbl.setVisibility(View.GONE);
    }

    private class getOrderListRespons extends DialogCallback<String> {


        public getOrderListRespons(Activity context) {
            super(context);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            if (s != null) {
                if (TextUtils.equals(pageNum, "1")) {
                    saveRespons(s);
                }
                hasData(s);
                saveCacheState(isFromCache);
            }
            refreshView.refreshComplete();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
            refreshView.refreshComplete();
        }
    }
    private void saveCacheState(Boolean isFromCache) {
        this.isFromCache = isFromCache;
    }

}
