package wuba.zhaobiao.utils;

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
}
