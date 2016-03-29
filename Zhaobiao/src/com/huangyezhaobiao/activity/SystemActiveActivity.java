package com.huangyezhaobiao.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * Created by shenzhixin on 2015/12/16.
 */
public class SystemActiveActivity extends QBBaseActivity implements View.OnClickListener {
    String url;
    private String mAppCachePath;
    private LinearLayout backLayout,ll_webview_container;
    private TextView txt_head;
    private WebView webView_introduce;
    private ProgressBar pb;
    public static Intent onNewIntent(Context context,String url){
        Intent intent = new Intent(context,SystemActiveActivity.class);
        intent.putExtra("url",url);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        initView();
        initListener();
    }

    private int getLayoutId(){
        return R.layout.activity_introduce_production;
    }

    @Override
    public void initView() {
        setContentView(getLayoutId());
        ll_webview_container = getView(R.id.ll_webview_container);
        layout_back_head = getView(R.id.layout_head);
        pb = getView(R.id.pb);
        backLayout = getView(R.id.back_layout);
        txt_head = getView(R.id.txt_head);
        tbl = getView(R.id.tbl);
        txt_head.setText("活动消息");
        webView_introduce = getView(R.id.webview);
        initWebViewConfigs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView_introduce.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView_introduce.onPause();
    }

    private void initWebViewConfigs() {
        webView_introduce.clearCache(true);
        webView_introduce.setFocusable(true);
        webView_introduce.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView_introduce.refreshDrawableState();
        webView_introduce.clearView();
        webView_introduce.clearHistory();
        webView_introduce.removeAllViews();
        webView_introduce.setVerticalScrollBarEnabled(true);
        webView_introduce.setHorizontalScrollBarEnabled(true);
        webView_introduce.setMapTrackballToArrowKeys(false); // use trackball directly
        webView_introduce.setWebViewClient(new TJWebViewClient());
        webView_introduce.setWebChromeClient(new TJWebChromeClient());
        WebSettings webSettings = webView_introduce.getSettings();
        syncStaticSettings(webSettings);
        webView_introduce.loadUrl(url);

    }

    class TJWebChromeClient extends WebChromeClient {



        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (pb != null) {
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                } else {
                    if (pb.getVisibility() == View.GONE) {
                        pb.setVisibility(View.VISIBLE);
                    }
                    pb.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }



    class TJWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (handler != null) {
                handler.proceed();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            return super.shouldOverrideUrlLoading(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }




    private void syncStaticSettings(WebSettings settings) {
        // Enable the built-in zoom
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 设置缓冲模式
        settings.setJavaScriptEnabled(true);// 设置是否支持JavaScript
        settings.setLoadsImagesAutomatically(true);
        final PackageManager pm = getPackageManager();
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            boolean supportsMultiTouch = pm
                    .hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)
                    || pm.hasSystemFeature(PackageManager.FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT);
            settings.setDisplayZoomControls(!supportsMultiTouch);

        } else {
            if (android.os.Build.VERSION.SDK_INT >= 11)
                settings.setDisplayZoomControls(true);
        }

        settings.setDefaultFontSize(16);
        settings.setDefaultFixedFontSize(13);
        // settings.setPageCacheCapaci

        // WebView inside Browser doesn't want initial focus to be set.
        settings.setNeedInitialFocus(false);

        // 设置网页自适应屏幕
        settings.setUseWideViewPort(true);

        // 默认的是false，如果设置为true，必须重写WebChromeClint的onCreatWindow方法
        // 如果为false，则默认为当前webview加载新的页面

        // enable smooth transition for better performance during panning or
        // zooming
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            settings.setEnableSmoothTransition(true);
            // disable content url access
            settings.setAllowContentAccess(false);
        }

        // HTML5 API flags
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        // settings.setWorkersEnabled(true); // This only affects V8.

        // HTML5 configuration parametersettings.
        settings.setAppCacheMaxSize(1024 * 1024 * 50);
        settings.setAppCachePath(getAppCachePath());
        settings.setDatabasePath(getDir("databases", 0).getPath());
        settings.setGeolocationDatabasePath(getDir("geolocation", 0)
                .getPath());

    }

    private String getAppCachePath() {
        if (mAppCachePath == null) {
            mAppCachePath =getDir("appcache", 0).getPath();
        }
        return mAppCachePath;
    }



    @Override
    public void initListener() {
        backLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        System.exit(0);
    }
}
