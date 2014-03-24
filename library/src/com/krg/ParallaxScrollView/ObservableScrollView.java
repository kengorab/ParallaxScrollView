package com.krg.ParallaxScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * User: Ken Gorab
 * Date: 3/13/14
 * Time: 10:20 PM
 *
 * A small extension of {@link android.widget.ScrollView}. This extension allows for customization
 * of the {@link android.widget.ScrollView#onScrollChanged(int, int, int, int)} method, which determines
 * the amount which the background view should scroll in parallax. Every other attribute
 * remains unchanged from ScrollView.
 */
public class ObservableScrollView extends ScrollView {
    private Callbacks mCallbacks;

    public ObservableScrollView(Context context) {
        super(context);
        setFadingEdgeLength(0);
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        mCallbacks.onScroll(x, y);
    }

    public interface Callbacks {
        public void onScroll(int scrollX, int scrollY);
    }
}
