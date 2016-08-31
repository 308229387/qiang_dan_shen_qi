package wuba.zhaobiao.mine.model;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.Utils;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.HelpActivity;

/**
 * Created by 58 on 2016/8/17.
 */
public class HelpModel extends BaseModel implements View.OnClickListener {

    private HelpActivity context;

    private View layout_back_head;
    private View back_layout;
    private TextView txt_head;

    private ProgressBar progress;
    private View  view_no_internet;

    private View rl_help_tel;

    private LinearLayout ll_webview_container;

    private WebView webView;

    private String url;

    public HelpModel(HelpActivity context){
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
        txt_head.setText(R.string.help);
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
        view_no_internet = context.findViewById(R.id.view_no_internet);
    }

    public void initHelpCall(){
        createHelpCall();
        setHelpCallListener();
    }

    private void createHelpCall(){
        rl_help_tel = context.findViewById(R.id.rl_help_tel);
    }

    private void setHelpCallListener(){
        rl_help_tel.setOnClickListener(this);
    }

    public void initWebViewContainer(){
        createWebViewContainer();
        createWebView();
        containsWebView();
        setWebViewProperty();
    }

    private void createWebViewContainer(){
        ll_webview_container = (LinearLayout) context.findViewById(R.id.ll_webview_container);
    }

    private void createWebView(){
        webView = new WebView(context);
    }

    private void containsWebView(){
        ll_webview_container.addView(webView);
    }

    private void setWebViewProperty(){
        webView.getSettings().setJavaScriptEnabled(true);
        if(NetUtils.isNetworkConnected(context)){
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        }else{
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//
        }
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setUseWideViewPort(true);

        String cacheDirPath = context.getFilesDir().getAbsolutePath()+"/help";
        //设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);

        webView.getSettings().setDefaultTextEncodingName("UTF-8"); // 设置默认的显示编码
        webView.setWebViewClient(new BaseWebClient());
        webView.setWebChromeClient(new WebChromeBaseClient());
    }

    public void initHelpPage(){
        initUrl();
        loadHelpPage();
    }

    private void initUrl(){
        url = Urls.HELP;
    }

    private void loadHelpPage(){
        if(!NetUtils.isNetworkConnected(context)){
            view_no_internet.setVisibility(View.VISIBLE);
        }
        webView.loadUrl(url);
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
            case R.id.rl_help_tel:
                ActivityUtils.goToDialActivity(context, Constans.HELP_TEL);
                break;

        }
    }

    private void back(){
        context.onBackPressed();
    }

    public void destoryWebView(){
        webView.removeAllViews();
        webView.destroy();
        webView = null;
        System.exit(0);//强制退出，防止内存泄露问题
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_HELP, context.stop_time - context.resume_time);
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
