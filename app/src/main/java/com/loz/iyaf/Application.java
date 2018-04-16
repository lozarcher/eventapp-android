package com.loz.iyaf;

import com.twitter.sdk.android.core.Twitter;

/**
 * Created by loz on 27/03/2018.
 */


public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(() -> Twitter.initialize(this)).start();
    }
}