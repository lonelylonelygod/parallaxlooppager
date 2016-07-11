package io.github.lonelylonelygod.parallaxlooppager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class ParallaxLoopPager<T> extends FrameLayout implements ViewPager.OnPageChangeListener {
    protected final static int TMP_AMOUNT = 1200;
    private final static int DEFAULT_INTERVAL = 5000;
    private final static int DEFAULT_START_DELAY = 2000;

    private FixSpeedViewPager mViewPager;
    private PageIndicator mPageIndicator;
    private LoopPageChangeListener mLoopPageChangeListener;
    private ItemAdapterWrapper<T> mItemAdapterWrapper;
    private Timer mTimer;
    private TimerTask mTask;
    private boolean mCanAutoLoop;
    private boolean mShowIndicator;
    private int mIntervalTime;
    private int mStartDelay;
    private ViewPager.PageTransformer mDefaultTransformer;

    public ParallaxLoopPager(Context context) {
        this(context, null);
    }

    public ParallaxLoopPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxLoopPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) return;
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxLoopPager, defStyleAttr, 0);
        if (a == null) return;

        mCanAutoLoop = a.getBoolean(R.styleable.ParallaxLoopPager_autoLoop, true);
        mShowIndicator = a.getBoolean(R.styleable.ParallaxLoopPager_showIndicator, true);
        mIntervalTime = a.getInteger(R.styleable.ParallaxLoopPager_interval, DEFAULT_INTERVAL);
        mStartDelay = a.getInteger(R.styleable.ParallaxLoopPager_startDelay, DEFAULT_START_DELAY);
        int indicatorMargin = a.getDimensionPixelSize(R.styleable.ParallaxLoopPager_indicatorMargin,
                getResources().getDimensionPixelSize(R.dimen.circle_indicator_inline_margin));
        Drawable selectDrawable = a.getDrawable(R.styleable.ParallaxLoopPager_selectDrawable);
        Drawable unSelectDrawable = a.getDrawable(R.styleable.ParallaxLoopPager_unSelectDrawable);
        int position = a.getInt(R.styleable.ParallaxLoopPager_indicatorPosition, 0);
        a.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.view_loop_pager_layout, this, true);
        mItemAdapterWrapper = new ItemAdapterWrapper<>();
        if (mShowIndicator)
            createDefaultIndicator(unSelectDrawable, selectDrawable, indicatorMargin, position);
    }

    private void createDefaultIndicator(Drawable unSelectDrawable, Drawable selectDrawable, int indicatorMargin, int position) {
        mPageIndicator = new PageIndicator(getContext());
        mPageIndicator.setIndicatorMargin(indicatorMargin);
        mPageIndicator.updateDrawable(unSelectDrawable, selectDrawable);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (position) {
            case 0:
                layoutParams.gravity = Gravity.BOTTOM | Gravity.END | Gravity.RIGHT;
                break;
            case 1:
                layoutParams.gravity = Gravity.BOTTOM | Gravity.START | Gravity.LEFT;
                break;
            case 2:
                layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                break;
        }
        int margin = getResources().getDimensionPixelSize(R.dimen.circle_indicator_margin);
        layoutParams.rightMargin = margin;
        layoutParams.bottomMargin = margin;
        layoutParams.topMargin = margin;
        layoutParams.leftMargin = margin;

        addView(mPageIndicator, layoutParams);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = (FixSpeedViewPager) this.findViewById(R.id.loop_pager_layout);
        mViewPager.setAdapter(mItemAdapterWrapper);
        mViewPager.addOnPageChangeListener(this);
        if(mDefaultTransformer == null){
            mViewPager.setPageTransformer(false, new ParallaxPageTransformer());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case KeyEvent.ACTION_DOWN:
                stopTimer();
                break;
            case KeyEvent.ACTION_UP:
                startTimer();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTask == null) {
            mTask = new TimerTask() {
                public void run() {
                    mViewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            };
            mTimer.schedule(mTask, mStartDelay, mIntervalTime);
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    private void doNormalShow() {
        mViewPager.setCurrentItem(TMP_AMOUNT);
        if (mCanAutoLoop) startLoop();
    }

    private void doOneSimpleImageShow() {
        mViewPager.setCurrentItem(0);
        stopLoop();
    }

    private int getAdapterRealCount() {
        return mItemAdapterWrapper.getRealCount();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        doOnPageScrolled(getRealPosition(position), positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        doOnPageSelected(getRealPosition(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        doOnPageScrollStateChanged(state);
    }

    private void doOnPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mLoopPageChangeListener != null) {
            mLoopPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    private void doOnPageSelected(int position) {
        if (mLoopPageChangeListener != null) {
            mLoopPageChangeListener.onPageSelected(position);
        }
        updateIndicatorPosition(position);
    }

    private void updateIndicatorPosition(int position) {
        if (mPageIndicator == null) return;
        mPageIndicator.updatePosition(position);
    }

    private void doOnPageScrollStateChanged(int state) {
        if (mLoopPageChangeListener != null) {
            mLoopPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private int getRealPosition(int position) {
        return position == 0 ? 0 : position % getAdapterRealCount();
    }

    public void setLoopPageChangeListener(LoopPageChangeListener listener) {
        mLoopPageChangeListener = listener;

    }

    public void setPageIndicator(PageIndicator pageIndicator) {
        mShowIndicator = true;
        if (mPageIndicator != null) {
            ((ViewGroup) mPageIndicator.getParent()).removeView(mPageIndicator);
            mPageIndicator = null;
        }
        mPageIndicator = pageIndicator;
        updateImageIndicator();
    }

    private void updateImageIndicator() {
        if (mPageIndicator == null) return;
        mPageIndicator.setVisibility(mItemAdapterWrapper.getRealCount() == 1 ? GONE : VISIBLE);
        mPageIndicator.updateCount(mItemAdapterWrapper.getRealCount());
        updateIndicatorPosition(getRealPosition(mViewPager.getCurrentItem()));
    }

    public void setLoopAdapter(LoopAdapter loopImager) {
        mItemAdapterWrapper.setLoopAdapter(loopImager);
    }

    public void startLoop() {
        startTimer();
    }

    public void stopLoop() {
        stopTimer();
    }


    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setPageTransformer(boolean reverseDrawingOrder, @NonNull ViewPager.PageTransformer transformer) {
        mDefaultTransformer = transformer;
        if (mViewPager != null) {
            mViewPager.setPageTransformer(reverseDrawingOrder, mDefaultTransformer);
        }
    }

    public void setData(List<T> data) {
        boolean isAddDataFromEmptyState = mItemAdapterWrapper.getCount() == 0;
        mItemAdapterWrapper.updateData(data);
        if (isAddDataFromEmptyState) {
            mViewPager.setCurrentItem(mItemAdapterWrapper.getRealCount() == 1 ? 0 : TMP_AMOUNT);
            if (mItemAdapterWrapper.getRealCount() == 1) {
                doOneSimpleImageShow();
            } else {
                doNormalShow();
            }
        }
        updateImageIndicator();
    }
}
