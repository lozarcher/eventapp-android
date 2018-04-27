package com.loz.iyaf.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loz.surbitonfood.events.EventListActivity;
import com.loz.iyaf.gallery.GalleryActivity;
import com.loz.iyaf.info.InfoListActivity;
import com.loz.iyaf.news.NewsListActivity;
import com.loz.iyaf.traders.TraderListActivity;
import com.loz.iyaf.twitter.TwitterListActivity;
import com.loz.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d("LOZ", "Density: "+getResources().getDisplayMetrics().density);

        setContentView(R.layout.activity_menu);

        initImageLoader(getApplicationContext());

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new MenuAdapter(this));

;        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MenuActivity.this, EventListActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MenuActivity.this, GalleryActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MenuActivity.this, NewsListActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MenuActivity.this, TraderListActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MenuActivity.this, InfoListActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MenuActivity.this, TwitterListActivity.class);
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

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
