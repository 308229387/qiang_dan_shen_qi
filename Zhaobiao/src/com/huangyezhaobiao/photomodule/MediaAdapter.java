package com.huangyezhaobiao.photomodule;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/9/24.
 */
public class MediaAdapter extends BaseAdapter {
    //允许的最大数
    private int maxCount;
    private List<BaseMediaBean> mBeans;
    private MediaAddBean addBean;
    private Context mContext;
    private LayoutInflater inflater;
    private boolean editable;
    private int mItemWidth;
    private int mItemHeight;
    private int mPicSizeMode = ImageUrlUtil.SizeMode.S;
    private OnItemClickListener mItemClickListener;

    public List<BaseMediaBean> getDataSources(){
        return mBeans;
    }

    public MediaAdapter(Context context, List<BaseMediaBean> beans, boolean editable, int maxCount) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mBeans = new ArrayList<>();
        mBeans.addAll(beans);
        this.editable = editable;
        this.maxCount = maxCount;
        addBean = new MediaAddBean();
        if (editable && mBeans.size() < maxCount) {
            mBeans.add(0, addBean);
        }
    }

    public MediaAdapter(Context context, List<BaseMediaBean> beans) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mBeans = beans;
    }

    public void refreshDatas(List<BaseMediaBean> beans) {
        mBeans.clear();
        if (beans != null && !beans.isEmpty()) {
            mBeans.addAll(beans);
            Log.e("sss", "size:" + beans.size());
        }
        if (mBeans.size() < maxCount && editable) {
            mBeans.add(0, addBean);
        }
        notifyDataSetChanged();
    }

    public void setItemSize(int itemWidth, int itemHeight) {
        mItemWidth = itemWidth;
        mItemHeight = itemHeight;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemSize(int itemWidth, int itemHeight, int sizeMode) {
        mItemWidth = itemWidth;
        mItemHeight = itemHeight;
        mPicSizeMode = sizeMode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mBeans != null) {
            return mBeans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mBeans != null) {
            return mBeans.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(getLayoutId(), parent, false);
            holder.ll_root = convertView.findViewById(R.id.ll_root);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            holder.iv_item = (ImageView) convertView.findViewById(R.id.iv_item_bean);
            holder.iv_close = (ImageView) convertView.findViewById(R.id.iv_close);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        resize(holder);
        final BaseMediaBean bean = mBeans.get(position);
        if (bean.getType() == MultiMediaSD.MEDIA_TYPE_ADD) {
            //ImageLoader.getInstance().cancelDisplayTask(holder.iv_item);
            holder.iv_item.setImageResource(R.drawable.ic_add_selector);
            holder.iv_close.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(bean.getUrl())) {
                if (!bean.getUrl().startsWith("http")) {
                    String uri = "file://" + bean.getUrl();
                    ImageLoader.getInstance().displayImage(uri, holder.iv_item, ImageLoaderOptionsUtil.PIC_IMG_OPTIONS);
                    holder.iv_close.setVisibility(View.VISIBLE);
                } else {
                    String picUrl = ImageUrlUtil.getImageUrl(bean.getUrl(), ImageUrlUtil.ImageType.LIST_ITEM_MEDIA_PIC, mPicSizeMode);
                    ImageLoader.getInstance().displayImage(picUrl, holder.iv_item, ImageLoaderOptionsUtil.PIC_IMG_OPTIONS);
                }
            }
        }
        if (!editable || bean instanceof MediaAddBean) {//不可编辑状态
            holder.iv_close.setVisibility(View.GONE);
        } else {
            holder.iv_close.setVisibility(View.VISIBLE);
        }
        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventAction action = new EventAction(EventType.EVENT_GRIDVIEW_ITEM_CLOSE, bean);
                EventbusAgent.getInstance().post(action);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position, bean);
                } else {
                    EventAction action = null;
                    if(bean.getType() == MultiMediaSD.MEDIA_TYPE_ADD){
                        action = new EventAction(EventType.EVENT_GRIDVIEW_ITEM_ADD,bean);
                    }else {
                        if (editable) {
                            action = new EventAction(EventType.EVENT_GRIDVIEW_ITEM, bean);
                        } else {
                            action = new EventAction(EventType.EVENT_GRIDVIEW_ITEM_VIEW, bean);
                        }
                    }
                    EventbusAgent.getInstance().post(action);
                }
            }
        });
        return convertView;
    }

    private void resize(ViewHolder viewHolder) {
        if (mItemWidth <= 0) {
            mItemWidth = mContext.getResources()
                    .getDimensionPixelOffset(R.dimen.grid_scroll_view_length);
        }
        if (mItemHeight <= 0) {
            mItemHeight = mContext.getResources()
                    .getDimensionPixelOffset(R.dimen.grid_scroll_view_length);
        }
        viewHolder.ll_root.getLayoutParams().width = mItemWidth;
        viewHolder.ll_root.getLayoutParams().height = mItemHeight;
        viewHolder.rl_item.getLayoutParams().width = mItemWidth;
        viewHolder.rl_item.getLayoutParams().height = mItemHeight;
        viewHolder.iv_item.getLayoutParams().width = mItemWidth;
        viewHolder.iv_item.getLayoutParams().height = mItemHeight;
        viewHolder.iv_item.setAdjustViewBounds(true);
        viewHolder.iv_item.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        private int getLayoutId() {
            return R.layout.item_media_bean;
        }

         public void releaseAllSources() {

         }

    public class ViewHolder {
            private View ll_root;
            public View rl_item;
            public ImageView iv_item;
            public ImageView iv_close;
        }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, BaseMediaBean bean);
    }
}
