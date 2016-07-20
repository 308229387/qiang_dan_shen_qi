package com.huangyezhaobiao.fragment.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.utils.UserConstans;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.AutoSettingsActivity;
import com.huangyezhaobiao.activity.BlankActivity;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.huangyezhaobiao.view.LoadingProgress;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.LogoutViewModel;
import com.wuba.loginsdk.external.LoginClient;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by 58 on 2016/6/17.
 */
public abstract class BaseHomeFragment extends Fragment implements TitleMessageBarLayout.OnTitleBarClickListener,INetStateChangedListener {
    private BiddingApplication app;
    protected View layout_back_head;
    protected TitleMessageBarLayout tbl;

    public static int current_index;

    protected LoadingProgress loading; //加载对话框

    protected Handler handler;

    protected long resume_time,stop_time;

    public abstract void OnFragmentSelectedChanged(boolean isSelected);


    /**
     * 刷新数据
     */
    protected abstract void loadDatas();

    /**
     * 加载更多
     */
    protected abstract void loadMore();
    @Override
    public void onTitleBarClosedClicked() {
        if(tbl!=null)
            tbl.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        app = BiddingApplication.getBiddingApplication();
        app.registerNetStateListener();
        NetStateManager.getNetStateManagerInstance().setINetStateChangedListener(this);
        if(tbl!=null){
            tbl.setVisibility(View.GONE);
            tbl.setTitleBarListener(this);
        }

        if(NetUtils.isNetworkConnected(app)){
            NetConnected();
        }else{
            NetDisConnected();
        }
        super.onResume();

        resume_time = System.currentTimeMillis();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int height = Utils.getStatusBarHeight(getActivity());
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }

    @Override
    public void onPause() {
        NetStateManager.getNetStateManagerInstance().removeINetStateChangedListener();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        if (app != null)
        app.unRegisterNetStateListener();//解除网络的变化Listener
//        app.stopTimer();//停止文件的上传
        super.onDestroy();
    }

    @Override
    public void NetConnected() {
        if(getActivity() != null && tbl!=null && tbl.getType()== TitleBarType.NETWORK_ERROR ){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tbl.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void NetDisConnected() {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tbl != null) {
                        tbl.showNetError();
                        tbl.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }


    /**
     * 对话框消失
     */
    public void stopLoading() {
        if (!getActivity().isFinishing() && loading != null && loading.isShowing()) {
            loading.dismiss();
            loading = null;
        }
    }

    /**
     * 加载效果
     */
    public void startLoading() {
        try {
            if (loading == null && getActivity()!=null) {
                loading = new LoadingProgress(getActivity(), R.style.loading);
            }
            loading.show();
        } catch (RuntimeException e) {
            if (!getActivity().isFinishing() && loading != null && loading.isShowing()) {
                loading.dismiss();
                loading = null;
            }
//			loading = null;
        }
    }

    /**
     *
     * @param mPullToRefreshListView
     */
    protected void configListViewCannotLoadMore(
            final PullToRefreshListView mPullToRefreshListView) {
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
//                            MDUtils.OrderListPageMD(orderState, "0", "0", MDConstans.ACTION_PULL_TO_REFRESH);
                        } else {
                            if (handler == null)
                                handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mPullToRefreshListView.onRefreshComplete();
                                }
                            }, 500);
                        }
                    }
                });
    }


    /**
     * 配置listView的下拉事件上拉下拉都可以
     */
    protected void configListViewRefreshListener(final PullToRefreshListView lv,final SwipeRefreshLayout srl) {
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                } else {
                    loadMore();
//                            MDUtils.servicePageMD(getActivity(), "0", "0", MDConstans.ACTION_LOAD_MORE_REFRESH);
                }
            }
        });
        final ListView listView = lv.getRefreshableView();
        if(srl!=null) {
            srl.setColorSchemeResources
                    (android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            srl.setProgressBackgroundColor(R.color.red);
            srl.setProgressViewEndTarget(true, 150);
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // 加载数据
                    if (listView.getFirstVisiblePosition() == 0) {
                        loadDatas();
                    }

                }
            });
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    View firstView = view.getChildAt(firstVisibleItem);
                    // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                    if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
                        srl.setEnabled(true);
                    } else {
                        srl.setEnabled(false);
                    }
                }
            });
        }
    }

}
