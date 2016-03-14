package com.huangyezhaobiao.photomodule;

import android.text.TextUtils;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;


/**
 * Created by 58 on 2015/10/22.
 */
public class ImageUrlUtil {
    private static final String KEY_SHORT_UNDERLINE = "_";
    private static final String KEY_DOT = ".";
    private static final String KEY_QA = "qa";
    private static final String KEY_T = "t";
    public final static String KEY_CHAR_ASK = "?";
    public final static String KEY_CHAR_EQUALS = "=";
    public final static String KEY_CHAR_AND = "&";

    public static interface ImageType {
        public static final int SKILL_LIST_HALL = 1; // 服务聚合页
        public static final int EVALUATE_SUBMIT_PAGE_HEAD = 2; // 填写评价页用户头像
        public static final int LIST_ITEM_ROUND_HEAD = 3; // list列表中圆形用户头像
        public static final int LIST_ITEM_MEDIA_PIC = 4; // list列表中多媒体图片
        public static final int PERSONAL_CENTER_USER_HEAD = 5; // 个人中心用户头像
        public static final int HOME_CATEGORY_ITEM = 6; // 首页类目
        public static final int HOME_NEARBY_ITEM = 7; // 首页附近的人
        public static final int DEAL_SELLER_GROUP_ITEM = 8; // 约定详情中已投标的卖家
        public static final int CHAT_USER_ROUND_HEAD = 9; // 聊天界面用户头像
        public static final int CHAT_MORE_ROUND_HEAD = 10; // 聊天设置页面
        public static final int IMPROVE_USER_ROUND_HEAD = 11; // 完善个人资料页面的头像
        public static final int PERSONAL_HOME_SKILL_COVER = 12; // 个人主页服务的封面图
    }

    public static interface SizeMode {
        public final static int S = 1;
        public final static int M = 2;
        public final static int L = 3;
        public final static int XL = 4;
    }

    public static interface PicQuarity {
        public final static int Origin = 100;
        public final static int High = 75;
        public final static int Low = 40;
    }

    public static int defaultT = 5;

    /**
     * 得到指定大小图片地址
     */
    public static String getImageUrl(String url, int type, int sizeMode) {
        String resultUrl = url;
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        int width = getBitmapWidthBySize(sizeMode);
        switch (type) {
            case ImageType.HOME_CATEGORY_ITEM:
            case ImageType.SKILL_LIST_HALL:
            case ImageType.LIST_ITEM_MEDIA_PIC:
            case ImageType.PERSONAL_CENTER_USER_HEAD:
            case ImageType.EVALUATE_SUBMIT_PAGE_HEAD:
                resultUrl = getUrlWithSize(url, width);
                resultUrl = appendUrlWithQa(resultUrl, PicQuarity.High);
                resultUrl = appendUrlWithT(resultUrl, defaultT);
                break;
            case ImageType.LIST_ITEM_ROUND_HEAD:
            case ImageType.HOME_NEARBY_ITEM:
            case ImageType.DEAL_SELLER_GROUP_ITEM:
            case ImageType.CHAT_USER_ROUND_HEAD:
            case ImageType.CHAT_MORE_ROUND_HEAD:
            case ImageType.PERSONAL_HOME_SKILL_COVER:
                resultUrl = getUrlWithSize(url, width);
                resultUrl = appendUrlWithQa(resultUrl, PicQuarity.Low);
                resultUrl = appendUrlWithT(resultUrl, defaultT);
                break;
            default:
                break;
        }
        return resultUrl;
    }

    private static String getUrlWithSize(String url, int width) {
        String resultUrl;
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        int index = url.lastIndexOf(KEY_DOT);
        if (index != -1) {
            String format = url.substring(index);
            resultUrl = url.substring(0, index) + KEY_SHORT_UNDERLINE + width + KEY_SHORT_UNDERLINE + width + format;
        } else {
            resultUrl = url;
        }
        return resultUrl;
    }

    private static String appendUrlWithQa(String url, int level) {
        String resultUrl = url;
        int qa = PicQuarity.Origin;
        switch (level) {
            case PicQuarity.High:
                qa = level;
                break;
            case PicQuarity.Low:
                qa = level;
                break;
            default:
                break;
        }
        if (qa == PicQuarity.Origin) {
            return resultUrl;
        }
        String qaParams = KEY_QA + KEY_CHAR_EQUALS + qa;
        if (resultUrl.endsWith(KEY_CHAR_ASK)) {
            return resultUrl + qaParams;
        } else {
            if (resultUrl.contains(KEY_CHAR_EQUALS)) {
                return resultUrl + KEY_CHAR_AND + qaParams;
            } else {
                return resultUrl + KEY_CHAR_ASK + qaParams;
            }
        }
    }

    private static String appendUrlWithT(String url, int t) {
        String resultUrl = url;
        String tParams = KEY_T + KEY_CHAR_EQUALS + defaultT;
        if (resultUrl.endsWith(KEY_CHAR_ASK)) {
            return resultUrl + tParams;
        } else {
            if (resultUrl.contains(KEY_CHAR_EQUALS)) {
                return resultUrl + KEY_CHAR_AND + tParams;
            } else {
                return resultUrl + KEY_CHAR_ASK + tParams;
            }
        }
    }

    private static int getBitmapWidthBySize(int size) {
        int width = 0;
        switch (size) {
            case ImageUrlUtil.SizeMode.S:
                width = BiddingApplication.getAppInstanceContext().getResources().getDimensionPixelSize(
                        R.dimen.image_url_small_width);
                break;
            case ImageUrlUtil.SizeMode.M:
                width =BiddingApplication.getAppInstanceContext().getResources().getDimensionPixelSize(
                        R.dimen.image_url_medium_width);
                break;
            case ImageUrlUtil.SizeMode.L:
                width = BiddingApplication.getAppInstanceContext().getResources().getDimensionPixelSize(
                        R.dimen.image_url_large_width);
                break;
            case ImageUrlUtil.SizeMode.XL:
                width = BiddingApplication.getAppInstanceContext().getResources().getDimensionPixelSize(
                        R.dimen.image_url_xlarge_width);
                break;
            default:
                width = BiddingApplication.getAppInstanceContext().getResources().getDimensionPixelSize(
                        R.dimen.image_url_large_width);
                break;
        }
        return width;
    }
}
