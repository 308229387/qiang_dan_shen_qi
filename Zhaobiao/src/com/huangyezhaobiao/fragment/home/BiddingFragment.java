package com.huangyezhaobiao.fragment.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.AutoSettingsActivity;
import com.huangyezhaobiao.activity.BidFailureActivity;
import com.huangyezhaobiao.activity.BidGoneActivity;
import com.huangyezhaobiao.activity.BidSuccessActivity;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.adapter.PopAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.AccountExpireBean;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.iview.SwitchButton;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.BidListUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.PullToRefreshListViewUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.huangyezhaobiao.view.QDWaitDialog;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.GrabListViewModel;
import com.huangyezhaobiao.vm.KnockViewModel;

import java.util.List;

/**
 * Created by 58 on 2016/6/17.
 */
public class BiddingFragment extends BaseHomeFragment  implements INotificationListener,ListNetWorkVMCallBack {
    private BiddingApplication app;
    private LinearLayout ll_grab;
    private TextView txt_head;
    private SwitchButton switch_button;
    private PullToRefreshListView mPullToRefreshListView; // 抢单列表
    private SwipeRefreshLayout srl; //下拉列表
    private ListView mListView;
    private View rl_qd;//抢单等待中
    private ViewStub viewStub_no_data; //没有数据

    private PopAdapter adapter; // 列表适配

    private View root;

    private GrabListViewModel listViewModel; // 抢单列表的viewModel

    private ProgressDialog progressDialog; //抢单等待对话框
    private ZhaoBiaoDialog yueNotEnoughDialog; //余额不足通知对话框

    private KnockViewModel knockViewModel;//抢单的viewModel

    private PushToPassBean passBean;//抢单流程传递参数

    private ZhaoBiaoDialog accountExpireDialog; //网邻通过期通知
    private ZhaoBiaoDialog updateMessageDialog; //更新对话框
    private ZhaoBiaoDialog autoSettingDialog; //自定义设置

    private boolean forceUpdate;//是否强制更新

    private boolean isFirstOpen = true;


    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
        if(isSelected){
            if(isFirstOpen){
                loadDatas();
            }else{
                try {
                    if(UnreadUtils.isHasNewOrder(getActivity())){
                        UnreadUtils.clearNewOder(getActivity());
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
        if(listViewModel!= null){
            listViewModel.refresh();

            BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_BIDDING_LIAT_PAGE_MANUAL_REFRESH);
            HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_BIDDING_LIAT_PAGE_MANUAL_REFRESH);
        }
        isFirstOpen = false;

    }

    @Override
    protected void loadMore() {
        if(listViewModel!= null){
            listViewModel.loadMore();
        }
    }

    @Override
    public void onResume() {
        app = BiddingApplication.getBiddingApplication();
        app.setCurrentNotificationListener(this);
        super.onResume();
        loadDatas();
        getExpireState();
//        UnreadUtils.clearNewOder(getActivity());
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET);
        EventbusAgent.getInstance().post(action);

        HYMob.getDataList(getActivity(), HYEventConstans.INDICATOR_BIDDING_LIST_PAGE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listViewModel = new GrabListViewModel(this, getActivity());
        progressDialog = new QDWaitDialog(getActivity());
        initAccountExpireDialog();
        initAutoSettingDialog();
    }

    private void getExpireState() {
        String expireState = SPUtils.getVByK(getActivity(), GlobalConfigBean.KEY_WLT_EXPIRE);
        String expireMsg = SPUtils.getVByK(getActivity(), GlobalConfigBean.KEY_WLT_EXPIRE_MSG);
        onLoadingSuccess(new AccountExpireBean(expireState, expireMsg));
    }

    /**
     * 初始化自定义设置界面的dialog
     */
    private void initAutoSettingDialog() {

        autoSettingDialog = new ZhaoBiaoDialog(getActivity(),"是否需要进行自定义的接单设置?");
        autoSettingDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                autoSettingDialog.dismiss();
                Intent intent = AutoSettingsActivity.onNewIntent(getActivity());
                startActivity(intent);
                SPUtils.saveAutoSetting(getActivity());
            }

            @Override
            public void onDialogCancelClick() {
                autoSettingDialog.dismiss();
                SPUtils.saveAutoSetting(getActivity());
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = null;
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_binding, null);
            ll_grab = (LinearLayout) view.findViewById(R.id.ll_grab);
            ll_grab.setVisibility(View.VISIBLE);
            txt_head = (TextView) view.findViewById(R.id.txt_head);
            txt_head.setText("抢单");
            switch_button = (SwitchButton) view.findViewById(R.id.switch_button);
            tbl = (TitleMessageBarLayout) view.findViewById(R.id.tbl);

            viewStub_no_data = (ViewStub) view.findViewById(R.id.viewStub_no_data);

            srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
            adapter = new PopAdapter(getActivity(), adapterListener);
            mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.mainlist);
            mListView = mPullToRefreshListView.getRefreshableView();
            mListView.setDividerHeight((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

            rl_qd = view.findViewById(R.id.rl_qd);

            layout_back_head = view.findViewById(R.id.layout_head);
            PullToRefreshListViewUtils.initListView(mPullToRefreshListView);

            configListViewRefreshListener(mPullToRefreshListView, srl);//配置PullToRefreshListView,swipeRefreshLayout

            initEvent();

        }else{
            ((FrameLayout)view.getParent()).removeView(view);
        }

        return view ;
    }

