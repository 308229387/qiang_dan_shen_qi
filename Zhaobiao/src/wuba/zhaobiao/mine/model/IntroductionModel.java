package wuba.zhaobiao.mine.model;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
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
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.Utils;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.IntroductionActivity;

/**
 * Created by 58 on 2016/8/17.
 */
public class IntroductionModel extends BaseModel implements View.OnClickListener{

    private IntroductionActivity context;

    private  View layout_back_head;
    private LinearLayout backLayout;
    private TextView txt_head;

    private ProgressBar progress;

    private View view_no_internet;

    private LinearLayout ll_webview_container;
    private WebView webView_introduce;

    private String url;

    public IntroductionModel(IntroductionActivity context){
        this.context = context;
    }

    public void initHeader(){
        createHeader();
        initBack();
        createTitle();
    }

    private void createHeader(){
        layout_back_head = context.findViewById(R.id.layout_head);
    }

    private void initBack(){
       createBack();
       setBackListener();
    }

    private void createBack(){
        backLayout = (LinearLayout) context.findViewById(R.id.back_layout);
        backLayout.setVisibility(View.VISIBLE);
    }

    private void setBackListener(){
        backLayout.setOnClickListener(this);
    }

    private void createTitle(){
        txt_head   = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText(R.string.introduction);
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
        view_no_internet =  context.findViewById(R.id.view_no_internet);
    }

    public void initWebViewContainer(){
        createWebViewContainer();
        createWebView();
        setWebViewProperty();
        removeJSInterface();
    }

    private void createWebViewContainer(){
        ll_webview_container = (LinearLayout) context.findViewById(R.id.ll_webview_container);
    }

    private void createWebView(){
        webView_introduce = (WebView) context.findViewById(R.id.webview);
    }

    private void setWebViewProperty(){
        webView_introduce.getSettings().setJavaScriptEnabled(true);
        webView_introduce.getSettings().setUseWideViewPort(true);
        webView_introduce.getSettings().setDefaultTextEncodingName("UTF-8"); // 设置默认的显示编码
        webView_introduce.setWebViewClient(new BaseWebClient());
        webView_introduce.setWebChromeClient(new WebChromeBaseClient());
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

    public void initIntorductionPage(){
        initUrl();
        loadIntroductionPage();
    }

    private void initUrl(){
        url = Urls.Introduction;
    }

    private void loadIntroductionPage(){
        if(!NetUtils.isNetworkConnected(context)){
            view_no_internet.setVisibility(View.VISIBLE);
        }
        webView_introduce.loadUrl(url);
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
            case R.id.back_layout:
                back();
                break;
        }
    }

    private void back(){
        context.onBackPressed();
    }

    public void destoryWebView(){
        webView_introduce.removeAllViews();
        webView_introduce.destroy();
        webView_introduce = null;
        System.exit(0);
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_INTRODUCTION, context.stop_time - context.resume_time);
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

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }
    }
}
