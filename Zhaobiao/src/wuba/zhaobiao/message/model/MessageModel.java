package wuba.zhaobiao.message.model;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.adapter.MessageSeriesAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.message.fragment.MessageFragment;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/8/16.
 */
public class MessageModel extends BaseModel implements TitleMessageBarLayout.OnTitleBarClickListener{

    private MessageFragment context;
    private View view;
    private View layout_back_head;
    private View    back_layout;
    private TextView txt_head;
    private TitleMessageBarLayout tbl;

    private ListView lv_message_center;
    private MessageSeriesAdapter adapter;

    private BiddingApplication app;

    public MessageModel(MessageFragment context){
        this.context =context;
    }

    public void createView(LayoutInflater inflater,ViewGroup container){
        view = inflater.inflate(R.layout.fragment_message, container, false);
    }

    public void initView(){
        initTopBar();
        initListView();
    }

    private void initTopBar(){
        layout_back_head = view.findViewById(R.id.layout_head);
        back_layout      = view.findViewById(R.id.back_layout);
        txt_head         = (TextView) view.findViewById(R.id.txt_head);
        txt_head.setText("我的消息");
        tbl = (TitleMessageBarLayout) view.findViewById(R.id.tbl);
    }

    private void initListView(){
        lv_message_center = (ListView) view.findViewById(R.id.lv_message_center);
    }

    public void createAdapter(){
        adapter = new MessageSeriesAdapter(context.getActivity());
    }

    public void registerMessageBar(){
        if (tbl != null) {
            tbl.setVisibility(View.GONE);
            tbl.setTitleBarListener(this);
        }
    }

    public void registerListener(){
        app = BiddingApplication.getBiddingApplication();
        app.setCurrentNotificationListener(context);
        app.registerNetStateListener();
    }

    public  void setHeaderHeight(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            context.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int height = Utils.getStatusBarHeight(context.getActivity());
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }


    public View getView() {
        return view;
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
        if (tbl != null) {
            tbl.showNetError();
            tbl.setVisibility(View.VISIBLE);
        }
    }

    private void NetConnected() {
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
            tbl.setPushBean(pushBean);
            tbl.setVisibility(View.VISIBLE);
            PushUtils.pushList.clear();
        }
    }

    private void dealWhitData() {
        String isSon = UserUtils.getIsSon(context.getActivity());
        if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
            rightInfo();
        } else {
            goingPushin();
        }
    }

    private void rightInfo() {
        String rbac = UserUtils.getRbac(context.getActivity());
        if (!TextUtils.isEmpty(rbac)
                && TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {
        } else {
            successGrab();
        }
    }

    private void goingPushin() {
        Intent intent = new Intent(context.getActivity(), PushInActivity.class);
        context.startActivity(intent);
    }

    private void successGrab() {
//        refresh();
        goingPushin();
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
}
