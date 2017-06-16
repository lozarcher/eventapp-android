package com.loz.iyaf.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loz.iyaf.feed.EventappService;
import com.loz.iyaf.R;
import com.loz.iyaf.feed.InfoData;
import com.loz.iyaf.feed.InfoList;
import com.loz.iyaf.imagehelpers.JsonCache;

import java.io.ObjectInput;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class InfoListActivity extends AppCompatActivity  {

    private ArrayList<InfoData> infoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traderlist);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventapp.lozarcher.co.uk")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        EventappService eventappService = retrofit.create(EventappService.class);
        Call<InfoList> call = eventappService.getInfo();
        Log.d("LOZ", "Starting call to /info... Hope it works...");
        call.enqueue(new Callback<InfoList>() {
            @Override
            public void onResponse(Response<InfoList> response, Retrofit retrofit) {
                Log.d("LOZ", "Got response: " + response.body().toString());

                InfoList infoList = response.body();
                JsonCache.writeToCache(getApplicationContext(), infoList, "info");
                processInfoList(infoList);
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                InfoList infoList = null;
                ObjectInput oi = JsonCache.readFromCache(getApplicationContext(), "info");
                if (oi != null) {
                    try {
                        infoList = (InfoList) oi.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("cache", e.getMessage());
                    }
                    if (infoList != null) {
                        processInfoList(infoList);
                    }
                }
            }
        });
    }

    private void processInfoList(InfoList infoList) {
        ArrayList<String> infoTitles = new ArrayList<>();
        for (InfoData info : infoList.getData()) {
            infoItems.add(info);
            infoTitles.add(info.getTitle());
        }
        ListView listView = (ListView) findViewById(R.id.listView);

        InfoListAdapter adapter = new InfoListAdapter(InfoListActivity.this, infoItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mEventClickedHandler);
    }

    private AdapterView.OnItemClickListener mEventClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Intent intent = new Intent(InfoListActivity.this, InfoActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("info", infoItems.get(position));
            intent.putExtras(b);
            startActivity(intent);

        }
    };


}
