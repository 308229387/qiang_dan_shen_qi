package com.huangyezhaobiao.presenter;

import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by shenzhixin on 2015/12/10.
 */
public class AnimatePresenter {
    private Context context;
    public AnimatePresenter(Context context){
        this.context = context;
    }

    public void showRotateTranslateAnimation(final View view1,final View view2,final View view3){
        view1.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        view2.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        view3.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        ObjectAnimator view1_animator1  = ObjectAnimator.ofFloat(view1, "rotation", 0,  360).setDuration(1000);
        ObjectAnimator view2_animator1 = ObjectAnimator.ofFloat(view2,"rotation",0, 360).setDuration(1000);
        ObjectAnimator view2_animator2 = ObjectAnimator.ofFloat(view2,"translationY",0,400).setDuration(1000);
        ObjectAnimator view3_animator1 = ObjectAnimator.ofFloat(view3,"rotation",0, 360).setDuration(1000);
        ObjectAnimator view3_animator2 = ObjectAnimator.ofFloat(view3,"translationY",0,800).setDuration(1000);
        view3_animator1.setStartDelay(300);
        view3_animator2.setStartDelay(300);
        AnimatorSet    animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        view2_animator2.setInterpolator(new OvershootInterpolator(1.3f));
        view3_animator2.setInterpolator(new OvershootInterpolator(1.3f));
        animatorSet.playTogether(view1_animator1,view2_animator1,view2_animator2,view3_animator1,view3_animator2);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view1.setLayerType(View.LAYER_TYPE_NONE, null);
                view2.setLayerType(View.LAYER_TYPE_NONE, null);
                view3.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
        animatorSet.start();
    }



    public void hideRotateTranslateAnimation(final View rootView,View view1,View view2,View view3){
        ObjectAnimator view1_animator1  = ObjectAnimator.ofFloat(view1, "rotation", 360, 0).setDuration(1000);
        ObjectAnimator view2_animator1 = ObjectAnimator.ofFloat(view2,"rotation",360,0).setDuration(1000);
        ObjectAnimator view2_animator2 = ObjectAnimator.ofFloat(view2,"translationY",400,0).setDuration(1000);
        ObjectAnimator view3_animator1 = ObjectAnimator.ofFloat(view3,"rotation",360,0).setDuration(1000);
        ObjectAnimator view3_animator2 = ObjectAnimator.ofFloat(view3,"translationY",800,0).setDuration(1000);
        view2_animator2.setStartDelay(300);
        view2_animator1.setStartDelay(300);
        AnimatorSet    animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new OvershootInterpolator(1.3f));
        animatorSet.playTogether(view1_animator1, view2_animator1, view2_animator2, view3_animator1, view3_animator2);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                rootView.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }
}
