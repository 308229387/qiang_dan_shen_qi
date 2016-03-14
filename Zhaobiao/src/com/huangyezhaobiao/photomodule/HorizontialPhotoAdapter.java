package com.huangyezhaobiao.photomodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.huangyezhaobiao.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/9/29.
 * 浏览模式下的底部adapter
 */
public class HorizontialPhotoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<MediaPicBean> beans = new ArrayList<>();
    private int                index_select = -1;

    public HorizontialPhotoAdapter(Context context,List<MediaPicBean> beans){
        this.context = context;
        this.beans.addAll(beans);
        mInflater    = LayoutInflater.from(context);
    }
    public void setSelectIndex(int index_select)
    {
     this.index_select = index_select;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return beans==null?0:beans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(getItemLayoutId(),parent,false);
            holder.iv_item_photo = (ImageView) convertView.findViewById(R.id.iv_item_photo);
            holder.iv_item_photo_select = (ImageView) convertView.findViewById(R.id.iv_item_photo_select);
            holder.pb_item       = (ProgressBar) convertView.findViewById(R.id.pb_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final ViewHolder myHolder = holder;
        ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                myHolder.pb_item.setVisibility(View.GONE);
                myHolder.iv_item_photo.setImageResource(R.drawable.default_pic);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                myHolder.pb_item.setVisibility(View.GONE);
                myHolder.iv_item_photo.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        };
        MediaPicBean picBean = beans.get(position);
        if(!picBean.getUrl().startsWith("http")) {
            ImageLoader.getInstance().loadImage("file://"+beans.get(position).getUrl(),new ImageSize(getLimitedSize(), getLimitedSize()), ImageLoaderOptionsUtil.PIC_IMG_OPTIONS,listener);
        }else{
            ImageLoader.getInstance().loadImage(beans.get(position).getUrl(),new ImageSize(getLimitedSize(), getLimitedSize()), ImageLoaderOptionsUtil.PIC_IMG_OPTIONS,listener);
        }
        if(position==index_select){
            holder.iv_item_photo_select.setVisibility(View.VISIBLE);
        }else{
            holder.iv_item_photo_select.setVisibility(View.GONE);
        }
        return convertView;
    }

    private int getItemLayoutId(){
        return R.layout.horizontial_item_photo;
    }

    class ViewHolder{
        public ImageView iv_item_photo;
        public ImageView iv_item_photo_select;
        public ProgressBar pb_item;
    }

    private int getLimitedSize(){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
    }
}
