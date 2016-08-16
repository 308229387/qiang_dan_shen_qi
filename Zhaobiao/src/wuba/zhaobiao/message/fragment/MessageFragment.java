package wuba.zhaobiao.message.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.message.model.MessageModel;

/**
 * Created by 58 on 2016/8/16.
 */
public class MessageFragment extends BaseFragment<MessageModel> implements INotificationListener {
    public long resume_time;
    public long stop_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {

    }

    @Override
    public void onResume() {
        super.onResume();
        resume_time = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected MessageModel createModel() {
        return new MessageModel(MessageFragment.this);
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {

    }
}
