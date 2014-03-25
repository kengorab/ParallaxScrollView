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
import android.widget.FrameLayout;

/**
 * An implementation-less FrameLayout subclass, used solely for distinction of
 * a Header View in a ParallaxScrollView.
 */
public class ScrollHeaderView extends FrameLayout {

    public ScrollHeaderView(Context context) {
        super(context);
    }

    public ScrollHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
