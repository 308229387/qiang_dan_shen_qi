package com.huangyezhaobiao.factory;

/**
 * Created by shenzhixin on 2015/12/9.
 */
public class BaseFactory {
    @SuppressWarnings("unchecked")
    protected   static <T> T makeProduct(Class<T> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        T t = null;
        t = (T) Class.forName(clazz.getName()).newInstance();
        return t;

    }
}
