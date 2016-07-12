package com.huangyezhaobiao.tab;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 58 on 2016/6/16.
 */
public class MainTabViewPager extends ViewPager {

    private boolean isCanScroll = false;

    public MainTabViewPager(Context context) {
        super(context);
    }

    public MainTabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isCanScroll){
            return super.onTouchEvent(ev);
        }else{
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isCanScroll) {
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }
    }
}
