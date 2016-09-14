package wuba.zhaobiao.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

/**
 * Created by SongYongmeng on 2016/9/13.
 */
public class PublickMethod {

    public static String getPriceFromString(String stringPrice) {
        double temp = Double.parseDouble(stringPrice);
        DecimalFormat df = new DecimalFormat("0.00");
        String price = df.format(temp);
        return price;
    }

    public static String getPriceFromDouble(Double doublePrice) {
        DecimalFormat df = new DecimalFormat("0.00");
        String price = df.format(doublePrice);
        return price;
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

}
