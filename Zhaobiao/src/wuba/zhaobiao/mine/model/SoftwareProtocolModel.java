package wuba.zhaobiao.mine.model;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import java.util.HashMap;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.SoftwareProtocolActivity;

/**
 * Created by 58 on 2016/8/18.
 */
public class SoftwareProtocolModel extends BaseModel implements View.OnClickListener{

    private SoftwareProtocolActivity context;

    private View layout_back_head;
    private LinearLayout backLayout;
    private TextView txt_head;
    private ProgressBar progress;
    private View view_no_internet;
    private LinearLayout ll_webview_container;
    private WebView webView_introduce;

    private String mTitle;
    private String url;

    private ZhaoBiaoDialog dialog;

    public SoftwareProtocolModel(SoftwareProtocolActivity context){
        this.context = context;
    }

    public void statisticsStartTime() {
        HYMob.getDataListByProtocol(context, HYEventConstans.USEAGE_PROTOCOL, UserUtils.getUserName(context));

    }

    public void initHeader() {
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

        if(TextUtils.isEmpty(context.getIntent().getStringExtra(AppConstants.H5_TITLE))){
            mTitle = context.getString(R.string.h5_login_softwareusage);
        }else{
            mTitle = context.getIntent().getStringExtra(AppConstants.H5_TITLE);
        }

        txt_head.setText(mTitle);
    }

    public void initProgressBar(){
        createProgressBar();
    }

    private void createProgressBar(){
        progress = (ProgressBar) context.findViewById(R.id.pb);

    }

    public void initNoInternetStatus(){
        createNoInternetStatus();
    }

    private void createNoInternetStatus(){
        view_no_internet = context.findViewById(R.id.view_no_internet);
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

    public void initSoftwareProtocolPage(){
        initUrl();
        loadSoftwareProtocolPage();
    }

    private void initUrl(){
        //TODO:换成软件使用协议的地址即可
        if(TextUtils.isEmpty(context.getIntent().getStringExtra(AppConstants.H5_WEBURL))){
            url = Urls.PROTOCOL;
        }else{
            url = context.getIntent().getStringExtra(AppConstants.H5_WEBURL);
        }
    }

    private void loadSoftwareProtocolPage(){
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
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_PROTOCOL, context.stop_time - context.resume_time);
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
            if(url.startsWith(AppConstants.H5_FAST_LOGIN)) {
                HashMap<String, String> map = new HashMap<>();
                map.put(AppConstants.H5_TITLE, "注册");
                map.put(AppConstants.H5_WEBURL, url);
                ActivityUtils.goToActivityWithString(context, SoftwareProtocolActivity.class, map);
            } else if(url.startsWith("tel:")) {
                final String urlTel = url.split(":")[1];
                // 拨打电话
                if(dialog == null || !dialog.isShowing()){
                    dialog = new ZhaoBiaoDialog(context,context.getString(R.string.make_sure_tel));
                    dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
                        @Override
                        public void onDialogOkClick() {
                            ActivityUtils.goToDialActivity(context, urlTel);
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
                ToastUtils.showToast(context.getString(R.string.h5_register_success));
                context.finish();
            }
            return true;
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
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            ToastUtils.showToast("onJsAlert()" + message);
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            ToastUtils.showToast("onJsConfirm()" + message);
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            ToastUtils.showToast("onJsPrompt()" + message);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }
    }
}
