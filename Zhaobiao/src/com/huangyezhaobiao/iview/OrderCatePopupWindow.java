package com.huangyezhaobiao.iview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.OrderStateAdapter;
import com.huangyezhaobiao.bean.mylist.OrderState.OrderStateEntity;
import com.huangyezhaobiao.fragment.home.OrderListFragment;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class OrderCatePopupWindow extends PopupWindow implements OnClickListener {
	
	private View conentView;  
	private OnOrderCatePopupWindowItemClick itemOrderCateClick;
//	private TextView mTVWateService;
//	private TextView mTVServicing;
//	private TextView mTVServiced;
	private TextView mTVReset;
    private TextView mTVConfirm;
    private GridView gridView_order_state;
    private OrderStateAdapter adapter;

    private List<OrderStateEntity> list = new ArrayList<>();
	
    public OrderCatePopupWindow(final Activity context,int resLayout,OnOrderCatePopupWindowItemClick itemClick,String type) {
        this.itemOrderCateClick = itemClick;
        initData();
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        conentView = inflater.inflate(resLayout, null);  

        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);  
        this.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
         /*设置点击menu以外其他地方以及返回键退出*/
        this.setFocusable(true);
         /*设置触摸外面时消失*/
        this.setOutsideTouchable(true);

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.7f;
        context.getWindow().setAttributes(lp);

        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });
//        //设置背景颜色(外部点击必须的)
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#00000000"));
        this.setBackgroundDrawable(dw);

//        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.iv_select_background));
        // 设置弹出窗体动画效果
        this.setAnimationStyle(android.R.style.Animation_Dialog);

//        //设置监听
//        this.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() ==MotionEvent.ACTION_OUTSIDE){
//                    //如果焦点不在popupWindow上，且点击了外面，不再往下dispatch事件：
//                    //不做任何响应,不 dismiss popupWindow
//                    return true;
//                }
//                //否则default，往下dispatch事件:关掉popupWindow，
//                return false;
//            }
//        });

        // 刷新状态
        this.update();

//
        mTVReset = (TextView) conentView.findViewById(R.id.tv_ordercate_popup_reset);
        mTVReset.setOnClickListener(this);
        mTVConfirm = (TextView) conentView.findViewById(R.id.tv_orderState_confirm);
        mTVConfirm.setOnClickListener(this);
        gridView_order_state = (GridView) conentView.findViewById(R.id.gridView_order_state);
        adapter = new OrderStateAdapter(context,list);
        gridView_order_state.setAdapter(adapter);


        mTVReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LogV("tag", "重置被点击");
                OrderListFragment.orderState = "0";
                OrderListFragment.checkedId.clear();
                adapter.notifyDataSetChanged();

                HYMob.getDataList(context, HYEventConstans.EVENT_ID_FILTER_RESET);
            }
        });


    }

    private void initData(){
        OrderStateEntity entity1 = new OrderStateEntity();
        entity1.setOrderStateId("1");
        entity1.setOrderState("待服务");
        list.add(entity1);
        OrderStateEntity entity2 = new OrderStateEntity();
        entity2.setOrderStateId("2");
        entity2.setOrderState("服务中");
        list.add(entity2);
        OrderStateEntity entity3 = new OrderStateEntity();
        entity3.setOrderStateId("3");
        entity3.setOrderState("已结束");
        list.add(entity3);
    }
  
    
    public interface OnOrderCatePopupWindowItemClick{
    	void onOrderWindowItemClick(View v);
    }

	@Override
	public void onClick(View v) {
		itemOrderCateClick.onOrderWindowItemClick(v);
	}
}
