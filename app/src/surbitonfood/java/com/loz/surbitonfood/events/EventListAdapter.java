package com.loz.surbitonfood.events;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loz.R;
import com.loz.iyaf.feed.EventData;
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

public class EventListAdapter extends BaseAdapter {

    private EventListActivity activity;
    private ArrayList<EventData> data = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();
    private static LayoutInflater inflater=null;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;


    public EventListAdapter (EventListActivity a) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final EventData item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        sectionHeader.add(data.size());
        EventData sectionEventPlaceholder = new EventData();
        sectionEventPlaceholder.setName(item);
        data.add(sectionEventPlaceholder);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public EventData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        int rowType = getItemViewType(position);
        switch (rowType) {
            case TYPE_ITEM:
                if (convertView == null)
                    vi = inflater.inflate(R.layout.list_row_event, null);
                TextView title = (TextView) vi.findViewById(R.id.title); // title
                TextView subtitle = (TextView) vi.findViewById(R.id.artist); // subtitle

                ImageView favouriteIcon = vi.findViewById(R.id.favouriteIcon); // thumb image
                EventData event = data.get(position);
                activity.setFavouriteIcon(favouriteIcon, event);

                favouriteIcon.setClickable(true);

                favouriteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("LOZ", "Clicked favourite");
                        activity.setFavourite((ImageView)view, event);
                    }
                });


                // Setting all values in listview
                title.setText(event.getName());
                Calendar startTime = GregorianCalendar.getInstance();
                startTime.setTime(event.getStartTime());

                CustomAnalogClock clock = vi.findViewById(R.id.analog_clock);

                Integer plate = plates[position%6];
                clock.init(activity, plate, R.drawable.white_hour_hand, R.drawable.white_minute_hand, 255, false, false);

                clock.setAutoUpdate(false);
                clock.setTime(startTime);
                clock.setScale(0.25f);

                TextView eventTime = vi.findViewById(R.id.eventTime);
                eventTime.setText(String.format("%02d:%02d", startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE)));
                subtitle.setText(event.getLocation());
                break;
            case TYPE_SEPARATOR:
                if (convertView == null)
                    vi = inflater.inflate(R.layout.list_section, null);
                TextView textSeparator = (TextView) vi.findViewById(R.id.textSeparator); // title
                EventData sectionHeading = data.get(position);
                textSeparator.setText(sectionHeading.getName());
                break;
        }


        return vi;
    }

    private Integer[] plates = {
            R.drawable.magenta_plate, R.drawable.orange_plate,
            R.drawable.blue_plate, R.drawable.red_plate,
            R.drawable.green_plate, R.drawable.indigo_plate
    };
}

