package com.huangyezhaobiao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ListView;

/**
 * Created by shenzhixin on 2015/9/15.
 * listview which is bounced
 */
public class BounceListView extends ListView{
    public BounceListView(Context context) {
        super(context);
    }

    public BounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BounceListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,getResources().getDisplayMetrics()), isTouchEvent);
    }
}
