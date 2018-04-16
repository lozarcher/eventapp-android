package com.loz.iyaf.twitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.loz.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

public class TwitterListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twitterlist);

        String hashtag = getString(R.string.twitter_hashtag);
        if (hashtag == null) {
            hashtag = "#twitter";
        }
        setTitle(hashtag+" Tweets");
        String searchTerm = getString(R.string.twitter_searchterm);
        //String searchTerm = "asdasdsadsadasdsad";

        final SearchTimeline searchTimeLine = new SearchTimeline.Builder()
                .query(searchTerm)
                .maxItemsPerRequest(20)
                .resultType(SearchTimeline.ResultType.RECENT)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeLine)
                .build();


        ListView listView = findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        TextView emptyView = findViewById(android.R.id.empty);
        listView.setEmptyView(emptyView);
    }


}
