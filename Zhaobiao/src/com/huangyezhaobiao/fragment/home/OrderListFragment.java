package com.huangyezhaobiao.fragment.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.FetchDetailsActivity;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.adapter.OrderLVAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.iview.OrderCatePopupWindow;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.PullToRefreshListViewUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.vm.QiangDanListViewModel;
import com.huangyezhaobiao.vm.YuEViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 58 on 2016/6/18.
 */
public class OrderListFragment extends  BaseHomeFragment implements INotificationListener,ListNetWorkVMCallBack {
    private BiddingApplication app;
    private TextView txt_head;
    private ImageView btn_clean;

    private OrderCatePopupWindow mOrderCateWindow; //筛选框

    private PullToRefreshListView lv_all_fragment;
    private ListView lv;
    private View layout_no_data; //您还没有已抢订单呢！
    private TextView tv_no_orders;
    private SwipeRefreshLayout srl;
    private OrderLVAdapter adapter;

    private  QiangDanListViewModel lvm;
    private YuEViewModel yuEViewModel;

    private boolean isLoadFirst;

    public static String orderState ="0"; //根据筛选器得到订单状态 待服务1,服务中2,已服务3

    public final static  List<String> checkedId = new ArrayList<>();

    private boolean isFirstOpen = true;

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
          if(isSelected){
              if(isFirstOpen){
                  loadDatas();
                  if(yuEViewModel != null){
                      yuEViewModel.getBalance();
                  }
              }else{

                  try {
                      if(UnreadUtils.isHasQDResult(getActivity())){
                          UnreadUtils.clearQDResult(getActivity());
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }

                  EventAction action = new EventAction(EventType.EVENT_TAB_RESET);
                  EventbusAgent.getInstance().post(action);
              }
          }
    }

    @Override
    protected void loadDatas() {
        if(lvm != null){
            lvm.refresh();
        }
        isFirstOpen =false;

    }

    @Override
    protected void loadMore() {
        if(lvm != null){
            lvm.loadMore();
        }

    }

    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onResume() {
        app = BiddingApplication.getBiddingApplication();
        app.setCurrentNotificationListener(this);
        super.onResume();

        //我的订单中心
        BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_MY_BIDDING);
        HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_MY_BIDDING);

        loadDatas();
        if(yuEViewModel != null){
            yuEViewModel.getBalance();
        }
