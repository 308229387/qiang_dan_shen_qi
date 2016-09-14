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

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BiddingDetailsActivity;
import com.huangyezhaobiao.activity.BusinessDetailsActivity;
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.adapter.OrderListAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.db.UserRequestDao;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.iview.OrderCatePopupWindow;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.order.fragment.OrderFragment;
import wuba.zhaobiao.respons.OrderListRespons;
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
    private OrderListAdapter orderListAdapter;   //新的adapter

    private String pageNum = "1";
    private int pageNumber = 1;
    private String totalPage = "";

    private String orderType = "";

    private List<OrderListRespons.bean> showData = new ArrayList<>();

    private BiddingApplication app;

    public static String orderState ="0"; //根据筛选器得到订单状态 待服务1,服务中2,已服务3,未分类4，待跟进5，已完结6

    public final static  List<String> CategoryCheckedId = new ArrayList<>(); //订单状态列表
    public final static  List<String> stateCheckedId = new ArrayList<>(); //订单状态列表

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

    public void initType() {
        String type = UserUtils.getHasaction(context.getActivity());
        if(TextUtils.equals(type,"2")){
            orderType = "2";
        }else{
            orderType = "1";
        }
    }

    public void createAdapter() {
        orderListAdapter  = new OrderListAdapter(context.getActivity(),showData);
    }

    public void setParamsForListVew() {
        listView.setDividerHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        listView.setAdapter(orderListAdapter);
        listView.setOnItemClickListener(itemClickListener);
        refreshView.setOnRefreshListener(new Refresh());

    }

    public void setCacheRespons(){
        String result = UserRequestDao.getData(UserRequestDao.INTERFACE_ORDERLIST);
        if (result != null) {
            Log.d("order_respons_set", result);
//            hasData(result);
        }
    }

    public void registerMessageBar() {
        app = BiddingApplication.getBiddingApplication();
        app.registerNetStateListener();
        NetStateManager.getNetStateManagerInstance().mListeners.add(context);
        initMessageBar();
    }

     private void initMessageBar(){
         if (tbl != null) {
             tbl.setVisibility(View.GONE);
             tbl.setTitleBarListener(this);
         }
     }


    public void registerListenrer() {
        app.setCurrentNotificationListener(context);
//        NetStateManager.getNetStateManagerInstance().setINetStateChangedListener(context);
    }


    public void checkNet() {
        if (NetUtils.isNetworkConnected(context.getActivity())) {
            NetConnected();
        } else {
            NetDisConnected();
        }
    }

    private void NetConnected() {
        context.NetConnected();
    }

    public void closeMessageBar(){
        if (tbl != null && tbl.getType() == TitleBarType.NETWORK_ERROR)
            tbl.setVisibility(View.GONE);
    }

    private void NetDisConnected() {
        context.NetDisConnected();
    }

    public void diaplayMessageBar(){
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
            if(UnreadUtils.isHasQDResult(context.getActivity())){
                UnreadUtils.clearQDResult(context.getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET);
        EventbusAgent.getInstance().post(action);
    }

    public void resetTabNumber(){
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
                closePopWindow();
            } else {
                LogUtils.LogV("tag", "正在打开");
                openPopWindow();
            }
        }
    };

    private void closePopWindow(){
        mOrderCateWindow.dismiss();
        refresh();
        filterClickedStatistics();
    }

    private void openPopWindow(){
        mOrderCateWindow = new OrderCatePopupWindow(context.getActivity(), R.layout.order_cate_popup, popListener, orderState);
        mOrderCateWindow.showAsDropDown(btn_clean, 0, 15);
        filterClickedStatistics();
    }

    private void filterClickedStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_FILTER);
    }

    private OrderCatePopupWindow.OnOrderCatePopupWindowItemClick popListener =  new OrderCatePopupWindow.OnOrderCatePopupWindowItemClick() {
        @Override
        public void onOrderWindowItemClick(View v) {
            switch (v.getId()) {

                case R.id.tv_orderState_confirm:
                    LogUtils.LogV("tag", "确定被点击");
                    getOrderType();
                    getOrderState();
                    closeAndGetData();
                    clickConfirmStatistics();
                    break;
            }
        }
    };

    private void getOrderType(){
        if(CategoryCheckedId != null && CategoryCheckedId.size() >0){
            for(String s: CategoryCheckedId){
                orderType =s;
            }
        }
    }

    private void getOrderState(){
        if(stateCheckedId != null && stateCheckedId.size() >0){
            for(String s: stateCheckedId){
                orderState =s;
            }
        }else{
            orderState = "0";
        }
    }

    private void closeAndGetData(){
        if(mOrderCateWindow!=null && mOrderCateWindow.isShowing()){
            mOrderCateWindow.dismiss();
            refresh();
        }
    }

    private void clickConfirmStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_FILTER_CONFIRM);
    }


    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OrderListRespons.bean  bean = (OrderListRespons.bean) orderListAdapter.getItem(position);
            Map<String, String> map = new HashMap<String, String>();
            map.put(Constans.ORDER_ID, bean.getId());
            if(TextUtils.equals(orderType,"1")){
                ActivityUtils.goToActivityWithString(context.getActivity(), BiddingDetailsActivity.class, map);
            }else{
                ActivityUtils.goToActivityWithString(context.getActivity(), BusinessDetailsActivity.class, map);
            }

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
        pageNumber =1;
        getData();
    }

    public void banPullUp() {
        refreshView.setBanPullUp(true);
    }

    public void canPullUp() {
        refreshView.setBanPullUp(false);
    }

    public void getData() {
//        OkHttpUtils.get("http://zhaobiao.58.com/appbatch/order/orderlist")
        OkHttpUtils.get(Urls.GET_NEW_ORDER_LIST)
                .params("pageNum", pageNum)
                .params("pageSize", "5")
                .params("type", orderType)
                .params("state", orderState)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new getOrderListRespons(context.getActivity(), true));
    }

    private void saveRespons(String s) {
        UserRequestDao.addData(UserRequestDao.INTERFACE_ORDERLIST, s);
    }

    private void hasData(OrderListRespons orderListRespons) {
        clearLocalInfo();
        dealWithData(orderListRespons);
        getPage(orderListRespons);
    }

    private void clearLocalInfo() {
        if (TextUtils.equals("1", pageNum)) {
            showData.clear();
        }
    }

    private void dealWithData(OrderListRespons orderListRespons) {

        List<OrderListRespons.bean> list = orderListRespons.getData();
        if (list != null)
            dealWithDateNow(list);
    }

    private String  getPage(OrderListRespons orderListRespons ){
        totalPage = orderListRespons.getOther().getPageCount();
        return totalPage;
    }

    private void dealWithDateNow(List<OrderListRespons.bean> list) {

        String isSon = UserUtils.getIsSon(context.getActivity());
        if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
            String rbac = UserUtils.getRbac(context.getActivity());
            if (!TextUtils.isEmpty(rbac)
                    && TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {
                layout_no_data.setVisibility(View.VISIBLE);
                tv_no_orders.setText("对不起，您的账号暂无\n查看已抢订单权限");
            } else {
                noData(list);
                hasData(list);
                hasDataButNotFull(list);
            }

        } else {
            noData(list);
            hasData(list);
            hasDataButNotFull(list);
        }
    }

    private void noData(List<OrderListRespons.bean> list) {
        if (list.size() == 0 && showData.size() == 0){
            layout_no_data.setVisibility(View.VISIBLE);
            layout_no_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh();
                }
            });
        }else{
            layout_no_data.setVisibility(View.GONE);
        }
    }
    private void hasData(List<OrderListRespons.bean> list) {
        if (list.size() > 0){
            showDataToList(list);
        }
    }

    private void showDataToList(List<OrderListRespons.bean> list) {
        setPageNum();
        showData.addAll(list);
        orderListAdapter.notifyDataSetChanged();
    }

    private void setPageNum(){
        if (isFromCache != null && !isFromCache ){
            try {
                int totalNumber = Integer.parseInt(totalPage);
                if(totalNumber <= 0){
                    totalNumber = 1;
                }
                if(pageNumber <= totalNumber){
                    pageNumber++;
                    pageNum = String.valueOf(pageNumber);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void hasDataButNotFull(List<OrderListRespons.bean> list) {
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

    private void successGrab() {
        goingPushin();
        refresh();
        refreshComeSuccess();
    }

    private void refreshComeSuccess(){
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET_COME_SUCCESS);
        EventbusAgent.getInstance().post(action);
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
//        NetStateManager.getNetStateManagerInstance().removeINetStateChangedListener();
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context.getActivity(), HYEventConstans.PAGE_MY_ORDER_LIST, context.stop_time - context.resume_time);
    }

    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onTitleBarClosedClicked() {
        if (tbl != null)
            tbl.setVisibility(View.GONE);
    }

    private class getOrderListRespons extends DialogCallback<OrderListRespons> {


        public getOrderListRespons(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, OrderListRespons orderListRespons, Request request, @Nullable Response response) {
            if (orderListRespons != null) {
//                if (TextUtils.equals(pageNum, "1")) {
//                    saveRespons(orderListRespons);
//                }
                saveCacheState(isFromCache);
                hasData(orderListRespons);

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
