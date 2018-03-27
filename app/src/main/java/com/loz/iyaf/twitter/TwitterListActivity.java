package com.loz.iyaf.twitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loz.R;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

public class TwitterListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);

        setContentView(R.layout.activity_twitterlist);

        String hashtag = getString(R.string.twitter_hashtag);
        if (hashtag == null) {
            hashtag = "#twitter";
        }
        setTitle(hashtag+" Tweets");
        String searchTerm = getString(R.string.twitter_searchterm);
        final SearchTimeline searchTimeLine = new SearchTimeline.Builder()
                .query(searchTerm)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeLine)
                .build();

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }

}
