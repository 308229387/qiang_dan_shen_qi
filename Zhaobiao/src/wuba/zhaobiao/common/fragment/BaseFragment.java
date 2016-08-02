package wuba.zhaobiao.common.fragment;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;

import wuba.zhaobiao.common.model.BaseModel;

/**
 * Created by SongYongmeng on 2016/8/2.
 */
public abstract class BaseFragment<T extends BaseModel> extends Fragment implements INetStateChangedListener, TitleMessageBarLayout.OnTitleBarClickListener {
    protected T model;
    protected View layout_back_head;
    private BiddingApplication app;
    protected TitleMessageBarLayout tbl;
    public static int current_index;
    protected long resume_time,stop_time;
    public abstract void OnFragmentSelectedChanged(boolean isSelected);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initModel();
        app = BiddingApplication.getBiddingApplication();
        app.registerNetStateListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

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
        super.onPause();
        NetStateManager.getNetStateManagerInstance().removeINetStateChangedListener();
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

    private void initModel() {
        model = createModel();
    }

    public void showToast(String str) {
        ToastUtils.showToast(str);
    }

    public void showToast(int i) {
        ToastUtils.showToast(i);
    }


    public abstract T createModel();
}
