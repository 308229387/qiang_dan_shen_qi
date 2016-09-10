package com.huangyezhaobiao.iview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.OrderCategoryAdapter;
import com.huangyezhaobiao.adapter.OrderStateAdapter;
import com.huangyezhaobiao.bean.mylist.Order.OrderStateEntity;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import wuba.zhaobiao.order.model.OrderModel;


public class OrderCatePopupWindow extends PopupWindow implements OnClickListener {
	
	private View conentView;  
	private OnOrderCatePopupWindowItemClick itemOrderCateClick;
//	private TextView mTVWateService;
//	private TextView mTVServicing;
//	private TextView mTVServiced;
	private TextView mTVReset;
    private TextView mTVConfirm;
    private LinearLayout ll_order_category;
    private GridView gridView_order_category,gridView_order_state;
    private OrderCategoryAdapter orderCategoryAdapter;
    private OrderStateAdapter orderStateAdapter;

    private List<OrderStateEntity> categoryList = new ArrayList<>();
    private List<OrderStateEntity> stateList = new ArrayList<>();

    public static Boolean isBidding = true;

    public OrderCatePopupWindow(final Activity context,int resLayout,OnOrderCatePopupWindowItemClick itemClick,String type) {
        this.itemOrderCateClick = itemClick;

        initCategoryData();
        initOrderStateData();

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

        mTVReset = (TextView) conentView.findViewById(R.id.tv_ordercate_popup_reset);
        mTVReset.setOnClickListener(this);
        mTVConfirm = (TextView) conentView.findViewById(R.id.tv_orderState_confirm);
        mTVConfirm.setOnClickListener(this);

        ll_order_category  = (LinearLayout) conentView.findViewById(R.id.ll_order_category);
        ll_order_category.setVisibility(View.VISIBLE);

        gridView_order_category = (GridView) conentView.findViewById(R.id.gridView_order_category);
        orderCategoryAdapter = new OrderCategoryAdapter(context, categoryList);
        gridView_order_category.setAdapter(orderCategoryAdapter);


        gridView_order_state = (GridView) conentView.findViewById(R.id.gridView_order_state);
        orderStateAdapter = new OrderStateAdapter(context, stateList);
        gridView_order_state.setAdapter(orderStateAdapter);

        gridView_order_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderStateEntity  entity = (OrderStateEntity)orderCategoryAdapter.getItem(position);
                String orderId = entity.getOrderStateId();
                if (OrderModel.CategoryCheckedId.contains(orderId)) {//之前选中,变成不选中
//                    OrderModel.CategoryCheckedId.remove(orderId);
                } else {//之前没选中，变成选中

                    if(TextUtils.equals(orderId,"1")){
                        isBidding = true;
                    }else if(TextUtils.equals(orderId,"2")){
                        isBidding = false;
                    }
                    OrderModel.CategoryCheckedId.clear();
                    OrderModel.CategoryCheckedId.add(orderId);
                }
                orderCategoryAdapter.notifyDataSetChanged();

                if(isBidding){
                    initOrderStateData();
                    orderStateAdapter = new OrderStateAdapter(context, stateList);
                }else{
                    initBusinessStateData();
                    orderStateAdapter = new OrderStateAdapter(context, stateList);
                }
                gridView_order_state.setAdapter(orderStateAdapter);
                OrderModel.stateCheckedId.clear();
                orderStateAdapter.notifyDataSetChanged();
            }
        });

        mTVReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LogV("tag", "重置被点击");
                OrderModel.CategoryCheckedId.clear();
                OrderModel.CategoryCheckedId.add("1");
                orderCategoryAdapter.notifyDataSetChanged();

                OrderModel.orderState = "0";
                OrderModel.stateCheckedId.clear();
                orderStateAdapter.notifyDataSetChanged();

                HYMob.getDataList(context, HYEventConstans.EVENT_ID_FILTER_RESET);
            }
        });


    }


    private void initCategoryData(){
        OrderStateEntity entity1 = new OrderStateEntity();
        entity1.setOrderStateId("1");
        entity1.setOrderState("抢单");
        categoryList.add(entity1);
        OrderStateEntity entity2 = new OrderStateEntity();
        entity2.setOrderStateId("2");
        entity2.setOrderState("商机");
        categoryList.add(entity2);
    }

    private void initOrderStateData(){
        stateList.clear();
            OrderStateEntity entity1 = new OrderStateEntity();
            entity1.setOrderStateId("1");
            entity1.setOrderState("待服务");
            stateList.add(entity1);
            OrderStateEntity entity2 = new OrderStateEntity();
            entity2.setOrderStateId("2");
            entity2.setOrderState("服务中");
            stateList.add(entity2);
            OrderStateEntity entity3 = new OrderStateEntity();
            entity3.setOrderStateId("3");
            entity3.setOrderState("已结束");
            stateList.add(entity3);

    }

    private void initBusinessStateData(){
        stateList.clear();
        OrderStateEntity entity4 = new OrderStateEntity();
        entity4.setOrderStateId("4");
        entity4.setOrderState("未分类");
        stateList.add(entity4);
        OrderStateEntity entity5 = new OrderStateEntity();
        entity5.setOrderStateId("5");
        entity5.setOrderState("待跟进");
        stateList.add(entity5);
        OrderStateEntity entity6 = new OrderStateEntity();
        entity6.setOrderStateId("6");
        entity6.setOrderState("已完结");
        stateList.add(entity6);
    }


  
    
    public interface OnOrderCatePopupWindowItemClick{
    	void onOrderWindowItemClick(View v);
    }

	@Override
	public void onClick(View v) {
		itemOrderCateClick.onOrderWindowItemClick(v);
	}
}
