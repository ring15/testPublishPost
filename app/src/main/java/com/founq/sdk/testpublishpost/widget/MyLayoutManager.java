package com.founq.sdk.testpublishpost.widget;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * Created by ring on 2020/9/23.
 */
public class MyLayoutManager extends GridLayoutManager {

    public MyLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
