package com.huangyezhaobiao.factory;

import android.util.SparseArray;

import com.huangyezhaobiao.bean.tt.RefundBaseBean;
import com.huangyezhaobiao.bean.tt.RefundCloseTimeBean;
import com.huangyezhaobiao.bean.tt.RefundFirstCommitBean;
import com.huangyezhaobiao.mediator.RefundMediator;

/**
 * Created by shenzhixin on 2015/12/9.
 */
public class RefundVMFactory extends BaseFactory{
    private static SparseArray<Class> sources = new SparseArray<>();
    static {
        sources.append(RefundMediator.TYPE_REFUND_TIME_CLOSE, RefundCloseTimeBean.class);
        sources.append(RefundMediator.TYPE_REFUND_FIRST_COMMIT, RefundFirstCommitBean.class);
        sources.append(RefundMediator.TYPE_REFUND_ADD, RefundCloseTimeBean.class);
        sources.append(RefundMediator.TYPE_REFUND_RESULT, RefundCloseTimeBean.class);
    }

   public static Class<? extends RefundBaseBean> getBeanClass(int type){
       return sources.get(type);
   }



}
