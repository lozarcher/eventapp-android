package com.loz.iyaf.traders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loz.iyaf.feed.TraderList;
import com.loz.iyaf.imagehelpers.JsonCache;
import com.loz.iyaf.feed.EventappService;
import com.loz.iyaf.R;
import com.loz.iyaf.feed.TraderData;

import java.io.ObjectInput;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class TraderListActivity extends AppCompatActivity  {

    private ArrayList<TraderData> traders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traderlist);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventapp.lozarcher.co.uk")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        EventappService eventappService = retrofit.create(EventappService.class);
        Call<TraderList> call = eventappService.getTraders();
        Log.d("LOZ", "Starting call to /traders... Hope it works...");
        call.enqueue(new Callback<TraderList>() {
            @Override
            public void onResponse(Response<TraderList> response, Retrofit retrofit) {
                Log.d("LOZ", "Got response: " + response.body().toString());

                TraderList traderList = response.body();
                JsonCache.writeToCache(getApplicationContext(), traderList, "traders");
                processTraderList(traderList);
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                TraderList traderList = null;
                ObjectInput oi = JsonCache.readFromCache(getApplicationContext(), "traders");
                if (oi != null) {
                    try {
                        traderList = (TraderList) oi.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("cache", e.getMessage());
                    }
                    if (traderList != null) {
                        processTraderList(traderList);
                    }
                }
            }
        });
    }

    private void processTraderList(TraderList traderList) {
        ArrayList<String> traderNames = new ArrayList<>();
        for (TraderData trader : traderList.getData()) {
            traders.add(trader);
            traderNames.add(trader.getName());
        }
        ListView listView = (ListView) findViewById(R.id.listView);

        TraderListAdapter adapter = new TraderListAdapter(TraderListActivity.this, traders);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mEventClickedHandler);
    }

    private AdapterView.OnItemClickListener mEventClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Intent intent = new Intent(TraderListActivity.this, TraderActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("trader", traders.get(position));
            intent.putExtras(b);
            startActivity(intent);

        }
    };


}
