package wuba.zhaobiao.grab.model;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.PopAdapter;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.iview.SwitchButton;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.grab.fragment.GrabFragment;
import wuba.zhaobiao.grab.utils.GrabCachUtils;

/**
 * Created by SongYongmeng on 2016/8/8.
 */
public class GrabModel<T> extends BaseModel {
    private GrabFragment context;
    private PullToRefreshLayout refreshView;
    private ListView listView;
    private LinearLayout choseLayout;
    private TextView textHead;
    private SwitchButton switchButton;
    private PopAdapter adapter;
    private PushToPassBean passBean;
    private KnockViewModel knockViewModel;
    private View view;
    private List<QDBaseBean> showData = new ArrayList<QDBaseBean>();
    private String pushId = "-1";
    private boolean banPullUpTag;


    public GrabModel(GrabFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_grab, container, false);
    }

    public void initView() {
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        listView = (ListView) view.findViewById(R.id.grab_list);
        choseLayout = (LinearLayout) view.findViewById(R.id.ll_grab);
        textHead = (TextView) view.findViewById(R.id.txt_head);
        switchButton = (SwitchButton) view.findViewById(R.id.switch_button);
        adapter = new PopAdapter(context.getActivity(), adapterListener);
    }

    public void banPullUp() {
        refreshView.setBanPullUp(true);
        banPullUpTag = true;
    }

    public void canPullUp() {
        refreshView.setBanPullUp(false);
        banPullUpTag = false;
    }


    public void setParamsForListView() {
        textHead.setText("抢单");
        choseLayout.setVisibility(View.VISIBLE);
        listView.setDividerHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        listView.setAdapter(adapter);
        refreshView.setOnRefreshListener(new Refresh());
    }

    public void getData() {
        OkHttpUtils.get(Urls.GRAB_GET_LIST)
                .params("pushId", pushId)
                .params("bidId", "-1")
                .params("bidState", "-1")
                .execute(new GetGrabListRespons(context.getActivity()));
    }

    private void grabRequset(PushToPassBean bean, String bidsourceList) {
        String serviceState = SPUtils.getServiceState(context.getActivity());
        String userState = "";
        switch (serviceState) {
            case "1"://服务模式
                userState = "0";
                break;
            case "2"://休息模式
                userState = "1";
                break;
        }

        long bidId = bean.getBidId();
        long pushId = bean.getPushId();
        int pushTurn = bean.getPushTurn();

        OkHttpUtils.get(Urls.GRAB_REQUEST)
                .params("bidId", "" + bidId)
                .params("pushId", "" + pushId)
                .params("pushturn", "" + pushTurn)
                .params("userState", userState)
                .params("bidSource", bidsourceList)
                .params("token", new Date().getTime() + "")
                .execute(new GrabRespons(context.getActivity(), true));
    }

    /**
     * adapter的回调监听
     */
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


    private class Refresh implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            canPullUp();
            pushId = "-1";
            getData();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (banPullUpTag)
                refreshView.refreshComplete();
            else
                getData();
        }
    }

    private class GrabRespons extends DialogCallback<String> {

        public GrabRespons(Activity context, boolean b) {
            super(context, b);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {

        }
    }

    private class GetGrabListRespons extends DialogCallback<String> {
        public GetGrabListRespons(Activity context) {
            super(context);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            if (pushId.equals("-1"))
                showData.clear();

            JSONObject result = JSON.parseObject(s);
            List<T> ts = new GrabCachUtils().transferToListBean(result.getString("data"));
            List<QDBaseBean> list = (List<QDBaseBean>) ts;
            if (list.size() > 0) {
                pushId = String.valueOf(list.get(list.size() - 1).getPushId());
                showData.addAll(list);
                adapter.loadMoreSuccess(showData);
            }
            if (list.size() < 20)
                banPullUp();

            refreshView.refreshComplete();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            ToastUtils.showToast("");
        }
    }

    public NetBean transformJsonToNetBean(String result) {
        NetBean bean = JsonUtils.jsonToNetBean(result);
        return bean;
    }
}
