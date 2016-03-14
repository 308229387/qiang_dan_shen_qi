package com.huangyezhaobiao.windowf;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huangyezhaobiao.R;

/**
 * Created by shenzhixin on 2016/3/8.
 * 后台service, 当应用在后台时,开启service显示这个悬浮窗
 *             当应用在前台时，关闭service也关闭这个悬浮窗
 */
public class AppExitService extends Service{
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //开启悬浮窗
        Toast.makeText(this,"service open",0).show();
        openFloatWindow();
    }

    /**
     * 开启悬浮窗的方法
     */
    private void openFloatWindow() {
        if(mWindowManager==null) {
            wmParams = new WindowManager.LayoutParams();
            //获取的是WindowManagerImpl.CompatModeWrapper
            mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            //设置图片格式，效果为背景透明
            wmParams.format = PixelFormat.RGBA_8888;
            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE /*| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED*/;
            //调整悬浮窗显示的停靠位置为左侧置顶
            wmParams.gravity = Gravity.LEFT | Gravity.TOP;
            // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
            wmParams.x = 0;
            wmParams.y = 0;
            //设置悬浮窗口长宽数据
            wmParams.width = 1;
            wmParams.height = 1;
            mFloatLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.window_float, null);
            mWindowManager.addView(mFloatLayout, wmParams);
            mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                    .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭悬浮窗
        closeFloatWindow();
    }

    /**
     * 关闭悬浮窗的方法
     */
    private void closeFloatWindow() {
        if(mWindowManager!=null) {
            if (mFloatLayout != null) {
                //移除悬浮窗口
                mWindowManager.removeView(mFloatLayout);
                mWindowManager = null;
            }
        }
    }
}
