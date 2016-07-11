package io.github.lonelylonelygod.parallaxlooppager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.lang.reflect.Field;


public class FixSpeedViewPager extends ViewPager{

    public static final int DEFAULT_DURATION = 2000;

    public FixSpeedViewPager(Context context) {
        super(context);
        setDuration(DEFAULT_DURATION);
    }

    public FixSpeedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDuration(DEFAULT_DURATION);
    }

    public void setDuration(int duration){
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this.getContext(), null, duration);
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
