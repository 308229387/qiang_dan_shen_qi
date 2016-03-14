package com.huangye.commonlib.sql;

import com.lidroid.xutils.DbUtils;

/**
 * Created by shenzhixin on 2015/9/8.
 * 处理数据库升级时在具体项目中的回调
 *
 */
public interface SqlUpgradeCallback {
    public void onUpgrade(DbUtils dbUtils);
}
