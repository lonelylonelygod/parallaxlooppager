package io.github.lonelylonelygod.parallaxlooppager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface LoopAdapter<T> {

    View createView(ViewGroup viewGroup, LayoutInflater inflater, Context context);

    void bindItem(View view, int position, T t);
}
