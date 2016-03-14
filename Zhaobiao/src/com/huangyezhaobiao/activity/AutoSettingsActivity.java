package com.huangyezhaobiao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * Created by shenzhixin on 2015/11/21.
 * 自定义设置界面
 */
public class AutoSettingsActivity extends QBBaseActivity implements View.OnClickListener {
    private WebView        webView_auto_settings;
    private ProgressBar    progress;
private View           back_layout;
private TextView       txt_head;
private WebViewClient  baseWebClient;
private WebChromeBaseClient webChromeBaseClient;
private ZhaoBiaoDialog dialog;
private LinearLayout ll_webview_container;
private TextView       tv_no_internet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    public static Intent onNewIntent(Context context){
        Intent intent = new Intent(context,AutoSettingsActivity.class);
        return intent;
    }

    private int getLayoutId(){
        return R.layout.activity_auto_settings;
    }

    @Override
    public void initView() {
        setContentView(getLayoutId());
        ll_webview_container  = getView(R.id.ll_webview_container);
        layout_back_head      = getView(R.id.layout_head);
        back_layout           = getView(R.id.back_layout);
        txt_head              = getView(R.id.txt_head);
        progress              = getView(R.id.pb);
        tv_no_internet        = getView(R.id.tv_no_internet);
        webView_auto_settings = new WebView(this);
        ll_webview_container.addView(webView_auto_settings);
        baseWebClient         = new BaseWebClient();
        webChromeBaseClient   = new WebChromeBaseClient();
        webView_auto_settings.setWebViewClient(baseWebClient);
        webView_auto_settings.getSettings().setJavaScriptEnabled(true);
        webView_auto_settings.getSettings().setUseWideViewPort(true);
        webView_auto_settings.getSettings().setDefaultTextEncodingName("UTF-8"); // 设置默认的显示编码
        webView_auto_settings.setWebViewClient(baseWebClient);
        webView_auto_settings.setWebChromeClient(webChromeBaseClient);
        removeJSInterface();
        String url = URLConstans.AUTO_SETTINGS + UrlSuffix.getAutoSettingSuffix(this);
        Log.e("ashen", "url:" + url);
        if(NetUtils.isNetworkConnected(this)) {
            ll_webview_container.setVisibility(View.VISIBLE);
            webView_auto_settings.setVisibility(View.VISIBLE);
            tv_no_internet.setVisibility(View.GONE);
            webView_auto_settings.loadUrl(url);
        }else{
            ll_webview_container.setVisibility(View.GONE);
            webView_auto_settings.setVisibility(View.GONE);
            tv_no_internet.setVisibility(View.VISIBLE);
        }
        txt_head.setText("自定义设置");
        dialog               = new ZhaoBiaoDialog(this,"提示","确定要退出自定义设置么");
        dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                dialog.dismiss();
                finish();
            }

            @Override
            public void onDialogCancelClick() {
                dialog.dismiss();
            }
        });

    }

    /**
     * 显示判断系统版本 ，如果在4.2以下，就手动移除removeJavascriptInterface
     * 因为在4.3.1~3.0版本，webview默认添加了searchBoxJavaBridge_接口,
     */
    private void removeJSInterface() {
        if(Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                webView_auto_settings.removeJavascriptInterface("searchBoxJavaBridge_");
                webView_auto_settings.removeJavascriptInterface("accessibility");
                webView_auto_settings.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }
    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                //TODO:弹出对话框
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        dialog.show();
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
        Log.e("shenzhixin", "url:" + url);
        if(TextUtils.equals("zb://back",url)){//点击了保存完成的按钮，此时直接截获，让当前页面消失
            finish();
            return true;
        }
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
        progress.setProgress(newProgress);
        if(newProgress==100){
            progress.setVisibility(View.GONE);
        }else{
            if(progress.getVisibility() != View.VISIBLE){
                progress.setVisibility(View.VISIBLE);
            }

        }
        super.onProgressChanged(view, newProgress);
    }

}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView_auto_settings.removeAllViews();
        webView_auto_settings.destroy();
        webView_auto_settings = null;
        System.exit(0);
    }
}
