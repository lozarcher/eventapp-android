package com.loz.iyaf.twitter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.loz.iyaf.feed.TwitterList;
import com.loz.iyaf.imagehelpers.JsonCache;
import com.loz.iyaf.R;
import com.loz.iyaf.feed.EventappService;
import com.loz.iyaf.feed.TwitterData;

import java.io.ObjectInput;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class TwitterListActivity extends AppCompatActivity {

    private ArrayList<TwitterData> tweets = new ArrayList<>();
    private EventappService eventappService;
    private TwitterListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitterlist);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventapp.lozarcher.co.uk")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        eventappService = retrofit.create(EventappService.class);

        Call<TwitterList> call = eventappService.getTweets();
        Log.d("LOZ", "Starting call to /tweets... Hope it works...");
        call.enqueue(new Callback<TwitterList>() {
            @Override
            public void onResponse(Response<TwitterList> response, Retrofit retrofit) {
                Log.d("LOZ", "Got response: " + response.body().toString());

                TwitterList tweetList = response.body();
                JsonCache.writeToCache(getApplicationContext(), tweetList, "twitter");
                processTwitterList(tweetList);
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                TwitterList twitterList = null;
                ObjectInput oi = JsonCache.readFromCache(getApplicationContext(), "twitter");
                if (oi != null) {
                    try {
                        twitterList = (TwitterList) oi.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("cache", e.getMessage());
                    }
                    if (twitterList != null) {
                        twitterList.setNext(null); // just show one page if there's no internet connection
                        processTwitterList(twitterList);
                    }
                }
            }
        });
    }

    private void processTwitterList(TwitterList twitterList) {
        for (TwitterData tweet : twitterList.getData()) {
            tweets.add(tweet);
        }

        listView = (ListView) findViewById(R.id.listView);
        if (twitterList.getNext() != null) {
            Button btnLoadMore = new Button(getApplicationContext());
            btnLoadMore.setText("Load More");
            listView.addFooterView(btnLoadMore);
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // Starting a new async task
                    new loadMoreListView().execute();
                }
            });
        }

        adapter = new TwitterListAdapter(TwitterListActivity.this, tweets);
        listView.setAdapter(adapter);

    }

    private class loadMoreListView extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        int current_page = 0;

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    TwitterListActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected void onPostExecute(Void unused) {
            // closing progress dialog
            pDialog.dismiss();
        }

        protected Void doInBackground(Void... unused) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // increment current page
                    current_page += 1;

                    Call<TwitterList> call = eventappService.getTweets(current_page);
                    Log.d("LOZ", "Starting call to /tweets... Hope it works...");
                    call.enqueue(new Callback<TwitterList>() {
                        @Override
                        public void onResponse(Response<TwitterList> response, Retrofit retrofit) {
                            Log.d("LOZ", "Got response: " + response.body().toString());

                            TwitterList twitterList = response.body();
                            for (TwitterData tweet : twitterList.getData()) {
                                tweets.add(tweet);
                            }

                            int currentPosition = listView.getFirstVisiblePosition();

                            // Appending new data to menuItems ArrayList
                            adapter = new TwitterListAdapter(
                                    TwitterListActivity.this,
                                    tweets);
                            listView.setAdapter(adapter);

                            listView.setSelectionFromTop(currentPosition + 1, 0);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            // something went completely south (like no internet connection)
                            Log.d("Error", t.getMessage());
                        }

                    });
                }
            });
            return (null);
        }

    }

}
