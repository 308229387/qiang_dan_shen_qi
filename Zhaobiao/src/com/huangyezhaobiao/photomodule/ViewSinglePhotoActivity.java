package com.huangyezhaobiao.photomodule;

import android.content.Context;
import android.content.Intent;
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

import com.huangye.commonlib.activity.BaseActivity;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/9/29.
 * 浏览模式下的照片预览
 */
public class ViewSinglePhotoActivity extends BaseActivity {
    ViewPager viewPager;
    RelativeLayout rl_view_single_photo;
    private List<View> views   = new ArrayList<>();
    ViewPagerAdapter    adapter;
    int                 index;
    int                 size;
    private ArrayList<MediaPicBean> beans = new ArrayList<>();
    private View       view_single_back;
    private TextView   tv_count;
    public int getContentViewResId() {
        return R.layout.view_single_photo_activity;
    }

    public static Intent newIntent(Context context, int index,ArrayList<MediaPicBean> beans){
        Intent intent = new Intent(context,ViewSinglePhotoActivity.class);
        intent.putExtra("index",index);
        intent.putParcelableArrayListExtra(GalleryConstans.VIEW_PHOTOS, beans);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        index = getIntent().getIntExtra("index", 0) ;
        beans = getIntent().getParcelableArrayListExtra(GalleryConstans.VIEW_PHOTOS);
        if(beans == null){
            beans = new ArrayList<>();
        }
        initWidget();
        initListener();
    }

    @Override
    public void initView() {
        setContentView(getContentViewResId());
        viewPager = getView(R.id.viewPager);
        rl_view_single_photo = getView(R.id.rl_view_single_photo);
        view_single_back     = getView(R.id.view_single_back);
        tv_count             = getView(R.id.tv_count);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_count.setText((position+1)+"/"+beans.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initListener() {
        view_single_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.e("shenzhixin","sssss");
            }
        });
    }

    private void initWidget() {
        initDataSource();
        rl_view_single_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //删除这一张图片
                beans.remove(viewPager.getCurrentItem());
                if(beans.size()>=1) {
                    initDataSource();

                }else{
                    onBackPressed();
                }
            }
        });

    }

    private void initDataSource() {
        size = beans.size();
        views.clear();
        for(int index = 0;index <size;index++){
            View view = LayoutInflater.from(this).inflate(getLayoutId(),null);
            views.add(view);
        }

        if(index<0) index=0;
        if(index>=size) index = size-1;
        Log.e("shenzhixin","index:"+index+",size:"+size);
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index, false);
        tv_count.setText("" + (viewPager.getCurrentItem()+1) + "/"+beans.size());
    }

    private int getLayoutId(){
        return R.layout.viewpager_photo;
    }




    class ViewPagerAdapter extends PagerAdapter {
        private List<View> views = new ArrayList<>();
        public ViewPagerAdapter(List<View> views){
            this.views = views;
        }
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
            if(position>=0 && position<views.size()) {
                ((ViewPager) container).removeView(views.get(position));
            }
        }

        @Override
        public Object instantiateItem(View container, int position) {
            Log.e("shenzhixin","position:"+position+",size:"+beans.size()+",view size:"+beans.size());
            View view = views.get(position);
            MediaPicBean picBean = beans.get(position);

            final ImageView imageView = (ImageView) view.findViewById(R.id.iv_photo);
            final ProgressBar pb      = (ProgressBar) view.findViewById(R.id.pb_view_single);
            final TextView    tv_progress = (TextView) view.findViewById(R.id.tv_progress);
            ImageLoadingListener listener = new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    pb.setVisibility(View.VISIBLE);
                    tv_progress.setVisibility(View.VISIBLE);
                }
                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    pb.setVisibility(View.GONE);
                    tv_progress.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.default_pic);
                }
                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    pb.setVisibility(View.GONE);
                    tv_progress.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);
                }
                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            };
            if(!picBean.getUrl().startsWith("http")){
                ImageLoader.getInstance().loadImage("file://" + beans.get(position).getUrl(), new ImageSize(400, 400), ImageLoaderOptionsUtil.PIC_IMG_OPTIONS, listener, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String s, View view, int current, int total) {
                        Log.e("shenzhixinUUU","current:"+current+",total:"+total);
                        int percent = current*100/total;
                        tv_progress.setText(""+percent+"%");
                    }
                });
            }else{
                ImageLoader.getInstance().displayImage(picBean.getUrl(),imageView,ImageLoaderOptionsUtil.PIC_IMG_OPTIONS);
            }
            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventAction eventAction = new EventAction(EventType.EVENT_BACK_FROM_VIEW_SINGLE,beans);
        EventbusAgent.getInstance().post(eventAction);
    }
}
