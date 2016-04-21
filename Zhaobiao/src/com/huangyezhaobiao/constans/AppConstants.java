package com.huangyezhaobiao.constans;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;

import java.io.File;

/**
 * Created by 58 on 2015/9/28.
 */
public class AppConstants {


    public static final String BUGTAGS_KEY = "2df806ee275e6ef1f8663c332def8eee";

    public static final String UPLOAD_REFUND_EVIDENCE = "image";
    public static int HOME_TAB_HOME = 0;
    public static int HOME_TAB_DISCOVER = 1;
    public static int HOME_TAB_POST = 2;
    public static int HOME_TAB_MESSAGE = 3;
    public static int HOME_TAB_PERSONAL_CENTER = 4;

    public static int HOME_PAGE_HOME = 0;
    public static int HOME_PAGE_DISCOVER = 1;
    public static int HOME_PAGE_MESSAGE = 2;
    public static int HOME_PAGE_PERSONAL_CENTER = 3;

    public static int HOME_PAGE_INDEX = 0;

    public static final String APP_CHANNEL = "APP_CHANNEL";
    public static final String APP_INNER_ID = "APP_INNER_ID";

    /**
     * 当获取不到经纬度时默认用北京天安门经纬度，默认城市北京
     */
    public static double latitude = 39.915112;
    public static double longitude = 116.403955;
    public static String DEFAULT_MAP_SCALE = "8";
    public static String DEFAULT_SERVICE_RADIUS = "1000";

    public static String city = "北京"; //当前的城市
    public static String address = "天安门"; //当前的地址

    // 列表每页的数据
    public static int LIST_PAGE_SIZE = 20;

    // 单张图片上传最大size
    public static final int UPLOAD_PIC_MAX_SIZE = 200; //最大200K

    // 评论、服务、需求、预约可上传图片最大张数
    public static final int UPLOAD_PIC_MAX_COUNT = 9;

    // 自我介绍最大字数限制
    public static final int USER_INTRODUCE_MAX_SIZE = 140;

    //我的交易列表页，每次请求的数据的条数
    public static final String DEAL_PAGE_SIZE_LOAD = "20";

    // 发布需求 ，买方备注，最少10个字符，最多1000个字符
    public static final int DEMAND_POST_COMMENT_MAX_LENGTH = 1000;
    public static final int DEMAND_POST_COMMENT_MIN_LENGTH = 10;
    // 需求标题的最大值
    public static final int DEMAND_TITLE_MAX_LENGTH = 12;
    //服务标题的最小值
    public static final int MIN_SKILL_LENGTH = 4;
    //服务内容的最小值
    public static final int MIN_SKILL_DESC = 20;

    /**
     * 屏幕宽和高
     */
    public static int screenWidth, screenHeight;
    public static final int BASE_SCREEN_WIDTH = 480;
    public static final int BASE_SCREEN_HEIGHT = 800;

    public static void initConfig(Context context) {
        initScreenSize(context);
        makeDirectoriesIngoreMedia();
//        AppInfo.init(context);
    }

    /**
     * 初始化屏幕大小
     */
    public static void initScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        if (screenWidth == 0) {
            screenWidth = BASE_SCREEN_WIDTH;
        }
        if (screenHeight == 0) {
            screenHeight = BASE_SCREEN_HEIGHT;
        }
    }

    /**
     * 创建SD卡目录
     */
    public static void makeDirectoriesIngoreMedia() {
        makeDirectoryIngoreMedia(Directorys.TEMP);
        makeDirectoryIngoreMedia(Directorys.CACHE);
        makeDirectoryIngoreMedia(Directorys.DOWNLOAD);
        makeDirectoryIngoreMedia(Directorys.IMAGEDIR);
    }

    /**
     * 创建文件夹
     */
    public static void makeDirectoryIngoreMedia(String dirName) {
        try {
            new File(dirName).mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序目录定义
     */
    public static class Directorys {
        /**
         * SD卡根目录
         */
        public static String SDCARD = Environment.getExternalStorageDirectory().toString();
        /**
         * 程序根目录
         */
        public static final String ROOT = SDCARD
                + File.separator
                + BiddingApplication.getAppInstanceContext().getString(
                R.string.app_names) + File.separator;
        /**
         * 临时位置
         */
        public static final String TEMP = ROOT + "temp" + File.separator;
        /**
         * 缓存位置
         */
        public static final String CACHE = ROOT + "cache" + File.separator;
        /**
         * 下载
         */
        public static final String DOWNLOAD = ROOT + "download" + File.separator;

        /**
         * SD卡上图片保存的目录
         */
        public static final String IMAGEDIR = SDCARD + "image" + File.separator;
    }


    /**
     * 在列表页抢的单子
     */
        public static final String BIDSOURCE_LIST = "1";
    /**
     * 在弹窗抢的单子
     */
        public static final String BIDSOURCE_WINDOW = "0";
    /**
     * 在详情页抢的单子
     */
        public static final String BIDSOURCE_DETAIL = "2";


    /**
     * 跳转到H5页 title
     * created by chenguangming 2016/03/14
     */
    public static final String H5_TITLE = "title";

    /**
     * 跳转到H5页 url
     * created by chenguangming 2016/03/14
     */
    public static final String H5_WEBURL = "url";
    public static final String H5_FAST_LOGIN = "http://m.m.58.com/reg/?from=qdsq";
    public static final String H5_FAST_SUCCESSREG= "http://m.m.58.com/";



    /**  added by chenguangming, the header's of http request params */
    public static final String PBI_APPVERSION = "PBI-AppVersion";
    public static final String PBI_PROTOCOLVERSION = "PBI-ProtocolVersion";
    public static final String PBI_REQUESTTIME = "PBI-RequestTime";
    public static final String PBI_SYSTEMPLATFORM = "PBI-SystemPlatform";
    public static final String PBI_NETTYPE = "PBI-NetType";
    public static final String PBI_NETCOMPANY = "PBI-NetCompany";
    public static final String PBI_SYSTEMMODEL = "PBI-SystemModel";
    public static final String PBI_SYSTEMVERSION = "PBI-SystemVersion";
    public static final String PBI_DEVICETOKEN = "PBI-DeviceToken";
    public static final String PBI_IMEI = "PBI-IMEI";
    public static final String PBI_IDENTIFIERFORVENDOR = "PBI-IdentifierForVendor";
    public static final String PBI_RELEASEMARKET = "PBI-ReleaseMarket";
    public static final String PBI_PPU ="ppu";
}
