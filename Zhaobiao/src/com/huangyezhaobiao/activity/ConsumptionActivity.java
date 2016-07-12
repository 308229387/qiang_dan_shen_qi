package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.ConsumptionAdapter;
import com.huangyezhaobiao.bean.ConsumeItemBean;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.PullToRefreshListViewUtils;
import com.huangyezhaobiao.vm.ConsumeListVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/12.
 */
public class ConsumptionActivity extends QBBaseActivity implements View.OnClickListener, ListNetWorkVMCallBack {
    private SwipeRefreshLayout    srl;
    private PullToRefreshListView consume_list;
    private View                  back_layout;
    private TextView              txt_head;
    private ConsumptionAdapter    adapter;
    private View                  datas_empty_layout;
    private List<ConsumeItemBean> itemBeans;
    private ListView              listView;
    private ConsumeListVM         consumeListVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        itemBeans = new ArrayList<ConsumeItemBean>();
        consumeListVM = new ConsumeListVM(this,this);
        initView();
        initSwipeRefreshLayout();
        initListener();
        consumeListVM.refresh();
    }

    private int getLayoutId(){
        return R.layout.activity_consumption;
    }

    @Override
    public void initView() {
        layout_back_head   = getView(R.id.layout_head);
        back_layout 	   = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        txt_head    	   = getView(R.id.txt_head);
        txt_head.setText("消费记录");
        tbl                = getView(R.id.tbl);
        srl                = getView(R.id.srl);
        consume_list = getView(R.id.consume_list);
        listView           = consume_list.getRefreshableView();
        datas_empty_layout = getView(R.id.datas_empty_layout);
        adapter            = new ConsumptionAdapter(this,itemBeans);

        PullToRefreshListViewUtils.initListView(consume_list);
        configListView();
    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout://
                onBackPressed();
                break;
        }
    }



    private void configListView(){
        consume_list
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if (refreshView.isHeaderShown()) {
                        } else {
                            consumeListVM.loadMore();
                        }
                    }
                });
    }

    private void initSwipeRefreshLayout(){
        srl.setColorSchemeResources
                (android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        srl.setProgressBackgroundColor(R.color.red);
        srl.setProgressViewEndTarget(true, 150);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                consumeListVM.refresh();
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
        itemBeans = (List<ConsumeItemBean>) t;
        if(itemBeans==null || itemBeans.size()==0){
            datas_empty_layout.setVisibility(View.VISIBLE);
        }else {
            datas_empty_layout.setVisibility(View.GONE);
            adapter.refreshDatas(itemBeans);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoadingMoreSuccess(Object res) {
        itemBeans = ((List<ConsumeItemBean>)res);
        adapter.notifyDatas(itemBeans);
        adapter.notifyDataSetChanged();
        consume_list.onRefreshComplete();
    }

    @Override
    public void canLoadMore() {
        PullToRefreshListViewUtils.PullToListViewCanLoadMore(consume_list);
        configListView();
    }

    private void configListViewNotLoadMore(){
        consume_list
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if (refreshView.isHeaderShown()) {
                        } else {
                            Toast.makeText(ConsumptionActivity.this,"不能加载更多了",Toast.LENGTH_SHORT).show();
                            consume_list.onRefreshComplete();
                        }
                    }
                });
    }

    @Override
    public void loadMoreEnd() {
        PullToRefreshListViewUtils.PullToListViewCannotLoadMore(consume_list);
        configListViewNotLoadMore();
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
         consume_list.onRefreshComplete();
         Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
        srl.setRefreshing(false);
        consume_list.onRefreshComplete();
    }

    @Override
    public void onNoInterNetError() {
        stopLoading();
        srl.setRefreshing(false);
        consume_list.onRefreshComplete();
        Toast.makeText(this,"没有网络",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginInvalidate() {
        stopLoading();
        srl.setRefreshing(false);
        consume_list.onRefreshComplete();
        showExitDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_RECORD_EXPENSES, stop_time - resume_time);
    }
}
