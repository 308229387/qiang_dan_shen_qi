package wuba.zhaobiao.message.model;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OtherDetailActivity;
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.activity.SystemNotiListActivity;
import com.huangyezhaobiao.adapter.MessageSeriesAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.db.DataBaseManager;
import com.huangyezhaobiao.enums.PopTypeEnum;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.vm.CenterMessageStorageViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.message.fragment.MessageFragment;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/8/16.
 */
public class MessageModel extends BaseModel implements TitleMessageBarLayout.OnTitleBarClickListener {

    private MessageFragment context;
    private View view;
    private View layout_back_head;
    private View back_layout;
    private TextView txt_head;
    private TitleMessageBarLayout tbl;

    private ListView lv_message_center;
    private MessageSeriesAdapter adapter;

    private List<PushToStorageBean> messageBeans = new ArrayList<>();
    private List<PushToStorageBean> qiangDan = new ArrayList<>();
    private List<PushToStorageBean> daoJiShi = new ArrayList<>();
    private List<PushToStorageBean> sysNoti = new ArrayList<>();

    private BiddingApplication app;

    private CenterMessageStorageViewModel vm;

    public MessageModel(MessageFragment context) {
        this.context = context;
    }

    public void createView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
    }

    public void initView() {
        initTopBar();
        initListView();
        setListViewListener();
    }

    private void initTopBar() {
        layout_back_head = view.findViewById(R.id.layout_head);
        back_layout = view.findViewById(R.id.back_layout);
        txt_head = (TextView) view.findViewById(R.id.txt_head);
        txt_head.setText("我的消息");
        tbl = (TitleMessageBarLayout) view.findViewById(R.id.tbl);
    }

    private void initListView() {
        lv_message_center = (ListView) view.findViewById(R.id.lv_message_center);
    }


    private void setListViewListener() {
        lv_message_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                int[] count = new int[]{qiangDan.size(), daoJiShi.size(), sysNoti.size()};
                switch (position + 1) {
                    case 1:
                        clearQDResult();
                        postEventBusForClear();
                        goToQDDetailActivity();
                        goToQDDetailStatistics();
                        break;
                    case 2:
                        clearDaojishiResult();
                        postEventBusForClear();
                        goToDaojishiDetailActivity();
                        goToDaojishiDetailStatistics();
                        break;
                    case 3:
                        clearSysNotiNum();
                        postEventBusForClear();
                        goToSysDetailActivity();
                        goToSystemDetailStatistics();
                        break;
                }

            }
        });
    }

    private void clearQDResult() {
        UnreadUtils.clearQDResult(context.getActivity());
    }

    private void postEventBusForClear() {
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET);
        EventbusAgent.getInstance().post(action);
    }

    private void goToQDDetailActivity() {
        Map<String, Integer> maps = new HashMap<>();
        maps.put("type", DataBaseManager.TABLE_OTHER.KOUFEI);
        ActivityUtils.goToActivityWithInteger(
                context.getActivity(), OtherDetailActivity.class, maps);
    }

    private void goToQDDetailStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_RESULT_ALERT);
    }

    private void clearDaojishiResult() {
        UnreadUtils.clearDaoJiShiResult(context.getActivity());
    }

    private void goToDaojishiDetailActivity() {
        Map<String, Integer> maps = new HashMap<>();
        maps.put("type", DataBaseManager.TABLE_OTHER.DAOJISHI);
        ActivityUtils.goToActivityWithInteger(context.getActivity(), OtherDetailActivity.class, maps);
    }

    private void goToDaojishiDetailStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVETN_ID_CONTACT_CLIENT);
    }

    private void clearSysNotiNum() {
        UnreadUtils.clearSysNotiNum(context.getActivity());
    }

    private void goToSysDetailActivity() {
        ActivityUtils.goToActivity(context.getActivity(), SystemNotiListActivity.class);
    }

    private void goToSystemDetailStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_SYSTEM_NOTICE);
    }


    public void createAdapter() {
        adapter = new MessageSeriesAdapter(context.getActivity());
    }

    public void registerMessageBar() {
        app = BiddingApplication.getBiddingApplication();
        app.registerNetStateListener();
        NetStateManager.getNetStateManagerInstance().add(context);
        initMessageBar();
    }

    private void initMessageBar(){
        if (tbl != null) {
            tbl.setVisibility(View.GONE);
            tbl.setTitleBarListener(this);
        }
    }

    public void registerListener() {
        app.setCurrentNotificationListener(context);
//        NetStateManager.getNetStateManagerInstance().setINetStateChangedListener(context);
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

    public void notifyDatasWithoutUnread() {
        messageBeans.clear();
        operateList(qiangDan);
        operateContactList(daoJiShi);
        operateSysList(sysNoti);
        adapter.setDataSources(messageBeans);
        lv_message_center.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void MessageClickedStatistics() {
        HYMob.getDataList(context.getActivity(), HYEventConstans.INDICATOR_MESSAGE_PAGE);
    }

    private void operateList(List<PushToStorageBean> bean) {
        if (bean.size() > 0) {
            PushToStorageBean single = bean.get(bean.size() - 1);
            messageBeans.add(single);
        } else {
            PushToStorageBean bean2 = new PushToStorageBean("暂无消息", "");
            messageBeans.add(bean2);
        }
    }

    private void operateContactList(List<PushToStorageBean> bean) {
        if (bean.size() > 0) {
            PushToStorageBean single = bean.get(bean.size() - 1);
            messageBeans.add(single);
        } else {
            PushToStorageBean bean2 = new PushToStorageBean("暂无消息", "");
            messageBeans.add(bean2);
        }
    }

    private void operateSysList(List<PushToStorageBean> bean) {
        if (bean.size() > 0) {
            PushToStorageBean single = bean.get(bean.size() - 1);
            messageBeans.add(single);
        } else {
            PushToStorageBean bean2 = new PushToStorageBean("点击查看", "");
            messageBeans.add(bean2);
        }
    }

    public View getView() {
        return view;
    }

    public void load() {  //频繁创建新线程导致OOM

        vm = new CenterMessageStorageViewModel(context.getActivity(), context);
        vm.getDate();

    }


    public void getDataSuccess(final Object o) {
        if (context.getActivity() != null) {
            context.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clearData();
                    ArrayList<PushToStorageBean> bean = (ArrayList<PushToStorageBean>) o;
                    if (bean != null) {
                        for (PushToStorageBean bean2 : bean) {
                            pickWithBeanType(bean2);
                        }
                    }
                    notifyDatasWithoutUnread();
                }
            });
        }
    }

    private void clearData() {
        qiangDan.clear();
        daoJiShi.clear();
        sysNoti.clear();
    }

    /**
     * 根据type,加入不同的list,主要为了显示未读数字
     *
     * @param type
     */
    private void pickWithBeanType(PushToStorageBean type) {
        int aType = Integer.valueOf(type.getTag());
        switch (PopTypeEnum.getPopType(aType)) {
            case ORDERRESULT:// 抢单结果提醒
                qiangDan.add(type);
                break;
            case COUNTDOWN:// 客户联系提醒
                daoJiShi.add(type);
                break;
            case SYSTEMMESSAGE:// 通知资讯
                sysNoti.add(type);
                break;
            default:
                break;
        }
    }

    public void getDataFailure() {
        if (context.getActivity() != null) {
            context.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(context.getString(R.string.app_exception));
                }
            });

        }
    }


    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onTitleBarClosedClicked() {
        if (tbl != null)
            tbl.setVisibility(View.GONE);
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

    public void diaplayMessageBar(){
        if (tbl != null) {
            tbl.showNetError();
            tbl.setVisibility(View.VISIBLE);
        }
    }

    private void NetConnected() {
        context.NetConnected();
    }

    public void closeMessageBar(){
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
            messageLogout();
        } else if (type != 100 && type != 105) {
//            tbl.setPushBean(pushBean);
//            tbl.setVisibility(View.VISIBLE);
//            PushUtils.pushList.clear();
            load();
        }
    }

    private void dealWhitData() {
        String isSon = UserUtils.getIsSon(context.getActivity());
        if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
            rightInfo();
        } else {
            goingPushin();
            refreshComeSuccess();
        }
    }

    private void rightInfo() {
        String rbac = UserUtils.getRbac(context.getActivity());
        if (!TextUtils.isEmpty(rbac)
                && TextUtils.equals("1", rbac) || TextUtils.equals("5", rbac)) {
        } else {
            goingPushin();
            refreshComeSuccess();
        }
    }


    private void goingPushin() {
        Intent intent = new Intent(context.getActivity(), PushInActivity.class);
        context.startActivity(intent);

    }

    private void refreshComeSuccess(){
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET_COME_SUCCESS);
        EventbusAgent.getInstance().post(action);
    }


    private void messageLogout() {
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
        HYMob.getBaseDataListForPage(context.getActivity(), HYEventConstans.PAGE_MESSAGE_LIST, context.stop_time - context.resume_time);
    }


}
