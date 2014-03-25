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

package com.krg.ParallaxScrollView.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.krg.ParallaxScrollView.ParallaxScrollView;
import com.krg.ParallaxScrollView.ScrollHeaderView;

/**
 * An example of the implementation of a ParallaxScrollView, as created in Java, for
 * those who prefer to create their layouts programmatically. The layout itself is a
 * duplicate of the one created in XML, but this demonstrates the process by which a
 * ParallaxScrollView can be dynamically created.
 */
public class CreatedInJavaActivity extends Activity implements View.OnClickListener {

    int imageResId = R.drawable.norway1;
    ParallaxScrollView parallaxScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create ParallaxScrollView, which will eventually be the container for everything.
        parallaxScrollView = new ParallaxScrollView(this);
        parallaxScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Create the Background View.
        ImageView backgroundView = new ImageView(this);
        backgroundView.setImageResource(R.drawable.norway1);
        backgroundView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
        backgroundView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Create the Content View.
        LinearLayout contentView = new LinearLayout(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentView.setBackgroundColor(0xffffffff);
        contentView.setOrientation(LinearLayout.VERTICAL);
        int contentPadding = getResources().getDimensionPixelSize(R.dimen.content_padding);
        contentView.setPadding(contentPadding, contentPadding, contentPadding, contentPadding);

        // Create the Header View.
        ScrollHeaderView headerView = new ScrollHeaderView(this);
        LinearLayout container1 = new LinearLayout(this);
        container1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout container2 = new LinearLayout(this);
        container2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container2.setOrientation(LinearLayout.HORIZONTAL);
        int headerPadding = getResources().getDimensionPixelSize(R.dimen.header_padding);
        container2.setPadding(headerPadding, headerPadding, headerPadding, headerPadding);
        container2.setBackgroundColor(0xffdddddd);

        // Create the Header title within the Header View.
        TextView headerTitle = new TextView(this);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.weight = 0.60f;
        titleLp.gravity = Gravity.CENTER_VERTICAL;
        headerTitle.setLayoutParams(titleLp);
        float headerTextSize = getResources().getDimension(R.dimen.header_text_size);
        headerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize);
        headerTitle.setTextColor(0xff333333);
        headerTitle.setText("This is a header");

        // Create a Button within the Header View.
        Button button = new Button(this);
        button.setId(R.id.norway_wiki);
        LinearLayout.LayoutParams wikiLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        wikiLp.weight = 0.40f;
        wikiLp.gravity = Gravity.CENTER_VERTICAL;
        button.setLayoutParams(wikiLp);
        button.setText("Switch image");
        button.setOnClickListener(this);
        container2.addView(headerTitle);
        container2.addView(button);

        // Create the shadow View within the Header View. Using the {@link ParallaxScrollView#OnHeaderStateChangedListener}
        // callback, this shadow can be toggled on and off when the Header View becomes stuck at the top of the screen.
        final View shadow = new View(this);
        shadow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8));
        shadow.setVisibility(View.INVISIBLE);
        shadow.setId(R.id.header_shadow);
        shadow.setBackgroundResource(R.drawable.shadow_fade_down);
        container1.addView(container2);
        container1.addView(shadow);

        headerView.addView(container1);

        // Create the TextView within the Content View.
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(R.string.norway);
        float textSize = getResources().getDimension(R.dimen.content_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        contentView.addView(headerView);
        contentView.addView(textView);

        // Make the ParallaxScrollView aware of the Background, Content, and Header Views, and set the content of
        // this view to the ParallaxScrollView.
        parallaxScrollView.setBackgroundView(backgroundView);
        parallaxScrollView.setContentView(contentView);
        parallaxScrollView.setHeaderView(headerView);
        parallaxScrollView.setOnHeaderStateChangedListener(new ParallaxScrollView.OnHeaderStateChangedListener() {
            @Override
            public void onHeaderStateChanged(boolean isStuck) {
                shadow.setVisibility(isStuck ? View.VISIBLE : View.INVISIBLE);
            }
        });
        setContentView(parallaxScrollView);
    }

    @Override
    public void onClick(View v) {
        if (imageResId == R.drawable.norway1)
            imageResId = R.drawable.norway2;
        else
            imageResId = R.drawable.norway1;

        // Demonstrate how the backgroundView can be accessed from the ParallaxScrollView.
        ImageView parallaxView = (ImageView) parallaxScrollView.getBackgroundView();
        parallaxView.setImageResource(imageResId);
    }
}