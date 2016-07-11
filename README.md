## ParallaxLoopPager
a auto scroll viewpager with parallax effect

## Sample
<img src="https://raw.githubusercontent.com/lonelylonelygod/parallaxlooppager/master/sample.gif">

## Usage


### define in xml like this:

	 <io.github.lonelylonelygod.parallaxlooppager.ParallaxLoopPager
        android:id="@+id/pager_main"
        parallaxLoopPager:autoLoop="true"
        parallaxLoopPager:interval="5000"
        parallaxLoopPager:startDelay="2000"
        parallaxLoopPager:showIndicator="true"
        parallaxLoopPager:indicatorMargin="8dp"
        parallaxLoopPager:selectDrawable="@drawable/circle_indicator_selected"
        parallaxLoopPager:unSelectDrawable="@drawable/circle_indicator_unselected"
        parallaxLoopPager:indicatorPosition="right"
        android:layout_width="match_parent"
        android:layout_height="220dp"/>

all the value above is default value.

### then in java code:

	    mLoopView = (ParallaxLoopPager<Integer>) findViewById(R.id.pager_main);
        mLoopView.setLoopAdapter(new LoopAdapter<Integer>() {
            @Override
            public View createView(ViewGroup viewGroup, LayoutInflater inflater, Context context) {
                return inflater.inflate(R.layout.view_pager_item, viewGroup, false);
            }

            @Override
            public void bindItem(View view, final int position, final Integer resId) {
                ImageView imageView = ((ImageView) view.findViewById(R.id.image_pager_item));
                imageView.setBackgroundResource(resId);
                ((TextView) view.findViewById(R.id.text_pager_item_title)).setText(String.valueOf(position + 1));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Click position : " + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mLoopView.setData(Arrays.asList(DATA_SOURCE));

### Gradle:

		compile 'io.github.lonelylonelygod:parallaxlooppager:0.1.0'
