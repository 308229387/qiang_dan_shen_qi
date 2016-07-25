package com.huangyezhaobiao.db;

/**
 * Created by SongYongmeng on 2016/7/21.
 */
public class UserRequestDao {
    public static final String TABLE_NAME = "qiangdan_user";
    public static final String INTERFACE_GETBINDS = "getBids";
    public static final String INTERFACE_ORDERLIST = "orderlist";

    public static void insert(String port, String data) {
        RequestDaoDBManager.getInstance().insert(port, data);
    }

    public static void deleteAll() {
        RequestDaoDBManager.getInstance().delete();
    }

    public static void deleteWhichOneByData(String port, String data) {
        RequestDaoDBManager.getInstance().delete(port, data);
    }

    public static void deleteWhichOneByPort(String port) {
        RequestDaoDBManager.getInstance().delete(port);
    }

    public static String getData(String port) {
        return RequestDaoDBManager.getInstance().getDate(port);
    }

    public static void updata(String port, String data) {
        RequestDaoDBManager.getInstance().update(port, data);
    }

    public static Boolean hasData(String port) {
        return RequestDaoDBManager.getInstance().hasData(port);
    }

    public static void replace(String port, String data) {
        RequestDaoDBManager.getInstance().replace(port, data);
    }

    public static void addData(String port, String data) {
        if (hasData())
            updata(port, data);
        else
            insert(port, data);
    }

    private static boolean hasData() {
        if (hasData(UserRequestDao.INTERFACE_GETBINDS) || hasData(UserRequestDao.INTERFACE_ORDERLIST))
            return true;
        else
            return false;
    }

}
