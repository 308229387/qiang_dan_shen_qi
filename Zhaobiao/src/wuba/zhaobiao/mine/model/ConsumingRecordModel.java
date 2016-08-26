package wuba.zhaobiao.mine.model;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.ConsumptionAdapter;
import com.huangyezhaobiao.bean.ConsumeItemBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.Utils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.ConsumingRecordActivity;
import wuba.zhaobiao.order.utils.OrderCachUtils;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/8/19.
 */
public class ConsumingRecordModel extends BaseModel implements View.OnClickListener{

    private ConsumingRecordActivity context;

    private View layout_back_head;
    private View  back_layout;
    private TextView txt_head;

    private PullToRefreshLayout refresh;
    private ListView consume_list;
    private View  datas_empty_layout;

    private ConsumptionAdapter adapter;
    private List<ConsumeItemBean> itemBeans= new ArrayList<>();

    private String pageNum = "1";
    private int pageNumber = 1;
    private String totalPage = "";

    private Boolean isFromCache;

    public ConsumingRecordModel(ConsumingRecordActivity context){
        this.context = context;
    }

    public void initHeader() {
        createHeader();
        initHeaderBack();
        createHeaderTitle();
    }

    private void createHeader(){
        layout_back_head   = context.findViewById(R.id.layout_head);
    }

    private void initHeaderBack(){
        createHeaderBack();
        setHeaderBackListener();
    }

    private void createHeaderBack(){
        back_layout = context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setHeaderBackListener(){
        back_layout.setOnClickListener(this);
    }

    private void createHeaderTitle(){
        txt_head   = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText("消费记录");
    }

    public void initListView(){
        createListView();
        setEmptyLayoutListener();
    }

    private void createListView(){
        refresh  = (PullToRefreshLayout) context.findViewById(R.id.refresh);
        consume_list = (ListView) context.findViewById(R.id.consume_list);
        datas_empty_layout = context.findViewById(R.id.datas_empty_layout);
    }


    private void setEmptyLayoutListener(){
        datas_empty_layout.setOnClickListener(this);
    }

    public void createAdapter(){
        adapter  = new ConsumptionAdapter(context,itemBeans);
    }

    public void setParamsForListView(){
        consume_list.setAdapter(adapter);
        refresh.setOnRefreshListener(new refreshListener());
    }

    private class refreshListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refresh.setBanPullUp(false);
            pageNum = "1";
            pageNumber =1;
            getCostRecord();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (refresh.getBanPullUpState()) {
                refresh.refreshComplete();
            }else{
                getCostRecord();
            }
        }
    }

    public void getCostRecord(){
        OkHttpUtils.get(Urls.CONSUMING_RECORD)
                .params("pageNum", pageNum)
                .execute(new GetCostRecordRespons(context, true));
    }

    private void hasData(String s){
        clearLocalInfo();
        dealWithData(s);

    }

    private void clearLocalInfo() {
        if (TextUtils.equals("1", pageNum)) {
            itemBeans.clear();
        }
    }

    private void dealWithData(String s) {
        JSONObject result = JSON.parseObject(s);
        String dataList  = result.getString("data");
        List<ConsumeItemBean> list = JsonUtils.jsonToObjectList(dataList, ConsumeItemBean.class);
        totalPage =  getJSONObjectOfPage(result);

        if(list != null){
            noData(list);
            hasData(list);
            hasDataButNotFull(list);
        }
    }

    private void noData(List<ConsumeItemBean> list) {
        if(list!=null &&  list.size()==0){
            datas_empty_layout.setVisibility(View.VISIBLE);
        }else {
            datas_empty_layout.setVisibility(View.GONE);
        }
    }

    private void hasDataButNotFull( List<ConsumeItemBean> list) {
        if (list!=null && list.size() < 10){
            refresh.setBanPullUp(true);
        }
    }

    private void hasData( List<ConsumeItemBean> list) {
        if (list!=null && list.size() > 0){
            setPageNum();
            itemBeans.addAll(list);
            adapter.notifyDatas(itemBeans);

        }
    }

    private void setPageNum(){
            try {
                if(pageNumber <= Integer.parseInt(totalPage)){
                    pageNumber++;
                    pageNum = String.valueOf(pageNumber);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
    }

    private String  getJSONObjectOfPage(JSONObject result) {
        try {
            return new OrderCachUtils().transferToBean(result.getString("other"));
        } catch (Exception e) {
            return null;
        }
    }

    public void setTopBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = Utils.getStatusBarHeight(context);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }

    public void setTopBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                back();
                break;
            case R.id.datas_empty_layout:
                getCostRecord();
                break;
        }
    }

    private void back(){
        context.onBackPressed();
    }

    public void pageDeadStatistics(){
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_RECORD_EXPENSES, context.stop_time - context.resume_time);
    }

    private class GetCostRecordRespons extends DialogCallback<String> {
        public GetCostRecordRespons(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            if (s != null) {
                hasData(s);
            }
            refresh.refreshComplete();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast && e != null) {
                ToastUtils.showToast(e.getMessage());
            }
            refresh.refreshComplete();
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
            if (e != null && e.getMessage().equals(NEED_DOWN_LINE)) {
                new LogoutDialogUtils(context, context.getString(R.string.force_exit)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_FUNCTION_BAN)) {
                new LogoutDialogUtils(context, context.getString(R.string.child_function_ban)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_HAS_UNBIND)) {
                new LogoutDialogUtils(context, context.getString(R.string.child_has_unbind)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(PPU_EXPIRED)) {
                new LogoutDialogUtils(context, context.getString(R.string.ppu_expired)).showSingleButtonDialog();
            }
        }
    }


}
