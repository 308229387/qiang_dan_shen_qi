package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.SystemNotiAdapter;
import com.huangyezhaobiao.bean.SysListBean;
import com.huangyezhaobiao.presenter.SystemNotiListPresenter;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.PullToRefreshListViewUtils;
import com.huangyezhaobiao.vm.SystemNotiListVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/15.
 * 系统推送的列表页
 */
public class SystemNotiListActivity extends  QBBaseActivity implements View.OnClickListener, ListNetWorkVMCallBack {
    private View                    back_layout;
    private TextView                txt_head;
    private SwipeRefreshLayout      srl;
    private PullToRefreshListView   sys_list;
    private ListView                listView;
    private View                    rl_no_unread;
    private SystemNotiListVM        systemNotiListVM;
    private SystemNotiListPresenter systemNotiListPresenter;
    private List<SysListBean>       sysListBeans = new ArrayList<SysListBean>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initSwipeRefreshLayout();
        systemNotiListVM = new SystemNotiListVM(this,this);
        systemNotiListPresenter = new SystemNotiListPresenter(this);
        systemNotiListVM.refresh();
        systemNotiListPresenter.initNotiAdapter(sysListBeans);
        listView.setAdapter(systemNotiListPresenter.getSysNotiAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - 1;
                if (position >= 0) {
                    SysListBean listBean = ((SystemNotiAdapter) systemNotiListPresenter.getSysNotiAdapter()).getDataSources().get(position);
                    systemNotiListPresenter.handleClick(listBean);
                }
            }
        });

    }


    @Override
    public void initView() {
        setContentView(getLayoutId());
        layout_back_head = getView(R.id.layout_head);
        back_layout      = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        txt_head         = getView(R.id.txt_head);
        txt_head.setText("通知资讯");
        tbl              = getView(R.id.tbl);
        srl              = getView(R.id.srl);
        sys_list         = getView(R.id.sys_list);
        rl_no_unread = getView(R.id.rl_no_unread);
        listView         = sys_list.getRefreshableView();
        PullToRefreshListViewUtils.initListView(sys_list);
        configListView();
    }


    private void configListView(){
        sys_list
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if (refreshView.isHeaderShown()) {
                        } else {
                            systemNotiListVM.loadMore();
                        }
                    }
                });
    }
    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
    }


    private int getLayoutId(){
        return R.layout.activity_sys_noti_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
        }
    }


    private void initSwipeRefreshLayout(){
        srl.setColorSchemeResources
                (android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        srl.setProgressBackgroundColor(R.color.red);
        srl.setProgressViewEndTarget(true, 150);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                systemNotiListVM.refresh();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
                    srl.setEnabled(true);
                } else {
                    srl.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onRefreshSuccess(Object t) {
        srl.setRefreshing(false);
        if(t instanceof List){
            List<SysListBean> listBeans = (List<SysListBean>) t;
            if(listBeans.size()==0){
                rl_no_unread.setVisibility(View.VISIBLE);
            }else {
                rl_no_unread.setVisibility(View.GONE);
                systemNotiListPresenter.initNotiAdapter(listBeans);
                listView.setAdapter(systemNotiListPresenter.getSysNotiAdapter());
            }
        }
    }

    @Override
    public void onLoadingMoreSuccess(Object res) {
        sys_list.onRefreshComplete();
        List<SysListBean> listBeans = (List<SysListBean>) res;
        Log.e("shenzhixin","size:"+listBeans.size());
        systemNotiListPresenter.refreshDatas(listBeans);
        systemNotiListPresenter.notifyData();

    }

    @Override
    public void canLoadMore() {//还可以加载
        PullToRefreshListViewUtils.PullToListViewCanLoadMore(sys_list);
        configListView();
    }

    @Override
    public void loadMoreEnd() {//不能加载更多啦
        PullToRefreshListViewUtils.PullToListViewCannotLoadMore(sys_list);
        configListViewNotLoadMore();
    }

    private void configListViewNotLoadMore(){
        sys_list
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if (refreshView.isHeaderShown()) {
                        } else {
                            sys_list.onRefreshComplete();
                            Toast.makeText(SystemNotiListActivity.this, "不能加载更多了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {
        stopLoading();
    }

    @Override
    public void onLoadingError(String msg) {
        stopLoading();
        srl.setRefreshing(false);
        sys_list.onRefreshComplete();
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
        srl.setRefreshing(false);
        sys_list.onRefreshComplete();
    }

    @Override
    public void onNoInterNetError() {
        stopLoading();
        srl.setRefreshing(false);
        sys_list.onRefreshComplete();
        Toast.makeText(this,"没有网络了",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginInvalidate() {
        stopLoading();
        srl.setRefreshing(false);
        sys_list.onRefreshComplete();
        showExitDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_SYSTEM_NOTICE, stop_time - resume_time);
    }
}
