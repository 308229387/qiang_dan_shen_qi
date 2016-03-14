package com.huangyezhaobiao.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;

/**
 * Created by shenzhixin on 2015/12/7.
 */
public class SoftwareUsageActivity extends QBBaseActivity implements View.OnClickListener {
    private LinearLayout backLayout,ll_webview_container;
    private TextView txt_head;
    private WebView webView_introduce;
    private BaseWebClient client;
    private WebChromeBaseClient chromeBaseClient;
    private ProgressBar pb;
    private View view_no_internet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
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
        txt_head.setText("软件使用协议");
        //TODO:换成软件使用协议的地址即可
        String url = URLConstans.SOFTWARE_USAGE;
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
            return super.shouldOverrideUrlLoading(view, url);
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
        System.exit(0);
    }

}
