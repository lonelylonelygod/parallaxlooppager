package io.github.lonelylonelygod.parallaxlooppager;

import android.support.v4.view.ViewPager;
import android.view.View;

public class ParallaxPageTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_SPEED = 0.35f;

    @Override
    public void transformPage(View view, float position) {
        if (position <= 0) {
            view.setTranslationX(-(position * view.getWidth() * DEFAULT_SPEED));
        }
    }
}
