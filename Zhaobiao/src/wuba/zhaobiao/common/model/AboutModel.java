package wuba.zhaobiao.common.model;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.IntroduceFunctionActivity;
import com.huangyezhaobiao.activity.SoftwareUsageActivity;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.activity.AboutActivity;
import wuba.zhaobiao.config.Urls;

/**
 * Created by 58 on 2016/8/3.
 */
public class AboutModel extends BaseModel implements View.OnClickListener {

    private AboutActivity context;
    private View layout_back_head; //标题栏
    private LinearLayout back_layout; //返回
    private TextView txt_head, tv_version; //标题、版本
    private String name; //
    private RelativeLayout rl_gongneng, rl_check_version, software_usage;

    int currentVersion = -1; //当前版本号
    int versionNum = -1; //获取当前系统版本号
    private boolean forceUpdate = false; //是否强制更新

    public AboutModel(AboutActivity context) {
        this.context = context;
    }

    public void initHeaderView() {
        createHeaderView();
        initHeaderBack();
        createHeaderTitle();
    }

    private void createHeaderView() {
        layout_back_head = context.findViewById(R.id.layout_head);
    }

    private void initHeaderBack() {
        createHeaderBack();
        setHeaderBackListener();
    }

    private void createHeaderBack() {
        back_layout = (LinearLayout) context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setHeaderBackListener() {
        back_layout.setOnClickListener(this);
    }

    private void createHeaderTitle() {
        txt_head = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText(R.string.about);
    }

    public void initVersionName() {
        createVersionName();
        setVersionName();
    }

    private void createVersionName() {
        tv_version = (TextView) context.findViewById(R.id.tv_version);
    }

    private void setVersionName() {
        try {
            name = "v" + VersionUtils.getVersionName(context);
        } catch (PackageManager.NameNotFoundException e) {
            name = context.getString(R.string.error_get_versioncode);
            e.printStackTrace();
        }
        tv_version.setText(name);
    }

    public void initFunctionInfo() {
        createFunctionInfo();
        setFunctionInfoListener();
    }

    private void createFunctionInfo() {
        rl_gongneng = (RelativeLayout) context.findViewById(R.id.rl_gongneng);
    }

    private void setFunctionInfoListener() {
        rl_gongneng.setOnClickListener(this);
    }

    public void initCheckUpdate() {
        createCheckUpdate();
        setCheckUpdateListener();
    }

    private void createCheckUpdate() {
        rl_check_version = (RelativeLayout) context.findViewById(R.id.rl_check_version);
    }

    private void setCheckUpdateListener() {
        rl_check_version.setOnClickListener(this);
    }

    public void initSoftwareUsage() {
        createSoftwareUsage();
        setSoftwareUsageListener();
    }

    private void createSoftwareUsage() {
        software_usage = (RelativeLayout) context.findViewById(R.id.software_usage);
    }

    private void setSoftwareUsageListener() {
        software_usage.setOnClickListener(this);
    }

    public void setTopBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = Utils.getStatusBarHeight(context);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }

    public void setTopBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout: //返回
                back();
                break;
            case R.id.rl_check_version://跳转到检查版本更新的界面
                checkVersion();
                break;
            case R.id.rl_gongneng://跳转到功能界面 都是h5;
                goToFunctionPage();
                goToFunctionPageStatistics();
                break;
            case R.id.software_usage://使用协议
                goToSoftWareUsagePage();
                break;
        }
    }

    private void back() {
        context.onBackPressed();
    }

    private void checkVersion() {
        OkHttpUtils.get(Urls.UPDATE_VERSION)//
                .params("platform","1")
                .execute(new checkVersionCallback(context, true));
    }


    private void goToFunctionPage(){
        ActivityUtils.goToActivity(context, IntroduceFunctionActivity.class);
    }

    private void goToFunctionPageStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_INTRODUCTION);
    }

    private void goToSoftWareUsagePage(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(AppConstants.H5_TITLE, context.getString(R.string.h5_login_softwareusage));
        map.put(AppConstants.H5_WEBURL, URLConstans.SOFTWARE_USEAGE_PROTOCOL);  //2016.5.3 add
        ActivityUtils.goToActivityWithString(context, SoftwareUsageActivity.class, map);
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_ABOUT, context.stop_time - context.resume_time);
    }

    private class checkVersionCallback extends DialogCallback<String>{

        public checkVersionCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            LogUtils.LogV("updateVersion", "update_success");
            if(response!=null){
                Headers responseHeadersString = response.headers();
                String version = responseHeadersString.get("version");//获取服务器header返回的版本号
                if (version != null) {
                    getVersion(version);
                }
            }
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {

            ToastUtils.showToast(e.getMessage());
        }
    }


    private void getVersion(String version ){
        try {
            if (version.contains("F")) {
                forceUpdate = true;
                String[] fs = version.split("F");
                String versionCode = fs[0];
                versionNum = Integer.parseInt(versionCode);
            } else {
                versionNum = Integer.parseInt(version);
            }
            if (versionNum == -1) return;

            currentVersion = Integer.parseInt(VersionUtils.getVersionCode(context));
            if (currentVersion == -1) return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        compareVersion(versionNum, currentVersion, forceUpdate);

    }
     private void compareVersion(int netVersion,int localVersion,boolean isForceUpdate) {
         UpdateManager.getUpdateManager().isUpdateNow(context, netVersion, localVersion, URLConstans.DOWNLOAD_ZHAOBIAO_ADDRESS, isForceUpdate);
         alreadyNewVersion();
     }
    private void alreadyNewVersion() {
        Boolean flag = UpdateManager.needUpdate;
        if (!flag) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(context.getString(R.string.already_new_version));
                }
            });
        }

    }
}
