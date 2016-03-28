package com.huangyezhaobiao.factory;

import com.huangyezhaobiao.fragment.refund.FirstRefundCommitFragment;
import com.huangyezhaobiao.fragment.refund.RefundAgainCommitFragment;
import com.huangyezhaobiao.fragment.refund.RefundBaseFragment;
import com.huangyezhaobiao.fragment.refund.RefundResultFragment;
import com.huangyezhaobiao.fragment.refund.RefundTimeCloseFragment;
import com.huangyezhaobiao.mediator.RefundMediator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenzhixin on 2015/12/9.
 */
public class RefundFragmentFactory extends BaseFactory{
    private static Map<String,Class<? extends RefundBaseFragment>> sources = new HashMap<String,Class<? extends RefundBaseFragment>>();
    static{
        sources.put(RefundMediator.VALUE_TYPE_TIME_CLOSE, RefundTimeCloseFragment.class);
        sources.put(RefundMediator.VALUE_TYPE_FIRST_REFUND, FirstRefundCommitFragment.class);
        sources.put(RefundMediator.VALUE_TYPE_ADD_REFUND, RefundAgainCommitFragment.class);
        sources.put(RefundMediator.VALUE_TYPE_REFUND_RESULT, RefundResultFragment.class);

    }

    public static RefundBaseFragment createRefundFragment(String type) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        RefundBaseFragment baseFragment = null;
        Class<? extends RefundBaseFragment> classInstance = sources.get(type);
        if(classInstance==null) return baseFragment;
       return makeProduct(classInstance);
    }




}
