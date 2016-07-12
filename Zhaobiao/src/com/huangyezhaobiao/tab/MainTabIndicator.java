package com.huangyezhaobiao.tab;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;

import org.apache.http.util.VersionInfo;

/**
 * Created by 58 on 2016/6/16.
 */
public class MainTabIndicator extends LinearLayout implements View.OnClickListener{
    private OnTabSelectedListener mOnTabSelectedListener;
    private MainTabIndicatorBean mMainNavigateTab;

    public MainTabIndicator(Context context) {
        this(context,null);
    }

    public MainTabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void adddTab(int paramInt,MainTabIndicatorBean.TabParam paramTabParam){
        int i = R.layout.home_tab_view;
        View localView = LayoutInflater.from(getContext()).inflate(i, null);
        localView.setFocusable(true);
        ViewHolder localViewHolder = new ViewHolder();
        localViewHolder.index = paramInt;
        localViewHolder.pageParam = paramTabParam;
        localViewHolder.tabIcon = (ImageView) localView.findViewById(R.id.iv_tab_icon);
        localViewHolder.tabTitle = (TextView) localView.findViewById(R.id.tv_tab_title);
        localViewHolder.tabNewTag = (TextView)localView.findViewById(R.id.tv_new_tag);
        if (paramTabParam.iconResId > 0) {
            localViewHolder.tabIcon.setImageResource(paramTabParam.iconResId);
        }
        localViewHolder.tabTitle.setTextColor(BiddingApplication.getBiddingApplication().getResources().getColor(R.color.home_tab_text_normal_color));
        if (TextUtils.isEmpty(paramTabParam.title)) {
            localViewHolder.tabTitle.setVisibility(View.GONE);
        } else {
            localViewHolder.tabTitle.setVisibility(View.VISIBLE);
            localViewHolder.tabTitle.setText(paramTabParam.title);
        }

        if (paramTabParam.backgroundColor > 0) {
            localView.setBackgroundResource(paramTabParam.backgroundColor);
        }
        localView.setTag(localViewHolder);
        localView.setOnClickListener(this);
        addView(localView, new LayoutParams(-1, -1, 1.0F));
    }

    private ViewHolder getTabViewHolder(int paramInt){
        try {
            Object localObject = getTabView(paramInt).getTag();
            if((localObject != null) && ((localObject instanceof ViewHolder))){
                ViewHolder localViewholder = (ViewHolder)localObject;
                return localViewholder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }

    public void showNewTag(int paramInt,int unReadCount){
        ViewHolder vh = getTabViewHolder(paramInt);
        if(vh != null && vh.tabNewTag != null){
            vh.tabNewTag.setText(unReadCount+"");
            vh.tabNewTag.setVisibility(View.VISIBLE);
            this.postInvalidate();
        }
    }

    public void hideNewTag(int paramInt){
        ViewHolder vh = getTabViewHolder(paramInt);
        if(vh != null && vh.tabNewTag != null){
            vh.tabNewTag.setVisibility(View.GONE);
        }
    }

    public View getTabView(int paramInt){
        try {
            if(getChildCount() > paramInt){
                View localView = getChildAt(paramInt);
                return localView;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCurrentTab(int paramInt){
        int count = getChildCount();
        if (paramInt <0 || paramInt >= count){
            return;
        }

        for (int i =0 ; i < count; i++){
            Object localObject = getChildAt(i).getTag();
            ViewHolder localViewHolder;
            if((localObject != null) && ((localObject instanceof ViewHolder))){
                localViewHolder = (ViewHolder) localObject;
                if((localViewHolder.pageParam != null) && (localViewHolder.tabIcon != null)){
                    if(localViewHolder.index != paramInt){
                        if (localViewHolder.pageParam.iconResId > 0){
                            localViewHolder.tabIcon.setImageResource(localViewHolder.pageParam.iconResId);
                        }
                        localViewHolder.tabTitle.setTextColor(BiddingApplication.getBiddingApplication().getResources().getColor(R.color.home_tab_text_normal_color));
                    }else{
                        if(localViewHolder.pageParam.iconSelectedResId >0){
                            localViewHolder.tabIcon.setImageResource(localViewHolder.pageParam.iconSelectedResId);
                        }
                        localViewHolder.tabTitle.setTextColor(BiddingApplication.getBiddingApplication().getResources().getColor(R.color.home_tab_text_selected_color));
                    }
                }
            }
        }
    }

    public void onClickSelectedTab(int paramInt){
        if(mOnTabSelectedListener != null){
            mOnTabSelectedListener.onTabReselected(paramInt);
        }

    }

    public MainTabIndicatorBean getMainNavigateTab(){
        return this.mMainNavigateTab;
    }

     public void setNavigateTab(MainTabIndicatorBean paramMainNavigateTab){
         if((paramMainNavigateTab != null) && (paramMainNavigateTab.getTabParams() != null)
                 && (paramMainNavigateTab.getTabParams().size() > 0)){
             this.mMainNavigateTab = paramMainNavigateTab;
             removeAllViews();
             for(int i = 0 ; i < this.mMainNavigateTab.getTabParams().size();i++)
                 adddTab(i,(MainTabIndicatorBean.TabParam)this.mMainNavigateTab.getTabParams().get(i));
         }
     }
    @Override
    public void onClick(View v) {
        Object localObject = v.getTag();
        ViewHolder localViewHolder;
        if ((localObject != null) &&(localObject instanceof ViewHolder)){
            localViewHolder = (ViewHolder) localObject;
            if (localViewHolder.pageParam != null){
                onClickSelectedTab(localViewHolder.index);
            }
        }
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener){
        this.mOnTabSelectedListener = listener;
    }
    public static abstract interface OnTabSelectedListener{
        public abstract void onTabReselected(int paramInt);
    }

    private static class ViewHolder{
        public int index;
        public MainTabIndicatorBean.TabParam pageParam;
        public ImageView tabIcon;
        public TextView tabTitle;
        public TextView tabNewTag;
    }
}