//        UnreadUtils.clearQDResult(getActivity());
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET);
        EventbusAgent.getInstance().post(action);

        HYMob.getDataList(getActivity(), HYEventConstans.INDICATOR_ORDER_PAGE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvm = new QiangDanListViewModel(this, getActivity());
        yuEViewModel = new YuEViewModel(this,getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = null;
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_order_list, null);
            layout_back_head = view.findViewById(R.id.layout_back);
            tbl = (TitleMessageBarLayout) view.findViewById(R.id.tbl);

            txt_head = (TextView) view.findViewById(R.id.txt_head);
            txt_head.setText(R.string.my_bidding);
            btn_clean = (ImageView) view.findViewById(R.id.btn_clean);
            btn_clean.setVisibility(View.VISIBLE);
            btn_clean.setOnClickListener(listener);
            srl             = (SwipeRefreshLayout) view.findViewById(R.id.srl);
            srl.setRefreshing(true);
            lv_all_fragment = (PullToRefreshListView) view.findViewById(R.id.lv_all_fragment);
            lv = lv_all_fragment.getRefreshableView();
            layout_no_data = view.findViewById(R.id.layout_no_data);
            layout_no_data.setVisibility(View.GONE);
            tv_no_orders = (TextView) view.findViewById(R.id.tv_no_orders);
//            layout_no_internet_click = view.findViewById(R.id.layout_no_internet_click_refresh);

            adapter = new OrderLVAdapter(getActivity(),adapterListener);

            configRefreshableListView(lv_all_fragment);//配置pullLv
            configListViewRefreshListener(lv_all_fragment,srl);//配置PullToRefreshListView,swipeRefreshLayout

            lv.setAdapter(adapter);
            lv.setDividerHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
            if (lv != null) {
                lv.setOnItemClickListener(itemClickListener);
            }
//            layout_no_internet_click.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ActivityUtils.goToActivity(getActivity(), BiddingFragment.class);
//                }
//            });

            layout_no_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadDatas();
                }
            });

        }else{
            ((FrameLayout)view.getParent()).removeView(view);
        }

        return view ;
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mOrderCateWindow !=null && mOrderCateWindow.isShowing()){
                LogUtils.LogV("tag", "正在关闭");
                mOrderCateWindow.dismiss();
                loadDatas();
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_FILTER);
            } else {
                LogUtils.LogV("tag", "正在打开");
                mOrderCateWindow = new OrderCatePopupWindow(getActivity(),R.layout.order_cate_popup,popListener,orderState);
                mOrderCateWindow.showAsDropDown(btn_clean, 0, 15);
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_FILTER);
            }
        }
    };

    OrderCatePopupWindow.OnOrderCatePopupWindowItemClick popListener =  new OrderCatePopupWindow.OnOrderCatePopupWindowItemClick() {
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
                        loadDatas();
                    }
                    HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_FILTER_CONFIRM);

                    break;
            }


        }
    };

    /**
     * 配置上拉刷新的lv
     *
     * @param mPullToRefreshListView
     */
    public void configRefreshableListView(
            PullToRefreshListView mPullToRefreshListView) {
        /** 初始化抢单列表 */
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        // 设置下拉刷新时的提示文本设置
        mPullToRefreshListView.getLoadingLayoutProxy(true, false)
                .setLastUpdatedLabel("");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(
                "下拉刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false)
                .setRefreshingLabel("正在刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false)
                .setReleaseLabel("放开以刷新");
        // 上拉加载更多时的提示文本设置
        mPullToRefreshListView.getLoadingLayoutProxy(false, true)
                .setLastUpdatedLabel("");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
                "上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true)
                .setRefreshingLabel("正在加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true)
                .setReleaseLabel("放开以加载");
    }


    ZBBaseAdapter.AdapterListener adapterListener = new ZBBaseAdapter.AdapterListener() {
        @Override
        public void onAdapterRefreshSuccess() {
            if (lv != null && adapter != null) {
//                setListViewWithAnimation(lv_all_fragment.getRefreshableView(),adapter);
                lv.setAdapter(adapter);
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


    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivityUtils.goToActivity(getActivity(), FetchDetailsActivity.class);

        }
    };

    /**
     * 配置listView的下拉事件上拉下拉都可以
     */
    protected void configListViewRefreshListener(
            PullToRefreshListView mPullToRefreshListView) {
        mPullToRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if (refreshView.isHeaderShown()) {
                            String label = DateUtils.formatDateTime(
                                    getActivity(), System.currentTimeMillis(),
                                    DateUtils.FORMAT_SHOW_TIME
                                            | DateUtils.FORMAT_SHOW_DATE
                                            | DateUtils.FORMAT_ABBREV_ALL);
                            refreshView.getLoadingLayoutProxy()
                                    .setLastUpdatedLabel(label);
                            loadDatas();
                            MDUtils.servicePageMD(getActivity(), "0", "0",
                                    MDConstans.ACTION_PULL_TO_REFRESH);
                        } else {
                            loadMore();
                            MDUtils.servicePageMD(getActivity(), "0", "0",
                                    MDConstans.ACTION_LOAD_MORE_REFRESH);
                        }
                    }
                });
    }



    /**
     * ListNetWorkVMCallBack 回调
     * @param t
     */
    @Override
    public void onRefreshSuccess(Object t) {
        List<QDBaseBean> beans = (List<QDBaseBean>) t;
        adapter.refreshSuccess(beans);
        lv_all_fragment.onRefreshComplete();
        srl.setRefreshing(false);

        String isSon = UserUtils.getIsSon(getActivity());
        if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
            String rbac = UserUtils.getRbac(getActivity());
            if (!TextUtils.isEmpty(rbac)
                    && TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {
                lv.setVisibility(View.GONE);
                layout_no_data.setVisibility(View.VISIBLE);
                tv_no_orders.setText("对不起，您的账号\n暂无查看已抢订单权限");
            }

        } else {

            if (beans.size() > 0) {
                lv.setVisibility(View.VISIBLE);
                layout_no_data.setVisibility(View.GONE);

            } else {
                lv.setVisibility(View.GONE);
                layout_no_data.setVisibility(View.VISIBLE);
//            TextView tv = (TextView) ((ViewGroup)layout_no_data).getChildAt(1);
//            tv.setText("您还没有订单");
            }
        }
    }

    @Override
    public void onLoadingMoreSuccess(Object res) {
        List<QDBaseBean> beans = (List<QDBaseBean>) res;
        adapter.loadMoreSuccess(beans);
        lv_all_fragment.onRefreshComplete();
    }

    @Override
    public void canLoadMore() {
        PullToRefreshListViewUtils.PullToListViewCanLoadMore(lv_all_fragment);
        configListViewRefreshListener(lv_all_fragment);
    }

    @Override
    public void loadMoreEnd() {
        PullToRefreshListViewUtils.PullToListViewCannotLoadMore(lv_all_fragment);
        configListViewCannotLoadMore(lv_all_fragment);
    }

    /**
     * NetWorkVMCallBack 回调
     */
    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {

        if (t instanceof Map<?, ?>) {
            Map<String, String> maps = (Map<String, String>) t;
            String userPhone = maps.get("phone");
            SPUtils.saveKV(getActivity(), GlobalConfigBean.KEY_USERPHONE, userPhone);
        }
        stopLoading();
    }

    @Override
    public void onLoadingError(String msg) {
        if(getActivity() != null){
            stopLoading();
        }

        if(lv_all_fragment!=null &&lv_all_fragment.isRefreshing())
            lv_all_fragment.onRefreshComplete();

        lv.setVisibility(View.GONE);
        layout_no_data.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
            MainActivity ola = (MainActivity) getActivity();
            ola.onLoadingError(msg);
        }

//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
        if(lv_all_fragment!=null &&lv_all_fragment.isRefreshing())
            lv_all_fragment.onRefreshComplete();
    }

    @Override
    public void onNoInterNetError() {
        if(lv_all_fragment!=null &&lv_all_fragment.isRefreshing())
            lv_all_fragment.onRefreshComplete();
//        Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginInvalidate() {
        stopLoading();
        MainActivity ola = (MainActivity) getActivity();
        ola.onLoginInvalidate();
    }

    @Override
    public void onVersionBack(String version) {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoadFirst) {
            // 设置orderState
           loadDatas();
            yuEViewModel.getBalance();
            isLoadFirst = true;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLoading();
        if (app != null)
            app.removeINotificationListener();
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        LogUtils.LogV("nnnnnnB3a", String.valueOf(pushBean.getTag()));
        if (null != pushBean) {
            LogUtils.LogV("nnnnnnB3b", String.valueOf(pushBean.getTag())+ StateUtils.getState(getActivity()) );
            int type = pushBean.getTag();
            if (type == 100 && StateUtils.getState(getActivity()) == 1) {
                LogUtils.LogV("nnnnnnB3c", String.valueOf(pushBean.getTag()));
                Intent intent = new Intent(getActivity(), PushInActivity.class);
                startActivity(intent);
            } else {
                LogUtils.LogV("nnnnnnB3d", String.valueOf(pushBean.getTag()));
                tbl.setPushBean(pushBean);
                tbl.setVisibility(View.VISIBLE);
                PushUtils.pushList.clear();
            }
        } else {
            Toast.makeText(getActivity(), "实体bean为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(getActivity(), HYEventConstans.PAGE_MY_ORDER_LIST, stop_time - resume_time);
    }
}
