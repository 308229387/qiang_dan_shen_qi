package com.huangye.commonlib.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * 所有的基类
 * @author shenzhixin
 *
 */
public abstract class BaseActivity extends Activity {
	public boolean isConfig = true;
	private boolean isInit = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowConfig(isConfig);


    }
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();

    	
    }
    
    private void requestWindowConfig(boolean isConfig) {
    	if(isConfig){
    		requestWindowFeature(Window.FEATURE_NO_TITLE);
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	}
	}
    
    public <T> T getView(int id){
    	return (T) findViewById(id);
    }

	public abstract void initView();
    public abstract void initListener();


}
