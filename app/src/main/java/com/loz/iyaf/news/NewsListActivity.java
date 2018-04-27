package com.loz.iyaf.news;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loz.iyaf.feed.NewsData;
import com.loz.R;
import com.loz.iyaf.feed.EventappService;
import com.loz.iyaf.feed.NewsList;
import com.loz.iyaf.imagehelpers.JsonCache;

import java.io.ObjectInput;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class NewsListActivity extends AppCompatActivity  {

    private ArrayList<NewsData> newsItems = new ArrayList<>();
    private EventappService eventappService;
    private NewsListAdapter adapter;
    private ListView listView;
    private int current_page = 0;
    private Button btnLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnLoadMore = new Button(getApplicationContext());
        btnLoadMore.setText("Load More");

        setContentView(R.layout.activity_newslist);

        listView = findViewById(R.id.listView);
        TextView emptyList = findViewById(R.id.emptyList);
        listView.setEmptyView(emptyList);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventapp.lozarcher.co.uk")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        eventappService = retrofit.create(EventappService.class);

        spinner(true);
        Call<NewsList> call = eventappService.getNews();
        Log.d("LOZ", "Starting call to /posts... Hope it works...");
        call.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Response<NewsList> response, Retrofit retrofit) {
                Log.d("LOZ", "Got response: " + response.body().toString());

                NewsList newsList = response.body();
                JsonCache.writeToCache(getApplicationContext(), newsList, "news");
                spinner(false);
                processNewsList(newsList);
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                Crashlytics.logException(t);
                spinner(false);
                NewsList newsList = null;
                ObjectInput oi = JsonCache.readFromCache(getApplicationContext(), "news");
                if (oi != null) {
                    try {
                        newsList = (NewsList) oi.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("cache", e.getMessage());
                        Crashlytics.logException(e);
                    }
                    if (newsList != null) {
                        newsList.setNext(null); // just show one page if there's no internet connection
                        processNewsList(newsList);
                    }
                }

            }
        });
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Post List View")
                .putContentType("Post List")
                .putContentId("postlist"));
    }

    @UiThread
    void spinner(boolean show){
        findViewById(R.id.listView).setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.emptyList).setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.spinner).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void processNewsList(NewsList newsList) {
        for (NewsData newsData : newsList.getData()) {
            newsItems.add(newsData);
        }

        listView = (ListView) findViewById(R.id.listView);

        if (newsList.getNext() != null) {
            listView.addFooterView(btnLoadMore);
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // Starting a new async task
                    new loadMoreListView().execute();
                }
            });
        }

        adapter = new NewsListAdapter(NewsListActivity.this, newsItems);
        listView.setAdapter(adapter);
    }

    private class loadMoreListView extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    NewsListActivity.this);
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

                    Call<NewsList> call = eventappService.getNews(current_page);
                    Log.d("LOZ", "Starting call to /posts/"+current_page+"... Hope it works...");
                    call.enqueue(new Callback<NewsList>() {
                        @Override
                        public void onResponse(Response<NewsList> response, Retrofit retrofit) {
                            Log.d("LOZ", "Got response: " + response.body().toString());

                            NewsList newsList = response.body();
                            for (NewsData newsData : newsList.getData()) {
                                newsItems.add(newsData);
                            }

                            if (newsList.getNext() == null) {
                                listView.removeFooterView(btnLoadMore);
                            }
                            int currentPosition = listView.getFirstVisiblePosition();

                            // Appending new data to menuItems ArrayList
                            adapter = new NewsListAdapter(
                                    NewsListActivity.this,
                                    newsItems);
                            listView.setAdapter(adapter);

                            listView.setSelectionFromTop(currentPosition + 1, 0);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            // something went completely south (like no internet connection)
                            Log.d("Error", t.getMessage());
                            Crashlytics.logException(t);
                        }

                    });
                }
            });
            return (null);
        }

    }

}
