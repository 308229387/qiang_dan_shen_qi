package wuba.zhaobiao.mine.model;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.AutoSettingActivity;

/**
 * Created by 58 on 2016/8/11.
 */
public class AutoSettingModel extends BaseModel implements View.OnClickListener{

    private AutoSettingActivity context;

    private View layout_back_head;
    private View back_layout;
    private TextView txt_head;
    private ProgressBar progress;
    private TextView  tv_no_internet;
    private LinearLayout ll_webview_container;

    private WebView webView_auto_settings;

    private String url;

    public AutoSettingModel(AutoSettingActivity context){
        this.context = context;
    }

    public void initHeader() {
        createHeader();
        initBack();
        createTitle();
    }

    private void createHeader(){
        layout_back_head  = context.findViewById(R.id.layout_head);
    }

    private void initBack(){
        createBack();
        setBackListener();
    }

    private void createBack(){
        back_layout = context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setBackListener(){
        back_layout.setOnClickListener(this);
    }

    private void createTitle(){
        txt_head = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText("自定义设置");
    }

    public  void initProgressBar(){
        createProgressBar();
    }

    private void createProgressBar(){
        progress = (ProgressBar) context.findViewById(R.id.pb);
    }


    public void initNoInternetStatus(){
        createNoInternetStatus();
    }

    private void createNoInternetStatus(){
        tv_no_internet = (TextView) context.findViewById(R.id.tv_no_internet);
    }

    public void initWebViewContainer(){
        createWebViewContainer();
        createWebView();
        containsWebView();
        setWebViewProperty();
        removeJSInterface();
    }

    private void createWebViewContainer(){
        ll_webview_container  = (LinearLayout) context.findViewById(R.id.ll_webview_container);
    }

    private void createWebView(){
        webView_auto_settings = new WebView(context);
    }

    private void containsWebView(){
        ll_webview_container.addView(webView_auto_settings);
    }

    private void setWebViewProperty(){
        webView_auto_settings.getSettings().setJavaScriptEnabled(true);
        webView_auto_settings.getSettings().setUseWideViewPort(true);
        webView_auto_settings.getSettings().setDefaultTextEncodingName("UTF-8"); // 设置默认的显示编码
        webView_auto_settings.setWebViewClient(new BaseWebClient());
        webView_auto_settings.setWebChromeClient(new WebChromeBaseClient());
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

    public void initAutoSettingPage(){
        initUrl();
        loadAutoSettingPage();
    }

    private void initUrl(){
        url = Urls.AUTO_SETTING + UrlSuffix.getAutoSettingSuffix(context);
    }

    private void loadAutoSettingPage(){
        if(NetUtils.isNetworkConnected(context)) {
            loadNetConnectPage();
        }else{
            loadNetUnConnectPage();
        }
    }

    private void loadNetConnectPage(){
        ll_webview_container.setVisibility(View.VISIBLE);
        webView_auto_settings.setVisibility(View.VISIBLE);
        tv_no_internet.setVisibility(View.GONE);
        webView_auto_settings.loadUrl(url);
    }

    private void loadNetUnConnectPage(){
        ll_webview_container.setVisibility(View.GONE);
        webView_auto_settings.setVisibility(View.GONE);
        tv_no_internet.setVisibility(View.VISIBLE);
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
        switch (v.getId()){
            case R.id.back_layout:
                back();
                break;
        }
    }

    private void back(){
        //TODO:弹出对话框
        context.onBackPressed();
    }

    public void initSettingDialog(){
        final ZhaoBiaoDialog settingDialog  = new ZhaoBiaoDialog(context,"确定要退出自定义设置么");
        settingDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                settingDialog.dismiss();
                context.finish();
            }

            @Override
            public void onDialogCancelClick() {
                settingDialog.dismiss();
            }
        });
        settingDialog.show();
    }

    public void detoryWebView(){
        webView_auto_settings.removeAllViews();
        webView_auto_settings.destroy();
        webView_auto_settings = null;
        System.exit(0);
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans. PAGE_AUTO_SETTING, context.stop_time - context.resume_time);
    }

    private class BaseWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            stopLoading();
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            startLoading();
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(TextUtils.equals("zb://back", url)){//点击了保存完成的按钮，此时直接截获，让当前页面消失
                context.finish();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

        }
    }

    private class WebChromeBaseClient  extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
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


}
