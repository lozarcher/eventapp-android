package com.loz.iyaf.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loz.iyaf.feed.EventData;
import com.loz.iyaf.feed.EventList;
import com.loz.iyaf.imagehelpers.JsonCache;
import com.loz.iyaf.feed.EventappService;
import com.loz.iyaf.R;

import java.io.ObjectInput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class EventListActivity extends AppCompatActivity  {

    private ArrayList<EventData> eventRows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlist);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eventapp.lozarcher.co.uk")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        EventappService eventappService = retrofit.create(EventappService.class);
        Call<EventList> call = eventappService.getEvents();
        Log.d("LOZ", "Starting call to /events... Hope it works...");
        call.enqueue(new Callback<EventList>() {
            @Override
            public void onResponse(Response<EventList> response, Retrofit retrofit) {
                Log.d("LOZ", "Got response: "+response.body().toString());

                EventList eventList = response.body();
                JsonCache.writeToCache(getApplicationContext(), eventList, "events");
                processEventList(eventList);
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                EventList eventList = null;
                ObjectInput oi = JsonCache.readFromCache(getApplicationContext(), "events");
                if (oi != null) {
                    try {
                        eventList = (EventList) oi.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("cache", e.getMessage());
                    }
                    if (eventList != null) {
                        processEventList(eventList);
                    }
                }
            }
        });
   }

    private void processEventList(EventList eventList) {
        ArrayList<String> eventNames = new ArrayList<>();
        ArrayList<EventData> events = new ArrayList<>();
        for (EventData event : eventList.getData()) {
            events.add(event);
            eventNames.add(event.getName());
        }
        ListView listView = (ListView)findViewById(R.id.listView);

        EventListAdapter adapter = new EventListAdapter(EventListActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mEventClickedHandler);

        TreeMap<Date, ArrayList<EventData>> map = getEventsByDay(events);
        for (Date date : map.keySet()) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMMM yyyy");
            eventRows.add(new EventData());
            adapter.addSectionHeaderItem(sdf.format(date));
            for (EventData event : map.get(date)) {
                adapter.addItem(event);
                eventRows.add(event);
            }
        }
    }

    private TreeMap<Date, ArrayList<EventData>> getEventsByDay(ArrayList<EventData> events) {
        TreeMap<Date, ArrayList<EventData>> map = new TreeMap<>();
        for (EventData event : events) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(event.getStartTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date dateAtMidnight = cal.getTime();

            ArrayList<EventData> eventsOnDay = map.get(dateAtMidnight);
            if (eventsOnDay == null) {
                eventsOnDay = new ArrayList<EventData>();
            }
            eventsOnDay.add(event);
            map.put(dateAtMidnight, eventsOnDay);
        }
        for (Date date : map.keySet()) {
            Log.d("Events for day", date.toString());
            for (EventData event : map.get(date)) {
                Log.d("Event", event.getName()+ " "+ event.getStartTime().toString());
            }
        }
        return map;
    }

    private AdapterView.OnItemClickListener mEventClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Intent intent = new Intent(EventListActivity.this, EventActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("event", eventRows.get(position));
            intent.putExtras(b);
            startActivity(intent);
        }
    };
}
