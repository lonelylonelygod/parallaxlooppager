package io.github.lonelylonelygod.parallaxlooppager;

public interface LoopPageChangeListener {

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

}
