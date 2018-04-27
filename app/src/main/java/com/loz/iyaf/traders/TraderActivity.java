package com.loz.iyaf.traders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loz.iyaf.imagehelpers.Utils;
import com.loz.R;
import com.loz.iyaf.feed.TraderData;

public class TraderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader);
        Bundle bundle = this.getIntent().getExtras();
        final TraderData trader = (TraderData) bundle.get("trader");

        setTitle(trader.getName());

        ImageView traderImage = (ImageView) findViewById(R.id.traderImage);
        if (trader.getCoverImg() != null) {
            Utils.loadImage(trader.getCoverImg(), traderImage, null);
        }

        ImageView kpoundImage = (ImageView) findViewById(R.id.kpoundImage);
        if (trader.isKingstonPound()) {
            kpoundImage.setVisibility(View.VISIBLE);
        } else {
            kpoundImage.setVisibility(View.INVISIBLE);
        }

        TextView traderName = (TextView) findViewById(R.id.traderName);
        traderName.setText(trader.getName());
        TextView traderAbout = (TextView) findViewById(R.id.traderAbout);
        traderAbout.setText(trader.getAbout());

        TextView traderWebsite = (TextView) findViewById(R.id.websiteLink);
        if (trader.getWebsite() != null) {
            traderWebsite.setText("Website");
            traderWebsite.setVisibility(View.VISIBLE);
            traderWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trader.getWebsite()));
                    startActivity(browserIntent);
                }
            });
        } else {
            traderWebsite.setVisibility(View.INVISIBLE);
        }

        TextView traderFacebook = (TextView) findViewById(R.id.facebookLink);
        if (trader.getLink() != null) {
            traderFacebook.setText("Facebook Page");
            traderFacebook.setVisibility(View.VISIBLE);
            traderFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trader.getLink()));
                    startActivity(browserIntent);
                }
            });
        } else {
            traderFacebook.setVisibility(View.INVISIBLE);
        }

        TextView traderCall = (TextView) findViewById(R.id.callButton);
        if (trader.getPhone() != null && Utils.isNumeric(trader.getPhone())) {
            traderCall.setText("Call " + trader.getName());
            traderCall.setVisibility(View.VISIBLE);
            traderCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_VIEW);
                    callIntent.setData(Uri.parse("tel:" + trader.getPhone()));
                    startActivity(callIntent);
                }
            });
        } else {
            traderCall.setVisibility(View.INVISIBLE);
        }
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(trader.getName())
                .putContentType("Trader List")
                .putContentId(String.valueOf(trader.getId())));
    }
}
