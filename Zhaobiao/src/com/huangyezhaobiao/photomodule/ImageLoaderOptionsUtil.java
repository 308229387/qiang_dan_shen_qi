package com.huangyezhaobiao.photomodule;

import android.content.Context;
import android.graphics.Bitmap;

import com.huangyezhaobiao.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by cy-love on 15/1/7.
 */
public class ImageLoaderOptionsUtil {


    public static ImageLoaderConfiguration initOptions(Context context){
            ImageLoaderConfiguration config = new ImageLoaderConfiguration
                    .Builder(context)
                    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                    .threadPoolSize(8)//线程池内加载的数量
                    .threadPriority(Thread.MAX_PRIORITY)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new WeakMemoryCache()) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                    .memoryCacheSize(2 * 1024 * 1024)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                    .build();//开始构建
            return config;
    }
  /*  *//**
     * 圆形头像
     *//*
    public static final DisplayImageOptions DEAL_PORTRAIT_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_user_portrait)
            .showImageForEmptyUri(R.drawable.default_user_portrait).considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(180))
            .showImageOnFail(R.drawable.default_user_portrait).cacheInMemory(true)
            .cacheOnDisk(true).build();*/

    /**
     * 正常的配置
     */
    public static final DisplayImageOptions PIC_IMG_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_pic)
            .showImageForEmptyUri(R.drawable.default_pic).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .showImageOnFail(R.drawable.default_pic).cacheInMemory(true)
            .cacheOnDisk(true).build();

    public static final DisplayImageOptions CATEGORY_IMG_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_pic)
            .showImageForEmptyUri(R.drawable.default_pic).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .showImageOnFail(R.drawable.default_pic).cacheInMemory(true)
            .cacheOnDisk(true).build();

   /* public static final DisplayImageOptions PERSONAL_SKILL_COVER_IMG_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.personal_skill_cover_default)
            .showImageForEmptyUri(R.drawable.personal_skill_cover_default).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .showImageOnFail(R.drawable.personal_skill_cover_default).cacheInMemory(true)
            .cacheOnDisk(true).build();*/

    public static final DisplayImageOptions GALLERY_IMG_OPTIONS = new DisplayImageOptions.Builder()
           /* .showImageOnLoading(R.drawable.default_pic) //设置图片在下载期间显示的图片
         */   .showImageForEmptyUri(R.drawable.default_pic)//设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.default_pic)  //设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)//设置下载的图片是否缓存在内存中
            .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
            .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
            .build();//构建完成

}
