package com.loz.iyaf.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    private static final double TOP_MARGIN_PROPORTION_FOR_MENU = 0.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        initImageLoader(getApplicationContext());

        RelativeLayout layout = findViewById(R.id.layout);

        layout.setBackgroundResource(backgroundImageForScreenSize());

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

    private int backgroundImageForScreenSize() {
        DisplayMetrics displayMetrics = getDisplayMetrics();

        HashMap<Float, Integer> backgroundImages = new HashMap<>();
        backgroundImages.put(0.444f, R.drawable.app_background_0444);
        backgroundImages.put(0.562f, R.drawable.app_background_0562);
        backgroundImages.put(0.766f, R.drawable.app_background_0766);


        float screenAspect = ((float)displayMetrics.widthPixels / (float)displayMetrics.heightPixels);
        Log.d("LOZ","Screen size is "+displayMetrics.widthPixels+" "+displayMetrics.heightPixels);
        Log.d("LOZ","Aspect is "+screenAspect);

        float bestDistance = 999.0f;
        Integer bestImageFile = 0;
        for (Float imageAspect :backgroundImages.keySet()) {
            Log.d("LOZ","Comparing aspect with  "+imageAspect);

            float thisDistance = Math.abs(screenAspect - imageAspect);
            if (thisDistance < bestDistance) {
                bestDistance = thisDistance;
                bestImageFile = backgroundImages.get(imageAspect);
                Log.d("LOZ","Best aspect so far is "+imageAspect);
                Log.d("LOZ","This is image "+bestImageFile);
            }
        }

        Log.d("LOZ", "Best image : "+ bestImageFile);

        return bestImageFile;
    }

    private void setTopMarginOfMenu(GridView gridView) {
        DisplayMetrics displayMetrics = getDisplayMetrics();
        RelativeLayout.LayoutParams gridLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        double topMargin = Math.abs(displayMetrics.heightPixels*TOP_MARGIN_PROPORTION_FOR_MENU);
        gridLayout.setMargins(0,(int)topMargin, 0,0);
        gridView.setLayoutParams(gridLayout);
    }

}
