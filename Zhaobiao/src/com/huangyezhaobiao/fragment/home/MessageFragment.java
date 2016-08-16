package com.huangyezhaobiao.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.StorageVMCallBack;
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
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.vm.CenterMessageStorageViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/6/17.
 */
public class MessageFragment extends BaseHomeFragment implements INotificationListener, StorageVMCallBack {
    private BiddingApplication app;
    private View    back_layout;
    private TextView txt_head;
    private ListView lv_message_center;
    private MessageSeriesAdapter adapter;
    private CenterMessageStorageViewModel vm;
    private List<PushToStorageBean> messageBeans;
    private List<PushToStorageBean> qiangDan = new ArrayList<PushToStorageBean>();
    private List<PushToStorageBean> daoJiShi = new ArrayList<PushToStorageBean>();
    private List<PushToStorageBean> sysNoti = new ArrayList<PushToStorageBean>();

    private Map<String, Integer> maps = new HashMap<String, Integer>();

    private boolean isFirstOpen = true;

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
          if(isSelected){
              if(isFirstOpen){
                  load();
              }
          }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new CenterMessageStorageViewModel(getActivity(), this);
        messageBeans = new ArrayList<PushToStorageBean>();
    }

    @Override
    public void onResume() {
        app = BiddingApplication.getBiddingApplication();
        app.setCurrentNotificationListener(this);
        super.onResume();
        load();
        notifyDatasWithoutUnread();
        adapter.notifyDataSetChanged();
        HYMob.getDataList(getActivity(), HYEventConstans.INDICATOR_MESSAGE_PAGE);
    }

    @Override
    protected void loadDatas() {
    }

    @Override
    protected void loadMore() {

    }

    private void load() {  //频繁创建新线程导致OOM

        if (vm != null){
            vm.getDate();
        }

        isFirstOpen = false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View  view = null;
        if(view == null){
            view = inflater.inflate(R.layout.activity_center_message, null);
            layout_back_head = view.findViewById(R.id.layout_head);
            back_layout      = view.findViewById(R.id.back_layout);
            txt_head         = (TextView) view.findViewById(R.id.txt_head);
            txt_head.setText("我的消息");

            tbl = (TitleMessageBarLayout) view.findViewById(R.id.tbl);

            adapter = new MessageSeriesAdapter(getActivity());
            lv_message_center = (ListView) view.findViewById(R.id.lv_message_center);

            lv_message_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    int[] count = new int[] {qiangDan.size(),
                            daoJiShi.size(), sysNoti.size() };
                    switch (position + 1) {
                        case 1:
                            UnreadUtils.clearQDResult(getActivity());
                            EventAction action1 = new EventAction(EventType.EVENT_TAB_RESET);
                            EventbusAgent.getInstance().post(action1);
                                maps.put("type", DataBaseManager.TABLE_OTHER.KOUFEI);
                            ActivityUtils.goToActivityWithInteger(
                                    getActivity(), OtherDetailActivity.class, maps);

                            HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_RESULT_ALERT);
                            break;
                        case 2:
                            UnreadUtils.clearDaoJiShiResult(getActivity());
                            EventAction action2 = new EventAction(EventType.EVENT_TAB_RESET);
                            EventbusAgent.getInstance().post(action2);
                            maps.put("type", DataBaseManager.TABLE_OTHER.DAOJISHI);
                            ActivityUtils.goToActivityWithInteger(
                                    getActivity(),
                                    OtherDetailActivity.class, maps);

                            HYMob.getDataList(getActivity(), HYEventConstans.EVETN_ID_CONTACT_CLIENT);
                            break;
                        case 3:
                            UnreadUtils.clearSysNotiNum(getActivity());
                            EventAction action3 = new EventAction(EventType.EVENT_TAB_RESET);
                            EventbusAgent.getInstance().post(action3);
                            ActivityUtils.goToActivity(getActivity(), SystemNotiListActivity.class);

                            HYMob.getDataList(getActivity(), HYEventConstans.EVENT_SYSTEM_NOTICE);

                            break;
                    }

                }
            });
        }else{
            ((FrameLayout)view.getParent()).removeView(view);
        }

        return view ;
    }

    private void notifyDatasWithoutUnread() {
        messageBeans.clear();
        operateList(qiangDan);
        operateContactList(daoJiShi);
        operateSysList(sysNoti);
        adapter.setDataSources(messageBeans);
        lv_message_center.setAdapter(adapter);
    }

    private void operateList(List<PushToStorageBean> bean) {
        if (bean.size() > 0) {
            PushToStorageBean single = bean.get(bean.size()-1);
            messageBeans.add(single);
        } else {
            PushToStorageBean bean2 = new PushToStorageBean("暂无消息", "");
            messageBeans.add(bean2);
        }
    }

    private void operateContactList(List<PushToStorageBean> bean) {
        if (bean.size() > 0) {
            PushToStorageBean single = bean.get(bean.size()-1);
            messageBeans.add(single);
        } else {
            PushToStorageBean bean2 = new PushToStorageBean("暂无消息", "");
            messageBeans.add(bean2);
        }
    }

    private void operateSysList(List<PushToStorageBean> bean) {
        if (bean.size() > 0) {
            PushToStorageBean single = bean.get(bean.size()-1);
            messageBeans.add(single);
        } else {
            PushToStorageBean bean2 = new PushToStorageBean("点击查看", "");
            messageBeans.add(bean2);
        }
    }

    /**
     * 根据type,加入不同的list,主要为了显示未读数字
     *
     * @param type
     */
    private void pickWithBeanType(PushToStorageBean type) {
        LogUtils.LogE("ashenPush", "tag:" + type.getTag() + "msg:" + type.getStr());
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

    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void getDataSuccess(final Object o) {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    qiangDan.clear();
                    daoJiShi.clear();
                    sysNoti.clear();
                    ArrayList<PushToStorageBean> bean = (ArrayList<PushToStorageBean>) o;
                    if (bean != null) {
                        for (PushToStorageBean bean2 : bean) {
                            pickWithBeanType(bean2);
                        }
                    }
                    notifyDatasWithoutUnread();
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void getDataFailure() {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), getString(R.string.app_exception), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public void insertDataSuccess() {

    }

    @Override
    public void insertDataFailure() {

    }

    @Override
    public void deleteDataSuccess() {

    }

    @Override
    public void deleteDataFailure() {

    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        LogUtils.LogV("nnnnnnB2a", String.valueOf(pushBean.getTag()));
        if (pushBean != null) {
            LogUtils.LogV("nnnnnnB2b", String.valueOf(pushBean.getTag()) + StateUtils.getState(getActivity()));
            int type = pushBean.getTag();
            if (type == 100 && StateUtils.getState(getActivity()) == 1) {
                LogUtils.LogV("nnnnnnB2c", String.valueOf(pushBean.getTag()));

                String isSon = UserUtils.getIsSon(getActivity());
                if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
                    String rbac = UserUtils.getRbac(getActivity());
                    if (!TextUtils.isEmpty(rbac)
                            && TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {
                        LogUtils.LogV("PushInActivity", "MessageFragment" + "没有权限弹窗");
                    }else{

                        Intent intent = new Intent(getActivity(), PushInActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Intent intent = new Intent(getActivity(), PushInActivity.class);
                    startActivity(intent);
                }

            }
            else if(type == 105){
                try {
                    new LogoutDialogUtils(getActivity(), "当前账号被强制退出").showSingleButtonDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(type != 100 && type != 105){
                LogUtils.LogV("nnnnnnB2d", String.valueOf(pushBean.getTag()));
//                if(tbl  != null){
//                    tbl.setPushBean(pushBean);
//                    tbl.setVisibility(View.VISIBLE);
//                }
//
//                PushUtils.pushList.clear();

                load();
        }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (app != null)
            app.removeINotificationListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(getActivity(), HYEventConstans.PAGE_MESSAGE_LIST, stop_time - resume_time);
    }
}
