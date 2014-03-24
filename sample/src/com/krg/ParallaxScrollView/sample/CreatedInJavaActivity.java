package com.krg.ParallaxScrollView.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.krg.ParallaxScrollView.ParallaxScrollView;

/**
 * User: Ken Gorab
 * Date: 2/22/14
 * Time: 7:42 PM
 */
public class CreatedInJavaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParallaxScrollView parallaxScrollView = new ParallaxScrollView(this);
        parallaxScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView backgroundView = new ImageView(this);
        backgroundView.setImageResource(R.drawable.norway);
        backgroundView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
        backgroundView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout contentView = new LinearLayout(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentView.setBackgroundColor(0xffffffff);
        int padding = getResources().getDimensionPixelSize(R.dimen.content_padding);
        contentView.setPadding(padding, padding, padding, padding);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(R.string.norway);
        float textSize = getResources().getDimension(R.dimen.content_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        contentView.addView(textView);

        parallaxScrollView.setBackgroundView(backgroundView);
        parallaxScrollView.setContentView(contentView);

        setContentView(parallaxScrollView);
    }
}