package com.example.creditmanagement.Main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AnimationUtil {
    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown){
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorTranslateY = ObjectAnimator.
                ofFloat(holder.itemView , "translationY" , goesDown ? 200 : -200 , 0);
        animatorTranslateY.setDuration(1000);

        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();
    }

    public static void animateView(View v , boolean flag){
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorTranslateY = ObjectAnimator.
                ofFloat(v , "translationY" , flag ? 200 : -200 , 0);
        animatorTranslateY.setDuration(1000);

        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();
    }

}
