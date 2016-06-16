package com.loz.iyaf.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.loz.iyaf.feed.GalleryData;
import com.loz.iyaf.R;

import java.util.ArrayList;

public class GalleryFullscreenActivity extends Activity{

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_fullscreen);

        viewPager = (ViewPager) findViewById(R.id.pager);
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        ArrayList<GalleryData> gallery = (ArrayList<GalleryData>)i.getSerializableExtra("gallery");

        adapter = new FullScreenImageAdapter(GalleryFullscreenActivity.this,
                gallery);

        viewPager.setAdapter(adapter);


        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
