package com.huangyezhaobiao.iview;

/**
 * Created by shenzhixin on 2015/11/11.
 */
public interface MobileChangeIView {
    public void onSubmitEnabled();

    public void onSubmitUnEnabled();

    void validateNumberEnabled();

    void validateNumberUnEnabled(int time);
}
