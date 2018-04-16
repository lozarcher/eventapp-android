package com.loz.iyaf;


import com.loz.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by loz on 27/03/2018.
 */


public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        Fabric.with(this, new Twitter(authConfig));
        new Thread(() -> Twitter.getInstance()).start();
    }
}