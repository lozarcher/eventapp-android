package com.loz.iyaf.info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.loz.iyaf.feed.InfoData;
import com.loz.iyaf.imagehelpers.Utils;
import com.loz.iyaf.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Bundle bundle = this.getIntent().getExtras();
        final InfoData info = (InfoData) bundle.get("info");

        setTitle(info.getTitle());

        ImageView infoImage = (ImageView) findViewById(R.id.infoImage);
        if (info.getPicture() != null) {
            Utils.loadImage(info.getPicture(), infoImage, null);
        }

        TextView infoTitle = (TextView) findViewById(R.id.infoTitle);
        infoTitle.setText(info.getTitle());
        TextView infoContent = (TextView) findViewById(R.id.infoContent);
        infoContent.setText(info.getContent());
    }

}
