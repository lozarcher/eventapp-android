package com.loz.iyaf.events;

import android.app.Activity;
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
import com.loz.iyaf.imagehelpers.Utils;

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
                ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image

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
                String startTimeStr = String.format("%02d:%02d", startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE));

                subtitle.setText(startTimeStr + " @ " + event.getLocation());
                Utils.loadImage(event.getProfileUrl(), thumb_image, null);
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
}
