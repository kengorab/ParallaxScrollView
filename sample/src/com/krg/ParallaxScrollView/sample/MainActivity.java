package com.krg.ParallaxScrollView.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView lv = (ListView) findViewById(R.id.list_view);

        String[] options = {"Created in XML", "Created in Java", "More complex background view"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            startActivity(new Intent(this, CreatedInXmlActivity.class));
        else if (position == 1)
            startActivity(new Intent(this, CreatedInJavaActivity.class));
        else if (position == 2)
            startActivity(new Intent(this, PagerViewBackgroundActivity.class));
    }
}
