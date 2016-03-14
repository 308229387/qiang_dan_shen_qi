package com.huangyezhaobiao.photomodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.huangyezhaobiao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/9/25.
 */
public class GalleryAdapter extends BaseAdapter {
    private List<MediaPicBean> photoInfos;
    private Context context;
    private LayoutInflater inflater;
    private OnGalleryClickListener listener;
    private int                    current_checked;
    private ImageLoader            imageLoader;
    public  static final String FILE_PERFIX = "file://";
    private boolean loading = true;
    private List<ImageView> viewLists = new ArrayList<>();

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.pictures_no)
            .showImageForEmptyUri(R.drawable.pictures_no)
            .showImageOnFail(R.drawable.pictures_no).cacheInMemory(true)
            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();
    private List<String> checkedUrls;

    public GalleryAdapter(Context context,List<MediaPicBean> photoInfos){
        this.context = context;
        inflater     = LayoutInflater.from(context);
        this.photoInfos   = new ArrayList<>();
        checkedUrls       = new ArrayList<>();
        this.photoInfos   = photoInfos;
        initImageLoader();

    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(
                ImageLoaderConfiguration.createDefault(context));


    }

    public void setOnGalleryClickLitener(OnGalleryClickListener listener){
        this.listener = listener;
    }




    @Override
    public int getCount() {
        return photoInfos==null?0:photoInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return photoInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView           = inflater.inflate(getLayoutId(), parent,false);
            holder                = new ViewHolder();
            holder.id_item_image  = (ImageView) convertView.findViewById(R.id.id_item_image);
            holder.id_item_select = (ImageView) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(holder);
        }else{
            holder                = (ViewHolder) convertView.getTag();
        }
                int realPos    =  position;
                MediaPicBean info = photoInfos.get(realPos);
                String path    = getPhotoPath(info);
                holder.id_item_image.setTag(path);
                Uri uri  = Uri.parse(path);
                if(loading) {
                    setContent(true, holder.id_item_image, path);
                }else{
                    holder.id_item_image.setImageResource(R.drawable.pictures_no);
                }
                holder.id_item_select.setVisibility(View.VISIBLE);
                changeImage(holder, info);
            bindListener(holder, position);
            viewLists.add(holder.id_item_image);
        return convertView;
    }

    private void changeImage(ViewHolder holder, MediaPicBean info) {
        if(GalleryScanHelper.isChecked(info.getUrl())){
            setCheckedImage(holder);
        }else{
            setUnCheckedImage(holder);
        }

    }

    private void bindListener(final ViewHolder holder, final int position) {
        holder.id_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onGalleryItemClicked(position,photoInfos.get(position),current_checked);
                }
            }
        });

        holder.id_item_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    MediaPicBean info = photoInfos.get(position);
                    if(GalleryScanHelper.isChecked(info.getUrl())) {//之前是选中的状态
                       GalleryScanHelper.setUnChecked(info.getUrl());
                        changeImage(holder, info);
                        listener.onGalleryUnCheckClicked(GalleryScanHelper.checkCounts(), info);
                    }else{//之前没被选中
                        if(GalleryScanHelper.checkCounts()>=StorageConstans.MAX_COUNT){
                           listener.onGalleryAlreaadyMax();
                        }else{
                            GalleryScanHelper.setChecked(info.getUrl());
                            changeImage(holder, info);
                            listener.onGalleryCheckClicked(GalleryScanHelper.checkCounts(),info);
                        }
                    }
                }
            }
        });
    }





    private void setCheckedImage(ViewHolder holder){
        holder.id_item_select
                .setImageResource(R.drawable.pictures_selected);
        holder.id_item_image.setColorFilter(Color
                .parseColor("#77000000"));
    }

    private void setUnCheckedImage(ViewHolder holder){
        holder.id_item_select
                .setImageResource(R.drawable.picture_unselected);
        holder.id_item_image.setColorFilter(null);
    }


    private void setContent(boolean needCompress,ImageView info, final String uri) {
        ImageLoader.getInstance().displayImage(uri, info, ImageLoaderOptionsUtil.GALLERY_IMG_OPTIONS);
        Log.e("uiphoto", "uri:" + uri);

    }






    private String getPhotoPath(MediaPicBean info){
        return FILE_PERFIX + info.getUrl();
    }


    private int getAddDrawable(){
        return R.drawable.add_photo;
    }


    private int getLayoutId(){
        return R.layout.grid_item;
    }

    public void lock() {
       loading = false;
    }


    private int first;
    private int last = Integer.MAX_VALUE;
    public void unLock(int start, int end) {
        loading = true;
        notifyDataSetChanged();
    }

    public void setCheckedUrls(List<String> checkedUrls) {
        this.checkedUrls = checkedUrls;
    }

    public void releaseSources() {
        for(int index=0;index<viewLists.size();index++){
            ImageView imageView = viewLists.get(index);
            imageView.setImageResource(0);
        }
        viewLists.clear();
        viewLists = null;
    }


    private class ViewHolder {
        private ImageView id_item_image;
        private ImageView id_item_select;

    }


    public void refreshDatas(List<MediaPicBean> infos){
        photoInfos = infos;
        notifyDataSetChanged();
    }


    public interface OnGalleryClickListener{
        public void onGalleryAddClick();
        public void onGalleryCheckClicked(int count, MediaPicBean info);
        public void onGalleryUnCheckClicked(int count, MediaPicBean info);
        public void onGalleryItemClicked(int pos, MediaPicBean info, int checked_size);
        public void onGalleryAlreaadyMax();
    }
}
