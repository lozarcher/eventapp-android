package com.loz.iyaf;

import com.loz.BuildConfig;
import com.loz.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by loz on 27/03/2018.
 */


public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();

        Fabric.with(this, crashlytics, new Twitter(authConfig));
        new Thread(() -> Twitter.getInstance()).start();
    }
}