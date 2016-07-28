package wuba.zhaobiao.common.model;

import java.lang.ref.WeakReference;

/**
 * Created by SongYongmeng on 2016/7/28.
 */
public class BaseModel<T> {
    private WeakReference<T> wr;
    /**
     * 将回调接口T与presenter进入注册
     *
     * @param t
     */
    public void onAttachView(T t) {
        this.wr = new WeakReference<T>(t);
    }
}
