package com.loz.iyaf.twitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loz.R;
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

        new Thread(() -> {
            final SearchTimeline searchTimeLine = new SearchTimeline.Builder()
                    .query(searchTerm)
                    .maxItemsPerRequest(20)
                    .resultType(SearchTimeline.ResultType.RECENT)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(searchTimeLine)
                    .build();
            runOnUiThread(() -> {
                showUi(adapter);
            });
        }).start();
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Twitter List")
                .putContentType("Twitter List")
                .putContentId("twitterlist"));
    }

    private void showUi(TweetTimelineListAdapter adapter) {
        ListView listView = findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        TextView emptyView = findViewById(android.R.id.empty);
        listView.setEmptyView(emptyView);
    }
}