package com.huangyezhaobiao.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import java.util.HashMap;

/**
 * Created by shenzhixin on 2015/12/7.
 *
 * edited by chenguangming on 2016/03/14
 *
 * description：目前包含抢单神器使用协议以及2步申请抢单神器会员
 */
public class SoftwareUsageActivity extends QBBaseActivity implements View.OnClickListener {
    private static final String TAG = SoftwareUsageActivity.class.getName();
    private LinearLayout backLayout,ll_webview_container;
    private TextView txt_head;
    private WebView webView_introduce;
    private BaseWebClient client;
    private WebChromeBaseClient chromeBaseClient;
    private ProgressBar pb;
    private View view_no_internet;
    private ZhaoBiaoDialog dialog;
    private String mTitle;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HYMob.getDataListByProtocol(this, HYEventConstans.USEAGE_PROTOCOL,UserUtils.getUserName(this));
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_introduce_production);
        ll_webview_container = getView(R.id.ll_webview_container);
        chromeBaseClient = new WebChromeBaseClient();

        view_no_internet = getView(R.id.view_no_internet);

        layout_back_head = getView(R.id.layout_head);

        pb         = getView(R.id.pb);

        backLayout = getView(R.id.back_layout);
        backLayout.setVisibility(View.VISIBLE);
        txt_head   = getView(R.id.txt_head);

        tbl        = getView(R.id.tbl);

        webView_introduce = getView(R.id.webview);

        client      = new BaseWebClient();
        webView_introduce.getSettings().setJavaScriptEnabled(true);
        webView_introduce.getSettings().setUseWideViewPort(true);
        webView_introduce.getSettings().setDefaultTextEncodingName("UTF-8"); // 设置默认的显示编码
        webView_introduce.setWebViewClient(client);
        webView_introduce.setWebChromeClient(chromeBaseClient);
        removeJSInterface();


        if(TextUtils.isEmpty(getIntent().getStringExtra(AppConstants.H5_TITLE))){
            mTitle = getString(R.string.h5_login_softwareusage);
        }else{
            mTitle = getIntent().getStringExtra(AppConstants.H5_TITLE);
        }

        txt_head.setText(mTitle);

        //TODO:换成软件使用协议的地址即可
        if(TextUtils.isEmpty(getIntent().getStringExtra(AppConstants.H5_WEBURL))){
            url = URLConstans.SOFTWARE_USEAGE_PROTOCOL;
        }else{
            url = getIntent().getStringExtra(AppConstants.H5_WEBURL);
        }

        LogUtils.LogE("ashenIntro", "url:" + url);
        if(!NetUtils.isNetworkConnected(this)){
            view_no_internet.setVisibility(View.VISIBLE);
        }
        webView_introduce.loadUrl(url);

    }

    /**
     * 显示判断系统版本 ，如果在4.2以下，就手动移除removeJavascriptInterface
     * 因为在4.3.1~3.0版本，webview默认添加了searchBoxJavaBridge_接口,
     */
    private void removeJSInterface(){
        if(Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                webView_introduce.removeJavascriptInterface("searchBoxJavaBridge_");
                webView_introduce.removeJavascriptInterface("accessibility");
                webView_introduce.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }


    @Override
    public void initListener() {
        backLayout.setOnClickListener(this);
    }

    private class BaseWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            stopLoading();
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            startLoading();
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.v(TAG,"shouldOverrideUrlLoading:url=" + url);
            if(url.startsWith(AppConstants.H5_FAST_LOGIN)) {
                // 跳转到注册界面 added by chenguangming
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(AppConstants.H5_TITLE, "注册");
                map.put(AppConstants.H5_WEBURL, url);
                ActivityUtils.goToActivityWithString(SoftwareUsageActivity.this, SoftwareUsageActivity.class, map);
            } else if(url.startsWith("tel:")) {
                final String urlTel = url.split(":")[1];
                // 拨打电话
                if(dialog == null || !dialog.isShowing()){
                    dialog = new ZhaoBiaoDialog(SoftwareUsageActivity.this,SoftwareUsageActivity.this.getString(R.string.make_sure_tel));
                    dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
                        @Override
                        public void onDialogOkClick() {
                            ActivityUtils.goToDialActivity(SoftwareUsageActivity.this, urlTel);
                            dialog.dismiss();
                        }

                        @Override
                        public void onDialogCancelClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            } else if(AppConstants.H5_FAST_SUCCESSREG.equals(url)) {
                ToastUtils.showShort(SoftwareUsageActivity.this,getString(R.string.h5_register_success),1500);
                // ToastUtils.makeText(SoftwareUsageActivity.this,getString(R.string.h5_register_success),Toast.LENGTH_LONG).show();
                SoftwareUsageActivity.this.finish();
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
           // handler.cancel(); 默认的处理方式，WebView变成空白页
           //handler.process();接受证书
           //handleMessage(Message msg); 其他处理
        }
    }

    private class WebChromeBaseClient  extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            LogUtils.LogE("shenzhixinPro", "progress:" + newProgress);
            pb.setProgress(newProgress);
            if(newProgress==100){
                pb.setVisibility(View.GONE);
            }else{
                if(pb.getVisibility() != View.VISIBLE){
                    pb.setVisibility(View.VISIBLE);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast.makeText(SoftwareUsageActivity.this,"onJsAlert()"+message,Toast.LENGTH_SHORT).show();
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Toast.makeText(SoftwareUsageActivity.this,"onJsConfirm()"+message,Toast.LENGTH_SHORT).show();
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Toast.makeText(SoftwareUsageActivity.this,"onJsPrompt()"+message,Toast.LENGTH_SHORT).show();
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView_introduce.removeAllViews();
        webView_introduce.destroy();
        webView_introduce = null;
//        System.exit(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_PROTOCOL, stop_time - resume_time);
    }
}
