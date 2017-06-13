package com.teralser.weatherapp.manager;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class AnimationManager {

    private static final long ANIM_TIME = 400;

    public AnimationManager() {
    }

    public ViewPropertyAnimatorCompat buildAnimationBaseIn(View v) {
        return ViewCompat
                .animate(v)
                .alpha(1f)
                .translationX(0)
                .translationY(0)
                .setDuration(ANIM_TIME)
                .setInterpolator(new DecelerateInterpolator());
    }

    public ViewPropertyAnimatorCompat buildAnimationBaseOut(View v) {
        return ViewCompat
                .animate(v)
                .alpha(0f)
                .setDuration(ANIM_TIME)
                .setInterpolator(new AccelerateInterpolator());
    }
}
