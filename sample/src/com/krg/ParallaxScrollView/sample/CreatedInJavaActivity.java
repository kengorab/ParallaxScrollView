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
 * User: Ken Gorab
 * Date: 2/22/14
 * Time: 7:42 PM
 */
public class CreatedInJavaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create ParallaxScrollView, which will eventually be the container for everything.
        ParallaxScrollView parallaxScrollView = new ParallaxScrollView(this);
        parallaxScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Create the Background View.
        ImageView backgroundView = new ImageView(this);
        backgroundView.setImageResource(R.drawable.norway);
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
        titleLp.weight = 0.75f;
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
        wikiLp.weight = 0.25f;
        wikiLp.gravity = Gravity.CENTER_VERTICAL;
        button.setLayoutParams(wikiLp);
        button.setText("Norway Wiki");
        container2.addView(headerTitle);
        container2.addView(button);

        // Create the shadow View within the Header View. Using the {@link ParallaxScrollView#OnHeaderStuckListener}
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
        parallaxScrollView.setOnHeaderStuckListener(new ParallaxScrollView.OnHeaderStuckListener() {
            @Override
            public void onHeaderStuck(boolean isStuck) {
                shadow.setVisibility(isStuck ? View.VISIBLE : View.INVISIBLE);
            }
        });
        setContentView(parallaxScrollView);
    }
}