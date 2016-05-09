package com.huangyezhaobiao.log;

import java.util.List;

/**
 * com.huangyezhaobiao.log
 * Created by shenzhixin on 2016/5/9 14:10.
 * Log实现者的抽象接口
 */
public interface ILogExecutor {
    /**
     * 上传
     */
    void upload(List<LogBean> logBeans);



}
