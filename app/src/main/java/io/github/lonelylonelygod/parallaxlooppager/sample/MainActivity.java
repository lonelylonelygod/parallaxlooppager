package io.github.lonelylonelygod.parallaxlooppager.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import io.github.lonelylonelygod.parallaxlooppager.LoopAdapter;
import io.github.lonelylonelygod.parallaxlooppager.ParallaxLoopPager;

public class MainActivity extends AppCompatActivity {


    private ParallaxLoopPager<Integer> mLoopView;

    private final Integer[] DATA_SOURCE = new Integer[]{
            R.drawable.one_punch_man1,
            R.drawable.one_punch_man2,
            R.drawable.one_punch_man3,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
