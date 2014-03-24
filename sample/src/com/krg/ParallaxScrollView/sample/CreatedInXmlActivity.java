package com.krg.ParallaxScrollView.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.krg.ParallaxScrollView.ParallaxScrollView;

/**
 * User: Ken Gorab
 * Date: 2/22/14
 * Time: 6:52 PM
 */
public class CreatedInXmlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.created_in_xml_activity);

        ((Button)findViewById(R.id.norway_wiki)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://en.wikipedia.org/wiki/Norway"));
                startActivity(i);
            }
        });

        ParallaxScrollView s = (ParallaxScrollView)findViewById(R.id.scroll_view);
        s.setOnHeaderStuckListener(new ParallaxScrollView.OnHeaderStuckListener() {
            @Override
            public void onHeaderStuck(boolean isStuck) {
                if (isStuck)
                    findViewById(R.id.header_shadow).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.header_shadow).setVisibility(View.GONE);
            }
        });
    }
}
