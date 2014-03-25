/*
 * Copyright 2014 Ken Gorab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.krg.ParallaxScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
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

    /**
     * Sets the callbacks for the ObservableScrollView. The callback contains methods that allow for
     * control over certain moments during the lifecycle of the ObservableScrollView.
     * @param callbacks
     */
    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        mCallbacks.onScroll(x, y);
    }

    /**
     * A callback interface which allows implementing classes control during
     * certain moments in the ObservableScrollView's lifecycle.
     */
    public interface Callbacks {

        /**
         * Called when the ObservableScrollView scrolls.
         * @param scrollX   The current horizontal scroll origin.
         * @param scrollY   The current vertical scroll origin.
         */
        public void onScroll(int scrollX, int scrollY);
    }
}
