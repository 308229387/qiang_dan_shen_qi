package com.huangyezhaobiao.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
/**
 * webView的缓存机制:
 * 					1.首先判断缓存文件是否存在，不存在直接读取网络数据---如何判断缓存文件是否存在
 * 					2.缓存文件存在,判断有无网络
 * 								有网络:从网络中获取
 * 								没有网络:从本地获取----如何从本地获取，本地的存到哪了
 * @author 58
 *
 */
public class HelpActivity extends QBBaseActivity implements onDialogClickListener, OnClickListener{
	private static final String APP_CACAHE_DIRNAME = "/help";
	private WebView webView;
	private LinearLayout back_layout,ll_webview_container;
	private TextView  txt_head;
	private BaseWebClient client;
	private ZhaoBiaoDialog dialog;
	private ProgressBar pb;
	private View rl_help_tel;
	private WebChromeBaseClient webChromeBaseClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		initView();
		initListener();
		dialog = new ZhaoBiaoDialog(this, "", "4009-321-112");
		dialog.setOnDialogClickListener(this);

	}

	@Override
	public void initView() {
		ll_webview_container = getView(R.id.ll_webview_container);
		layout_back_head = getView(R.id.layout_head);
		pb          = getView(R.id.pb);
		rl_help_tel = getView(R.id.rl_help_tel);
		//webView 	= getView(R.id.webview);
		webView     = new WebView(getApplicationContext());
		ll_webview_container.addView(webView);
		back_layout = getView(R.id.back_layout);
		txt_head    = getView(R.id.txt_head);
		tbl         = getView(R.id.tbl);
		client      = new BaseWebClient();
		webView.getSettings().setJavaScriptEnabled(true);
		webChromeBaseClient = new WebChromeBaseClient();
		if(NetUtils.isNetworkConnected(this)){
			webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
		}else{
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//
		}
		// 开启 DOM storage API 功能
		webView.getSettings().setDomStorageEnabled(true);
		//开启 database storage API 功能
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
		//设置数据库缓存路径 
		webView.getSettings().setDatabasePath(cacheDirPath);
		//设置  Application Caches 缓存目录
		webView.getSettings().setAppCachePath(cacheDirPath);
		//开启 Application Caches 功能
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("UTF-8"); // 设置默认的显示编码
		webView.setWebViewClient(client);
		webView.setWebChromeClient(webChromeBaseClient);
		txt_head.setText(R.string.help);
		String url = URLConstans.HELP_URL + UrlSuffix.getHelpSuffix();
		LogUtils.LogE("ashenTag", "url:" + url);
		webView.loadUrl(url);
	}

	@Override
	public void initListener() {
		back_layout.setOnClickListener(this);
		rl_help_tel.setOnClickListener(this);
	}

	private class BaseWebClient extends WebViewClient{
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

	}


	private class WebChromeBaseClient  extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			LogUtils.LogE("shenzhixinPro","progress:"+newProgress);
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

	}

	@Override
	public void onDialogOkClick() {
		if(exitDialog!=null && exitDialog.isShowing()){
			super.onDialogOkClick();
		}else {
			dialog.dismiss();
			ActivityUtils.goToDialActivity(this, Constans.HELP_TEL);
		}
	}

	@Override
	public void onDialogCancelClick() {
		dialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_layout:
				onBackPressed();
				break;
			case R.id.rl_help_tel:
				ActivityUtils.goToDialActivity(this, Constans.HELP_TEL);
				break;

		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		webView.removeAllViews();
		webView.destroy();
		webView = null;
		if(dialog!=null){
			dialog=null;
		}
		System.exit(0);//强制退出，防止内存泄露问题
	}
}
