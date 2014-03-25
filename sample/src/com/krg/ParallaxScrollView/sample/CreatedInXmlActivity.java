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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.krg.ParallaxScrollView.ParallaxScrollView;

/**
 * An example of the implementation of a ParallaxScrollView, whose layout is created in XML
 * and interfaced with it here.
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
        s.setOnHeaderStateChangedListener(new ParallaxScrollView.OnHeaderStateChangedListener() {
            @Override
            public void onHeaderStateChanged(boolean isStuck) {
                if (isStuck)
                    findViewById(R.id.header_shadow).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.header_shadow).setVisibility(View.GONE);
            }
        });
    }
}
