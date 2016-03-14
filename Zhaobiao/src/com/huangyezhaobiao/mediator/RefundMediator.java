package com.huangyezhaobiao.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/9.
 */
public class RefundMediator {
    public final static String VALUE_TYPE_TIME_CLOSE = "time_close";
    public final static String VALUE_TYPE_FIRST_REFUND = "first_refund";
    public final static String VALUE_TYPE_ADD_REFUND = "add_refund";
    public final static String VALUE_TYPE_REFUND_RESULT = "refund_result";


    //type来判断是哪个页面
    public final static int TYPE_REFUND_TIME_CLOSE   = 0;
    public final static int TYPE_REFUND_FIRST_COMMIT = 1;
    public final static int TYPE_REFUND_ADD          = 2;
    public final static int TYPE_REFUND_RESULT       = 3;


    public final static  List<String> checkedId = new ArrayList<>();


    //首次提交数据进行相册选取
    public final static int REFUND_COMMIT_TYPE  = 1;

}
