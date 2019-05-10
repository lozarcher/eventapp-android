package com.loz.iyaf.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loz.iyaf.events.EventListActivity;
import com.loz.iyaf.gallery.GalleryActivity;
import com.loz.iyaf.info.InfoListActivity;
import com.loz.iyaf.news.NewsListActivity;
import com.loz.iyaf.traders.TraderListActivity;
import com.loz.iyaf.twitter.TwitterListActivity;
import com.loz.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    private static final double TOP_MARGIN_PROPORTION_FOR_MENU = 0.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        initImageLoader(getApplicationContext());

        displayBackgroundImageForScreenSize();

        GridView gridview = findViewById(R.id.gridView);
        gridview.setAdapter(new MenuAdapter(this));

        setTopMarginOfMenu(gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MenuActivity.this, EventListActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MenuActivity.this, TraderListActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MenuActivity.this, NewsListActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MenuActivity.this, GalleryActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MenuActivity.this, TwitterListActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MenuActivity.this, InfoListActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Home View")
                .putContentType("Home View")
                .putContentId("home"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private void displayBackgroundImageForScreenSize() {
        DisplayMetrics displayMetrics = getDisplayMetrics();

        ArrayList<Integer> images = new ArrayList<>(
                Arrays.asList(R.drawable.iyaf1,R.drawable.iyaf2,R.drawable.iyaf3,R.drawable.iyaf4,R.drawable.iyaf5)
        );
        int randomIndex = (int) (Math.random() * images.size());

        Log.d("LOZ", "Best image : "+ images.get(randomIndex));

        ImageView backgroundImage = findViewById(R.id.backgroundImage);

        Picasso
                .with(this)
                .load(images.get(randomIndex))
                .resize(displayMetrics.widthPixels, displayMetrics.heightPixels)
                .onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                .into(backgroundImage);
    }

    private void setTopMarginOfMenu(GridView gridView) {
        DisplayMetrics displayMetrics = getDisplayMetrics();
        RelativeLayout.LayoutParams gridLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        double topMargin = Math.abs(displayMetrics.heightPixels*TOP_MARGIN_PROPORTION_FOR_MENU);
        gridLayout.setMargins(0,(int)topMargin, 0,0);
        gridView.setLayoutParams(gridLayout);
    }

}
