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

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.iview.SwitchButton;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.Date;

import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.grab.fragment.GrabFragment;
import wuba.zhaobiao.grab.utils.GrabAdapter;
import wuba.zhaobiao.grab.utils.GrabBaseAdapter;

/**
 * Created by SongYongmeng on 2016/8/8.
 */
public class GrabModel extends BaseModel {
    private GrabFragment context;
    private View view;
    private PullToRefreshLayout refreshView;
    private ListView listView;
    private LinearLayout choseLayout;
    private TextView textHead;
    private SwitchButton switchButton;
    private GrabAdapter adapter;
    private PushToPassBean passBean;
    private KnockViewModel knockViewModel;


    public GrabModel(GrabFragment context) {
        this.context = context;
    }

    public View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_binding, container, false);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        listView = (ListView) view.findViewById(R.id.grab_list);
        choseLayout = (LinearLayout) view.findViewById(R.id.ll_grab);
        choseLayout.setVisibility(View.VISIBLE);
        textHead = (TextView) view.findViewById(R.id.txt_head);
        textHead.setText("抢单");
        switchButton = (SwitchButton) view.findViewById(R.id.switch_button);
        adapter = new GrabAdapter(context.getActivity(), adapterListener);
        return view;
    }

    public void setParamsForListView() {
        listView.setDividerHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        listView.setAdapter(adapter);
        refreshView.setOnRefreshListener(new Refresh());
    }

    public void getData() {
        OkHttpUtils.get(Urls.GRAB_GET_LIST)//
                .params("pushId", "-1")//
                .params("bidId", "-1")//
                .params("bidState", "-1")//
                .execute(new GetGrabListRespons(context.getActivity(), true));
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
    GrabBaseAdapter.AdapterListener adapterListener = new GrabAdapter.AdapterListener() {


        @Override
        public void onAdapterViewClick(int id, PushToPassBean bean) {
            //点击了抢单
            BDMob.getBdMobInstance().onMobEvent(context.getActivity(), BDEventConstans.EVENT_ID_BIDDING_LIST_PAGE_BIDDING);

            HYMob.getDataListForQiangdan(context.getActivity(), HYEventConstans.EVENT_ID_BIDDING_LIST_PAGE_BIDDING, String.valueOf(bean.getBidId()), "1");

            passBean = bean;

            grabRequset(bean, AppConstants.BIDSOURCE_LIST);

        }
    };

    private class Refresh implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

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
        public GetGrabListRespons(Activity context, Boolean b) {
            super(context, b);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            ToastUtils.showToast(s);
        }
    }
}