   private void initEvent(){

       if("1".equals(SPUtils.getServiceState(getActivity()))){
           switch_button.setChecked(true);//选中服务模式
       }else{
           switch_button.setChecked(false);//选中休息模式
       }
       switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                   StateUtils.state = 1; //服务模式
                   BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_SERVICE_MODE);
                   HYMob.getDataListByModel(getActivity(), HYEventConstans.EVENT_ID_CHANGE_MODE);
               } else {
                   StateUtils.state = 2; //休息模式
                   BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_REST_MODE);
                   //点击事件埋点
                   HYMob.getDataListByModel(getActivity(), HYEventConstans.EVENT_ID_CHANGE_MODE);
               }

               SPUtils.setServiceState(getActivity(), StateUtils.state + "");
               loadDatas();
           }
       });
   }


    public void canLoad() {
        PullToRefreshListViewUtils.PullToListViewCanLoadMore(mPullToRefreshListView);
        configListViewRefreshListener(mPullToRefreshListView, srl);
    }

    public void canNotLoad() {
        PullToRefreshListViewUtils.PullToListViewCannotLoadMore(mPullToRefreshListView);
        configListViewCannotLoadMore(mPullToRefreshListView);
    }


    /**
     * ListNetWorkVMCallBack 回调
     */
    @Override
    public void onRefreshSuccess(Object t) {
        srl.setRefreshing(false);
        stopLoading();
        List<QDBaseBean> list = (List<QDBaseBean>) t;
        adapter.refreshSuccess(list);
        mPullToRefreshListView.onRefreshComplete();
        if (null == list || list.size() == 0) {
            //加载ViewStub
            if (root == null) {
                root = viewStub_no_data.inflate();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadDatas();
                    }
                });
            }
            root.setVisibility(View.VISIBLE);

        } else {
            if (root != null) {
                root.setVisibility(View.GONE);
            }
        }
        //为获取下一页列表数据添加方法
        QDBaseBean bean;
        if (null != list && list.size() > 0) {
            bean = list.get(list.size() - 1);
            BidListUtils.bidId = bean.getBidId();
            BidListUtils.pushId = bean.getPushId();
            BidListUtils.bidState = bean.getBidState();
        }
        //判断是否可以继续加载更多
        if (null == list || list.size() == 0 || list.size() % 20 != 0) {
            canNotLoad();
        } else {
            canLoad();
        }
    }

    @Override
    public void onLoadingMoreSuccess(Object res) {
        List<QDBaseBean> list = (List<QDBaseBean>) res;
        LogUtils.LogV("loadMore1", "" + list.size());
        adapter.loadMoreSuccess(list);
        mPullToRefreshListView.onRefreshComplete();
        stopLoading();
        //为获取下一页列表数据添加方法
        QDBaseBean bean;
        if (null != list && list.size() > 0) {
            bean = list.get(list.size() - 1);
            BidListUtils.bidId = bean.getBidId();
            BidListUtils.pushId = bean.getPushId();
            BidListUtils.bidState = bean.getBidState();
        }
        //判断是否可以继续加载更多
        if (null == list || list.size() == 0 || list.size() % 20 != 0) {
            LogUtils.LogV("loadMore2",""+list.size());
            canNotLoad();
        } else {
            LogUtils.LogV("loadMore3",""+list.size());
            canLoad();
        }
    }

    @Override
    public void canLoadMore() {

    }

    @Override
    public void loadMoreEnd() {

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
        if (t instanceof AccountExpireBean) {
            AccountExpireBean accountExpireBean = (AccountExpireBean) t;
            String expireState = accountExpireBean.getExpireState();
            String message = accountExpireBean.getMsg();
            if (!TextUtils.isEmpty(expireState) && TextUtils.equals("1",expireState)&& !TextUtils.isEmpty(message)) {
                    //网灵通过期
                if(accountExpireDialog!= null){
                    accountExpireDialog.setMessage(message);
                    accountExpireDialog.show();
                }


            }
        }else if (t instanceof Integer) {
            stopLoading();
            int status = (Integer) t;
//            Toast.makeText(getActivity(), "status:" + status, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("passBean", passBean);
            intent.putExtras(bundle);
            dismissQDWaitDialog();

            if (status == 3) {//抢单成功
                intent.setClass(getActivity(), BidSuccessActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "抢单成功", Toast.LENGTH_SHORT).show();
            } else if (status == 1) {
                intent.setClass(getActivity(), BidGoneActivity.class);
                startActivity(intent);
            } else if (status == 2) {
                yueNotEnoughDialog = new ZhaoBiaoDialog(getActivity(),getString(R.string.not_enough_balance));
                yueNotEnoughDialog.setCancelButtonGone();
                yueNotEnoughDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        yueNotEnoughDialog = null;
                    }
                });
                yueNotEnoughDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
                    @Override
                    public void onDialogOkClick() {
                        if (yueNotEnoughDialog != null) {
                            yueNotEnoughDialog.dismiss();
                        }

                    }

                    @Override
                    public void onDialogCancelClick() {
                    }
                });
                yueNotEnoughDialog.show();
                if (passBean != null) {
                    MDUtils.YuENotEnough(passBean.getCateId() + "", passBean.getBidId() + "");
                }
            } else if (status == 4) {
                Toast.makeText(getActivity(), getString(R.string.bidding_already_bid), Toast.LENGTH_SHORT).show();
            } else if (status == 5) {//抢单失败
                intent.setClass(getActivity(), BidFailureActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "抢单并没有成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.bidding_exception), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 初始化网邻通过期的dialog
     */
    private void initAccountExpireDialog(){
        accountExpireDialog = new ZhaoBiaoDialog(getActivity(), "");
        accountExpireDialog.setCancelable(false);
        accountExpireDialog.setCancelButtonGone();
        accountExpireDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                accountExpireDialog = null;
            }
        });
        accountExpireDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                    accountExpireDialog.dismiss();

            }

            @Override
            public void onDialogCancelClick() {

            }
        });
    }

    @Override
    public void onLoadingError(String msg) {
        mPullToRefreshListView.onRefreshComplete();
        if (srl != null && srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
        if(getActivity() != null){
            stopLoading();
        }

        //加载ViewStub
        if (root == null) {
            root = viewStub_no_data.inflate();
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadDatas();
                }
            });
        }
        root.setVisibility(View.VISIBLE);

        try {
            if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
                MainActivity ola = (MainActivity) getActivity();
                ola.onLoadingError(msg);
            }else if (getActivity() != null && !TextUtils.isEmpty(msg)) {
               ToastUtils.showToast(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismissQDWaitDialog();
    }

    @Override
    public void onLoadingCancel() {
        mPullToRefreshListView.onRefreshComplete();
        stopLoading();
    }

    @Override
    public void onNoInterNetError() {
        mPullToRefreshListView.onRefreshComplete();
        Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        stopLoading();
        dismissQDWaitDialog();
    }

    @Override
    public void onVersionBack(String version) {

       if(getActivity() != null && !UserUtils.isNeedUpdate(getActivity())){ //判断是否强制更新
           String versionCode = "";
           int currentVersion = -1; //当前版本号
           int versionNum = -1;
           //获取当前系统版本号
           try {
               currentVersion = Integer.parseInt(VersionUtils.getVersionCode(getActivity()));
           } catch (Exception e) {

           }
           if (currentVersion == -1) return;

           //当前是MainActivity，获取服务器header返回的版本号
           if (version != null) {
               if (version.contains("F")) {
                   forceUpdate = true;
                   String[] fs = version.split("F");
                   versionCode = fs[0];
                   try {
                       versionNum = Integer.parseInt(versionCode);
                   } catch (Exception e) {

                   }
               }else{
                   try {
                       versionNum = Integer.parseInt(version);
                   } catch (Exception e) {

                   }
               }

               if (versionNum == -1) {
                   return;
               }

               UpdateManager.getUpdateManager().isUpdateNow(getActivity(), versionNum, currentVersion, URLConstans.DOWNLOAD_ZHAOBIAO_ADDRESS, forceUpdate);
//          UpdateManager.getUpdateManager().isUpdateNow(this, versionNum, currentVersion, "http://10.252.23.45:8001/2.7.0_zhaobiao.apk", forceUpdate);
               Boolean flag = UpdateManager.needUpdate;
               Log.v("www", "flag:" + flag);
               if (!flag) {
                   //判断是不是第一次进入主界面
                   showFirst();
               }
           }
       }
    }


    @Override
    public void onLoginInvalidate() {
        stopLoading();
        MainActivity ola = (MainActivity) getActivity();
        ola.onLoginInvalidate();
    }

    /**
     * 第一次登录时的提示
     */
    private void showFirst() {
//      if(!SPUtils.getAppUpdate(this)){
        if (SPUtils.isFirstUpdate(getActivity())) {//需要弹窗
            if (updateMessageDialog == null) {
                updateMessageDialog = new ZhaoBiaoDialog(getActivity(),
//                    getString(R.string.update_hint),
                        getString(R.string.update_message));
                updateMessageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateMessageDialog = null;
                        //弹自定义界面的弹
                        if (SPUtils.isAutoSetting(getActivity())) {
                            autoSettingDialog.show();
                        }
                    }
                });
                updateMessageDialog.setCancelable(false);
                updateMessageDialog.setCancelButtonGone();
                updateMessageDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
                    @Override
                    public void onDialogOkClick() {
                        updateMessageDialog.dismiss();
                        SPUtils.saveAlreadyFirstUpdate(getActivity(), false);
//                    UserUtils.setAppVersion(MainActivity.this, ""); //2.7升级可删
//                       SPUtils.setAppUpdate(MainActivity.this, true);
                    }

                    @Override
                    public void onDialogCancelClick() {
                    }
                });
                updateMessageDialog.show();

            }
        } else {
            if (SPUtils.isAutoSetting(getActivity())) {
                autoSettingDialog.show();
            }
        }


    }

    /**
     * adapter的回调监听
     */
  ZBBaseAdapter.AdapterListener adapterListener = new ZBBaseAdapter.AdapterListener() {
      @Override
      public void onAdapterRefreshSuccess() {
          mPullToRefreshListView.setAdapter(adapter);
      }

      @Override
      public void onAdapterLoadMoreSuccess() {
          adapter.notifyDataSetChanged();
      }

      @Override
      public void onAdapterViewClick(int id, PushToPassBean bean) {
          //点击了抢单
          BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_BIDDING_LIST_PAGE_BIDDING);

          HYMob.getDataListForQiangdan(getActivity(), HYEventConstans.EVENT_ID_BIDDING_LIST_PAGE_BIDDING, String.valueOf(bean.getBidId()), "1");

          passBean = bean;
          knockViewModel = new KnockViewModel(BiddingFragment.this, getActivity());
          knockViewModel.knock(bean, AppConstants.BIDSOURCE_LIST);

          rl_qd.setVisibility(View.VISIBLE);
      }
  };


    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    private void dismissQDWaitDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        rl_qd.setVisibility(View.GONE);
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        LogUtils.LogV("nnnnnnB1a", String.valueOf(pushBean.getTag()));
        if (null != pushBean) {
            int type = pushBean.getTag();
            LogUtils.LogV("nnnnnnB1b", String.valueOf(pushBean.getTag())+ StateUtils.getState(getActivity()) );
            if (type == 100 && StateUtils.getState(getActivity()) == 1) {
                LogUtils.LogV("nnnnnnB1c", String.valueOf(pushBean.getTag()));
                Intent intent = new Intent(getActivity(), PushInActivity.class);
                startActivity(intent);
            } else {
                LogUtils.LogV("nnnnnnB1d", String.valueOf(pushBean.getTag()));
                tbl.setPushBean(pushBean);
                tbl.setVisibility(View.VISIBLE);
                PushUtils.pushList.clear();
            }
        } else {
            Toast.makeText(getActivity(), "实体bean为空", Toast.LENGTH_SHORT).show();
        }

        if (listViewModel != null){
            loadDatas();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(getActivity(), HYEventConstans.PAGE_BINGING_LIST, stop_time - resume_time);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (app != null)
            app.removeINotificationListener();

        if (getActivity() != null && !getActivity().isFinishing() && accountExpireDialog != null && accountExpireDialog.isShowing()) {
            accountExpireDialog.dismiss();
            accountExpireDialog = null;
        }
        if (getActivity() != null && !getActivity().isFinishing() && yueNotEnoughDialog != null && yueNotEnoughDialog.isShowing()) {
            yueNotEnoughDialog.dismiss();
            yueNotEnoughDialog = null;
        }
    }
}
