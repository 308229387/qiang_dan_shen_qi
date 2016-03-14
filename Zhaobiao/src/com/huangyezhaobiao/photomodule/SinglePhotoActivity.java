package com.huangyezhaobiao.photomodule;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.activity.BaseActivity;
import com.huangyezhaobiao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/9/25.
 * 单个预览照片的activity
 * 1.可以滑动
 * 2.可以选图片，已经选中的可以结束
 * 3.可以确定
 * 4.可以返回
 */
public class SinglePhotoActivity extends BaseActivity {
    DisplayImageOptions options;
    ViewPager viewPager;
    TextView tv_back;
    TextView tv_title;
    View ll_right_btn;
    TextView tv_single_photo_right;
    RelativeLayout rl_single_photo_check;
    TextView tv_single_photo_check;
    ImageView iv_single_photo_check;
    RelativeLayout rl_single_bottom;
    private boolean visible = true;
    private List<View> views = new ArrayList<>();
    private int index;
    private int size;
    private int checked_size;

    /**
     * @return
     */

    public int getContentViewResId() {
        return R.layout.single_photo_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        index = getIntent().getIntExtra("index", 0);
        checked_size = getIntent().getIntExtra("checkedSize", 0);
        updateChecked(checked_size);

        configImageLoader();
        initViews();
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(index, false);
        updateUI(PhotoHelper.current_photo_infos.get(index));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                tv_back.setText((position + 1) + "/" + size);
                MediaPicBean info = PhotoHelper.current_photo_infos.get(index);
                updateUI(info);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        rl_single_photo_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPicBean info = PhotoHelper.current_photo_infos.get(index);
                if (GalleryScanHelper.isChecked(info.getUrl())) {
                    checked_size--;
                    updateChecked(checked_size);
                    GalleryScanHelper.setUnChecked(info.getUrl());
                    updateUI(info);
                    changeModel(index, info);
                } else {
                    if (checked_size >= StorageConstans.MAX_COUNT) {
                        Toast.makeText(SinglePhotoActivity.this,"最多选择" + StorageConstans.MAX_COUNT + "张",0).show();
                    } else {
                        checked_size++;
                        updateChecked(checked_size);
                        GalleryScanHelper.setChecked(info.getUrl());
                        updateUI(info);
                        changeModel(index, info);
                    }
                }

            }
        });
        bindListener();

    }

    @Override
    public void initView() {
        setContentView(getContentViewResId());
        viewPager = getView(R.id.viewPager);
        tv_back   = getView(R.id.tv_back);
        tv_title  = getView(R.id.tv_title);
        ll_right_btn = getView(R.id.ll_right_btn);
        tv_single_photo_right = getView(R.id.tv_right_btn);
        rl_single_photo_check = getView(R.id.rl_single_photo_check);
        tv_single_photo_check = getView(R.id.tv_single_photo_check);
        iv_single_photo_check = getView(R.id.iv_single_photo_check);
        rl_single_bottom      = getView(R.id.rl_single_bottom);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onBackPressed() {
        setResult(StorageConstans.RESULT_CODE_SINGLE_2_GALLERY);
        finish();
    }

    private void bindListener() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(StorageConstans.RESULT_CODE_SINGLE_2_GALLERY);
                finish();
            }
        });
        ll_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GalleryScanHelper.checkCounts() > 0) {
                    setResult(StorageConstans.RESULT_CODE_SINGLE_2_GALLERY_2_OTHER);
                    finish();
                } else {
                    Toast.makeText(SinglePhotoActivity.this,"至少选择一张照片",0).show();
                }
            }
        });


    }

    private void changeModel(int index, MediaPicBean info) {
        // PhotoHelper.current_photo_infos.set(index, info);
    }

    private void updateChecked(int checked_size) {
        tv_single_photo_right.setText("确定:" + String.valueOf(checked_size));
    }


    private void updateUI(MediaPicBean info) {
        if (GalleryScanHelper.isChecked(info.getUrl())) {
            tv_single_photo_check.setText("选中了");
            iv_single_photo_check.setImageResource(R.drawable.auth_follow_cb_chd);
        } else {
            tv_single_photo_check.setText("没选中");
            iv_single_photo_check.setImageResource(R.drawable.auth_follow_cb_unc);
        }
    }


    private void initViews() {
        size = PhotoHelper.current_photo_infos.size();
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.viewpager_photo, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_photo);
            // 启用图片缩放功能

            views.add(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrHideNavigation();
                }
            });
        }
        tv_back.setText((index + 1) + "/" + size);
    }

    private void showOrHideNavigation() {
        if (visible) {
            navigationHide();
        } else {
            navigationShow();
        }

        visible = !visible;
    }


    private void configImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pictures_no)
                .showImageForEmptyUri(R.drawable.pictures_no)
                .showImageOnFail(R.drawable.pictures_no).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(this));


    }


    class ViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View view = views.get(position);
            final ImageView imageView = (ImageView) view.findViewById(R.id.iv_photo);
            final TextView  tv_progress = (TextView) view.findViewById(R.id.tv_progress);
            final ProgressBar pb_view_single = (ProgressBar) view.findViewById(R.id.pb_view_single);
            ImageLoader.getInstance().loadImage("file://" + PhotoHelper.current_photo_infos.get(position).getUrl(), new ImageSize(300, 300), options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    pb_view_single.setVisibility(View.VISIBLE);
                    tv_progress.setVisibility(View.VISIBLE);
                    Log.e("shenzhixinUUU","loading start");
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    Toast.makeText(SinglePhotoActivity.this, "加载图片失败了", 0).show();
                    tv_progress.setVisibility(View.GONE);
                    pb_view_single.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.default_pic);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    pb_view_single.setVisibility(View.GONE);
                    tv_progress.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String s, View view, int count, int total) {
                    int precent = count*100/total;
                    tv_progress.setText(""+precent+"%");
                }
            });

            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }
    }


    private void navigationShow() {
        tv_title.setVisibility(View.VISIBLE);
        rl_single_bottom.setVisibility(View.VISIBLE);
    }

    private void navigationHide() {
        tv_title.setVisibility(View.GONE);
        rl_single_bottom.setVisibility(View.GONE);
    }


}
